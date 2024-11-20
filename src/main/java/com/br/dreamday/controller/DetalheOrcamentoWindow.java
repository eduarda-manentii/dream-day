package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.Mensagens;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class DetalheOrcamentoWindow {

    @FXML
    private Label lblClientePreencher;

    @FXML
    private Label lblCustoEstimadoPreencher;

    @FXML
    private Label lblDataDeCriacaoPreencher;

    @FXML
    private Label lblDetalhesDoOrcamentoPreencher;

    @FXML
    private Label lblObservacoesPreencher;

    @FXML
    private Label lblStatusPreencher;

    @FXML
    private Label lblValorTotalPreencher;

    @FXML
    private TableView<ItemOrcamento> tableItensOrcamentos;

    @FXML
    private TableColumn<ItemOrcamento, String> codigoColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> precoColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> fornecedorColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> produtoColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> quantidadeColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> valorTotalColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> acoesColumn;

    private ObservableList<ItemOrcamento> itemOrcamentoList;
    private final ItemOrcamentoService service;
    private final OrcamentoService orcamentoService;
    private Orcamento orcamento;
    private Long orcamentoId;

    public DetalheOrcamentoWindow() {
        this.orcamentoId = 0L;
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
    }

    private void setupTableColumns() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoProduto"));
        fornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedor"));
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalProduto"));
        acoesColumn.setCellFactory(column -> criarAcaoDosBotoes());
    }

    private TableCell<ItemOrcamento, String> criarAcaoDosBotoes() {
        return new TableCell<>() {
            final Button editarButton = new Button("Editar");
            final Button excluirButton = new Button("Excluir");
            final HBox buttonBox = new HBox(editarButton, excluirButton);

            {
                buttonBox.setSpacing(10);
                excluirButton.setOnAction(event -> Mensagens.exibirMensagemDeConfirmacao("Tem certeza que deseja remover o item selecionado?", () -> {
                    int index = getIndex();
                    ItemOrcamento itemOrcamento = getTableView().getItems().get(index);
                    service.excluirPor(itemOrcamento.getId());
                    itemOrcamentoList.remove(itemOrcamento);
                    recarregarValorTotal(itemOrcamento);
                }));
                editarButton.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/vincular-item-window.fxml"));
                        Parent root = loader.load();
                        VincularItemWindow vincularItemWindow = loader.getController();
                        vincularItemWindow.setParentController(DetalheOrcamentoWindow.this);
                        vincularItemWindow.setOrcamentoId(orcamentoId);
                        int index = getIndex();
                        ItemOrcamento itemOrcamento = getTableView().getItems().get(index);
                        vincularItemWindow.setAttributes(itemOrcamento);
                        Stage popup = new Stage();
                        popup.setScene(new Scene(root));
                        popup.initModality(Modality.APPLICATION_MODAL);
                        popup.showAndWait();
                        recarregarTabela();
                    } catch (IOException e) {
                        Mensagens.exibirMensagemDeErro("Erro ao abrir a janela de edição.");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        };
    }

    @FXML
    public void editar(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/cadastro-orcamento-window.fxml"));
        Parent root = loader.load();
        CadastroOrcamentoWindow orcamentoController = loader.getController();
        orcamentoController.setAttributes(orcamentoService.buscarPor(orcamentoId));
        openModalWindow(root);
        recarregarTabela();
    }

    @FXML
    public void vincularItem(ActionEvent actionEvent) throws IOException {
        if (orcamentoId == 0) {
            Mensagens.exibirMensagemInformativa("Salve o orçamento antes de vincular um item.");
        } else {
            abrirTelaVincularItem();
        }
    }

    @FXML
    public void excluir(ActionEvent event) {
        Mensagens.exibirMensagemDeConfirmacao("Tem certeza que deseja excluir este orçamento e todos os seus itens?", () -> {
            orcamentoService.excluirOrcamentoEItensVinculados(orcamentoId);
            Mensagens.exibirMensagemInformativa("Orçamento e todos os itens foram excluídos com sucesso.");
            closeWindow(event);
        });
    }

    public void atualizarCampoValorTotal(String novoValor) {
        lblValorTotalPreencher.setText(novoValor);
    }

    public void setAttributes(Orcamento orcamentoSelecionado) {
        this.orcamento = orcamentoSelecionado;
        this.orcamentoId = orcamentoSelecionado.getId();
        popularCampos(orcamentoSelecionado);
        itemOrcamentoList = FXCollections.observableArrayList(service.listarPor(orcamento.getId()));
        setupTableColumns();
        tableItensOrcamentos.setItems(itemOrcamentoList);
    }

    private void openModalWindow(Parent root) {
        Stage popup = new Stage();
        popup.setScene(new Scene(root));
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void abrirTelaVincularItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("vincular-item-window.fxml")));
        Parent parent = loader.load();
        VincularItemWindow controller = loader.getController();
        controller.setOrcamentoId(orcamentoId);
        controller.setParentController(this);
        openModalWindow(parent);
        recarregarTabela();
    }

    private void recarregarValorTotal(ItemOrcamento itemOrcamento) {
        BigDecimal subtotal = calculateSubtotal(itemOrcamento);
        Orcamento orcamentoAtualizado = orcamentoService.buscarPor(orcamentoId);
        BigDecimal totalAtualizado = orcamentoAtualizado.getValorTotal().subtract(subtotal);
        orcamentoService.atualizarValorTotal(orcamentoId, totalAtualizado);
        lblValorTotalPreencher.setText(totalAtualizado.toString());
        recarregarTabela();
    }

    private BigDecimal calculateSubtotal(ItemOrcamento itemOrcamento) {
        BigDecimal precoProduto = new BigDecimal(itemOrcamento.getPrecoProduto());
        BigDecimal quantidade = BigDecimal.valueOf(itemOrcamento.getQuantidade());
        return quantidade.multiply(precoProduto);
    }

    private void recarregarTabela() {
        itemOrcamentoList.setAll(service.listarPor(orcamentoId));
    }

    private void popularCampos(Orcamento orcamentoSelecionado) {
        lblDetalhesDoOrcamentoPreencher.setText(orcamentoSelecionado.getId().toString());
        lblClientePreencher.setText(orcamentoSelecionado.getCliente().getNome());
        lblCustoEstimadoPreencher.setText(orcamentoSelecionado.getCustoEstimado().toString());
        lblDataDeCriacaoPreencher.setText(orcamentoSelecionado.getDataCriacao().toString());
        lblValorTotalPreencher.setText(orcamentoSelecionado.getValorTotal().toString());
        lblStatusPreencher.setText(orcamentoSelecionado.getStatus().toString());
        lblObservacoesPreencher.setText(orcamentoSelecionado.getObservaces());
    }

}

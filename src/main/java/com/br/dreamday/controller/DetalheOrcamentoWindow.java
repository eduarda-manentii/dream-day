package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
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
    private ItemFornecedorService itemFornecedorService;

    public DetalheOrcamentoWindow() {
        this.orcamentoId = Long.valueOf(0);
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    public void setAttributes(Orcamento orcamentoSelecionado)  {
        this.orcamento = orcamentoSelecionado;
        this.orcamentoId = orcamentoSelecionado.getId();
        populaCampos(orcamentoSelecionado);
        itemOrcamentoList = FXCollections.observableArrayList(service.listarPor(orcamento.getId()));;

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("precoProduto"));
        fornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("nomeFornecedor"));
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalProduto"));
        acoesColumn.setCellFactory(column -> new TableCell<>() {
            final Button editarButton = new Button("Editar");
            final Button excluirButton = new Button("Excluir");
            final HBox buttonBox = new HBox(editarButton, excluirButton);

            {
                buttonBox.setSpacing(10);
                excluirButton.setOnAction(event -> {
                    confirmationMessage("Tem certeza que deseja remover o item selecionado?", () -> {
                        int index = getIndex();
                        ItemOrcamento itemOrcamento = getTableView().getItems().get(index);
                        service.excluirPor(itemOrcamento.getId());
                        itemOrcamentoList.removeIf(item -> item.getId().equals(itemOrcamento.getId()));
                        tableItensOrcamentos.setItems(itemOrcamentoList);
                        tableItensOrcamentos.refresh();
                        recarregarValorTotal(itemOrcamento);
                    });
                });

                editarButton.setOnAction(event -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/vincular-item-window.fxml"));
                    Parent root;
                    try {
                        root = loader.load();
                        VincularItemWindow vincularItemWindow = loader.getController();
                        int index = getIndex();
                        ItemOrcamento itemOrcamento = getTableView().getItems().get(index);
                        vincularItemWindow.setAttributes(itemOrcamento);
                        Scene scene = new Scene(root);
                        Stage popup = new Stage();
                        popup.setScene(scene);
                        popup.initModality(Modality.APPLICATION_MODAL);
                        popup.showAndWait();
                        recarregarTabela();
                        tableItensOrcamentos.setItems(itemOrcamentoList);
                        tableItensOrcamentos.refresh();
                        recarregarValorTotal(itemOrcamento);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(buttonBox);
                } else {
                    setGraphic(null);
                }
            }
        });
        tableItensOrcamentos.setItems(itemOrcamentoList);
    }

    private void recarregarValorTotal(ItemOrcamento itemOrcamento) {
        BigDecimal precoProduto = new BigDecimal(itemOrcamento.getPrecoProduto());
        BigDecimal quantidade = BigDecimal.valueOf(itemOrcamento.getQuantidade());
        BigDecimal subtotal = quantidade.multiply(precoProduto);
        Orcamento orcamentoAtualizado = orcamentoService.buscarPor(orcamentoId);
        BigDecimal valorTotalAtual = orcamentoAtualizado.getValorTotal();
        BigDecimal totalAtualizado = valorTotalAtual.subtract(subtotal);
        orcamentoService.atualizarValorTotal(orcamentoId,  totalAtualizado);
        lblValorTotalPreencher.setText(totalAtualizado.toString());
        recarregarTabela();
    }


    private void populaCampos(Orcamento orcamentoSelecionado) {
        lblDetalhesDoOrcamentoPreencher.setText(orcamentoSelecionado.getId().toString());
        lblClientePreencher.setText(orcamentoSelecionado.getCliente().getNome());
        lblCustoEstimadoPreencher.setText(orcamentoSelecionado.getCustoEstimado().toString());
        lblDataDeCriacaoPreencher.setText(orcamentoSelecionado.getDataCriacao().toString());
        lblValorTotalPreencher.setText(orcamentoSelecionado.getValorTotal().toString());
        lblStatusPreencher.setText(orcamentoSelecionado.getStatus().toString());
        lblObservacoesPreencher.setText(orcamentoSelecionado.getObservaces());
    }

    @FXML
    public void onButtonEditarClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/cadastro-orcamento-window.fxml"));
        Parent root = loader.load();
        CadastroOrcamentoWindow orcamentoController = loader.getController();
        orcamentoController.setAttributes(orcamentoService.buscarPor(orcamentoId));
        Scene scene = new Scene(root);
        Stage popup = new Stage();
        popup.setScene(scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        recarregarTabela();
    }

    @FXML
    public void onButtonVincularItemClicked(ActionEvent actionEvent) throws IOException {
        if (orcamentoId == 0) {
            showMessage("Salve o orçamento antes de vincular um item.");
            return;
        }
        abrirTelaVincularItem();
    }

    @FXML
    public void onButtonExcluirClicked(ActionEvent event) {
        confirmationMessage("Tem certeza que deseja excluir este orçamento e todos os seus itens?", () -> {
            orcamentoService.excluirOrcamentoEItensVinculados(orcamentoId);
            showMessage("Orçamento e todos os itens foram excluídos com sucesso.");
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    private void recarregarTabela() {
        itemOrcamentoList.clear();
        itemOrcamentoList.addAll(service.listarPor(orcamentoId));
    }

    void abrirTelaVincularItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("vincular-item-window.fxml")));
        Parent parent = loader.load();
        VincularItemWindow controller = loader.getController();
        controller.setOrcamentoId(orcamentoId);
        controller.setParentController(this);
        Stage popupStage = new Stage();
        popupStage.setTitle("Vincular Item");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
        recarregarTabela();
    }

    public void atualizarCampoValorTotal(String novoValor) {
        lblValorTotalPreencher.setText(novoValor);
    }

    private void confirmationMessage(String mensagem, Runnable acao) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnYes = new ButtonType("Sim");
        ButtonType btnNo = new ButtonType("Não");
        dialog.setContentText(mensagem);
        dialog.getButtonTypes().setAll(btnYes, btnNo);
        dialog.showAndWait().ifPresent(b -> {
            if (b == btnYes) {
                acao.run();
            }
        });
    }

    private void showMessage(String mensagem) {
        ButtonType loginButtonType = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Aviso");
        dialog.setContentText(mensagem);
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        boolean desativado = false;
        dialog.getDialogPane().lookupButton(loginButtonType).setDisable(desativado);
        dialog.showAndWait();
    }

}

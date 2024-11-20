package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.*;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.WindowUtils;
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

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class DetalheOrcamentoWindowController {

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
    private Button btnAdicionarParcelas;

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

    @FXML
    private Button btnVerParcelas;

    private ObservableList<ItemOrcamento> itemOrcamentoList;
    private final ItemOrcamentoService service;
    private final OrcamentoService orcamentoService;
    private final ParcelamentoService parcelamentoService;
    private Orcamento orcamento;
    private Long orcamentoId;

    public DetalheOrcamentoWindowController() {
        this.orcamentoId = Long.valueOf(0);
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
        parcelamentoService = new ParcelamentoService();
    }

    public void setAttributes(Orcamento orcamentoSelecionado) {
        this.orcamento = orcamentoSelecionado;
        orcamentoId = orcamentoSelecionado.getId();
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
                    WindowUtils.confirmationMessage("Tem certeza que deseja remover o item selecionado?", () -> {
                        int index = getIndex();
                        ItemOrcamento itemOrcamento = getTableView().getItems().get(index);
                        service.excluirPor(itemOrcamento.getId());
                        itemOrcamentoList.remove(itemOrcamento);
                        tableItensOrcamentos.refresh();
                        recarregarValorTotal(itemOrcamento);
                    });
                });

                editarButton.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/vincular-item-window.fxml"));
                        Parent root = loader.load();
                        VincularItemWindowController vincularItemWindow = loader.getController();
                        vincularItemWindow.setParentController(DetalheOrcamentoWindowController.this);
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
                        exibirAlerta(
                                Alert.AlertType.ERROR,
                                "Erro de Validação",
                                "Erro ao abrir a janela de edição: " + e.getMessage()
                        );
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

        if (orcamento.getStatus().equals(OrcamentoStatus.APROVADO)) {
            if (parcelamentoService.isParcelamentoExistentePeloOrcamento(orcamento.getId())) {
                btnVerParcelas.setVisible(true);
            } else {
                btnAdicionarParcelas.setVisible(true);
            }
        }
    }

    @FXML
    public void editar(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/cadastro-orcamento-window.fxml"));
        Parent root = loader.load();
        CadastroOrcamentoWindowController orcamentoController = loader.getController();
        orcamentoController.setAttributes(orcamentoService.buscarPor(orcamentoId));
        Scene scene = new Scene(root);
        Stage popup = new Stage();
        popup.setScene(scene);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        Orcamento orcamentoAtualizado = orcamentoService.buscarPor(orcamentoId);
        populaCampos(orcamentoAtualizado);
        recarregarTabela();
    }

    @FXML
    public void vincularItem(ActionEvent actionEvent) throws IOException {
        if (orcamentoId == 0) {
            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Atenção",
                    "Salve o orçamento antes de vincular um item."
            );
            return;
        }
        abrirTelaVincularItem();
    }

    @FXML
    public void excluir(ActionEvent event) {
        WindowUtils.confirmationMessage("Tem certeza que deseja excluir este orçamento e todos os seus itens?", () -> {
            orcamentoService.excluirOrcamentoEItensVinculados(orcamentoId);
            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Confirmação de Exclusão",
                    "Orçamento e todos os itens foram excluídos com sucesso."
            );
            closeWindow(event);
        });
    }

    @FXML
    void adicionarParcelas() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("/com/br/dreamday/cadastro-parcelamento-window.fxml")));
        Parent root = loader.load();
        CadastroParcelamentoController cadastroParcelamentoController = loader.getController();
        cadastroParcelamentoController.definirAtributos(
                orcamento
        );
        Stage popupStage = new Stage();
        popupStage.setTitle("Detalhe Fornecedor");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    public void atualizarCampoValorTotal(String novoValor) {
        lblValorTotalPreencher.setText(novoValor);
    }

    @FXML
    void verParcelas() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/com/br/dreamday/parcelas-window.fxml")));
        Parent root = loader.load();
        ParcelasControllerWindow parcelasController = loader.getController();
        parcelamentoService.isParcelamentoExistentePeloOrcamento(orcamento.getId());
        Parcelamento parcelamento = parcelamentoService.buscarPorOrcamento(orcamento.getId());

        parcelasController.definirAtributos(
            parcelamento
        );

        Stage popupStage = new Stage();
        popupStage.setTitle("Parcelas");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    void abrirTelaVincularItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("/com/br/dreamday/vincular-item-window.fxml")));
        Parent parent = loader.load();
        VincularItemWindowController controller = loader.getController();
        controller.setParentController(this);
        controller.setOrcamentoId(orcamentoId);
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

    private void populaCampos(Orcamento orcamentoSelecionado) {
        lblDetalhesDoOrcamentoPreencher.setText(orcamentoSelecionado.getId().toString());
        lblClientePreencher.setText(orcamentoSelecionado.getCliente().getNome());
        lblCustoEstimadoPreencher.setText(formatarDecimal(orcamentoSelecionado.getCustoEstimado()));
        lblDataDeCriacaoPreencher.setText(orcamentoSelecionado.getDataCriacao().toString());
        lblValorTotalPreencher.setText(formatarDecimal(orcamentoSelecionado.getValorTotal()));
        lblStatusPreencher.setText(orcamentoSelecionado.getStatus().toString());
        lblObservacoesPreencher.setText(orcamentoSelecionado.getObservaces());
    }

    private String formatarDecimal(BigDecimal valor) {
        return String.format("%.2f", valor);
    }


    private void recarregarTabela() {
        itemOrcamentoList.clear();
        itemOrcamentoList.addAll(service.listarPor(orcamentoId));
        itemOrcamentoList.setAll(service.listarPor(orcamentoId));
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}

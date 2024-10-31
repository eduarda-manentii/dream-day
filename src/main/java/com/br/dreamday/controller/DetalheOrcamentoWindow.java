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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
        this.orcamentoId = Long.valueOf(0);
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
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
            final Button editarButton = new Button("Detalhes");
            final Button excluirButton = new Button("Excluir");
            final HBox buttonBox = new HBox(editarButton, excluirButton);

            {
                buttonBox.setSpacing(10);
                excluirButton.setOnAction(event -> {
                    ItemOrcamento itemOrcamento = getTableView().getItems().get(getIndex());
                    service.excluirPor(itemOrcamento.getId());
                    itemOrcamentoList.remove(itemOrcamento);
                    tableItensOrcamentos.refresh();
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
    public void onButtonExcluirClicked(ActionEvent actionEvent) {
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

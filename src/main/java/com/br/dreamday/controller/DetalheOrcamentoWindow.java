package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.*;
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
    private TableColumn<ItemOrcamento, String> acoesColumn;

    @FXML
    private Button btnVerParcelas;

    private ObservableList<ItemOrcamento> itemOrcamentoList;
    private final ItemOrcamentoService service;
    private final OrcamentoService orcamentoService;
    private final ParcelamentoService parcelamentoService;
    private Orcamento orcamento;

    public DetalheOrcamentoWindow() {
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
        parcelamentoService = new ParcelamentoService();
    }

    public void setAttributes(Orcamento orcamentoSelecionado) {
        this.orcamento = orcamentoSelecionado;
        populaCampos(orcamentoSelecionado);
        itemOrcamentoList = FXCollections.observableArrayList(service.listarPor(orcamento.getId()));;

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        fornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("fornecedor.nome"));
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produto.nome"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
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

        if (orcamento.getStatus().equals(OrcamentoStatus.APROVADO)) {
            if (!parcelamentoService.isParcelamentoExistentePeloOrcamento(orcamento.getId())) {
                btnAdicionarParcelas.setVisible(true);
            }
        }
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
    public void onButtonEditarClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonVincularItemClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonExcluirClicked(ActionEvent actionEvent) {
    }

    @FXML
    void onButtonAdicionarParcelasClicked() throws IOException {
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

    @FXML
    void onButtonVerParcelas() throws IOException {
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
}

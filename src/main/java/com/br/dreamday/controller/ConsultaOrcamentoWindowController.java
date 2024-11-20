package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.service.OrcamentoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class ConsultaOrcamentoWindowController {

    @FXML
    private TableView<Orcamento> tblOrcamento;

    @FXML
    private TableColumn<Orcamento, String> codigoColumn;

    @FXML
    private TableColumn<Orcamento, String> clienteColumn;

    @FXML
    private TableColumn<Orcamento, String> custoEstimadoColumn;

    @FXML
    private TableColumn<Orcamento, String> valorTotalColumn;

    @FXML
    private TableColumn<Orcamento, String> acoesColumn;

    @FXML
    private TableColumn<Orcamento, String> statusColumn;

    @FXML
    private TextField txtNomeDoCliente;

    @FXML
    private ComboBox<OrcamentoStatus> cbStatus;

    private ObservableList<Orcamento> orcamentoList;
    private final OrcamentoService service;

    public ConsultaOrcamentoWindowController() {
        this.service = new OrcamentoService();
    }

    @FXML
    public void initialize() {
        orcamentoList = FXCollections.observableArrayList(service.listarTodos());

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        clienteColumn.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        custoEstimadoColumn.setCellValueFactory(new PropertyValueFactory<>("custoEstimado"));
        valorTotalColumn.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        acoesColumn.setCellFactory(column -> new TableCell<>() {
            final Button button = new Button("Detalhes");
            {
                button.setOnAction(event -> {
                    Orcamento orcamento = getTableView().getItems().get(getIndex());
                    try {
                        detalhes(orcamento);
                    }
                    catch (IOException e) {
                        exibirAlerta(
                                Alert.AlertType.ERROR,
                                "Detalhes",
                                "Ocorreu um erro ao abrir a tela de detalhes de orçamento: ",
                                e.getMessage()
                        );

                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }
        });

        tblOrcamento.setItems(orcamentoList);
        initializeDropDown();
    }

    private void initializeDropDown() {
        List<OrcamentoStatus> status = Arrays.asList(OrcamentoStatus.values());
        ObservableList<OrcamentoStatus> obListStatus = FXCollections.observableArrayList(status);
        obListStatus.addFirst(null);
        cbStatus.setItems(obListStatus);
    }

    @FXML
    private void detalhes(Orcamento orcamentoSelecionado) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("/com/br/dreamday/detalhe-orcamento-window.fxml")));
        Parent root = loader.load();
        DetalheOrcamentoWindowController detalheOrcamentoWindowController = loader.getController();
        detalheOrcamentoWindowController.setAttributes(
                new Orcamento(
                        orcamentoSelecionado.getId(),
                        orcamentoSelecionado.getCliente(),
                        orcamentoSelecionado.getStatus(),
                        orcamentoSelecionado.getDataCriacao(),
                        orcamentoSelecionado.getCustoEstimado(),
                        orcamentoSelecionado.getValorTotal(),
                        orcamentoSelecionado.getObservaces()
                )
        );
        Stage popupStage = new Stage();
        popupStage.setTitle("Detalhe Fornecedor");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
        recarregarTabela();
    }

    @FXML
    void adicionar() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/br/dreamday/cadastro-orcamento-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro de Orçamentos");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    void filtrar() {
        try {
            List<Orcamento> orcamentos;
            if (!txtNomeDoCliente.getText().isBlank() && !(cbStatus.getValue() == null)) {
               orcamentos = service.listarPor(txtNomeDoCliente.getText(), cbStatus.getValue());
            } else if(!(cbStatus.getValue() == null)) {
                orcamentos = service.listarPor(cbStatus.getValue());
            } else if (!txtNomeDoCliente.getText().isBlank()) {
                orcamentos = service.listarPor(txtNomeDoCliente.getText());
            } else {
                orcamentos = service.listarTodos();
            }
            orcamentoList.clear();
            orcamentoList.addAll(orcamentos);
            tblOrcamento.setItems(orcamentoList);
            tblOrcamento.refresh();
        }  catch (Exception e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Filtrar Orçamento",
                    null,
                    "Ocorreu um erro ao filtro o orçamento: " + e.getMessage()
            );
        }
    }

    private void recarregarTabela() {
        orcamentoList.clear();
        orcamentoList.addAll(service.listarTodos());
    }

}

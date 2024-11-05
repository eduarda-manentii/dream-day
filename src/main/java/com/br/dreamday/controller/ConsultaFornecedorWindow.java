package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.utils.Mensagens;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConsultaFornecedorWindow {

    @FXML
    private TableView<Fornecedor> tableFornecedor;

    @FXML
    private TableColumn<Fornecedor, String> codigoColumn;

    @FXML
    private TableColumn<Fornecedor, String> nomeColumn;

    @FXML
    private TableColumn<Fornecedor, String> telefoneColumn;

    @FXML
    private TableColumn<Fornecedor, String> emailColumn;

    @FXML
    private TableColumn<Fornecedor, String> acoesColumn;

    @FXML
    private TextField txtNomeFiltro;

    private ObservableList<Fornecedor> fornecedorList;
    private final FornecedorService fornecedorService;

    public ConsultaFornecedorWindow() {
        this.fornecedorService = new FornecedorService();
    }

    @FXML
    public void initialize() {
        fornecedorList = FXCollections.observableArrayList(fornecedorService.listarTodas());

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        acoesColumn.setCellFactory(column -> new TableCell<>() {
            final Button button = new Button("Detalhes");

            {
                button.setOnAction(event -> {
                    Fornecedor fornecedor = getTableView().getItems().get(getIndex());

                    try {
                        mostrarTelaDetalhe(fornecedor);
                    } catch (IOException e) {
                        Mensagens.exibirMensagemDeErro("Ocorreu um erro carregar as informações da tela de detalhes.");
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

        tableFornecedor.setItems(fornecedorList);
    }

    @FXML
    void adicionar(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-fornecedor-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro Fornecedor");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
        recarregarTabela();
    }

    @FXML
    void filtrar(ActionEvent event) {

        List<Fornecedor> fornecedores;

        if (!txtNomeFiltro.getText().isBlank()) {
            fornecedores = fornecedorService.listarPor(txtNomeFiltro.getText());
        } else {
            fornecedores = fornecedorService.listarTodas();
        }

        fornecedorList.clear();
        fornecedorList.addAll(fornecedores);
        tableFornecedor.setItems(fornecedorList);
        tableFornecedor.refresh();
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    private void mostrarTelaDetalhe(Fornecedor fornecedorSelecionado) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("detalhe-fornecedor-window.fxml")));
        Parent root = loader.load();
        DetalheFornecedorWindow detalheFornecedorWindow = loader.getController();
        detalheFornecedorWindow.setAttributes(
                new Fornecedor(
                        fornecedorSelecionado.getId(),
                        fornecedorSelecionado.getNome(),
                        fornecedorSelecionado.getTelefone(),
                        fornecedorSelecionado.getEmail()
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
        tableFornecedor.refresh();
    }

    public void recarregarTabela() {
        fornecedorList.clear();
        fornecedorList.addAll(fornecedorService.listarTodas());
        tableFornecedor.refresh();
    }
}

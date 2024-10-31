package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.service.ItemFornecedorService;
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

public class DetalheFornecedorWindow {

    @FXML
    private TableView<ItemFornecedor> tableItens;

    @FXML
    private TableColumn<ItemFornecedor, String> codigoColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> descricaoColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> precoColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> categoriaColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> acoesColumn;

    @FXML
    private Label lblCodigoFornecedor;

    @FXML
    private Label lblNomeFornecedor;

    @FXML
    private Label lblTelefoneFornecedor;

    @FXML
    private Label lblEmailFornecedor;

    private ObservableList<ItemFornecedor> itemFornecedorList;
    private final ItemFornecedorService itemFornecedorService;
    private final FornecedorService fornecedorService;
    private Fornecedor fornecedor;

    public DetalheFornecedorWindow() {
        this.fornecedorService = new FornecedorService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    @FXML
    void editar(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-fornecedor-window.fxml")));
        Parent root = loader.load();
        CadastroFornecedorWindow cadastroFornecedorWindow = loader.getController();
        cadastroFornecedorWindow.setAttributes(
                new Fornecedor(fornecedor.getId(), fornecedor.getNome(), fornecedor.getTelefone(), fornecedor.getEmail())
        );

        Stage popupStage = new Stage();
        popupStage.setTitle("Alteração Fornecedor");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    void excluir(ActionEvent event) {

        confirmationMessage(() -> {
            fornecedorService.excluirPor(fornecedor.getId());
            Stage stage = (Stage) tableItens.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    @FXML
    void vincularItem(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-item-fornecedor-window.fxml")));
        Parent root = loader.load();
        CadastroItemFornecedorWindow cadastroItemFornecedorWindow = loader.getController();
        cadastroItemFornecedorWindow.setAttributes(
                new Fornecedor(fornecedor.getId(), fornecedor.getNome(), fornecedor.getTelefone(), fornecedor.getEmail())
        );

        Stage popupStage = new Stage();
        popupStage.setTitle("Vincular Item ao Fornecedor");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    private void mostrarTelaCadastroItemFornecedor(ItemFornecedor itemFornecedorSelecionado) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-item-fornecedor-window.fxml")));
        /*Parent root = loader.load();
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
        popupStage.showAndWait();*/
    }

    public void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }

    public void setAttributes(Fornecedor fornecedorSelecionado) {
        this.fornecedor = fornecedorSelecionado;
        populaCampos(fornecedorSelecionado);

        itemFornecedorList = FXCollections.observableArrayList(itemFornecedorService.listarPor(fornecedor.getId()));

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("produto.nome"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria.nome"));

        acoesColumn.setCellFactory(column -> new TableCell<>() {
            final Button editarButton = new Button("Detalhes");
            final Button excluirButton = new Button("Excluir");
            final HBox buttonBox = new HBox(editarButton, excluirButton);

            {
                buttonBox.setSpacing(10);

                editarButton.setOnAction(event -> {
                    ItemFornecedor itemFornecedor = getTableView().getItems().get(getIndex());

                    try {
                        mostrarTelaCadastroItemFornecedor(itemFornecedor);
                    } catch (IOException e) {
                        exibirAlerta(
                                Alert.AlertType.ERROR,
                                "Erro ao abrir a tela de item fornecedor",
                                null,
                                "Ocorreu um erro carregar as informações da tela de edição: " + e.getMessage()
                        );
                    }
                });

                excluirButton.setOnAction(event -> {
                    ItemFornecedor itemFornecedor = getTableView().getItems().get(getIndex());
                    itemFornecedorService.excluirPor(itemFornecedor.getId());
                    itemFornecedorList.remove(itemFornecedor);
                    tableItens.refresh();
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

        tableItens.setItems(itemFornecedorList);
    }

    private void populaCampos(Fornecedor fornecedorSelecionado) {
        lblCodigoFornecedor.setText(fornecedorSelecionado.getId().toString());
        lblNomeFornecedor.setText(fornecedorSelecionado.getNome());
        lblTelefoneFornecedor.setText(fornecedorSelecionado.getTelefone());
        lblEmailFornecedor.setText(fornecedorSelecionado.getEmail());
    }

    private void confirmationMessage(Runnable action) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnYes = new ButtonType("Sim");
        ButtonType btnNo = new ButtonType("Não");
        dialog.setContentText("Tem certeza que deseja remover?");
        dialog.getButtonTypes().setAll(btnYes, btnNo);
        dialog.showAndWait().ifPresent(b -> {
            if (b == btnYes) {
                action.run();
            }
        });
    }
}

package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.Categoria;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DetalheFornecedorWindow {

    @FXML
    private AnchorPane anchor;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnVincularItem;

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

    @FXML
    private MenuItem menuItemCadastroCategoria;

    @FXML
    private MenuItem menuItemCadastroFornecedor;

    @FXML
    private MenuItem menuItemCadastroProduto;

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

        populaCampos(cadastroFornecedorWindow.getFornecedor());
    }

    @FXML
    void excluir(ActionEvent event) {

        confirmationMessage(() -> {
            try {
                fornecedorService.excluirPor(fornecedor.getId());
                Stage stage = (Stage) tableItens.getScene().getWindow();
                stage.close();
            } catch (Exception ex) {
                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Erro ao deletar fornecedor",
                        null,
                        "Ocorreu um erro na exclusão do : " + ex.getMessage()
                );
            }
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
        cadastroItemFornecedorWindow.setAttributesInsercao(
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
        recarregarTabela();
        tableItens.refresh();
    }

    private void mostrarTelaCadastroItemFornecedor(ItemFornecedor itemFornecedorSelecionado) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-item-fornecedor-window.fxml")));
        Parent root = loader.load();
        CadastroItemFornecedorWindow cadastroItemFornecedorWindow = loader.getController();
        cadastroItemFornecedorWindow.setAttributesAlteracao(itemFornecedorSelecionado);

        Stage popupStage = new Stage();
        popupStage.setTitle("Alterar Item Fornecedor");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
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
        configuraColunasTabela();
        configuraColunaAcoes();
        tableItens.setItems(itemFornecedorList);
    }

    private void configuraColunasTabela() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoProduto"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));
    }

    private void configuraColunaAcoes() {
        acoesColumn.setCellFactory(column -> new TableCell<>() {
            final Button editarButton = new Button("Editar");
            final Button excluirButton = new Button("Excluir");
            final HBox buttonBox = new HBox(editarButton, excluirButton);

            {
                buttonBox.setSpacing(25);

                editarButton.setOnAction(event -> {
                    ItemFornecedor itemFornecedor = getTableView().getItems().get(getIndex());
                    itemFornecedor.setFornecedor(fornecedor);

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

                excluirButton.setOnAction(event -> confirmationMessage(() -> {
                    try {
                        ItemFornecedor itemFornecedor = getTableView().getItems().get(getIndex());
                        itemFornecedorService.excluirPor(itemFornecedor.getId());
                        itemFornecedorList.remove(itemFornecedor);
                        tableItens.refresh();
                    } catch(Exception ex) {
                        exibirAlerta(
                                Alert.AlertType.ERROR,
                                "Exclusão de Item Fornecedor",
                                null,
                                "Ocorreu um erro ao deletar o item: " + ex.getMessage()
                        );
                    }
                }));
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

    public void recarregarTabela() {
        itemFornecedorList.clear();
        itemFornecedorList.addAll(itemFornecedorService.listarPor(fornecedor.getId()));
    }
}

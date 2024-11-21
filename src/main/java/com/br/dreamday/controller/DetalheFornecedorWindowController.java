package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.service.ItemFornecedorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import static com.br.dreamday.utils.WindowUtils.deleteConfirmationMessage;
import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class DetalheFornecedorWindowController {

    @FXML
    private TableView<ItemFornecedor> tableItens;

    @FXML
    private TableColumn<ItemFornecedor, String> codigoColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> descricaoColumn;

    @FXML
    private TableColumn<ItemFornecedor, BigDecimal> precoColumn;

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

    public DetalheFornecedorWindowController() {
        this.fornecedorService = new FornecedorService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    @FXML
    void editar() throws IOException {

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-fornecedor-window.fxml")));
        Parent root = loader.load();
        CadastroFornecedorWindowController cadastroFornecedorWindowController = loader.getController();
        cadastroFornecedorWindowController.setAttributes(
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

        populaCampos(cadastroFornecedorWindowController.getFornecedor());
    }

    @FXML
    void excluir() {

        deleteConfirmationMessage(() -> {
            try {
                fornecedorService.excluirPor(fornecedor.getId());
                Stage stage = (Stage) tableItens.getScene().getWindow();
                stage.close();
            } catch (Exception ex) {
                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Erro ao deletar fornecedor",
                        "Ocorreu um erro na exclusão do : " + ex.getMessage()
                );
            }
        });
    }

    @FXML
    void vincularItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-item-fornecedor-window.fxml")));
        Parent root = loader.load();
        CadastroItemFornecedorWindowController cadastroItemFornecedorWindowController = loader.getController();
        cadastroItemFornecedorWindowController.setAttributesInsercao(
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
        CadastroItemFornecedorWindowController cadastroItemFornecedorWindowController = loader.getController();
        cadastroItemFornecedorWindowController.setAttributesAlteracao(itemFornecedorSelecionado);

        Stage popupStage = new Stage();
        popupStage.setTitle("Alterar Item Fornecedor");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();

        recarregarTabela();
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

        precoColumn.setCellFactory(column -> new TableCell<>() {
            private final NumberFormat formatoBrasileiro = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

            @Override
            protected void updateItem(BigDecimal preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(formatoBrasileiro.format(preco));
                }
            }
        });
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
                                "Ocorreu um erro carregar as informações da tela de edição: " + e.getMessage()
                        );
                    }
                });

                excluirButton.setOnAction(event -> deleteConfirmationMessage(() -> {
                    try {
                        ItemFornecedor itemFornecedor = getTableView().getItems().get(getIndex());
                        itemFornecedorService.excluirPor(itemFornecedor.getId());
                        itemFornecedorList.remove(itemFornecedor);
                        tableItens.refresh();
                    } catch(Exception ex) {
                        exibirAlerta(
                                Alert.AlertType.ERROR,
                                "Exclusão de Item Fornecedor",
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

    public void recarregarTabela() {
        itemFornecedorList.clear();
        itemFornecedorList.addAll(itemFornecedorService.listarPor(fornecedor.getId()));
    }
}

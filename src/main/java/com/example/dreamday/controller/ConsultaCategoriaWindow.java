package com.example.dreamday.controller;

import com.example.dreamday.MainViewApplication;
import com.example.dreamday.domain.Categoria;
import com.example.dreamday.service.CategoriaService;
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

public class ConsultaCategoriaWindow {

    @FXML
    private Button btnAdicionar;

    @FXML
    private Button btnFiltrar;

    @FXML
    private TableColumn<Categoria, String> codigoColumn;

    @FXML
    private TableColumn<Categoria, String> nomeColumn;

    @FXML
    private Label lblNome;

    @FXML
    private MenuItem menuItemCadastroCategoria;

    @FXML
    private MenuItem menuItemCadastroFornecedor;

    @FXML
    private MenuItem menuItemCadastroProduto;

    @FXML
    private TableView<Categoria> tableCategoria;

    @FXML
    private TextField txtNomeFiltro;

    private ObservableList<Categoria> categoriaList;
    private final CategoriaService categoriaService;

    public ConsultaCategoriaWindow() {
        this.categoriaService = new CategoriaService();
    }

    @FXML
    public void initialize() {
        categoriaList = FXCollections.observableArrayList(categoriaService.listarTodas());

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tableCategoria.setItems(categoriaList);
        tableCategoria.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

        });


    }

    @FXML
    void adicionar(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-categoria-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro Categoria");
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

        List<Categoria> categorias;

        if (!txtNomeFiltro.getText().isBlank()) {
            categorias = categoriaService.listarPor(txtNomeFiltro.getText());
        } else {
            categorias = categoriaService.listarTodas();
        }

        categoriaList.clear();
        categoriaList.addAll(categorias);
        tableCategoria.setItems(categoriaList);
        tableCategoria.refresh();
    }

    @FXML
    void editar(ActionEvent event) throws IOException {
        Categoria categoriaSelecionada = tableCategoria.getSelectionModel().getSelectedItem();

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-categoria-window.fxml")));
        Parent root = loader.load();
        CadastroCategoriaWindow cadastroCategoriaWindow = loader.getController();
        cadastroCategoriaWindow.setAttributes(new Categoria(categoriaSelecionada.getId(), categoriaSelecionada.getNome()));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro Categoria");
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
        recarregarTabela();
        tableCategoria.refresh();
    }

    @FXML
    void excluir(ActionEvent event) {
        Categoria categoriaSelecionada = tableCategoria.getSelectionModel().getSelectedItem();
        categoriaService.excluirPor(categoriaSelecionada.getId());
        recarregarTabela();
        tableCategoria.refresh();
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    public void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }

    public void recarregarTabela() {
        categoriaList.clear();
        categoriaList.addAll(categoriaService.listarTodas());
    }
}

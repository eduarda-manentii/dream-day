package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.service.CategoriaService;
import com.br.dreamday.domain.Categoria;
import com.br.dreamday.utils.Mensagens;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    private TableColumn<Categoria, String> codigoColumn;

    @FXML
    private TableColumn<Categoria, String> nomeColumn;

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

        try {
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
        } catch (Exception ex) {
            Mensagens.exibirMensagemDeAviso( "Ocorreu um erro na listagem das categorias: " + ex.getMessage());
        }
    }

    @FXML
    void editar(ActionEvent event) throws IOException {
        Categoria categoriaSelecionada = tableCategoria.getSelectionModel().getSelectedItem();

        if (categoriaSelecionada == null) {
            Mensagens.exibirMensagemDeAviso( "É necessário selecionar uma categoria para edição!");
        } else {

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

            Categoria categoria = cadastroCategoriaWindow.getCategoria();
            int index = categoriaList.indexOf(categoriaSelecionada);
            categoriaList.set(index, categoria);

            tableCategoria.refresh();
        }
    }

    @FXML
    void excluir(ActionEvent event) {
        Categoria categoriaSelecionada = tableCategoria.getSelectionModel().getSelectedItem();

        if (categoriaSelecionada == null) {
            Mensagens.exibirMensagemDeAviso("É necessário selecionar uma categoria para excluir.");
        } else {
            Mensagens.exibirMensagemDeConfirmacao("Tem certeza que deseja remover?", () -> {
                try {
                    categoriaService.excluirPor(categoriaSelecionada.getId());
                    categoriaList.remove(categoriaSelecionada);
                    tableCategoria.refresh();
                } catch (Exception ex) {
                    Mensagens.exibirMensagemDeErro("Ocorreu um erro ao excluir: " + ex.getMessage());
                }
            });
        }
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    public void recarregarTabela() {
        categoriaList.clear();
        categoriaList.addAll(categoriaService.listarTodas());
    }

}

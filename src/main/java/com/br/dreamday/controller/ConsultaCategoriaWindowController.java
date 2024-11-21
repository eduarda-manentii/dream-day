package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.service.CategoriaService;
import com.br.dreamday.domain.Categoria;
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

import static com.br.dreamday.utils.WindowUtils.*;

public class ConsultaCategoriaWindowController {

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

    public ConsultaCategoriaWindowController() {
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
    void adicionar() throws IOException {
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
    void filtrar() {

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
            showMessage(ex.getMessage());
        }
    }

    @FXML
    void editar(

    ) throws IOException {
        Categoria categoriaSelecionada = tableCategoria.getSelectionModel().getSelectedItem();

        if (categoriaSelecionada == null) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Seleção de Categoria",
                    null,
                    "É necessário selecionar uma categoria para edição!"
            );
        } else {

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-categoria-window.fxml")));
            Parent root = loader.load();
            CadastroCategoriaWindowController cadastroCategoriaWindowController = loader.getController();
            cadastroCategoriaWindowController.setAttributes(new Categoria(categoriaSelecionada.getId(), categoriaSelecionada.getNome()));
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
    }

    @FXML
    void excluir() {
        Categoria categoriaSelecionada = tableCategoria.getSelectionModel().getSelectedItem();

        if (categoriaSelecionada == null) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Seleção de Categoria",
                    "É necessário selecionar uma categoria para excluir!"
            );
        } else {

            deleteConfirmationMessage(() -> {
                try {
                    categoriaService.excluirPor(categoriaSelecionada.getId());
                    categoriaList.remove(categoriaSelecionada);
                    tableCategoria.refresh();
                } catch (Exception ex) {
                    exibirAlerta(
                            Alert.AlertType.ERROR,
                            "Erro de Exclusão",
                            "Ocorreu um erro ao excluir: " + ex.getMessage()
                    );
                }
            });
        }
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

    public void recarregarTabela() {
        categoriaList.clear();
        categoriaList.addAll(categoriaService.listarTodas());
    }


}

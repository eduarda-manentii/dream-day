package com.br.dreamday;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainViewController {

    @FXML
    private MenuItem menuItemCadastroCategoria;

    @FXML
    private MenuItem menuItemCadastroFornecedor;

    @FXML
    private MenuItem menuItemCadastroProduto;

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) throws IOException {
//        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-categoria-window.fxml")));
//        Stage popupStage = new Stage();
//        popupStage.setTitle("Consulta Categoria");
//        Scene scene = new Scene(parent);
//        popupStage.setScene(scene);
//        popupStage.initModality(Modality.APPLICATION_MODAL);
//        popupStage.centerOnScreen();
//        popupStage.setResizable(false);
//        popupStage.showAndWait();

        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-item-fornecedor-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Consulta Categoria");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-produto-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Consulta Produto");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    public void mostrarConsultaCliente() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-cliente-window.fxml")));
        Stage popStage = new Stage();
        popStage.setTitle("Consulta de cliente");
        Scene scene = new Scene(parent);
        popStage.setScene(scene);
        popStage.initModality(Modality.APPLICATION_MODAL);
        popStage.centerOnScreen();
        popStage.setResizable(false);
        popStage.showAndWait();
    }

    @FXML
    public void mostrarConsultaOrcamento() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-orcamento-window.fxml")));
        Stage popStage = new Stage();
        popStage.setTitle("Orçamentos");
        Scene scene = new Scene(parent);
        popStage.setScene(scene);
        popStage.initModality(Modality.APPLICATION_MODAL);
        popStage.centerOnScreen();
        popStage.setResizable(false);
        popStage.showAndWait();
    }

    @FXML
    void mostrarCadastroFornecedor(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-fornecedor-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Consulta Fornecedor");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();

    }

}

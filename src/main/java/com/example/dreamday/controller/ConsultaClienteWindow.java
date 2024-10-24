package com.example.dreamday.controller;

import com.example.dreamday.MainViewApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ConsultaClienteWindow {

    @FXML
    public void onButtonAdicionarCLicked(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-cliente-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro Cliente");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    public void onButtonFiltrarClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonEditarClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonExcluirClicked(ActionEvent actionEvent) {
    }
}

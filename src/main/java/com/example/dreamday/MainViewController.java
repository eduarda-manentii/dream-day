package com.example.dreamday;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class MainViewController {

    @FXML
    public void mostrarCadastroCliente() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("consulta-cliente-window.fxml")));
        Stage popStage = new Stage();
        popStage.setTitle("Cadastro de cliente");
        Scene scene = new Scene(parent);
        popStage.setScene(scene);
        popStage.initModality(Modality.APPLICATION_MODAL);
        popStage.centerOnScreen();
        popStage.setResizable(false);
        popStage.showAndWait();
    }

}
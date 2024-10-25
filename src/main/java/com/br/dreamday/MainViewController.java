package com.br.dreamday;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainViewController {

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

}
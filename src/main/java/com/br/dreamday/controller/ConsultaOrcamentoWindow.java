package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.service.OrcamentoService;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ConsultaOrcamentoWindow {

    @FXML
    private TextField txtNomeDoCliente;

    @FXML
    private ComboBox<OrcamentoService> cbStatus;

    @FXML
    void onButtonAdicionarClicked() throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/br/dreamday/cadastro-orcamento-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro de Orçamentos");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    void onButtonFiltrarClicked() {}
}

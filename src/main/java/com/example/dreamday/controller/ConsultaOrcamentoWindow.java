package com.example.dreamday.controller;

import com.example.dreamday.service.OrcamentoService;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
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
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("cadastro-cliente-window.fxml")));
        Stage popup = new Stage();
        popup.setTitle("");
        Scene scene = new Scene(parent);
        popup.setScene(scene);
        popup.showAndWait();
    }

    @FXML
    void onButtonFiltrarClicked() {}
}

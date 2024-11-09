package com.br.dreamday;

import com.br.dreamday.utils.WindowUtils;
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
    void mostrarConsultaCategoria() throws IOException {
        WindowUtils.abrirTelaConsultaCategoria();
    }

    @FXML
    void mostrarConsultaProduto() throws IOException {
        WindowUtils.abrirTelaConsultaProduto();
    }

    @FXML
    void mostrarConsultaCliente() throws IOException {
        WindowUtils.abrirTelaConsultaCliente();
    }

    @FXML
    void mostrarConsultaOrcamento() throws IOException {
        WindowUtils.abrirTelaConsultaOrcamento();
    }

    @FXML
    void mostrarConsultaFornecedor() throws IOException {
        WindowUtils.abrirTelaConsultaFornecedor();
    }

}

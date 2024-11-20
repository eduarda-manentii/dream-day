package com.br.dreamday.controller;

import com.br.dreamday.utils.WindowUtils;
import javafx.fxml.FXML;

import java.io.IOException;

public class RelatorioController {

    @FXML
    void aoClicarCliente() throws IOException {
        WindowUtils.abrirTela("cliente-relatorio");
    }

    @FXML
    void aoClicarStatus() {
    }

    @FXML
    void aoClicarValor() {
    }

}


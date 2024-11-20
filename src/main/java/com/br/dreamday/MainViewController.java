package com.br.dreamday;

import com.br.dreamday.utils.WindowUtils;
import javafx.fxml.FXML;
import java.io.IOException;

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

    @FXML
    void mostrarCalendarioOrcamento() throws IOException {
        WindowUtils.abrirTelaCalendarioOrcamento();
    }

    @FXML
    void mostrarRelatorio() throws IOException {
        WindowUtils.abrirTela("cliente-relatorio");
    }
}

package com.br.dreamday.utils;

import com.br.dreamday.MainViewApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WindowUtils {

    public static void abrirTelaConsultaCategoria() throws IOException {
        abrirTela("consulta-categoria");
    }

    public static void abrirTelaConsultaProduto() throws IOException {
        abrirTela("consulta-produto");
    }

    public static void abrirTelaConsultaCliente() throws IOException {
        abrirTela("consulta-cliente");
    }

    public static void abrirTelaConsultaOrcamento() throws IOException {
        abrirTela("consulta-orcamento");
    }

    public static void abrirTelaConsultaFornecedor() throws IOException {
        abrirTela("consulta-fornecedor");
    }

    public static void abrirTela(String nomeTela) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource(inserirPrefixo(nomeTela))));
        Stage popupStage = new Stage();
        popupStage.setTitle(gerarTituloPagina(nomeTela));
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    private static String inserirPrefixo(String nomeTela) {
        return nomeTela.concat("-window.fxml");
    }

    private static String gerarTituloPagina(String nomeTela) {
        if (nomeTela.isBlank() || !nomeTela.contains("-")) {
            throw new IllegalArgumentException("O nome da página está em branco ou não está no padrão (exemplo-exemplo)");
        }

        String[] partesNomeTela = nomeTela.trim().split("-");
        if (partesNomeTela.length < 2) {
            throw new IllegalArgumentException("O nome da página está em branco ou não está no padrão (exemplo-exemplo)");
        }

        return partesNomeTela[0].toUpperCase() + " " + partesNomeTela[1].toUpperCase();
    }
}

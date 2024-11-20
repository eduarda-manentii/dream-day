package com.br.dreamday.utils;

import com.br.dreamday.MainViewApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

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

    public static void abrirTelaCalendarioOrcamento() throws IOException {
        abrirTela("casamento-calendario");
    }


    public static void mostraMensagem(String titulo, String mensagem) throws IOException {
        exibirAlerta(
                Alert.AlertType.INFORMATION,
                titulo,
                mensagem
        );
    }

    public static void mostraMensagem(String mensagem) throws IOException {
        mostraMensagem("Aviso!", mensagem);
    }

    public static void exibirAlerta(Alert.AlertType tipo, String titulo, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }

    public static void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }

    public static void deleteConfirmationMessage(Runnable action) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnYes = new ButtonType("Sim");
        ButtonType btnNo = new ButtonType("Não");
        dialog.setContentText("Tem certeza que deseja remover?");
        dialog.getButtonTypes().setAll(btnYes, btnNo);
        dialog.showAndWait().ifPresent(b -> {
            if (b == btnYes) {
                action.run();
            }
        });
    }

    public static void confirmationMessage(String mensagem, Runnable acao) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnYes = new ButtonType("Sim");
        ButtonType btnNo = new ButtonType("Não");
        dialog.setContentText(mensagem);
        dialog.getButtonTypes().setAll(btnYes, btnNo);
        dialog.showAndWait().ifPresent(b -> {
            if (b == btnYes) {
                acao.run();
            }
        });
    }

    public static void abrirTela(String nomeTela) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource(inserirPrefixo(nomeTela))));
            Stage popupStage = new Stage();
            popupStage.setTitle(gerarTituloPagina(nomeTela));
            Scene scene = new Scene(parent);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.centerOnScreen();
            popupStage.setResizable(false);
            popupStage.showAndWait();
        } catch(Exception e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro ao abrir a tela",
                    "Ocorreu um erro na abertura da tela, favor verificar se o nome da tela foi inserido corretamente!"
            );
        }
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

package com.br.dreamday.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class Mensagens {

    public static void exibirMensagemInformativa(String mensagem) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION, mensagem, ButtonType.OK);
        dialog.showAndWait();
    }

    public static void exibirMensagemDeErro(String mensagem) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        dialog.showAndWait();
    }

    public static void exibirMensagemDeConfirmacao(String mensagem, Runnable acao) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, mensagem, ButtonType.YES, ButtonType.NO);
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) acao.run();
        });
    }

    public static void exibirMensagemDeAviso(String mensagem) {
        ButtonType botaoOk = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Aviso!");
        dialog.setContentText(mensagem);
        dialog.getDialogPane().getButtonTypes().add(botaoOk);
        boolean desativado = false;
        dialog.getDialogPane().lookupButton(botaoOk).setDisable(desativado);
        dialog.showAndWait();
    }

}

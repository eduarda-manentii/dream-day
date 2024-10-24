package com.example.dreamday.controller;

import com.example.dreamday.domain.Cliente;
import com.example.dreamday.service.ClienteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CadastroClienteWindowController {

    @FXML
    private Button btnCancelar;


    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtDataCasamento;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNomeCompleto;

    @FXML
    private TextField txtNomeConjugue;

    @FXML
    private TextField txtTelefone;

    private ClienteService service;

    public CadastroClienteWindowController() {
        this.service = new ClienteService();
    }

    @FXML
    void initialize() throws ParseException {
        txtDataCasamento.setPromptText("dd/MM/yyyy");
        txtCpf.setPromptText("XXX.XXX.XXX-XX");
        txtTelefone.setPromptText("(XX) XXXXX-XXXX");
        MascarasFX.mascaraData(txtDataCasamento);
        MascarasFX.mascaraCPF(txtCpf);
        MascarasFX.mascaraEmail(txtEmail);
        MascarasFX.mascaraTelefone(txtTelefone);
    }

    @FXML
    void onButtonSalvarClicked(ActionEvent event) {
        String nomeCompleto = txtNomeCompleto.getText();
        String nomeConjugue = txtNomeConjugue.getText();
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String telefone = txtTelefone.getText();
        String dataDoCasamento = txtDataCasamento.getText();
        if (!(nomeCompleto.isBlank() && nomeConjugue.isBlank() && cpf.isBlank() && email.isBlank() && telefone.isBlank() && dataDoCasamento.isBlank())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataDoCasamentoDT = LocalDate.parse(txtDataCasamento.getText(), formatter);
            Cliente cliente = new Cliente(nomeCompleto, nomeConjugue, dataDoCasamentoDT, telefone, email, cpf);
            service.salvar(cliente);
            limparCampos();
            showMessage("Cliente cadastrado com sucesso!");
        } else {
            showMessage("Todos os campos são obrigatórios!");
        }
    }

    @FXML
    void onButtonCancelarClicked(ActionEvent event) {
        if (camposPreenchidos()) {
            confirmationMessage("Tem certeza que deseja cancelar a inserção?", () -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
        } else {
            Stage stage = (Stage) btnCancelar.getScene().getWindow();
            stage.close();
        }
    }

    private boolean camposPreenchidos() {
        String nomeCompleto = txtNomeCompleto.getText();
        String nomeConjugue = txtNomeConjugue.getText();
        String cpf = txtCpf.getText();
        String email = txtEmail.getText();
        String telefone = txtTelefone.getText();
        String dataDoCasamento = txtDataCasamento.getText();
        return !nomeCompleto.isBlank() || !nomeConjugue.isBlank() ||
                !cpf.isBlank() || !email.isBlank() || !telefone.isBlank() ||
                !dataDoCasamento.isBlank();
    }

    private void limparCampos() {
        txtNomeCompleto.setText("");
        txtNomeConjugue.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtDataCasamento.setText("");
    }

    private void showMessage(String mensagem) {
        ButtonType loginButtonType = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText(mensagem);
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        boolean desativado = false;
        dialog.getDialogPane().lookupButton(loginButtonType).setDisable(desativado);
        dialog.showAndWait();
    }

    private void confirmationMessage(String mensagem, Runnable acao) {
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
}

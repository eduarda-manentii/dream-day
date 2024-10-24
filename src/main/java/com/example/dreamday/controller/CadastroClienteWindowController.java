package com.example.dreamday.controller;

import com.example.dreamday.domain.Cliente;
import com.example.dreamday.service.ClienteService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;


public class CadastroClienteWindowController {

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

    }

    private void limparCampos() {
        txtNomeCompleto.setText("");
        txtNomeConjugue.setText("");
        txtCpf.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtDataCasamento.setText("");
    }

    private void showMessage(String message) {
        ButtonType loginButtonType = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        boolean disabled = false;
        dialog.getDialogPane().lookupButton(loginButtonType).setDisable(disabled);
        dialog.showAndWait();
    }
}

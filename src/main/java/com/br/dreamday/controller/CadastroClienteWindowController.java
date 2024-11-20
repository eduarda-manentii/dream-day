package com.br.dreamday.controller;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.utils.MascarasUtils;
import com.br.dreamday.utils.WindowUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;


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

    private final ClienteService service;

    private Cliente clienteSelecionado;

    public CadastroClienteWindowController() {
        this.service = new ClienteService();
    }

    @FXML
    void initialize() throws ParseException {
        txtDataCasamento.setPromptText("dd/MM/yyyy");
        txtCpf.setPromptText("XXX.XXX.XXX-XX");
        txtTelefone.setPromptText("(XX) XXXXX-XXXX");
        MascarasUtils.mascaraData(txtDataCasamento);
        MascarasUtils.mascaraCPF(txtCpf);
        MascarasUtils.mascaraEmail(txtEmail);
        MascarasUtils.mascaraTelefone(txtTelefone);
    }

    @FXML
    void salvar(ActionEvent event) {
        try {
            String nomeCompleto = txtNomeCompleto.getText();
            String nomeConjugue = txtNomeConjugue.getText();
            String cpf = txtCpf.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataDoCasamentoDT = LocalDate.parse(txtDataCasamento.getText(), formatter);
            if (clienteSelecionado != null) {
                clienteSelecionado.setNome(nomeCompleto);
                clienteSelecionado.setConjugue(nomeConjugue);
                clienteSelecionado.setCpf(cpf);
                clienteSelecionado.setEmail(email);
                clienteSelecionado.setTelefone(telefone);
                clienteSelecionado.setDataCasamento(dataDoCasamentoDT);
                service.salvar(clienteSelecionado);
                clienteSelecionado = null;
            } else {
                Cliente cliente = new Cliente(nomeCompleto, nomeConjugue, dataDoCasamentoDT, telefone, email, cpf);
                service.salvar(cliente);
            }
            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Confirmação de Salvamento",
                    "As alterações foram salvas com sucesso. "
            );
            limparCampos();
        }  catch (DateTimeException ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    "Ocorreu um erro ao salvar as informações:  Digite um valor para a data válido."
            );
        } catch (Exception e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    "Ocorreu um erro ao salvar as informações: " + e.getMessage()
            );
        }
    }

    @FXML
    void cancelar(ActionEvent event) {
        if (camposPreenchidos()) {
            WindowUtils.confirmationMessage("Tem certeza que deseja cancelar a inserção?", () -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
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

    public void setAttributes(Cliente clienteSelecionado) {
        this.clienteSelecionado = clienteSelecionado;
        txtNomeCompleto.setText(clienteSelecionado.getNome());
        txtNomeConjugue.setText(clienteSelecionado.getConjugue());
        txtCpf.setText(clienteSelecionado.getCpf());
        txtEmail.setText(clienteSelecionado.getEmail());
        txtTelefone.setText(clienteSelecionado.getTelefone());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtDataCasamento.setText(clienteSelecionado.getDataCasamento().format(formatter));
    }

}

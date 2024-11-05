package com.br.dreamday.controller;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.utils.MascarasFX;
import com.br.dreamday.utils.Mensagens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class CadastroClienteWindow {

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

    private Cliente clienteSelecionado;

    public CadastroClienteWindow() {
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
        try {
            String nomeCompleto = txtNomeCompleto.getText();
            String nomeConjugue = txtNomeConjugue.getText();
            String cpf = txtCpf.getText();
            String email = txtEmail.getText();
            String telefone = txtTelefone.getText();
            String dataDoCasamento = txtDataCasamento.getText();
            if (!(nomeCompleto.isBlank() && nomeConjugue.isBlank() && cpf.isBlank() && email.isBlank() && telefone.isBlank() && dataDoCasamento.isBlank())) {
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
                    Mensagens.exibirMensagemInformativa("Cliente alterado com sucesso!");
                } else {
                    Cliente cliente = new Cliente(nomeCompleto, nomeConjugue, dataDoCasamentoDT, telefone, email, cpf);
                    service.salvar(cliente);
                    Mensagens.exibirMensagemInformativa("Cliente cadastrado com sucesso!");
                }
                limparCampos();
            } else {
                Mensagens.exibirMensagemDeAviso("Todos os campos são obrigatórios!");
            }
        }  catch (DateTimeException ex) {
            Mensagens.exibirMensagemDeErro("Digite um valor para a hora válido.");
        } catch (Exception e) {
            Mensagens.exibirMensagemDeErro(e.getMessage());
        }
    }

    @FXML
    void onButtonCancelarClicked(ActionEvent event) {
        if (camposPreenchidos()) {
            Mensagens.exibirMensagemDeConfirmacao("Tem certeza que deseja cancelar a inserção?", () -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
        }
    }

    @FXML
    void onButtonVoltarClicked(ActionEvent event) {
        if (camposPreenchidos()) {
            Mensagens.exibirMensagemDeConfirmacao("Tem certeza que deseja cancelar a inserção?", () -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

package com.br.dreamday.controller;

import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.utils.Mensagens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CadastroFornecedorWindow {

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTelefone;

    private FornecedorService fornecedorService;
    private boolean isEdicaoFornecedor;
    private Fornecedor fornecedor;

    public CadastroFornecedorWindow() {
        this.fornecedorService = new FornecedorService();
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    @FXML
    void salvar(ActionEvent event) {
        try {
            String nome = txtNome.getText();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();

            if (!isEdicaoFornecedor) {
                fornecedor = new Fornecedor(null, nome, telefone, email);
                limparCampos();
            } else {
                fornecedor.setNome(nome);
                fornecedor.setTelefone(telefone);
                fornecedor.setEmail(email);
            }

            fornecedorService.salvar(fornecedor);
            Mensagens.exibirMensagemInformativa("As alterações foram salvas com sucesso.");
        } catch (Exception ex) {
            Mensagens.exibirMensagemDeErro("Ocorreu um erro ao salvar as informações: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtTelefone.clear();
        txtEmail.clear();
    }

    public void setAttributes(Fornecedor fornecedorSelecionado) {
        this.fornecedor = fornecedorSelecionado;
        populaCampos(fornecedorSelecionado);
        isEdicaoFornecedor = true;
    }

    public void populaCampos(Fornecedor fornecedorSelecionado) {
        txtNome.setText(fornecedorSelecionado.getNome());
        txtTelefone.setText(fornecedorSelecionado.getTelefone());
        txtEmail.setText(fornecedorSelecionado.getEmail());
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }
}

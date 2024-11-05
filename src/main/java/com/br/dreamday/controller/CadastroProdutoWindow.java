package com.br.dreamday.controller;

import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.ProdutoService;
import com.br.dreamday.utils.Mensagens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CadastroProdutoWindow {

    @FXML
    private Button btnCancelar;

    @FXML
    private TextArea txaDescricao;

    @FXML
    private TextField txtNome;

    private ProdutoService produtoService;
    private boolean isEdicaoProduto;
    private Produto produto;

    public CadastroProdutoWindow() {
        this.produtoService = new ProdutoService();
    }

    @FXML
    void cancelar(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void salvar(ActionEvent event) {
        try {
            String nome = txtNome.getText();
            String descricao = txaDescricao.getText();

            if (!isEdicaoProduto) {
                produto = new Produto(null, nome, descricao);
                Mensagens.exibirMensagemInformativa("As alterações foram salvas com sucesso.");
                limparCampos();
            } else {
                produto.setNome(nome);
                produto.setDescricao(descricao);
                Mensagens.exibirMensagemInformativa("As alterações foram salvas com sucesso.");
            }

            produtoService.salvar(produto);
        } catch (Exception ex) {
            Mensagens.exibirMensagemDeAviso("Ocorreu um erro ao salvar as informações: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txaDescricao.clear();
    }

    public void setAttributes(Produto produtoSelecionado) {
        this.produto = produtoSelecionado;
        txtNome.setText(produtoSelecionado.getNome());
        txaDescricao.setText(produtoSelecionado.getDescricao());
        isEdicaoProduto = true;
    }

    public Produto getProduto() {
        return this.produto;
    }
}

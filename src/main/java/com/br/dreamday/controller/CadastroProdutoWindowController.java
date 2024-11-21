package com.br.dreamday.controller;

import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.ProdutoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class CadastroProdutoWindowController {

    @FXML
    private Button btnCancelar;

    @FXML
    private TextArea txaDescricao;

    @FXML
    private TextField txtNome;

    private ProdutoService produtoService;
    private boolean isEdicaoProduto;
    private Produto produto;

    public CadastroProdutoWindowController() {
        this.produtoService = new ProdutoService();
    }

    @FXML
    void cancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    @FXML
    void salvar() {

        try {
            String nome = txtNome.getText();
            String descricao = txaDescricao.getText();

            if (!isEdicaoProduto) {
                produto = new Produto(null, nome, descricao);
                produtoService.salvar(produto);
                limparCampos();
            } else {
                produto.setNome(nome);
                produto.setDescricao(descricao);
                produtoService.salvar(produto);
            }

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Confirmação de Salvamento",
                    "As alterações foram salvas com sucesso. "
            );
        } catch (Exception ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    "Ocorreu um erro ao salvar as informações: " + ex.getMessage()
            );
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
}

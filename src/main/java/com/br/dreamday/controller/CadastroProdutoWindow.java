package com.br.dreamday.controller;

import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.ProdutoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CadastroProdutoWindow {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

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

                exibirAlerta(
                        Alert.AlertType.INFORMATION,
                        "Confirmação de Salvamento",
                        null,
                        "As alterações foram salvas com sucesso. "
                );
                limparCampos();
            } else {
                produto.setNome(nome);
                produto.setDescricao(descricao);

                exibirAlerta(
                        Alert.AlertType.INFORMATION,
                        "Confirmação de Alteração",
                        null,
                        "As alterações foram salvas com sucesso. "
                );
            }

            produtoService.salvar(produto);
        } catch (Exception ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    null,
                    "Ocorreu um erro ao salvar as informações: " + ex.getMessage()
            );
        }
    }

    public void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
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

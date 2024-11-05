package com.br.dreamday.controller;

import com.br.dreamday.service.CategoriaService;
import com.br.dreamday.domain.Categoria;
import com.br.dreamday.utils.Mensagens;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroCategoriaWindow {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    @FXML
    private TextField txtNome;

    private CategoriaService categoriaService;
    private boolean isEdicaoCategoria;
    private Categoria categoria;

    public CadastroCategoriaWindow() {
        this.categoriaService = new CategoriaService();
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

            if (!isEdicaoCategoria) {
                categoria = new Categoria(null, nome);
                Mensagens.exibirMensagemInformativa("As alterações foram salvas com sucesso.");
                limparCampos();
            } else {
                categoria.setNome(nome);
                Mensagens.exibirMensagemInformativa("As alterações foram salvas com sucesso.");
            }

            categoriaService.salvar(categoria);
        } catch (Exception ex) {
            Mensagens.exibirMensagemDeErro("Ocorreu um erro ao salvar as informações: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtNome.clear();
    }

    public void setAttributes(Categoria categoriaSelecionada) {
        this.categoria = categoriaSelecionada;
        txtNome.setText(categoriaSelecionada.getNome());
        isEdicaoCategoria = true;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }
}

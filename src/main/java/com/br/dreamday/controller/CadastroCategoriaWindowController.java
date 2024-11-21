package com.br.dreamday.controller;

import com.br.dreamday.service.CategoriaService;
import com.br.dreamday.domain.Categoria;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class CadastroCategoriaWindowController {

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtNome;

    private final CategoriaService categoriaService;
    private boolean isEdicaoCategoria;
    private Categoria categoria;

    public CadastroCategoriaWindowController() {
        this.categoriaService = new CategoriaService();
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

            if (!isEdicaoCategoria) {
                categoria = new Categoria(nome);
                categoriaService.salvar(categoria);
                limparCampos();
            } else {
                categoria.setNome(nome);
                categoriaService.salvar(categoria);
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
    }

    public void setAttributes(Categoria categoriaSelecionada) {
        this.categoria = categoriaSelecionada;
        txtNome.setText(categoriaSelecionada.getNome());
        isEdicaoCategoria = true;
    }
}

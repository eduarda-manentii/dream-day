package com.example.dreamday.controller;

import com.example.dreamday.domain.Categoria;
import com.example.dreamday.service.CategoriaService;
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
                categoriaService.salvar(categoria);

                exibirAlerta(
                        Alert.AlertType.INFORMATION,
                        "Confirmação de Salvamento",
                        null,
                        "As alterações foram salvas com sucesso. "
                );
                limparCampos();
            } else {
                categoria.setNome(nome);
                categoriaService.salvar(categoria);

                exibirAlerta(
                        Alert.AlertType.INFORMATION,
                        "Confirmação de Alteração",
                        null,
                        "As alterações foram salvas com sucesso. "
                );
            }
        } catch (Exception ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    null,
                    "Ocorreu um erro ao salvar as informações: " + ex.getMessage()
            );
        }
    }

    private void limparCampos() {
        txtNome.clear();
    }

    public void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }

    public void setAttributes(Categoria categoriaSelecionada) {
        this.categoria = categoriaSelecionada;
        txtNome.setText(categoriaSelecionada.getNome());
        isEdicaoCategoria = true;
    }
}

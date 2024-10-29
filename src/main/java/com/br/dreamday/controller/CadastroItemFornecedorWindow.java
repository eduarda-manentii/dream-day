package com.br.dreamday.controller;

import com.br.dreamday.component.AutoCompleteProduto;
import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.service.ProdutoService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CadastroItemFornecedorWindow implements Initializable {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField autoTxtCategoria;

    private AutoCompleteProduto autoTxtProduto;

    @FXML
    private Label lblNomeFornecedor;

    private Fornecedor fornecedor;

    private final ProdutoService produtoService;

    public CadastroItemFornecedorWindow() {
        this.produtoService = new ProdutoService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autoTxtProduto = new AutoCompleteProduto(produtoService);

        autoTxtProduto.setLayoutX(156.0);
        autoTxtProduto.setLayoutY(119.0);
        autoTxtProduto.setPrefHeight(25.0);
        autoTxtProduto.setPrefWidth(407.0);

        rootPane.getChildren().removeIf(node -> node instanceof TextField &&
                (node.getId().equals("autoTxtCategoria") || node.getId().equals("autoTxtProduto")));
        rootPane.getChildren().addAll(autoTxtCategoria, autoTxtProduto);
    }

    public void setAttributes(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
        lblNomeFornecedor.setText(fornecedor.getNome());
    }
}

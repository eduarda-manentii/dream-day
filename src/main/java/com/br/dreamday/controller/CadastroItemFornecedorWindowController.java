package com.br.dreamday.controller;

import com.br.dreamday.component.AutoCompleteCategoria;
import com.br.dreamday.component.AutoCompleteProduto;
import com.br.dreamday.domain.Categoria;
import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.CategoriaService;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.ProdutoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class CadastroItemFornecedorWindowController implements Initializable {


    @FXML
    private AnchorPane rootPane;

    private AutoCompleteCategoria autoTxtCategoria;

    private AutoCompleteProduto autoTxtProduto;

    @FXML
    private Label lblNomeFornecedor;

    @FXML
    private TextField txtPreco;

    private boolean isEdicaoItem;
    private ItemFornecedor itemFornecedor;
    private Fornecedor fornecedor;
    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final ItemFornecedorService itemFornecedorService;

    public CadastroItemFornecedorWindowController() {
        this.produtoService = new ProdutoService();
        this.categoriaService = new CategoriaService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        autoTxtProduto = new AutoCompleteProduto(produtoService);
        autoTxtCategoria = new AutoCompleteCategoria(categoriaService);

        definirPosicaoAutoCompleteProd();
        definirPosicaoAutoCompleteCateg();
        rootPane.getChildren().addAll(autoTxtCategoria, autoTxtProduto);
    }

    @FXML
    void salvar() {
        try {

            Categoria categoria = autoTxtCategoria.getCategoriaSelecionada();
            BigDecimal preco = BigDecimal.valueOf(Double.parseDouble(txtPreco.getText()));

            if (!isEdicaoItem) {

                Produto produto = autoTxtProduto.getProdutoSelecionado();
                itemFornecedor = new ItemFornecedor(
                        fornecedor.getId(),
                        produto.getId(),
                        preco,
                        categoria,
                        fornecedor,
                        produto
                );
                limparCampos();
            } else {
                itemFornecedor.setPreco(preco);
                itemFornecedor.setCategoria(categoria);
            }

            itemFornecedorService.salvar(itemFornecedor);

            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Confirmação de Alteração",
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
        txtPreco.clear();
        autoTxtCategoria.limparAutoComplete();
        autoTxtProduto.limparAutoComplete();
    }

    public void setAttributesInsercao(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
        lblNomeFornecedor.setText(fornecedor.getNome());
    }

    public void setAttributesAlteracao(ItemFornecedor itemFornecedor) {
        this.itemFornecedor = itemFornecedor;
        this.lblNomeFornecedor.setText(itemFornecedor.getFornecedor().getNome());
        this.autoTxtCategoria.setCategoriaSelecionado(itemFornecedor.getCategoria());
        this.autoTxtProduto.setProdutoSelecionado(itemFornecedor.getProduto());
        this.txtPreco.setText(itemFornecedor.getPreco().toString());
        isEdicaoItem = true;
    }

    private void definirPosicaoAutoCompleteProd() {
        autoTxtProduto.setLayoutX(156.0);
        autoTxtProduto.setLayoutY(119.0);
        autoTxtProduto.setPrefHeight(25.0);
        autoTxtProduto.setPrefWidth(407.0);
    }

    private void definirPosicaoAutoCompleteCateg() {
        autoTxtCategoria.setLayoutX(156.0);
        autoTxtCategoria.setLayoutY(179.0);
        autoTxtCategoria.setPrefHeight(25.0);
        autoTxtCategoria.setPrefWidth(407.0);
    }
}

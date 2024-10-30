package com.br.dreamday.controller;

import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.service.ItemFornecedorService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;

public class ConsultaItemFornecedorWindow {

    @FXML
    private MenuItem menuItemCadastroCategoria;

    @FXML
    private MenuItem menuItemCadastroFornecedor;

    @FXML
    private MenuItem menuItemCadastroProduto;

    @FXML
    private TableView<ItemFornecedor> tableItemFornecedor;

    @FXML
    private TableColumn<ItemFornecedor, String> codigoColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> descricaoProdutoColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> valorColumn;

    @FXML
    private TableColumn<ItemFornecedor, String> descricaoFornecedorColumn;

    @FXML
    private TextField txtDescricaoFornecedor;

    @FXML
    private TextField txtDescricaoProduto;

    @FXML
    private TextField txtValorFinal;

    @FXML
    private TextField txtValorInicial;

    private ObservableList<ItemFornecedor> itemFornecedorList;
    private ItemFornecedorService itemFornecedorService;

    public ConsultaItemFornecedorWindow() {
    }

    @FXML
    public void initialize() {

        configuraColunasTabela();
    }

    @FXML
    void filtrar(ActionEvent event) {

        try {
            String descricaoFornecedor = txtDescricaoFornecedor.getText();
            String descricaoProduto = txtDescricaoProduto.getText();
            Double valorInicial = Double.parseDouble(txtValorFinal.getText());
            Double valorFinal = Double.parseDouble(txtValorFinal.getText());

            itemFornecedorService.listarPor(descricaoProduto, descricaoFornecedor, valorInicial, valorFinal);
        } catch (Exception ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Listagem de Item Fornecedor",
                    null,
                    "Ocorreu um erro na listagem dos itens: " + ex.getMessage()
            );
        }
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    private void configuraColunasTabela() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoProduto"));
        valorColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        descricaoFornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoFornecedor"));
    }

    public void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }
}

package com.br.dreamday.controller;

import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.utils.Mensagens;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ConsultaItemFornecedorWindow {

    @FXML
    private AnchorPane rootPane;

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
    private final ItemFornecedorService itemFornecedorService;

    public ConsultaItemFornecedorWindow() {
        itemFornecedorService = new ItemFornecedorService();
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

            BigDecimal valorInicial = null;
            BigDecimal valorFinal = null;

            if (!txtValorInicial.getText().isBlank()) {
                valorInicial = BigDecimal.valueOf(Double.parseDouble(txtValorInicial.getText()));
            }

            if (!txtValorFinal.getText().isBlank()) {
                valorFinal = BigDecimal.valueOf(Double.parseDouble(txtValorFinal.getText()));
            }

            itemFornecedorList = FXCollections.observableArrayList(
                    itemFornecedorService.listarPor(descricaoProduto, descricaoFornecedor, valorInicial, valorFinal)
            );

            tableItemFornecedor.setItems(itemFornecedorList);

        } catch (Exception ex) {
            Mensagens.exibirMensagemDeErro("Ocorreu um erro na listagem dos itens: " + ex.getMessage());
        }
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    @FXML
    void mostrarInformacaoDeFiltragem(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();

        Tooltip tooltip = new Tooltip("""
                Valores padrão:\s
                Valor Inicial: 0\s
                Valor Final: 1000""");
        Tooltip.install(imageView, tooltip);
    }

    private void configuraColunasTabela() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoProduto"));
        valorColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        descricaoFornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoFornecedor"));
    }

}

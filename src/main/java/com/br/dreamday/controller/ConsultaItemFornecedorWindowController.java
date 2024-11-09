package com.br.dreamday.controller;

import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.service.ItemFornecedorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.math.BigDecimal;

public class ConsultaItemFornecedorWindowController {

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

    @FXML
    private ImageView imgInformation;

    private ObservableList<ItemFornecedor> itemFornecedorList;
    private final ItemFornecedorService itemFornecedorService;

    public ConsultaItemFornecedorWindowController() {
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

    public void exibirAlerta(Alert.AlertType tipo, String titulo, String cabecalho, String conteudo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait().filter(response -> response == ButtonType.OK);
    }
}

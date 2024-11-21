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

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class ConsultaItemFornecedorWindowController {

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

    public ConsultaItemFornecedorWindowController() {
        itemFornecedorService = new ItemFornecedorService();
    }

    @FXML
    public void initialize() {
        configuraColunasTabela();
    }

    @FXML
    void filtrar() {

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
            showMessage(ex.getMessage());
        }
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

    private void showMessage(String mensagem) {
        ButtonType loginButtonType = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Aviso");
        dialog.setContentText(mensagem);
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        boolean desativado = false;
        dialog.getDialogPane().lookupButton(loginButtonType).setDisable(desativado);
        dialog.showAndWait();
    }

    private void configuraColunasTabela() {
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        descricaoProdutoColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoProduto"));
        valorColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        descricaoFornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("descricaoFornecedor"));
    }
}

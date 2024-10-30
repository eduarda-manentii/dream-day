package com.br.dreamday.controller;

import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.service.FornecedorService;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class DetalheOrcamentoWindow {

    @FXML
    private Label lblClientePreencher;

    @FXML
    private Label lblCustoEstimadoPreencher;

    @FXML
    private Label lblDataDeCriacaoPreencher;

    @FXML
    private Label lblDetalhesDoOrcamentoPreencher;

    @FXML
    private Label lblObservacoesPreencher;

    @FXML
    private Label lblStatusPreencher;

    @FXML
    private Label lblValorTotalPreencher;

    @FXML
    private TableView<ItemOrcamento> tableItensOrcamentos;

    @FXML
    private TableColumn<ItemOrcamento, String> codigoColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> precoColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> fornecedorColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> produtoColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> quantidadeColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> acoesColumn;

    private ObservableList<ItemOrcamento> itemOrcamentoList;
    private final ItemOrcamentoService service;
    private final OrcamentoService orcamentoService;
    private Orcamento orcamento;

    public DetalheOrcamentoWindow() {
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
    }

    public void setAttributes(Orcamento orcamentoSelecionado) {
        this.orcamento = orcamentoSelecionado;
        populaCampos(orcamentoSelecionado);
        itemOrcamentoList = FXCollections.observableArrayList(service.listarPor(orcamento.getId()));;

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        precoColumn.setCellValueFactory(new PropertyValueFactory<>("preco"));
        fornecedorColumn.setCellValueFactory(new PropertyValueFactory<>("fornecedor.nome"));
        produtoColumn.setCellValueFactory(new PropertyValueFactory<>("produto.nome"));
        quantidadeColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        acoesColumn.setCellFactory(column -> new TableCell<>() {
            final Button editarButton = new Button("Detalhes");
            final Button excluirButton = new Button("Excluir");
            final HBox buttonBox = new HBox(editarButton, excluirButton);

            {
                buttonBox.setSpacing(10);
                excluirButton.setOnAction(event -> {
                    ItemOrcamento itemOrcamento = getTableView().getItems().get(getIndex());
                    service.excluirPor(itemOrcamento.getId());
                    itemOrcamentoList.remove(itemOrcamento);
                    tableItensOrcamentos.refresh();
                });
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(buttonBox);
                } else {
                    setGraphic(null);
                }
            }
        });
        tableItensOrcamentos.setItems(itemOrcamentoList);
    }

    private void populaCampos(Orcamento orcamentoSelecionado) {
        lblDetalhesDoOrcamentoPreencher.setText(orcamentoSelecionado.getId().toString());
        lblClientePreencher.setText(orcamentoSelecionado.getCliente().getNome());
        lblCustoEstimadoPreencher.setText(orcamentoSelecionado.getCustoEstimado().toString());
        lblDataDeCriacaoPreencher.setText(orcamentoSelecionado.getDataCriacao().toString());
        lblValorTotalPreencher.setText(orcamentoSelecionado.getValorTotal().toString());
        lblStatusPreencher.setText(orcamentoSelecionado.getStatus().toString());
        lblObservacoesPreencher.setText(orcamentoSelecionado.getObservaces());
    }


    @FXML
    public void onButtonEditarClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonVincularItemClicked(ActionEvent actionEvent) {
    }

    @FXML
    public void onButtonExcluirClicked(ActionEvent actionEvent) {
    }
}

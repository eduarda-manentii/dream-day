package com.br.dreamday.controller;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.utils.Formatter;
import com.br.dreamday.utils.WindowUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ClienteRelatorioController {

    @FXML
    private Button btnVoltar;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private TableColumn<ItemOrcamento, String> itensColumn;

    @FXML
    private Label lblCusto;

    @FXML
    private Label lblNomeCliente;

    @FXML
    private Pane painelRelatorio;

    @FXML
    private TableColumn<ItemOrcamento, String> qtdeColumn;

    @FXML
    private TableView<ItemOrcamento> table;

    @FXML
    private TableColumn<ItemOrcamento, String> totalUnitarioColumn;

    @FXML
    private TableColumn<ItemOrcamento, String> valorColumn;

    private final ClienteService clienteService;

    private final ItemOrcamentoService itemOrcamentoService;

    public ClienteRelatorioController() {
        clienteService = new ClienteService();
        itemOrcamentoService = new ItemOrcamentoService();
    }

    @FXML
    void initialize() {
       prepararComboClientes();
    }

    @FXML
    void aoClicarFiltrar() throws IOException {
        Cliente cliente = cbCliente.getValue();
        if (cliente == null) {
            WindowUtils.mostraMensagem("Selecione um cliente!");
        } else {
            geraRelatorio(cliente);
        }
    }

    @FXML
    void aoClicarVoltar() {
        Stage stage = (Stage) btnVoltar.getScene().getWindow();
        stage.close();  
    }

    private void prepararComboClientes() {
        ObservableList<Cliente> obClientes = FXCollections.observableArrayList(clienteService.listarTodos());
        obClientes.addFirst(null);
        cbCliente.setItems(obClientes);
    }

    private void geraRelatorio(Cliente cliente) {

        lblNomeCliente.setText(cliente.getNome());

        List<ItemOrcamento> itens = itemOrcamentoService.listarPorCliente(cliente.getId());

        double valorTotal = itens.stream()
                .mapToDouble(i ->
                        i.getQuantidade() * i.getItemFornecedor().getPreco().doubleValue())
                .sum();

        lblCusto.setText(Double.toString(valorTotal));

        ObservableList<ItemOrcamento> obItens = FXCollections.observableArrayList(itens);
        itensColumn.setCellValueFactory(param -> {
            ItemOrcamento item = param.getValue();
            return new SimpleStringProperty(item.getProduto().getNome());
        });
        valorColumn.setCellValueFactory(param -> {
            ItemOrcamento item = param.getValue();
            return new SimpleStringProperty("R$ " + Formatter.converteParaPtBr(item.getItemFornecedor().getPreco()));
        });
        qtdeColumn.setCellValueFactory(param -> {
            ItemOrcamento item = param.getValue();
            return new SimpleStringProperty(String.valueOf(item.getQuantidade()));
        });
        totalUnitarioColumn.setCellValueFactory(param -> {
            ItemOrcamento item = param.getValue();
            double valor = item.getQuantidade() * item.getItemFornecedor().getPreco().doubleValue();
            return new SimpleStringProperty("R$ " + Formatter.converteParaPtBr(valor));
        });
        table.setItems(obItens);
        painelRelatorio.setVisible(true);
    }

}

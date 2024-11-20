package com.br.dreamday.controller;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
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
    private Button btnFiltrar;

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

    private final OrcamentoService orcamentoService;

    private final ItemOrcamentoService itemOrcamentoService;

    public ClienteRelatorioController() {
        clienteService = new ClienteService();
        orcamentoService = new OrcamentoService();
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
        List<Orcamento> orcamentos = orcamentoService.listarPor(cliente.getNome());
        double valorTotal = 0;

        for (Orcamento orcamento : orcamentos) {
            valorTotal += orcamento.getCustoEstimado().doubleValue();
        }

        lblCusto.setText("R$ " + Formatter.converteParaPtBr(valorTotal));

        ObservableList<ItemOrcamento> itens = FXCollections.observableArrayList(itemOrcamentoService.listarPorCliente(cliente.getId()));

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
            return new SimpleStringProperty();
        });

        totalUnitarioColumn.setCellValueFactory(param -> {
            ItemOrcamento item = param.getValue();
            double valor = item.getQuantidade() * item.getItemFornecedor().getPreco().doubleValue();
            return new SimpleStringProperty("R$ " + Formatter.converteParaPtBr(valor));
        });

        table.setItems(itens);
        painelRelatorio.setVisible(true);
    }

}

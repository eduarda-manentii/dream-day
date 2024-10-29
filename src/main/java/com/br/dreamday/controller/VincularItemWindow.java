package com.br.dreamday.controller;

import com.br.dreamday.domain.*;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.MascarasFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class VincularItemWindow {

    @FXML
    private ComboBox<ItemFornecedor> cbItem;

    @FXML
    private ComboBox<ItemOrcamentoStatus> cbStatus;

    @FXML
    private TextField txtDadaDeEntrega;

    @FXML
    private TextField txtQuantidade;

    private Long orcamentoId;
    private Orcamento orcamento;
    private OrcamentoService orcamentoService;
    private ItemFornecedorService itemFornecedorService;
    private ItemOrcamentoService service;

    public VincularItemWindow() {
        this.service = new ItemOrcamentoService();
        this.orcamentoService = new OrcamentoService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    public void setOrcamentoId(Long orcamentoId) {
        this.orcamentoId = orcamentoId;
        carregarOrcamento();
    }

    private void carregarOrcamento() {
        this.orcamento = orcamentoService.buscarPor(orcamentoId);
        if (orcamento == null) {
            throw new RuntimeException("Orçamento não encontrado para o ID: " + orcamentoId);
        }
    }

    @FXML
    void onButtonConfirmarClicked(ActionEvent event) throws IOException {
        ItemFornecedor itemFornecedor = cbItem.getValue();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataEntrega = LocalDate.parse(txtDadaDeEntrega.getText(), formatter);
        ItemOrcamentoStatus status = cbStatus.getValue();
        Double quantidade = Double.parseDouble(txtQuantidade.getText());

        ItemOrcamento itemOrcamento = new ItemOrcamento(orcamento, itemFornecedor, dataEntrega, quantidade, status);
        service.salvar(itemOrcamento);
        BigDecimal subtotal = itemFornecedor.getPreco().multiply(new BigDecimal(quantidade));
        orcamentoService.atualizarValorTotal(orcamento.getId(), subtotal);
    }

    @FXML
    void initialize() throws ParseException {
        MascarasFX.mascaraData(txtDadaDeEntrega);
        MascarasFX.mascaraNumeroInteiro(txtQuantidade);
        txtDadaDeEntrega.setPromptText("dd/MM/yyyy");
        initializeDropDown();
    }

    private void initializeDropDown() {
        List<ItemOrcamentoStatus> status = Arrays.asList(ItemOrcamentoStatus.values());
        ObservableList<ItemOrcamentoStatus> obListStatus = FXCollections.observableArrayList(status);
        obListStatus.addFirst(null);
        cbStatus.setItems(obListStatus);

        List<ItemFornecedor> itensFornecedires = itemFornecedorService.listarTodos();
        ObservableList<ItemFornecedor> obListClientes = FXCollections.observableArrayList(itensFornecedires);
        obListClientes.addFirst(null);
        cbItem.setItems(obListClientes);
    }

    @FXML
    void onButtonCancelarClicked(ActionEvent event) throws IOException {

    }

}

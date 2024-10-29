package com.br.dreamday.controller;

import com.br.dreamday.domain.*;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.MascarasFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class VincularItemWindow {

    @FXML
    private ComboBox<ItemFornecedor> cbItem;

    @FXML
    private ComboBox<ItemOrcamentoStatus> cbStatus;

    @FXML
    private TextField txtDadaDeEntrega;

    private ItemFornecedorService itemFornecedorService;
   // private ItemOrcamentoService service;

    public VincularItemWindow() {
        // this.service = new OrcamentoService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    @FXML
    void initialize() throws ParseException {
        MascarasFX.mascaraData(txtDadaDeEntrega);
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
    void onButtonConfirmarClicked(ActionEvent event) throws IOException {

    }
    @FXML
    void onButtonCancelarClicked(ActionEvent event) throws IOException {

    }

}

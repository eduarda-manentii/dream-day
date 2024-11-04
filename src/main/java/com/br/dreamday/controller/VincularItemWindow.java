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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    private DetalheOrcamentoWindow parent;

    public VincularItemWindow() {
        this.service = new ItemOrcamentoService();
        this.orcamentoService = new OrcamentoService();
        this.itemFornecedorService = new ItemFornecedorService();
    }

    public void setParentController(DetalheOrcamentoWindow parent) {
        this.parent = parent;
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
        if (!validarCampos()) {
            return;
        }

        try {
            ItemFornecedor itemFornecedor = cbItem.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataEntrega = LocalDate.parse(txtDadaDeEntrega.getText(), formatter);
            ItemOrcamentoStatus status = cbStatus.getValue();
            double quantidade = Double.parseDouble(txtQuantidade.getText());

            if (quantidade <= 0) {
                showMessage("A quantidade deve ser maior que zero.");
                return;
            }

            ItemOrcamento itemOrcamento = new ItemOrcamento(orcamento, itemFornecedor, dataEntrega, quantidade, status);
            service.salvar(itemOrcamento);

            BigDecimal subtotal = itemFornecedor.getPreco().multiply(new BigDecimal(quantidade));
            Orcamento orcamentoAtualizado = orcamentoService.buscarPor(orcamentoId);
            BigDecimal valorTotal = orcamentoAtualizado.getValorTotal();
            BigDecimal totalAtualizado = valorTotal.add(subtotal);
            orcamentoService.atualizarValorTotal(orcamentoId, totalAtualizado);

            parent.atualizarCampoValorTotal(totalAtualizado.toString());
            showMessage("Item vinculado com sucesso!");
            limparCampos();

        } catch (Exception e) {
            showMessage("Erro ao vincular item: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (cbItem.getValue() == null) {
            showMessage("Selecione um item do fornecedor.");
            return false;
        }
        if (cbStatus.getValue() == null) {
            showMessage("Selecione o status do item.");
            return false;
        }
        if (txtDadaDeEntrega.getText().isBlank()) {
            showMessage("Informe a data de entrega.");
            return false;
        }
        if (txtQuantidade.getText().isBlank()) {
            showMessage("Informe a quantidade.");
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(txtDadaDeEntrega.getText(), formatter);
        } catch (Exception e) {
            showMessage("A data de entrega deve estar no formato dd/MM/yyyy.");
            return false;
        }
        try {
            double quantidade = Double.parseDouble(txtQuantidade.getText());
            if (quantidade <= 0) {
                showMessage("A quantidade deve ser maior que zero.");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("A quantidade deve ser um número válido.");
            return false;
        }

        return true;
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
        if (camposPreenchidos()) {
            confirmationMessage("Tem certeza que deseja cancelar a inserção?", () -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    private boolean camposPreenchidos() {
        String dataDeEntrega = txtDadaDeEntrega.getText();
        String quantidade = txtQuantidade.getText();
        return !dataDeEntrega.isBlank() || !quantidade.isBlank() ||
                cbItem.getValue() == null || cbStatus.getValue() == null;
    }

    void limparCampos() {
        txtDadaDeEntrega.setText("");
        txtQuantidade.setText("");
        cbItem.setValue(null);
        cbStatus.setValue(null);
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

    private void confirmationMessage(String mensagem, Runnable acao) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType btnYes = new ButtonType("Sim");
        ButtonType btnNo = new ButtonType("Não");
        dialog.setContentText(mensagem);
        dialog.getButtonTypes().setAll(btnYes, btnNo);
        dialog.showAndWait().ifPresent(b -> {
            if (b == btnYes) {
                acao.run();
            }
        });
    }

}

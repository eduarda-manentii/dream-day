package com.br.dreamday.controller;

import com.br.dreamday.domain.*;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.service.ParcelamentoService;
import com.br.dreamday.utils.MascarasUtils;
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
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.br.dreamday.utils.WindowUtils.confirmationMessage;
import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class VincularItemWindowController {

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
    private DetalheOrcamentoWindowController parent;
    private ItemOrcamento itemOrcamentoSelecionado;

    public VincularItemWindowController() {
        this.itemFornecedorService = new ItemFornecedorService();
        this.orcamentoService = new OrcamentoService();
        this.service = new ItemOrcamentoService();
    }

    @FXML
    void initialize() throws ParseException {
        MascarasUtils.mascaraData(txtDadaDeEntrega);
        MascarasUtils.mascaraNumeroInteiro(txtQuantidade);
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

    public void setParentController(DetalheOrcamentoWindowController parent) {
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
    void confirmar(ActionEvent event) throws IOException {
        try {
            ItemFornecedor itemFornecedor = cbItem.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataEntrega = LocalDate.parse(txtDadaDeEntrega.getText(), formatter);
            ItemOrcamentoStatus status = cbStatus.getValue();
            int quantidade = 0;
            String quantidadeText = txtQuantidade.getText();

            if (quantidadeText.trim().isEmpty()) {
                throw new IllegalArgumentException("Ocorreu um erro ao salvar as informações: A quantidade é obrigatória.");
            } else {
                quantidade = Integer.parseInt(quantidadeText);
            }

            if (itemOrcamentoSelecionado != null) {
                double quantidadeAntiga = itemOrcamentoSelecionado.getQuantidade();

                itemOrcamentoSelecionado.setItemFornecedor(itemFornecedor);
                itemOrcamentoSelecionado.setDataDeEntrega(dataEntrega);
                itemOrcamentoSelecionado.setStatus(status);
                itemOrcamentoSelecionado.setQuantidade(quantidade);
                service.salvar(itemOrcamentoSelecionado);

                BigDecimal subtotalAntigo = itemFornecedor.getPreco().multiply(new BigDecimal(quantidadeAntiga));
                BigDecimal subtotalNovo = itemFornecedor.getPreco().multiply(new BigDecimal(quantidade));

                Orcamento orcamentoAtualizado = orcamentoService.buscarPor(orcamentoId);
                BigDecimal valorTotal = orcamentoAtualizado.getValorTotal();
                BigDecimal totalAtualizado = valorTotal.subtract(subtotalAntigo).add(subtotalNovo);
                orcamentoService.atualizarValorTotal(orcamentoId, totalAtualizado);
                parent.atualizarCampoValorTotal(totalAtualizado.toString());
                itemOrcamentoSelecionado = null;
            } else {
                ItemOrcamento itemOrcamento = new ItemOrcamento(orcamento, itemFornecedor, dataEntrega, quantidade, status);
                service.salvar(itemOrcamento);

                BigDecimal subtotal = itemFornecedor.getPreco().multiply(new BigDecimal(quantidade));
                Orcamento orcamentoAtualizado = orcamentoService.buscarPor(orcamentoId);
                BigDecimal valorTotal = orcamentoAtualizado.getValorTotal();
                BigDecimal totalAtualizado = valorTotal.add(subtotal);
                orcamentoService.atualizarValorTotal(orcamentoId, totalAtualizado);

                parent.atualizarCampoValorTotal(totalAtualizado.toString());
                limparCampos();
            }
            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Confirmação de Salvamento",
                    "As alterações foram salvas com sucesso."
            );

        } catch (DateTimeException ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    "Ocorreu um erro ao salvar as informações:  Digite um valor para a data válido."
            );
        } catch (Exception e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Erro de Validação",
                    "Ocorreu um erro ao salvar as informações: " + e.getMessage()
            );
        }
    }

    @FXML
    void cancelar(ActionEvent event) throws IOException {
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

    public void setAttributes(ItemOrcamento itemOrcamentoSelecionado) {
        this.itemOrcamentoSelecionado = itemOrcamentoSelecionado;
        txtQuantidade.setText(String.valueOf(itemOrcamentoSelecionado.getQuantidade()));
        cbItem.setValue(itemOrcamentoSelecionado.getItemFornecedor());
        cbStatus.setValue(itemOrcamentoSelecionado.getStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtDadaDeEntrega.setText(itemOrcamentoSelecionado.getDataDeEntrega().format(formatter));
    }

    private boolean camposPreenchidos() {
        String dataDeEntrega = txtDadaDeEntrega.getText();
        String quantidade = txtQuantidade.getText();
        return !dataDeEntrega.isBlank() || !quantidade.isBlank() ||
                cbItem.getValue() == null || cbStatus.getValue() == null;
    }

    private void limparCampos() {
        txtDadaDeEntrega.setText("");
        txtQuantidade.setText("");
        cbItem.setValue(null);
        cbStatus.setValue(null);
    }

}

package com.br.dreamday.controller;

import com.br.dreamday.domain.*;
import com.br.dreamday.service.ItemFornecedorService;
import com.br.dreamday.service.ItemOrcamentoService;
import com.br.dreamday.service.OrcamentoService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static com.br.dreamday.utils.WindowUtils.confirmationMessage;

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

    public VincularItemWindowController() {
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
        double quantidade = Double.parseDouble(txtQuantidade.getText());

        ItemOrcamento itemOrcamento = new ItemOrcamento(orcamento, itemFornecedor, dataEntrega, quantidade, status);
        service.salvar(itemOrcamento);
        BigDecimal subtotal = itemFornecedor.getPreco().multiply(new BigDecimal(quantidade));
        orcamentoService.atualizarValorTotal(orcamento.getId(), subtotal);
        showMessage("Item vinculado com sucesso!");
        limparCampos();
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

        //TODO tem que ver essa parte porque não existe mais listarTodos() do itemFornecedor
        List<ItemFornecedor> itensFornecedores = itemFornecedorService.listarTodos();
        ObservableList<ItemFornecedor> obListClientes = FXCollections.observableArrayList(itensFornecedores);
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
}

package com.br.dreamday.controller;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.OrcamentoStatus;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.MascarasFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CadastroOrcamentoWindow {

    @FXML
    private ComboBox<OrcamentoStatus> cbStatus;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private TextField txtCustoEstimado;

    @FXML
    private TextArea txtAreaObservacoes;

    private OrcamentoService service;
    private ClienteService clienteService;

    public CadastroOrcamentoWindow() {
        this.service = new OrcamentoService();
        this.clienteService = new ClienteService();
    }

    @FXML
    void initialize() throws ParseException {
        MascarasFX.mascaraNumeroInteiro(txtCustoEstimado);
        initializeDropDown();
    }

    private void initializeDropDown() {
        List<OrcamentoStatus> status = Arrays.asList(OrcamentoStatus.values());
        ObservableList<OrcamentoStatus> obListStatus = FXCollections.observableArrayList(status);
        obListStatus.addFirst(null);
        cbStatus.setItems(obListStatus);

        List<Cliente> clientes = clienteService.listarTodos();
        ObservableList<Cliente> obListClientes = FXCollections.observableArrayList(clientes);
        obListClientes.addFirst(null);
        cbCliente.setItems(obListClientes);
    }

    @FXML
    void onButtonSalvarClicked() {
        try {
            Cliente cliente = cbCliente.getValue();
            BigDecimal custoEstimado = new BigDecimal(txtCustoEstimado.getText());
            OrcamentoStatus status = cbStatus.getValue();
            String observacoes = txtAreaObservacoes.getText();
            LocalDate dataDeCriacao = LocalDate.now();
            //TODO: listar itens de um orçamento para calcular valor total e passar para o parâmetro
            Orcamento orcamento = new Orcamento(cliente, status, dataDeCriacao, custoEstimado, custoEstimado, observacoes);
            service.salvar(orcamento);
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    @FXML
    void onButtonCancelarClicked(ActionEvent event) {

    }

    @FXML
    void onButtonVincularItemClicked(ActionEvent event) {

    }

    @FXML
    void onButtonVoltarClicked(ActionEvent event) {
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
        String custoEstimado = txtCustoEstimado.getText();
        return cbCliente.getValue() != null || !custoEstimado.isBlank() ||  cbStatus.getValue() != null;
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

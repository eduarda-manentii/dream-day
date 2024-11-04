package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.MascarasFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private Long orcamentoId;
    private Orcamento orcamentoSelecionado;

    public CadastroOrcamentoWindow() {
        this.orcamentoId = Long.valueOf(0);
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
        if (!validarCampos()) {
            return;
        }

        try {
            Cliente cliente = cbCliente.getValue();
            BigDecimal custoEstimado = new BigDecimal(txtCustoEstimado.getText());
            OrcamentoStatus status = cbStatus.getValue();
            String observacoes = txtAreaObservacoes.getText();
            LocalDate dataDeCriacao = LocalDate.now();

            if (orcamentoSelecionado == null) {
                Orcamento orcamento = new Orcamento(cliente, status, dataDeCriacao, custoEstimado, BigDecimal.ZERO, observacoes);
                orcamentoId = service.salvar(orcamento);
                showMessage("Orçamento salvo com sucesso!");
            } else {
                orcamentoSelecionado.setCliente(cliente);
                orcamentoSelecionado.setCustoEstimado(custoEstimado);
                orcamentoSelecionado.setDataCriacao(dataDeCriacao);
                orcamentoSelecionado.setStatus(status);
                orcamentoSelecionado.setObservaces(observacoes);
                orcamentoId = service.salvar(orcamentoSelecionado);
                orcamentoSelecionado = null;
                showMessage("Orçamento alterado com sucesso!");
            }
        } catch (Exception e) {
            showMessage("Erro ao salvar orçamento: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        if (cbCliente.getValue() == null) {
            showMessage("Selecione um cliente.");
            return false;
        }
        if (cbStatus.getValue() == null) {
            showMessage("Selecione o status do orçamento.");
            return false;
        }
        if (txtCustoEstimado.getText().isBlank()) {
            showMessage("Informe o custo estimado.");
            return false;
        }
        try {
            BigDecimal custoEstimado = new BigDecimal(txtCustoEstimado.getText());
            if (custoEstimado.compareTo(BigDecimal.ZERO) <= 0) {
                showMessage("O custo estimado deve ser um valor positivo.");
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("O custo estimado deve ser um número válido.");
            return false;
        }

        return true;
    }

    void abrirTelaVincularItem() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("vincular-item-window.fxml")));
        Parent parent = loader.load();
        VincularItemWindow controller = loader.getController();
        controller.setOrcamentoId(orcamentoId);
        Stage popupStage = new Stage();
        popupStage.setTitle("Vincular Item");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    @FXML
    void onButtonCancelarClicked(ActionEvent event) {
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

    @FXML
    void onButtonVincularItemClicked(ActionEvent event) throws IOException {
        if (orcamentoId == 0) {
            showMessage("Salve o orçamento antes de vincular um item.");
            return;
        }
        abrirTelaVincularItem();
    }

    public void setAttributes(Orcamento orcamentoSelecionado) {
        this.orcamentoSelecionado = orcamentoSelecionado;
        cbCliente.setValue(orcamentoSelecionado.getCliente());
        cbStatus.setValue(orcamentoSelecionado.getStatus());
        txtCustoEstimado.setText(orcamentoSelecionado.getCustoEstimado().toString());
        txtAreaObservacoes.setText(orcamentoSelecionado.getObservaces());
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

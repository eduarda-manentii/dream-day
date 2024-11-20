package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.*;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.service.OrcamentoService;
import com.br.dreamday.utils.MascarasUtils;
import com.br.dreamday.utils.WindowUtils;
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

import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class CadastroOrcamentoWindowController {

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
    private Orcamento orcamentoSelecionado;

    public CadastroOrcamentoWindowController() {
        this.service = new OrcamentoService();
        this.clienteService = new ClienteService();
    }

    @FXML
    void initialize() throws ParseException {
        MascarasUtils.mascaraNumero(txtCustoEstimado);
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
    void salvar() {
        try {
            Cliente cliente = cbCliente.getValue();
            BigDecimal custoEstimado = BigDecimal.ZERO;
            String custoEstimadoText = txtCustoEstimado.getText();
            OrcamentoStatus status = cbStatus.getValue();
            String observacoes = txtAreaObservacoes.getText();
            LocalDate dataDeCriacao = LocalDate.now();

            if (custoEstimadoText.trim().isEmpty()) {
                throw new IllegalArgumentException("Ocorreu um erro ao salvar as informações: O custo estimado é obrigatório.");
            }

            custoEstimadoText = custoEstimadoText.replace(",", ".");
            custoEstimado = new BigDecimal(custoEstimadoText);

            if (orcamentoSelecionado == null) {
                Orcamento orcamento = new Orcamento(cliente, status, dataDeCriacao, custoEstimado, BigDecimal.ZERO, observacoes);
                service.salvar(orcamento);
                limparCampor();
            } else {
                orcamentoSelecionado.setCliente(cliente);
                orcamentoSelecionado.setCustoEstimado(custoEstimado);
                orcamentoSelecionado.setDataCriacao(dataDeCriacao);
                orcamentoSelecionado.setStatus(status);
                orcamentoSelecionado.setObservaces(observacoes);
                service.salvar(orcamentoSelecionado);
                orcamentoSelecionado = null;
            }
            exibirAlerta(
                    Alert.AlertType.INFORMATION,
                    "Confirmação de Salvamento",
                    "As alterações foram salvas com sucesso. "
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
    void cancelar(ActionEvent event) {
        if (camposPreenchidos()) {
            WindowUtils.confirmationMessage("Tem certeza que deseja cancelar a inserção?", () -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            });
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void setAttributes(Orcamento orcamentoSelecionado) {
        this.orcamentoSelecionado = orcamentoSelecionado;
        cbCliente.setValue(orcamentoSelecionado.getCliente());
        cbStatus.setValue(orcamentoSelecionado.getStatus());
        txtAreaObservacoes.setText(orcamentoSelecionado.getObservaces());
        txtCustoEstimado.setText(formatarDecimal(orcamentoSelecionado.getCustoEstimado()));
    }

    private String formatarDecimal(BigDecimal valor) {
        return String.format("%.2f", valor);
    }

    private void limparCampor() {
        txtAreaObservacoes.setText("");
        txtCustoEstimado.setText("");
        cbCliente.setItems(null);
        cbStatus.setItems(null);
    }

    private boolean camposPreenchidos() {
        String custoEstimado = txtCustoEstimado.getText();
        return cbCliente.getValue() != null || !custoEstimado.isBlank() ||  cbStatus.getValue() != null;
    }

}

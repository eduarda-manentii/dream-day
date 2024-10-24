package com.example.dreamday.controller;

import com.example.dreamday.MainViewApplication;
import com.example.dreamday.domain.Cliente;
import com.example.dreamday.service.ClienteService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ConsultaClienteWindow {

    @FXML
    private TextField txtNomeDoCliente;

    @FXML
    private TableView<Cliente> tableCliente;

    @FXML
    private TableColumn<Cliente, String> codigoColumn;

    @FXML
    private TableColumn<Cliente, String> nomeColumn;

    @FXML
    private TableColumn<Cliente, String> conjugueColumn;

    @FXML
    private TableColumn<Cliente, String> dataCasamentoColumn;

    @FXML
    private TableColumn<Cliente, String> telefoneColumn;

    @FXML
    private TextField txtDataDoCasamento;

    private ObservableList<Cliente> clienteList;
    private final ClienteService service;

    public ConsultaClienteWindow() {
        this.service = new ClienteService();
    }

    @FXML
    public void initialize() {
        clienteList = FXCollections.observableArrayList(service.listarTodos());
        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        conjugueColumn.setCellValueFactory(new PropertyValueFactory<>("conjugue"));
        dataCasamentoColumn.setCellValueFactory(cellData -> {
            LocalDate dataCasamento = cellData.getValue().getDataCasamento();
            String formattedDate = dataCasamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new SimpleStringProperty(formattedDate);
        });
        telefoneColumn.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        tableCliente.setItems(clienteList);
        tableCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        });

        txtDataDoCasamento.setPromptText("dd/MM/yyyy");
        MascarasFX.mascaraData(txtDataDoCasamento);
    }

    @FXML
    public void onButtonAdicionarCLicked(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-cliente-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro Cliente");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
        recarregarTabela();
    }

    @FXML
    public void onButtonFiltrarClicked(ActionEvent actionEvent) {
        try {
            List<Cliente> clientes;
            if (!txtNomeDoCliente.getText().isBlank()) {
                clientes = service.listarPor(txtNomeDoCliente.getText());
            } else {
                clientes = service.listarTodos();
            }
            clienteList.clear();
            clienteList.addAll(clientes);
            tableCliente.setItems(clienteList);
            tableCliente.refresh();
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    @FXML
    public void onButtonEditarClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/dreamday/cadastro-cliente-window.fxml"));
        Parent root = loader.load();
        CadastroClienteWindowController clienteController = loader.getController();
        Cliente clienteSelecionado = tableCliente.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            warningMessage();
        } else {
            clienteController.setAttributes(clienteSelecionado);
            Scene scene = new Scene(root, 1500, 1500);
            Stage popup = new Stage();
            popup.setTitle("");
            popup.setScene(scene);
            popup.show();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            tableCliente.refresh();
        }
    }

    @FXML
    public void onButtonExcluirClicked(ActionEvent actionEvent) {
        Cliente clienteSelecionado = tableCliente.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {
            warningMessage();
        } else {
            confirmationMessage("Tem certeza que deseja remover o item selecionado?", () -> {
                service.excluirPor(clienteSelecionado.getId());
                recarregarTabela();
                tableCliente.refresh();
            });
        }
    }

    private void recarregarTabela() {
        clienteList.clear();
        clienteList.addAll(service.listarTodos());
    }

    private void showMessage(String mensagem) {
        ButtonType loginButtonType = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
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

    private void warningMessage() {
        ButtonType loginButtonType = new ButtonType("Ok!", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Aviso!");
        dialog.setContentText("Selecione um cliente.");
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        boolean disabled = false;
        dialog.getDialogPane().lookupButton(loginButtonType).setDisable(disabled);
        dialog.showAndWait();
    }
}

package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.Cliente;
import com.br.dreamday.service.ClienteService;
import com.br.dreamday.utils.MascarasUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.br.dreamday.utils.WindowUtils.deleteConfirmationMessage;
import static com.br.dreamday.utils.WindowUtils.exibirAlerta;

public class ConsultaClienteWindowController {

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

    public ConsultaClienteWindowController() {
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
        telefoneColumn.setCellValueFactory(cellData -> {
            String telefone = cellData.getValue().getTelefone();
            if (telefone != null && telefone.length() == 11) {
                telefone = String.format("(%s) %s-%s", telefone.substring(0, 2), telefone.substring(2, 6), telefone.substring(6));
            }
            return new SimpleStringProperty(telefone);
        });
        tableCliente.setItems(clienteList);
        tableCliente.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        });

        txtDataDoCasamento.setPromptText("dd/MM/yyyy");
        MascarasUtils.mascaraData(txtDataDoCasamento);
    }

    @FXML
    public void adicionar(ActionEvent actionEvent) throws IOException {
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
    public void filtrar(ActionEvent actionEvent) {
        try {
            List<Cliente> clientes;
            if (!txtDataDoCasamento.getText().isBlank() && !txtNomeDoCliente.getText().isBlank()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataDoCasamentoDT = LocalDate.parse(txtDataDoCasamento.getText(), formatter);
                clientes = service.listarPor(txtNomeDoCliente.getText(), dataDoCasamentoDT);
            } else if (!txtDataDoCasamento.getText().isBlank()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataDoCasamentoDT = LocalDate.parse(txtDataDoCasamento.getText(), formatter);
                clientes = service.listarPor("%", dataDoCasamentoDT);
            } else if (!txtNomeDoCliente.getText().isBlank()) {
                clientes = service.listarPor(txtNomeDoCliente.getText());
            } else {
                clientes = service.listarTodos();
            }
            clienteList.clear();
            clienteList.addAll(clientes);
            tableCliente.setItems(clienteList);
            tableCliente.refresh();
        }  catch (DateTimeException ex) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Filtrar Cliente",
                    null,
                    "Ocorreu um erro ao filtro o cliente: Digite um valor para a data válido "
            );
        } catch (Exception e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Filtrar Cliente",
                    null,
                    "Ocorreu um erro ao filtro o cliente: " + e.getMessage()
            );
        }
    }

    @FXML
    public void editar(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/br/dreamday/cadastro-cliente-window.fxml"));
        Parent root = loader.load();
        CadastroClienteWindowController clienteController = loader.getController();
        Cliente clienteSelecionado = tableCliente.getSelectionModel().getSelectedItem();
        if (clienteSelecionado == null) {

            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Seleção de Cliente",
                    null,
                    "É necessário selecionar um cliente para edição!"
            );
        } else {
            clienteController.setAttributes(clienteSelecionado);
            Scene scene = new Scene(root, 640, 400);

            Stage popup = new Stage();
            popup.setScene(scene);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.showAndWait();
            recarregarTabela();
        }
    }

    @FXML
    public void excluir(ActionEvent actionEvent) {
        try {
            Cliente clienteSelecionado = tableCliente.getSelectionModel().getSelectedItem();
            if (clienteSelecionado == null) {
                exibirAlerta(
                        Alert.AlertType.ERROR,
                        "Seleção de Cliente",
                        null,
                        "É necessário selecionar um cliente para exclusão!"
                );
            } else {
                deleteConfirmationMessage(() -> {
                    service.excluirPor(clienteSelecionado.getId());
                    recarregarTabela();
                    tableCliente.refresh();
                });
            }
        } catch (Exception e) {
            exibirAlerta(
                    Alert.AlertType.ERROR,
                    "Excluir Cliente",
                    null,
                    "Ocorreu um erro ao excluir o cliente: " + e.getMessage()
            );
        }
    }

    private void recarregarTabela() {
        clienteList.clear();
        clienteList.addAll(service.listarTodos());
    }

}

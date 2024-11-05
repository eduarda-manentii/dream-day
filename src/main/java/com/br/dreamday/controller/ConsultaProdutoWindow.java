package com.br.dreamday.controller;

import com.br.dreamday.MainViewApplication;
import com.br.dreamday.domain.Categoria;
import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.ProdutoService;
import com.br.dreamday.utils.Mensagens;
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
import java.util.List;
import java.util.Objects;

public class ConsultaProdutoWindow {

    @FXML
    private TableView<Produto> tableProduto;

    @FXML
    private TableColumn<Produto, String> codigoColumn;

    @FXML
    private TableColumn<Produto, String> nomeColumn;

    @FXML
    private TextField txtNomeFiltro;

    private ObservableList<Produto> produtoList;
    private final ProdutoService produtoService;

    public ConsultaProdutoWindow() {
        this.produtoService = new ProdutoService();
    }

    @FXML
    public void initialize() {
        produtoList = FXCollections.observableArrayList(produtoService.listarTodos());

        codigoColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tableProduto.setItems(produtoList);
        tableProduto.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

        });
    }

    @FXML
    void adicionar(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-produto-window.fxml")));
        Stage popupStage = new Stage();
        popupStage.setTitle("Cadastro Produto");
        Scene scene = new Scene(parent);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.setResizable(false);
        popupStage.showAndWait();
        recarregarTabela();
    }

    @FXML
    void editar(ActionEvent event) throws IOException {
        Produto produtoSelecionado = tableProduto.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            Mensagens.exibirMensagemDeAviso("É necessário selecionar um produto para edição!.");
        } else {

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainViewApplication.class.getResource("cadastro-produto-window.fxml")));
            Parent root = loader.load();
            CadastroProdutoWindow cadastroProdutoWindow = loader.getController();
            cadastroProdutoWindow.setAttributes(new Produto(
                    produtoSelecionado.getId(),
                    produtoSelecionado.getNome(),
                    produtoSelecionado.getDescricao()
            ));
            Stage popupStage = new Stage();
            popupStage.setTitle("Edição Produto");
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.centerOnScreen();
            popupStage.setResizable(false);
            popupStage.showAndWait();

            Produto produto = cadastroProdutoWindow.getProduto();
            int index = produtoList.indexOf(produtoSelecionado);
            produtoList.set(index, produto);

            tableProduto.refresh();
        }
    }

    @FXML
    void excluir(ActionEvent event) {
        Produto produtoSelecionado = tableProduto.getSelectionModel().getSelectedItem();
        if (produtoSelecionado == null) {
            Mensagens.exibirMensagemDeAviso("É necessário selecionar um produto para excluir!.");
        } else {
            Mensagens.exibirMensagemDeConfirmacao("Tem certeza que deseja remover?", () -> {
                produtoService.excluirPor(produtoSelecionado.getId());
                produtoList.remove(produtoSelecionado);
                tableProduto.refresh();
            });
        }
    }

    @FXML
    void filtrar(ActionEvent event) {
        List<Produto> produtos;

        if (!txtNomeFiltro.getText().isBlank()) {
            produtos = produtoService.listarPor(txtNomeFiltro.getText());
        } else {
            produtos = produtoService.listarTodos();
        }

        produtoList.clear();
        produtoList.addAll(produtos);
        tableProduto.setItems(produtoList);
        tableProduto.refresh();
    }

    @FXML
    void mostrarCadastroCategoria(ActionEvent event) {

    }

    @FXML
    void mostrarCadastroProduto(ActionEvent event) {

    }

    public void recarregarTabela() {
        produtoList.clear();
        produtoList.addAll(produtoService.listarTodos());
    }

}

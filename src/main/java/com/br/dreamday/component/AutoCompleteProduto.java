package com.br.dreamday.component;

import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.ProdutoService;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;

public class AutoCompleteProduto extends TextField {

    private final ContextMenu entriesPopup;
    private Produto produtoSelecionado;
    private static final int LIMITADOR = 6;

    public AutoCompleteProduto(ProdutoService produtoService) {
        super();
        entriesPopup = new ContextMenu();

        textProperty().addListener((observableValue, oldText, newText) -> {
            if (newText.length() < 3) {
                entriesPopup.hide();
            } else {
                List<Produto> resultadosEncontrados = produtoService.listarPor(newText, LIMITADOR);
                if (!resultadosEncontrados.isEmpty()) {
                    popularPopup(resultadosEncontrados);
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(AutoCompleteProduto.this, Side.BOTTOM, 0, 0);
                    }
                } else {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> entriesPopup.hide());
    }

    public Produto getProdutoSelecionado() {
        return this.produtoSelecionado;
    }

    public void setProdutoSelecionado(Produto produto) {
        setText(produto.getNome());
        this.produtoSelecionado = produto;
    }

    public void limparAutoComplete() {
        this.produtoSelecionado = null;
        setText("");
    }

    private void popularPopup(List<Produto> resultadosEncontrados) {
        entriesPopup.getItems().clear();
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(resultadosEncontrados.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final Produto produto = resultadosEncontrados.get(i);
            Label entryLabel = new Label(produto.getNome());
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent -> {
                setText(produto.getNome());
                produtoSelecionado = produto;
                entriesPopup.hide();
            });
            menuItems.add(item);
        }

        entriesPopup.getItems().addAll(menuItems);
    }
}

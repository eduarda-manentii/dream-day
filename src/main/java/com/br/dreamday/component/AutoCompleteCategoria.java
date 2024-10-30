package com.br.dreamday.component;

import com.br.dreamday.domain.Categoria;
import com.br.dreamday.domain.Produto;
import com.br.dreamday.service.CategoriaService;
import com.br.dreamday.service.ProdutoService;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;

public class AutoCompleteCategoria extends TextField {

    private final ContextMenu entriesPopup;
    private Categoria categoriaSelecionada;
    private static final int LIMITADOR = 6;

    public AutoCompleteCategoria(CategoriaService categoriaService) {
        super();
        entriesPopup = new ContextMenu();

        textProperty().addListener((observableValue, oldText, newText) -> {
            if (newText.length() < 3) {
                entriesPopup.hide();
            } else {
                List<Categoria> resultadosEncontrados = categoriaService.listarPor(newText, LIMITADOR);
                if (!resultadosEncontrados.isEmpty()) {
                    popularPopup(resultadosEncontrados);
                    if (!entriesPopup.isShowing()) {
                        entriesPopup.show(AutoCompleteCategoria.this, Side.BOTTOM, 0, 0);
                    }
                } else {
                    entriesPopup.hide();
                }
            }
        });

        focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> entriesPopup.hide());
    }

    public Categoria getCategoriaSelecionada() {
        return this.categoriaSelecionada;
    }

    public void setCategoriaSelecionado(Categoria categoria) {
        setText(categoria.getNome());
        this.categoriaSelecionada = categoria;
    }

    public void limparAutoComplete() {
        this.categoriaSelecionada = null;
    }

    private void popularPopup(List<Categoria> resultadosEncontrados) {
        entriesPopup.getItems().clear();
        List<CustomMenuItem> menuItems = new LinkedList<>();
        int maxEntries = 10;
        int count = Math.min(resultadosEncontrados.size(), maxEntries);
        for (int i = 0; i < count; i++) {
            final Categoria categoria = resultadosEncontrados.get(i);
            Label entryLabel = new Label(categoria.getNome());
            CustomMenuItem item = new CustomMenuItem(entryLabel, true);
            item.setOnAction(actionEvent -> {
                setText(categoria.getNome());
                categoriaSelecionada = categoria;
                entriesPopup.hide();
            });
            menuItems.add(item);
        }

        entriesPopup.getItems().addAll(menuItems);
    }
}

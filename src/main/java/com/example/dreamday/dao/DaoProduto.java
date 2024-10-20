package com.example.dreamday.dao;

import com.example.dreamday.domain.Categoria;
import com.example.dreamday.domain.Produto;

import java.util.List;

public interface DaoProduto {

    void inserir(Produto produto);
    void alterar(Produto  produto);
    void excluirPor(Long id);
    List<Produto> listarPor(String nome);
    List<Produto> listarTodos();
}

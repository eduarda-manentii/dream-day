package com.br.dreamday.dao;

import com.br.dreamday.domain.Produto;

import java.util.List;

public interface DaoProduto {

    void inserir(Produto produto);
    void alterar(Produto  produto);
    void excluirPor(Long id);
    List<Produto> listarPor(String nome);
    List<Produto> listarTodos();
}

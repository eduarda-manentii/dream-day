package com.br.dreamday.dao;

import com.br.dreamday.domain.Categoria;

import java.util.List;

public interface DaoCategoria {

    void inserir(Categoria categoria);
    void alterar(Categoria  categoria);
    void excluirPor(Long id);
    List<Categoria> listarPor(String nome);
    List<Categoria> listarTodas();
}

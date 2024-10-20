package com.example.dreamday.dao;

import com.example.dreamday.domain.Categoria;

import java.util.List;

public interface DaoCategoria {

    void inserir(Categoria categoria);
    void alterar(Categoria  categoria);
    void excluirPor(Integer id);
    List<Categoria> listarPor(String nome);
    List<Categoria> listarTodas();
}

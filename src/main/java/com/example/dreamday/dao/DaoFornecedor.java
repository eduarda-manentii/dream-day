package com.example.dreamday.dao;

import com.example.dreamday.domain.Categoria;
import com.example.dreamday.domain.Fornecedor;

import java.util.List;

public interface DaoFornecedor {

    void inserir(Fornecedor fornecedor);
    void alterar(Fornecedor  fornecedor);
    void excluirPor(Integer id);
    List<Fornecedor> listarPor(String nome);
    List<Fornecedor> listarTodos();
}

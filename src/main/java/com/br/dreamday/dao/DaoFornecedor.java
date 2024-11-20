package com.br.dreamday.dao;

import com.br.dreamday.domain.Fornecedor;

import java.util.List;

public interface DaoFornecedor {

    void inserir(Fornecedor fornecedor);
    void alterar(Fornecedor  fornecedor);
    void excluirPor(Long id);
    List<Fornecedor> listarPor(String nome);
    List<Fornecedor> listarTodos();

}

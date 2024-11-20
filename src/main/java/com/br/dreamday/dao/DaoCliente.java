package com.br.dreamday.dao;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.Orcamento;

import java.time.LocalDate;
import java.util.List;

public interface DaoCliente {

    void inserir(Cliente cliente);
    void alterar(Cliente cliente);
    void excluirPor(int id);
    Cliente buscarPor(int id);
    List<Cliente> listarPor(String nome);
    List<Cliente> listarPor(String nome, LocalDate dataDeCasamento);
    List<Cliente> listarTodos();
    List<Cliente> listarPor(LocalDate data);
}

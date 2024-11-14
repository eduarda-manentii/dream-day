package com.br.dreamday.dao;

import com.br.dreamday.domain.Cliente;

import java.time.LocalDate;
import java.util.List;

public interface DaoCliente {

    public void inserir(Cliente cliente);

    public void alterar(Cliente cliente);

    public void excluirPor(int id);

    public Cliente buscarPor(int id);

    public List<Cliente> listarPor(String nome);

    public List<Cliente> listarPor(String nome, LocalDate dataDeCasamento);

    public List<Cliente> listarTodos();
}

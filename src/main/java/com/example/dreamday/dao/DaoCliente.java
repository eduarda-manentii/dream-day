package com.example.dreamday.dao;

import com.example.dreamday.domain.Cliente;

import java.util.List;

public interface DaoCliente {

    public void inserir(Cliente cliente);

    public void alterar(Cliente cliente);

    public void excluirPor(int id);

    public Cliente buscarPor(int id);

    public List<Cliente> listarPor(String nome);

    List<Cliente> listarTodos();
}

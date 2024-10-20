package com.example.dreamday.dao;

import com.example.dreamday.domain.Orcamento;

import java.util.List;

public interface DaoOrcamento {

    public void inserir(Orcamento orcamento);

    public void alterar(Orcamento orcamento);

    public void excluirPor(int id);

    public Orcamento buscarPor(int id);

    public List<Orcamento> listarPor(String nome);

}

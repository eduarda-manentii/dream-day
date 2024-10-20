package com.example.dreamday.dao;

import com.example.dreamday.domain.Orcamento;

public interface DaoOrcamento {

    public void inserir(Orcamento orcamento);

    public void alterar(Orcamento orcamento);

    public void excluirPor(Long id);

    public Orcamento buscarPor(Long id);

}

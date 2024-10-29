package com.br.dreamday.dao;

import com.br.dreamday.domain.Orcamento;

import java.math.BigDecimal;

public interface DaoOrcamento {

    public void inserir(Orcamento orcamento);

    public void alterar(Orcamento orcamento);

    public void excluirPor(Long id);

    public Orcamento buscarPor(Long id);

    public void atualizarValorTotal(Long idOrcamento, BigDecimal subtotal);

}

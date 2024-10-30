package com.br.dreamday.dao;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.Orcamento;

import java.math.BigDecimal;
import java.util.List;

public interface DaoOrcamento {

    public Long inserir(Orcamento orcamento);

    public void alterar(Orcamento orcamento);

    public void excluirPor(Long id);

    public Orcamento buscarPor(Long id);

    public void atualizarValorTotal(Long idOrcamento, BigDecimal subtotal);

    public List<Orcamento> listarTodos();

}

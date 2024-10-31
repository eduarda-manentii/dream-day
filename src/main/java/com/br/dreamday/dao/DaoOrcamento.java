package com.br.dreamday.dao;

import com.br.dreamday.domain.ItemOrcamentoStatus;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.OrcamentoStatus;

import java.math.BigDecimal;
import java.util.List;

public interface DaoOrcamento {

    public Long inserir(Orcamento orcamento);

    public void alterar(Orcamento orcamento);

    public void excluirPor(Long id);

    public Orcamento buscarPor(Long id);

    public void atualizarValorTotal(Long idOrcamento, BigDecimal subtotal);

    public List<Orcamento> listarPor(String nomeDoCliente, OrcamentoStatus status);

    public List<Orcamento> listarPor(OrcamentoStatus status);

    public List<Orcamento> listarPor(String nomeDoCliente);

    public List<Orcamento> listarTodos();

}

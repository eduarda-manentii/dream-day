package com.br.dreamday.dao;

import com.br.dreamday.domain.ItemOrcamentoStatus;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.OrcamentoStatus;

import java.math.BigDecimal;
import java.util.List;

public interface DaoOrcamento {

    Long inserir(Orcamento orcamento);
    void alterar(Orcamento orcamento);
    void excluirPor(Long id);
    Orcamento buscarPor(Long id);
    void atualizarValorTotal(Long idOrcamento, BigDecimal subtotal);
    List<Orcamento> listarPor(String nomeDoCliente, OrcamentoStatus status);
    List<Orcamento> listarPor(OrcamentoStatus status);
    List<Orcamento> listarPor(String nomeDoCliente);
    List<Orcamento> listarTodos();

}

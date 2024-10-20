package com.example.dreamday.service;

import com.example.dreamday.dao.DaoOrcamento;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.Orcamento;

import java.math.BigDecimal;
import java.util.List;

public class OrcamentoService {

    private DaoOrcamento dao;

    public OrcamentoService() {
        this.dao = FactoryDao.getInstance().getDaoOrcamento();
    }

    public void salvar(Orcamento orcamento) {
        validar(orcamento);
        boolean isJaInserido = orcamento.getId() != null && orcamento.getId() > 0;
        if (isJaInserido) {
            this.dao.alterar(orcamento);
        } else {
            this.dao.inserir(orcamento);
        }
    }

    private void validar(Orcamento orcamento) {
        if (orcamento == null) {
            throw new NullPointerException("O orçamento não pode ser nulo");
        }

        if (orcamento.getCliente() == null) {
            throw new NullPointerException("O cliente do orçamento não pode ser nulo");
        }

        if (orcamento.getStatus() == null) {
            throw new NullPointerException("O status do orçamento não pode ser nulo");
        }

        if (orcamento.getDataCriacao() == null) {
            throw new NullPointerException("A data de criação não pode ser nula");
        }

        if (orcamento.getCustoEstimado() == null || orcamento.getCustoEstimado().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O custo estimado deve ser maior ou igual a zero");
        }

        if (orcamento.getValorTotal() == null || orcamento.getValorTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor total deve ser maior ou igual a zero");
        }
    }

    public void excluirPor(Long id) {
        if (id != null && id > 0) {
            this.dao.excluirPor(id);
        } else {
            throw new IllegalArgumentException("O id para exclusão deve ser maior que zero");
        }
    }

    public Orcamento buscarPor(Long id) {
        if (id != null && id > 0) {
            Orcamento orcamentoEncontrado = this.dao.buscarPor(id);
            if (orcamentoEncontrado == null) {
                throw new IllegalArgumentException("Não existe orçamento vinculado ao id informado");
            }
            return orcamentoEncontrado;
        } else {
            throw new IllegalArgumentException("O id para busca não pode ser menor que zero");
        }
    }

}

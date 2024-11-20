package com.br.dreamday.service;

import com.br.dreamday.dao.DaoOrcamento;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.OrcamentoStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrcamentoService {

    private DaoOrcamento dao;
    private ItemOrcamentoService service;

    public OrcamentoService() {
        this.dao = FactoryDao.getInstance().getDaoOrcamento();
        this.service = new ItemOrcamentoService();
    }

    public void salvar(Orcamento orcamento) {
        this.validar(orcamento);
        boolean isJaInserido = orcamento.getId() != null && orcamento.getId() > 0;
        if (isJaInserido) {
            this.dao.alterar(orcamento);
        } else {
            Long novoId = this.dao.inserir(orcamento);
            orcamento.setId(novoId);
        }
    }

    private void validar(Orcamento orcamento) {
        if (orcamento == null) {
            throw new IllegalArgumentException("O orçamento não pode ser nulo");
        }

        if (orcamento.getCliente() == null) {
            throw new IllegalArgumentException("O cliente do orçamento não pode ser nulo");
        }

        if (orcamento.getStatus() == null) {
            throw new IllegalArgumentException("O status do orçamento não pode ser nulo");
        }

        if (orcamento.getDataCriacao() == null) {
            throw new IllegalArgumentException("A data de criação não pode ser nula");
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

    public void excluirOrcamentoEItensVinculados(Long orcamentoId) {
        service.excluirItensPorOrcamentoId(orcamentoId);
        excluirPor(orcamentoId);
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

    public void atualizarValorTotal(Long idOrcamento, BigDecimal subtotal) {
        dao.atualizarValorTotal(idOrcamento, subtotal);
    }

    public List<Orcamento> listarPor(String nomeDoCliente, OrcamentoStatus status) {
        return dao.listarPor(nomeDoCliente, status);
    }

    public List<Orcamento> listarPor(OrcamentoStatus status) {
        return dao.listarPor(status);
    }

    public List<Orcamento> listarPor(String nomeDoCliente) {
        return dao.listarPor(nomeDoCliente);
    }

    public List<Orcamento> listarTodos() {
        return this.dao.listarTodos();
    }

}

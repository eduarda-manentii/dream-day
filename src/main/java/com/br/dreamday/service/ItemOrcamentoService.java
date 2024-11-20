package com.br.dreamday.service;

import com.br.dreamday.dao.DaoItemOrcamento;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.key.ItemFornecedorKey;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ItemOrcamentoService {

    private DaoItemOrcamento dao;

    public ItemOrcamentoService() {
        this.dao = FactoryDao.getInstance().getDaoItemOrcamento();
    }

    public void salvar(ItemOrcamento itemOrcamento) {
        this.validar(itemOrcamento);
        boolean isJaInserido = itemOrcamento.getId() != null && itemOrcamento.getId() > 0;
        if (isJaInserido) {
            this.dao.alterar(itemOrcamento);
        } else {
            this.dao.inserir(itemOrcamento);
        }
    }
    private void validar(ItemOrcamento itemOrcamento) {
        if (itemOrcamento == null) {
            throw new IllegalArgumentException("O Item de Orçamento não pode ser nulo");
        }

        if (itemOrcamento.getItemFornecedor() == null) {
            throw new IllegalArgumentException("O item do fornecedor do orçamento não pode ser nulo");
        }

        if (itemOrcamento.getStatus() == null) {
            throw new IllegalArgumentException("O status do item do orçamento não pode ser nulo");
        }

        LocalDate dataAtual = LocalDate.now();
        if (itemOrcamento.getDataDeEntrega() == null || itemOrcamento.getDataDeEntrega().isBefore(dataAtual)) {
            throw new IllegalArgumentException("A data de entrega não pode ser nula e não pode ser anterior ao dia atual.");
        }

        if (itemOrcamento.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade estimado deve ser maior ou igual a zero");
        }
    }

    public void excluirPor(Long id) {
        if (id == null) {
            throw new NullPointerException("O id do item de orçamento é obrigatório!");
        }
        this.dao.excluirPor(id);
    }

    public void excluirItensPorOrcamentoId(Long orcamentoId) {
        List<ItemOrcamento> itens = listarPor(orcamentoId);
        for (ItemOrcamento item : itens) {
            excluirPor(item.getId());
        }
    }

    public List<ItemOrcamento> listarPor(Long id) {
        return dao.listarPor(id);
    }

    public List<ItemOrcamento> listarTodos() {
        return dao.listarTodos();
    }

    public List<ItemOrcamento> listarPorCliente(Long clienteId) {
        return dao.listarPorCliente(clienteId);
    }

}

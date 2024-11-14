package com.br.dreamday.service;

import com.br.dreamday.dao.DaoItemOrcamento;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.domain.key.ItemFornecedorKey;

import java.math.BigDecimal;
import java.util.List;

public class ItemOrcamentoService {

    private DaoItemOrcamento dao;

    public ItemOrcamentoService() {
        this.dao = FactoryDao.getInstance().getDaoItemOrcamento();
    }

    public void salvar(ItemOrcamento itemOrcamento) {
        boolean isJaInserido = itemOrcamento.getId() != null && itemOrcamento.getId() > 0;
        if (isJaInserido) {
            this.dao.alterar(itemOrcamento);
        } else {
            this.dao.inserir(itemOrcamento);
        }
    }

    public void excluirPor(Long id) {
        if (id == null) {
            throw new NullPointerException("O id do item de orçamento é obrigatório!");
        }
        this.dao.excluirPor(id);
    }


    public List<ItemOrcamento> listarPor(Long id) {
        return dao.listarPor(id);
    }

    public List<ItemOrcamento> listarTodos() {
        return dao.listarTodos();
    }

}

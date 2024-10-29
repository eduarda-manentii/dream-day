package com.br.dreamday.service;

import com.br.dreamday.dao.DaoItemOrcamento;
import com.br.dreamday.dao.DaoOrcamento;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.ItemOrcamento;
import com.br.dreamday.domain.Orcamento;

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

}

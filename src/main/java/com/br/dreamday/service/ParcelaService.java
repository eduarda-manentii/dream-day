package com.br.dreamday.service;

import com.br.dreamday.dao.DaoParcela;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.Parcela;

import java.util.List;

public class ParcelaService {

    private DaoParcela dao;

    public ParcelaService() {
        this.dao = FactoryDao.getInstance().getDaoParcela();
    }

    public void salvar(Parcela parcela) {
        if (parcela.getId() != null) {
            dao.alterar(parcela);
            return;
        }
        dao.inserir(parcela);
    }

    public Parcela buscarPor(Long id) {
        if (id == null) {
            throw new NullPointerException("Id é obrigatório");
        }
        Parcela parcela = dao.buscarPor(id);
        if (parcela == null) {
            throw new NullPointerException("Não foi encontrado nenhuma paracela para o id fornecido");
        }
        return parcela;
    }

    public Parcela excluirPor(Long id) {
        Parcela parcela = buscarPor(id);
        dao.excluirPor(id);
        return parcela;
    }

    public List<Parcela> listarPorParcelamento(Long parcelamentoId) {
        return dao.listarPor(parcelamentoId);
    }

}

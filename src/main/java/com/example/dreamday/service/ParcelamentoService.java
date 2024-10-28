package com.example.dreamday.service;

import com.example.dreamday.dao.DaoParcelamento;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.Parcelamento;

public class ParcelamentoService {

    private DaoParcelamento dao;

    public ParcelamentoService() {
        this.dao = FactoryDao.getInstance().getDaoParcelamento();
    }

    public void salvar(Parcelamento parcelamento) {
        if (parcelamento.getId() != null) {
            dao.alterar(parcelamento);
            return;
        }
        dao.inserir(parcelamento);
    }

    public Parcelamento buscarPor(Long id) {
        if (id == null) {
            throw new NullPointerException("Id é obrigatório");
        }
        Parcelamento parcelamento = dao.buscarPor(id);
        if (parcelamento == null) {
            throw new NullPointerException("Não foi encontrado nenhum paracelamento para o id fornecido");
        }
        return parcelamento;
    }

    public Parcelamento excluirPor(Long id) {
        Parcelamento parcelamento = buscarPor(id);
        dao.excluirPor(id);
        return parcelamento;
    }

}

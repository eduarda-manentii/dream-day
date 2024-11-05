package com.br.dreamday.service;


import com.br.dreamday.dao.DaoParcelamento;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.Parcelamento;

public class ParcelamentoService {

    private DaoParcelamento dao;

    public ParcelamentoService() {
        this.dao = FactoryDao.getInstance().getDaoParcelamento();
    }

    public Long salvar(Parcelamento parcelamento) {
        if (parcelamento.getId() != null) {
            dao.alterar(parcelamento);
            return parcelamento.getId();
        }
        return dao.inserir(parcelamento);
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

    public boolean isParcelamentoExistentePeloOrcamento(Long idOrcamento) {
        return dao.possuiParcelamento(idOrcamento);
    }

}

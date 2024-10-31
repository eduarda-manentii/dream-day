package com.br.dreamday.dao;

import com.br.dreamday.domain.Parcelamento;

public interface DaoParcelamento {

    void inserir(Parcelamento parcelamento);

    void alterar(Parcelamento parcelamento);

    void excluirPor(Long id);

    Parcelamento buscarPor(Long id);

}

package com.example.dreamday.dao;

import com.example.dreamday.domain.Parcelamento;

public interface DaoParcelamento {

    void inserir(Parcelamento parcelamento);

    void alterar(Parcelamento parcelamento);

    void excluirPor(Long id);

    Parcelamento buscarPor(Long id);

}

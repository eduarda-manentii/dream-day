package com.br.dreamday.dao;

import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.Parcelamento;

public interface DaoParcelamento {

    Long inserir(Parcelamento parcelamento);

    void alterar(Parcelamento parcelamento);

    void excluirPor(Long id);

    Parcelamento buscarPor(Long id);

    boolean possuiParcelamento(Long id);

}

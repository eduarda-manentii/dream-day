package com.br.dreamday.dao;

import com.br.dreamday.domain.Parcela;

public interface DaoParcela {

    void inserir(Parcela parcela);

    void alterar(Parcela parcela);

    void excluirPor(Long id);

    Parcela buscarPor(Long id);

}

package com.br.dreamday.dao;

import com.br.dreamday.dao.postgres.*;

public class FactoryDao {

    private static FactoryDao instance;

    private FactoryDao() {}

    public DaoCliente getDaoCliente() {
        return new DaoPostgresCliente();
    }

    public  DaoOrcamento getDaoOrcamento() {
        return new DaoPostgresOrcamento();
    }

    public DaoCategoria getDaoCategoria() {
        return new DaoPostgresqlCategoria();
    }

    public DaoFornecedor getDaoFornecedor() {
        return new DaoPostgresqlFornecedor();
    }

    public DaoProduto getDaoProduto() {
        return new DaoPostgresqlProduto();
    }

    public DaoItemFornecedor getDaoItemFornecedor() {
        return new DaoPostgresqlItemFornecedor();
    }

    public DaoItemOrcamento getDaoItemOrcamento() {
        return new DaoPostgresItemOrcamento();
    }

    public static FactoryDao getInstance() {
        if (instance == null) {
            instance = new FactoryDao();
        }
        return instance;
    }

    public DaoParcelamento getDaoParcelamento() {
        return new DaoPostgresParcelamento();
    }

}

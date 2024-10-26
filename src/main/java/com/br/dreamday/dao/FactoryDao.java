package com.br.dreamday.dao;

import com.br.dreamday.dao.postgres.DaoPostgresqlCategoria;
import com.br.dreamday.dao.postgres.DaoPostgresqlFornecedor;
import com.br.dreamday.dao.postgres.DaoPostgresqlItemFornecedor;
import com.br.dreamday.dao.postgres.DaoPostgresqlProduto;

public class FactoryDao {


    private static FactoryDao instance;

    private FactoryDao() {}

    public static FactoryDao getInstance() {
        if (instance == null) {
            instance = new FactoryDao();
        }
        return instance;
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
}

package com.example.dreamday.dao;

import com.example.dreamday.dao.postgres.DaoPostgresqlCategoria;
import com.example.dreamday.dao.postgres.DaoPostgresqlFornecedor;
import com.example.dreamday.dao.postgres.DaoPostgresqlProduto;

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
}

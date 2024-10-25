package com.br.dreamday.dao;

import com.br.dreamday.dao.postgres.DaoPostgresCliente;
import com.br.dreamday.dao.postgres.DaoPostgresOrcamento;

public class FactoryDao {


    private static FactoryDao instance;

    private FactoryDao() {}

    public DaoCliente getDaoCliente() {
        return new DaoPostgresCliente();
    }

    public  DaoOrcamento getDaoOrcamento() {
        return new DaoPostgresOrcamento();
    }

    public static FactoryDao getInstance() {
        if (instance == null) {
            instance = new FactoryDao();
        }
        return instance;
    }
}

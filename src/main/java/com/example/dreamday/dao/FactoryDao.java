package com.example.dreamday.dao;

public class FactoryDao {


    private static FactoryDao instance;

    private FactoryDao() {}

    public static FactoryDao getInstance() {
        if (instance == null) {
            instance = new FactoryDao();
        }
        return instance;
    }
}

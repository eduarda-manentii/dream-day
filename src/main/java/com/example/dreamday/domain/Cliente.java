package com.example.dreamday.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

public class Cliente {

    private Long id;
    private String nome;
    private String conjugue;
    private LocalDate dataCasamento;
    private String telefone;
    private String email;
    private String cpf;

    public Cliente(String nome, String conjugue, LocalDate dataCasamento,
            String telefone, String email, String cpf) {
        this.nome = nome;
        this.conjugue = conjugue;
        this.dataCasamento = dataCasamento;
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
    }

    public Cliente(Long id, String nome, String conjugue, LocalDate dataCasamento, String telefone, String email, String cpf) {
        this.id = id;
        this.nome = nome;
        this.conjugue = conjugue;
        this.dataCasamento = dataCasamento;
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getConjugue() {
        return conjugue;
    }

    public LocalDate getDataCasamento() {
        return dataCasamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setConjugue(String conjugue) {
        this.conjugue = conjugue;
    }

    public void setDataCasamento(LocalDate dataCasamento) {
        this.dataCasamento = dataCasamento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return nome;
    }

}

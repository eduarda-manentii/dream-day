package com.br.dreamday.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Parcela {

    private Long id;

    private BigDecimal valor;

    private String observacao;

    private Parcelamento parcelamento;

    public Parcela() {
    }

    public Parcela(Long id, BigDecimal valor, String observacao, Parcelamento parcelamento) {
        this.id = id;
        this.valor = valor;
        this.observacao = observacao;
        this.parcelamento = parcelamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcela parcela = (Parcela) o;
        return Objects.equals(id, parcela.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Parcelamento getParcelamento() {
        return parcelamento;
    }

    public void setParcelamento(Parcelamento parcelamento) {
        this.parcelamento = parcelamento;
    }

    @Override
    public String toString() {
        return "Parcela{" +
                "id=" + id +
                ", valor=" + valor +
                ", observacao='" + observacao + '\'' +
                ", parcelamento=" + parcelamento +
                '}';
    }
}





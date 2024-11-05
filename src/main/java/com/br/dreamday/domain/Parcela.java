package com.br.dreamday.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Parcela {

    private Long id;

    private BigDecimal valor;

    private ParcelaStatus parcelaStatus;

    private Parcelamento parcelamento;

    public Parcela() {
    }

    public Parcela(Long id, BigDecimal valor, ParcelaStatus parcelaStatus, Parcelamento parcelamento) {
        this.id = id;
        this.valor = valor;
        this.parcelaStatus = parcelaStatus;
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

    public ParcelaStatus getParcelaStatus() {
        return parcelaStatus;
    }

    public void setParcelaStatus(ParcelaStatus parcelaStatus) {
        this.parcelaStatus = parcelaStatus;
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
                ", observacao='" + parcelaStatus + '\'' +
                ", parcelamento=" + parcelamento +
                '}';
    }
}





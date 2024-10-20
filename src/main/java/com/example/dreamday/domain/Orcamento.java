package com.example.dreamday.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Objects;

public class Orcamento {

    private Long id;
    private Cliente cliente;
    private OrcamentoStatus status;
    private LocalDate dataCriacao;
    private BigDecimal custoEstimado;
    private BigDecimal valorTotal;
    private String observaces;

    public Orcamento(
            Long id, Cliente cliente, OrcamentoStatus status,
            LocalDate dataCriacao, BigDecimal custoEstimado,
            BigDecimal valorTotal, String observaces) {
        this.id = id;
        this.cliente = cliente;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.custoEstimado = custoEstimado;
        this.valorTotal = valorTotal;
        this.observaces = observaces;
    }

    public Orcamento(Cliente cliente, OrcamentoStatus status, LocalDate dataCriacao, BigDecimal custoEstimado, BigDecimal valorTotal, String observaces) {
        this.cliente = cliente;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.custoEstimado = custoEstimado;
        this.valorTotal = valorTotal;
        this.observaces = observaces;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public OrcamentoStatus getStatus() {
        return status;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public BigDecimal getCustoEstimado() {
        return custoEstimado;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getObservaces() {
        return observaces;
    }

    public void setObservaces(String observaces) {
        this.observaces = observaces;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setCustoEstimado(BigDecimal custoEstimado) {
        this.custoEstimado = custoEstimado;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setStatus(OrcamentoStatus status) {
        this.status = status;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Orcamento other = (Orcamento) obj;
        return id == other.id;
    }

}

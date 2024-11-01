package com.br.dreamday.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Parcelamento {

    private Long id;

    private Orcamento orcamento;

    private BigDecimal valor;

    private LocalDateTime dataVencimento;

    private LocalDateTime dataPagamento;

    private ParcelamentoStatus status;

    private String observacao;

    private Integer qtdeParcelas;

    public Parcelamento() {
    }

    public Parcelamento(Long id,
                        Orcamento orcamento,
                        BigDecimal valor,
                        LocalDateTime dataVencimento,
                        LocalDateTime dataPagamento,
                        ParcelamentoStatus status,
                        String observacao,
                        Integer qtdeParcelas) {
        this.id = id;
        this.orcamento = orcamento;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.status = status;
        this.observacao = observacao;
        this.qtdeParcelas = qtdeParcelas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDateTime dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public ParcelamentoStatus getStatus() {
        return status;
    }

    public void setStatus(ParcelamentoStatus status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getQtdeParcelas() {
        return qtdeParcelas;
    }

    public void setQtdeParcelas(Integer qtdeParcelas) {
        this.qtdeParcelas = qtdeParcelas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcelamento that = (Parcelamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Parcelamento{" +
                "id=" + id +
                ", orcamento=" + orcamento +
                ", valor=" + valor +
                ", dataVencimento=" + dataVencimento +
                ", dataPagamento=" + dataPagamento +
                ", status=" + status +
                ", observacao='" + observacao + '\'' +
                ", qtdeParcelas=" + qtdeParcelas +
                '}';
    }
}

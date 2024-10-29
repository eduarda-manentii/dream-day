package com.br.dreamday.domain;

import java.time.LocalDate;
import java.util.Objects;

public class ItemOrcamento {

    private Long id;
    private Orcamento orcamento;
    private ItemFornecedor itemFornecedor;
    private LocalDate dataDeEntrega;
    private Double quantidade;
    private  ItemOrcamentoStatus status;

    public ItemOrcamento(Long id, Orcamento orcamento, ItemFornecedor itemFornecedor, LocalDate dataDeEntrega, Double quantidade, ItemOrcamentoStatus status) {
        this.id = id;
        this.orcamento = orcamento;
        this.itemFornecedor = itemFornecedor;
        this.dataDeEntrega = dataDeEntrega;
        this.quantidade = quantidade;
        this.status = status;
    }

    public ItemOrcamento(Orcamento orcamento, ItemFornecedor itemFornecedor, LocalDate dataDeEntrega, Double quantidade, ItemOrcamentoStatus status) {
        this.orcamento = orcamento;
        this.itemFornecedor = itemFornecedor;
        this.dataDeEntrega = dataDeEntrega;
        this.quantidade = quantidade;
        this.status = status;
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

    public ItemFornecedor getItemFornecedor() {
        return itemFornecedor;
    }

    public void setItemFornecedor(ItemFornecedor itemFornecedor) {
        this.itemFornecedor = itemFornecedor;
    }

    public LocalDate getDataDeEntrega() {
        return dataDeEntrega;
    }

    public void setDataDeEntrega(LocalDate dataDeEntrega) {
        this.dataDeEntrega = dataDeEntrega;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public ItemOrcamentoStatus getStatus() {
        return status;
    }

    public void setStatus(ItemOrcamentoStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemOrcamento that = (ItemOrcamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

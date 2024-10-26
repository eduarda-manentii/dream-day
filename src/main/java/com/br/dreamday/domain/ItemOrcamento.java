package com.br.dreamday.domain;

import java.time.LocalDate;
import java.util.Objects;

public class ItemOrcamento {

    private Long id;
    private Orcamento orcamento;
    //private ItemFornecedor itemFornecedor;
    private Long itemFornecedorId;
    private LocalDate dataDeEntrega;
    private  ItemOrcamentoStatus status;

    public ItemOrcamento(Long id, Orcamento orcamento, ItemOrcamentoStatus status, Long itemFornecedorId, LocalDate dataDeEntrega) {
        this.id = id;
        this.orcamento = orcamento;
        this.status = status;
        this.itemFornecedorId = itemFornecedorId;
        this.dataDeEntrega = dataDeEntrega;
    }

    public ItemOrcamento(Orcamento orcamento, Long itemFornecedorId, LocalDate dataDeEntrega, ItemOrcamentoStatus status) {
        this.orcamento = orcamento;
        this.itemFornecedorId = itemFornecedorId;
        this.dataDeEntrega = dataDeEntrega;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Orcamento getOrcamento() {
        return orcamento;
    }

    public Long getItemFornecedorId() {
        return itemFornecedorId;
    }

    public LocalDate getDataDeEntrega() {
        return dataDeEntrega;
    }

    public ItemOrcamentoStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;
    }

    public void setItemFornecedorId(Long itemFornecedorId) {
        this.itemFornecedorId = itemFornecedorId;
    }

    public void setDataDeEntrega(LocalDate dataDeEntrega) {
        this.dataDeEntrega = dataDeEntrega;
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

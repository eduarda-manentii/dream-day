package com.br.dreamday.domain.dto;

import com.br.dreamday.domain.ItemOrcamento;

import java.math.BigDecimal;
import java.util.List;

public class ClienteRelatorioDto {

    private List<ItemOrcamento> itens;

    private BigDecimal valor;

    private BigDecimal totalUnitario;

    public List<ItemOrcamento> getItens() {
        return itens;
    }

    public void setItens(List<ItemOrcamento> itens) {
        this.itens = itens;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getTotalUnitario() {
        return totalUnitario;
    }

    public void setTotalUnitario(BigDecimal totalUnitario) {
        this.totalUnitario = totalUnitario;
    }
}

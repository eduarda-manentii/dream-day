package com.example.dreamday.domain.key;

import java.util.Objects;

public class ItemFornecedorKey {

    private Long idFornecedor;
    private Long idProduto;

    public ItemFornecedorKey(Long idFornecedor, Long idProduto) {
        this.idFornecedor = idFornecedor;
        this.idProduto = idProduto;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemFornecedorKey that = (ItemFornecedorKey) o;
        return idFornecedor == that.idFornecedor && idProduto == that.idProduto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFornecedor, idProduto);
    }
}

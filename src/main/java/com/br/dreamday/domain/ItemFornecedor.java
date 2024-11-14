package com.br.dreamday.domain;

import com.br.dreamday.domain.key.ItemFornecedorKey;

import java.math.BigDecimal;

public class ItemFornecedor {

    private ItemFornecedorKey id;
    private BigDecimal preco;
    private Categoria categoria;
    private Fornecedor fornecedor;
    private Produto produto;

    public ItemFornecedor() {
    }

    public ItemFornecedor(Long idFornecedor, Long idProduto, BigDecimal preco, Categoria categoria, Fornecedor fornecedor, Produto produto) {
        this.id = new ItemFornecedorKey(idFornecedor, idProduto);
        this.preco = preco;
        this.categoria = categoria;
        this.fornecedor = fornecedor;
        this.produto = produto;
    }

    public ItemFornecedorKey getId() {
        return id;
    }

    public void setId(ItemFornecedorKey id) {
        this.id = id;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public String getCodigo() {
        return (id.getIdFornecedor() + "" + id.getIdProduto());
    }

    public String getDescricaoProduto() {
        return produto.getNome();
    }

    public String getNomeCategoria() {
        return categoria.getNome();
    }

    public String getDescricaoFornecedor() {
        return fornecedor.getNome();
    }

    @Override
    public String toString() {
        return "Fornecedor: " + fornecedor.getNome() +
                " / Produto: " + produto.getNome();
    }
}

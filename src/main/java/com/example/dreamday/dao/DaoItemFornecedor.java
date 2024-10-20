package com.example.dreamday.dao;

import com.example.dreamday.domain.ItemFornecedor;
import com.example.dreamday.domain.Produto;

import java.math.BigDecimal;
import java.util.List;

public interface DaoItemFornecedor {

    void inserir(ItemFornecedor itemFornecedor);
    void alterar(ItemFornecedor  itemFornecedor);
    void excluirPor(Long idFornecedor, Long idProduto);
    List<ItemFornecedor> listarPor(String nomeProduto, String nomeFornecedor, BigDecimal valorInicial, BigDecimal valorFinal);
    boolean validarRemocaoCategoria(Long idCategoria);
    boolean validarRemocaoFornecedor(Long idFornecedor);
    boolean validarRemocaoProduto(Long idProduto);
}

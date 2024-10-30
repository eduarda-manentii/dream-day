package com.br.dreamday.dao;

import com.br.dreamday.domain.ItemFornecedor;

import java.math.BigDecimal;
import java.util.List;

public interface DaoItemFornecedor {

    void inserir(ItemFornecedor itemFornecedor);
    void alterar(ItemFornecedor  itemFornecedor);
    void excluirPor(Long idFornecedor, Long idProduto);
    List<ItemFornecedor> listarPor(String nomeProduto, String nomeFornecedor, Double valorInicial, Double valorFinal);
    List<ItemFornecedor> listarPor(Long idFornecedor);
    boolean validarRemocaoCategoria(Long idCategoria);
    boolean validarRemocaoFornecedor(Long idFornecedor);
    boolean validarRemocaoProduto(Long idProduto);
    boolean validarEdicao(Long idFornecedor, Long idProduto);
}

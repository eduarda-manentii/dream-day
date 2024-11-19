package com.br.dreamday.service;

import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.domain.key.ItemFornecedorKey;
import com.br.dreamday.dao.DaoItemFornecedor;
import com.br.dreamday.dao.FactoryDao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ItemFornecedorService {

    private final DaoItemFornecedor daoItemFornecedor;

    public ItemFornecedorService() {
        this.daoItemFornecedor = FactoryDao.getInstance().getDaoItemFornecedor();
    }

    public void salvar(ItemFornecedor itemFornecedor) {
        this.validar(itemFornecedor);
        boolean isPersistido = daoItemFornecedor.validarEdicao(itemFornecedor.getFornecedor().getId(), itemFornecedor.getProduto().getId());
        if (isPersistido) {
            this.daoItemFornecedor.alterar(itemFornecedor);
        } else {
            this.daoItemFornecedor.inserir(itemFornecedor);
        }
    }

    public void validar(ItemFornecedor itemFornecedor) {

        if (itemFornecedor == null) {
            throw new NullPointerException("O fornecedor não pode ser nula.");
        }

        boolean isIdInvalido = itemFornecedor.getId() == null
                && itemFornecedor.getFornecedor() == null
                && itemFornecedor.getProduto() == null;

        if (isIdInvalido) {
            throw new IllegalArgumentException("O item deve ter um produto e um fornecedor vinculado.");
        }

        boolean isPrecoInvalido = itemFornecedor.getPreco() == null
                || itemFornecedor.getPreco().signum() <= 0;

        if (isPrecoInvalido) {
            throw new IllegalArgumentException("O preço do item precisa ser positivo");
        }

        boolean isCategoriaInvalida = itemFornecedor.getCategoria().getId() == null;
        boolean isFornecedorInvalida = itemFornecedor.getFornecedor().getId() == null;
        boolean isProdutoInvalida = itemFornecedor.getProduto().getId() == null;

        if (isCategoriaInvalida || isFornecedorInvalida || isProdutoInvalida) {
            throw new IllegalArgumentException("A categoria, fornecedor e produto são obrigatórios!");
        }

        boolean existe = daoItemFornecedor.validarItemDuplicado(
                itemFornecedor.getProduto().getId(),
                itemFornecedor.getFornecedor().getId()
        );

        if (existe) {
            throw new IllegalArgumentException("O item informado já está cadastrado para este fornecedor!");
        }
    }

    public void excluirPor(ItemFornecedorKey itemFornecedorKey) {

        if (itemFornecedorKey.getIdFornecedor() == null || itemFornecedorKey.getIdProduto() == null) {
            throw new NullPointerException("O id do item fornecedor é obrigatório!");
        }

        this.daoItemFornecedor.excluirPor(itemFornecedorKey.getIdFornecedor(), itemFornecedorKey.getIdProduto());
    }

    public List<ItemFornecedor> listarPor(String nomeProduto, String nomeFornecedor, BigDecimal valorInicial, BigDecimal valorFinal) {
        boolean isFiltroObrigatorioInvalido = nomeProduto.isBlank() && nomeProduto.length() < 3;

        if (isFiltroObrigatorioInvalido) {
            throw new IllegalArgumentException("O filtro (descrição) é obrigatório e deve ter mais que 2 caracteres!");
        }

        if (Objects.isNull(valorInicial)) {
            valorInicial = BigDecimal.ZERO;
        }

        if (Objects.isNull(valorFinal)) {
            valorFinal = BigDecimal.valueOf(1000);
        }

        nomeProduto = nomeProduto.concat("%");

        if (nomeFornecedor.isBlank()) {
            nomeFornecedor = nomeFornecedor.concat("%%");
        }

        return daoItemFornecedor.listarPor(nomeProduto, nomeFornecedor, valorInicial, valorFinal);
    }

    public List<ItemFornecedor> listarPor(Long idFornecedor) {
        return daoItemFornecedor.listarPor(idFornecedor);
    }


    public List<ItemFornecedor> listarTodos() {
        return daoItemFornecedor.listarTodos();
    }

}

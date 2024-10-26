package com.br.dreamday.service;

import com.br.dreamday.domain.Produto;
import com.br.dreamday.dao.DaoItemFornecedor;
import com.br.dreamday.dao.DaoProduto;
import com.br.dreamday.dao.FactoryDao;

import java.util.List;

public class ProdutoService {

    private final DaoProduto daoProduto;
    private final DaoItemFornecedor daoItemFornecedor;

    public ProdutoService() {
        this.daoProduto = FactoryDao.getInstance().getDaoProduto();
        this.daoItemFornecedor = FactoryDao.getInstance().getDaoItemFornecedor();
    }

    public void salvar(Produto produto) {
        this.validar(produto);
        boolean isPersistido = produto.getId() != null && produto.getId() > 0;
        if (isPersistido) {
            this.daoProduto.alterar(produto);
        } else {
            this.daoProduto.inserir(produto);
        }
    }

    public void validar(Produto produto) {

        if (produto == null) {
            throw new NullPointerException("O produto não pode ser nulo.");
        }

        boolean isNomeInvalido = produto.getNome().isBlank()
                || produto.getNome().length() > 100
                || produto.getNome().length() < 3;

        if (isNomeInvalido) {
            throw new IllegalArgumentException("O nome do produto deve possuir"
                + " entre 3 a 100 caracteres.");
        }

        boolean isDescricaoInvalido = produto.getDescricao().isBlank()
                || produto.getDescricao().length() > 255
                || produto.getDescricao().length() < 3;

        if (isDescricaoInvalido) {
            throw new IllegalArgumentException("A descrição do produto deve possuir entre 3 e 100 caracteres!");
        }
    }

    public void excluirPor(Long idProduto) {
        if (idProduto == null) {
            throw new NullPointerException("O id para remoção"
                + " da categoria deve ser maior que 0.");
        }

        boolean isRemocaoInvalida = daoItemFornecedor.validarRemocaoProduto(idProduto);

        if (isRemocaoInvalida) {
            throw new IllegalArgumentException("Nao é possível remover um"
                + " produto vinculado a um item de fornecedor.");
        }

        this.daoProduto.excluirPor(idProduto);
    }

    public List<Produto> listarPor(String nome) {
        boolean isFiltroInvalido = nome.isBlank() && nome.length() < 3;

        if (isFiltroInvalido) {
            throw new IllegalArgumentException("O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
        }

        String filtro = nome + "%";
        return daoProduto.listarPor(filtro);
    }

    public List<Produto> listarTodos() {
        return daoProduto.listarTodos();
    }
}

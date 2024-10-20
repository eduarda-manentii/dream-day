package com.example.dreamday.service;

import com.example.dreamday.dao.DaoItemFornecedor;
import com.example.dreamday.dao.DaoProduto;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.Produto;
import com.google.common.base.Preconditions;

import java.util.List;

public class ProdutoService {

    private DaoProduto daoProduto;
    private DaoItemFornecedor daoItemFornecedor;

    public ProdutoService() {
        this.daoProduto = FactoryDao.getInstance().getDaoProduto();
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
        Preconditions.checkNotNull(produto, "O produto não pode ser nulo.");
        boolean isNomeInvalido = produto.getNome().isBlank()
                || produto.getNome().length() > 100
                || produto.getNome().length() < 3;

        boolean isDescricaoInvalido = produto.getDescricao().isBlank()
                || produto.getDescricao().length() > 255
                || produto.getDescricao().length() < 3;

        Preconditions.checkArgument(!isNomeInvalido, "O nome do produto deve possuir"
                + " entre 3 a 100 caracteres.");

        Preconditions.checkArgument(!isDescricaoInvalido, "A descricao do produto deve possuir"
                + " entre 3 a 100 caracteres.");
    }

    public void excluirPor(Long idProduto) {
        Preconditions.checkArgument(idProduto > 0, "O id para remoção"
                + " da categoria deve ser maior que 0.");

        boolean isRemocaoInvalida = daoItemFornecedor.validarRemocaoProduto(idProduto);
        Preconditions.checkArgument(!isRemocaoInvalida, "Nao é possível remover um"
                + " produto vinculado a um item de fornecedor.");

        this.daoProduto.excluirPor(idProduto);
    }

    public List<Produto> listarPor(String nome) {
        boolean isFiltroValido = !nome.isBlank() && nome.length() >= 3;
        Preconditions.checkArgument(isFiltroValido, "O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
        String filtro = nome + "%";
        return daoProduto.listarPor(filtro);
    }

    public List<Produto> listarTodos() {
        return daoProduto.listarTodos();
    }
}

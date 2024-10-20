package com.example.dreamday.service;

import com.example.dreamday.dao.DaoItemFornecedor;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.ItemFornecedor;
import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.util.List;

public class ItemFornecedorService {

    private DaoItemFornecedor daoItemFornecedor;

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
        Preconditions.checkNotNull(itemFornecedor, "O fornecedor não pode ser nula.");

        boolean isIdInvalido = itemFornecedor.getId() == null
                && itemFornecedor.getId().getIdFornecedor() != null
                && itemFornecedor.getId().getIdProduto() != null;

        Preconditions.checkArgument(!isIdInvalido, "O item deve ter um produto e um fornecedor vinculado.");

        boolean isPrecoInvalido = itemFornecedor.getPreco() != null
                || itemFornecedor.getPreco().signum() <= 0;

        Preconditions.checkArgument(!isPrecoInvalido, "O preço do item precisa ser positivo");

        boolean isCategoriaInvalida = itemFornecedor.getCategoria().getId() == null;
        Preconditions.checkArgument(!isCategoriaInvalida, "A categoria é obrigatória.");

        boolean isFornecedorInvalida = itemFornecedor.getFornecedor().getId() == null;
        Preconditions.checkArgument(!isFornecedorInvalida, "O fornecedor é obrigatória.");

        boolean isProdutoInvalida = itemFornecedor.getProduto().getId() == null;
        Preconditions.checkArgument(!isProdutoInvalida, "O produto é obrigatória.");
    }

    public void excluirPor(Long idFornecedor, Long idProduto) {
        Preconditions.checkArgument(idFornecedor > 0, "O id para remoção"
                + " do fornecedor deve ser maior que 0.");

        Preconditions.checkArgument(idProduto > 0, "O id para remoção"
                + " do produto deve ser maior que 0.");

        this.daoItemFornecedor.excluirPor(idFornecedor, idProduto);
    }

    public List<ItemFornecedor> listarPor(String nomeProduto, String nomeFornecedor, BigDecimal valorInicial, BigDecimal valorFinal) {
        boolean isFiltroValido = !nomeProduto.isBlank() && nomeProduto.length() >= 3;

        Preconditions.checkArgument(isFiltroValido, "O filtro (nome) é obrigatório e deve ter mais que 2 caracteres.");
        String filtro = nomeProduto + "%";
        return daoItemFornecedor.listarPor(nomeProduto, nomeFornecedor, valorInicial, valorFinal);
    }
}

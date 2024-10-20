package com.example.dreamday.service;

import com.example.dreamday.dao.DaoCategoria;
import com.example.dreamday.dao.DaoItemFornecedor;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.Categoria;
import com.google.common.base.Preconditions;

import java.util.List;

public class CategoriaService {

    private DaoCategoria daoCategoria;
    private DaoItemFornecedor daoItemFornecedor;

    public CategoriaService() {
        this.daoCategoria = FactoryDao.getInstance().getDaoCategoria();
    }

    public void salvar(Categoria categoria) {
        this.validar(categoria);
        boolean isPersistido = categoria.getId() != null && categoria.getId() > 0;
        if (isPersistido) {
            this.daoCategoria.alterar(categoria);
        } else {
            this.daoCategoria.inserir(categoria);
        }
    }

    public void validar(Categoria categoria) {
        Preconditions.checkNotNull(categoria, "A categoria nao pode ser nula.");
        boolean isNomeInvalido = categoria.getNome().isBlank()
                || categoria.getNome().length() > 100
                || categoria.getNome().length() < 3;

        Preconditions.checkArgument(!isNomeInvalido, "O nome da categoria deve possuir"
                + " entre 3 a 100 caracteres.");
    }

    public void excluirPor(Long idCategoria) {
        Preconditions.checkArgument(idCategoria > 0, "O id para remoção"
                + " da categoria deve ser maior que 0.");

        boolean isRemocaoInvalida = daoItemFornecedor.validarRemocaoCategoria(idCategoria);
        Preconditions.checkArgument(!isRemocaoInvalida, "Nao é possível remover uma"
                + " categoria vinculada a um item.");

        this.daoCategoria.excluirPor(idCategoria);
    }

    public List<Categoria> listarPor(String nome) {
        boolean isFiltroValido = !nome.isBlank() && nome.length() >= 3;
        Preconditions.checkArgument(isFiltroValido, "O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
        String filtro = nome + "%";
        return daoCategoria.listarPor(filtro);
    }

    public List<Categoria> listarTodas() {
        return daoCategoria.listarTodas();
    }
}

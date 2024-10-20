package com.example.dreamday.service;

import com.example.dreamday.dao.DaoFornecedor;
import com.example.dreamday.dao.DaoItemFornecedor;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.Fornecedor;
import com.google.common.base.Preconditions;

import java.util.List;

public class FornecedorService {

    private DaoFornecedor daoFornecedor;
    private DaoItemFornecedor daoItemFornecedor;

    public FornecedorService() {
        this.daoFornecedor = FactoryDao.getInstance().getDaoFornecedor();
    }

    public void salvar(Fornecedor fornecedor) {
        this.validar(fornecedor);
        boolean isPersistido = fornecedor.getId() != null && fornecedor.getId() > 0;
        if (isPersistido) {
            this.daoFornecedor.alterar(fornecedor);
        } else {
            this.daoFornecedor.inserir(fornecedor);
        }
    }

    public void validar(Fornecedor fornecedor) {
        Preconditions.checkNotNull(fornecedor, "O fornecedor não pode ser nula.");
        boolean isNomeInvalido = fornecedor.getNome().isBlank()
                || fornecedor.getNome().length() > 255
                || fornecedor.getNome().length() < 3;

        Preconditions.checkArgument(!isNomeInvalido, "O nome do fornecedor deve possuir"
                + " entre 3 a 255 caracteres.");

        boolean isTelefoneInvalido = fornecedor.getTelefone().isBlank()
                || fornecedor.getTelefone().length() != 15;

        Preconditions.checkArgument(!isTelefoneInvalido, "O telefone do fornecedor deve possuir"
                + " 15 caracteres.");

        boolean isEmailInvalido = fornecedor.getEmail().isBlank()
                || fornecedor.getEmail().length() > 255
                || fornecedor.getEmail().length() < 3
                || fornecedor.getEmail().contains("@");

        Preconditions.checkArgument(!isEmailInvalido, "O email do fornecedor deve possuir"
                + " entre 3 e 255.");
    }

    public void excluirPor(Long idFornecedor) {
        Preconditions.checkArgument(idFornecedor > 0, "O id para remoção"
                + " do fornecedor deve ser maior que 0.");

        boolean isRemocaoInvalida = daoItemFornecedor.validarRemocaoFornecedor(idFornecedor);
        Preconditions.checkArgument(!isRemocaoInvalida, "Nao é possível remover uma"
                + " categoria vinculada a um restaurante.");

        this.daoFornecedor.excluirPor(idFornecedor);
    }

    public List<Fornecedor> listarPor(String nome) {
        boolean isFiltroValido = !nome.isBlank() && nome.length() >= 3;
        Preconditions.checkArgument(isFiltroValido, "O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
        String filtro = nome + "%";
        return daoFornecedor.listarPor(filtro);
    }

    public List<Fornecedor> listarTodas() {
        return daoFornecedor.listarTodos();
    }
}

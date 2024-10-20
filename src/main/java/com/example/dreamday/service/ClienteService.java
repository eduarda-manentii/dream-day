package com.example.dreamday.service;

import com.example.dreamday.dao.DaoCliente;
import com.example.dreamday.dao.FactoryDao;
import com.example.dreamday.domain.Cliente;

import java.util.List;

public class ClienteService {

    private DaoCliente dao;

    public ClienteService() {
        this.dao = FactoryDao.getInstance().getDaoCliente();
    }

    public void salvar(Cliente cliente) {
        this.validar(cliente);
        boolean isJaInserido = cliente.getId() != null && cliente.getId() > 0;
        if (isJaInserido) {
            this.dao.alterar(cliente);
        } else {
            this.dao.inserir(cliente);
        }
    }

    private void validar(Cliente cliente) {
        if (cliente == null) {
            throw new NullPointerException("O cliente não pode ser nulo");
        }
        if (cliente.getNome() == null || cliente.getNome().isBlank() || cliente.getNome().length() > 250) {
            throw new IllegalArgumentException("O nome é obrigatório e deve conter menos de 250 caracteres");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O telefone é obrigatório");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank()) {
            throw new IllegalArgumentException("O email é obrigatório");
        }
        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new IllegalArgumentException("O CPF é obrigatório");
        }
    }

    public List<Cliente> listarPor(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome para listagem");
        }
        return dao.listarPor(nome + "%");
    }

    public List<Cliente> listarTodos() {
        return dao.listarPor("%%");
    }

    public void excluirPor(Long idDoCliente) {
        if (idDoCliente == null || idDoCliente <= 0) {
            throw new IllegalArgumentException("O id para exclusão deve ser maior que zero");
        }
        this.dao.excluirPor(idDoCliente.intValue());
    }

    public Cliente buscarPor(Long idDoCliente) {
        if (idDoCliente == null || idDoCliente <= 0) {
            throw new IllegalArgumentException("O id para busca não pode ser menor que zero");
        }
        Cliente clienteEncontrado = this.dao.buscarPor(idDoCliente.intValue());
        if (clienteEncontrado == null) {
            throw new IllegalArgumentException("Não existe cliente vinculado ao id informado");
        }
        return clienteEncontrado;
    }

}

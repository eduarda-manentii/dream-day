package com.br.dreamday.service;

import com.br.dreamday.dao.DaoCliente;
import com.br.dreamday.dao.DaoOrcamento;
import com.br.dreamday.dao.FactoryDao;
import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.Orcamento;

import java.time.LocalDate;
import java.util.List;

public class ClienteService {

    private DaoCliente dao;
    private DaoOrcamento daoOrcamento;

    public ClienteService() {
        this.dao = FactoryDao.getInstance().getDaoCliente();
        this.daoOrcamento = FactoryDao.getInstance().getDaoOrcamento();
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
        if (cliente.getNome() == null || cliente.getNome().isBlank() || cliente.getNome().length() > 250  || cliente.getNome().length() < 5) {
            throw new IllegalArgumentException("O nome é obrigatório e deve conter entre 5 a 250 caracteres");
        }
        if (cliente.getConjugue() == null || cliente.getConjugue().isBlank() || cliente.getConjugue().length() > 250  || cliente.getConjugue().length() < 5) {
            throw new IllegalArgumentException("O nome do conjugue é obrigatório e deve conter entre 5 a 250 caracteres");
        }
        if (cliente.getTelefone() == null || cliente.getTelefone().isBlank()) {
            throw new IllegalArgumentException("O telefone é obrigatório");
        }
        if (cliente.getEmail() == null || cliente.getEmail().isBlank() || !cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException("O email é obrigatório");
        }
        if (cliente.getCpf() == null || cliente.getCpf().isBlank()) {
            throw new IllegalArgumentException("O CPF é obrigatório");
        }

        LocalDate dataAtual = LocalDate.now();
        if(cliente.getDataCasamento() == null || cliente.getDataCasamento().isBefore(dataAtual)) {
            throw new IllegalArgumentException("A data de casamento não pode ser anterior ao dia atual.");
        }
    }

    public List<Cliente> listarPor(String nome) {
        boolean isFiltroInvalido = nome.isBlank() || nome.length() < 3;
        if (isFiltroInvalido) {
            throw new IllegalArgumentException("O filtro para listagem é obrigatório e deve ter mais que 2 caracteres.");
        }
        return dao.listarPor(nome + "%");
    }

      public void excluirPor(Long idDoCliente) {
        if (idDoCliente == null || idDoCliente <= 0) {
            throw new IllegalArgumentException("O id para exclusão deve ser maior que zero");
        }
        if (daoOrcamento.contarOrcamentosPorClienteId(idDoCliente) > 0) {
            throw new IllegalArgumentException("Não é possível excluir um cliente vinculado a orçamentos.");
        }
        this.dao.excluirPor(idDoCliente);
    }

    public List<Cliente> listarPor(String nome, LocalDate dataDeCasamento) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Informe o nome para listagem");
        }
        return dao.listarPor(nome + "%", dataDeCasamento);
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

    public List<Cliente> listarPor(LocalDate data) {
        return dao.listarPor(data);
    }

    public List<Cliente> listarTodos() {
        return dao.listarTodos();
    }

}

package com.br.dreamday.dao;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.ItemFornecedor;
import com.br.dreamday.domain.ItemOrcamento;

import java.time.LocalDate;
import java.util.List;

public interface DaoItemOrcamento {

    void inserir(ItemOrcamento itemOrcamento);
    void alterar(ItemOrcamento itemOrcamento);
    void excluirPor(Long id);
    ItemOrcamento buscarPor(int id);
    List<ItemOrcamento> listarPor(Long idOrcamento);
    List<ItemOrcamento> listarPorCliente(Long idCliente);
    List<ItemOrcamento> listarTodos();

}

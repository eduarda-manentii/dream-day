package com.br.dreamday.dao;

import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.ItemOrcamento;

import java.time.LocalDate;
import java.util.List;

public interface DaoItemOrcamento {

    public void inserir(ItemOrcamento itemOrcamento);

    public void alterar(ItemOrcamento itemOrcamento);

    public void excluirPor(int id);

    public ItemOrcamento buscarPor(int id);

    public List<ItemOrcamento> listarTodos();

}

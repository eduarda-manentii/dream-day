package com.br.dreamday.dao.postgres;

import com.br.dreamday.dao.DaoItemOrcamento;
import com.br.dreamday.dao.ManagerDb;
import com.br.dreamday.domain.ItemOrcamento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

public class DaoPostgresItemOrcamento implements DaoItemOrcamento {

    private final String INSERT = "INSERT INTO itens_orcamentos (id_fornecedor, id_categoria, id_orcamento, data_entrega, quantidade, status) VALUES (?, ?, ?, ?, ?, ?)";

    private final String UPDATE = "UPDATE itens_orcamentos SET id_fornecedor = ?, id_categoria = ?, id_orcamento = ?, data_entrega = ?, quantidade = ?, status = ? WHERE id = ?";

    private final String DELETE = "DELETE FROM itens_orcamentos WHERE id = ?";

    private final String SELECT_BY_ID = "SELECT " +
            "it.id, " +
            "it.id_fornecedor, " +
            "it.id_categoria, " +
            "it.id_orcamento, " +
            "it.data_entrega, " +
            "it.quantidade, " +
            "it.status, " +
            "f.nome AS fornecedor_nome, " +
            "f.telefone AS fornecedor_telefone, " +
            "f.email AS fornecedor_email, " +
            "o.id_cliente, " +
            "o.status AS orcamento_status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total, " +
            "c.nome AS categoria_nome " +
            "FROM " +
            "itens_orcamentos it " +
            "JOIN fornecedores f ON it.id_fornecedor = f.id " +
            "JOIN categorias c ON it.id_categoria = c.id " +
            "JOIN orcamentos o ON it.id_orcamento = o.id " +
            "WHERE it.id = ?";

    private Connection conexao;

    @Override
    public void inserir(ItemOrcamento itemOrcamento) {
    }

    @Override
    public void alterar(ItemOrcamento itemOrcamento) {

    }

    @Override
    public void excluirPor(int id) {

    }

    @Override
    public ItemOrcamento buscarPor(int id) {
        return null;
    }

    @Override
    public List<ItemOrcamento> listarTodos() {
        return List.of();
    }
}

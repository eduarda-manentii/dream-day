package com.br.dreamday.dao.postgres;

import com.br.dreamday.dao.DaoItemOrcamento;
import com.br.dreamday.dao.ManagerDb;
import com.br.dreamday.domain.*;
import com.br.dreamday.domain.key.ItemFornecedorKey;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaoPostgresItemOrcamento implements DaoItemOrcamento {

    private final String INSERT = "INSERT INTO itens_orcamentos (id_fornecedor, id_produto, id_orcamento, data_entrega, quantidade, status) VALUES (?, ?, ?, ?, ?, ?)";

    private final String UPDATE = "UPDATE itens_orcamentos SET " +
            "id_fornecedor = ?, " +
            "id_produto = ?, " +
            "id_orcamento = ?, " +
            "data_entrega = ?, " +
            "quantidade = ?, " +
            "status = ? " +
            "WHERE id = ?";

    private final String DELETE = "DELETE FROM itens_orcamentos WHERE id = ?";

    private final String SELECT_BY_ID_ORC = "SELECT " +
            "it.id, " +
            "it.id_fornecedor, " +
            "f.id_categoria, " +
            "it.id_orcamento, " +
            "it.data_entrega, " +
            "it.quantidade, " +
            "it.status, " +
            "f.preco AS item_preco, " +
            "f.id_fornecedor AS fornecedor_id, " +
            "f.id_produto AS produto_id, " +
            "f.id_categoria AS categoria_id, " +
            "forn.nome AS fornecedor_nome, " +
            "forn.telefone AS fornecedor_telefone, " +
            "forn.email AS fornecedor_email, " +
            "p.id AS produto_id, " +
            "p.nome AS produto_nome, " +
            "p.descricao AS produto_descricao, " +
            "c.id AS categoria_id, " +
            "c.nome AS categoria_nome, " +
            "o.status AS orcamento_status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total, " +
            "cli.id cliente_id, " +
            "cli.nome cliente_nome, " +
            "cli.conjugue cliente_conjugue, " +
            "cli.data_casamento cliente_data_casamento, " +
            "cli.telefone cliente_telefone, " +
            "cli.email cliente_email, " +
            "cli.cpf cliente_cpf " +
            "FROM " +
            "itens_orcamentos it " +
            "JOIN itens_fornecedores f ON it.id_fornecedor = f.id_fornecedor AND it.id_produto = f.id_produto  " +
            "JOIN fornecedores forn ON it.id_fornecedor = forn.id  " +
            "JOIN orcamentos o ON it.id_orcamento = o.id " +
            "JOIN produtos p ON f.id_produto = p.id " +
            "JOIN categorias c ON f.id_categoria = c.id " +
            "JOIN clientes cli ON cli.id = o.id_cliente " +
            "WHERE it.id_orcamento = ? " +
            "ORDER BY p.nome";

    private final String SELECT_BY_ID = "SELECT " +
            "it.id, " +
            "it.id_fornecedor, " +
            "it.id_categoria, " +
            "it.id_orcamento, " +
            "it.data_entrega, " +
            "it.quantidade, " +
            "it.status, " +
            "f.preco, " +
            "f.id_fornecedor AS fornecedor_id, " +
            "f.id_produto AS produto_id, " +
            "f.nome AS fornecedor_nome, " +
            "f.telefone AS fornecedor_telefone, " +
            "f.email AS fornecedor_email, " +
            "p.id AS produto_id, " +
            "p.nome AS produto_nome, " +
            "p.descricao AS produto_descricao, " +
            "c.id AS categoria_id, " +
            "c.nome AS categoria_nome, " +
            "o.id_categoria, " +
            "o.status AS orcamento_status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total, " +
            "cli.id cliente_id, " +
            "cli.nome cliente_nome, " +
            "cli.conjugue cliente_conjugue, " +
            "cli.data_casamento cliente_data_casamento, " +
            "cli.telefone cliente_telefone, " +
            "cli.email cliente_email, " +
            "cli.cpf cliente_cpf " +
            "FROM " +
            "itens_orcamentos it " +
            "JOIN itens_fornecedores f ON it.id_fornecedor = f.id_fornecedor AND it.id_produto = f.id_produto  " +
            "JOIN orcamentos o ON it.id_orcamento = o.id " +
            "JOIN produtos p ON f.id_produto = p.id " +
            "JOIN categorias c ON it.id_categoria = c.id " +
            "JOIN clientes cli ON cli.id = orcamentos.id_cliente " +
            "WHERE it.id = ?";

    private final String SELECT_TODES = "SELECT " +
            "it.id, " +
            "it.id_fornecedor, " +
            "it.id_categoria, " +
            "it.id_orcamento, " +
            "it.data_entrega, " +
            "it.quantidade, " +
            "it.status, " +
            "f.preco, " +
            "f.id_fornecedor AS fornecedor_id, " +
            "f.id_produto AS produto_id, " +
            "f.nome AS fornecedor_nome, " +
            "f.telefone AS fornecedor_telefone, " +
            "f.email AS fornecedor_email, " +
            "p.id AS produto_id, " +
            "p.nome AS produto_nome, " +
            "p.descricao AS produto_descricao, " +
            "c.id AS categoria_id, " +
            "c.nome AS categoria_nome, " +
            "o.id_categoria, " +
            "o.status AS orcamento_status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total, " +
            "cli.id cliente_id, " +
            "cli.nome cliente_nome, " +
            "cli.conjugue cliente_conjugue, " +
            "cli.data_casamento cliente_data_casamento, " +
            "cli.telefone cliente_telefone, " +
            "cli.email cliente_email, " +
            "cli.cpf cliente_cpf " +
            "FROM " +
            "itens_orcamentos it " +
            "JOIN itens_fornecedores f ON it.id_fornecedor = f.id_fornecedor AND it.id_produto = f.id_produto  " +
            "JOIN fornecedores for ON it.id_fornecedor = f.id_fornecedor AND it.id_produto = f.id_produto  " +
            "JOIN orcamentos o ON it.id_orcamento = o.id " +
            "JOIN produtos p ON f.id_produto = p.id " +
            "JOIN categorias c ON it.id_categoria = c.id " +
            "JOIN clientes cli ON cli.id = orcamentos.id_cliente " +
            "ORDER BY LOWER(it.data_entrega) DESC";


    private Connection conexao;

    public DaoPostgresItemOrcamento() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }


    @Override
    public void inserir(ItemOrcamento itemOrcamento) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setLong(1, itemOrcamento.getItemFornecedor().getId().getIdFornecedor());
            ps.setLong(2, itemOrcamento.getItemFornecedor().getId().getIdProduto());
            ps.setLong(3, itemOrcamento.getOrcamento().getId());
            ps.setDate(4, Date.valueOf(itemOrcamento.getDataDeEntrega()));
            ps.setDouble(5, itemOrcamento.getQuantidade());
            ps.setString(6, String.valueOf(itemOrcamento.getStatus()));
            ps.execute();
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na inserção do item de orçamento." + " Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void alterar(ItemOrcamento itemOrcamento) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setLong(1, itemOrcamento.getItemFornecedor().getId().getIdFornecedor());
            ps.setLong(2, itemOrcamento.getItemFornecedor().getId().getIdProduto());
            ps.setLong(3, itemOrcamento.getOrcamento().getId());
            ps.setDate(4, Date.valueOf(itemOrcamento.getDataDeEntrega()));
            ps.setDouble(5, itemOrcamento.getQuantidade());
            ps.setString(6, String.valueOf(itemOrcamento.getStatus()));
            ps.setLong(7, itemOrcamento.getId());
            boolean isAlteracaoOK = ps.executeUpdate() == 1;
            if (isAlteracaoOK) {
                this.conexao.commit();
            }else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao alterar o item de orçamento. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void excluirPor(Long id) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(DELETE);
            ps.setLong(1, id);
            boolean isExclusaoOK = ps.executeUpdate() == 1;
            if (isExclusaoOK) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao excluir o item de orçamento."
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public List<ItemOrcamento> listarPor(Long idOrcamento) {
        List<ItemOrcamento> itens = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_ID_ORC);
            ps.setLong(1, idOrcamento);
            rs = ps.executeQuery();
            while (rs.next()) {
                itens.add(extrairDo(rs));
            }
            return itens;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro ao listar o " + "nome do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public ItemOrcamento buscarPor(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extrairDo(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao buscar o Item do orcamento. "
                    + "Motivo: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public List<ItemOrcamento> listarTodos() {
        List<ItemOrcamento> itensOrcamentos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_TODES);
            rs = ps.executeQuery();
            while(rs.next()) {
                itensOrcamentos.add(extrairDo(rs));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na listagem"
                    + " dos itens dos orcamentos. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
        return itensOrcamentos;
    }

    private ItemOrcamento extrairDo(ResultSet rs) {
        try {
            Long idFornecedor = rs.getLong("fornecedor_id");
            String nomeFornecedor = rs.getString("fornecedor_nome");
            String telefoneFornecedor = rs.getString("fornecedor_telefone");
            String emailFornecedor = rs.getString("fornecedor_email");
            Fornecedor fornecedor = new Fornecedor(idFornecedor, nomeFornecedor, telefoneFornecedor, emailFornecedor);

            Long idProduto = rs.getLong("produto_id");
            String nomeProduto = rs.getString("produto_nome");
            String descricaoProduto = rs.getString("produto_descricao");
            Produto produto = new Produto(idProduto, nomeProduto, descricaoProduto);

            Long idCategoria = rs.getLong("categoria_id");
            String nomeCategoria = rs.getString("categoria_nome");
            Categoria categoria = new Categoria(idCategoria, nomeCategoria);

            Long idCliente = rs.getLong("cliente_id");
            String nomeCliente = rs.getString("cliente_nome");
            String conjugue = rs.getString("cliente_conjugue");
            LocalDate dataCasamento = rs.getDate("cliente_data_casamento").toLocalDate();
            String telefoneCliente = rs.getString("cliente_telefone");
            String emailCliente = rs.getString("cliente_email");
            String cpfCliente = rs.getString("cliente_cpf");
            Cliente cliente = new Cliente(idCliente, nomeCliente, conjugue, dataCasamento, telefoneCliente, emailCliente, cpfCliente);

            ItemFornecedorKey id = new ItemFornecedorKey(idFornecedor, idProduto);
            BigDecimal preco_item = rs.getBigDecimal("item_preco");
            ItemFornecedor itemFornecedor = new ItemFornecedor(id, preco_item, categoria, fornecedor, produto);

            Long idItemOrcamento = rs.getLong("id");
            String observacoes = rs.getString("observacoes");
            LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
            BigDecimal custoEstimado = rs.getBigDecimal("custo_estimado");
            OrcamentoStatus orcamentoStatus = OrcamentoStatus.valueOf(rs.getString("status"));
            BigDecimal valorTotal = rs.getBigDecimal("valor_total");
            Double quantidade = rs.getDouble("quantidade");

            Long idOrcamento = rs.getLong("id_orcamento");
            LocalDate dataEntrega = rs.getDate("data_entrega").toLocalDate();
            ItemOrcamentoStatus status = ItemOrcamentoStatus.valueOf(rs.getString("status"));
            Orcamento orcamento = new Orcamento(idOrcamento, cliente, orcamentoStatus, dataCriacao, custoEstimado, valorTotal, observacoes);

            ItemOrcamento itemOrcamento = new ItemOrcamento(idItemOrcamento, orcamento, itemFornecedor, dataEntrega, quantidade, status);
            return itemOrcamento;
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao extrair o item. Motivo: " + ex.getMessage(), ex);
        }
    }


}

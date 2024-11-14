package com.br.dreamday.dao.postgres;

import com.br.dreamday.domain.Produto;
import com.br.dreamday.dao.DaoProduto;
import com.br.dreamday.dao.ManagerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DaoPostgresqlProduto implements DaoProduto {

    private final String INSERT = "INSERT INTO produtos (nome, descricao) VALUES (?, ?)";

    private final String UPDATE = "UPDATE produtos SET "
            + "nome = ?, "
            + "descricao = ? "
            + "WHERE id = ?";

    private final String DELETE = "DELETE FROM produtos WHERE id = ?";

    private final String SELECT_BY_NOME = "SELECT "
            + "pro.id, "
            + "pro.nome, "
            + "pro.descricao "
            + "FROM produtos pro "
            + "WHERE Upper(pro.nome) LIKE Upper(?) ORDER BY pro.nome ";

    private final String SELECT_TODOS = "SELECT "
            + "pro.id, "
            + "pro.nome, "
            + "pro.descricao "
            + "FROM produtos pro "
            + "ORDER BY LOWER(pro.nome)";

    private final Connection conexao;

    public DaoPostgresqlProduto() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    public void inserir(Produto produto) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.execute();
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na inserção"
                    + " do produto. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    public void alterar(Produto produto) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setString(1, produto.getNome());
            ps.setString(2, produto.getDescricao());
            ps.setLong(3, produto.getId());
            boolean isAlteracaoOk = ps.executeUpdate() == 1;
            if (isAlteracaoOk) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao alterar"
                    + " o produto. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    public void excluirPor(Long id) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(DELETE);
            ps.setLong(1, id);
            boolean isExclusaoOk = ps.executeUpdate() == 1;
            if (isExclusaoOk) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao excluir o produto. "
                    + "Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    public List<Produto> listarPor(String nome) {
        List<Produto> produtos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_NOME);
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while(rs.next()) {
                produtos.add(extrairDo(rs));
            }
            return produtos;
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao "
                    + "listar os produtos. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    public List<Produto> listarPor(String nome, Integer limite) {
        List<Produto> produtos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_NOME + " LIMIT ?");
            ps.setString(1, nome);
            ps.setInt(2, limite);
            rs = ps.executeQuery();
            while(rs.next()) {
                produtos.add(extrairDo(rs));
            }

            return produtos;
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao "
                    + "listar os produtos. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_TODOS);
            rs = ps.executeQuery();
            while(rs.next()) {
                produtos.add(extrairDo(rs));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na listagem"
                    + " dos fornecedores. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
        return produtos;
    }

    private Produto extrairDo(ResultSet rs) {
        try {
            long id = rs.getLong("id");
            String nome = rs.getString("nome");
            String descricao = rs.getString("descricao");
            return new Produto(id, nome, descricao);
        }catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao "
                    + "extrair o produto. Motivo: " + ex.getMessage());
        }
    }

}
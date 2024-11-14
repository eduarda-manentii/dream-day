package com.br.dreamday.dao.postgres;

import com.br.dreamday.domain.Fornecedor;
import com.br.dreamday.dao.DaoFornecedor;
import com.br.dreamday.dao.ManagerDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DaoPostgresqlFornecedor implements DaoFornecedor {

    private final String INSERT = "INSERT INTO fornecedores (nome, telefone, email) VALUES (?, ?, ?)";

    private final String UPDATE = "UPDATE fornecedores SET "
            + "nome = ?, "
            + "telefone = ?, "
            + "email = ? "
            + "WHERE id = ?";

    private final String DELETE = "DELETE FROM fornecedores WHERE id = ?";

    private final String SELECT_BY_NOME = "SELECT "
            + "f.id, "
            + "f.nome, "
            + "f.telefone, "
            + "f.email "
            + "FROM fornecedores f "
            + " WHERE Upper(f.nome) LIKE Upper(?) ORDER BY f.nome";

    private final String SELECT_TODOS = "SELECT "
            + "f.id, "
            + "f.nome, "
            + "f.telefone, "
            + "f.email "
            + "FROM fornecedores f "
            + "ORDER BY LOWER(f.nome) ";

    private Connection conexao;

    public DaoPostgresqlFornecedor( ) {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    public void inserir(Fornecedor fornecedor) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTelefone());
            ps.setString(3, fornecedor.getEmail());
            ps.execute();
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na inserção"
                    + " do fornecedor. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    public void alterar(Fornecedor fornecedor) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setString(1, fornecedor.getNome());
            ps.setString(2, fornecedor.getTelefone());
            ps.setString(3, fornecedor.getEmail());
            ps.setLong(4, fornecedor.getId());
            boolean isAlteracaoOk = ps.executeUpdate() == 1;
            if (isAlteracaoOk) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao alterar"
                    + " o fornecedor. Motivo: " + ex.getMessage());
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
            throw new RuntimeException("Ocorreu um erro ao excluir o fornecedor. "
                    + "Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    public List<Fornecedor> listarPor(String nome) {
        List<Fornecedor> fornecedores = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_NOME);
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while(rs.next()) {
                fornecedores.add(extrairDo(rs));
            }
            return fornecedores;
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao "
                    + "listar as categorias. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    public List<Fornecedor> listarTodos() {
        List<Fornecedor> fornecedores = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_TODOS);
            rs = ps.executeQuery();
            while(rs.next()) {
                fornecedores.add(extrairDo(rs));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na listagem"
                    + " dos fornecedores. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
        return fornecedores;
    }

    private Fornecedor extrairDo(ResultSet rs) {
        try {
            long id = rs.getLong("id");
            String nome = rs.getString("nome");
            String telefone = rs.getString("telefone");
            String email = rs.getString("email");
            return new Fornecedor(id, nome, telefone, email);
        }catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao "
                    + "extrair o fornecedor. Motivo: " + ex.getMessage());
        }
    }

}
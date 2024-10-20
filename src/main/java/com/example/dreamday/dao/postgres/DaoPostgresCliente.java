package com.example.dreamday.dao.postgres;

import com.example.dreamday.dao.DaoCliente;
import com.example.dreamday.dao.ManagerDb;
import com.example.dreamday.domain.Cliente;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaoPostgresCliente implements DaoCliente {

    private final String INSERT = "INSERT INTO clientes (nome, conjugue, data_casamento, telefone, email, cpf) VALUES (?, ?, ?, ?, ?, ?)";

    private final String UPDATE = "UPDATE clientes SET nome = ?, conjugue = ?, data_casamento = ?, telefone = ?, email = ?, cpf = ? WHERE id = ?";

    private final String DELETE = "DELETE FROM clientes WHERE id = ?";

    private final String SELECT_BY_ID = "SELECT c.id, c.nome, c.conjugue, c.data_casamento, c.telefone, c.email, c.cpf "
            + " FROM clientes c "
            + " WHERE c.id = ? ";

    private final String SELECT_BY_NOME = "SELECT c.id, c.nome, c.conjugue, c.data_casamento, c.telefone, c.email, c.cpf "
            + " FROM clientes c "
            + "WHERE Upper(c.nome) LIKE Upper(?) "
            + "ORDER BY c.nome ";

    private Connection conexao;

    public DaoPostgresCliente() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public void inserir(Cliente cliente) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setString(1, cliente.getNome());
            ps.execute();
        }catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao inserir o cliente. "
                    + "Motivo: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void alterar(Cliente cliente) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getConjugue());
            ps.setDate(3, Date.valueOf(cliente.getDataCasamento()));
            ps.setString(4, cliente.getTelefone());
            ps.setString(5, cliente.getEmail());
            ps.setString(6, cliente.getCpf());
            ps.setLong(7, cliente.getId());
            boolean isAlteracaoOK = ps.executeUpdate() == 1;
            if (isAlteracaoOK) {
                this.conexao.commit();
            }else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao alterar o cliente. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void excluirPor(int id) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
            ps = conexao.prepareStatement(DELETE);
            ps.setInt(1, id);
            boolean isExclusaoOK = ps.executeUpdate() == 1;
            if (isExclusaoOK) {
                this.conexao.commit();
            }else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao excluir o cliente. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public Cliente buscarPor(int id) {
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
            throw new RuntimeException("Ocorreu um erro ao buscar o cliente. "
                    + "Motivo: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public List<Cliente> listarPor(String nome) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Cliente> clientes = new ArrayList<Cliente>();
        try {
            ps = conexao.prepareStatement(SELECT_BY_NOME);
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                clientes.add(extrairDo(rs));
            }
            return clientes;
        }catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao listar clientes. "
                    + "Motivo: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    private Cliente extrairDo(ResultSet rs) {
        try {
            String nomeDoCliente = rs.getString("nome");
            String conjugue = rs.getString("conjugue");
            LocalDate dataCasamento = rs.getDate("data_casamento").toLocalDate();
            String telefone = rs.getString("telefone");
            String email = rs.getString("email");
            String cpf = rs.getString("cpf");

            return new Cliente(nomeDoCliente, conjugue, dataCasamento, telefone, email, cpf);

        }catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao extrair o cliente. "
                    + "Motivo: " + e.getMessage());
        }
    }

}

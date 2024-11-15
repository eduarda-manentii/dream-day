package com.br.dreamday.dao.postgres;

import com.br.dreamday.dao.DaoOrcamento;
import com.br.dreamday.dao.ManagerDb;
import com.br.dreamday.domain.Cliente;
import com.br.dreamday.domain.ItemOrcamentoStatus;
import com.br.dreamday.domain.Orcamento;
import com.br.dreamday.domain.OrcamentoStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DaoPostgresOrcamento implements DaoOrcamento {

    private final String INSERT = "INSERT INTO orcamentos (id_cliente, status, observacoes, data_criacao, custo_estimado, valor_total) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private final String UPDATE = "UPDATE orcamentos SET id_cliente = ?, status = ?, observacoes = ?, data_criacao = ?, custo_estimado = ?, valor_total = ? WHERE id = ?";
    private final String DELETE = "DELETE FROM orcamentos WHERE id = ?";

    private final String SELECT_BY_ID = "SELECT " +
            "o.id, " +
            "c.id id_cliente, " +
            "c.nome, " +
            "c.conjugue, " +
            "c.data_casamento, " +
            "c.telefone, " +
            "c.email, " +
            "c.cpf, " +
            "o.status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total "
            + " FROM orcamentos o," +
            "       clientes c "
            + " WHERE o.id_cliente = c.id " +
            "   AND o.id = ? ";

    private final String UPDATE_VALOR_TOTAL = "UPDATE orcamentos SET valor_total = valor_total + ? WHERE id = ?";

    private final String SELECT_BY_CLI_NOME = "SELECT " +
            "o.id, " +
            "c.id id_cliente, " +
            "c.nome, " +
            "c.conjugue, " +
            "c.data_casamento, " +
            "c.telefone, " +
            "c.email, " +
            "c.cpf, " +
            "o.status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total " +
            "FROM orcamentos o, " +
            "clientes c " +
            "WHERE o.id_cliente = c.id " +
            "AND c.nome = ? " +
            "ORDER BY c.nome ";

    private final String SELECT_BY_CLI_NOME_AND_STATUS = "SELECT " +
            "o.id, " +
            "c.id id_cliente, " +
            "c.nome, " +
            "c.conjugue, " +
            "c.data_casamento, " +
            "c.telefone, " +
            "c.email, " +
            "c.cpf, " +
            "o.status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total "
            + " FROM orcamentos o," +
            "       clientes c " +
            "WHERE o.id_cliente = c.id " +
            "AND c.nome = ? " +
            "AND o.status = ? "
            + "ORDER BY c.nome";

    private final String SELECT_BY_STATUS = "SELECT " +
            "o.id, " +
            "c.id id_cliente, " +
            "c.nome, " +
            "c.conjugue, " +
            "c.data_casamento, " +
            "c.telefone, " +
            "c.email, " +
            "c.cpf, " +
            "o.status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total "
            + " FROM orcamentos o," +
            "       clientes c " +
            "WHERE o.id_cliente = c.id " +
            "AND o.status = ? "
            + "ORDER BY c.nome";

    private final String SELECT_TODES = "SELECT " +
            "o.id, " +
            "c.id id_cliente, " +
            "c.nome, " +
            "c.conjugue, " +
            "c.data_casamento, " +
            "c.telefone, " +
            "c.email, " +
            "c.cpf, " +
            "o.status, " +
            "o.observacoes, " +
            "o.data_criacao, " +
            "o.custo_estimado, " +
            "o.valor_total "
            + " FROM orcamentos o," +
            "       clientes c " +
            "WHERE o.id_cliente = c.id "
            + "ORDER BY o.id";

    private Connection conexao;

    public DaoPostgresOrcamento() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public Long inserir(Orcamento orcamento) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setLong(1, orcamento.getCliente().getId());
            ps.setString(2, String.valueOf(orcamento.getStatus()));
            ps.setString(3, orcamento.getObservaces());
            ps.setDate(4, Date.valueOf(orcamento.getDataCriacao()));
            ps.setBigDecimal(5, orcamento.getCustoEstimado());
            ps.setBigDecimal(6, orcamento.getValorTotal());

            rs = ps.executeQuery();
            if (rs.next()) {
                Long idGerado = rs.getLong("id");
                orcamento.setId(idGerado);
                return idGerado;
            } else {
                throw new RuntimeException("Falha ao inserir o orçamento: ID não encontrado após a inserção.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao inserir o orçamento. Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public void alterar(Orcamento orcamento) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setLong(1, orcamento.getCliente().getId());
            ps.setString(2, String.valueOf(orcamento.getStatus()));
            ps.setString(3, orcamento.getObservaces());
            ps.setDate(4, Date.valueOf(orcamento.getDataCriacao()));
            ps.setBigDecimal(5, orcamento.getCustoEstimado());
            ps.setBigDecimal(6, orcamento.getValorTotal());
            ps.setLong(7, orcamento.getId());
            boolean isAlteracaoOK = ps.executeUpdate() == 1;
            if (isAlteracaoOK) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao alterar o orçamento. Motivo: " + e.getMessage());
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
            throw new RuntimeException("Ocorreu um erro ao excluir o orçamento. Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public Orcamento buscarPor(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extrairDo(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao buscar o orçamento. Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public void atualizarValorTotal(Long idOrcamento, BigDecimal subtotal) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(UPDATE_VALOR_TOTAL);
            ps.setBigDecimal(1, subtotal);
            ps.setLong(2, idOrcamento);
            ps.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao atualizar o valor total do orçamento. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public List<Orcamento> listarPor(String nomeDoCliente, OrcamentoStatus status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Orcamento> orcamentos = new ArrayList<Orcamento>();
        try {
            ps = conexao.prepareStatement(SELECT_BY_CLI_NOME_AND_STATUS);
            ps.setString(1, nomeDoCliente);
            ps.setString(2, String.valueOf(status));
            rs = ps.executeQuery();
            while (rs.next()) {
                orcamentos.add(extrairDo(rs));
            }
            return orcamentos;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao listar os orcamentos. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public List<Orcamento> listarPor(OrcamentoStatus status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Orcamento> orcamentos = new ArrayList<Orcamento>();
        try {
            ps = conexao.prepareStatement(SELECT_BY_STATUS);
            ps.setString(1, String.valueOf(status));
            rs = ps.executeQuery();
            while (rs.next()) {
                orcamentos.add(extrairDo(rs));
            }
            return orcamentos;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao listar os orcamentos. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public List<Orcamento> listarPor(String nomeDoCliente) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Orcamento> orcamentos = new ArrayList<Orcamento>();
        try {
            ps = conexao.prepareStatement(SELECT_BY_CLI_NOME);
            ps.setString(1, nomeDoCliente);
            rs = ps.executeQuery();
            while (rs.next()) {
                orcamentos.add(extrairDo(rs));
            }
            return orcamentos;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao listar os orcamentos. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public List<Orcamento> listarTodos() {
        List<Orcamento> orcamentos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_TODES);
            rs = ps.executeQuery();
            while(rs.next()) {
                orcamentos.add(extrairDo(rs));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na listagem"
                    + " dos orcamentos. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
        return orcamentos;
    }

    private Orcamento extrairDo(ResultSet rs) {
        try {
            long idCliente = rs.getLong("id_cliente");
            String nomeDoCliente = rs.getString("nome");
            String conjugueDoCliente = rs.getString("conjugue");
            LocalDate dataDoCasamento = rs.getDate("data_casamento").toLocalDate();
            String telefone = rs.getString("telefone");
            String email = rs.getString("email");
            String cpf = rs.getString("cpf");
            Cliente cliente = new Cliente(idCliente, nomeDoCliente, conjugueDoCliente, dataDoCasamento, telefone, email, cpf);

            long idOrcamento = rs.getLong("id");
            OrcamentoStatus status = OrcamentoStatus.valueOf(rs.getString("status"));
            String observacoes = rs.getString("observacoes");
            LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
            BigDecimal custoEstimado = rs.getBigDecimal("custo_estimado");
            BigDecimal valorTotal = rs.getBigDecimal("valor_total");
            return new Orcamento(idOrcamento, cliente, status, dataCriacao, custoEstimado, valorTotal, observacoes);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao extrair o orçamento. Motivo: " + e.getMessage());
        }
    }
}

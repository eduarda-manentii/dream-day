package com.example.dreamday.dao.postgres;

import com.example.dreamday.dao.DaoOrcamento;
import com.example.dreamday.dao.ManagerDb;
import com.example.dreamday.domain.Cliente;
import com.example.dreamday.domain.Orcamento;
import com.example.dreamday.domain.OrcamentoStatus;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class DaoPostgresOrcamento implements DaoOrcamento {

    private final String INSERT = "INSERT INTO orcamentos (id_cliente, status, observacoes, data_criacao, custo_estimado, valor_total) VALUES (?, ?, ?, ?, ?, ?)";

    private final String UPDATE = "UPDATE orcamentos SET id_cliente = ?, status = ?, observacoes = ?, data_criacao = ?, custo_estimado = ? , valor_total = ? WHERE id = ?";

    private final String DELETE = "DELETE FROM orcamentos WHERE id = ?";

    private final String SELECT_BY_ID = "SELECT o.id, c.nome nome_cliente, o.status, o.observacoes, o.data_criacao, o.custo_estimado, o.valor_total"
            + " FROM orcamentos o," +
            "       clientes c "
            + " WHERE orcamentos.id_cliente = c.id " +
            "   AND o.id = ? ";

    private Connection conexao;

    public DaoPostgresOrcamento() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public void inserir(Orcamento orcamento) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setString(1, orcamento.getCliente().getNome());
            ps.setString(2, String.valueOf(orcamento.getStatus()));
            ps.setString(3, orcamento.getObservaces());
            ps.setDate(4, Date.valueOf(orcamento.getDataCriacao()));
            ps.setBigDecimal(5, orcamento.getCustoEstimado());
            ps.setBigDecimal(6, orcamento.getValorTotal());
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao inserir o orçamento. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void alterar(Orcamento orcamento) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setString(1, orcamento.getCliente().getNome());
            ps.setString(2, String.valueOf(orcamento.getStatus()));
            ps.setString(3, orcamento.getObservaces());
            ps.setDate(4, Date.valueOf(orcamento.getDataCriacao()));
            ps.setBigDecimal(5, orcamento.getCustoEstimado());
            ps.setBigDecimal(6, orcamento.getValorTotal());
            ps.setLong(7, orcamento.getId());
            boolean isAlteracaoOK = ps.executeUpdate() == 1;
            if (isAlteracaoOK) {
                this.conexao.commit();
            }else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao alterar o orçamento. "
                    + "Motivo: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void excluirPor(Long id) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutocommitDa(conexao, false);
            ps = conexao.prepareStatement(DELETE);
            ps.setInt(1, id);
            boolean isExclusaoOK = ps.executeUpdate() == 1;
            if (isExclusaoOK) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutocommitDa(conexao, true);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao excluir o orçamento."
                    + "Motivo: " + e.getMessage());
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
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extrairDo(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao buscar o orçamento. "
                    + "Motivo: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    private Orcamento extrairDo(ResultSet rs) {
        try {
            long idCliente = rs.getLong("id_cliente");;
            String nomeDoCliente = rs.getString("nome_cliente");
            String conjugueDoCliente = rs.getString("conjugue_cliente");
            LocalDate dataDoCasamento = rs.getDate("data_casamento").toLocalDate();
            String telefone = rs.getString("telefone");
            String email = rs.getString("email");
            String cpf = rs.getString("cpf");
            Cliente cliente = new Cliente(idCliente, nomeDoCliente, conjugueDoCliente, dataDoCasamento, telefone, email, cpf);

            OrcamentoStatus status = OrcamentoStatus.valueOf(rs.getString("status"));
            String observacoes = rs.getString("observacoes");
            LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
            BigDecimal custoEstimado = rs.getBigDecimal("custo_estimado");
            BigDecimal valorTotal = rs.getBigDecimal("valor_total");
            return new Orcamento(cliente, status, dataCriacao, custoEstimado, valorTotal, observacoes);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao extrair o orçamento. Motivo: " + e.getMessage());
        }
    }

}

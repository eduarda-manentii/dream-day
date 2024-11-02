package com.br.dreamday.dao.postgres;

import com.br.dreamday.dao.DaoParcelamento;
import com.br.dreamday.dao.ManagerDb;
import com.br.dreamday.domain.*;

import javax.xml.transform.Result;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DaoPostgresParcelamento implements DaoParcelamento {

    private Connection conexao;

    private final String INSERT = "INSERT INTO parcelamentos (" +
            "id_orcamento, " +
            "valor, " +
            "data_vencimento, " +
            "data_pagamento, " +
            "status, " +
            "observacao," +
            "qtde_parcelas) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?)";

    private final String UPDATE = "UPDATE parcelamentos " +
            "SET " +
            "id_orcamento = ?, " +
            "valor = ?," +
            "data_vencimento = ?, " +
            "data_pagamento = ?, " +
            "status = ?, " +
            "observacao = ? " +
            "qtde_parcelas = ? " +
            "WHERE id = ?";

    private final String DELETE = "DELETE FROM parcelamentos WHERE id = ?";


    private final String SELECT_BY_ID = "SELECT " +
            "parcelamentos.id, " +
            "parcelamentos.id_orcamento, " +
            "parcelamentos.valor, " +
            "parcelamentos.data_vencimento, " +
            "parcelamentos.data_pagamento, " +
            "parcelamentos.status as parcelas_status, " +
            "parcelamentos.observacao, " +
            "parcelamentos.qtde_parcelas, " +
            "orcamentos.id, " +
            "orcamentos.id_cliente, " +
            "orcamentos.status as orcamentos_status, " +
            "orcamentos.observacoes, " +
            "orcamentos.data_criacao, " +
            "orcamentos.custo_estimado, " +
            "orcamentos.valor_total, " +
            "clientes.id, " +
            "clientes.nome, " +
            "clientes.conjugue, " +
            "clientes.data_casamento, " +
            "clientes.telefone, " +
            "clientes.email, " +
            "clientes.cpf " +
            "FROM " +
            "parcelamentos " +
            "JOIN orcamentos ON parcelamentos.id_orcamento = orcamentos.id " +
            "JOIN clientes ON orcamentos.id_cliente = clientes.id " +
            "WHERE " +
            "parcelamentos.id = ?";


    private PreparedStatement ps;

    public DaoPostgresParcelamento() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public void inserir(Parcelamento parcelamento) {
        ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            preparar(parcelamento);
            ps.execute();

        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao inserir o parcelamento: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void alterar(Parcelamento parcelamento) {
        ps = null;
        try {
            desativaAutocommit();
            ps = conexao.prepareStatement(UPDATE);
            preparar(parcelamento);
            tentaExecutarTransacao();
            ativaAutocmmit();
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao alterar o parcelamento: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void excluirPor(Long id) {
        ps = null;
        try {
            desativaAutocommit();
            ps = conexao.prepareStatement(DELETE);
            ps.setLong(1, id);
            tentaExecutarTransacao();
            ativaAutocmmit();
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao excluir o parcelamento: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public Parcelamento buscarPor(Long id) {
        ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extrairParcelamento(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao buscar o parcelamento: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    private Parcelamento extrairParcelamento(ResultSet rs) {
        try {
            Long id = rs.getLong("id");
            BigDecimal valor = rs.getBigDecimal("valor");
            LocalDate dataVencimento = rs.getDate("data_vencimento").toLocalDate();
            LocalDate dataPagamento = rs.getDate("data_pagamento").toLocalDate();
            ParcelamentoStatus status = ParcelamentoStatus.valueOf(rs.getString("parcelas_status"));
            String observacao = rs.getString("observacao");
            Integer qtdeParcelas = rs.getInt("qtde_parcelas");
            Orcamento orcamento = extrairOrcamento(rs);
            return new Parcelamento(id, orcamento, valor, dataVencimento, dataPagamento, status, observacao, qtdeParcelas);
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao extrair o orçamento. Motivo: " + e.getMessage());
        }
    }

    private Orcamento extrairOrcamento(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        OrcamentoStatus status = OrcamentoStatus.valueOf(rs.getString("orcamentos_status"));
        String observacoes = rs.getString("observacoes");
        LocalDate dataCriacao = rs.getDate("data_criacao").toLocalDate();
        BigDecimal custoEstimado = rs.getBigDecimal("custo_estimado");
        BigDecimal valorTotal = rs.getBigDecimal("valor_total");
        Cliente cliente = extrairCliente(rs);
        return new Orcamento(id, cliente, status, dataCriacao, custoEstimado, valorTotal, observacoes);
    }

    private Cliente extrairCliente(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String conjugue = rs.getString("conjugue");
        LocalDate dataCasamento = rs.getDate("data_casamento").toLocalDate();
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String cpf = rs.getString("cpf");
        return new Cliente(id, nome, conjugue, dataCasamento, telefone, email, cpf);
    }


    private void preparar(Parcelamento parcelamento) throws SQLException {
        ps.setLong(1, parcelamento.getOrcamento().getId());
        ps.setBigDecimal(2, parcelamento.getValor());
        ps.setDate(3, Date.valueOf(parcelamento.getDataVencimento()));
        if (parcelamento.getDataPagamento() != null) {
            ps.setDate(4, Date.valueOf(parcelamento.getDataPagamento()));
        } else {
            ps.setNull(4, Types.DATE);
        }

        ps.setString(5, parcelamento.getStatus().toString());
        ps.setString(6, parcelamento.getObservacao());
        ps.setInt(7, parcelamento.getQtdeParcelas());
        if (parcelamento.getId() != null) {
            ps.setLong(8, parcelamento.getId());
        }
    }

    private void tentaExecutarTransacao() throws SQLException {
        if (ps.executeUpdate() == 1) {
            conexao.commit();
        }else {
            conexao.rollback();
        }
    }

    private void desativaAutocommit() {
        ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
    }

    private void ativaAutocmmit() {
        ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
    }

}

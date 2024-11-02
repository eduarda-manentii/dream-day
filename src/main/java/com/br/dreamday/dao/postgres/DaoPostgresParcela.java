package com.br.dreamday.dao.postgres;

import com.br.dreamday.dao.DaoParcela;
import com.br.dreamday.dao.DaoParcelamento;
import com.br.dreamday.dao.ManagerDb;
import com.br.dreamday.domain.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DaoPostgresParcela implements DaoParcela {

    private Connection conexao;

    private final String INSERT = "INSERT INTO parcelas" +
            "(valor, observacao, parcelamento_id) " +
            "VALUES(?, ?, ?);";

    private final String UPDATE = "UPDATE parcelas " +
            "SET " +
            "valor = ?, " +
            "observacao = ?," +
            "parcelamento_id = ? " +
            "WHERE id = ?";

    private final String DELETE = "DELETE FROM parcelas WHERE id = ?";

    private final String SELECT_BY_ID =  "SELECT " +
            "p.id AS parcela_id, " +
            "p.valor AS parcela_valor, " +
            "p.observacao AS parcela_observacao, " +
            "par.id AS parcelamento_id, " +
            "par.valor AS parcelamento_valor, " +
            "par.data_vencimento AS parcelamento_data_vencimento, " +
            "par.data_pagamento AS parcelamento_data_pagamento, " +
            "par.status AS parcelamento_status, " +
            "par.observacao AS parcelamento_observacao, " +
            "par.qtde_parcelas AS parcelamento_qtde_parcelas, " +
            "o.id AS orcamento_id, " +
            "o.status AS orcamento_status, " +
            "o.observacoes AS orcamento_observacoes, " +
            "o.data_criacao AS orcamento_data_criacao, " +
            "o.custo_estimado AS orcamento_custo_estimado, " +
            "o.valor_total AS orcamento_valor_total, " +
            "c.id AS cliente_id, " +
            "c.nome AS cliente_nome, " +
            "c.conjugue AS cliente_conjugue, " +
            "c.data_casamento AS cliente_data_casamento, " +
            "c.telefone AS cliente_telefone, " +
            "c.email AS cliente_email, " +
            "c.cpf AS cliente_cpf " +
            "FROM parcelas p " +
            "JOIN parcelamentos par ON p.parcelamento_id = par.id " +
            "JOIN orcamentos o ON par.id_orcamento = o.id " +
            "JOIN clientes c ON c.id = o.id_cliente " +
            "WHERE p.id = ?;";


    private PreparedStatement ps;

    public DaoPostgresParcela() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public void inserir(Parcela parcela) {
        ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            preparar(parcela);
            ps.execute();
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao inserir o parcelamento: " + e.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void alterar(Parcela parcela) {
        ps = null;
        try {
            desativaAutocommit();
            ps = conexao.prepareStatement(UPDATE);
            preparar(parcela);
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
    public Parcela buscarPor(Long id) {
        ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_BY_ID);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extrairParcela(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao buscar o parcelamento: " + e.getMessage());
        }finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    private Parcela extrairParcela(ResultSet rs) throws SQLException {
        Long id = rs.getLong("parcela_id");
        BigDecimal valor = rs.getBigDecimal("parcela_valor");
        String observacao = rs.getString("parcela_observacao");
        return new Parcela(id, valor, observacao, extrairParcelamento(rs));
    }


    private Parcelamento extrairParcelamento(ResultSet rs) throws SQLException {
        Long id = rs.getLong("parcelamento_id");
        BigDecimal valor = rs.getBigDecimal("parcelamento_valor");
        LocalDate dataVencimento = rs.getDate("parcelamento_data_vencimento").toLocalDate();
        LocalDate dataPagamento = rs.getDate("parcelamento_data_pagamento").toLocalDate();
        ParcelamentoStatus status = ParcelamentoStatus.valueOf(rs.getString("parcelamento_status"));
        String observacao = rs.getString("parcelamento_observacao");
        Integer qtdeParcelas = rs.getInt("parcelamento_qtde_parcelas");
        Orcamento orcamento = extrairOrcamento(rs);
        return new Parcelamento(id, orcamento, valor, dataVencimento, dataPagamento, status, observacao, qtdeParcelas);
    }

    private Orcamento extrairOrcamento(ResultSet rs) throws SQLException {
        Long id = rs.getLong("orcamento_id");
        OrcamentoStatus status = OrcamentoStatus.valueOf(rs.getString("orcamento_status"));
        String observacoes = rs.getString("orcamento_observacoes");
        LocalDate dataCriacao = rs.getDate("orcamento_data_criacao").toLocalDate();
        BigDecimal custoEstimado = rs.getBigDecimal("orcamento_custo_estimado");
        BigDecimal valorTotal = rs.getBigDecimal("orcamento_valor_total");

        Cliente cliente = extrairCliente(rs);
        return new Orcamento(id, cliente, status, dataCriacao, custoEstimado, valorTotal, observacoes);
    }

    private Cliente extrairCliente(ResultSet rs) throws SQLException {
        Long id = rs.getLong("cliente_id");
        String nome = rs.getString("cliente_nome");
        String conjugue = rs.getString("cliente_conjugue");
        LocalDate dataCasamento = rs.getDate("cliente_data_casamento").toLocalDate();
        String telefone = rs.getString("cliente_telefone");
        String email = rs.getString("cliente_email");
        String cpf = rs.getString("cliente_cpf");
        return new Cliente(id, nome, conjugue, dataCasamento, telefone, email, cpf);
    }


    private void preparar(Parcela parcela) throws SQLException {
        ps.setBigDecimal(1, parcela.getValor());
        ps.setString(2, parcela.getObservacao());
        ps.setLong(3, parcela.getParcelamento().getId());
        if (parcela.getId() != null) {
            ps.setLong(4, parcela.getId());
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

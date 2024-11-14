package com.br.dreamday.dao.postgres;

import com.br.dreamday.domain.*;
import com.br.dreamday.dao.DaoItemFornecedor;
import com.br.dreamday.dao.ManagerDb;
import com.br.dreamday.domain.key.ItemFornecedorKey;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class DaoPostgresqlItemFornecedor implements DaoItemFornecedor {

    private final String INSERT = "INSERT INTO itens_fornecedores (" +
            "id_fornecedor, " +
            "id_produto, " +
            "preco, " +
            "id_categoria) " +
            "VALUES (?, ?, ?, ?)";

    private final String UPDATE = "UPDATE itens_fornecedores SET "
            + "preco = ?, "
            + "id_categoria = ? "
            + "WHERE id_fornecedor = ? and id_produto = ?";

    private final String DELETE = "DELETE FROM itens_fornecedores WHERE id_fornecedor = ? and id_produto = ?";

    private final String SELECT_BY_ID_FORN = "SELECT "
            + "p.id id_produto, "
            + "p.nome nome_produto, "
            + "c.id id_categoria, "
            + "c.nome nome_categoria, "
            + "ifs.id_fornecedor, "
            + "ifs.id_produto, "
            + "ifs.preco "
            + "FROM itens_fornecedores ifs "
            + "join categorias c on c.id = ifs.id_categoria "
            + "join produtos p on p.id = ifs.id_produto "
            + "WHERE ifs.id_fornecedor = ? "
            + "ORDER BY p.nome ";


    private final String SELECT_BY_NOME_PRECO_FORN = "SELECT "
            + "p.id AS id_produto, "
            + "p.nome AS nome_produto, "
            + "f.id AS id_fornecedor, "
            + "f.nome AS nome_fornecedor, "
            + "c.id AS id_categoria, "
            + "c.nome AS nome_categoria, "
            + "if.id_fornecedor, "
            + "if.id_produto, "
            + "if.preco "
            + "FROM itens_fornecedores if "
            + "JOIN categorias c ON c.id = if.id_categoria "
            + "JOIN fornecedores f ON f.id = if.id_fornecedor "
            + "JOIN produtos p ON p.id = if.id_produto "
            + "WHERE UPPER(p.nome) LIKE UPPER(?)";

    private final String SELECT_TODOS = "SELECT "
            + "ifs.id_fornecedor, "
            + "f.nome AS nome_fornecedor, "
            + "p.id AS id_produto, "
            + "p.nome AS nome_produto, "
            + "c.id AS id_categoria, "
            + "c.nome AS nome_categoria, "
            + "ifs.preco "
            + "FROM itens_fornecedores ifs "
            + "JOIN fornecedores f ON f.id = ifs.id_fornecedor "
            + "JOIN produtos p ON p.id = ifs.id_produto "
            + "JOIN categorias c ON c.id = ifs.id_categoria "
            + "ORDER BY LOWER(f.nome)";

    private final String SELECT_ID_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_categoria) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_fornecedor = ? AND itens_fornecedores.id_produto = ?";

    private final String SELECT_ID_CATEG_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_categoria) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_categoria = ?";

    private final String SELECT_ID_PROD_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_produto) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_produto = ?";

    private final String SELECT_ID_FORN_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_fornecedor) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_fornecedor = ?";

    private final String SELECT_ITEM_DUPLICADO = "SELECT EXISTS ("
            + "SELECT 1 "
            + "FROM itens_fornecedores i "
            + "WHERE i.id_produto = ? "
            + "AND i.id_fornecedor = ? "
            + ") AS existe";

    private final Connection conexao;

    public DaoPostgresqlItemFornecedor() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public void inserir(ItemFornecedor itemFornecedor) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setLong(1, itemFornecedor.getFornecedor().getId());
            ps.setLong(2, itemFornecedor.getProduto().getId());
            ps.setBigDecimal(3, itemFornecedor.getPreco());
            ps.setLong(4, itemFornecedor.getCategoria().getId());
            ps.execute();
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na inserção do item." + " Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }
    }

    @Override
    public void alterar(ItemFornecedor itemFornecedor) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(UPDATE);
            ps.setBigDecimal(1, itemFornecedor.getPreco());
            ps.setLong(2, itemFornecedor.getCategoria().getId());
            ps.setLong(3, itemFornecedor.getFornecedor().getId());
            ps.setLong(4, itemFornecedor.getProduto().getId());

            boolean isAlteracaoOK = ps.executeUpdate() == 1;
            if (isAlteracaoOK) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na alteração do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }

    }

    @Override
    public void excluirPor(Long idFornecedor, Long idProduto) {
        PreparedStatement ps = null;
        try {
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, false);
            ps = conexao.prepareStatement(DELETE);
            ps.setLong(1, idFornecedor);
            ps.setLong(2, idProduto);
            boolean isExclusaoOk = ps.executeUpdate() == 1;
            if (isExclusaoOk) {
                this.conexao.commit();
            } else {
                this.conexao.rollback();
            }
            ManagerDb.getInstance().configurarAutoCommitDa(conexao, true);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao excluir o item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
        }

    }

    @Override
    public List<ItemFornecedor> listarPor(String nomeProduto, String nomeFornecedor, BigDecimal valorInicial, BigDecimal valorFinal) {
        List<ItemFornecedor> itens = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            StringBuilder consulta = new StringBuilder(SELECT_BY_NOME_PRECO_FORN);

            consulta.append("AND Upper(f.nome) LIKE Upper(?) ");
            consulta.append("AND if.preco >= ? ");
            consulta.append("AND if.preco <= ? ");
            consulta.append("ORDER BY p.nome ");
            ps = conexao.prepareStatement(consulta.toString());

            ps.setString(1, nomeProduto);
            ps.setString(2, nomeFornecedor);
            ps.setBigDecimal(3, valorInicial);
            ps.setBigDecimal(4, valorFinal);

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
    public List<ItemFornecedor> listarPor(Long idFornecedor) {
        List<ItemFornecedor> itens = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = conexao.prepareStatement(SELECT_BY_ID_FORN);
            ps.setLong(1, idFornecedor);
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
    public List<ItemFornecedor> listarTodos() {
        List<ItemFornecedor> itensFornecedores = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_TODOS);
            rs = ps.executeQuery();
            while(rs.next()) {
                itensFornecedores.add(extrairDo(rs));
            }
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro na listagem"
                    + " dos itens de fornecedores. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
        return itensFornecedores;
    }

    @Override
    public boolean validarEdicao(Long idFornecedor, Long idProduto) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_ID_EXISTENTE);
            ps.setLong(1, idFornecedor);
            ps.setLong(2, idProduto);
            boolean isValidaoOk = false;
            rs = ps.executeQuery();
            if (rs.next()) {
                isValidaoOk = rs.getInt("qtde") > 0;
            }
            return isValidaoOk;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro na validação " + "de id para edição do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public boolean validarItemDuplicado(Long idProduto, Long idFornecedor) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_ITEM_DUPLICADO);
            ps.setLong(1, idProduto);
            ps.setLong(2, idFornecedor);
            boolean isValidaoOk = false;
            rs = ps.executeQuery();
            if (rs.next()) {
                isValidaoOk = rs.getBoolean("existe");
            }
            return isValidaoOk;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro na validação de id para edição do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public boolean validarRemocaoProduto(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_ID_PROD_EXISTENTE);
            ps.setLong(1, id);
            boolean isValidaoOk = false;
            rs = ps.executeQuery();
            if (rs.next()) {
                isValidaoOk = rs.getInt("qtde") > 0;
            }
            return isValidaoOk;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro na validação " + "de produto para remoção do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public boolean validarRemocaoCategoria(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_ID_CATEG_EXISTENTE);
            ps.setLong(1, id);
            boolean isValidaoOk = false;
            rs = ps.executeQuery();
            if (rs.next()) {
                isValidaoOk = rs.getInt("qtde") > 0;
            }
            return isValidaoOk;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro na validação " + "de categoria para remoção do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    @Override
    public boolean validarRemocaoFornecedor(Long id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conexao.prepareStatement(SELECT_ID_FORN_EXISTENTE);
            ps.setLong(1, id);
            boolean isValidaoOk = false;
            rs = ps.executeQuery();
            if (rs.next()) {
                isValidaoOk = rs.getInt("qtde") > 0;
            }
            return isValidaoOk;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro na validação " + "de fornecedor para remoção do item. Motivo: " + ex.getMessage());
        } finally {
            ManagerDb.getInstance().fechar(ps);
            ManagerDb.getInstance().fechar(rs);
        }
    }

    private ItemFornecedor extrairDo(ResultSet rs) {
        try {
            Long idProduto = rs.getLong("id_produto");
            String nomeProduto = rs.getString("nome_produto");

            Long idFornecedor = rs.getLong("id_fornecedor");

            String nomeFornecedor = null;

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                if ("nome_fornecedor".equalsIgnoreCase(metaData.getColumnName(i))) {
                    nomeFornecedor = rs.getString("nome_fornecedor");
                    break;
                }
            }

            Long idCategoria = rs.getLong("id_categoria");
            String nomeCategoria = rs.getString("nome_categoria");

            BigDecimal preco = rs.getBigDecimal("preco");

            Categoria categoria = new Categoria(idCategoria, nomeCategoria);
            Produto produto = new Produto(idProduto, nomeProduto);

            Fornecedor fornecedor;

            if (idFornecedor > 0L) {
                fornecedor = new Fornecedor(idFornecedor, nomeFornecedor);
            } else {
                fornecedor = new Fornecedor();
            }

            return new ItemFornecedor(idFornecedor, idProduto, preco, categoria, fornecedor, produto);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao " + "extrair o item. Motivo: " + ex.getMessage());
        }
    }

}
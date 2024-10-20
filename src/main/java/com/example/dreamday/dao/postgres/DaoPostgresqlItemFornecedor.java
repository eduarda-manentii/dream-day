package com.example.dreamday.dao.postgres;

import com.example.dreamday.dao.DaoItemFornecedor;
import com.example.dreamday.dao.DaoProduto;
import com.example.dreamday.dao.ManagerDb;
import com.example.dreamday.domain.Categoria;
import com.example.dreamday.domain.Fornecedor;
import com.example.dreamday.domain.ItemFornecedor;
import com.example.dreamday.domain.Produto;
import com.example.dreamday.domain.key.ItemFornecedorKey;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            + "id_fornecedor = ?, "
            + "id_produto = ?, "
            + "preco = ?, "
            + "id_categoria = ?, "
            + "WHERE id_fornecedor = ? and id_produto = ?";

    private final String DELETE = "DELETE FROM itens_fornecedores WHERE id_fornecedor = ? and id_produto = ?";

    private final String SELECT_BY_NOME_PRECO_FORN = "SELECT "
            + "p.id id_produto, "
            + "p.nome nome_produto, "
            + "f.id id_fornecedor "
            + "f.nome nome_fornecedor "
            + "c.id id_categoria "
            + "c.nome nome_categoria "
            + "if.id_fornecedor "
            + "if.id_produto "
            + "if.preco, "
            + "FROM itens_fornecedores if, "
            + "join categorias c on c.id = if.id_categoria "
            + "join fornecedores f on f.id = if.id_fornecedor "
            + "join produtos p on p.id = if.id_produto "
            + "WHERE Upper(p.nome) LIKE Upper(?) "
            + "ORDER BY for.nome ";

    private final String SELECT_ID_CATEG_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_categoria) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_categoria = ?";

    private final String SELECT_ID_PROD_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_produto) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_produto = ?";

    private final String SELECT_ID_FORN_EXISTENTE = "SELECT COUNT (itens_fornecedores.id_produto) as qtde "
            + "FROM itens_fornecedores " + "WHERE itens_fornecedores.id_produto = ?";

    private Connection conexao;

    public DaoPostgresqlItemFornecedor() {
        this.conexao = ManagerDb.getInstance().getConexao();
    }

    @Override
    public void inserir(ItemFornecedor itemFornecedor) {
        PreparedStatement ps = null;
        try {
            ps = conexao.prepareStatement(INSERT);
            ps.setLong(1, itemFornecedor.getId().getIdFornecedor());
            ps.setLong(2, itemFornecedor.getId().getIdProduto());
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
            ps.setLong(1, itemFornecedor.getId().getIdFornecedor());
            ps.setLong(2, itemFornecedor.getId().getIdProduto());
            ps.setBigDecimal(3, itemFornecedor.getPreco());
            ps.setLong(4, itemFornecedor.getCategoria().getId());
            ps.setLong(5, itemFornecedor.getId().getIdFornecedor());
            ps.setLong(6, itemFornecedor.getId().getIdProduto());

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
            if (!nomeFornecedor.isBlank()) {
                consulta.append("AND Upper(f.nome) LIKE Upper(?) ");
            }

            if (valorInicial != null) {
                consulta.append("AND if.preco >= ? ");
            }

            if (valorFinal != null) {
                consulta.append("AND if.preco <= ? ");
            }

            consulta.append(" ORDER BY p.nome ");

            ps = conexao.prepareStatement(consulta.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                itens.add(extrairDo(rs));
            }
            return itens;
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Ocorreu um erro ao listar o " + "nome do restaurante. Motivo: " + ex.getMessage());
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
                    "Ocorreu um erro na validação " + "de id para remoção da categoria. Motivo: " + ex.getMessage());
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
                    "Ocorreu um erro na validação " + "de id para remoção da categoria. Motivo: " + ex.getMessage());
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
                    "Ocorreu um erro na validação " + "de id para remoção da categoria. Motivo: " + ex.getMessage());
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
            String nomeFornecedor = rs.getString("nome_fornecedor");

            Long idCategoria = rs.getLong("id_fornecedor");
            String nomeCategoria = rs.getString("nome_fornecedor");

            BigDecimal preco = rs.getBigDecimal("preco");

            Categoria categoria = new Categoria(idCategoria, nomeCategoria);
            Produto produto = new Produto(idProduto, nomeProduto);
            Fornecedor fornecedor = new Fornecedor(idFornecedor, nomeFornecedor);

            ItemFornecedorKey id = new ItemFornecedorKey(idFornecedor, idProduto);

            return new ItemFornecedor(id, preco, categoria, fornecedor, produto);
        } catch (Exception ex) {
            throw new RuntimeException("Ocorreu um erro ao " + "extrair o item. Motivo: " + ex.getMessage());
        }
    }

}
package pedidopronto.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pedidopronto.model.MetodoPagamento;
import pedidopronto.model.Pedido;
import pedidopronto.model.Produto;

public class PedidoRepository {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/SQLdb";
        String user = "root";
        String password = "nova_senha";
        return DriverManager.getConnection(url, user, password);
    }

    public void create(Pedido pedido) {
        String insertPedidoSql = "INSERT INTO cad_pedido (status, total, metodoPagamentoId) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(insertPedidoSql,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setBoolean(1, pedido.isStatus());
            statement.setDouble(2, pedido.getTotal());
            statement.setInt(3, pedido.getMetodoPagamento().getId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idPedido = generatedKeys.getInt(1);
                pedido.setId(idPedido);

                // Adicionar produtos ao pedido na tabela de associação
                String insertPedidoProdutoSql = "INSERT INTO cad_pedido_produto (idPedido, idProduto, quantidade) VALUES (?, ?, ?)";
                for (Produto produto : pedido.getListaProdutos()) {
                    try (PreparedStatement produtoStatement = conn.prepareStatement(insertPedidoProdutoSql)) {
                        produtoStatement.setInt(1, idPedido);
                        produtoStatement.setInt(2, produto.getId());
                        produtoStatement.setInt(3, 1); // Ajuste conforme sua lógica de quantidade
                        produtoStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> read() {
        String selectPedidoSql = "SELECT * FROM cad_pedido";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(selectPedidoSql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                boolean status = resultSet.getBoolean("status");
                double total = resultSet.getDouble("total");
                int metodoPagamentoId = resultSet.getInt("metodoPagamentoId");

                // Buscar MetodoPagamento pelo ID
                MetodoPagamentoRepository metodoPagamentoRepository = new MetodoPagamentoRepository();
                MetodoPagamento metodoPagamento = metodoPagamentoRepository.findById(metodoPagamentoId);

                // Buscar produtos associados ao pedido
                List<Produto> produtos = buscarProdutosPorPedido(id);

                // Criar uma instância de Pedido apenas para adicionar à lista (sem criar um
                // novo objeto Pedido)
                pedidos.add(new Pedido(id, status, total, metodoPagamento));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    public void update(Pedido pedido) {
        String updatePedidoSql = "UPDATE cad_pedido SET status = ?, total = ?, metodoPagamentoId = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(updatePedidoSql)) {

            statement.setBoolean(1, pedido.isStatus());
            statement.setDouble(2, pedido.getTotal());
            statement.setInt(3, pedido.getMetodoPagamento().getId());
            statement.setInt(4, pedido.getId());
            statement.executeUpdate();

            // Atualizar lista de produtos do pedido na tabela de associação, se necessário
            String deleteProdutosSql = "DELETE FROM cad_pedido_produto WHERE idPedido = ?";
            try (PreparedStatement deleteStatement = conn.prepareStatement(deleteProdutosSql)) {
                deleteStatement.setInt(1, pedido.getId());
                deleteStatement.executeUpdate();
            }

            String insertProdutosSql = "INSERT INTO cad_pedido_produto (idPedido, idProduto, quantidade) VALUES (?, ?, ?)";
            for (Produto produto : pedido.getListaProdutos()) {
                try (PreparedStatement insertStatement = conn.prepareStatement(insertProdutosSql)) {
                    insertStatement.setInt(1, pedido.getId());
                    insertStatement.setInt(2, produto.getId());
                    insertStatement.setInt(3, 1); // Ajuste conforme sua lógica de quantidade
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String deletePedidoSql = "DELETE FROM cad_pedido WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(deletePedidoSql)) {

            // Remover entradas relacionadas na tabela de associação de produtos
            String deleteProdutosSql = "DELETE FROM cad_pedido_produto WHERE idPedido = ?";
            try (PreparedStatement deleteStatement = conn.prepareStatement(deleteProdutosSql)) {
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
            }

            // Deletar o pedido
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MetodoPagamento findByName(String nome) {
        String sql = "SELECT * FROM cad_metodo_pagamento WHERE nome = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    // Outros campos, se necessário

                    return new MetodoPagamento(id, nome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Produto> buscarProdutosPorPedido(int idPedido) {
        String sql = "SELECT * FROM cad_produto_pedido WHERE idPedido = ?";

        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, idPedido);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int idProduto = resultSet.getInt("idProduto");
                    // Outros campos, se necessário

                    // Aqui você precisaria buscar o produto pelo ID
                    ProdutoRepository produtoRepository = new ProdutoRepository();

                    Produto produto = produtoRepository.findById(idProduto);
                            produtos.add(produto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }
}

package pedidopronto.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pedidopronto.model.CategoriaProduto;
import pedidopronto.model.ItemPedido;
import pedidopronto.model.MetodoPagamento;
import pedidopronto.model.Pedido;
import pedidopronto.model.Produto;

public class PedidoRepository {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/PedidoProntoDB";
        String user = "root";
        String password = "Jon@s1541";
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

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idPedido = generatedKeys.getInt(1);
                    pedido.setId(idPedido);

                    // Agora insira os produtos associados ao pedido
                    String insertPedidoProdutoSql = "INSERT INTO cad_pedido_produto (idPedido, idProduto, quantidade) VALUES (?, ?, ?)";
                    try (PreparedStatement produtoStatement = conn.prepareStatement(insertPedidoProdutoSql)) {
                        for (ItemPedido item : pedido.getListaItensPedidos()) {
                            produtoStatement.setInt(1, idPedido);
                            produtoStatement.setInt(2, item.getProduto().getId());
                            produtoStatement.setInt(3, item.getQuantidade());
                            produtoStatement.executeUpdate();
                        }
                    }
                } else {
                    throw new SQLException("Falha ao obter o ID gerado para o pedido.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> read() {
        String sql = "SELECT * FROM cad_pedido";
        List<Pedido> pedidos = new ArrayList<>();
    
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
    
            MetodoPagamentoRepository metodoPagamentoRepo = new MetodoPagamentoRepository();
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                boolean status = resultSet.getBoolean("status");
                double total = resultSet.getDouble("total");
                int metodoPagamentoId = resultSet.getInt("metodoPagamentoId");
    
                MetodoPagamento metodoPagamento = metodoPagamentoRepo.findById(metodoPagamentoId);
    
                Pedido pedido = new Pedido(id, status, total, metodoPagamento);
    
                // Adicionar itens do pedido
                List<ItemPedido> itensPedido = buscarProdutosPorPedido(id);
                for (ItemPedido item : itensPedido) {
                    pedido.addItemPedido(item);
                }
    
                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
    

    private List<ItemPedido> buscarProdutosPorPedido(int idPedido) {
        List<ItemPedido> itensPedido = new ArrayList<>();
        String sql = "SELECT p.*, pp.quantidade FROM cad_produto AS p " +
                "JOIN cad_pedido_produto AS pp ON p.id = pp.idProduto " +
                "WHERE pp.idPedido = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, idPedido);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nome = resultSet.getString("nome");
                    String descricao = resultSet.getString("descricao");
                    double preco = resultSet.getDouble("preco");
                    int categoriaId = resultSet.getInt("categoriaId");
                    int quantidade = resultSet.getInt("quantidade");

                    CategoriaProdutoRepository categoriaRepo = new CategoriaProdutoRepository();
                    CategoriaProduto categoria = categoriaRepo.findById(categoriaId);

                    Produto produto = new Produto(id, nome, descricao, preco, categoria);
                    ItemPedido itemPedido = new ItemPedido(produto, quantidade);

                    itensPedido.add(itemPedido);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itensPedido;
    }

    public void update(Pedido pedido) {
        String sql = "UPDATE cad_pedido SET status = ?, total = ?, metodoPagamentoId = ? WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setBoolean(1, pedido.isStatus());
            statement.setDouble(2, pedido.getTotal());
            statement.setInt(3, pedido.getMetodoPagamento().getId());
            statement.setInt(4, pedido.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Atualizar lista de produtos do pedido na tabela de associação
        // Remover entradas existentes
        String deleteProdutosSql = "DELETE FROM cad_pedido_produto WHERE idPedido = ?";
        try (Connection conn = getConnection();
                PreparedStatement deleteStatement = conn.prepareStatement(deleteProdutosSql)) {
            deleteStatement.setInt(1, pedido.getId());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Inserir entradas atualizadas
        String insertProdutosSql = "INSERT INTO cad_pedido_produto (idPedido, idProduto, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement insertStatement = conn.prepareStatement(insertProdutosSql)) {
            for (ItemPedido item : pedido.getListaItensPedidos()) {
                insertStatement.setInt(1, pedido.getId());
                insertStatement.setInt(2, item.getProduto().getId());
                insertStatement.setInt(3, item.getQuantidade());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM cad_pedido WHERE id = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Remover entradas relacionadas na tabela de associação de produtos, se
        // necessário
    }

    public Pedido findById(int id) {
        String sql = "SELECT * FROM cad_pedido WHERE id = ?";
        Pedido pedido = null;

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                boolean status = resultSet.getBoolean("status");
                double total = resultSet.getDouble("total");
                int metodoPagamentoId = resultSet.getInt("metodoPagamentoId");

                MetodoPagamentoRepository metodoPagamentoRepo = new MetodoPagamentoRepository();
                MetodoPagamento metodoPagamento = metodoPagamentoRepo.findById(metodoPagamentoId);

                pedido = new Pedido(id, status, total, metodoPagamento);

                // Adicionar itens do pedido
                List<ItemPedido> itensPedido = buscarProdutosPorPedido(id);
                for (ItemPedido item : itensPedido) {
                    pedido.addItemPedido(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pedido;
    }
}

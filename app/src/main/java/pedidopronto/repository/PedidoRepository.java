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
        String sql = "INSERT INTO cad_pedido (id, status, total, metodoPagamentoId) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, pedido.getId());
            statement.setBoolean(2, pedido.isStatus());
            statement.setDouble(3, pedido.getTotal());
            statement.setInt(4, pedido.getMetodoPagamento().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Adicionar produtos ao pedido em uma tabela de associação, se necessário
    }

    public List<Pedido> read() {
        String sql = "SELECT * FROM cad_pedido";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                boolean status = resultSet.getBoolean("status");
                double total = resultSet.getDouble("total");
                int metodoPagamentoId = resultSet.getInt("metodoPagamentoId");

                // Aqui você precisaria buscar o MetodoPagamento pelo ID
                MetodoPagamento metodoPagamento = // Buscar MetodoPagamento

                Pedido pedido = new Pedido(id, status, total, metodoPagamento);

                // Adicionar produtos ao pedido, se necessário

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
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

        // Atualizar lista de produtos do pedido, se necessário
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

        // Remover entradas relacionadas na tabela de associação de produtos, se necessário
    }
}

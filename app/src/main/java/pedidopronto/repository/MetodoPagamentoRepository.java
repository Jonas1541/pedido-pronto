package pedidopronto.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pedidopronto.model.CategoriaProduto;
import pedidopronto.model.MetodoPagamento;

public class MetodoPagamentoRepository {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/PedidoProntoDB";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    public void create(MetodoPagamento metodoPagamento) {
        String sql = "INSERT INTO cad_metodo_pagamento (nome) VALUES (?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, metodoPagamento.getNome());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MetodoPagamento> read() {
        String sql = "SELECT * FROM cad_metodo_pagamento";
        List<MetodoPagamento> metodos = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                metodos.add(new MetodoPagamento(id, nome));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metodos;
    }

    public void update(MetodoPagamento metodoPagamento) {
        String sql = "UPDATE cad_metodo_pagamento SET nome = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, metodoPagamento.getNome());
            statement.setInt(2, metodoPagamento.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM cad_metodo_pagamento WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MetodoPagamento findById(int id) {
        String sql = "SELECT * FROM cad_metodo_pagamento WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    return new MetodoPagamento(id, nome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MetodoPagamento findByName(String nome) {
        String sql = "SELECT * FROM cad_metodo_pagamento WHERE nome = ?";
        MetodoPagamento metodoPagamento = null;
    
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
    
            statement.setString(1, nome);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int metodoPagamentoId = resultSet.getInt("id");
                    String metodoPagamentoNome = resultSet.getString("nome");
                    metodoPagamento = new MetodoPagamento(metodoPagamentoId, metodoPagamentoNome);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return metodoPagamento;
    }
}

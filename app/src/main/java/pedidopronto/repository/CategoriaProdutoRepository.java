package pedidopronto.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pedidopronto.model.CategoriaProduto;

public class CategoriaProdutoRepository {

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/PedidoProntoDB";
        String user = "root";
        String password = "Jon@s1541";
        return DriverManager.getConnection(url, user, password);
    }

    public void create(CategoriaProduto categoriaProduto) {
        String sql = "INSERT INTO cad_categoria_produto (categoria) VALUES (?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, categoriaProduto.getCategoria());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CategoriaProduto> read() {
        String sql = "SELECT * FROM cad_categoria_produto";
        List<CategoriaProduto> categorias = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String categoria = resultSet.getString("categoria");
                categorias.add(new CategoriaProduto(id, categoria));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public void update(CategoriaProduto categoriaProduto) {
        String sql = "UPDATE cad_categoria_produto SET categoria = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, categoriaProduto.getCategoria());
            statement.setInt(2, categoriaProduto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM cad_categoria_produto WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public CategoriaProduto findById(int id) {
        String sql = "SELECT * FROM cad_categoria_produto WHERE id = ?";
        CategoriaProduto categoria = null;
    
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
    
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int categoriaId = resultSet.getInt("id");
                    String nomeCategoria = resultSet.getString("categoria");
                    categoria = new CategoriaProduto(categoriaId, nomeCategoria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoria;
    }

    public CategoriaProduto findByName(String nome) {
        String sql = "SELECT * FROM cad_categoria_produto WHERE categoria = ?";
        CategoriaProduto categoria = null;
    
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
    
            statement.setString(1, nome);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int categoriaId = resultSet.getInt("id");
                    String nomeCategoria = resultSet.getString("categoria");
                    categoria = new CategoriaProduto(categoriaId, nomeCategoria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoria;
    }
}


package pedidopronto.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pedidopronto.model.CategoriaProduto;
import pedidopronto.model.Produto; 

public class ProdutoRepository {

    private CategoriaProdutoRepository categoriaProdutoRepository = new CategoriaProdutoRepository();

    private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/PedidoProntoDB";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }

    public void create(Produto produto) {
        String sql = "INSERT INTO cad_produto (nome, descricao, preco, categoriaId) VALUES ( ?, ?, ?, ?)";

        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            statement.setDouble(3, produto.getPreco());
            statement.setInt(4, produto.getCategoria().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Produto> read() {
        String sql = "SELECT * FROM cad_produto";
        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                String descricao = resultSet.getString("descricao");
                double preco = resultSet.getDouble("preco");
                int categoriaId = resultSet.getInt("categoriaId");

                CategoriaProduto categoria = categoriaProdutoRepository.findById(categoriaId);

                produtos.add(new Produto(id, nome, descricao, preco, categoria));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public void update(Produto produto) {
        String sql = "UPDATE cad_produto SET nome = ?, descricao = ?, preco = ?, categoriaId = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            statement.setDouble(3, produto.getPreco());
            statement.setInt(4, produto.getCategoria().getId());
            statement.setInt(5, produto.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM cad_produto WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Produto findById(int id) {
        String sql = "SELECT * FROM cad_produto WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    String descricao = resultSet.getString("descricao");
                    double preco = resultSet.getDouble("preco");
                    int categoriaId = resultSet.getInt("categoriaId");

                    CategoriaProduto categoria = categoriaProdutoRepository.findById(categoriaId);

                    return new Produto(id, nome, descricao, preco, categoria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}


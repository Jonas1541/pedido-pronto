package pedidopronto.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.checkerframework.checker.units.qual.cd;

import com.google.gson.Gson;

import pedidopronto.model.CategoriaProduto;

public class CategoriaProdutoRepository {

    private final Connection connection;


    public CategoriaProdutoRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(CategoriaProduto categoriaProduto) {
        String sql = "INSERT INTO cad_categoria_produto (idProduto, categoria) VALUES (?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Define o id do produto
            statement.setInt(1, categoriaProduto.getId());
    
            // Define a categoria
            statement.setString(2, categoriaProduto.getCategoria());
    
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
      
}

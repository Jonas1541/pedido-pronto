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
            statement.setString(1, categoriaProduto);

            // Converte a lista de disciplinas em formato JSON
            Gson gson = new Gson();
            String disciplinasJSON = gson.toJson(estudante.getList());

            statement.setString(2, disciplinasJSON);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }  
}

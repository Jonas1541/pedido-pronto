package pedidopronto.controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import pedidopronto.database.MySQLDB;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pedidopronto.model.CategoriaProduto;

public class CategoriaProdutoController {

    private final Connection connection;

    public CategoriaProdutoController(Connection connection) {
        this.connection = connection;
    }

    public void createCategoriaProduto(int idProduto, String categoria) {
        CategoriaProduto categoriaProduto = new CategoriaProduto(idProduto, categoria);

        String sql = "INSERT INTO cad_categoria_produto (idProduto, categoria) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, estudante.getNome());

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

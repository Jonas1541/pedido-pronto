package pedidopronto.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDB {
   private static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/SQLdb";
        String user = "root";
        String password = "nova_senha";
        return DriverManager.getConnection(url, user, password);
    }
}

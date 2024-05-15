import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Cria a conexão com o banco de dados
            connection = DriverManager.getConnection("jdbc:sqlite:dataBase.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // Espera só por 30 segundos para conectar
        } catch(SQLException e) {
            // Se a mensagem de erro for: "out of memory",
            // Provavelmente erro ao criar(permissão) ou caminho do banco de dados
            System.err.println(e.getMessage());
          }

        finally {
            try {
              if(connection != null){
                connection.close();
                }
            } catch(SQLException e) {
                // Falhou também para fechar o arquivo
                System.err.println(e.getMessage());
            }
        }
    }
}
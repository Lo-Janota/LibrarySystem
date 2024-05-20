import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAOImpl implements LivroDAO {
    private Connection connection;

    public LivroDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Livro> pesquisarLivro(String termoPesquisa) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String query = "SELECT * FROM livros WHERE titulo LIKE ? OR autor LIKE ? OR categoria LIKE ? OR isbn LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + termoPesquisa + "%");
            preparedStatement.setString(2, "%" + termoPesquisa + "%");
            preparedStatement.setString(3, "%" + termoPesquisa + "%");
            preparedStatement.setString(4, "%" + termoPesquisa + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String titulo = resultSet.getString("titulo");
                    String autor = resultSet.getString("autor");
                    String categoria = resultSet.getString("categoria");
                    int isbn = resultSet.getInt("isbn");
                    int prazoEntrega = resultSet.getInt("prazoEntrega");
                    boolean disponibilidade = resultSet.getBoolean("Disponibilidade");
                    livros.add(new Livro(id, titulo, autor, categoria, isbn, prazoEntrega, disponibilidade));
                }
            }
        }
        return livros;
    }

    @Override
    public void adicionarLivro(Livro livro) throws SQLException {
        String query = "INSERT INTO livros (titulo, autor, categoria, isbn, prazoEntrega, disponibilidade) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, livro.getTitulo());
            preparedStatement.setString(2, livro.getAutor());
            preparedStatement.setString(3, livro.getCategoria());
            preparedStatement.setInt(4, livro.getIsbn());
            preparedStatement.setInt(5, livro.getPrazoEntrega());
            preparedStatement.setBoolean(6, livro.getDisponibilidade());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void removerLivro(Livro livro) throws SQLException {
        String query = "DELETE FROM livros WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, livro.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void editarLivro(Livro livro) throws SQLException {
        String query = "UPDATE livros SET titulo = ?, autor = ?, categoria = ?, isbn = ?, prazoEntrega = ?, disponibilidade = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, livro.getTitulo());
            preparedStatement.setString(2, livro.getAutor());
            preparedStatement.setString(3, livro.getCategoria());
            preparedStatement.setInt(4, livro.getIsbn());
            preparedStatement.setInt(5, livro.getPrazoEntrega());
            preparedStatement.setBoolean(6, livro.getDisponibilidade());
            preparedStatement.setInt(7, livro.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Livro> getLivros() throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String query = "SELECT * FROM livros";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String categoria = resultSet.getString("categoria");
                int isbn = resultSet.getInt("isbn");
                int prazoEntrega = resultSet.getInt("prazoEntrega");
                boolean disponibilidade = resultSet.getBoolean("disponibilidade");
                livros.add(new Livro(id, titulo, autor, categoria, isbn, prazoEntrega, disponibilidade));
            }
        }
        return livros;
    }
}

import javax.swing.*;
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
                    boolean disponibilidade = resultSet.getBoolean("disponibilidade");
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

    @Override
    public void atualizarPrazo(int id, int prazo) throws SQLException {
        String query = "UPDATE livros SET prazoEntrega = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, prazo);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void registrarEmprestimo(int livroId, String alunoCodigo, int prazo) throws SQLException {
        // Verificar a disponibilidade do livro
        String queryCheckDisponibilidade = "SELECT disponibilidade FROM livros WHERE id = ?";
        boolean disponibilidade = false;

        try (PreparedStatement preparedStatementCheckDisponibilidade = connection.prepareStatement(queryCheckDisponibilidade)) {
            preparedStatementCheckDisponibilidade.setInt(1, livroId);
            ResultSet resultSet = preparedStatementCheckDisponibilidade.executeQuery();
            if (resultSet.next()) {
                disponibilidade = resultSet.getBoolean("disponibilidade");
            }
        }

        // Se o livro estiver disponível, registrar o empréstimo
        if (disponibilidade) {
            // Criar a tabela de empréstimos se ela não existir
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS emprestimos (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "livroId INTEGER, " +
                                "alunoCodigo TEXT, " +
                                "prazo INTEGER, " +
                                "FOREIGN KEY (livroId) REFERENCES livros(id)" +
                                ")"
                );
            }

            // Atualizar a disponibilidade do livro
            String queryUpdateLivro = "UPDATE livros SET disponibilidade = ? WHERE id = ?";
            try (PreparedStatement preparedStatementUpdateLivro = connection.prepareStatement(queryUpdateLivro)) {
                preparedStatementUpdateLivro.setBoolean(1, false);
                preparedStatementUpdateLivro.setInt(2, livroId);
                preparedStatementUpdateLivro.executeUpdate();
            }

            // Inserir o empréstimo na tabela de empréstimos
            String queryInsertEmprestimo = "INSERT INTO emprestimos (livroId, alunoCodigo, prazo) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatementInsertEmprestimo = connection.prepareStatement(queryInsertEmprestimo)) {
                preparedStatementInsertEmprestimo.setInt(1, livroId);
                preparedStatementInsertEmprestimo.setString(2, alunoCodigo);
                preparedStatementInsertEmprestimo.setInt(3, prazo);
                preparedStatementInsertEmprestimo.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Empréstimo registrado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Livro indisponível no momento!.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para listar apenas livros disponíveis com pesquisa
    public List<Livro> pesquisarLivrosDisponiveis(String termoPesquisa) throws SQLException {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT * FROM livros WHERE disponibilidade = 1 AND (titulo LIKE ? OR autor LIKE ? OR categoria LIKE ? OR isbn LIKE ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pesquisa = "%" + termoPesquisa + "%";
            pstmt.setString(1, pesquisa);
            pstmt.setString(2, pesquisa);
            pstmt.setString(3, pesquisa);
            pstmt.setString(4, pesquisa);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String categoria = rs.getString("categoria");
                    int isbn = rs.getInt("isbn");
                    int prazoEntrega = rs.getInt("prazoEntrega");
                    boolean disponibilidade = rs.getBoolean("disponibilidade");

                    Livro livro = new Livro(id, titulo, autor, categoria, isbn, prazoEntrega, disponibilidade);
                    livros.add(livro);
                }
            }
        }
        return livros;
    }

    // Método para atualizar a disponibilidade do livro
    public void atualizarDisponibilidade(int livroId, boolean disponibilidade) throws SQLException {
        String sql = "UPDATE livros SET disponibilidade = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, disponibilidade);
            pstmt.setInt(2, livroId);
            pstmt.executeUpdate();
        }
    }

}

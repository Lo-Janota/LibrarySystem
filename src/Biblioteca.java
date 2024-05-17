import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Biblioteca {
    //Adicionando LOG para salvar qualquer alteração no banco de dados.
    private static final Logger logger = LoggerFactory.getLogger(Biblioteca.class);
    private Connection connection;

    public Biblioteca() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:dataBase.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS livros (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, autor TEXT, categoria TEXT, isbn INTEGER, prazoEntrega INTEGER)");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Livro> pesquisarLivro(String termoPesquisa) {
        List<Livro> livros = new ArrayList<>();
        try {
            String query = "SELECT * FROM livros WHERE titulo LIKE ? OR autor LIKE ? OR categoria LIKE ? OR isbn LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + termoPesquisa + "%");
            preparedStatement.setString(2, "%" + termoPesquisa + "%");
            preparedStatement.setString(3, "%" + termoPesquisa + "%");
            preparedStatement.setString(4, "%" + termoPesquisa + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String categoria = resultSet.getString("categoria");
                int isbn = resultSet.getInt("isbn");
                int prazoEntrega = resultSet.getInt("prazoEntrega");
                livros.add(new Livro(titulo, autor, categoria, isbn, prazoEntrega));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return livros;
    }

    public void adicionarLivro(Livro livro) {
        try {
            String query = "INSERT INTO livros (titulo, autor, categoria, isbn, prazoEntrega) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, livro.getTitulo());
            preparedStatement.setString(2, livro.getAutor());
            preparedStatement.setString(3, livro.getCategoria());
            preparedStatement.setInt(4, livro.getIsbn());
            preparedStatement.setInt(5, livro.getPrazoEntrega());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void removerLivro(Livro livro) {
        try {
            String query = "DELETE FROM livros WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, livro.getId());
            System.out.println("Removendo livro com ID: " + livro.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Livro> getLivros() {
        List<Livro> livros = new ArrayList<>();
        try {
            String query = "SELECT * FROM livros";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String categoria = resultSet.getString("categoria");
                int isbn = resultSet.getInt("isbn");
                int prazoEntrega = resultSet.getInt("prazoEntrega");
                livros.add(new Livro(titulo, autor, categoria, isbn, prazoEntrega));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return livros;
    }
}

class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private int isbn;
    private int prazoEntrega;

    public Livro(String titulo, String autor, String categoria, int isbn, int prazoEntrega) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.prazoEntrega = prazoEntrega;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(int prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
    }

    @Override
    public String toString() {
        return "Livro: " +
                "Id = " + id +
                " | Título = '" + titulo + '\'' +
                " | Autor = '" + autor + '\'' +
                " | Categoria = '" + categoria + '\'' +
                " | ISBN = " + isbn +
                " | Prazo de Entrega = " + prazoEntrega +
                " dias";
    }
}
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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS livros (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, autor TEXT, categoria TEXT, isbn INTEGER)");
            logger.info("Tabela 'livros' criada ou já existente.");
        } catch (SQLException e) {
            logger.error("Erro ao conectar ao banco de dados ou criar a tabela.", e);
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
                livros.add(new Livro(titulo, autor, categoria, isbn));
            }
            logger.info("Pesquisa de livros com o termo '{}'. {} livros encontrados.", termoPesquisa, livros.size());
        } catch (SQLException e) {
            logger.error("Erro ao pesquisar livros.", e);
        }
        return livros;
    }

    public void adicionarLivro(Livro livro) {
        try {
            String query = "INSERT INTO livros (titulo, autor, categoria, isbn) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, livro.getTitulo());
            preparedStatement.setString(2, livro.getAutor());
            preparedStatement.setString(3, livro.getCategoria());
            preparedStatement.setInt(4, livro.getIsbn());
            preparedStatement.executeUpdate();
            logger.info("Livro adicionado: {}", livro);
        } catch (SQLException e) {
            logger.error("Erro ao adicionar livro.", e);
        }
    }

    public void removerLivro(Livro livro) {
        try {
            String query = "DELETE FROM livros WHERE isbn = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, livro.getIsbn());
            preparedStatement.executeUpdate();
            logger.info("Livro removido: {}", livro);
        } catch (SQLException e) {
            logger.error("Erro ao remover livro.", e);
        }
    }

    public List<Livro> getLivros() {
        List<Livro> livros = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM livros");
            while (resultSet.next()) {
                String titulo = resultSet.getString("titulo");
                String autor = resultSet.getString("autor");
                String categoria = resultSet.getString("categoria");
                int isbn = resultSet.getInt("isbn");
                livros.add(new Livro(titulo, autor, categoria, isbn));
            }
            logger.info("Obtidos todos os livros. Total de livros: {}", livros.size());
        } catch (SQLException e) {
            logger.error("Erro ao obter todos os livros.", e);
        }
        return livros;
    }
}

class Livro {
    private String titulo;
    private String autor;
    private String categoria;
    private int isbn;
    private int prazoEntrega; // prazo de entrega opcional

    public Livro(String titulo, String autor, String categoria, int isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
    }

    public Livro(String titulo, String autor, String categoria, int isbn, int prazoEntrega) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.prazoEntrega = prazoEntrega;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public void setPrazoEntrega(int prazoEntrega) {
        this.prazoEntrega = prazoEntrega;
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

    @Override
    public String toString() {
        return "Título: " + titulo + ", Autor: " + autor + ", Categoria: " + categoria + ", ISBN: " + isbn + ", Prazo de Entrega: " + prazoEntrega;
    }
}

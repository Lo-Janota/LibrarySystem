import java.sql.*;
import java.util.List;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
class Biblioteca {
    private static final Logger logger = LoggerFactory.getLogger(Biblioteca.class);
    private Connection connection;
    private LivroDAO livroDAO;

    public Biblioteca() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:dataBase.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS livros (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, autor TEXT, categoria TEXT, isbn INTEGER, prazoEntrega INTEGER)");
        } catch (SQLException e) {
            System.err.println("Erro ao criar a tabela: " + e.getMessage());
            logger.error("Erro ao criar a tabela: " + e.getMessage());
        }

        livroDAO = new LivroDAOImpl(connection);
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu(livroDAO);
            menu.setVisible(true);
        });
    }

    public List<Livro> pesquisarLivro(String termoPesquisa) {
        try {
            return livroDAO.pesquisarLivro(termoPesquisa);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            logger.error("Erro ao pesquisar livro: " + e.getMessage());
            return null;
        }
    }

    public void adicionarLivro(Livro livro) {
        try {
            livroDAO.adicionarLivro(livro);
            logger.info("Livro adicionado com sucesso: " + livro.getTitulo());
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar livro: " + e.getMessage());
            logger.error("Erro ao adicionar livro: " + e.getMessage());
        }
    }

    public void removerLivro(Livro livro) {
        try {
            livroDAO.removerLivro(livro);
            logger.info("Livro removido com sucesso: " + livro.getTitulo());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            logger.error("Erro ao remover livro: " + e.getMessage());
        }
    }

    public void editarLivro(Livro livro) {
        try {
            livroDAO.editarLivro(livro);
            logger.info("Livro atualizado com sucesso: " + livro.getTitulo());
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar o livro: " + e.getMessage());
            logger.error("Erro ao atualizar o livro: " + e.getMessage());
        }
    }

    public List<Livro> getLivros() {
        try {
            return livroDAO.getLivros();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            logger.error("Erro ao obter lista de livros: " + e.getMessage());
            return null;
        }
    }

}
     */

class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private int isbn;
    private int prazoEntrega;

    public Livro(int id, String titulo, String autor, String categoria, int isbn, int prazoEntrega) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.prazoEntrega = prazoEntrega;
    }

    public Livro(String titulo, String autor, String categoria, int isbn, int prazoEntrega, boolean disponibilidade) {
    }

    public int getId() {
        return id;
    }

    /*
    public void setId(int id) {
        this.id = id;
    }
     */

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

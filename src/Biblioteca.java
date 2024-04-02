import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Livro> livros; // Lista de livros da biblioteca

    public Biblioteca() {
        livros = new ArrayList<>(); // Inicializa a lista de livros como uma ArrayList vazia
    }

    public void adicionarLivro(Livro livro) {
        livros.add(livro); // Adiciona um livro à lista de livros
    }

    public void removerLivro(Livro livro) {
        livros.remove(livro); // Remove um livro da lista de livros
    }

    public Livro buscarLivroPorTitulo(String titulo) {
        for (Livro livro : livros) { // Itera sobre todos os livros da lista
            if (livro.getTitulo().equals(titulo)) { // Verifica se o título do livro atual é igual ao título procurado
                return livro; // Retorna o livro se encontrado
            }
        }
        return null; // Retorna null se o livro não for encontrado
    }

    public List<Livro> getLivros() {
        return livros; // Retorna a lista de livros
    }
}

class Livro {
    private String titulo; // Título do livro
    private String autor; // Autor do livro
    private String categoria; // Categoria do livro
    private Integer isbn; // ISBN do livro
    private boolean disponivel; // Indica se o livro está disponível
    private Integer prazoEntrega; // Prazo de entrega do livro em dias

    public Livro(String titulo, String autor, String categoria, Integer isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.disponivel = false; // Por padrão, o livro é inicializado como disponível
        this.prazoEntrega = null; // Por padrão, o prazo de entrega não está definido
    }

    public String getTitulo() {
        return titulo; // Retorna o título do livro
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo; // Define o título do livro
    }

    public String getAutor() {
        return autor; // Retorna o autor do livro
    }

    public void setAutor(String autor) {
        this.autor = autor; // Define o autor do livro
    }

    public String getCategoria() {
        return categoria; // Retorna a categoria do livro
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria; // Define a categoria do livro
    }

    public Integer getIsbn() {
        return isbn; // Retorna o ISBN do livro
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn; // Define o ISBN do livro
    }

    public boolean isDisponivel() {
        return disponivel; // Retorna se o livro está disponível
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel; // Define se o livro está disponível
    }

    public Integer getPrazoEntrega() {
        return prazoEntrega; // Retorna o prazo de entrega do livro
    }

    public void setPrazoEntrega(Integer prazoEntrega) {
        this.prazoEntrega = prazoEntrega; // Define o prazo de entrega do livro
    }

    @Override
    public String toString() {
        return  "Titulo = '" + titulo + '\'' +
                " | Autor = '" + autor + '\'' +
                " | Categoria = '" + categoria + '\'' +
                " | ISBN = '" + isbn + '\'' +
                " | Disponível = " + (disponivel ? "Sim" : "Não") +
                " | Prazo de Entrega = " + (prazoEntrega != null ? prazoEntrega + " dias" : "Indefinido"); // Retorna uma representação em string do livro
    }
}

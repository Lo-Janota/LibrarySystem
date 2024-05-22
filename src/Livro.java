class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private int isbn;
    private int prazoEntrega;
    private boolean disponibilidade;

    public Livro(int id, String titulo, String autor, String categoria, int isbn, int prazoEntrega, boolean disponibilidade) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.prazoEntrega = prazoEntrega;
        this.disponibilidade = disponibilidade;
    }

    public Livro(String titulo, String autor, String categoria, int isbn, int prazoEntrega, boolean disponibilidade) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.prazoEntrega = prazoEntrega;
        this.disponibilidade = disponibilidade;
    }

    public Livro(int id, String titulo) {
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

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    /*@Override
    public String toString() {
        return "Livro: " +
                "Id = " + id +
                " | Título = '" + titulo + '\'' +
                " | Autor = '" + autor + '\'' +
                " | Categoria = '" + categoria + '\'' +
                " | ISBN = " + isbn +
                " | Prazo de Entrega = " + prazoEntrega +
                " dias | Disponibilidade = " + (disponibilidade ? "Disponível" : "Indisponível");
    } */
}

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

    public Livro() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getIsbn() {
        return isbn;
    }

    public int getPrazoEntrega() {
        return prazoEntrega;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public boolean getDisponibilidade() {
        return disponibilidade;
    }

}

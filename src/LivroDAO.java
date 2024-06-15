/*
O padrão DAO (Data Access Object) é um padrão de design que abstrai e encapsula o acesso a uma fonte de dados,
como um banco de dados. O objetivo principal do DAO é separar a lógica de persistência de dados da lógica de negócios de uma aplicação,
proporcionando uma interface limpa para operações de CRUD (Create, Read, Update, Delete) e outras interações com o banco de dados.
 */

import java.sql.SQLException;
import java.util.List;

public interface LivroDAO {
    List<Livro> pesquisarLivro(String termoPesquisa)
            throws SQLException;
    void adicionarLivro(Livro livro)
            throws SQLException;
    void removerLivro(Livro livro)
            throws SQLException;
    void editarLivro(Livro livro)
            throws SQLException;
    List<Livro> getLivros()
            throws SQLException;

    void atualizarPrazo(int id, int prazo)
            throws SQLException;
    void registrarEmprestimo(int livroId, String alunoCodigo, int prazo)
            throws SQLException;

    void atualizarDisponibilidade(int i, boolean b)
            throws SQLException;

    List<Livro> pesquisarLivrosDisponiveis(String termoPesquisa)
            throws SQLException;
}

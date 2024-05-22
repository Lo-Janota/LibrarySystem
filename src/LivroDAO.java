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
}

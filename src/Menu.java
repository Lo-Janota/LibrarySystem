import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Menu extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton configButton;
    private JButton editButton;
    private JButton prazoButton;
    private JTextArea bookListArea;
    private LivroDAO livroDAO;

    public Menu(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;

        setTitle("Menu Biblioteca");
        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("🔎");
        addButton = new JButton("➕");
        deleteButton = new JButton("❌");
        configButton = new JButton("🙋‍");
        editButton = new JButton("🔧");
        prazoButton = new JButton("⏳");
        bookListArea = new JTextArea();
        bookListArea.setEditable(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(deleteButton);
        searchPanel.add(editButton);
        searchPanel.add(prazoButton);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(configButton, BorderLayout.WEST);

        Dimension buttonSize = new Dimension(60, 30);
        searchButton.setPreferredSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        configButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        prazoButton.setPreferredSize(buttonSize);

        searchButton.setToolTipText("Pesquisar");
        addButton.setToolTipText("Adicionar");
        deleteButton.setToolTipText("Excluir");
        configButton.setToolTipText("Configurações de Usuários");
        editButton.setToolTipText("Editar");
        prazoButton.setToolTipText("Prazo de Entrega");

        JScrollPane scrollPane = new JScrollPane(bookListArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        configButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Configuração de usuários aqui...");
        });

        searchButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            try {
                List<Livro> resultado = livroDAO.pesquisarLivro(termoPesquisa);
                StringBuilder sb = new StringBuilder();
                for (Livro livro : resultado) {
                    sb.append(livro).append("\n");
                }
                bookListArea.setText(sb.toString());
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao pesquisar livros: " + ex.getMessage());
            }
        });

        addButton.addActionListener(e -> {
            JTextField tituloField = new JTextField(10);
            JTextField autorField = new JTextField(10);
            JTextField categoriaField = new JTextField(10);
            JTextField isbnField = new JTextField(10);
            JTextField prazoEntregaField = new JTextField(10);
            JCheckBox disponibilidadeCheckBox = new JCheckBox();

            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("Título:"));
            panel.add(tituloField);
            panel.add(new JLabel("Autor:"));
            panel.add(autorField);
            panel.add(new JLabel("Categoria:"));
            panel.add(categoriaField);
            panel.add(new JLabel("ISBN:"));
            panel.add(isbnField);
            panel.add(new JLabel("Prazo de Entrega:"));
            panel.add(prazoEntregaField);
            panel.add(new JLabel("Disponibilidade:"));
            panel.add(disponibilidadeCheckBox);

            int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Livro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String titulo = tituloField.getText();
                String autor = autorField.getText();
                String categoria = categoriaField.getText();
                String isbnStr = isbnField.getText();
                String prazoEntregaStr = prazoEntregaField.getText();
                boolean disponibilidade = disponibilidadeCheckBox.isSelected();

                try {
                    int isbn = Integer.parseInt(isbnStr);
                    int prazoEntrega = Integer.parseInt(prazoEntregaStr);
                    Livro livro = new Livro(titulo, autor, categoria, isbn, prazoEntrega, disponibilidade);
                    livroDAO.adicionarLivro(livro);
                    atualizarListaLivros();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O ISBN e o Prazo de Entrega devem ser números inteiros.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar livro: " + ex.getMessage());
                }
            }
        });

        editButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            try {
                Livro livro = livroDAO.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
                if (livro != null) {
                    JTextField tituloField = new JTextField(livro.getTitulo(), 10);
                    JTextField autorField = new JTextField(livro.getAutor(), 10);
                    JTextField categoriaField = new JTextField(livro.getCategoria(), 10);
                    JTextField isbnField = new JTextField(String.valueOf(livro.getIsbn()), 10);
                    JTextField prazoEntregaField = new JTextField(10);

                    JPanel panel = new JPanel(new GridLayout(5, 2));
                    panel.add(new JLabel("Título:"));
                    panel.add(tituloField);
                    panel.add(new JLabel("Autor:"));
                    panel.add(autorField);
                    panel.add(new JLabel("Categoria:"));
                    panel.add(categoriaField);
                    panel.add(new JLabel("ISBN:"));
                    panel.add(isbnField);
                    panel.add(new JLabel("Prazo de Entrega:"));
                    panel.add(prazoEntregaField);

                    int result = JOptionPane.showConfirmDialog(null, panel, "Editar Livro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String novoTitulo = tituloField.getText();
                        String novoAutor = autorField.getText();
                        String novaCategoria = categoriaField.getText();
                        String novoIsbnStr = isbnField.getText();
                        String novoPrazoEntrega = prazoEntregaField.getText();

                        try {
                            int novoIsbn = Integer.parseInt(novoIsbnStr);
                            int novoPrazo = Integer.parseInt(novoPrazoEntrega);
                            livro.setTitulo(novoTitulo);
                            livro.setAutor(novoAutor);
                            livro.setCategoria(novaCategoria);
                            livro.setIsbn(novoIsbn);
                            livro.setPrazoEntrega(novoPrazo);

                            livroDAO.editarLivro(livro);

                            atualizarListaLivros();
                            JOptionPane.showMessageDialog(null, "Livro editado com sucesso!");
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "O ISBN deve ser um número inteiro.");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Erro ao editar livro: " + ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao pesquisar livros: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            try {
                Livro livro = livroDAO.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
                if (livro != null) {
                    int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o livro '" + livro.getTitulo() + "'?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
                    if (confirmacao == JOptionPane.YES_OPTION) {
                        livroDAO.removerLivro(livro);
                        atualizarListaLivros();
                        JOptionPane.showMessageDialog(null, "Livro removido com sucesso!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao pesquisar livros: " + ex.getMessage());
            }
        });

        prazoButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            try {
                Livro livro = livroDAO.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
                if (livro != null) {
                    String prazoStr = JOptionPane.showInputDialog("Informe o prazo de empréstimos em dias:");
                    try {
                        int prazo = Integer.parseInt(prazoStr);
                        livro.setPrazoEntrega(prazo);
                        livroDAO.editarLivro(livro);
                        atualizarListaLivros();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "O prazo de empréstimo deve ser um número inteiro.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao atualizar prazo de entrega: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao pesquisar livros: " + ex.getMessage());
            }
        });

        atualizarListaLivros();
        setVisible(true);
    }

    private void atualizarListaLivros() {
        try {
            List<Livro> livros = livroDAO.getLivros();
            StringBuilder sb = new StringBuilder();
            for (Livro livro : livros) {
                sb.append(livro).append("\n");
            }
            bookListArea.setText(sb.toString());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar lista de livros: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection connection = DriverManager.getConnection("jdbc:sqlite:dataBase.db");
                LivroDAO livroDAO = new LivroDAOImpl(connection);
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(30);
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS livros (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, autor TEXT, categoria TEXT, isbn INTEGER, prazoEntrega INTEGER)");
                Menu menu = new Menu(livroDAO);
                menu.setVisible(true);
           } catch (SQLException e) {
                System.err.println("Erro ao criar a tabela: " + e.getMessage());
                Logger logger = null;
                logger.error("Erro ao criar a tabela: " + e.getMessage());
            }
        });
    }
}

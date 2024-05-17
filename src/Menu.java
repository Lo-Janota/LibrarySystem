import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Menu extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton configButton;
    private JButton editButton;
    private JButton prazoButton;
    private JTextArea bookListArea;
    private Biblioteca biblioteca;

    public Menu() {
        setTitle("Menu Biblioteca");
        setSize(750, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        biblioteca = new Biblioteca();

        searchField = new JTextField(20);
        searchButton = new JButton("🔎");
        addButton = new JButton("➕");
        deleteButton = new JButton("❌");
        configButton = new JButton("🔧");
        editButton = new JButton("❓");
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
        configButton.setToolTipText("Configuração");
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
            List<Livro> resultado = biblioteca.pesquisarLivro(termoPesquisa);
            StringBuilder sb = new StringBuilder();
            for (Livro livro : resultado) {
                sb.append(livro).append("\n");
            }
            bookListArea.setText(sb.toString());
        });

        addButton.addActionListener(e -> {
            JTextField tituloField = new JTextField(10);
            JTextField autorField = new JTextField(10);
            JTextField categoriaField = new JTextField(10);
            JTextField isbnField = new JTextField(10);
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

            int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Livro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String titulo = tituloField.getText();
                String autor = autorField.getText();
                String categoria = categoriaField.getText();
                String isbnStr = isbnField.getText();
                String prazoEntregaStr = prazoEntregaField.getText();

                try {
                    Integer isbn = Integer.parseInt(isbnStr);
                    Integer prazoEntrega = Integer.parseInt(prazoEntregaStr);
                    biblioteca.adicionarLivro(new Livro(titulo, autor, categoria, isbn, prazoEntrega));
                    atualizarListaLivros();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O ISBN e o prazo de entrega devem ser números inteiros.");
                }
            }
        });

        editButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            Livro livro = biblioteca.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
            if (livro != null) {
                JTextField tituloField = new JTextField(livro.getTitulo(), 10);
                JTextField autorField = new JTextField(livro.getAutor(), 10);
                JTextField categoriaField = new JTextField(livro.getCategoria(), 10);
                JTextField isbnField = new JTextField(String.valueOf(livro.getIsbn()), 10);

                JPanel panel = new JPanel(new GridLayout(4, 2));
                panel.add(new JLabel("Título:"));
                panel.add(tituloField);
                panel.add(new JLabel("Autor:"));
                panel.add(autorField);
                panel.add(new JLabel("Categoria:"));
                panel.add(categoriaField);
                panel.add(new JLabel("ISBN:"));
                panel.add(isbnField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Editar Livro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String novoTitulo = tituloField.getText();
                    String novoAutor = autorField.getText();
                    String novaCategoria = categoriaField.getText();
                    String novoIsbnStr = isbnField.getText();

                    try {
                        Integer novoIsbn = Integer.parseInt(novoIsbnStr);
                        livro.setTitulo(novoTitulo);
                        livro.setAutor(novoAutor);
                        livro.setCategoria(novaCategoria);
                        livro.setIsbn(novoIsbn);

                        biblioteca.editarLivro(livro);

                        atualizarListaLivros();
                        JOptionPane.showMessageDialog(null, "Livro editado com sucesso!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "O ISBN deve ser um número inteiro.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Livro não encontrado.");
            }
        });

        deleteButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            Livro livro = biblioteca.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
            if (livro != null) {
                int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o livro '" + livro.getTitulo() + "'?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    biblioteca.removerLivro(livro);
                    atualizarListaLivros();
                    JOptionPane.showMessageDialog(null, "Livro removido com sucesso!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Livro não encontrado.");
            }
        });

        prazoButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            Livro livro = biblioteca.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
            if (livro != null) {
                String prazoStr = JOptionPane.showInputDialog("Informe o prazo de empréstimos em dias:");
                try {
                    Integer prazo = Integer.parseInt(prazoStr);
                    livro.setPrazoEntrega(prazo);
                    atualizarListaLivros();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O prazo de empréstimo deve ser um número inteiro.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Livro não encontrado.");
            }
        });
        atualizarListaLivros();
    }

    private void atualizarListaLivros() {
        StringBuilder sb = new StringBuilder();
        for (Livro livro : biblioteca.getLivros()) {
            sb.append(livro).append("\n");
        }
        bookListArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}
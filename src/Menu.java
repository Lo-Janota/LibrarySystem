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

        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Configuração de usuários aqui...");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = searchField.getText().toLowerCase();
                List<Livro> resultado = biblioteca.pesquisarLivro(termoPesquisa);
                StringBuilder sb = new StringBuilder();
                for (Livro livro : resultado) {
                    sb.append(livro).append("\n");
                }
                bookListArea.setText(sb.toString());
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = JOptionPane.showInputDialog("Digite o título do livro:");
                String autor = JOptionPane.showInputDialog("Digite o autor do livro:");
                String categoria = JOptionPane.showInputDialog("Digite a categoria do livro:");
                String isbnStr = JOptionPane.showInputDialog("Digite o número ISBN do livro:");
                String prazoEntregaStr = JOptionPane.showInputDialog("Digite o prazo de entrega do livro:");

                try {
                    Integer isbn = Integer.parseInt(isbnStr);
                    Integer prazoEntrega = Integer.parseInt(prazoEntregaStr); // Converte o prazo de entrega para Integer
                    biblioteca.adicionarLivro(new Livro(titulo, autor, categoria, isbn, prazoEntrega)); // Passa o prazo de entrega para o construtor do Livro
                    atualizarListaLivros();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "O ISBN e o prazo de entrega devem ser números inteiros.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = searchField.getText().toLowerCase();
                Livro livro = biblioteca.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
                if (livro != null) {
                    biblioteca.removerLivro(livro);
                    atualizarListaLivros();
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = searchField.getText().toLowerCase();
                Livro livro = biblioteca.pesquisarLivro(termoPesquisa).stream().findFirst().orElse(null);
                if (livro != null) {
                    String novoTitulo = JOptionPane.showInputDialog("Editar Título:", livro.getTitulo());
                    String novoAutor = JOptionPane.showInputDialog("Editar Autor:", livro.getAutor());
                    String novaCategoria = JOptionPane.showInputDialog("Editar Categoria:", livro.getCategoria());
                    String novoIsbnStr = JOptionPane.showInputDialog("Editar ISBN:", livro.getIsbn());

                    try {
                        Integer novoIsbn = Integer.parseInt(novoIsbnStr);
                        livro.setTitulo(novoTitulo);
                        livro.setAutor(novoAutor);
                        livro.setCategoria(novaCategoria);
                        livro.setIsbn(novoIsbn);
                        atualizarListaLivros();
                        JOptionPane.showMessageDialog(null, "Livro editado com sucesso!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "O ISBN deve ser um número inteiro.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            }
        });

        prazoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

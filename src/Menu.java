import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton deleteButton;
    private JTextArea bookListArea;

    private Biblioteca biblioteca;

    public Menu() {
        setTitle("Menu");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        biblioteca = new Biblioteca();

        searchField = new JTextField(20);
        searchButton = new JButton("🔎");
        addButton = new JButton("➕");
        deleteButton = new JButton("❌");
        bookListArea = new JTextArea();
        bookListArea.setEditable(false);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(addButton);
        topPanel.add(deleteButton);

        searchButton.setPreferredSize(new Dimension(60, 30));
        addButton.setPreferredSize(new Dimension(60, 30));
        deleteButton.setPreferredSize(new Dimension(60, 30));

        // Adiciona tooltips aos botões
        searchButton.setToolTipText("Pesquisar");
        addButton.setToolTipText("Adicionar");
        deleteButton.setToolTipText("Excluir");

        JScrollPane scrollPane = new JScrollPane(bookListArea);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = searchField.getText().toLowerCase();
                StringBuilder resultado = new StringBuilder();
                for (Livro livro : biblioteca.getLivros()) {
                    if (livro.getTitulo().toLowerCase().contains(termoPesquisa) ||
                            livro.getAutor().toLowerCase().contains(termoPesquisa) ||
                            livro.getCategoria().toLowerCase().contains(termoPesquisa)) {
                        resultado.append(livro).append("\n");
                    }
                }
                bookListArea.setText(resultado.toString());
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = JOptionPane.showInputDialog("Digite o título do livro:");
                String autor = JOptionPane.showInputDialog("Digite o autor do livro:");
                String categoria = JOptionPane.showInputDialog("Digite a categoria do livro:");
        
                // Validação dos campos
                if (titulo != null && !titulo.isEmpty() &&
                    autor != null && !autor.isEmpty() &&
                    categoria != null && !categoria.isEmpty()) {
                    biblioteca.adicionarLivro(new Livro(titulo, autor, categoria));
                    atualizarListaLivros();
                } else {
                    JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos.");
                }
            }
        });
        

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = searchField.getText().toLowerCase();
                Livro livroSelecionado = null;
                for (Livro livro : biblioteca.getLivros()) {
                    if (livro.getTitulo().toLowerCase().contains(termoPesquisa)) {
                        livroSelecionado = livro;
                        break; // Interrompe o loop ao encontrar o primeiro livro correspondente
                    }
                }
                if (livroSelecionado != null) {
                    biblioteca.removerLivro(livroSelecionado);
                    atualizarListaLivros();
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            }
        });
        

        atualizarListaLivros();
    }

    private void atualizarListaLivros() {
        StringBuilder listaLivros = new StringBuilder();
        for (Livro livro : biblioteca.getLivros()) {
            listaLivros.append(livro).append("\n");
        }
        bookListArea.setText(listaLivros.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}

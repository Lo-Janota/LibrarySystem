import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class Menu extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton deleteButton;
    private JButton configButton;
    private JButton editButton;
    private JButton empDevButton;
    private JTable bookTable;
    private LivroDAO livroDAO;

    public Menu(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;

        setTitle("Menu Biblioteca");
        setSize(1000, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("Pesquisar");
        addButton = new JButton("Adicionar");
        deleteButton = new JButton("Remover");
        configButton = new JButton("Usuarios");
        editButton = new JButton("Editar");
        empDevButton = new JButton("Empréstimo/Devolução");

        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);
        searchPanel.add(editButton);
        searchPanel.add(deleteButton);
        searchPanel.add(empDevButton);

        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(configButton, BorderLayout.WEST);

        Dimension buttonSize = new Dimension(100, 30);
        Dimension newButtonSize = new Dimension(180, 30);
        searchButton.setPreferredSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        configButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        empDevButton.setPreferredSize(newButtonSize);

        JPanel tablePanel = new JPanel(new BorderLayout());
        DefaultTableModel tableModel = new DefaultTableModel();
        bookTable = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Título");
        tableModel.addColumn("Autor");
        tableModel.addColumn("Categoria");
        tableModel.addColumn("ISBN");
        tableModel.addColumn("Prazo");
        tableModel.addColumn("Disponibilidade");

        JScrollPane scrollPane = new JScrollPane(bookTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        configButton.addActionListener(e -> {
            JFrame configFrame = new JFrame("Configuração de Usuários");
            configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            configFrame.setSize(750, 300);
            configFrame.setLocationRelativeTo(null);

            ConfigUsers configUsers = new ConfigUsers();
            configFrame.add(configUsers);

            configFrame.setVisible(true);
        });

        searchButton.addActionListener(e -> {
            String termoPesquisa = searchField.getText().toLowerCase();
            try {
                List<Livro> resultado = livroDAO.pesquisarLivro(termoPesquisa);
                DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
                model.setRowCount(0); // Limpa a tabela
                for (Livro livro : resultado) {
                    model.addRow(new Object[]{livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getCategoria(), livro.getIsbn(), livro.getPrazoEntrega(), livro.isDisponibilidade()});
                }
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
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) bookTable.getValueAt(selectedRow, 0);
                String titulo = (String) bookTable.getValueAt(selectedRow, 1);
                String autor = (String) bookTable.getValueAt(selectedRow, 2);
                String categoria = (String) bookTable.getValueAt(selectedRow, 3);
                int isbn = (int) bookTable.getValueAt(selectedRow, 4);
                int prazo = (int) bookTable.getValueAt(selectedRow, 5);
                boolean disponibilidade = (boolean) bookTable.getValueAt(selectedRow, 6);

                JTextField tituloField = new JTextField(titulo, 10);
                JTextField autorField = new JTextField(autor, 10);
                JTextField categoriaField = new JTextField(categoria, 10);
                JTextField isbnField = new JTextField(String.valueOf(isbn), 10);
                JTextField prazoEntregaField = new JTextField(String.valueOf(prazo), 10);
                JCheckBox disponibilidadeCheckBox = new JCheckBox();
                disponibilidadeCheckBox.setSelected(disponibilidade);

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

                int result = JOptionPane.showConfirmDialog(null, panel, "Editar Livro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String novoTitulo = tituloField.getText();
                    String novoAutor = autorField.getText();
                    String novaCategoria = categoriaField.getText();
                    String novoIsbnStr = isbnField.getText();
                    String novoPrazoEntregaStr = prazoEntregaField.getText();
                    boolean novaDisponibilidade = disponibilidadeCheckBox.isSelected();

                    try {
                        int novoIsbn = Integer.parseInt(novoIsbnStr);
                        int novoPrazo = Integer.parseInt(novoPrazoEntregaStr);
                        Livro livro = new Livro(id, novoTitulo, novoAutor, novaCategoria, novoIsbn, novoPrazo, novaDisponibilidade);
                        livroDAO.editarLivro(livro);
                        atualizarListaLivros();
                        JOptionPane.showMessageDialog(null, "Livro editado com sucesso!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "O ISBN e o Prazo de Entrega devem ser números inteiros.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao editar livro: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um livro para editar.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) bookTable.getValueAt(selectedRow, 0);
                String titulo = (String) bookTable.getValueAt(selectedRow, 1);

                int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o livro '(" + id +") - " + titulo + "'?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        Livro livro = new Livro();
                        livro.setId(id);
                        livroDAO.removerLivro(livro);
                        atualizarListaLivros();
                        JOptionPane.showMessageDialog(null, "Livro removido com sucesso!");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao remover livro: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um livro para remover.");
            }
        });

        empDevButton.addActionListener(e -> {
            JFrame empDevFrame = new JFrame("Empréstimo/Devolução");
            empDevFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            empDevFrame.setSize(800, 400);
            empDevFrame.setLocationRelativeTo(null);

            EmprestimoDevolucao empDevPanel = new EmprestimoDevolucao(livroDAO);
            empDevFrame.add(empDevPanel);

            empDevFrame.setVisible(true);
        });

        atualizarListaLivros();
        setVisible(true);

        /*
        setVisible(true); // Torna a janela visível
        atualizarVisibilidadeBotoes("Funcionario"); // Define a visibilidade inicial dos botões para usuário
         */
    }

    public void atualizarVisibilidadeBotoes(String permissao) {
        // Verifica o nível de permissão e esconde ou mostra os botões conforme necessário
        if ("USUARIO".equals(permissao)) {
            // Esconde os botões destinados apenas para funcionário
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            configButton.setVisible(false);
        } else if ("FUNCIONARIO".equals(permissao)) {
            // Mostra todos os botões
            addButton.setVisible(true);
            editButton.setVisible(true);
            deleteButton.setVisible(true);
            configButton.setVisible(true);
        }
    }

    private void atualizarListaLivros() {
        try {
            List<Livro> livros = livroDAO.getLivros();
            DefaultTableModel model = (DefaultTableModel) bookTable.getModel();
            model.setRowCount(0);
            for (Livro livro : livros) {
                model.addRow(new Object[]{livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getCategoria(), livro.getIsbn(), livro.getPrazoEntrega(), livro.isDisponibilidade()});
            }
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
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS livros (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "titulo TEXT, " +
                                "autor TEXT, " +
                                "categoria TEXT, " +
                                "isbn INTEGER, " +
                                "prazoEntrega INTEGER, " +
                                "disponibilidade BOOLEAN)"
                );

                new Menu(livroDAO);
            } catch (SQLException e) {
                System.err.println("Erro ao criar a tabela: " + e.getMessage());
            }
        });
    }


}



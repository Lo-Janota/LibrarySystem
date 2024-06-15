import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EmprestimoDevolucao extends JPanel {
    private LivroDAO livroDAO;
    private JTable emprestimosTable;
    private JTextField searchField;
    private JButton searchButton;

    public EmprestimoDevolucao(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
        setLayout(new BorderLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("Pesquisar");

        JButton registrarEmprestimoButton = new JButton("Realizar Empréstimo");
        JButton registrarDevolucaoButton = new JButton("Realizar Devolução");

        registrarEmprestimoButton.addActionListener(e -> abrirTelaEmprestimo());
        registrarDevolucaoButton.addActionListener(e -> abrirTelaDevolucao());

        searchButton.addActionListener(e -> atualizarListaEmprestimos(searchField.getText()));

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Pesquisar:"));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(registrarEmprestimoButton);
        topPanel.add(registrarDevolucaoButton);

        add(topPanel, BorderLayout.NORTH);

        emprestimosTable = new JTable(new DefaultTableModel(new Object[]{"ID", "Título", "Autor", "Categoria", "ISBN", "Prazo Entrega", "Disponibilidade"}, 0));
        add(new JScrollPane(emprestimosTable), BorderLayout.CENTER);

        atualizarListaEmprestimos("");
    }

    private void abrirTelaEmprestimo() {
        JFrame emprestimoFrame = new JFrame("Registrar Empréstimo");
        emprestimoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        emprestimoFrame.setSize(400, 200);
        emprestimoFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Código do Aluno:"), gbc);
        JTextField alunoField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(alunoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Código do Livro:"), gbc);
        JTextField livroField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(livroField, gbc);

        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.addActionListener(e -> {
            String livroCodigo = livroField.getText();
            // Lógica para registrar empréstimo e atualizar disponibilidade
            try {
                livroDAO.atualizarDisponibilidade(Integer.parseInt(livroCodigo), false);
                JOptionPane.showMessageDialog(null, "Empréstimo registrado com sucesso!");
                atualizarListaEmprestimos("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao registrar empréstimo: " + ex.getMessage());
            }
            emprestimoFrame.dispose();
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(confirmarButton, gbc);

        emprestimoFrame.add(panel);
        emprestimoFrame.setVisible(true);
    }

    private void abrirTelaDevolucao() {
        JFrame devolucaoFrame = new JFrame("Registrar Devolução");
        devolucaoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        devolucaoFrame.setSize(400, 200);
        devolucaoFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Código do Aluno:"), gbc);
        JTextField alunoField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(alunoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Código do Livro:"), gbc);
        JTextField livroField = new JTextField(10);
        gbc.gridx = 1;
        panel.add(livroField, gbc);

        JButton confirmarButton = new JButton("Confirmar");
        confirmarButton.addActionListener(e -> {
            String livroCodigo = livroField.getText();
            // Lógica para registrar devolução e atualizar disponibilidade
            try {
                livroDAO.atualizarDisponibilidade(Integer.parseInt(livroCodigo), true);
                JOptionPane.showMessageDialog(null, "Devolução registrada com sucesso!");
                atualizarListaEmprestimos("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao registrar devolução: " + ex.getMessage());
            }
            devolucaoFrame.dispose();
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(confirmarButton, gbc);

        devolucaoFrame.add(panel);
        devolucaoFrame.setVisible(true);
    }

    private void atualizarListaEmprestimos(String termoPesquisa) {
        try {
            List<Livro> listaLivros = livroDAO.pesquisarLivrosDisponiveis(termoPesquisa); // Método alterado para pesquisar apenas livros disponíveis
            DefaultTableModel model = (DefaultTableModel) emprestimosTable.getModel();
            model.setRowCount(0); // Limpa a tabela

            for (Livro livro : listaLivros) {
                model.addRow(new Object[]{livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getCategoria(), livro.getIsbn(), livro.getPrazoEntrega(), livro.isDisponibilidade()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar lista de livros: " + ex.getMessage());
        }
    }
}

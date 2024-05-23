import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ConfigUsers extends JPanel {
    private Connection conn;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton searchButton;

    public ConfigUsers() {
        conn = conectarBanco();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        tableModel = new DefaultTableModel(new String[]{"ID", "Código", "Nome", "Senha", "Permissão"}, 0);

        setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton removeButton = new JButton("Excluir");
        searchField = new JTextField(20);
        searchButton = new JButton("Pesquisar");

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(searchField);
        buttonsPanel.add(searchButton);

        addButton.setToolTipText("Adicionar Usuário");
        editButton.setToolTipText("Editar Usuário");
        removeButton.setToolTipText("Remover Usuário");
        searchField.setToolTipText("Pesquisar por Código ou Nome");
        searchButton.setToolTipText("Clique para pesquisar usuários");

        JTable userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        add(buttonsPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirTelaAdicionarUsuario();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String nomeUsuario = (String) tableModel.getValueAt(selectedRow, 2);
                    exibirTelaEditarUsuario(nomeUsuario);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um usuário para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int idUsuario = (Integer) tableModel.getValueAt(selectedRow, 0);
                    String nomeUsuario = (String) tableModel.getValueAt(selectedRow, 2);
                    exibirTelaRemoverUsuario(idUsuario, nomeUsuario);
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione um usuário para remover.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarUsuario(searchField.getText());
            }
        });

        atualizarListaUsuarios();
    }

    private Connection conectarBanco() {
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:dataBase.db";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void exibirTelaAdicionarUsuario() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JTextField codigoField = new JTextField();
        JTextField nomeField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JCheckBox funcionarioCheckBox = new JCheckBox("Funcionario");
        JCheckBox usuarioCheckBox = new JCheckBox("Usuario");

        ButtonGroup group = new ButtonGroup();
        group.add(funcionarioCheckBox);
        group.add(usuarioCheckBox);

        panel.add(new JLabel("Codigo:"));
        panel.add(codigoField);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);
        panel.add(funcionarioCheckBox);
        panel.add(usuarioCheckBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String codigo = codigoField.getText();
            String nome = nomeField.getText();
            String senha = new String(senhaField.getPassword());
            String permissao = funcionarioCheckBox.isSelected() ? "FUNCIONARIO" : usuarioCheckBox.isSelected() ? "USUARIO" : "";

            if (permissao.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Selecione uma permissão.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (codigoExiste(codigo)) {
                JOptionPane.showMessageDialog(null, "Já existe um usuário com esse código.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                inserirUsuario(codigo, nome, senha, permissao);
                atualizarListaUsuarios();
            }
        }
    }

    private void exibirTelaEditarUsuario(String nomeUsuario) {
        if (nomeUsuario == null || nomeUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Selecione um usuário para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM usuarios WHERE nome = ?");
            statement.setString(1, nomeUsuario);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                JPanel panel = new JPanel(new GridLayout(4, 1));
                JTextField nomeField = new JTextField(rs.getString("nome"));
                JPasswordField senhaField = new JPasswordField(rs.getString("senha"));
                JCheckBox funcionarioCheckBox = new JCheckBox("Funcionario");
                JCheckBox usuarioCheckBox = new JCheckBox("Usuario");

                String permissao = rs.getString("permissao");
                if (permissao.equals("FUNCIONARIO")) {
                    funcionarioCheckBox.setSelected(true);
                } else if (permissao.equals("USUARIO")) {
                    usuarioCheckBox.setSelected(true);
                }

                ButtonGroup group = new ButtonGroup();
                group.add(funcionarioCheckBox);
                group.add(usuarioCheckBox);

                panel.add(new JLabel("Nome:"));
                panel.add(nomeField);
                panel.add(new JLabel("Senha:"));
                panel.add(senhaField);
                panel.add(funcionarioCheckBox);
                panel.add(usuarioCheckBox);

                int result = JOptionPane.showConfirmDialog(null, panel, "Editar Usuário",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String novoNome = nomeField.getText();
                    String novaSenha = new String(senhaField.getPassword());
                    String novaPermissao = funcionarioCheckBox.isSelected() ? "FUNCIONARIO" : usuarioCheckBox.isSelected() ? "USUARIO" : "";

                    if (novaPermissao.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Selecione uma permissão.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    atualizarUsuario(nomeUsuario, novoNome, novaSenha, novaPermissao);
                    atualizarListaUsuarios();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao obter informações do usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirTelaRemoverUsuario(int idUsuario, String nomeUsuario) {
        int confirmacao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover o usuário '" + nomeUsuario + "'?", "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            removerUsuario(idUsuario);
            atualizarListaUsuarios();
        }
    }

    private boolean codigoExiste(String codigo) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM usuarios WHERE codigo = ?");
            statement.setString(1, codigo);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void inserirUsuario(String codigo, String nome, String senha, String permissao) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO usuarios (codigo, nome, senha, permissao) VALUES (?, ?, ?, ?)");
            statement.setString(1, codigo);
            statement.setString(2, nome);
            statement.setString(3, senha);
            statement.setString(4, permissao);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarUsuario(String nomeAntigo, String novoNome, String novaSenha, String novaPermissao) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE usuarios SET nome = ?, senha = ?, permissao = ? WHERE nome = ?");
            statement.setString(1, novoNome);
            statement.setString(2, novaSenha);
            statement.setString(3, novaPermissao);
            statement.setString(4, nomeAntigo);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerUsuario(int idUsuario) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            statement.setInt(1, idUsuario);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Usuário removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao remover usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pesquisarUsuario(String termo) {
        tableModel.setRowCount(0); // Limpar a tabela antes de inserir os dados atualizados

        String query = "SELECT * FROM usuarios WHERE codigo LIKE ? OR nome LIKE ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            String likeTerm = "%" + termo + "%";
            statement.setString(1, likeTerm);
            statement.setString(2, likeTerm);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int codigo = rs.getInt("codigo");
                    String nome = rs.getString("nome");
                    String senha = rs.getString("senha");
                    String permissao = rs.getString("permissao");
                    tableModel.addRow(new Object[]{id, codigo, nome, senha, permissao});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao pesquisar usuários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaUsuarios() {
        tableModel.setRowCount(0); // Limpar a tabela antes de inserir os dados atualizados

        String query = "SELECT * FROM usuarios";
        try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int codigo = rs.getInt("codigo");
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                String permissao = rs.getString("permissao");
                tableModel.addRow(new Object[]{id, codigo, nome, senha, permissao});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar a lista de usuários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Configuração de Usuários");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(750, 400);
            frame.setLocationRelativeTo(null);

            ConfigUsers configUsers = new ConfigUsers();
            frame.add(configUsers);

            frame.setVisible(true);
        });
    }
}

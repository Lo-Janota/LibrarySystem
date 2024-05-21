import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ConfigUsers extends JPanel {
    private Connection conn;
    private DefaultListModel<String> listModel; // Modelo de lista para o JList

    public ConfigUsers() {
        conn = conectarBanco(); // Conectar ao banco de dados SQLite
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        listModel = new DefaultListModel<>(); // Inicializa o modelo de lista

        setLayout(new BorderLayout()); // Define o layout do painel como BorderLayout

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Painel para os botões de ação
        JButton addButton = new JButton("Adicionar"); // Botão para adicionar usuário
        JButton editButton = new JButton("Editar"); // Botão para editar usuário
        JButton removeButton = new JButton("Excluir"); // Botão para remover usuário

        buttonsPanel.add(addButton); // Adiciona o botão de adicionar usuário ao painel de botões
        buttonsPanel.add(editButton); // Adiciona o botão de editar usuário ao painel de botões
        buttonsPanel.add(removeButton); // Adiciona o botão de remover usuário ao painel de botões

        // Adiciona tooltips aos botões
        addButton.setToolTipText("Adicionar Usuário");
        editButton.setToolTipText("Editar Usuário");
        removeButton.setToolTipText("Remover Usuário");

        JList<String> userList = new JList<>(listModel); // Lista de usuários
        JScrollPane scrollPane = new JScrollPane(userList); // Cria um JScrollPane para a lista de usuários

        add(buttonsPanel, BorderLayout.NORTH); // Adiciona o painel de botões à parte superior do painel de configuração
        add(scrollPane, BorderLayout.CENTER); // Adiciona o JScrollPane à parte central do painel de configuração

        // Adiciona um ActionListener ao botão de adicionar usuário
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirTelaAdicionarUsuario();
            }
        });

        // Adiciona um ActionListener ao botão de editar usuário
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirTelaEditarUsuario();
            }
        });

        // Adiciona um ActionListener ao botão de remover usuário
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirTelaRemoverUsuario();
            }
        });

        // Inicializa a lista de usuários
        atualizarListaUsuarios();
    }

    // Conecta ao banco de dados SQLite
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

    // Método para exibir a tela de adicionar usuário
    private void exibirTelaAdicionarUsuario() {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JTextField codigoField = new JTextField();
        JTextField nomeField = new JTextField();
        JPasswordField senhaField = new JPasswordField();
        JTextField permissaoField = new JTextField();

        panel.add(new JLabel("Codigo:"));
        panel.add(codigoField);
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Senha:"));
        panel.add(senhaField);
        panel.add(new JLabel("Permissao:"));
        panel.add(permissaoField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String codigo = codigoField.getText();
            String nome = nomeField.getText();
            String senha = new String(senhaField.getPassword());
            String permissao = permissaoField.getText();

            // Verifica se o código já existe no banco de dados
            if (codigoExiste(codigo)) {
                JOptionPane.showMessageDialog(null, "Já existe um usuário com esse código.", "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                // Insere o novo usuário no banco de dados
                inserirUsuario(codigo, nome, senha, permissao);
                atualizarListaUsuarios();
            }
        }
    }

    // Verifica se um código de usuário já existe no banco de dados
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

    // Insere um novo usuário no banco de dados
    private void inserirUsuario(String codigo, String nome
            , String senha, String permissao) {
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

    // Método para exibir a tela de editar usuário
    private void exibirTelaEditarUsuario() {
        String nomeUsuario = JOptionPane.showInputDialog("Digite o nome do usuário a ser editado:");
        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
            String novoNome = JOptionPane.showInputDialog("Digite o novo nome do usuário:");
            if (novoNome != null && !novoNome.isEmpty()) {
                // Atualiza o usuário no banco de dados
                atualizarUsuario(nomeUsuario, novoNome);
                atualizarListaUsuarios();
            }
        }
    }

    // Atualiza um usuário no banco de dados
    private void atualizarUsuario(String nomeAntigo, String novoNome) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE usuarios SET nome = ? WHERE nome = ?");
            statement.setString(1, novoNome);
            statement.setString(2, nomeAntigo);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Usuário editado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao editar usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para exibir a tela de remover usuário
    private void exibirTelaRemoverUsuario() {
        String nomeUsuario = JOptionPane.showInputDialog("Digite o nome do usuário a ser removido:");
        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
            // Remove o usuário do banco de dados
            removerUsuario(nomeUsuario);
            atualizarListaUsuarios();
        }
    }

    // Remove um usuário do banco de dados
    private void removerUsuario(String nomeUsuario) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM usuarios WHERE nome = ?");
            statement.setString(1, nomeUsuario);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Usuário removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao remover usuário.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Atualiza a lista de usuários no JList
    private void atualizarListaUsuarios() {
        // Limpa o modelo de lista
        listModel.clear();

        // Obtém os usuários do banco de dados e os adiciona ao modelo de lista
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM usuarios");
            while (rs.next()) {
                String nome = rs.getString("nome");
                listModel.addElement(nome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao obter lista de usuários.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método principal para executar a aplicação Swing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Configuração de Usuários");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            ConfigUsers configUsers = new ConfigUsers();
            frame.add(configUsers);

            frame.setVisible(true);
        });
    }

}

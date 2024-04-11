import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ConfigUsers extends JPanel {
    private List<Usuario> usuarios; // Lista de usuários
    private DefaultListModel<String> listModel; // Modelo de lista para o JList

    public ConfigUsers() {
        usuarios = new ArrayList<>(); // Inicializa a lista de usuários
        listModel = new DefaultListModel<>(); // Inicializa o modelo de lista

        setLayout(new BorderLayout()); // Define o layout do painel como BorderLayout

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Painel para os botões de ação
        JButton addButton = new JButton("➕"); // Botão para adicionar usuário
        JButton editButton = new JButton("🖊"); // Botão para editar usuário
        JButton removeButton = new JButton("❌"); // Botão para remover usuário

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
                String nome = JOptionPane.showInputDialog("Digite o nome do usuário:");
                if (nome != null && !nome.isEmpty()) {
                    boolean usuarioExistente = false;
                    for (Usuario usuario : usuarios) {
                        if (usuario.getNome().equals(nome)) {
                            usuarioExistente = true;
                            break;
                        }
                    }
                    if (usuarioExistente) {
                        JOptionPane.showMessageDialog(null, "Já existe um usuário com esse nome.", "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        Usuario usuario = new Usuario(nome);
                        usuarios.add(usuario);
                        listModel.addElement(usuario.getNome());
                        JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // Adiciona um ActionListener ao botão de editar usuário
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeAntigo = JOptionPane.showInputDialog("Digite o nome do usuário a ser editado:");
                boolean usuarioEncontrado = false;
                for (Usuario usuario : usuarios) {
                    if (usuario.getNome().equals(nomeAntigo)) {
                        String novoNome = JOptionPane.showInputDialog("Digite o novo nome do usuário:");
                        if (novoNome != null && !novoNome.isEmpty()) {
                            usuario.setNome(novoNome);
                            atualizarListaUsuarios();
                            usuarioEncontrado = true;
                            JOptionPane.showMessageDialog(null, "Usuário editado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }
                    }
                }
                if (!usuarioEncontrado) {
                    JOptionPane.showMessageDialog(null, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Adiciona um ActionListener ao botão de remover usuário
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeUsuario = JOptionPane.showInputDialog("Digite o nome do usuário a ser removido:");
                Usuario usuarioRemover = null;
                for (Usuario usuario : usuarios) {
                    if (usuario.getNome().equals(nomeUsuario)) {
                        usuarioRemover = usuario;
                        break;
                    }
                }
                if (usuarioRemover != null) {
                    usuarios.remove(usuarioRemover);
                    listModel.removeElement(nomeUsuario);
                    JOptionPane.showMessageDialog(null, "Usuário removido com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Inicializa a lista de usuários
        atualizarListaUsuarios();
    }

    // Atualiza a lista de usuários no JList
    private void atualizarListaUsuarios() {
        listModel.clear(); // Limpa o modelo de lista
        for (Usuario usuario : usuarios) {
            listModel.addElement(usuario.getNome()); // Adiciona cada usuário ao modelo de lista
        }
    }

    // Classe interna para representar um usuário
    private class Usuario {
        private String nome;

        public Usuario(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
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


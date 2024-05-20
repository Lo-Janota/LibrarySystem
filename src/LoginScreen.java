import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginScreen extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Login");
        setSize(400, 350); // Configura tamanho da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Configura ação ao fechar janela
        setLocationRelativeTo(null); // Centraliza janela
        setLayout(new BorderLayout()); // Altera o layout principal para BorderLayout

        // Cria um JPanel para conter a imagem no topo e centralizada
        JPanel imagePanel = new JPanel(new BorderLayout());
        // Carrega a imagem do arquivo imageLogin.jpg
        ImageIcon icon = new ImageIcon("imagens/imageLogin.jpg");
        // Redimensiona a imagem para um tamanho específico
        Image image = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        // Cria um JLabel com a imagem redimensionada e o posiciona no centro do JPanel
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Adiciona o JPanel com a imagem no topo da tela
        add(imagePanel, BorderLayout.NORTH);

        // Cria um JPanel para os campos de texto e botão
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS)); // Organiza os componentes verticalmente
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Adiciona margem

        usernameLabel = new JLabel("Usuário:");
        passwordLabel = new JLabel("Senha:");
        usernameField = new JTextField(20); // Define o tamanho do campo de usuário
        passwordField = new JPasswordField(20); // Define o tamanho do campo de senha
        JButton loginButton = new JButton("Login");

        // Define o tamanho mínimo e máximo da altura dos campos de texto
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height));
        usernameField.setMinimumSize(new Dimension(0, usernameField.getPreferredSize().height));
        passwordField.setMinimumSize(new Dimension(0, passwordField.getPreferredSize().height));

        // Adiciona os componentes ao JPanel
        fieldsPanel.add(usernameLabel);
        fieldsPanel.add(usernameField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        // Adiciona tooltips (legendas) aos campos de texto
        usernameField.setToolTipText("Informe seu usuário");
        passwordField.setToolTipText("Informe sua senha");

        // Adiciona um espaço flexível entre os campos de senha e o botão de login
        fieldsPanel.add(Box.createVerticalGlue());

        // Adiciona o JPanel com os campos de texto e botão abaixo da imagem
        add(fieldsPanel, BorderLayout.CENTER);

        // Cria um JPanel com um FlowLayout para centralizar o botão de login
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        // Adiciona o JPanel do botão de login ao final da tela
        add(buttonPanel, BorderLayout.SOUTH);

        // Adiciona um KeyListener ao campo de senha para verificar se a tecla Enter foi pressionada
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        // Verifica se o usuário e a senha foram preenchidos
        if (usernameField.getText().isEmpty() && passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(LoginScreen.this, "Por favor, preencha os campos: Usuário e Senha.");
        } else if (usernameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(LoginScreen.this, "Por favor, preencha o campo: Usuário.");
        } else if (passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(LoginScreen.this, "Por favor, preencha o campo: Senha.");
        } else {
            try {
                Connection conn = obterConexao();
                if (conn != null) {
                    new Menu(new LivroDAOImpl(conn)).setVisible(true);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this, "Erro ao conectar ao banco de dados.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginScreen.this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
            }
        }
    }

    private Connection obterConexao() throws SQLException {
        String url = "jdbc:sqlite:dataBase.db"; // Atualize com o caminho do seu banco de dados
        return DriverManager.getConnection(url);
    }

    /*
    ADICIONAR A FUNCIONALIDADE PARA CRIAR A TABELA DE USUARIOS
    - CODIGO, NOME, SENHA
    - JA CRIAR COM USUARIO (ADMIN)
    - VALIDADAR SE O USUARIO JA EXISTE NA TABELA (CASO NAO RETORNAR ERRO E NAO IR PARA A TELA DE MENU)
    - CRIAR ESTILOS DE PERMISSÃO -> USUÁRIO SÓ PODE PESQUISAR E FAZER EMPRÉSTIMOS DOS LIVROS QUE ESTÃO DISPONÍVELS.. FUNCIONÁRIOS PODEM MEXER NO SISTEMA GERAL
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
    }
}

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginScreen() {

        setTitle("Livraria Login");
        setSize(400, 500); // Configura tamanho da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Configura ação ao fechar janela
        setLocationRelativeTo(null); // Centraliza janela
        setLayout(new BorderLayout()); // Altera o layout principal para BorderLayout

        usernameLabel = new JLabel("Usuário:");
        passwordLabel = new JLabel("Senha:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Painel para agrupar os campos e labels
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS)); // Organiza os componentes verticalmente
        fieldsPanel.add(usernameLabel);
        fieldsPanel.add(usernameField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        // Centraliza os componentes no fieldsPanel
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Adiciona uma margem ao redor dos campos
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adiciona o fieldsPanel ao centro da tela de login
        add(fieldsPanel, BorderLayout.CENTER);

        // Adiciona o botão de login na parte inferior
        loginButton.setLayout(new FlowLayout(FlowLayout.CENTER)); // Centraliza o botão no painel
        loginButton.add(loginButton);
        add(loginButton, BorderLayout.SOUTH);

        // Ação do botão de login
        loginButton.addActionListener(e -> {
            new Menu().setVisible(true); // Abre a janela do menu ao clicar no botão
            this.setVisible(false); // Faz a tela de login desaparecer
        });

    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}

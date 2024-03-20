import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;



public class LoginScreen extends JFrame {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ImageIcon backgroundImage; // Removido o final

    public LoginScreen() {

        setTitle("Livraria Login");
        setSize(400, 500); // Configura tamanho da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Configura ação ao fechar janela
        setLocationRelativeTo(null); // Centraliza janela
        usernameLabel = new JLabel("Usuário:");
        passwordLabel = new JLabel("Senha:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        try {
            backgroundImage = new ImageIcon(ImageIO.read(new File("./Login.png")));
        } catch (IOException e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundImage = null; // Agora é permitido pois 'backgroundImage' não é final
            // Removido o 'return'; o frame ainda pode ser exibido sem a imagem de fundo.
        }

        // Configurações da interface
        setLayout(new BorderLayout());
        JLabel backgroundLabel = new JLabel(backgroundImage);
        JButton loginButton = new JButton("Login");
        backgroundLabel.setLayout(new GridBagLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(30, 10, 10, 10); // Margens

        // Adiciona componentes ao label de fundo
        backgroundLabel.add(usernameLabel, gbc);
        backgroundLabel.add(usernameField, gbc);
        backgroundLabel.add(passwordLabel, gbc);
        backgroundLabel.add(passwordField, gbc);
        backgroundLabel.add(loginButton, gbc);
    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}
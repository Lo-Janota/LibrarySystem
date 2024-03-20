import javax.swing.*;
import java.awt.*;

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
        ImageIcon icon = new ImageIcon("imageLogin.jpg");
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

        // Adiciona um espaço flexível entre os campos de senha e o botão de login
        fieldsPanel.add(Box.createVerticalGlue());

        // Adiciona o JPanel com os campos de texto e botão abaixo da imagem
        add(fieldsPanel, BorderLayout.CENTER);

        // Cria um JPanel com um FlowLayout para centralizar o botão de login
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(loginButton);
        // Adiciona o JPanel do botão de login ao final da tela
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            new Menu().setVisible(true); // Abre a janela do menu ao clicar no botão
            this.setVisible(false); // Faz a tela de login desaparecer
        });

    }

    public static void main(String[] args) {
        new LoginScreen().setVisible(true);
    }
}

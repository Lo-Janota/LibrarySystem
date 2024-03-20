import javax.swing.*;
import java.awt.*;

public class Menu extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JButton addButton;
    private JButton deleteButton;

    public Menu() {
        setTitle("Menu");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Configura ação ao fechar janela
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new FlowLayout()); // Layout simples para os elementos

        // Cria e adiciona os componentes na tela
        searchField = new JTextField(20);
        searchButton = new JButton("🔎");
        addButton = new JButton("Adicionar");
        deleteButton = new JButton("Excluir");

        searchButton.setPreferredSize(new Dimension(60, 30));
        addButton.setPreferredSize(new Dimension(100, 30));
        deleteButton.setPreferredSize(new Dimension(100, 30));

        add(searchField);
        add(searchButton);
        add(addButton);
        add(deleteButton);
    }

    
}

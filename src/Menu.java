import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private JTextField searchField; // Campo de texto para pesquisa
    private JButton searchButton; // Botão de pesquisa
    private JButton addButton; // Botão de adicionar livro
    private JButton deleteButton; // Botão de excluir livro
    private JButton configButton; // Botão de configuração
    private JTextArea bookListArea; // Área de texto para listar os livros
    private Biblioteca biblioteca; // Instância da classe Biblioteca para gerenciar os livros
    private ConfigUsers ConfigUsers; // Painel de configuração

    public Menu() {
        setTitle("Menu Biblioteca"); // Define o título da janela
        setSize(700, 400); // Define o tamanho da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Define a operação padrão ao fechar a janela
        setLocationRelativeTo(null); // Define a posição da janela como centralizada na tela
        setLayout(new BorderLayout()); // Define o layout da janela como BorderLayout

        biblioteca = new Biblioteca(); // Inicializa a instância da classe Biblioteca

        searchField = new JTextField(20); // Cria um campo de texto para pesquisa com tamanho 20
        searchButton = new JButton("🔎"); // Cria um botão de pesquisa com ícone
        addButton = new JButton("➕"); // Cria um botão de adicionar livro com ícone
        deleteButton = new JButton("❌"); // Cria um botão de excluir livro com ícone
        configButton = new JButton("🔧"); // Cria um botão de configuração com ícone
        bookListArea = new JTextArea(); // Cria uma área de texto para listar os livros
        bookListArea.setEditable(false); // Define a área de texto como somente leitura

        JPanel topPanel = new JPanel(new BorderLayout()); // Cria um painel superior com layout BorderLayout
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Painel para centralizar a barra de pesquisa
        JPanel emptyPanel = new JPanel(); // Painel vazio para alinhar a barra de pesquisa à esquerda

        searchPanel.add(searchField); // Adiciona o campo de pesquisa ao painel
        searchPanel.add(searchButton); // Adiciona o botão de pesquisa ao painel
        searchPanel.add(addButton); // Adiciona o botão de adicionar livro ao painel
        searchPanel.add(deleteButton); // Adiciona o botão de excluir livro ao painel

        topPanel.add(searchPanel, BorderLayout.CENTER); // Adiciona o painel de pesquisa ao centro do painel superior
        topPanel.add(configButton, BorderLayout.WEST); // Adiciona o botão de configuração à esquerda do painel superior

        // Define o tamanho dos botões -> passa uma variavel para pegar esse tamanho
        Dimension buttonSize = new Dimension(60, 30);
        searchButton.setPreferredSize(buttonSize);
        addButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        configButton.setPreferredSize(buttonSize);

        // Adiciona tooltips (legenda) aos botões
        searchButton.setToolTipText("Pesquisar");
        addButton.setToolTipText("Adicionar");
        deleteButton.setToolTipText("Excluir");
        configButton.setToolTipText("Configuração");

        JScrollPane scrollPane = new JScrollPane(bookListArea); // Cria um JScrollPane para a área de texto para listar os livros

        add(topPanel, BorderLayout.NORTH); // Adiciona o painel superior à parte norte da janela
        add(scrollPane, BorderLayout.CENTER); // Adiciona a área de texto com scroll à parte central da janela

        // Adiciona um ActionListener ao botão de configuração
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ConfigUsers == null) {
                    ConfigUsers = new ConfigUsers(); // Cria o painel de configuração se ainda não existir
                }
                JOptionPane.showMessageDialog(null, ConfigUsers, "Configurar Usuários", JOptionPane.PLAIN_MESSAGE); // Exibe o painel de configuração em um JOptionPane
            }
        });

        // Adiciona um ActionListener ao botão de pesquisa
        searchButton.addActionListener(new ActionListener() { // Adiciona um ActionListener ao botão de pesquisa
            @Override
            public void actionPerformed(ActionEvent e) { // Sobrescreve o método actionPerformed
                String termoPesquisa = searchField.getText().toLowerCase(); // Obtém o termo de pesquisa do campo de texto e converte para minúsculas
                StringBuilder resultado = new StringBuilder(); // Cria um StringBuilder para armazenar o resultado da busca
                boolean livroEncontrado = false; // Variável para verificar se pelo menos um livro foi encontrado
                for (Livro livro : biblioteca.getLivros()) { // Itera sobre a lista de livros da biblioteca
                    if (livro.getTitulo().toLowerCase().contains(termoPesquisa) || // Verifica se o título, autor ou categoria do livro contém o termo de pesquisa
                            livro.getAutor().toLowerCase().contains(termoPesquisa) ||
                            livro.getCategoria().toLowerCase().contains(termoPesquisa)) {
                        resultado.append(livro).append("\n"); // Adiciona o livro ao resultado se for encontrado
                        livroEncontrado = true; // Define que pelo menos um livro foi encontrado
                    }
                }
                if (!livroEncontrado) { // Se nenhum livro foi encontrado
                    JOptionPane.showMessageDialog(null, "Nenhum livro foi encontrado.", "Nenhum resultado", JOptionPane.INFORMATION_MESSAGE); // Exibe um JOptionPane com a mensagem
                } else {
                    bookListArea.setText(resultado.toString()); // Atualiza o texto da área de texto com o resultado da busca
                }
            }
        });

        // Adiciona um ActionListener ao botão de adicionar livro
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = JOptionPane.showInputDialog("Digite o título do livro:");
                String autor = JOptionPane.showInputDialog("Digite o autor do livro:");
                String categoria = JOptionPane.showInputDialog("Digite a categoria do livro:");

                // Validação dos campos
                if (titulo != null && !titulo.isEmpty() &&
                        autor != null && !autor.isEmpty() &&
                        categoria != null && !categoria.isEmpty()) {
                    biblioteca.adicionarLivro(new Livro(titulo, autor, categoria));
                    atualizarListaLivros();
                } else {
                    JOptionPane.showMessageDialog(null, "ATENÇÃO!\nTodos os campos devem ser preenchidos.");
                }
            }
        });

        // Adiciona um ActionListener ao botão de excluir livro
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String termoPesquisa = searchField.getText().toLowerCase();
                Livro livroSelecionado = null;
                for (Livro livro : biblioteca.getLivros()) {
                    if (livro.getTitulo().toLowerCase().contains(termoPesquisa)) {
                        livroSelecionado = livro;
                        break; // Interrompe o loop ao encontrar o primeiro livro correspondente
                    }
                }
                if (livroSelecionado != null) {
                    biblioteca.removerLivro(livroSelecionado);
                    atualizarListaLivros();
                } else {
                    JOptionPane.showMessageDialog(null, "Livro não encontrado.");
                }
            }
        });
        

        atualizarListaLivros(); // Atualiza a lista de livros ao iniciar a aplicação
    }

    // Atualiza a lista de livros na área de texto
    private void atualizarListaLivros() {
        StringBuilder listaLivros = new StringBuilder();
        for (Livro livro : biblioteca.getLivros()) {
            listaLivros.append(livro).append("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        }
        bookListArea.setText(listaLivros.toString());
    }

    // Método principal para executar a aplicação Swing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.setVisible(true);
        });
    }
}

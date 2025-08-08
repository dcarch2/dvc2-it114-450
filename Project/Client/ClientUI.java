package Project.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Project.Common.Constants;

/**
 * DVC2 - 8/07/2025 - This class implements the graphical user interface for the client, including panels for connection,
 * ready checks, and the main game area.
 */
public class ClientUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField usernameField;
    private JTextField hostField;
    private JTextField portField;

    private JButton readyButton;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;

    private JTextArea gameEventArea;
    private JList<String> userList;
    private DefaultListModel<String> userListModel;
    
    public ClientUI() {
        setTitle("Rock Paper Scissors Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize panels
        setupConnectPanel();
        setupReadyCheckPanel();
        setupGamePanel();

        add(mainPanel);
        setVisible(true);

        // Show the connection panel first
        cardLayout.show(mainPanel, "ConnectPanel");
    }

    private void setupConnectPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Components for the Connect Panel
        usernameField = new JTextField(15);
        hostField = new JTextField(15);
        portField = new JTextField(15);
        JButton connectButton = new JButton("Connect");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Host:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(hostField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Port:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(portField, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3; panel.add(connectButton, gbc);

        connectButton.addActionListener(e -> {
            // TODO: Implement connection logic here
            // String username = usernameField.getText();
            // String host = hostField.getText();
            // int port = Integer.parseInt(portField.getText());
            // Connect to server and then switch panels
            cardLayout.show(mainPanel, "ReadyCheckPanel");
        });

        mainPanel.add(panel, "ConnectPanel");
    }

    private void setupReadyCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel readyStatusLabel = new JLabel("Click 'Ready' to start a new game session.", SwingConstants.CENTER);
        readyButton = new JButton("Ready");

        panel.add(readyStatusLabel, BorderLayout.CENTER);
        panel.add(readyButton, BorderLayout.SOUTH);
        
        readyButton.addActionListener(e -> {
            // TODO: Send /ready command to server
            // After ready check is complete (server confirms), switch to game panel
            cardLayout.show(mainPanel, "GamePanel");
        });

        mainPanel.add(panel, "ReadyCheckPanel");
    }

    private void setupGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Top section for game events
        gameEventArea = new JTextArea(10, 30);
        gameEventArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameEventArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Right side for user list
        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(200, 0));
        panel.add(userScrollPane, BorderLayout.EAST);
        
        // Bottom section for game controls
        JPanel controlsPanel = new JPanel();
        rockButton = new JButton("Rock");
        paperButton = new JButton("Paper");
        scissorsButton = new JButton("Scissors");
        controlsPanel.add(rockButton);
        controlsPanel.add(paperButton);
        controlsPanel.add(scissorsButton);
        panel.add(controlsPanel, BorderLayout.SOUTH);

        rockButton.addActionListener(e -> {
            // TODO: Send /pick r command to server
        });
        paperButton.addActionListener(e -> {
            // TODO: Send /pick p command to server
        });
        scissorsButton.addActionListener(e -> {
            // TODO: Send /pick s command to server
        });

        mainPanel.add(panel, "GamePanel");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientUI::new);
    }
}
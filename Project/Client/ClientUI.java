import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Project.Client.Interfaces.ICardControls;
import Project.Client.Interfaces.IConnectionEvents;
import Project.Client.Interfaces.IMessageEvents;
import Project.Client.Interfaces.IRoomEvents;
import Project.Client.Views.ChatGameView;
import Project.Client.Views.ConnectionView;
import Project.Client.Views.MenuBar;
import Project.Client.Views.RoomsView;
import Project.Client.Views.UserDetailsView;
import Project.Common.Constants;
import Project.Common.LoggerUtil;

public class ClientUI extends JFrame implements ICardControls, IConnectionEvents, IRoomEvents {
    private CardLayout cardLayout = new CardLayout();
    private Container frameContainer;
    private JPanel cardContainer;
    private JPanel activeCardViewPanel;
    private CardViewName activeCardViewEnum;
    private String originalTitle = "";
    private JMenuBar menuBar;
    private JLabel currentRoomLabel = new JLabel(Constants.NOT_CONNECTED);
    // separate UI views
    private ConnectionView connectionView;
    private UserDetailsView userDetailsView;
    private ChatGameView chatGameView;
    private RoomsView roomsView;
    
    // DVC2 - 8/6/2025 - Refactored main to launch the UI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ClientUI("dvc2-Client");
            } catch (Throwable t) {
                LoggerUtil.INSTANCE.severe("Unhandled exception in main thread", t);
            }
        });
    }

    public ClientUI(String title) {
        super(title);
        originalTitle = title;
        setMinimumSize(new Dimension(400, 400));
        setSize(getMinimumSize());
        setLocationRelativeTo(null);
        Client.INSTANCE.registerCallback(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int response = JOptionPane.showConfirmDialog(cardContainer,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        Client.INSTANCE.sendDisconnect();
                    } catch (NullPointerException | IOException e) {
                        LoggerUtil.INSTANCE.severe("Error during disconnect: " + e.getMessage());
                    }
                    System.exit(0);
                }
            }
        });
        menuBar = new MenuBar(this);
        this.setJMenuBar(menuBar);
        frameContainer = getContentPane();
        cardContainer = new JPanel();
        cardContainer.setLayout(cardLayout);
        frameContainer.add(currentRoomLabel, BorderLayout.NORTH);
        frameContainer.add(cardContainer, BorderLayout.CENTER);
        connectionView = new ConnectionView(this);
        userDetailsView = new UserDetailsView(this);
        chatGameView = new ChatGameView(this);
        roomsView = new RoomsView(this);
        roomsView.setVisible(false);
        pack();
        setVisible(true);
    }
    // All other methods as per the provided `matttoegel` file.
    // ...
}
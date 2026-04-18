package client.admin.view;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;
import client.ui.components.MinecraftButton;
import client.ui.components.MinecraftLabel;
import client.ui.components.MinecraftPanel;
import client.ui.components.MinecraftTextField;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;

public class AdminDashboardView extends JFrame {

    private final PlayerManagementView playerManagementView = new PlayerManagementView();
    private final JTextField waitingTimeField = new MinecraftTextField(8);
    private final JTextField roundDurationField = new MinecraftTextField(8);
    private final JButton loadConfigButton = new MinecraftButton("LOAD CONFIG");
    private final JButton saveConfigButton = new MinecraftButton("SAVE CONFIG");
    private final JLabel configMessageLabel = new JLabel(" ");
    private final JButton closeButton = new MinecraftButton("LOGOUT");

    private final JButton playersNavButton = new MinecraftButton("PLAYERS");
    private final JButton configNavButton = new MinecraftButton("CONFIG");
    private final JPanel contentCards = new JPanel(new CardLayout());

    public AdminDashboardView() {
        setTitle("Wordy - Admin Dashboard");
        setSize(1220, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MinecraftPanel root = new MinecraftPanel("/textures/stone.png", 95);
        root.setLayout(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        root.add(buildTopBar(), BorderLayout.NORTH);
        root.add(buildSidebar(), BorderLayout.WEST);

        contentCards.setOpaque(false);
        contentCards.add(wrapCard(playerManagementView), "players");
        contentCards.add(buildConfigPanel(), "config");
        root.add(contentCards, BorderLayout.CENTER);

        playersNavButton.addActionListener(e -> showCard("players"));
        configNavButton.addActionListener(e -> showCard("config"));

        setContentPane(root);
    }

    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(new MinecraftLabel("ADMIN CONSOLE", MinecraftFonts.LARGE, MinecraftColors.TEXT_RED), BorderLayout.WEST);
        top.add(closeButton, BorderLayout.EAST);
        return top;
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setOpaque(true);
        sidebar.setBackground(new Color(0, 0, 0, 125));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        playersNavButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        configNavButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        sidebar.add(playersNavButton);
        sidebar.add(javax.swing.Box.createVerticalStrut(8));
        sidebar.add(configNavButton);
        return sidebar;
    }

    private JPanel buildConfigPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 8));
        panel.setOpaque(false);
        configMessageLabel.setFont(MinecraftFonts.SMALL);
        configMessageLabel.setForeground(MinecraftColors.TEXT_AQUA);
        panel.add(new MinecraftLabel("Waiting Time (s)", MinecraftFonts.REGULAR, MinecraftColors.TEXT_WHITE));
        panel.add(waitingTimeField);
        panel.add(new MinecraftLabel("Round Duration (s)", MinecraftFonts.REGULAR, MinecraftColors.TEXT_WHITE));
        panel.add(roundDurationField);
        panel.add(loadConfigButton);
        panel.add(saveConfigButton);
        panel.add(configMessageLabel);
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        panel.add(new JLabel());
        return wrapCard(panel);
    }

    private JPanel wrapCard(JComponent body) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(true);
        wrapper.setBackground(new Color(0, 0, 0, 120));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        wrapper.add(body, BorderLayout.CENTER);
        return wrapper;
    }

    private void showCard(String card) {
        CardLayout layout = (CardLayout) contentCards.getLayout();
        layout.show(contentCards, card);
    }

    public PlayerManagementView getPlayerManagementView() { return playerManagementView; }
    public JTextField getWaitingTimeField() { return waitingTimeField; }
    public JTextField getRoundDurationField() { return roundDurationField; }
    public JButton getLoadConfigButton() { return loadConfigButton; }
    public JButton getSaveConfigButton() { return saveConfigButton; }
    public JLabel getConfigMessageLabel() { return configMessageLabel; }
    public JButton getCloseButton() { return closeButton; }
}


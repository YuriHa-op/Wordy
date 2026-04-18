package client.player.view;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;
import client.ui.components.MinecraftButton;
import client.ui.components.MinecraftLabel;
import client.ui.components.MinecraftPanel;

import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class HomeView extends JFrame {

    private final JButton startGameButton = new MinecraftButton("START WORDY GAME");
    private final JButton leaderboardButton = new MinecraftButton("VIEW TOP PLAYERS");
    private final JButton logoutButton = new MinecraftButton("LOGOUT");

    private final MinecraftLabel titleLabel = new MinecraftLabel("WORDY", MinecraftFonts.TITLE, MinecraftColors.TEXT_YELLOW);

    public HomeView() {
        setTitle("Wordy - Home");
        setSize(920, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MinecraftPanel background = new MinecraftPanel("/textures/grass.png", 85);
        background.setLayout(new BorderLayout(16, 16));

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(0, 110));
        background.add(titleLabel, BorderLayout.NORTH);

        MinecraftLabel welcome = new MinecraftLabel("Welcome to the server!", MinecraftFonts.REGULAR, MinecraftColors.TEXT_AQUA);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new BorderLayout(8, 12));
        center.setOpaque(false);
        center.add(welcome, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setPreferredSize(new Dimension(360, 210));
        menu.setMaximumSize(new Dimension(360, 210));

        startGameButton.setMaximumSize(new Dimension(320, 46));
        leaderboardButton.setMaximumSize(new Dimension(320, 46));
        logoutButton.setMaximumSize(new Dimension(320, 46));

        menu.add(startGameButton);
        menu.add(Box.createVerticalStrut(12));
        menu.add(leaderboardButton);
        menu.add(Box.createVerticalStrut(12));
        menu.add(logoutButton);

        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel menuHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        menuHolder.setOpaque(false);
        menuHolder.add(menu);
        center.add(menuHolder, BorderLayout.CENTER);
        background.add(center, BorderLayout.CENTER);

        MinecraftLabel footer = new MinecraftLabel("MC://server.wordy.game  v1.0", MinecraftFonts.SMALL, MinecraftColors.TEXT_GRAY);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(footer, BorderLayout.SOUTH);

        setContentPane(background);
    }

    public JButton getStartGameButton() { return startGameButton; }
    public JButton getLeaderboardButton() { return leaderboardButton; }
    public JButton getLogoutButton() { return logoutButton; }
}


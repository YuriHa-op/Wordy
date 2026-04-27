package client.player.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.components.StyledButton;
import client.ui.components.StyledLabel;
import client.ui.components.StyledPanel;

public class HomeView extends JFrame {

    private final JButton startGameButton = new StyledButton("START WORDY GAME");
    private final JButton leaderboardButton = new StyledButton("VIEW TOP PLAYERS");
    private final JButton logoutButton = new StyledButton("LOGOUT");

    private final StyledLabel titleLabel = new StyledLabel("WORDY", UiFonts.TITLE, UiColors.TEXT_YELLOW);

    public HomeView() {
        setTitle("Wordy - Home");
        setSize(920, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        StyledPanel background = new StyledPanel("/textures/clienthome.png", 35);
        background.setLayout(new BorderLayout(16, 16));

        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(0, 110));
        background.add(titleLabel, BorderLayout.NORTH);

        StyledLabel welcome = new StyledLabel("Welcome to the server!", UiFonts.REGULAR, UiColors.TEXT_AQUA);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel(new BorderLayout(8, 12));
        center.setOpaque(false);
        center.add(welcome, BorderLayout.NORTH);

        JPanel menu = new JPanel();
        menu.setOpaque(false);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setPreferredSize(new Dimension(360, 300));
        menu.setMaximumSize(new Dimension(360, 300));

        Dimension menuButtonSize = new Dimension(320, 72);
        startGameButton.setPreferredSize(menuButtonSize);
        startGameButton.setMaximumSize(menuButtonSize);
        leaderboardButton.setPreferredSize(menuButtonSize);
        leaderboardButton.setMaximumSize(menuButtonSize);
        logoutButton.setPreferredSize(menuButtonSize);
        logoutButton.setMaximumSize(menuButtonSize);

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

        StyledLabel footer = new StyledLabel("hotdogdiggydidog-v1.0", UiFonts.SMALL, UiColors.TEXT_GRAY);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        background.add(footer, BorderLayout.SOUTH);

        setContentPane(background);
    }

    public JButton getStartGameButton() { return startGameButton; }
    public JButton getLeaderboardButton() { return leaderboardButton; }
    public JButton getLogoutButton() { return logoutButton; }
}


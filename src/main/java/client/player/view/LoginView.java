package client.player.view;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.components.StyledButton;
import client.ui.components.StyledLabel;
import client.ui.components.StyledPasswordField;
import client.ui.components.StyledPanel;
import client.ui.components.StyledTextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class LoginView extends JFrame {

    private final JTextField usernameField = new StyledTextField(20);
    private final JPasswordField passwordField = new StyledPasswordField(20);
    private final JButton loginButton = new StyledButton("LOGIN");
    private final JLabel messageLabel = new JLabel(" ");

    public LoginView() {
        setTitle("Wordy - Player Login");
        setSize(860, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        StyledPanel background = new StyledPanel("/textures/dirt.png", 80);
        background.setLayout(new GridBagLayout());

        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(true);
        card.setBackground(new Color(0, 0, 0, 125));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 3),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));
        card.setPreferredSize(new Dimension(500, 360));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        StyledLabel title = new StyledLabel("WORDY", UiFonts.TITLE, UiColors.TEXT_YELLOW);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        card.add(new StyledLabel("Username", UiFonts.REGULAR, UiColors.TEXT_WHITE), gbc);
        gbc.gridx = 1;
        card.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(new StyledLabel("Password", UiFonts.REGULAR, UiColors.TEXT_WHITE), gbc);
        gbc.gridx = 1;
        card.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        card.add(loginButton, gbc);

        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(UiFonts.SMALL);
        messageLabel.setForeground(UiColors.TEXT_RED);
        gbc.gridy = 4;
        card.add(messageLabel, gbc);

        background.add(card);
        setContentPane(background);

        getRootPane().setDefaultButton(loginButton);
    }


    public JTextField getUsernameField() { return usernameField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getLoginButton() { return loginButton; }
    public JLabel getMessageLabel() { return messageLabel; }
}


package client.ui.components;

import client.ui.UiColors;
import client.ui.UiFonts;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopNotification {

    public static void showNoMatchFound(JFrame parent) {
        JDialog dialog = new JDialog(parent, true);
        dialog.setUndecorated(true);
        dialog.setSize(480, 220);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(15, 15, 15));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel title = new JLabel("NO MATCH FOUND", JLabel.CENTER);
        title.setFont(UiFonts.LARGE);
        title.setForeground(new Color(255, 170, 0));

        JLabel details = new JLabel("Matchmaking timer ran out.", JLabel.CENTER);
        details.setFont(UiFonts.REGULAR);
        details.setForeground(UiColors.TEXT_WHITE);

        StyledButton ok = new StyledButton("OK");
        ok.setPreferredSize(new Dimension(160, 42));
        ok.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(ok);

        panel.add(title, BorderLayout.NORTH);
        panel.add(details, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public static void showGameOver(JFrame parent, String currentUsername, String winnerName) {
        boolean isWinner = currentUsername != null && currentUsername.equalsIgnoreCase(winnerName);

        JDialog dialog = new JDialog(parent, true);
        dialog.setUndecorated(true);
        dialog.setSize(700, 320);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(15, 15, 15));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel result = new JLabel(isWinner ? "YOU WIN!" : "YOU LOST!", JLabel.CENTER);
        result.setFont(UiFonts.TITLE.deriveFont(52f));
        result.setForeground(isWinner ? new Color(85, 255, 85) : new Color(255, 85, 85));

        JLabel winner = new JLabel("Winner: " + winnerName, JLabel.CENTER);
        winner.setFont(UiFonts.LARGE);
        winner.setForeground(new Color(255, 85, 85));

        JPanel centerContent = new JPanel(new BorderLayout(8, 8));
        centerContent.setOpaque(false);
        centerContent.add(result, BorderLayout.CENTER);
        if (!isWinner) {
            centerContent.add(winner, BorderLayout.SOUTH);
        }

        panel.add(centerContent, BorderLayout.CENTER);

        JLabel clickHint = new JLabel("Click anywhere to continue", JLabel.CENTER);
        clickHint.setFont(UiFonts.SMALL);
        clickHint.setForeground(new Color(170, 170, 170));
        panel.add(clickHint, BorderLayout.SOUTH);

        MouseAdapter closeOnClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        };
        panel.addMouseListener(closeOnClick);
        centerContent.addMouseListener(closeOnClick);
        result.addMouseListener(closeOnClick);
        winner.addMouseListener(closeOnClick);
        clickHint.addMouseListener(closeOnClick);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public static void showSessionEnded(JFrame parent, String message) {
        JDialog dialog = new JDialog(parent, true);
        dialog.setUndecorated(true);
        dialog.setSize(520, 220);
        dialog.setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(15, 15, 15));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel title = new JLabel("SESSION ENDED", JLabel.CENTER);
        title.setFont(UiFonts.LARGE);
        title.setForeground(new Color(255, 170, 0));

        JLabel details = new JLabel(message, JLabel.CENTER);
        details.setFont(UiFonts.REGULAR);
        details.setForeground(UiColors.TEXT_WHITE);

        StyledButton ok = new StyledButton("OK");
        ok.setPreferredSize(new Dimension(160, 42));
        ok.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(ok);

        panel.add(title, BorderLayout.NORTH);
        panel.add(details, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
}

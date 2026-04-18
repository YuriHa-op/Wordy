package client.player.view;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;
import client.ui.components.InventorySlot;
import client.ui.components.MinecraftButton;
import client.ui.components.MinecraftLabel;
import client.ui.components.MinecraftPanel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

public class LeaderboardView extends JFrame {

    private final JTextArea boardArea = new JTextArea();
    private final JButton backButton = new MinecraftButton("BACK HOME");
    private final JPanel winnersPanel = new JPanel(new GridLayout(5, 1, 0, 6));
    private final JPanel wordsPanel = new JPanel(new GridLayout(5, 1, 0, 6));

    public LeaderboardView() {
        setTitle("Wordy - Leaderboard");
        setSize(980, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        MinecraftPanel root = new MinecraftPanel("/textures/stone.png", 85);
        root.setLayout(new BorderLayout(10, 10));
        root.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        MinecraftLabel title = new MinecraftLabel("TOP PLAYERS", MinecraftFonts.LARGE, MinecraftColors.TEXT_GOLD);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(title, BorderLayout.NORTH);

        winnersPanel.setOpaque(false);
        wordsPanel.setOpaque(false);

        JPanel center = new JPanel(new GridLayout(2, 1, 0, 10));
        center.setOpaque(false);
        center.add(wrapSection("Most Wins", winnersPanel));
        center.add(wrapSection("Longest Words", wordsPanel));
        root.add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(backButton, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);

        boardArea.setEditable(false);
        boardArea.setVisible(false);
    }

    public void render(List<String> topPlayers, List<String> longestWords) {
        winnersPanel.removeAll();
        wordsPanel.removeAll();

        fillSlots(winnersPanel, topPlayers);
        fillSlots(wordsPanel, longestWords);

        winnersPanel.revalidate();
        wordsPanel.revalidate();
        winnersPanel.repaint();
        wordsPanel.repaint();
    }

    private JPanel wrapSection(String title, JPanel content) {
        JPanel section = new JPanel(new BorderLayout(8, 8));
        section.setOpaque(true);
        section.setBackground(new Color(0, 0, 0, 110));
        section.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(Color.BLACK, 2),
                javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        section.add(new MinecraftLabel(title, MinecraftFonts.REGULAR, MinecraftColors.TEXT_YELLOW), BorderLayout.NORTH);
        section.add(content, BorderLayout.CENTER);
        return section;
    }

    private void fillSlots(JPanel panel, List<String> lines) {
        if (lines.isEmpty()) {
            panel.add(new InventorySlot("No records found"));
            return;
        }
        for (String line : lines) {
            panel.add(new InventorySlot(line));
        }
    }

    public JTextArea getBoardArea() { return boardArea; }
    public JButton getBackButton() { return backButton; }
}


package client.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ui.util.TextureLoader;

public class HealthBar extends JPanel {
    private static final int HEART_SIZE = 26;
    private static final int HEART_GAP = 8;
    private static final ImageIcon FILLED_HEART_ICON = createIcon(TextureLoader.load("/textures/heart.png"));
    private static final ImageIcon EMPTY_HEART_ICON = createIcon(TextureLoader.load("/textures/emptyheart.png"));

    private int wins = -1;
    private final int maxWins;
    private final JLabel[] heartLabels;

    public HealthBar(int maxWins) {
        this.maxWins = Math.max(0, maxWins);
        int width = 10 + (HEART_SIZE * this.maxWins) + (HEART_GAP * Math.max(0, this.maxWins - 1));
        setPreferredSize(new Dimension(width, HEART_SIZE + 10));
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, HEART_GAP, 5));

        heartLabels = new JLabel[this.maxWins];
        for (int i = 0; i < this.maxWins; i++) {
            JLabel heartLabel = new JLabel();
            heartLabel.setOpaque(false);
            heartLabel.setPreferredSize(new Dimension(HEART_SIZE, HEART_SIZE));
            heartLabel.setHorizontalAlignment(JLabel.CENTER);
            heartLabels[i] = heartLabel;
            add(heartLabel);
        }

        this.wins = 0;
        for (JLabel heartLabel : heartLabels) {
            updateHeartIcon(heartLabel, false);
        }
    }

    public void setWins(int wins) {
        int clampedWins = Math.max(0, Math.min(wins, maxWins));
        if (this.wins == clampedWins) {
            return;
        }

        this.wins = clampedWins;
        int index = 0;
        for (JLabel heartLabel : heartLabels) {
            updateHeartIcon(heartLabel, index < this.wins);
            index++;
        }
        repaint();
    }

    private static ImageIcon createIcon(BufferedImage image) {
        if (image == null) {
            return null;
        }
        Image scaledImage = image.getScaledInstance(HEART_SIZE, HEART_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void updateHeartIcon(JLabel heartLabel, boolean filled) {
        ImageIcon icon = filled ? FILLED_HEART_ICON : EMPTY_HEART_ICON;

        if (icon == null) {
            heartLabel.setIcon(null);
            heartLabel.setText(filled ? "O" : "o");
            heartLabel.setForeground(filled ? new Color(220, 20, 20) : new Color(90, 90, 90));
        } else {
            heartLabel.setText("");
            heartLabel.setIcon(icon);
        }
    }
}


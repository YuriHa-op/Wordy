package client.ui.components;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class HealthBar extends JPanel {
    private int wins;
    private final int maxWins;

    public HealthBar(int maxWins) {
        this.maxWins = maxWins;
        setPreferredSize(new Dimension(140, 30));
        setOpaque(false);
    }

    public void setWins(int wins) {
        this.wins = Math.max(0, wins);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < maxWins; i++) {
            drawHeart(g2, 5 + i * 35, 5, i < wins);
        }
        g2.dispose();
    }

    private void drawHeart(Graphics2D g, int x, int y, boolean filled) {
        int[][] shape = {
                {0, 1, 1, 0, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 0, 0, 0}
        };

        g.setColor(filled ? new Color(220, 20, 20) : new Color(70, 70, 70));
        int pixel = 3;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    g.fillRect(x + col * pixel, y + row * pixel, pixel, pixel);
                }
            }
        }
    }
}


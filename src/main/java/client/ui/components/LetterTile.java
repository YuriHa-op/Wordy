package client.ui.components;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class LetterTile extends JPanel {
    private char letter = '?';
    private boolean selected;

    public LetterTile() {
        setPreferredSize(new Dimension(50, 50));
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setLetter(char value) {
        this.letter = Character.toUpperCase(value);
        repaint();
    }

    public char getLetter() {
        return letter;
    }

    public boolean isSelectedTile() {
        return selected;
    }

    public void setSelectedTile(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();

        Color base = selected ? new Color(66, 170, 66) : new Color(181, 140, 94);
        Color topLight = selected ? new Color(95, 210, 95) : new Color(220, 180, 130);
        Color lowDark = selected ? new Color(40, 120, 40) : new Color(130, 90, 50);

        g2.setColor(base);
        g2.fillRect(0, 0, w, h);
        g2.setColor(topLight);
        g2.fillRect(0, 0, w, 4);
        g2.fillRect(0, 0, 4, h);
        g2.setColor(lowDark);
        g2.fillRect(0, h - 4, w, 4);
        g2.fillRect(w - 4, 0, 4, h);
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, w - 1, h - 1);

        g2.setFont(MinecraftFonts.LARGE);
        String text = String.valueOf(letter);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(text)) / 2;
        int ty = (h + fm.getAscent()) / 2 - 3;
        g2.setColor(MinecraftColors.TEXT_SHADOW);
        g2.drawString(text, tx + 2, ty + 2);
        g2.setColor(MinecraftColors.TEXT_YELLOW);
        g2.drawString(text, tx, ty);
        g2.dispose();
    }
}


package client.ui.components;

import client.ui.UiColors;
import client.ui.UiFonts;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class StyledLabel extends JLabel {
    private Color shadowColor = UiColors.TEXT_SHADOW;

    public StyledLabel(String text) {
        super(text);
        setFont(UiFonts.REGULAR);
        setForeground(UiColors.TEXT_WHITE);
        setOpaque(false);
    }

    public StyledLabel(String text, Font font, Color color) {
        super(text);
        setFont(font);
        setForeground(color);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        String text = getText();
        if (text == null || text.isEmpty()) {
            super.paintComponent(g);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();

        int x = 0;
        if (getHorizontalAlignment() == CENTER) {
            x = (getWidth() - fm.stringWidth(text)) / 2;
        } else if (getHorizontalAlignment() == RIGHT) {
            x = getWidth() - fm.stringWidth(text);
        }

        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(shadowColor);
        g2.drawString(text, x + 2, y + 2);
        g2.setColor(getForeground());
        g2.drawString(text, x, y);
        g2.dispose();
    }
}


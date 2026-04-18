package client.ui.components;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinecraftButton extends JButton {
    private boolean hovered;
    private boolean pressed;

    public MinecraftButton(String text) {
        super(text);
        setFont(MinecraftFonts.REGULAR);
        setForeground(MinecraftColors.TEXT_WHITE);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(250, 42));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();

        Color base = hovered ? MinecraftColors.BUTTON_HOVER : MinecraftColors.BUTTON_GRAY;
        if (pressed) {
            base = base.darker();
        }

        g2.setColor(base);
        g2.fillRect(0, 0, w, h);

        if (!pressed) {
            g2.setColor(base.brighter());
            g2.fillRect(0, 0, w, 3);
            g2.fillRect(0, 0, 3, h);
            g2.setColor(base.darker());
            g2.fillRect(0, h - 3, w, 3);
            g2.fillRect(w - 3, 0, 3, h);
        } else {
            g2.setColor(base.darker());
            g2.fillRect(0, 0, w, 3);
            g2.fillRect(0, 0, 3, h);
        }

        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, w - 1, h - 1);

        FontMetrics fm = g2.getFontMetrics(getFont());
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h + fm.getAscent()) / 2 - 2;
        g2.setFont(getFont());
        g2.setColor(MinecraftColors.TEXT_SHADOW);
        g2.drawString(getText(), tx + 2, ty + 2);
        g2.setColor(hovered ? MinecraftColors.TEXT_YELLOW : MinecraftColors.TEXT_WHITE);
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }
}


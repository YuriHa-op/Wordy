package client.ui.components;

import client.ui.UiColors;
import client.ui.UiFonts;

import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class StyledPasswordField extends JPasswordField {
    public StyledPasswordField(int columns) {
        super(columns);
        setFont(UiFonts.REGULAR);
        setForeground(UiColors.TEXT_WHITE);
        setBackground(new Color(20, 20, 20));
        setCaretColor(UiColors.TEXT_WHITE);
        setSelectionColor(new Color(80, 80, 120));
        setSelectedTextColor(UiColors.TEXT_WHITE);
        setBorder(new EmptyBorder(8, 10, 8, 10));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(20, 20, 20));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g2.dispose();
        super.paintComponent(g);
    }
}


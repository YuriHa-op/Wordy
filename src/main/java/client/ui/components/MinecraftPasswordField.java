package client.ui.components;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;

import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class MinecraftPasswordField extends JPasswordField {
    public MinecraftPasswordField(int columns) {
        super(columns);
        setFont(MinecraftFonts.REGULAR);
        setForeground(MinecraftColors.TEXT_WHITE);
        setBackground(new Color(20, 20, 20));
        setCaretColor(MinecraftColors.TEXT_WHITE);
        setSelectionColor(new Color(80, 80, 120));
        setSelectedTextColor(MinecraftColors.TEXT_WHITE);
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


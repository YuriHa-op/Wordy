package client.ui.components;

import client.ui.UiColors;
import client.ui.UiFonts;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class InventorySlot extends JPanel {
    private final StyledLabel textLabel;

    public InventorySlot(String text) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(420, 34));

        textLabel = new StyledLabel(text, UiFonts.SMALL, UiColors.TEXT_WHITE);
        textLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10));
        add(textLabel, BorderLayout.CENTER);
    }

    public void setText(String text) {
        textLabel.setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();
        g2.setColor(new Color(90, 90, 90, 220));
        g2.fillRect(0, 0, w, h);
        g2.setColor(UiColors.SLOT_BG.brighter());
        g2.drawLine(0, 0, w - 1, 0);
        g2.drawLine(0, 0, 0, h - 1);
        g2.setColor(UiColors.SLOT_BG.darker());
        g2.drawLine(0, h - 1, w - 1, h - 1);
        g2.drawLine(w - 1, 0, w - 1, h - 1);
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, w - 1, h - 1);
        g2.dispose();
        super.paintComponent(g);
    }
}


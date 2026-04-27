package client.ui.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.util.TextureLoader;

public class InventorySlot extends JPanel {
    private static final Dimension SLOT_SIZE = new Dimension(420, 34);
    private static final ImageIcon SLOT_ICON = loadSlotIcon();

    private final JLabel backgroundLabel = new JLabel();
    private final StyledLabel textLabel;

    public InventorySlot(String text) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(SLOT_SIZE);

        textLabel = new StyledLabel(text, UiFonts.SMALL, UiColors.TEXT_WHITE);
        textLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.setOpaque(false);
        backgroundLabel.add(textLabel, BorderLayout.CENTER);

        if (SLOT_ICON != null) {
            backgroundLabel.setIcon(SLOT_ICON);
        } else {
            backgroundLabel.setOpaque(true);
            backgroundLabel.setBackground(new Color(90, 90, 90, 220));
            backgroundLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }

        add(backgroundLabel, BorderLayout.CENTER);
    }

    public void setText(String text) {
        textLabel.setText(text);
    }

    private static ImageIcon loadSlotIcon() {
        BufferedImage loaded = TextureLoader.load("/textures/button.png");
        if (loaded == null) {
            return null;
        }

        Rectangle bounds = TextureLoader.findOpaqueBounds(loaded);
        BufferedImage cropped = loaded.getSubimage(bounds.x, bounds.y, bounds.width, bounds.height);
        Image scaled = cropped.getScaledInstance(SLOT_SIZE.width, SLOT_SIZE.height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}


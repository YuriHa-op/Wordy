package client.ui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import client.ui.UiFonts;
import client.ui.util.TextureLoader;

public class StyledButton extends JButton {
    private static final Dimension DEFAULT_SIZE = new Dimension(220, 60);
    private static final BufferedImage BUTTON_TEXTURE = loadButtonTexture();

    public StyledButton(String text) {
        super(text);
        setFont(UiFonts.REGULAR.deriveFont(17f));
        setForeground(new Color(34, 21, 10));
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setIconTextGap(0);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(null);
        super.setPreferredSize(DEFAULT_SIZE);
        super.setMinimumSize(DEFAULT_SIZE);
        setRolloverEnabled(false);
        applyIconForSize(DEFAULT_SIZE);
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        super.setPreferredSize(preferredSize);
        applyIconForSize(preferredSize);
    }

    private void applyIconForSize(Dimension size) {
        if (BUTTON_TEXTURE == null || size == null || size.width <= 0 || size.height <= 0) {
            return;
        }

        Image scaled = BUTTON_TEXTURE.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaled);
        setIcon(icon);
        setPressedIcon(icon);
        setRolloverIcon(icon);
        setDisabledIcon(icon);
    }

    private static BufferedImage loadButtonTexture() {
        BufferedImage loaded = TextureLoader.load("/textures/button.png");
        if (loaded == null) {
            return null;
        }

        Rectangle bounds = TextureLoader.findOpaqueBounds(loaded);
        return loaded.getSubimage(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}


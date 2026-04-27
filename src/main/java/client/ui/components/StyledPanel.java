package client.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import client.ui.util.TextureLoader;

public class StyledPanel extends JPanel {
    private final BufferedImage backgroundImage;
    private final int overlayAlpha;

    public StyledPanel(String texturePath) {
        this(texturePath, 90);
    }

    public StyledPanel(String texturePath, int overlayAlpha) {
        this.backgroundImage = TextureLoader.load(texturePath);
        this.overlayAlpha = Math.max(0, Math.min(255, overlayAlpha));
        setOpaque(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TextureLoader.drawCover(g, backgroundImage, 0, 0, getWidth(), getHeight());
        if (overlayAlpha > 0) {
            g.setColor(new Color(0, 0, 0, overlayAlpha));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}


package client.ui.util;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TextureLoader {
    private static final Map<String, BufferedImage> CACHE = new HashMap<>();

    private TextureLoader() {
    }

    public static BufferedImage load(String path) {
        return CACHE.computeIfAbsent(path, TextureLoader::read);
    }

    private static BufferedImage read(String path) {
        try {
            return ImageIO.read(TextureLoader.class.getResource(path));
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    public static void tile(Graphics g, BufferedImage texture, int x, int y, int width, int height) {
        if (texture == null) {
            return;
        }

        int tileWidth = texture.getWidth();
        int tileHeight = texture.getHeight();
        for (int px = 0; px < width; px += tileWidth) {
            for (int py = 0; py < height; py += tileHeight) {
                g.drawImage(texture, x + px, y + py, null);
            }
        }
    }
}


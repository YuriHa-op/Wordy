package client.ui.util;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

public final class TextureLoader {
    private static final Map<String, BufferedImage> CACHE = new ConcurrentHashMap<>();

    private TextureLoader() {
    }

    public static BufferedImage load(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        String normalized = path.startsWith("/") ? path : "/" + path;
        return CACHE.computeIfAbsent(normalized, TextureLoader::read);
    }

    private static BufferedImage read(String path) {
        try {
            java.net.URL resource = TextureLoader.class.getResource(path);
            if (resource == null) {
                return null;
            }
            return ImageIO.read(resource);
        } catch (IOException | IllegalArgumentException e) {
            return null;
        }
    }

    public static void drawStretched(Graphics g, BufferedImage texture, int x, int y, int width, int height) {
        if (texture == null || width <= 0 || height <= 0) {
            return;
        }
        g.drawImage(texture, x, y, width, height, null);
    }

    public static void drawCover(Graphics g, BufferedImage texture, int x, int y, int width, int height) {
        if (texture == null || width <= 0 || height <= 0) {
            return;
        }

        double scale = Math.max((double) width / texture.getWidth(), (double) height / texture.getHeight());
        int drawWidth = (int) Math.ceil(texture.getWidth() * scale);
        int drawHeight = (int) Math.ceil(texture.getHeight() * scale);
        int drawX = x + (width - drawWidth) / 2;
        int drawY = y + (height - drawHeight) / 2;

        g.drawImage(texture, drawX, drawY, drawWidth, drawHeight, null);
    }

    public static Rectangle findOpaqueBounds(BufferedImage texture) {
        if (texture == null) {
            return new Rectangle(0, 0, 1, 1);
        }

        int minX = texture.getWidth();
        int minY = texture.getHeight();
        int maxX = -1;
        int maxY = -1;

        for (int y = 0; y < texture.getHeight(); y++) {
            for (int x = 0; x < texture.getWidth(); x++) {
                int alpha = (texture.getRGB(x, y) >>> 24) & 0xFF;
                if (alpha == 0) {
                    continue;
                }

                if (x < minX) {
                    minX = x;
                }
                if (y < minY) {
                    minY = y;
                }
                if (x > maxX) {
                    maxX = x;
                }
                if (y > maxY) {
                    maxY = y;
                }
            }
        }

        if (maxX < minX || maxY < minY) {
            return new Rectangle(0, 0, texture.getWidth(), texture.getHeight());
        }
        return new Rectangle(minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }
}


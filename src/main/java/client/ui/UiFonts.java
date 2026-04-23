package client.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public final class UiFonts {
    public static final Font TITLE;
    public static final Font LARGE;
    public static final Font REGULAR;
    public static final Font SMALL;

    private UiFonts() {
    }

    static {
        Font title;
        Font large;
        Font regular;
        Font small;

        try (InputStream is = UiFonts.class.getResourceAsStream("/fonts/Minecraftia-Regular.ttf")) {
            if (is == null) {
                throw new IOException("Missing /fonts/Minecraftia-Regular.ttf");
            }
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            title = base.deriveFont(Font.BOLD, 36f);
            large = base.deriveFont(Font.PLAIN, 24f);
            regular = base.deriveFont(Font.PLAIN, 16f);
            small = base.deriveFont(Font.PLAIN, 12f);
        } catch (IOException | FontFormatException e) {
            title = new Font("Monospaced", Font.BOLD, 36);
            large = new Font("Monospaced", Font.BOLD, 24);
            regular = new Font("Monospaced", Font.BOLD, 16);
            small = new Font("Monospaced", Font.BOLD, 12);
        }

        TITLE = title;
        LARGE = large;
        REGULAR = regular;
        SMALL = small;
    }
}


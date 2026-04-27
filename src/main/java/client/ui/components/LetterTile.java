package client.ui.components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.util.TextureLoader;

public class LetterTile extends JLabel {
    private static final int TILE_SIZE = 80;
    private static final Map<Character, ImageIcon> NORMAL_ICONS = loadIcons(false);
    private static final Map<Character, ImageIcon> SELECTED_ICONS = loadIcons(true);
    private static final ImageIcon PLACEHOLDER_ICON = loadPlaceholderIcon();

    private char letter = '?';
    private boolean selected;

    public LetterTile() {
        Dimension tileSize = new Dimension(TILE_SIZE, TILE_SIZE);
        setPreferredSize(tileSize);
        setMinimumSize(tileSize);
        setMaximumSize(tileSize);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshIcon();
    }

    public void setLetter(char value) {
        this.letter = Character.toUpperCase(value);
        refreshIcon();
    }

    public char getLetter() {
        return letter;
    }

    public boolean isSelectedTile() {
        return selected;
    }

    public void setSelectedTile(boolean selected) {
        this.selected = selected;
        refreshIcon();
    }

    private void refreshIcon() {
        if (letter < 'A' || letter > 'Z') {
            if (PLACEHOLDER_ICON != null) {
                setIcon(PLACEHOLDER_ICON);
                setText("");
            } else {
                setIcon(null);
                setText("?");
                setFont(UiFonts.LARGE);
                setForeground(UiColors.TEXT_YELLOW);
            }
            return;
        }

        ImageIcon icon = selected ? SELECTED_ICONS.get(letter) : NORMAL_ICONS.get(letter);
        if (icon != null) {
            setIcon(icon);
            setText("");
            return;
        }

        // Fallback if an image is missing.
        setIcon(null);
        setText(String.valueOf(letter));
        setFont(UiFonts.LARGE);
        setForeground(selected ? UiColors.TEXT_GREEN : UiColors.TEXT_YELLOW);
    }

    private static ImageIcon loadPlaceholderIcon() {
        BufferedImage image = TextureLoader.load("/textures/what.png");
        if (image == null) {
            return null;
        }

        Image scaled = image.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private static Map<Character, ImageIcon> loadIcons(boolean selected) {
        Map<Character, ImageIcon> icons = new HashMap<>();
        for (char value = 'A'; value <= 'Z'; value++) {
            String lower = String.valueOf(Character.toLowerCase(value));
            String path = "/textures/letter_tiles/" + lower + (selected ? "_selected" : "") + ".png";
            BufferedImage image = TextureLoader.load(path);
            if (image == null) {
                continue;
            }

            Image scaled = image.getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
            icons.put(value, new ImageIcon(scaled));
        }
        return Collections.unmodifiableMap(icons);
    }
}


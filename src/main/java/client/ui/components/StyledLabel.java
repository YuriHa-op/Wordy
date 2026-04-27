package client.ui.components;

import client.ui.UiColors;
import client.ui.UiFonts;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class StyledLabel extends JLabel {
    public StyledLabel(String text) {
        super(text);
        setFont(UiFonts.REGULAR);
        setForeground(UiColors.TEXT_WHITE);
        setOpaque(false);
    }

    public StyledLabel(String text, Font font, Color color) {
        super(text);
        setFont(font);
        setForeground(color);
        setOpaque(false);
    }
}


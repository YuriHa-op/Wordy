package client.ui.components;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import client.ui.UiColors;
import client.ui.UiFonts;

public class StyledTextField extends JTextField {
    public StyledTextField(int columns) {
        super(columns);
        setFont(UiFonts.REGULAR);
        setForeground(UiColors.TEXT_WHITE);
        setCaretColor(UiColors.TEXT_WHITE);
        setSelectionColor(new Color(80, 80, 120, 180));
        setSelectedTextColor(UiColors.TEXT_WHITE);
        setOpaque(true);
        setBackground(new Color(24, 24, 24));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(175, 175, 175), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }
}


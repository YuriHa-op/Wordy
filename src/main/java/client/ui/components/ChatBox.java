package client.ui.components;

import client.ui.UiColors;
import client.ui.UiFonts;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;

public class ChatBox extends JPanel {
    private final JTextArea textArea = new JTextArea();

    public ChatBox() {
        setLayout(new BorderLayout());
        setOpaque(false);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(UiFonts.SMALL);
        textArea.setForeground(UiColors.TEXT_WHITE);
        textArea.setBackground(new Color(20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));
        scrollPane.setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public void appendLine(String line) {
        if (textArea.getText().isBlank()) {
            textArea.setText(line);
        } else {
            textArea.append("\n" + line);
        }
    }
}


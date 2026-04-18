package client.admin.view;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;
import client.ui.components.MinecraftButton;
import client.ui.components.MinecraftLabel;
import client.ui.components.MinecraftTextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

public class PlayerManagementView extends JPanel {

    private final JTextField idField = new MinecraftTextField(6);
    private final JTextField usernameField = new MinecraftTextField(10);
    private final JTextField passwordField = new MinecraftTextField(10);
    private final JTextField roleField = new MinecraftTextField(8);
    private final JTextField winsField = new MinecraftTextField(6);
    private final JTextField searchField = new MinecraftTextField(10);

    private final JButton addButton = new MinecraftButton("CREATE");
    private final JButton readButton = new MinecraftButton("READ");
    private final JButton updateButton = new MinecraftButton("EDIT");
    private final JButton deleteButton = new MinecraftButton("DELETE");
    private final JButton searchButton = new MinecraftButton("SEARCH");

    private final JTextArea outputArea = new JTextArea(14, 45);

    public PlayerManagementView() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(false);

        JPanel form = new JPanel(new GridLayout(4, 4, 6, 6));
        form.setOpaque(true);
        form.setBackground(new Color(0, 0, 0, 100));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        form.add(new MinecraftLabel("User ID", MinecraftFonts.SMALL, MinecraftColors.TEXT_WHITE));
        form.add(idField);
        form.add(new MinecraftLabel("Username", MinecraftFonts.SMALL, MinecraftColors.TEXT_WHITE));
        form.add(usernameField);
        form.add(new MinecraftLabel("Password", MinecraftFonts.SMALL, MinecraftColors.TEXT_WHITE));
        form.add(passwordField);
        form.add(new MinecraftLabel("Role", MinecraftFonts.SMALL, MinecraftColors.TEXT_WHITE));
        form.add(roleField);
        form.add(new MinecraftLabel("Wins", MinecraftFonts.SMALL, MinecraftColors.TEXT_WHITE));
        form.add(winsField);
        form.add(new MinecraftLabel("Search Keyword", MinecraftFonts.SMALL, MinecraftColors.TEXT_WHITE));
        form.add(searchField);
        form.add(searchButton);
        form.add(new JLabel(""));

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(addButton);
        buttons.add(readButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);

        outputArea.setEditable(false);
        outputArea.setFont(MinecraftFonts.SMALL);
        outputArea.setForeground(MinecraftColors.TEXT_WHITE);
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        outputArea.setCaretColor(MinecraftColors.TEXT_WHITE);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.getViewport().setOpaque(true);
        outputScroll.getViewport().setBackground(new Color(20, 20, 20));
        outputScroll.setOpaque(true);
        outputScroll.setBackground(new Color(20, 20, 20));

        add(form, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(outputScroll, BorderLayout.SOUTH);
    }

    public JTextField getIdField() { return idField; }
    public JTextField getUsernameField() { return usernameField; }
    public JTextField getPasswordField() { return passwordField; }
    public JTextField getRoleField() { return roleField; }
    public JTextField getWinsField() { return winsField; }
    public JTextField getSearchField() { return searchField; }
    public JButton getAddButton() { return addButton; }
    public JButton getReadButton() { return readButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getSearchButton() { return searchButton; }
    public JTextArea getOutputArea() { return outputArea; }
}



package client.admin.view;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.components.StyledButton;
import client.ui.components.StyledLabel;
import client.ui.components.StyledTextField;

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

    private final JTextField idField = new StyledTextField(6);
    private final JTextField usernameField = new StyledTextField(10);
    private final JTextField passwordField = new StyledTextField(10);
    private final JTextField roleField = new StyledTextField(8);
    private final JTextField winsField = new StyledTextField(6);
    private final JTextField searchField = new StyledTextField(10);

    private final JButton addButton = new StyledButton("CREATE");
    private final JButton readButton = new StyledButton("READ");
    private final JButton updateButton = new StyledButton("EDIT");
    private final JButton deleteButton = new StyledButton("DELETE");
    private final JButton searchButton = new StyledButton("SEARCH");

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

        form.add(new StyledLabel("User ID", UiFonts.SMALL, UiColors.TEXT_WHITE));
        form.add(idField);
        form.add(new StyledLabel("Username", UiFonts.SMALL, UiColors.TEXT_WHITE));
        form.add(usernameField);
        form.add(new StyledLabel("Password", UiFonts.SMALL, UiColors.TEXT_WHITE));
        form.add(passwordField);
        form.add(new StyledLabel("Role", UiFonts.SMALL, UiColors.TEXT_WHITE));
        form.add(roleField);
        form.add(new StyledLabel("Wins", UiFonts.SMALL, UiColors.TEXT_WHITE));
        form.add(winsField);
        form.add(new StyledLabel("Search Keyword", UiFonts.SMALL, UiColors.TEXT_WHITE));
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
        outputArea.setFont(UiFonts.SMALL);
        outputArea.setForeground(UiColors.TEXT_WHITE);
        outputArea.setBackground(new Color(20, 20, 20));
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        outputArea.setCaretColor(UiColors.TEXT_WHITE);

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



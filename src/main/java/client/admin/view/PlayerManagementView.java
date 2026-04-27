package client.admin.view;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.components.StyledButton;
import client.ui.components.StyledLabel;
import client.ui.components.StyledTextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

    private final JTextArea outputArea = new JTextArea(12, 45);
    private static final Dimension CRUD_BUTTON_SIZE = new Dimension(150, 46);

    public PlayerManagementView() {
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);

        JPanel form = new JPanel(new GridLayout(3, 4, 6, 6));
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

        addButton.setPreferredSize(CRUD_BUTTON_SIZE);
        readButton.setPreferredSize(CRUD_BUTTON_SIZE);
        updateButton.setPreferredSize(CRUD_BUTTON_SIZE);
        deleteButton.setPreferredSize(CRUD_BUTTON_SIZE);
        searchButton.setPreferredSize(CRUD_BUTTON_SIZE);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setOpaque(false);
        buttons.add(addButton);
        buttons.add(readButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(searchButton);

        JPanel topSection = new JPanel(new BorderLayout(0, 8));
        topSection.setOpaque(false);
        topSection.add(form, BorderLayout.CENTER);
        topSection.add(buttons, BorderLayout.SOUTH);

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
        outputScroll.setPreferredSize(new Dimension(0, 320));

        add(topSection, BorderLayout.NORTH);
        add(outputScroll, BorderLayout.CENTER);
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



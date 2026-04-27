package client.player.view;

import client.ui.UiColors;
import client.ui.UiFonts;
import client.ui.components.ChatBox;
import client.ui.components.HealthBar;
import client.ui.components.LetterTile;
import client.ui.components.StyledButton;
import client.ui.components.StyledLabel;
import client.ui.components.StyledPanel;
import client.ui.components.StyledTextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GameView extends JFrame {

    private final JLabel statusLabel = new StyledLabel("Status: WAITING", UiFonts.SMALL, UiColors.TEXT_AQUA);
    private final JLabel roundLabel = new StyledLabel("Round: 0", UiFonts.SMALL, UiColors.TEXT_WHITE);
    private final JLabel timerLabel = new StyledLabel("Time: 0", UiFonts.REGULAR, UiColors.TEXT_WHITE);
    private final JLabel playerNameLabel = new JLabel("Player: -");
    private final JTextArea lettersArea = new JTextArea(2, 30);
    private final JEditorPane scoreArea = new JEditorPane("text/html", "");
    private final JTextField wordField = new StyledTextField(24);
    private final JButton submitButton = new StyledButton("SUBMIT");
    private final JButton backspaceButton = new StyledButton("BACKSPACE");
    private final JButton backButton = new StyledButton("BACK HOME");
    private final JLabel submitMessageLabel = new StyledLabel(" ", UiFonts.SMALL, UiColors.TEXT_GREEN);
    private final ChatBox chatBox = new ChatBox();
    private final HealthBar healthBar = new HealthBar(3);
    private final List<LetterTile> letterTiles = new ArrayList<>();
    private final List<LetterTile> selectedTileOrder = new ArrayList<>();
    private String lastLettersSignature = "";

    public GameView() {
        this(true);
    }

    public GameView(boolean enterSubmitEnabled) {
        setTitle("Wordy - Game");
        setSize(1160, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        lettersArea.setEditable(false);
        lettersArea.setVisible(false);
        scoreArea.setEditable(false);
        scoreArea.setFont(UiFonts.SMALL);
        scoreArea.setForeground(UiColors.TEXT_WHITE);
        scoreArea.setBackground(new Color(20, 20, 20));
        scoreArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);

        StyledPanel root = new StyledPanel("/textures/gamesession_background.png", 50);
        root.setLayout(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel top = new JPanel(new GridLayout(1, 4, 8, 8));
        top.setOpaque(false);
        top.add(statusLabel);
        top.add(roundLabel);
        JPanel timerAndName = new JPanel(new BorderLayout(8, 0));
        timerAndName.setOpaque(false);
        timerAndName.add(timerLabel, BorderLayout.WEST);
        timerAndName.add(playerNameLabel, BorderLayout.CENTER);
        top.add(timerAndName);

        JPanel heartInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 2));
        heartInfo.setOpaque(false);
        playerNameLabel.setFont(UiFonts.SMALL);
        playerNameLabel.setForeground(UiColors.TEXT_GREEN);
        heartInfo.add(healthBar);
        top.add(heartInfo);
        root.add(top, BorderLayout.NORTH);

        JPanel letterGrid = new JPanel(new GridLayout(2, 10, 2, 2));
        letterGrid.setOpaque(false);
        letterGrid.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        for (int i = 0; i < 20; i++) {
            LetterTile tile = new LetterTile();
            letterTiles.add(tile);
            letterGrid.add(tile);

            tile.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onLetterTileClicked(tile);
                }
            });
        }

        JPanel input = new JPanel(new BorderLayout(8, 8));
        input.setOpaque(false);
        input.setPreferredSize(new Dimension(0, 86));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 16));
        left.setOpaque(false);
        wordField.setPreferredSize(new Dimension(420, 46));
        left.add(new StyledLabel("Your word:", UiFonts.REGULAR, UiColors.TEXT_WHITE));
        left.add(wordField);
        input.add(left, BorderLayout.WEST);

        JPanel inputButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 16));
        inputButtons.setOpaque(false);
        submitButton.setPreferredSize(new Dimension(170, 46));
        backspaceButton.setPreferredSize(new Dimension(180, 46));
        backButton.setPreferredSize(new Dimension(190, 46));
        inputButtons.add(submitButton);
        inputButtons.add(backspaceButton);
        inputButtons.add(backButton);
        input.add(inputButtons, BorderLayout.EAST);

        backspaceButton.addActionListener(e -> removeLastCharacter());
        wordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deselectLastTileSelection();
                }
            }
        });

        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setOpaque(false);
        center.add(letterGrid, BorderLayout.CENTER);

        JPanel bottomInputSection = new JPanel(new BorderLayout(6, 6));
        bottomInputSection.setOpaque(false);
        bottomInputSection.add(input, BorderLayout.CENTER);
        bottomInputSection.add(submitMessageLabel, BorderLayout.SOUTH);

        center.add(bottomInputSection, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);

        JScrollPane scoreScroll = new JScrollPane(scoreArea);
        scoreScroll.setBorder(BorderFactory.createTitledBorder("Scoreboard"));
        scoreScroll.getViewport().setOpaque(true);
        scoreScroll.getViewport().setBackground(new Color(20, 20, 20));

        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setOpaque(false);
        logPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
        logPanel.add(chatBox, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scoreScroll, logPanel);
        splitPane.setResizeWeight(0.42);
        splitPane.setOpaque(false);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setPreferredSize(new Dimension(0, 260));
        root.add(splitPane, BorderLayout.SOUTH);

        setContentPane(root);
        if (enterSubmitEnabled) {
            getRootPane().setDefaultButton(submitButton);
        }
    }

    public void setLetters(List<String> letters) {
        String joined = String.join(" ", letters);
        lettersArea.setText("Letters: " + joined);

        if (!joined.equals(lastLettersSignature)) {
            clearTileSelections();
            lastLettersSignature = joined;
        }

        for (int i = 0; i < letterTiles.size(); i++) {
            char value = '?';
            if (i < letters.size() && !letters.get(i).isBlank()) {
                value = letters.get(i).charAt(0);
            }
            letterTiles.get(i).setLetter(value);
        }
    }

    public void appendLog(String line) {
        chatBox.appendLine(line);
    }

    public void setRoundWins(int wins) {
        healthBar.setWins(wins);
    }

    public void setPlayerName(String username) {
        if (username == null || username.isBlank()) {
            playerNameLabel.setText("Player: -");
        } else {
            playerNameLabel.setText("Player: " + username);
        }
    }

    private void onLetterTileClicked(LetterTile tile) {
        if (!wordField.isEnabled()) {
            return;
        }
        if (tile.isSelectedTile()) {
            return;
        }

        char letter = tile.getLetter();
        if (letter == '?') {
            return;
        }

        tile.setSelectedTile(true);
        selectedTileOrder.add(tile);

        int caret = wordField.getCaretPosition();
        String text = wordField.getText();
        String updated = text.substring(0, caret) + letter + text.substring(caret);
        wordField.setText(updated);
        wordField.setCaretPosition(caret + 1);
        wordField.requestFocusInWindow();
    }

    private void removeLastCharacter() {
        if (!wordField.isEnabled()) {
            return;
        }
        String text = wordField.getText();
        if (text == null || text.isEmpty()) {
            return;
        }

        int caret = wordField.getCaretPosition();
        if (caret <= 0) {
            caret = text.length();
        }
        String updated = text.substring(0, caret - 1) + text.substring(caret);
        wordField.setText(updated);
        wordField.setCaretPosition(Math.max(0, caret - 1));
        deselectLastTileSelection();
        wordField.requestFocusInWindow();
    }

    private void clearTileSelections() {
        for (LetterTile tile : letterTiles) {
            tile.setSelectedTile(false);
        }
        selectedTileOrder.clear();
    }

    private void deselectLastTileSelection() {
        if (selectedTileOrder.isEmpty()) {
            return;
        }
        LetterTile tile = selectedTileOrder.remove(selectedTileOrder.size() - 1);
        tile.setSelectedTile(false);
    }


    public void setScoreboardHtml(String html) {
        scoreArea.setText(html);
        scoreArea.setCaretPosition(0);
    }

    

    public JLabel getStatusLabel() { return statusLabel; }
    public JLabel getRoundLabel() { return roundLabel; }
    public JLabel getTimerLabel() { return timerLabel; }
    public JTextArea getLettersArea() { return lettersArea; }
    public JEditorPane getScoreArea() { return scoreArea; }
    public JTextField getWordField() { return wordField; }
    public JButton getSubmitButton() { return submitButton; }
    public JButton getBackspaceButton() { return backspaceButton; }
    public JButton getBackButton() { return backButton; }
    public JLabel getSubmitMessageLabel() { return submitMessageLabel; }
}


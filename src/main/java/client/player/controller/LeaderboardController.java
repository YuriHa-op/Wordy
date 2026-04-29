package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.LeaderboardView;
import client.ui.components.PopNotification;
import client.ui.util.StyledDialog;
import com.wordy.grpc.GetLeaderboardResponse;
import com.wordy.grpc.LeaderboardPlayer;
import com.wordy.grpc.LeaderboardWord;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardController {

    private final LeaderboardView view;
    private final PlayerGrpcClient client;
    private Timer sessionChecker;

    public LeaderboardController(PlayerGrpcClient client) {
        this.client = client;
        this.view = new LeaderboardView();
        bind();
        loadBoard();
        startSessionChecker();
    }

    private void startSessionChecker() {
        sessionChecker = new Timer(2000, e -> {
            if (!client.checkSession()) {
                sessionChecker.stop();
                PopNotification.showSessionEnded(view, "<html><center>You have been disconnected because someone else<br>logged in with your account.</center></html>");
                client.shutdown();
                view.dispose();
                new LoginController().show();
            }
        });
        sessionChecker.start();
    }

    private void cleanup() {
        if (sessionChecker != null) {
            sessionChecker.stop();
        }
        view.dispose();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bind() {
        view.getBackButton().addActionListener(e -> {
            cleanup();
            new HomeController(client).show();
        });
    }

    private void loadBoard() {
        GetLeaderboardResponse response = client.getLeaderboard();
        List<String> topPlayers = new ArrayList<>();
        List<String> longestWords = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        sb.append("Top Winners\n");
        int rank = 1;
        for (LeaderboardPlayer player : response.getTopPlayersList()) {
            String row = rank + ". " + player.getUsername() + " - " + player.getTotalWins() + " wins";
            sb.append(player.getUsername()).append(" - ").append(player.getTotalWins()).append("\n");
            topPlayers.add(row);
            rank++;
        }

        sb.append("\nTop Longest Words\n");
        rank = 1;
        for (LeaderboardWord word : response.getLongestWordsList()) {
            String row = rank + ". \"" + word.getWord() + "\" (" + word.getWordLength() + ") - " + word.getUsername();
            sb.append(word.getUsername()).append(" - ").append(word.getWord())
                    .append(" (").append(word.getWordLength()).append(")\n");
            longestWords.add(row);
            rank++;
        }

        view.render(topPlayers, longestWords);
        view.getBoardArea().setText(sb.toString());
    }
}


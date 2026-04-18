package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.LeaderboardView;
import com.wordy.grpc.GetLeaderboardResponse;
import com.wordy.grpc.LeaderboardPlayer;
import com.wordy.grpc.LeaderboardWord;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardController {

    private final LeaderboardView view;
    private final PlayerGrpcClient client;

    public LeaderboardController(PlayerGrpcClient client) {
        this.client = client;
        this.view = new LeaderboardView();
        bind();
        loadBoard();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bind() {
        view.getBackButton().addActionListener(e -> {
            view.dispose();
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


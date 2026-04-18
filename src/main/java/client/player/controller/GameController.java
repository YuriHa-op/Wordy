package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.GameView;
import com.wordy.grpc.GameStateResponse;
import com.wordy.grpc.JoinGameResponse;
import com.wordy.grpc.PlayerScore;
import com.wordy.grpc.SubmitWordResponse;

import javax.swing.Timer;
import java.awt.Color;

public class GameController {

    private final GameView view;
    private final PlayerGrpcClient client;
    private Timer pollTimer;
    private int gameId = -1;
    private int currentRound = 0;
    private int previousRoundNumber = -1;
    private String previousStatus = "";
    private String previousRoundWinner = "";
    private boolean gameOverShown;
    private boolean noMatchPopupShown;

    public GameController(PlayerGrpcClient client) {
        this.client = client;
        this.view = new GameView();
        bind();
        startJoinAndPolling();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bind() {
        view.getSubmitButton().addActionListener(e -> onSubmitWord());
        view.getBackButton().addActionListener(e -> backHome());
        view.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                stopPolling();
            }
        });
    }

    private void startJoinAndPolling() {
        JoinGameResponse join = client.joinGame();
        if (join.getGameId() > 0) {
            gameId = join.getGameId();
        }
        view.getStatusLabel().setText("Status: " + join.getStatus());
        view.appendLog(">> " + join.getMessage());

        pollTimer = new Timer(1000, e -> pollState());
        pollTimer.start();
    }

    private void pollState() {
        GameStateResponse state = client.pollGameState(gameId);
        if (!state.getSuccess()) {
            if ("NO_MATCH".equals(state.getStatus()) && !noMatchPopupShown) {
                noMatchPopupShown = true;
                view.showNoMatchFoundPopup();
                backHome();
                return;
            }
            view.getStatusLabel().setText("Status: " + state.getStatus());
            return;
        }

        if (gameId <= 0 && state.getStatus().equals("ROUND_ACTIVE")) {
            // Game became active after waiting period.
            JoinGameResponse refreshJoin = client.joinGame();
            if (refreshJoin.getGameId() > 0) {
                gameId = refreshJoin.getGameId();
            }
        }

        currentRound = state.getRoundNumber();
        if ("ROUND_ACTIVE".equals(state.getStatus()) && currentRound != previousRoundNumber) {
            view.getSubmitMessageLabel().setText(" ");
            previousRoundNumber = currentRound;
        }

        view.getStatusLabel().setText("Status: " + state.getStatus());
        view.getRoundLabel().setText("Round: " + state.getRoundNumber());
        int seconds = Math.max(0, state.getTimeRemaining());
        if ("WAITING".equals(state.getStatus())) {
            view.getTimerLabel().setText("Matchmaking: " + seconds + "s");
        } else {
            view.getTimerLabel().setText("Time: " + seconds + "s");
        }
        view.setLetters(state.getLettersList());

        String sessionUsername = client.getCurrentUsername();
        String displayName = sessionUsername;
        if (displayName == null || displayName.isBlank()) {
            displayName = state.getPlayersList().stream()
                    .findFirst()
                    .map(PlayerScore::getUsername)
                    .orElse("");
        }
        view.setPlayerName(displayName);

        boolean activeRound = "ROUND_ACTIVE".equals(state.getStatus());
        view.getWordField().setEnabled(activeRound);
        view.getSubmitButton().setEnabled(activeRound);
        view.getBackspaceButton().setEnabled(activeRound);
        if (activeRound) {
            view.getSubmitMessageLabel().setForeground(new Color(85, 255, 85));
        } else {
            view.getSubmitMessageLabel().setForeground(new Color(255, 170, 0));
            if ("WAITING".equals(state.getStatus()) && view.getSubmitMessageLabel().getText().isBlank()) {
                view.getSubmitMessageLabel().setText("Waiting for players...");
            }
        }

        view.setScoreboardHtml(buildScoreboardHtml(state));

        state.getPlayersList().stream()
                .filter(p -> p.getUsername().equalsIgnoreCase(sessionUsername))
                .findFirst()
                .ifPresent(p -> view.setRoundWins(calculateRemainingHearts(state, p)));

        if (!state.getStatus().equals(previousStatus)) {
            previousStatus = state.getStatus();
            view.appendLog(">> State changed: " + state.getStatus());
        }

        if (!state.getRoundWinner().isBlank() && !state.getRoundWinner().equals(previousRoundWinner)) {
            previousRoundWinner = state.getRoundWinner();
            view.appendLog(">> Round winner: " + state.getRoundWinner());
        }

        if ("WAITING".equals(state.getStatus()) && gameId <= 0 && seconds <= 0 && !noMatchPopupShown) {
            noMatchPopupShown = true;
            view.showNoMatchFoundPopup();
            backHome();
            return;
        }

        if ("GAME_OVER".equals(state.getStatus())) {
            view.appendLog(">> Game ended. Winner: " + state.getGameWinner());
            if (!gameOverShown) {
                gameOverShown = true;
                view.showGameOverPopup(client.getCurrentUsername(), state.getGameWinner());
                backHome();
                return;
            }
            stopPolling();
        }
    }

    private String buildScoreboardHtml(GameStateResponse state) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='background:#141414;color:#ffffff;font-family:monospace;font-size:14px;'>");

        for (PlayerScore score : state.getPlayersList()) {
            String color = score.getSubmittedThisRound() ? "#55ff55" : "#ffffff";
            html.append("<div style='color:").append(color).append(";'>")
                    .append(escapeHtml(score.getUsername()))
                    .append(" | wins=")
                    .append(score.getRoundWins())
                    .append(" | submitted=")
                    .append(score.getSubmittedThisRound())
                    .append("</div>");
        }

        html.append("<br/>")
                .append("<div style='color:#ffaa00;'>Round winner: ")
                .append(escapeHtml(state.getRoundWinner()))
                .append("</div>")
                .append("<div style='color:#ff5555;'>Game winner: ")
                .append(escapeHtml(state.getGameWinner()))
                .append("</div>")
                .append("</body></html>");
        return html.toString();
    }

    private String escapeHtml(String value) {
        if (value == null || value.isBlank()) {
            return "-";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private void onSubmitWord() {
        if (gameId <= 0) {
            view.getSubmitMessageLabel().setText("Still waiting for game to start");
            return;
        }
        String word = view.getWordField().getText().trim();
        SubmitWordResponse response = client.submitWord(gameId, word, currentRound);
        if (response.getSuccess()) {
            view.getSubmitMessageLabel().setText("Word submitted");
        } else {
            view.getSubmitMessageLabel().setText(response.getMessage());
        }
        view.appendLog(">> " + word + " -> " + response.getMessage());
        if (response.getSuccess()) {
            view.getWordField().setText("");
        }
    }

    private int calculateRemainingHearts(GameStateResponse state, PlayerScore score) {
        // Count only rounds that had an actual winner.
        int decidedRounds = state.getPlayersList().stream()
                .mapToInt(PlayerScore::getRoundWins)
                .sum();

        int losses = Math.max(0, decidedRounds - score.getRoundWins());
        int remaining = 3 - losses;
        return Math.max(0, Math.min(3, remaining));
    }

    private void backHome() {
        stopPolling();
        view.dispose();
        new HomeController(client).show();
    }

    private void stopPolling() {
        if (pollTimer != null) {
            pollTimer.stop();
            pollTimer = null;
        }
    }
}


package server.handler.core;

import server.handler.model.PlayerState;
import server.handler.model.Round;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameSession {

    public static final String WAITING = "WAITING";
    public static final String ROUND_ACTIVE = "ROUND_ACTIVE";
    public static final String ROUND_OVER = "ROUND_OVER";
    public static final String GAME_OVER = "GAME_OVER";

    private final int gameId;
    private final Map<String, PlayerState> players = new LinkedHashMap<>();
    private final Map<String, String> lastRoundWords = new LinkedHashMap<>();

    private Round currentRound;
    private String state = WAITING;
    private String roundWinner;
    private String gameWinner;

    public GameSession(int gameId, List<String> usernames) {
        this.gameId = gameId;
        for (String username : usernames) {
            players.put(username, new PlayerState(username));
            lastRoundWords.put(username, null);
        }
    }

    public synchronized int getGameId() {
        return gameId;
    }

    public synchronized String getState() {
        return state;
    }

    public synchronized Round getCurrentRound() {
        return currentRound;
    }

    public synchronized Collection<PlayerState> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public synchronized PlayerState getPlayer(String username) {
        return players.get(username);
    }

    public synchronized String getRoundWinner() {
        return roundWinner;
    }

    public synchronized String getGameWinner() {
        return gameWinner;
    }

    public synchronized void startRound(List<String> letters, int durationSeconds, int roundNumber) {
        if (this.currentRound != null) {
            for (PlayerState playerState : players.values()) {
                lastRoundWords.put(playerState.getUsername(), playerState.getSubmittedWord());
            }
        }
        this.currentRound = new Round(roundNumber, letters, durationSeconds);
        this.state = ROUND_ACTIVE;
        this.roundWinner = "";
        for (PlayerState playerState : players.values()) {
            playerState.clearSubmission();
        }
    }

    public synchronized boolean submitWord(String username, String word, int requestRound) {
        if (!ROUND_ACTIVE.equals(state) || currentRound == null) {
            return false;
        }
        if (currentRound.getRoundNumber() != requestRound) {
            return false;
        }
        PlayerState playerState = players.get(username);
        if (playerState == null || playerState.hasSubmitted()) {
            return false;
        }
        playerState.setSubmittedWord(word);
        return true;
    }

    public synchronized void endRoundNoWinner() {
        this.state = ROUND_OVER;
        this.roundWinner = "";
    }

    public synchronized void endRoundWithWinner(String username) {
        this.state = ROUND_OVER;
        this.roundWinner = username;
        PlayerState winner = players.get(username);
        if (winner != null) {
            winner.incrementRoundWins();
            if (winner.getRoundWins() >= 3) {
                this.gameWinner = username;
                this.state = GAME_OVER;
            }
        }
    }

    public synchronized List<PlayerState> getPlayersSnapshot() {
        return new ArrayList<>(players.values());
    }

    public synchronized String getDisplayWordForPlayer(String username) {
        if (currentRound == null) {
            return null;
        }
        PlayerState playerState = players.get(username);
        if (playerState == null) {
            return null;
        }
        if (currentRound.getRoundNumber() <= 1) {
            return playerState.getSubmittedWord();
        }
        return lastRoundWords.get(username);
    }
}



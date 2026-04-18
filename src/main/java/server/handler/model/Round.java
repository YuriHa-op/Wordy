package server.handler.model;

import java.util.List;

public class Round {

    private final int roundNumber;
    private final List<String> letters;
    private final long startTimeMillis;
    private final int durationSeconds;

    public Round(int roundNumber, List<String> letters, int durationSeconds) {
        this.roundNumber = roundNumber;
        this.letters = letters;
        this.durationSeconds = durationSeconds;
        this.startTimeMillis = System.currentTimeMillis();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<String> getLetters() {
        return letters;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public int getTimeRemainingSeconds() {
        long elapsedMillis = System.currentTimeMillis() - startTimeMillis;
        int elapsedSeconds = (int) (elapsedMillis / 1000L);
        int remaining = durationSeconds - elapsedSeconds;
        return Math.max(remaining, 0);
    }
}


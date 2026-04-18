package server.handler.model;

public class PlayerState {

    private final String username;
    private int roundWins;
    private String submittedWord;

    public PlayerState(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getRoundWins() {
        return roundWins;
    }

    public void incrementRoundWins() {
        this.roundWins++;
    }

    public String getSubmittedWord() {
        return submittedWord;
    }

    public void setSubmittedWord(String submittedWord) {
        this.submittedWord = submittedWord;
    }

    public void clearSubmission() {
        this.submittedWord = null;
    }

    public boolean hasSubmitted() {
        return submittedWord != null && !submittedWord.isBlank();
    }
}


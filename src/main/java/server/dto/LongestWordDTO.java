package server.dto;

public class LongestWordDTO {
    private String username;
    private String word;
    private int wordLength;

    public LongestWordDTO(String username, String word, int wordLength) {
        this.username = username;
        this.word = word;
        this.wordLength = wordLength;
    }

    public String getUsername() { return username; }
    public String getWord() { return word; }
    public int getWordLength() { return wordLength; }
}


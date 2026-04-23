package server.controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.UUID;

/**
 * GameManager handles the core business logic of matchmaking,
 * currently independent but it's by service layer for later ig
 */
public class GameManager {
    private static final Logger logger = Logger.getLogger(GameManager.class.getName());

    // In-memory mock storage before we plug in Database logic
    private final ConcurrentHashMap<String, String> activeMatches = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> playerScores = new ConcurrentHashMap<>();

    private static final GameManager INSTANCE = new GameManager();

    private GameManager() {}

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public String joinMatchmaking(String playerId) {
        logger.info("Player " + playerId + " attempting to join matchmaking...");
        // Placeholder for real logic (putting player in queue, finding opponent)
        String mockMatchId = UUID.randomUUID().toString();
        activeMatches.put(mockMatchId, playerId);
        return mockMatchId;
    }

    public boolean validateWord(String matchId, String playerId, String word) {
        logger.info("Validating word " + word + " in match " + matchId);

        // Mock validation: word must be greater than 3 chars
        boolean isValid = word != null && word.length() > 3;

        if (isValid) {
            // Update score
            playerScores.merge(playerId, word.length(), Integer::sum);
        }

        return isValid;
    }

    public int getPlayerScore(String playerId) {
        return playerScores.getOrDefault(playerId, 0);
    }
}


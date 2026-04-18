package server.handler.service;

import server.handler.core.GameManager;
import server.handler.core.GameSession;

public class GameService {

    private final GameManager gameManager;
    private final AuthService authService;

    public GameService(GameManager gameManager, AuthService authService) {
        this.gameManager = gameManager;
        this.authService = authService;
    }

    public GameManager.JoinResult joinGame(String sessionToken) {
        if (!authService.isValidSession(sessionToken)) {
            return new GameManager.JoinResult("INVALID_SESSION", -1);
        }
        String username = authService.getUsername(sessionToken);
        return gameManager.joinGame(username);
    }

    public GameSession pollState(String sessionToken, int gameId) {
        if (!authService.isValidSession(sessionToken)) {
            return null;
        }
        String username = authService.getUsername(sessionToken);
        GameSession session = gameManager.getGameForUser(username);
        if (session == null && gameId > 0) {
            session = gameManager.getGameById(gameId);
        }
        return session;
    }

    public String submitWord(String sessionToken, int gameId, String word, int roundNumber) {
        if (!authService.isValidSession(sessionToken)) {
            return "INVALID_SESSION";
        }
        if (word == null || word.isBlank()) {
            return "INVALID";
        }
        if (word.length() < 5) {
            return "TOO_SHORT";
        }

        String username = authService.getUsername(sessionToken);
        GameSession session = gameManager.getGameById(gameId);
        if (session == null || session.getCurrentRound() == null) {
            return "NO_GAME";
        }
        if (!GameSession.ROUND_ACTIVE.equals(session.getState())) {
            return "ROUND_NOT_ACTIVE";
        }

        boolean ok = gameManager.submitWord(username, gameId, word, roundNumber);
        return ok ? "VALID" : "INVALID";
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}


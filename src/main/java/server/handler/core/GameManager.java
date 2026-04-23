package server.handler.core;

import server.db.ConfigDAO;
import server.db.LongestWordDAO;
import server.db.UserDAO;
import server.handler.data.WordValidator;
import server.handler.model.PlayerState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {

    private static final int GAME_OVER_RETENTION_SECONDS = 8;
    private static final int DEBUG_WORD_LIMIT = 30;

    public static class JoinResult {
        public final String status;
        public final int gameId;

        public JoinResult(String status, int gameId) {
            this.status = status;
            this.gameId = gameId;
        }
    }

    public static class MonitorEntry {
        public final int gameId;
        public final String state;
        public final int roundNumber;
        public final List<String> players;
        public final List<String> availableWords;

        public MonitorEntry(int gameId, String state, int roundNumber, List<String> players, List<String> availableWords) {
            this.gameId = gameId;
            this.state = state;
            this.roundNumber = roundNumber;
            this.players = players;
            this.availableWords = availableWords;
        }
    }

    private final Set<String> lobby = new LinkedHashSet<>();
    private final Set<String> timedOutLobbyUsers = new LinkedHashSet<>();
    private final Map<Integer, GameSession> activeGames = new ConcurrentHashMap<>();
    private final Map<String, Integer> userGameIndex = new ConcurrentHashMap<>();

    private final AtomicInteger gameIdSeq = new AtomicInteger(1);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Random random = new Random();

    private final ConfigDAO configDAO;
    private final UserDAO userDAO;
    private final LongestWordDAO longestWordDAO;

    private volatile boolean lobbyTimerRunning = false;
    private volatile long lobbyDeadlineMillis = 0L;

    public GameManager(ConfigDAO configDAO, UserDAO userDAO, LongestWordDAO longestWordDAO) {
        this.configDAO = configDAO;
        this.userDAO = userDAO;
        this.longestWordDAO = longestWordDAO;
    }

    public synchronized JoinResult joinGame(String username) {
        Integer existingGameId = userGameIndex.get(username);
        if (existingGameId != null) {
            return new JoinResult("MATCHED", existingGameId);
        }

        timedOutLobbyUsers.remove(username);
        lobby.add(username);
        if (!lobbyTimerRunning) {
            int waitingTime = configDAO.getConfigValue("waiting_time", 10);
            lobbyTimerRunning = true;
            lobbyDeadlineMillis = System.currentTimeMillis() + waitingTime * 1000L;
            scheduler.schedule(this::promoteLobbyToGame, waitingTime, TimeUnit.SECONDS);
        }
        return new JoinResult("WAITING", -1);
    }

    private synchronized void promoteLobbyToGame() {
        lobbyTimerRunning = false;
        if (lobby.size() < 2) {
            timedOutLobbyUsers.addAll(lobby);
            lobby.clear();
            lobbyDeadlineMillis = 0L;
            return;
        }

        List<String> players = new ArrayList<>(lobby);
        lobby.clear();

        int gameId = gameIdSeq.getAndIncrement();
        GameSession session = new GameSession(gameId, players);
        activeGames.put(gameId, session);
        for (String player : players) {
            userGameIndex.put(player, gameId);
        }
        lobbyDeadlineMillis = 0L;

        startRound(session, 1);
    }

    public GameSession getGameForUser(String username) {
        Integer gameId = userGameIndex.get(username);
        return gameId == null ? null : activeGames.get(gameId);
    }

    public synchronized boolean isUserInLobby(String username) {
        return lobby.contains(username);
    }

    public synchronized int getLobbyRemainingSeconds(String username) {
        if (!lobby.contains(username) || lobbyDeadlineMillis <= 0L) {
            return 0;
        }
        long remainingMillis = lobbyDeadlineMillis - System.currentTimeMillis();
        if (remainingMillis <= 0L) {
            return 0;
        }
        return (int) Math.ceil(remainingMillis / 1000.0);
    }

    public synchronized boolean consumeLobbyTimeoutForUser(String username) {
        return timedOutLobbyUsers.remove(username);
    }

    public GameSession getGameById(int gameId) {
        return activeGames.get(gameId);
    }

    public boolean submitWord(String username, int gameId, String word, int roundNumber) {
        GameSession session = activeGames.get(gameId);
        if (session == null) {
            return false;
        }

        if (!WordValidator.isInDictionary(word)) {
            return false;
        }
        if (word.length() < 5) {
            return false;
        }
        if (!WordValidator.canBeFormed(word, session.getCurrentRound().getLetters())) {
            return false;
        }

        boolean accepted = session.submitWord(username, word.toUpperCase(), roundNumber);
        return accepted;
    }

    private void startRound(GameSession session, int roundNumber) {
        int duration = configDAO.getConfigValue("round_duration", 30);
        session.startRound(generateLetters(), duration, roundNumber);
        logCurrentRoundWordsForActiveGames();
        scheduler.schedule(() -> resolveRound(session.getGameId(), roundNumber), duration, TimeUnit.SECONDS);
    }

    private void logCurrentRoundWordsForActiveGames() {
        for (GameSession activeSession : activeGames.values()) {
            if (!GameSession.ROUND_ACTIVE.equals(activeSession.getState()) || activeSession.getCurrentRound() == null) {
                continue;
            }

            List<String> playerNames = new ArrayList<>();
            for (PlayerState playerState : activeSession.getPlayersSnapshot()) {
                playerNames.add(playerState.getUsername());
            }

            List<String> allWords = WordValidator.findSubmittableWords(activeSession.getCurrentRound().getLetters(), 5);
            int visibleCount = Math.min(DEBUG_WORD_LIMIT, allWords.size());
            List<String> visibleWords = allWords.subList(0, visibleCount);
            int hiddenCount = allWords.size() - visibleCount;

            String playersLabel = "[" + String.join(" vs ", playerNames) + "]";
            String moreSuffix = hiddenCount > 0 ? " (+" + hiddenCount + " more)" : "";
            System.out.println("[Round " + activeSession.getCurrentRound().getRoundNumber() + "] "
                    + playersLabel + ": " + visibleWords + moreSuffix);
        }
    }

    private void resolveRound(int gameId, int roundNumber) {
        GameSession session = activeGames.get(gameId);
        if (session == null || session.getCurrentRound() == null) {
            return;
        }
        if (session.getCurrentRound().getRoundNumber() != roundNumber) {
            return;
        }

        String winner = findRoundWinner(session);
        if (winner == null) {
            session.endRoundNoWinner();
        } else {
            session.endRoundWithWinner(winner);
            if (session.getGameWinner() != null) {
                try {
                    userDAO.incrementWins(session.getGameWinner());
                } catch (Exception ignored) {
                    // Keep game loop alive if DB update fails.
                }
            }
        }

        // Persist submitted words for leaderboard purposes.
        for (PlayerState playerState : session.getPlayersSnapshot()) {
            if (playerState.hasSubmitted()) {
                try {
                    longestWordDAO.insertLongestWord(playerState.getUsername(), playerState.getSubmittedWord());
                } catch (Exception ignored) {
                    // Ignore persistence failures during round resolution.
                }
            }
        }

        if (GameSession.GAME_OVER.equals(session.getState())) {
            for (PlayerState playerState : session.getPlayersSnapshot()) {
                userGameIndex.remove(playerState.getUsername());
            }
            scheduler.schedule(() -> {
                activeGames.remove(session.getGameId());
            }, GAME_OVER_RETENTION_SECONDS, TimeUnit.SECONDS);
            return;
        }

        scheduler.schedule(() -> startRound(session, roundNumber + 1), 2, TimeUnit.SECONDS);
    }

    private String findRoundWinner(GameSession session) {
        int bestLen = -1;
        String bestUser = null;
        boolean tie = false;

        for (PlayerState playerState : session.getPlayersSnapshot()) {
            if (!playerState.hasSubmitted()) {
                continue;
            }
            int len = playerState.getSubmittedWord().length();
            if (len > bestLen) {
                bestLen = len;
                bestUser = playerState.getUsername();
                tie = false;
            } else if (len == bestLen) {
                tie = true;
            }
        }

        if (bestLen < 0 || tie) {
            return null;
        }
        return bestUser;
    }

    private List<String> generateLetters() {
        char[] vowels = {'A', 'E', 'I', 'O', 'U'};
        List<Character> consonants = new ArrayList<>();
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c != 'A' && c != 'E' && c != 'I' && c != 'O' && c != 'U') {
                consonants.add(c);
            }
        }

        int vowelCount = 5 + random.nextInt(3); // 5-7
        List<String> letters = new ArrayList<>(20);

        for (int i = 0; i < vowelCount; i++) {
            letters.add(String.valueOf(vowels[random.nextInt(vowels.length)]));
        }
        for (int i = vowelCount; i < 20; i++) {
            letters.add(String.valueOf(consonants.get(random.nextInt(consonants.size()))));
        }

        Collections.shuffle(letters, random);
        return letters;
    }

    public Map<String, Integer> getRoundWins(GameSession session) {
        Map<String, Integer> scores = new HashMap<>();
        for (PlayerState playerState : session.getPlayersSnapshot()) {
            scores.put(playerState.getUsername(), playerState.getRoundWins());
        }
        return scores;
    }

    public synchronized List<MonitorEntry> getMonitorSnapshot() {
        List<MonitorEntry> snapshot = new ArrayList<>();
        for (Map.Entry<Integer, GameSession> entry : activeGames.entrySet()) {
            GameSession session = entry.getValue();
            List<PlayerState> players = session.getPlayersSnapshot();
            List<String> names = new ArrayList<>();
            for (PlayerState player : players) {
                names.add(player.getUsername());
            }

            List<String> availableWords = session.getCurrentRound() == null
                    ? Collections.emptyList()
                    : WordValidator.findSubmittableWords(session.getCurrentRound().getLetters(), 5);

            snapshot.add(new MonitorEntry(
                    session.getGameId(),
                    session.getState(),
                    session.getCurrentRound() == null ? 0 : session.getCurrentRound().getRoundNumber(),
                    names,
                    availableWords
            ));
        }
        return snapshot;
    }

    public synchronized List<String> getLobbySnapshot() {
        return new ArrayList<>(lobby);
    }

    public void shutdown() {
        scheduler.shutdownNow();
    }
}







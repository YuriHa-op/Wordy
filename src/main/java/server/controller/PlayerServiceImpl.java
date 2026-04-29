package server.controller;

import com.wordy.grpc.GameStateRequest;
import com.wordy.grpc.GameStateResponse;
import com.wordy.grpc.GetLeaderboardRequest;
import com.wordy.grpc.GetLeaderboardResponse;
import com.wordy.grpc.JoinGameRequest;
import com.wordy.grpc.JoinGameResponse;
import com.wordy.grpc.LeaderboardPlayer;
import com.wordy.grpc.LeaderboardWord;
import com.wordy.grpc.LoginRequest;
import com.wordy.grpc.LoginResponse;
import com.wordy.grpc.LogoutRequest;
import com.wordy.grpc.LogoutResponse;
import com.wordy.grpc.PlayerScore;
import com.wordy.grpc.PlayerServiceGrpc;
import com.wordy.grpc.SubmitWordRequest;
import com.wordy.grpc.SubmitWordResponse;
import io.grpc.stub.StreamObserver;
import server.dto.LongestWordDTO;
import server.dto.UserDTO;
import server.handler.core.GameManager;
import server.handler.core.GameSession;
import server.handler.model.PlayerState;
import server.handler.service.AuthService;
import server.handler.service.GameService;
import server.handler.service.LeaderboardService;

public class PlayerServiceImpl extends PlayerServiceGrpc.PlayerServiceImplBase {

    private final AuthService authService;
    private final GameService gameService;
    private final LeaderboardService leaderboardService;

    public PlayerServiceImpl(AuthService authService, GameService gameService, LeaderboardService leaderboardService) {
        this.authService = authService;
        this.gameService = gameService;
        this.leaderboardService = leaderboardService;
    }

    @Override
    public void joinGame(JoinGameRequest request, StreamObserver<JoinGameResponse> responseObserver) {
        GameManager.JoinResult result = gameService.joinGame(request.getSessionToken());
        boolean success = !"INVALID_SESSION".equals(result.status);
        responseObserver.onNext(JoinGameResponse.newBuilder()
                .setSuccess(success)
                .setMessage(success ? "Join request accepted" : "Invalid session")
                .setStatus(result.status)
                .setGameId(result.gameId)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void pollGameState(GameStateRequest request, StreamObserver<GameStateResponse> responseObserver) {
        String username = authService.getUsername(request.getSessionToken());
        if (username == null || username.isBlank()) {
            responseObserver.onNext(GameStateResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Invalid session")
                    .setStatus("INVALID_SESSION")
                    .build());
            responseObserver.onCompleted();
            return;
        }

        GameSession session = gameService.pollState(request.getSessionToken(), request.getGameId());
        if (session == null || session.getCurrentRound() == null) {
            GameManager gameManager = gameService.getGameManager();
            if (gameManager.consumeLobbyTimeoutForUser(username)) {
                responseObserver.onNext(GameStateResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("No match found")
                        .setStatus("NO_MATCH")
                        .setTimeRemaining(0)
                        .build());
                responseObserver.onCompleted();
                return;
            }

            if (gameManager.isUserInLobby(username)) {
                responseObserver.onNext(GameStateResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Waiting for players")
                        .setStatus("WAITING")
                        .setTimeRemaining(gameManager.getLobbyRemainingSeconds(username))
                        .build());
                responseObserver.onCompleted();
                return;
            }

            responseObserver.onNext(GameStateResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("No active game")
                    .setStatus("WAITING")
                    .build());
            responseObserver.onCompleted();
            return;
        }

        GameStateResponse.Builder builder = GameStateResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Game state")
                .setStatus(session.getState())
                .setTimeRemaining(session.getCurrentRound().getTimeRemainingSeconds())
                .setRoundWinner(session.getRoundWinner() == null ? "" : session.getRoundWinner())
                .setGameWinner(session.getGameWinner() == null ? "" : session.getGameWinner())
                .setRoundNumber(session.getCurrentRound().getRoundNumber())
                .addAllLetters(session.getCurrentRound().getLetters());

        for (PlayerState player : session.getPlayersSnapshot()) {
            builder.addPlayers(PlayerScore.newBuilder()
                    .setUsername(player.getUsername())
                    .setRoundWins(player.getRoundWins())
                    .setSubmittedThisRound(player.hasSubmitted())
                    .build());
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void submitWord(SubmitWordRequest request, StreamObserver<SubmitWordResponse> responseObserver) {
        String result = gameService.submitWord(
                request.getSessionToken(),
                request.getGameId(),
                request.getWord(),
                request.getRoundNumber()
        );

        boolean success = "VALID".equals(result);
        String message;
        switch (result) {
            case "TOO_SHORT" -> message = "Word too short (min length 5)";
            case "INVALID_SESSION" -> message = "Invalid session";
            case "ROUND_NOT_ACTIVE" -> message = "Round is not active";
            case "NO_GAME" -> message = "Game not found";
            case "INVALID" -> message = "Invalid word";
            default -> message = "Word submitted";
        }

        responseObserver.onNext(SubmitWordResponse.newBuilder()
                .setSuccess(success)
                .setMessage(message)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getLeaderboard(GetLeaderboardRequest request, StreamObserver<GetLeaderboardResponse> responseObserver) {
        GetLeaderboardResponse.Builder builder = GetLeaderboardResponse.newBuilder();
        for (UserDTO user : leaderboardService.getTopPlayers()) {
            builder.addTopPlayers(LeaderboardPlayer.newBuilder()
                    .setUsername(user.getUsername())
                    .setTotalWins(user.getTotalWins())
                    .build());
        }
        for (LongestWordDTO word : leaderboardService.getTopLongestWords()) {
            builder.addLongestWords(LeaderboardWord.newBuilder()
                    .setUsername(word.getUsername())
                    .setWord(word.getWord())
                    .setWordLength(word.getWordLength())
                    .build());
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}


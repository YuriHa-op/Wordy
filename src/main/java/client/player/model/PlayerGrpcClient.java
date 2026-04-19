package client.player.model;

import com.wordy.grpc.GameStateRequest;
import com.wordy.grpc.GameStateResponse;
import com.wordy.grpc.GetLeaderboardRequest;
import com.wordy.grpc.GetLeaderboardResponse;
import com.wordy.grpc.JoinGameRequest;
import com.wordy.grpc.JoinGameResponse;
import com.wordy.grpc.LoginRequest;
import com.wordy.grpc.LoginResponse;
import com.wordy.grpc.LogoutRequest;
import com.wordy.grpc.LogoutResponse;
import com.wordy.grpc.PlayerServiceGrpc;
import com.wordy.grpc.SubmitWordRequest;
import com.wordy.grpc.SubmitWordResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class PlayerGrpcClient {

    private final ManagedChannel channel;
    private final PlayerServiceGrpc.PlayerServiceBlockingStub stub;
    private String sessionToken;
    private String currentUsername;

    public PlayerGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = PlayerServiceGrpc.newBlockingStub(channel);
    }

    public LoginResponse login(String username, String password) {
        LoginResponse response = stub.login(LoginRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build());
        if (response.getSuccess()) {
            this.sessionToken = response.getSessionToken();
            this.currentUsername = username;
        }
        return response;
    }

    public LogoutResponse logout() {
        if (sessionToken == null || sessionToken.isBlank()) {
            return LogoutResponse.newBuilder().setSuccess(true).setMessage("Already logged out").build();
        }
        LogoutResponse response = stub.logout(LogoutRequest.newBuilder().setSessionToken(sessionToken).build());
        if (response.getSuccess()) {
            this.sessionToken = null;
            this.currentUsername = null;
        }
        return response;
    }

    public JoinGameResponse joinGame() {
        return stub.joinGame(JoinGameRequest.newBuilder().setSessionToken(sessionToken).build());
    }

    public GameStateResponse pollGameState(int gameId) {
        return stub.pollGameState(GameStateRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setGameId(gameId)
                .build());
    }

    public SubmitWordResponse submitWord(int gameId, String word, int roundNumber) {
        return stub.submitWord(SubmitWordRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setGameId(gameId)
                .setWord(word)
                .setRoundNumber(roundNumber)
                .build());
    }

    public GetLeaderboardResponse getLeaderboard() {
        return stub.getLeaderboard(GetLeaderboardRequest.newBuilder().build());
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void clearLocalSession() {
        this.sessionToken = null;
        this.currentUsername = null;
    }

    public void shutdown() {
        try {
            channel.shutdownNow().awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}


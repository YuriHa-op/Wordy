package client.admin.model;

import com.wordy.grpc.AdminServiceGrpc;
import com.wordy.grpc.CreatePlayerRequest;
import com.wordy.grpc.CreatePlayerResponse;
import com.wordy.grpc.DeletePlayerRequest;
import com.wordy.grpc.DeletePlayerResponse;
import com.wordy.grpc.GetConfigRequest;
import com.wordy.grpc.GetConfigResponse;
import com.wordy.grpc.LoginRequest;
import com.wordy.grpc.LoginResponse;
import com.wordy.grpc.ReadPlayerRequest;
import com.wordy.grpc.ReadPlayerResponse;
import com.wordy.grpc.SearchPlayerRequest;
import com.wordy.grpc.SearchPlayerResponse;
import com.wordy.grpc.UpdateConfigRequest;
import com.wordy.grpc.UpdateConfigResponse;
import com.wordy.grpc.UpdatePlayerRequest;
import com.wordy.grpc.UpdatePlayerResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class AdminGrpcClient {

    private final ManagedChannel channel;
    private final AdminServiceGrpc.AdminServiceBlockingStub stub;
    private String sessionToken;

    public AdminGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = AdminServiceGrpc.newBlockingStub(channel);
    }

    public LoginResponse login(String username, String password) {
        LoginResponse response = stub.adminLogin(LoginRequest.newBuilder()
                .setUsername(username)
                .setPassword(password)
                .build());
        if (response.getSuccess()) {
            sessionToken = response.getSessionToken();
        }
        return response;
    }

    public CreatePlayerResponse createPlayer(String username, String password, String role) {
        return stub.createPlayer(CreatePlayerRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                .build());
    }

    public ReadPlayerResponse readPlayer(int userId) {
        return stub.readPlayer(ReadPlayerRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setUserId(userId)
                .build());
    }

    public UpdatePlayerResponse updatePlayer(int userId, String username, String password, String role, int totalWins) {
        return stub.updatePlayer(UpdatePlayerRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setUserId(userId)
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                .setTotalWins(totalWins)
                .build());
    }

    public DeletePlayerResponse deletePlayer(int userId) {
        return stub.deletePlayer(DeletePlayerRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setUserId(userId)
                .build());
    }

    public SearchPlayerResponse searchPlayer(String keyword) {
        return stub.searchPlayer(SearchPlayerRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setKeyword(keyword)
                .build());
    }

    public GetConfigResponse getConfig() {
        return stub.getConfig(GetConfigRequest.newBuilder().setSessionToken(sessionToken).build());
    }

    public UpdateConfigResponse updateConfig(String key, int value) {
        return stub.updateConfig(UpdateConfigRequest.newBuilder()
                .setSessionToken(sessionToken)
                .setKey(key)
                .setValue(value)
                .build());
    }

    public void shutdown() {
        try {
            channel.shutdownNow().awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}



package server.controller;

import java.util.List;
import java.util.Map;

import com.wordy.grpc.AdminServiceGrpc;
import com.wordy.grpc.ConfigItem;
import com.wordy.grpc.CreatePlayerRequest;
import com.wordy.grpc.CreatePlayerResponse;
import com.wordy.grpc.DeletePlayerRequest;
import com.wordy.grpc.DeletePlayerResponse;
import com.wordy.grpc.GetConfigRequest;
import com.wordy.grpc.GetConfigResponse;
import com.wordy.grpc.ReadPlayerRequest;
import com.wordy.grpc.ReadPlayerResponse;
import com.wordy.grpc.SearchPlayerRequest;
import com.wordy.grpc.SearchPlayerResponse;
import com.wordy.grpc.UpdateConfigRequest;
import com.wordy.grpc.UpdateConfigResponse;
import com.wordy.grpc.UpdatePlayerRequest;
import com.wordy.grpc.UpdatePlayerResponse;

import io.grpc.stub.StreamObserver;
import server.db.ConfigDAO;
import server.db.UserDAO;
import server.dto.UserDTO;
import server.handler.service.AuthService;

public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {

    private final AuthService authService;
    private final UserDAO userDAO;
    private final ConfigDAO configDAO;

    public AdminServiceImpl(AuthService authService, UserDAO userDAO, ConfigDAO configDAO) {
        this.authService = authService;
        this.userDAO = userDAO;
        this.configDAO = configDAO;
    }

    @Override
    public void createPlayer(CreatePlayerRequest request, StreamObserver<CreatePlayerResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(CreatePlayerResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            String role = request.getRole().isBlank() ? "PLAYER" : request.getRole();
            boolean success = userDAO.createUser(request.getUsername(), request.getPassword(), role);
            responseObserver.onNext(CreatePlayerResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(success ? "Player created" : "Could not create player")
                    .build());
        } catch (Exception e) {
            responseObserver.onNext(CreatePlayerResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void readPlayer(ReadPlayerRequest request, StreamObserver<ReadPlayerResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(ReadPlayerResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            UserDTO user = userDAO.findById(request.getUserId());
            if (user == null) {
                responseObserver.onNext(ReadPlayerResponse.newBuilder().setSuccess(false).setMessage("User not found").build());
            } else {
                responseObserver.onNext(toReadResponse(user).toBuilder().setSuccess(true).setMessage("OK").build());
            }
        } catch (Exception e) {
            responseObserver.onNext(ReadPlayerResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updatePlayer(UpdatePlayerRequest request, StreamObserver<UpdatePlayerResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(UpdatePlayerResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            int wins = Math.max(0, request.getTotalWins());
            boolean success = userDAO.updateUser(
                    request.getUserId(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getRole(),
                    wins
            );
            responseObserver.onNext(UpdatePlayerResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(success ? "Player updated" : "Update failed")
                    .build());
        } catch (Exception e) {
            responseObserver.onNext(UpdatePlayerResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void deletePlayer(DeletePlayerRequest request, StreamObserver<DeletePlayerResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(DeletePlayerResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            boolean success = userDAO.deleteUser(request.getUserId());
            responseObserver.onNext(DeletePlayerResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(success ? "Player deleted" : "Delete failed")
                    .build());
        } catch (Exception e) {
            responseObserver.onNext(DeletePlayerResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void searchPlayer(SearchPlayerRequest request, StreamObserver<SearchPlayerResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(SearchPlayerResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            List<UserDTO> users = userDAO.searchUsers(request.getKeyword());
            SearchPlayerResponse.Builder builder = SearchPlayerResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("OK");
            for (UserDTO user : users) {
                builder.addPlayers(toReadResponse(user));
            }
            responseObserver.onNext(builder.build());
        } catch (Exception e) {
            responseObserver.onNext(SearchPlayerResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getConfig(GetConfigRequest request, StreamObserver<GetConfigResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(GetConfigResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            Map<String, Integer> configs = configDAO.getAllConfigs();
            GetConfigResponse.Builder builder = GetConfigResponse.newBuilder().setSuccess(true).setMessage("OK");
            for (Map.Entry<String, Integer> entry : configs.entrySet()) {
                builder.addConfigs(ConfigItem.newBuilder().setKey(entry.getKey()).setValue(entry.getValue()).build());
            }
            responseObserver.onNext(builder.build());
        } catch (Exception e) {
            responseObserver.onNext(GetConfigResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateConfig(UpdateConfigRequest request, StreamObserver<UpdateConfigResponse> responseObserver) {
        if (!isAdminSession(request.getSessionToken())) {
            responseObserver.onNext(UpdateConfigResponse.newBuilder().setSuccess(false).setMessage("Unauthorized").build());
            responseObserver.onCompleted();
            return;
        }

        try {
            boolean success = configDAO.updateConfig(request.getKey(), request.getValue());
            responseObserver.onNext(UpdateConfigResponse.newBuilder()
                    .setSuccess(success)
                    .setMessage(success ? "Config updated" : "Config update failed")
                    .build());
        } catch (Exception e) {
            responseObserver.onNext(UpdateConfigResponse.newBuilder().setSuccess(false).setMessage(e.getMessage()).build());
        }
        responseObserver.onCompleted();
    }

    private boolean isAdminSession(String token) {
        return authService.isValidSession(token) && authService.isAdmin(token);
    }

    private ReadPlayerResponse toReadResponse(UserDTO user) {
        return ReadPlayerResponse.newBuilder()
                .setUserId(user.getUserId())
                .setUsername(user.getUsername())
                .setRole(user.getRole())
                .setTotalWins(user.getTotalWins())
                .build();
    }
}



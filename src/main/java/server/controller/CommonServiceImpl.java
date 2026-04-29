package server.controller;

import com.wordy.grpc.CheckSessionRequest;
import com.wordy.grpc.CheckSessionResponse;
import com.wordy.grpc.CommonServiceGrpc;
import com.wordy.grpc.LoginRequest;
import com.wordy.grpc.LoginResponse;
import com.wordy.grpc.LogoutRequest;
import com.wordy.grpc.LogoutResponse;

import io.grpc.stub.StreamObserver;
import server.handler.service.AuthService;

public class CommonServiceImpl extends CommonServiceGrpc.CommonServiceImplBase {

    private final AuthService authService;

    public CommonServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();
        
        // Let the client decide if they need admin view or not after logging in.
        // Actually, the new unified login should probably just return success and the role?
        // Wait, does LoginResponse need the role returned?
        
        server.handler.service.AuthService.AuthResult result = authService.login(username, password, false);
        
        LoginResponse response = LoginResponse.newBuilder()
                .setSuccess(result.success)
                .setMessage(result.message)
                .setSessionToken(result.token)
                .setRole(result.role)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void logout(LogoutRequest request, StreamObserver<LogoutResponse> responseObserver) {
        server.handler.service.AuthService.AuthResult result = authService.logout(request.getSessionToken());
        
        LogoutResponse response = LogoutResponse.newBuilder()
                .setSuccess(result.success)
                .setMessage(result.message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkSession(CheckSessionRequest request, StreamObserver<CheckSessionResponse> responseObserver) {
        boolean valid = authService.isValidSession(request.getSessionToken());
        responseObserver.onNext(CheckSessionResponse.newBuilder().setIsValid(valid).build());
        responseObserver.onCompleted();
    }
}

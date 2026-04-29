package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import server.controller.AdminServiceImpl;
import server.controller.CommonServiceImpl;
import server.controller.PlayerServiceImpl;
import server.db.ConfigDAO;
import server.db.LongestWordDAO;
import server.db.UserDAO;
import server.handler.core.GameManager;
import server.handler.core.SessionManager;
import server.handler.service.AuthService;
import server.handler.service.GameService;
import server.handler.service.LeaderboardService;

public class GrpcServer {

    public static void main(String[] args) throws Exception {
        UserDAO userDAO = new UserDAO();
        ConfigDAO configDAO = new ConfigDAO();
        LongestWordDAO longestWordDAO = new LongestWordDAO();

        SessionManager sessionManager = new SessionManager();
        GameManager gameManager = new GameManager(configDAO, userDAO, longestWordDAO);

        AuthService authService = new AuthService(userDAO, sessionManager);
        GameService gameService = new GameService(gameManager, authService);
        LeaderboardService leaderboardService = new LeaderboardService(userDAO, longestWordDAO);

        Server server = ServerBuilder.forPort(6767)
                .addService(new CommonServiceImpl(authService))
                .addService(new PlayerServiceImpl(authService, gameService, leaderboardService))
                .addService(new AdminServiceImpl(authService, userDAO, configDAO))
                .build()
                .start();

        System.out.println("Wordy gRPC server started on port 6767");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gameManager.shutdown();
            server.shutdown();
        }));

        server.awaitTermination();
    }
}


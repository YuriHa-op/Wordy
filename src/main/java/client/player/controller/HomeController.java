package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.HomeView;
import client.ui.components.PopNotification;
import client.ui.util.StyledDialog;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class HomeController {

    private final HomeView view;
    private final PlayerGrpcClient client;
    private Timer sessionChecker;

    public HomeController(PlayerGrpcClient client) {
        this.client = client;
        this.view = new HomeView();
        bind();
        startSessionChecker();
    }

    private void startSessionChecker() {
        sessionChecker = new Timer(2000, e -> {
            if (!client.checkSession()) {
                sessionChecker.stop();
                PopNotification.showSessionEnded(view, "<html><center>You have been disconnected because someone else<br>logged in with your account.</center></html>");
                client.shutdown();
                view.dispose();
                new LoginController().show();
            }
        });
        sessionChecker.start();
    }

    public void show() {
        view.setVisible(true);
    }

    private void cleanup() {
        if (sessionChecker != null) {
            sessionChecker.stop();
        }
        view.dispose();
    }

    private void bind() {
        view.getStartGameButton().addActionListener(e -> {
            cleanup();
            new GameController(client).show();
        });

        view.getLeaderboardButton().addActionListener(e -> {
            cleanup();
            new LeaderboardController(client).show();
        });

        view.getLogoutButton().addActionListener(e -> {
            try {
                if (sessionChecker != null) sessionChecker.stop();
                client.logout();
            } catch (Exception ex) {
                StyledDialog.showMessage(view, "Logout Error", ex.getMessage());
            } finally {
                client.shutdown();
                view.dispose();
                new LoginController().show();
            }
        });
    }
}


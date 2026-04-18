package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.HomeView;
import client.ui.util.MinecraftDialog;

public class HomeController {

    private final HomeView view;
    private final PlayerGrpcClient client;

    public HomeController(PlayerGrpcClient client) {
        this.client = client;
        this.view = new HomeView();
        bind();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bind() {
        view.getStartGameButton().addActionListener(e -> {
            view.dispose();
            new GameController(client).show();
        });

        view.getLeaderboardButton().addActionListener(e -> {
            view.dispose();
            new LeaderboardController(client).show();
        });

        view.getLogoutButton().addActionListener(e -> {
            try {
                client.logout();
            } catch (Exception ex) {
                MinecraftDialog.showMessage(view, "Logout Error", ex.getMessage());
            } finally {
                client.shutdown();
                view.dispose();
                new LoginController().show();
            }
        });
    }
}


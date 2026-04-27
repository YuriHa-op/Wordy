package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.LoginView;
import com.wordy.grpc.LoginResponse;

import javax.swing.SwingUtilities;

public class LoginController {

    private final LoginView view;
    private final PlayerGrpcClient client;

    public LoginController() {
        this.view = new LoginView();
        String host = System.getProperty("wordy.server.host");
        if (host == null || host.isBlank()) host = System.getenv("WORDY_SERVER_HOST");
        if (host == null || host.isBlank()) host = "localhost";

        String portStr = System.getProperty("wordy.server.port");
        if (portStr == null || portStr.isBlank()) portStr = System.getenv("WORDY_SERVER_PORT");
        int port = 6767;
        if (portStr != null && !portStr.isBlank()) {
            try { port = Integer.parseInt(portStr); } catch (NumberFormatException ignore) {}
        }

        this.client = new PlayerGrpcClient(host, port);
        bind();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bind() {
        view.getLoginButton().addActionListener(e -> onLogin());
    }

    private void onLogin() {
        String username = view.getUsernameField().getText().trim();
        String password = new String(view.getPasswordField().getPassword());
        view.getMessageLabel().setText(" ");
        view.getLoginButton().setEnabled(false);
        try {
            LoginResponse response = client.login(username, password);
            if (response.getSuccess()) {
                view.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    new HomeController(client).show();
                    view.dispose();
                });
            } else {
                view.getMessageLabel().setText(response.getMessage());
            }
        } catch (Exception ex) {
            view.getMessageLabel().setText(ex.getMessage());
        } finally {
            if (view.isDisplayable()) {
                view.getLoginButton().setEnabled(true);
            }
        }
    }
}



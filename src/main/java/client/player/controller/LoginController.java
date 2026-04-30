package client.player.controller;

import client.admin.controller.AdminDashboardController;
import client.admin.model.AdminGrpcClient;
import client.player.model.PlayerGrpcClient;
import client.player.view.LoginView;
import com.wordy.grpc.LoginResponse;

import javax.swing.SwingUtilities;

public class LoginController {

    private final LoginView view;
    private final String host;
    private final int port;
    private final PlayerGrpcClient client;

    public LoginController() {
        this.view = new LoginView();
        String tempHost = System.getProperty("wordy.server.host");
        if (tempHost == null || tempHost.isBlank()) tempHost = System.getenv("WORDY_SERVER_HOST");
        if (tempHost == null || tempHost.isBlank()) tempHost = "localhost";
        this.host = tempHost;

        String portStr = System.getProperty("wordy.server.port");
        if (portStr == null || portStr.isBlank()) portStr = System.getenv("WORDY_SERVER_PORT");
        int tempPort = 6767;
        if (portStr != null && !portStr.isBlank()) {
            try { tempPort = Integer.parseInt(portStr); } catch (NumberFormatException ignore) {}
        }
        this.port = tempPort;

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
                    if ("ADMIN".equals(response.getRole())) {
                        AdminGrpcClient adminClient = new AdminGrpcClient(host, port);
                        // Re-authenticate admin to get their own session token inside AdminGrpcClient
                        adminClient.login(username, password);
                        new AdminDashboardController(adminClient).show();
                    } else {
                        new HomeController(client).show();
                    }
                    view.dispose();
                });
            } else {
                view.getMessageLabel().setText(response.getMessage());
                view.getMessageLabel().revalidate();
                view.getMessageLabel().repaint();
            }
        } catch (Exception ex) {
            String errorMsg = ex.getMessage();
            if (errorMsg != null && errorMsg.contains("UNAVAILABLE")) {
                errorMsg = "Cannot connect to server.";
            }
            view.getMessageLabel().setText(errorMsg);
            view.getMessageLabel().revalidate();
            view.getMessageLabel().repaint();
        } finally {
            if (view.isDisplayable()) {
                view.getLoginButton().setEnabled(true);
            }
        }
    }
}

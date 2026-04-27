package client.admin.controller;

import client.admin.model.AdminGrpcClient;
import client.admin.view.AdminLoginView;
import com.wordy.grpc.LoginResponse;

import javax.swing.SwingUtilities;

public class AdminLoginController {

    private final AdminLoginView view;
    private final AdminGrpcClient client;

    public AdminLoginController() {
        this.view = new AdminLoginView();
        String host = System.getProperty("wordy.server.host");
        if (host == null || host.isBlank()) host = System.getenv("WORDY_SERVER_HOST");
        if (host == null || host.isBlank()) host = "localhost";

        String portStr = System.getProperty("wordy.server.port");
        if (portStr == null || portStr.isBlank()) portStr = System.getenv("WORDY_SERVER_PORT");
        int port = 6767;
        if (portStr != null && !portStr.isBlank()) {
            try { port = Integer.parseInt(portStr); } catch (NumberFormatException ignore) {}
        }

        this.client = new AdminGrpcClient(host, port);
        bind();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bind() {
        view.getLoginButton().addActionListener(e -> login());
    }

    private void login() {
        String username = view.getUsernameField().getText().trim();
        String password = new String(view.getPasswordField().getPassword());
        view.getMessageLabel().setText(" ");
        view.getLoginButton().setEnabled(false);
        try {
            LoginResponse response = client.login(username, password);
            if (response.getSuccess()) {
                view.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    new AdminDashboardController(client).show();
                    view.dispose();
                });
            } else {
                view.getMessageLabel().setText(response.getMessage());
            }
        } catch (Exception e) {
            view.getMessageLabel().setText(e.getMessage());
        } finally {
            if (view.isDisplayable()) {
                view.getLoginButton().setEnabled(true);
            }
        }
    }
}



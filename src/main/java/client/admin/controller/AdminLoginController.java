package client.admin.controller;

import client.admin.model.AdminGrpcClient;
import client.admin.view.AdminLoginView;
import com.wordy.grpc.LoginResponse;

public class AdminLoginController {

    private final AdminLoginView view;
    private final AdminGrpcClient client;

    public AdminLoginController() {
        this.view = new AdminLoginView();
        this.client = new AdminGrpcClient("localhost", 6767);
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
        try {
            LoginResponse response = client.login(username, password);
            if (response.getSuccess()) {
                view.dispose();
                new AdminDashboardController(client).show();
            } else {
                view.getMessageLabel().setText(response.getMessage());
            }
        } catch (Exception e) {
            view.getMessageLabel().setText(e.getMessage());
        }
    }
}



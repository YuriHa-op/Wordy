package client.player.controller;

import client.player.model.PlayerGrpcClient;
import client.player.view.LoginView;
import com.wordy.grpc.LoginResponse;

public class LoginController {

    private final LoginView view;
    private final PlayerGrpcClient client;

    public LoginController() {
        this.view = new LoginView();
        this.client = new PlayerGrpcClient("localhost", 6767);
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
        try {
            LoginResponse response = client.login(username, password);
            if (response.getSuccess()) {
                view.dispose();
                new HomeController(client).show();
            } else {
                view.getMessageLabel().setText(response.getMessage());
            }
        } catch (Exception ex) {
            view.getMessageLabel().setText(ex.getMessage());
        }
    }
}



package client.admin.controller;

import client.admin.model.AdminGrpcClient;
import client.admin.view.AdminDashboardView;
import com.wordy.grpc.ConfigItem;
import com.wordy.grpc.GetConfigResponse;

public class AdminDashboardController {

    private final AdminDashboardView view;
    private final AdminGrpcClient client;

    public AdminDashboardController(AdminGrpcClient client) {
        this.client = client;
        this.view = new AdminDashboardView();

        new PlayerManagementController(client, view.getPlayerManagementView());
        bindConfigActions();
        bindClose();
    }

    public void show() {
        view.setVisible(true);
    }

    private void bindConfigActions() {
        view.getLoadConfigButton().addActionListener(e -> loadConfig());
        view.getSaveConfigButton().addActionListener(e -> saveConfig());
    }

    private void bindClose() {
        view.getCloseButton().addActionListener(e -> {
            client.shutdown();
            view.dispose();
            new AdminLoginController().show();
        });
    }

    private void loadConfig() {
        GetConfigResponse response = client.getConfig();
        if (!response.getSuccess()) {
            view.getConfigMessageLabel().setText(response.getMessage());
            return;
        }

        for (ConfigItem item : response.getConfigsList()) {
            if ("waiting_time".equals(item.getKey())) {
                view.getWaitingTimeField().setText(String.valueOf(item.getValue()));
            } else if ("round_duration".equals(item.getKey())) {
                view.getRoundDurationField().setText(String.valueOf(item.getValue()));
            }
        }
        view.getConfigMessageLabel().setText("Config loaded");
    }

    private void saveConfig() {
        try {
            int waiting = Integer.parseInt(view.getWaitingTimeField().getText().trim());
            int round = Integer.parseInt(view.getRoundDurationField().getText().trim());

            var r1 = client.updateConfig("waiting_time", waiting);
            var r2 = client.updateConfig("round_duration", round);

            if (r1.getSuccess() && r2.getSuccess()) {
                view.getConfigMessageLabel().setText("Config updated");
            } else {
                view.getConfigMessageLabel().setText(r1.getMessage() + " | " + r2.getMessage());
            }
        } catch (Exception e) {
            view.getConfigMessageLabel().setText("Invalid numbers");
        }
    }
}


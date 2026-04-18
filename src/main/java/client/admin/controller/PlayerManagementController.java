package client.admin.controller;

import client.admin.model.AdminGrpcClient;
import client.admin.view.PlayerManagementView;
import com.wordy.grpc.ReadPlayerResponse;
import com.wordy.grpc.SearchPlayerResponse;

public class PlayerManagementController {

    private final AdminGrpcClient client;
    private final PlayerManagementView view;

    public PlayerManagementController(AdminGrpcClient client, PlayerManagementView view) {
        this.client = client;
        this.view = view;
        bind();
    }

    private void bind() {
        view.getAddButton().addActionListener(e -> add());
        view.getReadButton().addActionListener(e -> read());
        view.getUpdateButton().addActionListener(e -> update());
        view.getDeleteButton().addActionListener(e -> delete());
        view.getSearchButton().addActionListener(e -> search());
    }

    private void add() {
        var response = client.createPlayer(
                view.getUsernameField().getText().trim(),
                view.getPasswordField().getText(),
                normalizeRole(view.getRoleField().getText())
        );
        write(response.getMessage());
    }

    private void read() {
        int id = parseId();
        if (id <= 0) {
            return;
        }
        ReadPlayerResponse response = client.readPlayer(id);
        if (!response.getSuccess()) {
            write(response.getMessage());
            return;
        }
        write("ID=" + response.getUserId() + " | " + response.getUsername() + " | role=" + response.getRole() + " | wins=" + response.getTotalWins());
    }

    private void update() {
        int id = parseId();
        if (id <= 0) {
            return;
        }
        int wins = parseWins();
        if (wins < 0) {
            return;
        }
        var response = client.updatePlayer(
                id,
                view.getUsernameField().getText().trim(),
                view.getPasswordField().getText(),
                normalizeRole(view.getRoleField().getText()),
                wins
        );
        write(response.getMessage());
    }

    private void delete() {
        int id = parseId();
        if (id <= 0) {
            return;
        }
        var response = client.deletePlayer(id);
        write(response.getMessage());
    }

    private void search() {
        SearchPlayerResponse response = client.searchPlayer(view.getSearchField().getText().trim());
        if (!response.getSuccess()) {
            write(response.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder("Search Results\n");
        for (ReadPlayerResponse p : response.getPlayersList()) {
            sb.append("ID=").append(p.getUserId())
                    .append(" | ").append(p.getUsername())
                    .append(" | role=").append(p.getRole())
                    .append(" | wins=").append(p.getTotalWins())
                    .append("\n");
        }
        write(sb.toString());
    }

    private int parseId() {
        try {
            return Integer.parseInt(view.getIdField().getText().trim());
        } catch (Exception e) {
            write("Invalid user id");
            return -1;
        }
    }

    private int parseWins() {
        try {
            int wins = Integer.parseInt(view.getWinsField().getText().trim());
            if (wins < 0) {
                write("Wins must be zero or greater");
                return -1;
            }
            return wins;
        } catch (Exception e) {
            write("Invalid wins value");
            return -1;
        }
    }

    private String normalizeRole(String role) {
        String value = role == null ? "" : role.trim().toUpperCase();
        if (!"ADMIN".equals(value)) {
            return "PLAYER";
        }
        return value;
    }

    private void write(String text) {
        view.getOutputArea().setText(text);
    }
}



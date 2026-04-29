package client;

import client.player.controller.LoginController;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ClientApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String host = JOptionPane.showInputDialog(null, "Enter server IP address:", "Server Connection", JOptionPane.QUESTION_MESSAGE);
            if (host == null) {
                System.exit(0); // User hit cancel
            }
            if (host.trim().isEmpty()) {
                host = "localhost";
            }
            // Temporarily set system property so the LoginController can pick it up
            System.setProperty("wordy.server.host", host);
            
            new LoginController().show();
        });
    }
}

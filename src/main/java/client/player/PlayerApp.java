package client.player;

import client.player.controller.LoginController;

import javax.swing.SwingUtilities;

public class PlayerApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginController().show());
    }
}


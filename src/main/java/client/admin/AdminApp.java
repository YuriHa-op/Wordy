package client.admin;

import client.admin.controller.AdminLoginController;

import javax.swing.SwingUtilities;

public class AdminApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLoginController().show());
    }
}


package client.ui.util;

import client.ui.MinecraftColors;
import client.ui.MinecraftFonts;
import client.ui.components.MinecraftButton;
import client.ui.components.MinecraftLabel;
import client.ui.components.MinecraftPanel;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

public final class MinecraftDialog {
    private MinecraftDialog() {
    }

    public static void showMessage(Component parent, String title, String message) {
        JDialog dialog = baseDialog(parent, title);

        MinecraftPanel panel = new MinecraftPanel("/textures/stone.png", 70);
        panel.setLayout(new BorderLayout(10, 10));

        MinecraftLabel label = new MinecraftLabel(message, MinecraftFonts.REGULAR, MinecraftColors.TEXT_WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(label, BorderLayout.CENTER);

        MinecraftButton okButton = new MinecraftButton("OK");
        okButton.addActionListener(e -> dialog.dispose());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(okButton);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public static boolean showConfirm(Component parent, String title, String message) {
        final boolean[] accepted = {false};
        JDialog dialog = baseDialog(parent, title);

        MinecraftPanel panel = new MinecraftPanel("/textures/stone.png", 70);
        panel.setLayout(new BorderLayout(10, 10));

        MinecraftLabel label = new MinecraftLabel(message, MinecraftFonts.REGULAR, MinecraftColors.TEXT_WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(label, BorderLayout.CENTER);

        MinecraftButton yesButton = new MinecraftButton("YES");
        MinecraftButton noButton = new MinecraftButton("NO");
        yesButton.addActionListener(e -> {
            accepted[0] = true;
            dialog.dispose();
        });
        noButton.addActionListener(e -> dialog.dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.setOpaque(false);
        bottom.add(yesButton);
        bottom.add(noButton);
        panel.add(bottom, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
        return accepted[0];
    }

    private static JDialog baseDialog(Component parent, String title) {
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setSize(460, 220);
        dialog.setLocationRelativeTo(parent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return dialog;
    }
}


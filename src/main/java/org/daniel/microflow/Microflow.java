package org.daniel.microflow;

import org.daniel.microflow.controller.OuterController;
import org.daniel.microflow.view.OuterView;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Microflow {

    public static final String VERSION = "1.6.1";

    public static void main(String[] args) {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (System.getProperty("os.name").startsWith("Mac")) {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Microflow");
                UIManager.put("TabbedPane.selected", Color.WHITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            OuterView view = new OuterView();
            OuterController controller = new OuterController(view);
            view.registerController(controller);
        });

        checkUpdates();

    }

    private static void checkUpdates() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/ortizdaniel/microflow/master/VERSION");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = reader.readLine();
            if (line != null && line.compareTo(VERSION) > 0) {
                int res = JOptionPane.showConfirmDialog(null,
                        "New update available. Would you like to be taken to the download page?", "Update",
                        JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().browse(new URI("https://github.com/ortizdaniel/microflow/releases"));
                }
            }
            reader.close();
        } catch (URISyntaxException | IOException e) {
            System.err.println("Could not check for updates.");
        }

    }
}

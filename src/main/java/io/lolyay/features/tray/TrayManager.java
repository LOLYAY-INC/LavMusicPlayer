package io.lolyay.features.tray;


import io.lolyay.panel.webserver.HttpServer;
import io.lolyay.utils.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class TrayManager {

    public static void setupTray() {
        // 1. Check if the SystemTray is supported on the current platform.
        if (!SystemTray.isSupported()) {
            Logger.err("SystemTray is not supported. Running without tray icon.");
            return;
        }

        // 2. Load the icon image from the resources folder.
        URL imageURL = TrayManager.class.getResource("/icon.png");
        if (imageURL == null) {
            Logger.err("Resource not found: icon.png");
            return;
        }
        Image iconImage;
        try {
            iconImage = ImageIO.read(imageURL);
        } catch (IOException e) {
            Logger.err("Error loading icon image: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // 3. Get the system tray instance.
        final SystemTray tray = SystemTray.getSystemTray();

        // 4. Create the popup menu for the tray icon.
        final PopupMenu popup = new PopupMenu();

        // Create a menu item for showing status (optional)
        MenuItem statusItem = new MenuItem("Status: Running");
        statusItem.setEnabled(false); // Make it not clickable

        // Create a menu item for exiting the application.
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.log("Exiting application from tray...");
                tray.remove(tray.getTrayIcons()[0]); // Cleanly remove the icon
                System.exit(0);
            }
        });

        MenuItem openPlayerItem = new MenuItem("Open Player");
        openPlayerItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.debug("Opening player from tray...");
                String url = "http://localhost:" + HttpServer.port;
                try {
                    Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to open player", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        MenuItem buffer = new MenuItem("_____");
        buffer.setEnabled(false); // Make it not clickable
        // Add items to the popup menu
        popup.add(statusItem);
        popup.addSeparator(); // A visual line separator
        popup.add(exitItem);
        popup.add(openPlayerItem);
        popup.add(buffer);

        // 5. Create the TrayIcon.
        final TrayIcon trayIcon = new TrayIcon(iconImage, "LavMusicPlayer", popup);
        // Let the OS resize the image if needed
        trayIcon.setImageAutoSize(true);

        try {
            // 6. Add the icon to the system tray.
            tray.add(trayIcon);
            Logger.success("Tray icon added successfully.");
        } catch (AWTException e) {
            Logger.err("TrayIcon could not be added.");
            e.printStackTrace();
        }
    }
}
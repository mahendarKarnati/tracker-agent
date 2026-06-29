
package com.tracker.service;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrayManager {

    private TrayIcon trayIcon;

    public void init(JFrame frame) {

        try {
        	if (trayIcon != null) {
        	    return;
        	}

            if (!SystemTray.isSupported()) {
                return;
            }

            SystemTray tray =
                    SystemTray.getSystemTray();

            Image image =
                    new BufferedImage(
                            16,
                            16,
                            BufferedImage.TYPE_INT_ARGB);

            PopupMenu menu =
                    new PopupMenu();

            MenuItem open =
                    new MenuItem("Open");

            MenuItem exit =
                    new MenuItem("Exit");

            open.addActionListener(e -> {

                tray.remove(trayIcon);

                trayIcon = null;

                frame.setVisible(true);

                frame.setExtendedState(
                        JFrame.NORMAL);

            });

            exit.addActionListener(e -> {

                if (trayIcon != null) {

                    tray.remove(trayIcon);
                }

                System.exit(0);

            });

            menu.add(open);
            menu.add(exit);

            trayIcon =
                    new TrayIcon(
                            image,
                            "Tracker Agent",
                            menu);

            trayIcon.setImageAutoSize(true);

            tray.add(trayIcon);

        } catch (Exception e) {

            log.error(e.getMessage());
        }
    }
    public void showMessage(
            String title,
            String message) {

        if (trayIcon != null) {

            trayIcon.displayMessage(
                    title,
                    message,
                    TrayIcon.MessageType.INFO);
        }
    }
}
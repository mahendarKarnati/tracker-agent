
package com.tracker.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.tracker.service.DeviceRegistrationService;
import com.tracker.service.LoginService;
import com.tracker.service.RegisterService;
import com.tracker.service.StartupService;
import com.tracker.service.TrayManager;

public class LoginFrame extends JFrame {

    private final LoginService loginService;
    private final DeviceRegistrationService deviceService;
    private final RegisterService registerService;
    private final StartupService startupService;
    private final TrayManager trayManager;

    private JTextField username;

    public LoginFrame(
            LoginService loginService,
            DeviceRegistrationService deviceService,
            RegisterService registerService,
            StartupService startupService,
            TrayManager trayManager) {

        this.loginService = loginService;
        this.deviceService = deviceService;
        this.registerService = registerService;
        this.startupService = startupService;
        this.trayManager = trayManager;

        setTitle("Tracker Agent");
        setSize(450, 260);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
        Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("TRACKER AGENT");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(titleFont);

        JLabel subtitle = new JLabel("Employee Activity Monitor");
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setFont(normalFont);

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(title);
        top.add(subtitle);

        root.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 5, 5));

        center.add(new JLabel("Username"));

        username = new JTextField();
        username.setFont(normalFont);
        center.add(username);

        root.add(center, BorderLayout.CENTER);

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        JPanel bottom = new JPanel();
        bottom.add(login);
        bottom.add(register);

        root.add(bottom, BorderLayout.SOUTH);

        add(root);

        register.addActionListener(e -> {

            RegisterFrame frame =
                    new RegisterFrame(
                            registerService,
                            loginService,
                            deviceService,
                            startupService,
                            trayManager);

            frame.setVisible(true);
        });

        login.addActionListener(e -> {

            boolean success =
                    loginService.login(
                            username.getText().trim());

            if (success) {

                startupService.enableStartup();

                deviceService.registerDevice();

                trayManager.init(this);

                setVisible(false);

                trayManager.showMessage(
                        "Tracker Agent",
                        "Tracking Started");
            }
        });
    }
}
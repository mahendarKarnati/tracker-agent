//package com.tracker.ui;
//
//import java.awt.GridLayout;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPasswordField;
//import javax.swing.JTextField;
//
//import com.tracker.service.DeviceRegistrationService;
//import com.tracker.service.LoginService;
//import com.tracker.service.RegisterService;
//import com.tracker.service.StartupService;
//import com.tracker.service.TrayManager;
//public class RegisterFrame extends JFrame {
//
//    private JTextField username;
//    private JPasswordField password;
//
//    public RegisterFrame(
//            RegisterService registerService,
//            LoginService loginService,
//            DeviceRegistrationService deviceService,
//            StartupService startupService,
//            TrayManager trayManager) {
//
//        setTitle("Register");
//
//        setSize(350, 220);
//
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        username = new JTextField();
////        password = new JPasswordField();
//
//        JButton register =
//                new JButton("Register");
//
//        register.addActionListener(e -> {
//        	 System.out.println("REGISTER BUTTON CLICKED");
//            boolean success =
//                    registerService.register(
//                            username.getText()
////                            ,
////                            new String(
////                                    password.getPassword())
//                            );
//
////            if(success) {
////
////                JOptionPane.showMessageDialog(
////                        this,
////                        "Registration Success");
////
////                dispose();
////                LoginFrame loginFrame =
////                        new LoginFrame(
////                                loginService,
////                                deviceService,
////                                registerService);
////
////                loginFrame.setVisible(true);
////
////            } 
//            if(success){
//
//                JOptionPane.showMessageDialog(
//                        this,
//                        "Registration Success");
//
//                dispose();
//
////                LoginFrame loginFrame =
////                        new LoginFrame(
////                                loginService,
////                                deviceService,
////                                registerService,
////                                startupService,
////                                trayManager);
////
////                loginFrame.setVisible(true);
//            }
//            
//            else {
//
//                JOptionPane.showMessageDialog(
//                        this,
//                        "Registration Failed");
//            }
//        });
//
//        setLayout(new GridLayout(3,2));
//
//        add(new JLabel("Username"));
//        add(username);
//
//
//        add(register);
//    }
//}


package com.tracker.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.tracker.service.DeviceRegistrationService;
import com.tracker.service.LoginService;
import com.tracker.service.RegisterService;
import com.tracker.service.StartupService;
import com.tracker.service.TrayManager;

public class RegisterFrame extends JFrame {

    private JTextField username;

    public RegisterFrame(
            RegisterService registerService,
            LoginService loginService,
            DeviceRegistrationService deviceService,
            StartupService startupService,
            TrayManager trayManager) {

        setTitle("Tracker Agent - Register");

        setSize(450, 260);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("CREATE ACCOUNT");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(titleFont);

        JLabel subtitle = new JLabel("Tracker Agent Registration");
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

        JButton register = new JButton("Register");

        JPanel bottom = new JPanel();
        bottom.add(register);

        root.add(bottom, BorderLayout.SOUTH);

        add(root);

        register.addActionListener(e -> {

            boolean success =
                    registerService.register(
                            username.getText().trim());

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration Successful");

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Registration Failed");
            }
        });
    }
}
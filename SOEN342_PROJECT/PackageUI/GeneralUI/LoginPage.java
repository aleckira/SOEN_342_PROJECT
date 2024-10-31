package SOEN342_PROJECT.PackageUI.GeneralUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    // Constructor for LoginPage
    public LoginPage() {
        // Set up the frame
        setTitle("Login Page");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // 4 rows, 1 column, with spacing

        // Create the label for login selection
        JLabel loginLabel = new JLabel("Select Login Type", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(loginLabel);

        // Create buttons for User, Instructor, and Admin logins
        JButton userLoginBtn = new JButton("Client Login");
        JButton instructorLoginBtn = new JButton("Instructor Login");
        JButton adminLoginBtn = new JButton("Admin Login");

        // Add action listeners for each button
        userLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //handleLogin("user"); // Call method for User login
                new LoginForm("client");
                dispose();

            }
        });

        instructorLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //handleLogin("instructor"); // Call method for Instructor login
                new LoginForm("instructor");
                dispose();
            }
        });

        adminLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm("admin");
                dispose();
            }
        });

        panel.add(userLoginBtn);
        panel.add(instructorLoginBtn);
        panel.add(adminLoginBtn);
        add(panel);
        // Add panel to the frame

        setVisible(true);
    }



}

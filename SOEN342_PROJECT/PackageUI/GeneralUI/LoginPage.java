package PackageUI.GeneralUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    // Constructor for LoginPage
    public LoginPage() {
        // Set up the frame
        setTitle("Login Page");
        setSize(400, 250);  // Increased height to accommodate extra button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, 1 column, with spacing

        // Create the label for login selection
        JLabel loginLabel = new JLabel("Select Login Type", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(loginLabel);

        // Create buttons for Client, Instructor, Admin, and Guardian logins
        JButton clientLoginBtn = new JButton("Client Login");
        JButton instructorLoginBtn = new JButton("Instructor Login");
        JButton adminLoginBtn = new JButton("Admin Login");
        JButton guardianLoginBtn = new JButton("Guardian Login");

        // Add action listeners for each button
        clientLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm("client");
                dispose();
            }
        });

        instructorLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        guardianLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginForm("guardian");
                dispose();
            }
        });

        // Add buttons to the panel
        panel.add(clientLoginBtn);
        panel.add(instructorLoginBtn);
        panel.add(adminLoginBtn);
        panel.add(guardianLoginBtn);

        // Add panel to the frame
        add(panel);

        setVisible(true);
    }
}

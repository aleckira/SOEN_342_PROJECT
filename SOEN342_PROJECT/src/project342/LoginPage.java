package src.project342;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        JButton userLoginBtn = new JButton("User Login");
        JButton instructorLoginBtn = new JButton("Instructor Login");
        JButton adminLoginBtn = new JButton("Admin Login");

        // Add action listeners for each button
        userLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin("user"); // Call method for User login
            }
        });

        instructorLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin("instructor"); // Call method for Instructor login
            }
        });

        adminLoginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin("admin"); // Call method for Admin login
            }
        });

        // Add buttons to the panel
        panel.add(userLoginBtn);
        panel.add(instructorLoginBtn);
        panel.add(adminLoginBtn);

        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    // Method to handle login for different user types
    private void handleLogin(String userType) {
        // Prompt for username and password
        String username = JOptionPane.showInputDialog(this, "Enter Username for " + userType + ":");
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.");
            return;
        }

        String password = JOptionPane.showInputDialog(this, "Enter Password for " + userType + ":");
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password is required.");
            return;
        }

        // Validate credentials based on user type
        if (authenticateUser(userType, username, password)) {
            JOptionPane.showMessageDialog(this, userType + " login successful!");
            // Proceed to the next page (e.g., Offering page)
            new Offering();
            dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid " + userType + " credentials. Try again.");
        }
    }

    // Method to authenticate user based on user type and credentials
    private boolean authenticateUser(String userType, String username, String password) {
        // Sample authentication logic using DbFunctions (database connectivity)
        DbFunctions db = new DbFunctions();
        Connection connection = db.connectToDb();
        String query = "SELECT * FROM " + userType + " WHERE username = ? AND password = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true; // Authentication successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Authentication failed
    }

}

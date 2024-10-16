package PackageUI;

import PackageActors.Admin;
import src.project342.InitialDbFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginForm extends JFrame {

    public LoginForm(String userType) {
        setTitle("Login as " + Character.toUpperCase(userType.charAt(0)) + userType.substring(1));
        setSize(450, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a panel for the form elements
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components
        JLabel firstFieldLabel = new JLabel("Name:");
        JLabel secondFieldLabel = new JLabel("Phone number:");
        JTextField firstField = new JTextField();
        JTextField secondField = new JTextField();
        if (userType.equals("admin")) {
            secondFieldLabel = new JLabel("Password:");
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(firstFieldLabel, gbc);

        firstField.setPreferredSize(new Dimension(150, 25)); // Set preferred size for username field
        gbc.gridx = 1;
        panel.add(firstField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(secondFieldLabel, gbc);

        secondField.setPreferredSize(new Dimension(150, 25)); // Set preferred size for password field
        gbc.gridx = 1;
        panel.add(secondField, gbc);

        // Create Account button
        JButton createAccountButton = new JButton("Create Account as " + Character.toUpperCase(userType.charAt(0)) + userType.substring(1));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST; // Align left
        if (!userType.equals("admin")) {
            panel.add(createAccountButton, gbc);
        }

        // Login button
        JButton loginButton = new JButton("Login");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST; // Align right
        panel.add(loginButton, gbc);

        // Add the panel to the frame
        add(panel);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(userType, "client")) {
                    new CreateAccountClient();
                }
                if (Objects.equals(userType, "instructor")) {
                    new CreateAccountInstructor();
                }
                dispose();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = firstField.getText();
                String secondFieldString = secondField.getText();
                if (userType.equals("admin")) {
                    Admin a = InitialDbFunctions.getAdmin(name, secondFieldString);
                    if (a != null) {
                        new AdminPage();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect login information. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (userType.equals("client")) {
                    new ClientPage();
                    dispose();

                }
                if (userType.equals("instructor")) {
                    new InstructorPage();
                    dispose();
                }
            }

        });

        setVisible(true); // Make the frame visible
    }
}

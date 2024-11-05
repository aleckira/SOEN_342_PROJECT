package PackageUI.GeneralUI;

import PackageUI.AdminUI.AdminPage;
import PackageUI.ClientUI.ClientPage;
import PackageUI.ClientUI.CreateAccountClient;
import PackageUI.GuardianUI.CreateAccountGuardian;
import PackageUI.GuardianUI.GuardianPage;
import PackageUI.InstructorsUI.CreateAccountInstructor;
import PackageUI.InstructorsUI.InstructorPage;
import Services.LoginService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginForm extends JFrame {

    public LoginForm(String userType) {
        setTitle("Login as " + Character.toUpperCase(userType.charAt(0)) + userType.substring(1));
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a panel for the form elements
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for components
        JLabel firstFieldLabel = new JLabel("Name:");
        JLabel secondFieldLabel = new JLabel("Phone number: (123456789012345)");
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

        JButton backButton = new JButton("Back");
        if (userType.equals("admin")) {
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST; // Align left
        }
        else {
            gbc.gridx = 0; // Set to the first column (left side)
            gbc.gridy = 3; // Set to the next row (below the other buttons)
            gbc.anchor = GridBagConstraints.WEST; // Align left
        }
        panel.add(backButton, gbc);
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
                if (Objects.equals(userType, "guardian")) {
                    new CreateAccountGuardian();
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
                    boolean loginSuccess = LoginService.loginAdmin(name, secondFieldString);
                    if (loginSuccess) {
                        new AdminPage();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect login information. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (userType.equals("client")) {
                    boolean loginSuccess = LoginService.loginClient(name, secondFieldString);
                    if (loginSuccess) {
                        new ClientPage();
                        dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Incorrect login information. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (userType.equals("instructor")) {
                    boolean loginSuccess = LoginService.loginInstructor(name, secondFieldString);
                    if (loginSuccess) {
                        new InstructorPage();
                        dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Incorrect login information. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (userType.equals("guardian")) {
                    boolean loginSuccess = LoginService. loginGuardian(name, secondFieldString);
                    if (loginSuccess) {
                        new GuardianPage();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect login information. Please try again.", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });
        setVisible(true);
    }
}

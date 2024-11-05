package PackageUI.GuardianUI;

import PackageActorsAndObjects.Guardian;
import Services.RegisterService;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountGuardian extends JFrame {
    public CreateAccountGuardian() {
        setTitle("Create Guardian Account");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField minorsField = new JTextField("Comma-separated minors' names");

        JButton registerButton = new JButton("Register");

        add(new JLabel("Name:"));
        add(nameField);

        add(new JLabel("Phone Number:"));
        add(phoneField);

        add(new JLabel("Age:"));
        add(ageField);

        add(new JLabel("Minors:"));
        add(minorsField);

        add(new JLabel());
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phoneNumber = phoneField.getText();
                int age = Integer.parseInt(ageField.getText());
                String minors = minorsField.getText();

                if (RegisterService.registerGuardian(name, phoneNumber, age, minors)) {
                    JOptionPane.showMessageDialog(CreateAccountGuardian.this, "Guardian account created successfully.");
                    // Set the current user as the registered guardian
                    Guardian guardian = new Guardian(name, phoneNumber, age);
                    UserSession.setCurrentUserRole("guardian", guardian);

                    // Open GuardianPage
                    new GuardianPage();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(CreateAccountGuardian.this, "Failed to create guardian account.");
                }
            }
        });

        setVisible(true);
    }
}

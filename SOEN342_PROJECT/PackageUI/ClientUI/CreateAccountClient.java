package PackageUI.ClientUI;


import PackageUI.GeneralUI.LoginForm;
import Services.RegisterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreateAccountClient extends JFrame {

    public CreateAccountClient() {
        setTitle("Create account as Instructor");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneNumberLabel = new JLabel("Phone number: (123456789012345)");
        JLabel ageLabel = new JLabel("Age: (18+)");

        JTextField nameField = new JTextField(15);
        JTextField phoneNumberField = new JTextField(15);
        JTextField ageField = new JTextField(15);


        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(phoneNumberLabel, gbc);

        gbc.gridx = 1;
        panel.add(phoneNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(ageLabel, gbc);

        gbc.gridx = 1;
        panel.add(ageField, gbc);


        JButton createAccountButton = new JButton("Create Account");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(createAccountButton, gbc);

        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(backButton, gbc);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String age = ageField.getText().trim();

                try {
                    boolean registerSuccess = RegisterService.registerClient(name, phoneNumber, age);
                    if (registerSuccess) {
                        new ClientPage();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(CreateAccountClient.this,
                                "Registration failed. Please check your input.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginForm("client");
            }
        });

        add(panel);
        setVisible(true);
    }
}

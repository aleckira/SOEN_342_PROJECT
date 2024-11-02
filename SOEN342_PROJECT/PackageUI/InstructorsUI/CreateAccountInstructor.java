package PackageUI.InstructorsUI;

import PackageActorsAndObjects.Instructor;
import PackageUI.GeneralUI.LoginForm;
import PackageUI.GeneralUI.LoginPage;
import Services.RegisterService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CreateAccountInstructor extends JFrame {

    public CreateAccountInstructor() {
        setTitle("Create account as Instructor");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneNumberLabel = new JLabel("Phone number:");
        JLabel specialtyLabel = new JLabel("Specialty:");
        JLabel cityLabel = new JLabel("Cities:");

        JTextField nameField = new JTextField(15);
        JTextField phoneNumberField = new JTextField(15);

        String[] specialties = {"Swimming", "Judo", "MMA", "Basketball", "Soccer", "Yoga"};
        JComboBox<String> specialtyDropdown = new JComboBox<>(specialties);

        JCheckBox montrealCheckBox = new JCheckBox("Montreal");
        JCheckBox lavalCheckBox = new JCheckBox("Laval");
        JCheckBox quebecCityCheckBox = new JCheckBox("Quebec City");
        JCheckBox gatineauCheckBox = new JCheckBox("Gatineau");
        JCheckBox sherbrookeCheckBox = new JCheckBox("Sherbrooke");
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
        panel.add(specialtyLabel, gbc);

        gbc.gridx = 1;
        panel.add(specialtyDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(cityLabel, gbc);

        gbc.gridx = 1;
        JPanel cityPanel = new JPanel();
        cityPanel.setLayout(new BoxLayout(cityPanel, BoxLayout.Y_AXIS));
        cityPanel.add(montrealCheckBox);
        cityPanel.add(lavalCheckBox);
        cityPanel.add(quebecCityCheckBox);
        cityPanel.add(gatineauCheckBox);
        cityPanel.add(sherbrookeCheckBox);

        panel.add(cityPanel, gbc);

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
                String specialty = (String) specialtyDropdown.getSelectedItem();

                // Collect selected cities
                StringBuilder cities = new StringBuilder();
                if (montrealCheckBox.isSelected()) cities.append("Montreal, ");
                if (lavalCheckBox.isSelected()) cities.append("Laval, ");
                if (quebecCityCheckBox.isSelected()) cities.append("QuebecCity, ");
                if (gatineauCheckBox.isSelected()) cities.append("Gatineau, ");
                if (sherbrookeCheckBox.isSelected()) cities.append("Sherbrooke, ");
                // Remove trailing comma and space
                if (cities.length() > 0) {
                    cities.setLength(cities.length() - 2);
                }

                try {
                    boolean registerSuccess = RegisterService.registerInstructor(name, phoneNumber, specialty, cities.toString());
                    if (registerSuccess) {
                        new InstructorPage();
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(CreateAccountInstructor.this,
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
                new LoginForm("instructor");
            }
        });

        add(panel);
        setVisible(true);
    }
}

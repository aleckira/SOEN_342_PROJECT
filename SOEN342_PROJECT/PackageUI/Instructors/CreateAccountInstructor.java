package PackageUI.Instructors;

import GeneralServices.RegisterService;
import PackageActors.Instructor;

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
        JLabel cityLabel = new JLabel("Cities (comma separated):");

        JTextField nameField = new JTextField(15);
        JTextField phoneNumberField = new JTextField(15);

        String[] specialties = {"Swimming", "Judo", "MMA", "Basketball", "Soccer", "Yoga"};
        JComboBox<String> specialtyDropdown = new JComboBox<>(specialties);

        JTextArea citiesArea = new JTextArea(3, 15);
        JScrollPane scrollPane = new JScrollPane(citiesArea);

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
        panel.add(scrollPane, gbc);

        JButton createAccountButton = new JButton("Create Account");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(createAccountButton, gbc);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phoneNumber = phoneNumberField.getText().trim();
                String specialty = (String) specialtyDropdown.getSelectedItem();
                String citiesInput = citiesArea.getText().trim();

                try {
                    Instructor i = RegisterService.registerInstructor(name, phoneNumber, specialty, citiesInput);
                    if (i != null) {
                        new InstructorPage(i);
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

        add(panel);
        setVisible(true);
    }
}

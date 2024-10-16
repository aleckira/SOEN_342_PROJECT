package PackageUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountInstructor extends JFrame {

    public CreateAccountInstructor() {
        setTitle("Create account as Instructor"); // Corrected title
        setSize(450, 400); // Increased height for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Name:");
        JLabel phoneNumberLabel = new JLabel("Phone number:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel specialtyLabel = new JLabel("Specialty:");
        JLabel cityLabel = new JLabel("Cities (comma separated):"); // Label for cities input

        JTextField nameField = new JTextField(15);
        JTextField phoneNumberField = new JTextField(15);
        JTextField ageField = new JTextField(15);

        // Dropdown for specialties
        String[] specialties = {"Sports", "Music", "Art", "Dance", "Yoga"};
        JComboBox<String> specialtyDropdown = new JComboBox<>(specialties);

        // Text area for cities input
        JTextArea citiesArea = new JTextArea(3, 15); // 3 rows, 15 columns
        JScrollPane scrollPane = new JScrollPane(citiesArea); // Make the text area scrollable

        // Add components to the panel
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
        gbc.gridy = 3; // Update gridy for specialty label
        panel.add(specialtyLabel, gbc);

        gbc.gridx = 1;
        panel.add(specialtyDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4; // Update gridy for city label
        panel.add(cityLabel, gbc);

        gbc.gridx = 1;
        panel.add(scrollPane, gbc); // Add scrollable text area for cities

        // Create Account button
        JButton createAccountButton = new JButton("Create Account");
        gbc.gridx = 0;
        gbc.gridy = 5; // Update gridy for the button
        gbc.gridwidth = 2; // Make button span across two columns
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(createAccountButton, gbc);

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (true) {
                    new ClientPage();
                    dispose();
                }
            }
        });

        add(panel);
        setVisible(true);
    }
}

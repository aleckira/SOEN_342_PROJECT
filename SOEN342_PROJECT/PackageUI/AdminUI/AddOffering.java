package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddOffering extends JFrame {

    private JTextField classTypeField, locationField, capacityField, startTimeField, endTimeField; // Fields for adding new offering
    private JRadioButton montrealRadioButton, lavalRadioButton, quebecCityRadioButton, gatineauRadioButton, sherbrookeRadioButton; // Radio buttons for cities
    private JPanel cityPanel; // Panel for city radio buttons
    Admin admin = (Admin) UserSession.getCurrentUser();

    public AddOffering() {
        // Set up the frame
        setTitle("Add an Offering");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400); // Increase height to allow for more space
        setLocationRelativeTo(null); // Center the frame

        // Use BoxLayout for the main panel
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
        addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        classTypeField = new JTextField();
        locationField = new JTextField();
        capacityField = new JTextField();
        startTimeField = new JTextField();
        endTimeField = new JTextField();

        // Radio buttons for cities
        montrealRadioButton = new JRadioButton("Montreal");
        lavalRadioButton = new JRadioButton("Laval");
        quebecCityRadioButton = new JRadioButton("Quebec City");
        gatineauRadioButton = new JRadioButton("Gatineau");
        sherbrookeRadioButton = new JRadioButton("Sherbrooke");
        // Add radio buttons to a ButtonGroup to make them mutually exclusive
        ButtonGroup cityGroup = new ButtonGroup();
        cityGroup.add(montrealRadioButton);
        cityGroup.add(lavalRadioButton);
        cityGroup.add(quebecCityRadioButton);
        cityGroup.add(gatineauRadioButton);
        cityGroup.add(sherbrookeRadioButton);

        // Add labels and fields to the panel
        addPanel.add(new JLabel("Class Type:"));
        addPanel.add(classTypeField);
        addPanel.add(new JLabel("Location:"));
        addPanel.add(locationField);
        addPanel.add(new JLabel("Cities:"));

        // Panel for the city radio buttons
        cityPanel = new JPanel();
        cityPanel.setLayout(new BoxLayout(cityPanel, BoxLayout.Y_AXIS));
        cityPanel.add(montrealRadioButton);
        cityPanel.add(lavalRadioButton);
        cityPanel.add(quebecCityRadioButton);
        cityPanel.add(gatineauRadioButton);
        cityPanel.add(sherbrookeRadioButton);

        // Wrap city panel in JScrollPane to make it scrollable
        JScrollPane cityScrollPane = new JScrollPane(cityPanel);
        cityScrollPane.setPreferredSize(new Dimension(200, 100)); // Set preferred size for scrolling
        cityScrollPane.setMaximumSize(new Dimension(200, 100)); // Limit the maximum size

        addPanel.add(cityScrollPane); // Add the scroll pane to the main panel
        addPanel.add(new JLabel("Capacity:"));
        addPanel.add(capacityField);
        addPanel.add(new JLabel("Start Time (YYYY-MM-DD HH:MM:SS):"));
        addPanel.add(startTimeField);
        addPanel.add(new JLabel("End Time (YYYY-MM-DD HH:MM:SS):"));
        addPanel.add(endTimeField);

        // Button panel for centering the button
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Offering");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOffering();
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the button in the panel

        // Add panels to the frame
        add(addPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH); // Place button panel at the bottom

        setVisible(true);
    }

    // Method to add offerings
    private void addOffering() {
        String classType = classTypeField.getText();
        String location = locationField.getText();
        String city = ""; // Variable to store the selected city

        // Collect selected city
        if (montrealRadioButton.isSelected()) city = "Montreal";
        else if (lavalRadioButton.isSelected()) city = "Laval";
        else if (quebecCityRadioButton.isSelected()) city = "Quebec City";
        else if (gatineauRadioButton.isSelected()) city = "Gatineau";
        else if (sherbrookeRadioButton.isSelected()) city = "Sherbrooke";
        else {
            JOptionPane.showMessageDialog(this, "Please select a city", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse the start and end times as Timestamps
        String startTimeInput = startTimeField.getText();
        String endTimeInput = endTimeField.getText();
        Timestamp startTime;
        Timestamp endTime;
        try {
            startTime = parseTimestamp(startTimeInput);
            endTime = parseTimestamp(endTimeInput);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (startTime.after(endTime) || startTime.equals(endTime)) {
            JOptionPane.showMessageDialog(this, "End time must be after start time.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (classType.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isUnique = admin.isNewOfferingUnique(city, location, startTime, endTime);
        if (!isUnique) {
            JOptionPane.showMessageDialog(this, "Offering not unique", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            boolean addOfferingSuccess = admin.enterNewOffering(classType, location, city, capacity, startTime, endTime);

            if (addOfferingSuccess) {
                JOptionPane.showMessageDialog(this, "Offering added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error adding offering", "Error", JOptionPane.ERROR_MESSAGE);
            }
            clearFields();
        }
    }

    // Helper method to parse the timestamp from a string
    private Timestamp parseTimestamp(String timestampStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date parsedDate = dateFormat.parse(timestampStr);
        return new Timestamp(parsedDate.getTime());
    }

    // Method to clear the input fields after adding an offering
    private void clearFields() {
        classTypeField.setText("");
        locationField.setText("");
        capacityField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");

        // Clear city radio buttons
        montrealRadioButton.setSelected(false);
        lavalRadioButton.setSelected(false);
        quebecCityRadioButton.setSelected(false);
        gatineauRadioButton.setSelected(false);
        sherbrookeRadioButton.setSelected(false);
    }
}

package PackageUI;

import src.project342.DbFunctions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Offering extends JFrame {

    private JTextArea offeringsDisplay; // To display current offerings
    private JTextField classTypeField, locationField, instructorField, timeSlotField; // Fields for adding new offering
    private DbFunctions db;

    public Offering() {
        db = new DbFunctions();

        // Set up the frame
        setTitle("Offerings");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Panel for displaying current offerings
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());

        offeringsDisplay = new JTextArea(10, 40);
        offeringsDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(offeringsDisplay);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Offerings");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayOfferings();
            }
        });
        displayPanel.add(refreshButton, BorderLayout.SOUTH);

        // Panel for adding new offerings
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(5, 2, 10, 10));

        classTypeField = new JTextField();
        locationField = new JTextField();
        instructorField = new JTextField();
        timeSlotField = new JTextField();

        addPanel.add(new JLabel("Class Type:"));
        addPanel.add(classTypeField);
        addPanel.add(new JLabel("Location:"));
        addPanel.add(locationField);
        addPanel.add(new JLabel("Instructor:"));
        addPanel.add(instructorField);
        addPanel.add(new JLabel("Time Slot:"));
        addPanel.add(timeSlotField);

        JButton addButton = new JButton("Add Offering");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOffering();
            }
        });

        addPanel.add(addButton);

        // Add panels to the frame
        add(displayPanel, BorderLayout.NORTH);
        add(addPanel, BorderLayout.SOUTH);

        // Initial display of offerings
        displayOfferings();

        setVisible(true);
    }

    // Method to display all offerings from the database
    private void displayOfferings() {
        offeringsDisplay.setText(""); // Clear the text area
        String query = "SELECT * FROM offerings"; // Query to fetch all offerings
        try (Connection connection = db.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String classType = rs.getString("class_type");
                String location = rs.getString("location");
                String instructor = rs.getString("instructor");
                String timeSlot = rs.getString("time_slot");

                offeringsDisplay.append("Class Type: " + classType + ", Location: " + location +
                        ", Instructor: " + instructor + ", Time Slot: " + timeSlot + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new offering to the database
    private void addOffering() {
        String classType = classTypeField.getText();
        String location = locationField.getText();
        String instructor = instructorField.getText();
        String timeSlot = timeSlotField.getText();

        if (classType.isEmpty() || location.isEmpty() || instructor.isEmpty() || timeSlot.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO offerings (class_type, location, instructor, time_slot) VALUES (?, ?, ?, ?)";

        try (Connection connection = db.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, classType);
            stmt.setString(2, location);
            stmt.setString(3, instructor);
            stmt.setString(4, timeSlot);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Offering added successfully!");
            displayOfferings(); // Refresh offerings display
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to clear the input fields after adding an offering
    private void clearFields() {
        classTypeField.setText("");
        locationField.setText("");
        instructorField.setText("");
        timeSlotField.setText("");
    }

}

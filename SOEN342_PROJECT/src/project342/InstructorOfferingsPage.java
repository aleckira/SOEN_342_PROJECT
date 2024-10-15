package src.project342;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InstructorOfferingsPage extends JFrame {

    private JList<String> offeringsList; // List to show available offerings
    private DefaultListModel<String> listModel; // Model for the offerings list
    private JButton takeOfferingBtn;

    public InstructorOfferingsPage() {
        // Frame setup
        setTitle("Instructor - Available Offerings");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Heading label
        JLabel heading = new JLabel("Available Offerings", JLabel.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(heading, BorderLayout.NORTH);

        // List model and JList for available offerings
        listModel = new DefaultListModel<>();
        offeringsList = new JList<>(listModel);
        panel.add(new JScrollPane(offeringsList), BorderLayout.CENTER);

        // Button to take an offering
        takeOfferingBtn = new JButton("Take Offering");
        panel.add(takeOfferingBtn, BorderLayout.SOUTH);

        // Load available offerings from the database
        loadAvailableOfferings();

        // Action listener for the "Take Offering" button
        takeOfferingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOffering = offeringsList.getSelectedValue();
                if (selectedOffering != null) {
                    takeOffering(selectedOffering);
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an offering to take.");
                }
            }
        });

        add(panel);
        setVisible(true);
    }

    // Method to load available offerings from the database
    private void loadAvailableOfferings() {
        try {
            Connection connection = DbFunctions.connectToDb();
            String query = "SELECT * FROM offerings WHERE instructor_id IS NULL"; // Offerings without assigned instructors
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String offering = rs.getString("class_type") + " at " + rs.getString("location") + " on " +
                        rs.getString("day") + " from " + rs.getString("start_time") + " to " +
                        rs.getString("end_time");
                listModel.addElement(offering);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading offerings.");
        }
    }

    // Method to take an offering
    private void takeOffering(String offering) {
        try {
            Connection connection = DbFunctions.connectToDb();
            String[] offeringDetails = offering.split(" at | on | from | to ");
            String classType = offeringDetails[0];
            String location = offeringDetails[1];
            String day = offeringDetails[2];
            String startTime = offeringDetails[3];

            // Get the instructor id (you can modify this to fetch the current instructor's id dynamically)
            int instructorId = 1; // Replace with dynamic value as needed

            // Update the offering in the database to assign the instructor
            String updateQuery = "UPDATE offerings SET instructor_id = ? WHERE class_type = ? AND location = ? AND day = ? AND start_time = ?";
            PreparedStatement stmt = connection.prepareStatement(updateQuery);
            stmt.setInt(1, instructorId);
            stmt.setString(2, classType);
            stmt.setString(3, location);
            stmt.setString(4, day);
            stmt.setString(5, startTime);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Successfully took on the offering!");
                listModel.removeElement(offering); // Remove the taken offering from the list
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error taking on offering.");
        }
    }
}

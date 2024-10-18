package PackageUI.GeneralUI;

import PackageActorsAndObjects.Instructor;
import Services.DbConnectionService;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

import static Services.DbConnectionService.connectToDb;

public class Offering extends JFrame {

    // Check user role
    String role = UserSession.getCurrentUserRole();
    Object user = UserSession.getCurrentUser();


    private JTable offeringsTable; // Table to display current offerings
    private DefaultTableModel tableModel;
    private JButton actionButton; // Button to perform actions based on selected row
    private JButton refreshButton; // Button to refresh offerings display

    public Offering() {
        // Set up the frame
        setTitle("Offerings");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the table model with column headers, and make it non-editable
        tableModel = new DefaultTableModel(new String[]{"id", "Class Type", "Location", "City", "Capacity", "Start Time", "End Time", "Instructor ID", "Client IDs"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing for all cells
            }
        };

        // Create the table and assign the model to it
        offeringsTable = new JTable(tableModel);
        offeringsTable.setFillsViewportHeight(true);

        // Add mouse listener for row selection
        offeringsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = offeringsTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    offeringsTable.setRowSelectionInterval(row, row); // Select the clicked row
                }
            }
        });

        // Style the table for a better appearance
        offeringsTable.setRowHeight(25); // Set row height
        offeringsTable.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set font for table
        offeringsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Set font for headers

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(offeringsTable);
        add(scrollPane, BorderLayout.CENTER); // Add scroll pane to frame

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // Use FlowLayout for the panel

        if ("admin".equals(role)) {
            // Button to perform actions based on selected row
            actionButton = new JButton("Edit");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        // Add your action logic here
                        JOptionPane.showMessageDialog(Offering.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(Offering.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel

            actionButton = new JButton("Delete");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        // Add your action logic here
                        JOptionPane.showMessageDialog(Offering.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(Offering.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }

        if ("instructor".equals(role)) {
            Instructor instructor = (Instructor) user;
            ArrayList<String> instructorCities = instructor.getCities();
            int id = instructor.getId();
            // Button to perform actions based on selected row
            actionButton = new JButton("Reserve");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        String cityFromRow = (String) tableModel.getValueAt(selectedRow, 3);
                        int instructorID = (int) tableModel.getValueAt(selectedRow, 7);
                        int offeringID = (int) tableModel.getValueAt(selectedRow, 0);

                        // Remove any curly braces and trim the location
                        String formattedLocation = cityFromRow.replaceAll("[{}]", "").trim();

                        // Check if any of the instructor's cities match the selected location
                        boolean cityMatch = false; // Initialize cityMatch as false
                        for (String city : instructorCities) {
                            // Remove any curly braces and trim the city
                            String formattedCity = city.replaceAll("[{}]", "").trim();
                            if (formattedCity.equalsIgnoreCase(formattedLocation)) {
                                cityMatch = true; // Set to true if a match is found
                                break; // Exit the loop as we found a match
                            }
                        }
                        if (cityMatch && instructorID == 0) {
                            // City matches; perform the reservation action
                            JOptionPane.showMessageDialog(Offering.this, "Reservation successful for class type: " + classType);

                            // Update the database with the instructor ID for this class
                            updateInstructorIdInDatabase(id, offeringID); // Call the method to update the database

                        } else if (!cityMatch && instructorID == 0) {
                            // No match found
                            JOptionPane.showMessageDialog(Offering.this, "No matching city found for reservation.");
                        } else if (instructorID != 0) {
                            JOptionPane.showMessageDialog(Offering.this, "Lesson already booked by an instructor.");
                        }
                    } else {
                        // No row selected
                        JOptionPane.showMessageDialog(Offering.this, "Please select a row to reserve.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }

        if ("client".equals(role)) {
            // Button to perform actions based on selected row
            actionButton = new JButton("Reserve");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        // Add your action logic here
                        JOptionPane.showMessageDialog(Offering.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(Offering.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }

        // Button to refresh the offerings display
        refreshButton = new JButton("Refresh Offerings");
        refreshButton.addActionListener(e -> displayOfferings());
        buttonPanel.add(refreshButton); // Add refresh button to the panel

        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom

        // Initial display of offerings
        displayOfferings();

        setVisible(true);
    }

    // Method to display all offerings from the database in the table
    private void displayOfferings() {
        // Clear the table before fetching new data
        tableModel.setRowCount(0);

        String query = "SELECT * FROM offerings"; // Query to fetch all offerings
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String classType = rs.getString("class_type");
                String location = rs.getString("location");
                String city = rs.getString("city");
                int capacity = rs.getInt("capacity");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                int instructorId = rs.getInt("instructor_id");

                // Assuming client_ids is an integer array (PostgreSQL integer[])
                Array clientIdsArray = rs.getArray("client_ids");
                Integer[] clientIds = (Integer[]) clientIdsArray.getArray(); // Convert Array to Integer[]
                String clientIdsStr = formatClientIds(clientIds);

                // Add the data to the table row by row
                tableModel.addRow(new Object[]{id, classType, location, city, capacity, startTime, endTime, instructorId, clientIdsStr});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to format client IDs as a comma-separated string
    private String formatClientIds(Integer[] clientIds) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < clientIds.length; i++) {
            sb.append(clientIds[i]);
            if (i < clientIds.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    // Method to update the instructor ID in the database
    private void updateInstructorIdInDatabase(int instructorId, int offeringId) {
        String query = "UPDATE offerings SET instructor_id = ? WHERE id = ?"; // Update based on offering ID
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, instructorId);
            stmt.setInt(2, offeringId); // Set the offering ID
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Instructor ID updated successfully.");
                System.out.println("Updating instructor ID: " + instructorId + " for offering ID: " + offeringId);
            } else {
                System.out.println("No matching offering found to update.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating instructor ID in the database.");
        }
    }
}

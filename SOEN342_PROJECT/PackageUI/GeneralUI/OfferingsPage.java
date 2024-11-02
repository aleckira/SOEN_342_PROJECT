package PackageUI.GeneralUI;

import PackageActorsAndObjects.Actor;
import PackageActorsAndObjects.Client;
import PackageActorsAndObjects.Instructor;
import PackageActorsAndObjects.Offering;
import PackageUI.AdminUI.AddOffering;
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
import java.util.Objects;

import static Services.DbConnectionService.connectToDb;

public class OfferingsPage extends JFrame {

    // Check user role
    String role = UserSession.getCurrentUserRole();
    Actor user = UserSession.getCurrentUser();

    private JTable offeringsTable; // Table to display current offerings
    private DefaultTableModel tableModel;
    private JButton actionButton; // Button to perform actions based on selected row
    private JButton refreshButton; // Button to refresh offerings display

    public OfferingsPage() {
        // Set up the frame
        setTitle("Offerings");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the table model with column headers based on user role
        tableModel = new DefaultTableModel(new String[]{"Id", "Class Type", "Location", "City", "Start Time", "End Time", "Capacity", "Availability Status", "Instructor ID"}, 0);


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
        actionButton = new JButton("View instructor name");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = offeringsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorIdString = (String) tableModel.getValueAt(selectedRow, 8);
                    if (Objects.equals(instructorIdString, "")) {
                        JOptionPane.showMessageDialog(OfferingsPage.this, "No instructor for this offering.");
                    }
                    else {
                        int instructorId = Integer.parseInt(instructorIdString);
                        String instructorName = Objects.requireNonNull(Instructor.fetchInstructorById(instructorId)).getName();
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Instructor name: " + instructorName);
                    }
                } else {
                    JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
                }
            }
        });
        buttonPanel.add(actionButton); // Add the action button to the panel
        // Create action button based on user role
        if ("admin".equals(role)) {
            actionButton = new JButton("Add");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AddOffering();
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
            actionButton = new JButton("Edit");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
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
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
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
                            boolean takeOfferingSuccess = ((Instructor) user).takeOffering(offeringID); // Call the method to update the database
                            if (!takeOfferingSuccess) {
                                JOptionPane.showMessageDialog(OfferingsPage.this, "Error updating instructor ID in the database.");
                            }
                            else {
                                JOptionPane.showMessageDialog(OfferingsPage.this, "Reservation successful for class type: " + classType);
                            }

                            // Update the database with the instructor ID for this class


                        } else if (!cityMatch && instructorID == 0) {
                            // No match found
                            JOptionPane.showMessageDialog(OfferingsPage.this, "No matching city found for reservation.");
                        } else if (instructorID != 0) {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Lesson already booked by an instructor.");
                        }
                    } else {
                        // No row selected
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row to reserve.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }

        if ("client".equals(role)) {
            Client client = (Client) user;
            int id = client.getId();

            // Button to perform actions based on selected row
            actionButton = new JButton("Reserve");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String status = (String) tableModel.getValueAt(selectedRow, 4);
                        int offeringID = (int) tableModel.getValueAt(selectedRow, 0);

                        if (Objects.equals(status, "Available")) {

                            // Reserve the lesson
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Lesson booked successfully.");

                            // Update the database with the client ID for this class
                            updateClientIdInDatabase(id, offeringID); // Call the method to update the database

                        } else if (Objects.equals(status, "Full")){

                            JOptionPane.showMessageDialog(OfferingsPage.this, "Lesson fully booked already.");

                        } else {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
                        }
                    }}
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
        // Clear the table before displaying new data
        tableModel.setRowCount(0);

        ArrayList<Offering> displayedOfferings = user.getOfferingsForViewing();

        // Iterate over each offering in the list and add it to the table model
        for (Offering offering : displayedOfferings) {
            int id = offering.getId();
            String classType = offering.getClassType();
            String location = offering.getLocation();
            String city = offering.getCity();
            int capacity = offering.getCapacity();
            String startTime = offering.getStartTime().toString();
            String endTime = offering.getEndTime().toString();
            boolean status = offering.isAvailable();
            String instructorId = String.valueOf(offering.getInstructorId());
            if (instructorId.equals("0")) {
                instructorId = "";
            }
            tableModel.addRow(new Object[]{id, classType, location, city, startTime, endTime, capacity, status, instructorId});

        }
    }

    // Method to update the client ID in the database
    //this should be rewritten into Client
    private void updateClientIdInDatabase(int clientId, int offeringId) {
        String query = "UPDATE offerings SET client_ids = array_append(client_ids, ?) WHERE id = ?"; // Use array_append for PostgreSQL
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, clientId);
            stmt.setInt(2, offeringId); // Set the offering ID
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Client ID added successfully.");
            } else {
                System.out.println("No matching offering found to update.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating client ID in the database.");
        }
    }
}

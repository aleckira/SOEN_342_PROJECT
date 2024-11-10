package PackageUI.GeneralUI;

import PackageActorsAndObjects.*;
import PackageUI.AdminUI.AddOfferingPage;
import PackageUI.AdminUI.AdminPage;
import PackageUI.AdminUI.EditOfferingPage;
import PackageUI.ClientUI.ClientPage;
import PackageUI.GuardianUI.GuardianPage;
import PackageUI.GuardianUI.SelectMinorPage;
import PackageUI.InstructorsUI.InstructorPage;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

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
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the table model with column headers based on user role
        tableModel = new DefaultTableModel(new String[]{"Id", "Class Type", "Location", "City", "Start Time", "End Time", "Availability", "Capacity", "Spots Left", "Instructor ID"}, 0);


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
        actionButton = new JButton("Back");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (role.equals("admin")) {
                    new AdminPage();
                }
                if (role.equals("client")) {
                    new ClientPage();
                }
                if (role.equals("instructor")) {
                    new InstructorPage();
                }
                if ("guardian".equals(role)) {
                    new GuardianPage(); // Ensure GuardianPage is defined
                }
            }
        });
        buttonPanel.add(actionButton); // Add the action button to the panel
        actionButton = new JButton("View instructor");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = offeringsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorIdString = (String) tableModel.getValueAt(selectedRow, 9);
                    if (Objects.equals(instructorIdString, "") || Objects.equals(instructorIdString, "N/A")) {
                        JOptionPane.showMessageDialog(OfferingsPage.this, "No instructor for this offering or offering not available.");
                    } else {
                        int instructorId = Integer.parseInt(instructorIdString);
                        Instructor instructor = Instructor.fetchInstructorById(instructorId);

                        if (instructor != null) {
                            String instructorName = instructor.getName();
                            String phoneNumber = instructor.getPhoneNumber();
                            ArrayList<String> cities = instructor.getCities();

                            String citiesString = String.join(", ", cities);

                            JOptionPane.showMessageDialog(
                                    OfferingsPage.this,
                                    "Instructor name: " + instructorName
                                            + "\nPhone number: " + phoneNumber
                                            + "\nCities: " + citiesString
                            );
                        } else {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Instructor not found.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
                }
            }

        });
        buttonPanel.add(actionButton); // Add the action button to the panel
        //for now just doing names, but we should be able to see everything probably
        // Create action button based on user role
        if ("admin".equals(role)) {
            Admin a = (Admin) user;
            actionButton = new JButton("Add");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new AddOfferingPage();
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
            actionButton = new JButton("Edit");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int offeringId = (int) tableModel.getValueAt(selectedRow, 0);
                        new EditOfferingPage(offeringId);
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
                        int offeringId = (int) tableModel.getValueAt(selectedRow, 0);
                        boolean deleteOfferingSuccess = a.deleteOffering(offeringId);
                        if (deleteOfferingSuccess) {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Offer deleted successfully.");
                        }
                        else {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Offer could not be deleted.");
                        }
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
            actionButton = new JButton("Take on an offering");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        String cityFromRow = (String) tableModel.getValueAt(selectedRow, 3);
                        String instructorIdString = (String) tableModel.getValueAt(selectedRow, 9);
                        String offeringIDString = tableModel.getValueAt(selectedRow, 0).toString();

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
                        if (classType.equals(instructor.getSpecialty()) && cityMatch && Objects.equals(instructorIdString, "") && !Objects.equals(instructorIdString, "N/A") && !Objects.equals(offeringIDString, "N/A")) {
                            int offeringID = Integer.parseInt(offeringIDString);
                            boolean takeOfferingSuccess = ((Instructor) user).takeOffering(offeringID); // Call the method to update the database
                            if (!takeOfferingSuccess) {
                                JOptionPane.showMessageDialog(OfferingsPage.this, "Error updating instructor ID in the database.");
                            }
                            else {
                                JOptionPane.showMessageDialog(OfferingsPage.this, "Success! You are now teaching this class!");
                            }

                            // Update the database with the instructor ID for this class


                        } else if (!cityMatch && instructorIdString.isEmpty()) {
                            // No match found
                            JOptionPane.showMessageDialog(OfferingsPage.this, "No matching city found for reservation.");
                        }
                        else if (!classType.equals(instructor.getSpecialty())) {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "You don't teach this type of class!.");
                        }
                        else if (!instructorIdString.isEmpty()) {
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

        if (role.equals("client")) {
            actionButton = new JButton("Reserve");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String availability = (String) tableModel.getValueAt(selectedRow, 6);
                        String startTimeStr = (String) tableModel.getValueAt(selectedRow, 4);
                        String endTimeStr = (String) tableModel.getValueAt(selectedRow, 5);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                        LocalDateTime startLocalDateTime = LocalDateTime.parse(startTimeStr, formatter);
                        LocalDateTime endLocalDateTime = LocalDateTime.parse(endTimeStr, formatter);

                        Timestamp startTime = Timestamp.valueOf(startLocalDateTime);
                        Timestamp endTime = Timestamp.valueOf(endLocalDateTime);

                        if (Objects.equals(availability, "Available")) {
                            int offeringID = (int) tableModel.getValueAt(selectedRow, 0);
                            if (Offering.hasOfferingBeenBookedByClient(offeringID, ((Client) user).getId())) {
                                JOptionPane.showMessageDialog(OfferingsPage.this, "You already booked this offering.");
                            } else {
                                if (((Client) user).isThereBookingTimeConflict(startTime, endTime)) {
                                    JOptionPane.showMessageDialog(OfferingsPage.this, "You already have booked an offering at this time and day.");
                                }
                                else {
                                    boolean makeBookingSuccess = ((Client) user).makeBooking(offeringID);
                                    if (!makeBookingSuccess) {
                                        JOptionPane.showMessageDialog(OfferingsPage.this, "Error adding booking to the database.");
                                    }
                                    JOptionPane.showMessageDialog(OfferingsPage.this, "Lesson booked successfully.");
                                }

                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Lesson fully booked already.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
                    }}
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }

        if (role.equals("guardian")) {
            actionButton = new JButton("Reserve");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = offeringsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String availability = (String) tableModel.getValueAt(selectedRow, 6);

                        if ("Available".equals(availability)) {
                            int offeringId = (int) tableModel.getValueAt(selectedRow, 0);
                            String startTime = (String) tableModel.getValueAt(selectedRow, 4);
                            String endTime = (String) tableModel.getValueAt(selectedRow, 5);
                            new SelectMinorPage(offeringId, startTime, endTime);
                        } else {
                            JOptionPane.showMessageDialog(OfferingsPage.this, "Lesson fully booked already.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(OfferingsPage.this, "Please select a row first.");
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
        // Clear the table before displaying new data
        tableModel.setRowCount(0);
        ArrayList<Offering> displayedOfferings = user.getOfferingsForViewing();

        // Iterate over each offering in the list and add it to the table model
        for (Offering offering : displayedOfferings) {
            int spotsLeft = offering.getSpotsLeft();
            String location = offering.getLocation();
            String city = offering.getCity();
            String startTime = offering.getStartTime().toString();
            String endTime = offering.getEndTime().toString();
            String availability = spotsLeft == 0 ? "Not Available" : "Available";
            //A client should only be able to see all Offering details for offerings that are not at capacity. They can see their bookings in another page
            //An instructor can't see Offering details for offerings that are at capacity, period.
            //An admin should only be able to see all Offering details for offerings that are not at capacity. They can see ALL bookings in another page
            if (spotsLeft == 0) {
                tableModel.addRow(new Object[]{"N/A", "N/A", location, city, startTime, endTime, availability, "N/A", "N/A", "N/A"});
            }
            else {
                int id = offering.getId();
                String classType = offering.getClassType();
                int capacity = offering.getCapacity();
                String instructorId = String.valueOf(offering.getInstructorId());
                if (instructorId.equals("0")) {
                    instructorId = "";
                }
                tableModel.addRow(new Object[]{id, classType, location, city, startTime, endTime, availability, capacity, spotsLeft, instructorId});
            }


        }
    }

}

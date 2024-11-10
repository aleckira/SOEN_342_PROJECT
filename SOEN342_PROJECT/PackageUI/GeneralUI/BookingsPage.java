package PackageUI.GeneralUI;

import PackageActorsAndObjects.*;
import PackageUI.AdminUI.AdminPage;
import PackageUI.ClientUI.ClientPage;
import PackageUI.GuardianUI.GuardianPage;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookingsPage extends JFrame {

    // Check user role
    String role = UserSession.getCurrentUserRole();
    Actor user = UserSession.getCurrentUser();

    private JTable bookingsTable; // Table to display current offerings
    private DefaultTableModel tableModel;
    private JButton actionButton; // Button to perform actions based on selected row
    private JButton refreshButton; // Button to refresh offerings display

    public BookingsPage() {
        // Set up the frame
        setTitle("Bookings");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the table model with column headers based on user role
        tableModel = new DefaultTableModel(new String[]{"Id", "Offering Id", "Class Type", "Location", "City", "Start Time", "End Time", "Capacity", "Spots Left", "Instructor ID"}, 0);


        // Create the table and assign the model to it
        bookingsTable = new JTable(tableModel);
        bookingsTable.setFillsViewportHeight(true);

        // Add mouse listener for row selection
        bookingsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookingsTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    bookingsTable.setRowSelectionInterval(row, row); // Select the clicked row
                }
            }
        });

        // Style the table for a better appearance
        bookingsTable.setRowHeight(25); // Set row height
        bookingsTable.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set font for table
        bookingsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Set font for headers

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
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
                if (role.equals("guardian")) {
                    new GuardianPage();
                }
            }
        });
        buttonPanel.add(actionButton); // Add the action button to the panel
        actionButton = new JButton("View instructor");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorIdString = (String) tableModel.getValueAt(selectedRow, 9);
                    if (Objects.equals(instructorIdString, "") || Objects.equals(instructorIdString, "N/A")) {
                        JOptionPane.showMessageDialog(BookingsPage.this, "No instructor for this offering.");
                    } else {
                        int instructorId = Integer.parseInt(instructorIdString);
                        Instructor instructor = Instructor.fetchInstructorById(instructorId);

                        if (instructor != null) {
                            String instructorName = instructor.getName();
                            String phoneNumber = instructor.getPhoneNumber();
                            ArrayList<String> cities = instructor.getCities();

                            String citiesString = String.join(", ", cities);

                            JOptionPane.showMessageDialog(
                                    BookingsPage.this,
                                    "Instructor name: " + instructorName
                                            + "\nPhone number: " + phoneNumber
                                            + "\nCities: " + citiesString
                            );
                        } else {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Instructor not found.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                }
            }

        });
        buttonPanel.add(actionButton); // Add the action button to the panel


        // Create action button based on user role
        if ("admin".equals(role)) {
            actionButton = new JButton("View client for booking");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                        Client c = Offering.fetchClientForBooking(bookingId);
                        if (c != null) {
                            JOptionPane.showMessageDialog(
                                    BookingsPage.this,
                                    "Client ID, Name, Phone Number, and Age: "
                                            + c.getId() + " - " + c.getName() + " - " + c.getPhoneNumber() + " - " + c.getAge()
                            );
                        }
                        else {
                            Minor m = Offering.fetchMinorForBooking(bookingId);
                            JOptionPane.showMessageDialog(
                                    BookingsPage.this,
                                    "Minor ID, Name, and Guardian Id: "
                                            + m.getId() + " - " + m.getName() + " - " + m.getGuardianId()
                            );
                        }
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }
                }
            });

            buttonPanel.add(actionButton); // Add the action button to the panel
            Admin a = (Admin) user;

            actionButton = new JButton("Delete");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                        boolean deleteBookingSuccess = a.deleteBooking(bookingId);
                        if (deleteBookingSuccess) {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Booking deleted.");
                        }
                        else {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Error deleting booking.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }


        if (role.equals("client")) {
            actionButton = new JButton("Cancel");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                        Client c = (Client) user;
                        boolean cancelSuccess = c.cancelBooking(bookingId);
                        if (cancelSuccess) {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Booking canceled.");
                        }
                        else {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Booking cancel failed.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
        }

        if (role.equals("guardian")) {
            actionButton = new JButton("Cancel");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                        Guardian guardian = (Guardian) user;

                        // Fetch the minor's name associated with this booking
                        String minorName = guardian.getMinorNameForBooking(bookingId); // This method should already be in the Guardian class

                        boolean cancelSuccess = guardian.cancelBooking(bookingId);
                        if (cancelSuccess) {
                            JOptionPane.showMessageDialog(BookingsPage.this,
                                    "Booking canceled for minor: " + (minorName != null ? minorName : "Unknown"));
                        } else {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Booking cancel failed.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel

            actionButton = new JButton("View Minor");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                        Guardian guardian = (Guardian) user;

                        // Fetch the minor's name associated with this booking
                        String minorName = guardian.getMinorNameForBooking(bookingId); // Implement this method in Guardian class

                        // Display the minor's name in a dialog
                        JOptionPane.showMessageDialog(BookingsPage.this,
                                "Minor associated with this booking: " + (minorName != null ? minorName : "Unknown"));
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the "View Minor" button to the panel
        }



        refreshButton = new JButton("Refresh Bookings");
        refreshButton.addActionListener(e -> displayBookings());
        buttonPanel.add(refreshButton); // Add refresh button to the panel

        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom

        // Initial display of offerings
        displayBookings();

        setVisible(true);
    }

    // Method to display all offerings from the database in the table
    private void displayBookings() {
        // Clear the table before displaying new data
        tableModel.setRowCount(0);

        Map<Offering, Integer> displayedBookings = new HashMap<>();
        if (role.equals("client")) {
            Client c = (Client) user;
            displayedBookings = c.getBookingsForViewing();
        } else if (role.equals("admin")) {
            Admin a = (Admin) user;
            displayedBookings = a.getAllBookingsForViewing();
        }
        else if (role.equals("guardian")) {
            Guardian g = (Guardian) user;
            displayedBookings = g.getBookingsForViewing();  // This method should fetch all bookings for minors
        }

        for (Map.Entry<Offering, Integer> entry : displayedBookings.entrySet()) {
            Offering offering = entry.getKey();
            int bookingId = entry.getValue();  // Retrieve bookingId from the map

            // Retrieve offering details
            String location = offering.getLocation();
            String city = offering.getCity();
            String startTime = offering.getStartTime().toString();
            String endTime = offering.getEndTime().toString();
            int spotsLeft = offering.getSpotsLeft();
            int offeringId = offering.getId();  // Original offering ID
            String classType = offering.getClassType();
            int capacity = offering.getCapacity();
            String instructorId = String.valueOf(offering.getInstructorId());

            // Add the row with bookingId as `id` and offeringId as `offeringId`
            tableModel.addRow(new Object[]{bookingId, offeringId, classType, location, city, startTime, endTime, capacity, spotsLeft, instructorId});
        }
    }


}

package PackageUI.GeneralUI;

import PackageActorsAndObjects.*;
import PackageUI.AdminUI.AddOffering;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
        tableModel = new DefaultTableModel(new String[]{"Id", "Class Type", "Location", "City", "Start Time", "End Time", "Capacity", "Spots Left", "Instructor ID"}, 0);


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
        actionButton = new JButton("View instructor name");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String instructorIdString = (String) tableModel.getValueAt(selectedRow, 8);
                    if (Objects.equals(instructorIdString, "")) {
                        JOptionPane.showMessageDialog(BookingsPage.this, "No instructor for this offering.");
                    }
                    else {
                        int instructorId = Integer.parseInt(instructorIdString);
                        String instructorName = Objects.requireNonNull(Instructor.fetchInstructorById(instructorId)).getName();
                        JOptionPane.showMessageDialog(BookingsPage.this, "Instructor name: " + instructorName);
                    }
                } else {
                    JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                }
            }
        });
        buttonPanel.add(actionButton); // Add the action button to the panel
        actionButton = new JButton("View clients");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookingsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int offeringId = (int) tableModel.getValueAt(selectedRow, 0);
                    ArrayList<Client> clients = Offering.fetchClientsForBooking(offeringId);
                    StringBuilder message = new StringBuilder("Client names: " );
                    for (Client client : clients) {
                        message.append("\n- ").append(client.getName());
                    }
                    JOptionPane.showMessageDialog(BookingsPage.this, message.toString());

                } else {
                    JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
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
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        JOptionPane.showMessageDialog(BookingsPage.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }
                }
            });
            buttonPanel.add(actionButton); // Add the action button to the panel

            actionButton = new JButton("Delete");
            actionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String classType = (String) tableModel.getValueAt(selectedRow, 1);
                        JOptionPane.showMessageDialog(BookingsPage.this, "Action performed on: " + classType);
                    } else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
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
                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Perform action based on the selected row
                        String availability = (String) tableModel.getValueAt(selectedRow, 6);

                        if (Objects.equals(availability, "Available")) {
                            int offeringID = (int) tableModel.getValueAt(selectedRow, 0);
                            if (Offering.hasOfferingBeenBookedByClient(offeringID, ((Client) user).getId())) {
                                JOptionPane.showMessageDialog(BookingsPage.this, "You already booked this offering.");
                            } else {
                                boolean makeBookingSuccess = ((Client) user).makeBooking(offeringID);
                                JOptionPane.showMessageDialog(BookingsPage.this, "Lesson booked successfully.");
                                if (!makeBookingSuccess) {
                                    JOptionPane.showMessageDialog(BookingsPage.this, "Error adding booking to the database.");
                                }
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(BookingsPage.this, "Lesson fully booked already.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(BookingsPage.this, "Please select a row first.");
                    }}
            });
            buttonPanel.add(actionButton); // Add the action button to the panel
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

        ArrayList<Offering> displayedBookings = new ArrayList<Offering>();
        if (role.equals("client")) {
            Client c = (Client) user;
            displayedBookings = c.getBookingsForViewing();
        }
        else if (role.equals("admin")) {
            Admin a = (Admin) user;
            displayedBookings = a.getAllBookingsForViewing();
        }

        for (Offering offering : displayedBookings) {
            String location = offering.getLocation();
            String city = offering.getCity();
            String startTime = offering.getStartTime().toString();
            String endTime = offering.getEndTime().toString();
            int spotsLeft = offering.getSpotsLeft();
            int id = offering.getId();
            String classType = offering.getClassType();
            int capacity = offering.getCapacity();
            String instructorId = String.valueOf(offering.getInstructorId());
            tableModel.addRow(new Object[]{id, classType, location, city, startTime, endTime, capacity, spotsLeft, instructorId});
        }
    }

}

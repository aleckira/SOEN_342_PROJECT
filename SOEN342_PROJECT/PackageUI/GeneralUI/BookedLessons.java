package PackageUI.GeneralUI;


import PackageActorsAndObjects.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Services.DbConnectionService.connectToDb;
import static Services.UserSession.getCurrentUser;
import static Services.UserSession.getCurrentUserRole;

//This should be separated into two, one in the AdminUI Package and another in ClientUI Package
//Clients get THEIR bookings, while Admins get ALL available bookings
//use the appropriate methods in Client and Admin to get them
public class BookedLessons extends JFrame {

    String role = getCurrentUserRole(); // Get the current user role
    Object user = getCurrentUser(); // Get the current user

    private JTable bookedLessonsTable; // Table to display booked lessons
    private BookedLessonsTableModel tableModel; // Use custom model

    public BookedLessons() {
        // Set up the frame
        setTitle("Booked Lessons");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the custom table model
        tableModel = new BookedLessonsTableModel(new String[]{"Class Type", "Location", "City", "Start Time", "End Time"}, 0);
        bookedLessonsTable = new JTable(tableModel);
        bookedLessonsTable.setFillsViewportHeight(true);

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(bookedLessonsTable);
        add(scrollPane, BorderLayout.CENTER); // Add scroll pane to frame

        // Initial display of booked lessons
        displayBookedLessons();

        setVisible(true);
    }

    // Method to display all booked lessons from the database based on user role
    private void displayBookedLessons() {
        // Clear the table before fetching new data
        tableModel.setRowCount(0);

        // Check if the user is a client or an admin
        if (role.equals("client")) {
            Client client = (Client) user;
            int id = client.getId();

            String query = "SELECT class_type, location, city, start_time, end_time " +
                    "FROM offerings WHERE client_ids @> ?"; // Use PostgreSQL array containment operator
            try (Connection connection = connectToDb();
                 PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setArray(1, connection.createArrayOf("integer", new Integer[]{id})); // Set the client ID as an array

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String classType = rs.getString("class_type");
                    String location = rs.getString("location");
                    String city = rs.getString("city");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");

                    // Add the data to the table row by row
                    tableModel.addRow(new Object[]{classType, location, city, startTime, endTime});
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching booked lessons from the database.");
            }
        } else if (role.equals("admin")) {
            // For admin: select all offerings with at least one client ID
            String query = "SELECT class_type, location, city, start_time, end_time " +
                    "FROM offerings WHERE array_length(client_ids, 1) > 0"; // Check for non-empty client_ids array
            try (Connection connection = connectToDb();
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String classType = rs.getString("class_type");
                    String location = rs.getString("location");
                    String city = rs.getString("city");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");

                    // Add the data to the table row by row
                    tableModel.addRow(new Object[]{classType, location, city, startTime, endTime});
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching booked lessons from the database.");
            }
        }
    }

    // Custom table model to make the table uneditable
    private static class BookedLessonsTableModel extends DefaultTableModel {
        public BookedLessonsTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Prevent editing of cells
        }
    }
}

package PackageUI.GeneralUI;

import Services.DbConnectionService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Accounts extends JFrame {

    private JTable instructorsTable; // Table to display instructors
    private JTable clientsTable; // Table to display clients
    private DefaultTableModel instructorsTableModel; // Model for instructors table
    private DefaultTableModel clientsTableModel; // Model for clients table
    private JButton deleteButton; // Button to delete selected row

    public Accounts() {
        // Set up the frame
        setTitle("Accounts");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // Use BoxLayout to remove gaps

        // Panel for Instructors
        JPanel instructorsPanel = new JPanel();
        instructorsPanel.setLayout(new BorderLayout()); // Use BorderLayout for the panel

        // Title for Instructors Table
        JLabel instructorsTitle = new JLabel("Instructors", JLabel.CENTER);
        instructorsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        instructorsPanel.add(instructorsTitle, BorderLayout.NORTH); // Add title at the top

        // Create the instructors table model with appropriate column headers
        instructorsTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone Number", "Specialty", "Cities"}, 0);
        instructorsTable = new JTable(instructorsTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing of cells
            }
        };

        // Make rows selectable
        instructorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instructorsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    clientsTable.clearSelection(); // Deselect clients table if instructors table is selected
                }
            }
        });

        // Scroll pane for the instructors table
        JScrollPane instructorsScrollPane = new JScrollPane(instructorsTable);
        instructorsPanel.add(instructorsScrollPane, BorderLayout.CENTER); // Add table scroll pane

        // Panel for Clients
        JPanel clientsPanel = new JPanel();
        clientsPanel.setLayout(new BorderLayout()); // Use BorderLayout for the panel

        // Title for Clients Table
        JLabel clientsTitle = new JLabel("Clients", JLabel.CENTER);
        clientsTitle.setFont(new Font("Arial", Font.BOLD, 16));
        clientsPanel.add(clientsTitle, BorderLayout.NORTH); // Add title at the top

        // Create the clients table model with appropriate column headers
        clientsTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone Number", "Age", "Booking IDs"}, 0);
        clientsTable = new JTable(clientsTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing of cells
            }
        };

        // Make rows selectable
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        clientsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    instructorsTable.clearSelection(); // Deselect instructors table if clients table is selected
                }
            }
        });

        // Scroll pane for the clients table
        JScrollPane clientsScrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(clientsScrollPane, BorderLayout.CENTER); // Add table scroll pane

        // Create a panel for the delete button
        JPanel buttonPanel = new JPanel();
        deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedRow()); // Action for delete button
        buttonPanel.add(deleteButton); // Add button to the panel

        // Add panels to the frame
        add(instructorsPanel); // Add instructors panel to the frame
        add(clientsPanel); // Add clients panel to the frame
        add(buttonPanel); // Add button panel to the frame

        // Initial display of accounts
        displayAccounts();

        setVisible(true);
    }

    // Method to display all instructors and clients from the database
    private void displayAccounts() {
        // Clear the tables before fetching new data
        instructorsTableModel.setRowCount(0);
        clientsTableModel.setRowCount(0);

        String instructorQuery = "SELECT id, name, phone_number, specialty, cities FROM instructors"; // Query for instructors
        String clientQuery = "SELECT id, name, phone_number, age, booking_ids FROM clients"; // Query for clients

        try (Connection connection = DbConnectionService.connectToDb();
             Statement stmt = connection.createStatement()) {

            // Fetch and add instructors to the instructors table
            try (ResultSet rs = stmt.executeQuery(instructorQuery)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String phoneNumber = rs.getString("phone_number");
                    String specialty = rs.getString("specialty");
                    Array citiesArray = rs.getArray("cities");
                    String cities = formatArray(citiesArray); // Convert array to string

                    instructorsTableModel.addRow(new Object[]{id, name, phoneNumber, specialty, cities});
                }
            }

            // Fetch and add clients to the clients table
            try (ResultSet rs = stmt.executeQuery(clientQuery)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String phoneNumber = rs.getString("phone_number");
                    int age = rs.getInt("age");
                    Array bookingIdsArray = rs.getArray("booking_ids");
                    String bookingIds = formatArray(bookingIdsArray); // Convert array to string

                    clientsTableModel.addRow(new Object[]{id, name, phoneNumber, age, bookingIds});
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching accounts from the database.");
        }
    }

    // Method to delete the selected row
    private void deleteSelectedRow() {
        // Get selected instructor
        int selectedInstructorRow = instructorsTable.getSelectedRow();
        if (selectedInstructorRow != -1) {
            int instructorId = (int) instructorsTableModel.getValueAt(selectedInstructorRow, 0);
            JOptionPane.showMessageDialog(this, "Delete action for Instructor ID: " + instructorId);
            return; // Exit after displaying the message for the instructor
        }

        // Get selected client
        int selectedClientRow = clientsTable.getSelectedRow();
        if (selectedClientRow != -1) {
            int clientId = (int) clientsTableModel.getValueAt(selectedClientRow, 0);
            JOptionPane.showMessageDialog(this, "Delete action for Client ID: " + clientId);
        }
    }

    // Helper method to format PostgreSQL array to a comma-separated string
    private String formatArray(Array array) throws SQLException {
        if (array != null) {
            StringBuilder sb = new StringBuilder();
            Object[] arrayData = (Object[]) array.getArray(); // Convert to Object array
            for (int i = 0; i < arrayData.length; i++) {
                sb.append(arrayData[i].toString());
                if (i < arrayData.length - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
        return ""; // Return empty string if the array is null
    }

}

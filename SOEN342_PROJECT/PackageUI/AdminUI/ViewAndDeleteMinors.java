package PackageUI.AdminUI;

import Services.DbConnectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewAndDeleteMinors extends JFrame {

    private JTable minorsTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton backButton;

    public ViewAndDeleteMinors() {
        setTitle("View and Delete Minors");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Guardian ID"}, 0);
        minorsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(minorsTable);

        // Load minors data into table
        loadMinors();

        // Delete button setup
        deleteButton = new JButton("Delete Selected Minor");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = minorsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int minorId = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteMinorAndRelatedBookings(minorId);
                    loadMinors(); // Refresh the table after deletion
                } else {
                    JOptionPane.showMessageDialog(ViewAndDeleteMinors.this, "Please select a minor to delete.");
                }
            }
        });

        // Back button setup
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminPage(); // Go back to AdminPage
            }
        });

        // Layout setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(deleteButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadMinors() {
        // Clear existing rows in table model
        tableModel.setRowCount(0);

        String query = "SELECT id, name, guardian_id FROM minors";
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int guardianId = rs.getInt("guardian_id");
                tableModel.addRow(new Object[]{id, name, guardianId});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading minors.");
        }
    }

    private void deleteMinorAndRelatedBookings(int minorId) {
        String deleteBookingsQuery = "DELETE FROM bookings WHERE minor_id = ?";
        String deleteMinorQuery = "DELETE FROM minors WHERE id = ?";

        try (Connection connection = DbConnectionService.connectToDb()) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement deleteBookingsStmt = connection.prepareStatement(deleteBookingsQuery);
                 PreparedStatement deleteMinorStmt = connection.prepareStatement(deleteMinorQuery)) {

                // Delete associated bookings
                deleteBookingsStmt.setInt(1, minorId);
                deleteBookingsStmt.executeUpdate();

                // Delete the minor
                deleteMinorStmt.setInt(1, minorId);
                deleteMinorStmt.executeUpdate();

                connection.commit(); // Commit the transaction
                JOptionPane.showMessageDialog(this, "Minor and all associated bookings deleted successfully.");
            } catch (SQLException ex) {
                connection.rollback(); // Rollback transaction on failure
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting minor and associated bookings.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

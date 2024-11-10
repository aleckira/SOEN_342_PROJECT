package PackageUI.AdminUI;

import PackageActorsAndObjects.Guardian;
import Services.DbConnectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewAndDeleteGuardians extends JFrame {

    private JTable guardiansTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton backButton;  // Add back button

    public ViewAndDeleteGuardians() {
        setTitle("View and Delete Guardians");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone Number", "Age"}, 0);
        guardiansTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(guardiansTable);

        // Load guardians data into table
        loadGuardians();

        // Delete button setup
        deleteButton = new JButton("Delete Selected Guardian");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = guardiansTable.getSelectedRow();
                if (selectedRow != -1) {
                    int guardianId = (int) tableModel.getValueAt(selectedRow, 0);
                    deleteGuardianAndRelatedData(guardianId);
                    loadGuardians(); // Refresh the table after deletion
                } else {
                    JOptionPane.showMessageDialog(ViewAndDeleteGuardians.this, "Please select a guardian to delete.");
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

    private void loadGuardians() {
        // Clear existing rows in table model
        tableModel.setRowCount(0);

        String query = "SELECT id, name, phone_number, age FROM guardians";
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                int age = rs.getInt("age");
                tableModel.addRow(new Object[]{id, name, phoneNumber, age});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading guardians.");
        }
    }

    private void deleteGuardianAndRelatedData(int guardianId) {
        String deleteBookingsQuery = "DELETE FROM bookings WHERE minor_id IN (SELECT id FROM minors WHERE guardian_id = ?)";
        String deleteMinorsQuery = "DELETE FROM minors WHERE guardian_id = ?";
        String deleteGuardianQuery = "DELETE FROM guardians WHERE id = ?";

        try (Connection connection = DbConnectionService.connectToDb()) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement deleteBookingsStmt = connection.prepareStatement(deleteBookingsQuery);
                 PreparedStatement deleteMinorsStmt = connection.prepareStatement(deleteMinorsQuery);
                 PreparedStatement deleteGuardianStmt = connection.prepareStatement(deleteGuardianQuery)) {

                // Delete associated bookings
                deleteBookingsStmt.setInt(1, guardianId);
                deleteBookingsStmt.executeUpdate();

                // Delete associated minors
                deleteMinorsStmt.setInt(1, guardianId);
                deleteMinorsStmt.executeUpdate();

                // Delete the guardian
                deleteGuardianStmt.setInt(1, guardianId);
                deleteGuardianStmt.executeUpdate();

                connection.commit(); // Commit the transaction
                JOptionPane.showMessageDialog(this, "Guardian and all associated data deleted successfully.");
            } catch (SQLException ex) {
                connection.rollback(); // Rollback transaction on failure
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting guardian and associated data.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

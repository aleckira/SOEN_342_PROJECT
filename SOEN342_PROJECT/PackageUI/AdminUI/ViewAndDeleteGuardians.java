package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import PackageActorsAndObjects.Guardian;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewAndDeleteGuardians extends JFrame {

    private JTable guardiansTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton backButton;  // Add back button
    Admin user = (Admin) UserSession.getCurrentUser();
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
        displayGuardians();

        // Delete button setup
        deleteButton = new JButton("Delete Selected Guardian");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = guardiansTable.getSelectedRow();
                if (selectedRow != -1) {
                    int guardianId = (int) tableModel.getValueAt(selectedRow, 0);
                    boolean deleteGuardianSuccess = user.deleteGuardian(guardianId);
                    if (deleteGuardianSuccess) {
                        JOptionPane.showMessageDialog(ViewAndDeleteGuardians.this, "Guardian deleted.");
                    }
                    else {
                        JOptionPane.showMessageDialog(ViewAndDeleteGuardians.this, "Failed to delete guardian.");
                    }
                    displayGuardians(); // Refresh the table after deletion
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


    private void displayGuardians() {
        // Clear the table before displaying new data
        tableModel.setRowCount(0);

        ArrayList<Guardian> displayedGuardians = user.getAllGuardiansForViewing();

        // Iterate over each offering in the list and add it to the table model
        for (Guardian guardian : displayedGuardians) {
            int id = guardian.getId();
            String name = guardian.getName();
            String phoneNumber = guardian.getPhoneNumber();
            int age = guardian.getAge();
            tableModel.addRow(new Object[]{id, name, phoneNumber, age});
        }
    }


}

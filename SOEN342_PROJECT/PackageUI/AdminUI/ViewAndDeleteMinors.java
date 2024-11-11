package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import PackageActorsAndObjects.Minor;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewAndDeleteMinors extends JFrame {

    private JTable minorsTable;
    private DefaultTableModel tableModel;
    private JButton deleteButton;
    private JButton backButton;
    Admin user = (Admin) UserSession.getCurrentUser();
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
        displayMinors();

        // Delete button setup
        deleteButton = new JButton("Delete Selected Minor");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = minorsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int minorId = (int) tableModel.getValueAt(selectedRow, 0);
                    boolean deleteMinorSuccess = user.deleteMinor(minorId);
                    if (deleteMinorSuccess) {
                        JOptionPane.showMessageDialog(ViewAndDeleteMinors.this, "Successfully deleted Minor .");
                    }
                    else {
                        JOptionPane.showMessageDialog(ViewAndDeleteMinors.this, "Failed to delete Minor .");
                    }
                    displayMinors(); // Refresh the table after deletion
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


    private void displayMinors() {
        // Clear the table before displaying new data
        tableModel.setRowCount(0);

        ArrayList<Minor> displayedMinors = user.getAllMinorsForViewing();

        // Iterate over each offering in the list and add it to the table model
        for (Minor minor : displayedMinors) {
            int id = minor.getId();
            String name = minor.getName();
            int guardianId = minor.getGuardianId();
            tableModel.addRow(new Object[]{id, name, guardianId});
        }
    }


}

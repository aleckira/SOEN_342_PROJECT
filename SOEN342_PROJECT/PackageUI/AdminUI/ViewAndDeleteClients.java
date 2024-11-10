package PackageUI.AdminUI;

import PackageActorsAndObjects.*;
import Services.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


public class ViewAndDeleteClients extends JFrame {

    Admin user = (Admin)UserSession.getCurrentUser();

    private JTable clientsTable; // Table to display current offerings
    private DefaultTableModel tableModel;
    private JButton actionButton; // Button to perform actions based on selected row
    private JButton refreshButton; // Button to refresh offerings display

    public ViewAndDeleteClients() {
        // Set up the frame
        setTitle("Clients");
        setSize(1000, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the table model with column headers based on user role
        tableModel = new DefaultTableModel(new String[]{"Id", "Name", "Phone Number"}, 0);


        // Create the table and assign the model to it
        clientsTable = new JTable(tableModel);
        clientsTable.setFillsViewportHeight(true);

        // Add mouse listener for row selection
        clientsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = clientsTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    clientsTable.setRowSelectionInterval(row, row); // Select the clicked row
                }
            }
        });

        // Style the table for a better appearance
        clientsTable.setRowHeight(25); // Set row height
        clientsTable.setFont(new Font("SansSerif", Font.PLAIN, 14)); // Set font for table
        clientsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Set font for headers

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(clientsTable);
        add(scrollPane, BorderLayout.CENTER); // Add scroll pane to frame

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        actionButton = new JButton("Back");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminPage();
            }
        });
        buttonPanel.add(actionButton); // Add the action button to the panel
        actionButton = new JButton("Delete Client");
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int clientId = (int) tableModel.getValueAt(selectedRow, 0);
                    boolean deleteSuccess = user.deleteClient(clientId);
                    if (deleteSuccess) {
                        JOptionPane.showMessageDialog(ViewAndDeleteClients.this, "Deleted Client");
                    }
                    else {
                        JOptionPane.showMessageDialog(ViewAndDeleteClients.this, "Client not found");
                    }

                } else {
                    JOptionPane.showMessageDialog(ViewAndDeleteClients.this, "Please select a row first.");
                }
            }
        });
        buttonPanel.add(actionButton); // Add the action button to the panel

        // Button to refresh the offerings display
        refreshButton = new JButton("Refresh Clients");
        refreshButton.addActionListener(e -> displayClients());
        buttonPanel.add(refreshButton); // Add refresh button to the panel

        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the bottom

        // Initial display of offerings
        displayClients();

        setVisible(true);
    }

    // Method to display all offerings from the database in the table
    private void displayClients() {
        // Clear the table before displaying new data
        tableModel.setRowCount(0);

        ArrayList<Client> displayedClients = user.getAllClientsForViewing();

        // Iterate over each offering in the list and add it to the table model
        for (Client client : displayedClients) {
            int id = client.getId();
            String name = client.getName();
            String phoneNumber = client.getPhoneNumber();
            tableModel.addRow(new Object[]{id, name, phoneNumber});
        }
    }

}

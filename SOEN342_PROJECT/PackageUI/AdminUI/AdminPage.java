package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import PackageUI.GeneralUI.BookingsPage;
import PackageUI.GeneralUI.LoginPage;
import PackageUI.GeneralUI.OfferingsPage;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPage extends JFrame {

    public AdminPage() {
        Admin a = (Admin) UserSession.getCurrentUser();

        // Set up the frame
        setTitle("Admin Page");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10)); // 8 rows, 1 column, with spacing

        // Create the label for options selection
        JLabel loginLabel = new JLabel("Select among the following options", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(loginLabel);

        // Create buttons for each action
        JButton viewOfferingsBtn = new JButton("View, add, edit and delete offerings");
        JButton viewBookingsBtn = new JButton("View, and delete all bookings");
        JButton deleteInstructorBtn = new JButton("View and delete instructors");
        JButton deleteClientBtn = new JButton("View and delete clients");
        JButton deleteGuardianBtn = new JButton("View and delete guardians");
        JButton deleteMinorBtn = new JButton("View and delete minors");
        JButton logoutBtn = new JButton("Logout");

        // Add action listeners for each button
        viewOfferingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new OfferingsPage();
            }
        });

        viewBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new BookingsPage();
            }
        });

        deleteInstructorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewAndDeleteInstructors();
            }
        });

        deleteClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewAndDeleteClients();
            }
        });

        deleteGuardianBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewAndDeleteGuardians();
            }
        });

        deleteMinorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ViewAndDeleteMinors();
            }
        });

        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserSession.setCurrentUserRole(null, null);
                new LoginPage();
            }
        });

        // Add buttons to the panel
        panel.add(viewOfferingsBtn);
        panel.add(viewBookingsBtn);
        panel.add(deleteInstructorBtn);
        panel.add(deleteClientBtn);
        panel.add(deleteGuardianBtn);
        panel.add(deleteMinorBtn);
        panel.add(logoutBtn);

        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }
}

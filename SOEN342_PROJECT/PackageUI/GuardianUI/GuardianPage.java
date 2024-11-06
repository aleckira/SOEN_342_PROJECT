package PackageUI.GuardianUI;

import PackageActorsAndObjects.Guardian;
import PackageUI.GeneralUI.BookingsPage;
import PackageUI.GeneralUI.LoginPage;
import PackageUI.GeneralUI.OfferingsPage;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuardianPage extends JFrame {
    private Guardian guardian;

    public GuardianPage() {
        // Set up the frame
        setTitle("Guardian Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Retrieve the current guardian from the UserSession
        guardian = (Guardian) UserSession.getCurrentUser();

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // 4 rows, 1 column, with spacing

        // Create the label for selection
        JLabel selectionLabel = new JLabel("Select among the following options", JLabel.CENTER);
        selectionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(selectionLabel);

        // Create buttons for viewing offerings, viewing bookings, and logout
        JButton viewOfferingsButton = new JButton("View available offerings and make bookings");
        JButton viewBookingsButton = new JButton("View your minors' bookings, cancel bookings");
        JButton logoutButton = new JButton("Logout");

        // Add action listeners for each button
        viewOfferingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open offerings page for guardian
                dispose();
                new OfferingsPage();
            }
        });

        viewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open bookings page for minors
                dispose();
                new BookingsPage();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserSession.setCurrentUserRole(null, null);
                new LoginPage(); // Redirect to login page
            }
        });

        // Add buttons to the panel
        panel.add(viewOfferingsButton);
        panel.add(viewBookingsButton);
        panel.add(logoutButton);

        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }
}

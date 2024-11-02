package PackageUI.ClientUI;



import PackageActorsAndObjects.Client;
import PackageUI.GeneralUI.BookedLessons;
import PackageUI.GeneralUI.OfferingsPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ClientPage extends JFrame {

    // Constructor for LoginPage
    public ClientPage(Client c) {
        // Set up the frame
        setTitle("Client Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // 4 rows, 1 column, with spacing

        // Create the label for login selection
        JLabel loginLabel = new JLabel("Select among the following options", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(loginLabel);

        // Create buttons for User, Instructor, and Admin logins
        JButton viewAllAvailableOfferings = new JButton("View available offerings and make bookings");
        JButton viewBookingsBtn = new JButton("View your bookings");

        // Add action listeners for each button
        viewAllAvailableOfferings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("client");
                dispose();
                new OfferingsPage(); // Create and display offerings page

            }
        });

        viewBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("instructor");
                dispose();
                new BookedLessons(); // Create and display booked lessons
            }
        });
        viewBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("instructor");
                dispose();
                //new BookedLessons(); refactor this into the below
                new ViewClientBookings();
            }
        });


        // Add buttons to the panel
        panel.add(viewAllAvailableOfferings);
        panel.add(viewBookingsBtn);
        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

}

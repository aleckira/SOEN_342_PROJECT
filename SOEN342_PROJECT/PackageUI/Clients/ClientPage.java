package PackageUI.Clients;

import PackageImportantObjects.Client;

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
        JButton viewAllAvailableOfferings = new JButton("View available offerings");
        JButton viewBookingsBtn = new JButton("View your bookings");
        JButton logout = new JButton("Logout");

        // Add action listeners for each button
        viewAllAvailableOfferings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("client");
                dispose();

            }
        });

        viewBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("instructor");
                dispose();
            }
        });

        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("admin");
                dispose();
            }
        });


        // Add buttons to the panel
        panel.add(viewAllAvailableOfferings);
        panel.add(viewBookingsBtn);
        panel.add(logout);
        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

}

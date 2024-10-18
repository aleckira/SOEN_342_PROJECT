package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import PackageUI.GeneralUI.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AdminPage extends JFrame {

    // Constructor for LoginPage
    public AdminPage(Admin a) {
        // Set up the frame
        setTitle("Admin Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10)); // 4 rows, 1 column, with spacing

        // Create the label for login selection
        JLabel loginLabel = new JLabel("Select among the following options", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(loginLabel);

        // Create buttons for User, Instructor, and Admin logins
        JButton enterOfferingsBtn = new JButton("Enter organization offerings");
        JButton deleteAccBtn = new JButton("Delete an account");
        JButton viewOfferingsBtn = new JButton("View all offerings");
        JButton viewBookingsBtn = new JButton("View all bookings");
        JButton logout = new JButton("Logout");

        // Add action listeners for each button
        enterOfferingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("client");
                dispose();
                new AddOffering();

            }
        });

        deleteAccBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("instructor");
                dispose();
                new Accounts();
            }
        });

        viewOfferingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("admin");
                dispose();
                new OfferingsPage();
            }
        });
        viewBookingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("admin");
                dispose();
                new BookedLessons();
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new LoginForm("admin");
                dispose();
                new LoginPage(); // Create and display the login page
            }
        });

        // Add buttons to the panel
        panel.add(enterOfferingsBtn);
        panel.add(deleteAccBtn);
        panel.add(viewOfferingsBtn);
        panel.add(viewBookingsBtn);
        panel.add(logout);

        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

}

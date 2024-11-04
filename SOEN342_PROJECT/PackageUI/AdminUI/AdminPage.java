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

    // Constructor for LoginPage
    public AdminPage() {
        Admin a = (Admin)UserSession.getCurrentUser();
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
//        JButton deleteAccBtn = new JButton("Delete an account");
        JButton viewOfferingsBtn = new JButton("View, add, edit and delete offerings");
        JButton viewBookingsBtn = new JButton("View, edit and delete all bookings");
        JButton deleteInstructorBtn = new JButton("View and delete instructors");
        JButton deleteClientBtn = new JButton("View and delete clients");
        JButton logout = new JButton("Logout");

//        deleteAccBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose();
//                new Accounts();
//            }
//        });

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
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserSession.setCurrentUserRole(null, null);
                new LoginPage(); // Create and display the offerings page
            }
        });


        // Add buttons to the panel
        panel.add(viewOfferingsBtn);
        panel.add(viewBookingsBtn);
        panel.add(deleteInstructorBtn);
        panel.add(deleteClientBtn);
        panel.add(logout);
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

}

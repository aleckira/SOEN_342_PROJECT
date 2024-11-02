package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import PackageActorsAndObjects.Instructor;
import PackageUI.GeneralUI.AddOffering;
import PackageUI.GeneralUI.BookedLessons;
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
        JButton enterOfferingsBtn = new JButton("Enter organization offerings");
//        JButton deleteAccBtn = new JButton("Delete an account");
        JButton viewOfferingsBtn = new JButton("View, edit and delete offerings");
        JButton viewBookingsBtn = new JButton("View, edit and delete all bookings");
        JButton deleteInstructorBtn = new JButton("View and delete instructors");
        JButton deleteClientBtn = new JButton("View and delete clients");

        // Add action listeners for each button
        enterOfferingsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AddOffering();

            }
        });

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
                new BookedLessons();
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


        // Add buttons to the panel
        panel.add(viewOfferingsBtn);
        panel.add(viewBookingsBtn);
        panel.add(enterOfferingsBtn);
        //panel.add(deleteAccBtn); separate this into the two below
        panel.add(deleteInstructorBtn);
        panel.add(deleteClientBtn);
        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

}

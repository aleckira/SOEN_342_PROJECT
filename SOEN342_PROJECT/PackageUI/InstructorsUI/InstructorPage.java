package PackageUI.InstructorsUI;

import PackageActorsAndObjects.Instructor;
import PackageUI.GeneralUI.LoginPage;
import PackageUI.GeneralUI.OfferingsPage;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class InstructorPage extends JFrame {

    public InstructorPage() {
        // Set up the frame
        Instructor i = (Instructor )UserSession.getCurrentUser();
        setTitle("Instructor Page");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10)); // 4 rows, 1 column, with spacing

        // Create the label for login selection
        JLabel loginLabel = new JLabel("Select among the following options", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(loginLabel);

        // Create buttons for User, Instructor, and Admin logins
        JButton viewAllOfferings = new JButton("View all offerings, select an offering to teach");
        JButton logout = new JButton("Logout");
        // Add action listeners for each button
        viewAllOfferings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new OfferingsPage(); // Create and display the offerings page

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
        panel.add(viewAllOfferings);
        panel.add(logout);
        // Add panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

}

package PackageUI.GuardianUI;

import PackageActorsAndObjects.Guardian;
import PackageUI.GeneralUI.BookingsPage;
import PackageUI.GeneralUI.OfferingsPage;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuardianPage extends JFrame {
    private Guardian guardian;

    public GuardianPage() {
        setTitle("Guardian Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);

        // Retrieve the current guardian from the UserSession
        guardian = (Guardian) UserSession.getCurrentUser();

        JButton viewOfferingsButton = new JButton("View Offerings");
        JButton viewBookingsButton = new JButton("View Minors' Bookings");

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

        setLayout(new FlowLayout());
        add(viewOfferingsButton);
        add(viewBookingsButton);

        setVisible(true);
    }
}

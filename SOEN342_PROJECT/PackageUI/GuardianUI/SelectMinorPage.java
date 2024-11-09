package PackageUI.GuardianUI;

import PackageActorsAndObjects.Admin;
import PackageActorsAndObjects.Guardian;
import PackageActorsAndObjects.Minor;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SelectMinorPage extends JFrame {


    Guardian guardian = (Guardian) UserSession.getCurrentUser();

    public SelectMinorPage(int offeringId, String startTime, String endTime) {
        // Set up the frame
        setTitle("Select Minor");
        setSize(300, 200); // Increase height to allow for more space
        setLayout(new BorderLayout());

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();

        ArrayList<Minor> minors = guardian.getMinors(); // Assume this method exists in Guardian class
        for (Minor minor : minors) {
            JRadioButton radioButton = new JRadioButton(minor.getName());
            radioButton.setActionCommand(String.valueOf(minor.getId()));
            group.add(radioButton);
            radioPanel.add(radioButton);
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMinorId = group.getSelection().getActionCommand();
                if (selectedMinorId != null) {
                    int minorId = Integer.parseInt(selectedMinorId);

                    // Check for time conflicts and proceed with booking


                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss.S");
                    LocalDateTime startLocalDateTime = LocalDateTime.parse(startTime, formatter);
                    LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime, formatter);

                    Timestamp startTimeTimestamp = Timestamp.valueOf(startLocalDateTime);
                    Timestamp endTimeTimeStamp = Timestamp.valueOf(endLocalDateTime);

                    if (guardian.isThereBookingTimeConflict(minorId, startTimeTimestamp, endTimeTimeStamp)) {
                        JOptionPane.showMessageDialog(SelectMinorPage.this, "The selected minor already has a booking at this time.");
                    } else {
                        boolean makeBookingSuccess = guardian.makeBooking(minorId, offeringId);
                        if (makeBookingSuccess) {
                            JOptionPane.showMessageDialog(SelectMinorPage.this, "Lesson booked successfully.");
                            SelectMinorPage.this.dispose();
                        } else {
                            JOptionPane.showMessageDialog(SelectMinorPage.this, "Error booking lesson.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(SelectMinorPage.this, "Please select a minor.");
                }
            }
        });

        add(radioPanel, BorderLayout.CENTER);
        add(confirmButton, BorderLayout.SOUTH);
        setVisible(true);

    }
}

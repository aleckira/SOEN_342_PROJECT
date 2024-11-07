package PackageUI.AdminUI;

import PackageActorsAndObjects.Admin;
import Services.UserSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditOfferingPage extends JFrame {

    public EditOfferingPage(int offeringId) {
        setTitle("Edit Offering");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(9, 2));

        // City dropdown options
        JComboBox<String> cityDropdown = new JComboBox<>(new String[]{"Montreal", "Laval", "Quebec City", "Gatineau", "Sherbrooke"});

        // Class type dropdown options
        JComboBox<String> classTypeDropdown = new JComboBox<>(new String[]{"Swimming", "Judo", "MMA", "Basketball", "Soccer", "Yoga"});

        // Other form fields
        JTextField locationField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField startTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");
        JTextField endTimeField = new JTextField("yyyy-MM-dd HH:mm:ss");
        JTextField instructorIdField = new JTextField();

        JButton submitButton = new JButton("Edit Offering");

        add(new JLabel("City:"));
        add(cityDropdown);

        add(new JLabel("Location:"));
        add(locationField);

        add(new JLabel("Class Type:"));
        add(classTypeDropdown);

        add(new JLabel("Capacity:"));
        add(capacityField);

        add(new JLabel("Start Time (yyyy-MM-dd HH:mm:ss):"));
        add(startTimeField);

        add(new JLabel("End Time (yyyy-MM-dd HH:mm:ss):"));
        add(endTimeField);

        add(new JLabel("Instructor ID:"));
        add(instructorIdField);

        add(new JLabel());  // Empty space
        add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String city = (String) cityDropdown.getSelectedItem();
                    String location = locationField.getText();
                    String classType = (String) classTypeDropdown.getSelectedItem();
                    int capacity = Integer.parseInt(capacityField.getText());
                    LocalDateTime startTime = LocalDateTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime endTime = LocalDateTime.parse(endTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    int instructorId = Integer.parseInt(instructorIdField.getText());

                    if (capacity <= 1) {
                        JOptionPane.showMessageDialog(EditOfferingPage.this, "Capacity must be greater than 1.");
                        return;
                    }

                    Admin admin = (Admin) UserSession.getCurrentUser();

                    // Validate the instructor ID
                    if (!admin.isInstructorValid(instructorId)) {
                        JOptionPane.showMessageDialog(EditOfferingPage.this, "Instructor ID does not exist.");
                        return;
                    }

                    boolean isUnique = admin.isNewOfferingUnique(location, city, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));

                    if (!isUnique) {
                        JOptionPane.showMessageDialog(EditOfferingPage.this, "The offering is not unique. Please modify the details.");
                        return;
                    }

                    boolean success = admin.editOffering(offeringId, city, location, classType, capacity, Timestamp.valueOf(startTime), Timestamp.valueOf(endTime), instructorId);

                    if (success) {
                        JOptionPane.showMessageDialog(EditOfferingPage.this, "Offering updated successfully.");
                        dispose();  // Close the window
                    } else {
                        JOptionPane.showMessageDialog(EditOfferingPage.this, "Failed to update offering. Please check your inputs.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(EditOfferingPage.this, "Error: " + ex.getMessage());
                }
            }
        });

        setVisible(true);
    }
}

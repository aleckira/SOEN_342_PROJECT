//package PackageUI;
//
//import src.project342.InitialDbFunctions;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UserOfferingsPage extends JFrame {
//
//    private JList<String> offeringsList; // List to display offerings
//    private DefaultListModel<String> listModel; // Model for the offerings list
//    private JButton bookOfferingBtn;
//
//    public UserOfferingsPage() {
//        // Frame setup
//        setTitle("User - Available Offerings");
//        setSize(400, 300);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // Create the main panel
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout());
//
//        // Heading label
//        JLabel heading = new JLabel("Available Offerings", JLabel.CENTER);
//        heading.setFont(new Font("Arial", Font.BOLD, 16));
//        panel.add(heading, BorderLayout.NORTH);
//
//        // List model and JList for available offerings
//        listModel = new DefaultListModel<>();
//        offeringsList = new JList<>(listModel);
//        panel.add(new JScrollPane(offeringsList), BorderLayout.CENTER);
//
//        // Button to book an offering
//        bookOfferingBtn = new JButton("Book Offering");
//        panel.add(bookOfferingBtn, BorderLayout.SOUTH);
//
//        // Load available offerings from the database
//        loadAvailableOfferings();
//
//        // Action listener for the "Book Offering" button
//        bookOfferingBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String selectedOffering = offeringsList.getSelectedValue();
//                if (selectedOffering != null) {
//                    bookOffering(selectedOffering);
//                } else {
//                    JOptionPane.showMessageDialog(null, "Please select an offering to book.");
//                }
//            }
//        });
//
//        add(panel);
//        setVisible(true);
//    }
//
//    // Method to load available offerings from the database
//    private void loadAvailableOfferings() {
//        try {
//            Connection connection = InitialDbFunctions.getConnection();
//            String query = "SELECT * FROM offerings"; // Fetch all offerings
//            PreparedStatement stmt = connection.prepareStatement(query);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                String offering = rs.getString("class_type") + " at " + rs.getString("location") + " on " +
//                        rs.getString("day") + " from " + rs.getString("start_time") + " to " +
//                        rs.getString("end_time");
//                listModel.addElement(offering);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error loading offerings.");
//        }
//    }
//
//    // Method to book an offering
//    private void bookOffering(String offering) {
//        try {
//            Connection connection = InitialDbFunctions.getConnection();
//            String[] offeringDetails = offering.split(" at | on | from | to ");
//            String classType = offeringDetails[0];
//            String location = offeringDetails[1];
//            String day = offeringDetails[2];
//            String startTime = offeringDetails[3];
//
//            // Get the user id (you can modify this to fetch the current user's id dynamically)
//            int userId = 1; // Replace with dynamic value as needed
//
//            // Insert booking into the 'bookings' table
//            String insertQuery = "INSERT INTO bookings (user_id, class_type, location, day, start_time) VALUES (?, ?, ?, ?, ?)";
//            PreparedStatement stmt = connection.prepareStatement(insertQuery);
//            stmt.setInt(1, userId);
//            stmt.setString(2, classType);
//            stmt.setString(3, location);
//            stmt.setString(4, day);
//            stmt.setString(5, startTime);
//
//            int rowsInserted = stmt.executeUpdate();
//            if (rowsInserted > 0) {
//                JOptionPane.showMessageDialog(null, "Successfully booked the offering!");
//            } else {
//                JOptionPane.showMessageDialog(null, "Error booking the offering.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(null, "Error booking offering.");
//        }
//    }
//}
//

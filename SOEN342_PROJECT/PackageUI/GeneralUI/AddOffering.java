package PackageUI.GeneralUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static Services.DbConnectionService.connectToDb;


public class AddOffering extends JFrame {

    private JTextArea offeringsDisplay; // To display current offerings
    private JTextField classTypeField, locationField, cityField, capacityField, startTimeField, endTimeField, instructorIdField, clientIdsField; // Fields for adding new offering

    public AddOffering() {

        // Set up the frame
        setTitle("Offerings");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Panel for displaying current offerings
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());

        offeringsDisplay = new JTextArea(10, 50);
        offeringsDisplay.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(offeringsDisplay);
        displayPanel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Offerings");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayOfferings();
            }
        });
        displayPanel.add(refreshButton, BorderLayout.SOUTH);

        // Panel for adding new offerings
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new GridLayout(9, 2, 10, 10)); // Adjusted grid for 9 fields

        classTypeField = new JTextField();
        locationField = new JTextField();
        cityField = new JTextField();
        capacityField = new JTextField();
        startTimeField = new JTextField();
        endTimeField = new JTextField();
        instructorIdField = new JTextField();
        clientIdsField = new JTextField(); // Field to input client IDs

        addPanel.add(new JLabel("Class Type:"));
        addPanel.add(classTypeField);
        addPanel.add(new JLabel("Location:"));
        addPanel.add(locationField);
        addPanel.add(new JLabel("City:"));
        addPanel.add(cityField);
        addPanel.add(new JLabel("Capacity:"));
        addPanel.add(capacityField);
        addPanel.add(new JLabel("Start Time (YYYY-MM-DD HH:MM:SS):"));
        addPanel.add(startTimeField);
        addPanel.add(new JLabel("End Time (YYYY-MM-DD HH:MM:SS):"));
        addPanel.add(endTimeField);
        addPanel.add(new JLabel("Instructor ID:"));
        addPanel.add(instructorIdField);
        addPanel.add(new JLabel("Client IDs (comma-separated):"));
        addPanel.add(clientIdsField); // Add field to input client IDs

        JButton addButton = new JButton("Add Offering");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOffering();
            }
        });

        addPanel.add(addButton);

        // Add panels to the frame
        add(displayPanel, BorderLayout.NORTH);
        add(addPanel, BorderLayout.SOUTH);

        // Initial display of offerings
        displayOfferings();

        setVisible(true);
    }

    // Method to display all offerings from the database
    private void displayOfferings() {
        offeringsDisplay.setText(""); // Clear the text area
        String query = "SELECT * FROM offerings"; // Query to fetch all offerings
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String classType = rs.getString("class_type");
                String location = rs.getString("location");
                String city = rs.getString("city");
                int capacity = rs.getInt("capacity");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                int instructorId = rs.getInt("instructor_id");

                offeringsDisplay.append("Class Type: " + classType + ", Location: " + location + ", City: " + city +
                        ", Capacity: " + capacity + ", Start Time: " + startTime + ", End Time: " + endTime +
                        ", Instructor ID: " + instructorId + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add offerings
    private void addOffering() {
        String classType = classTypeField.getText();
        String location = locationField.getText();
        String city = cityField.getText();
        int capacity;
        try {
            capacity = Integer.parseInt(capacityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse the start and end times as Timestamps
        String startTimeInput = startTimeField.getText();
        String endTimeInput = endTimeField.getText();
        Timestamp startTime;
        Timestamp endTime;
        try {
            startTime = parseTimestamp(startTimeInput);
            endTime = parseTimestamp(endTimeInput);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD HH:MM:SS", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int instructorId;
        try {
            instructorId = Integer.parseInt(instructorIdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Instructor ID must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Parse client_ids field as a comma-separated string of integers
        String clientIdsInput = clientIdsField.getText();
        int[] clientIds;
        try {
            clientIds = parseClientIds(clientIdsInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Client IDs must be a comma-separated list of numbers", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (classType.isEmpty() || location.isEmpty() || city.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO offerings (class_type, location, city, capacity, start_time, end_time, instructor_id, client_ids) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, classType);
            stmt.setString(2, location);
            stmt.setString(3, city);
            stmt.setInt(4, capacity);
            stmt.setTimestamp(5, startTime);  // Use Timestamp for start_time
            stmt.setTimestamp(6, endTime);    // Use Timestamp for end_time
            stmt.setInt(7, instructorId);

            // Convert clientIds to Integer[] and then to a PostgreSQL array
            Array clientIdsArray = connection.createArrayOf("INTEGER", toIntegerArray(clientIds));
            stmt.setArray(8, clientIdsArray);  // Set client_ids as an array

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Offering added successfully!");
            displayOfferings(); // Refresh offerings display
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to convert int[] to Integer[]
    private Integer[] toIntegerArray(int[] intArray) {
        Integer[] result = new Integer[intArray.length];
        for (int i = 0; i < intArray.length; i++) {
            result[i] = Integer.valueOf(intArray[i]);
        }
        return result;
    }

    // Helper method to parse the timestamp from a string
    private Timestamp parseTimestamp(String timestampStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date parsedDate = dateFormat.parse(timestampStr);
        return new Timestamp(parsedDate.getTime());
    }

    // Helper method to parse client IDs from a comma-separated string into an integer array
    private int[] parseClientIds(String clientIdsInput) throws NumberFormatException {
        if (clientIdsInput.isEmpty()) {
            return new int[0]; // Return empty array if no client IDs are provided
        }
        String[] clientIdsStrArray = clientIdsInput.split(",");
        int[] clientIds = new int[clientIdsStrArray.length];
        for (int i = 0; i < clientIdsStrArray.length; i++) {
            clientIds[i] = Integer.parseInt(clientIdsStrArray[i].trim());
        }
        return clientIds;
    }



    // Method to clear the input fields after adding an offering
    private void clearFields() {
        classTypeField.setText("");
        locationField.setText("");
        cityField.setText("");
        capacityField.setText("");
        startTimeField.setText("");
        endTimeField.setText("");
        instructorIdField.setText("");
        clientIdsField.setText(""); // Clear clientIdsField
    }
}

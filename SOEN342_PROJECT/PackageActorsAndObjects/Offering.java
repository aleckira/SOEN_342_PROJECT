package PackageActorsAndObjects;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;

import static Services.DbConnectionService.connectToDb;


public class Offering {
    private int id;
    private String city;
    private String location;
    private String classType;
    private int capacity;
    private int spotsLeft;
    private String mode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int instructorId;
    public Offering() {}

    public Offering(int id, String city, String location, String classType, int capacity, LocalDateTime startTime, LocalDateTime endTime, int instructorId) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.classType = classType;
        this.mode = capacity == 1 ? "private" : "group";
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.spotsLeft = isOfferingAvailable(id, capacity);
        this.instructorId = instructorId;
    }
    private static int isOfferingAvailable(int offeringId, int capacity) {
        String query = "SELECT COUNT(*) FROM public.bookings WHERE offering_id = ?"; // Count rows with the given offering_id
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, offeringId); // Set the offering ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int numberOfBookings = rs.getInt(1); // Get the count of bookings for this offering
                    return capacity - numberOfBookings; // Return true if capacity is greater than bookings
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static boolean hasOfferingBeenBookedByClient(int offeringId, int clientId) {
        String query = "SELECT COUNT(*) FROM public.bookings WHERE client_id = ? AND offering_id = ?"; // Count rows with the given offering_id
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, clientId);
            stmt.setInt(2, offeringId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int numberOfBookings = rs.getInt(1); // Get the count of bookings for this offering
                    return numberOfBookings == 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error checking offering availability.");
        }
        return false;
    }
    public static Client fetchClientForBooking(int bookingId) {
        Client client = null;  // Initialize client as null
        String query = """
        SELECT c.id, c.name, c.phone_number, c.age
        FROM public.clients c
        INNER JOIN public.bookings b ON c.id = b.client_id
        WHERE b.id = ?  
    """;

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingId);  // Set the booking ID in the query

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {  // Check if a result is returned
                    int clientId = rs.getInt("id");
                    String name = rs.getString("name");
                    String phoneNumber = rs.getString("phone_number");
                    int age = rs.getInt("age");
                    client = new Client(clientId, name, phoneNumber, age);  // Create Client object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;  // Return the single Client or null if not found
    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getSpotsLeft() { return spotsLeft; }
    public void setSpotsLeft(int spotsLeft) { this.spotsLeft = spotsLeft; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }


}

package PackageActorsAndObjects;

import Services.DbConnectionService;

import javax.swing.*;
import java.sql.*;

import static Services.DbConnectionService.connectToDb;


public class Offering {
    private int id;
    private String city;
    private String location;
    private String classType;
    private int capacity;
    private int spotsLeft;
    private Timestamp startTime;
    private Timestamp endTime;
    private int instructorId;
    public Offering() {}
    //offering with no instructor
    public Offering(String city, String location, String classType, int capacity, Timestamp startTime, Timestamp endTime) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.classType = classType;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.spotsLeft = getSpotsLeft(id, capacity);
        this.instructorId = instructorId;
    }

    public Offering(int id, String city, String location, String classType, int capacity, Timestamp startTime, Timestamp endTime, int instructorId) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.classType = classType;
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.spotsLeft = getSpotsLeft(id, capacity);
        this.instructorId = instructorId;
    }
    private static int getSpotsLeft(int offeringId, int capacity) {
        String query = "SELECT COUNT(*) FROM public.bookings WHERE offering_id = ?"; // Count rows with the given offering_id
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, offeringId); // Set the offering ID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int numberOfBookings = rs.getInt(1); // Get the count of bookings for this offering
                    return capacity - numberOfBookings;
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
    public static Minor fetchMinorForBooking(int bookingId) {
        String query = """
        SELECT m.id, m.name, m.guardian_id
        FROM minors m
        INNER JOIN bookings b ON b.minor_id = m.id
        WHERE b.id = ?
    """;

        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int guardianId = rs.getInt("guardian_id");
                    return new Minor(id, name, guardianId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }


}

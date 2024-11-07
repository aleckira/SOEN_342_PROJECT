package PackageActorsAndObjects;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Services.DbConnectionService.connectToDb;

public class Client extends Actor {
    private int id;
    private String phoneNumber;
    private int age;
    public Client() {}
    public Client(int id, String name, String phoneNumber, int age) { // if a new Client Registers, also for the Offering class
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.age = age;

    }
    public ArrayList<Offering> getOfferingsForViewing() {
        ArrayList<Offering> offerings = new ArrayList<>();
        String query = "SELECT * FROM public.offerings WHERE instructor_id IS NOT NULL";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String city = rs.getString("city");
                String location = rs.getString("location");
                String classType = rs.getString("class_type");
                int capacity = rs.getInt("capacity");
                int instructorId = rs.getInt("instructor_id");
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");

                // Create an Offering object and add it to the list
                Offering offering = new Offering(id, city, location, classType, capacity, startTime, endTime, instructorId);
                offerings.add(offering);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }

    public Map<Offering, Integer> getBookingsForViewing() {
        Map<Offering, Integer> offerings = new HashMap<>();
        String query = """
        SELECT o.*, b.id AS booking_id
        FROM public.offerings o
        INNER JOIN public.bookings b ON o.id = b.offering_id
        WHERE b.client_id = ? AND o.instructor_id IS NOT NULL
        """;

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, this.getId());  // Set the client ID in the query

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String city = rs.getString("city");
                    String location = rs.getString("location");
                    String classType = rs.getString("class_type");
                    int capacity = rs.getInt("capacity");
                    int instructorId = rs.getInt("instructor_id");
                    Timestamp startTime = rs.getTimestamp("start_time");
                    Timestamp endTime = rs.getTimestamp("end_time");
                    int bookingId = rs.getInt("booking_id");  // Retrieve booking ID

                    // Create an Offering object
                    Offering offering = new Offering(id, city, location, classType, capacity, startTime, endTime, instructorId);

                    // Add the Offering and bookingId to the map
                    offerings.put(offering, bookingId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }


    public boolean makeBooking(int offeringId) {
        String query = "INSERT INTO public.bookings (client_id, offering_id) VALUES (?, ?)"; // Insert a new row
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, this.getId());
            stmt.setInt(2, offeringId);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Booking added successfully.");
                return true;
            } else {
                System.out.println("Failed to add booking.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean cancelBooking(int bookingId) {
        String query = "DELETE FROM public.bookings WHERE id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Booking canceled successfully.");
                return true;
            } else {
                System.out.println("Failed to cancel booking. Booking ID may not exist.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isThereBookingTimeConflict(Timestamp startTime, Timestamp endTime) {
        String offeringQuery = "SELECT id FROM public.offerings WHERE start_time = ? AND end_time = ?";
        String bookingQuery = "SELECT COUNT(*) FROM public.bookings WHERE client_id = ? AND offering_id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement offeringStmt = connection.prepareStatement(offeringQuery)) {

            // Set parameters for the offering query
            offeringStmt.setTimestamp(1, startTime);
            offeringStmt.setTimestamp(2, endTime);

            // Execute the offering query
            try (ResultSet offeringRs = offeringStmt.executeQuery()) {
                while (offeringRs.next()) {
                    int offeringId = offeringRs.getInt("id");

                    // Check if there’s a booking for this client with the found offering_id
                    try (PreparedStatement bookingStmt = connection.prepareStatement(bookingQuery)) {
                        bookingStmt.setInt(1, this.getId());
                        bookingStmt.setInt(2, offeringId);

                        try (ResultSet bookingRs = bookingStmt.executeQuery()) {
                            if (bookingRs.next() && bookingRs.getInt(1) > 0) {
                                // Conflict found: a booking exists for the client with this time slot
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // No conflict found
        return false;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public int getAge() {
        return age;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setAge(int age) {
        this.age = age;
    }



}

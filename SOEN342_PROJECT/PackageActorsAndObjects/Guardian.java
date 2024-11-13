package PackageActorsAndObjects;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static Services.DbConnectionService.connectToDb;

public class Guardian extends Actor {
    private int id;
    private String phoneNumber;
    private int age;

    public Guardian() {}

    public Guardian(int id, String name, String phoneNumber, int age) {
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
    SELECT o.*, b.id AS booking_id, m.name AS minor_name
    FROM public.offerings o
    INNER JOIN public.bookings b ON o.id = b.offering_id
    INNER JOIN public.minors m ON b.minor_id = m.id
    WHERE m.guardian_id = ? AND o.instructor_id IS NOT NULL
    """;

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, this.getId());  // Set the guardian ID in the query

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


    public MinorBooking makeBooking(int minorId, int offeringId) {
        String query = "INSERT INTO public.bookings (minor_id, offering_id) VALUES (?, ?)";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, minorId);
            stmt.setInt(2, offeringId);

            return new MinorBooking(offeringId,minorId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Minor> getMinors() {
        ArrayList<Minor> minors = new ArrayList<>();
        String query = "SELECT id, name FROM minors WHERE guardian_id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, this.id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    minors.add(new Minor(id, name, this.id));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return minors;
    }


    public boolean cancelBooking(int bookingId) {
        String query = "DELETE FROM public.bookings WHERE id = ?";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isThereBookingTimeConflict(int minorId, Timestamp startTime, Timestamp endTime) {
        String query = """
    SELECT COUNT(*)
    FROM bookings b
    JOIN offerings o ON b.offering_id = o.id
    WHERE b.minor_id = ? AND (
        (o.start_time < ? AND o.end_time > ?) OR
        (o.start_time < ? AND o.end_time > ?) OR
        (o.start_time >= ? AND o.end_time <= ?)
    )
    """;


        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, minorId);
            stmt.setTimestamp(2, endTime);
            stmt.setTimestamp(3, startTime);
            stmt.setTimestamp(4, startTime);
            stmt.setTimestamp(5, endTime);
            stmt.setTimestamp(6, startTime);
            stmt.setTimestamp(7, endTime);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Conflict found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // No conflict
    }

    public String getMinorNameForBooking(int bookingId) {
        String query = "SELECT m.name FROM minors m INNER JOIN bookings b ON m.id = b.minor_id WHERE b.id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name"); // Return minor's name
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no minor found
    }



    public int getId() {
        return id;
    }

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

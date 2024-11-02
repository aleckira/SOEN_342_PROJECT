package PackageActorsAndObjects;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static Services.DbConnectionService.connectToDb;

public class Admin extends Actor {
    private final String password;
    private static Admin instance;

    private Admin(String name, String password) {
        super(name);
        this.password = password;
    }

    public static Admin getInstance(String name, String password) {
        if (instance == null) {
            instance = new Admin(name, password);
        }
        return instance;
    }

    @Override
    public ArrayList<Offering> getOfferingsForViewing() {
        ArrayList<Offering> offerings = new ArrayList<>();
        String query = "SELECT * FROM public.offerings";
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
                LocalDateTime startTime = rs.getObject("start_time", LocalDateTime.class);
                LocalDateTime endTime = rs.getObject("end_time", LocalDateTime.class);

                Offering offering = new Offering(id, city, location, classType, capacity, startTime, endTime, instructorId, false);
                offerings.add(offering);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }
    public ArrayList<Offering> getAllBookingsForViewing() {
        return null;
    }
    public boolean isNewOfferingUnique(String location, String city, Timestamp startTime, Timestamp endTime) {
        String query = "SELECT COUNT(*) FROM public.offerings " +
                "WHERE city = ? AND location = ? AND start_time = ? AND end_time = ?";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, city);
            stmt.setString(2, location);
            stmt.setTimestamp(3, startTime);
            stmt.setTimestamp(4, endTime);

            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean enterNewOffering(String classType, String location, String city, int capacity, Timestamp startTime, Timestamp endTime) {
        String query = "INSERT INTO offerings (class_type, location, city, capacity, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, classType);
            stmt.setString(2, location);
            stmt.setString(3, city);
            stmt.setInt(4, capacity);
            stmt.setTimestamp(5, startTime);  // Use Timestamp for start_time
            stmt.setTimestamp(6, endTime);    // Use Timestamp for end_time
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deleteOffering(int offeringId) {
    }
    public void deleteBooking(int bookingId) {
    }
    public void deleteInstructor(int instructorId) {
    }
    public void deleteClient(int clientId) {
    }
    public void editOffering(Offering o) {
    }
    public void editBooking(Offering b) {
    }
}

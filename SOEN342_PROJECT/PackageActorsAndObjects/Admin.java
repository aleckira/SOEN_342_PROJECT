package PackageActorsAndObjects;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

                Offering offering = new Offering(id, city, location, classType, capacity, startTime, endTime, instructorId);
                offerings.add(offering);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }
    public Map<Offering, Integer> getAllBookingsForViewing() {
        Map<Offering, Integer> offerings = new HashMap<>();
        String query = """
        SELECT DISTINCT o.*, b.id AS booking_id
        FROM public.offerings o
        INNER JOIN public.bookings b ON o.id = b.offering_id
        """;

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
                int bookingId = rs.getInt("booking_id");  // Retrieve booking ID

                // Create an Offering object
                Offering offering = new Offering(id, city, location, classType, capacity, startTime, endTime, instructorId);

                // Add the Offering and bookingId to the map
                offerings.put(offering, bookingId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }


    public static boolean isNewOfferingUnique(String location, String city, Timestamp startTime, Timestamp endTime) {
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
    public ArrayList<Client> getAllClientsForViewing() {
        ArrayList<Client> clients = new ArrayList<>();
        String query = "SELECT * FROM public.clients";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                int age = rs.getInt("age");


                Client c = new Client(id,name,phoneNumber,age);
                clients.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }
    public ArrayList<Instructor> getAllInstructorsForViewing() {
        ArrayList<Instructor> instructors = new ArrayList<>();
        String query = "SELECT * FROM public.instructors";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String specialty = rs.getString("specialty");
                String citiesString = rs.getString("cities").replaceAll("[{}]", "");
                String[] citiesArray = citiesString.split(",");
                ArrayList<String> citiesList = new ArrayList<>();
                for (String city : citiesArray) {
                    citiesList.add(city.trim());
                }

                Instructor i = new Instructor(id,name,phoneNumber, specialty, citiesList);
                instructors.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;
    }
    public boolean deleteOffering(int offeringId) {
        String deleteBookingsQuery = "DELETE FROM bookings WHERE offering_id = ?";
        String deleteOfferingQuery = "DELETE FROM offerings WHERE id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement deleteBookingsStmt = connection.prepareStatement(deleteBookingsQuery);
             PreparedStatement deleteOfferingStmt = connection.prepareStatement(deleteOfferingQuery)) {

            // Delete bookings associated with the offering
            deleteBookingsStmt.setInt(1, offeringId);
            deleteBookingsStmt.executeUpdate();

            // Delete the offering itself
            deleteOfferingStmt.setInt(1, offeringId);
            int rowsDeleted = deleteOfferingStmt.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteBooking(int bookingId) {
        String query = "DELETE FROM bookings WHERE id = ?";

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
    public boolean deleteInstructor(int instructorId) {
        String deleteBookingsQuery = "DELETE FROM bookings WHERE offering_id IN (SELECT id FROM offerings WHERE instructor_id = ?)";
        String updateOfferingsQuery = "UPDATE offerings SET instructor_id = NULL WHERE instructor_id = ?";
        String deleteInstructorQuery = "DELETE FROM instructors WHERE id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement deleteBookingsStmt = connection.prepareStatement(deleteBookingsQuery);
             PreparedStatement updateOfferingsStmt = connection.prepareStatement(updateOfferingsQuery);
             PreparedStatement deleteInstructorStmt = connection.prepareStatement(deleteInstructorQuery)) {

            // Delete bookings for the instructor's offerings
            deleteBookingsStmt.setInt(1, instructorId);
            deleteBookingsStmt.executeUpdate();

            // Set instructor_id to NULL for offerings
            updateOfferingsStmt.setInt(1, instructorId);
            updateOfferingsStmt.executeUpdate();

            // Delete the instructor
            deleteInstructorStmt.setInt(1, instructorId);
            int rowsDeleted = deleteInstructorStmt.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteClient(int clientId) {
        String deleteBookingsQuery = "DELETE FROM bookings WHERE client_id = ?";
        String deleteClientQuery = "DELETE FROM clients WHERE id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement deleteBookingsStmt = connection.prepareStatement(deleteBookingsQuery);
             PreparedStatement deleteClientStmt = connection.prepareStatement(deleteClientQuery)) {

            // Delete bookings for the client
            deleteBookingsStmt.setInt(1, clientId);
            deleteBookingsStmt.executeUpdate();

            // Delete the client
            deleteClientStmt.setInt(1, clientId);
            int rowsDeleted = deleteClientStmt.executeUpdate();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean editOffering(int offeringId, String city, String location, String classType, int capacity, Timestamp startTime, Timestamp endTime, int instructor_id) {
        String query = "UPDATE offerings SET city = ?, location = ?, class_type = ?, capacity = ?, start_time = ?, end_time = ?, instructor_id = ? WHERE id = ?";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, city);
            stmt.setString(2, location);
            stmt.setString(3, classType);
            stmt.setInt(4, capacity);
            stmt.setTimestamp(5, startTime);
            stmt.setTimestamp(6, endTime);
            stmt.setInt(7, instructor_id);
            stmt.setInt(8, offeringId);
            int rowsUpdated = stmt.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isInstructorValid(int instructorId) {
        String query = "SELECT COUNT(*) FROM instructors WHERE id = ?";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

package PackageActorsAndObjects;

import javax.swing.*;
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
        //delete offering should...
        //1. Delete the offering in the offerings table
        //2. Delete ALL bookings associated to this (for clients AND minors, but it's all in the bookings table anyway)
        //return true if it worked, else return false
        return true;
    }
    public boolean deleteBooking(int bookingId) {
        //should ONLY delete one booking in the bookings table
        //return true if it worked, else return false
        return true;
    }
    public boolean deleteInstructor(int instructorId) {
        //delete instructor should...
        //1. delete the instructor in the instructors table
        //2. set the "instructor_id" column to [null] in the offerings table for all offerings with instructor_id for that instructor
        //3. should delete ALL bookings associated to that instructor i.e. delete all bookings for all offerings in 2. (you can't have a class without an instructor)
        //return true if it worked, else return false
        return true;
    }
    public boolean deleteClient(int clientId) {
        //delete client should...
        //1. delete the client in the clients table
        //2. delete ALL bookings associated to that client in the bookings table
        //return true if it worked, else return false
        return true;
    }
    public boolean editOffering(int offeringId, String city, String location, String classType, int capacity, Timestamp startTime, Timestamp endTime, int instructor_id) {
        //edit offering should...
        //find the offering in the offerings table via id
        //replace ALL values of that offering with the new offering info
        //return true if it worked, else return false
        return true;
    }
    public boolean editBooking(int bookingdId, int clientId, int offeringId) {
        //edit bookings should...

        //return true if it worked, else return false
        return true;
    }
}

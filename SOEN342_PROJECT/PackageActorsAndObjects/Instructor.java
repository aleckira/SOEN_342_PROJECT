package PackageActorsAndObjects;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static Services.DbConnectionService.connectToDb;

public class Instructor extends Actor {
    private int id;
    private String phoneNumber;
    private String specialty;
    private ArrayList<String> cities;
    public Instructor() {}
    public Instructor(int id, String name, String phoneNumber, String specialty, ArrayList<String> cities) {
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.specialty = specialty;
        this.cities = cities;
    }

    public ArrayList<Offering> getOfferingsForViewing() {
        ArrayList<Offering> offerings = new ArrayList<>();
        String query = "SELECT * FROM public.offerings";
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            // Process each row in the result set
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
    public static Instructor fetchInstructorById(int instructorId) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = null;
        String query = "SELECT \"id\", \"name\", \"phone_number\", \"specialty\", \"cities\" FROM \"public\".\"instructors\" WHERE \"id\" = ?";

        try {
            connection = connectToDb();
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, instructorId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phone_number");
                String specialty = rs.getString("specialty");
                String cities = rs.getString("cities");

                ArrayList<String> citiesArrList = new ArrayList<>(Arrays.asList(cities.split(",")));

                return new Instructor(id, name, phoneNumber, specialty, citiesArrList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean takeOffering(int offeringId) {
        String query = "UPDATE offerings SET instructor_id = ? WHERE id = ?"; // Update based on offering ID
        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, this.getId());
            stmt.setInt(2, offeringId); // Set the offering ID
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getSpecialty() {
        return specialty;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    public ArrayList<String> getCities() {
        return cities;
    }
    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }
}

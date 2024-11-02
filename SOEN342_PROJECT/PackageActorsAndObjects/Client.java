package PackageActorsAndObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
    public Client(int id, String name, String phoneNumber, int age, ArrayList<Integer> bookingIds) { // if a Client logs in, we use bookingIds to find the ids
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.age = age;

    }

    @Override
    public ArrayList<Offering> getOfferingsForViewing() {
        ArrayList<Offering> offerings = new ArrayList<>();
        String query = "SELECT * FROM public.offerings WHERE instructor_id IS NOT NULL";
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
                LocalDateTime startTime = rs.getObject("start_time", LocalDateTime.class);
                LocalDateTime endTime = rs.getObject("end_time", LocalDateTime.class);

                // Create an Offering object and add it to the list
                Offering offering = new Offering(id, city, location, classType, capacity, startTime, endTime, instructorId, false);
                offerings.add(offering);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offerings;
    }

    public ArrayList<Booking> getBookingsForViewing() {
        return null;
    }
    public void makeBooking(int offeringId) {

    }
    public void cancelBooking(int bookingId) {

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

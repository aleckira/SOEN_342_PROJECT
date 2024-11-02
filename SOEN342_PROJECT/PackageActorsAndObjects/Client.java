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
    @Override
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

    public ArrayList<Offering> getBookingsForViewing() {
        return null;
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

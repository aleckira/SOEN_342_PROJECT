package PackageActorsAndObjects;

import Services.DbConnectionService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class Client extends Actor {
    private int id;
    private String phoneNumber;
    private int age;
    private ArrayList<Offering> bookings = new ArrayList<Offering>();
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
        this.bookings = fetchBookings(bookingIds);

    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Client ID: ").append(id)
                .append(", Name: ").append(getName()) // Assuming getName() is defined in the Actor class
                .append(", Phone Number: ").append(phoneNumber)
                .append(", Age: ").append(age)
                .append(", Bookings: [");

        for (Offering offering : bookings) {
            sb.append(offering.toString()).append(", "); // Assuming Offering has a toString method
        }

        // Remove the last comma and space if bookings are not empty
        if (!bookings.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("]");
        return sb.toString();
    }

    private ArrayList<Offering> fetchBookings(ArrayList<Integer> bookingIds) {
        ArrayList<Offering> bookings = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM public.offerings WHERE id = ?"; // Adjust this query as needed
        Connection connection = null;
        try {
            connection = DbConnectionService.connectToDb();
            stmt = connection.prepareStatement(query);

            for (int bookingId : bookingIds) {
                stmt.setInt(1, bookingId);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String city = rs.getString("city");
                    String location = rs.getString("location");
                    String classType = rs.getString("class_type");
                    int capacity = rs.getInt("capacity");
                    LocalDateTime startTime = rs.getObject("start_time", LocalDateTime.class);
                    LocalDateTime endTime = rs.getObject("end_time", LocalDateTime.class);
                    int instructorId = rs.getInt("instructor_id");
                    Integer[] clientIdsArray = (Integer[]) rs.getArray("client_ids").getArray();
                    ArrayList<Integer> clientIds = new ArrayList<>(Arrays.asList(clientIdsArray));

                    // Create an Offering object and add it to the list
                    Offering offering = new Offering( id, city, location, classType, capacity, startTime, endTime, instructorId, clientIds);
                    bookings.add(offering);
                }
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
        return bookings;
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
    public ArrayList<Offering> getBookings() {return bookings;}



}

package SOEN342_PROJECT.PackageActorsAndObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static SOEN342_PROJECT.Services.DbConnectionService.connectToDb;

public class Offering {
    private int id;
    private String city;
    private String location;
    private String classType;
    private int capacity;
    private boolean available; //this should go in Booking
    private String mode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instructor instructor; //an offering could be without an instructor, specifically when it is first created
    private ArrayList<Client> clients = new ArrayList<Client>(); //this should go in Booking
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    public Offering() {}
    // when Admin creates a new offering, it has no instructor nor clients
    public Offering(int id, String city, String location, String classType, int capacity, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.classType = classType;
        this.mode = capacity == 1 ? "private" : "group";
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
    }
    //when anyone tries to see a booking (in the table), we associate the instructor with its ID and clients to this booking via clientIds
    //We know if we can't find an instructor upon calling fetchInstructor(), it doesn't have an instructor yet (public can't see it)
    //We know if we can't find clients upon calling fetchClients(), we have no clients
    //We know if the capacity is above the current number of clients, the offering is no longer available (public can't reserve it)
    //this should go in Booking
    public Offering(int id, String city, String location, String classType, int capacity, LocalDateTime startTime, LocalDateTime endTime, int instructorId, ArrayList<Integer> clientIds) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.classType = classType;
        this.mode = capacity == 1 ? "private" : "group";
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.instructor = fetchInstructor(instructorId);
        this.clients = fetchClients(clientIds); //need to fix this, it's screwing with connections for some reason
        this.available = this.capacity > this.clients.size();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Offering Details:\n")
                .append("ID: ").append(id).append("\n")
                .append("City: ").append(city).append("\n")
                .append("Location: ").append(location).append("\n")
                .append("Class type: ").append(classType).append("\n")
                .append("Capacity: ").append(capacity).append("\n")
                .append("Available: ").append(available ? "Yes" : "No").append("\n")
                .append("Mode: ").append(mode).append("\n")
                .append("Start Time: ").append(startTime.format(timeFormatter)).append("\n")
                .append("End Time: ").append(endTime.format(timeFormatter)).append("\n")
                .append("Instructor: ").append(instructor != null ? instructor.getName() : "No instructor assigned").append("\n")
                .append("Clients: ");

        if (clients.isEmpty()) {
            sb.append("No clients registered.");
        } else {
            for (Client client : clients) {
                sb.append(client.getName()).append(" (ID: ").append(client.getId()).append("), ");
            }
            // Remove the trailing comma and space
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private Instructor fetchInstructor(int instructorId) {
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
    private ArrayList<Client> fetchClients(ArrayList<Integer> clientIds) { //this should go in Booking
        ArrayList<Client> clients = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = null; // Declare the connection variable
        String query = "SELECT \"id\", \"name\", \"phone_number\", \"age\" FROM \"public\".\"clients\" WHERE \"id\" = ?";

        try {
            connection = connectToDb(); // Establish the connection
            stmt = connection.prepareStatement(query);

            for (int clientId : clientIds) {
                stmt.setInt(1, clientId);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String phoneNumber = rs.getString("phone_number");
                    int age = rs.getInt("age");

                    Client client = new Client(id, name, phoneNumber, age);
                    clients.add(client);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the ResultSet, PreparedStatement, and Connection
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close(); // Close the connection here
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clients;
    }

    public String getLegibleDate() {return startTime.format(dateFormatter); }
    public String getLegibleStartTime() { return startTime.format(timeFormatter); }
    public String getLegibleEndTime() { return endTime.format(timeFormatter); }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public ArrayList<Client> getClients() { return clients; }
    public void setClients(ArrayList<Client> clients) { this.clients = clients; }

}

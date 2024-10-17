package PackageImportantObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Offering {
    private int id;
    private String city;
    private String location;
    private int capacity;
    private boolean available;
    private String mode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Instructor instructor; //an offering could be without an instructor, specifically when it is first created
    private ArrayList<Client> clients = new ArrayList<Client>();
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    // when Admin creates a new offering, it has no instructor nor clients
    public Offering(String city, String location, int capacity, LocalDateTime startTime, LocalDateTime endTime) {
        this.city = city;
        this.location = location;
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
    public Offering(String city, String location, int capacity, LocalDateTime startTime, LocalDateTime endTime, int instructorId, int[] clientIds) {
        this.city = city;
        this.location = location;
        this.mode = capacity == 1 ? "private" : "group";
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.instructor = fetchInstructor(instructorId);
        this.clients = fetchClients(clientIds);
        this.available = this.capacity > this.clients.size();
    }
    public void setInstructor(Instructor instructor) {
        //should have code to associate an instructor with this booking via an ID
        int instructorId = instructor.getId();
        this.instructor = instructor;
    }
    private Instructor fetchInstructor(int instructorId) {
        return new Instructor(); //this should get the instructor via the ID
    }
    private ArrayList<Client> fetchClients(int[] clientIds) {
        return new ArrayList<Client>(); // this should return the client associated with this booking
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

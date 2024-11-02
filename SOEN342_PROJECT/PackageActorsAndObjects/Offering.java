package PackageActorsAndObjects;

import java.time.LocalDateTime;


public class Offering {
    private int id;
    private String city;
    private String location;
    private String classType;
    private int capacity;
    private boolean available;
    private String mode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int instructorId;
    private Instructor instructor; //an offering could be without an instructor, specifically when it is first created
    public Offering() {}

    //for viewing and creating offerings...doesn't include Instructor so things aren't slow
    public Offering(int id, String city, String location, String classType, int capacity, LocalDateTime startTime, LocalDateTime endTime, int instructorId, boolean getInstructor) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.classType = classType;
        this.mode = capacity == 1 ? "private" : "group";
        this.capacity = capacity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = true;
        this.instructorId = instructorId;
        if (getInstructor) {
            this.instructor = Instructor.fetchInstructorById(instructorId);
        }
    }
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
    public String getClassType() { return classType; }
    public void setClassType(String classType) { this.classType = classType; }
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public Instructor getInstructor() { return instructor; }
    public void setInstructor(Instructor i) { this.instructor = i; }


}

package PackageActors;

public class Instructor extends Actor {
    private String phoneNumber;
    private String specialty;
    private String[] cities;
    //private Booking[] bookings;

    public Instructor(String name, String phoneNumber) {
        super(name);
        this.phoneNumber = phoneNumber;
        //this.cities = getCities
        //this.bookings = getBookings()
    }
    public String getName() {
        return this.getName();
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
    public String[] getCities() {
        return cities;
    }
    public void setCities(String[] cities) {
        this.cities = cities;
    }
}

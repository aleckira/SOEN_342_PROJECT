package PackageImportantObjects;

public class Instructor extends Actor {
    private int id;
    private String phoneNumber;
    private String specialty;
    private String[] cities;
    public Instructor() {}
    public Instructor(int id, String name, String phoneNumber, String specialty, String[] cities) {
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.specialty = specialty;
        this.cities = cities;
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
    public String[] getCities() {
        return cities;
    }
    public void setCities(String[] cities) {
        this.cities = cities;
    }
}

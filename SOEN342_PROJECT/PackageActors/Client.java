package PackageActors;

public class Client extends Actor {
    private String phoneNumber;
    private int age;
    //private int Booking[] bookings
    public Client(String name, String phoneNumber, int age) {
        super(name);
        this.phoneNumber = phoneNumber;
        this.age = age;
        //this.bookings = getBookings()
    }
    public String getName() {
        return this.getName();
    }
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

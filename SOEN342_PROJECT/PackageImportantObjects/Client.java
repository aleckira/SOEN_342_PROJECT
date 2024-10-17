package PackageImportantObjects;

import java.util.ArrayList;

public class Client extends Actor {
    private int id;
    private String phoneNumber;
    private int age;
    private ArrayList<Offering> bookings = new ArrayList<Offering>();
    public Client() {}
    public Client(int id, String name, String phoneNumber, int age) { // if a new Client Registers
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.age = age;

    }
    public Client(int id, String name, String phoneNumber, int age, int[] bookingIds) { // if a Client logs in, we use bookingIds to find the ids
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.bookings = fetchBookings(bookingIds);

    }
    private ArrayList<Offering> fetchBookings(int[] bookingIds) {
        return new ArrayList<Offering>(); // fetches a client's bookings
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

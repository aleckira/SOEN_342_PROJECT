package PackageActorsAndObjects;

import java.util.ArrayList;

public class Admin extends Actor {
    private final String password;
    private static Admin instance;  // Static variable to hold the single instance of Admin

    // Private constructor to prevent instantiation from outside
    private Admin(String name, String password) {
        super(name);
        this.password = password;
    }

    // Public method to provide access to the single instance
    public static Admin getInstance(String name, String password) {
        if (instance == null) {
            instance = new Admin(name, password);
        }
        return instance;
    }

    @Override
    public ArrayList<Offering> getOfferingsForViewing() {
        return null;
    }
    public ArrayList<Booking> getAllBookingsForViewing() {
        return null;
    }
    public void enterNewOffering(Offering o) {
    }
    public void deleteOffering(int offeringId) {
    }
    public void deleteBooking(int bookingId) {
    }
    public void deleteInstructor(int instructorId) {
    }
    public void deleteClient(int clientId) {
    }
    public void editOffering(Offering o) {
    }
    public void editBooking(Booking b) {
    }
    public String getPassword() {
        return password;
    }
}

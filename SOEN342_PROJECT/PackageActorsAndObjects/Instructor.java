package SOEN342_PROJECT.PackageActorsAndObjects;

import java.util.ArrayList;

public class Instructor extends Actor {
    private int id;
    private String phoneNumber;
    private String specialty;
    private ArrayList<String> cities;
    public Instructor() {}
    public Instructor(int id, String name, String phoneNumber, String specialty, ArrayList<String> cities) {
        super(name);
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.specialty = specialty;
        this.cities = cities;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Instructor ID: ").append(id)
                .append(", Name: ").append(getName()) // Assuming getName() is defined in the Actor class
                .append(", Phone Number: ").append(phoneNumber)
                .append(", Specialty: ").append(specialty)
                .append(", Cities: [");

        for (String city : cities) {
            sb.append(city).append(", ");
        }

        // Remove the last comma and space if cities are not empty
        if (!cities.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("]");
        return sb.toString();
    }
    @Override
    public ArrayList<Offering> getOfferingsForViewing() {
        return null;
    }
    public void takeOffering(int offeringId) {
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
    public ArrayList<String> getCities() {
        return cities;
    }
    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }
}

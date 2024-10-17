package GeneralServices;

import PackageActors.Client;
import PackageActors.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static GeneralServices.DbConnectionService.connectToDb;

public class RegisterService {
    public static Instructor registerInstructor(String name, String phoneNumber, String specialty, String cities) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (!phoneNumber.matches("\\d{15}") || !isPhoneNumberUnique(phoneNumber, "instructor")) {
            return null;
        }
        if (cities.trim().isEmpty()) {
            return null;
        }

        String[] citiesArray = cities.split(",\\s*"); // Split cities by comma and trim whitespace

        String query= "INSERT INTO \"public\".\"instructors\" (\"name\", \"phone_number\", \"specialty\", \"cities\") VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, specialty);
            stmt.setArray(4, connection.createArrayOf("text", citiesArray));
            stmt.executeUpdate();
            Instructor a = new Instructor(name, phoneNumber, specialty, citiesArray);
            return a;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    public static Client registerClient(String name, String phoneNumber, String age) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (!phoneNumber.matches("\\d{15}") || !isPhoneNumberUnique(phoneNumber, "client")) {
            return null;
        }
        if (!age.trim().matches("\\d+") || Integer.parseInt(age) <= 0) {
            return null;
        }
        int ageInt = Integer.parseInt(age);

        String query= "INSERT INTO \"public\".\"clients\" (\"name\", \"phone_number\", \"age\") VALUES (?, ?, ?)";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setInt(3, ageInt);
            stmt.executeUpdate();
            Client c = new Client(name, phoneNumber, ageInt);
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    private static boolean isPhoneNumberUnique(String phoneNumber, String typeOfActor) {
        String query = typeOfActor == "client" ? "SELECT COUNT(*) FROM \"public\".\"clients\" WHERE \"phone_number\" = ?" :
                "SELECT COUNT(*) FROM \"public\".\"instructors\" WHERE \"phone_number\" = ?";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, phoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}

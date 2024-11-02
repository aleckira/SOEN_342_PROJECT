package Services;


import PackageActorsAndObjects.Client;
import PackageActorsAndObjects.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static Services.DbConnectionService.connectToDb;


public class RegisterService {
    public static boolean registerInstructor(String name, String phoneNumber, String specialty, String cities) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (!phoneNumber.matches("\\d{15}") || !isPhoneNumberUnique(phoneNumber, "instructor")) {
            return false;
        }
        if (cities.trim().isEmpty()) {
            return false;
        }

        String[] citiesArray = cities.split(",\\s*");

        String query = "INSERT INTO \"public\".\"instructors\" (\"name\", \"phone_number\", \"specialty\", \"cities\") VALUES (?, ?, ?, ?) RETURNING \"id\"";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setString(3, specialty);
            stmt.setArray(4, connection.createArrayOf("text", citiesArray));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ArrayList<String> citiesArrList = new ArrayList<>(Arrays.asList(citiesArray));
                int id = rs.getInt("id");
                Instructor i = new Instructor(id, name, phoneNumber, specialty, citiesArrList);
                UserSession.setCurrentUserRole("instructor", i);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean registerClient(String name, String phoneNumber, String age) throws SQLException {

        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (!phoneNumber.matches("\\d{15}") || !isPhoneNumberUnique(phoneNumber, "client")) {
            return false;
        }
        if (!age.trim().matches("\\d+") || Integer.parseInt(age) <= 0 || Integer.parseInt(age) < 18) {
            return false;
        }

        int ageInt = Integer.parseInt(age);

        String query = "INSERT INTO \"public\".\"clients\" (\"name\", \"phone_number\", \"age\") VALUES (?, ?, ?) RETURNING \"id\"";

        try (Connection connection = connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            stmt.setInt(3, ageInt);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");

                Client c = new Client(id, name, phoneNumber, ageInt);
                UserSession.setCurrentUserRole("client", c);
                return true;
            } else {
                return false;
            }
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

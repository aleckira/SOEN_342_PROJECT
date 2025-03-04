package Services;

import PackageActorsAndObjects.Admin;
import PackageActorsAndObjects.Client;
import PackageActorsAndObjects.Guardian;
import PackageActorsAndObjects.Instructor;

import java.sql.*;
import java.util.ArrayList;

public class LoginService {

    public static boolean loginAdmin(String name, String password) {
        String query = "SELECT * FROM admin WHERE name = ? AND password = ?";
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Admin admin = new Admin(rs.getString("name"), password);
                    UserSession.setCurrentUserRole("admin", admin);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginClient(String name, String phoneNumber) {
        String query = "SELECT * FROM clients WHERE name = ? AND phone_number = ?";
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(rs.getInt("id"), rs.getString("name"), rs.getString("phone_number"), rs.getInt("age"));
                    UserSession.setCurrentUserRole("client", client);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean loginInstructor(String name, String phoneNumber) {
        String query = "SELECT * FROM instructors WHERE name = ? AND phone_number = ?";
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Array cityArray = rs.getArray("cities");

                    ArrayList<String> cities = new ArrayList<>();
                    if (cityArray != null) {
                        String[] cityList = (String[]) cityArray.getArray();
                        for (String city : cityList) {
                            cities.add(city);
                        }
                    }

                    Instructor instructor = new Instructor(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("phone_number"),
                            rs.getString("specialty"),
                            cities);

                    UserSession.setCurrentUserRole("instructor", instructor);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean loginGuardian(String name, String phoneNumber) {
        String query = "SELECT * FROM guardians WHERE name = ? AND phone_number = ?";
        try (Connection connection = DbConnectionService.connectToDb();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Guardian guardian = new Guardian(rs.getInt("id"), rs.getString("name"), rs.getString("phone_number"), rs.getInt("age"));
                    UserSession.setCurrentUserRole("guardian", guardian);
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

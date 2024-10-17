package GeneralServices;

import PackageImportantObjects.Admin;
import PackageImportantObjects.Client;
import PackageImportantObjects.Instructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static GeneralServices.DbConnectionService.connectToDb;

public class LoginService {
    public static Admin loginAdmin(String name, String password) {
        PreparedStatement stmt = null;
        String query = "SELECT \"name\", \"password\" FROM \"admin\" LIMIT 1";
        ResultSet rs = null;
        String adminName = null;
        String adminPassword = null;

        try {
            Connection connection = connectToDb();
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                adminName = rs.getString("name");
                adminPassword = rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (adminName != null && adminPassword != null && name.equals(adminName) && password.equals(adminPassword)) {
            return new Admin(name, password);
        }
        return null;
    }
    public static Instructor loginInstructor(String name, String phoneNumber) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT \"id\", \"name\", \"phone_number\", \"specialty\", \"cities\" FROM \"public\".\"instructors\" WHERE \"name\" = ? AND \"phone_number\" = ?";

        try {
            Connection connection = connectToDb();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String instructorName = rs.getString("name");
                String instructorPhoneNumber = rs.getString("phone_number");
                String specialty = rs.getString("specialty");
                String cities = rs.getString("cities");

                // Create and return an Instructor object
                return new Instructor(id, instructorName, instructorPhoneNumber, specialty, cities.split(",\\s*")); // Assuming cities are stored as a comma-separated string
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Client loginClient(String name, String phoneNumber) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT \"id\", \"name\", \"phone_number\", \"age\", \"booking_ids\" FROM \"public\".\"clients\" WHERE \"name\" = ? AND \"phone_number\" = ?";

        try {
            Connection connection = connectToDb();
            stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, phoneNumber);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String clientName = rs.getString("name");
                String clientPhoneNumber = rs.getString("phone_number");
                int age = rs.getInt("age");
                Integer[] bookingIdsArray = (Integer[]) rs.getArray("booking_ids").getArray();

                int[] bookingIds = new int[bookingIdsArray.length];
                for (int i = 0; i < bookingIdsArray.length; i++) {
                    bookingIds[i] = bookingIdsArray[i];
                }

                return new Client(id, clientName, clientPhoneNumber, age, bookingIds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null; // Return null if no client was found
    }




}

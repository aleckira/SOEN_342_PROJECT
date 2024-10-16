package src.project342;

import PackageActors.Admin;

import java.sql.*;

public class InitialDbFunctions {
    private static Connection connection;
    private static void connectToDb() {
        Connection conn = null;
        String url = "jdbc:postgresql://db342-do-user-13923136-0.g.db.ondigitalocean.com:25060/defaultdb?sslmode=require";
        String user = "doadmin";
        String password = "AVNS_W1VGJX2LCLaI2p5l0wf";
        try {
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to PostgreSQL database");
            } else {
                System.out.println("Failed to connect to PostgreSQL database");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        connection = conn;
    }
    public static Connection getConnection() {
        if (connection == null) {
            connectToDb();
        }
        return connection;
    }
    public static Admin getAdmin(String name, String password) {
        PreparedStatement stmt = null;
        String query = "SELECT \"name\", \"password\" FROM \"admin\" LIMIT 1";
        ResultSet rs = null;
        String adminName = null;
        String adminPassword = null;

        try {
            Connection connection = getConnection();
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
        System.out.println(adminName + " " + adminPassword);
        return null;
    }
}

package src.project342;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbFunctions {
    public Connection connectToDb() {
        Connection conn = null;
        String url = "jdbc:postgresql://localhost:5432/SOEN342-Project";
        String user = "postgres";
        String password = "DataBahel420!";
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
        return conn;
    }
}

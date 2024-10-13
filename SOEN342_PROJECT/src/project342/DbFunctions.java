package SOEN342_PROJECT.src.project342;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbFunctions {
    public Connection connectToDb() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" +  "SOEN342-Project", "postgres", "DataBahel420!");
            if (conn != null) {
                System.out.println("Connected to PostgreSQL database");
            } else {
                System.out.println("Failed to connect to PostgreSQL database");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }
}

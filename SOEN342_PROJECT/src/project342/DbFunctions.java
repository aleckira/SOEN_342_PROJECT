package src.project342;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbFunctions {
    public Connection connectToDb() {
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
        return conn;
    }
}

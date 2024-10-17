package GeneralServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionService {
    private static Connection connection;
    public static Connection connectToDb() {
        String url = "jdbc:postgresql://db342-do-user-13923136-0.g.db.ondigitalocean.com:25060/defaultdb?sslmode=require";
        String user = "doadmin";
        String password = "AVNS_W1VGJX2LCLaI2p5l0wf";
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
            } else {
                System.out.println("Failed to connect to PostgreSQL database");
            }
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
        return connection;
    }
}

package GeneralServices;

import PackageActors.Admin;

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
}

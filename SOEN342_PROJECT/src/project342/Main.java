package src.project342;

import PackageUI.LoginForm;
import PackageUI.LoginPage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
		DbFunctions db = new DbFunctions();
		PreparedStatement stmt = null;
		String query = "SELECT \"offerings\".\"class_type\" FROM \"offerings\"";
		ResultSet rs = null;

		try {
			Connection connection = db.connectToDb();
			stmt = connection.prepareStatement(query);
			rs = stmt.executeQuery();
			System.out.println("Available Offerings:");
			while (rs.next()) {
				String data = rs.getString("class_type");
				System.out.println(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginPage(); // Create and display the login page
				//new LoginForm("instructor");
				//new Offering();  // Open the Offering interface
				//new InstructorOfferingsPage(); // Open the Offering interface for instructors
				//new UserOfferingsPage(); // Open the Offering interface for the public
			}
		});
	}
}

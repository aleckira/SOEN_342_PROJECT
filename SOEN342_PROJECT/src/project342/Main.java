package src.project342;

import PackageUI.LoginForm;
import PackageUI.LoginPage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
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
package src.project342;

import PackageActorsAndObjects.Client;
import PackageUI.GeneralUI.LoginPage;

import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new LoginPage(); // Create and display the login page
			}

		});
	}
}
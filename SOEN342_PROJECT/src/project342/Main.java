package SOEN342_PROJECT.src.project342;
import src.project342.DbFunctions;

import javax.swing.*;
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
			while (rs.next()) {
				String data = rs.getString("class_type");
				System.out.println(data);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

}

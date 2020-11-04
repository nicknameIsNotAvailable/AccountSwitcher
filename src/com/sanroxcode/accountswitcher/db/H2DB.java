package com.sanroxcode.accountswitcher.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DB {

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bundle", java.util.Locale.getDefault());
		;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:~/accswitch;DB_CLOSE_DELAY=-1", "sa", "sa");
			// createTableUsers();
			// createTableCountries(conn);
			// createTableUsers(conn);
			return conn;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ClassNotFoundException("*" + bundle.getString("h2db.driverError") + "\n*" + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + bundle.getString("h2db.connectionError") + "\n*" + e.getMessage());
		}
	}

}

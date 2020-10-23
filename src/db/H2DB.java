package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DB {

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("org.h2.Driver");
			conn = DriverManager.getConnection("jdbc:h2:~/accswitch;DB_CLOSE_DELAY=-1", "sa", "sa");
			// createTableUsers();
			//createTableCountries(conn);
			//createTableUsers(conn);
			return conn;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
		
		return null;
	}

}

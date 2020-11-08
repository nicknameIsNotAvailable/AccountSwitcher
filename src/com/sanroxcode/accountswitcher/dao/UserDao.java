package com.sanroxcode.accountswitcher.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sanroxcode.accountswitcher.db.H2DB;
import com.sanroxcode.accountswitcher.dto.User;

public class UserDao {
	private static final String CREATE_USER_TABLE_PASS = getMaintenancePass("createUserTable");
	private static final String FIX_USER_TABLE_PASS = getMaintenancePass("fixUserTable");
	private String cmd = "";

	public UserDao() {
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			createTableUsers();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public boolean flyToVenus(String selfDestructionCommand) throws ClassNotFoundException, SQLException {
		if (selfDestructionCommand.equals(CREATE_USER_TABLE_PASS)) {
			this.cmd = selfDestructionCommand;
			createTableUsers();
			System.out.println("FINISHED...");
			System.exit(0);
		}
		if (selfDestructionCommand.equals(FIX_USER_TABLE_PASS)) {
			this.cmd = selfDestructionCommand;
			maintenanceUser();
			System.out.println("FINISHED...");			
		}

		return false;

	}

	public User findByUsername(String username) throws SQLException, ClassNotFoundException {
		Statement stmt = null;
		User user = null;
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();
			String sql = "select platform, domain, username, alias from users where LOWER(username) = '"
					+ username.toLowerCase() + "'";
			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println("select username");
			while (rs.next()) {
				user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));

				System.out.println(
						rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3) + ":" + rs.getString(4));
			}

			return user;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void insert(User user) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			String sql = "insert into users (platform, domain, username, alias) ";
			sql += "values('".concat(user.getPlatform()).concat("','").concat(user.getCountry()).concat("','")
					.concat(user.getUserName()).concat("','").concat(user.getAlias()).concat("')");

			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("userDao.userInsertError") + "\n*" + e.getMessage());

		} catch (ClassNotFoundException e) {
			throw new Error(e.getMessage());

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException("*" + texto("userDao.userInsertError") + "\n*" + e.getMessage());
				}
			}
		}
	}

	public void delete(String username) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			String sql = "delete from users where lower(username) = '" + username.toLowerCase() + "'";

			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("userDao.userDeleteError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException("*" + texto("userDao.userDeleteError") + "\n*" + e.getMessage());
				}
			}
		}
	}

	public void update(User user) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			String sql = "update users set alias = '" + user.getAlias() + "' where lower(username) = '"
					+ user.getUserName().toLowerCase() + "'";

			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("userDao.userUpdateError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException("*" + texto("userDao.userUpdateError") + "\n*" + e.getMessage());
				}
			}
		}

	}

	public List<User> findAll() throws SQLException, ClassNotFoundException {
		Statement stmt;
		Connection conn = null;
		ArrayList<User> users = null;
		try {
			users = new ArrayList<User>();

			conn = H2DB.getConnection();

			stmt = conn.createStatement();
			String sql = "select platform, domain, username, alias from users";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

				/*
				 * System.out.println( rs.getString(1) + ":" + rs.getString(2) + ":" +
				 * rs.getString(3) + ":" + rs.getString(4));
				 */
			}

			return users;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("userDao.findAllError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException("*" + texto("userDao.findAllError") + "\n*" + e.getMessage());
				}
			}
		}
	}

	private void createTableUsers() throws ClassNotFoundException, SQLException {
		Statement stmt;
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			if (CREATE_USER_TABLE_PASS.equals(cmd)) {
				String drop = "drop table users";
				stmt.executeUpdate(drop);
				System.out.println("Recreating Users...");
			}

			String sql = "create table if not exists users" + "(platform varchar(100)," + "domain varchar(3),"
					+ "username varchar(100)," + "alias varchar(100)" + ")";
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("userDao.createTableUsersError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException("*" + texto("userDao.createTableUsersError") + "\n*" + e.getMessage());
				}
			}
		}

	}

	private String texto(String stringToGet) {
		return java.util.ResourceBundle.getBundle("bundle", java.util.Locale.getDefault()).getString(stringToGet);
	}

	private static String getMaintenancePass(String property) {
		Properties prop = new Properties();
		InputStream isReader = UserDao.class.getClassLoader().getResourceAsStream("maintenance.properties");
		if (isReader != null) {
			try {
				prop.load(isReader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("maintenance.properties file not found.");
		}

		return prop.getProperty(property);

	}

	private void maintenanceUser() throws SQLException, ClassNotFoundException {

		if (!FIX_USER_TABLE_PASS.equals(cmd))
			return;

		Statement stmt;
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			System.out.println("Try to fix Users...");
			System.out.println("Dropping temp table...");
			String drop = "drop table if exists tempUsers";
			stmt.executeUpdate(drop);

			System.out.println("Re/Creating temp table...");
			String sql = "create local temporary table tempUsers(platform varchar(100), domain varchar(3), username varchar(100), alias varchar(100))";
			stmt.executeUpdate(sql);

			System.out.println("Populating temp table...");
			sql = "insert into tempUsers( platform, domain, username, alias) "
					+ "select  platform, domain, username, alias from users";
			stmt.executeUpdate(sql);

			// trick
			String aux = cmd;
			cmd = CREATE_USER_TABLE_PASS;
			createTableUsers();

			cmd = aux;

			System.out.println("Populating users table...");
			sql = "insert into users( platform, domain, username, alias) "
					+ "select  platform, domain, username, alias from tempUsers";
			stmt.executeUpdate(sql);

			System.out.println("Dropping temp table...");
			drop = "drop table if exists tempUsers";
			stmt.executeUpdate(drop);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("userDao.createTableUsersError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException("*" + texto("userDao.createTableUsersError") + "\n*" + e.getMessage());
				}
			}
		}

	}

}
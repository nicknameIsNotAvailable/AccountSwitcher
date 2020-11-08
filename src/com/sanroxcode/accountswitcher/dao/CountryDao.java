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
import com.sanroxcode.accountswitcher.dto.Country;

public class CountryDao {
	private static final String CREATE_COUNTRY_TABLE_PASS = getMaintenancePass("createCountryTable");
	private String cmd = "";

	public CountryDao() {
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			createTableCountries();
			populateCountries();
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
		if (selfDestructionCommand.equals(CREATE_COUNTRY_TABLE_PASS)) {			
			this.cmd = selfDestructionCommand;
			System.out.println("Recreating countries table...");
			createTableCountries();
			populateCountries();
			System.out.println("FINISHED...");
		}

		return false;
	}

	public Country findByCountryName(String name) throws ClassNotFoundException, SQLException {
		Statement stmt = null;
		Country country = null;
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();
			String sql = "select domain, name, flag from countries where LOWER(name) = '" + name.toLowerCase() + "'";
			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println("select username");
			while (rs.next()) {
				country = new Country(rs.getString(1), rs.getString(2), rs.getString(3));

				System.out.println(rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getString(3));
			}

			return country;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("frmAccountSwitcher.countryDoesNotExist") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new SQLException(
							"*" + texto("frmAccountSwitcher.countryDoesNotExist") + "\n*" + e.getMessage());
				}
			}
		}
	}

	public List<Country> findAll() throws ClassNotFoundException, SQLException {
		Statement stmt;
		Connection conn = null;
		ArrayList<Country> countries = null;
		try {
			countries = new ArrayList<Country>();
			conn = H2DB.getConnection();
			stmt = conn.createStatement();
			String sql = "select domain, name, flag from countries";
			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println("select all");
			while (rs.next()) {
				countries.add(new Country(rs.getString(1), rs.getString(2), rs.getString(3)));
				/*
				 * System.out.println( rs.getString(1) + ":" + rs.getString(2) + ":" +
				 * rs.getString(3) + ":" + rs.getString(4));
				 */
			}

			return countries;

		} catch (SQLException e) {
			throw new SQLException("*" + texto("countryDao.findAllError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(e.getMessage());
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

	private void createTableCountries() throws ClassNotFoundException, SQLException {

		Connection conn = null;
		Statement stmt;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			if (cmd.equals(CREATE_COUNTRY_TABLE_PASS)) {
				String drop = "drop table countries";
				stmt.executeUpdate(drop);
			}

			String sql = "create table if not exists countries" + "(domain varchar(3)," + "name varchar(100),"
					+ "flag varchar(100))";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException("*" + texto("countryDao.createTableCountriesError") + "\n*" + e.getMessage());
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException(
							"*" + texto("countryDao.createTableCountriesError") + "\n*" + e.getMessage());
				}
			}
		}
	}

	private void populateCountries() throws SQLException {
		Statement stmt;
		Connection conn = null;
		try {
			conn = H2DB.getConnection();

			stmt = conn.createStatement();
			String sql = "select * from countries";
			stmt.executeQuery(sql);
			ResultSet rs = stmt.getResultSet();

			if (rs.next() == true)
				return;

			sql = "insert into countries values('ar', 'Argentina', 'ar.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('br', 'Brazil', 'br.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('mx', 'Mexico', 'mx.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('pe', 'Peru', 'pe.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('ru', 'Russia', 'ru.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('tr', 'Turkey', 'tr.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('us', 'USA', 'us.png')";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Error(e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new SQLException(
							"*" + texto("countryDao.createTableCountriesError") + "\n*" + e.getMessage());
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
}

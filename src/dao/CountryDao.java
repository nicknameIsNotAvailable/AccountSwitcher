package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.H2DB;
import dto.Country;

public class CountryDao {

	public CountryDao() {
		Connection conn = null;
		try {
			conn = H2DB.getConnection();
			createTableCountries(conn);
			populateCountries(conn);
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

	public Country findByCountryName(String name) {
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

	public List<Country> findAll() {
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

	private void createTableCountries(Connection conn) {

		Statement stmt;
		try {

			stmt = conn.createStatement();
			// String drop = "drop table countries";
			// stmt.executeUpdate(drop);

			String sql = "create table if not exists countries" + "(domain varchar(3)," + "name varchar(100),"
					+ "flag varchar(100))";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void populateCountries(Connection conn) {
		Statement stmt;
		try {
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
			sql = "insert into countries values('ru', 'Russia', 'ru.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('tr', 'Turkey', 'tr.png')";
			stmt.executeUpdate(sql);
			sql = "insert into countries values('us', 'USA', 'us.png')";
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}

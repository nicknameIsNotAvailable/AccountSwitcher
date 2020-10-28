package com.sanroxcode.accountswitcher.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.sanroxcode.accountswitcher.db.H2DB;
import com.sanroxcode.accountswitcher.dto.User;

public class UserDao {
	private String cmd = "-GetRootFromNASAFromArea51";
	private String cmd2 = "";

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

	public boolean flyToVenus(String selfDestructionCommand) {
		if (!selfDestructionCommand.equals(this.cmd))
			return false;
		this.cmd2 = selfDestructionCommand;
		createTableUsers();
		System.out.println("...");
		System.exit(0);

		return false;

	}

	public User findByUsername(String username) {
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

	public void insert(User user) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			String sql = "insert into users (platform, domain, username, alias) ";
			sql += "values('".concat(user.getPlatform()).concat("','").concat(user.getCountry()).concat("','")
					.concat(user.getUserName()).concat("','").concat(user.getAlias()).concat("')");

			stmt.executeUpdate(sql);
			// System.out.println(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void delete(String username) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			String sql = "delete from users where lower(username) = '" + username.toLowerCase() + "'";

			stmt.executeUpdate(sql);
			// System.out.println(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void update(User user) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = H2DB.getConnection();
			stmt = conn.createStatement();

			String sql = "update users set alias = '" + user.getAlias() + "' where lower(username) = '"
					+ user.getUserName().toLowerCase() + "'";

			stmt.executeUpdate(sql);
			// System.out.println(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public List<User> findAll() {
		Statement stmt;
		Connection conn = null;
		ArrayList<User> users = null;
		try {
			users = new ArrayList<User>();
			conn = H2DB.getConnection();
			stmt = conn.createStatement();
			String sql = "select platform, domain, username, alias, 6 from users";
			ResultSet rs = stmt.executeQuery(sql);
			// System.out.println("select all");
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

	private void createTableUsers() {
		Statement stmt;
		Connection conn = H2DB.getConnection();
		try {
			stmt = conn.createStatement();

			if (cmd.equals(cmd2)) {
				String drop = "drop table users";
				stmt.executeUpdate(drop);
				System.out.println("Recreating Users...");
			}

			String sql = "create table if not exists users" + "(platform varchar(100)," + "domain varchar(3),"
					+ "username varchar(100)," + "alias varchar(100)" + ")";
			stmt.executeUpdate(sql);
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

}
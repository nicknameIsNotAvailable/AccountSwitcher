package com.sanroxcode.accountswitcher.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

import com.sanroxcode.accountswitcher.dao.UserDao;
import com.sanroxcode.accountswitcher.dto.User;

public class UserController {
	private final ArrayList<User> listUsers = new ArrayList<User>();
	private final UserDao userDao = new UserDao();
	private final java.util.ResourceBundle bundle;

	public UserController() throws Exception {
		java.util.Locale locale = Locale.getDefault();
		bundle = java.util.ResourceBundle.getBundle("bundle", locale);
		try {
			listUsersRefresh();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private boolean validateUser(User user) throws Error {
		String regexSteamUserName = "^[A-Za-z]\\w{3,19}$";

		if (user.getUserName().equals("") || !user.getUserName().matches(regexSteamUserName))
			throw new Error(texto("userController.invalidUsername"));

		if (listUsers.size() >= 12)
			throw new Error(texto("userController.userLimitReched"));

		if (user.getAlias().equals(""))
			throw new Error(texto("userController.invalidUserAlias"));

		return true;
	}

	public void add(User user) {

		if (!validateUser(user))
			return;

		if (exists(user.getUserName()))
			throw new Error(texto("userController.usernameAlreadyExists"));

		try {
			userDao.insert(user);
			listUsersRefresh();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	public void remove(String username) throws Exception {
		username = username.trim();

		if (username.equals(""))
			throw new Error(texto("userController.invalidUsername"));

		try {
			userDao.delete(username);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		listUsersRefresh();

	}

	public void remove(User user) throws Exception {
		if (user == null)
			throw new Error(texto("userController.invalidUserObject"));

		try {
			remove(user.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public void update(User user) throws Exception {
		if (user == null)
			throw new Error(texto("userController.invalidUserObject"));

		if (user.getAlias().equals(findByName(user.getUserName()).getAlias()))
			return;

		if (!validateUser(user))
			return;

		user.setAlias(user.getAlias().trim());
		try {
			userDao.update(user);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		listUsersRefresh(user, false);
	}

	private boolean exists(String username) {
		for (User user : listUsers) {
			if (user.getUserName().equals(username.trim()))
				return true;
		}
		return false;
	}

	private ArrayList<User> listUsersRefresh() throws Exception {

		listUsers.clear();
		try {
			listUsers.addAll(userDao.findAll());
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return listUsers;

	}

	private ArrayList<User> listUsersRefresh(User user, boolean syncDB) {
		if (syncDB)
			try {
				return listUsersRefresh();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}

		for (User u : listUsers) {
			if (user.getUserName().equals(u.getUserName())) {
				u.setAlias(user.getAlias());
				break;
			}
		}

		return listUsers;
	}

	/*
	 * public User findByName(String username) { User user = null; username =
	 * username.trim(); if (username.equals("")) throw new
	 * Error("Invalid username !!!");
	 * 
	 * user = userDao.findByUsername(username);
	 * 
	 * return user; }
	 */
	public User findByName(String username) {
		username = username.trim();
		for (User user : listUsers) {
			if (user.getUserName().equals(username))
				return user;
		}
		return null;
	}

	public ArrayList<User> getListUsers() {
		return listUsers;
	}

	public void maintain(String selfDestructionCommand) {
		try {
			if (userDao.flyToVenus(selfDestructionCommand))
				return;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private String texto(String bundleGetString) {
		return bundle.getString(bundleGetString);
	}
}

package com.sanroxcode.accountswitcher.controller;

import java.util.ArrayList;

import com.sanroxcode.accountswitcher.dao.UserDao;
import com.sanroxcode.accountswitcher.dto.User;

public class UserController {
	private final ArrayList<User> listUsers = new ArrayList<User>();
	private final UserDao userDao = new UserDao();

	public UserController() {
		listUsersRefresh();
	}

	private boolean validateUser(User user) throws Error {
		String regexSteamUserName = "^[A-Za-z]\\w{3,19}$";

		if (user.getUserName().equals("") || !user.getUserName().matches(regexSteamUserName))
			throw new Error("Invalid Username!!!");

		if (listUsers.size() >= 12)
			throw new Error("User limit reached!!!");

		if (user.getAlias().equals(""))
			throw new Error("Invalid Alias!!!");

		return true;
	}

	public void add(User user) throws Error {

		if (!validateUser(user))
			return;

		if (exists(user.getUserName()))
			throw new Error("Username Already exists!!!");

		userDao.insert(user);
		listUsersRefresh();
	}

	public void remove(String username) throws Error {
		username = username.trim();

		if (username.equals(""))
			throw new Error("Invalid User name...");

		userDao.delete(username);
		listUsersRefresh();

	}

	public void remove(User user) throws Error {
		if (user == null)
			throw new Error("Invalid User");

		remove(user.getUserName());
	}

	public void update(User user) throws Error {
		if (user == null)
			throw new Error("Invalid User");

		if (user.getAlias().equals(findByName(user.getUserName()).getAlias()))
			return;

		if (!validateUser(user))
			return;

		user.setAlias(user.getAlias().trim());
		userDao.update(user);

		listUsersRefresh(user, false);
	}

	private boolean exists(String username) {
		for (User user : listUsers) {
			if (user.getUserName().equals(username.trim()))
				return true;
		}
		return false;
	}

	private ArrayList<User> listUsersRefresh() {

		listUsers.clear();
		listUsers.addAll(userDao.findAll());
		return listUsers;

	}

	private ArrayList<User> listUsersRefresh(User user, boolean syncDB) {
		if (syncDB)
			return listUsersRefresh();

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
		if (userDao.flyToVenus(selfDestructionCommand))
			System.exit(0);
		
	}
}

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

	public void add(User user) throws Error {
		String regexSteamUserName = "^[A-Za-z]\\w{3,19}$";

		if (user.getUserName().equals("") || !user.getUserName().matches(regexSteamUserName))
			throw new Error("Invalid Username!!!");

		if (listUsers.size() >= 12)
			throw new Error("User limit reached!!!");

		if (exists(user.getUserName()))
			throw new Error("Username Already exists!!!");
		
		if (user.getAlias().equals(""))
			throw new Error("Invalid Alias!!!");
		
		user.setShortcutKey("F" + (listUsers.size() + 1));

		userDao.insert(user);
		listUsersRefresh();
	}

	public void remove(String username) throws Error {
		username = username.trim();

		if (username.equals(""))
			throw new Error("Invalid User name...");

		userDao.delete(username);
		update();
		listUsersRefresh();

	}

	public void remove(User user) throws Error {
		if (user == null)
			throw new Error("Invalid User");

		remove(user.getUserName());
	}
	
	//sync db with list and update db again
	private void update() {
		int i = 1;
		listUsersRefresh();
		for (User user: listUsers) {
			user.setShortcutKey("F" + i);
			i++;
			userDao.update(user);
		}		
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
}

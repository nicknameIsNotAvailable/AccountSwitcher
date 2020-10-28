package com.sanroxcode.accountswitcher.dto;

public class User {
	private String platform;
	private String country;
	private String userName;
	private String alias;

	public User() {
		platform = null;
		country = null;
		userName = null;		
	}

	public User(String platform, String country, String userName, String alias) {
		
		super();
		this.platform = platform.trim();
		this.country = country.trim();
		this.userName = userName.trim();

		if (alias == null)
			this.alias = "";
		else
			this.alias = alias.trim();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}

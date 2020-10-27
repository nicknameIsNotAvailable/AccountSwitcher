package com.sanroxcode.accountswitcher.dto;

public class User {
	private String platform;
	private String country;
	private String userName;
	private String shortcutKey;
	private String alias;

	public User() {
		platform = null;
		country = null;
		userName = null;
		shortcutKey = null;
	}

	public User(String platform, String country, String userName, String shortcutKey, String alias) {
		super();
		this.platform = platform.trim();
		this.country = country.trim();
		this.userName = userName.trim();

		if (shortcutKey == null)
			this.shortcutKey = "";
		else
			this.shortcutKey = shortcutKey.trim();

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

	public String getShortcutKey() {
		return shortcutKey;
	}

	public void setShortcutKey(String shortcutKey) {
		this.shortcutKey = shortcutKey;
	}

	@Override
	public String toString() {
		return "(" + shortcutKey + ") " + userName;

	}

	/**
	 * @param showAlias false -> concat FKey and username 
	 * 					true -> concat FKey and alias
	 */
	public String toString(boolean showAlias) {
		if (showAlias)
			return "(" + shortcutKey + ") " + alias;

		return toString();
	}

	public String toFullString() {
		return this.platform.concat(":").concat(this.country).concat(":").concat(this.userName).concat(":")
				.concat(this.shortcutKey).concat(":").concat(this.alias);
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

}

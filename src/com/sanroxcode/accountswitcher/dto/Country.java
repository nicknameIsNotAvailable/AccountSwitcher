package com.sanroxcode.accountswitcher.dto;

public class Country {
	private String domain;
	private String name;
	private String flag;
	
	public Country(String domain, String name, String flag) {
		super();
		this.domain = domain;
		this.name = name;
		this.flag = flag;
	}

	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
}

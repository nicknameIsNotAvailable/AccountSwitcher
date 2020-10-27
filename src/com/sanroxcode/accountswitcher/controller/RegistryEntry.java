package com.sanroxcode.accountswitcher.controller;

abstract class RegistryEntry {
	private String registryAddress;
	private String registryKey;
	private String registryValue;

	public RegistryEntry() {
		this.registryAddress = null;
		this.registryKey = null;
		this.registryValue = null;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public String getRegistryKey() {
		return registryKey;
	}

	public String getRegistryValue(String regKey) {
		return registryValue;
	}

	public void setValue(String regKey, String regValue, String regType) throws Exception {
		throw new Exception("Method must be implemented!");
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public void setRegistryKey(String registryKey) {
		this.registryKey = registryKey;
	}
	
}

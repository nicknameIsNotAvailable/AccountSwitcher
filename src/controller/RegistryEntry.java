package controller;
abstract public class RegistryEntry {
	String registryAddress;
	String registryKey;
	String registryValue;
	
	protected RegistryEntry() {
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
}

package com.sanroxcode.accountswitcher.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SteamRegistryEntry extends RegistryEntry {
	private static final Logger logger = LogManager.getLogger(SteamRegistryEntry.class);

	public SteamRegistryEntry() {
		super();
		setRegistryAddress("HKCU\\SOFTWARE\\Valve\\Steam");
	}

	public String getRegistryValue(String regKey) {
		setRegistryKey(regKey);
		//this.registryKey = regKey;
		ProcessBuilder processBuilder = new ProcessBuilder();
		//logger.debug("Searching for steam registry value : " + registryAddress + "/" + registryKey);
		processBuilder.command("reg", "query", getRegistryAddress(), "/v", getRegistryKey());

		Process process;
		String retorno = null;

		try {
			process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			while ((line = reader.readLine()) != null) {
				// SteamExe REG_SZ j:/program files/steam/steam.exe
				line = line.trim().replaceAll("\\s+", "\t");
				if (line.contains("\t")) {
					String[] splitted = line.split("\t");
					String exePath = "";
					if (splitted.length > 3) {
						for (int i = 3; i <= splitted.length; i++) {
							exePath += " " + splitted[i - 1];
						}
					} else
						exePath = splitted[splitted.length - 1];

					retorno = exePath.trim();
					return retorno;
				}
			}
			reader.close();
			process.waitFor();
			// System.out.println("Get Value exit code : " + code +
			// "\n----------------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//logger.debug("Returning : " + retorno);
		}
		return retorno;
	}

	@Override
	public void setValue(String regKey, String regValue, String regType) {
		setRegistryKey(regKey);
			
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("reg", "add", getRegistryAddress(), "/v", getRegistryKey(), "/t", regType , "/d", regValue, "/f");
		Process process;

		try {
			//logger.debug("Setting value to steam registry value : " + regKey + " = " + regValue);
			process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.length() <= 0)
					continue;
				line = line.trim();
				//System.out.println(line);
			}
			reader.close();
			process.waitFor();
			process.destroy();
			// System.out.println("Set Value exit code : " + code +
			// "\n----------------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getSteamActiveUsername() {		
		String retorno = new SteamRegistryEntry().getRegistryValue("AutoLoginUser");
		if (retorno == null) {
			// System.out.println("Active username null");
			logger.debug("System exit : active username null");
			System.exit(1);
		}
		return retorno;
	}

	public static String getSteamExePath() {
		logger.debug("Searching for steam registry value...");
		String strPath = new SteamRegistryEntry().getRegistryValue("SteamExe");
		return strPath;
		// return strPath.replace("/", "\\");
	}

	public static String getSteamDirPath() {
		logger.debug("Searching for steam registry value...");
		String strPath = new SteamRegistryEntry().getRegistryValue("SteamPath");
		return strPath;
		// return strPath.replace("/", "\\");
	}
}

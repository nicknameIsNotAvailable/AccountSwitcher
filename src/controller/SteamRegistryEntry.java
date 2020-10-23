package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SteamRegistryEntry extends RegistryEntry {

	public SteamRegistryEntry() {
		super();
		this.registryAddress = "HKCU\\SOFTWARE\\Valve\\Steam";
	}

	@Override
	public String getRegistryValue(String regKey) {
		this.registryKey = regKey;
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("reg", "query", registryAddress, "/v", registryKey);

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
		}
		return retorno;
	}

	@Override
	public void setValue(String regKey, String regValue, String regType) {
		registryKey = regKey;
		registryValue = regValue;

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("reg", "add", registryAddress, "/v", registryKey, "/t", regType, "/d", regValue, "/f");
		Process process;

		try {
			process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.length() <= 0)
					continue;
				line = line.trim();
				System.out.println(line);
			}
			reader.close();
			process.waitFor();
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
			System.out.println("Active username null");
			System.exit(1);
		}
		/*
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("retorno = " + retorno);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		*/
		return retorno;
	}

	public static String getSteamExePath() {
		return new SteamRegistryEntry().getRegistryValue("SteamExe");
	}
}

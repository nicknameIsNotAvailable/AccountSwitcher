package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SteamProcess {
	String steamExePath = null;

	public SteamProcess(String exePath) {
		steamExePath = exePath;
	}

	public void start() {
		close(); // close previous opened instance

		ProcessBuilder processBuilder = new ProcessBuilder();
		System.out.println("Running steam instance...");
		processBuilder.command("cmd.exe", "/c", "start", "steam://open/main");
		go(processBuilder, true);
	}

	private void close() {
		if (isSteamRunning()) {
			System.out.println("Closing another steam instance...");
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", steamExePath, "-shutdown");
			go(processBuilder, true);
		}
	}

	private static String go(ProcessBuilder processBuilder, boolean bolwait) {
		String retorno = null;
		try {
			Process process;
			process = processBuilder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			retorno = "";
			while ((line = reader.readLine()) != null) {
				if (line.length() <= 0)
					continue;
				line = line.trim();
				retorno += line;
				// System.out.println(line);
			}
			reader.close();

			process.waitFor();
			Thread.sleep(bolwait ? 1500 : 100);
			//System.out.println("Go exit code : " + code + "\n----------------------------");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retorno;
	}

	private static String listProcesses() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("tasklist.exe", "/fi", "imagename eq steam.exe", "/fi", "windowtitle eq steam*", "/fo",
				"csv", "/nh");
		return go(processBuilder, false);
	}

	public static boolean isSteamRunning() {
		String steamproc = listProcesses();
		if (steamproc.toLowerCase().contains("steam.exe")) {
			return true;
		}
		return false;
	}

}

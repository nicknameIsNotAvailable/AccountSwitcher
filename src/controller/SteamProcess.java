package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SteamProcess {
	private String steamExePath = null;
	private static final Logger logger = LogManager.getLogger(SteamProcess.class);
	private static final String osName = System.getProperty("os.name");

	public SteamProcess(String exePath) {
		steamExePath = exePath;
		// steamDirPath = dirPath;
	}

	public void start() throws InterruptedException, IOException {

		// close(); // close previous opened instance

		// System.out.println("Running steam instance...");
		logger.debug("Starting new steam instance with sleep...");

		if (osName.toLowerCase().contains("windows 10")) {
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", "start", "steam://open/main");
			go(processBuilder, true);
		} else {
			String[] initParams = { steamExePath/* , "-silent" */ };
			Runtime.getRuntime().exec(initParams);
		}

	}

	public void close() throws InterruptedException {
		logger.debug("Looking for steam.exe in tasklist result");
		if (isSteamRunning()) {
			logger.debug("Closing previous steam instance...");
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command("cmd.exe", "/c", steamExePath, "-shutdown");
			logger.debug("Launching go(" + steamExePath + ") with sleep...");
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

			// System.out.println("Go exit code : " + code +
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

	private static String listProcesses() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("tasklist.exe", "/fi", "\"imagename eq steam.exe\"", "/fi", "\"windowtitle eq steam*\"",
				"/fo", "csv", "/nh");
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

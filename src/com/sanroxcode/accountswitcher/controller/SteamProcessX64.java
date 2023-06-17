package com.sanroxcode.accountswitcher.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sanroxcode.accountswitcher.util.SteamWindowTitleList;

public class SteamProcessX64 implements SteamProcess {
	private String steamExePath;

	private static final Logger logger = LogManager.getLogger(SteamProcessX64.class);

	public SteamProcessX64(String exePath) {
		steamExePath = exePath;
		// steamDirPath = dirPath;
	}

	@Override
	public void start() throws InterruptedException, IOException {

		// close(); // close previous opened instance
		// System.out.println("Running steam instance...");
		logger.debug("Starting new steam instance with sleep...");

		ProcessBuilder processBuilder = new ProcessBuilder();
		// processBuilder.command("cmd.exe", "/c", "start", "steam://open/main");
		processBuilder.command("cmd.exe", "/c", "start", "steam://open/games");
		go(processBuilder, true);

	}

	public void close() throws InterruptedException {
		logger.debug("Looking for steam.exe in tasklist result");
		if (isSteamRunning(true)) {
			// if (isSteamRunning(false)) {
			logger.debug("Closing previous steam instance...");
			ProcessBuilder processBuilder = new ProcessBuilder();
			// processBuilder.command("cmd.exe", "/c", steamExePath, "-shutdown");

			processBuilder.command(getSteamExePath(), "-shutdown");

			go(processBuilder, true);
		}
	}

	static String go(ProcessBuilder processBuilder, boolean bolwait) {
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

			if (bolwait)
				process.waitFor();

			Thread.sleep(100);

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

	private static String listProcesses(boolean checkWindowed) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		String retorno = "";
		if (checkWindowed) {			

			for (String titulo : SteamWindowTitleList.getWindowTitles()) {
				processBuilder.command("tasklist.exe", "/fi", "\"imagename eq steam.exe\"", "/fi",
						"\"windowtitle eq " + titulo + "*\"", "/fo", "csv", "/nh");
				retorno = retorno + go(processBuilder, false) + System.lineSeparator();
			}
			
			if (!retorno.contains("steam.exe")) {
				logger.warn("Steam window title not found. Forcing search without title...");
				processBuilder.command("tasklist.exe", "/fi", "\"imagename eq steam.exe\"", "/fi",
						"\"windowtitle ne \"\"", "/fo", "csv", "/nh");
				retorno = retorno + go(processBuilder, false) + System.lineSeparator();
			}
			
		} else {
			processBuilder.command("tasklist.exe", "/fi", "\"imagename eq steam.exe\"", "/fo", "csv", "/nh");
			retorno = go(processBuilder, false) + System.lineSeparator();
		}

		return retorno;
	}

	public static boolean isSteamRunning(boolean checkWindowed) {
		String steamproc = listProcesses(checkWindowed);
		if (steamproc.toLowerCase().contains("steam.exe")) {
			return true;
		}else
		
		return false;
	}

	public String getSteamExePath() {
		return steamExePath;
	}

	@Override
	public Logger logger() {
		return logger;
	}
}
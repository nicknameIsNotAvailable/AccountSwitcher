package com.sanroxcode.accountswitcher.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SteamProcessX86 implements SteamProcess {
	private String steamExePath;

	private static final Logger logger = LogManager.getLogger(SteamProcessX86.class);

	public SteamProcessX86(String exePath) {
		steamExePath = exePath;
		// steamDirPath = dirPath;
	}

	@Override
	public void start() throws InterruptedException, IOException {
		logger.debug("Starting new steam instance with sleep...");

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(steamExePath, "-nocache",
				"-noverifyfiles");/*
									 * .redirectErrorStream(true).redirectError(Redirect.INHERIT)
									 * .redirectOutput(Redirect.INHERIT);
									 */
		processBuilder.start();

	}

	public void close() throws InterruptedException {
		logger.debug("Looking for steam.exe in tasklist result");
		if (isSteamRunning(true)) {
			logger.debug("Closing previous steam instance...");
			ProcessBuilder processBuilder = new ProcessBuilder();
			processBuilder.command(steamExePath, "-shutdown");

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
		if (checkWindowed)
			processBuilder.command("tasklist.exe", "/fi", "\"imagename eq steam.exe\"", "/fi",
					"\"windowtitle eq steam*\"", "/fo", "csv", "/nh");
		else
			processBuilder.command("tasklist.exe", "/fi", "\"imagename eq steam.exe\"", "/fo", "csv", "/nh");

		return go(processBuilder, false);
	}

	public static boolean isSteamRunning(boolean checkWindowed) {
		String steamproc = listProcesses(checkWindowed);
		if (steamproc.toLowerCase().contains("steam.exe")) {
			return true;
		}
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
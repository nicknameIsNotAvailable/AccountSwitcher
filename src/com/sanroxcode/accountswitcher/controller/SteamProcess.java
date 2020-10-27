package com.sanroxcode.accountswitcher.controller;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

public interface SteamProcess {

	public abstract Logger logger();

	public abstract void start() throws InterruptedException, IOException;

	public abstract void close() throws InterruptedException;

	static String go(ProcessBuilder processBuilder, boolean boolWaitFor) {
		return null;
	}

	public static boolean isSteamRunning(boolean checkWindowed) {
		return false;
	}

}
package controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SteamProcessMonitor {
	public static boolean isAlive = false;
	private static final Logger logger = LogManager.getLogger(SteamProcessMonitor.class);	

	public static void go() {
		logger.debug("Starting thread to check if steam is running");
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						isAlive = SteamProcess.isSteamRunning();						
						
						//System.out.println("Steam isAlive : " + (isAlive ? "TRUE" : "FALSE") + "...");
						Thread.sleep(6);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
}

package controller;

public final class SteamProcessMonitor {
	public static boolean isAlive = false;

	public static void go() {
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						isAlive = SteamProcess.isSteamRunning();						
						
						//System.out.println("Steam isAlive : " + (isAlive ? "TRUE" : "FALSE") + "...");
						Thread.sleep(2000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
}

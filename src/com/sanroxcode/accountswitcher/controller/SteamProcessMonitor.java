package com.sanroxcode.accountswitcher.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class SteamProcessMonitor {
	public static boolean isAlive = false;
	private static final Logger logger = LogManager.getLogger(SteamProcessMonitor.class);

	public static void go(Class<? extends SteamProcess> c) {
		logger.debug("Starting thread to check if steam is running");
		new Thread() {
			@Override
			public void run() {
				try {
					boolean actualState = isAlive;
					while (true) {
						Method meth = c.getMethod("isSteamRunning", Boolean.TYPE);
						isAlive = (boolean) meth.invoke(null, false);
						
						if (actualState!=isAlive) {
							actualState = isAlive;
							Thread.sleep(5000);
						}
						
					}
				} catch (InterruptedException | NoSuchMethodException | SecurityException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public static void waitSteamProcessTerminate(Class<? extends SteamProcess> c) {
		try {
			Method meth = c.getMethod("isSteamRunning", Boolean.TYPE);
			while ((boolean) meth.invoke(null, false)) {
				// wait
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
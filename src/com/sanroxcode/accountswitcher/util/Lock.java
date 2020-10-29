package com.sanroxcode.accountswitcher.util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class Lock {

	public static void preventMultipleInstances() throws Error {
		String userHome = System.getProperty("user.home");
		File lockFile = new File(userHome, "accswitch.lock");
		java.util.Locale locale = Locale.getDefault();
		java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("bundle", locale);
		try {
			FileChannel fileChannel = FileChannel.open(lockFile.toPath(), StandardOpenOption.CREATE,
					StandardOpenOption.WRITE);
			FileLock lock = fileChannel.tryLock();
			if (lock == null) {
				throw new Error(bundle.getString("lock.justOneInstance"));
				// System.exit(0);
			}
			lockFile.deleteOnExit();
		} catch (IOException e) {
			System.exit(1);
		}
	}
}

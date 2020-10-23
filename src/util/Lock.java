package util;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

public class Lock {

	public static void preventMultipleInstances() throws Error {
		String userHome = System.getProperty("user.home");
		File lockFile = new File(userHome, "accswitch.lock");
		try {
			FileChannel fileChannel = FileChannel.open(lockFile.toPath(), StandardOpenOption.CREATE,
					StandardOpenOption.WRITE);
			FileLock lock = fileChannel.tryLock();
			if (lock == null) {
				throw new Error("Just one instance may run");
				// System.exit(0);
			}
			lockFile.deleteOnExit();
		} catch (IOException e) {
			System.exit(1);
		}
	}
}

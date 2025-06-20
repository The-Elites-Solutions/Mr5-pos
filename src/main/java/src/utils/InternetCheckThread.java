package src.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetCheckThread extends Thread {

	private static final int CHECK_INTERVAL = 60000; // Check every minute

	@Override
	public void run() {
		while (true) {
			if (isInternetAvailable()) {
				System.out.println("Internet is available.");
				// Trigger the backup process here
				MongoDBBackup.backupData();
			} else {
				System.out.println("Internet is not available.");
			}

			try {
				Thread.sleep(CHECK_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isInternetAvailable() {
		try {
			URL url = new URL("http://www.google.com");
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
			urlConnect.setConnectTimeout(5000);
			urlConnect.getContent();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}

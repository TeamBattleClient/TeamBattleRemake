package me.client.Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.client.Client;
import me.client.utils.Logger;

public final class UpdateCheckThread extends Thread {
	@Override
	public void run() {
		boolean success = false;
		Logger.logConsole("Update checking has begun, now checking for an update...");
		try {
			final URL url = new URL(
					"http://latematt.ml/latestversion.php?version="
							+ Client.getBuild());
			final HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection
					.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
			connection.connect();
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			
			success = true;
		} catch (final MalformedURLException e) {
			Logger.logConsole("Update checking failed! " + e.toString());
			e.printStackTrace();
		} catch (final IOException e) {
			Logger.logConsole("Update checking failed! " + e.toString());
			e.printStackTrace();
		}
		if (success) {
			Logger.logConsole("Update checking has finished. There "
					+ (Client.isNewerVersionAvailable() ? "is" : "is not")
					+ " a new update.");
		}
	}
}
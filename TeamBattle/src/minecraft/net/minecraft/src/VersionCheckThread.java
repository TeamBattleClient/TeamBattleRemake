package net.minecraft.src;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionCheckThread extends Thread {
	@Override
	public void run() {
		HttpURLConnection conn = null;

		try {
			Config.dbg("Checking for new version");
			final URL e = new URL("http://optifine.net/version/1.7.10/HD_U.txt");
			conn = (HttpURLConnection) e.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(false);
			conn.connect();

			try {
				final InputStream in = conn.getInputStream();
				final String verStr = Config.readInputStream(in);
				in.close();
				final String[] verLines = Config.tokenize(verStr, "\n\r");

				if (verLines.length < 1)
					return;

				final String newVer = verLines[0];
				Config.dbg("Version found: " + newVer);

				if (Config.compareRelease(newVer, "A4") > 0) {
					Config.setNewRelease(newVer);
					return;
				}
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		} catch (final Exception var11) {
			Config.dbg(var11.getClass().getName() + ": " + var11.getMessage());
		}
	}
}

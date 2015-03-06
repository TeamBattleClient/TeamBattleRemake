package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadLanServerPing extends Thread {
	private static final AtomicInteger field_148658_a = new AtomicInteger(0);

	private static final Logger logger = LogManager.getLogger();

	public static String getAdFromPingResponse(String p_77523_0_) {
		final int var1 = p_77523_0_.indexOf("[/MOTD]");

		if (var1 < 0)
			return null;
		else {
			final int var2 = p_77523_0_.indexOf("[/MOTD]",
					var1 + "[/MOTD]".length());

			if (var2 >= 0)
				return null;
			else {
				final int var3 = p_77523_0_.indexOf("[AD]",
						var1 + "[/MOTD]".length());

				if (var3 < 0)
					return null;
				else {
					final int var4 = p_77523_0_.indexOf("[/AD]",
							var3 + "[AD]".length());
					return var4 < var3 ? null : p_77523_0_.substring(var3
							+ "[AD]".length(), var4);
				}
			}
		}
	}

	public static String getMotdFromPingResponse(String p_77524_0_) {
		final int var1 = p_77524_0_.indexOf("[MOTD]");

		if (var1 < 0)
			return "missing no";
		else {
			final int var2 = p_77524_0_.indexOf("[/MOTD]",
					var1 + "[MOTD]".length());
			return var2 < var1 ? "missing no" : p_77524_0_.substring(var1
					+ "[MOTD]".length(), var2);
		}
	}

	public static String getPingResponse(String p_77525_0_, String p_77525_1_) {
		return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
	}

	private final String address;

	private boolean isStopping = true;

	private final String motd;

	/** The socket we're using to send packets on. */
	private final DatagramSocket socket;

	public ThreadLanServerPing(String p_i1321_1_, String p_i1321_2_)
			throws IOException {
		super("LanServerPinger #" + field_148658_a.incrementAndGet());
		motd = p_i1321_1_;
		address = p_i1321_2_;
		setDaemon(true);
		socket = new DatagramSocket();
	}

	@Override
	public void interrupt() {
		super.interrupt();
		isStopping = false;
	}

	@Override
	public void run() {
		final String var1 = getPingResponse(motd, address);
		final byte[] var2 = var1.getBytes();

		while (!isInterrupted() && isStopping) {
			try {
				final InetAddress var3 = InetAddress.getByName("224.0.2.60");
				final DatagramPacket var4 = new DatagramPacket(var2,
						var2.length, var3, 4445);
				socket.send(var4);
			} catch (final IOException var6) {
				logger.warn("LanServerPinger: " + var6.getMessage());
				break;
			}

			try {
				sleep(1500L);
			} catch (final InterruptedException var5) {
				;
			}
		}
	}
}

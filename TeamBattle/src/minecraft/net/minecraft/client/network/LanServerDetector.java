package net.minecraft.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanServerDetector {
	public static class LanServer {
		private final String lanServerIpPort;
		private final String lanServerMotd;
		private long timeLastSeen;

		public LanServer(String p_i1319_1_, String p_i1319_2_) {
			lanServerMotd = p_i1319_1_;
			lanServerIpPort = p_i1319_2_;
			timeLastSeen = Minecraft.getSystemTime();
		}

		public String getServerIpPort() {
			return lanServerIpPort;
		}

		public String getServerMotd() {
			return lanServerMotd;
		}

		public void updateLastSeen() {
			timeLastSeen = Minecraft.getSystemTime();
		}
	}

	public static class LanServerList {
		private final ArrayList listOfLanServers = new ArrayList();
		boolean wasUpdated;

		public synchronized void func_77551_a(String p_77551_1_,
				InetAddress p_77551_2_) {
			final String var3 = ThreadLanServerPing
					.getMotdFromPingResponse(p_77551_1_);
			String var4 = ThreadLanServerPing.getAdFromPingResponse(p_77551_1_);

			if (var4 != null) {
				var4 = p_77551_2_.getHostAddress() + ":" + var4;
				boolean var5 = false;
				final Iterator var6 = listOfLanServers.iterator();

				while (var6.hasNext()) {
					final LanServerDetector.LanServer var7 = (LanServerDetector.LanServer) var6
							.next();

					if (var7.getServerIpPort().equals(var4)) {
						var7.updateLastSeen();
						var5 = true;
						break;
					}
				}

				if (!var5) {
					listOfLanServers.add(new LanServerDetector.LanServer(var3,
							var4));
					wasUpdated = true;
				}
			}
		}

		public synchronized List getLanServers() {
			return Collections.unmodifiableList(listOfLanServers);
		}

		public synchronized boolean getWasUpdated() {
			return wasUpdated;
		}

		public synchronized void setWasNotUpdated() {
			wasUpdated = false;
		}
	}

	public static class ThreadLanServerFind extends Thread {
		private final InetAddress broadcastAddress;
		private final LanServerDetector.LanServerList localServerList;
		private final MulticastSocket socket;

		public ThreadLanServerFind(LanServerDetector.LanServerList p_i1320_1_)
				throws IOException {
			super("LanServerDetector #"
					+ LanServerDetector.field_148551_a.incrementAndGet());
			localServerList = p_i1320_1_;
			setDaemon(true);
			socket = new MulticastSocket(4445);
			broadcastAddress = InetAddress.getByName("224.0.2.60");
			socket.setSoTimeout(5000);
			socket.joinGroup(broadcastAddress);
		}

		@Override
		public void run() {
			final byte[] var2 = new byte[1024];

			while (!isInterrupted()) {
				final DatagramPacket var1 = new DatagramPacket(var2,
						var2.length);

				try {
					socket.receive(var1);
				} catch (final SocketTimeoutException var5) {
					continue;
				} catch (final IOException var6) {
					LanServerDetector.logger.error("Couldn\'t ping server",
							var6);
					break;
				}

				final String var3 = new String(var1.getData(),
						var1.getOffset(), var1.getLength());
				LanServerDetector.logger.debug(var1.getAddress() + ": " + var3);
				localServerList.func_77551_a(var3, var1.getAddress());
			}

			try {
				socket.leaveGroup(broadcastAddress);
			} catch (final IOException var4) {
				;
			}

			socket.close();
		}
	}

	private static final AtomicInteger field_148551_a = new AtomicInteger(0);

	private static final Logger logger = LogManager.getLogger();
}

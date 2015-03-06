package net.minecraft.profiler;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import net.minecraft.util.HttpUtil;

import com.google.common.collect.Maps;

public class PlayerUsageSnooper {
	static int access$308(PlayerUsageSnooper p_access$308_0_) {
		return p_access$308_0_.selfCounter++;
	}

	private final Map field_152773_a = Maps.newHashMap();
	private final Map field_152774_b = Maps.newHashMap();

	private boolean isRunning;
	private final long minecraftStartTimeMilis;

	private final IPlayerUsage playerStatsCollector;
	/** incremented on every getSelfCounterFor */
	private int selfCounter;
	/** URL of the server to send the report to */
	private final URL serverUrl;
	private final Object syncLock = new Object();

	/** set to fire the snooperThread every 15 mins */
	private final Timer threadTrigger = new Timer("Snooper Timer", true);

	private final String uniqueID = UUID.randomUUID().toString();

	public PlayerUsageSnooper(String p_i1563_1_, IPlayerUsage p_i1563_2_,
			long p_i1563_3_) {
		try {
			serverUrl = new URL("http://snoop.minecraft.net/" + p_i1563_1_
					+ "?version=" + 2);
		} catch (final MalformedURLException var6) {
			throw new IllegalArgumentException();
		}

		playerStatsCollector = p_i1563_2_;
		minecraftStartTimeMilis = p_i1563_3_;
	}

	private void addJvmArgsToSnooper() {
		final RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
		final List var2 = var1.getInputArguments();
		int var3 = 0;
		final Iterator var4 = var2.iterator();

		while (var4.hasNext()) {
			final String var5 = (String) var4.next();

			if (var5.startsWith("-X")) {
				func_152768_a("jvm_arg[" + var3++ + "]", var5);
			}
		}

		func_152768_a("jvm_args", Integer.valueOf(var3));
	}

	public void addMemoryStatsToSnooper() {
		func_152767_b("memory_total",
				Long.valueOf(Runtime.getRuntime().totalMemory()));
		func_152767_b("memory_max",
				Long.valueOf(Runtime.getRuntime().maxMemory()));
		func_152767_b("memory_free",
				Long.valueOf(Runtime.getRuntime().freeMemory()));
		func_152767_b("cpu_cores",
				Integer.valueOf(Runtime.getRuntime().availableProcessors()));
		playerStatsCollector.addServerStatsToSnooper(this);
	}

	private void func_152766_h() {
		addJvmArgsToSnooper();
		func_152768_a("snooper_token", uniqueID);
		func_152767_b("snooper_token", uniqueID);
		func_152767_b("os_name", System.getProperty("os.name"));
		func_152767_b("os_version", System.getProperty("os.version"));
		func_152767_b("os_architecture", System.getProperty("os.arch"));
		func_152767_b("java_version", System.getProperty("java.version"));
		func_152767_b("version", "1.7.10");
		playerStatsCollector.addServerTypeToSnooper(this);
	}

	public void func_152767_b(String p_152767_1_, Object p_152767_2_) {
		synchronized (syncLock) {
			field_152773_a.put(p_152767_1_, p_152767_2_);
		}
	}

	public void func_152768_a(String p_152768_1_, Object p_152768_2_) {
		synchronized (syncLock) {
			field_152774_b.put(p_152768_1_, p_152768_2_);
		}
	}

	public Map getCurrentStats() {
		final LinkedHashMap var1 = new LinkedHashMap();
		synchronized (syncLock) {
			addMemoryStatsToSnooper();
			Iterator var3 = field_152773_a.entrySet().iterator();
			Entry var4;

			while (var3.hasNext()) {
				var4 = (Entry) var3.next();
				var1.put(var4.getKey(), var4.getValue().toString());
			}

			var3 = field_152774_b.entrySet().iterator();

			while (var3.hasNext()) {
				var4 = (Entry) var3.next();
				var1.put(var4.getKey(), var4.getValue().toString());
			}

			return var1;
		}
	}

	/**
	 * Returns the saved value of System#currentTimeMillis when the game started
	 */
	public long getMinecraftStartTimeMillis() {
		return minecraftStartTimeMilis;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public boolean isSnooperRunning() {
		return isRunning;
	}

	/**
	 * Note issuing start multiple times is not an error.
	 */
	public void startSnooper() {
		if (!isRunning) {
			isRunning = true;
			func_152766_h();
			threadTrigger.schedule(new TimerTask() {

				@Override
				public void run() {
					if (playerStatsCollector.isSnooperEnabled()) {
						HashMap var1;

						synchronized (syncLock) {
							var1 = new HashMap(field_152774_b);

							if (selfCounter == 0) {
								var1.putAll(field_152773_a);
							}

							var1.put(
									"snooper_count",
									Integer.valueOf(PlayerUsageSnooper
											.access$308(PlayerUsageSnooper.this)));
							var1.put("snooper_token", uniqueID);
						}

						HttpUtil.func_151226_a(serverUrl, var1, true);
					}
				}
			}, 0L, 900000L);
		}
	}

	public void stopSnooper() {
		threadTrigger.cancel();
	}
}

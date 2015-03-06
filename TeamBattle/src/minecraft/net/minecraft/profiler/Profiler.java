package net.minecraft.profiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.src.Config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
	public static final class Result implements Comparable {
		public double field_76330_b;
		public String field_76331_c;
		public double field_76332_a;

		public Result(String par1Str, double par2, double par4) {
			field_76331_c = par1Str;
			field_76332_a = par2;
			field_76330_b = par4;
		}

		@Override
		public int compareTo(Object par1Obj) {
			return this.compareTo((Profiler.Result) par1Obj);
		}

		public int compareTo(Profiler.Result par1Obj) {
			return par1Obj.field_76332_a < field_76332_a ? -1
					: par1Obj.field_76332_a > field_76332_a ? 1
							: par1Obj.field_76331_c.compareTo(field_76331_c);
		}

		public int func_76329_a() {
			return (field_76331_c.hashCode() & 11184810) + 4473924;
		}
	}

	private static final Logger logger = LogManager.getLogger();

	public boolean profilerGlobalEnabled = true;

	private boolean profilerLocalEnabled;

	/** Flag profiling enabled */
	public boolean profilingEnabled;

	/** Profiling map */
	private final Map profilingMap = new HashMap();

	/** Current profiling section */
	private String profilingSection = "";
	/** List of parent sections */
	private final List sectionList = new ArrayList();
	private long startTickNano;
	private long startUpdateChunksNano;
	/** List of timestamps (System.nanoTime) */
	private final List timestampList = new ArrayList();
	public long timeTickNano;

	public long timeUpdateChunksNano;

	public Profiler() {
		profilerLocalEnabled = profilerGlobalEnabled;
		startTickNano = 0L;
		timeTickNano = 0L;
		startUpdateChunksNano = 0L;
		timeUpdateChunksNano = 0L;
	}

	/**
	 * Clear profiling.
	 */
	public void clearProfiling() {
		profilingMap.clear();
		profilingSection = "";
		sectionList.clear();
		profilerLocalEnabled = profilerGlobalEnabled;
	}

	/**
	 * End section
	 */
	public void endSection() {
		if (profilerLocalEnabled) {
			if (profilingEnabled) {
				final long var1 = System.nanoTime();
				final long var3 = ((Long) timestampList.remove(timestampList
						.size() - 1)).longValue();
				sectionList.remove(sectionList.size() - 1);
				final long var5 = var1 - var3;

				if (profilingMap.containsKey(profilingSection)) {
					profilingMap
							.put(profilingSection, Long
									.valueOf(((Long) profilingMap
											.get(profilingSection)).longValue()
											+ var5));
				} else {
					profilingMap.put(profilingSection, Long.valueOf(var5));
				}

				if (var5 > 100000000L) {
					logger.warn("Something\'s taking too long! \'"
							+ profilingSection + "\' took aprox " + var5
							/ 1000000.0D + " ms");
				}

				profilingSection = !sectionList.isEmpty() ? (String) sectionList
						.get(sectionList.size() - 1) : "";
			}
		}
	}

	/**
	 * End current section and start a new section
	 */
	public void endStartSection(String par1Str) {
		if (profilerLocalEnabled) {
			endSection();
			startSection(par1Str);
		}
	}

	public String getNameOfLastSection() {
		return sectionList.size() == 0 ? "[UNKNOWN]" : (String) sectionList
				.get(sectionList.size() - 1);
	}

	/**
	 * Get profiling data
	 */
	public List getProfilingData(String par1Str) {
		profilerLocalEnabled = profilerGlobalEnabled;

		if (!profilerLocalEnabled)
			return new ArrayList(
					Arrays.asList(new Profiler.Result[] { new Profiler.Result(
							"root", 0.0D, 0.0D) }));
		else if (!profilingEnabled)
			return null;
		else {
			long var3 = profilingMap.containsKey("root") ? ((Long) profilingMap
					.get("root")).longValue() : 0L;
			final long var5 = profilingMap.containsKey(par1Str) ? ((Long) profilingMap
					.get(par1Str)).longValue() : -1L;
			final ArrayList var7 = new ArrayList();

			if (par1Str.length() > 0) {
				par1Str = par1Str + ".";
			}

			long var8 = 0L;
			final Iterator var10 = profilingMap.keySet().iterator();

			while (var10.hasNext()) {
				final String var20 = (String) var10.next();

				if (var20.length() > par1Str.length()
						&& var20.startsWith(par1Str)
						&& var20.indexOf(".", par1Str.length() + 1) < 0) {
					var8 += ((Long) profilingMap.get(var20)).longValue();
				}
			}

			final float var201 = var8;

			if (var8 < var5) {
				var8 = var5;
			}

			if (var3 < var8) {
				var3 = var8;
			}

			Iterator var21 = profilingMap.keySet().iterator();
			String var12;

			while (var21.hasNext()) {
				var12 = (String) var21.next();

				if (var12.length() > par1Str.length()
						&& var12.startsWith(par1Str)
						&& var12.indexOf(".", par1Str.length() + 1) < 0) {
					final long var13 = ((Long) profilingMap.get(var12))
							.longValue();
					final double var15 = var13 * 100.0D / var8;
					final double var17 = var13 * 100.0D / var3;
					final String var19 = var12.substring(par1Str.length());
					var7.add(new Profiler.Result(var19, var15, var17));
				}
			}

			var21 = profilingMap.keySet().iterator();

			while (var21.hasNext()) {
				var12 = (String) var21.next();
				profilingMap.put(var12, Long.valueOf(((Long) profilingMap
						.get(var12)).longValue() * 999L / 1000L));
			}

			if (var8 > var201) {
				var7.add(new Profiler.Result("unspecified", (var8 - var201)
						* 100.0D / var8, (var8 - var201) * 100.0D / var3));
			}

			Collections.sort(var7);
			var7.add(0, new Profiler.Result(par1Str, 100.0D, var8 * 100.0D
					/ var3));
			return var7;
		}
	}

	/**
	 * Start section
	 */
	public void startSection(String par1Str) {
		if (Config.getGameSettings().showDebugInfo) {
			if (startTickNano == 0L && par1Str.equals("tick")) {
				startTickNano = System.nanoTime();
			}

			if (startTickNano != 0L && par1Str.equals("preRenderErrors")) {
				timeTickNano = System.nanoTime() - startTickNano;
				startTickNano = 0L;
			}

			if (startUpdateChunksNano == 0L && par1Str.equals("updatechunks")) {
				startUpdateChunksNano = System.nanoTime();
			}

			if (startUpdateChunksNano != 0L && par1Str.equals("terrain")) {
				timeUpdateChunksNano = System.nanoTime()
						- startUpdateChunksNano;
				startUpdateChunksNano = 0L;
			}
		}

		if (profilerLocalEnabled) {
			if (profilingEnabled) {
				if (profilingSection.length() > 0) {
					profilingSection = profilingSection + ".";
				}

				profilingSection = profilingSection + par1Str;
				sectionList.add(profilingSection);
				timestampList.add(Long.valueOf(System.nanoTime()));
			}
		}
	}
}

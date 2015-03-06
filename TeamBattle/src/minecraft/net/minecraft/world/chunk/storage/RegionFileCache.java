package net.minecraft.world.chunk.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegionFileCache {
	/** A map containing Files as keys and RegionFiles as values */
	private static final Map regionsByFilename = new HashMap();

	/**
	 * Saves the current Chunk Map Cache
	 */
	public static synchronized void clearRegionFileReferences() {
		final Iterator var0 = regionsByFilename.values().iterator();

		while (var0.hasNext()) {
			final RegionFile var1 = (RegionFile) var0.next();

			try {
				if (var1 != null) {
					var1.close();
				}
			} catch (final IOException var3) {
				var3.printStackTrace();
			}
		}

		regionsByFilename.clear();
	}

	public static synchronized RegionFile createOrLoadRegionFile(
			File p_76550_0_, int p_76550_1_, int p_76550_2_) {
		final File var3 = new File(p_76550_0_, "region");
		final File var4 = new File(var3, "r." + (p_76550_1_ >> 5) + "."
				+ (p_76550_2_ >> 5) + ".mca");
		final RegionFile var5 = (RegionFile) regionsByFilename.get(var4);

		if (var5 != null)
			return var5;
		else {
			if (!var3.exists()) {
				var3.mkdirs();
			}

			if (regionsByFilename.size() >= 256) {
				clearRegionFileReferences();
			}

			final RegionFile var6 = new RegionFile(var4);
			regionsByFilename.put(var4, var6);
			return var6;
		}
	}

	/**
	 * Returns an input stream for the specified chunk. Args: worldDir, chunkX,
	 * chunkZ
	 */
	public static DataInputStream getChunkInputStream(File p_76549_0_,
			int p_76549_1_, int p_76549_2_) {
		final RegionFile var3 = createOrLoadRegionFile(p_76549_0_, p_76549_1_,
				p_76549_2_);
		return var3.getChunkDataInputStream(p_76549_1_ & 31, p_76549_2_ & 31);
	}

	/**
	 * Returns an output stream for the specified chunk. Args: worldDir, chunkX,
	 * chunkZ
	 */
	public static DataOutputStream getChunkOutputStream(File p_76552_0_,
			int p_76552_1_, int p_76552_2_) {
		final RegionFile var3 = createOrLoadRegionFile(p_76552_0_, p_76552_1_,
				p_76552_2_);
		return var3.getChunkDataOutputStream(p_76552_1_ & 31, p_76552_2_ & 31);
	}
}

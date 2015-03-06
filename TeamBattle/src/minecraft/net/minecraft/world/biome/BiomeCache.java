package net.minecraft.world.biome;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.LongHashMap;

public class BiomeCache {
	public class Block {
		public BiomeGenBase[] biomes = new BiomeGenBase[256];
		public long lastAccessTime;
		public float[] rainfallValues = new float[256];
		public int xPosition;
		public int zPosition;

		public Block(int p_i1972_2_, int p_i1972_3_) {
			xPosition = p_i1972_2_;
			zPosition = p_i1972_3_;
			chunkManager.getRainfall(rainfallValues, p_i1972_2_ << 4,
					p_i1972_3_ << 4, 16, 16);
			chunkManager.getBiomeGenAt(biomes, p_i1972_2_ << 4,
					p_i1972_3_ << 4, 16, 16, false);
		}

		public BiomeGenBase getBiomeGenAt(int p_76885_1_, int p_76885_2_) {
			return biomes[p_76885_1_ & 15 | (p_76885_2_ & 15) << 4];
		}
	}

	/** The list of cached BiomeCacheBlocks */
	private final List cache = new ArrayList();

	/**
	 * The map of keys to BiomeCacheBlocks. Keys are based on the chunk x, z
	 * coordinates as (x | z << 32).
	 */
	private final LongHashMap cacheMap = new LongHashMap();

	/** Reference to the WorldChunkManager */
	private final WorldChunkManager chunkManager;

	/** The last time this BiomeCache was cleaned, in milliseconds. */
	private long lastCleanupTime;

	public BiomeCache(WorldChunkManager p_i1973_1_) {
		chunkManager = p_i1973_1_;
	}

	/**
	 * Removes BiomeCacheBlocks from this cache that haven't been accessed in at
	 * least 30 seconds.
	 */
	public void cleanupCache() {
		final long var1 = MinecraftServer.getSystemTimeMillis();
		final long var3 = var1 - lastCleanupTime;

		if (var3 > 7500L || var3 < 0L) {
			lastCleanupTime = var1;

			for (int var5 = 0; var5 < cache.size(); ++var5) {
				final BiomeCache.Block var6 = (BiomeCache.Block) cache
						.get(var5);
				final long var7 = var1 - var6.lastAccessTime;

				if (var7 > 30000L || var7 < 0L) {
					cache.remove(var5--);
					final long var9 = var6.xPosition & 4294967295L
							| (var6.zPosition & 4294967295L) << 32;
					cacheMap.remove(var9);
				}
			}
		}
	}

	/**
	 * Returns a biome cache block at location specified.
	 */
	public BiomeCache.Block getBiomeCacheBlock(int p_76840_1_, int p_76840_2_) {
		p_76840_1_ >>= 4;
		p_76840_2_ >>= 4;
		final long var3 = p_76840_1_ & 4294967295L
				| (p_76840_2_ & 4294967295L) << 32;
		BiomeCache.Block var5 = (BiomeCache.Block) cacheMap.getValueByKey(var3);

		if (var5 == null) {
			var5 = new BiomeCache.Block(p_76840_1_, p_76840_2_);
			cacheMap.add(var3, var5);
			cache.add(var5);
		}

		var5.lastAccessTime = MinecraftServer.getSystemTimeMillis();
		return var5;
	}

	/**
	 * Returns the BiomeGenBase related to the x, z position from the cache.
	 */
	public BiomeGenBase getBiomeGenAt(int p_76837_1_, int p_76837_2_) {
		return getBiomeCacheBlock(p_76837_1_, p_76837_2_).getBiomeGenAt(
				p_76837_1_, p_76837_2_);
	}

	/**
	 * Returns the array of cached biome types in the BiomeCacheBlock at the
	 * given location.
	 */
	public BiomeGenBase[] getCachedBiomes(int p_76839_1_, int p_76839_2_) {
		return getBiomeCacheBlock(p_76839_1_, p_76839_2_).biomes;
	}
}

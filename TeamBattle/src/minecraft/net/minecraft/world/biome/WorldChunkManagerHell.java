package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.world.ChunkPosition;

public class WorldChunkManagerHell extends WorldChunkManager {
	/** The biome generator object. */
	private final BiomeGenBase biomeGenerator;

	/** The rainfall in the world */
	private final float rainfall;

	public WorldChunkManagerHell(BiomeGenBase p_i45374_1_, float p_i45374_2_) {
		biomeGenerator = p_i45374_1_;
		rainfall = p_i45374_2_;
	}

	/**
	 * checks given Chunk's Biomes against List of allowed ones
	 */
	@Override
	public boolean areBiomesViable(int p_76940_1_, int p_76940_2_,
			int p_76940_3_, List p_76940_4_) {
		return p_76940_4_.contains(biomeGenerator);
	}

	@Override
	public ChunkPosition func_150795_a(int p_150795_1_, int p_150795_2_,
			int p_150795_3_, List p_150795_4_, Random p_150795_5_) {
		return p_150795_4_.contains(biomeGenerator) ? new ChunkPosition(
				p_150795_1_ - p_150795_3_
						+ p_150795_5_.nextInt(p_150795_3_ * 2 + 1), 0,
				p_150795_2_ - p_150795_3_
						+ p_150795_5_.nextInt(p_150795_3_ * 2 + 1)) : null;
	}

	/**
	 * Return a list of biomes for the specified blocks. Args: listToReuse, x,
	 * y, width, length, cacheFlag (if false, don't check biomeCache to avoid
	 * infinite loop in BiomeCacheBlock)
	 */
	@Override
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] p_76931_1_,
			int p_76931_2_, int p_76931_3_, int p_76931_4_, int p_76931_5_,
			boolean p_76931_6_) {
		return loadBlockGeneratorData(p_76931_1_, p_76931_2_, p_76931_3_,
				p_76931_4_, p_76931_5_);
	}

	/**
	 * Returns the BiomeGenBase related to the x, z position on the world.
	 */
	@Override
	public BiomeGenBase getBiomeGenAt(int p_76935_1_, int p_76935_2_) {
		return biomeGenerator;
	}

	/**
	 * Returns an array of biomes for the location input.
	 */
	@Override
	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_,
			int p_76937_2_, int p_76937_3_, int p_76937_4_, int p_76937_5_) {
		if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_) {
			p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
		}

		Arrays.fill(p_76937_1_, 0, p_76937_4_ * p_76937_5_, biomeGenerator);
		return p_76937_1_;
	}

	/**
	 * Returns a list of rainfall values for the specified blocks. Args:
	 * listToReuse, x, z, width, length.
	 */
	@Override
	public float[] getRainfall(float[] p_76936_1_, int p_76936_2_,
			int p_76936_3_, int p_76936_4_, int p_76936_5_) {
		if (p_76936_1_ == null || p_76936_1_.length < p_76936_4_ * p_76936_5_) {
			p_76936_1_ = new float[p_76936_4_ * p_76936_5_];
		}

		Arrays.fill(p_76936_1_, 0, p_76936_4_ * p_76936_5_, rainfall);
		return p_76936_1_;
	}

	/**
	 * Returns biomes to use for the blocks and loads the other data like
	 * temperature and humidity onto the WorldChunkManager Args: oldBiomeList,
	 * x, z, width, depth
	 */
	@Override
	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] p_76933_1_,
			int p_76933_2_, int p_76933_3_, int p_76933_4_, int p_76933_5_) {
		if (p_76933_1_ == null || p_76933_1_.length < p_76933_4_ * p_76933_5_) {
			p_76933_1_ = new BiomeGenBase[p_76933_4_ * p_76933_5_];
		}

		Arrays.fill(p_76933_1_, 0, p_76933_4_ * p_76933_5_, biomeGenerator);
		return p_76933_1_;
	}
}

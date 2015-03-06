package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRareBiome extends GenLayer {

	public GenLayerRareBiome(long p_i45478_1_, GenLayer p_i45478_3_) {
		super(p_i45478_1_);
		parent = p_i45478_3_;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be
	 * interpreted as temperatures, rainfall amounts, or biomeList[] indices
	 * based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_,
			int p_75904_4_) {
		final int[] var5 = parent.getInts(p_75904_1_ - 1, p_75904_2_ - 1,
				p_75904_3_ + 2, p_75904_4_ + 2);
		final int[] var6 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int var7 = 0; var7 < p_75904_4_; ++var7) {
			for (int var8 = 0; var8 < p_75904_3_; ++var8) {
				initChunkSeed(var8 + p_75904_1_, var7 + p_75904_2_);
				final int var9 = var5[var8 + 1 + (var7 + 1) * (p_75904_3_ + 2)];

				if (nextInt(57) == 0) {
					if (var9 == BiomeGenBase.plains.biomeID) {
						var6[var8 + var7 * p_75904_3_] = BiomeGenBase.plains.biomeID + 128;
					} else {
						var6[var8 + var7 * p_75904_3_] = var9;
					}
				} else {
					var6[var8 + var7 * p_75904_3_] = var9;
				}
			}
		}

		return var6;
	}
}

package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerRiverMix extends GenLayer {
	private final GenLayer biomePatternGeneratorChain;
	private final GenLayer riverPatternGeneratorChain;

	public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_,
			GenLayer p_i2129_4_) {
		super(p_i2129_1_);
		biomePatternGeneratorChain = p_i2129_3_;
		riverPatternGeneratorChain = p_i2129_4_;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be
	 * interpreted as temperatures, rainfall amounts, or biomeList[] indices
	 * based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_,
			int p_75904_4_) {
		final int[] var5 = biomePatternGeneratorChain.getInts(p_75904_1_,
				p_75904_2_, p_75904_3_, p_75904_4_);
		final int[] var6 = riverPatternGeneratorChain.getInts(p_75904_1_,
				p_75904_2_, p_75904_3_, p_75904_4_);
		final int[] var7 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int var8 = 0; var8 < p_75904_3_ * p_75904_4_; ++var8) {
			if (var5[var8] != BiomeGenBase.ocean.biomeID
					&& var5[var8] != BiomeGenBase.field_150575_M.biomeID) {
				if (var6[var8] == BiomeGenBase.river.biomeID) {
					if (var5[var8] == BiomeGenBase.icePlains.biomeID) {
						var7[var8] = BiomeGenBase.frozenRiver.biomeID;
					} else if (var5[var8] != BiomeGenBase.mushroomIsland.biomeID
							&& var5[var8] != BiomeGenBase.mushroomIslandShore.biomeID) {
						var7[var8] = var6[var8] & 255;
					} else {
						var7[var8] = BiomeGenBase.mushroomIslandShore.biomeID;
					}
				} else {
					var7[var8] = var5[var8];
				}
			} else {
				var7[var8] = var5[var8];
			}
		}

		return var7;
	}

	/**
	 * Initialize layer's local worldGenSeed based on its own baseSeed and the
	 * world's global seed (passed in as an argument).
	 */
	@Override
	public void initWorldGenSeed(long p_75905_1_) {
		biomePatternGeneratorChain.initWorldGenSeed(p_75905_1_);
		riverPatternGeneratorChain.initWorldGenSeed(p_75905_1_);
		super.initWorldGenSeed(p_75905_1_);
	}
}

package net.minecraft.world.gen.layer;

public class GenLayerRiverInit extends GenLayer {

	public GenLayerRiverInit(long p_i2127_1_, GenLayer p_i2127_3_) {
		super(p_i2127_1_);
		parent = p_i2127_3_;
	}

	/**
	 * Returns a list of integer values generated by this layer. These may be
	 * interpreted as temperatures, rainfall amounts, or biomeList[] indices
	 * based on the particular GenLayer subclass.
	 */
	@Override
	public int[] getInts(int p_75904_1_, int p_75904_2_, int p_75904_3_,
			int p_75904_4_) {
		final int[] var5 = parent.getInts(p_75904_1_, p_75904_2_, p_75904_3_,
				p_75904_4_);
		final int[] var6 = IntCache.getIntCache(p_75904_3_ * p_75904_4_);

		for (int var7 = 0; var7 < p_75904_4_; ++var7) {
			for (int var8 = 0; var8 < p_75904_3_; ++var8) {
				initChunkSeed(var8 + p_75904_1_, var7 + p_75904_2_);
				var6[var8 + var7 * p_75904_3_] = var5[var8 + var7 * p_75904_3_] > 0 ? nextInt(299999) + 2
						: 0;
			}
		}

		return var6;
	}
}

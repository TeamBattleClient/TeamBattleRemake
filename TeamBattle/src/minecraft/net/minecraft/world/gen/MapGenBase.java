package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class MapGenBase {
	/** The RNG used by the MapGen classes. */
	protected Random rand = new Random();

	/** The number of Chunks to gen-check in any given direction. */
	protected int range = 8;

	/** This world object. */
	protected World worldObj;

	protected void func_151538_a(World p_151538_1_, int p_151538_2_,
			int p_151538_3_, int p_151538_4_, int p_151538_5_,
			Block[] p_151538_6_) {
	}

	public void func_151539_a(IChunkProvider p_151539_1_, World p_151539_2_,
			int p_151539_3_, int p_151539_4_, Block[] p_151539_5_) {
		final int var6 = range;
		worldObj = p_151539_2_;
		rand.setSeed(p_151539_2_.getSeed());
		final long var7 = rand.nextLong();
		final long var9 = rand.nextLong();

		for (int var11 = p_151539_3_ - var6; var11 <= p_151539_3_ + var6; ++var11) {
			for (int var12 = p_151539_4_ - var6; var12 <= p_151539_4_ + var6; ++var12) {
				final long var13 = var11 * var7;
				final long var15 = var12 * var9;
				rand.setSeed(var13 ^ var15 ^ p_151539_2_.getSeed());
				func_151538_a(p_151539_2_, var11, var12, p_151539_3_,
						p_151539_4_, p_151539_5_);
			}
		}
	}
}

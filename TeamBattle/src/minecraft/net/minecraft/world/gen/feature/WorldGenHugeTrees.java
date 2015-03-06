package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree {
	/** The base height of the tree */
	protected final int baseHeight;

	protected int field_150538_d;

	/** Sets the metadata for the leaves used in huge trees */
	protected final int leavesMetadata;
	/** Sets the metadata for the wood blocks used */
	protected final int woodMetadata;

	public WorldGenHugeTrees(boolean p_i45458_1_, int p_i45458_2_,
			int p_i45458_3_, int p_i45458_4_, int p_i45458_5_) {
		super(p_i45458_1_);
		baseHeight = p_i45458_2_;
		field_150538_d = p_i45458_3_;
		woodMetadata = p_i45458_4_;
		leavesMetadata = p_i45458_5_;
	}

	private boolean func_150532_c(World p_150532_1_, Random p_150532_2_,
			int p_150532_3_, int p_150532_4_, int p_150532_5_) {
		final Block var6 = p_150532_1_.getBlock(p_150532_3_, p_150532_4_ - 1,
				p_150532_5_);

		if ((var6 == Blocks.grass || var6 == Blocks.dirt) && p_150532_4_ >= 2) {
			p_150532_1_.setBlock(p_150532_3_, p_150532_4_ - 1, p_150532_5_,
					Blocks.dirt, 0, 2);
			p_150532_1_.setBlock(p_150532_3_ + 1, p_150532_4_ - 1, p_150532_5_,
					Blocks.dirt, 0, 2);
			p_150532_1_.setBlock(p_150532_3_, p_150532_4_ - 1, p_150532_5_ + 1,
					Blocks.dirt, 0, 2);
			p_150532_1_.setBlock(p_150532_3_ + 1, p_150532_4_ - 1,
					p_150532_5_ + 1, Blocks.dirt, 0, 2);
			return true;
		} else
			return false;
	}

	protected int func_150533_a(Random p_150533_1_) {
		int var2 = p_150533_1_.nextInt(3) + baseHeight;

		if (field_150538_d > 1) {
			var2 += p_150533_1_.nextInt(field_150538_d);
		}

		return var2;
	}

	protected void func_150534_b(World p_150534_1_, int p_150534_2_,
			int p_150534_3_, int p_150534_4_, int p_150534_5_,
			Random p_150534_6_) {
		final int var7 = p_150534_5_ * p_150534_5_;

		for (int var8 = p_150534_2_ - p_150534_5_; var8 <= p_150534_2_
				+ p_150534_5_; ++var8) {
			final int var9 = var8 - p_150534_2_;

			for (int var10 = p_150534_4_ - p_150534_5_; var10 <= p_150534_4_
					+ p_150534_5_; ++var10) {
				final int var11 = var10 - p_150534_4_;

				if (var9 * var9 + var11 * var11 <= var7) {
					final Block var12 = p_150534_1_.getBlock(var8, p_150534_3_,
							var10);

					if (var12.getMaterial() == Material.air
							|| var12.getMaterial() == Material.leaves) {
						func_150516_a(p_150534_1_, var8, p_150534_3_, var10,
								Blocks.leaves, leavesMetadata);
					}
				}
			}
		}
	}

	protected void func_150535_a(World p_150535_1_, int p_150535_2_,
			int p_150535_3_, int p_150535_4_, int p_150535_5_,
			Random p_150535_6_) {
		final int var7 = p_150535_5_ * p_150535_5_;

		for (int var8 = p_150535_2_ - p_150535_5_; var8 <= p_150535_2_
				+ p_150535_5_ + 1; ++var8) {
			final int var9 = var8 - p_150535_2_;

			for (int var10 = p_150535_4_ - p_150535_5_; var10 <= p_150535_4_
					+ p_150535_5_ + 1; ++var10) {
				final int var11 = var10 - p_150535_4_;
				final int var12 = var9 - 1;
				final int var13 = var11 - 1;

				if (var9 * var9 + var11 * var11 <= var7
						|| var12 * var12 + var13 * var13 <= var7
						|| var9 * var9 + var13 * var13 <= var7
						|| var12 * var12 + var11 * var11 <= var7) {
					final Block var14 = p_150535_1_.getBlock(var8, p_150535_3_,
							var10);

					if (var14.getMaterial() == Material.air
							|| var14.getMaterial() == Material.leaves) {
						func_150516_a(p_150535_1_, var8, p_150535_3_, var10,
								Blocks.leaves, leavesMetadata);
					}
				}
			}
		}
	}

	private boolean func_150536_b(World p_150536_1_, Random p_150536_2_,
			int p_150536_3_, int p_150536_4_, int p_150536_5_, int p_150536_6_) {
		boolean var7 = true;

		if (p_150536_4_ >= 1 && p_150536_4_ + p_150536_6_ + 1 <= 256) {
			for (int var8 = p_150536_4_; var8 <= p_150536_4_ + 1 + p_150536_6_; ++var8) {
				byte var9 = 2;

				if (var8 == p_150536_4_) {
					var9 = 1;
				}

				if (var8 >= p_150536_4_ + 1 + p_150536_6_ - 2) {
					var9 = 2;
				}

				for (int var10 = p_150536_3_ - var9; var10 <= p_150536_3_
						+ var9
						&& var7; ++var10) {
					for (int var11 = p_150536_5_ - var9; var11 <= p_150536_5_
							+ var9
							&& var7; ++var11) {
						if (var8 >= 0 && var8 < 256) {
							final Block var12 = p_150536_1_.getBlock(var10,
									var8, var11);

							if (!func_150523_a(var12)) {
								var7 = false;
							}
						} else {
							var7 = false;
						}
					}
				}
			}

			return var7;
		} else
			return false;
	}

	protected boolean func_150537_a(World p_150537_1_, Random p_150537_2_,
			int p_150537_3_, int p_150537_4_, int p_150537_5_, int p_150537_6_) {
		return func_150536_b(p_150537_1_, p_150537_2_, p_150537_3_,
				p_150537_4_, p_150537_5_, p_150537_6_)
				&& func_150532_c(p_150537_1_, p_150537_2_, p_150537_3_,
						p_150537_4_, p_150537_5_);
	}
}

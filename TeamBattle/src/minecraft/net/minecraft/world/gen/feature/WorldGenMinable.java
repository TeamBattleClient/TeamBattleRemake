package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMinable extends WorldGenerator {
	private final Block field_150518_c;

	private final Block field_150519_a;
	/** The number of blocks to generate. */
	private final int numberOfBlocks;

	public WorldGenMinable(Block p_i45459_1_, int p_i45459_2_) {
		this(p_i45459_1_, p_i45459_2_, Blocks.stone);
	}

	public WorldGenMinable(Block p_i45460_1_, int p_i45460_2_, Block p_i45460_3_) {
		field_150519_a = p_i45460_1_;
		numberOfBlocks = p_i45460_2_;
		field_150518_c = p_i45460_3_;
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_,
			int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		final float var6 = p_76484_2_.nextFloat() * (float) Math.PI;
		final double var7 = p_76484_3_ + 8 + MathHelper.sin(var6)
				* numberOfBlocks / 8.0F;
		final double var9 = p_76484_3_ + 8 - MathHelper.sin(var6)
				* numberOfBlocks / 8.0F;
		final double var11 = p_76484_5_ + 8 + MathHelper.cos(var6)
				* numberOfBlocks / 8.0F;
		final double var13 = p_76484_5_ + 8 - MathHelper.cos(var6)
				* numberOfBlocks / 8.0F;
		final double var15 = p_76484_4_ + p_76484_2_.nextInt(3) - 2;
		final double var17 = p_76484_4_ + p_76484_2_.nextInt(3) - 2;

		for (int var19 = 0; var19 <= numberOfBlocks; ++var19) {
			final double var20 = var7 + (var9 - var7) * var19 / numberOfBlocks;
			final double var22 = var15 + (var17 - var15) * var19
					/ numberOfBlocks;
			final double var24 = var11 + (var13 - var11) * var19
					/ numberOfBlocks;
			final double var26 = p_76484_2_.nextDouble() * numberOfBlocks
					/ 16.0D;
			final double var28 = (MathHelper.sin(var19 * (float) Math.PI
					/ numberOfBlocks) + 1.0F)
					* var26 + 1.0D;
			final double var30 = (MathHelper.sin(var19 * (float) Math.PI
					/ numberOfBlocks) + 1.0F)
					* var26 + 1.0D;
			final int var32 = MathHelper.floor_double(var20 - var28 / 2.0D);
			final int var33 = MathHelper.floor_double(var22 - var30 / 2.0D);
			final int var34 = MathHelper.floor_double(var24 - var28 / 2.0D);
			final int var35 = MathHelper.floor_double(var20 + var28 / 2.0D);
			final int var36 = MathHelper.floor_double(var22 + var30 / 2.0D);
			final int var37 = MathHelper.floor_double(var24 + var28 / 2.0D);

			for (int var38 = var32; var38 <= var35; ++var38) {
				final double var39 = (var38 + 0.5D - var20) / (var28 / 2.0D);

				if (var39 * var39 < 1.0D) {
					for (int var41 = var33; var41 <= var36; ++var41) {
						final double var42 = (var41 + 0.5D - var22)
								/ (var30 / 2.0D);

						if (var39 * var39 + var42 * var42 < 1.0D) {
							for (int var44 = var34; var44 <= var37; ++var44) {
								final double var45 = (var44 + 0.5D - var24)
										/ (var28 / 2.0D);

								if (var39 * var39 + var42 * var42 + var45
										* var45 < 1.0D
										&& p_76484_1_.getBlock(var38, var41,
												var44) == field_150518_c) {
									p_76484_1_.setBlock(var38, var41, var44,
											field_150519_a, 0, 2);
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}

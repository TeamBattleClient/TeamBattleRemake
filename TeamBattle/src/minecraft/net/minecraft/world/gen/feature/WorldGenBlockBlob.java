package net.minecraft.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator {
	private final int field_150544_b;
	private final Block field_150545_a;

	public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_) {
		super(false);
		field_150545_a = p_i45450_1_;
		field_150544_b = p_i45450_2_;
	}

	@Override
	public boolean generate(World p_76484_1_, Random p_76484_2_,
			int p_76484_3_, int p_76484_4_, int p_76484_5_) {
		while (true) {
			if (p_76484_4_ > 3) {
				label63: {
					if (!p_76484_1_.isAirBlock(p_76484_3_, p_76484_4_ - 1,
							p_76484_5_)) {
						final Block var6 = p_76484_1_.getBlock(p_76484_3_,
								p_76484_4_ - 1, p_76484_5_);

						if (var6 == Blocks.grass || var6 == Blocks.dirt
								|| var6 == Blocks.stone) {
							break label63;
						}
					}

					--p_76484_4_;
					continue;
				}
			}

			if (p_76484_4_ <= 3)
				return false;

			final int var18 = field_150544_b;

			for (int var7 = 0; var18 >= 0 && var7 < 3; ++var7) {
				final int var8 = var18 + p_76484_2_.nextInt(2);
				final int var9 = var18 + p_76484_2_.nextInt(2);
				final int var10 = var18 + p_76484_2_.nextInt(2);
				final float var11 = (var8 + var9 + var10) * 0.333F + 0.5F;

				for (int var12 = p_76484_3_ - var8; var12 <= p_76484_3_ + var8; ++var12) {
					for (int var13 = p_76484_5_ - var10; var13 <= p_76484_5_
							+ var10; ++var13) {
						for (int var14 = p_76484_4_ - var9; var14 <= p_76484_4_
								+ var9; ++var14) {
							final float var15 = var12 - p_76484_3_;
							final float var16 = var13 - p_76484_5_;
							final float var17 = var14 - p_76484_4_;

							if (var15 * var15 + var16 * var16 + var17 * var17 <= var11
									* var11) {
								p_76484_1_.setBlock(var12, var14, var13,
										field_150545_a, 0, 4);
							}
						}
					}
				}

				p_76484_3_ += -(var18 + 1) + p_76484_2_.nextInt(2 + var18 * 2);
				p_76484_5_ += -(var18 + 1) + p_76484_2_.nextInt(2 + var18 * 2);
				p_76484_4_ += 0 - p_76484_2_.nextInt(2);
			}

			return true;
		}
	}
}

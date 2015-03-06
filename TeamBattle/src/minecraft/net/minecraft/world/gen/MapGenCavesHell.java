package net.minecraft.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class MapGenCavesHell extends MapGenBase {

	@Override
	protected void func_151538_a(World p_151538_1_, int p_151538_2_,
			int p_151538_3_, int p_151538_4_, int p_151538_5_,
			Block[] p_151538_6_) {
		int var7 = rand.nextInt(rand.nextInt(rand.nextInt(10) + 1) + 1);

		if (rand.nextInt(5) != 0) {
			var7 = 0;
		}

		for (int var8 = 0; var8 < var7; ++var8) {
			final double var9 = p_151538_2_ * 16 + rand.nextInt(16);
			final double var11 = rand.nextInt(128);
			final double var13 = p_151538_3_ * 16 + rand.nextInt(16);
			int var15 = 1;

			if (rand.nextInt(4) == 0) {
				func_151544_a(rand.nextLong(), p_151538_4_, p_151538_5_,
						p_151538_6_, var9, var11, var13);
				var15 += rand.nextInt(4);
			}

			for (int var16 = 0; var16 < var15; ++var16) {
				final float var17 = rand.nextFloat() * (float) Math.PI * 2.0F;
				final float var18 = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				final float var19 = rand.nextFloat() * 2.0F + rand.nextFloat();
				func_151543_a(rand.nextLong(), p_151538_4_, p_151538_5_,
						p_151538_6_, var9, var11, var13, var19 * 2.0F, var17,
						var18, 0, 0, 0.5D);
			}
		}
	}

	protected void func_151543_a(long p_151543_1_, int p_151543_3_,
			int p_151543_4_, Block[] p_151543_5_, double p_151543_6_,
			double p_151543_8_, double p_151543_10_, float p_151543_12_,
			float p_151543_13_, float p_151543_14_, int p_151543_15_,
			int p_151543_16_, double p_151543_17_) {
		final double var19 = p_151543_3_ * 16 + 8;
		final double var21 = p_151543_4_ * 16 + 8;
		float var23 = 0.0F;
		float var24 = 0.0F;
		final Random var25 = new Random(p_151543_1_);

		if (p_151543_16_ <= 0) {
			final int var26 = range * 16 - 16;
			p_151543_16_ = var26 - var25.nextInt(var26 / 4);
		}

		boolean var53 = false;

		if (p_151543_15_ == -1) {
			p_151543_15_ = p_151543_16_ / 2;
			var53 = true;
		}

		final int var27 = var25.nextInt(p_151543_16_ / 2) + p_151543_16_ / 4;

		for (final boolean var28 = var25.nextInt(6) == 0; p_151543_15_ < p_151543_16_; ++p_151543_15_) {
			final double var29 = 1.5D
					+ MathHelper.sin(p_151543_15_ * (float) Math.PI
							/ p_151543_16_) * p_151543_12_ * 1.0F;
			final double var31 = var29 * p_151543_17_;
			final float var33 = MathHelper.cos(p_151543_14_);
			final float var34 = MathHelper.sin(p_151543_14_);
			p_151543_6_ += MathHelper.cos(p_151543_13_) * var33;
			p_151543_8_ += var34;
			p_151543_10_ += MathHelper.sin(p_151543_13_) * var33;

			if (var28) {
				p_151543_14_ *= 0.92F;
			} else {
				p_151543_14_ *= 0.7F;
			}

			p_151543_14_ += var24 * 0.1F;
			p_151543_13_ += var23 * 0.1F;
			var24 *= 0.9F;
			var23 *= 0.75F;
			var24 += (var25.nextFloat() - var25.nextFloat())
					* var25.nextFloat() * 2.0F;
			var23 += (var25.nextFloat() - var25.nextFloat())
					* var25.nextFloat() * 4.0F;

			if (!var53 && p_151543_15_ == var27 && p_151543_12_ > 1.0F) {
				func_151543_a(var25.nextLong(), p_151543_3_, p_151543_4_,
						p_151543_5_, p_151543_6_, p_151543_8_, p_151543_10_,
						var25.nextFloat() * 0.5F + 0.5F, p_151543_13_
								- (float) Math.PI / 2F, p_151543_14_ / 3.0F,
						p_151543_15_, p_151543_16_, 1.0D);
				func_151543_a(var25.nextLong(), p_151543_3_, p_151543_4_,
						p_151543_5_, p_151543_6_, p_151543_8_, p_151543_10_,
						var25.nextFloat() * 0.5F + 0.5F, p_151543_13_
								+ (float) Math.PI / 2F, p_151543_14_ / 3.0F,
						p_151543_15_, p_151543_16_, 1.0D);
				return;
			}

			if (var53 || var25.nextInt(4) != 0) {
				final double var35 = p_151543_6_ - var19;
				final double var37 = p_151543_10_ - var21;
				final double var39 = p_151543_16_ - p_151543_15_;
				final double var41 = p_151543_12_ + 2.0F + 16.0F;

				if (var35 * var35 + var37 * var37 - var39 * var39 > var41
						* var41)
					return;

				if (p_151543_6_ >= var19 - 16.0D - var29 * 2.0D
						&& p_151543_10_ >= var21 - 16.0D - var29 * 2.0D
						&& p_151543_6_ <= var19 + 16.0D + var29 * 2.0D
						&& p_151543_10_ <= var21 + 16.0D + var29 * 2.0D) {
					int var54 = MathHelper.floor_double(p_151543_6_ - var29)
							- p_151543_3_ * 16 - 1;
					int var36 = MathHelper.floor_double(p_151543_6_ + var29)
							- p_151543_3_ * 16 + 1;
					int var55 = MathHelper.floor_double(p_151543_8_ - var31) - 1;
					int var38 = MathHelper.floor_double(p_151543_8_ + var31) + 1;
					int var56 = MathHelper.floor_double(p_151543_10_ - var29)
							- p_151543_4_ * 16 - 1;
					int var40 = MathHelper.floor_double(p_151543_10_ + var29)
							- p_151543_4_ * 16 + 1;

					if (var54 < 0) {
						var54 = 0;
					}

					if (var36 > 16) {
						var36 = 16;
					}

					if (var55 < 1) {
						var55 = 1;
					}

					if (var38 > 120) {
						var38 = 120;
					}

					if (var56 < 0) {
						var56 = 0;
					}

					if (var40 > 16) {
						var40 = 16;
					}

					boolean var57 = false;
					int var42;
					int var45;

					for (var42 = var54; !var57 && var42 < var36; ++var42) {
						for (int var43 = var56; !var57 && var43 < var40; ++var43) {
							for (int var44 = var38 + 1; !var57
									&& var44 >= var55 - 1; --var44) {
								var45 = (var42 * 16 + var43) * 128 + var44;

								if (var44 >= 0 && var44 < 128) {
									final Block var46 = p_151543_5_[var45];

									if (var46 == Blocks.flowing_lava
											|| var46 == Blocks.lava) {
										var57 = true;
									}

									if (var44 != var55 - 1 && var42 != var54
											&& var42 != var36 - 1
											&& var43 != var56
											&& var43 != var40 - 1) {
										var44 = var55;
									}
								}
							}
						}
					}

					if (!var57) {
						for (var42 = var54; var42 < var36; ++var42) {
							final double var58 = (var42 + p_151543_3_ * 16
									+ 0.5D - p_151543_6_)
									/ var29;

							for (var45 = var56; var45 < var40; ++var45) {
								final double var59 = (var45 + p_151543_4_ * 16
										+ 0.5D - p_151543_10_)
										/ var29;
								int var48 = (var42 * 16 + var45) * 128 + var38;

								for (int var49 = var38 - 1; var49 >= var55; --var49) {
									final double var50 = (var49 + 0.5D - p_151543_8_)
											/ var31;

									if (var50 > -0.7D
											&& var58 * var58 + var50 * var50
													+ var59 * var59 < 1.0D) {
										final Block var52 = p_151543_5_[var48];

										if (var52 == Blocks.netherrack
												|| var52 == Blocks.dirt
												|| var52 == Blocks.grass) {
											p_151543_5_[var48] = null;
										}
									}

									--var48;
								}
							}
						}

						if (var53) {
							break;
						}
					}
				}
			}
		}
	}

	protected void func_151544_a(long p_151544_1_, int p_151544_3_,
			int p_151544_4_, Block[] p_151544_5_, double p_151544_6_,
			double p_151544_8_, double p_151544_10_) {
		func_151543_a(p_151544_1_, p_151544_3_, p_151544_4_, p_151544_5_,
				p_151544_6_, p_151544_8_, p_151544_10_,
				1.0F + rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}
}

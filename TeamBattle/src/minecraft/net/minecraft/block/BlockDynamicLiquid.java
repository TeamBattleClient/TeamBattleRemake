package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockDynamicLiquid extends BlockLiquid {
	boolean[] field_149814_b = new boolean[4];
	int field_149815_a;
	int[] field_149816_M = new int[4];

	protected BlockDynamicLiquid(Material p_i45403_1_) {
		super(p_i45403_1_);
	}

	@Override
	public boolean func_149698_L() {
		return true;
	}

	private boolean func_149807_p(World p_149807_1_, int p_149807_2_,
			int p_149807_3_, int p_149807_4_) {
		final Block var5 = p_149807_1_.getBlock(p_149807_2_, p_149807_3_,
				p_149807_4_);
		return var5 != Blocks.wooden_door && var5 != Blocks.iron_door
				&& var5 != Blocks.standing_sign && var5 != Blocks.ladder
				&& var5 != Blocks.reeds ? var5.blockMaterial == Material.Portal ? true
				: var5.blockMaterial.blocksMovement()
				: true;
	}

	private boolean[] func_149808_o(World p_149808_1_, int p_149808_2_,
			int p_149808_3_, int p_149808_4_) {
		int var5;
		int var6;

		for (var5 = 0; var5 < 4; ++var5) {
			field_149816_M[var5] = 1000;
			var6 = p_149808_2_;
			int var8 = p_149808_4_;

			if (var5 == 0) {
				var6 = p_149808_2_ - 1;
			}

			if (var5 == 1) {
				++var6;
			}

			if (var5 == 2) {
				var8 = p_149808_4_ - 1;
			}

			if (var5 == 3) {
				++var8;
			}

			if (!func_149807_p(p_149808_1_, var6, p_149808_3_, var8)
					&& (p_149808_1_.getBlock(var6, p_149808_3_, var8)
							.getMaterial() != blockMaterial || p_149808_1_
							.getBlockMetadata(var6, p_149808_3_, var8) != 0)) {
				if (func_149807_p(p_149808_1_, var6, p_149808_3_ - 1, var8)) {
					field_149816_M[var5] = func_149812_c(p_149808_1_, var6,
							p_149808_3_, var8, 1, var5);
				} else {
					field_149816_M[var5] = 0;
				}
			}
		}

		var5 = field_149816_M[0];

		for (var6 = 1; var6 < 4; ++var6) {
			if (field_149816_M[var6] < var5) {
				var5 = field_149816_M[var6];
			}
		}

		for (var6 = 0; var6 < 4; ++var6) {
			field_149814_b[var6] = field_149816_M[var6] == var5;
		}

		return field_149814_b;
	}

	private boolean func_149809_q(World p_149809_1_, int p_149809_2_,
			int p_149809_3_, int p_149809_4_) {
		final Material var5 = p_149809_1_.getBlock(p_149809_2_, p_149809_3_,
				p_149809_4_).getMaterial();
		return var5 == blockMaterial ? false : var5 == Material.lava ? false
				: !func_149807_p(p_149809_1_, p_149809_2_, p_149809_3_,
						p_149809_4_);
	}

	protected int func_149810_a(World p_149810_1_, int p_149810_2_,
			int p_149810_3_, int p_149810_4_, int p_149810_5_) {
		int var6 = func_149804_e(p_149810_1_, p_149810_2_, p_149810_3_,
				p_149810_4_);

		if (var6 < 0)
			return p_149810_5_;
		else {
			if (var6 == 0) {
				++field_149815_a;
			}

			if (var6 >= 8) {
				var6 = 0;
			}

			return p_149810_5_ >= 0 && var6 >= p_149810_5_ ? p_149810_5_ : var6;
		}
	}

	private void func_149811_n(World p_149811_1_, int p_149811_2_,
			int p_149811_3_, int p_149811_4_) {
		final int var5 = p_149811_1_.getBlockMetadata(p_149811_2_, p_149811_3_,
				p_149811_4_);
		p_149811_1_.setBlock(p_149811_2_, p_149811_3_, p_149811_4_,
				Block.getBlockById(Block.getIdFromBlock(this) + 1), var5, 2);
	}

	private int func_149812_c(World p_149812_1_, int p_149812_2_,
			int p_149812_3_, int p_149812_4_, int p_149812_5_, int p_149812_6_) {
		int var7 = 1000;

		for (int var8 = 0; var8 < 4; ++var8) {
			if ((var8 != 0 || p_149812_6_ != 1)
					&& (var8 != 1 || p_149812_6_ != 0)
					&& (var8 != 2 || p_149812_6_ != 3)
					&& (var8 != 3 || p_149812_6_ != 2)) {
				int var9 = p_149812_2_;
				int var11 = p_149812_4_;

				if (var8 == 0) {
					var9 = p_149812_2_ - 1;
				}

				if (var8 == 1) {
					++var9;
				}

				if (var8 == 2) {
					var11 = p_149812_4_ - 1;
				}

				if (var8 == 3) {
					++var11;
				}

				if (!func_149807_p(p_149812_1_, var9, p_149812_3_, var11)
						&& (p_149812_1_.getBlock(var9, p_149812_3_, var11)
								.getMaterial() != blockMaterial || p_149812_1_
								.getBlockMetadata(var9, p_149812_3_, var11) != 0)) {
					if (!func_149807_p(p_149812_1_, var9, p_149812_3_ - 1,
							var11))
						return p_149812_5_;

					if (p_149812_5_ < 4) {
						final int var12 = func_149812_c(p_149812_1_, var9,
								p_149812_3_, var11, p_149812_5_ + 1, var8);

						if (var12 < var7) {
							var7 = var12;
						}
					}
				}
			}
		}

		return var7;
	}

	private void func_149813_h(World p_149813_1_, int p_149813_2_,
			int p_149813_3_, int p_149813_4_, int p_149813_5_) {
		if (func_149809_q(p_149813_1_, p_149813_2_, p_149813_3_, p_149813_4_)) {
			final Block var6 = p_149813_1_.getBlock(p_149813_2_, p_149813_3_,
					p_149813_4_);

			if (blockMaterial == Material.lava) {
				func_149799_m(p_149813_1_, p_149813_2_, p_149813_3_,
						p_149813_4_);
			} else {
				var6.dropBlockAsItem(p_149813_1_, p_149813_2_, p_149813_3_,
						p_149813_4_, p_149813_1_.getBlockMetadata(p_149813_2_,
								p_149813_3_, p_149813_4_), 0);
			}

			p_149813_1_.setBlock(p_149813_2_, p_149813_3_, p_149813_4_, this,
					p_149813_5_, 3);
		}
	}

	@Override
	public void onBlockAdded(World p_149726_1_, int p_149726_2_,
			int p_149726_3_, int p_149726_4_) {
		super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);

		if (p_149726_1_.getBlock(p_149726_2_, p_149726_3_, p_149726_4_) == this) {
			p_149726_1_.scheduleBlockUpdate(p_149726_2_, p_149726_3_,
					p_149726_4_, this, func_149738_a(p_149726_1_));
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		int var6 = func_149804_e(p_149674_1_, p_149674_2_, p_149674_3_,
				p_149674_4_);
		byte var7 = 1;

		if (blockMaterial == Material.lava && !p_149674_1_.provider.isHellWorld) {
			var7 = 2;
		}

		final boolean var8 = true;
		int var9 = func_149738_a(p_149674_1_);
		int var11;

		if (var6 > 0) {
			final byte var10 = -100;
			field_149815_a = 0;
			int var13 = func_149810_a(p_149674_1_, p_149674_2_ - 1,
					p_149674_3_, p_149674_4_, var10);
			var13 = func_149810_a(p_149674_1_, p_149674_2_ + 1, p_149674_3_,
					p_149674_4_, var13);
			var13 = func_149810_a(p_149674_1_, p_149674_2_, p_149674_3_,
					p_149674_4_ - 1, var13);
			var13 = func_149810_a(p_149674_1_, p_149674_2_, p_149674_3_,
					p_149674_4_ + 1, var13);
			var11 = var13 + var7;

			if (var11 >= 8 || var13 < 0) {
				var11 = -1;
			}

			if (func_149804_e(p_149674_1_, p_149674_2_, p_149674_3_ + 1,
					p_149674_4_) >= 0) {
				final int var12 = func_149804_e(p_149674_1_, p_149674_2_,
						p_149674_3_ + 1, p_149674_4_);

				if (var12 >= 8) {
					var11 = var12;
				} else {
					var11 = var12 + 8;
				}
			}

			if (field_149815_a >= 2 && blockMaterial == Material.water) {
				if (p_149674_1_
						.getBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_)
						.getMaterial().isSolid()) {
					var11 = 0;
				} else if (p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - 1,
						p_149674_4_).getMaterial() == blockMaterial
						&& p_149674_1_.getBlockMetadata(p_149674_2_,
								p_149674_3_ - 1, p_149674_4_) == 0) {
					var11 = 0;
				}
			}

			if (blockMaterial == Material.lava && var6 < 8 && var11 < 8
					&& var11 > var6 && p_149674_5_.nextInt(4) != 0) {
				var9 *= 4;
			}

			if (var11 == var6) {
				if (var8) {
					func_149811_n(p_149674_1_, p_149674_2_, p_149674_3_,
							p_149674_4_);
				}
			} else {
				var6 = var11;

				if (var11 < 0) {
					p_149674_1_.setBlockToAir(p_149674_2_, p_149674_3_,
							p_149674_4_);
				} else {
					p_149674_1_.setBlockMetadataWithNotify(p_149674_2_,
							p_149674_3_, p_149674_4_, var11, 2);
					p_149674_1_.scheduleBlockUpdate(p_149674_2_, p_149674_3_,
							p_149674_4_, this, var9);
					p_149674_1_.notifyBlocksOfNeighborChange(p_149674_2_,
							p_149674_3_, p_149674_4_, this);
				}
			}
		} else {
			func_149811_n(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_);
		}

		if (func_149809_q(p_149674_1_, p_149674_2_, p_149674_3_ - 1,
				p_149674_4_)) {
			if (blockMaterial == Material.lava
					&& p_149674_1_.getBlock(p_149674_2_, p_149674_3_ - 1,
							p_149674_4_).getMaterial() == Material.water) {
				p_149674_1_.setBlock(p_149674_2_, p_149674_3_ - 1, p_149674_4_,
						Blocks.stone);
				func_149799_m(p_149674_1_, p_149674_2_, p_149674_3_ - 1,
						p_149674_4_);
				return;
			}

			if (var6 >= 8) {
				func_149813_h(p_149674_1_, p_149674_2_, p_149674_3_ - 1,
						p_149674_4_, var6);
			} else {
				func_149813_h(p_149674_1_, p_149674_2_, p_149674_3_ - 1,
						p_149674_4_, var6 + 8);
			}
		} else if (var6 >= 0
				&& (var6 == 0 || func_149807_p(p_149674_1_, p_149674_2_,
						p_149674_3_ - 1, p_149674_4_))) {
			final boolean[] var14 = func_149808_o(p_149674_1_, p_149674_2_,
					p_149674_3_, p_149674_4_);
			var11 = var6 + var7;

			if (var6 >= 8) {
				var11 = 1;
			}

			if (var11 >= 8)
				return;

			if (var14[0]) {
				func_149813_h(p_149674_1_, p_149674_2_ - 1, p_149674_3_,
						p_149674_4_, var11);
			}

			if (var14[1]) {
				func_149813_h(p_149674_1_, p_149674_2_ + 1, p_149674_3_,
						p_149674_4_, var11);
			}

			if (var14[2]) {
				func_149813_h(p_149674_1_, p_149674_2_, p_149674_3_,
						p_149674_4_ - 1, var11);
			}

			if (var14[3]) {
				func_149813_h(p_149674_1_, p_149674_2_, p_149674_3_,
						p_149674_4_ + 1, var11);
			}
		}
	}
}

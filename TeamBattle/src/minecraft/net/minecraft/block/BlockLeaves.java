package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLeaves extends BlockLeavesBase {
	protected int field_150127_b;
	int[] field_150128_a;
	protected IIcon[][] field_150129_M = new IIcon[2][];

	public BlockLeaves() {
		super(Material.leaves, false);
		setTickRandomly(true);
		setCreativeTab(CreativeTabs.tabDecorations);
		setHardness(0.2F);
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_,
			int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
		final byte var7 = 1;
		final int var8 = var7 + 1;

		if (p_149749_1_.checkChunksExist(p_149749_2_ - var8,
				p_149749_3_ - var8, p_149749_4_ - var8, p_149749_2_ + var8,
				p_149749_3_ + var8, p_149749_4_ + var8)) {
			for (int var9 = -var7; var9 <= var7; ++var9) {
				for (int var10 = -var7; var10 <= var7; ++var10) {
					for (int var11 = -var7; var11 <= var7; ++var11) {
						if (p_149749_1_.getBlock(p_149749_2_ + var9,
								p_149749_3_ + var10, p_149749_4_ + var11)
								.getMaterial() == Material.leaves) {
							final int var12 = p_149749_1_.getBlockMetadata(
									p_149749_2_ + var9, p_149749_3_ + var10,
									p_149749_4_ + var11);
							p_149749_1_.setBlockMetadataWithNotify(p_149749_2_
									+ var9, p_149749_3_ + var10, p_149749_4_
									+ var11, var12 | 8, 4);
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied
	 * against the blocks color. Note only called when first determining what to
	 * render.
	 */
	@Override
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_,
			int p_149720_3_, int p_149720_4_) {
		int var5 = 0;
		int var6 = 0;
		int var7 = 0;

		for (int var8 = -1; var8 <= 1; ++var8) {
			for (int var9 = -1; var9 <= 1; ++var9) {
				final int var10 = p_149720_1_.getBiomeGenForCoords(
						p_149720_2_ + var9, p_149720_4_ + var8)
						.getBiomeFoliageColor(p_149720_2_ + var9, p_149720_3_,
								p_149720_4_ + var8);
				var5 += (var10 & 16711680) >> 16;
				var6 += (var10 & 65280) >> 8;
				var7 += var10 & 255;
			}
		}

		return (var5 / 9 & 255) << 16 | (var6 / 9 & 255) << 8 | var7 / 9 & 255;
	}

	/**
	 * Returns an item stack containing a single instance of the current block
	 * type. 'i' is the block's subtype/damage and is ignored for blocks which
	 * do not support subtypes. Blocks which cannot be harvested should return
	 * null.
	 */
	@Override
	protected ItemStack createStackedBlock(int p_149644_1_) {
		return new ItemStack(Item.getItemFromBlock(this), 1, p_149644_1_ & 3);
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_ & 3;
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified
	 * items
	 */
	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_,
			int p_149690_3_, int p_149690_4_, int p_149690_5_,
			float p_149690_6_, int p_149690_7_) {
		if (!p_149690_1_.isClient) {
			int var8 = func_150123_b(p_149690_5_);

			if (p_149690_7_ > 0) {
				var8 -= 2 << p_149690_7_;

				if (var8 < 10) {
					var8 = 10;
				}
			}

			if (p_149690_1_.rand.nextInt(var8) == 0) {
				final Item var9 = getItemDropped(p_149690_5_, p_149690_1_.rand,
						p_149690_7_);
				dropBlockAsItem_do(p_149690_1_, p_149690_2_, p_149690_3_,
						p_149690_4_, new ItemStack(var9, 1,
								damageDropped(p_149690_5_)));
			}

			var8 = 200;

			if (p_149690_7_ > 0) {
				var8 -= 10 << p_149690_7_;

				if (var8 < 40) {
					var8 = 40;
				}
			}

			func_150124_c(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_,
					p_149690_5_, var8);
		}
	}

	public void func_150122_b(boolean p_150122_1_) {
		field_150121_P = p_150122_1_;
		field_150127_b = p_150122_1_ ? 0 : 1;
	}

	protected int func_150123_b(int p_150123_1_) {
		return 20;
	}

	protected void func_150124_c(World p_150124_1_, int p_150124_2_,
			int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {
	}

	public abstract String[] func_150125_e();

	private void func_150126_e(World p_150126_1_, int p_150126_2_,
			int p_150126_3_, int p_150126_4_) {
		dropBlockAsItem(p_150126_1_, p_150126_2_, p_150126_3_, p_150126_4_,
				p_150126_1_.getBlockMetadata(p_150126_2_, p_150126_3_,
						p_150126_4_), 0);
		p_150126_1_.setBlockToAir(p_150126_2_, p_150126_3_, p_150126_4_);
	}

	@Override
	public int getBlockColor() {
		final double var1 = 0.5D;
		final double var3 = 1.0D;
		return ColorizerFoliage.getFoliageColor(var1, var3);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public abstract IIcon getIcon(int p_149691_1_, int p_149691_2_);

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.sapling);
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@Override
	public int getRenderColor(int p_149741_1_) {
		return ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	public void harvestBlock(World p_149636_1_, EntityPlayer p_149636_2_,
			int p_149636_3_, int p_149636_4_, int p_149636_5_, int p_149636_6_) {
		if (!p_149636_1_.isClient
				&& p_149636_2_.getCurrentEquippedItem() != null
				&& p_149636_2_.getCurrentEquippedItem().getItem() == Items.shears) {
			p_149636_2_.addStat(
					StatList.mineBlockStatArray[Block.getIdFromBlock(this)], 1);
			dropBlockAsItem_do(p_149636_1_, p_149636_3_, p_149636_4_,
					p_149636_5_, new ItemStack(Item.getItemFromBlock(this), 1,
							p_149636_6_ & 3));
		} else {
			super.harvestBlock(p_149636_1_, p_149636_2_, p_149636_3_,
					p_149636_4_, p_149636_5_, p_149636_6_);
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return !field_150121_P;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return p_149745_1_.nextInt(20) == 0 ? 1 : 0;
	}

	/**
	 * A randomly called display update to be able to add particles or other
	 * items for display
	 */
	@Override
	public void randomDisplayTick(World p_149734_1_, int p_149734_2_,
			int p_149734_3_, int p_149734_4_, Random p_149734_5_) {
		if (p_149734_1_.canLightningStrikeAt(p_149734_2_, p_149734_3_ + 1,
				p_149734_4_)
				&& !World.doesBlockHaveSolidTopSurface(p_149734_1_,
						p_149734_2_, p_149734_3_ - 1, p_149734_4_)
				&& p_149734_5_.nextInt(15) == 1) {
			final double var6 = p_149734_2_ + p_149734_5_.nextFloat();
			final double var8 = p_149734_3_ - 0.05D;
			final double var10 = p_149734_4_ + p_149734_5_.nextFloat();
			p_149734_1_.spawnParticle("dripWater", var6, var8, var10, 0.0D,
					0.0D, 0.0D);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		if (!p_149674_1_.isClient) {
			final int var6 = p_149674_1_.getBlockMetadata(p_149674_2_,
					p_149674_3_, p_149674_4_);

			if ((var6 & 8) != 0 && (var6 & 4) == 0) {
				final byte var7 = 4;
				final int var8 = var7 + 1;
				final byte var9 = 32;
				final int var10 = var9 * var9;
				final int var11 = var9 / 2;

				if (field_150128_a == null) {
					field_150128_a = new int[var9 * var9 * var9];
				}

				int var12;

				if (p_149674_1_.checkChunksExist(p_149674_2_ - var8,
						p_149674_3_ - var8, p_149674_4_ - var8, p_149674_2_
								+ var8, p_149674_3_ + var8, p_149674_4_ + var8)) {
					int var13;
					int var14;

					for (var12 = -var7; var12 <= var7; ++var12) {
						for (var13 = -var7; var13 <= var7; ++var13) {
							for (var14 = -var7; var14 <= var7; ++var14) {
								final Block var15 = p_149674_1_.getBlock(
										p_149674_2_ + var12, p_149674_3_
												+ var13, p_149674_4_ + var14);

								if (var15 != Blocks.log && var15 != Blocks.log2) {
									if (var15.getMaterial() == Material.leaves) {
										field_150128_a[(var12 + var11) * var10
												+ (var13 + var11) * var9
												+ var14 + var11] = -2;
									} else {
										field_150128_a[(var12 + var11) * var10
												+ (var13 + var11) * var9
												+ var14 + var11] = -1;
									}
								} else {
									field_150128_a[(var12 + var11) * var10
											+ (var13 + var11) * var9 + var14
											+ var11] = 0;
								}
							}
						}
					}

					for (var12 = 1; var12 <= 4; ++var12) {
						for (var13 = -var7; var13 <= var7; ++var13) {
							for (var14 = -var7; var14 <= var7; ++var14) {
								for (int var16 = -var7; var16 <= var7; ++var16) {
									if (field_150128_a[(var13 + var11) * var10
											+ (var14 + var11) * var9 + var16
											+ var11] == var12 - 1) {
										if (field_150128_a[(var13 + var11 - 1)
												* var10 + (var14 + var11)
												* var9 + var16 + var11] == -2) {
											field_150128_a[(var13 + var11 - 1)
													* var10 + (var14 + var11)
													* var9 + var16 + var11] = var12;
										}

										if (field_150128_a[(var13 + var11 + 1)
												* var10 + (var14 + var11)
												* var9 + var16 + var11] == -2) {
											field_150128_a[(var13 + var11 + 1)
													* var10 + (var14 + var11)
													* var9 + var16 + var11] = var12;
										}

										if (field_150128_a[(var13 + var11)
												* var10 + (var14 + var11 - 1)
												* var9 + var16 + var11] == -2) {
											field_150128_a[(var13 + var11)
													* var10
													+ (var14 + var11 - 1)
													* var9 + var16 + var11] = var12;
										}

										if (field_150128_a[(var13 + var11)
												* var10 + (var14 + var11 + 1)
												* var9 + var16 + var11] == -2) {
											field_150128_a[(var13 + var11)
													* var10
													+ (var14 + var11 + 1)
													* var9 + var16 + var11] = var12;
										}

										if (field_150128_a[(var13 + var11)
												* var10 + (var14 + var11)
												* var9 + var16 + var11 - 1] == -2) {
											field_150128_a[(var13 + var11)
													* var10 + (var14 + var11)
													* var9 + var16 + var11 - 1] = var12;
										}

										if (field_150128_a[(var13 + var11)
												* var10 + (var14 + var11)
												* var9 + var16 + var11 + 1] == -2) {
											field_150128_a[(var13 + var11)
													* var10 + (var14 + var11)
													* var9 + var16 + var11 + 1] = var12;
										}
									}
								}
							}
						}
					}
				}

				var12 = field_150128_a[var11 * var10 + var11 * var9 + var11];

				if (var12 >= 0) {
					p_149674_1_.setBlockMetadataWithNotify(p_149674_2_,
							p_149674_3_, p_149674_4_, var6 & -9, 4);
				} else {
					func_150126_e(p_149674_1_, p_149674_2_, p_149674_3_,
							p_149674_4_);
				}
			}
		}
	}
}

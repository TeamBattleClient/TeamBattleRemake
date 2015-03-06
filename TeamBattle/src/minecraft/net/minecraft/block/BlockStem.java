package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStem extends BlockBush implements IGrowable {
	private IIcon field_149876_b;
	private final Block field_149877_a;

	protected BlockStem(Block p_i45430_1_) {
		field_149877_a = p_i45430_1_;
		setTickRandomly(true);
		final float var2 = 0.125F;
		setBlockBounds(0.5F - var2, 0.0F, 0.5F - var2, 0.5F + var2, 0.25F,
				0.5F + var2);
		setCreativeTab((CreativeTabs) null);
	}

	/**
	 * Returns a integer with hex for 0xrrggbb with this color multiplied
	 * against the blocks color. Note only called when first determining what to
	 * render.
	 */
	@Override
	public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_,
			int p_149720_3_, int p_149720_4_) {
		return getRenderColor(p_149720_1_.getBlockMetadata(p_149720_2_,
				p_149720_3_, p_149720_4_));
	}

	/**
	 * Drops the block items with a specified chance of dropping the specified
	 * items
	 */
	@Override
	public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_,
			int p_149690_3_, int p_149690_4_, int p_149690_5_,
			float p_149690_6_, int p_149690_7_) {
		super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_,
				p_149690_4_, p_149690_5_, p_149690_6_, p_149690_7_);

		if (!p_149690_1_.isClient) {
			Item var8 = null;

			if (field_149877_a == Blocks.pumpkin) {
				var8 = Items.pumpkin_seeds;
			}

			if (field_149877_a == Blocks.melon_block) {
				var8 = Items.melon_seeds;
			}

			for (int var9 = 0; var9 < 3; ++var9) {
				if (p_149690_1_.rand.nextInt(15) <= p_149690_5_) {
					dropBlockAsItem_do(p_149690_1_, p_149690_2_, p_149690_3_,
							p_149690_4_, new ItemStack(var8));
				}
			}
		}
	}

	@Override
	public boolean func_149851_a(World p_149851_1_, int p_149851_2_,
			int p_149851_3_, int p_149851_4_, boolean p_149851_5_) {
		return p_149851_1_.getBlockMetadata(p_149851_2_, p_149851_3_,
				p_149851_4_) != 7;
	}

	@Override
	public boolean func_149852_a(World p_149852_1_, Random p_149852_2_,
			int p_149852_3_, int p_149852_4_, int p_149852_5_) {
		return true;
	}

	@Override
	public void func_149853_b(World p_149853_1_, Random p_149853_2_,
			int p_149853_3_, int p_149853_4_, int p_149853_5_) {
		func_149874_m(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_);
	}

	@Override
	protected boolean func_149854_a(Block p_149854_1_) {
		return p_149854_1_ == Blocks.farmland;
	}

	public IIcon func_149872_i() {
		return field_149876_b;
	}

	public int func_149873_e(IBlockAccess p_149873_1_, int p_149873_2_,
			int p_149873_3_, int p_149873_4_) {
		final int var5 = p_149873_1_.getBlockMetadata(p_149873_2_, p_149873_3_,
				p_149873_4_);
		return var5 < 7 ? -1
				: p_149873_1_.getBlock(p_149873_2_ - 1, p_149873_3_,
						p_149873_4_) == field_149877_a ? 0
						: p_149873_1_.getBlock(p_149873_2_ + 1, p_149873_3_,
								p_149873_4_) == field_149877_a ? 1
								: p_149873_1_.getBlock(p_149873_2_,
										p_149873_3_, p_149873_4_ - 1) == field_149877_a ? 2
										: p_149873_1_.getBlock(p_149873_2_,
												p_149873_3_, p_149873_4_ + 1) == field_149877_a ? 3
												: -1;
	}

	public void func_149874_m(World p_149874_1_, int p_149874_2_,
			int p_149874_3_, int p_149874_4_) {
		int var5 = p_149874_1_.getBlockMetadata(p_149874_2_, p_149874_3_,
				p_149874_4_)
				+ MathHelper.getRandomIntegerInRange(p_149874_1_.rand, 2, 5);

		if (var5 > 7) {
			var5 = 7;
		}

		p_149874_1_.setBlockMetadataWithNotify(p_149874_2_, p_149874_3_,
				p_149874_4_, var5, 2);
	}

	private float func_149875_n(World p_149875_1_, int p_149875_2_,
			int p_149875_3_, int p_149875_4_) {
		float var5 = 1.0F;
		final Block var6 = p_149875_1_.getBlock(p_149875_2_, p_149875_3_,
				p_149875_4_ - 1);
		final Block var7 = p_149875_1_.getBlock(p_149875_2_, p_149875_3_,
				p_149875_4_ + 1);
		final Block var8 = p_149875_1_.getBlock(p_149875_2_ - 1, p_149875_3_,
				p_149875_4_);
		final Block var9 = p_149875_1_.getBlock(p_149875_2_ + 1, p_149875_3_,
				p_149875_4_);
		final Block var10 = p_149875_1_.getBlock(p_149875_2_ - 1, p_149875_3_,
				p_149875_4_ - 1);
		final Block var11 = p_149875_1_.getBlock(p_149875_2_ + 1, p_149875_3_,
				p_149875_4_ - 1);
		final Block var12 = p_149875_1_.getBlock(p_149875_2_ + 1, p_149875_3_,
				p_149875_4_ + 1);
		final Block var13 = p_149875_1_.getBlock(p_149875_2_ - 1, p_149875_3_,
				p_149875_4_ + 1);
		final boolean var14 = var8 == this || var9 == this;
		final boolean var15 = var6 == this || var7 == this;
		final boolean var16 = var10 == this || var11 == this || var12 == this
				|| var13 == this;

		for (int var17 = p_149875_2_ - 1; var17 <= p_149875_2_ + 1; ++var17) {
			for (int var18 = p_149875_4_ - 1; var18 <= p_149875_4_ + 1; ++var18) {
				final Block var19 = p_149875_1_.getBlock(var17,
						p_149875_3_ - 1, var18);
				float var20 = 0.0F;

				if (var19 == Blocks.farmland) {
					var20 = 1.0F;

					if (p_149875_1_.getBlockMetadata(var17, p_149875_3_ - 1,
							var18) > 0) {
						var20 = 3.0F;
					}
				}

				if (var17 != p_149875_2_ || var18 != p_149875_4_) {
					var20 /= 4.0F;
				}

				var5 += var20;
			}
		}

		if (var16 || var14 && var15) {
			var5 /= 2.0F;
		}

		return var5;
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return field_149877_a == Blocks.pumpkin ? Items.pumpkin_seeds
				: field_149877_a == Blocks.melon_block ? Items.melon_seeds
						: Item.getItemById(0);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return null;
	}

	/**
	 * Returns the color this block should be rendered. Used by leaves.
	 */
	@Override
	public int getRenderColor(int p_149741_1_) {
		final int var2 = p_149741_1_ * 32;
		final int var3 = 255 - p_149741_1_ * 8;
		final int var4 = p_149741_1_ * 4;
		return var2 << 16 | var3 << 8 | var4;
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 19;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 1;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_
				.registerIcon(getTextureName() + "_disconnected");
		field_149876_b = p_149651_1_.registerIcon(getTextureName()
				+ "_connected");
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_,
			int p_149719_2_, int p_149719_3_, int p_149719_4_) {
		field_149756_F = (p_149719_1_.getBlockMetadata(p_149719_2_,
				p_149719_3_, p_149719_4_) * 2 + 2) / 16.0F;
		final float var5 = 0.125F;
		setBlockBounds(0.5F - var5, 0.0F, 0.5F - var5, 0.5F + var5,
				(float) field_149756_F, 0.5F + var5);
	}

	/**
	 * Sets the block's bounds for rendering it as an item
	 */
	@Override
	public void setBlockBoundsForItemRender() {
		final float var1 = 0.125F;
		setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F,
				0.5F + var1);
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_,
				p_149674_5_);

		if (p_149674_1_.getBlockLightValue(p_149674_2_, p_149674_3_ + 1,
				p_149674_4_) >= 9) {
			final float var6 = func_149875_n(p_149674_1_, p_149674_2_,
					p_149674_3_, p_149674_4_);

			if (p_149674_5_.nextInt((int) (25.0F / var6) + 1) == 0) {
				int var7 = p_149674_1_.getBlockMetadata(p_149674_2_,
						p_149674_3_, p_149674_4_);

				if (var7 < 7) {
					++var7;
					p_149674_1_.setBlockMetadataWithNotify(p_149674_2_,
							p_149674_3_, p_149674_4_, var7, 2);
				} else {
					if (p_149674_1_.getBlock(p_149674_2_ - 1, p_149674_3_,
							p_149674_4_) == field_149877_a)
						return;

					if (p_149674_1_.getBlock(p_149674_2_ + 1, p_149674_3_,
							p_149674_4_) == field_149877_a)
						return;

					if (p_149674_1_.getBlock(p_149674_2_, p_149674_3_,
							p_149674_4_ - 1) == field_149877_a)
						return;

					if (p_149674_1_.getBlock(p_149674_2_, p_149674_3_,
							p_149674_4_ + 1) == field_149877_a)
						return;

					final int var8 = p_149674_5_.nextInt(4);
					int var9 = p_149674_2_;
					int var10 = p_149674_4_;

					if (var8 == 0) {
						var9 = p_149674_2_ - 1;
					}

					if (var8 == 1) {
						++var9;
					}

					if (var8 == 2) {
						var10 = p_149674_4_ - 1;
					}

					if (var8 == 3) {
						++var10;
					}

					final Block var11 = p_149674_1_.getBlock(var9,
							p_149674_3_ - 1, var10);

					if (p_149674_1_.getBlock(var9, p_149674_3_, var10).blockMaterial == Material.air
							&& (var11 == Blocks.farmland
									|| var11 == Blocks.dirt || var11 == Blocks.grass)) {
						p_149674_1_.setBlock(var9, p_149674_3_, var10,
								field_149877_a);
					}
				}
			}
		}
	}
}

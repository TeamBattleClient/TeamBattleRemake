package net.minecraft.block;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCrops extends BlockBush implements IGrowable {
	private IIcon[] field_149867_a;

	protected BlockCrops() {
		setTickRandomly(true);
		final float var1 = 0.5F;
		setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F,
				0.5F + var1);
		setCreativeTab((CreativeTabs) null);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		disableStats();
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
				p_149690_4_, p_149690_5_, p_149690_6_, 0);

		if (!p_149690_1_.isClient) {
			if (p_149690_5_ >= 7) {
				final int var8 = 3 + p_149690_7_;

				for (int var9 = 0; var9 < var8; ++var9) {
					if (p_149690_1_.rand.nextInt(15) <= p_149690_5_) {
						dropBlockAsItem_do(p_149690_1_, p_149690_2_,
								p_149690_3_, p_149690_4_, new ItemStack(
										func_149866_i(), 1, 0));
					}
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
		func_149863_m(p_149853_1_, p_149853_3_, p_149853_4_, p_149853_5_);
	}

	@Override
	protected boolean func_149854_a(Block p_149854_1_) {
		return p_149854_1_ == Blocks.farmland;
	}

	public void func_149863_m(World p_149863_1_, int p_149863_2_,
			int p_149863_3_, int p_149863_4_) {
		int var5 = p_149863_1_.getBlockMetadata(p_149863_2_, p_149863_3_,
				p_149863_4_)
				+ MathHelper.getRandomIntegerInRange(p_149863_1_.rand, 2, 5);

		if (var5 > 7) {
			var5 = 7;
		}

		p_149863_1_.setBlockMetadataWithNotify(p_149863_2_, p_149863_3_,
				p_149863_4_, var5, 2);
	}

	private float func_149864_n(World p_149864_1_, int p_149864_2_,
			int p_149864_3_, int p_149864_4_) {
		float var5 = 1.0F;
		final Block var6 = p_149864_1_.getBlock(p_149864_2_, p_149864_3_,
				p_149864_4_ - 1);
		final Block var7 = p_149864_1_.getBlock(p_149864_2_, p_149864_3_,
				p_149864_4_ + 1);
		final Block var8 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_,
				p_149864_4_);
		final Block var9 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_,
				p_149864_4_);
		final Block var10 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_,
				p_149864_4_ - 1);
		final Block var11 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_,
				p_149864_4_ - 1);
		final Block var12 = p_149864_1_.getBlock(p_149864_2_ + 1, p_149864_3_,
				p_149864_4_ + 1);
		final Block var13 = p_149864_1_.getBlock(p_149864_2_ - 1, p_149864_3_,
				p_149864_4_ + 1);
		final boolean var14 = var8 == this || var9 == this;
		final boolean var15 = var6 == this || var7 == this;
		final boolean var16 = var10 == this || var11 == this || var12 == this
				|| var13 == this;

		for (int var17 = p_149864_2_ - 1; var17 <= p_149864_2_ + 1; ++var17) {
			for (int var18 = p_149864_4_ - 1; var18 <= p_149864_4_ + 1; ++var18) {
				float var19 = 0.0F;

				if (p_149864_1_.getBlock(var17, p_149864_3_ - 1, var18) == Blocks.farmland) {
					var19 = 1.0F;

					if (p_149864_1_.getBlockMetadata(var17, p_149864_3_ - 1,
							var18) > 0) {
						var19 = 3.0F;
					}
				}

				if (var17 != p_149864_2_ || var18 != p_149864_4_) {
					var19 /= 4.0F;
				}

				var5 += var19;
			}
		}

		if (var16 || var14 && var15) {
			var5 /= 2.0F;
		}

		return var5;
	}

	protected Item func_149865_P() {
		return Items.wheat;
	}

	protected Item func_149866_i() {
		return Items.wheat_seeds;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		if (p_149691_2_ < 0 || p_149691_2_ > 7) {
			p_149691_2_ = 7;
		}

		return field_149867_a[p_149691_2_];
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return func_149866_i();
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return p_149650_1_ == 7 ? func_149865_P() : func_149866_i();
	}

	/**
	 * The type of render function that is called for this block
	 */
	@Override
	public int getRenderType() {
		return 6;
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
		field_149867_a = new IIcon[8];

		for (int var2 = 0; var2 < field_149867_a.length; ++var2) {
			field_149867_a[var2] = p_149651_1_.registerIcon(getTextureName()
					+ "_stage_" + var2);
		}
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
			int var6 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_,
					p_149674_4_);

			if (var6 < 7) {
				final float var7 = func_149864_n(p_149674_1_, p_149674_2_,
						p_149674_3_, p_149674_4_);

				if (p_149674_5_.nextInt((int) (25.0F / var7) + 1) == 0) {
					++var6;
					p_149674_1_.setBlockMetadataWithNotify(p_149674_2_,
							p_149674_3_, p_149674_4_, var6, 2);
				}
			}
		}
	}
}

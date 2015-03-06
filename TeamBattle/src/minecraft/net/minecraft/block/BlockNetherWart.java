package net.minecraft.block;

import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockNetherWart extends BlockBush {
	private IIcon[] field_149883_a;

	protected BlockNetherWart() {
		setTickRandomly(true);
		final float var1 = 0.5F;
		setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F,
				0.5F + var1);
		setCreativeTab((CreativeTabs) null);
	}

	/**
	 * Can this block stay at this position. Similar to canPlaceBlockAt except
	 * gets checked often with plants.
	 */
	@Override
	public boolean canBlockStay(World p_149718_1_, int p_149718_2_,
			int p_149718_3_, int p_149718_4_) {
		return func_149854_a(p_149718_1_.getBlock(p_149718_2_, p_149718_3_ - 1,
				p_149718_4_));
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
			int var8 = 1;

			if (p_149690_5_ >= 3) {
				var8 = 2 + p_149690_1_.rand.nextInt(3);

				if (p_149690_7_ > 0) {
					var8 += p_149690_1_.rand.nextInt(p_149690_7_ + 1);
				}
			}

			for (int var9 = 0; var9 < var8; ++var9) {
				dropBlockAsItem_do(p_149690_1_, p_149690_2_, p_149690_3_,
						p_149690_4_, new ItemStack(Items.nether_wart));
			}
		}
	}

	@Override
	protected boolean func_149854_a(Block p_149854_1_) {
		return p_149854_1_ == Blocks.soul_sand;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_2_ >= 3 ? field_149883_a[2]
				: p_149691_2_ > 0 ? field_149883_a[1] : field_149883_a[0];
	}

	/**
	 * Gets an item for the block being called on. Args: world, x, y, z
	 */
	@Override
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_,
			int p_149694_4_) {
		return Items.nether_wart;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return null;
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
		return 0;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149883_a = new IIcon[3];

		for (int var2 = 0; var2 < field_149883_a.length; ++var2) {
			field_149883_a[var2] = p_149651_1_.registerIcon(getTextureName()
					+ "_stage_" + var2);
		}
	}

	/**
	 * Ticks the block if it's been scheduled
	 */
	@Override
	public void updateTick(World p_149674_1_, int p_149674_2_, int p_149674_3_,
			int p_149674_4_, Random p_149674_5_) {
		int var6 = p_149674_1_.getBlockMetadata(p_149674_2_, p_149674_3_,
				p_149674_4_);

		if (var6 < 3 && p_149674_5_.nextInt(10) == 0) {
			++var6;
			p_149674_1_.setBlockMetadataWithNotify(p_149674_2_, p_149674_3_,
					p_149674_4_, var6, 2);
		}

		super.updateTick(p_149674_1_, p_149674_2_, p_149674_3_, p_149674_4_,
				p_149674_5_);
	}
}

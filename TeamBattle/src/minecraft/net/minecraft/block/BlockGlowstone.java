package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;

public class BlockGlowstone extends Block {

	public BlockGlowstone(Material p_i45409_1_) {
		super(p_i45409_1_);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Items.glowstone_dust;
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return MapColor.field_151658_d;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 2 + p_149745_1_.nextInt(3);
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i'
	 * (inclusive).
	 */
	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
		return MathHelper.clamp_int(
				quantityDropped(p_149679_2_)
						+ p_149679_2_.nextInt(p_149679_1_ + 1), 1, 4);
	}
}

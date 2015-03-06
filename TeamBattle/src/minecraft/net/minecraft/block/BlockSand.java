package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockSand extends BlockFalling {
	private static IIcon field_149837_b;
	public static final String[] field_149838_a = new String[] { "default",
			"red" };
	private static IIcon field_149839_N;

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	@Override
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_2_ == 1 ? field_149839_N : field_149837_b;
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return p_149728_1_ == 1 ? MapColor.field_151664_l
				: MapColor.field_151658_d;
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_,
			List p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_149837_b = p_149651_1_.registerIcon("sand");
		field_149839_N = p_149651_1_.registerIcon("red_sand");
	}
}

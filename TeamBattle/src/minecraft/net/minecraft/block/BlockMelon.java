package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockMelon extends Block {
	private IIcon field_150201_a;

	protected BlockMelon() {
		super(Material.field_151572_C);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return p_149691_1_ != 1 && p_149691_1_ != 0 ? blockIcon
				: field_150201_a;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Items.melon;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 3 + p_149745_1_.nextInt(5);
	}

	/**
	 * Returns the usual quantity dropped by the block plus a bonus of 1 to 'i'
	 * (inclusive).
	 */
	@Override
	public int quantityDroppedWithBonus(int p_149679_1_, Random p_149679_2_) {
		int var3 = quantityDropped(p_149679_2_)
				+ p_149679_2_.nextInt(1 + p_149679_1_);

		if (var3 > 9) {
			var3 = 9;
		}

		return var3;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
		field_150201_a = p_149651_1_.registerIcon(getTextureName() + "_top");
	}
}

package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStoneBrick extends Block {
	public static final String[] field_150141_b = new String[] { null, "mossy",
			"cracked", "carved" };
	public static final String[] field_150142_a = new String[] { "default",
			"mossy", "cracked", "chiseled" };
	private IIcon[] field_150143_M;

	public BlockStoneBrick() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

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
		if (p_149691_2_ < 0 || p_149691_2_ >= field_150141_b.length) {
			p_149691_2_ = 0;
		}

		return field_150143_M[p_149691_2_];
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_,
			List p_149666_3_) {
		for (int var4 = 0; var4 < 4; ++var4) {
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, var4));
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150143_M = new IIcon[field_150141_b.length];

		for (int var2 = 0; var2 < field_150143_M.length; ++var2) {
			String var3 = getTextureName();

			if (field_150141_b[var2] != null) {
				var3 = var3 + "_" + field_150141_b[var2];
			}

			field_150143_M[var2] = p_149651_1_.registerIcon(var3);
		}
	}
}

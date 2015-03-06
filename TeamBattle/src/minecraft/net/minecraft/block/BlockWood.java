package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockWood extends Block {
	public static final String[] field_150096_a = new String[] { "oak",
			"spruce", "birch", "jungle", "acacia", "big_oak" };
	private IIcon[] field_150095_b;

	public BlockWood() {
		super(Material.wood);
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
		if (p_149691_2_ < 0 || p_149691_2_ >= field_150095_b.length) {
			p_149691_2_ = 0;
		}

		return field_150095_b[p_149691_2_];
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_,
			List p_149666_3_) {
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 0));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 1));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 2));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 3));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 4));
		p_149666_3_.add(new ItemStack(p_149666_1_, 1, 5));
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150095_b = new IIcon[field_150096_a.length];

		for (int var2 = 0; var2 < field_150095_b.length; ++var2) {
			field_150095_b[var2] = p_149651_1_.registerIcon(getTextureName()
					+ "_" + field_150096_a[var2]);
		}
	}
}

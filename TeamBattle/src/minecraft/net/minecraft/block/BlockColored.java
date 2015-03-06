package net.minecraft.block;

import java.util.List;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockColored extends Block {
	public static int func_150031_c(int p_150031_0_) {
		return ~p_150031_0_ & 15;
	}

	public static int func_150032_b(int p_150032_0_) {
		return func_150031_c(p_150032_0_);
	}

	private IIcon[] field_150033_a;

	public BlockColored(Material p_i45398_1_) {
		super(p_i45398_1_);
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
		return field_150033_a[p_149691_2_ % field_150033_a.length];
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return MapColor.func_151644_a(p_149728_1_);
	}

	@Override
	public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_,
			List p_149666_3_) {
		for (int var4 = 0; var4 < 16; ++var4) {
			p_149666_3_.add(new ItemStack(p_149666_1_, 1, var4));
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		field_150033_a = new IIcon[16];

		for (int var2 = 0; var2 < field_150033_a.length; ++var2) {
			field_150033_a[var2] = p_149651_1_.registerIcon(getTextureName()
					+ "_" + ItemDye.field_150921_b[func_150031_c(var2)]);
		}
	}
}

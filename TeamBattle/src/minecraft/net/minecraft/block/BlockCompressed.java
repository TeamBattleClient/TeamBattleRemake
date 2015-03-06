package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCompressed extends Block {
	private final MapColor field_150202_a;

	public BlockCompressed(MapColor p_i45414_1_) {
		super(Material.iron);
		field_150202_a = p_i45414_1_;
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return field_150202_a;
	}
}

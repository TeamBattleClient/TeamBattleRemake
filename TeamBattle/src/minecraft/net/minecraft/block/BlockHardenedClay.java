package net.minecraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockHardenedClay extends Block {

	public BlockHardenedClay() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return MapColor.field_151676_q;
	}
}

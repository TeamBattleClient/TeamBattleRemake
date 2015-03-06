package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockPackedIce extends Block {

	public BlockPackedIce() {
		super(Material.field_151598_x);
		slipperiness = 0.98F;
		setCreativeTab(CreativeTabs.tabBlock);
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}
}

package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockGlass extends BlockBreakable {

	public BlockGlass(Material p_i45408_1_, boolean p_i45408_2_) {
		super("glass", p_i45408_1_, p_i45408_2_);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1
	 * for alpha
	 */
	@Override
	public int getRenderBlockPass() {
		return 0;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}

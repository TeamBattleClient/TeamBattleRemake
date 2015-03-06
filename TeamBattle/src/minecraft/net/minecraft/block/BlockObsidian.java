package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class BlockObsidian extends BlockStone {

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_,
			int p_149650_3_) {
		return Item.getItemFromBlock(Blocks.obsidian);
	}

	@Override
	public MapColor getMapColor(int p_149728_1_) {
		return MapColor.field_151654_J;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 1;
	}
}

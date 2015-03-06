package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock {

	public ItemPiston(Block p_i45348_1_) {
		super(p_i45348_1_);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int p_77647_1_) {
		return 7;
	}
}

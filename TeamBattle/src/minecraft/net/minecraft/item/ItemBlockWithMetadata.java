package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemBlockWithMetadata extends ItemBlock {
	private final Block field_150950_b;

	public ItemBlockWithMetadata(Block p_i45326_1_, Block p_i45326_2_) {
		super(p_i45326_1_);
		field_150950_b = p_i45326_2_;
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150950_b.getIcon(2, p_77617_1_);
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}
}

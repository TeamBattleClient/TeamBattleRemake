package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.util.IIcon;

public class ItemCloth extends ItemBlock {

	public ItemCloth(Block p_i45358_1_) {
		super(p_i45358_1_);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150939_a.func_149735_b(2,
				BlockColored.func_150032_b(p_77617_1_));
	}

	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int p_77647_1_) {
		return p_77647_1_;
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return super.getUnlocalizedName()
				+ "."
				+ ItemDye.field_150923_a[BlockColored.func_150032_b(p_77667_1_
						.getItemDamage())];
	}
}

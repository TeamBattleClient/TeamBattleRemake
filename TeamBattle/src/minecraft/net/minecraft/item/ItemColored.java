package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class ItemColored extends ItemBlock {
	private final Block field_150944_b;
	private String[] field_150945_c;

	public ItemColored(Block p_i45332_1_, boolean p_i45332_2_) {
		super(p_i45332_1_);
		field_150944_b = p_i45332_1_;

		if (p_i45332_2_) {
			setMaxDamage(0);
			setHasSubtypes(true);
		}
	}

	public ItemColored func_150943_a(String[] p_150943_1_) {
		field_150945_c = p_150943_1_;
		return this;
	}

	@Override
	public int getColorFromItemStack(ItemStack p_82790_1_, int p_82790_2_) {
		return field_150944_b.getRenderColor(p_82790_1_.getItemDamage());
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@Override
	public IIcon getIconFromDamage(int p_77617_1_) {
		return field_150944_b.getIcon(0, p_77617_1_);
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
		if (field_150945_c == null)
			return super.getUnlocalizedName(p_77667_1_);
		else {
			final int var2 = p_77667_1_.getItemDamage();
			return var2 >= 0 && var2 < field_150945_c.length ? super
					.getUnlocalizedName(p_77667_1_)
					+ "."
					+ field_150945_c[var2] : super
					.getUnlocalizedName(p_77667_1_);
		}
	}
}

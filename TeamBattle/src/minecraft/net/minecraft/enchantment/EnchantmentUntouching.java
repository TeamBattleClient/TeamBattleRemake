package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EnchantmentUntouching extends Enchantment {

	protected EnchantmentUntouching(int p_i1938_1_, int p_i1938_2_) {
		super(p_i1938_1_, p_i1938_2_, EnumEnchantmentType.digger);
		setName("untouching");
	}

	@Override
	public boolean canApply(ItemStack p_92089_1_) {
		return p_92089_1_.getItem() == Items.shears ? true : super
				.canApply(p_92089_1_);
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this
	 * enchantment.
	 */
	@Override
	public boolean canApplyTogether(Enchantment p_77326_1_) {
		return super.canApplyTogether(p_77326_1_)
				&& p_77326_1_.effectId != fortune.effectId;
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return super.getMinEnchantability(p_77317_1_) + 50;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel() {
		return 1;
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 15;
	}
}

package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EnchantmentDigging extends Enchantment {

	protected EnchantmentDigging(int p_i1925_1_, int p_i1925_2_) {
		super(p_i1925_1_, p_i1925_2_, EnumEnchantmentType.digger);
		setName("digging");
	}

	@Override
	public boolean canApply(ItemStack p_92089_1_) {
		return p_92089_1_.getItem() == Items.shears ? true : super
				.canApply(p_92089_1_);
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
		return 5;
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 1 + 10 * (p_77321_1_ - 1);
	}
}

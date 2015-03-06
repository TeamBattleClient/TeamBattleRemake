package net.minecraft.enchantment;

public class EnchantmentArrowKnockback extends Enchantment {

	public EnchantmentArrowKnockback(int p_i1922_1_, int p_i1922_2_) {
		super(p_i1922_1_, p_i1922_2_, EnumEnchantmentType.bow);
		setName("arrowKnockback");
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + 25;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel() {
		return 2;
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 12 + (p_77321_1_ - 1) * 20;
	}
}

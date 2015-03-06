package net.minecraft.enchantment;

public class EnchantmentArrowDamage extends Enchantment {

	public EnchantmentArrowDamage(int p_i1919_1_, int p_i1919_2_) {
		super(p_i1919_1_, p_i1919_2_, EnumEnchantmentType.bow);
		setName("arrowDamage");
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + 15;
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
		return 1 + (p_77321_1_ - 1) * 10;
	}
}

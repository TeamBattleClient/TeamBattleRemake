package net.minecraft.enchantment;

public class EnchantmentArrowInfinite extends Enchantment {

	public EnchantmentArrowInfinite(int p_i1921_1_, int p_i1921_2_) {
		super(p_i1921_1_, p_i1921_2_, EnumEnchantmentType.bow);
		setName("arrowInfinite");
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return 50;
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
		return 20;
	}
}

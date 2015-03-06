package net.minecraft.enchantment;

public class EnchantmentOxygen extends Enchantment {

	public EnchantmentOxygen(int p_i1935_1_, int p_i1935_2_) {
		super(p_i1935_1_, p_i1935_2_, EnumEnchantmentType.armor_head);
		setName("oxygen");
	}

	/**
	 * Returns the maximum value of enchantability nedded on the enchantment
	 * level passed.
	 */
	@Override
	public int getMaxEnchantability(int p_77317_1_) {
		return getMinEnchantability(p_77317_1_) + 30;
	}

	/**
	 * Returns the maximum level that the enchantment can have.
	 */
	@Override
	public int getMaxLevel() {
		return 3;
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 10 * p_77321_1_;
	}
}

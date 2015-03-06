package net.minecraft.enchantment;

import net.minecraft.util.WeightedRandom;

public class EnchantmentData extends WeightedRandom.Item {
	/** Enchantment level associated with this EnchantmentData */
	public final int enchantmentLevel;

	/** Enchantment object associated with this EnchantmentData */
	public final Enchantment enchantmentobj;

	public EnchantmentData(Enchantment p_i1930_1_, int p_i1930_2_) {
		super(p_i1930_1_.getWeight());
		enchantmentobj = p_i1930_1_;
		enchantmentLevel = p_i1930_2_;
	}

	public EnchantmentData(int p_i1931_1_, int p_i1931_2_) {
		this(Enchantment.enchantmentsList[p_i1931_1_], p_i1931_2_);
	}
}

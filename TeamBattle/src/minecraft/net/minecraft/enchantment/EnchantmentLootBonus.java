package net.minecraft.enchantment;

public class EnchantmentLootBonus extends Enchantment {

	protected EnchantmentLootBonus(int p_i1934_1_, int p_i1934_2_,
			EnumEnchantmentType p_i1934_3_) {
		super(p_i1934_1_, p_i1934_2_, p_i1934_3_);

		if (p_i1934_3_ == EnumEnchantmentType.digger) {
			setName("lootBonusDigger");
		} else if (p_i1934_3_ == EnumEnchantmentType.fishing_rod) {
			setName("lootBonusFishing");
		} else {
			setName("lootBonus");
		}
	}

	/**
	 * Determines if the enchantment passed can be applyied together with this
	 * enchantment.
	 */
	@Override
	public boolean canApplyTogether(Enchantment p_77326_1_) {
		return super.canApplyTogether(p_77326_1_)
				&& p_77326_1_.effectId != silkTouch.effectId;
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
		return 3;
	}

	/**
	 * Returns the minimal value of enchantability needed on the enchantment
	 * level passed.
	 */
	@Override
	public int getMinEnchantability(int p_77321_1_) {
		return 15 + (p_77321_1_ - 1) * 9;
	}
}

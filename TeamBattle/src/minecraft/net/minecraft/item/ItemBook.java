package net.minecraft.item;

public class ItemBook extends Item {

	/**
	 * Return the enchantability factor of the item, most of the time is based
	 * on material.
	 */
	@Override
	public int getItemEnchantability() {
		return 1;
	}

	/**
	 * Checks isDamagable and if it cannot be stacked
	 */
	@Override
	public boolean isItemTool(ItemStack p_77616_1_) {
		return p_77616_1_.stackSize == 1;
	}
}

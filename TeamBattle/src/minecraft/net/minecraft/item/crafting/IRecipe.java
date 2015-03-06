package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRecipe {
	/**
	 * Returns an Item that is the result of this recipe
	 */
	ItemStack getCraftingResult(InventoryCrafting p_77572_1_);

	ItemStack getRecipeOutput();

	/**
	 * Returns the size of the recipe area
	 */
	int getRecipeSize();

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_);
}

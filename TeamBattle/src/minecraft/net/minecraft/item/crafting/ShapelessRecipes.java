package net.minecraft.item.crafting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShapelessRecipes implements IRecipe {
	/** Is a List of ItemStack that composes the recipe. */
	private final List recipeItems;

	/** Is the ItemStack that you get when craft the recipe. */
	private final ItemStack recipeOutput;

	public ShapelessRecipes(ItemStack p_i1918_1_, List p_i1918_2_) {
		recipeOutput = p_i1918_1_;
		recipeItems = p_i1918_2_;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		return recipeOutput.copy();
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}

	/**
	 * Returns the size of the recipe area
	 */
	@Override
	public int getRecipeSize() {
		return recipeItems.size();
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		final ArrayList var3 = new ArrayList(recipeItems);

		for (int var4 = 0; var4 < 3; ++var4) {
			for (int var5 = 0; var5 < 3; ++var5) {
				final ItemStack var6 = p_77569_1_.getStackInRowAndColumn(var5,
						var4);

				if (var6 != null) {
					boolean var7 = false;
					final Iterator var8 = var3.iterator();

					while (var8.hasNext()) {
						final ItemStack var9 = (ItemStack) var8.next();

						if (var6.getItem() == var9.getItem()
								&& (var9.getItemDamage() == 32767 || var6
										.getItemDamage() == var9
										.getItemDamage())) {
							var7 = true;
							var3.remove(var9);
							break;
						}
					}

					if (!var7)
						return false;
				}
			}
		}

		return var3.isEmpty();
	}
}

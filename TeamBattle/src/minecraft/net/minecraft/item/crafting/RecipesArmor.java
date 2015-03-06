package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesArmor {
	private final Object[][] recipeItems;
	private final String[][] recipePatterns = new String[][] {
			{ "XXX", "X X" }, { "X X", "XXX", "XXX" }, { "XXX", "X X", "X X" },
			{ "X X", "X X" } };

	public RecipesArmor() {
		recipeItems = new Object[][] {
				{ Items.leather, Blocks.fire, Items.iron_ingot, Items.diamond,
						Items.gold_ingot },
				{ Items.leather_helmet, Items.chainmail_helmet,
						Items.iron_helmet, Items.diamond_helmet,
						Items.golden_helmet },
				{ Items.leather_chestplate, Items.chainmail_chestplate,
						Items.iron_chestplate, Items.diamond_chestplate,
						Items.golden_chestplate },
				{ Items.leather_leggings, Items.chainmail_leggings,
						Items.iron_leggings, Items.diamond_leggings,
						Items.golden_leggings },
				{ Items.leather_boots, Items.chainmail_boots, Items.iron_boots,
						Items.diamond_boots, Items.golden_boots } };
	}

	/**
	 * Adds the armor recipes to the CraftingManager.
	 */
	public void addRecipes(CraftingManager p_77609_1_) {
		for (int var2 = 0; var2 < recipeItems[0].length; ++var2) {
			final Object var3 = recipeItems[0][var2];

			for (int var4 = 0; var4 < recipeItems.length - 1; ++var4) {
				final Item var5 = (Item) recipeItems[var4 + 1][var2];
				p_77609_1_.addRecipe(new ItemStack(var5), new Object[] {
						recipePatterns[var4], 'X', var3 });
			}
		}
	}
}

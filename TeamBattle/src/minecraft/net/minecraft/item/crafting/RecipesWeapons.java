package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesWeapons {
	private final Object[][] recipeItems;
	private final String[][] recipePatterns = new String[][] { { "X", "X", "#" } };

	public RecipesWeapons() {
		recipeItems = new Object[][] {
				{ Blocks.planks, Blocks.cobblestone, Items.iron_ingot,
						Items.diamond, Items.gold_ingot },
				{ Items.wooden_sword, Items.stone_sword, Items.iron_sword,
						Items.diamond_sword, Items.golden_sword } };
	}

	/**
	 * Adds the weapon recipes to the CraftingManager.
	 */
	public void addRecipes(CraftingManager p_77583_1_) {
		for (int var2 = 0; var2 < recipeItems[0].length; ++var2) {
			final Object var3 = recipeItems[0][var2];

			for (int var4 = 0; var4 < recipeItems.length - 1; ++var4) {
				final Item var5 = (Item) recipeItems[var4 + 1][var2];
				p_77583_1_.addRecipe(new ItemStack(var5), new Object[] {
						recipePatterns[var4], '#', Items.stick, 'X', var3 });
			}
		}

		p_77583_1_.addRecipe(new ItemStack(Items.bow, 1), new Object[] { " #X",
				"# X", " #X", 'X', Items.string, '#', Items.stick });
		p_77583_1_.addRecipe(new ItemStack(Items.arrow, 4), new Object[] { "X",
				"#", "Y", 'Y', Items.feather, 'X', Items.flint, '#',
				Items.stick });
	}
}

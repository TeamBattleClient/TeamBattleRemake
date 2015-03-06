package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ShapedRecipes implements IRecipe {
	private boolean field_92101_f;

	/** How many vertical slots this recipe uses. */
	private final int recipeHeight;

	/** Is a array of ItemStack that composes the recipe. */
	private final ItemStack[] recipeItems;

	/** Is the ItemStack that you get when craft the recipe. */
	private final ItemStack recipeOutput;
	/** How many horizontal slots this recipe is wide. */
	private final int recipeWidth;

	public ShapedRecipes(int p_i1917_1_, int p_i1917_2_,
			ItemStack[] p_i1917_3_, ItemStack p_i1917_4_) {
		recipeWidth = p_i1917_1_;
		recipeHeight = p_i1917_2_;
		recipeItems = p_i1917_3_;
		recipeOutput = p_i1917_4_;
	}

	/**
	 * Checks if the region of a crafting inventory is match for the recipe.
	 */
	private boolean checkMatch(InventoryCrafting p_77573_1_, int p_77573_2_,
			int p_77573_3_, boolean p_77573_4_) {
		for (int var5 = 0; var5 < 3; ++var5) {
			for (int var6 = 0; var6 < 3; ++var6) {
				final int var7 = var5 - p_77573_2_;
				final int var8 = var6 - p_77573_3_;
				ItemStack var9 = null;

				if (var7 >= 0 && var8 >= 0 && var7 < recipeWidth
						&& var8 < recipeHeight) {
					if (p_77573_4_) {
						var9 = recipeItems[recipeWidth - var7 - 1 + var8
								* recipeWidth];
					} else {
						var9 = recipeItems[var7 + var8 * recipeWidth];
					}
				}

				final ItemStack var10 = p_77573_1_.getStackInRowAndColumn(var5,
						var6);

				if (var10 != null || var9 != null) {
					if (var10 == null && var9 != null || var10 != null
							&& var9 == null)
						return false;

					if (var9.getItem() != var10.getItem())
						return false;

					if (var9.getItemDamage() != 32767
							&& var9.getItemDamage() != var10.getItemDamage())
						return false;
				}
			}
		}

		return true;
	}

	public ShapedRecipes func_92100_c() {
		field_92101_f = true;
		return this;
	}

	/**
	 * Returns an Item that is the result of this recipe
	 */
	@Override
	public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
		final ItemStack var2 = getRecipeOutput().copy();

		if (field_92101_f) {
			for (int var3 = 0; var3 < p_77572_1_.getSizeInventory(); ++var3) {
				final ItemStack var4 = p_77572_1_.getStackInSlot(var3);

				if (var4 != null && var4.hasTagCompound()) {
					var2.setTagCompound((NBTTagCompound) var4.stackTagCompound
							.copy());
				}
			}
		}

		return var2;
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
		return recipeWidth * recipeHeight;
	}

	/**
	 * Used to check if a recipe matches current crafting inventory
	 */
	@Override
	public boolean matches(InventoryCrafting p_77569_1_, World p_77569_2_) {
		for (int var3 = 0; var3 <= 3 - recipeWidth; ++var3) {
			for (int var4 = 0; var4 <= 3 - recipeHeight; ++var4) {
				if (checkMatch(p_77569_1_, var3, var4, true))
					return true;

				if (checkMatch(p_77569_1_, var3, var4, false))
					return true;
			}
		}

		return false;
	}
}

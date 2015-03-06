package net.minecraft.util;

import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;

public class WeightedRandomChestContent extends WeightedRandom.Item {
	public static void func_150706_a(Random p_150706_0_,
			WeightedRandomChestContent[] p_150706_1_,
			TileEntityDispenser p_150706_2_, int p_150706_3_) {
		for (int var4 = 0; var4 < p_150706_3_; ++var4) {
			final WeightedRandomChestContent var5 = (WeightedRandomChestContent) WeightedRandom
					.getRandomItem(p_150706_0_, p_150706_1_);
			final int var6 = var5.theMinimumChanceToGenerateItem
					+ p_150706_0_.nextInt(var5.theMaximumChanceToGenerateItem
							- var5.theMinimumChanceToGenerateItem + 1);

			if (var5.theItemId.getMaxStackSize() >= var6) {
				final ItemStack var7 = var5.theItemId.copy();
				var7.stackSize = var6;
				p_150706_2_.setInventorySlotContents(
						p_150706_0_.nextInt(p_150706_2_.getSizeInventory()),
						var7);
			} else {
				for (int var9 = 0; var9 < var6; ++var9) {
					final ItemStack var8 = var5.theItemId.copy();
					var8.stackSize = 1;
					p_150706_2_
							.setInventorySlotContents(p_150706_0_
									.nextInt(p_150706_2_.getSizeInventory()),
									var8);
				}
			}
		}
	}

	public static WeightedRandomChestContent[] func_92080_a(
			WeightedRandomChestContent[] p_92080_0_,
			WeightedRandomChestContent... p_92080_1_) {
		final WeightedRandomChestContent[] var2 = new WeightedRandomChestContent[p_92080_0_.length
				+ p_92080_1_.length];
		int var3 = 0;

		for (final WeightedRandomChestContent element : p_92080_0_) {
			var2[var3++] = element;
		}

		final WeightedRandomChestContent[] var8 = p_92080_1_;
		final int var5 = p_92080_1_.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			final WeightedRandomChestContent var7 = var8[var6];
			var2[var3++] = var7;
		}

		return var2;
	}

	/**
	 * Generates the Chest contents.
	 */
	public static void generateChestContents(Random p_76293_0_,
			WeightedRandomChestContent[] p_76293_1_, IInventory p_76293_2_,
			int p_76293_3_) {
		for (int var4 = 0; var4 < p_76293_3_; ++var4) {
			final WeightedRandomChestContent var5 = (WeightedRandomChestContent) WeightedRandom
					.getRandomItem(p_76293_0_, p_76293_1_);
			final int var6 = var5.theMinimumChanceToGenerateItem
					+ p_76293_0_.nextInt(var5.theMaximumChanceToGenerateItem
							- var5.theMinimumChanceToGenerateItem + 1);

			if (var5.theItemId.getMaxStackSize() >= var6) {
				final ItemStack var7 = var5.theItemId.copy();
				var7.stackSize = var6;
				p_76293_2_
						.setInventorySlotContents(p_76293_0_.nextInt(p_76293_2_
								.getSizeInventory()), var7);
			} else {
				for (int var9 = 0; var9 < var6; ++var9) {
					final ItemStack var8 = var5.theItemId.copy();
					var8.stackSize = 1;
					p_76293_2_.setInventorySlotContents(
							p_76293_0_.nextInt(p_76293_2_.getSizeInventory()),
							var8);
				}
			}
		}
	}

	/** The Item/Block ID to generate in the Chest. */
	private final ItemStack theItemId;

	/** The maximum chance of item generating. */
	private final int theMaximumChanceToGenerateItem;

	/** The minimum chance of item generating. */
	private final int theMinimumChanceToGenerateItem;

	public WeightedRandomChestContent(Item p_i45311_1_, int p_i45311_2_,
			int p_i45311_3_, int p_i45311_4_, int p_i45311_5_) {
		super(p_i45311_5_);
		theItemId = new ItemStack(p_i45311_1_, 1, p_i45311_2_);
		theMinimumChanceToGenerateItem = p_i45311_3_;
		theMaximumChanceToGenerateItem = p_i45311_4_;
	}

	public WeightedRandomChestContent(ItemStack p_i1558_1_, int p_i1558_2_,
			int p_i1558_3_, int p_i1558_4_) {
		super(p_i1558_4_);
		theItemId = p_i1558_1_;
		theMinimumChanceToGenerateItem = p_i1558_2_;
		theMaximumChanceToGenerateItem = p_i1558_3_;
	}
}

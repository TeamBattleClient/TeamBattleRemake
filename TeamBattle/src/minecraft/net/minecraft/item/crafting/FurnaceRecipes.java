package net.minecraft.item.crafting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;

public class FurnaceRecipes {
	private static final FurnaceRecipes smeltingBase = new FurnaceRecipes();

	/**
	 * Used to call methods addSmelting and getSmeltingResult.
	 */
	public static FurnaceRecipes smelting() {
		return smeltingBase;
	}

	private final Map experienceList = new HashMap();

	/** The list of smelting results. */
	private final Map smeltingList = new HashMap();

	private FurnaceRecipes() {
		func_151393_a(Blocks.iron_ore, new ItemStack(Items.iron_ingot), 0.7F);
		func_151393_a(Blocks.gold_ore, new ItemStack(Items.gold_ingot), 1.0F);
		func_151393_a(Blocks.diamond_ore, new ItemStack(Items.diamond), 1.0F);
		func_151393_a(Blocks.sand, new ItemStack(Blocks.glass), 0.1F);
		func_151396_a(Items.porkchop, new ItemStack(Items.cooked_porkchop),
				0.35F);
		func_151396_a(Items.beef, new ItemStack(Items.cooked_beef), 0.35F);
		func_151396_a(Items.chicken, new ItemStack(Items.cooked_chicken), 0.35F);
		func_151393_a(Blocks.cobblestone, new ItemStack(Blocks.stone), 0.1F);
		func_151396_a(Items.clay_ball, new ItemStack(Items.brick), 0.3F);
		func_151393_a(Blocks.clay, new ItemStack(Blocks.hardened_clay), 0.35F);
		func_151393_a(Blocks.cactus, new ItemStack(Items.dye, 1, 2), 0.2F);
		func_151393_a(Blocks.log, new ItemStack(Items.coal, 1, 1), 0.15F);
		func_151393_a(Blocks.log2, new ItemStack(Items.coal, 1, 1), 0.15F);
		func_151393_a(Blocks.emerald_ore, new ItemStack(Items.emerald), 1.0F);
		func_151396_a(Items.potato, new ItemStack(Items.baked_potato), 0.35F);
		func_151393_a(Blocks.netherrack, new ItemStack(Items.netherbrick), 0.1F);
		final ItemFishFood.FishType[] var1 = ItemFishFood.FishType.values();
		final int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			final ItemFishFood.FishType var4 = var1[var3];

			if (var4.func_150973_i()) {
				func_151394_a(
						new ItemStack(Items.fish, 1, var4.func_150976_a()),
						new ItemStack(Items.cooked_fished, 1, var4
								.func_150976_a()), 0.35F);
			}
		}

		func_151393_a(Blocks.coal_ore, new ItemStack(Items.coal), 0.1F);
		func_151393_a(Blocks.redstone_ore, new ItemStack(Items.redstone), 0.7F);
		func_151393_a(Blocks.lapis_ore, new ItemStack(Items.dye, 1, 4), 0.2F);
		func_151393_a(Blocks.quartz_ore, new ItemStack(Items.quartz), 0.2F);
	}

	public void func_151393_a(Block p_151393_1_, ItemStack p_151393_2_,
			float p_151393_3_) {
		func_151396_a(Item.getItemFromBlock(p_151393_1_), p_151393_2_,
				p_151393_3_);
	}

	public void func_151394_a(ItemStack p_151394_1_, ItemStack p_151394_2_,
			float p_151394_3_) {
		smeltingList.put(p_151394_1_, p_151394_2_);
		experienceList.put(p_151394_2_, Float.valueOf(p_151394_3_));
	}

	public ItemStack func_151395_a(ItemStack p_151395_1_) {
		final Iterator var2 = smeltingList.entrySet().iterator();
		Entry var3;

		do {
			if (!var2.hasNext())
				return null;

			var3 = (Entry) var2.next();
		} while (!func_151397_a(p_151395_1_, (ItemStack) var3.getKey()));

		return (ItemStack) var3.getValue();
	}

	public void func_151396_a(Item p_151396_1_, ItemStack p_151396_2_,
			float p_151396_3_) {
		func_151394_a(new ItemStack(p_151396_1_, 1, 32767), p_151396_2_,
				p_151396_3_);
	}

	private boolean func_151397_a(ItemStack p_151397_1_, ItemStack p_151397_2_) {
		return p_151397_2_.getItem() == p_151397_1_.getItem()
				&& (p_151397_2_.getItemDamage() == 32767 || p_151397_2_
						.getItemDamage() == p_151397_1_.getItemDamage());
	}

	public float func_151398_b(ItemStack p_151398_1_) {
		final Iterator var2 = experienceList.entrySet().iterator();
		Entry var3;

		do {
			if (!var2.hasNext())
				return 0.0F;

			var3 = (Entry) var2.next();
		} while (!func_151397_a(p_151398_1_, (ItemStack) var3.getKey()));

		return ((Float) var3.getValue()).floatValue();
	}

	public Map getSmeltingList() {
		return smeltingList;
	}
}

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.stats.AchievementList;

public class SlotCrafting extends Slot {
	/**
	 * The number of items that have been crafted so far. Gets passed to
	 * ItemStack.onCrafting before being reset.
	 */
	private int amountCrafted;

	/** The craft matrix inventory linked to this result slot. */
	private final IInventory craftMatrix;

	/** The player that is using the GUI where this slot resides. */
	private final EntityPlayer thePlayer;

	public SlotCrafting(EntityPlayer p_i1823_1_, IInventory p_i1823_2_,
			IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_,
			int p_i1823_6_) {
		super(p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
		thePlayer = p_i1823_1_;
		craftMatrix = p_i1823_2_;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_75209_1_) {
		if (getHasStack()) {
			amountCrafted += Math.min(p_75209_1_, getStack().stackSize);
		}

		return super.decrStackSize(p_75209_1_);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood.
	 */
	@Override
	protected void onCrafting(ItemStack p_75208_1_) {
		p_75208_1_.onCrafting(thePlayer.worldObj, thePlayer, amountCrafted);
		amountCrafted = 0;

		if (p_75208_1_.getItem() == Item
				.getItemFromBlock(Blocks.crafting_table)) {
			thePlayer.addStat(AchievementList.buildWorkBench, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemPickaxe) {
			thePlayer.addStat(AchievementList.buildPickaxe, 1);
		}

		if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
			thePlayer.addStat(AchievementList.buildFurnace, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemHoe) {
			thePlayer.addStat(AchievementList.buildHoe, 1);
		}

		if (p_75208_1_.getItem() == Items.bread) {
			thePlayer.addStat(AchievementList.makeBread, 1);
		}

		if (p_75208_1_.getItem() == Items.cake) {
			thePlayer.addStat(AchievementList.bakeCake, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemPickaxe
				&& ((ItemPickaxe) p_75208_1_.getItem()).func_150913_i() != Item.ToolMaterial.WOOD) {
			thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		}

		if (p_75208_1_.getItem() instanceof ItemSword) {
			thePlayer.addStat(AchievementList.buildSword, 1);
		}

		if (p_75208_1_.getItem() == Item
				.getItemFromBlock(Blocks.enchanting_table)) {
			thePlayer.addStat(AchievementList.enchantments, 1);
		}

		if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
			thePlayer.addStat(AchievementList.bookcase, 1);
		}
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		amountCrafted += p_75210_2_;
		this.onCrafting(p_75210_1_);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		this.onCrafting(p_82870_2_);

		for (int var3 = 0; var3 < craftMatrix.getSizeInventory(); ++var3) {
			final ItemStack var4 = craftMatrix.getStackInSlot(var3);

			if (var4 != null) {
				craftMatrix.decrStackSize(var3, 1);

				if (var4.getItem().hasContainerItem()) {
					final ItemStack var5 = new ItemStack(var4.getItem()
							.getContainerItem());

					if (!var4.getItem()
							.doesContainerItemLeaveCraftingGrid(var4)
							|| !thePlayer.inventory
									.addItemStackToInventory(var5)) {
						if (craftMatrix.getStackInSlot(var3) == null) {
							craftMatrix.setInventorySlotContents(var3, var5);
						} else {
							thePlayer.dropPlayerItemWithRandomChoice(var5,
									false);
						}
					}
				}
			}
		}
	}
}

package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class InventoryMerchant implements IInventory {
	private MerchantRecipe currentRecipe;
	private int currentRecipeIndex;
	private final ItemStack[] theInventory = new ItemStack[3];
	private final IMerchant theMerchant;
	private final EntityPlayer thePlayer;

	public InventoryMerchant(EntityPlayer p_i1820_1_, IMerchant p_i1820_2_) {
		thePlayer = p_i1820_1_;
		theMerchant = p_i1820_2_;
	}

	@Override
	public void closeInventory() {
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (theInventory[p_70298_1_] != null) {
			ItemStack var3;

			if (p_70298_1_ == 2) {
				var3 = theInventory[p_70298_1_];
				theInventory[p_70298_1_] = null;
				return var3;
			} else if (theInventory[p_70298_1_].stackSize <= p_70298_2_) {
				var3 = theInventory[p_70298_1_];
				theInventory[p_70298_1_] = null;

				if (inventoryResetNeededOnSlotChange(p_70298_1_)) {
					resetRecipeAndSlots();
				}

				return var3;
			} else {
				var3 = theInventory[p_70298_1_].splitStack(p_70298_2_);

				if (theInventory[p_70298_1_].stackSize == 0) {
					theInventory[p_70298_1_] = null;
				}

				if (inventoryResetNeededOnSlotChange(p_70298_1_)) {
					resetRecipeAndSlots();
				}

				return var3;
			}
		} else
			return null;
	}

	public MerchantRecipe getCurrentRecipe() {
		return currentRecipe;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return "mob.villager";
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return theInventory.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return theInventory[p_70301_1_];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (theInventory[p_70304_1_] != null) {
			final ItemStack var2 = theInventory[p_70304_1_];
			theInventory[p_70304_1_] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * if par1 slot has changed, does resetRecipeAndSlots need to be called?
	 */
	private boolean inventoryResetNeededOnSlotChange(int p_70469_1_) {
		return p_70469_1_ == 0 || p_70469_1_ == 1;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return false;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return true;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return theMerchant.getCustomer() == p_70300_1_;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	@Override
	public void onInventoryChanged() {
		resetRecipeAndSlots();
	}

	@Override
	public void openInventory() {
	}

	public void resetRecipeAndSlots() {
		currentRecipe = null;
		ItemStack var1 = theInventory[0];
		ItemStack var2 = theInventory[1];

		if (var1 == null) {
			var1 = var2;
			var2 = null;
		}

		if (var1 == null) {
			setInventorySlotContents(2, (ItemStack) null);
		} else {
			final MerchantRecipeList var3 = theMerchant.getRecipes(thePlayer);

			if (var3 != null) {
				MerchantRecipe var4 = var3.canRecipeBeUsed(var1, var2,
						currentRecipeIndex);

				if (var4 != null && !var4.isRecipeDisabled()) {
					currentRecipe = var4;
					setInventorySlotContents(2, var4.getItemToSell().copy());
				} else if (var2 != null) {
					var4 = var3.canRecipeBeUsed(var2, var1, currentRecipeIndex);

					if (var4 != null && !var4.isRecipeDisabled()) {
						currentRecipe = var4;
						setInventorySlotContents(2, var4.getItemToSell().copy());
					} else {
						setInventorySlotContents(2, (ItemStack) null);
					}
				} else {
					setInventorySlotContents(2, (ItemStack) null);
				}
			}
		}

		theMerchant.func_110297_a_(getStackInSlot(2));
	}

	public void setCurrentRecipeIndex(int p_70471_1_) {
		currentRecipeIndex = p_70471_1_;
		resetRecipeAndSlots();
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		theInventory[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null
				&& p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}

		if (inventoryResetNeededOnSlotChange(p_70299_1_)) {
			resetRecipeAndSlots();
		}
	}
}

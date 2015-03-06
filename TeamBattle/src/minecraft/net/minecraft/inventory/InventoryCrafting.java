package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryCrafting implements IInventory {
	/**
	 * Class containing the callbacks for the events on_GUIClosed and
	 * on_CraftMaxtrixChanged.
	 */
	private final Container eventHandler;

	/** the width of the crafting inventory */
	private final int inventoryWidth;

	/** List of the stacks in the crafting matrix. */
	private final ItemStack[] stackList;

	public InventoryCrafting(Container p_i1807_1_, int p_i1807_2_,
			int p_i1807_3_) {
		final int var4 = p_i1807_2_ * p_i1807_3_;
		stackList = new ItemStack[var4];
		eventHandler = p_i1807_1_;
		inventoryWidth = p_i1807_2_;
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
		if (stackList[p_70298_1_] != null) {
			ItemStack var3;

			if (stackList[p_70298_1_].stackSize <= p_70298_2_) {
				var3 = stackList[p_70298_1_];
				stackList[p_70298_1_] = null;
				eventHandler.onCraftMatrixChanged(this);
				return var3;
			} else {
				var3 = stackList[p_70298_1_].splitStack(p_70298_2_);

				if (stackList[p_70298_1_].stackSize == 0) {
					stackList[p_70298_1_] = null;
				}

				eventHandler.onCraftMatrixChanged(this);
				return var3;
			}
		} else
			return null;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return "container.crafting";
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
		return stackList.length;
	}

	/**
	 * Returns the itemstack in the slot specified (Top left is 0, 0). Args:
	 * row, column
	 */
	public ItemStack getStackInRowAndColumn(int p_70463_1_, int p_70463_2_) {
		if (p_70463_1_ >= 0 && p_70463_1_ < inventoryWidth) {
			final int var3 = p_70463_1_ + p_70463_2_ * inventoryWidth;
			return getStackInSlot(var3);
		} else
			return null;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= getSizeInventory() ? null : stackList[p_70301_1_];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (stackList[p_70304_1_] != null) {
			final ItemStack var2 = stackList[p_70304_1_];
			stackList[p_70304_1_] = null;
			return var2;
		} else
			return null;
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
		return true;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	@Override
	public void onInventoryChanged() {
	}

	@Override
	public void openInventory() {
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		stackList[p_70299_1_] = p_70299_2_;
		eventHandler.onCraftMatrixChanged(this);
	}
}

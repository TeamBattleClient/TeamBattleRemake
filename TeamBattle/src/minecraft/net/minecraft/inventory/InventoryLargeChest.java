package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryLargeChest implements IInventory {
	/** Inventory object corresponding to double chest lower part */
	private final IInventory lowerChest;

	/** Name of the chest. */
	private final String name;

	/** Inventory object corresponding to double chest upper part */
	private final IInventory upperChest;

	public InventoryLargeChest(String p_i1559_1_, IInventory p_i1559_2_,
			IInventory p_i1559_3_) {
		name = p_i1559_1_;

		if (p_i1559_2_ == null) {
			p_i1559_2_ = p_i1559_3_;
		}

		if (p_i1559_3_ == null) {
			p_i1559_3_ = p_i1559_2_;
		}

		upperChest = p_i1559_2_;
		lowerChest = p_i1559_3_;
	}

	@Override
	public void closeInventory() {
		upperChest.closeInventory();
		lowerChest.closeInventory();
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		return p_70298_1_ >= upperChest.getSizeInventory() ? lowerChest
				.decrStackSize(p_70298_1_ - upperChest.getSizeInventory(),
						p_70298_2_) : upperChest.decrStackSize(p_70298_1_,
				p_70298_2_);
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return upperChest.isInventoryNameLocalized() ? upperChest
				.getInventoryName()
				: lowerChest.isInventoryNameLocalized() ? lowerChest
						.getInventoryName() : name;
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return upperChest.getInventoryStackLimit();
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return upperChest.getSizeInventory() + lowerChest.getSizeInventory();
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= upperChest.getSizeInventory() ? lowerChest
				.getStackInSlot(p_70301_1_ - upperChest.getSizeInventory())
				: upperChest.getStackInSlot(p_70301_1_);
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		return p_70304_1_ >= upperChest.getSizeInventory() ? lowerChest
				.getStackInSlotOnClosing(p_70304_1_
						- upperChest.getSizeInventory()) : upperChest
				.getStackInSlotOnClosing(p_70304_1_);
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return upperChest.isInventoryNameLocalized()
				|| lowerChest.isInventoryNameLocalized();
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
	 * Return whether the given inventory is part of this large chest.
	 */
	public boolean isPartOfLargeChest(IInventory p_90010_1_) {
		return upperChest == p_90010_1_ || lowerChest == p_90010_1_;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return upperChest.isUseableByPlayer(p_70300_1_)
				&& lowerChest.isUseableByPlayer(p_70300_1_);
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	@Override
	public void onInventoryChanged() {
		upperChest.onInventoryChanged();
		lowerChest.onInventoryChanged();
	}

	@Override
	public void openInventory() {
		upperChest.openInventory();
		lowerChest.openInventory();
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ >= upperChest.getSizeInventory()) {
			lowerChest.setInventorySlotContents(
					p_70299_1_ - upperChest.getSizeInventory(), p_70299_2_);
		} else {
			upperChest.setInventorySlotContents(p_70299_1_, p_70299_2_);
		}
	}
}

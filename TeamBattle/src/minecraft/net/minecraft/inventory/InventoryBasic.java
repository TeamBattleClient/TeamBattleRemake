package net.minecraft.inventory;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class InventoryBasic implements IInventory {
	private List field_70480_d;
	private boolean field_94051_e;
	private final ItemStack[] inventoryContents;
	private String inventoryTitle;
	private final int slotsCount;

	public InventoryBasic(String p_i1561_1_, boolean p_i1561_2_, int p_i1561_3_) {
		inventoryTitle = p_i1561_1_;
		field_94051_e = p_i1561_2_;
		slotsCount = p_i1561_3_;
		inventoryContents = new ItemStack[p_i1561_3_];
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
		if (inventoryContents[p_70298_1_] != null) {
			ItemStack var3;

			if (inventoryContents[p_70298_1_].stackSize <= p_70298_2_) {
				var3 = inventoryContents[p_70298_1_];
				inventoryContents[p_70298_1_] = null;
				onInventoryChanged();
				return var3;
			} else {
				var3 = inventoryContents[p_70298_1_].splitStack(p_70298_2_);

				if (inventoryContents[p_70298_1_].stackSize == 0) {
					inventoryContents[p_70298_1_] = null;
				}

				onInventoryChanged();
				return var3;
			}
		} else
			return null;
	}

	public void func_110132_b(IInvBasic p_110132_1_) {
		field_70480_d.remove(p_110132_1_);
	}

	public void func_110133_a(String p_110133_1_) {
		field_94051_e = true;
		inventoryTitle = p_110133_1_;
	}

	public void func_110134_a(IInvBasic p_110134_1_) {
		if (field_70480_d == null) {
			field_70480_d = new ArrayList();
		}

		field_70480_d.add(p_110134_1_);
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return inventoryTitle;
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
		return slotsCount;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= 0 && p_70301_1_ < inventoryContents.length ? inventoryContents[p_70301_1_]
				: null;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (inventoryContents[p_70304_1_] != null) {
			final ItemStack var2 = inventoryContents[p_70304_1_];
			inventoryContents[p_70304_1_] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return field_94051_e;
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
		if (field_70480_d != null) {
			for (int var1 = 0; var1 < field_70480_d.size(); ++var1) {
				((IInvBasic) field_70480_d.get(var1)).onInventoryChanged(this);
			}
		}
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
		inventoryContents[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null
				&& p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}

		onInventoryChanged();
	}
}

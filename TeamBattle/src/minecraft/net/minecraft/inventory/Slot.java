package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class Slot {
	/** The inventory we want to extract a slot from. */
	public final IInventory inventory;

	/** The index of the slot in the inventory. */
	private final int slotIndex;

	/** the id of the slot(also the index in the inventory arraylist) */
	public int slotNumber;

	/** display position of the inventory slot on the screen x axis */
	public int xDisplayPosition;

	/** display position of the inventory slot on the screen y axis */
	public int yDisplayPosition;

	public Slot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_,
			int p_i1824_4_) {
		inventory = p_i1824_1_;
		slotIndex = p_i1824_2_;
		xDisplayPosition = p_i1824_3_;
		yDisplayPosition = p_i1824_4_;
	}

	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	public boolean canTakeStack(EntityPlayer p_82869_1_) {
		return true;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new stack.
	 */
	public ItemStack decrStackSize(int p_75209_1_) {
		return inventory.decrStackSize(slotIndex, p_75209_1_);
	}

	public boolean func_111238_b() {
		return true;
	}

	/**
	 * Returns the icon index on items.png that is used as background image of
	 * the slot.
	 */
	public IIcon getBackgroundIconIndex() {
		return null;
	}

	/**
	 * Returns if this slot contains a stack.
	 */
	public boolean getHasStack() {
		return getStack() != null;
	}

	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	public int getSlotStackLimit() {
		return inventory.getInventoryStackLimit();
	}

	/**
	 * Helper fnct to get the stack in the slot.
	 */
	public ItemStack getStack() {
		return inventory.getStackInSlot(slotIndex);
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	public boolean isItemValid(ItemStack p_75214_1_) {
		return true;
	}

	/**
	 * returns true if this slot is in par2 of par1
	 */
	public boolean isSlotInInventory(IInventory p_75217_1_, int p_75217_2_) {
		return p_75217_1_ == inventory && p_75217_2_ == slotIndex;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood.
	 */
	protected void onCrafting(ItemStack p_75208_1_) {
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
	}

	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		onSlotChanged();
	}

	/**
	 * if par2 has more items than par1, onCrafting(item,countIncrease) is
	 * called
	 */
	public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
		if (p_75220_1_ != null && p_75220_2_ != null) {
			if (p_75220_1_.getItem() == p_75220_2_.getItem()) {
				final int var3 = p_75220_2_.stackSize - p_75220_1_.stackSize;

				if (var3 > 0) {
					this.onCrafting(p_75220_1_, var3);
				}
			}
		}
	}

	/**
	 * Called when the stack in a Slot changes
	 */
	public void onSlotChanged() {
		inventory.onInventoryChanged();
	}

	/**
	 * Helper method to put a stack in the slot.
	 */
	public void putStack(ItemStack p_75215_1_) {
		inventory.setInventorySlotContents(slotIndex, p_75215_1_);
		onSlotChanged();
	}
}

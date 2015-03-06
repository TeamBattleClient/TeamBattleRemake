package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerChest extends Container {
	private final IInventory lowerChestInventory;
	private final int numRows;

	public ContainerChest(IInventory p_i1806_1_, IInventory p_i1806_2_) {
		lowerChestInventory = p_i1806_2_;
		numRows = p_i1806_2_.getSizeInventory() / 9;
		p_i1806_2_.openInventory();
		final int var3 = (numRows - 4) * 18;
		int var4;
		int var5;

		for (var4 = 0; var4 < numRows; ++var4) {
			for (var5 = 0; var5 < 9; ++var5) {
				addSlotToContainer(new Slot(p_i1806_2_, var5 + var4 * 9,
						8 + var5 * 18, 18 + var4 * 18));
			}
		}

		for (var4 = 0; var4 < 3; ++var4) {
			for (var5 = 0; var5 < 9; ++var5) {
				addSlotToContainer(new Slot(p_i1806_1_, var5 + var4 * 9 + 9,
						8 + var5 * 18, 103 + var4 * 18 + var3));
			}
		}

		for (var4 = 0; var4 < 9; ++var4) {
			addSlotToContainer(new Slot(p_i1806_1_, var4, 8 + var4 * 18,
					161 + var3));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return lowerChestInventory.isUseableByPlayer(p_75145_1_);
	}

	/**
	 * Return this chest container's lower chest inventory.
	 */
	public IInventory getLowerChestInventory() {
		return lowerChestInventory;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		lowerChestInventory.closeInventory();
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or
	 * you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_) {
		ItemStack var3 = null;
		final Slot var4 = (Slot) inventorySlots.get(p_82846_2_);

		if (var4 != null && var4.getHasStack()) {
			final ItemStack var5 = var4.getStack();
			var3 = var5.copy();

			if (p_82846_2_ < numRows * 9) {
				if (!mergeItemStack(var5, numRows * 9, inventorySlots.size(),
						true))
					return null;
			} else if (!mergeItemStack(var5, 0, numRows * 9, false))
				return null;

			if (var5.stackSize == 0) {
				var4.putStack((ItemStack) null);
			} else {
				var4.onSlotChanged();
			}
		}

		return var3;
	}
}

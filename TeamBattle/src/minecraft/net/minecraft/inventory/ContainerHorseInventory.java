package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container {
	private IInventory field_111243_a;
	private EntityHorse theHorse;

	public ContainerHorseInventory(IInventory p_i1817_1_,
			final IInventory p_i1817_2_, final EntityHorse p_i1817_3_) {
		field_111243_a = p_i1817_2_;
		theHorse = p_i1817_3_;
		final byte var4 = 3;
		p_i1817_2_.openInventory();
		final int var5 = (var4 - 4) * 18;
		addSlotToContainer(new Slot(p_i1817_2_, 0, 8, 18) {

			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return super.isItemValid(p_75214_1_)
						&& p_75214_1_.getItem() == Items.saddle
						&& !getHasStack();
			}
		});
		addSlotToContainer(new Slot(p_i1817_2_, 1, 8, 36) {

			@Override
			public boolean func_111238_b() {
				return p_i1817_3_.func_110259_cr();
			}

			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return super.isItemValid(p_75214_1_)
						&& p_i1817_3_.func_110259_cr()
						&& EntityHorse.func_146085_a(p_75214_1_.getItem());
			}
		});
		int var6;
		int var7;

		if (p_i1817_3_.isChested()) {
			for (var6 = 0; var6 < var4; ++var6) {
				for (var7 = 0; var7 < 5; ++var7) {
					addSlotToContainer(new Slot(p_i1817_2_,
							2 + var7 + var6 * 5, 80 + var7 * 18, 18 + var6 * 18));
				}
			}
		}

		for (var6 = 0; var6 < 3; ++var6) {
			for (var7 = 0; var7 < 9; ++var7) {
				addSlotToContainer(new Slot(p_i1817_1_, var7 + var6 * 9 + 9,
						8 + var7 * 18, 102 + var6 * 18 + var5));
			}
		}

		for (var6 = 0; var6 < 9; ++var6) {
			addSlotToContainer(new Slot(p_i1817_1_, var6, 8 + var6 * 18,
					160 + var5));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return field_111243_a.isUseableByPlayer(p_75145_1_)
				&& theHorse.isEntityAlive()
				&& theHorse.getDistanceToEntity(p_75145_1_) < 8.0F;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		field_111243_a.closeInventory();
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

			if (p_82846_2_ < field_111243_a.getSizeInventory()) {
				if (!mergeItemStack(var5, field_111243_a.getSizeInventory(),
						inventorySlots.size(), true))
					return null;
			} else if (getSlot(1).isItemValid(var5)
					&& !getSlot(1).getHasStack()) {
				if (!mergeItemStack(var5, 1, 2, false))
					return null;
			} else if (getSlot(0).isItemValid(var5)) {
				if (!mergeItemStack(var5, 0, 1, false))
					return null;
			} else if (field_111243_a.getSizeInventory() <= 2
					|| !mergeItemStack(var5, 2,
							field_111243_a.getSizeInventory(), false))
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

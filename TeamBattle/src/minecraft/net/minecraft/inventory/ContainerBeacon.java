package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;

public class ContainerBeacon extends Container {
	class BeaconSlot extends Slot {

		public BeaconSlot(IInventory p_i1801_2_, int p_i1801_3_,
				int p_i1801_4_, int p_i1801_5_) {
			super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return p_75214_1_ == null ? false
					: p_75214_1_.getItem() == Items.emerald
							|| p_75214_1_.getItem() == Items.diamond
							|| p_75214_1_.getItem() == Items.gold_ingot
							|| p_75214_1_.getItem() == Items.iron_ingot;
		}
	}

	/**
	 * This beacon's slot where you put in Emerald, Diamond, Gold or Iron Ingot.
	 */
	private final ContainerBeacon.BeaconSlot beaconSlot;
	private final int field_82865_g;
	private final int field_82867_h;
	private final int field_82868_i;

	private final TileEntityBeacon theBeacon;

	public ContainerBeacon(InventoryPlayer p_i1802_1_,
			TileEntityBeacon p_i1802_2_) {
		theBeacon = p_i1802_2_;
		addSlotToContainer(beaconSlot = new ContainerBeacon.BeaconSlot(
				p_i1802_2_, 0, 136, 110));
		final byte var3 = 36;
		final short var4 = 137;
		int var5;

		for (var5 = 0; var5 < 3; ++var5) {
			for (int var6 = 0; var6 < 9; ++var6) {
				addSlotToContainer(new Slot(p_i1802_1_, var6 + var5 * 9 + 9,
						var3 + var6 * 18, var4 + var5 * 18));
			}
		}

		for (var5 = 0; var5 < 9; ++var5) {
			addSlotToContainer(new Slot(p_i1802_1_, var5, var3 + var5 * 18,
					58 + var4));
		}

		field_82865_g = p_i1802_2_.func_145998_l();
		field_82867_h = p_i1802_2_.func_146007_j();
		field_82868_i = p_i1802_2_.func_146006_k();
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, field_82865_g);
		p_75132_1_.sendProgressBarUpdate(this, 1, field_82867_h);
		p_75132_1_.sendProgressBarUpdate(this, 2, field_82868_i);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return theBeacon.isUseableByPlayer(p_75145_1_);
	}

	public TileEntityBeacon func_148327_e() {
		return theBeacon;
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

			if (p_82846_2_ == 0) {
				if (!mergeItemStack(var5, 1, 37, true))
					return null;

				var4.onSlotChange(var5, var3);
			} else if (!beaconSlot.getHasStack()
					&& beaconSlot.isItemValid(var5) && var5.stackSize == 1) {
				if (!mergeItemStack(var5, 0, 1, false))
					return null;
			} else if (p_82846_2_ >= 1 && p_82846_2_ < 28) {
				if (!mergeItemStack(var5, 28, 37, false))
					return null;
			} else if (p_82846_2_ >= 28 && p_82846_2_ < 37) {
				if (!mergeItemStack(var5, 1, 28, false))
					return null;
			} else if (!mergeItemStack(var5, 1, 37, false))
				return null;

			if (var5.stackSize == 0) {
				var4.putStack((ItemStack) null);
			} else {
				var4.onSlotChanged();
			}

			if (var5.stackSize == var3.stackSize)
				return null;

			var4.onPickupFromSlot(p_82846_1_, var5);
		}

		return var3;
	}

	@Override
	public void updateProgressBar(int p_75137_1_, int p_75137_2_) {
		if (p_75137_1_ == 0) {
			theBeacon.func_146005_c(p_75137_2_);
		}

		if (p_75137_1_ == 1) {
			theBeacon.func_146001_d(p_75137_2_);
		}

		if (p_75137_1_ == 2) {
			theBeacon.func_146004_e(p_75137_2_);
		}
	}
}

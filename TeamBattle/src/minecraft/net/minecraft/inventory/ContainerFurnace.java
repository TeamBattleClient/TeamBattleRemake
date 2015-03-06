package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container {
	private final TileEntityFurnace furnace;
	private int lastBurnTime;
	private int lastCookTime;
	private int lastItemBurnTime;

	public ContainerFurnace(InventoryPlayer p_i1812_1_,
			TileEntityFurnace p_i1812_2_) {
		furnace = p_i1812_2_;
		addSlotToContainer(new Slot(p_i1812_2_, 0, 56, 17));
		addSlotToContainer(new Slot(p_i1812_2_, 1, 56, 53));
		addSlotToContainer(new SlotFurnace(p_i1812_1_.player, p_i1812_2_, 2,
				116, 35));
		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				addSlotToContainer(new Slot(p_i1812_1_, var4 + var3 * 9 + 9,
						8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			addSlotToContainer(new Slot(p_i1812_1_, var3, 8 + var3 * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, furnace.field_145961_j);
		p_75132_1_.sendProgressBarUpdate(this, 1, furnace.field_145956_a);
		p_75132_1_.sendProgressBarUpdate(this, 2, furnace.field_145963_i);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return furnace.isUseableByPlayer(p_75145_1_);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int var1 = 0; var1 < crafters.size(); ++var1) {
			final ICrafting var2 = (ICrafting) crafters.get(var1);

			if (lastCookTime != furnace.field_145961_j) {
				var2.sendProgressBarUpdate(this, 0, furnace.field_145961_j);
			}

			if (lastBurnTime != furnace.field_145956_a) {
				var2.sendProgressBarUpdate(this, 1, furnace.field_145956_a);
			}

			if (lastItemBurnTime != furnace.field_145963_i) {
				var2.sendProgressBarUpdate(this, 2, furnace.field_145963_i);
			}
		}

		lastCookTime = furnace.field_145961_j;
		lastBurnTime = furnace.field_145956_a;
		lastItemBurnTime = furnace.field_145963_i;
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

			if (p_82846_2_ == 2) {
				if (!mergeItemStack(var5, 3, 39, true))
					return null;

				var4.onSlotChange(var5, var3);
			} else if (p_82846_2_ != 1 && p_82846_2_ != 0) {
				if (FurnaceRecipes.smelting().func_151395_a(var5) != null) {
					if (!mergeItemStack(var5, 0, 1, false))
						return null;
				} else if (TileEntityFurnace.func_145954_b(var5)) {
					if (!mergeItemStack(var5, 1, 2, false))
						return null;
				} else if (p_82846_2_ >= 3 && p_82846_2_ < 30) {
					if (!mergeItemStack(var5, 30, 39, false))
						return null;
				} else if (p_82846_2_ >= 30 && p_82846_2_ < 39
						&& !mergeItemStack(var5, 3, 30, false))
					return null;
			} else if (!mergeItemStack(var5, 3, 39, false))
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
			furnace.field_145961_j = p_75137_2_;
		}

		if (p_75137_1_ == 1) {
			furnace.field_145956_a = p_75137_2_;
		}

		if (p_75137_1_ == 2) {
			furnace.field_145963_i = p_75137_2_;
		}
	}
}

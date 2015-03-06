package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMerchant extends Container {
	private final InventoryMerchant merchantInventory;
	/** Instance of Merchant. */
	private final IMerchant theMerchant;

	/** Instance of World. */
	private final World theWorld;

	public ContainerMerchant(InventoryPlayer p_i1821_1_, IMerchant p_i1821_2_,
			World p_i1821_3_) {
		theMerchant = p_i1821_2_;
		theWorld = p_i1821_3_;
		merchantInventory = new InventoryMerchant(p_i1821_1_.player, p_i1821_2_);
		addSlotToContainer(new Slot(merchantInventory, 0, 36, 53));
		addSlotToContainer(new Slot(merchantInventory, 1, 62, 53));
		addSlotToContainer(new SlotMerchantResult(p_i1821_1_.player,
				p_i1821_2_, merchantInventory, 2, 120, 53));
		int var4;

		for (var4 = 0; var4 < 3; ++var4) {
			for (int var5 = 0; var5 < 9; ++var5) {
				addSlotToContainer(new Slot(p_i1821_1_, var5 + var4 * 9 + 9,
						8 + var5 * 18, 84 + var4 * 18));
			}
		}

		for (var4 = 0; var4 < 9; ++var4) {
			addSlotToContainer(new Slot(p_i1821_1_, var4, 8 + var4 * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return theMerchant.getCustomer() == p_75145_1_;
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	public InventoryMerchant getMerchantInventory() {
		return merchantInventory;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);
		theMerchant.setCustomer((EntityPlayer) null);
		super.onContainerClosed(p_75134_1_);

		if (!theWorld.isClient) {
			ItemStack var2 = merchantInventory.getStackInSlotOnClosing(0);

			if (var2 != null) {
				p_75134_1_.dropPlayerItemWithRandomChoice(var2, false);
			}

			var2 = merchantInventory.getStackInSlotOnClosing(1);

			if (var2 != null) {
				p_75134_1_.dropPlayerItemWithRandomChoice(var2, false);
			}
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(IInventory p_75130_1_) {
		merchantInventory.resetRecipeAndSlots();
		super.onCraftMatrixChanged(p_75130_1_);
	}

	public void setCurrentRecipeIndex(int p_75175_1_) {
		merchantInventory.setCurrentRecipeIndex(p_75175_1_);
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
			} else if (p_82846_2_ != 0 && p_82846_2_ != 1) {
				if (p_82846_2_ >= 3 && p_82846_2_ < 30) {
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
	}
}

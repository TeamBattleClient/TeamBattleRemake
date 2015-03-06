package net.minecraft.inventory;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

public class SlotMerchantResult extends Slot {
	private int field_75231_g;

	/** "Instance" of the Merchant. */
	private final IMerchant theMerchant;
	/** Merchant's inventory. */
	private final InventoryMerchant theMerchantInventory;

	/** The Player whos trying to buy/sell stuff. */
	private final EntityPlayer thePlayer;

	public SlotMerchantResult(EntityPlayer p_i1822_1_, IMerchant p_i1822_2_,
			InventoryMerchant p_i1822_3_, int p_i1822_4_, int p_i1822_5_,
			int p_i1822_6_) {
		super(p_i1822_3_, p_i1822_4_, p_i1822_5_, p_i1822_6_);
		thePlayer = p_i1822_1_;
		theMerchant = p_i1822_2_;
		theMerchantInventory = p_i1822_3_;
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of
	 * the second int arg. Returns the new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_75209_1_) {
		if (getHasStack()) {
			field_75231_g += Math.min(p_75209_1_, getStack().stackSize);
		}

		return super.decrStackSize(p_75209_1_);
	}

	private boolean func_75230_a(MerchantRecipe p_75230_1_,
			ItemStack p_75230_2_, ItemStack p_75230_3_) {
		final ItemStack var4 = p_75230_1_.getItemToBuy();
		final ItemStack var5 = p_75230_1_.getSecondItemToBuy();

		if (p_75230_2_ != null && p_75230_2_.getItem() == var4.getItem()) {
			if (var5 != null && p_75230_3_ != null
					&& var5.getItem() == p_75230_3_.getItem()) {
				p_75230_2_.stackSize -= var4.stackSize;
				p_75230_3_.stackSize -= var5.stackSize;
				return true;
			}

			if (var5 == null && p_75230_3_ == null) {
				p_75230_2_.stackSize -= var4.stackSize;
				return true;
			}
		}

		return false;
	}

	/**
	 * Check if the stack is a valid item for this slot. Always true beside for
	 * the armor slots.
	 */
	@Override
	public boolean isItemValid(ItemStack p_75214_1_) {
		return false;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood.
	 */
	@Override
	protected void onCrafting(ItemStack p_75208_1_) {
		p_75208_1_.onCrafting(thePlayer.worldObj, thePlayer, field_75231_g);
		field_75231_g = 0;
	}

	/**
	 * the itemStack passed in is the output - ie, iron ingots, and pickaxes,
	 * not ore and wood. Typically increases an internal count then calls
	 * onCrafting(item).
	 */
	@Override
	protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
		field_75231_g += p_75210_2_;
		this.onCrafting(p_75210_1_);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer p_82870_1_, ItemStack p_82870_2_) {
		this.onCrafting(p_82870_2_);
		final MerchantRecipe var3 = theMerchantInventory.getCurrentRecipe();

		if (var3 != null) {
			ItemStack var4 = theMerchantInventory.getStackInSlot(0);
			ItemStack var5 = theMerchantInventory.getStackInSlot(1);

			if (func_75230_a(var3, var4, var5)
					|| func_75230_a(var3, var5, var4)) {
				theMerchant.useRecipe(var3);

				if (var4 != null && var4.stackSize <= 0) {
					var4 = null;
				}

				if (var5 != null && var5.stackSize <= 0) {
					var5 = null;
				}

				theMerchantInventory.setInventorySlotContents(0, var4);
				theMerchantInventory.setInventorySlotContents(1, var5);
			}
		}
	}
}

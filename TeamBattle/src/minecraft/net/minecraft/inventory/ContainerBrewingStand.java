package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntityBrewingStand;

public class ContainerBrewingStand extends Container {
	class Ingredient extends Slot {

		public Ingredient(IInventory p_i1803_2_, int p_i1803_3_,
				int p_i1803_4_, int p_i1803_5_) {
			super(p_i1803_2_, p_i1803_3_, p_i1803_4_, p_i1803_5_);
		}

		@Override
		public int getSlotStackLimit() {
			return 64;
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return p_75214_1_ != null ? p_75214_1_.getItem()
					.isPotionIngredient(p_75214_1_) : false;
		}
	}

	static class Potion extends Slot {
		public static boolean canHoldPotion(ItemStack p_75243_0_) {
			return p_75243_0_ != null
					&& (p_75243_0_.getItem() == Items.potionitem || p_75243_0_
							.getItem() == Items.glass_bottle);
		}

		private final EntityPlayer player;

		public Potion(EntityPlayer p_i1804_1_, IInventory p_i1804_2_,
				int p_i1804_3_, int p_i1804_4_, int p_i1804_5_) {
			super(p_i1804_2_, p_i1804_3_, p_i1804_4_, p_i1804_5_);
			player = p_i1804_1_;
		}

		@Override
		public int getSlotStackLimit() {
			return 1;
		}

		@Override
		public boolean isItemValid(ItemStack p_75214_1_) {
			return canHoldPotion(p_75214_1_);
		}

		@Override
		public void onPickupFromSlot(EntityPlayer p_82870_1_,
				ItemStack p_82870_2_) {
			if (p_82870_2_.getItem() == Items.potionitem
					&& p_82870_2_.getItemDamage() > 0) {
				player.addStat(AchievementList.potion, 1);
			}

			super.onPickupFromSlot(p_82870_1_, p_82870_2_);
		}
	}

	private int brewTime;

	/** Instance of Slot. */
	private final Slot theSlot;

	private final TileEntityBrewingStand tileBrewingStand;

	public ContainerBrewingStand(InventoryPlayer p_i1805_1_,
			TileEntityBrewingStand p_i1805_2_) {
		tileBrewingStand = p_i1805_2_;
		addSlotToContainer(new ContainerBrewingStand.Potion(p_i1805_1_.player,
				p_i1805_2_, 0, 56, 46));
		addSlotToContainer(new ContainerBrewingStand.Potion(p_i1805_1_.player,
				p_i1805_2_, 1, 79, 53));
		addSlotToContainer(new ContainerBrewingStand.Potion(p_i1805_1_.player,
				p_i1805_2_, 2, 102, 46));
		theSlot = addSlotToContainer(new ContainerBrewingStand.Ingredient(
				p_i1805_2_, 3, 79, 17));
		int var3;

		for (var3 = 0; var3 < 3; ++var3) {
			for (int var4 = 0; var4 < 9; ++var4) {
				addSlotToContainer(new Slot(p_i1805_1_, var4 + var3 * 9 + 9,
						8 + var4 * 18, 84 + var3 * 18));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			addSlotToContainer(new Slot(p_i1805_1_, var3, 8 + var3 * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0,
				tileBrewingStand.func_145935_i());
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return tileBrewingStand.isUseableByPlayer(p_75145_1_);
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int var1 = 0; var1 < crafters.size(); ++var1) {
			final ICrafting var2 = (ICrafting) crafters.get(var1);

			if (brewTime != tileBrewingStand.func_145935_i()) {
				var2.sendProgressBarUpdate(this, 0,
						tileBrewingStand.func_145935_i());
			}
		}

		brewTime = tileBrewingStand.func_145935_i();
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

			if ((p_82846_2_ < 0 || p_82846_2_ > 2) && p_82846_2_ != 3) {
				if (!theSlot.getHasStack() && theSlot.isItemValid(var5)) {
					if (!mergeItemStack(var5, 3, 4, false))
						return null;
				} else if (ContainerBrewingStand.Potion.canHoldPotion(var3)) {
					if (!mergeItemStack(var5, 0, 3, false))
						return null;
				} else if (p_82846_2_ >= 4 && p_82846_2_ < 31) {
					if (!mergeItemStack(var5, 31, 40, false))
						return null;
				} else if (p_82846_2_ >= 31 && p_82846_2_ < 40) {
					if (!mergeItemStack(var5, 4, 31, false))
						return null;
				} else if (!mergeItemStack(var5, 4, 40, false))
					return null;
			} else {
				if (!mergeItemStack(var5, 4, 40, true))
					return null;

				var4.onSlotChange(var5, var3);
			}

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
			tileBrewingStand.func_145938_d(p_75137_2_);
		}
	}
}

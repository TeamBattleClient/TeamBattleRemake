package net.minecraft.entity.player;

import java.util.concurrent.Callable;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ReportedException;

public class InventoryPlayer implements IInventory {
	/**
	 * Get the size of the player hotbar inventory
	 */
	public static int getHotbarSize() {
		return 9;
	}

	/** An array of 4 item stacks containing the currently worn armor pieces. */
	public ItemStack[] armorInventory = new ItemStack[4];

	/** The index of the currently held item (0-8). */
	public int currentItem;

	/** The current ItemStack. */
	private ItemStack currentItemStack;

	/**
	 * Set true whenever the inventory changes. Nothing sets it false so you
	 * will have to write your own code to check it and reset the value.
	 */
	public boolean inventoryChanged;
	private ItemStack itemStack;

	/**
	 * An array of 36 item stacks indicating the main player inventory
	 * (including the visible bar).
	 */
	public ItemStack[] mainInventory = new ItemStack[36];

	/** The player whose inventory this is. */
	public EntityPlayer player;

	public InventoryPlayer(EntityPlayer p_i1750_1_) {
		player = p_i1750_1_;
	}

	/**
	 * Adds the item stack to the inventory, returns false if it is impossible.
	 */
	public boolean addItemStackToInventory(final ItemStack p_70441_1_) {
		if (p_70441_1_ != null && p_70441_1_.stackSize != 0
				&& p_70441_1_.getItem() != null) {
			try {
				int var2;

				if (p_70441_1_.isItemDamaged()) {
					var2 = getFirstEmptyStack();

					if (var2 >= 0) {
						mainInventory[var2] = ItemStack
								.copyItemStack(p_70441_1_);
						mainInventory[var2].animationsToGo = 5;
						p_70441_1_.stackSize = 0;
						return true;
					} else if (player.capabilities.isCreativeMode) {
						p_70441_1_.stackSize = 0;
						return true;
					} else
						return false;
				} else {
					do {
						var2 = p_70441_1_.stackSize;
						p_70441_1_.stackSize = storePartialItemStack(p_70441_1_);
					} while (p_70441_1_.stackSize > 0
							&& p_70441_1_.stackSize < var2);

					if (p_70441_1_.stackSize == var2
							&& player.capabilities.isCreativeMode) {
						p_70441_1_.stackSize = 0;
						return true;
					} else
						return p_70441_1_.stackSize < var2;
				}
			} catch (final Throwable var5) {
				final CrashReport var3 = CrashReport.makeCrashReport(var5,
						"Adding item to inventory");
				final CrashReportCategory var4 = var3
						.makeCategory("Item being added");
				var4.addCrashSection("Item ID", Integer.valueOf(Item
						.getIdFromItem(p_70441_1_.getItem())));
				var4.addCrashSection("Item data",
						Integer.valueOf(p_70441_1_.getItemDamage()));
				var4.addCrashSectionCallable("Item name", new Callable() {

					@Override
					public String call() {
						return p_70441_1_.getDisplayName();
					}
				});
				throw new ReportedException(var3);
			}
		} else
			return false;
	}

	/**
	 * returns a player armor item (as itemstack) contained in specified armor
	 * slot.
	 */
	public ItemStack armorItemInSlot(int p_70440_1_) {
		return armorInventory[p_70440_1_];
	}

	/**
	 * Switch the current item to the next one or the previous one
	 */
	public void changeCurrentItem(int p_70453_1_) {
		if (p_70453_1_ > 0) {
			p_70453_1_ = 1;
		}

		if (p_70453_1_ < 0) {
			p_70453_1_ = -1;
		}

		for (currentItem -= p_70453_1_; currentItem < 0; currentItem += 9) {
			;
		}

		while (currentItem >= 9) {
			currentItem -= 9;
		}
	}

	/**
	 * Removes all items from player inventory, including armor
	 */
	public int clearInventory(Item p_146027_1_, int p_146027_2_) {
		int var3 = 0;
		int var4;
		ItemStack var5;

		for (var4 = 0; var4 < mainInventory.length; ++var4) {
			var5 = mainInventory[var4];

			if (var5 != null
					&& (p_146027_1_ == null || var5.getItem() == p_146027_1_)
					&& (p_146027_2_ <= -1 || var5.getItemDamage() == p_146027_2_)) {
				var3 += var5.stackSize;
				mainInventory[var4] = null;
			}
		}

		for (var4 = 0; var4 < armorInventory.length; ++var4) {
			var5 = armorInventory[var4];

			if (var5 != null
					&& (p_146027_1_ == null || var5.getItem() == p_146027_1_)
					&& (p_146027_2_ <= -1 || var5.getItemDamage() == p_146027_2_)) {
				var3 += var5.stackSize;
				armorInventory[var4] = null;
			}
		}

		if (itemStack != null) {
			if (p_146027_1_ != null && itemStack.getItem() != p_146027_1_)
				return var3;

			if (p_146027_2_ > -1 && itemStack.getItemDamage() != p_146027_2_)
				return var3;

			var3 += itemStack.stackSize;
			setItemStack((ItemStack) null);
		}

		return var3;
	}

	@Override
	public void closeInventory() {
	}

	public boolean consumeInventoryItem(Item p_146026_1_) {
		final int var2 = func_146029_c(p_146026_1_);

		if (var2 < 0)
			return false;
		else {
			if (--mainInventory[var2].stackSize <= 0) {
				mainInventory[var2] = null;
			}

			return true;
		}
	}

	/**
	 * Copy the ItemStack contents from another InventoryPlayer instance
	 */
	public void copyInventory(InventoryPlayer p_70455_1_) {
		int var2;

		for (var2 = 0; var2 < mainInventory.length; ++var2) {
			mainInventory[var2] = ItemStack
					.copyItemStack(p_70455_1_.mainInventory[var2]);
		}

		for (var2 = 0; var2 < armorInventory.length; ++var2) {
			armorInventory[var2] = ItemStack
					.copyItemStack(p_70455_1_.armorInventory[var2]);
		}

		currentItem = p_70455_1_.currentItem;
	}

	/**
	 * Damages armor in each slot by the specified amount.
	 */
	public void damageArmor(float p_70449_1_) {
		p_70449_1_ /= 4.0F;

		if (p_70449_1_ < 1.0F) {
			p_70449_1_ = 1.0F;
		}

		for (int var2 = 0; var2 < armorInventory.length; ++var2) {
			if (armorInventory[var2] != null
					&& armorInventory[var2].getItem() instanceof ItemArmor) {
				armorInventory[var2].damageItem((int) p_70449_1_, player);

				if (armorInventory[var2].stackSize == 0) {
					armorInventory[var2] = null;
				}
			}
		}
	}

	/**
	 * Decrement the number of animations remaining. Only called on client side.
	 * This is used to handle the animation of receiving a block.
	 */
	public void decrementAnimations() {
		for (int var1 = 0; var1 < mainInventory.length; ++var1) {
			if (mainInventory[var1] != null) {
				mainInventory[var1].updateAnimation(player.worldObj, player,
						var1, currentItem == var1);
			}
		}
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		ItemStack[] var3 = mainInventory;

		if (p_70298_1_ >= mainInventory.length) {
			var3 = armorInventory;
			p_70298_1_ -= mainInventory.length;
		}

		if (var3[p_70298_1_] != null) {
			ItemStack var4;

			if (var3[p_70298_1_].stackSize <= p_70298_2_) {
				var4 = var3[p_70298_1_];
				var3[p_70298_1_] = null;
				return var4;
			} else {
				var4 = var3[p_70298_1_].splitStack(p_70298_2_);

				if (var3[p_70298_1_].stackSize == 0) {
					var3[p_70298_1_] = null;
				}

				return var4;
			}
		} else
			return null;
	}

	/**
	 * Drop all armor and main inventory items.
	 */
	public void dropAllItems() {
		int var1;

		for (var1 = 0; var1 < mainInventory.length; ++var1) {
			if (mainInventory[var1] != null) {
				player.func_146097_a(mainInventory[var1], true, false);
				mainInventory[var1] = null;
			}
		}

		for (var1 = 0; var1 < armorInventory.length; ++var1) {
			if (armorInventory[var1] != null) {
				player.func_146097_a(armorInventory[var1], true, false);
				armorInventory[var1] = null;
			}
		}
	}

	public float func_146023_a(Block p_146023_1_) {
		float var2 = 1.0F;

		if (mainInventory[currentItem] != null) {
			var2 *= mainInventory[currentItem].func_150997_a(p_146023_1_);
		}

		return var2;
	}

	private int func_146024_c(Item p_146024_1_, int p_146024_2_) {
		for (int var3 = 0; var3 < mainInventory.length; ++var3) {
			if (mainInventory[var3] != null
					&& mainInventory[var3].getItem() == p_146024_1_
					&& mainInventory[var3].getItemDamage() == p_146024_2_)
				return var3;
		}

		return -1;
	}

	public boolean func_146025_b(Block p_146025_1_) {
		if (p_146025_1_.getMaterial().isToolNotRequired())
			return true;
		else {
			final ItemStack var2 = getStackInSlot(currentItem);
			return var2 != null ? var2.func_150998_b(p_146025_1_) : false;
		}
	}

	private int func_146029_c(Item p_146029_1_) {
		for (int var2 = 0; var2 < mainInventory.length; ++var2) {
			if (mainInventory[var2] != null
					&& mainInventory[var2].getItem() == p_146029_1_)
				return var2;
		}

		return -1;
	}

	public void func_146030_a(Item p_146030_1_, int p_146030_2_,
			boolean p_146030_3_, boolean p_146030_4_) {
		currentItemStack = getCurrentItem();
		int var7;

		if (p_146030_3_) {
			var7 = func_146024_c(p_146030_1_, p_146030_2_);
		} else {
			var7 = func_146029_c(p_146030_1_);
		}

		if (var7 >= 0 && var7 < 9) {
			currentItem = var7;
		} else {
			if (p_146030_4_ && p_146030_1_ != null) {
				final int var6 = getFirstEmptyStack();

				if (var6 >= 0 && var6 < 9) {
					currentItem = var6;
				}

				func_70439_a(p_146030_1_, p_146030_2_);
			}
		}
	}

	public void func_70439_a(Item p_70439_1_, int p_70439_2_) {
		if (p_70439_1_ != null) {
			if (currentItemStack != null
					&& currentItemStack.isItemEnchantable()
					&& func_146024_c(currentItemStack.getItem(),
							currentItemStack.getItemDamageForDisplay()) == currentItem)
				return;

			final int var3 = func_146024_c(p_70439_1_, p_70439_2_);

			if (var3 >= 0) {
				final int var4 = mainInventory[var3].stackSize;
				mainInventory[var3] = mainInventory[currentItem];
				mainInventory[currentItem] = new ItemStack(p_70439_1_, var4,
						p_70439_2_);
			} else {
				mainInventory[currentItem] = new ItemStack(p_70439_1_, 1,
						p_70439_2_);
			}
		}
	}

	/**
	 * Returns the item stack currently held by the player.
	 */
	public ItemStack getCurrentItem() {
		return currentItem < 9 && currentItem >= 0 ? mainInventory[currentItem]
				: null;
	}

	/**
	 * Returns the first item stack that is empty.
	 */
	public int getFirstEmptyStack() {
		for (int var1 = 0; var1 < mainInventory.length; ++var1) {
			if (mainInventory[var1] == null)
				return var1;
		}

		return -1;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return "container.inventory";
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return mainInventory.length + 4;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		ItemStack[] var2 = mainInventory;

		if (p_70301_1_ >= var2.length) {
			p_70301_1_ -= var2.length;
			var2 = armorInventory;
		}

		return var2[p_70301_1_];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		ItemStack[] var2 = mainInventory;

		if (p_70304_1_ >= mainInventory.length) {
			var2 = armorInventory;
			p_70304_1_ -= mainInventory.length;
		}

		if (var2[p_70304_1_] != null) {
			final ItemStack var3 = var2[p_70304_1_];
			var2[p_70304_1_] = null;
			return var3;
		} else
			return null;
	}

	/**
	 * Based on the damage values and maximum damage values of each armor item,
	 * returns the current armor value.
	 */
	public int getTotalArmorValue() {
		int var1 = 0;

		for (final ItemStack element : armorInventory) {
			if (element != null && element.getItem() instanceof ItemArmor) {
				final int var3 = ((ItemArmor) element.getItem()).damageReduceAmount;
				var1 += var3;
			}
		}

		return var1;
	}

	public boolean hasItem(Item p_146028_1_) {
		final int var2 = func_146029_c(p_146028_1_);
		return var2 >= 0;
	}

	/**
	 * Returns true if the specified ItemStack exists in the inventory.
	 */
	public boolean hasItemStack(ItemStack p_70431_1_) {
		int var2;

		for (var2 = 0; var2 < armorInventory.length; ++var2) {
			if (armorInventory[var2] != null
					&& armorInventory[var2].isItemEqual(p_70431_1_))
				return true;
		}

		for (var2 = 0; var2 < mainInventory.length; ++var2) {
			if (mainInventory[var2] != null
					&& mainInventory[var2].isItemEqual(p_70431_1_))
				return true;
		}

		return false;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return false;
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
		return player.isDead ? false
				: p_70300_1_.getDistanceSqToEntity(player) <= 64.0D;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	@Override
	public void onInventoryChanged() {
		inventoryChanged = true;
	}

	@Override
	public void openInventory() {
	}

	/**
	 * Reads from the given tag list and fills the slots in the inventory with
	 * the correct items.
	 */
	public void readFromNBT(NBTTagList p_70443_1_) {
		mainInventory = new ItemStack[36];
		armorInventory = new ItemStack[4];

		for (int var2 = 0; var2 < p_70443_1_.tagCount(); ++var2) {
			final NBTTagCompound var3 = p_70443_1_.getCompoundTagAt(var2);
			final int var4 = var3.getByte("Slot") & 255;
			final ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);

			if (var5 != null) {
				if (var4 >= 0 && var4 < mainInventory.length) {
					mainInventory[var4] = var5;
				}

				if (var4 >= 100 && var4 < armorInventory.length + 100) {
					armorInventory[var4 - 100] = var5;
				}
			}
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		ItemStack[] var3 = mainInventory;

		if (p_70299_1_ >= var3.length) {
			p_70299_1_ -= var3.length;
			var3 = armorInventory;
		}

		var3[p_70299_1_] = p_70299_2_;
	}

	public void setItemStack(ItemStack p_70437_1_) {
		itemStack = p_70437_1_;
	}

	/**
	 * stores an itemstack in the users inventory
	 */
	private int storeItemStack(ItemStack p_70432_1_) {
		for (int var2 = 0; var2 < mainInventory.length; ++var2) {
			if (mainInventory[var2] != null
					&& mainInventory[var2].getItem() == p_70432_1_.getItem()
					&& mainInventory[var2].isStackable()
					&& mainInventory[var2].stackSize < mainInventory[var2]
							.getMaxStackSize()
					&& mainInventory[var2].stackSize < getInventoryStackLimit()
					&& (!mainInventory[var2].getHasSubtypes() || mainInventory[var2]
							.getItemDamage() == p_70432_1_.getItemDamage())
					&& ItemStack.areItemStackTagsEqual(mainInventory[var2],
							p_70432_1_))
				return var2;
		}

		return -1;
	}

	/**
	 * This function stores as many items of an ItemStack as possible in a
	 * matching slot and returns the quantity of left over items.
	 */
	private int storePartialItemStack(ItemStack p_70452_1_) {
		final Item var2 = p_70452_1_.getItem();
		int var3 = p_70452_1_.stackSize;
		int var4;

		if (p_70452_1_.getMaxStackSize() == 1) {
			var4 = getFirstEmptyStack();

			if (var4 < 0)
				return var3;
			else {
				if (mainInventory[var4] == null) {
					mainInventory[var4] = ItemStack.copyItemStack(p_70452_1_);
				}

				return 0;
			}
		} else {
			var4 = storeItemStack(p_70452_1_);

			if (var4 < 0) {
				var4 = getFirstEmptyStack();
			}

			if (var4 < 0)
				return var3;
			else {
				if (mainInventory[var4] == null) {
					mainInventory[var4] = new ItemStack(var2, 0,
							p_70452_1_.getItemDamage());

					if (p_70452_1_.hasTagCompound()) {
						mainInventory[var4]
								.setTagCompound((NBTTagCompound) p_70452_1_
										.getTagCompound().copy());
					}
				}

				int var5 = var3;

				if (var3 > mainInventory[var4].getMaxStackSize()
						- mainInventory[var4].stackSize) {
					var5 = mainInventory[var4].getMaxStackSize()
							- mainInventory[var4].stackSize;
				}

				if (var5 > getInventoryStackLimit()
						- mainInventory[var4].stackSize) {
					var5 = getInventoryStackLimit()
							- mainInventory[var4].stackSize;
				}

				if (var5 == 0)
					return var3;
				else {
					var3 -= var5;
					mainInventory[var4].stackSize += var5;
					mainInventory[var4].animationsToGo = 5;
					return var3;
				}
			}
		}
	}

	/**
	 * Writes the inventory out as a list of compound tags. This is where the
	 * slot indices are used (+100 for armor, +80 for crafting).
	 */
	public NBTTagList writeToNBT(NBTTagList p_70442_1_) {
		int var2;
		NBTTagCompound var3;

		for (var2 = 0; var2 < mainInventory.length; ++var2) {
			if (mainInventory[var2] != null) {
				var3 = new NBTTagCompound();
				var3.setByte("Slot", (byte) var2);
				mainInventory[var2].writeToNBT(var3);
				p_70442_1_.appendTag(var3);
			}
		}

		for (var2 = 0; var2 < armorInventory.length; ++var2) {
			if (armorInventory[var2] != null) {
				var3 = new NBTTagCompound();
				var3.setByte("Slot", (byte) (var2 + 100));
				armorInventory[var2].writeToNBT(var3);
				p_70442_1_.appendTag(var3);
			}
		}

		return p_70442_1_;
	}
}

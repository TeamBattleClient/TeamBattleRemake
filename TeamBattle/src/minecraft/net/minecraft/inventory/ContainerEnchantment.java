package net.minecraft.inventory;

import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container {
	/** 3-member array storing the enchantment levels of each slot */
	public int[] enchantLevels = new int[3];

	/** used as seed for EnchantmentNameParts (see GuiEnchantment) */
	public long nameSeed;
	private int posX;
	private int posY;
	private int posZ;
	private final Random rand = new Random();

	/** SlotEnchantmentTable object with ItemStack to be enchanted */
	public IInventory tableInventory = new InventoryBasic("Enchant", true, 1) {

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public void onInventoryChanged() {
			super.onInventoryChanged();
			ContainerEnchantment.this.onCraftMatrixChanged(this);
		}
	};

	/** current world (for bookshelf counting) */
	private World worldPointer;

	public ContainerEnchantment(InventoryPlayer p_i1811_1_, World p_i1811_2_,
			int p_i1811_3_, int p_i1811_4_, int p_i1811_5_) {
		worldPointer = p_i1811_2_;
		posX = p_i1811_3_;
		posY = p_i1811_4_;
		posZ = p_i1811_5_;
		addSlotToContainer(new Slot(tableInventory, 0, 25, 47) {

			@Override
			public boolean isItemValid(ItemStack p_75214_1_) {
				return true;
			}
		});
		int var6;

		for (var6 = 0; var6 < 3; ++var6) {
			for (int var7 = 0; var7 < 9; ++var7) {
				addSlotToContainer(new Slot(p_i1811_1_, var7 + var6 * 9 + 9,
						8 + var7 * 18, 84 + var6 * 18));
			}
		}

		for (var6 = 0; var6 < 9; ++var6) {
			addSlotToContainer(new Slot(p_i1811_1_, var6, 8 + var6 * 18, 142));
		}
	}

	@Override
	public void addCraftingToCrafters(ICrafting p_75132_1_) {
		super.addCraftingToCrafters(p_75132_1_);
		p_75132_1_.sendProgressBarUpdate(this, 0, enchantLevels[0]);
		p_75132_1_.sendProgressBarUpdate(this, 1, enchantLevels[1]);
		p_75132_1_.sendProgressBarUpdate(this, 2, enchantLevels[2]);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return worldPointer.getBlock(posX, posY, posZ) != Blocks.enchanting_table ? false
				: p_75145_1_.getDistanceSq(posX + 0.5D, posY + 0.5D,
						posZ + 0.5D) <= 64.0D;
	}

	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int var1 = 0; var1 < crafters.size(); ++var1) {
			final ICrafting var2 = (ICrafting) crafters.get(var1);
			var2.sendProgressBarUpdate(this, 0, enchantLevels[0]);
			var2.sendProgressBarUpdate(this, 1, enchantLevels[1]);
			var2.sendProgressBarUpdate(this, 2, enchantLevels[2]);
		}
	}

	/**
	 * enchants the item on the table using the specified slot; also deducts XP
	 * from player
	 */
	@Override
	public boolean enchantItem(EntityPlayer p_75140_1_, int p_75140_2_) {
		final ItemStack var3 = tableInventory.getStackInSlot(0);

		if (enchantLevels[p_75140_2_] > 0
				&& var3 != null
				&& (p_75140_1_.experienceLevel >= enchantLevels[p_75140_2_] || p_75140_1_.capabilities.isCreativeMode)) {
			if (!worldPointer.isClient) {
				final List var4 = EnchantmentHelper.buildEnchantmentList(rand,
						var3, enchantLevels[p_75140_2_]);
				final boolean var5 = var3.getItem() == Items.book;

				if (var4 != null) {
					p_75140_1_.addExperienceLevel(-enchantLevels[p_75140_2_]);

					if (var5) {
						var3.func_150996_a(Items.enchanted_book);
					}

					final int var6 = var5 && var4.size() > 1 ? rand
							.nextInt(var4.size()) : -1;

					for (int var7 = 0; var7 < var4.size(); ++var7) {
						final EnchantmentData var8 = (EnchantmentData) var4
								.get(var7);

						if (!var5 || var7 != var6) {
							if (var5) {
								Items.enchanted_book.addEnchantment(var3, var8);
							} else {
								var3.addEnchantment(var8.enchantmentobj,
										var8.enchantmentLevel);
							}
						}
					}

					onCraftMatrixChanged(tableInventory);
				}
			}

			return true;
		} else
			return false;
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(EntityPlayer p_75134_1_) {
		super.onContainerClosed(p_75134_1_);

		if (!worldPointer.isClient) {
			final ItemStack var2 = tableInventory.getStackInSlotOnClosing(0);

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
		if (p_75130_1_ == tableInventory) {
			final ItemStack var2 = p_75130_1_.getStackInSlot(0);
			int var3;

			if (var2 != null && var2.isItemEnchantable()) {
				nameSeed = rand.nextLong();

				if (!worldPointer.isClient) {
					var3 = 0;
					int var4;

					for (var4 = -1; var4 <= 1; ++var4) {
						for (int var5 = -1; var5 <= 1; ++var5) {
							if ((var4 != 0 || var5 != 0)
									&& worldPointer.isAirBlock(posX + var5,
											posY, posZ + var4)
									&& worldPointer.isAirBlock(posX + var5,
											posY + 1, posZ + var4)) {
								if (worldPointer.getBlock(posX + var5 * 2,
										posY, posZ + var4 * 2) == Blocks.bookshelf) {
									++var3;
								}

								if (worldPointer.getBlock(posX + var5 * 2,
										posY + 1, posZ + var4 * 2) == Blocks.bookshelf) {
									++var3;
								}

								if (var5 != 0 && var4 != 0) {
									if (worldPointer.getBlock(posX + var5 * 2,
											posY, posZ + var4) == Blocks.bookshelf) {
										++var3;
									}

									if (worldPointer.getBlock(posX + var5 * 2,
											posY + 1, posZ + var4) == Blocks.bookshelf) {
										++var3;
									}

									if (worldPointer.getBlock(posX + var5,
											posY, posZ + var4 * 2) == Blocks.bookshelf) {
										++var3;
									}

									if (worldPointer.getBlock(posX + var5,
											posY + 1, posZ + var4 * 2) == Blocks.bookshelf) {
										++var3;
									}
								}
							}
						}
					}

					for (var4 = 0; var4 < 3; ++var4) {
						enchantLevels[var4] = EnchantmentHelper
								.calcItemStackEnchantability(rand, var4, var3,
										var2);
					}

					detectAndSendChanges();
				}
			} else {
				for (var3 = 0; var3 < 3; ++var3) {
					enchantLevels[var3] = 0;
				}
			}
		}
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
			} else {
				if (((Slot) inventorySlots.get(0)).getHasStack()
						|| !((Slot) inventorySlots.get(0)).isItemValid(var5))
					return null;

				if (var5.hasTagCompound() && var5.stackSize == 1) {
					((Slot) inventorySlots.get(0)).putStack(var5.copy());
					var5.stackSize = 0;
				} else if (var5.stackSize >= 1) {
					((Slot) inventorySlots.get(0)).putStack(new ItemStack(var5
							.getItem(), 1, var5.getItemDamage()));
					--var5.stackSize;
				}
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
		if (p_75137_1_ >= 0 && p_75137_1_ <= 2) {
			enchantLevels[p_75137_1_] = p_75137_2_;
		} else {
			super.updateProgressBar(p_75137_1_, p_75137_2_);
		}
	}
}

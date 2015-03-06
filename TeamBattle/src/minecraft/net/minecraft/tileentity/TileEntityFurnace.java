package net.minecraft.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityFurnace extends TileEntity implements ISidedInventory {
	private static final int[] field_145959_l = new int[] { 2, 1 };

	private static final int[] field_145960_m = new int[] { 1 };

	private static final int[] field_145962_k = new int[] { 0 };

	public static int func_145952_a(ItemStack p_145952_0_) {
		if (p_145952_0_ == null)
			return 0;
		else {
			final Item var1 = p_145952_0_.getItem();

			if (var1 instanceof ItemBlock
					&& Block.getBlockFromItem(var1) != Blocks.air) {
				final Block var2 = Block.getBlockFromItem(var1);

				if (var2 == Blocks.wooden_slab)
					return 150;

				if (var2.getMaterial() == Material.wood)
					return 300;

				if (var2 == Blocks.coal_block)
					return 16000;
			}

			return var1 instanceof ItemTool
					&& ((ItemTool) var1).getToolMaterialName().equals("WOOD") ? 200
					: var1 instanceof ItemSword
							&& ((ItemSword) var1).func_150932_j()
									.equals("WOOD") ? 200
							: var1 instanceof ItemHoe
									&& ((ItemHoe) var1).getMaterialName()
											.equals("WOOD") ? 200
									: var1 == Items.stick ? 100
											: var1 == Items.coal ? 1600
													: var1 == Items.lava_bucket ? 20000
															: var1 == Item
																	.getItemFromBlock(Blocks.sapling) ? 100
																	: var1 == Items.blaze_rod ? 2400
																			: 0;
		}
	}

	public static boolean func_145954_b(ItemStack p_145954_0_) {
		return func_145952_a(p_145954_0_) > 0;
	}

	public int field_145956_a;
	private ItemStack[] field_145957_n = new ItemStack[3];
	private String field_145958_o;

	public int field_145961_j;

	public int field_145963_i;

	/**
	 * Returns true if automation can extract the given item in the given slot
	 * from the given side. Args: Slot, item, side
	 */
	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		return p_102008_3_ != 0 || p_102008_1_ != 1
				|| p_102008_2_.getItem() == Items.bucket;
	}

	/**
	 * Returns true if automation can insert the given item in the given slot
	 * from the given side. Args: Slot, item, side
	 */
	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_,
			int p_102007_3_) {
		return isItemValidForSlot(p_102007_1_, p_102007_2_);
	}

	@Override
	public void closeInventory() {
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (field_145957_n[p_70298_1_] != null) {
			ItemStack var3;

			if (field_145957_n[p_70298_1_].stackSize <= p_70298_2_) {
				var3 = field_145957_n[p_70298_1_];
				field_145957_n[p_70298_1_] = null;
				return var3;
			} else {
				var3 = field_145957_n[p_70298_1_].splitStack(p_70298_2_);

				if (field_145957_n[p_70298_1_].stackSize == 0) {
					field_145957_n[p_70298_1_] = null;
				}

				return var3;
			}
		} else
			return null;
	}

	private boolean func_145948_k() {
		if (field_145957_n[0] == null)
			return false;
		else {
			final ItemStack var1 = FurnaceRecipes.smelting().func_151395_a(
					field_145957_n[0]);
			return var1 == null ? false
					: field_145957_n[2] == null ? true
							: !field_145957_n[2].isItemEqual(var1) ? false
									: field_145957_n[2].stackSize < getInventoryStackLimit()
											&& field_145957_n[2].stackSize < field_145957_n[2]
													.getMaxStackSize() ? true
											: field_145957_n[2].stackSize < var1
													.getMaxStackSize();
		}
	}

	public void func_145949_j() {
		if (func_145948_k()) {
			final ItemStack var1 = FurnaceRecipes.smelting().func_151395_a(
					field_145957_n[0]);

			if (field_145957_n[2] == null) {
				field_145957_n[2] = var1.copy();
			} else if (field_145957_n[2].getItem() == var1.getItem()) {
				++field_145957_n[2].stackSize;
			}

			--field_145957_n[0].stackSize;

			if (field_145957_n[0].stackSize <= 0) {
				field_145957_n[0] = null;
			}
		}
	}

	public boolean func_145950_i() {
		return field_145956_a > 0;
	}

	public void func_145951_a(String p_145951_1_) {
		field_145958_o = p_145951_1_;
	}

	public int func_145953_d(int p_145953_1_) {
		return field_145961_j * p_145953_1_ / 200;
	}

	public int func_145955_e(int p_145955_1_) {
		if (field_145963_i == 0) {
			field_145963_i = 200;
		}

		return field_145956_a * p_145955_1_ / field_145963_i;
	}

	/**
	 * Returns an array containing the indices of the slots that can be accessed
	 * by automation on the given side of this block.
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return p_94128_1_ == 0 ? field_145959_l
				: p_94128_1_ == 1 ? field_145962_k : field_145960_m;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return isInventoryNameLocalized() ? field_145958_o
				: "container.furnace";
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return field_145957_n.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return field_145957_n[p_70301_1_];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (field_145957_n[p_70304_1_] != null) {
			final ItemStack var2 = field_145957_n[p_70304_1_];
			field_145957_n[p_70304_1_] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return field_145958_o != null && field_145958_o.length() > 0;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 2 ? false
				: p_94041_1_ == 1 ? func_145954_b(p_94041_2_) : true;
	}

	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		return worldObj.getTileEntity(field_145851_c, field_145848_d,
				field_145849_e) != this ? false : p_70300_1_.getDistanceSq(
				field_145851_c + 0.5D, field_145848_d + 0.5D,
				field_145849_e + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		final NBTTagList var2 = p_145839_1_.getTagList("Items", 10);
		field_145957_n = new ItemStack[getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < field_145957_n.length) {
				field_145957_n[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		field_145956_a = p_145839_1_.getShort("BurnTime");
		field_145961_j = p_145839_1_.getShort("CookTime");
		field_145963_i = func_145952_a(field_145957_n[1]);

		if (p_145839_1_.func_150297_b("CustomName", 8)) {
			field_145958_o = p_145839_1_.getString("CustomName");
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		field_145957_n[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null
				&& p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public void updateEntity() {
		final boolean var1 = field_145956_a > 0;
		boolean var2 = false;

		if (field_145956_a > 0) {
			--field_145956_a;
		}

		if (!worldObj.isClient) {
			if (field_145956_a != 0 || field_145957_n[1] != null
					&& field_145957_n[0] != null) {
				if (field_145956_a == 0 && func_145948_k()) {
					field_145963_i = field_145956_a = func_145952_a(field_145957_n[1]);

					if (field_145956_a > 0) {
						var2 = true;

						if (field_145957_n[1] != null) {
							--field_145957_n[1].stackSize;

							if (field_145957_n[1].stackSize == 0) {
								final Item var3 = field_145957_n[1].getItem()
										.getContainerItem();
								field_145957_n[1] = var3 != null ? new ItemStack(
										var3) : null;
							}
						}
					}
				}

				if (func_145950_i() && func_145948_k()) {
					++field_145961_j;

					if (field_145961_j == 200) {
						field_145961_j = 0;
						func_145949_j();
						var2 = true;
					}
				} else {
					field_145961_j = 0;
				}
			}

			if (var1 != field_145956_a > 0) {
				var2 = true;
				BlockFurnace.func_149931_a(field_145956_a > 0, worldObj,
						field_145851_c, field_145848_d, field_145849_e);
			}
		}

		if (var2) {
			onInventoryChanged();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setShort("BurnTime", (short) field_145956_a);
		p_145841_1_.setShort("CookTime", (short) field_145961_j);
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < field_145957_n.length; ++var3) {
			if (field_145957_n[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				field_145957_n[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		p_145841_1_.setTag("Items", var2);

		if (isInventoryNameLocalized()) {
			p_145841_1_.setString("CustomName", field_145958_o);
		}
	}
}

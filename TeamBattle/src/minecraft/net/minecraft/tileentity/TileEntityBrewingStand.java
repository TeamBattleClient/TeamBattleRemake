package net.minecraft.tileentity;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionHelper;

public class TileEntityBrewingStand extends TileEntity implements
		ISidedInventory {
	private static final int[] field_145941_a = new int[] { 3 };
	private static final int[] field_145947_i = new int[] { 0, 1, 2 };
	private String field_145942_n;
	private int field_145943_l;
	private Item field_145944_m;
	private ItemStack[] field_145945_j = new ItemStack[4];
	private int field_145946_k;

	/**
	 * Returns true if automation can extract the given item in the given slot
	 * from the given side. Args: Slot, item, side
	 */
	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_,
			int p_102008_3_) {
		return true;
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
		if (p_70298_1_ >= 0 && p_70298_1_ < field_145945_j.length) {
			final ItemStack var3 = field_145945_j[p_70298_1_];
			field_145945_j[p_70298_1_] = null;
			return var3;
		} else
			return null;
	}

	private boolean func_145934_k() {
		if (field_145945_j[3] != null && field_145945_j[3].stackSize > 0) {
			final ItemStack var1 = field_145945_j[3];

			if (!var1.getItem().isPotionIngredient(var1))
				return false;
			else {
				boolean var2 = false;

				for (int var3 = 0; var3 < 3; ++var3) {
					if (field_145945_j[var3] != null
							&& field_145945_j[var3].getItem() == Items.potionitem) {
						final int var4 = field_145945_j[var3].getItemDamage();
						final int var5 = func_145936_c(var4, var1);

						if (!ItemPotion.isSplash(var4)
								&& ItemPotion.isSplash(var5)) {
							var2 = true;
							break;
						}

						final List var6 = Items.potionitem.getEffects(var4);
						final List var7 = Items.potionitem.getEffects(var5);

						if ((var4 <= 0 || var6 != var7)
								&& (var6 == null || !var6.equals(var7)
										&& var7 != null) && var4 != var5) {
							var2 = true;
							break;
						}
					}
				}

				return var2;
			}
		} else
			return false;
	}

	public int func_145935_i() {
		return field_145946_k;
	}

	private int func_145936_c(int p_145936_1_, ItemStack p_145936_2_) {
		return p_145936_2_ == null ? p_145936_1_ : p_145936_2_.getItem()
				.isPotionIngredient(p_145936_2_) ? PotionHelper
				.applyIngredient(p_145936_1_, p_145936_2_.getItem()
						.getPotionEffect(p_145936_2_)) : p_145936_1_;
	}

	public void func_145937_a(String p_145937_1_) {
		field_145942_n = p_145937_1_;
	}

	public void func_145938_d(int p_145938_1_) {
		field_145946_k = p_145938_1_;
	}

	public int func_145939_j() {
		int var1 = 0;

		for (int var2 = 0; var2 < 3; ++var2) {
			if (field_145945_j[var2] != null) {
				var1 |= 1 << var2;
			}
		}

		return var1;
	}

	private void func_145940_l() {
		if (func_145934_k()) {
			final ItemStack var1 = field_145945_j[3];

			for (int var2 = 0; var2 < 3; ++var2) {
				if (field_145945_j[var2] != null
						&& field_145945_j[var2].getItem() == Items.potionitem) {
					final int var3 = field_145945_j[var2].getItemDamage();
					final int var4 = func_145936_c(var3, var1);
					final List var5 = Items.potionitem.getEffects(var3);
					final List var6 = Items.potionitem.getEffects(var4);

					if ((var3 <= 0 || var5 != var6)
							&& (var5 == null || !var5.equals(var6)
									&& var6 != null)) {
						if (var3 != var4) {
							field_145945_j[var2].setItemDamage(var4);
						}
					} else if (!ItemPotion.isSplash(var3)
							&& ItemPotion.isSplash(var4)) {
						field_145945_j[var2].setItemDamage(var4);
					}
				}
			}

			if (var1.getItem().hasContainerItem()) {
				field_145945_j[3] = new ItemStack(var1.getItem()
						.getContainerItem());
			} else {
				--field_145945_j[3].stackSize;

				if (field_145945_j[3].stackSize <= 0) {
					field_145945_j[3] = null;
				}
			}
		}
	}

	/**
	 * Returns an array containing the indices of the slots that can be accessed
	 * by automation on the given side of this block.
	 */
	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		return p_94128_1_ == 1 ? field_145941_a : field_145947_i;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return isInventoryNameLocalized() ? field_145942_n
				: "container.brewing";
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
		return field_145945_j.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return p_70301_1_ >= 0 && p_70301_1_ < field_145945_j.length ? field_145945_j[p_70301_1_]
				: null;
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (p_70304_1_ >= 0 && p_70304_1_ < field_145945_j.length) {
			final ItemStack var2 = field_145945_j[p_70304_1_];
			field_145945_j[p_70304_1_] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return field_145942_n != null && field_145942_n.length() > 0;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		return p_94041_1_ == 3 ? p_94041_2_.getItem().isPotionIngredient(
				p_94041_2_) : p_94041_2_.getItem() == Items.potionitem
				|| p_94041_2_.getItem() == Items.glass_bottle;
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
		field_145945_j = new ItemStack[getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < field_145945_j.length) {
				field_145945_j[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}

		field_145946_k = p_145839_1_.getShort("BrewTime");

		if (p_145839_1_.func_150297_b("CustomName", 8)) {
			field_145942_n = p_145839_1_.getString("CustomName");
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		if (p_70299_1_ >= 0 && p_70299_1_ < field_145945_j.length) {
			field_145945_j[p_70299_1_] = p_70299_2_;
		}
	}

	@Override
	public void updateEntity() {
		if (field_145946_k > 0) {
			--field_145946_k;

			if (field_145946_k == 0) {
				func_145940_l();
				onInventoryChanged();
			} else if (!func_145934_k()) {
				field_145946_k = 0;
				onInventoryChanged();
			} else if (field_145944_m != field_145945_j[3].getItem()) {
				field_145946_k = 0;
				onInventoryChanged();
			}
		} else if (func_145934_k()) {
			field_145946_k = 400;
			field_145944_m = field_145945_j[3].getItem();
		}

		final int var1 = func_145939_j();

		if (var1 != field_145943_l) {
			field_145943_l = var1;
			worldObj.setBlockMetadataWithNotify(field_145851_c, field_145848_d,
					field_145849_e, var1, 2);
		}

		super.updateEntity();
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		p_145841_1_.setShort("BrewTime", (short) field_145946_k);
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < field_145945_j.length; ++var3) {
			if (field_145945_j[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				field_145945_j[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		p_145841_1_.setTag("Items", var2);

		if (isInventoryNameLocalized()) {
			p_145841_1_.setString("CustomName", field_145942_n);
		}
	}
}

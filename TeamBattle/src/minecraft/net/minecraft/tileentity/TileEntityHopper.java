package net.minecraft.tileentity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHopper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityHopper extends TileEntity implements IHopper {
	public static IInventory func_145884_b(IHopper p_145884_0_) {
		return func_145893_b(p_145884_0_.getWorldObj(), p_145884_0_.getXPos(),
				p_145884_0_.getYPos() + 1.0D, p_145884_0_.getZPos());
	}

	private static boolean func_145885_a(IInventory p_145885_0_,
			ItemStack p_145885_1_, int p_145885_2_, int p_145885_3_) {
		return !p_145885_0_.isItemValidForSlot(p_145885_2_, p_145885_1_) ? false
				: !(p_145885_0_ instanceof ISidedInventory)
						|| ((ISidedInventory) p_145885_0_).canInsertItem(
								p_145885_2_, p_145885_1_, p_145885_3_);
	}

	public static ItemStack func_145889_a(IInventory p_145889_0_,
			ItemStack p_145889_1_, int p_145889_2_) {
		if (p_145889_0_ instanceof ISidedInventory && p_145889_2_ > -1) {
			final ISidedInventory var6 = (ISidedInventory) p_145889_0_;
			final int[] var7 = var6.getAccessibleSlotsFromSide(p_145889_2_);

			for (int var5 = 0; var5 < var7.length && p_145889_1_ != null
					&& p_145889_1_.stackSize > 0; ++var5) {
				p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_,
						var7[var5], p_145889_2_);
			}
		} else {
			final int var3 = p_145889_0_.getSizeInventory();

			for (int var4 = 0; var4 < var3 && p_145889_1_ != null
					&& p_145889_1_.stackSize > 0; ++var4) {
				p_145889_1_ = func_145899_c(p_145889_0_, p_145889_1_, var4,
						p_145889_2_);
			}
		}

		if (p_145889_1_ != null && p_145889_1_.stackSize == 0) {
			p_145889_1_ = null;
		}

		return p_145889_1_;
	}

	private static boolean func_145890_b(IInventory p_145890_0_,
			ItemStack p_145890_1_, int p_145890_2_, int p_145890_3_) {
		return !(p_145890_0_ instanceof ISidedInventory)
				|| ((ISidedInventory) p_145890_0_).canExtractItem(p_145890_2_,
						p_145890_1_, p_145890_3_);
	}

	public static boolean func_145891_a(IHopper p_145891_0_) {
		final IInventory var1 = func_145884_b(p_145891_0_);

		if (var1 != null) {
			final byte var2 = 0;

			if (func_152103_b(var1, var2))
				return false;

			if (var1 instanceof ISidedInventory && var2 > -1) {
				final ISidedInventory var7 = (ISidedInventory) var1;
				final int[] var8 = var7.getAccessibleSlotsFromSide(var2);

				for (final int element : var8) {
					if (func_145892_a(p_145891_0_, var1, element, var2))
						return true;
				}
			} else {
				final int var3 = var1.getSizeInventory();

				for (int var4 = 0; var4 < var3; ++var4) {
					if (func_145892_a(p_145891_0_, var1, var4, var2))
						return true;
				}
			}
		} else {
			final EntityItem var6 = func_145897_a(p_145891_0_.getWorldObj(),
					p_145891_0_.getXPos(), p_145891_0_.getYPos() + 1.0D,
					p_145891_0_.getZPos());

			if (var6 != null)
				return func_145898_a(p_145891_0_, var6);
		}

		return false;
	}

	private static boolean func_145892_a(IHopper p_145892_0_,
			IInventory p_145892_1_, int p_145892_2_, int p_145892_3_) {
		final ItemStack var4 = p_145892_1_.getStackInSlot(p_145892_2_);

		if (var4 != null
				&& func_145890_b(p_145892_1_, var4, p_145892_2_, p_145892_3_)) {
			final ItemStack var5 = var4.copy();
			final ItemStack var6 = func_145889_a(p_145892_0_,
					p_145892_1_.decrStackSize(p_145892_2_, 1), -1);

			if (var6 == null || var6.stackSize == 0) {
				p_145892_1_.onInventoryChanged();
				return true;
			}

			p_145892_1_.setInventorySlotContents(p_145892_2_, var5);
		}

		return false;
	}

	public static IInventory func_145893_b(World p_145893_0_,
			double p_145893_1_, double p_145893_3_, double p_145893_5_) {
		IInventory var7 = null;
		final int var8 = MathHelper.floor_double(p_145893_1_);
		final int var9 = MathHelper.floor_double(p_145893_3_);
		final int var10 = MathHelper.floor_double(p_145893_5_);
		final TileEntity var11 = p_145893_0_.getTileEntity(var8, var9, var10);

		if (var11 != null && var11 instanceof IInventory) {
			var7 = (IInventory) var11;

			if (var7 instanceof TileEntityChest) {
				final Block var12 = p_145893_0_.getBlock(var8, var9, var10);

				if (var12 instanceof BlockChest) {
					var7 = ((BlockChest) var12).func_149951_m(p_145893_0_,
							var8, var9, var10);
				}
			}
		}

		if (var7 == null) {
			final List var13 = p_145893_0_
					.getEntitiesWithinAABBExcludingEntity((Entity) null,
							AxisAlignedBB.getBoundingBox(p_145893_1_,
									p_145893_3_, p_145893_5_,
									p_145893_1_ + 1.0D, p_145893_3_ + 1.0D,
									p_145893_5_ + 1.0D),
							IEntitySelector.selectInventories);

			if (var13 != null && var13.size() > 0) {
				var7 = (IInventory) var13.get(p_145893_0_.rand.nextInt(var13
						.size()));
			}
		}

		return var7;
	}

	private static boolean func_145894_a(ItemStack p_145894_0_,
			ItemStack p_145894_1_) {
		return p_145894_0_.getItem() != p_145894_1_.getItem() ? false
				: p_145894_0_.getItemDamage() != p_145894_1_.getItemDamage() ? false
						: p_145894_0_.stackSize > p_145894_0_.getMaxStackSize() ? false
								: ItemStack.areItemStackTagsEqual(p_145894_0_,
										p_145894_1_);
	}

	public static EntityItem func_145897_a(World p_145897_0_,
			double p_145897_1_, double p_145897_3_, double p_145897_5_) {
		final List var7 = p_145897_0_.selectEntitiesWithinAABB(
				EntityItem.class, AxisAlignedBB.getBoundingBox(p_145897_1_,
						p_145897_3_, p_145897_5_, p_145897_1_ + 1.0D,
						p_145897_3_ + 1.0D, p_145897_5_ + 1.0D),
				IEntitySelector.selectAnything);
		return var7.size() > 0 ? (EntityItem) var7.get(0) : null;
	}

	public static boolean func_145898_a(IInventory p_145898_0_,
			EntityItem p_145898_1_) {
		boolean var2 = false;

		if (p_145898_1_ == null)
			return false;
		else {
			final ItemStack var3 = p_145898_1_.getEntityItem().copy();
			final ItemStack var4 = func_145889_a(p_145898_0_, var3, -1);

			if (var4 != null && var4.stackSize != 0) {
				p_145898_1_.setEntityItemStack(var4);
			} else {
				var2 = true;
				p_145898_1_.setDead();
			}

			return var2;
		}
	}

	private static ItemStack func_145899_c(IInventory p_145899_0_,
			ItemStack p_145899_1_, int p_145899_2_, int p_145899_3_) {
		final ItemStack var4 = p_145899_0_.getStackInSlot(p_145899_2_);

		if (func_145885_a(p_145899_0_, p_145899_1_, p_145899_2_, p_145899_3_)) {
			boolean var5 = false;

			if (var4 == null) {
				p_145899_0_.setInventorySlotContents(p_145899_2_, p_145899_1_);
				p_145899_1_ = null;
				var5 = true;
			} else if (func_145894_a(var4, p_145899_1_)) {
				final int var6 = p_145899_1_.getMaxStackSize() - var4.stackSize;
				final int var7 = Math.min(p_145899_1_.stackSize, var6);
				p_145899_1_.stackSize -= var7;
				var4.stackSize += var7;
				var5 = var7 > 0;
			}

			if (var5) {
				if (p_145899_0_ instanceof TileEntityHopper) {
					((TileEntityHopper) p_145899_0_).func_145896_c(8);
					p_145899_0_.onInventoryChanged();
				}

				p_145899_0_.onInventoryChanged();
			}
		}

		return p_145899_1_;
	}

	private static boolean func_152103_b(IInventory p_152103_0_, int p_152103_1_) {
		if (p_152103_0_ instanceof ISidedInventory && p_152103_1_ > -1) {
			final ISidedInventory var5 = (ISidedInventory) p_152103_0_;
			final int[] var6 = var5.getAccessibleSlotsFromSide(p_152103_1_);

			for (final int element : var6) {
				if (var5.getStackInSlot(element) != null)
					return false;
			}
		} else {
			final int var2 = p_152103_0_.getSizeInventory();

			for (int var3 = 0; var3 < var2; ++var3) {
				if (p_152103_0_.getStackInSlot(var3) != null)
					return false;
			}
		}

		return true;
	}

	private ItemStack[] field_145900_a = new ItemStack[5];

	private int field_145901_j = -1;

	private String field_145902_i;

	@Override
	public void closeInventory() {
	}

	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		if (field_145900_a[p_70298_1_] != null) {
			ItemStack var3;

			if (field_145900_a[p_70298_1_].stackSize <= p_70298_2_) {
				var3 = field_145900_a[p_70298_1_];
				field_145900_a[p_70298_1_] = null;
				return var3;
			} else {
				var3 = field_145900_a[p_70298_1_].splitStack(p_70298_2_);

				if (field_145900_a[p_70298_1_].stackSize == 0) {
					field_145900_a[p_70298_1_] = null;
				}

				return var3;
			}
		} else
			return null;
	}

	private boolean func_145883_k() {
		final IInventory var1 = func_145895_l();

		if (var1 == null)
			return false;
		else {
			final int var2 = Facing.oppositeSide[BlockHopper
					.func_149918_b(getBlockMetadata())];

			if (func_152102_a(var1, var2))
				return false;
			else {
				for (int var3 = 0; var3 < getSizeInventory(); ++var3) {
					if (getStackInSlot(var3) != null) {
						final ItemStack var4 = getStackInSlot(var3).copy();
						final ItemStack var5 = func_145889_a(var1,
								decrStackSize(var3, 1), var2);

						if (var5 == null || var5.stackSize == 0) {
							var1.onInventoryChanged();
							return true;
						}

						setInventorySlotContents(var3, var4);
					}
				}

				return false;
			}
		}
	}

	public void func_145886_a(String p_145886_1_) {
		field_145902_i = p_145886_1_;
	}

	public boolean func_145887_i() {
		if (worldObj != null && !worldObj.isClient) {
			if (!func_145888_j()
					&& BlockHopper.func_149917_c(getBlockMetadata())) {
				boolean var1 = false;

				if (!func_152104_k()) {
					var1 = func_145883_k();
				}

				if (!func_152105_l()) {
					var1 = func_145891_a(this) || var1;
				}

				if (var1) {
					func_145896_c(8);
					onInventoryChanged();
					return true;
				}
			}

			return false;
		} else
			return false;
	}

	public boolean func_145888_j() {
		return field_145901_j > 0;
	}

	private IInventory func_145895_l() {
		final int var1 = BlockHopper.func_149918_b(getBlockMetadata());
		return func_145893_b(getWorldObj(), field_145851_c
				+ Facing.offsetsXForSide[var1], field_145848_d
				+ Facing.offsetsYForSide[var1], field_145849_e
				+ Facing.offsetsZForSide[var1]);
	}

	public void func_145896_c(int p_145896_1_) {
		field_145901_j = p_145896_1_;
	}

	private boolean func_152102_a(IInventory p_152102_1_, int p_152102_2_) {
		if (p_152102_1_ instanceof ISidedInventory && p_152102_2_ > -1) {
			final ISidedInventory var7 = (ISidedInventory) p_152102_1_;
			final int[] var8 = var7.getAccessibleSlotsFromSide(p_152102_2_);

			for (final int element : var8) {
				final ItemStack var6 = var7.getStackInSlot(element);

				if (var6 == null || var6.stackSize != var6.getMaxStackSize())
					return false;
			}
		} else {
			final int var3 = p_152102_1_.getSizeInventory();

			for (int var4 = 0; var4 < var3; ++var4) {
				final ItemStack var5 = p_152102_1_.getStackInSlot(var4);

				if (var5 == null || var5.stackSize != var5.getMaxStackSize())
					return false;
			}
		}

		return true;
	}

	private boolean func_152104_k() {
		final ItemStack[] var1 = field_145900_a;
		final int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			final ItemStack var4 = var1[var3];

			if (var4 != null)
				return false;
		}

		return true;
	}

	private boolean func_152105_l() {
		final ItemStack[] var1 = field_145900_a;
		final int var2 = var1.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			final ItemStack var4 = var1[var3];

			if (var4 == null || var4.stackSize != var4.getMaxStackSize())
				return false;
		}

		return true;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return isInventoryNameLocalized() ? field_145902_i : "container.hopper";
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
		return field_145900_a.length;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return field_145900_a[p_70301_1_];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (field_145900_a[p_70304_1_] != null) {
			final ItemStack var2 = field_145900_a[p_70304_1_];
			field_145900_a[p_70304_1_] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * Gets the world X position for this hopper entity.
	 */
	@Override
	public double getXPos() {
		return field_145851_c;
	}

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	@Override
	public double getYPos() {
		return field_145848_d;
	}

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	@Override
	public double getZPos() {
		return field_145849_e;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	@Override
	public boolean isInventoryNameLocalized() {
		return field_145902_i != null && field_145902_i.length() > 0;
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
		return worldObj.getTileEntity(field_145851_c, field_145848_d,
				field_145849_e) != this ? false : p_70300_1_.getDistanceSq(
				field_145851_c + 0.5D, field_145848_d + 0.5D,
				field_145849_e + 0.5D) <= 64.0D;
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	@Override
	public void onInventoryChanged() {
		super.onInventoryChanged();
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void readFromNBT(NBTTagCompound p_145839_1_) {
		super.readFromNBT(p_145839_1_);
		final NBTTagList var2 = p_145839_1_.getTagList("Items", 10);
		field_145900_a = new ItemStack[getSizeInventory()];

		if (p_145839_1_.func_150297_b("CustomName", 8)) {
			field_145902_i = p_145839_1_.getString("CustomName");
		}

		field_145901_j = p_145839_1_.getInteger("TransferCooldown");

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final byte var5 = var4.getByte("Slot");

			if (var5 >= 0 && var5 < field_145900_a.length) {
				field_145900_a[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		field_145900_a[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null
				&& p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public void updateEntity() {
		if (worldObj != null && !worldObj.isClient) {
			--field_145901_j;

			if (!func_145888_j()) {
				func_145896_c(0);
				func_145887_i();
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound p_145841_1_) {
		super.writeToNBT(p_145841_1_);
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < field_145900_a.length; ++var3) {
			if (field_145900_a[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				field_145900_a[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		p_145841_1_.setTag("Items", var2);
		p_145841_1_.setInteger("TransferCooldown", field_145901_j);

		if (isInventoryNameLocalized()) {
			p_145841_1_.setString("CustomName", field_145902_i);
		}
	}
}

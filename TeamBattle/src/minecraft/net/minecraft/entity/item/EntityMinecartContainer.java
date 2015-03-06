package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityMinecartContainer extends EntityMinecart implements
		IInventory {
	/**
	 * When set to true, the minecart will drop all items when setDead() is
	 * called. When false (such as when travelling dimensions) it preserves its
	 * contents.
	 */
	private boolean dropContentsWhenDead = true;

	private ItemStack[] minecartContainerItems = new ItemStack[36];

	public EntityMinecartContainer(World p_i1716_1_) {
		super(p_i1716_1_);
	}

	public EntityMinecartContainer(World p_i1717_1_, double p_i1717_2_,
			double p_i1717_4_, double p_i1717_6_) {
		super(p_i1717_1_, p_i1717_2_, p_i1717_4_, p_i1717_6_);
	}

	@Override
	protected void applyDrag() {
		final int var1 = 15 - Container.calcRedstoneFromInventory(this);
		final float var2 = 0.98F + var1 * 0.001F;
		motionX *= var2;
		motionY *= 0.0D;
		motionZ *= var2;
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
		if (minecartContainerItems[p_70298_1_] != null) {
			ItemStack var3;

			if (minecartContainerItems[p_70298_1_].stackSize <= p_70298_2_) {
				var3 = minecartContainerItems[p_70298_1_];
				minecartContainerItems[p_70298_1_] = null;
				return var3;
			} else {
				var3 = minecartContainerItems[p_70298_1_]
						.splitStack(p_70298_2_);

				if (minecartContainerItems[p_70298_1_].stackSize == 0) {
					minecartContainerItems[p_70298_1_] = null;
				}

				return var3;
			}
		} else
			return null;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		return isInventoryNameLocalized() ? func_95999_t()
				: "container.minecart";
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		return minecartContainerItems[p_70301_1_];
	}

	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem - like when you close a workbench
	 * GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		if (minecartContainerItems[p_70304_1_] != null) {
			final ItemStack var2 = minecartContainerItems[p_70304_1_];
			minecartContainerItems[p_70304_1_] = null;
			return var2;
		} else
			return null;
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (!worldObj.isClient) {
			p_130002_1_.displayGUIChest(this);
		}

		return true;
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
		return isDead ? false : p_70300_1_.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);

		for (int var2 = 0; var2 < getSizeInventory(); ++var2) {
			final ItemStack var3 = getStackInSlot(var2);

			if (var3 != null) {
				final float var4 = rand.nextFloat() * 0.8F + 0.1F;
				final float var5 = rand.nextFloat() * 0.8F + 0.1F;
				final float var6 = rand.nextFloat() * 0.8F + 0.1F;

				while (var3.stackSize > 0) {
					int var7 = rand.nextInt(21) + 10;

					if (var7 > var3.stackSize) {
						var7 = var3.stackSize;
					}

					var3.stackSize -= var7;
					final EntityItem var8 = new EntityItem(worldObj, posX
							+ var4, posY + var5, posZ + var6, new ItemStack(
							var3.getItem(), var7, var3.getItemDamage()));
					final float var9 = 0.05F;
					var8.motionX = (float) rand.nextGaussian() * var9;
					var8.motionY = (float) rand.nextGaussian() * var9 + 0.2F;
					var8.motionZ = (float) rand.nextGaussian() * var9;
					worldObj.spawnEntityInWorld(var8);
				}
			}
		}
	}

	/**
	 * Called when an the contents of an Inventory change, usually
	 */
	@Override
	public void onInventoryChanged() {
	}

	@Override
	public void openInventory() {
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		final NBTTagList var2 = p_70037_1_.getTagList("Items", 10);
		minecartContainerItems = new ItemStack[getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			final int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < minecartContainerItems.length) {
				minecartContainerItems[var5] = ItemStack
						.loadItemStackFromNBT(var4);
			}
		}
	}

	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void setDead() {
		if (dropContentsWhenDead) {
			for (int var1 = 0; var1 < getSizeInventory(); ++var1) {
				final ItemStack var2 = getStackInSlot(var1);

				if (var2 != null) {
					final float var3 = rand.nextFloat() * 0.8F + 0.1F;
					final float var4 = rand.nextFloat() * 0.8F + 0.1F;
					final float var5 = rand.nextFloat() * 0.8F + 0.1F;

					while (var2.stackSize > 0) {
						int var6 = rand.nextInt(21) + 10;

						if (var6 > var2.stackSize) {
							var6 = var2.stackSize;
						}

						var2.stackSize -= var6;
						final EntityItem var7 = new EntityItem(worldObj, posX
								+ var3, posY + var4, posZ + var5,
								new ItemStack(var2.getItem(), var6,
										var2.getItemDamage()));

						if (var2.hasTagCompound()) {
							var7.getEntityItem().setTagCompound(
									(NBTTagCompound) var2.getTagCompound()
											.copy());
						}

						final float var8 = 0.05F;
						var7.motionX = (float) rand.nextGaussian() * var8;
						var7.motionY = (float) rand.nextGaussian() * var8
								+ 0.2F;
						var7.motionZ = (float) rand.nextGaussian() * var8;
						worldObj.spawnEntityInWorld(var7);
					}
				}
			}
		}

		super.setDead();
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		minecartContainerItems[p_70299_1_] = p_70299_2_;

		if (p_70299_2_ != null
				&& p_70299_2_.stackSize > getInventoryStackLimit()) {
			p_70299_2_.stackSize = getInventoryStackLimit();
		}
	}

	/**
	 * Teleports the entity to another dimension. Params: Dimension number to
	 * teleport to
	 */
	@Override
	public void travelToDimension(int p_71027_1_) {
		dropContentsWhenDead = false;
		super.travelToDimension(p_71027_1_);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		final NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < minecartContainerItems.length; ++var3) {
			if (minecartContainerItems[var3] != null) {
				final NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte) var3);
				minecartContainerItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		p_70014_1_.setTag("Items", var2);
	}
}

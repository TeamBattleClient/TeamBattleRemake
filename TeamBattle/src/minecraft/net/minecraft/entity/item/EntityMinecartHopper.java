package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements
		IHopper {
	/** Whether this hopper minecart is being blocked by an activator rail. */
	private boolean isBlocked = true;
	private int transferTicker = -1;

	public EntityMinecartHopper(World p_i1720_1_) {
		super(p_i1720_1_);
	}

	public EntityMinecartHopper(World p_i1721_1_, double p_i1721_2_,
			double p_i1721_4_, double p_i1721_6_) {
		super(p_i1721_1_, p_i1721_2_, p_i1721_4_, p_i1721_6_);
	}

	/**
	 * Returns whether the hopper cart can currently transfer an item.
	 */
	public boolean canTransfer() {
		return transferTicker > 0;
	}

	@Override
	public Block func_145817_o() {
		return Blocks.hopper;
	}

	public boolean func_96112_aD() {
		if (TileEntityHopper.func_145891_a(this))
			return true;
		else {
			final List var1 = worldObj.selectEntitiesWithinAABB(
					EntityItem.class, boundingBox.expand(0.25D, 0.0D, 0.25D),
					IEntitySelector.selectAnything);

			if (var1.size() > 0) {
				TileEntityHopper.func_145898_a(this, (EntityItem) var1.get(0));
			}

			return false;
		}
	}

	/**
	 * Get whether this hopper minecart is being blocked by an activator rail.
	 */
	public boolean getBlocked() {
		return isBlocked;
	}

	@Override
	public int getDefaultDisplayTileOffset() {
		return 1;
	}

	@Override
	public int getMinecartType() {
		return 5;
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return 5;
	}

	/**
	 * Returns the worldObj for this tileEntity.
	 */
	@Override
	public World getWorldObj() {
		return worldObj;
	}

	/**
	 * Gets the world X position for this hopper entity.
	 */
	@Override
	public double getXPos() {
		return posX;
	}

	/**
	 * Gets the world Y position for this hopper entity.
	 */
	@Override
	public double getYPos() {
		return posY;
	}

	/**
	 * Gets the world Z position for this hopper entity.
	 */
	@Override
	public double getZPos() {
		return posZ;
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		if (!worldObj.isClient) {
			p_130002_1_.displayGUIHopperMinecart(this);
		}

		return true;
	}

	@Override
	public void killMinecart(DamageSource p_94095_1_) {
		super.killMinecart(p_94095_1_);
		func_145778_a(Item.getItemFromBlock(Blocks.hopper), 1, 0.0F);
	}

	/**
	 * Called every tick the minecart is on an activator rail. Args: x, y, z, is
	 * the rail receiving power
	 */
	@Override
	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_,
			int p_96095_3_, boolean p_96095_4_) {
		final boolean var5 = !p_96095_4_;

		if (var5 != getBlocked()) {
			setBlocked(var5);
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isClient && isEntityAlive() && getBlocked()) {
			--transferTicker;

			if (!canTransfer()) {
				setTransferTicker(0);

				if (func_96112_aD()) {
					setTransferTicker(4);
					onInventoryChanged();
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		transferTicker = p_70037_1_.getInteger("TransferCooldown");
	}

	/**
	 * Set whether this hopper minecart is being blocked by an activator rail.
	 */
	public void setBlocked(boolean p_96110_1_) {
		isBlocked = p_96110_1_;
	}

	/**
	 * Sets the transfer ticker, used to determine the delay between transfers.
	 */
	public void setTransferTicker(int p_98042_1_) {
		transferTicker = p_98042_1_;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("TransferCooldown", transferTicker);
	}
}

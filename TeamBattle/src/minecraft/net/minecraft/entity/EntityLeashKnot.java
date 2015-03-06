package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging {

	public static EntityLeashKnot func_110129_a(World p_110129_0_,
			int p_110129_1_, int p_110129_2_, int p_110129_3_) {
		final EntityLeashKnot var4 = new EntityLeashKnot(p_110129_0_,
				p_110129_1_, p_110129_2_, p_110129_3_);
		var4.forceSpawn = true;
		p_110129_0_.spawnEntityInWorld(var4);
		return var4;
	}

	public static EntityLeashKnot getKnotForBlock(World p_110130_0_,
			int p_110130_1_, int p_110130_2_, int p_110130_3_) {
		final List var4 = p_110130_0_.getEntitiesWithinAABB(
				EntityLeashKnot.class, AxisAlignedBB.getBoundingBox(
						p_110130_1_ - 1.0D, p_110130_2_ - 1.0D,
						p_110130_3_ - 1.0D, p_110130_1_ + 1.0D,
						p_110130_2_ + 1.0D, p_110130_3_ + 1.0D));

		if (var4 != null) {
			final Iterator var5 = var4.iterator();

			while (var5.hasNext()) {
				final EntityLeashKnot var6 = (EntityLeashKnot) var5.next();

				if (var6.field_146063_b == p_110130_1_
						&& var6.field_146064_c == p_110130_2_
						&& var6.field_146062_d == p_110130_3_)
					return var6;
			}
		}

		return null;
	}

	public EntityLeashKnot(World p_i1592_1_) {
		super(p_i1592_1_);
	}

	public EntityLeashKnot(World p_i1593_1_, int p_i1593_2_, int p_i1593_3_,
			int p_i1593_4_) {
		super(p_i1593_1_, p_i1593_2_, p_i1593_3_, p_i1593_4_, 0);
		setPosition(p_i1593_2_ + 0.5D, p_i1593_3_ + 0.5D, p_i1593_4_ + 0.5D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public int getHeightPixels() {
		return 9;
	}

	@Override
	public int getWidthPixels() {
		return 9;
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		final ItemStack var2 = p_130002_1_.getHeldItem();
		boolean var3 = false;
		double var4;
		List var6;
		Iterator var7;
		EntityLiving var8;

		if (var2 != null && var2.getItem() == Items.lead && !worldObj.isClient) {
			var4 = 7.0D;
			var6 = worldObj.getEntitiesWithinAABB(
					EntityLiving.class,
					AxisAlignedBB.getBoundingBox(posX - var4, posY - var4, posZ
							- var4, posX + var4, posY + var4, posZ + var4));

			if (var6 != null) {
				var7 = var6.iterator();

				while (var7.hasNext()) {
					var8 = (EntityLiving) var7.next();

					if (var8.getLeashed()
							&& var8.getLeashedToEntity() == p_130002_1_) {
						var8.setLeashedToEntity(this, true);
						var3 = true;
					}
				}
			}
		}

		if (!worldObj.isClient && !var3) {
			setDead();

			if (p_130002_1_.capabilities.isCreativeMode) {
				var4 = 7.0D;
				var6 = worldObj.getEntitiesWithinAABB(EntityLiving.class,
						AxisAlignedBB.getBoundingBox(posX - var4, posY - var4,
								posZ - var4, posX + var4, posY + var4, posZ
										+ var4));

				if (var6 != null) {
					var7 = var6.iterator();

					while (var7.hasNext()) {
						var8 = (EntityLiving) var7.next();

						if (var8.getLeashed()
								&& var8.getLeashedToEntity() == this) {
							var8.clearLeashed(true, false);
						}
					}
				}
			}
		}

		return true;
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	@Override
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return p_70112_1_ < 1024.0D;
	}

	/**
	 * Called when this entity is broken. Entity parameter may be null.
	 */
	@Override
	public void onBroken(Entity p_110128_1_) {
	}

	/**
	 * checks to make sure painting can be placed there
	 */
	@Override
	public boolean onValidSurface() {
		return worldObj
				.getBlock(field_146063_b, field_146064_c, field_146062_d)
				.getRenderType() == 11;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	public void setDirection(int p_82328_1_) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}

	/**
	 * Either write this entity to the NBT tag given and return true, or return
	 * false without doing anything. If this returns false the entity is not
	 * saved on disk. Ridden entities return false here as they are saved with
	 * their rider.
	 */
	@Override
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		return false;
	}
}

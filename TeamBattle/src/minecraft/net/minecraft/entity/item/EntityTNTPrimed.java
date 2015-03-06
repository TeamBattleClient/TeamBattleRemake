package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityTNTPrimed extends Entity {
	/** How long the fuse is */
	public int fuse;
	private EntityLivingBase tntPlacedBy;

	public EntityTNTPrimed(World p_i1729_1_) {
		super(p_i1729_1_);
		preventEntitySpawning = true;
		setSize(0.98F, 0.98F);
		yOffset = height / 2.0F;
	}

	public EntityTNTPrimed(World p_i1730_1_, double p_i1730_2_,
			double p_i1730_4_, double p_i1730_6_, EntityLivingBase p_i1730_8_) {
		this(p_i1730_1_);
		setPosition(p_i1730_2_, p_i1730_4_, p_i1730_6_);
		final float var9 = (float) (Math.random() * Math.PI * 2.0D);
		motionX = -((float) Math.sin(var9)) * 0.02F;
		motionY = 0.20000000298023224D;
		motionZ = -((float) Math.cos(var9)) * 0.02F;
		fuse = 80;
		prevPosX = p_i1730_2_;
		prevPosY = p_i1730_4_;
		prevPosZ = p_i1730_6_;
		tntPlacedBy = p_i1730_8_;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
	}

	private void explode() {
		final float var1 = 4.0F;
		worldObj.createExplosion(this, posX, posY, posZ, var1, true);
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * returns null or the entityliving it was placed or ignited by
	 */
	public EntityLivingBase getTntPlacedBy() {
		return tntPlacedBy;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= 0.03999999910593033D;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (onGround) {
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
			motionY *= -0.5D;
		}

		if (fuse-- <= 0) {
			setDead();

			if (!worldObj.isClient) {
				explode();
			}
		} else {
			worldObj.spawnParticle("smoke", posX, posY + 0.5D, posZ, 0.0D,
					0.0D, 0.0D);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		fuse = p_70037_1_.getByte("Fuse");
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setByte("Fuse", (byte) fuse);
	}
}

package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityFireworkRocket extends Entity {
	/** The age of the firework in ticks. */
	private int fireworkAge;

	/**
	 * The lifetime of the firework in ticks. When the age reaches the lifetime
	 * the firework explodes.
	 */
	private int lifetime;

	public EntityFireworkRocket(World p_i1762_1_) {
		super(p_i1762_1_);
		setSize(0.25F, 0.25F);
	}

	public EntityFireworkRocket(World p_i1763_1_, double p_i1763_2_,
			double p_i1763_4_, double p_i1763_6_, ItemStack p_i1763_8_) {
		super(p_i1763_1_);
		fireworkAge = 0;
		setSize(0.25F, 0.25F);
		setPosition(p_i1763_2_, p_i1763_4_, p_i1763_6_);
		yOffset = 0.0F;
		int var9 = 1;

		if (p_i1763_8_ != null && p_i1763_8_.hasTagCompound()) {
			dataWatcher.updateObject(8, p_i1763_8_);
			final NBTTagCompound var10 = p_i1763_8_.getTagCompound();
			final NBTTagCompound var11 = var10.getCompoundTag("Fireworks");

			if (var11 != null) {
				var9 += var11.getByte("Flight");
			}
		}

		motionX = rand.nextGaussian() * 0.001D;
		motionZ = rand.nextGaussian() * 0.001D;
		motionY = 0.05D;
		lifetime = 10 * var9 + rand.nextInt(6) + rand.nextInt(7);
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObjectByDataType(8, 5);
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return super.getBrightness(p_70013_1_);
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return super.getBrightnessForRender(p_70070_1_);
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 17 && worldObj.isClient) {
			final ItemStack var2 = dataWatcher.getWatchableObjectItemStack(8);
			NBTTagCompound var3 = null;

			if (var2 != null && var2.hasTagCompound()) {
				var3 = var2.getTagCompound().getCompoundTag("Fireworks");
			}

			worldObj.makeFireworks(posX, posY, posZ, motionX, motionY, motionZ,
					var3);
		}

		super.handleHealthUpdate(p_70103_1_);
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	@Override
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		return p_70112_1_ < 4096.0D;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		lastTickPosX = posX;
		lastTickPosY = posY;
		lastTickPosZ = posZ;
		super.onUpdate();
		motionX *= 1.15D;
		motionZ *= 1.15D;
		motionY += 0.04D;
		moveEntity(motionX, motionY, motionZ);
		final float var1 = MathHelper.sqrt_double(motionX * motionX + motionZ
				* motionZ);
		rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for (rotationPitch = (float) (Math.atan2(motionY, var1) * 180.0D / Math.PI); rotationPitch
				- prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
			;
		}

		while (rotationPitch - prevRotationPitch >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while (rotationYaw - prevRotationYaw < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch)
				* 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;

		if (fireworkAge == 0) {
			worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0F, 1.0F);
		}

		++fireworkAge;

		if (worldObj.isClient && fireworkAge % 2 < 2) {
			worldObj.spawnParticle("fireworksSpark", posX, posY - 0.3D, posZ,
					rand.nextGaussian() * 0.05D, -motionY * 0.5D,
					rand.nextGaussian() * 0.05D);
		}

		if (!worldObj.isClient && fireworkAge > lifetime) {
			worldObj.setEntityState(this, (byte) 17);
			setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		fireworkAge = p_70037_1_.getInteger("Life");
		lifetime = p_70037_1_.getInteger("LifeTime");
		final NBTTagCompound var2 = p_70037_1_.getCompoundTag("FireworksItem");

		if (var2 != null) {
			final ItemStack var3 = ItemStack.loadItemStackFromNBT(var2);

			if (var3 != null) {
				dataWatcher.updateObject(8, var3);
			}
		}
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@Override
	public void setVelocity(double p_70016_1_, double p_70016_3_,
			double p_70016_5_) {
		motionX = p_70016_1_;
		motionY = p_70016_3_;
		motionZ = p_70016_5_;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			final float var7 = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_
					+ p_70016_5_ * p_70016_5_);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(p_70016_1_,
					p_70016_5_) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(p_70016_3_,
					var7) * 180.0D / Math.PI);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setInteger("Life", fireworkAge);
		p_70014_1_.setInteger("LifeTime", lifetime);
		final ItemStack var2 = dataWatcher.getWatchableObjectItemStack(8);

		if (var2 != null) {
			final NBTTagCompound var3 = new NBTTagCompound();
			var2.writeToNBT(var3);
			p_70014_1_.setTag("FireworksItem", var3);
		}
	}
}

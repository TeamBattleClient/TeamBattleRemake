package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
	private int despawnTimer;

	private boolean shatterOrDrop;

	/** 'x' location the eye should float towards. */
	private double targetX;
	/** 'y' location the eye should float towards. */
	private double targetY;
	/** 'z' location the eye should float towards. */
	private double targetZ;

	public EntityEnderEye(World p_i1757_1_) {
		super(p_i1757_1_);
		setSize(0.25F, 0.25F);
	}

	public EntityEnderEye(World p_i1758_1_, double p_i1758_2_,
			double p_i1758_4_, double p_i1758_6_) {
		super(p_i1758_1_);
		despawnTimer = 0;
		setSize(0.25F, 0.25F);
		setPosition(p_i1758_2_, p_i1758_4_, p_i1758_6_);
		yOffset = 0.0F;
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
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	@Override
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double var3 = boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return p_70112_1_ < var3 * var3;
	}

	/**
	 * The location the eye should float/move towards. Currently used for moving
	 * towards the nearest stronghold. Args: strongholdX, strongholdY,
	 * strongholdZ
	 */
	public void moveTowards(double p_70220_1_, int p_70220_3_, double p_70220_4_) {
		final double var6 = p_70220_1_ - posX;
		final double var8 = p_70220_4_ - posZ;
		final float var10 = MathHelper.sqrt_double(var6 * var6 + var8 * var8);

		if (var10 > 12.0F) {
			targetX = posX + var6 / var10 * 12.0D;
			targetZ = posZ + var8 / var10 * 12.0D;
			targetY = posY + 8.0D;
		} else {
			targetX = p_70220_1_;
			targetY = p_70220_3_;
			targetZ = p_70220_4_;
		}

		despawnTimer = 0;
		shatterOrDrop = rand.nextInt(5) > 0;
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
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
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

		if (!worldObj.isClient) {
			final double var2 = targetX - posX;
			final double var4 = targetZ - posZ;
			final float var6 = (float) Math.sqrt(var2 * var2 + var4 * var4);
			final float var7 = (float) Math.atan2(var4, var2);
			double var8 = var1 + (var6 - var1) * 0.0025D;

			if (var6 < 1.0F) {
				var8 *= 0.8D;
				motionY *= 0.8D;
			}

			motionX = Math.cos(var7) * var8;
			motionZ = Math.sin(var7) * var8;

			if (posY < targetY) {
				motionY += (1.0D - motionY) * 0.014999999664723873D;
			} else {
				motionY += (-1.0D - motionY) * 0.014999999664723873D;
			}
		}

		final float var10 = 0.25F;

		if (isInWater()) {
			for (int var3 = 0; var3 < 4; ++var3) {
				worldObj.spawnParticle("bubble", posX - motionX * var10, posY
						- motionY * var10, posZ - motionZ * var10, motionX,
						motionY, motionZ);
			}
		} else {
			worldObj.spawnParticle("portal",
					posX - motionX * var10 + rand.nextDouble() * 0.6D - 0.3D,
					posY - motionY * var10 - 0.5D, posZ - motionZ * var10
							+ rand.nextDouble() * 0.6D - 0.3D, motionX,
					motionY, motionZ);
		}

		if (!worldObj.isClient) {
			setPosition(posX, posY, posZ);
			++despawnTimer;

			if (despawnTimer > 80 && !worldObj.isClient) {
				setDead();

				if (shatterOrDrop) {
					worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX,
							posY, posZ, new ItemStack(Items.ender_eye)));
				} else {
					worldObj.playAuxSFX(2003, (int) Math.round(posX),
							(int) Math.round(posY), (int) Math.round(posZ), 0);
				}
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
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
	}
}

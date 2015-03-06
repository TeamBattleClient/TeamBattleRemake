package net.minecraft.entity;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving {

	public EntityFlying(World p_i1587_1_) {
		super(p_i1587_1_);
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder() {
		return false;
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		if (isInWater()) {
			moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929D;
			motionY *= 0.800000011920929D;
			motionZ *= 0.800000011920929D;
		} else if (handleLavaMovement()) {
			moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		} else {
			float var3 = 0.91F;

			if (onGround) {
				var3 = worldObj.getBlock(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			final float var4 = 0.16277136F / (var3 * var3 * var3);
			moveFlying(p_70612_1_, p_70612_2_, onGround ? 0.1F * var4 : 0.02F);
			var3 = 0.91F;

			if (onGround) {
				var3 = worldObj.getBlock(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			moveEntity(motionX, motionY, motionZ);
			motionX *= var3;
			motionY *= var3;
			motionZ *= var3;
		}

		prevLimbSwingAmount = limbSwingAmount;
		final double var8 = posX - prevPosX;
		final double var5 = posZ - prevPosZ;
		float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0F;

		if (var7 > 1.0F) {
			var7 = 1.0F;
		}

		limbSwingAmount += (var7 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on
	 * the ground to update the fall distance and deal fall damage if landing on
	 * the ground. Args: distanceFallenThisTick, onGround
	 */
	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
	}
}

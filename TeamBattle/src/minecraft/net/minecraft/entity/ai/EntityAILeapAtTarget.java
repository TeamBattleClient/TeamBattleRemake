package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class EntityAILeapAtTarget extends EntityAIBase {
	/** The entity that is leaping. */
	EntityLiving leaper;

	/** The entity's motionY after leaping. */
	float leapMotionY;

	/** The entity that the leaper is leaping towards. */
	EntityLivingBase leapTarget;

	public EntityAILeapAtTarget(EntityLiving p_i1630_1_, float p_i1630_2_) {
		leaper = p_i1630_1_;
		leapMotionY = p_i1630_2_;
		setMutexBits(5);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !leaper.onGround;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		leapTarget = leaper.getAttackTarget();

		if (leapTarget == null)
			return false;
		else {
			final double var1 = leaper.getDistanceSqToEntity(leapTarget);
			return var1 >= 4.0D && var1 <= 16.0D ? !leaper.onGround ? false
					: leaper.getRNG().nextInt(5) == 0 : false;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		final double var1 = leapTarget.posX - leaper.posX;
		final double var3 = leapTarget.posZ - leaper.posZ;
		final float var5 = MathHelper.sqrt_double(var1 * var1 + var3 * var3);
		leaper.motionX += var1 / var5 * 0.5D * 0.800000011920929D
				+ leaper.motionX * 0.20000000298023224D;
		leaper.motionZ += var3 / var5 * 0.5D * 0.800000011920929D
				+ leaper.motionZ * 0.20000000298023224D;
		leaper.motionY = leapMotionY;
	}
}

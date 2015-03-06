package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAILookIdle extends EntityAIBase {
	/** The entity that is looking idle. */
	private final EntityLiving idleEntity;

	/**
	 * A decrementing tick that stops the entity from being idle once it reaches
	 * 0.
	 */
	private int idleTime;

	/** X offset to look at */
	private double lookX;

	/** Z offset to look at */
	private double lookZ;

	public EntityAILookIdle(EntityLiving p_i1647_1_) {
		idleEntity = p_i1647_1_;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return idleTime >= 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return idleEntity.getRNG().nextFloat() < 0.02F;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		final double var1 = Math.PI * 2D * idleEntity.getRNG().nextDouble();
		lookX = Math.cos(var1);
		lookZ = Math.sin(var1);
		idleTime = 20 + idleEntity.getRNG().nextInt(20);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		--idleTime;
		idleEntity.getLookHelper().setLookPosition(idleEntity.posX + lookX,
				idleEntity.posY + idleEntity.getEyeHeight(),
				idleEntity.posZ + lookZ, 10.0F,
				idleEntity.getVerticalFaceSpeed());
	}
}

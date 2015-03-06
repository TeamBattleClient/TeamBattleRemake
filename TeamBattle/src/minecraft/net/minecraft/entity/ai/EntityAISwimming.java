package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAISwimming extends EntityAIBase {
	private final EntityLiving theEntity;

	public EntityAISwimming(EntityLiving p_i1624_1_) {
		theEntity = p_i1624_1_;
		setMutexBits(4);
		p_i1624_1_.getNavigator().setCanSwim(true);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return theEntity.isInWater() || theEntity.handleLavaMovement();
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		if (theEntity.getRNG().nextFloat() < 0.8F) {
			theEntity.getJumpHelper().setJumping();
		}
	}
}

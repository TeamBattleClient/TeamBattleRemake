package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;

public class EntityAIRestrictSun extends EntityAIBase {
	private final EntityCreature theEntity;

	public EntityAIRestrictSun(EntityCreature p_i1652_1_) {
		theEntity = p_i1652_1_;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		theEntity.getNavigator().setAvoidSun(false);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return theEntity.worldObj.isDaytime();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		theEntity.getNavigator().setAvoidSun(true);
	}
}

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIOpenDoor extends EntityAIDoorInteract {
	int field_75360_j;
	boolean field_75361_i;

	public EntityAIOpenDoor(EntityLiving p_i1644_1_, boolean p_i1644_2_) {
		super(p_i1644_1_);
		theEntity = p_i1644_1_;
		field_75361_i = p_i1644_2_;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return field_75361_i && field_75360_j > 0 && super.continueExecuting();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		if (field_75361_i) {
			field_151504_e.func_150014_a(theEntity.worldObj, entityPosX,
					entityPosY, entityPosZ, false);
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		field_75360_j = 20;
		field_151504_e.func_150014_a(theEntity.worldObj, entityPosX,
				entityPosY, entityPosZ, true);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		--field_75360_j;
		super.updateTask();
	}
}

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase {
	/** If the EntityTameable is sitting. */
	private boolean isSitting;

	private final EntityTameable theEntity;

	public EntityAISit(EntityTameable p_i1654_1_) {
		theEntity = p_i1654_1_;
		setMutexBits(5);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		theEntity.setSitting(false);
	}

	/**
	 * Sets the sitting flag.
	 */
	public void setSitting(boolean p_75270_1_) {
		isSitting = p_75270_1_;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theEntity.isTamed())
			return false;
		else if (theEntity.isInWater())
			return false;
		else if (!theEntity.onGround)
			return false;
		else {
			final EntityLivingBase var1 = theEntity.getOwner();
			return var1 == null ? true
					: theEntity.getDistanceSqToEntity(var1) < 144.0D
							&& var1.getAITarget() != null ? false : isSitting;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		theEntity.getNavigator().clearPathEntity();
		theEntity.setSitting(true);
	}
}

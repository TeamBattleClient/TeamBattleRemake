package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
	private int field_142050_e;
	EntityTameable theEntityTameable;
	EntityLivingBase theTarget;

	public EntityAIOwnerHurtTarget(EntityTameable p_i1668_1_) {
		super(p_i1668_1_, false);
		theEntityTameable = p_i1668_1_;
		setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theEntityTameable.isTamed())
			return false;
		else {
			final EntityLivingBase var1 = theEntityTameable.getOwner();

			if (var1 == null)
				return false;
			else {
				theTarget = var1.getLastAttacker();
				final int var2 = var1.getLastAttackerTime();
				return var2 != field_142050_e
						&& isSuitableTarget(theTarget, false)
						&& theEntityTameable.func_142018_a(theTarget, var1);
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(theTarget);
		final EntityLivingBase var1 = theEntityTameable.getOwner();

		if (var1 != null) {
			field_142050_e = var1.getLastAttackerTime();
		}

		super.startExecuting();
	}
}

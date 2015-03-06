package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
	private int field_142051_e;
	EntityTameable theDefendingTameable;
	EntityLivingBase theOwnerAttacker;

	public EntityAIOwnerHurtByTarget(EntityTameable p_i1667_1_) {
		super(p_i1667_1_, false);
		theDefendingTameable = p_i1667_1_;
		setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theDefendingTameable.isTamed())
			return false;
		else {
			final EntityLivingBase var1 = theDefendingTameable.getOwner();

			if (var1 == null)
				return false;
			else {
				theOwnerAttacker = var1.getAITarget();
				final int var2 = var1.func_142015_aE();
				return var2 != field_142051_e
						&& isSuitableTarget(theOwnerAttacker, false)
						&& theDefendingTameable.func_142018_a(theOwnerAttacker,
								var1);
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(theOwnerAttacker);
		final EntityLivingBase var1 = theDefendingTameable.getOwner();

		if (var1 != null) {
			field_142051_e = var1.func_142015_aE();
		}

		super.startExecuting();
	}
}

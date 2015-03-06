package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack extends EntityAIBase {
	int attackCountdown;
	EntityLiving theEntity;
	EntityLivingBase theVictim;
	World theWorld;

	public EntityAIOcelotAttack(EntityLiving p_i1641_1_) {
		theEntity = p_i1641_1_;
		theWorld = p_i1641_1_.worldObj;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !theVictim.isEntityAlive() ? false : theEntity
				.getDistanceSqToEntity(theVictim) > 225.0D ? false : !theEntity
				.getNavigator().noPath() || shouldExecute();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		theVictim = null;
		theEntity.getNavigator().clearPathEntity();
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final EntityLivingBase var1 = theEntity.getAttackTarget();

		if (var1 == null)
			return false;
		else {
			theVictim = var1;
			return true;
		}
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		theEntity.getLookHelper().setLookPositionWithEntity(theVictim, 30.0F,
				30.0F);
		final double var1 = theEntity.width * 2.0F * theEntity.width * 2.0F;
		final double var3 = theEntity.getDistanceSq(theVictim.posX,
				theVictim.boundingBox.minY, theVictim.posZ);
		double var5 = 0.8D;

		if (var3 > var1 && var3 < 16.0D) {
			var5 = 1.33D;
		} else if (var3 < 225.0D) {
			var5 = 0.6D;
		}

		theEntity.getNavigator().tryMoveToEntityLiving(theVictim, var5);
		attackCountdown = Math.max(attackCountdown - 1, 0);

		if (var3 <= var1) {
			if (attackCountdown <= 0) {
				attackCountdown = 20;
				theEntity.attackEntityAsMob(theVictim);
			}
		}
	}
}

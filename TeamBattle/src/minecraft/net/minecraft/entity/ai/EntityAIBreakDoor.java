package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract {
	private int breakingTime;
	private int field_75358_j = -1;

	public EntityAIBreakDoor(EntityLiving p_i1618_1_) {
		super(p_i1618_1_);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		final double var1 = theEntity.getDistanceSq(entityPosX, entityPosY,
				entityPosZ);
		return breakingTime <= 240
				&& !field_151504_e.func_150015_f(theEntity.worldObj,
						entityPosX, entityPosY, entityPosZ) && var1 < 4.0D;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		super.resetTask();
		theEntity.worldObj
				.destroyBlockInWorldPartially(theEntity.getEntityId(),
						entityPosX, entityPosY, entityPosZ, -1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		return !super.shouldExecute() ? false : !theEntity.worldObj
				.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false
				: !field_151504_e.func_150015_f(theEntity.worldObj, entityPosX,
						entityPosY, entityPosZ);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		super.startExecuting();
		breakingTime = 0;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		super.updateTask();

		if (theEntity.getRNG().nextInt(20) == 0) {
			theEntity.worldObj.playAuxSFX(1010, entityPosX, entityPosY,
					entityPosZ, 0);
		}

		++breakingTime;
		final int var1 = (int) (breakingTime / 240.0F * 10.0F);

		if (var1 != field_75358_j) {
			theEntity.worldObj.destroyBlockInWorldPartially(
					theEntity.getEntityId(), entityPosX, entityPosY,
					entityPosZ, var1);
			field_75358_j = var1;
		}

		if (breakingTime == 240
				&& theEntity.worldObj.difficultySetting == EnumDifficulty.HARD) {
			theEntity.worldObj
					.setBlockToAir(entityPosX, entityPosY, entityPosZ);
			theEntity.worldObj.playAuxSFX(1012, entityPosX, entityPosY,
					entityPosZ, 0);
			theEntity.worldObj.playAuxSFX(2001, entityPosX, entityPosY,
					entityPosZ, Block.getIdFromBlock(field_151504_e));
		}
	}
}

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EntityAITempt extends EntityAIBase {
	/**
	 * A counter that is decremented each time the shouldExecute method is
	 * called. The shouldExecute method will always return false if
	 * delayTemptCounter is greater than 0.
	 */
	private int delayTemptCounter;
	private final Item field_151484_k;

	private double field_75278_f;

	private double field_75279_g;

	private final double field_75282_b;
	private boolean field_75286_m;
	/** True if this EntityAITempt task is running */
	private boolean isRunning;

	/**
	 * Whether the entity using this AI will be scared by the tempter's sudden
	 * movement.
	 */
	private final boolean scaredByPlayerMovement;

	/** X position of player tempting this mob */
	private double targetX;

	/** Y position of player tempting this mob */
	private double targetY;
	/** Z position of player tempting this mob */
	private double targetZ;

	/** The entity using this AI that is tempted by the player. */
	private final EntityCreature temptedEntity;
	/** The player that is tempting the entity that is using this AI. */
	private EntityPlayer temptingPlayer;

	public EntityAITempt(EntityCreature p_i45316_1_, double p_i45316_2_,
			Item p_i45316_4_, boolean p_i45316_5_) {
		temptedEntity = p_i45316_1_;
		field_75282_b = p_i45316_2_;
		field_151484_k = p_i45316_4_;
		scaredByPlayerMovement = p_i45316_5_;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		if (scaredByPlayerMovement) {
			if (temptedEntity.getDistanceSqToEntity(temptingPlayer) < 36.0D) {
				if (temptingPlayer.getDistanceSq(targetX, targetY, targetZ) > 0.010000000000000002D)
					return false;

				if (Math.abs(temptingPlayer.rotationPitch - field_75278_f) > 5.0D
						|| Math.abs(temptingPlayer.rotationYaw - field_75279_g) > 5.0D)
					return false;
			} else {
				targetX = temptingPlayer.posX;
				targetY = temptingPlayer.posY;
				targetZ = temptingPlayer.posZ;
			}

			field_75278_f = temptingPlayer.rotationPitch;
			field_75279_g = temptingPlayer.rotationYaw;
		}

		return shouldExecute();
	}

	/**
	 * @see #isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		temptingPlayer = null;
		temptedEntity.getNavigator().clearPathEntity();
		delayTemptCounter = 100;
		isRunning = false;
		temptedEntity.getNavigator().setAvoidsWater(field_75286_m);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (delayTemptCounter > 0) {
			--delayTemptCounter;
			return false;
		} else {
			temptingPlayer = temptedEntity.worldObj.getClosestPlayerToEntity(
					temptedEntity, 10.0D);

			if (temptingPlayer == null)
				return false;
			else {
				final ItemStack var1 = temptingPlayer.getCurrentEquippedItem();
				return var1 == null ? false : var1.getItem() == field_151484_k;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		targetX = temptingPlayer.posX;
		targetY = temptingPlayer.posY;
		targetZ = temptingPlayer.posZ;
		isRunning = true;
		field_75286_m = temptedEntity.getNavigator().getAvoidsWater();
		temptedEntity.getNavigator().setAvoidsWater(false);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		temptedEntity.getLookHelper().setLookPositionWithEntity(temptingPlayer,
				30.0F, temptedEntity.getVerticalFaceSpeed());

		if (temptedEntity.getDistanceSqToEntity(temptingPlayer) < 6.25D) {
			temptedEntity.getNavigator().clearPathEntity();
		} else {
			temptedEntity.getNavigator().tryMoveToEntityLiving(temptingPlayer,
					field_75282_b);
		}
	}
}

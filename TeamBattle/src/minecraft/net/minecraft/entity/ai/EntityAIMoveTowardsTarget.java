package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsTarget extends EntityAIBase {
	/**
	 * If the distance to the target entity is further than this, this AI task
	 * will not run.
	 */
	private final float maxTargetDistance;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private final double speed;
	private EntityLivingBase targetEntity;

	private final EntityCreature theEntity;

	public EntityAIMoveTowardsTarget(EntityCreature p_i1640_1_,
			double p_i1640_2_, float p_i1640_4_) {
		theEntity = p_i1640_1_;
		speed = p_i1640_2_;
		maxTargetDistance = p_i1640_4_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !theEntity.getNavigator().noPath()
				&& targetEntity.isEntityAlive()
				&& targetEntity.getDistanceSqToEntity(theEntity) < maxTargetDistance
						* maxTargetDistance;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		targetEntity = null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		targetEntity = theEntity.getAttackTarget();

		if (targetEntity == null)
			return false;
		else if (targetEntity.getDistanceSqToEntity(theEntity) > maxTargetDistance
				* maxTargetDistance)
			return false;
		else {
			final Vec3 var1 = RandomPositionGenerator
					.findRandomTargetBlockTowards(theEntity, 16, 7, Vec3
							.createVectorHelper(targetEntity.posX,
									targetEntity.posY, targetEntity.posZ));

			if (var1 == null)
				return false;
			else {
				movePosX = var1.xCoord;
				movePosY = var1.yCoord;
				movePosZ = var1.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		theEntity.getNavigator().tryMoveToXYZ(movePosX, movePosY, movePosZ,
				speed);
	}
}

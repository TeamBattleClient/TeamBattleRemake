package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsRestriction extends EntityAIBase {
	private final double movementSpeed;
	private double movePosX;
	private double movePosY;
	private double movePosZ;
	private final EntityCreature theEntity;

	public EntityAIMoveTowardsRestriction(EntityCreature p_i2347_1_,
			double p_i2347_2_) {
		theEntity = p_i2347_1_;
		movementSpeed = p_i2347_2_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !theEntity.getNavigator().noPath();
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (theEntity.isWithinHomeDistanceCurrentPosition())
			return false;
		else {
			final ChunkCoordinates var1 = theEntity.getHomePosition();
			final Vec3 var2 = RandomPositionGenerator
					.findRandomTargetBlockTowards(theEntity, 16, 7,
							Vec3.createVectorHelper(var1.posX, var1.posY,
									var1.posZ));

			if (var2 == null)
				return false;
			else {
				movePosX = var2.xCoord;
				movePosY = var2.yCoord;
				movePosZ = var2.zCoord;
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
				movementSpeed);
	}
}

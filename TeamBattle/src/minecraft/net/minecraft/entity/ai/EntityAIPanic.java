package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIPanic extends EntityAIBase {
	private double randPosX;
	private double randPosY;
	private double randPosZ;
	private final double speed;
	private final EntityCreature theEntityCreature;

	public EntityAIPanic(EntityCreature p_i1645_1_, double p_i1645_2_) {
		theEntityCreature = p_i1645_1_;
		speed = p_i1645_2_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !theEntityCreature.getNavigator().noPath();
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (theEntityCreature.getAITarget() == null
				&& !theEntityCreature.isBurning())
			return false;
		else {
			final Vec3 var1 = RandomPositionGenerator.findRandomTarget(
					theEntityCreature, 5, 4);

			if (var1 == null)
				return false;
			else {
				randPosX = var1.xCoord;
				randPosY = var1.yCoord;
				randPosZ = var1.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		theEntityCreature.getNavigator().tryMoveToXYZ(randPosX, randPosY,
				randPosZ, speed);
	}
}

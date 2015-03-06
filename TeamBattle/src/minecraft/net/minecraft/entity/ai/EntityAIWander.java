package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIWander extends EntityAIBase {
	private final EntityCreature entity;
	private final double speed;
	private double xPosition;
	private double yPosition;
	private double zPosition;

	public EntityAIWander(EntityCreature p_i1648_1_, double p_i1648_2_) {
		entity = p_i1648_1_;
		speed = p_i1648_2_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !entity.getNavigator().noPath();
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (entity.getAge() >= 100)
			return false;
		else if (entity.getRNG().nextInt(120) != 0)
			return false;
		else {
			final Vec3 var1 = RandomPositionGenerator.findRandomTarget(entity,
					10, 7);

			if (var1 == null)
				return false;
			else {
				xPosition = var1.xCoord;
				yPosition = var1.yCoord;
				zPosition = var1.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition,
				speed);
	}
}

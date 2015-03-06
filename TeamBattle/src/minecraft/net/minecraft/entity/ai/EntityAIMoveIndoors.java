package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveIndoors extends EntityAIBase {
	private VillageDoorInfo doorInfo;
	private final EntityCreature entityObj;
	private int insidePosX = -1;
	private int insidePosZ = -1;

	public EntityAIMoveIndoors(EntityCreature p_i1637_1_) {
		entityObj = p_i1637_1_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !entityObj.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		insidePosX = doorInfo.getInsidePosX();
		insidePosZ = doorInfo.getInsidePosZ();
		doorInfo = null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final int var1 = MathHelper.floor_double(entityObj.posX);
		final int var2 = MathHelper.floor_double(entityObj.posY);
		final int var3 = MathHelper.floor_double(entityObj.posZ);

		if ((!entityObj.worldObj.isDaytime() || entityObj.worldObj.isRaining() || !entityObj.worldObj
				.getBiomeGenForCoords(var1, var3).canSpawnLightningBolt())
				&& !entityObj.worldObj.provider.hasNoSky) {
			if (entityObj.getRNG().nextInt(50) != 0)
				return false;
			else if (insidePosX != -1
					&& entityObj.getDistanceSq(insidePosX, entityObj.posY,
							insidePosZ) < 4.0D)
				return false;
			else {
				final Village var4 = entityObj.worldObj.villageCollectionObj
						.findNearestVillage(var1, var2, var3, 14);

				if (var4 == null)
					return false;
				else {
					doorInfo = var4.findNearestDoorUnrestricted(var1, var2,
							var3);
					return doorInfo != null;
				}
			}
		} else
			return false;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		insidePosX = -1;

		if (entityObj.getDistanceSq(doorInfo.getInsidePosX(), doorInfo.posY,
				doorInfo.getInsidePosZ()) > 256.0D) {
			final Vec3 var1 = RandomPositionGenerator
					.findRandomTargetBlockTowards(entityObj, 14, 3, Vec3
							.createVectorHelper(
									doorInfo.getInsidePosX() + 0.5D,
									doorInfo.getInsidePosY(),
									doorInfo.getInsidePosZ() + 0.5D));

			if (var1 != null) {
				entityObj.getNavigator().tryMoveToXYZ(var1.xCoord, var1.yCoord,
						var1.zCoord, 1.0D);
			}
		} else {
			entityObj.getNavigator().tryMoveToXYZ(
					doorInfo.getInsidePosX() + 0.5D, doorInfo.getInsidePosY(),
					doorInfo.getInsidePosZ() + 0.5D, 1.0D);
		}
	}
}

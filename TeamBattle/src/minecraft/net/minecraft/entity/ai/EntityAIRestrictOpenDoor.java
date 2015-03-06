package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIRestrictOpenDoor extends EntityAIBase {
	private final EntityCreature entityObj;
	private VillageDoorInfo frontDoor;

	public EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_) {
		entityObj = p_i1651_1_;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return entityObj.worldObj.isDaytime() ? false
				: !frontDoor.isDetachedFromVillageFlag
						&& frontDoor.isInside(
								MathHelper.floor_double(entityObj.posX),
								MathHelper.floor_double(entityObj.posZ));
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		entityObj.getNavigator().setBreakDoors(true);
		entityObj.getNavigator().setEnterDoors(true);
		frontDoor = null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (entityObj.worldObj.isDaytime())
			return false;
		else {
			final Village var1 = entityObj.worldObj.villageCollectionObj
					.findNearestVillage(
							MathHelper.floor_double(entityObj.posX),
							MathHelper.floor_double(entityObj.posY),
							MathHelper.floor_double(entityObj.posZ), 16);

			if (var1 == null)
				return false;
			else {
				frontDoor = var1.findNearestDoor(
						MathHelper.floor_double(entityObj.posX),
						MathHelper.floor_double(entityObj.posY),
						MathHelper.floor_double(entityObj.posZ));
				return frontDoor == null ? false
						: frontDoor.getInsideDistanceSquare(
								MathHelper.floor_double(entityObj.posX),
								MathHelper.floor_double(entityObj.posY),
								MathHelper.floor_double(entityObj.posZ)) < 2.25D;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		entityObj.getNavigator().setBreakDoors(false);
		entityObj.getNavigator().setEnterDoors(false);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		frontDoor.incrementDoorOpeningRestrictionCounter();
	}
}

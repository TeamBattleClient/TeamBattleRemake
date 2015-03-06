package net.minecraft.entity.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class EntityAIMoveThroughVillage extends EntityAIBase {
	private VillageDoorInfo doorInfo;
	private final List doorList = new ArrayList();

	/** The PathNavigate of our entity. */
	private PathEntity entityPathNavigate;
	private final boolean isNocturnal;
	private final double movementSpeed;
	private final EntityCreature theEntity;

	public EntityAIMoveThroughVillage(EntityCreature p_i1638_1_,
			double p_i1638_2_, boolean p_i1638_4_) {
		theEntity = p_i1638_1_;
		movementSpeed = p_i1638_2_;
		isNocturnal = p_i1638_4_;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		if (theEntity.getNavigator().noPath())
			return false;
		else {
			final float var1 = theEntity.width + 4.0F;
			return theEntity.getDistanceSq(doorInfo.posX, doorInfo.posY,
					doorInfo.posZ) > var1 * var1;
		}
	}

	private VillageDoorInfo func_75412_a(Village p_75412_1_) {
		VillageDoorInfo var2 = null;
		int var3 = Integer.MAX_VALUE;
		final List var4 = p_75412_1_.getVillageDoorInfoList();
		final Iterator var5 = var4.iterator();

		while (var5.hasNext()) {
			final VillageDoorInfo var6 = (VillageDoorInfo) var5.next();
			final int var7 = var6.getDistanceSquared(
					MathHelper.floor_double(theEntity.posX),
					MathHelper.floor_double(theEntity.posY),
					MathHelper.floor_double(theEntity.posZ));

			if (var7 < var3 && !func_75413_a(var6)) {
				var2 = var6;
				var3 = var7;
			}
		}

		return var2;
	}

	private boolean func_75413_a(VillageDoorInfo p_75413_1_) {
		final Iterator var2 = doorList.iterator();
		VillageDoorInfo var3;

		do {
			if (!var2.hasNext())
				return false;

			var3 = (VillageDoorInfo) var2.next();
		} while (p_75413_1_.posX != var3.posX || p_75413_1_.posY != var3.posY
				|| p_75413_1_.posZ != var3.posZ);

		return true;
	}

	private void func_75414_f() {
		if (doorList.size() > 15) {
			doorList.remove(0);
		}
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		if (theEntity.getNavigator().noPath()
				|| theEntity.getDistanceSq(doorInfo.posX, doorInfo.posY,
						doorInfo.posZ) < 16.0D) {
			doorList.add(doorInfo);
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		func_75414_f();

		if (isNocturnal && theEntity.worldObj.isDaytime())
			return false;
		else {
			final Village var1 = theEntity.worldObj.villageCollectionObj
					.findNearestVillage(
							MathHelper.floor_double(theEntity.posX),
							MathHelper.floor_double(theEntity.posY),
							MathHelper.floor_double(theEntity.posZ), 0);

			if (var1 == null)
				return false;
			else {
				doorInfo = func_75412_a(var1);

				if (doorInfo == null)
					return false;
				else {
					final boolean var2 = theEntity.getNavigator()
							.getCanBreakDoors();
					theEntity.getNavigator().setBreakDoors(false);
					entityPathNavigate = theEntity.getNavigator().getPathToXYZ(
							doorInfo.posX, doorInfo.posY, doorInfo.posZ);
					theEntity.getNavigator().setBreakDoors(var2);

					if (entityPathNavigate != null)
						return true;
					else {
						final Vec3 var3 = RandomPositionGenerator
								.findRandomTargetBlockTowards(theEntity, 10, 7,
										Vec3.createVectorHelper(doorInfo.posX,
												doorInfo.posY, doorInfo.posZ));

						if (var3 == null)
							return false;
						else {
							theEntity.getNavigator().setBreakDoors(false);
							entityPathNavigate = theEntity.getNavigator()
									.getPathToXYZ(var3.xCoord, var3.yCoord,
											var3.zCoord);
							theEntity.getNavigator().setBreakDoors(var2);
							return entityPathNavigate != null;
						}
					}
				}
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		theEntity.getNavigator().setPath(entityPathNavigate, movementSpeed);
	}
}

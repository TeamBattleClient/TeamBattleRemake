package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public abstract class EntityAIDoorInteract extends EntityAIBase {
	float entityPositionX;
	float entityPositionZ;
	protected int entityPosX;
	protected int entityPosY;
	protected int entityPosZ;

	protected BlockDoor field_151504_e;
	/**
	 * If is true then the Entity has stopped Door Interaction and compoleted
	 * the task.
	 */
	boolean hasStoppedDoorInteraction;
	protected EntityLiving theEntity;

	public EntityAIDoorInteract(EntityLiving p_i1621_1_) {
		theEntity = p_i1621_1_;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !hasStoppedDoorInteraction;
	}

	private BlockDoor func_151503_a(int p_151503_1_, int p_151503_2_,
			int p_151503_3_) {
		final Block var4 = theEntity.worldObj.getBlock(p_151503_1_,
				p_151503_2_, p_151503_3_);
		return var4 != Blocks.wooden_door ? null : (BlockDoor) var4;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theEntity.isCollidedHorizontally)
			return false;
		else {
			final PathNavigate var1 = theEntity.getNavigator();
			final PathEntity var2 = var1.getPath();

			if (var2 != null && !var2.isFinished() && var1.getCanBreakDoors()) {
				for (int var3 = 0; var3 < Math.min(
						var2.getCurrentPathIndex() + 2,
						var2.getCurrentPathLength()); ++var3) {
					final PathPoint var4 = var2.getPathPointFromIndex(var3);
					entityPosX = var4.xCoord;
					entityPosY = var4.yCoord + 1;
					entityPosZ = var4.zCoord;

					if (theEntity.getDistanceSq(entityPosX, theEntity.posY,
							entityPosZ) <= 2.25D) {
						field_151504_e = func_151503_a(entityPosX, entityPosY,
								entityPosZ);

						if (field_151504_e != null)
							return true;
					}
				}

				entityPosX = MathHelper.floor_double(theEntity.posX);
				entityPosY = MathHelper.floor_double(theEntity.posY + 1.0D);
				entityPosZ = MathHelper.floor_double(theEntity.posZ);
				field_151504_e = func_151503_a(entityPosX, entityPosY,
						entityPosZ);
				return field_151504_e != null;
			} else
				return false;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		hasStoppedDoorInteraction = false;
		entityPositionX = (float) (entityPosX + 0.5F - theEntity.posX);
		entityPositionZ = (float) (entityPosZ + 0.5F - theEntity.posZ);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		final float var1 = (float) (entityPosX + 0.5F - theEntity.posX);
		final float var2 = (float) (entityPosZ + 0.5F - theEntity.posZ);
		final float var3 = entityPositionX * var1 + entityPositionZ * var2;

		if (var3 < 0.0F) {
			hasStoppedDoorInteraction = true;
		}
	}
}

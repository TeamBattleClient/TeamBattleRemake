package net.minecraft.entity.ai;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

public class EntityAIAvoidEntity extends EntityAIBase {
	private Entity closestLivingEntity;

	private final float distanceFromEntity;
	/** The PathEntity of our entity */
	private PathEntity entityPathEntity;
	/** The PathNavigate of our entity */
	private final PathNavigate entityPathNavigate;
	private final double farSpeed;
	public final IEntitySelector field_98218_a = new IEntitySelector() {

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_.isEntityAlive()
					&& theEntity.getEntitySenses().canSee(p_82704_1_);
		}
	};

	private final double nearSpeed;

	/** The class of the entity we should avoid */
	private final Class targetEntityClass;

	/** The entity we are attached to */
	private final EntityCreature theEntity;

	public EntityAIAvoidEntity(EntityCreature p_i1616_1_, Class p_i1616_2_,
			float p_i1616_3_, double p_i1616_4_, double p_i1616_6_) {
		theEntity = p_i1616_1_;
		targetEntityClass = p_i1616_2_;
		distanceFromEntity = p_i1616_3_;
		farSpeed = p_i1616_4_;
		nearSpeed = p_i1616_6_;
		entityPathNavigate = p_i1616_1_.getNavigator();
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return !entityPathNavigate.noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		closestLivingEntity = null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (targetEntityClass == EntityPlayer.class) {
			if (theEntity instanceof EntityTameable
					&& ((EntityTameable) theEntity).isTamed())
				return false;

			closestLivingEntity = theEntity.worldObj.getClosestPlayerToEntity(
					theEntity, distanceFromEntity);

			if (closestLivingEntity == null)
				return false;
		} else {
			final List var1 = theEntity.worldObj.selectEntitiesWithinAABB(
					targetEntityClass, theEntity.boundingBox.expand(
							distanceFromEntity, 3.0D, distanceFromEntity),
					field_98218_a);

			if (var1.isEmpty())
				return false;

			closestLivingEntity = (Entity) var1.get(0);
		}

		final Vec3 var2 = RandomPositionGenerator
				.findRandomTargetBlockAwayFrom(theEntity, 16, 7, Vec3
						.createVectorHelper(closestLivingEntity.posX,
								closestLivingEntity.posY,
								closestLivingEntity.posZ));

		if (var2 == null)
			return false;
		else if (closestLivingEntity.getDistanceSq(var2.xCoord, var2.yCoord,
				var2.zCoord) < closestLivingEntity
				.getDistanceSqToEntity(theEntity))
			return false;
		else {
			entityPathEntity = entityPathNavigate.getPathToXYZ(var2.xCoord,
					var2.yCoord, var2.zCoord);
			return entityPathEntity == null ? false : entityPathEntity
					.isDestinationSame(var2);
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		entityPathNavigate.setPath(entityPathEntity, farSpeed);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		if (theEntity.getDistanceSqToEntity(closestLivingEntity) < 49.0D) {
			theEntity.getNavigator().setSpeed(nearSpeed);
		} else {
			theEntity.getNavigator().setSpeed(farSpeed);
		}
	}
}

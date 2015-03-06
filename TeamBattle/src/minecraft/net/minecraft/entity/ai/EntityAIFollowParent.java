package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent extends EntityAIBase {
	/** The child that is following its parent. */
	EntityAnimal childAnimal;
	private int field_75345_d;
	double field_75347_c;
	EntityAnimal parentAnimal;

	public EntityAIFollowParent(EntityAnimal p_i1626_1_, double p_i1626_2_) {
		childAnimal = p_i1626_1_;
		field_75347_c = p_i1626_2_;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		if (!parentAnimal.isEntityAlive())
			return false;
		else {
			final double var1 = childAnimal.getDistanceSqToEntity(parentAnimal);
			return var1 >= 9.0D && var1 <= 256.0D;
		}
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		parentAnimal = null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (childAnimal.getGrowingAge() >= 0)
			return false;
		else {
			final List var1 = childAnimal.worldObj.getEntitiesWithinAABB(
					childAnimal.getClass(),
					childAnimal.boundingBox.expand(8.0D, 4.0D, 8.0D));
			EntityAnimal var2 = null;
			double var3 = Double.MAX_VALUE;
			final Iterator var5 = var1.iterator();

			while (var5.hasNext()) {
				final EntityAnimal var6 = (EntityAnimal) var5.next();

				if (var6.getGrowingAge() >= 0) {
					final double var7 = childAnimal.getDistanceSqToEntity(var6);

					if (var7 <= var3) {
						var3 = var7;
						var2 = var6;
					}
				}
			}

			if (var2 == null)
				return false;
			else if (var3 < 9.0D)
				return false;
			else {
				parentAnimal = var2;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		field_75345_d = 0;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		if (--field_75345_d <= 0) {
			field_75345_d = 10;
			childAnimal.getNavigator().tryMoveToEntityLiving(parentAnimal,
					field_75347_c);
		}
	}
}

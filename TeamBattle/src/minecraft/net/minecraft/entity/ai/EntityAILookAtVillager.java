package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAILookAtVillager extends EntityAIBase {
	private int lookTime;
	private final EntityIronGolem theGolem;
	private EntityVillager theVillager;

	public EntityAILookAtVillager(EntityIronGolem p_i1643_1_) {
		theGolem = p_i1643_1_;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return lookTime > 0;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		theGolem.setHoldingRose(false);
		theVillager = null;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theGolem.worldObj.isDaytime())
			return false;
		else if (theGolem.getRNG().nextInt(8000) != 0)
			return false;
		else {
			theVillager = (EntityVillager) theGolem.worldObj
					.findNearestEntityWithinAABB(EntityVillager.class,
							theGolem.boundingBox.expand(6.0D, 2.0D, 6.0D),
							theGolem);
			return theVillager != null;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		lookTime = 400;
		theGolem.setHoldingRose(true);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		theGolem.getLookHelper().setLookPositionWithEntity(theVillager, 30.0F,
				30.0F);
		--lookTime;
	}
}

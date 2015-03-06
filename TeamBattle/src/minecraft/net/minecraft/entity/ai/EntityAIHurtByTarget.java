package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
	boolean entityCallsForHelp;
	private int field_142052_b;

	public EntityAIHurtByTarget(EntityCreature p_i1660_1_, boolean p_i1660_2_) {
		super(p_i1660_1_, false);
		entityCallsForHelp = p_i1660_2_;
		setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final int var1 = taskOwner.func_142015_aE();
		return var1 != field_142052_b
				&& isSuitableTarget(taskOwner.getAITarget(), false);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(taskOwner.getAITarget());
		field_142052_b = taskOwner.func_142015_aE();

		if (entityCallsForHelp) {
			final double var1 = getTargetDistance();
			final List var3 = taskOwner.worldObj.getEntitiesWithinAABB(
					taskOwner.getClass(),
					AxisAlignedBB.getBoundingBox(taskOwner.posX,
							taskOwner.posY, taskOwner.posZ,
							taskOwner.posX + 1.0D, taskOwner.posY + 1.0D,
							taskOwner.posZ + 1.0D).expand(var1, 10.0D, var1));
			final Iterator var4 = var3.iterator();

			while (var4.hasNext()) {
				final EntityCreature var5 = (EntityCreature) var4.next();

				if (taskOwner != var5 && var5.getAttackTarget() == null
						&& !var5.isOnSameTeam(taskOwner.getAITarget())) {
					var5.setAttackTarget(taskOwner.getAITarget());
				}
			}
		}

		super.startExecuting();
	}
}

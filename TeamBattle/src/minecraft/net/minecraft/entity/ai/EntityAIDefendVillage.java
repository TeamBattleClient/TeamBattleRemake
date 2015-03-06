package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.village.Village;

public class EntityAIDefendVillage extends EntityAITarget {
	EntityIronGolem irongolem;

	/**
	 * The aggressor of the iron golem's village which is now the golem's attack
	 * target.
	 */
	EntityLivingBase villageAgressorTarget;

	public EntityAIDefendVillage(EntityIronGolem p_i1659_1_) {
		super(p_i1659_1_, false, true);
		irongolem = p_i1659_1_;
		setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final Village var1 = irongolem.getVillage();

		if (var1 == null)
			return false;
		else {
			villageAgressorTarget = var1.findNearestVillageAggressor(irongolem);

			if (!isSuitableTarget(villageAgressorTarget, false)) {
				if (taskOwner.getRNG().nextInt(20) == 0) {
					villageAgressorTarget = var1.func_82685_c(irongolem);
					return isSuitableTarget(villageAgressorTarget, false);
				} else
					return false;
			} else
				return true;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		irongolem.setAttackTarget(villageAgressorTarget);
		super.startExecuting();
	}
}

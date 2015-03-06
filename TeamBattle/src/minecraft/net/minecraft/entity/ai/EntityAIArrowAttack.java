package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.MathHelper;

public class EntityAIArrowAttack extends EntityAIBase {
	private EntityLivingBase attackTarget;

	/** The entity the AI instance has been applied to */
	private final EntityLiving entityHost;
	private double entityMoveSpeed;

	private int field_75318_f;
	private float field_82642_h;
	private int field_96561_g;
	private float field_96562_i;

	/**
	 * The maximum time the AI has to wait before peforming another ranged
	 * attack.
	 */
	private int maxRangedAttackTime;
	/**
	 * The entity (as a RangedAttackMob) the AI instance has been applied to.
	 */
	private final IRangedAttackMob rangedAttackEntityHost;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches
	 * 0. It is then set back to the maxRangedAttackTime.
	 */
	private int rangedAttackTime;

	public EntityAIArrowAttack(IRangedAttackMob p_i1649_1_, double p_i1649_2_,
			int p_i1649_4_, float p_i1649_5_) {
		this(p_i1649_1_, p_i1649_2_, p_i1649_4_, p_i1649_4_, p_i1649_5_);
	}

	public EntityAIArrowAttack(IRangedAttackMob p_i1650_1_, double p_i1650_2_,
			int p_i1650_4_, int p_i1650_5_, float p_i1650_6_) {
		rangedAttackTime = -1;

		if (!(p_i1650_1_ instanceof EntityLivingBase))
			throw new IllegalArgumentException(
					"ArrowAttackGoal requires Mob implements RangedAttackMob");
		else {
			rangedAttackEntityHost = p_i1650_1_;
			entityHost = (EntityLiving) p_i1650_1_;
			entityMoveSpeed = p_i1650_2_;
			field_96561_g = p_i1650_4_;
			maxRangedAttackTime = p_i1650_5_;
			field_96562_i = p_i1650_6_;
			field_82642_h = p_i1650_6_ * p_i1650_6_;
			setMutexBits(3);
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return shouldExecute() || !entityHost.getNavigator().noPath();
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		attackTarget = null;
		field_75318_f = 0;
		rangedAttackTime = -1;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		final EntityLivingBase var1 = entityHost.getAttackTarget();

		if (var1 == null)
			return false;
		else {
			attackTarget = var1;
			return true;
		}
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		final double var1 = entityHost.getDistanceSq(attackTarget.posX,
				attackTarget.boundingBox.minY, attackTarget.posZ);
		final boolean var3 = entityHost.getEntitySenses().canSee(attackTarget);

		if (var3) {
			++field_75318_f;
		} else {
			field_75318_f = 0;
		}

		if (var1 <= field_82642_h && field_75318_f >= 20) {
			entityHost.getNavigator().clearPathEntity();
		} else {
			entityHost.getNavigator().tryMoveToEntityLiving(attackTarget,
					entityMoveSpeed);
		}

		entityHost.getLookHelper().setLookPositionWithEntity(attackTarget,
				30.0F, 30.0F);
		float var4;

		if (--rangedAttackTime == 0) {
			if (var1 > field_82642_h || !var3)
				return;

			var4 = MathHelper.sqrt_double(var1) / field_96562_i;
			float var5 = var4;

			if (var4 < 0.1F) {
				var5 = 0.1F;
			}

			if (var5 > 1.0F) {
				var5 = 1.0F;
			}

			rangedAttackEntityHost.attackEntityWithRangedAttack(attackTarget,
					var5);
			rangedAttackTime = MathHelper.floor_float(var4
					* (maxRangedAttackTime - field_96561_g) + field_96561_g);
		} else if (rangedAttackTime < 0) {
			var4 = MathHelper.sqrt_double(var1) / field_96562_i;
			rangedAttackTime = MathHelper.floor_float(var4
					* (maxRangedAttackTime - field_96561_g) + field_96561_g);
		}
	}
}

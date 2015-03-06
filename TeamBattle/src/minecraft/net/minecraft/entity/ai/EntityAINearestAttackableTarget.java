package net.minecraft.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;

public class EntityAINearestAttackableTarget extends EntityAITarget {
	public static class Sorter implements Comparator {
		private final Entity theEntity;

		public Sorter(Entity p_i1662_1_) {
			theEntity = p_i1662_1_;
		}

		public int compare(Entity p_compare_1_, Entity p_compare_2_) {
			final double var3 = theEntity.getDistanceSqToEntity(p_compare_1_);
			final double var5 = theEntity.getDistanceSqToEntity(p_compare_2_);
			return var3 < var5 ? -1 : var3 > var5 ? 1 : 0;
		}

		@Override
		public int compare(Object p_compare_1_, Object p_compare_2_) {
			return this.compare((Entity) p_compare_1_, (Entity) p_compare_2_);
		}
	}

	private final int targetChance;

	private final Class targetClass;

	private EntityLivingBase targetEntity;
	/**
	 * This filter is applied to the Entity search. Only matching entities will
	 * be targetted. (null -> no restrictions)
	 */
	private final IEntitySelector targetEntitySelector;

	/** Instance of EntityAINearestAttackableTargetSorter. */
	private final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;

	public EntityAINearestAttackableTarget(EntityCreature p_i1663_1_,
			Class p_i1663_2_, int p_i1663_3_, boolean p_i1663_4_) {
		this(p_i1663_1_, p_i1663_2_, p_i1663_3_, p_i1663_4_, false);
	}

	public EntityAINearestAttackableTarget(EntityCreature p_i1664_1_,
			Class p_i1664_2_, int p_i1664_3_, boolean p_i1664_4_,
			boolean p_i1664_5_) {
		this(p_i1664_1_, p_i1664_2_, p_i1664_3_, p_i1664_4_, p_i1664_5_,
				(IEntitySelector) null);
	}

	public EntityAINearestAttackableTarget(EntityCreature p_i1665_1_,
			Class p_i1665_2_, int p_i1665_3_, boolean p_i1665_4_,
			boolean p_i1665_5_, final IEntitySelector p_i1665_6_) {
		super(p_i1665_1_, p_i1665_4_, p_i1665_5_);
		targetClass = p_i1665_2_;
		targetChance = p_i1665_3_;
		theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(
				p_i1665_1_);
		setMutexBits(1);
		targetEntitySelector = new IEntitySelector() {

			@Override
			public boolean isEntityApplicable(Entity p_82704_1_) {
				return !(p_82704_1_ instanceof EntityLivingBase) ? false
						: p_i1665_6_ != null
								&& !p_i1665_6_.isEntityApplicable(p_82704_1_) ? false
								: EntityAINearestAttackableTarget.this
										.isSuitableTarget(
												(EntityLivingBase) p_82704_1_,
												false);
			}
		};
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (targetChance > 0 && taskOwner.getRNG().nextInt(targetChance) != 0)
			return false;
		else {
			final double var1 = getTargetDistance();
			final List var3 = taskOwner.worldObj.selectEntitiesWithinAABB(
					targetClass,
					taskOwner.boundingBox.expand(var1, 4.0D, var1),
					targetEntitySelector);
			Collections.sort(var3, theNearestAttackableTargetSorter);

			if (var3.isEmpty())
				return false;
			else {
				targetEntity = (EntityLivingBase) var3.get(0);
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		taskOwner.setAttackTarget(targetEntity);
		super.startExecuting();
	}
}

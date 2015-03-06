package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class EntityAIMate extends EntityAIBase {
	/** The speed the creature moves at during mating behavior. */
	double moveSpeed;
	/**
	 * Delay preventing a baby from spawning immediately when two mate-able
	 * animals find each other.
	 */
	int spawnBabyDelay;
	private EntityAnimal targetMate;

	private final EntityAnimal theAnimal;

	World theWorld;

	public EntityAIMate(EntityAnimal p_i1619_1_, double p_i1619_2_) {
		theAnimal = p_i1619_1_;
		theWorld = p_i1619_1_.worldObj;
		moveSpeed = p_i1619_2_;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return targetMate.isEntityAlive() && targetMate.isInLove()
				&& spawnBabyDelay < 60;
	}

	/**
	 * Loops through nearby animals and finds another animal of the same type
	 * that can be mated with. Returns the first valid mate found.
	 */
	private EntityAnimal getNearbyMate() {
		final float var1 = 8.0F;
		final List var2 = theWorld.getEntitiesWithinAABB(theAnimal.getClass(),
				theAnimal.boundingBox.expand(var1, var1, var1));
		double var3 = Double.MAX_VALUE;
		EntityAnimal var5 = null;
		final Iterator var6 = var2.iterator();

		while (var6.hasNext()) {
			final EntityAnimal var7 = (EntityAnimal) var6.next();

			if (theAnimal.canMateWith(var7)
					&& theAnimal.getDistanceSqToEntity(var7) < var3) {
				var5 = var7;
				var3 = theAnimal.getDistanceSqToEntity(var7);
			}
		}

		return var5;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		targetMate = null;
		spawnBabyDelay = 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!theAnimal.isInLove())
			return false;
		else {
			targetMate = getNearbyMate();
			return targetMate != null;
		}
	}

	/**
	 * Spawns a baby animal of the same type.
	 */
	private void spawnBaby() {
		final EntityAgeable var1 = theAnimal.createChild(targetMate);

		if (var1 != null) {
			EntityPlayer var2 = theAnimal.func_146083_cb();

			if (var2 == null && targetMate.func_146083_cb() != null) {
				var2 = targetMate.func_146083_cb();
			}

			if (var2 != null) {
				var2.triggerAchievement(StatList.field_151186_x);

				if (theAnimal instanceof EntityCow) {
					var2.triggerAchievement(AchievementList.field_150962_H);
				}
			}

			theAnimal.setGrowingAge(6000);
			targetMate.setGrowingAge(6000);
			theAnimal.resetInLove();
			targetMate.resetInLove();
			var1.setGrowingAge(-24000);
			var1.setLocationAndAngles(theAnimal.posX, theAnimal.posY,
					theAnimal.posZ, 0.0F, 0.0F);
			theWorld.spawnEntityInWorld(var1);
			final Random var3 = theAnimal.getRNG();

			for (int var4 = 0; var4 < 7; ++var4) {
				final double var5 = var3.nextGaussian() * 0.02D;
				final double var7 = var3.nextGaussian() * 0.02D;
				final double var9 = var3.nextGaussian() * 0.02D;
				theWorld.spawnParticle("heart",
						theAnimal.posX + var3.nextFloat() * theAnimal.width
								* 2.0F - theAnimal.width, theAnimal.posY + 0.5D
								+ var3.nextFloat() * theAnimal.height,
						theAnimal.posZ + var3.nextFloat() * theAnimal.width
								* 2.0F - theAnimal.width, var5, var7, var9);
			}

			if (theWorld.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
				theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld,
						theAnimal.posX, theAnimal.posY, theAnimal.posZ, var3
								.nextInt(7) + 1));
			}
		}
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		theAnimal.getLookHelper().setLookPositionWithEntity(targetMate, 10.0F,
				theAnimal.getVerticalFaceSpeed());
		theAnimal.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
		++spawnBabyDelay;

		if (spawnBabyDelay >= 60
				&& theAnimal.getDistanceSqToEntity(targetMate) < 9.0D) {
			spawnBaby();
		}
	}
}

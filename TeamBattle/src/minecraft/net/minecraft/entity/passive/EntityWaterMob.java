package net.minecraft.entity.passive;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob extends EntityCreature implements IAnimals {

	public EntityWaterMob(World p_i1695_1_) {
		super(p_i1695_1_);
	}

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return true;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return worldObj.checkNoEntityCollision(boundingBox);
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 1 + worldObj.rand.nextInt(3);
	}

	/**
	 * Get number of ticks, at least during which the living entity will be
	 * silent.
	 */
	@Override
	public int getTalkInterval() {
		return 120;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate() {
		int var1 = getAir();
		super.onEntityUpdate();

		if (isEntityAlive() && !isInWater()) {
			--var1;
			setAir(var1);

			if (getAir() == -20) {
				setAir(0);
				attackEntityFrom(DamageSource.drown, 2.0F);
			}
		} else {
			setAir(300);
		}
	}
}

package net.minecraft.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public abstract class EntityMob extends EntityCreature implements IMob {

	public EntityMob(World p_i1738_1_) {
		super(p_i1738_1_);
		experienceValue = 5;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(
				SharedMonsterAttributes.attackDamage);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (attackTime <= 0 && p_70785_2_ < 2.0F
				&& p_70785_1_.boundingBox.maxY > boundingBox.minY
				&& p_70785_1_.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			attackEntityAsMob(p_70785_1_);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		float var2 = (float) getEntityAttribute(
				SharedMonsterAttributes.attackDamage).getAttributeValue();
		int var3 = 0;

		if (p_70652_1_ instanceof EntityLivingBase) {
			var2 += EnchantmentHelper.getEnchantmentModifierLiving(this,
					(EntityLivingBase) p_70652_1_);
			var3 += EnchantmentHelper.getKnockbackModifier(this,
					(EntityLivingBase) p_70652_1_);
		}

		final boolean var4 = p_70652_1_.attackEntityFrom(
				DamageSource.causeMobDamage(this), var2);

		if (var4) {
			if (var3 > 0) {
				p_70652_1_.addVelocity(
						-MathHelper.sin(rotationYaw * (float) Math.PI / 180.0F)
								* var3 * 0.5F, 0.1D,
						MathHelper.cos(rotationYaw * (float) Math.PI / 180.0F)
								* var3 * 0.5F);
				motionX *= 0.6D;
				motionZ *= 0.6D;
			}

			final int var5 = EnchantmentHelper.getFireAspectModifier(this);

			if (var5 > 0) {
				p_70652_1_.setFire(var5 * 4);
			}

			if (p_70652_1_ instanceof EntityLivingBase) {
				EnchantmentHelper.func_151384_a((EntityLivingBase) p_70652_1_,
						this);
			}

			EnchantmentHelper.func_151385_b(this, p_70652_1_);
		}

		return var4;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (super.attackEntityFrom(p_70097_1_, p_70097_2_)) {
			final Entity var3 = p_70097_1_.getEntity();

			if (riddenByEntity != var3 && ridingEntity != var3) {
				if (var3 != this) {
					entityToAttack = var3;
				}

				return true;
			} else
				return true;
		} else
			return false;
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack() {
		final EntityPlayer var1 = worldObj.getClosestVulnerablePlayerToEntity(
				this, 16.0D);
		return var1 != null && canEntityBeSeen(var1) ? var1 : null;
	}

	@Override
	protected boolean func_146066_aG() {
		return true;
	}

	@Override
	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.hostile.hurt.fall.big"
				: "game.hostile.hurt.fall.small";
	}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this
	 * creature will try to path to the block. Args: x, y, z
	 */
	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_,
			int p_70783_3_) {
		return 0.5F - worldObj.getLightBrightness(p_70783_1_, p_70783_2_,
				p_70783_3_);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return worldObj.difficultySetting != EnumDifficulty.PEACEFUL
				&& isValidLightLevel() && super.getCanSpawnHere();
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "game.hostile.die";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "game.hostile.hurt";
	}

	@Override
	protected String getSplashSound() {
		return "game.hostile.swim.splash";
	}

	@Override
	protected String getSwimSound() {
		return "game.hostile.swim";
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	protected boolean isValidLightLevel() {
		final int var1 = MathHelper.floor_double(posX);
		final int var2 = MathHelper.floor_double(boundingBox.minY);
		final int var3 = MathHelper.floor_double(posZ);

		if (worldObj.getSavedLightValue(EnumSkyBlock.Sky, var1, var2, var3) > rand
				.nextInt(32))
			return false;
		else {
			int var4 = worldObj.getBlockLightValue(var1, var2, var3);

			if (worldObj.isThundering()) {
				final int var5 = worldObj.skylightSubtracted;
				worldObj.skylightSubtracted = 10;
				var4 = worldObj.getBlockLightValue(var1, var2, var3);
				worldObj.skylightSubtracted = var5;
			}

			return var4 <= rand.nextInt(8);
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		updateArmSwingProgress();
		final float var1 = getBrightness(1.0F);

		if (var1 > 0.5F) {
			entityAge += 2;
		}

		super.onLivingUpdate();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isClient
				&& worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
			setDead();
		}
	}
}

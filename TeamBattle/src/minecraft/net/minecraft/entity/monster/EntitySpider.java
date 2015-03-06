package net.minecraft.entity.monster;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpider extends EntityMob {

	public static class GroupData implements IEntityLivingData {
		public int field_111105_a;

		public void func_111104_a(Random p_111104_1_) {
			final int var2 = p_111104_1_.nextInt(5);

			if (var2 <= 1) {
				field_111105_a = Potion.moveSpeed.id;
			} else if (var2 <= 2) {
				field_111105_a = Potion.damageBoost.id;
			} else if (var2 <= 3) {
				field_111105_a = Potion.regeneration.id;
			} else if (var2 <= 4) {
				field_111105_a = Potion.invisibility.id;
			}
		}
	}

	public EntitySpider(World p_i1743_1_) {
		super(p_i1743_1_);
		setSize(1.4F, 0.9F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				16.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.800000011920929D);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		final float var3 = getBrightness(1.0F);

		if (var3 > 0.5F && rand.nextInt(100) == 0) {
			entityToAttack = null;
		} else {
			if (p_70785_2_ > 2.0F && p_70785_2_ < 6.0F && rand.nextInt(10) == 0) {
				if (onGround) {
					final double var4 = p_70785_1_.posX - posX;
					final double var6 = p_70785_1_.posZ - posZ;
					final float var8 = MathHelper.sqrt_double(var4 * var4
							+ var6 * var6);
					motionX = var4 / var8 * 0.5D * 0.800000011920929D + motionX
							* 0.20000000298023224D;
					motionZ = var6 / var8 * 0.5D * 0.800000011920929D + motionZ
							* 0.20000000298023224D;
					motionY = 0.4000000059604645D;
				}
			} else {
				super.attackEntity(p_70785_1_, p_70785_2_);
			}
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		super.dropFewItems(p_70628_1_, p_70628_2_);

		if (p_70628_1_
				&& (rand.nextInt(3) == 0 || rand.nextInt(1 + p_70628_2_) > 0)) {
			func_145779_a(Items.spider_eye, 1);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 0));
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack() {
		final float var1 = getBrightness(1.0F);

		if (var1 < 0.5F) {
			final double var2 = 16.0D;
			return worldObj.getClosestVulnerablePlayerToEntity(this, var2);
		} else
			return null;
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.spider.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Items.string;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.spider.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.spider.say";
	}

	/**
	 * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns
	 * false. The WatchableObject is updated using setBesideClimableBlock.
	 */
	public boolean isBesideClimbableBlock() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder() {
		return isBesideClimbableBlock();
	}

	@Override
	public boolean isPotionApplicable(PotionEffect p_70687_1_) {
		return p_70687_1_.getPotionID() == Potion.poison.id ? false : super
				.isPotionApplicable(p_70687_1_);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);

		if (worldObj.rand.nextInt(100) == 0) {
			final EntitySkeleton var2 = new EntitySkeleton(worldObj);
			var2.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
			var2.onSpawnWithEgg((IEntityLivingData) null);
			worldObj.spawnEntityInWorld(var2);
			var2.mountEntity(this);
		}

		if (p_110161_1_1 == null) {
			p_110161_1_1 = new EntitySpider.GroupData();

			if (worldObj.difficultySetting == EnumDifficulty.HARD
					&& worldObj.rand.nextFloat() < 0.1F * worldObj
							.func_147462_b(posX, posY, posZ)) {
				((EntitySpider.GroupData) p_110161_1_1)
						.func_111104_a(worldObj.rand);
			}
		}

		if (p_110161_1_1 instanceof EntitySpider.GroupData) {
			final int var4 = ((EntitySpider.GroupData) p_110161_1_1).field_111105_a;

			if (var4 > 0 && Potion.potionTypes[var4] != null) {
				addPotionEffect(new PotionEffect(var4, Integer.MAX_VALUE));
			}
		}

		return (IEntityLivingData) p_110161_1_1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isClient) {
			setBesideClimbableBlock(isCollidedHorizontally);
		}
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to
	 * 0x01 if par1 is true or 0x00 if it is false.
	 */
	public void setBesideClimbableBlock(boolean p_70839_1_) {
		byte var2 = dataWatcher.getWatchableObjectByte(16);

		if (p_70839_1_) {
			var2 = (byte) (var2 | 1);
		} else {
			var2 &= -2;
		}

		dataWatcher.updateObject(16, Byte.valueOf(var2));
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	@Override
	public void setInWeb() {
	}
}

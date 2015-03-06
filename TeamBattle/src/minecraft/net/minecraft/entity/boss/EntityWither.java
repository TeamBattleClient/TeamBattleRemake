package net.minecraft.entity.boss;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither extends EntityMob implements IBossDisplayData,
		IRangedAttackMob {
	/** Selector used to determine the entities a wither boss should attack. */
	private static final IEntitySelector attackEntitySelector = new IEntitySelector() {

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_ instanceof EntityLivingBase
					&& ((EntityLivingBase) p_82704_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
		}
	};
	private final float[] field_82217_f = new float[2];
	private final float[] field_82218_g = new float[2];
	private final float[] field_82220_d = new float[2];
	private final float[] field_82221_e = new float[2];
	private int field_82222_j;
	private final int[] field_82223_h = new int[2];

	private final int[] field_82224_i = new int[2];

	public EntityWither(World p_i1701_1_) {
		super(p_i1701_1_);
		setHealth(getMaxHealth());
		setSize(0.9F, 4.0F);
		isImmuneToFire = true;
		getNavigator().setCanSwim(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class,
				8.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
				EntityLiving.class, 0, false, false, attackEntitySelector));
		experienceValue = 50;
	}

	/**
	 * adds a PotionEffect to the entity
	 */
	@Override
	public void addPotionEffect(PotionEffect p_70690_1_) {
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				300.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.6000000238418579D);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(
				40.0D);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (p_70097_1_ == DamageSource.drown)
			return false;
		else if (func_82212_n() > 0)
			return false;
		else {
			Entity var3;

			if (isArmored()) {
				var3 = p_70097_1_.getSourceOfDamage();

				if (var3 instanceof EntityArrow)
					return false;
			}

			var3 = p_70097_1_.getEntity();

			if (var3 != null
					&& !(var3 instanceof EntityPlayer)
					&& var3 instanceof EntityLivingBase
					&& ((EntityLivingBase) var3).getCreatureAttribute() == getCreatureAttribute())
				return false;
			else {
				if (field_82222_j <= 0) {
					field_82222_j = 20;
				}

				for (int var4 = 0; var4 < field_82224_i.length; ++var4) {
					field_82224_i[var4] += 3;
				}

				return super.attackEntityFrom(p_70097_1_, p_70097_2_);
			}
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_,
			float p_82196_2_) {
		func_82216_a(0, p_82196_1_);
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	@Override
	public void despawnEntity() {
		entityAge = 0;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		func_145779_a(Items.nether_star, 1);

		if (!worldObj.isClient) {
			final Iterator var3 = worldObj.getEntitiesWithinAABB(
					EntityPlayer.class,
					boundingBox.expand(50.0D, 100.0D, 50.0D)).iterator();

			while (var3.hasNext()) {
				final EntityPlayer var4 = (EntityPlayer) var3.next();
				var4.triggerAchievement(AchievementList.field_150964_J);
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(0));
		dataWatcher.addObject(19, new Integer(0));
		dataWatcher.addObject(20, new Integer(0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
	}

	private float func_82204_b(float p_82204_1_, float p_82204_2_,
			float p_82204_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);

		if (var4 > p_82204_3_) {
			var4 = p_82204_3_;
		}

		if (var4 < -p_82204_3_) {
			var4 = -p_82204_3_;
		}

		return p_82204_1_ + var4;
	}

	public void func_82206_m() {
		func_82215_s(220);
		setHealth(getMaxHealth() / 3.0F);
	}

	public float func_82207_a(int p_82207_1_) {
		return field_82221_e[p_82207_1_];
	}

	private double func_82208_v(int p_82208_1_) {
		return p_82208_1_ <= 0 ? posY + 3.0D : posY + 2.2D;
	}

	private void func_82209_a(int p_82209_1_, double p_82209_2_,
			double p_82209_4_, double p_82209_6_, boolean p_82209_8_) {
		worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1014, (int) posX,
				(int) posY, (int) posZ, 0);
		final double var9 = func_82214_u(p_82209_1_);
		final double var11 = func_82208_v(p_82209_1_);
		final double var13 = func_82213_w(p_82209_1_);
		final double var15 = p_82209_2_ - var9;
		final double var17 = p_82209_4_ - var11;
		final double var19 = p_82209_6_ - var13;
		final EntityWitherSkull var21 = new EntityWitherSkull(worldObj, this,
				var15, var17, var19);

		if (p_82209_8_) {
			var21.setInvulnerable(true);
		}

		var21.posY = var11;
		var21.posX = var9;
		var21.posZ = var13;
		worldObj.spawnEntityInWorld(var21);
	}

	public float func_82210_r(int p_82210_1_) {
		return field_82220_d[p_82210_1_];
	}

	public void func_82211_c(int p_82211_1_, int p_82211_2_) {
		dataWatcher.updateObject(17 + p_82211_1_, Integer.valueOf(p_82211_2_));
	}

	public int func_82212_n() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	private double func_82213_w(int p_82213_1_) {
		if (p_82213_1_ <= 0)
			return posZ;
		else {
			final float var2 = (renderYawOffset + 180 * (p_82213_1_ - 1))
					/ 180.0F * (float) Math.PI;
			final float var3 = MathHelper.sin(var2);
			return posZ + var3 * 1.3D;
		}
	}

	private double func_82214_u(int p_82214_1_) {
		if (p_82214_1_ <= 0)
			return posX;
		else {
			final float var2 = (renderYawOffset + 180 * (p_82214_1_ - 1))
					/ 180.0F * (float) Math.PI;
			final float var3 = MathHelper.cos(var2);
			return posX + var3 * 1.3D;
		}
	}

	public void func_82215_s(int p_82215_1_) {
		dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
	}

	private void func_82216_a(int p_82216_1_, EntityLivingBase p_82216_2_) {
		func_82209_a(p_82216_1_, p_82216_2_.posX,
				p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5D,
				p_82216_2_.posZ, p_82216_1_ == 0 && rand.nextFloat() < 0.001F);
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.wither.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.wither.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.wither.idle";
	}

	@Override
	public float getShadowSize() {
		return height / 8.0F;
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue() {
		return 4;
	}

	/**
	 * Returns the target entity ID if present, or -1 if not @param par1 The
	 * target offset, should be from 0-2
	 */
	public int getWatchedTargetId(int p_82203_1_) {
		return dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * Returns whether the wither is armored with its boss armor or not by
	 * checking whether its health is below half of its maximum.
	 */
	public boolean isArmored() {
		return getHealth() <= getMaxHealth() / 2.0F;
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	@Override
	public void mountEntity(Entity p_70078_1_) {
		ridingEntity = null;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		motionY *= 0.6000000238418579D;
		double var4;
		double var6;
		double var8;

		if (!worldObj.isClient && getWatchedTargetId(0) > 0) {
			final Entity var1 = worldObj.getEntityByID(getWatchedTargetId(0));

			if (var1 != null) {
				if (posY < var1.posY || !isArmored() && posY < var1.posY + 5.0D) {
					if (motionY < 0.0D) {
						motionY = 0.0D;
					}

					motionY += (0.5D - motionY) * 0.6000000238418579D;
				}

				final double var2 = var1.posX - posX;
				var4 = var1.posZ - posZ;
				var6 = var2 * var2 + var4 * var4;

				if (var6 > 9.0D) {
					var8 = MathHelper.sqrt_double(var6);
					motionX += (var2 / var8 * 0.5D - motionX) * 0.6000000238418579D;
					motionZ += (var4 / var8 * 0.5D - motionZ) * 0.6000000238418579D;
				}
			}
		}

		if (motionX * motionX + motionZ * motionZ > 0.05000000074505806D) {
			rotationYaw = (float) Math.atan2(motionZ, motionX)
					* (180F / (float) Math.PI) - 90.0F;
		}

		super.onLivingUpdate();
		int var20;

		for (var20 = 0; var20 < 2; ++var20) {
			field_82218_g[var20] = field_82221_e[var20];
			field_82217_f[var20] = field_82220_d[var20];
		}

		int var22;

		for (var20 = 0; var20 < 2; ++var20) {
			var22 = getWatchedTargetId(var20 + 1);
			Entity var3 = null;

			if (var22 > 0) {
				var3 = worldObj.getEntityByID(var22);
			}

			if (var3 != null) {
				var4 = func_82214_u(var20 + 1);
				var6 = func_82208_v(var20 + 1);
				var8 = func_82213_w(var20 + 1);
				final double var10 = var3.posX - var4;
				final double var12 = var3.posY + var3.getEyeHeight() - var6;
				final double var14 = var3.posZ - var8;
				final double var16 = MathHelper.sqrt_double(var10 * var10
						+ var14 * var14);
				final float var18 = (float) (Math.atan2(var14, var10) * 180.0D / Math.PI) - 90.0F;
				final float var19 = (float) -(Math.atan2(var12, var16) * 180.0D / Math.PI);
				field_82220_d[var20] = func_82204_b(field_82220_d[var20],
						var19, 40.0F);
				field_82221_e[var20] = func_82204_b(field_82221_e[var20],
						var18, 10.0F);
			} else {
				field_82221_e[var20] = func_82204_b(field_82221_e[var20],
						renderYawOffset, 10.0F);
			}
		}

		final boolean var21 = isArmored();

		for (var22 = 0; var22 < 3; ++var22) {
			final double var23 = func_82214_u(var22);
			final double var5 = func_82208_v(var22);
			final double var7 = func_82213_w(var22);
			worldObj.spawnParticle("smoke", var23 + rand.nextGaussian()
					* 0.30000001192092896D, var5 + rand.nextGaussian()
					* 0.30000001192092896D, var7 + rand.nextGaussian()
					* 0.30000001192092896D, 0.0D, 0.0D, 0.0D);

			if (var21 && worldObj.rand.nextInt(4) == 0) {
				worldObj.spawnParticle("mobSpell", var23 + rand.nextGaussian()
						* 0.30000001192092896D, var5 + rand.nextGaussian()
						* 0.30000001192092896D, var7 + rand.nextGaussian()
						* 0.30000001192092896D, 0.699999988079071D,
						0.699999988079071D, 0.5D);
			}
		}

		if (func_82212_n() > 0) {
			for (var22 = 0; var22 < 3; ++var22) {
				worldObj.spawnParticle("mobSpell", posX + rand.nextGaussian()
						* 1.0D, posY + rand.nextFloat() * 3.3F,
						posZ + rand.nextGaussian() * 1.0D, 0.699999988079071D,
						0.699999988079071D, 0.8999999761581421D);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		func_82215_s(p_70037_1_.getInteger("Invul"));
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	@Override
	public void setInWeb() {
	}

	@Override
	protected void updateAITasks() {
		int var1;

		if (func_82212_n() > 0) {
			var1 = func_82212_n() - 1;

			if (var1 <= 0) {
				worldObj.newExplosion(this, posX, posY + getEyeHeight(), posZ,
						7.0F, false, worldObj.getGameRules()
								.getGameRuleBooleanValue("mobGriefing"));
				worldObj.playBroadcastSound(1013, (int) posX, (int) posY,
						(int) posZ, 0);
			}

			func_82215_s(var1);

			if (ticksExisted % 10 == 0) {
				heal(10.0F);
			}
		} else {
			super.updateAITasks();
			int var12;

			for (var1 = 1; var1 < 3; ++var1) {
				if (ticksExisted >= field_82223_h[var1 - 1]) {
					field_82223_h[var1 - 1] = ticksExisted + 10
							+ rand.nextInt(10);

					if (worldObj.difficultySetting == EnumDifficulty.NORMAL
							|| worldObj.difficultySetting == EnumDifficulty.HARD) {
						final int var10001 = var1 - 1;
						final int var10003 = field_82224_i[var1 - 1];
						field_82224_i[var10001] = field_82224_i[var1 - 1] + 1;

						if (var10003 > 15) {
							final float var2 = 10.0F;
							final float var3 = 5.0F;
							final double var4 = MathHelper
									.getRandomDoubleInRange(rand, posX - var2,
											posX + var2);
							final double var6 = MathHelper
									.getRandomDoubleInRange(rand, posY - var3,
											posY + var3);
							final double var8 = MathHelper
									.getRandomDoubleInRange(rand, posZ - var2,
											posZ + var2);
							func_82209_a(var1 + 1, var4, var6, var8, true);
							field_82224_i[var1 - 1] = 0;
						}
					}

					var12 = getWatchedTargetId(var1);

					if (var12 > 0) {
						final Entity var14 = worldObj.getEntityByID(var12);

						if (var14 != null && var14.isEntityAlive()
								&& getDistanceSqToEntity(var14) <= 900.0D
								&& canEntityBeSeen(var14)) {
							func_82216_a(var1 + 1, (EntityLivingBase) var14);
							field_82223_h[var1 - 1] = ticksExisted + 40
									+ rand.nextInt(20);
							field_82224_i[var1 - 1] = 0;
						} else {
							func_82211_c(var1, 0);
						}
					} else {
						final List var13 = worldObj.selectEntitiesWithinAABB(
								EntityLivingBase.class,
								boundingBox.expand(20.0D, 8.0D, 20.0D),
								attackEntitySelector);

						for (int var16 = 0; var16 < 10 && !var13.isEmpty(); ++var16) {
							final EntityLivingBase var5 = (EntityLivingBase) var13
									.get(rand.nextInt(var13.size()));

							if (var5 != this && var5.isEntityAlive()
									&& canEntityBeSeen(var5)) {
								if (var5 instanceof EntityPlayer) {
									if (!((EntityPlayer) var5).capabilities.disableDamage) {
										func_82211_c(var1, var5.getEntityId());
									}
								} else {
									func_82211_c(var1, var5.getEntityId());
								}

								break;
							}

							var13.remove(var5);
						}
					}
				}
			}

			if (getAttackTarget() != null) {
				func_82211_c(0, getAttackTarget().getEntityId());
			} else {
				func_82211_c(0, 0);
			}

			if (field_82222_j > 0) {
				--field_82222_j;

				if (field_82222_j == 0
						&& worldObj.getGameRules().getGameRuleBooleanValue(
								"mobGriefing")) {
					var1 = MathHelper.floor_double(posY);
					var12 = MathHelper.floor_double(posX);
					final int var15 = MathHelper.floor_double(posZ);
					boolean var17 = false;

					for (int var18 = -1; var18 <= 1; ++var18) {
						for (int var19 = -1; var19 <= 1; ++var19) {
							for (int var7 = 0; var7 <= 3; ++var7) {
								final int var20 = var12 + var18;
								final int var9 = var1 + var7;
								final int var10 = var15 + var19;
								final Block var11 = worldObj.getBlock(var20,
										var9, var10);

								if (var11.getMaterial() != Material.air
										&& var11 != Blocks.bedrock
										&& var11 != Blocks.end_portal
										&& var11 != Blocks.end_portal_frame
										&& var11 != Blocks.command_block) {
									var17 = worldObj.func_147480_a(var20, var9,
											var10, true) || var17;
								}
							}
						}
					}

					if (var17) {
						worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1012,
								(int) posX, (int) posY, (int) posZ, 0);
					}
				}
			}

			if (ticksExisted % 20 == 0) {
				heal(1.0F);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Invul", func_82212_n());
	}
}

package net.minecraft.entity.monster;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
	private final EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(
			this, 1.0D, 20, 60, 15.0F);
	private final EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(
			this, EntityPlayer.class, 1.2D, false);

	public EntitySkeleton(World p_i1741_1_) {
		super(p_i1741_1_);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIRestrictSun(this));
		tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class,
				8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
				EntityPlayer.class, 0, true));

		if (p_i1741_1_ != null && !p_i1741_1_.isClient) {
			setCombatTask();
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor() {
		super.addRandomArmor();
		setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.25D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		if (super.attackEntityAsMob(p_70652_1_)) {
			if (getSkeletonType() == 1
					&& p_70652_1_ instanceof EntityLivingBase) {
				((EntityLivingBase) p_70652_1_)
						.addPotionEffect(new PotionEffect(Potion.wither.id, 200));
			}

			return true;
		} else
			return false;
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_,
			float p_82196_2_) {
		final EntityArrow var3 = new EntityArrow(worldObj, this, p_82196_1_,
				1.6F, 14 - worldObj.difficultySetting.getDifficultyId() * 4);
		final int var4 = EnchantmentHelper.getEnchantmentLevel(
				Enchantment.power.effectId, getHeldItem());
		final int var5 = EnchantmentHelper.getEnchantmentLevel(
				Enchantment.punch.effectId, getHeldItem());
		var3.setDamage(p_82196_2_ * 2.0F + rand.nextGaussian() * 0.25D
				+ worldObj.difficultySetting.getDifficultyId() * 0.11F);

		if (var4 > 0) {
			var3.setDamage(var3.getDamage() + var4 * 0.5D + 0.5D);
		}

		if (var5 > 0) {
			var3.setKnockbackStrength(var5);
		}

		if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId,
				getHeldItem()) > 0 || getSkeletonType() == 1) {
			var3.setFire(100);
		}

		playSound("random.bow", 1.0F,
				1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
		worldObj.spawnEntityInWorld(var3);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int var3;
		int var4;

		if (getSkeletonType() == 1) {
			var3 = rand.nextInt(3 + p_70628_2_) - 1;

			for (var4 = 0; var4 < var3; ++var4) {
				func_145779_a(Items.coal, 1);
			}
		} else {
			var3 = rand.nextInt(3 + p_70628_2_);

			for (var4 = 0; var4 < var3; ++var4) {
				func_145779_a(Items.arrow, 1);
			}
		}

		var3 = rand.nextInt(3 + p_70628_2_);

		for (var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.bone, 1);
		}
	}

	@Override
	protected void dropRareDrop(int p_70600_1_) {
		if (getSkeletonType() == 1) {
			entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(13, new Byte((byte) 0));
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.skeleton.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Items.arrow;
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
		return "mob.skeleton.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}

	/**
	 * Return this skeleton's type.
	 */
	public int getSkeletonType() {
		return dataWatcher.getWatchableObjectByte(13);
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset() {
		return super.getYOffset() - 0.5D;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (p_70645_1_.getSourceOfDamage() instanceof EntityArrow
				&& p_70645_1_.getEntity() instanceof EntityPlayer) {
			final EntityPlayer var2 = (EntityPlayer) p_70645_1_.getEntity();
			final double var3 = var2.posX - posX;
			final double var5 = var2.posZ - posZ;

			if (var3 * var3 + var5 * var5 >= 2500.0D) {
				var2.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (worldObj.isDaytime() && !worldObj.isClient) {
			final float var1 = getBrightness(1.0F);

			if (var1 > 0.5F
					&& rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F
					&& worldObj.canBlockSeeTheSky(
							MathHelper.floor_double(posX),
							MathHelper.floor_double(posY),
							MathHelper.floor_double(posZ))) {
				boolean var2 = true;
				final ItemStack var3 = getEquipmentInSlot(4);

				if (var3 != null) {
					if (var3.isItemStackDamageable()) {
						var3.setItemDamage(var3.getItemDamageForDisplay()
								+ rand.nextInt(2));

						if (var3.getItemDamageForDisplay() >= var3
								.getMaxDamage()) {
							renderBrokenItemStack(var3);
							setCurrentItemOrArmor(4, (ItemStack) null);
						}
					}

					var2 = false;
				}

				if (var2) {
					setFire(8);
				}
			}
		}

		if (worldObj.isClient && getSkeletonType() == 1) {
			setSize(0.72F, 2.34F);
		}

		super.onLivingUpdate();
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

		if (worldObj.provider instanceof WorldProviderHell
				&& getRNG().nextInt(5) > 0) {
			tasks.addTask(4, aiAttackOnCollide);
			setSkeletonType(1);
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			getEntityAttribute(SharedMonsterAttributes.attackDamage)
					.setBaseValue(4.0D);
		} else {
			tasks.addTask(4, aiArrowAttack);
			addRandomArmor();
			enchantEquipment();
		}

		setCanPickUpLoot(rand.nextFloat() < 0.55F * worldObj.func_147462_b(
				posX, posY, posZ));

		if (getEquipmentInSlot(4) == null) {
			final Calendar var2 = worldObj.getCurrentDate();

			if (var2.get(2) + 1 == 10 && var2.get(5) == 31
					&& rand.nextFloat() < 0.25F) {
				setCurrentItemOrArmor(4, new ItemStack(
						rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin
								: Blocks.pumpkin));
				equipmentDropChances[4] = 0.0F;
			}
		}

		return p_110161_1_;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.func_150297_b("SkeletonType", 99)) {
			final byte var2 = p_70037_1_.getByte("SkeletonType");
			setSkeletonType(var2);
		}

		setCombatTask();
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask() {
		tasks.removeTask(aiAttackOnCollide);
		tasks.removeTask(aiArrowAttack);
		final ItemStack var1 = getHeldItem();

		if (var1 != null && var1.getItem() == Items.bow) {
			tasks.addTask(4, aiArrowAttack);
		} else {
			tasks.addTask(4, aiAttackOnCollide);
		}
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		super.setCurrentItemOrArmor(p_70062_1_, p_70062_2_);

		if (!worldObj.isClient && p_70062_1_ == 0) {
			setCombatTask();
		}
	}

	/**
	 * Set this skeleton's type.
	 */
	public void setSkeletonType(int p_82201_1_) {
		dataWatcher.updateObject(13, Byte.valueOf((byte) p_82201_1_));
		isImmuneToFire = p_82201_1_ == 1;

		if (p_82201_1_ == 1) {
			setSize(0.72F, 2.34F);
		} else {
			setSize(0.6F, 1.8F);
		}
	}

	/**
	 * Handles updating while being ridden by an entity
	 */
	@Override
	public void updateRidden() {
		super.updateRidden();

		if (ridingEntity instanceof EntityCreature) {
			final EntityCreature var1 = (EntityCreature) ridingEntity;
			renderYawOffset = var1.renderYawOffset;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setByte("SkeletonType", (byte) getSkeletonType());
	}
}

package net.minecraft.entity.monster;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityZombie extends EntityMob {
	class GroupData implements IEntityLivingData {
		public boolean field_142046_b;
		public boolean field_142048_a;

		private GroupData(boolean p_i2348_2_, boolean p_i2348_3_) {
			field_142048_a = false;
			field_142046_b = false;
			field_142048_a = p_i2348_2_;
			field_142046_b = p_i2348_3_;
		}

		GroupData(boolean p_i2349_2_, boolean p_i2349_3_, Object p_i2349_4_) {
			this(p_i2349_2_, p_i2349_3_);
		}
	}

	private static final UUID babySpeedBoostUUID = UUID
			.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
	private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(
			babySpeedBoostUUID, "Baby speed boost", 0.5D, 1);
	protected static final IAttribute field_110186_bp = new RangedAttribute(
			"zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)
			.setDescription("Spawn Reinforcements Chance");

	/**
	 * Ticker used to determine the time remaining for this zombie to convert
	 * into a villager when cured.
	 */
	private int conversionTime;
	private float field_146073_bw;
	private float field_146074_bv = -1.0F;
	private final EntityAIBreakDoor field_146075_bs = new EntityAIBreakDoor(
			this);

	private boolean field_146076_bu = false;

	public EntityZombie(World p_i1745_1_) {
		super(p_i1745_1_);
		getNavigator().setBreakDoors(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class,
				1.0D, false));
		tasks.addTask(4, new EntityAIAttackOnCollide(this,
				EntityVillager.class, 1.0D, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		tasks.addTask(7, new EntityAIWander(this, 1.0D));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class,
				8.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
				EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
				EntityVillager.class, 0, false));
		setSize(0.6F, 1.8F);
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor() {
		super.addRandomArmor();

		if (rand.nextFloat() < (worldObj.difficultySetting == EnumDifficulty.HARD ? 0.05F
				: 0.01F)) {
			final int var1 = rand.nextInt(3);

			if (var1 == 0) {
				setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
			} else {
				setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
			}
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(
				40.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.23000000417232513D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(
				3.0D);
		getAttributeMap().registerAttribute(field_110186_bp).setBaseValue(
				rand.nextDouble() * 0.10000000149011612D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		final boolean var2 = super.attackEntityAsMob(p_70652_1_);

		if (var2) {
			final int var3 = worldObj.difficultySetting.getDifficultyId();

			if (getHeldItem() == null && isBurning()
					&& rand.nextFloat() < var3 * 0.3F) {
				p_70652_1_.setFire(2 * var3);
			}
		}

		return var2;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (!super.attackEntityFrom(p_70097_1_, p_70097_2_))
			return false;
		else {
			EntityLivingBase var3 = getAttackTarget();

			if (var3 == null && getEntityToAttack() instanceof EntityLivingBase) {
				var3 = (EntityLivingBase) getEntityToAttack();
			}

			if (var3 == null
					&& p_70097_1_.getEntity() instanceof EntityLivingBase) {
				var3 = (EntityLivingBase) p_70097_1_.getEntity();
			}

			if (var3 != null
					&& worldObj.difficultySetting == EnumDifficulty.HARD
					&& rand.nextFloat() < getEntityAttribute(field_110186_bp)
							.getAttributeValue()) {
				final int var4 = MathHelper.floor_double(posX);
				final int var5 = MathHelper.floor_double(posY);
				final int var6 = MathHelper.floor_double(posZ);
				final EntityZombie var7 = new EntityZombie(worldObj);

				for (int var8 = 0; var8 < 50; ++var8) {
					final int var9 = var4
							+ MathHelper.getRandomIntegerInRange(rand, 7, 40)
							* MathHelper.getRandomIntegerInRange(rand, -1, 1);
					final int var10 = var5
							+ MathHelper.getRandomIntegerInRange(rand, 7, 40)
							* MathHelper.getRandomIntegerInRange(rand, -1, 1);
					final int var11 = var6
							+ MathHelper.getRandomIntegerInRange(rand, 7, 40)
							* MathHelper.getRandomIntegerInRange(rand, -1, 1);

					if (World.doesBlockHaveSolidTopSurface(worldObj, var9,
							var10 - 1, var11)
							&& worldObj.getBlockLightValue(var9, var10, var11) < 10) {
						var7.setPosition(var9, var10, var11);

						if (worldObj.checkNoEntityCollision(var7.boundingBox)
								&& worldObj.getCollidingBoundingBoxes(var7,
										var7.boundingBox).isEmpty()
								&& !worldObj.isAnyLiquid(var7.boundingBox)) {
							worldObj.spawnEntityInWorld(var7);
							var7.setAttackTarget(var3);
							var7.onSpawnWithEgg((IEntityLivingData) null);
							getEntityAttribute(field_110186_bp)
									.applyModifier(
											new AttributeModifier(
													"Zombie reinforcement caller charge",
													-0.05000000074505806D, 0));
							var7.getEntityAttribute(field_110186_bp)
									.applyModifier(
											new AttributeModifier(
													"Zombie reinforcement callee charge",
													-0.05000000074505806D, 0));
							break;
						}
					}
				}
			}

			return true;
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return !isConverting();
	}

	/**
	 * Convert this zombie into a villager.
	 */
	protected void convertToVillager() {
		final EntityVillager var1 = new EntityVillager(worldObj);
		var1.copyLocationAndAnglesFrom(this);
		var1.onSpawnWithEgg((IEntityLivingData) null);
		var1.setLookingForHome();

		if (isChild()) {
			var1.setGrowingAge(-24000);
		}

		worldObj.removeEntity(this);
		worldObj.spawnEntityInWorld(var1);
		var1.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
		worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1017, (int) posX,
				(int) posY, (int) posZ, 0);
	}

	@Override
	protected void dropRareDrop(int p_70600_1_) {
		switch (rand.nextInt(3)) {
		case 0:
			func_145779_a(Items.iron_ingot, 1);
			break;

		case 1:
			func_145779_a(Items.carrot, 1);
			break;

		case 2:
			func_145779_a(Items.potato, 1);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		getDataWatcher().addObject(12, Byte.valueOf((byte) 0));
		getDataWatcher().addObject(13, Byte.valueOf((byte) 0));
		getDataWatcher().addObject(14, Byte.valueOf((byte) 0));
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.zombie.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Items.rotten_flesh;
	}

	protected final void func_146069_a(float p_146069_1_) {
		super.setSize(field_146074_bv * p_146069_1_, field_146073_bw
				* p_146069_1_);
	}

	public void func_146070_a(boolean p_146070_1_) {
		if (field_146076_bu != p_146070_1_) {
			field_146076_bu = p_146070_1_;

			if (p_146070_1_) {
				tasks.addTask(1, field_146075_bs);
			} else {
				tasks.removeTask(field_146075_bs);
			}
		}
	}

	public void func_146071_k(boolean p_146071_1_) {
		func_146069_a(p_146071_1_ ? 0.5F : 1.0F);
	}

	public boolean func_146072_bX() {
		return field_146076_bu;
	}

	/**
	 * Return the amount of time decremented from conversionTime every tick.
	 */
	protected int getConversionTimeBoost() {
		int var1 = 1;

		if (rand.nextFloat() < 0.01F) {
			int var2 = 0;

			for (int var3 = (int) posX - 4; var3 < (int) posX + 4 && var2 < 14; ++var3) {
				for (int var4 = (int) posY - 4; var4 < (int) posY + 4
						&& var2 < 14; ++var4) {
					for (int var5 = (int) posZ - 4; var5 < (int) posZ + 4
							&& var2 < 14; ++var5) {
						final Block var6 = worldObj.getBlock(var3, var4, var5);

						if (var6 == Blocks.iron_bars || var6 == Blocks.bed) {
							if (rand.nextFloat() < 0.3F) {
								++var1;
							}

							++var2;
						}
					}
				}
			}
		}

		return var1;
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
		return "mob.zombie.death";
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		if (isChild()) {
			experienceValue = (int) (experienceValue * 2.5F);
		}

		return super.getExperiencePoints(p_70693_1_);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.zombie.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.zombie.say";
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue() {
		int var1 = super.getTotalArmorValue() + 2;

		if (var1 > 20) {
			var1 = 20;
		}

		return var1;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 16) {
			worldObj.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D,
					"mob.zombie.remedy", 1.0F + rand.nextFloat(),
					rand.nextFloat() * 0.7F + 0.3F, false);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.getCurrentEquippedItem();

		if (var2 != null && var2.getItem() == Items.golden_apple
				&& var2.getItemDamage() == 0 && isVillager()
				&& this.isPotionActive(Potion.weakness)) {
			if (!p_70085_1_.capabilities.isCreativeMode) {
				--var2.stackSize;
			}

			if (var2.stackSize <= 0) {
				p_70085_1_.inventory.setInventorySlotContents(
						p_70085_1_.inventory.currentItem, (ItemStack) null);
			}

			if (!worldObj.isClient) {
				startConversion(rand.nextInt(2401) + 3600);
			}

			return true;
		} else
			return false;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	@Override
	public boolean isChild() {
		return getDataWatcher().getWatchableObjectByte(12) == 1;
	}

	/**
	 * Returns whether this zombie is in the process of converting to a villager
	 */
	public boolean isConverting() {
		return getDataWatcher().getWatchableObjectByte(14) == 1;
	}

	/**
	 * Return whether this zombie is a villager.
	 */
	public boolean isVillager() {
		return getDataWatcher().getWatchableObjectByte(13) == 1;
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	@Override
	public void onKillEntity(EntityLivingBase p_70074_1_) {
		super.onKillEntity(p_70074_1_);

		if ((worldObj.difficultySetting == EnumDifficulty.NORMAL || worldObj.difficultySetting == EnumDifficulty.HARD)
				&& p_70074_1_ instanceof EntityVillager) {
			if (worldObj.difficultySetting != EnumDifficulty.HARD
					&& rand.nextBoolean())
				return;

			final EntityZombie var2 = new EntityZombie(worldObj);
			var2.copyLocationAndAnglesFrom(p_70074_1_);
			worldObj.removeEntity(p_70074_1_);
			var2.onSpawnWithEgg((IEntityLivingData) null);
			var2.setVillager(true);

			if (p_70074_1_.isChild()) {
				var2.setChild(true);
			}

			worldObj.spawnEntityInWorld(var2);
			worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, (int) posX,
					(int) posY, (int) posZ, 0);
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (worldObj.isDaytime() && !worldObj.isClient && !isChild()) {
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

		if (isRiding() && getAttackTarget() != null
				&& ridingEntity instanceof EntityChicken) {
			((EntityLiving) ridingEntity).getNavigator().setPath(
					getNavigator().getPath(), 1.5D);
		}

		super.onLivingUpdate();
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
		final float var2 = worldObj.func_147462_b(posX, posY, posZ);
		setCanPickUpLoot(rand.nextFloat() < 0.55F * var2);

		if (p_110161_1_1 == null) {
			p_110161_1_1 = new EntityZombie.GroupData(
					worldObj.rand.nextFloat() < 0.05F,
					worldObj.rand.nextFloat() < 0.05F, null);
		}

		if (p_110161_1_1 instanceof EntityZombie.GroupData) {
			final EntityZombie.GroupData var3 = (EntityZombie.GroupData) p_110161_1_1;

			if (var3.field_142046_b) {
				setVillager(true);
			}

			if (var3.field_142048_a) {
				setChild(true);

				if (worldObj.rand.nextFloat() < 0.05D) {
					final List var4 = worldObj.selectEntitiesWithinAABB(
							EntityChicken.class,
							boundingBox.expand(5.0D, 3.0D, 5.0D),
							IEntitySelector.field_152785_b);

					if (!var4.isEmpty()) {
						final EntityChicken var5 = (EntityChicken) var4.get(0);
						var5.func_152117_i(true);
						mountEntity(var5);
					}
				} else if (worldObj.rand.nextFloat() < 0.05D) {
					final EntityChicken var9 = new EntityChicken(worldObj);
					var9.setLocationAndAngles(posX, posY, posZ, rotationYaw,
							0.0F);
					var9.onSpawnWithEgg((IEntityLivingData) null);
					var9.func_152117_i(true);
					worldObj.spawnEntityInWorld(var9);
					mountEntity(var9);
				}
			}
		}

		func_146070_a(rand.nextFloat() < var2 * 0.1F);
		addRandomArmor();
		enchantEquipment();

		if (getEquipmentInSlot(4) == null) {
			final Calendar var7 = worldObj.getCurrentDate();

			if (var7.get(2) + 1 == 10 && var7.get(5) == 31
					&& rand.nextFloat() < 0.25F) {
				setCurrentItemOrArmor(4, new ItemStack(
						rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin
								: Blocks.pumpkin));
				equipmentDropChances[4] = 0.0F;
			}
		}

		getEntityAttribute(SharedMonsterAttributes.knockbackResistance)
				.applyModifier(
						new AttributeModifier("Random spawn bonus", rand
								.nextDouble() * 0.05000000074505806D, 0));
		final double var8 = rand.nextDouble() * 1.5D
				* worldObj.func_147462_b(posX, posY, posZ);

		if (var8 > 1.0D) {
			getEntityAttribute(SharedMonsterAttributes.followRange)
					.applyModifier(
							new AttributeModifier("Random zombie-spawn bonus",
									var8, 2));
		}

		if (rand.nextFloat() < var2 * 0.05F) {
			getEntityAttribute(field_110186_bp).applyModifier(
					new AttributeModifier("Leader zombie bonus", rand
							.nextDouble() * 0.25D + 0.5D, 0));
			getEntityAttribute(SharedMonsterAttributes.maxHealth)
					.applyModifier(
							new AttributeModifier("Leader zombie bonus", rand
									.nextDouble() * 3.0D + 1.0D, 2));
			func_146070_a(true);
		}

		return (IEntityLivingData) p_110161_1_1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (!worldObj.isClient && isConverting()) {
			final int var1 = getConversionTimeBoost();
			conversionTime -= var1;

			if (conversionTime <= 0) {
				convertToVillager();
			}
		}

		super.onUpdate();
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);

		if (p_70037_1_.getBoolean("IsBaby")) {
			setChild(true);
		}

		if (p_70037_1_.getBoolean("IsVillager")) {
			setVillager(true);
		}

		if (p_70037_1_.func_150297_b("ConversionTime", 99)
				&& p_70037_1_.getInteger("ConversionTime") > -1) {
			startConversion(p_70037_1_.getInteger("ConversionTime"));
		}

		func_146070_a(p_70037_1_.getBoolean("CanBreakDoors"));
	}

	/**
	 * Set whether this zombie is a child.
	 */
	public void setChild(boolean p_82227_1_) {
		getDataWatcher().updateObject(12,
				Byte.valueOf((byte) (p_82227_1_ ? 1 : 0)));

		if (worldObj != null && !worldObj.isClient) {
			final IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			var2.removeModifier(babySpeedBoostModifier);

			if (p_82227_1_) {
				var2.applyModifier(babySpeedBoostModifier);
			}
		}

		func_146071_k(p_82227_1_);
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	@Override
	protected final void setSize(float p_70105_1_, float p_70105_2_) {
		final boolean var3 = field_146074_bv > 0.0F && field_146073_bw > 0.0F;
		field_146074_bv = p_70105_1_;
		field_146073_bw = p_70105_2_;

		if (!var3) {
			func_146069_a(1.0F);
		}
	}

	/**
	 * Set whether this zombie is a villager.
	 */
	public void setVillager(boolean p_82229_1_) {
		getDataWatcher().updateObject(13,
				Byte.valueOf((byte) (p_82229_1_ ? 1 : 0)));
	}

	/**
	 * Starts converting this zombie into a villager. The zombie converts into a
	 * villager after the specified time in ticks.
	 */
	protected void startConversion(int p_82228_1_) {
		conversionTime = p_82228_1_;
		getDataWatcher().updateObject(14, Byte.valueOf((byte) 1));
		removePotionEffect(Potion.weakness.id);
		addPotionEffect(new PotionEffect(Potion.damageBoost.id, p_82228_1_,
				Math.min(worldObj.difficultySetting.getDifficultyId() - 1, 0)));
		worldObj.setEntityState(this, (byte) 16);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (isChild()) {
			p_70014_1_.setBoolean("IsBaby", true);
		}

		if (isVillager()) {
			p_70014_1_.setBoolean("IsVillager", true);
		}

		p_70014_1_.setInteger("ConversionTime", isConverting() ? conversionTime
				: -1);
		p_70014_1_.setBoolean("CanBreakDoors", func_146072_bX());
	}
}

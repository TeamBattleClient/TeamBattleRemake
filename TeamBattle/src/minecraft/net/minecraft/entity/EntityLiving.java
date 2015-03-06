package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityLiving extends EntityLivingBase {
	/**
	 * Params: Armor slot, Item tier
	 */
	public static Item getArmorItemForSlot(int p_82161_0_, int p_82161_1_) {
		switch (p_82161_0_) {
		case 4:
			if (p_82161_1_ == 0)
				return Items.leather_helmet;
			else if (p_82161_1_ == 1)
				return Items.golden_helmet;
			else if (p_82161_1_ == 2)
				return Items.chainmail_helmet;
			else if (p_82161_1_ == 3)
				return Items.iron_helmet;
			else if (p_82161_1_ == 4)
				return Items.diamond_helmet;

		case 3:
			if (p_82161_1_ == 0)
				return Items.leather_chestplate;
			else if (p_82161_1_ == 1)
				return Items.golden_chestplate;
			else if (p_82161_1_ == 2)
				return Items.chainmail_chestplate;
			else if (p_82161_1_ == 3)
				return Items.iron_chestplate;
			else if (p_82161_1_ == 4)
				return Items.diamond_chestplate;

		case 2:
			if (p_82161_1_ == 0)
				return Items.leather_leggings;
			else if (p_82161_1_ == 1)
				return Items.golden_leggings;
			else if (p_82161_1_ == 2)
				return Items.chainmail_leggings;
			else if (p_82161_1_ == 3)
				return Items.iron_leggings;
			else if (p_82161_1_ == 4)
				return Items.diamond_leggings;

		case 1:
			if (p_82161_1_ == 0)
				return Items.leather_boots;
			else if (p_82161_1_ == 1)
				return Items.golden_boots;
			else if (p_82161_1_ == 2)
				return Items.chainmail_boots;
			else if (p_82161_1_ == 3)
				return Items.iron_boots;
			else if (p_82161_1_ == 4)
				return Items.diamond_boots;

		default:
			return null;
		}
	}

	public static int getArmorPosition(ItemStack p_82159_0_) {
		if (p_82159_0_.getItem() != Item.getItemFromBlock(Blocks.pumpkin)
				&& p_82159_0_.getItem() != Items.skull) {
			if (p_82159_0_.getItem() instanceof ItemArmor) {
				switch (((ItemArmor) p_82159_0_.getItem()).armorType) {
				case 0:
					return 4;

				case 1:
					return 3;

				case 2:
					return 2;

				case 3:
					return 1;
				}
			}

			return 0;
		} else
			return 4;
	}

	/** The active target the Task system uses for tracking */
	private EntityLivingBase attackTarget;
	private final EntityBodyHelper bodyHelper;

	/** Whether this entity can pick up items from the ground. */
	private boolean canPickUpLoot;
	/** This entity's current target. */
	private Entity currentTarget;
	protected float defaultPitch;
	/** Equipment (armor and held item) for this entity. */
	private final ItemStack[] equipment = new ItemStack[5];
	/** Chances for each equipment piece from dropping when this entity dies. */
	protected float[] equipmentDropChances = new float[5];

	/** The experience points the Entity gives. */
	protected int experienceValue;
	private NBTTagCompound field_110170_bx;

	private boolean isLeashed;

	/** Entity jumping helper */
	private final EntityJumpHelper jumpHelper;

	private Entity leashedToEntity;

	/** Number of ticks since this EntityLiving last produced its sound */
	public int livingSoundTime;
	private final EntityLookHelper lookHelper;

	private final EntityMoveHelper moveHelper;

	private final PathNavigate navigator;
	/** How long to keep a specific target entity */
	protected int numTicksToChaseTarget;
	/** Whether this entity should NOT despawn. */
	private boolean persistenceRequired;
	private final EntitySenses senses;

	protected final EntityAITasks targetTasks;

	protected final EntityAITasks tasks;

	public EntityLiving(World p_i1595_1_) {
		super(p_i1595_1_);
		tasks = new EntityAITasks(p_i1595_1_ != null
				&& p_i1595_1_.theProfiler != null ? p_i1595_1_.theProfiler
				: null);
		targetTasks = new EntityAITasks(p_i1595_1_ != null
				&& p_i1595_1_.theProfiler != null ? p_i1595_1_.theProfiler
				: null);
		lookHelper = new EntityLookHelper(this);
		moveHelper = new EntityMoveHelper(this);
		jumpHelper = new EntityJumpHelper(this);
		bodyHelper = new EntityBodyHelper(this);
		navigator = new PathNavigate(this, p_i1595_1_);
		senses = new EntitySenses(this);

		for (int var2 = 0; var2 < equipmentDropChances.length; ++var2) {
			equipmentDropChances[var2] = 0.085F;
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	protected void addRandomArmor() {
		if (rand.nextFloat() < 0.15F * worldObj.func_147462_b(posX, posY, posZ)) {
			int var1 = rand.nextInt(2);
			final float var2 = worldObj.difficultySetting == EnumDifficulty.HARD ? 0.1F
					: 0.25F;

			if (rand.nextFloat() < 0.095F) {
				++var1;
			}

			if (rand.nextFloat() < 0.095F) {
				++var1;
			}

			if (rand.nextFloat() < 0.095F) {
				++var1;
			}

			for (int var3 = 3; var3 >= 0; --var3) {
				final ItemStack var4 = func_130225_q(var3);

				if (var3 < 3 && rand.nextFloat() < var2) {
					break;
				}

				if (var4 == null) {
					final Item var5 = getArmorItemForSlot(var3 + 1, var1);

					if (var5 != null) {
						setCurrentItemOrArmor(var3 + 1, new ItemStack(var5));
					}
				}
			}
		}
	}

	public boolean allowLeashing() {
		return !getLeashed() && !(this instanceof IMob);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap()
				.registerAttribute(SharedMonsterAttributes.followRange)
				.setBaseValue(16.0D);
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	public boolean canAttackClass(Class p_70686_1_) {
		return EntityCreeper.class != p_70686_1_
				&& EntityGhast.class != p_70686_1_;
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For
	 * pigs, this is true if it is being ridden by a player and the player is
	 * holding a carrot-on-a-stick
	 */
	public boolean canBeSteered() {
		return false;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return true;
	}

	public boolean canPickUpLoot() {
		return canPickUpLoot;
	}

	/**
	 * Removes the leash from this entity. Second parameter tells whether to
	 * send a packet to surrounding players.
	 */
	public void clearLeashed(boolean p_110160_1_, boolean p_110160_2_) {
		if (isLeashed) {
			isLeashed = false;
			leashedToEntity = null;

			if (!worldObj.isClient && p_110160_2_) {
				func_145779_a(Items.lead, 1);
			}

			if (!worldObj.isClient && p_110160_1_
					&& worldObj instanceof WorldServer) {
				((WorldServer) worldObj).getEntityTracker().func_151247_a(this,
						new S1BPacketEntityAttach(1, this, (Entity) null));
			}
		}
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	public void despawnEntity() {
		if (persistenceRequired) {
			entityAge = 0;
		} else {
			final EntityPlayer var1 = worldObj.getClosestPlayerToEntity(this,
					-1.0D);

			if (var1 != null) {
				final double var2 = var1.posX - posX;
				final double var4 = var1.posY - posY;
				final double var6 = var1.posZ - posZ;
				final double var8 = var2 * var2 + var4 * var4 + var6 * var6;

				if (canDespawn() && var8 > 16384.0D) {
					setDead();
				}

				if (entityAge > 600 && rand.nextInt(800) == 0 && var8 > 1024.0D
						&& canDespawn()) {
					setDead();
				} else if (var8 < 1024.0D) {
					entityAge = 0;
				}
			}
		}
	}

	/**
	 * Drop the equipment for this entity.
	 */
	@Override
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {
		for (int var3 = 0; var3 < getLastActiveItems().length; ++var3) {
			final ItemStack var4 = getEquipmentInSlot(var3);
			final boolean var5 = equipmentDropChances[var3] > 1.0F;

			if (var4 != null
					&& (p_82160_1_ || var5)
					&& rand.nextFloat() - p_82160_2_ * 0.01F < equipmentDropChances[var3]) {
				if (!var5 && var4.isItemStackDamageable()) {
					final int var6 = Math.max(var4.getMaxDamage() - 25, 1);
					int var7 = var4.getMaxDamage()
							- rand.nextInt(rand.nextInt(var6) + 1);

					if (var7 > var6) {
						var7 = var6;
					}

					if (var7 < 1) {
						var7 = 1;
					}

					var4.setItemDamage(var7);
				}

				entityDropItem(var4, 0.0F);
			}
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final Item var3 = func_146068_u();

		if (var3 != null) {
			int var4 = rand.nextInt(3);

			if (p_70628_2_ > 0) {
				var4 += rand.nextInt(p_70628_2_ + 1);
			}

			for (int var5 = 0; var5 < var4; ++var5) {
				func_145779_a(var3, 1);
			}
		}
	}

	/**
	 * This function applies the benefits of growing back wool and faster
	 * growing up to the acting entity. (This function is used in the
	 * AIEatGrass)
	 */
	public void eatGrassBonus() {
	}

	/**
	 * Enchants the entity's armor and held item based on difficulty
	 */
	protected void enchantEquipment() {
		final float var1 = worldObj.func_147462_b(posX, posY, posZ);

		if (getHeldItem() != null && rand.nextFloat() < 0.25F * var1) {
			EnchantmentHelper.addRandomEnchantment(rand, getHeldItem(),
					(int) (5.0F + var1 * rand.nextInt(18)));
		}

		for (int var2 = 0; var2 < 4; ++var2) {
			final ItemStack var3 = func_130225_q(var2);

			if (var3 != null && rand.nextFloat() < 0.5F * var1) {
				EnchantmentHelper.addRandomEnchantment(rand, var3,
						(int) (5.0F + var1 * rand.nextInt(18)));
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(11, Byte.valueOf((byte) 0));
		dataWatcher.addObject(10, "");
	}

	/**
	 * Changes pitch and yaw so that the entity calling the function is facing
	 * the entity provided as an argument.
	 */
	public void faceEntity(Entity p_70625_1_, float p_70625_2_, float p_70625_3_) {
		final double var4 = p_70625_1_.posX - posX;
		final double var8 = p_70625_1_.posZ - posZ;
		double var6;

		if (p_70625_1_ instanceof EntityLivingBase) {
			final EntityLivingBase var10 = (EntityLivingBase) p_70625_1_;
			var6 = var10.posY + var10.getEyeHeight() - (posY + getEyeHeight());
		} else {
			var6 = (p_70625_1_.boundingBox.minY + p_70625_1_.boundingBox.maxY)
					/ 2.0D - (posY + getEyeHeight());
		}

		final double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
		final float var12 = (float) (Math.atan2(var8, var4) * 180.0D / Math.PI) - 90.0F;
		final float var13 = (float) -(Math.atan2(var6, var14) * 180.0D / Math.PI);
		rotationPitch = updateRotation(rotationPitch, var13, p_70625_3_);
		rotationYaw = updateRotation(rotationYaw, var12, p_70625_2_);
	}

	@Override
	protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
		if (isAIEnabled()) {
			bodyHelper.func_75664_a();
			return p_110146_2_;
		} else
			return super.func_110146_f(p_110146_1_, p_110146_2_);
	}

	public void func_110163_bv() {
		persistenceRequired = true;
	}

	public ItemStack func_130225_q(int p_130225_1_) {
		return equipment[p_130225_1_ + 1];
	}

	protected Item func_146068_u() {
		return Item.getItemById(0);
	}

	public boolean getAlwaysRenderNameTag() {
		return dataWatcher.getWatchableObjectByte(11) == 1;
	}

	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return getAlwaysRenderNameTag();
	}

	/**
	 * Gets the active target the Task system uses for tracking
	 */
	public EntityLivingBase getAttackTarget() {
		return attackTarget;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	public boolean getCanSpawnHere() {
		return worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox)
						.isEmpty() && !worldObj.isAnyLiquid(boundingBox);
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return hasCustomNameTag() ? getCustomNameTag() : super
				.getCommandSenderName();
	}

	public String getCustomNameTag() {
		return dataWatcher.getWatchableObjectString(10);
	}

	/**
	 * returns the EntitySenses Object for the EntityLiving
	 */
	public EntitySenses getEntitySenses() {
		return senses;
	}

	/**
	 * 0: Tool in Hand; 1-4: Armor
	 */
	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		return equipment[p_71124_1_];
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		if (experienceValue > 0) {
			int var2 = experienceValue;
			final ItemStack[] var3 = getLastActiveItems();

			for (int var4 = 0; var4 < var3.length; ++var4) {
				if (var3[var4] != null && equipmentDropChances[var4] <= 1.0F) {
					var2 += 1 + rand.nextInt(3);
				}
			}

			return var2;
		} else
			return experienceValue;
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	@Override
	public ItemStack getHeldItem() {
		return equipment[0];
	}

	public EntityJumpHelper getJumpHelper() {
		return jumpHelper;
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return equipment;
	}

	public boolean getLeashed() {
		return isLeashed;
	}

	public Entity getLeashedToEntity() {
		return leashedToEntity;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return null;
	}

	public EntityLookHelper getLookHelper() {
		return lookHelper;
	}

	/**
	 * The number of iterations PathFinder.getSafePoint will execute before
	 * giving up.
	 */
	@Override
	public int getMaxSafePointTries() {
		if (getAttackTarget() == null)
			return 3;
		else {
			int var1 = (int) (getHealth() - getMaxHealth() * 0.33F);
			var1 -= (3 - worldObj.difficultySetting.getDifficultyId()) * 4;

			if (var1 < 0) {
				var1 = 0;
			}

			return var1 + 3;
		}
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	public int getMaxSpawnedInChunk() {
		return 4;
	}

	public EntityMoveHelper getMoveHelper() {
		return moveHelper;
	}

	public PathNavigate getNavigator() {
		return navigator;
	}

	/**
	 * Returns render size modifier
	 */
	public float getRenderSizeModifier() {
		return 1.0F;
	}

	/**
	 * Get number of ticks, at least during which the living entity will be
	 * silent.
	 */
	public int getTalkInterval() {
		return 80;
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the
	 * faceEntity method. This is only currently use in wolves.
	 */
	public int getVerticalFaceSpeed() {
		return 40;
	}

	public boolean hasCustomNameTag() {
		return dataWatcher.getWatchableObjectString(10).length() > 0;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	protected boolean interact(EntityPlayer p_70085_1_) {
		return false;
	}

	/**
	 * First layer of player interaction
	 */
	@Override
	public final boolean interactFirst(EntityPlayer p_130002_1_) {
		if (getLeashed() && getLeashedToEntity() == p_130002_1_) {
			clearLeashed(true, !p_130002_1_.capabilities.isCreativeMode);
			return true;
		} else {
			final ItemStack var2 = p_130002_1_.inventory.getCurrentItem();

			if (var2 != null && var2.getItem() == Items.lead && allowLeashing()) {
				if (!(this instanceof EntityTameable)
						|| !((EntityTameable) this).isTamed()) {
					setLeashedToEntity(p_130002_1_, true);
					--var2.stackSize;
					return true;
				}

				if (((EntityTameable) this).func_152114_e(p_130002_1_)) {
					setLeashedToEntity(p_130002_1_, true);
					--var2.stackSize;
					return true;
				}
			}

			return interact(p_130002_1_) ? true : super
					.interactFirst(p_130002_1_);
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return false;
	}

	public boolean isNoDespawnRequired() {
		return persistenceRequired;
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		worldObj.theProfiler.startSection("mobBaseTick");

		if (isEntityAlive() && rand.nextInt(1000) < livingSoundTime++) {
			livingSoundTime = -getTalkInterval();
			playLivingSound();
		}

		worldObj.theProfiler.endSection();
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		worldObj.theProfiler.startSection("looting");

		if (!worldObj.isClient
				&& canPickUpLoot()
				&& !dead
				&& worldObj.getGameRules().getGameRuleBooleanValue(
						"mobGriefing")) {
			final List var1 = worldObj.getEntitiesWithinAABB(EntityItem.class,
					boundingBox.expand(1.0D, 0.0D, 1.0D));
			final Iterator var2 = var1.iterator();

			while (var2.hasNext()) {
				final EntityItem var3 = (EntityItem) var2.next();

				if (!var3.isDead && var3.getEntityItem() != null) {
					final ItemStack var4 = var3.getEntityItem();
					final int var5 = getArmorPosition(var4);

					if (var5 > -1) {
						boolean var6 = true;
						final ItemStack var7 = getEquipmentInSlot(var5);

						if (var7 != null) {
							if (var5 == 0) {
								if (var4.getItem() instanceof ItemSword
										&& !(var7.getItem() instanceof ItemSword)) {
									var6 = true;
								} else if (var4.getItem() instanceof ItemSword
										&& var7.getItem() instanceof ItemSword) {
									final ItemSword var8 = (ItemSword) var4
											.getItem();
									final ItemSword var9 = (ItemSword) var7
											.getItem();

									if (var8.func_150931_i() == var9
											.func_150931_i()) {
										var6 = var4.getItemDamage() > var7
												.getItemDamage()
												|| var4.hasTagCompound()
												&& !var7.hasTagCompound();
									} else {
										var6 = var8.func_150931_i() > var9
												.func_150931_i();
									}
								} else {
									var6 = false;
								}
							} else if (var4.getItem() instanceof ItemArmor
									&& !(var7.getItem() instanceof ItemArmor)) {
								var6 = true;
							} else if (var4.getItem() instanceof ItemArmor
									&& var7.getItem() instanceof ItemArmor) {
								final ItemArmor var10 = (ItemArmor) var4
										.getItem();
								final ItemArmor var12 = (ItemArmor) var7
										.getItem();

								if (var10.damageReduceAmount == var12.damageReduceAmount) {
									var6 = var4.getItemDamage() > var7
											.getItemDamage()
											|| var4.hasTagCompound()
											&& !var7.hasTagCompound();
								} else {
									var6 = var10.damageReduceAmount > var12.damageReduceAmount;
								}
							} else {
								var6 = false;
							}
						}

						if (var6) {
							if (var7 != null
									&& rand.nextFloat() - 0.1F < equipmentDropChances[var5]) {
								entityDropItem(var7, 0.0F);
							}

							if (var4.getItem() == Items.diamond
									&& var3.func_145800_j() != null) {
								final EntityPlayer var11 = worldObj
										.getPlayerEntityByName(var3
												.func_145800_j());

								if (var11 != null) {
									var11.triggerAchievement(AchievementList.field_150966_x);
								}
							}

							setCurrentItemOrArmor(var5, var4);
							equipmentDropChances[var5] = 2.0F;
							persistenceRequired = true;
							onItemPickup(var3, 1);
							var3.setDead();
						}
					}
				}
			}
		}

		worldObj.theProfiler.endSection();
	}

	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(
				new AttributeModifier("Random spawn bonus",
						rand.nextGaussian() * 0.05D, 1));
		return p_110161_1_;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isClient) {
			updateLeashedState();
		}
	}

	/**
	 * Plays living's sound at its position
	 */
	public void playLivingSound() {
		final String var1 = getLivingSound();

		if (var1 != null) {
			playSound(var1, getSoundVolume(), getSoundPitch());
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setCanPickUpLoot(p_70037_1_.getBoolean("CanPickUpLoot"));
		persistenceRequired = p_70037_1_.getBoolean("PersistenceRequired");

		if (p_70037_1_.func_150297_b("CustomName", 8)
				&& p_70037_1_.getString("CustomName").length() > 0) {
			setCustomNameTag(p_70037_1_.getString("CustomName"));
		}

		setAlwaysRenderNameTag(p_70037_1_.getBoolean("CustomNameVisible"));
		NBTTagList var2;
		int var3;

		if (p_70037_1_.func_150297_b("Equipment", 9)) {
			var2 = p_70037_1_.getTagList("Equipment", 10);

			for (var3 = 0; var3 < equipment.length; ++var3) {
				equipment[var3] = ItemStack.loadItemStackFromNBT(var2
						.getCompoundTagAt(var3));
			}
		}

		if (p_70037_1_.func_150297_b("DropChances", 9)) {
			var2 = p_70037_1_.getTagList("DropChances", 5);

			for (var3 = 0; var3 < var2.tagCount(); ++var3) {
				equipmentDropChances[var3] = var2.func_150308_e(var3);
			}
		}

		isLeashed = p_70037_1_.getBoolean("Leashed");

		if (isLeashed && p_70037_1_.func_150297_b("Leash", 10)) {
			field_110170_bx = p_70037_1_.getCompoundTag("Leash");
		}
	}

	private void recreateLeash() {
		if (isLeashed && field_110170_bx != null) {
			if (field_110170_bx.func_150297_b("UUIDMost", 4)
					&& field_110170_bx.func_150297_b("UUIDLeast", 4)) {
				final UUID var5 = new UUID(field_110170_bx.getLong("UUIDMost"),
						field_110170_bx.getLong("UUIDLeast"));
				final List var6 = worldObj.getEntitiesWithinAABB(
						EntityLivingBase.class,
						boundingBox.expand(10.0D, 10.0D, 10.0D));
				final Iterator var7 = var6.iterator();

				while (var7.hasNext()) {
					final EntityLivingBase var8 = (EntityLivingBase) var7
							.next();

					if (var8.getUniqueID().equals(var5)) {
						leashedToEntity = var8;
						break;
					}
				}
			} else if (field_110170_bx.func_150297_b("X", 99)
					&& field_110170_bx.func_150297_b("Y", 99)
					&& field_110170_bx.func_150297_b("Z", 99)) {
				final int var1 = field_110170_bx.getInteger("X");
				final int var2 = field_110170_bx.getInteger("Y");
				final int var3 = field_110170_bx.getInteger("Z");
				EntityLeashKnot var4 = EntityLeashKnot.getKnotForBlock(
						worldObj, var1, var2, var3);

				if (var4 == null) {
					var4 = EntityLeashKnot.func_110129_a(worldObj, var1, var2,
							var3);
				}

				leashedToEntity = var4;
			} else {
				clearLeashed(false, true);
			}
		}

		field_110170_bx = null;
	}

	/**
	 * set the movespeed used for the new AI system
	 */
	@Override
	public void setAIMoveSpeed(float p_70659_1_) {
		super.setAIMoveSpeed(p_70659_1_);
		setMoveForward(p_70659_1_);
	}

	public void setAlwaysRenderNameTag(boolean p_94061_1_) {
		dataWatcher.updateObject(11, Byte.valueOf((byte) (p_94061_1_ ? 1 : 0)));
	}

	/**
	 * Sets the active target the Task system uses for tracking
	 */
	public void setAttackTarget(EntityLivingBase p_70624_1_) {
		attackTarget = p_70624_1_;
	}

	public void setCanPickUpLoot(boolean p_98053_1_) {
		canPickUpLoot = p_98053_1_;
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		equipment[p_70062_1_] = p_70062_2_;
	}

	public void setCustomNameTag(String p_94058_1_) {
		dataWatcher.updateObject(10, p_94058_1_);
	}

	public void setEquipmentDropChance(int p_96120_1_, float p_96120_2_) {
		equipmentDropChances[p_96120_1_] = p_96120_2_;
	}

	/**
	 * Sets the entity to be leashed to.\nArgs:\n@param par1Entity: The entity
	 * to be tethered to.\n@param par2: Whether to send an attaching
	 * notification packet to surrounding players.
	 */
	public void setLeashedToEntity(Entity p_110162_1_, boolean p_110162_2_) {
		isLeashed = true;
		leashedToEntity = p_110162_1_;

		if (!worldObj.isClient && p_110162_2_
				&& worldObj instanceof WorldServer) {
			((WorldServer) worldObj).getEntityTracker().func_151247_a(this,
					new S1BPacketEntityAttach(1, this, leashedToEntity));
		}
	}

	public void setMoveForward(float p_70657_1_) {
		moveForward = p_70657_1_;
	}

	/**
	 * Spawns an explosion particle around the Entity's location
	 */
	public void spawnExplosionParticle() {
		for (int var1 = 0; var1 < 20; ++var1) {
			final double var2 = rand.nextGaussian() * 0.02D;
			final double var4 = rand.nextGaussian() * 0.02D;
			final double var6 = rand.nextGaussian() * 0.02D;
			final double var8 = 10.0D;
			worldObj.spawnParticle("explode", posX + rand.nextFloat() * width
					* 2.0F - width - var2 * var8, posY + rand.nextFloat()
					* height - var4 * var8, posZ + rand.nextFloat() * width
					* 2.0F - width - var6 * var8, var2, var4, var6);
		}
	}

	@Override
	protected void updateAITasks() {
		++entityAge;
		worldObj.theProfiler.startSection("checkDespawn");
		despawnEntity();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("sensing");
		senses.clearSensingCache();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("targetSelector");
		targetTasks.onUpdateTasks();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("goalSelector");
		tasks.onUpdateTasks();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("navigation");
		navigator.onUpdateNavigation();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("mob tick");
		updateAITick();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("controls");
		worldObj.theProfiler.startSection("move");
		moveHelper.onUpdateMoveHelper();
		worldObj.theProfiler.endStartSection("look");
		lookHelper.onUpdateLook();
		worldObj.theProfiler.endStartSection("jump");
		jumpHelper.doJump();
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.endSection();
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		moveStrafing = 0.0F;
		moveForward = 0.0F;
		despawnEntity();
		final float var1 = 8.0F;

		if (rand.nextFloat() < 0.02F) {
			final EntityPlayer var2 = worldObj.getClosestPlayerToEntity(this,
					var1);

			if (var2 != null) {
				currentTarget = var2;
				numTicksToChaseTarget = 10 + rand.nextInt(20);
			} else {
				randomYawVelocity = (rand.nextFloat() - 0.5F) * 20.0F;
			}
		}

		if (currentTarget != null) {
			faceEntity(currentTarget, 10.0F, getVerticalFaceSpeed());

			if (numTicksToChaseTarget-- <= 0 || currentTarget.isDead
					|| currentTarget.getDistanceSqToEntity(this) > var1 * var1) {
				currentTarget = null;
			}
		} else {
			if (rand.nextFloat() < 0.05F) {
				randomYawVelocity = (rand.nextFloat() - 0.5F) * 20.0F;
			}

			rotationYaw += randomYawVelocity;
			rotationPitch = defaultPitch;
		}

		final boolean var4 = isInWater();
		final boolean var3 = handleLavaMovement();

		if (var4 || var3) {
			isJumping = rand.nextFloat() < 0.8F;
		}
	}

	/**
	 * Applies logic related to leashes, for example dragging the entity or
	 * breaking the leash.
	 */
	protected void updateLeashedState() {
		if (field_110170_bx != null) {
			recreateLeash();
		}

		if (isLeashed) {
			if (leashedToEntity == null || leashedToEntity.isDead) {
				clearLeashed(true, true);
			}
		}
	}

	/**
	 * Arguments: current rotation, intended rotation, max increment.
	 */
	private float updateRotation(float p_70663_1_, float p_70663_2_,
			float p_70663_3_) {
		float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);

		if (var4 > p_70663_3_) {
			var4 = p_70663_3_;
		}

		if (var4 < -p_70663_3_) {
			var4 = -p_70663_3_;
		}

		return p_70663_1_ + var4;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("CanPickUpLoot", canPickUpLoot());
		p_70014_1_.setBoolean("PersistenceRequired", persistenceRequired);
		final NBTTagList var2 = new NBTTagList();
		NBTTagCompound var4;

		for (final ItemStack element : equipment) {
			var4 = new NBTTagCompound();

			if (element != null) {
				element.writeToNBT(var4);
			}

			var2.appendTag(var4);
		}

		p_70014_1_.setTag("Equipment", var2);
		final NBTTagList var6 = new NBTTagList();

		for (final float equipmentDropChance : equipmentDropChances) {
			var6.appendTag(new NBTTagFloat(equipmentDropChance));
		}

		p_70014_1_.setTag("DropChances", var6);
		p_70014_1_.setString("CustomName", getCustomNameTag());
		p_70014_1_.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
		p_70014_1_.setBoolean("Leashed", isLeashed);

		if (leashedToEntity != null) {
			var4 = new NBTTagCompound();

			if (leashedToEntity instanceof EntityLivingBase) {
				var4.setLong("UUIDMost", leashedToEntity.getUniqueID()
						.getMostSignificantBits());
				var4.setLong("UUIDLeast", leashedToEntity.getUniqueID()
						.getLeastSignificantBits());
			} else if (leashedToEntity instanceof EntityHanging) {
				final EntityHanging var5 = (EntityHanging) leashedToEntity;
				var4.setInteger("X", var5.field_146063_b);
				var4.setInteger("Y", var5.field_146064_c);
				var4.setInteger("Z", var5.field_146062_d);
			}

			p_70014_1_.setTag("Leash", var4);
		}
	}
}

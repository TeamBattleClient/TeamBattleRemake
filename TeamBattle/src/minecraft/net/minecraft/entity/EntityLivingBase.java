package net.minecraft.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityLivingBase extends Entity {
	private static final UUID sprintingSpeedBoostModifierUUID = UUID
			.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
	private static final AttributeModifier sprintingSpeedBoostModifier = new AttributeModifier(
			sprintingSpeedBoostModifierUUID, "Sprinting speed boost",
			0.30000001192092896D, 2).setSaved(false);
	private final CombatTracker _combatTracker = new CombatTracker(this);
	private final HashMap activePotionsMap = new HashMap();
	public int arrowHitTimer;

	/** The yaw at which this entity was last attacked from. */
	public float attackedAtYaw;

	/** The most recent player that has attacked this entity */
	protected EntityPlayer attackingPlayer;
	public int attackTime;
	private BaseAttributeMap attributeMap;
	public float cameraPitch;

	/**
	 * This gets set on entity death, but never used. Looks like a duplicate of
	 * isDead
	 */
	protected boolean dead;

	/**
	 * The amount of time remaining this entity should act 'dead', i.e. have a
	 * corpse in the world.
	 */
	public int deathTime;

	/** The age of this EntityLiving (used to determine when it dies) */
	public int entityAge;

	/** is only being set, has no uses as of MC 1.1 */
	private EntityLivingBase entityLivingToAttack;
	private float field_110151_bq;
	protected float field_110154_aX;
	protected float field_70741_aB;
	protected float field_70763_ax;
	protected float field_70764_aw;

	protected float field_70768_au;
	public float field_70769_ao;
	public float field_70770_ap;
	/**
	 * The amount of time remaining this entity should act 'hurt'. (Visual
	 * appearance of red tint)
	 */
	public int hurtTime;
	/** used to check whether entity is jumping. */
	protected boolean isJumping;
	/** Whether an arm swing is currently in progress. */
	public boolean isSwingInProgress;
	/**
	 * A factor used to determine how far this entity will move each tick if it
	 * is jumping or falling.
	 */
	public float jumpMovementFactor = 0.02F;
	/** Number of ticks since last jump */
	private int jumpTicks;

	/**
	 * A factor used to determine how far this entity will move each tick if it
	 * is walking on land. Adjusted by speed, and slipperiness of the current
	 * block.
	 */
	public float landMovementFactor;

	private EntityLivingBase lastAttacker;

	/** Holds the value of ticksExisted when setLastAttacker was last called. */
	private int lastAttackerTime;

	/**
	 * Damage taken in the last hit. Mobs are resistant to damage less than this
	 * for a short time after taking damage.
	 */
	protected float lastDamage;

	/**
	 * Only relevant when limbYaw is not 0(the entity is moving). Influences
	 * where in its swing legs and arms currently are.
	 */
	public float limbSwing;

	public float limbSwingAmount;

	public int maxHurtResistantTime = 20;
	/** What the hurt time was max set to last. */
	public int maxHurtTime;
	public float moveForward;
	public float moveStrafing;
	/**
	 * The number of updates over which the new position and rotation are to be
	 * applied to the entity.
	 */
	protected int newPosRotationIncrements;
	/** The new X position to be applied to the entity. */
	protected double newPosX;

	/** The new Y position to be applied to the entity. */
	protected double newPosY;

	protected double newPosZ;

	/** The new yaw rotation to be applied to the entity. */
	protected double newRotationPitch;
	/** The new yaw rotation to be applied to the entity. */
	protected double newRotationYaw;
	/** Whether the DataWatcher needs to be updated with the active potions */
	private boolean potionsNeedUpdate = true;
	public float prevCameraPitch;

	public float prevHealth;

	/** The equipment this mob was previously wearing, used for syncing. */
	private final ItemStack[] previousEquipment = new ItemStack[5];

	public float prevLimbSwingAmount;
	public float prevRenderYawOffset;

	/** Entity head rotation yaw at previous tick */
	public float prevRotationYawHead;

	public float prevSwingProgress;

	protected float randomYawVelocity;

	/**
	 * Set to 60 when hit by the player or the player's wolf, then decrements.
	 * Used to determine whether the entity should drop items on death.
	 */
	protected int recentlyHit;
	public float renderYawOffset;
	private int revengeTimer;

	/** Entity head rotation yaw */
	public float rotationYawHead;

	/** The score value of the Mob, the amount of points the mob is worth. */
	protected int scoreValue;

	public float swingProgress;
	public int swingProgressInt;

	public EntityLivingBase(World p_i1594_1_) {
		super(p_i1594_1_);
		applyEntityAttributes();
		setHealth(getMaxHealth());
		preventEntitySpawning = true;
		field_70770_ap = (float) (Math.random() + 1.0D) * 0.01F;
		setPosition(posX, posY, posZ);
		field_70769_ao = (float) Math.random() * 12398.0F;
		rotationYaw = (float) (Math.random() * Math.PI * 2.0D);
		rotationYawHead = rotationYaw;
		stepHeight = 0.5F;
	}

	/**
	 * adds a PotionEffect to the entity
	 */
	public void addPotionEffect(PotionEffect p_70690_1_) {
		if (isPotionApplicable(p_70690_1_)) {
			if (activePotionsMap.containsKey(Integer.valueOf(p_70690_1_
					.getPotionID()))) {
				((PotionEffect) activePotionsMap.get(Integer.valueOf(p_70690_1_
						.getPotionID()))).combine(p_70690_1_);
				onChangedPotionEffect(
						(PotionEffect) activePotionsMap.get(Integer
								.valueOf(p_70690_1_.getPotionID())), true);
			} else {
				activePotionsMap.put(Integer.valueOf(p_70690_1_.getPotionID()),
						p_70690_1_);
				onNewPotionEffect(p_70690_1_);
			}
		}
	}

	/**
	 * Reduces damage, depending on armor
	 */
	protected float applyArmorCalculations(DamageSource p_70655_1_,
			float p_70655_2_) {
		if (!p_70655_1_.isUnblockable()) {
			final int var3 = 25 - getTotalArmorValue();
			final float var4 = p_70655_2_ * var3;
			damageArmor(p_70655_2_);
			p_70655_2_ = var4 / 25.0F;
		}

		return p_70655_2_;
	}

	protected void applyEntityAttributes() {
		getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth);
		getAttributeMap().registerAttribute(
				SharedMonsterAttributes.knockbackResistance);
		getAttributeMap().registerAttribute(
				SharedMonsterAttributes.movementSpeed);

		if (!isAIEnabled()) {
			getEntityAttribute(SharedMonsterAttributes.movementSpeed)
					.setBaseValue(0.10000000149011612D);
		}
	}

	/**
	 * Reduces damage, depending on potions
	 */
	protected float applyPotionDamageCalculations(DamageSource p_70672_1_,
			float p_70672_2_) {
		if (p_70672_1_.isDamageAbsolute())
			return p_70672_2_;
		else {
			int var3;
			int var4;
			float var5;

			if (this.isPotionActive(Potion.resistance)
					&& p_70672_1_ != DamageSource.outOfWorld) {
				var3 = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
				var4 = 25 - var3;
				var5 = p_70672_2_ * var4;
				p_70672_2_ = var5 / 25.0F;
			}

			if (p_70672_2_ <= 0.0F)
				return 0.0F;
			else {
				var3 = EnchantmentHelper.getEnchantmentModifierDamage(
						getLastActiveItems(), p_70672_1_);

				if (var3 > 20) {
					var3 = 20;
				}

				if (var3 > 0 && var3 <= 20) {
					var4 = 25 - var3;
					var5 = p_70672_2_ * var4;
					p_70672_2_ = var5 / 25.0F;
				}

				return p_70672_2_;
			}
		}
	}

	public boolean attackEntityAsMob(Entity p_70652_1_) {
		setLastAttacker(p_70652_1_);
		return false;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (worldObj.isClient)
			return false;
		else {
			entityAge = 0;

			if (getHealth() <= 0.0F)
				return false;
			else if (p_70097_1_.isFireDamage()
					&& this.isPotionActive(Potion.fireResistance))
				return false;
			else {
				if ((p_70097_1_ == DamageSource.anvil || p_70097_1_ == DamageSource.fallingBlock)
						&& getEquipmentInSlot(4) != null) {
					getEquipmentInSlot(4).damageItem(
							(int) (p_70097_2_ * 4.0F + rand.nextFloat()
									* p_70097_2_ * 2.0F), this);
					p_70097_2_ *= 0.75F;
				}

				limbSwingAmount = 1.5F;
				boolean var3 = true;

				if (hurtResistantTime > maxHurtResistantTime / 2.0F) {
					if (p_70097_2_ <= lastDamage)
						return false;

					damageEntity(p_70097_1_, p_70097_2_ - lastDamage);
					lastDamage = p_70097_2_;
					var3 = false;
				} else {
					lastDamage = p_70097_2_;
					prevHealth = getHealth();
					hurtResistantTime = maxHurtResistantTime;
					damageEntity(p_70097_1_, p_70097_2_);
					hurtTime = maxHurtTime = 10;
				}

				attackedAtYaw = 0.0F;
				final Entity var4 = p_70097_1_.getEntity();

				if (var4 != null) {
					if (var4 instanceof EntityLivingBase) {
						setRevengeTarget((EntityLivingBase) var4);
					}

					if (var4 instanceof EntityPlayer) {
						recentlyHit = 100;
						attackingPlayer = (EntityPlayer) var4;
					} else if (var4 instanceof EntityWolf) {
						final EntityWolf var5 = (EntityWolf) var4;

						if (var5.isTamed()) {
							recentlyHit = 100;
							attackingPlayer = null;
						}
					}
				}

				if (var3) {
					worldObj.setEntityState(this, (byte) 2);

					if (p_70097_1_ != DamageSource.drown) {
						setBeenAttacked();
					}

					if (var4 != null) {
						double var9 = var4.posX - posX;
						double var7;

						for (var7 = var4.posZ - posZ; var9 * var9 + var7 * var7 < 1.0E-4D; var7 = (Math
								.random() - Math.random()) * 0.01D) {
							var9 = (Math.random() - Math.random()) * 0.01D;
						}

						attackedAtYaw = (float) (Math.atan2(var7, var9) * 180.0D / Math.PI)
								- rotationYaw;
						knockBack(var4, p_70097_2_, var9, var7);
					} else {
						attackedAtYaw = (int) (Math.random() * 2.0D) * 180;
					}
				}

				String var10;

				if (getHealth() <= 0.0F) {
					var10 = getDeathSound();

					if (var3 && var10 != null) {
						playSound(var10, getSoundVolume(), getSoundPitch());
					}

					onDeath(p_70097_1_);
				} else {
					var10 = getHurtSound();

					if (var3 && var10 != null) {
						playSound(var10, getSoundVolume(), getSoundPitch());
					}
				}

				return true;
			}
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return !isDead;
	}

	public boolean canBreatheUnderwater() {
		return false;
	}

	/**
	 * returns true if the entity provided in the argument can be seen.
	 * (Raytrace)
	 */
	public boolean canEntityBeSeen(Entity p_70685_1_) {
		return worldObj.rayTraceBlocks(
				Vec3.createVectorHelper(posX, posY + getEyeHeight(), posZ),
				Vec3.createVectorHelper(p_70685_1_.posX, p_70685_1_.posY
						+ p_70685_1_.getEyeHeight(), p_70685_1_.posZ)) == null;
	}

	public void clearActivePotions() {
		final Iterator var1 = activePotionsMap.keySet().iterator();

		while (var1.hasNext()) {
			final Integer var2 = (Integer) var1.next();
			final PotionEffect var3 = (PotionEffect) activePotionsMap.get(var2);

			if (!worldObj.isClient) {
				var1.remove();
				onFinishedPotionEffect(var3);
			}
		}
	}

	protected void collideWithEntity(Entity p_82167_1_) {
		p_82167_1_.applyEntityCollision(this);
	}

	protected void collideWithNearbyEntities() {
		final List var1 = worldObj.getEntitiesWithinAABBExcludingEntity(this,
				boundingBox.expand(0.20000000298023224D, 0.0D,
						0.20000000298023224D));

		if (var1 != null && !var1.isEmpty()) {
			for (int var2 = 0; var2 < var1.size(); ++var2) {
				final Entity var3 = (Entity) var1.get(var2);

				if (var3.canBePushed()) {
					collideWithEntity(var3);
				}
			}
		}
	}

	protected void damageArmor(float p_70675_1_) {
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage
	 * from the armor first and then health second with the reduced value. Args:
	 * damageAmount
	 */
	protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
		if (!isEntityInvulnerable()) {
			p_70665_2_ = applyArmorCalculations(p_70665_1_, p_70665_2_);
			p_70665_2_ = applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
			final float var3 = p_70665_2_;
			p_70665_2_ = Math.max(p_70665_2_ - getAbsorptionAmount(), 0.0F);
			setAbsorptionAmount(getAbsorptionAmount() - (var3 - p_70665_2_));

			if (p_70665_2_ != 0.0F) {
				final float var4 = getHealth();
				setHealth(var4 - p_70665_2_);
				func_110142_aN().func_94547_a(p_70665_1_, var4, p_70665_2_);
				setAbsorptionAmount(getAbsorptionAmount() - p_70665_2_);
			}
		}
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	protected int decreaseAirSupply(int p_70682_1_) {
		final int var2 = EnchantmentHelper.getRespiration(this);
		return var2 > 0 && rand.nextInt(var2 + 1) > 0 ? p_70682_1_
				: p_70682_1_ - 1;
	}

	/**
	 * Moves the entity to a position out of the way of its mount.
	 */
	public void dismountEntity(Entity p_110145_1_) {
		double var3 = p_110145_1_.posX;
		double var5 = p_110145_1_.boundingBox.minY - p_110145_1_.height;
		double var7 = p_110145_1_.posZ;
		final byte var9 = 1;

		for (int var10 = -var9; var10 <= var9; ++var10) {
			for (int var11 = -var9; var11 < var9; ++var11) {
				if (var10 != 0 || var11 != 0) {
					final int var12 = (int) (posX + var10);
					final int var13 = (int) (posZ + var11);
					final AxisAlignedBB var2 = boundingBox
							.getOffsetBoundingBox(var10, 1.0D, var11);

					if (worldObj.func_147461_a(var2).isEmpty()) {
						if (World.doesBlockHaveSolidTopSurface(worldObj, var12,
								(int) posY, var13)) {
							setPositionAndUpdate(posX + var10, posY - 1.0D,
									posZ + var11);
							return;
						}

						if (World.doesBlockHaveSolidTopSurface(worldObj, var12,
								(int) posY - 1, var13)
								|| worldObj.getBlock(var12, (int) posY - 1,
										var13).getMaterial() == Material.water) {
							var3 = posX + var10;
							var5 = posY + 1.0D;
							var7 = posZ + var11;
						}
					}
				}
			}
		}

		setPositionAndUpdate(var3, var5, var7);
	}

	/**
	 * Drop the equipment for this entity.
	 */
	protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
	}

	protected void dropRareDrop(int p_70600_1_) {
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(7, Integer.valueOf(0));
		dataWatcher.addObject(8, Byte.valueOf((byte) 0));
		dataWatcher.addObject(9, Byte.valueOf((byte) 0));
		dataWatcher.addObject(6, Float.valueOf(1.0F));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		super.fall(p_70069_1_);
		final PotionEffect var2 = getActivePotionEffect(Potion.jump);
		final float var3 = var2 != null ? (float) (var2.getAmplifier() + 1)
				: 0.0F;
		final int var4 = MathHelper.ceiling_float_int(p_70069_1_ - 3.0F - var3);

		if (var4 > 0) {
			playSound(func_146067_o(var4), 1.0F, 1.0F);
			attackEntityFrom(DamageSource.fall, var4);
			final int var5 = MathHelper.floor_double(posX);
			final int var6 = MathHelper.floor_double(posY
					- 0.20000000298023224D - yOffset);
			final int var7 = MathHelper.floor_double(posZ);
			final Block var8 = worldObj.getBlock(var5, var6, var7);

			if (var8.getMaterial() != Material.air) {
				final Block.SoundType var9 = var8.stepSound;
				playSound(var9.func_150498_e(), var9.func_150497_c() * 0.5F,
						var9.func_150494_d() * 0.75F);
			}
		}
	}

	public CombatTracker func_110142_aN() {
		return _combatTracker;
	}

	protected float func_110146_f(float p_110146_1_, float p_110146_2_) {
		final float var3 = MathHelper.wrapAngleTo180_float(p_110146_1_
				- renderYawOffset);
		renderYawOffset += var3 * 0.3F;
		float var4 = MathHelper.wrapAngleTo180_float(rotationYaw
				- renderYawOffset);
		final boolean var5 = var4 < -90.0F || var4 >= 90.0F;

		if (var4 < -75.0F) {
			var4 = -75.0F;
		}

		if (var4 >= 75.0F) {
			var4 = 75.0F;
		}

		renderYawOffset = rotationYaw - var4;

		if (var4 * var4 > 2500.0F) {
			renderYawOffset += var4 * 0.2F;
		}

		if (var5) {
			p_110146_2_ *= -1.0F;
		}

		return p_110146_2_;
	}

	public int func_142015_aE() {
		return revengeTimer;
	}

	protected boolean func_146066_aG() {
		return !isChild();
	}

	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.neutral.hurt.fall.big"
				: "game.neutral.hurt.fall.small";
	}

	public void func_152111_bt() {
	}

	public void func_152112_bu() {
	}

	public EntityLivingBase func_94060_bK() {
		return _combatTracker.func_94550_c() != null ? _combatTracker
				.func_94550_c() : attackingPlayer != null ? attackingPlayer
				: entityLivingToAttack != null ? entityLivingToAttack : null;
	}

	public float getAbsorptionAmount() {
		return field_110151_bq;
	}

	/**
	 * returns the PotionEffect for the supplied Potion if it is active, null
	 * otherwise.
	 */
	public PotionEffect getActivePotionEffect(Potion p_70660_1_) {
		return (PotionEffect) activePotionsMap.get(Integer
				.valueOf(p_70660_1_.id));
	}

	public Collection getActivePotionEffects() {
		return activePotionsMap.values();
	}

	public int getAge() {
		return entityAge;
	}

	/**
	 * the movespeed used for the new AI system
	 */
	public float getAIMoveSpeed() {
		return isAIEnabled() ? landMovementFactor : 0.1F;
	}

	public EntityLivingBase getAITarget() {
		return entityLivingToAttack;
	}

	public boolean getAlwaysRenderNameTagForRender() {
		return false;
	}

	/**
	 * Returns an integer indicating the end point of the swing animation, used
	 * by {@link #swingProgress} to provide a progress indicator. Takes dig
	 * speed enchantments into account.
	 */
	private int getArmSwingAnimationEnd() {
		return this.isPotionActive(Potion.digSpeed) ? 6 - (1 + getActivePotionEffect(
				Potion.digSpeed).getAmplifier()) * 1
				: this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + getActivePotionEffect(
						Potion.digSlowdown).getAmplifier()) * 2
						: 6;
	}

	/**
	 * counts the amount of arrows stuck in the entity. getting hit by arrows
	 * increases this, used in rendering
	 */
	public final int getArrowCountInEntity() {
		return dataWatcher.getWatchableObjectByte(9);
	}

	public BaseAttributeMap getAttributeMap() {
		if (attributeMap == null) {
			attributeMap = new ServersideAttributeMap();
		}

		return attributeMap;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "game.neutral.die";
	}

	public IAttributeInstance getEntityAttribute(IAttribute p_110148_1_) {
		return getAttributeMap().getAttributeInstance(p_110148_1_);
	}

	/**
	 * 0: Tool in Hand; 1-4: Armor
	 */
	public abstract ItemStack getEquipmentInSlot(int p_71124_1_);

	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 0;
	}

	@Override
	public float getEyeHeight() {
		return height * 0.85F;
	}

	public final float getHealth() {
		return dataWatcher.getWatchableObjectFloat(6);
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	public abstract ItemStack getHeldItem();

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "game.neutral.hurt";
	}

	/**
	 * Gets the Icon Index of the item currently held
	 */
	public IIcon getItemIcon(ItemStack p_70620_1_, int p_70620_2_) {
		return p_70620_1_.getItem().requiresMultipleRenderPasses() ? p_70620_1_
				.getItem().getIconFromDamageForRenderPass(
						p_70620_1_.getItemDamage(), p_70620_2_) : p_70620_1_
				.getIconIndex();
	}

	@Override
	public abstract ItemStack[] getLastActiveItems();

	public EntityLivingBase getLastAttacker() {
		return lastAttacker;
	}

	public int getLastAttackerTime() {
		return lastAttackerTime;
	}

	/**
	 * interpolated look vector
	 */
	public Vec3 getLook(float p_70676_1_) {
		float var2;
		float var3;
		float var4;
		float var5;

		if (p_70676_1_ == 1.0F) {
			var2 = MathHelper
					.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
			var3 = MathHelper
					.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
			var4 = -MathHelper.cos(-rotationPitch * 0.017453292F);
			var5 = MathHelper.sin(-rotationPitch * 0.017453292F);
			return Vec3.createVectorHelper(var3 * var4, var5, var2 * var4);
		} else {
			var2 = prevRotationPitch + (rotationPitch - prevRotationPitch)
					* p_70676_1_;
			var3 = prevRotationYaw + (rotationYaw - prevRotationYaw)
					* p_70676_1_;
			var4 = MathHelper.cos(-var3 * 0.017453292F - (float) Math.PI);
			var5 = MathHelper.sin(-var3 * 0.017453292F - (float) Math.PI);
			final float var6 = -MathHelper.cos(-var2 * 0.017453292F);
			final float var7 = MathHelper.sin(-var2 * 0.017453292F);
			return Vec3.createVectorHelper(var5 * var6, var7, var4 * var6);
		}
	}

	/**
	 * returns a (normalized) vector of where this entity is looking
	 */
	@Override
	public Vec3 getLookVec() {
		return getLook(1.0F);
	}

	public final float getMaxHealth() {
		return (float) getEntityAttribute(SharedMonsterAttributes.maxHealth)
				.getAttributeValue();
	}

	/**
	 * interpolated position vector
	 */
	public Vec3 getPosition(float p_70666_1_) {
		if (p_70666_1_ == 1.0F)
			return Vec3.createVectorHelper(posX, posY, posZ);
		else {
			final double var2 = prevPosX + (posX - prevPosX) * p_70666_1_;
			final double var4 = prevPosY + (posY - prevPosY) * p_70666_1_;
			final double var6 = prevPosZ + (posZ - prevPosZ) * p_70666_1_;
			return Vec3.createVectorHelper(var2, var4, var6);
		}
	}

	public Random getRNG() {
		return rand;
	}

	@Override
	public float getRotationYawHead() {
		return rotationYawHead;
	}

	/**
	 * Gets the pitch of living sounds in living entities.
	 */
	protected float getSoundPitch() {
		return isChild() ? (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F
				: (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	protected float getSoundVolume() {
		return 1.0F;
	}

	/**
	 * Returns where in the swing animation the living entity is (from 0 to 1).
	 * Args: partialTickTime
	 */
	public float getSwingProgress(float p_70678_1_) {
		float var2 = swingProgress - prevSwingProgress;

		if (var2 < 0.0F) {
			++var2;
		}

		return prevSwingProgress + var2 * p_70678_1_;
	}

	public Team getTeam() {
		return null;
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	public int getTotalArmorValue() {
		int var1 = 0;
		final ItemStack[] var2 = getLastActiveItems();
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final ItemStack var5 = var2[var4];

			if (var5 != null && var5.getItem() instanceof ItemArmor) {
				final int var6 = ((ItemArmor) var5.getItem()).damageReduceAmount;
				var1 += var6;
			}
		}

		return var1;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 2) {
			limbSwingAmount = 1.5F;
			hurtResistantTime = maxHurtResistantTime;
			hurtTime = maxHurtTime = 10;
			attackedAtYaw = 0.0F;
			playSound(getHurtSound(), getSoundVolume(),
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			attackEntityFrom(DamageSource.generic, 0.0F);
		} else if (p_70103_1_ == 3) {
			playSound(getDeathSound(), getSoundVolume(),
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			setHealth(0.0F);
			onDeath(DamageSource.generic);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Heal living entity (param: amount of half-hearts)
	 */
	public void heal(float p_70691_1_) {
		final float var2 = getHealth();

		if (var2 > 0.0F) {
			setHealth(var2 + p_70691_1_);
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return false;
	}

	/**
	 * If Animal, checks if the age timer is negative
	 */
	public boolean isChild() {
		return false;
	}

	/**
	 * Returns whether the entity is in a local (client) world
	 */
	public boolean isClientWorld() {
		return !worldObj.isClient;
	}

	/**
	 * Checks whether target entity is alive.
	 */
	@Override
	public boolean isEntityAlive() {
		return !isDead && getHealth() > 0.0F;
	}

	/**
	 * Returns true if this entity is undead.
	 */
	public boolean isEntityUndead() {
		return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Dead and sleeping entities cannot move
	 */
	protected boolean isMovementBlocked() {
		return getHealth() <= 0.0F;
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	public boolean isOnLadder() {
		final int var1 = MathHelper.floor_double(posX);
		final int var2 = MathHelper.floor_double(boundingBox.minY);
		final int var3 = MathHelper.floor_double(posZ);
		final Block var4 = worldObj.getBlock(var1, var2, var3);
		return var4 == Blocks.ladder || var4 == Blocks.vine;
	}

	public boolean isOnSameTeam(EntityLivingBase p_142014_1_) {
		return isOnTeam(p_142014_1_.getTeam());
	}

	/**
	 * Returns true if the entity is on a specific team.
	 */
	public boolean isOnTeam(Team p_142012_1_) {
		return getTeam() != null ? getTeam().isSameTeam(p_142012_1_) : false;
	}

	/**
	 * Only use is to identify if class is an instance of player for experience
	 * dropping
	 */
	protected boolean isPlayer() {
		return false;
	}

	/**
	 * Returns whether player is sleeping or not
	 */
	public boolean isPlayerSleeping() {
		return false;
	}

	public boolean isPotionActive(int p_82165_1_) {
		return activePotionsMap.containsKey(Integer.valueOf(p_82165_1_));
	}

	public boolean isPotionActive(Potion p_70644_1_) {
		return activePotionsMap.containsKey(Integer.valueOf(p_70644_1_.id));
	}

	public boolean isPotionApplicable(PotionEffect p_70687_1_) {
		if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
			final int var2 = p_70687_1_.getPotionID();

			if (var2 == Potion.regeneration.id || var2 == Potion.poison.id)
				return false;
		}

		return true;
	}

	/**
	 * Causes this entity to do an upwards motion (jumping).
	 */
	protected void jump() {
		motionY = 0.41999998688697815D;

		if (this.isPotionActive(Potion.jump)) {
			motionY += (getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
		}

		if (isSprinting()) {
			final float var1 = rotationYaw * 0.017453292F;
			motionX -= MathHelper.sin(var1) * 0.2F;
			motionZ += MathHelper.cos(var1) * 0.2F;
		}

		isAirBorne = true;
	}

	/**
	 * sets the dead flag. Used when you fall off the bottom of the world.
	 */
	@Override
	protected void kill() {
		attackEntityFrom(DamageSource.outOfWorld, 4.0F);
	}

	/**
	 * knocks back this entity
	 */
	public void knockBack(Entity p_70653_1_, float p_70653_2_,
			double p_70653_3_, double p_70653_5_) {
		if (rand.nextDouble() >= getEntityAttribute(
				SharedMonsterAttributes.knockbackResistance)
				.getAttributeValue()) {
			isAirBorne = true;
			final float var7 = MathHelper.sqrt_double(p_70653_3_ * p_70653_3_
					+ p_70653_5_ * p_70653_5_);
			final float var8 = 0.4F;
			motionX /= 2.0D;
			motionY /= 2.0D;
			motionZ /= 2.0D;
			motionX -= p_70653_3_ / var7 * var8;
			motionY += var8;
			motionZ -= p_70653_5_ / var7 * var8;

			if (motionY > 0.4000000059604645D) {
				motionY = 0.4000000059604645D;
			}
		}
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		double var8;

		if (isInWater()
				&& (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
			var8 = posY;
			moveFlying(p_70612_1_, p_70612_2_, isAIEnabled() ? 0.04F : 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.800000011920929D;
			motionY *= 0.800000011920929D;
			motionZ *= 0.800000011920929D;
			motionY -= 0.02D;

			if (isCollidedHorizontally
					&& isOffsetPositionInLiquid(motionX, motionY
							+ 0.6000000238418579D - posY + var8, motionZ)) {
				motionY = 0.30000001192092896D;
			}
		} else if (handleLavaMovement()
				&& (!(this instanceof EntityPlayer) || !((EntityPlayer) this).capabilities.isFlying)) {
			var8 = posY;
			moveFlying(p_70612_1_, p_70612_2_, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
			motionY -= 0.02D;

			if (isCollidedHorizontally
					&& isOffsetPositionInLiquid(motionX, motionY
							+ 0.6000000238418579D - posY + var8, motionZ)) {
				motionY = 0.30000001192092896D;
			}
		} else {
			float var3 = 0.91F;

			if (onGround) {
				var3 = worldObj.getBlock(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			final float var4 = 0.16277136F / (var3 * var3 * var3);
			float var5;

			if (onGround) {
				var5 = getAIMoveSpeed() * var4;
			} else {
				var5 = jumpMovementFactor;
			}

			moveFlying(p_70612_1_, p_70612_2_, var5);
			var3 = 0.91F;

			if (onGround) {
				var3 = worldObj.getBlock(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1,
						MathHelper.floor_double(posZ)).slipperiness * 0.91F;
			}

			if (isOnLadder()) {
				final float var6 = 0.15F;

				if (motionX < -var6) {
					motionX = -var6;
				}

				if (motionX > var6) {
					motionX = var6;
				}

				if (motionZ < -var6) {
					motionZ = -var6;
				}

				if (motionZ > var6) {
					motionZ = var6;
				}

				fallDistance = 0.0F;

				if (motionY < -0.15D) {
					motionY = -0.15D;
				}

				final boolean var7 = isSneaking()
						&& this instanceof EntityPlayer;

				if (var7 && motionY < 0.0D) {
					motionY = 0.0D;
				}
			}

			moveEntity(motionX, motionY, motionZ);

			if (isCollidedHorizontally && isOnLadder()) {
				motionY = 0.2D;
			}

			if (worldObj.isClient
					&& (!worldObj.blockExists((int) posX, 0, (int) posZ) || !worldObj
							.getChunkFromBlockCoords((int) posX, (int) posZ).isChunkLoaded)) {
				if (posY > 0.0D) {
					motionY = -0.1D;
				} else {
					motionY = 0.0D;
				}
			} else {
				motionY -= 0.08D;
			}

			motionY *= 0.9800000190734863D;
			motionX *= var3;
			motionZ *= var3;
		}

		prevLimbSwingAmount = limbSwingAmount;
		var8 = posX - prevPosX;
		final double var9 = posZ - prevPosZ;
		float var10 = MathHelper.sqrt_double(var8 * var8 + var9 * var9) * 4.0F;

		if (var10 > 1.0F) {
			var10 = 1.0F;
		}

		limbSwingAmount += (var10 - limbSwingAmount) * 0.4F;
		limbSwing += limbSwingAmount;
	}

	protected void onChangedPotionEffect(PotionEffect p_70695_1_,
			boolean p_70695_2_) {
		potionsNeedUpdate = true;

		if (p_70695_2_ && !worldObj.isClient) {
			Potion.potionTypes[p_70695_1_.getPotionID()]
					.removeAttributesModifiersFromEntity(this,
							getAttributeMap(), p_70695_1_.getAmplifier());
			Potion.potionTypes[p_70695_1_.getPotionID()]
					.applyAttributesModifiersToEntity(this, getAttributeMap(),
							p_70695_1_.getAmplifier());
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource p_70645_1_) {
		final Entity var2 = p_70645_1_.getEntity();
		final EntityLivingBase var3 = func_94060_bK();

		if (scoreValue >= 0 && var3 != null) {
			var3.addToPlayerScore(this, scoreValue);
		}

		if (var2 != null) {
			var2.onKillEntity(this);
		}

		dead = true;
		func_110142_aN().func_94549_h();

		if (!worldObj.isClient) {
			int var4 = 0;

			if (var2 instanceof EntityPlayer) {
				var4 = EnchantmentHelper
						.getLootingModifier((EntityLivingBase) var2);
			}

			if (func_146066_aG()
					&& worldObj.getGameRules().getGameRuleBooleanValue(
							"doMobLoot")) {
				dropFewItems(recentlyHit > 0, var4);
				dropEquipment(recentlyHit > 0, var4);

				if (recentlyHit > 0) {
					final int var5 = rand.nextInt(200) - var4;

					if (var5 < 5) {
						dropRareDrop(var5 <= 0 ? 1 : 0);
					}
				}
			}
		}

		worldObj.setEntityState(this, (byte) 3);
	}

	/**
	 * handles entity death timer, experience orb and particle creation
	 */
	protected void onDeathUpdate() {
		++deathTime;

		if (deathTime == 20) {
			int var1;

			if (!worldObj.isClient
					&& (recentlyHit > 0 || isPlayer())
					&& func_146066_aG()
					&& worldObj.getGameRules().getGameRuleBooleanValue(
							"doMobLoot")) {
				var1 = getExperiencePoints(attackingPlayer);

				while (var1 > 0) {
					final int var2 = EntityXPOrb.getXPSplit(var1);
					var1 -= var2;
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX,
							posY, posZ, var2));
				}
			}

			setDead();

			for (var1 = 0; var1 < 20; ++var1) {
				final double var8 = rand.nextGaussian() * 0.02D;
				final double var4 = rand.nextGaussian() * 0.02D;
				final double var6 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("explode", posX + rand.nextFloat()
						* width * 2.0F - width, posY + rand.nextFloat()
						* height, posZ + rand.nextFloat() * width * 2.0F
						- width, var8, var4, var6);
			}
		}
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void onEntityUpdate() {
		prevSwingProgress = swingProgress;
		super.onEntityUpdate();
		worldObj.theProfiler.startSection("livingEntityBaseTick");

		if (isEntityAlive() && isEntityInsideOpaqueBlock()) {
			attackEntityFrom(DamageSource.inWall, 1.0F);
		}

		if (isImmuneToFire() || worldObj.isClient) {
			extinguish();
		}

		final boolean var1 = this instanceof EntityPlayer
				&& ((EntityPlayer) this).capabilities.disableDamage;

		if (isEntityAlive() && isInsideOfMaterial(Material.water)) {
			if (!canBreatheUnderwater()
					&& !this.isPotionActive(Potion.waterBreathing.id) && !var1) {
				setAir(decreaseAirSupply(getAir()));

				if (getAir() == -20) {
					setAir(0);

					for (int var2 = 0; var2 < 8; ++var2) {
						final float var3 = rand.nextFloat() - rand.nextFloat();
						final float var4 = rand.nextFloat() - rand.nextFloat();
						final float var5 = rand.nextFloat() - rand.nextFloat();
						worldObj.spawnParticle("bubble", posX + var3, posY
								+ var4, posZ + var5, motionX, motionY, motionZ);
					}

					attackEntityFrom(DamageSource.drown, 2.0F);
				}
			}

			if (!worldObj.isClient && isRiding()
					&& ridingEntity instanceof EntityLivingBase) {
				mountEntity((Entity) null);
			}
		} else {
			setAir(300);
		}

		if (isEntityAlive() && isWet()) {
			extinguish();
		}

		prevCameraPitch = cameraPitch;

		if (attackTime > 0) {
			--attackTime;
		}

		if (hurtTime > 0) {
			--hurtTime;
		}

		if (hurtResistantTime > 0 && !(this instanceof EntityPlayerMP)) {
			--hurtResistantTime;
		}

		if (getHealth() <= 0.0F) {
			onDeathUpdate();
		}

		if (recentlyHit > 0) {
			--recentlyHit;
		} else {
			attackingPlayer = null;
		}

		if (lastAttacker != null && !lastAttacker.isEntityAlive()) {
			lastAttacker = null;
		}

		if (entityLivingToAttack != null) {
			if (!entityLivingToAttack.isEntityAlive()) {
				setRevengeTarget((EntityLivingBase) null);
			} else if (ticksExisted - revengeTimer > 100) {
				setRevengeTarget((EntityLivingBase) null);
			}
		}

		updatePotionEffects();
		field_70763_ax = field_70764_aw;
		prevRenderYawOffset = renderYawOffset;
		prevRotationYawHead = rotationYawHead;
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		worldObj.theProfiler.endSection();
	}

	protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
		potionsNeedUpdate = true;

		if (!worldObj.isClient) {
			Potion.potionTypes[p_70688_1_.getPotionID()]
					.removeAttributesModifiersFromEntity(this,
							getAttributeMap(), p_70688_1_.getAmplifier());
		}
	}

	/**
	 * Called whenever an item is picked up from walking over it. Args:
	 * pickedUpEntity, stackSize
	 */
	public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
		if (!p_71001_1_.isDead && !worldObj.isClient) {
			final EntityTracker var3 = ((WorldServer) worldObj)
					.getEntityTracker();

			if (p_71001_1_ instanceof EntityItem) {
				var3.func_151247_a(p_71001_1_, new S0DPacketCollectItem(
						p_71001_1_.getEntityId(), getEntityId()));
			}

			if (p_71001_1_ instanceof EntityArrow) {
				var3.func_151247_a(p_71001_1_, new S0DPacketCollectItem(
						p_71001_1_.getEntityId(), getEntityId()));
			}

			if (p_71001_1_ instanceof EntityXPOrb) {
				var3.func_151247_a(p_71001_1_, new S0DPacketCollectItem(
						p_71001_1_.getEntityId(), getEntityId()));
			}
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		if (jumpTicks > 0) {
			--jumpTicks;
		}

		if (newPosRotationIncrements > 0) {
			final double var1 = posX + (newPosX - posX)
					/ newPosRotationIncrements;
			final double var3 = posY + (newPosY - posY)
					/ newPosRotationIncrements;
			final double var5 = posZ + (newPosZ - posZ)
					/ newPosRotationIncrements;
			final double var7 = MathHelper.wrapAngleTo180_double(newRotationYaw
					- rotationYaw);
			rotationYaw = (float) (rotationYaw + var7
					/ newPosRotationIncrements);
			rotationPitch = (float) (rotationPitch + (newRotationPitch - rotationPitch)
					/ newPosRotationIncrements);
			--newPosRotationIncrements;
			setPosition(var1, var3, var5);
			setRotation(rotationYaw, rotationPitch);
		} else if (!isClientWorld()) {
			motionX *= 0.98D;
			motionY *= 0.98D;
			motionZ *= 0.98D;
		}

		if (Math.abs(motionX) < 0.005D) {
			motionX = 0.0D;
		}

		if (Math.abs(motionY) < 0.005D) {
			motionY = 0.0D;
		}

		if (Math.abs(motionZ) < 0.005D) {
			motionZ = 0.0D;
		}

		worldObj.theProfiler.startSection("ai");

		if (isMovementBlocked()) {
			isJumping = false;
			moveStrafing = 0.0F;
			moveForward = 0.0F;
			randomYawVelocity = 0.0F;
		} else if (isClientWorld()) {
			if (isAIEnabled()) {
				worldObj.theProfiler.startSection("newAi");
				updateAITasks();
				worldObj.theProfiler.endSection();
			} else {
				worldObj.theProfiler.startSection("oldAi");
				updateEntityActionState();
				worldObj.theProfiler.endSection();
				rotationYawHead = rotationYaw;
			}
		}

		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("jump");

		if (isJumping) {
			if (!isInWater() && !handleLavaMovement()) {
				if (onGround && jumpTicks == 0) {
					jump();
					jumpTicks = 10;
				}
			} else {
				motionY += 0.03999999910593033D;
			}
		} else {
			jumpTicks = 0;
		}

		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("travel");
		moveStrafing *= 0.98F;
		moveForward *= 0.98F;
		randomYawVelocity *= 0.9F;
		moveEntityWithHeading(moveStrafing, moveForward);
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("push");

		if (!worldObj.isClient) {
			collideWithNearbyEntities();
		}

		worldObj.theProfiler.endSection();
	}

	protected void onNewPotionEffect(PotionEffect p_70670_1_) {
		potionsNeedUpdate = true;

		if (!worldObj.isClient) {
			Potion.potionTypes[p_70670_1_.getPotionID()]
					.applyAttributesModifiersToEntity(this, getAttributeMap(),
							p_70670_1_.getAmplifier());
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (!worldObj.isClient) {
			final int var1 = getArrowCountInEntity();

			if (var1 > 0) {
				if (arrowHitTimer <= 0) {
					arrowHitTimer = 20 * (30 - var1);
				}

				--arrowHitTimer;

				if (arrowHitTimer <= 0) {
					setArrowCountInEntity(var1 - 1);
				}
			}

			for (int var2 = 0; var2 < 5; ++var2) {
				final ItemStack var3 = previousEquipment[var2];
				final ItemStack var4 = getEquipmentInSlot(var2);

				if (!ItemStack.areItemStacksEqual(var4, var3)) {
					((WorldServer) worldObj).getEntityTracker().func_151247_a(
							this,
							new S04PacketEntityEquipment(getEntityId(), var2,
									var4));

					if (var3 != null) {
						attributeMap.removeAttributeModifiers(var3
								.getAttributeModifiers());
					}

					if (var4 != null) {
						attributeMap.applyAttributeModifiers(var4
								.getAttributeModifiers());
					}

					previousEquipment[var2] = var4 == null ? null : var4.copy();
				}
			}

			if (ticksExisted % 20 == 0) {
				func_110142_aN().func_94549_h();
			}
		}

		onLivingUpdate();
		final double var9 = posX - prevPosX;
		final double var10 = posZ - prevPosZ;
		final float var5 = (float) (var9 * var9 + var10 * var10);
		float var6 = renderYawOffset;
		float var7 = 0.0F;
		field_70768_au = field_110154_aX;
		float var8 = 0.0F;

		if (var5 > 0.0025000002F) {
			var8 = 1.0F;
			var7 = (float) Math.sqrt(var5) * 3.0F;
			var6 = (float) Math.atan2(var10, var9) * 180.0F / (float) Math.PI
					- 90.0F;
		}

		if (swingProgress > 0.0F) {
			var6 = rotationYaw;
		}

		if (!onGround) {
			var8 = 0.0F;
		}

		field_110154_aX += (var8 - field_110154_aX) * 0.3F;
		worldObj.theProfiler.startSection("headTurn");
		var7 = func_110146_f(var6, var7);
		worldObj.theProfiler.endSection();
		worldObj.theProfiler.startSection("rangeChecks");

		while (rotationYaw - prevRotationYaw < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		while (renderYawOffset - prevRenderYawOffset < -180.0F) {
			prevRenderYawOffset -= 360.0F;
		}

		while (renderYawOffset - prevRenderYawOffset >= 180.0F) {
			prevRenderYawOffset += 360.0F;
		}

		while (rotationPitch - prevRotationPitch < -180.0F) {
			prevRotationPitch -= 360.0F;
		}

		while (rotationPitch - prevRotationPitch >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while (rotationYawHead - prevRotationYawHead < -180.0F) {
			prevRotationYawHead -= 360.0F;
		}

		while (rotationYawHead - prevRotationYawHead >= 180.0F) {
			prevRotationYawHead += 360.0F;
		}

		worldObj.theProfiler.endSection();
		field_70764_aw += var7;
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in
	 * multiplayer.
	 */
	@Override
	public void performHurtAnimation() {
		hurtTime = maxHurtTime = 10;
		attackedAtYaw = 0.0F;
	}

	/**
	 * Performs a ray trace for the distance specified and using the partial
	 * tick time. Args: distance, partialTickTime
	 */
	public MovingObjectPosition rayTrace(double p_70614_1_, float p_70614_3_) {
		final Vec3 var4 = getPosition(p_70614_3_);
		final Vec3 var5 = getLook(p_70614_3_);
		final Vec3 var6 = var4.addVector(var5.xCoord * p_70614_1_, var5.yCoord
				* p_70614_1_, var5.zCoord * p_70614_1_);
		return worldObj.func_147447_a(var4, var6, false, false, true);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		setAbsorptionAmount(p_70037_1_.getFloat("AbsorptionAmount"));

		if (p_70037_1_.func_150297_b("Attributes", 9) && worldObj != null
				&& !worldObj.isClient) {
			SharedMonsterAttributes.func_151475_a(getAttributeMap(),
					p_70037_1_.getTagList("Attributes", 10));
		}

		if (p_70037_1_.func_150297_b("ActiveEffects", 9)) {
			final NBTTagList var2 = p_70037_1_.getTagList("ActiveEffects", 10);

			for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
				final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
				final PotionEffect var5 = PotionEffect
						.readCustomPotionEffectFromNBT(var4);

				if (var5 != null) {
					activePotionsMap.put(Integer.valueOf(var5.getPotionID()),
							var5);
				}
			}
		}

		if (p_70037_1_.func_150297_b("HealF", 99)) {
			setHealth(p_70037_1_.getFloat("HealF"));
		} else {
			final NBTBase var6 = p_70037_1_.getTag("Health");

			if (var6 == null) {
				setHealth(getMaxHealth());
			} else if (var6.getId() == 5) {
				setHealth(((NBTTagFloat) var6).func_150288_h());
			} else if (var6.getId() == 2) {
				setHealth(((NBTTagShort) var6).func_150289_e());
			}
		}

		hurtTime = p_70037_1_.getShort("HurtTime");
		deathTime = p_70037_1_.getShort("DeathTime");
		attackTime = p_70037_1_.getShort("AttackTime");
	}

	/**
	 * Remove the specified potion effect from this entity.
	 */
	public void removePotionEffect(int p_82170_1_) {
		final PotionEffect var2 = (PotionEffect) activePotionsMap
				.remove(Integer.valueOf(p_82170_1_));

		if (var2 != null) {
			onFinishedPotionEffect(var2);
		}
	}

	/**
	 * Remove the speified potion effect from this entity.
	 */
	public void removePotionEffectClient(int p_70618_1_) {
		activePotionsMap.remove(Integer.valueOf(p_70618_1_));
	}

	/**
	 * Renders broken item particles using the given ItemStack
	 */
	public void renderBrokenItemStack(ItemStack p_70669_1_) {
		playSound("random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F);

		for (int var2 = 0; var2 < 5; ++var2) {
			final Vec3 var3 = Vec3.createVectorHelper(
					(rand.nextFloat() - 0.5D) * 0.1D,
					Math.random() * 0.1D + 0.1D, 0.0D);
			var3.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
			var3.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
			Vec3 var4 = Vec3.createVectorHelper(
					(rand.nextFloat() - 0.5D) * 0.3D,
					-rand.nextFloat() * 0.6D - 0.3D, 0.6D);
			var4.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
			var4.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
			var4 = var4.addVector(posX, posY + getEyeHeight(), posZ);
			worldObj.spawnParticle(
					"iconcrack_" + Item.getIdFromItem(p_70669_1_.getItem()),
					var4.xCoord, var4.yCoord, var4.zCoord, var3.xCoord,
					var3.yCoord + 0.05D, var3.zCoord);
		}
	}

	public void setAbsorptionAmount(float p_110149_1_) {
		if (p_110149_1_ < 0.0F) {
			p_110149_1_ = 0.0F;
		}

		field_110151_bq = p_110149_1_;
	}

	/**
	 * set the movespeed used for the new AI system
	 */
	public void setAIMoveSpeed(float p_70659_1_) {
		landMovementFactor = p_70659_1_;
	}

	/**
	 * sets the amount of arrows stuck in the entity. used for rendering those
	 */
	public final void setArrowCountInEntity(int p_85034_1_) {
		dataWatcher.updateObject(9, Byte.valueOf((byte) p_85034_1_));
	}

	/**
	 * Sets that this entity has been attacked.
	 */
	@Override
	protected void setBeenAttacked() {
		velocityChanged = rand.nextDouble() >= getEntityAttribute(
				SharedMonsterAttributes.knockbackResistance)
				.getAttributeValue();
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	@Override
	public abstract void setCurrentItemOrArmor(int p_70062_1_,
			ItemStack p_70062_2_);

	public void setHealth(float p_70606_1_) {
		dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(
				p_70606_1_, 0.0F, getMaxHealth())));
	}

	public void setJumping(boolean p_70637_1_) {
		isJumping = p_70637_1_;
	}

	public void setLastAttacker(Entity p_130011_1_) {
		if (p_130011_1_ instanceof EntityLivingBase) {
			lastAttacker = (EntityLivingBase) p_130011_1_;
		} else {
			lastAttacker = null;
		}

		lastAttackerTime = ticksExisted;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	@Override
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_,
			double p_70056_5_, float p_70056_7_, float p_70056_8_,
			int p_70056_9_) {
		yOffset = 0.0F;
		newPosX = p_70056_1_;
		newPosY = p_70056_3_;
		newPosZ = p_70056_5_;
		newRotationYaw = p_70056_7_;
		newRotationPitch = p_70056_8_;
		newPosRotationIncrements = p_70056_9_;
	}

	/**
	 * Sets the position of the entity and updates the 'last' variables
	 */
	public void setPositionAndUpdate(double p_70634_1_, double p_70634_3_,
			double p_70634_5_) {
		setLocationAndAngles(p_70634_1_, p_70634_3_, p_70634_5_, rotationYaw,
				rotationPitch);
	}

	public void setRevengeTarget(EntityLivingBase p_70604_1_) {
		entityLivingToAttack = p_70604_1_;
		revengeTimer = ticksExisted;
	}

	/**
	 * Sets the head's yaw rotation of the entity.
	 */
	@Override
	public void setRotationYawHead(float p_70034_1_) {
		rotationYawHead = p_70034_1_;
	}

	/**
	 * Set sprinting switch for Entity.
	 */
	@Override
	public void setSprinting(boolean p_70031_1_) {
		super.setSprinting(p_70031_1_);
		final IAttributeInstance var2 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (var2.getModifier(sprintingSpeedBoostModifierUUID) != null) {
			var2.removeModifier(sprintingSpeedBoostModifier);
		}

		if (p_70031_1_) {
			var2.applyModifier(sprintingSpeedBoostModifier);
		}
	}

	/**
	 * Swings the item the player is holding.
	 */
	public void swingItem() {
		if (!isSwingInProgress
				|| swingProgressInt >= getArmSwingAnimationEnd() / 2
				|| swingProgressInt < 0) {
			swingProgressInt = -1;
			isSwingInProgress = true;

			if (worldObj instanceof WorldServer) {
				((WorldServer) worldObj).getEntityTracker().func_151247_a(this,
						new S0BPacketAnimation(this, 0));
			}
		}
	}

	protected void updateAITasks() {
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {
	}

	/**
	 * Updates the arm swing progress counters and animation progress
	 */
	protected void updateArmSwingProgress() {
		final int var1 = getArmSwingAnimationEnd();

		if (isSwingInProgress) {
			++swingProgressInt;

			if (swingProgressInt >= var1) {
				swingProgressInt = 0;
				isSwingInProgress = false;
			}
		} else {
			swingProgressInt = 0;
		}

		swingProgress = (float) swingProgressInt / (float) var1;
	}

	protected void updateEntityActionState() {
		++entityAge;
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on
	 * the ground to update the fall distance and deal fall damage if landing on
	 * the ground. Args: distanceFallenThisTick, onGround
	 */
	@Override
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
		if (!isInWater()) {
			handleWaterMovement();
		}

		if (p_70064_3_ && fallDistance > 0.0F) {
			final int var4 = MathHelper.floor_double(posX);
			final int var5 = MathHelper.floor_double(posY
					- 0.20000000298023224D - yOffset);
			final int var6 = MathHelper.floor_double(posZ);
			Block var7 = worldObj.getBlock(var4, var5, var6);

			if (var7.getMaterial() == Material.air) {
				final int var8 = worldObj.getBlock(var4, var5 - 1, var6)
						.getRenderType();

				if (var8 == 11 || var8 == 32 || var8 == 21) {
					var7 = worldObj.getBlock(var4, var5 - 1, var6);
				}
			} else if (!worldObj.isClient && fallDistance > 3.0F) {
				worldObj.playAuxSFX(2006, var4, var5, var6,
						MathHelper.ceiling_float_int(fallDistance - 3.0F));
			}

			var7.onFallenUpon(worldObj, var4, var5, var6, this, fallDistance);
		}

		super.updateFallState(p_70064_1_, p_70064_3_);
	}

	protected void updatePotionEffects() {
		final Iterator var1 = activePotionsMap.keySet().iterator();

		while (var1.hasNext()) {
			final Integer var2 = (Integer) var1.next();
			final PotionEffect var3 = (PotionEffect) activePotionsMap.get(var2);

			if (!var3.onUpdate(this)) {
				if (!worldObj.isClient) {
					var1.remove();
					onFinishedPotionEffect(var3);
				}
			} else if (var3.getDuration() % 600 == 0) {
				onChangedPotionEffect(var3, false);
			}
		}

		int var11;

		if (potionsNeedUpdate) {
			if (!worldObj.isClient) {
				if (activePotionsMap.isEmpty()) {
					dataWatcher.updateObject(8, Byte.valueOf((byte) 0));
					dataWatcher.updateObject(7, Integer.valueOf(0));
					setInvisible(false);
				} else {
					var11 = PotionHelper.calcPotionLiquidColor(activePotionsMap
							.values());
					dataWatcher
							.updateObject(8,
									Byte.valueOf((byte) (PotionHelper
											.func_82817_b(activePotionsMap
													.values()) ? 1 : 0)));
					dataWatcher.updateObject(7, Integer.valueOf(var11));
					setInvisible(this.isPotionActive(Potion.invisibility.id));
				}
			}

			potionsNeedUpdate = false;
		}

		var11 = dataWatcher.getWatchableObjectInt(7);
		final boolean var12 = dataWatcher.getWatchableObjectByte(8) > 0;

		if (var11 > 0) {
			boolean var4 = false;

			if (!isInvisible()) {
				var4 = rand.nextBoolean();
			} else {
				var4 = rand.nextInt(15) == 0;
			}

			if (var12) {
				var4 &= rand.nextInt(5) == 0;
			}

			if (var4 && var11 > 0) {
				final double var5 = (var11 >> 16 & 255) / 255.0D;
				final double var7 = (var11 >> 8 & 255) / 255.0D;
				final double var9 = (var11 >> 0 & 255) / 255.0D;
				worldObj.spawnParticle(var12 ? "mobSpellAmbient" : "mobSpell",
						posX + (rand.nextDouble() - 0.5D) * width,
						posY + rand.nextDouble() * height - yOffset, posZ
								+ (rand.nextDouble() - 0.5D) * width, var5,
						var7, var9);
			}
		}
	}

	/**
	 * Handles updating while being ridden by an entity
	 */
	@Override
	public void updateRidden() {
		super.updateRidden();
		field_70768_au = field_110154_aX;
		field_110154_aX = 0.0F;
		fallDistance = 0.0F;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setFloat("HealF", getHealth());
		p_70014_1_.setShort("Health", (short) (int) Math.ceil(getHealth()));
		p_70014_1_.setShort("HurtTime", (short) hurtTime);
		p_70014_1_.setShort("DeathTime", (short) deathTime);
		p_70014_1_.setShort("AttackTime", (short) attackTime);
		p_70014_1_.setFloat("AbsorptionAmount", getAbsorptionAmount());
		ItemStack[] var2 = getLastActiveItems();
		int var3 = var2.length;
		int var4;
		ItemStack var5;

		for (var4 = 0; var4 < var3; ++var4) {
			var5 = var2[var4];

			if (var5 != null) {
				attributeMap.removeAttributeModifiers(var5
						.getAttributeModifiers());
			}
		}

		p_70014_1_.setTag("Attributes", SharedMonsterAttributes
				.writeBaseAttributeMapToNBT(getAttributeMap()));
		var2 = getLastActiveItems();
		var3 = var2.length;

		for (var4 = 0; var4 < var3; ++var4) {
			var5 = var2[var4];

			if (var5 != null) {
				attributeMap.applyAttributeModifiers(var5
						.getAttributeModifiers());
			}
		}

		if (!activePotionsMap.isEmpty()) {
			final NBTTagList var6 = new NBTTagList();
			final Iterator var7 = activePotionsMap.values().iterator();

			while (var7.hasNext()) {
				final PotionEffect var8 = (PotionEffect) var7.next();
				var6.appendTag(var8
						.writeCustomPotionEffectToNBT(new NBTTagCompound()));
			}

			p_70014_1_.setTag("ActiveEffects", var6);
		}
	}
}

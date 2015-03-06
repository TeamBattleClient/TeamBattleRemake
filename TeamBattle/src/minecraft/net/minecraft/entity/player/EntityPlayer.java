package net.minecraft.entity.player;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.EventSystem.events.EventInsideBlock;

public abstract class EntityPlayer extends EntityLivingBase implements
		ICommandSender {
	public static enum EnumChatVisibility {
		FULL("FULL", 0, 0, "options.chat.visibility.full"), HIDDEN("HIDDEN", 2,
				2, "options.chat.visibility.hidden"), SYSTEM("SYSTEM", 1, 1,
				"options.chat.visibility.system");
		private static final EntityPlayer.EnumChatVisibility[] field_151432_d = new EntityPlayer.EnumChatVisibility[values().length];

		static {
			final EntityPlayer.EnumChatVisibility[] var0 = values();
			final int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2) {
				final EntityPlayer.EnumChatVisibility var3 = var0[var2];
				field_151432_d[var3.chatVisibility] = var3;
			}
		}

		public static EntityPlayer.EnumChatVisibility getEnumChatVisibility(
				int p_151426_0_) {
			return field_151432_d[p_151426_0_ % field_151432_d.length];
		}

		private final int chatVisibility;

		private final String resourceKey;

		private EnumChatVisibility(String p_i45323_1_, int p_i45323_2_,
				int p_i45323_3_, String p_i45323_4_) {
			chatVisibility = p_i45323_3_;
			resourceKey = p_i45323_4_;
		}

		public int getChatVisibility() {
			return chatVisibility;
		}

		public String getResourceKey() {
			return resourceKey;
		}
	}

	public static enum EnumStatus {
		NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1), NOT_POSSIBLE_NOW(
				"NOT_POSSIBLE_NOW", 2), NOT_SAFE("NOT_SAFE", 5), OK("OK", 0), OTHER_PROBLEM(
				"OTHER_PROBLEM", 4), TOO_FAR_AWAY("TOO_FAR_AWAY", 3);

		private EnumStatus(String p_i1751_1_, int p_i1751_2_) {
		}
	}

	public static UUID func_146094_a(GameProfile p_146094_0_) {
		UUID var1 = p_146094_0_.getId();

		if (var1 == null) {
			var1 = UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_146094_0_
					.getName()).getBytes(Charsets.UTF_8));
		}

		return var1;
	}

	/**
	 * Ensure that a block enabling respawning exists at the specified
	 * coordinates and find an empty space nearby to spawn.
	 */
	public static ChunkCoordinates verifyRespawnCoordinates(World p_71056_0_,
			ChunkCoordinates p_71056_1_, boolean p_71056_2_) {
		final IChunkProvider var3 = p_71056_0_.getChunkProvider();
		var3.loadChunk(p_71056_1_.posX - 3 >> 4, p_71056_1_.posZ - 3 >> 4);
		var3.loadChunk(p_71056_1_.posX + 3 >> 4, p_71056_1_.posZ - 3 >> 4);
		var3.loadChunk(p_71056_1_.posX - 3 >> 4, p_71056_1_.posZ + 3 >> 4);
		var3.loadChunk(p_71056_1_.posX + 3 >> 4, p_71056_1_.posZ + 3 >> 4);

		if (p_71056_0_.getBlock(p_71056_1_.posX, p_71056_1_.posY,
				p_71056_1_.posZ) == Blocks.bed) {
			final ChunkCoordinates var8 = BlockBed.func_149977_a(p_71056_0_,
					p_71056_1_.posX, p_71056_1_.posY, p_71056_1_.posZ, 0);
			return var8;
		} else {
			final Material var4 = p_71056_0_.getBlock(p_71056_1_.posX,
					p_71056_1_.posY, p_71056_1_.posZ).getMaterial();
			final Material var5 = p_71056_0_.getBlock(p_71056_1_.posX,
					p_71056_1_.posY + 1, p_71056_1_.posZ).getMaterial();
			final boolean var6 = !var4.isSolid() && !var4.isLiquid();
			final boolean var7 = !var5.isSolid() && !var5.isLiquid();
			return p_71056_2_ && var6 && var7 ? p_71056_1_ : null;
		}
	}

	public float cameraYaw;

	/** The player's capabilities. (See class PlayerCapabilities) */
	public PlayerCapabilities capabilities = new PlayerCapabilities();
	/**
	 * The current amount of experience the player has within their Experience
	 * Bar.
	 */
	public float experience;
	/** The current experience level the player is on. */
	public int experienceLevel;

	/**
	 * The total amount of experience the player has. This also includes the
	 * amount of experience within their Experience Bar.
	 */
	public int experienceTotal;
	private final GameProfile field_146106_i;
	public float field_71079_bU;
	public float field_71082_cx;
	public double field_71085_bR;
	public float field_71089_bV;
	public double field_71091_bM;

	public double field_71094_bP;

	public double field_71095_bQ;
	public double field_71096_bN;
	public double field_71097_bO;
	private int field_82249_h;
	/**
	 * An instance of a fishing rod's hook. If this isn't null, the icon image
	 * of the fishing rod is slightly different
	 */
	public EntityFishHook fishEntity;

	/**
	 * Used to tell if the player pressed jump twice. If this is at 0 and it's
	 * pressed (And they are allowed to fly, as defined in the player's
	 * movementInput) it sets this to 7. If it's pressed and it's greater than 0
	 * enable fly.
	 */
	protected int flyToggleTimer;

	/** The food object of the player, the general hunger logic. */
	protected FoodStats foodStats = new FoodStats();

	/** Inventory of the player */
	public InventoryPlayer inventory = new InventoryPlayer(this);

	/**
	 * The Container for the player's inventory (which opens when they press E)
	 */
	public Container inventoryContainer;

	/**
	 * This is the item that is in use when the player is holding down the
	 * useItemButton (e.g., bow, food, sword)
	 */
	private ItemStack itemInUse;

	/**
	 * This field starts off equal to getMaxItemUseDuration and is decremented
	 * on each tick
	 */
	private int itemInUseCount;

	/** The Container the player has open. */
	public Container openContainer;

	/** the current location of the player */
	public ChunkCoordinates playerLocation;

	public float prevCameraYaw;
	/** Boolean value indicating weather a player is sleeping or not */
	protected boolean sleeping;
	private int sleepTimer;
	/** holds the spawn chunk of the player */
	private ChunkCoordinates spawnChunk;
	/**
	 * Whether this player's spawn point is forced, preventing execution of bed
	 * checks.
	 */
	private boolean spawnForced;

	protected float speedInAir = 0.02F;

	protected float speedOnGround = 0.1F;

	/** Holds the coordinate of the player when enter a minecraft to ride. */
	private ChunkCoordinates startMinecartRidingCoordinate;

	private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();

	/**
	 * Used by EntityPlayer to prevent too many xp orbs from getting absorbed at
	 * once.
	 */
	public int xpCooldown;

	public EntityPlayer(World p_i45324_1_, GameProfile p_i45324_2_) {
		super(p_i45324_1_);
		entityUniqueID = func_146094_a(p_i45324_2_);
		field_146106_i = p_i45324_2_;
		inventoryContainer = new ContainerPlayer(inventory,
				!p_i45324_1_.isClient, this);
		openContainer = inventoryContainer;
		yOffset = 1.62F;
		final ChunkCoordinates var3 = p_i45324_1_.getSpawnPoint();
		setLocationAndAngles(var3.posX + 0.5D, var3.posY + 1, var3.posZ + 0.5D,
				0.0F, 0.0F);
		field_70741_aB = 180.0F;
		fireResistance = 20;
	}

	public void addChatComponentMessage(IChatComponent p_146105_1_) {
	}

	/**
	 * increases exhaustion level by supplied amount
	 */
	public void addExhaustion(float p_71020_1_) {
		if (!capabilities.disableDamage) {
			if (!worldObj.isClient) {
				foodStats.addExhaustion(p_71020_1_);
			}
		}
	}

	/**
	 * Add experience points to player.
	 */
	public void addExperience(int p_71023_1_) {
		addScore(p_71023_1_);
		final int var2 = Integer.MAX_VALUE - experienceTotal;

		if (p_71023_1_ > var2) {
			p_71023_1_ = var2;
		}

		experience += (float) p_71023_1_ / (float) xpBarCap();

		for (experienceTotal += p_71023_1_; experience >= 1.0F; experience /= xpBarCap()) {
			experience = (experience - 1.0F) * xpBarCap();
			addExperienceLevel(1);
		}
	}

	/**
	 * Add experience levels to this player.
	 */
	public void addExperienceLevel(int p_82242_1_) {
		experienceLevel += p_82242_1_;

		if (experienceLevel < 0) {
			experienceLevel = 0;
			experience = 0.0F;
			experienceTotal = 0;
		}

		if (p_82242_1_ > 0 && experienceLevel % 5 == 0
				&& field_82249_h < ticksExisted - 100.0F) {
			final float var2 = experienceLevel > 30 ? 1.0F
					: experienceLevel / 30.0F;
			worldObj.playSoundAtEntity(this, "random.levelup", var2 * 0.75F,
					1.0F);
			field_82249_h = ticksExisted;
		}
	}

	/**
	 * Adds a value to a mounted movement statistic field - by minecart, boat,
	 * or pig.
	 */
	private void addMountedMovementStat(double p_71015_1_, double p_71015_3_,
			double p_71015_5_) {
		if (ridingEntity != null) {
			final int var7 = Math.round(MathHelper.sqrt_double(p_71015_1_
					* p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_
					* p_71015_5_) * 100.0F);

			if (var7 > 0) {
				if (ridingEntity instanceof EntityMinecart) {
					addStat(StatList.distanceByMinecartStat, var7);

					if (startMinecartRidingCoordinate == null) {
						startMinecartRidingCoordinate = new ChunkCoordinates(
								MathHelper.floor_double(posX),
								MathHelper.floor_double(posY),
								MathHelper.floor_double(posZ));
					} else if (startMinecartRidingCoordinate
							.getDistanceSquared(MathHelper.floor_double(posX),
									MathHelper.floor_double(posY),
									MathHelper.floor_double(posZ)) >= 1000000.0D) {
						addStat(AchievementList.onARail, 1);
					}
				} else if (ridingEntity instanceof EntityBoat) {
					addStat(StatList.distanceByBoatStat, var7);
				} else if (ridingEntity instanceof EntityPig) {
					addStat(StatList.distanceByPigStat, var7);
				} else if (ridingEntity instanceof EntityHorse) {
					addStat(StatList.field_151185_q, var7);
				}
			}
		}
	}

	/**
	 * Adds a value to a movement statistic field - like run, walk, swin or
	 * climb.
	 */
	public void addMovementStat(double p_71000_1_, double p_71000_3_,
			double p_71000_5_) {
		if (ridingEntity == null) {
			int var7;

			if (isInsideOfMaterial(Material.water)) {
				var7 = Math.round(MathHelper.sqrt_double(p_71000_1_
						* p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_
						* p_71000_5_) * 100.0F);

				if (var7 > 0) {
					addStat(StatList.distanceDoveStat, var7);
					addExhaustion(0.015F * var7 * 0.01F);
				}
			} else if (isInWater()) {
				var7 = Math.round(MathHelper.sqrt_double(p_71000_1_
						* p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (var7 > 0) {
					addStat(StatList.distanceSwumStat, var7);
					addExhaustion(0.015F * var7 * 0.01F);
				}
			} else if (isOnLadder()) {
				if (p_71000_3_ > 0.0D) {
					addStat(StatList.distanceClimbedStat,
							(int) Math.round(p_71000_3_ * 100.0D));
				}
			} else if (onGround) {
				var7 = Math.round(MathHelper.sqrt_double(p_71000_1_
						* p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (var7 > 0) {
					addStat(StatList.distanceWalkedStat, var7);

					if (isSprinting()) {
						addExhaustion(0.099999994F * var7 * 0.01F);
					} else {
						addExhaustion(0.01F * var7 * 0.01F);
					}
				}
			} else {
				var7 = Math.round(MathHelper.sqrt_double(p_71000_1_
						* p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);

				if (var7 > 25) {
					addStat(StatList.distanceFlownStat, var7);
				}
			}
		}
	}

	/**
	 * Add to player's score
	 */
	public void addScore(int p_85039_1_) {
		final int var2 = getScore();
		dataWatcher.updateObject(18, Integer.valueOf(var2 + p_85039_1_));
	}

	/**
	 * Adds a value to a statistic field.
	 */
	public void addStat(StatBase p_71064_1_, int p_71064_2_) {
	}

	/**
	 * Adds a value to the player score. Currently not actually used and the
	 * entity passed in does nothing. Args: entity, scoreToAdd
	 */
	@Override
	public void addToPlayerScore(Entity p_70084_1_, int p_70084_2_) {
		addScore(p_70084_2_);
		final Collection var3 = getWorldScoreboard().func_96520_a(
				IScoreObjectiveCriteria.totalKillCount);

		if (p_70084_1_ instanceof EntityPlayer) {
			addStat(StatList.playerKillsStat, 1);
			var3.addAll(getWorldScoreboard().func_96520_a(
					IScoreObjectiveCriteria.playerKillCount));
		} else {
			addStat(StatList.mobKillsStat, 1);
		}

		final Iterator var4 = var3.iterator();

		while (var4.hasNext()) {
			final ScoreObjective var5 = (ScoreObjective) var4.next();
			final Score var6 = getWorldScoreboard().func_96529_a(
					getCommandSenderName(), var5);
			var6.func_96648_a();
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(
				SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else if (capabilities.disableDamage && !p_70097_1_.canHarmInCreative())
			return false;
		else {
			entityAge = 0;

			if (getHealth() <= 0.0F)
				return false;
			else {
				if (isPlayerSleeping() && !worldObj.isClient) {
					wakeUpPlayer(true, true, false);
				}

				if (p_70097_1_.isDifficultyScaled()) {
					if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
						p_70097_2_ = 0.0F;
					}

					if (worldObj.difficultySetting == EnumDifficulty.EASY) {
						p_70097_2_ = p_70097_2_ / 2.0F + 1.0F;
					}

					if (worldObj.difficultySetting == EnumDifficulty.HARD) {
						p_70097_2_ = p_70097_2_ * 3.0F / 2.0F;
					}
				}

				if (p_70097_2_ == 0.0F)
					return false;
				else {
					Entity var3 = p_70097_1_.getEntity();

					if (var3 instanceof EntityArrow
							&& ((EntityArrow) var3).shootingEntity != null) {
						var3 = ((EntityArrow) var3).shootingEntity;
					}

					addStat(StatList.damageTakenStat,
							Math.round(p_70097_2_ * 10.0F));
					return super.attackEntityFrom(p_70097_1_, p_70097_2_);
				}
			}
		}
	}

	/**
	 * Attacks for the player the targeted entity with the currently equipped
	 * item. The equipped item has hitEntity called on it. Args: targetEntity
	 */
	public void attackTargetEntityWithCurrentItem(Entity p_71059_1_) {
		if (p_71059_1_.canAttackWithItem()) {
			if (!p_71059_1_.hitByEntity(this)) {
				float var2 = (float) getEntityAttribute(
						SharedMonsterAttributes.attackDamage)
						.getAttributeValue();
				int var3 = 0;
				float var4 = 0.0F;

				if (p_71059_1_ instanceof EntityLivingBase) {
					var4 = EnchantmentHelper.getEnchantmentModifierLiving(this,
							(EntityLivingBase) p_71059_1_);
					var3 += EnchantmentHelper.getKnockbackModifier(this,
							(EntityLivingBase) p_71059_1_);
				}

				if (isSprinting()) {
					++var3;
				}

				if (var2 > 0.0F || var4 > 0.0F) {
					final boolean var5 = fallDistance > 0.0F && !onGround
							&& !isOnLadder() && !isInWater()
							&& !this.isPotionActive(Potion.blindness)
							&& ridingEntity == null
							&& p_71059_1_ instanceof EntityLivingBase;

					if (var5 && var2 > 0.0F) {
						var2 *= 1.5F;
					}

					var2 += var4;
					boolean var6 = false;
					final int var7 = EnchantmentHelper
							.getFireAspectModifier(this);

					if (p_71059_1_ instanceof EntityLivingBase && var7 > 0
							&& !p_71059_1_.isBurning()) {
						var6 = true;
						p_71059_1_.setFire(1);
					}

					final boolean var8 = p_71059_1_.attackEntityFrom(
							DamageSource.causePlayerDamage(this), var2);

					if (var8) {
						if (var3 > 0) {
							p_71059_1_.addVelocity(
									-MathHelper.sin(rotationYaw
											* (float) Math.PI / 180.0F)
											* var3 * 0.5F,
									0.1D,
									MathHelper.cos(rotationYaw
											* (float) Math.PI / 180.0F)
											* var3 * 0.5F);
							motionX *= 0.6D;
							motionZ *= 0.6D;
							setSprinting(false);
						}

						if (var5) {
							onCriticalHit(p_71059_1_);
						}

						if (var4 > 0.0F) {
							onEnchantmentCritical(p_71059_1_);
						}

						if (var2 >= 18.0F) {
							triggerAchievement(AchievementList.overkill);
						}

						setLastAttacker(p_71059_1_);

						if (p_71059_1_ instanceof EntityLivingBase) {
							EnchantmentHelper.func_151384_a(
									(EntityLivingBase) p_71059_1_, this);
						}

						EnchantmentHelper.func_151385_b(this, p_71059_1_);
						final ItemStack var9 = getCurrentEquippedItem();
						Object var10 = p_71059_1_;

						if (p_71059_1_ instanceof EntityDragonPart) {
							final IEntityMultiPart var11 = ((EntityDragonPart) p_71059_1_).entityDragonObj;

							if (var11 != null
									&& var11 instanceof EntityLivingBase) {
								var10 = var11;
							}
						}

						if (var9 != null && var10 instanceof EntityLivingBase) {
							var9.hitEntity((EntityLivingBase) var10, this);

							if (var9.stackSize <= 0) {
								destroyCurrentEquippedItem();
							}
						}

						if (p_71059_1_ instanceof EntityLivingBase) {
							addStat(StatList.damageDealtStat,
									Math.round(var2 * 10.0F));

							if (var7 > 0) {
								p_71059_1_.setFire(var7 * 4);
							}
						}

						addExhaustion(0.3F);
					} else if (var6) {
						p_71059_1_.extinguish();
					}
				}
			}
		}
	}

	public boolean canAttackPlayer(EntityPlayer p_96122_1_) {
		final Team var2 = getTeam();
		final Team var3 = p_96122_1_.getTeam();
		return var2 == null ? true : !var2.isSameTeam(var3) ? true : var2
				.getAllowFriendlyFire();
	}

	public boolean canEat(boolean p_71043_1_) {
		return (p_71043_1_ || foodStats.needFood())
				&& !capabilities.disableDamage;
	}

	/**
	 * Checks if the player has the ability to harvest a block (checks current
	 * inventory item for a tool if necessary)
	 */
	public boolean canHarvestBlock(Block p_146099_1_) {
		return inventory.func_146025_b(p_146099_1_);
	}

	public boolean canPlayerEdit(int p_82247_1_, int p_82247_2_,
			int p_82247_3_, int p_82247_4_, ItemStack p_82247_5_) {
		return capabilities.allowEdit ? true : p_82247_5_ != null ? p_82247_5_
				.canEditBlocks() : false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return !capabilities.isFlying;
	}

	public void clearItemInUse() {
		itemInUse = null;
		itemInUseCount = 0;

		if (!worldObj.isClient) {
			setEating(false);
		}
	}

	/**
	 * Copies the values from the given player into this player if boolean par2
	 * is true. Always clones Ender Chest Inventory.
	 */
	public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_) {
		if (p_71049_2_) {
			inventory.copyInventory(p_71049_1_.inventory);
			setHealth(p_71049_1_.getHealth());
			foodStats = p_71049_1_.foodStats;
			experienceLevel = p_71049_1_.experienceLevel;
			experienceTotal = p_71049_1_.experienceTotal;
			experience = p_71049_1_.experience;
			setScore(p_71049_1_.getScore());
			teleportDirection = p_71049_1_.teleportDirection;
		} else if (worldObj.getGameRules().getGameRuleBooleanValue(
				"keepInventory")) {
			inventory.copyInventory(p_71049_1_.inventory);
			experienceLevel = p_71049_1_.experienceLevel;
			experienceTotal = p_71049_1_.experienceTotal;
			experience = p_71049_1_.experience;
			setScore(p_71049_1_.getScore());
		}

		theInventoryEnderChest = p_71049_1_.theInventoryEnderChest;
	}

	/**
	 * set current crafting inventory back to the 2x2 square
	 */
	protected void closeScreen() {
		openContainer = inventoryContainer;
	}

	private void collideWithPlayer(Entity p_71044_1_) {
		p_71044_1_.onCollideWithPlayer(this);
	}

	@Override
	protected void damageArmor(float p_70675_1_) {
		inventory.damageArmor(p_70675_1_);
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage
	 * from the armor first and then health second with the reduced value. Args:
	 * damageAmount
	 */
	@Override
	protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
		if (!isEntityInvulnerable()) {
			if (!p_70665_1_.isUnblockable() && isBlocking()
					&& p_70665_2_ > 0.0F) {
				p_70665_2_ = (1.0F + p_70665_2_) * 0.5F;
			}

			p_70665_2_ = applyArmorCalculations(p_70665_1_, p_70665_2_);
			p_70665_2_ = applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
			final float var3 = p_70665_2_;
			p_70665_2_ = Math.max(p_70665_2_ - getAbsorptionAmount(), 0.0F);
			setAbsorptionAmount(getAbsorptionAmount() - (var3 - p_70665_2_));

			if (p_70665_2_ != 0.0F) {
				addExhaustion(p_70665_1_.getHungerDamage());
				final float var4 = getHealth();
				setHealth(getHealth() - p_70665_2_);
				func_110142_aN().func_94547_a(p_70665_1_, var4, p_70665_2_);
			}
		}
	}

	/**
	 * Destroys the currently equipped item from the player's inventory.
	 */
	public void destroyCurrentEquippedItem() {
		inventory.setInventorySlotContents(inventory.currentItem,
				(ItemStack) null);
	}

	/**
	 * Displays the GUI for interacting with an anvil.
	 */
	public void displayGUIAnvil(int p_82244_1_, int p_82244_2_, int p_82244_3_) {
	}

	/**
	 * Displays the GUI for interacting with a book.
	 */
	public void displayGUIBook(ItemStack p_71048_1_) {
	}

	/**
	 * Displays the GUI for interacting with a chest inventory. Args:
	 * chestInventory
	 */
	public void displayGUIChest(IInventory p_71007_1_) {
	}

	public void displayGUIEnchantment(int p_71002_1_, int p_71002_2_,
			int p_71002_3_, String p_71002_4_) {
	}

	public void displayGUIHopperMinecart(EntityMinecartHopper p_96125_1_) {
	}

	public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
	}

	public void displayGUIMerchant(IMerchant p_71030_1_, String p_71030_2_) {
	}

	/**
	 * Displays the crafting GUI for a workbench.
	 */
	public void displayGUIWorkbench(int p_71058_1_, int p_71058_2_,
			int p_71058_3_) {
	}

	/**
	 * Called when player presses the drop item key
	 */
	public EntityItem dropOneItem(boolean p_71040_1_) {
		return func_146097_a(inventory.decrStackSize(
				inventory.currentItem,
				p_71040_1_ && inventory.getCurrentItem() != null ? inventory
						.getCurrentItem().stackSize : 1), false, true);
	}

	/**
	 * Args: itemstack, flag
	 */
	public EntityItem dropPlayerItemWithRandomChoice(ItemStack p_71019_1_,
			boolean p_71019_2_) {
		return func_146097_a(p_71019_1_, false, false);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
		dataWatcher.addObject(17, Float.valueOf(0.0F));
		dataWatcher.addObject(18, Integer.valueOf(0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		if (!capabilities.allowFlying) {
			if (p_70069_1_ >= 2.0F) {
				addStat(StatList.distanceFallenStat,
						(int) Math.round(p_70069_1_ * 100.0D));
			}

			super.fall(p_70069_1_);
		}
	}

	@Override
	public IChatComponent func_145748_c_() {
		final ChatComponentText var1 = new ChatComponentText(
				ScorePlayerTeam.formatPlayerName(getTeam(),
						getCommandSenderName()));
		var1.getChatStyle().setChatClickEvent(
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg "
						+ getCommandSenderName() + " "));
		return var1;
	}

	@Override
	protected String func_146067_o(int p_146067_1_) {
		return p_146067_1_ > 4 ? "game.player.hurt.fall.big"
				: "game.player.hurt.fall.small";
	}

	public void func_146093_a(TileEntityHopper p_146093_1_) {
	}

	public void func_146095_a(CommandBlockLogic p_146095_1_) {
	}

	public EntityItem func_146097_a(ItemStack p_146097_1_, boolean p_146097_2_,
			boolean p_146097_3_) {
		if (p_146097_1_ == null)
			return null;
		else if (p_146097_1_.stackSize == 0)
			return null;
		else {
			final EntityItem var4 = new EntityItem(worldObj, posX, posY
					- 0.30000001192092896D + getEyeHeight(), posZ, p_146097_1_);
			var4.delayBeforeCanPickup = 40;

			if (p_146097_3_) {
				var4.func_145799_b(getCommandSenderName());
			}

			float var5 = 0.1F;
			float var6;

			if (p_146097_2_) {
				var6 = rand.nextFloat() * 0.5F;
				final float var7 = rand.nextFloat() * (float) Math.PI * 2.0F;
				var4.motionX = -MathHelper.sin(var7) * var6;
				var4.motionZ = MathHelper.cos(var7) * var6;
				var4.motionY = 0.20000000298023224D;
			} else {
				var5 = 0.3F;
				var4.motionX = -MathHelper.sin(rotationYaw / 180.0F
						* (float) Math.PI)
						* MathHelper.cos(rotationPitch / 180.0F
								* (float) Math.PI) * var5;
				var4.motionZ = MathHelper.cos(rotationYaw / 180.0F
						* (float) Math.PI)
						* MathHelper.cos(rotationPitch / 180.0F
								* (float) Math.PI) * var5;
				var4.motionY = -MathHelper.sin(rotationPitch / 180.0F
						* (float) Math.PI)
						* var5 + 0.1F;
				var5 = 0.02F;
				var6 = rand.nextFloat() * (float) Math.PI * 2.0F;
				var5 *= rand.nextFloat();
				var4.motionX += Math.cos(var6) * var5;
				var4.motionY += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				var4.motionZ += Math.sin(var6) * var5;
			}

			joinEntityItemWithWorld(var4);
			addStat(StatList.dropStat, 1);
			return var4;
		}
	}

	public void func_146098_a(TileEntityBrewingStand p_146098_1_) {
	}

	public void func_146100_a(TileEntity p_146100_1_) {
	}

	public void func_146101_a(TileEntityFurnace p_146101_1_) {
	}

	public void func_146102_a(TileEntityDispenser p_146102_1_) {
	}

	public void func_146104_a(TileEntityBeacon p_146104_1_) {
	}

	private void func_71013_b(int p_71013_1_) {
		field_71079_bU = 0.0F;
		field_71089_bV = 0.0F;

		switch (p_71013_1_) {
		case 0:
			field_71089_bV = -1.8F;
			break;

		case 1:
			field_71079_bU = 1.8F;
			break;

		case 2:
			field_71089_bV = 1.8F;
			break;

		case 3:
			field_71079_bU = -1.8F;
		}
	}

	@Override
	public float getAbsorptionAmount() {
		return getDataWatcher().getWatchableObjectFloat(17);
	}

	/**
	 * the movespeed used for the new AI system
	 */
	@Override
	public float getAIMoveSpeed() {
		return (float) getEntityAttribute(SharedMonsterAttributes.movementSpeed)
				.getAttributeValue();
	}

	@Override
	public boolean getAlwaysRenderNameTagForRender() {
		return true;
	}

	/**
	 * When searching for vulnerable players, if a player is invisible, the
	 * return value of this is the chance of seeing them anyway.
	 */
	public float getArmorVisibility() {
		int var1 = 0;
		final ItemStack[] var2 = inventory.armorInventory;
		final int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final ItemStack var5 = var2[var4];

			if (var5 != null) {
				++var1;
			}
		}

		return (float) var1 / (float) inventory.armorInventory.length;
	}

	/**
	 * Returns the location of the bed the player will respawn at, or null if
	 * the player has not slept in a bed.
	 */
	public ChunkCoordinates getBedLocation() {
		return spawnChunk;
	}

	/**
	 * Returns the orientation of the bed in degrees.
	 */
	public float getBedOrientationInDegrees() {
		if (playerLocation != null) {
			final int var1 = worldObj.getBlockMetadata(playerLocation.posX,
					playerLocation.posY, playerLocation.posZ);
			final int var2 = BlockDirectional.func_149895_l(var1);

			switch (var2) {
			case 0:
				return 90.0F;

			case 1:
				return 0.0F;

			case 2:
				return 270.0F;

			case 3:
				return 180.0F;
			}
		}

		return 0.0F;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return field_146106_i.getName();
	}

	public ItemStack getCurrentArmor(int p_82169_1_) {
		return inventory.armorItemInSlot(p_82169_1_);
	}

	/**
	 * Returns the currently being used item by the player.
	 */
	public ItemStack getCurrentEquippedItem() {
		return inventory.getCurrentItem();
	}

	/**
	 * Returns how strong the player is against the specified block at this
	 * moment
	 */
	public float getCurrentPlayerStrVsBlock(Block p_146096_1_,
			boolean p_146096_2_) {
		float var3 = inventory.func_146023_a(p_146096_1_);

		if (var3 > 1.0F) {
			final int var4 = EnchantmentHelper.getEfficiencyModifier(this);
			final ItemStack var5 = inventory.getCurrentItem();

			if (var4 > 0 && var5 != null) {
				final float var6 = var4 * var4 + 1;

				if (!var5.func_150998_b(p_146096_1_) && var3 <= 1.0F) {
					var3 += var6 * 0.08F;
				} else {
					var3 += var6;
				}
			}
		}

		if (this.isPotionActive(Potion.digSpeed)) {
			var3 *= 1.0F + (getActivePotionEffect(Potion.digSpeed)
					.getAmplifier() + 1) * 0.2F;
		}

		if (this.isPotionActive(Potion.digSlowdown)) {
			var3 *= 1.0F - (getActivePotionEffect(Potion.digSlowdown)
					.getAmplifier() + 1) * 0.2F;
		}

		if (isInsideOfMaterial(Material.water)
				&& !EnchantmentHelper.getAquaAffinityModifier(this)) {
			var3 /= 5.0F;
		}

		if (!onGround) {
			var3 /= 5.0F;
		}

		return var3;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "game.player.die";
	}

	@Override
	public World getEntityWorld() {
		return worldObj;
	}

	/**
	 * 0: Tool in Hand; 1-4: Armor
	 */
	@Override
	public ItemStack getEquipmentInSlot(int p_71124_1_) {
		return p_71124_1_ == 0 ? inventory.getCurrentItem()
				: inventory.armorInventory[p_71124_1_ - 1];
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		if (worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
			return 0;
		else {
			final int var2 = experienceLevel * 7;
			return var2 > 100 ? 100 : var2;
		}
	}

	@Override
	public float getEyeHeight() {
		return 0.12F;
	}

	/**
	 * Returns the player's FoodStats object.
	 */
	public FoodStats getFoodStats() {
		return foodStats;
	}

	/**
	 * Returns the GameProfile for this player
	 */
	public GameProfile getGameProfile() {
		return field_146106_i;
	}

	/**
	 * Returns the item that this EntityLiving is holding, if any.
	 */
	@Override
	public ItemStack getHeldItem() {
		return inventory.getCurrentItem();
	}

	public boolean getHideCape() {
		return this.getHideCape(1);
	}

	protected boolean getHideCape(int p_82241_1_) {
		return (dataWatcher.getWatchableObjectByte(16) & 1 << p_82241_1_) != 0;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "game.player.hurt";
	}

	/**
	 * Returns the InventoryEnderChest of this player.
	 */
	public InventoryEnderChest getInventoryEnderChest() {
		return theInventoryEnderChest;
	}

	/**
	 * Gets the Icon Index of the item currently held
	 */
	@Override
	public IIcon getItemIcon(ItemStack p_70620_1_, int p_70620_2_) {
		IIcon var3 = super.getItemIcon(p_70620_1_, p_70620_2_);

		if (p_70620_1_.getItem() == Items.fishing_rod && fishEntity != null) {
			var3 = Items.fishing_rod.func_94597_g();
		} else {
			if (p_70620_1_.getItem().requiresMultipleRenderPasses())
				return p_70620_1_.getItem().getIconFromDamageForRenderPass(
						p_70620_1_.getItemDamage(), p_70620_2_);

			if (itemInUse != null && p_70620_1_.getItem() == Items.bow) {
				final int var4 = p_70620_1_.getMaxItemUseDuration()
						- itemInUseCount;

				if (var4 >= 18)
					return Items.bow.getItemIconForUseDuration(2);

				if (var4 > 13)
					return Items.bow.getItemIconForUseDuration(1);

				if (var4 > 0)
					return Items.bow.getItemIconForUseDuration(0);
			}
		}

		return var3;
	}

	/**
	 * returns the ItemStack containing the itemInUse
	 */
	public ItemStack getItemInUse() {
		return itemInUse;
	}

	/**
	 * Returns the item in use count
	 */
	public int getItemInUseCount() {
		return itemInUseCount;
	}

	/**
	 * gets the duration for how long the current itemInUse has been in use
	 */
	public int getItemInUseDuration() {
		return isUsingItem() ? itemInUse.getMaxItemUseDuration()
				- itemInUseCount : 0;
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return inventory.armorInventory;
	}

	/**
	 * Return the amount of time this entity should stay in a portal before
	 * being transported.
	 */
	@Override
	public int getMaxInPortalTime() {
		return capabilities.disableDamage ? 0 : 80;
	}

	/**
	 * Return the amount of cooldown before this entity can use a portal again.
	 */
	@Override
	public int getPortalCooldown() {
		return 10;
	}

	public int getScore() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	public int getSleepTimer() {
		return sleepTimer;
	}

	@Override
	protected String getSplashSound() {
		return "game.player.swim.splash";
	}

	@Override
	protected String getSwimSound() {
		return "game.player.swim";
	}

	@Override
	public Team getTeam() {
		return getWorldScoreboard().getPlayersTeam(getCommandSenderName());
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue() {
		return inventory.getTotalArmorValue();
	}

	public Scoreboard getWorldScoreboard() {
		return worldObj.getScoreboard();
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset() {
		return yOffset - 0.5F;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 9) {
			onItemUseFinish();
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public boolean interactWith(Entity p_70998_1_) {
		ItemStack var2 = getCurrentEquippedItem();
		final ItemStack var3 = var2 != null ? var2.copy() : null;

		if (!p_70998_1_.interactFirst(this)) {
			if (var2 != null && p_70998_1_ instanceof EntityLivingBase) {
				if (capabilities.isCreativeMode) {
					var2 = var3;
				}

				if (var2.interactWithEntity(this, (EntityLivingBase) p_70998_1_)) {
					if (var2.stackSize <= 0 && !capabilities.isCreativeMode) {
						destroyCurrentEquippedItem();
					}

					return true;
				}
			}

			return false;
		} else {
			if (var2 != null && var2 == getCurrentEquippedItem()) {
				if (var2.stackSize <= 0 && !capabilities.isCreativeMode) {
					destroyCurrentEquippedItem();
				} else if (var2.stackSize < var3.stackSize
						&& capabilities.isCreativeMode) {
					var2.stackSize = var3.stackSize;
				}
			}

			return true;
		}
	}

	public boolean isBlocking() {
		return isUsingItem()
				&& itemInUse.getItem().getItemUseAction(itemInUse) == EnumAction.block;
	}

	/**
	 * Returns true if the given block can be mined with the current tool in
	 * adventure mode.
	 */
	public boolean isCurrentToolAdventureModeExempt(int p_82246_1_,
			int p_82246_2_, int p_82246_3_) {
		if (capabilities.allowEdit)
			return true;
		else {
			final Block var4 = worldObj.getBlock(p_82246_1_, p_82246_2_,
					p_82246_3_);

			if (var4.getMaterial() != Material.air) {
				if (var4.getMaterial().isAdventureModeExempt())
					return true;

				if (getCurrentEquippedItem() != null) {
					final ItemStack var5 = getCurrentEquippedItem();

					if (var5.func_150998_b(var4)
							|| var5.func_150997_a(var4) > 1.0F)
						return true;
				}
			}

			return false;
		}
	}

	/**
	 * Checks if this entity is inside of an opaque block
	 */
	@Override
	public boolean isEntityInsideOpaqueBlock() {
		final EventInsideBlock event = new EventInsideBlock();
		TeamBattleClient.getEventManager().call(event);
		return !sleeping && super.isEntityInsideOpaqueBlock()
				&& !event.isCancelled();
	}

	/**
	 * Checks if the player is currently in a bed
	 */
	private boolean isInBed() {
		return worldObj.getBlock(playerLocation.posX, playerLocation.posY,
				playerLocation.posZ) == Blocks.bed;
	}

	/**
	 * Only used by renderer in EntityLivingBase subclasses.\nDetermines if an
	 * entity is visible or not to a specfic player, if the entity is normally
	 * invisible.\nFor EntityLivingBase subclasses, returning false when
	 * invisible will render the entity semitransparent.
	 */
	@Override
	public boolean isInvisibleToPlayer(EntityPlayer p_98034_1_) {
		if (!isInvisible())
			return false;
		else {
			final Team var2 = getTeam();
			return var2 == null || p_98034_1_ == null
					|| p_98034_1_.getTeam() != var2 || !var2.func_98297_h();
		}
	}

	/**
	 * Dead and sleeping entities cannot move
	 */
	@Override
	protected boolean isMovementBlocked() {
		return getHealth() <= 0.0F || isPlayerSleeping();
	}

	/**
	 * Only use is to identify if class is an instance of player for experience
	 * dropping
	 */
	@Override
	protected boolean isPlayer() {
		return true;
	}

	/**
	 * Returns whether or not the player is asleep and the screen has fully
	 * faded.
	 */
	public boolean isPlayerFullyAsleep() {
		return sleeping && sleepTimer >= 100;
	}

	/**
	 * Returns whether player is sleeping or not
	 */
	@Override
	public boolean isPlayerSleeping() {
		return sleeping;
	}

	@Override
	public boolean isPushedByWater() {
		return !capabilities.isFlying;
	}

	public boolean isSpawnForced() {
		return spawnForced;
	}

	/**
	 * Checks if the entity is currently using an item (e.g., bow, food, sword)
	 * by holding down the useItemButton
	 */
	public boolean isUsingItem() {
		return itemInUse != null;
	}

	/**
	 * Joins the passed in entity item with the world. Args: entityItem
	 */
	protected void joinEntityItemWithWorld(EntityItem p_71012_1_) {
		worldObj.spawnEntityInWorld(p_71012_1_);
	}

	/**
	 * Causes this entity to do an upwards motion (jumping).
	 */
	@Override
	public void jump() {
		super.jump();
		addStat(StatList.jumpStat, 1);

		if (isSprinting()) {
			addExhaustion(0.8F);
		} else {
			addExhaustion(0.2F);
		}
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	@Override
	public void mountEntity(Entity p_70078_1_) {
		if (ridingEntity != null && p_70078_1_ == null) {
			if (!worldObj.isClient) {
				dismountEntity(ridingEntity);
			}

			if (ridingEntity != null) {
				ridingEntity.riddenByEntity = null;
			}

			ridingEntity = null;
		} else {
			super.mountEntity(p_70078_1_);
		}
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		final double var3 = posX;
		final double var5 = posY;
		final double var7 = posZ;

		if (capabilities.isFlying && ridingEntity == null) {
			final double var9 = motionY;
			final float var11 = jumpMovementFactor;
			jumpMovementFactor = capabilities.getFlySpeed();
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			motionY = var9 * 0.6D;
			jumpMovementFactor = var11;
		} else {
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		}

		addMovementStat(posX - var3, posY - var5, posZ - var7);
	}

	/**
	 * Called when the player performs a critical hit on the Entity. Args:
	 * entity that was hit critically
	 */
	public void onCriticalHit(Entity p_71009_1_) {
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);
		setSize(0.2F, 0.2F);
		setPosition(posX, posY, posZ);
		motionY = 0.10000000149011612D;

		if (getCommandSenderName().equals("Notch")) {
			func_146097_a(new ItemStack(Items.apple, 1), true, false);
		}

		if (!worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
			inventory.dropAllItems();
		}

		if (p_70645_1_ != null) {
			motionX = -MathHelper.cos((attackedAtYaw + rotationYaw)
					* (float) Math.PI / 180.0F) * 0.1F;
			motionZ = -MathHelper.sin((attackedAtYaw + rotationYaw)
					* (float) Math.PI / 180.0F) * 0.1F;
		} else {
			motionX = motionZ = 0.0D;
		}

		yOffset = 0.1F;
		addStat(StatList.deathsStat, 1);
	}

	public void onEnchantmentCritical(Entity p_71047_1_) {
	}

	/**
	 * Used for when item use count runs out, ie: eating completed
	 */
	protected void onItemUseFinish() {
		if (itemInUse != null) {
			updateItemUse(itemInUse, 16);
			final int var1 = itemInUse.stackSize;
			final ItemStack var2 = itemInUse.onFoodEaten(worldObj, this);

			if (var2 != itemInUse || var2 != null && var2.stackSize != var1) {
				inventory.mainInventory[inventory.currentItem] = var2;

				if (var2.stackSize == 0) {
					inventory.mainInventory[inventory.currentItem] = null;
				}
			}

			clearItemInUse();
		}
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	@Override
	public void onKillEntity(EntityLivingBase p_70074_1_) {
		if (p_70074_1_ instanceof IMob) {
			triggerAchievement(AchievementList.killEnemy);
		}

		final int var2 = EntityList.getEntityID(p_70074_1_);
		final EntityList.EntityEggInfo var3 = (EntityList.EntityEggInfo) EntityList.entityEggs
				.get(Integer.valueOf(var2));

		if (var3 != null) {
			addStat(var3.field_151512_d, 1);
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (flyToggleTimer > 0) {
			--flyToggleTimer;
		}

		if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL
				&& getHealth() < getMaxHealth()
				&& worldObj.getGameRules().getGameRuleBooleanValue(
						"naturalRegeneration") && ticksExisted % 20 * 12 == 0) {
			heal(1.0F);
		}

		inventory.decrementAnimations();
		prevCameraYaw = cameraYaw;
		super.onLivingUpdate();
		final IAttributeInstance var1 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

		if (!worldObj.isClient) {
			var1.setBaseValue(capabilities.getWalkSpeed());
		}

		jumpMovementFactor = speedInAir;

		if (isSprinting()) {
			jumpMovementFactor = (float) (jumpMovementFactor + speedInAir * 0.3D);
		}

		setAIMoveSpeed((float) var1.getAttributeValue());
		float var2 = MathHelper.sqrt_double(motionX * motionX + motionZ
				* motionZ);
		float var3 = (float) Math.atan(-motionY * 0.20000000298023224D) * 15.0F;

		if (var2 > 0.1F) {
			var2 = 0.1F;
		}

		if (!onGround || getHealth() <= 0.0F) {
			var2 = 0.0F;
		}

		if (onGround || getHealth() <= 0.0F) {
			var3 = 0.0F;
		}

		cameraYaw += (var2 - cameraYaw) * 0.4F;
		cameraPitch += (var3 - cameraPitch) * 0.8F;

		if (getHealth() > 0.0F) {
			AxisAlignedBB var4 = null;

			if (ridingEntity != null && !ridingEntity.isDead) {
				var4 = boundingBox.func_111270_a(ridingEntity.boundingBox)
						.expand(1.0D, 0.0D, 1.0D);
			} else {
				var4 = boundingBox.expand(1.0D, 0.5D, 1.0D);
			}

			final List var5 = worldObj.getEntitiesWithinAABBExcludingEntity(
					this, var4);

			if (var5 != null) {
				for (int var6 = 0; var6 < var5.size(); ++var6) {
					final Entity var7 = (Entity) var5.get(var6);

					if (!var7.isDead) {
						collideWithPlayer(var7);
					}
				}
			}
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (itemInUse != null) {
			final ItemStack var1 = inventory.getCurrentItem();

			if (var1 == itemInUse) {
				if (itemInUseCount <= 25 && itemInUseCount % 4 == 0) {
					updateItemUse(var1, 5);
				}

				if (--itemInUseCount == 0 && !worldObj.isClient) {
					onItemUseFinish();
				}
			} else {
				clearItemInUse();
			}
		}

		if (xpCooldown > 0) {
			--xpCooldown;
		}

		if (isPlayerSleeping()) {
			++sleepTimer;

			if (sleepTimer > 100) {
				sleepTimer = 100;
			}

			if (!worldObj.isClient) {
				if (!isInBed()) {
					wakeUpPlayer(true, true, false);
				} else if (worldObj.isDaytime()) {
					wakeUpPlayer(false, true, true);
				}
			}
		} else if (sleepTimer > 0) {
			++sleepTimer;

			if (sleepTimer >= 110) {
				sleepTimer = 0;
			}
		}

		super.onUpdate();

		if (!worldObj.isClient && openContainer != null
				&& !openContainer.canInteractWith(this)) {
			closeScreen();
			openContainer = inventoryContainer;
		}

		if (isBurning() && capabilities.disableDamage) {
			extinguish();
		}

		field_71091_bM = field_71094_bP;
		field_71096_bN = field_71095_bQ;
		field_71097_bO = field_71085_bR;
		final double var9 = posX - field_71094_bP;
		final double var3 = posY - field_71095_bQ;
		final double var5 = posZ - field_71085_bR;
		final double var7 = 10.0D;

		if (var9 > var7) {
			field_71091_bM = field_71094_bP = posX;
		}

		if (var5 > var7) {
			field_71097_bO = field_71085_bR = posZ;
		}

		if (var3 > var7) {
			field_71096_bN = field_71095_bQ = posY;
		}

		if (var9 < -var7) {
			field_71091_bM = field_71094_bP = posX;
		}

		if (var5 < -var7) {
			field_71097_bO = field_71085_bR = posZ;
		}

		if (var3 < -var7) {
			field_71096_bN = field_71095_bQ = posY;
		}

		field_71094_bP += var9 * 0.25D;
		field_71085_bR += var5 * 0.25D;
		field_71095_bQ += var3 * 0.25D;

		if (ridingEntity == null) {
			startMinecartRidingCoordinate = null;
		}

		if (!worldObj.isClient) {
			foodStats.onUpdate(this);
			addStat(StatList.minutesPlayedStat, 1);
		}
	}

	@Override
	public void playSound(String p_85030_1_, float p_85030_2_, float p_85030_3_) {
		worldObj.playSoundToNearExcept(this, p_85030_1_, p_85030_2_, p_85030_3_);
	}

	/**
	 * Keeps moving the entity up so it isn't colliding with blocks and other
	 * requirements for this entity to be spawned (only actually used on players
	 * though its also on Entity)
	 */
	@Override
	public void preparePlayerToSpawn() {
		yOffset = 1.62F;
		setSize(0.6F, 1.8F);
		super.preparePlayerToSpawn();
		setHealth(getMaxHealth());
		deathTime = 0;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		entityUniqueID = func_146094_a(field_146106_i);
		final NBTTagList var2 = p_70037_1_.getTagList("Inventory", 10);
		inventory.readFromNBT(var2);
		inventory.currentItem = p_70037_1_.getInteger("SelectedItemSlot");
		sleeping = p_70037_1_.getBoolean("Sleeping");
		sleepTimer = p_70037_1_.getShort("SleepTimer");
		experience = p_70037_1_.getFloat("XpP");
		experienceLevel = p_70037_1_.getInteger("XpLevel");
		experienceTotal = p_70037_1_.getInteger("XpTotal");
		setScore(p_70037_1_.getInteger("Score"));

		if (sleeping) {
			playerLocation = new ChunkCoordinates(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ));
			wakeUpPlayer(true, true, false);
		}

		if (p_70037_1_.func_150297_b("SpawnX", 99)
				&& p_70037_1_.func_150297_b("SpawnY", 99)
				&& p_70037_1_.func_150297_b("SpawnZ", 99)) {
			spawnChunk = new ChunkCoordinates(p_70037_1_.getInteger("SpawnX"),
					p_70037_1_.getInteger("SpawnY"),
					p_70037_1_.getInteger("SpawnZ"));
			spawnForced = p_70037_1_.getBoolean("SpawnForced");
		}

		foodStats.readNBT(p_70037_1_);
		capabilities.readCapabilitiesFromNBT(p_70037_1_);

		if (p_70037_1_.func_150297_b("EnderItems", 9)) {
			final NBTTagList var3 = p_70037_1_.getTagList("EnderItems", 10);
			theInventoryEnderChest.loadInventoryFromNBT(var3);
		}
	}

	/**
	 * sets the players height back to normal after doing things like sleeping
	 * and dieing
	 */
	protected void resetHeight() {
		yOffset = 1.62F;
	}

	public void respawnPlayer() {
	}

	/**
	 * Sends the player's abilities to the server (if there is one).
	 */
	public void sendPlayerAbilities() {
	}

	@Override
	public void setAbsorptionAmount(float p_110149_1_) {
		if (p_110149_1_ < 0.0F) {
			p_110149_1_ = 0.0F;
		}

		getDataWatcher().updateObject(17, Float.valueOf(p_110149_1_));
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	@Override
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
		inventory.armorInventory[p_70062_1_] = p_70062_2_;
	}

	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void setDead() {
		super.setDead();
		inventoryContainer.onContainerClosed(this);

		if (openContainer != null) {
			openContainer.onContainerClosed(this);
		}
	}

	/**
	 * Sets the player's game mode and sends it to them.
	 */
	public void setGameType(WorldSettings.GameType p_71033_1_) {
	}

	protected void setHideCape(int p_82239_1_, boolean p_82239_2_) {
		final byte var3 = dataWatcher.getWatchableObjectByte(16);

		if (p_82239_2_) {
			dataWatcher.updateObject(16,
					Byte.valueOf((byte) (var3 | 1 << p_82239_1_)));
		} else {
			dataWatcher.updateObject(16,
					Byte.valueOf((byte) (var3 & ~(1 << p_82239_1_))));
		}
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	@Override
	public void setInWeb() {
		if (!capabilities.isFlying) {
			super.setInWeb();
		}
	}

	/**
	 * sets the itemInUse when the use item button is clicked. Args: itemstack,
	 * int maxItemUseDuration
	 */
	public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_) {
		if (p_71008_1_ != itemInUse) {
			itemInUse = p_71008_1_;
			itemInUseCount = p_71008_2_;

			if (!worldObj.isClient) {
				setEating(true);
			}
		}
	}

	/**
	 * Set player's score
	 */
	public void setScore(int p_85040_1_) {
		dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
	}

	/**
	 * Defines a spawn coordinate to player spawn. Used by bed after the player
	 * sleep on it.
	 */
	public void setSpawnChunk(ChunkCoordinates p_71063_1_, boolean p_71063_2_) {
		if (p_71063_1_ != null) {
			spawnChunk = new ChunkCoordinates(p_71063_1_);
			spawnForced = p_71063_2_;
		} else {
			spawnChunk = null;
			spawnForced = false;
		}
	}

	/**
	 * Checks if the player's health is not full and not zero.
	 */
	public boolean shouldHeal() {
		return getHealth() > 0.0F && getHealth() < getMaxHealth();
	}

	/**
	 * puts player to sleep on specified bed if possible
	 */
	public EntityPlayer.EnumStatus sleepInBedAt(int p_71018_1_, int p_71018_2_,
			int p_71018_3_) {
		if (!worldObj.isClient) {
			if (isPlayerSleeping() || !isEntityAlive())
				return EntityPlayer.EnumStatus.OTHER_PROBLEM;

			if (!worldObj.provider.isSurfaceWorld())
				return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;

			if (worldObj.isDaytime())
				return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;

			if (Math.abs(posX - p_71018_1_) > 3.0D
					|| Math.abs(posY - p_71018_2_) > 2.0D
					|| Math.abs(posZ - p_71018_3_) > 3.0D)
				return EntityPlayer.EnumStatus.TOO_FAR_AWAY;

			final double var4 = 8.0D;
			final double var6 = 5.0D;
			final List var8 = worldObj.getEntitiesWithinAABB(EntityMob.class,
					AxisAlignedBB.getBoundingBox(p_71018_1_ - var4, p_71018_2_
							- var6, p_71018_3_ - var4, p_71018_1_ + var4,
							p_71018_2_ + var6, p_71018_3_ + var4));

			if (!var8.isEmpty())
				return EntityPlayer.EnumStatus.NOT_SAFE;
		}

		if (isRiding()) {
			mountEntity((Entity) null);
		}

		setSize(0.2F, 0.2F);
		yOffset = 0.2F;

		if (worldObj.blockExists(p_71018_1_, p_71018_2_, p_71018_3_)) {
			final int var9 = worldObj.getBlockMetadata(p_71018_1_, p_71018_2_,
					p_71018_3_);
			final int var5 = BlockDirectional.func_149895_l(var9);
			float var10 = 0.5F;
			float var7 = 0.5F;

			switch (var5) {
			case 0:
				var7 = 0.9F;
				break;

			case 1:
				var10 = 0.1F;
				break;

			case 2:
				var7 = 0.1F;
				break;

			case 3:
				var10 = 0.9F;
			}

			func_71013_b(var5);
			setPosition(p_71018_1_ + var10, p_71018_2_ + 0.9375F, p_71018_3_
					+ var7);
		} else {
			setPosition(p_71018_1_ + 0.5F, p_71018_2_ + 0.9375F,
					p_71018_3_ + 0.5F);
		}

		sleeping = true;
		sleepTimer = 0;
		playerLocation = new ChunkCoordinates(p_71018_1_, p_71018_2_,
				p_71018_3_);
		motionX = motionZ = motionY = 0.0D;

		if (!worldObj.isClient) {
			worldObj.updateAllPlayersSleepingFlag();
		}

		return EntityPlayer.EnumStatus.OK;
	}

	public void stopUsingItem() {
		if (itemInUse != null) {
			itemInUse.onPlayerStoppedUsing(worldObj, this, itemInUseCount);
		}

		clearItemInUse();
	}

	/**
	 * Will trigger the specified trigger.
	 */
	public void triggerAchievement(StatBase p_71029_1_) {
		addStat(p_71029_1_, 1);
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();
		updateArmSwingProgress();
	}

	/**
	 * Plays sounds and makes particles for item in use state
	 */
	protected void updateItemUse(ItemStack p_71010_1_, int p_71010_2_) {
		if (p_71010_1_.getItemUseAction() == EnumAction.drink) {
			playSound("random.drink", 0.5F,
					worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (p_71010_1_.getItemUseAction() == EnumAction.eat) {
			for (int var3 = 0; var3 < p_71010_2_; ++var3) {
				final Vec3 var4 = Vec3.createVectorHelper(
						(rand.nextFloat() - 0.5D) * 0.1D,
						Math.random() * 0.1D + 0.1D, 0.0D);
				var4.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
				var4.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
				Vec3 var5 = Vec3.createVectorHelper(
						(rand.nextFloat() - 0.5D) * 0.3D,
						-rand.nextFloat() * 0.6D - 0.3D, 0.6D);
				var5.rotateAroundX(-rotationPitch * (float) Math.PI / 180.0F);
				var5.rotateAroundY(-rotationYaw * (float) Math.PI / 180.0F);
				var5 = var5.addVector(posX, posY + getEyeHeight(), posZ);
				String var6 = "iconcrack_"
						+ Item.getIdFromItem(p_71010_1_.getItem());

				if (p_71010_1_.getHasSubtypes()) {
					var6 = var6 + "_" + p_71010_1_.getItemDamage();
				}

				worldObj.spawnParticle(var6, var5.xCoord, var5.yCoord,
						var5.zCoord, var4.xCoord, var4.yCoord + 0.05D,
						var4.zCoord);
			}

			playSound("random.eat", 0.5F + 0.5F * rand.nextInt(2),
					(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		}
	}

	/**
	 * Handles updating while being ridden by an entity
	 */
	@Override
	public void updateRidden() {
		if (!worldObj.isClient && isSneaking()) {
			mountEntity((Entity) null);
			setSneaking(false);
		} else {
			final double var1 = posX;
			final double var3 = posY;
			final double var5 = posZ;
			final float var7 = rotationYaw;
			final float var8 = rotationPitch;
			super.updateRidden();
			prevCameraYaw = cameraYaw;
			cameraYaw = 0.0F;
			addMountedMovementStat(posX - var1, posY - var3, posZ - var5);

			if (ridingEntity instanceof EntityPig) {
				rotationPitch = var8;
				rotationYaw = var7;
				renderYawOffset = ((EntityPig) ridingEntity).renderYawOffset;
			}
		}
	}

	/**
	 * Wake up the player if they're sleeping.
	 */
	public void wakeUpPlayer(boolean p_70999_1_, boolean p_70999_2_,
			boolean p_70999_3_) {
		setSize(0.6F, 1.8F);
		resetHeight();
		final ChunkCoordinates var4 = playerLocation;
		ChunkCoordinates var5 = playerLocation;

		if (var4 != null
				&& worldObj.getBlock(var4.posX, var4.posY, var4.posZ) == Blocks.bed) {
			BlockBed.func_149979_a(worldObj, var4.posX, var4.posY, var4.posZ,
					false);
			var5 = BlockBed.func_149977_a(worldObj, var4.posX, var4.posY,
					var4.posZ, 0);

			if (var5 == null) {
				var5 = new ChunkCoordinates(var4.posX, var4.posY + 1, var4.posZ);
			}

			setPosition(var5.posX + 0.5F, var5.posY + yOffset + 0.1F,
					var5.posZ + 0.5F);
		}

		sleeping = false;

		if (!worldObj.isClient && p_70999_2_) {
			worldObj.updateAllPlayersSleepingFlag();
		}

		if (p_70999_1_) {
			sleepTimer = 0;
		} else {
			sleepTimer = 100;
		}

		if (p_70999_3_) {
			setSpawnChunk(playerLocation, false);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setTag("Inventory", inventory.writeToNBT(new NBTTagList()));
		p_70014_1_.setInteger("SelectedItemSlot", inventory.currentItem);
		p_70014_1_.setBoolean("Sleeping", sleeping);
		p_70014_1_.setShort("SleepTimer", (short) sleepTimer);
		p_70014_1_.setFloat("XpP", experience);
		p_70014_1_.setInteger("XpLevel", experienceLevel);
		p_70014_1_.setInteger("XpTotal", experienceTotal);
		p_70014_1_.setInteger("Score", getScore());

		if (spawnChunk != null) {
			p_70014_1_.setInteger("SpawnX", spawnChunk.posX);
			p_70014_1_.setInteger("SpawnY", spawnChunk.posY);
			p_70014_1_.setInteger("SpawnZ", spawnChunk.posZ);
			p_70014_1_.setBoolean("SpawnForced", spawnForced);
		}

		foodStats.writeNBT(p_70014_1_);
		capabilities.writeCapabilitiesToNBT(p_70014_1_);
		p_70014_1_.setTag("EnderItems",
				theInventoryEnderChest.saveInventoryToNBT());
	}

	/**
	 * This method returns the cap amount of experience that the experience bar
	 * can hold. With each level, the experience cap on the player's experience
	 * bar is raised by 10.
	 */
	public int xpBarCap() {
		return experienceLevel >= 30 ? 62 + (experienceLevel - 30) * 7
				: experienceLevel >= 15 ? 17 + (experienceLevel - 15) * 3 : 17;
	}
}

package net.minecraft.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

import me.client.Client;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import event.events.EventEntityStep;
import event.events.EventEntityStepPost;

public abstract class Entity {
	public static enum EnumEntitySize {
		SIZE_1("SIZE_1", 0), SIZE_2("SIZE_2", 1), SIZE_3("SIZE_3", 2), SIZE_4(
				"SIZE_4", 3), SIZE_5("SIZE_5", 4), SIZE_6("SIZE_6", 5);

		private EnumEntitySize(String p_i1581_1_, int p_i1581_2_) {
		}

		public int multiplyBy32AndRound(double p_75630_1_) {
			final double var3 = p_75630_1_
					- (MathHelper.floor_double(p_75630_1_) + 0.5D);

			switch (Entity.SwitchEnumEntitySize.field_96565_a[ordinal()]) {
			case 1:
				if (var3 < 0.0D) {
					if (var3 < -0.3125D)
						return MathHelper
								.ceiling_double_int(p_75630_1_ * 32.0D);
				} else if (var3 < 0.3125D)
					return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

				return MathHelper.floor_double(p_75630_1_ * 32.0D);

			case 2:
				if (var3 < 0.0D) {
					if (var3 < -0.3125D)
						return MathHelper.floor_double(p_75630_1_ * 32.0D);
				} else if (var3 < 0.3125D)
					return MathHelper.floor_double(p_75630_1_ * 32.0D);

				return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

			case 3:
				if (var3 > 0.0D)
					return MathHelper.floor_double(p_75630_1_ * 32.0D);

				return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

			case 4:
				if (var3 < 0.0D) {
					if (var3 < -0.1875D)
						return MathHelper
								.ceiling_double_int(p_75630_1_ * 32.0D);
				} else if (var3 < 0.1875D)
					return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

				return MathHelper.floor_double(p_75630_1_ * 32.0D);

			case 5:
				if (var3 < 0.0D) {
					if (var3 < -0.1875D)
						return MathHelper.floor_double(p_75630_1_ * 32.0D);
				} else if (var3 < 0.1875D)
					return MathHelper.floor_double(p_75630_1_ * 32.0D);

				return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

			case 6:
			default:
				if (var3 > 0.0D)
					return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
				else
					return MathHelper.floor_double(p_75630_1_ * 32.0D);
			}
		}
	}

	static final class SwitchEnumEntitySize {
		static final int[] field_96565_a = new int[Entity.EnumEntitySize
				.values().length];

		static {
			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_1.ordinal()] = 1;
			} catch (final NoSuchFieldError var6) {
				;
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_2.ordinal()] = 2;
			} catch (final NoSuchFieldError var5) {
				;
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_3.ordinal()] = 3;
			} catch (final NoSuchFieldError var4) {
				;
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_4.ordinal()] = 4;
			} catch (final NoSuchFieldError var3) {
				;
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_5.ordinal()] = 5;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				field_96565_a[Entity.EnumEntitySize.SIZE_6.ordinal()] = 6;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	private static int nextEntityID;

	/** Has this entity been added to the chunk its within */
	public boolean addedToChunk;

	/** Axis aligned bounding box. */
	public final AxisAlignedBB boundingBox;

	public int chunkCoordX;
	public int chunkCoordY;

	public int chunkCoordZ;
	protected DataWatcher dataWatcher;
	/** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
	public int dimension;
	/** The distance walked multiplied by 0.6 */
	public float distanceWalkedModified;

	public float distanceWalkedOnStepModified;

	/**
	 * Reduces the velocity applied by entity collisions by the specified
	 * percent.
	 */
	public float entityCollisionReduction;

	private double entityRiderPitchDelta;

	private double entityRiderYawDelta;

	protected UUID entityUniqueID;

	public float fallDistance;

	private int field_145783_c;

	public boolean field_70135_K;
	public int fire;
	/**
	 * The amount of ticks you have to stand inside of fire before be set on
	 * fire
	 */
	public int fireResistance;

	private boolean firstUpdate;
	public boolean forceSpawn;

	/** How high this entity is considered to be */
	public float height;

	/**
	 * Remaining time an entity will be "immune" to further damage after being
	 * hurt.
	 */
	public int hurtResistantTime;

	/**
	 * Render entity even if it is outside the camera frustum. Only true in
	 * EntityFish for now. Used in RenderGlobal: render if ignoreFrustumCheck or
	 * in frustum.
	 */
	public boolean ignoreFrustumCheck;
	/** Whether the entity is inside a Portal */
	protected boolean inPortal;
	private boolean invulnerable;
	/**
	 * Whether this entity is currently inside of water (if it handles water
	 * movement that is)
	 */
	protected boolean inWater;

	public boolean isAirBorne;
	/**
	 * True if after a move this entity has collided with something either
	 * vertically or horizontally
	 */
	public boolean isCollided;

	/**
	 * True if after a move this entity has collided with something on X- or
	 * Z-axis
	 */
	public boolean isCollidedHorizontally;

	/**
	 * True if after a move this entity has collided with something on Y-axis
	 */
	public boolean isCollidedVertically;

	/**
	 * Gets set by setDead, so this must be the flag whether an Entity is dead
	 * (inactive may be better term)
	 */
	public boolean isDead;

	protected boolean isImmuneToFire;
	protected boolean isInWeb;
	/**
	 * The entity's X coordinate at the previous tick, used to calculate
	 * position during rendering routines
	 */
	public double lastTickPosX;

	/**
	 * The entity's Y coordinate at the previous tick, used to calculate
	 * position during rendering routines
	 */
	public double lastTickPosY;

	/**
	 * The entity's Z coordinate at the previous tick, used to calculate
	 * position during rendering routines
	 */
	public double lastTickPosZ;

	/** Entity motion X */
	public double motionX;

	/** Entity motion Y */
	public double motionY;
	/** Entity motion Z */
	public double motionZ;

	public Entity.EnumEntitySize myEntitySize;

	/**
	 * The distance that has to be exceeded in order to triger a new step sound
	 * and an onEntityWalking event on a block
	 */
	private int nextStepDistance;

	/**
	 * Whether this entity won't clip with collision or not (make note it won't
	 * disable gravity)
	 */
	public boolean noClip;
	public boolean onGround;

	protected int portalCounter;

	/** Entity position X */
	public double posX;
	/** Entity position Y */
	public double posY;

	/** Entity position Z */
	public double posZ;

	/** The previous ticks distance walked multiplied by 0.6 */
	public float prevDistanceWalkedModified;
	/**
	 * Blocks entities from spawning when they do their AABB check to make sure
	 * the spot is clear of entities that can prevent spawning.
	 */
	public boolean preventEntitySpawning;
	public double prevPosX;
	public double prevPosY;
	public double prevPosZ;
	public float prevRotationPitch;

	public float prevRotationYaw;
	protected Random rand;
	public double renderDistanceWeight;
	/** The entity that is riding this entity */
	public Entity riddenByEntity;
	/** The entity we are currently riding */
	public Entity ridingEntity;
	/** Entity rotation Pitch */
	public float rotationPitch;
	/** Entity rotation Yaw */
	public float rotationYaw;

	public int serverPosX;
	public int serverPosY;
	public int serverPosZ;

	/**
	 * How high this entity can step up when running into a block to try to get
	 * over it (currently make note the entity will always step up this amount
	 * and not just the amount needed)
	 */
	public float stepHeight;
	protected int teleportDirection;

	/** How many ticks has this entity had ran since being alive */
	public int ticksExisted;
	public int timeUntilPortal;
	public boolean velocityChanged;
	/** How wide this entity is considered to be */
	public float width;
	/** Reference to the World object. */
	public World worldObj;

	public float yOffset;

	public float ySize;

	public Entity(World p_i1582_1_) {
		field_145783_c = nextEntityID++;
		renderDistanceWeight = 1.0D;
		boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D,
				0.0D, 0.0D);
		field_70135_K = true;
		width = 0.6F;
		height = 1.8F;
		nextStepDistance = 1;
		rand = new Random();
		fireResistance = 1;
		firstUpdate = true;
		entityUniqueID = UUID.randomUUID();
		myEntitySize = Entity.EnumEntitySize.SIZE_2;
		worldObj = p_i1582_1_;
		setPosition(0.0D, 0.0D, 0.0D);

		if (p_i1582_1_ != null) {
			dimension = p_i1582_1_.provider.dimensionId;
		}

		dataWatcher = new DataWatcher(this);
		dataWatcher.addObject(0, Byte.valueOf((byte) 0));
		dataWatcher.addObject(1, Short.valueOf((short) 300));
		entityInit();
	}

	public void addEntityCrashInfo(CrashReportCategory p_85029_1_) {
		p_85029_1_.addCrashSectionCallable("Entity Type", new Callable() {

			@Override
			public String call() {
				return EntityList.getEntityString(Entity.this) + " ("
						+ Entity.this.getClass().getCanonicalName() + ")";
			}
		});
		p_85029_1_
				.addCrashSection("Entity ID", Integer.valueOf(field_145783_c));
		p_85029_1_.addCrashSectionCallable("Entity Name", new Callable() {

			@Override
			public String call() {
				return Entity.this.getCommandSenderName();
			}
		});
		p_85029_1_.addCrashSection(
				"Entity\'s Exact location",
				String.format(
						"%.2f, %.2f, %.2f",
						new Object[] { Double.valueOf(posX),
								Double.valueOf(posY), Double.valueOf(posZ) }));
		p_85029_1_.addCrashSection(
				"Entity\'s Block location",
				CrashReportCategory.getLocationInfo(
						MathHelper.floor_double(posX),
						MathHelper.floor_double(posY),
						MathHelper.floor_double(posZ)));
		p_85029_1_.addCrashSection(
				"Entity\'s Momentum",
				String.format(
						"%.2f, %.2f, %.2f",
						new Object[] { Double.valueOf(motionX),
								Double.valueOf(motionY),
								Double.valueOf(motionZ) }));
	}

	/**
	 * Adds a value to the player score. Currently not actually used and the
	 * entity passed in does nothing. Args: entity, scoreToAdd
	 */
	public void addToPlayerScore(Entity p_70084_1_, int p_70084_2_) {
	}

	/**
	 * Adds to the current velocity of the entity. Args: x, y, z
	 */
	public void addVelocity(double p_70024_1_, double p_70024_3_,
			double p_70024_5_) {
		motionX += p_70024_1_;
		motionY += p_70024_3_;
		motionZ += p_70024_5_;
		isAirBorne = true;
	}

	/**
	 * Applies a velocity to each of the entities pushing them away from each
	 * other. Args: entity
	 */
	public void applyEntityCollision(Entity p_70108_1_) {
		if (p_70108_1_.riddenByEntity != this
				&& p_70108_1_.ridingEntity != this) {
			double var2 = p_70108_1_.posX - posX;
			double var4 = p_70108_1_.posZ - posZ;
			double var6 = MathHelper.abs_max(var2, var4);

			if (var6 >= 0.009999999776482582D) {
				var6 = MathHelper.sqrt_double(var6);
				var2 /= var6;
				var4 /= var6;
				double var8 = 1.0D / var6;

				if (var8 > 1.0D) {
					var8 = 1.0D;
				}

				var2 *= var8;
				var4 *= var8;
				var2 *= 0.05000000074505806D;
				var4 *= 0.05000000074505806D;
				var2 *= 1.0F - entityCollisionReduction;
				var4 *= 1.0F - entityCollisionReduction;
				addVelocity(-var2, 0.0D, -var4);
				p_70108_1_.addVelocity(var2, 0.0D, var4);
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			setBeenAttacked();
			return false;
		}
	}

	/**
	 * If returns false, the item will not inflict any damage against entities.
	 */
	public boolean canAttackWithItem() {
		return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	public boolean canBeCollidedWith() {
		return false;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	public boolean canBePushed() {
		return false;
	}

	/**
	 * Return whether this entity should be rendered as on fire.
	 */
	public boolean canRenderOnFire() {
		return isBurning();
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	protected boolean canTriggerWalking() {
		return true;
	}

	/**
	 * Copies important data from another entity to this entity. Used when
	 * teleporting entities between worlds, as this actually deletes the
	 * teleporting entity and re-creates it on the other side. Params: Entity to
	 * copy from, unused (always true)
	 */
	public void copyDataFrom(Entity p_82141_1_, boolean p_82141_2_) {
		final NBTTagCompound var3 = new NBTTagCompound();
		p_82141_1_.writeToNBT(var3);
		readFromNBT(var3);
		timeUntilPortal = p_82141_1_.timeUntilPortal;
		teleportDirection = p_82141_1_.teleportDirection;
	}

	/**
	 * Sets this entity's location and angles to the location and angles of the
	 * passed in entity.
	 */
	public void copyLocationAndAnglesFrom(Entity p_82149_1_) {
		setLocationAndAngles(p_82149_1_.posX, p_82149_1_.posY, p_82149_1_.posZ,
				p_82149_1_.rotationYaw, p_82149_1_.rotationPitch);
	}

	/**
	 * Will deal the specified amount of damage to the entity if the entity
	 * isn't immune to fire damage. Args: amountDamage
	 */
	protected void dealFireDamage(int p_70081_1_) {
		if (!isImmuneToFire) {
			attackEntityFrom(DamageSource.inFire, p_70081_1_);
		}
	}

	public boolean doesEntityNotTriggerPressurePlate() {
		return false;
	}

	/**
	 * Drops an item at the position of the entity.
	 */
	public EntityItem entityDropItem(ItemStack p_70099_1_, float p_70099_2_) {
		if (p_70099_1_.stackSize != 0 && p_70099_1_.getItem() != null) {
			final EntityItem var3 = new EntityItem(worldObj, posX, posY
					+ p_70099_2_, posZ, p_70099_1_);
			var3.delayBeforeCanPickup = 10;
			worldObj.spawnEntityInWorld(var3);
			return var3;
		} else
			return null;
	}

	protected abstract void entityInit();

	@Override
	public boolean equals(Object p_equals_1_) {
		return p_equals_1_ instanceof Entity ? ((Entity) p_equals_1_).field_145783_c == field_145783_c
				: false;
	}

	/**
	 * Removes fire from entity.
	 */
	public void extinguish() {
		fire = 0;
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float p_70069_1_) {
		if (riddenByEntity != null) {
			riddenByEntity.fall(p_70069_1_);
		}
	}

	public IChatComponent func_145748_c_() {
		return new ChatComponentText(getCommandSenderName());
	}

	protected boolean func_145771_j(double p_145771_1_, double p_145771_3_,
			double p_145771_5_) {
		final int var7 = MathHelper.floor_double(p_145771_1_);
		final int var8 = MathHelper.floor_double(p_145771_3_);
		final int var9 = MathHelper.floor_double(p_145771_5_);
		final double var10 = p_145771_1_ - var7;
		final double var12 = p_145771_3_ - var8;
		final double var14 = p_145771_5_ - var9;
		final List var16 = worldObj.func_147461_a(boundingBox);

		if (var16.isEmpty() && !worldObj.func_147469_q(var7, var8, var9))
			return false;
		else {
			final boolean var17 = !worldObj.func_147469_q(var7 - 1, var8, var9);
			final boolean var18 = !worldObj.func_147469_q(var7 + 1, var8, var9);
			worldObj.func_147469_q(var7, var8 - 1, var9);
			final boolean var20 = !worldObj.func_147469_q(var7, var8 + 1, var9);
			final boolean var21 = !worldObj.func_147469_q(var7, var8, var9 - 1);
			final boolean var22 = !worldObj.func_147469_q(var7, var8, var9 + 1);
			byte var23 = 3;
			double var24 = 9999.0D;

			if (var17 && var10 < var24) {
				var24 = var10;
				var23 = 0;
			}

			if (var18 && 1.0D - var10 < var24) {
				var24 = 1.0D - var10;
				var23 = 1;
			}

			if (var20 && 1.0D - var12 < var24) {
				var24 = 1.0D - var12;
				var23 = 3;
			}

			if (var21 && var14 < var24) {
				var24 = var14;
				var23 = 4;
			}

			if (var22 && 1.0D - var14 < var24) {
				var24 = 1.0D - var14;
				var23 = 5;
			}

			final float var26 = rand.nextFloat() * 0.2F + 0.1F;

			if (var23 == 0) {
				motionX = -var26;
			}

			if (var23 == 1) {
				motionX = var26;
			}

			if (var23 == 2) {
				motionY = -var26;
			}

			if (var23 == 3) {
				motionY = var26;
			}

			if (var23 == 4) {
				motionZ = -var26;
			}

			if (var23 == 5) {
				motionZ = var26;
			}

			return true;
		}
	}

	public float func_145772_a(Explosion p_145772_1_, World p_145772_2_,
			int p_145772_3_, int p_145772_4_, int p_145772_5_, Block p_145772_6_) {
		return p_145772_6_.getExplosionResistance(this);
	}

	public boolean func_145774_a(Explosion p_145774_1_, World p_145774_2_,
			int p_145774_3_, int p_145774_4_, int p_145774_5_,
			Block p_145774_6_, float p_145774_7_) {
		return true;
	}

	protected void func_145775_I() {
		final int var1 = MathHelper.floor_double(boundingBox.minX + 0.001D);
		final int var2 = MathHelper.floor_double(boundingBox.minY + 0.001D);
		final int var3 = MathHelper.floor_double(boundingBox.minZ + 0.001D);
		final int var4 = MathHelper.floor_double(boundingBox.maxX - 0.001D);
		final int var5 = MathHelper.floor_double(boundingBox.maxY - 0.001D);
		final int var6 = MathHelper.floor_double(boundingBox.maxZ - 0.001D);

		if (worldObj.checkChunksExist(var1, var2, var3, var4, var5, var6)) {
			for (int var7 = var1; var7 <= var4; ++var7) {
				for (int var8 = var2; var8 <= var5; ++var8) {
					for (int var9 = var3; var9 <= var6; ++var9) {
						final Block var10 = worldObj.getBlock(var7, var8, var9);

						try {
							var10.onEntityCollidedWithBlock(worldObj, var7,
									var8, var9, this);
						} catch (final Throwable var14) {
							final CrashReport var12 = CrashReport
									.makeCrashReport(var14,
											"Colliding entity with block");
							final CrashReportCategory var13 = var12
									.makeCategory("Block being collided with");
							CrashReportCategory
									.func_147153_a(var13, var7, var8, var9,
											var10, worldObj.getBlockMetadata(
													var7, var8, var9));
							throw new ReportedException(var12);
						}
					}
				}
			}
		}
	}

	public EntityItem func_145778_a(Item p_145778_1_, int p_145778_2_,
			float p_145778_3_) {
		return entityDropItem(new ItemStack(p_145778_1_, p_145778_2_, 0),
				p_145778_3_);
	}

	public EntityItem func_145779_a(Item p_145779_1_, int p_145779_2_) {
		return func_145778_a(p_145779_1_, p_145779_2_, 0.0F);
	}

	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		Block.SoundType var5 = p_145780_4_.stepSound;

		if (worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) == Blocks.snow_layer) {
			var5 = Blocks.snow_layer.stepSound;
			playSound(var5.func_150498_e(), var5.func_150497_c() * 0.15F,
					var5.func_150494_d());
		} else if (!p_145780_4_.getMaterial().isLiquid()) {
			playSound(var5.func_150498_e(), var5.func_150497_c() * 0.15F,
					var5.func_150494_d());
		}
	}

	public void func_145781_i(int p_145781_1_) {
	}

	public int getAir() {
		return dataWatcher.getWatchableObjectShort(1);
	}

	/**
	 * returns the bounding box for this entity
	 */
	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	/**
	 * Gets how bright this entity is.
	 */
	public float getBrightness(float p_70013_1_) {
		final int var2 = MathHelper.floor_double(posX);
		final int var3 = MathHelper.floor_double(posZ);

		if (worldObj.blockExists(var2, 0, var3)) {
			final double var4 = (boundingBox.maxY - boundingBox.minY) * 0.66D;
			final int var6 = MathHelper.floor_double(posY - yOffset + var4);
			return worldObj.getLightBrightness(var2, var6, var3);
		} else
			return 0.0F;
	}

	public int getBrightnessForRender(float p_70070_1_) {
		final int var2 = MathHelper.floor_double(posX);
		final int var3 = MathHelper.floor_double(posZ);

		if (worldObj.blockExists(var2, 0, var3)) {
			final double var4 = (boundingBox.maxY - boundingBox.minY) * 0.66D;
			final int var6 = MathHelper.floor_double(posY - yOffset + var4);
			return worldObj.getLightBrightnessForSkyBlocks(var2, var6, var3, 0);
		} else
			return 0;
	}

	public float getCollisionBorderSize() {
		return 0.1F;
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and
	 * blocks. This enables the entity to be pushable on contact, like boats or
	 * minecarts.
	 */
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return null;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	public String getCommandSenderName() {
		String var1 = EntityList.getEntityString(this);

		if (var1 == null) {
			var1 = "generic";
		}

		return StatCollector.translateToLocal("entity." + var1 + ".name");
	}

	public DataWatcher getDataWatcher() {
		return dataWatcher;
	}

	/**
	 * Gets the distance to the position. Args: x, y, z
	 */
	public double getDistance(double p_70011_1_, double p_70011_3_,
			double p_70011_5_) {
		final double var7 = posX - p_70011_1_;
		final double var9 = posY - p_70011_3_;
		final double var11 = posZ - p_70011_5_;
		return MathHelper
				.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
	}

	/**
	 * Gets the squared distance to the position. Args: x, y, z
	 */
	public double getDistanceSq(double p_70092_1_, double p_70092_3_,
			double p_70092_5_) {
		final double var7 = posX - p_70092_1_;
		final double var9 = posY - p_70092_3_;
		final double var11 = posZ - p_70092_5_;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}

	/**
	 * Returns the squared distance to the entity. Args: entity
	 */
	public double getDistanceSqToEntity(Entity p_70068_1_) {
		final double var2 = posX - p_70068_1_.posX;
		final double var4 = posY - p_70068_1_.posY;
		final double var6 = posZ - p_70068_1_.posZ;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}

	/**
	 * Returns the distance to the entity. Args: entity
	 */
	public float getDistanceToEntity(Entity p_70032_1_) {
		final float var2 = (float) (posX - p_70032_1_.posX);
		final float var3 = (float) (posY - p_70032_1_.posY);
		final float var4 = (float) (posZ - p_70032_1_.posZ);
		return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
	}

	public int getEntityId() {
		return field_145783_c;
	}

	/**
	 * Returns the string that identifies this Entity's class
	 */
	protected final String getEntityString() {
		return EntityList.getEntityString(this);
	}

	public float getEyeHeight() {
		return 0.0F;
	}

	/**
	 * Returns true if the flag is active for the entity. Known flags: 0) is
	 * burning; 1) is sneaking; 2) is riding something; 3) is sprinting; 4) is
	 * eating
	 */
	protected boolean getFlag(int p_70083_1_) {
		return (dataWatcher.getWatchableObjectByte(0) & 1 << p_70083_1_) != 0;
	}

	public ItemStack[] getLastActiveItems() {
		return null;
	}

	/**
	 * returns a (normalized) vector of where this entity is looking
	 */
	public Vec3 getLookVec() {
		return null;
	}

	/**
	 * Return the amount of time this entity should stay in a portal before
	 * being transported.
	 */
	public int getMaxInPortalTime() {
		return 0;
	}

	/**
	 * The number of iterations PathFinder.getSafePoint will execute before
	 * giving up.
	 */
	public int getMaxSafePointTries() {
		return 3;
	}

	/**
	 * Returns the Y offset from the entity's position for any entity riding
	 * this one.
	 */
	public double getMountedYOffset() {
		return height * 0.75D;
	}

	/**
	 * Return the Entity parts making up this Entity (currently only for
	 * dragons)
	 */
	public Entity[] getParts() {
		return null;
	}

	/**
	 * Return the amount of cooldown before this entity can use a portal again.
	 */
	public int getPortalCooldown() {
		return 300;
	}

	public float getRotationYawHead() {
		return 0.0F;
	}

	public float getShadowSize() {
		return height / 2.0F;
	}

	protected String getSplashSound() {
		return "game.neutral.swim.splash";
	}

	protected String getSwimSound() {
		return "game.neutral.swim";
	}

	public int getTeleportDirection() {
		return teleportDirection;
	}

	public UUID getUniqueID() {
		return entityUniqueID;
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	public double getYOffset() {
		return yOffset;
	}

	public void handleHealthUpdate(byte p_70103_1_) {
	}

	/**
	 * Whether or not the current entity is in lava
	 */
	public boolean handleLavaMovement() {
		return worldObj.isMaterialInBB(boundingBox.expand(
				-0.10000000149011612D, -0.4000000059604645D,
				-0.10000000149011612D), Material.lava);
	}

	/**
	 * Returns if this entity is in water and will end up adding the waters
	 * velocity to the entity
	 */
	public boolean handleWaterMovement() {
		if (worldObj.handleMaterialAcceleration(
				boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(
						0.001D, 0.001D, 0.001D), Material.water, this)) {
			if (!inWater && !firstUpdate) {
				float var1 = MathHelper.sqrt_double(motionX * motionX
						* 0.20000000298023224D + motionY * motionY + motionZ
						* motionZ * 0.20000000298023224D) * 0.2F;

				if (var1 > 1.0F) {
					var1 = 1.0F;
				}

				playSound(getSplashSound(), var1,
						1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
				final float var2 = MathHelper.floor_double(boundingBox.minY);
				int var3;
				float var4;
				float var5;

				for (var3 = 0; var3 < 1.0F + width * 20.0F; ++var3) {
					var4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					var5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					worldObj.spawnParticle("bubble", posX + var4, var2 + 1.0F,
							posZ + var5, motionX, motionY - rand.nextFloat()
									* 0.2F, motionZ);
				}

				for (var3 = 0; var3 < 1.0F + width * 20.0F; ++var3) {
					var4 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					var5 = (rand.nextFloat() * 2.0F - 1.0F) * width;
					worldObj.spawnParticle("splash", posX + var4, var2 + 1.0F,
							posZ + var5, motionX, motionY, motionZ);
				}
			}

			fallDistance = 0.0F;
			inWater = true;
			fire = 0;
		} else {
			inWater = false;
		}

		return inWater;
	}

	@Override
	public int hashCode() {
		return field_145783_c;
	}

	/**
	 * Called when a player attacks an entity. If this returns true the attack
	 * will not happen.
	 */
	public boolean hitByEntity(Entity p_85031_1_) {
		return false;
	}

	/**
	 * First layer of player interaction
	 */
	public boolean interactFirst(EntityPlayer p_130002_1_) {
		return false;
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire
	 * effect on rendering.
	 */
	public boolean isBurning() {
		final boolean var1 = worldObj != null && worldObj.isClient;
		return !isImmuneToFire && (fire > 0 || var1 && getFlag(0));
	}

	public boolean isEating() {
		return getFlag(4);
	}

	/**
	 * Checks whether target entity is alive.
	 */
	public boolean isEntityAlive() {
		return !isDead;
	}

	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	public boolean isEntityEqual(Entity p_70028_1_) {
		return this == p_70028_1_;
	}

	/**
	 * Checks if this entity is inside of an opaque block
	 */
	public boolean isEntityInsideOpaqueBlock() {
		for (int var1 = 0; var1 < 8; ++var1) {
			final float var2 = ((var1 >> 0) % 2 - 0.5F) * width * 0.8F;
			final float var3 = ((var1 >> 1) % 2 - 0.5F) * 0.1F;
			final float var4 = ((var1 >> 2) % 2 - 0.5F) * width * 0.8F;
			final int var5 = MathHelper.floor_double(posX + var2);
			final int var6 = MathHelper.floor_double(posY + getEyeHeight()
					+ var3);
			final int var7 = MathHelper.floor_double(posZ + var4);

			if (worldObj.getBlock(var5, var6, var7).isNormalCube())
				return true;
		}

		return false;
	}

	/**
	 * Return whether this entity is invulnerable to damage.
	 */
	public boolean isEntityInvulnerable() {
		return invulnerable;
	}

	public final boolean isImmuneToFire() {
		return isImmuneToFire;
	}

	public boolean isInRangeToRender3d(double p_145770_1_, double p_145770_3_,
			double p_145770_5_) {
		final double var7 = posX - p_145770_1_;
		final double var9 = posY - p_145770_3_;
		final double var11 = posZ - p_145770_5_;
		final double var13 = var7 * var7 + var9 * var9 + var11 * var11;
		return isInRangeToRenderDist(var13);
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double var3 = boundingBox.getAverageEdgeLength();
		var3 *= 64.0D * renderDistanceWeight;
		return p_70112_1_ < var3 * var3;
	}

	/**
	 * Checks if the current block the entity is within of the specified
	 * material type
	 */
	public boolean isInsideOfMaterial(Material p_70055_1_) {
		final double var2 = posY + getEyeHeight();
		final int var4 = MathHelper.floor_double(posX);
		final int var5 = MathHelper.floor_float(MathHelper.floor_double(var2));
		final int var6 = MathHelper.floor_double(posZ);
		final Block var7 = worldObj.getBlock(var4, var5, var6);

		if (var7.getMaterial() == p_70055_1_) {
			final float var8 = BlockLiquid.func_149801_b(worldObj
					.getBlockMetadata(var4, var5, var6)) - 0.11111111F;
			final float var9 = var5 + 1 - var8;
			return var2 < var9;
		} else
			return false;
	}

	public boolean isInvisible() {
		return getFlag(5);
	}

	/**
	 * Only used by renderer in EntityLivingBase subclasses.\nDetermines if an
	 * entity is visible or not to a specfic player, if the entity is normally
	 * invisible.\nFor EntityLivingBase subclasses, returning false when
	 * invisible will render the entity semitransparent.
	 */
	public boolean isInvisibleToPlayer(EntityPlayer p_98034_1_) {
		return isInvisible();
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a
	 * result of handleWaterMovement() returning true)
	 */
	public boolean isInWater() {
		return inWater;
	}

	/**
	 * Checks if the offset position from the entity's current position is
	 * inside of liquid. Args: x, y, z
	 */
	public boolean isOffsetPositionInLiquid(double p_70038_1_,
			double p_70038_3_, double p_70038_5_) {
		final AxisAlignedBB var7 = boundingBox.getOffsetBoundingBox(p_70038_1_,
				p_70038_3_, p_70038_5_);
		final List var8 = worldObj.getCollidingBoundingBoxes(this, var7);
		return !var8.isEmpty() ? false : !worldObj.isAnyLiquid(var7);
	}

	public boolean isPushedByWater() {
		return true;
	}

	/**
	 * Returns true if the entity is riding another entity, used by render to
	 * rotate the legs to be in 'sit' position for players.
	 */
	public boolean isRiding() {
		return ridingEntity != null;
	}

	/**
	 * Returns if this entity is sneaking.
	 */
	public boolean isSneaking() {
		return getFlag(1);
	}

	/**
	 * Get if the Entity is sprinting.
	 */
	public boolean isSprinting() {
		return getFlag(3);
	}

	/**
	 * Checks if this entity is either in water or on an open air block in rain
	 * (used in wolves).
	 */
	public boolean isWet() {
		return inWater
				|| worldObj.canLightningStrikeAt(MathHelper.floor_double(posX),
						MathHelper.floor_double(posY),
						MathHelper.floor_double(posZ))
				|| worldObj.canLightningStrikeAt(MathHelper.floor_double(posX),
						MathHelper.floor_double(posY + height),
						MathHelper.floor_double(posZ));
	}

	/**
	 * sets the dead flag. Used when you fall off the bottom of the world.
	 */
	protected void kill() {
		setDead();
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	public void mountEntity(Entity p_70078_1_) {
		entityRiderPitchDelta = 0.0D;
		entityRiderYawDelta = 0.0D;

		if (p_70078_1_ == null) {
			if (ridingEntity != null) {
				setLocationAndAngles(ridingEntity.posX,
						ridingEntity.boundingBox.minY + ridingEntity.height,
						ridingEntity.posZ, rotationYaw, rotationPitch);
				ridingEntity.riddenByEntity = null;
			}

			ridingEntity = null;
		} else {
			if (ridingEntity != null) {
				ridingEntity.riddenByEntity = null;
			}

			if (p_70078_1_ != null) {
				for (Entity var2 = p_70078_1_.ridingEntity; var2 != null; var2 = var2.ridingEntity) {
					if (var2 == this)
						return;
				}
			}

			ridingEntity = p_70078_1_;
			p_70078_1_.riddenByEntity = this;
		}
	}

	/**
	 * Tries to moves the entity by the passed in displacement. Args: x, y, z
	 */
	public void moveEntity(double p_70091_1_, double p_70091_3_,
			double p_70091_5_) {
		if (noClip) {
			boundingBox.offset(p_70091_1_, p_70091_3_, p_70091_5_);
			posX = (boundingBox.minX + boundingBox.maxX) / 2.0D;
			posY = boundingBox.minY + yOffset - ySize;
			posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D;
		} else {
			worldObj.theProfiler.startSection("move");
			ySize *= 0.4F;
			final double var7 = posX;
			final double var9 = posY;
			final double var11 = posZ;

			if (isInWeb) {
				isInWeb = false;
				p_70091_1_ *= 0.25D;
				p_70091_3_ *= 0.05000000074505806D;
				p_70091_5_ *= 0.25D;
				motionX = 0.0D;
				motionY = 0.0D;
				motionZ = 0.0D;
			}

			double var13 = p_70091_1_;
			final double var15 = p_70091_3_;
			double var17 = p_70091_5_;
			final AxisAlignedBB var19 = boundingBox.copy();
			final boolean var20 = onGround && isSneaking()
					&& this instanceof EntityPlayer;

			if (var20) {
				double var21;

				for (var21 = 0.05D; p_70091_1_ != 0.0D
						&& worldObj.getCollidingBoundingBoxes(
								this,
								boundingBox.getOffsetBoundingBox(p_70091_1_,
										-1.0D, 0.0D)).isEmpty(); var13 = p_70091_1_) {
					if (p_70091_1_ < var21 && p_70091_1_ >= -var21) {
						p_70091_1_ = 0.0D;
					} else if (p_70091_1_ > 0.0D) {
						p_70091_1_ -= var21;
					} else {
						p_70091_1_ += var21;
					}
				}

				for (; p_70091_5_ != 0.0D
						&& worldObj.getCollidingBoundingBoxes(
								this,
								boundingBox.getOffsetBoundingBox(0.0D, -1.0D,
										p_70091_5_)).isEmpty(); var17 = p_70091_5_) {
					if (p_70091_5_ < var21 && p_70091_5_ >= -var21) {
						p_70091_5_ = 0.0D;
					} else if (p_70091_5_ > 0.0D) {
						p_70091_5_ -= var21;
					} else {
						p_70091_5_ += var21;
					}
				}

				while (p_70091_1_ != 0.0D
						&& p_70091_5_ != 0.0D
						&& worldObj.getCollidingBoundingBoxes(
								this,
								boundingBox.getOffsetBoundingBox(p_70091_1_,
										-1.0D, p_70091_5_)).isEmpty()) {
					if (p_70091_1_ < var21 && p_70091_1_ >= -var21) {
						p_70091_1_ = 0.0D;
					} else if (p_70091_1_ > 0.0D) {
						p_70091_1_ -= var21;
					} else {
						p_70091_1_ += var21;
					}

					if (p_70091_5_ < var21 && p_70091_5_ >= -var21) {
						p_70091_5_ = 0.0D;
					} else if (p_70091_5_ > 0.0D) {
						p_70091_5_ -= var21;
					} else {
						p_70091_5_ += var21;
					}

					var13 = p_70091_1_;
					var17 = p_70091_5_;
				}
			}

			List var36 = worldObj.getCollidingBoundingBoxes(this,
					boundingBox.addCoord(p_70091_1_, p_70091_3_, p_70091_5_));

			for (int var22 = 0; var22 < var36.size(); ++var22) {
				p_70091_3_ = ((AxisAlignedBB) var36.get(var22))
						.calculateYOffset(boundingBox, p_70091_3_);
			}

			boundingBox.offset(0.0D, p_70091_3_, 0.0D);

			if (!field_70135_K && var15 != p_70091_3_) {
				p_70091_5_ = 0.0D;
				p_70091_3_ = 0.0D;
				p_70091_1_ = 0.0D;
			}

			final boolean var37 = onGround || var15 != p_70091_3_
					&& var15 < 0.0D;
			int var23;

			for (var23 = 0; var23 < var36.size(); ++var23) {
				p_70091_1_ = ((AxisAlignedBB) var36.get(var23))
						.calculateXOffset(boundingBox, p_70091_1_);
			}

			boundingBox.offset(p_70091_1_, 0.0D, 0.0D);

			if (!field_70135_K && var13 != p_70091_1_) {
				p_70091_5_ = 0.0D;
				p_70091_3_ = 0.0D;
				p_70091_1_ = 0.0D;
			}

			for (var23 = 0; var23 < var36.size(); ++var23) {
				p_70091_5_ = ((AxisAlignedBB) var36.get(var23))
						.calculateZOffset(boundingBox, p_70091_5_);
			}

			boundingBox.offset(0.0D, 0.0D, p_70091_5_);

			if (!field_70135_K && var17 != p_70091_5_) {
				p_70091_5_ = 0.0D;
				p_70091_3_ = 0.0D;
				p_70091_1_ = 0.0D;
			}

			double var25;
			double var27;
			int var30;
			double var38;

			if (stepHeight > 0.0F && var37 && (var20 || ySize < 0.05F)
					&& (var13 != p_70091_1_ || var17 != p_70091_5_)) {
				final EventEntityStep event = new EventEntityStep(this);
				Client.getEventManager().call(event);
				var38 = p_70091_1_;
				var25 = p_70091_3_;
				var27 = p_70091_5_;
				p_70091_1_ = var13;
				p_70091_3_ = stepHeight;
				p_70091_5_ = var17;
				final AxisAlignedBB var29 = boundingBox.copy();
				boundingBox.setBB(var19);
				var36 = worldObj.getCollidingBoundingBoxes(this,
						boundingBox.addCoord(var13, p_70091_3_, var17));

				for (var30 = 0; var30 < var36.size(); ++var30) {
					p_70091_3_ = ((AxisAlignedBB) var36.get(var30))
							.calculateYOffset(boundingBox, p_70091_3_);
				}

				boundingBox.offset(0.0D, p_70091_3_, 0.0D);

				if (!field_70135_K && var15 != p_70091_3_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				}

				for (var30 = 0; var30 < var36.size(); ++var30) {
					p_70091_1_ = ((AxisAlignedBB) var36.get(var30))
							.calculateXOffset(boundingBox, p_70091_1_);
				}

				boundingBox.offset(p_70091_1_, 0.0D, 0.0D);

				if (!field_70135_K && var13 != p_70091_1_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				}

				for (var30 = 0; var30 < var36.size(); ++var30) {
					p_70091_5_ = ((AxisAlignedBB) var36.get(var30))
							.calculateZOffset(boundingBox, p_70091_5_);
				}

				boundingBox.offset(0.0D, 0.0D, p_70091_5_);

				if (!field_70135_K && var17 != p_70091_5_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				}

				if (!field_70135_K && var15 != p_70091_3_) {
					p_70091_5_ = 0.0D;
					p_70091_3_ = 0.0D;
					p_70091_1_ = 0.0D;
				} else {
					p_70091_3_ = -stepHeight;

					for (var30 = 0; var30 < var36.size(); ++var30) {
						p_70091_3_ = ((AxisAlignedBB) var36.get(var30))
								.calculateYOffset(boundingBox, p_70091_3_);
					}

					boundingBox.offset(0.0D, p_70091_3_, 0.0D);
				}

				if (var38 * var38 + var27 * var27 >= p_70091_1_ * p_70091_1_
						+ p_70091_5_ * p_70091_5_) {
					p_70091_1_ = var38;
					p_70091_3_ = var25;
					p_70091_5_ = var27;
					boundingBox.setBB(var29);
					EventEntityStepPost postEvent = new EventEntityStepPost(this);
					Client.getEventManager().call(postEvent);
				}
			}

			worldObj.theProfiler.endSection();
			worldObj.theProfiler.startSection("rest");
			posX = (boundingBox.minX + boundingBox.maxX) / 2.0D;
			posY = boundingBox.minY + yOffset - ySize;
			posZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0D;
			isCollidedHorizontally = var13 != p_70091_1_ || var17 != p_70091_5_;
			isCollidedVertically = var15 != p_70091_3_;
			onGround = var15 != p_70091_3_ && var15 < 0.0D;
			isCollided = isCollidedHorizontally || isCollidedVertically;
			updateFallState(p_70091_3_, onGround);

			if (var13 != p_70091_1_) {
				motionX = 0.0D;
			}

			if (var15 != p_70091_3_) {
				motionY = 0.0D;
			}

			if (var17 != p_70091_5_) {
				motionZ = 0.0D;
			}

			var38 = posX - var7;
			var25 = posY - var9;
			var27 = posZ - var11;

			if (canTriggerWalking() && !var20 && ridingEntity == null) {
				final int var39 = MathHelper.floor_double(posX);
				var30 = MathHelper.floor_double(posY - 0.20000000298023224D
						- yOffset);
				final int var31 = MathHelper.floor_double(posZ);
				Block var32 = worldObj.getBlock(var39, var30, var31);
				final int var33 = worldObj.getBlock(var39, var30 - 1, var31)
						.getRenderType();

				if (var33 == 11 || var33 == 32 || var33 == 21) {
					var32 = worldObj.getBlock(var39, var30 - 1, var31);
				}

				if (var32 != Blocks.ladder) {
					var25 = 0.0D;
				}

				distanceWalkedModified = (float) (distanceWalkedModified + MathHelper
						.sqrt_double(var38 * var38 + var27 * var27) * 0.6D);
				distanceWalkedOnStepModified = (float) (distanceWalkedOnStepModified + MathHelper
						.sqrt_double(var38 * var38 + var25 * var25 + var27
								* var27) * 0.6D);

				if (distanceWalkedOnStepModified > nextStepDistance
						&& var32.getMaterial() != Material.air) {
					nextStepDistance = (int) distanceWalkedOnStepModified + 1;

					if (isInWater()) {
						float var34 = MathHelper.sqrt_double(motionX * motionX
								* 0.20000000298023224D + motionY * motionY
								+ motionZ * motionZ * 0.20000000298023224D) * 0.35F;

						if (var34 > 1.0F) {
							var34 = 1.0F;
						}

						playSound(
								getSwimSound(),
								var34,
								1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
					}

					func_145780_a(var39, var30, var31, var32);
					var32.onEntityWalking(worldObj, var39, var30, var31, this);
				}
			}

			try {
				func_145775_I();
			} catch (final Throwable var35) {
				final CrashReport var41 = CrashReport.makeCrashReport(var35,
						"Checking entity block collision");
				final CrashReportCategory var42 = var41
						.makeCategory("Entity being checked for collision");
				addEntityCrashInfo(var42);
				throw new ReportedException(var41);
			}

			final boolean var40 = isWet();

			if (worldObj.func_147470_e(boundingBox.contract(0.001D, 0.001D,
					0.001D))) {
				dealFireDamage(1);

				if (!var40) {
					++fire;

					if (fire == 0) {
						setFire(8);
					}
				}
			} else if (fire <= 0) {
				fire = -fireResistance;
			}

			if (var40 && fire > 0) {
				playSound("random.fizz", 0.7F,
						1.6F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
				fire = -fireResistance;
			}

			worldObj.theProfiler.endSection();
		}
	}

	/**
	 * Used in both water and by flying objects
	 */
	public void moveFlying(float p_70060_1_, float p_70060_2_, float p_70060_3_) {
		float var4 = p_70060_1_ * p_70060_1_ + p_70060_2_ * p_70060_2_;

		if (var4 >= 1.0E-4F) {
			var4 = MathHelper.sqrt_float(var4);

			if (var4 < 1.0F) {
				var4 = 1.0F;
			}

			var4 = p_70060_3_ / var4;
			p_70060_1_ *= var4;
			p_70060_2_ *= var4;
			final float var5 = MathHelper.sin(rotationYaw * (float) Math.PI
					/ 180.0F);
			final float var6 = MathHelper.cos(rotationYaw * (float) Math.PI
					/ 180.0F);
			motionX += p_70060_1_ * var6 - p_70060_2_ * var5;
			motionZ += p_70060_2_ * var6 + p_70060_1_ * var5;
		}
	}

	/**
	 * creates a NBT list from the array of doubles passed to this function
	 */
	protected NBTTagList newDoubleNBTList(double... p_70087_1_) {
		final NBTTagList var2 = new NBTTagList();
		final double[] var3 = p_70087_1_;
		final int var4 = p_70087_1_.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			final double var6 = var3[var5];
			var2.appendTag(new NBTTagDouble(var6));
		}

		return var2;
	}

	/**
	 * Returns a new NBTTagList filled with the specified floats
	 */
	protected NBTTagList newFloatNBTList(float... p_70049_1_) {
		final NBTTagList var2 = new NBTTagList();
		final float[] var3 = p_70049_1_;
		final int var4 = p_70049_1_.length;

		for (int var5 = 0; var5 < var4; ++var5) {
			final float var6 = var3[var5];
			var2.appendTag(new NBTTagFloat(var6));
		}

		return var2;
	}

	public void onChunkLoad() {
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
	}

	/**
	 * Gets called every tick from main Entity class
	 */
	public void onEntityUpdate() {
		worldObj.theProfiler.startSection("entityBaseTick");

		if (ridingEntity != null && ridingEntity.isDead) {
			ridingEntity = null;
		}

		prevDistanceWalkedModified = distanceWalkedModified;
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		prevRotationPitch = rotationPitch;
		prevRotationYaw = rotationYaw;
		int var2;

		if (!worldObj.isClient && worldObj instanceof WorldServer) {
			worldObj.theProfiler.startSection("portal");
			final MinecraftServer var1 = ((WorldServer) worldObj)
					.func_73046_m();
			var2 = getMaxInPortalTime();

			if (inPortal) {
				if (var1.getAllowNether()) {
					if (ridingEntity == null && portalCounter++ >= var2) {
						portalCounter = var2;
						timeUntilPortal = getPortalCooldown();
						byte var3;

						if (worldObj.provider.dimensionId == -1) {
							var3 = 0;
						} else {
							var3 = -1;
						}

						travelToDimension(var3);
					}

					inPortal = false;
				}
			} else {
				if (portalCounter > 0) {
					portalCounter -= 4;
				}

				if (portalCounter < 0) {
					portalCounter = 0;
				}
			}

			if (timeUntilPortal > 0) {
				--timeUntilPortal;
			}

			worldObj.theProfiler.endSection();
		}

		if (isSprinting() && !isInWater()) {
			final int var5 = MathHelper.floor_double(posX);
			var2 = MathHelper.floor_double(posY - 0.20000000298023224D
					- yOffset);
			final int var6 = MathHelper.floor_double(posZ);
			final Block var4 = worldObj.getBlock(var5, var2, var6);

			if (var4.getMaterial() != Material.air) {
				worldObj.spawnParticle(
						"blockcrack_" + Block.getIdFromBlock(var4) + "_"
								+ worldObj.getBlockMetadata(var5, var2, var6),
						posX + (rand.nextFloat() - 0.5D) * width,
						boundingBox.minY + 0.1D, posZ
								+ (rand.nextFloat() - 0.5D) * width,
						-motionX * 4.0D, 1.5D, -motionZ * 4.0D);
			}
		}

		handleWaterMovement();

		if (worldObj.isClient) {
			fire = 0;
		} else if (fire > 0) {
			if (isImmuneToFire) {
				fire -= 4;

				if (fire < 0) {
					fire = 0;
				}
			} else {
				if (fire % 20 == 0) {
					attackEntityFrom(DamageSource.onFire, 1.0F);
				}

				--fire;
			}
		}

		if (handleLavaMovement()) {
			setOnFireFromLava();
			fallDistance *= 0.5F;
		}

		if (posY < -64.0D) {
			kill();
		}

		if (!worldObj.isClient) {
			setFlag(0, fire > 0);
		}

		firstUpdate = false;
		worldObj.theProfiler.endSection();
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	public void onKillEntity(EntityLivingBase p_70074_1_) {
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		dealFireDamage(5);
		++fire;

		if (fire == 0) {
			setFire(8);
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		onEntityUpdate();
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in
	 * multiplayer.
	 */
	public void performHurtAnimation() {
	}

	public void playSound(String p_85030_1_, float p_85030_2_, float p_85030_3_) {
		worldObj.playSoundAtEntity(this, p_85030_1_, p_85030_2_, p_85030_3_);
	}

	/**
	 * Keeps moving the entity up so it isn't colliding with blocks and other
	 * requirements for this entity to be spawned (only actually used on players
	 * though its also on Entity)
	 */
	protected void preparePlayerToSpawn() {
		if (worldObj != null) {
			while (posY > 0.0D) {
				setPosition(posX, posY, posZ);

				if (worldObj.getCollidingBoundingBoxes(this, boundingBox)
						.isEmpty()) {
					break;
				}

				++posY;
			}

			motionX = motionY = motionZ = 0.0D;
			rotationPitch = 0.0F;
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	protected abstract void readEntityFromNBT(NBTTagCompound p_70037_1_);

	/**
	 * Reads the entity from NBT (calls an abstract helper method to read
	 * specialized data)
	 */
	public void readFromNBT(NBTTagCompound p_70020_1_) {
		try {
			final NBTTagList var2 = p_70020_1_.getTagList("Pos", 6);
			final NBTTagList var6 = p_70020_1_.getTagList("Motion", 6);
			final NBTTagList var7 = p_70020_1_.getTagList("Rotation", 5);
			motionX = var6.func_150309_d(0);
			motionY = var6.func_150309_d(1);
			motionZ = var6.func_150309_d(2);

			if (Math.abs(motionX) > 10.0D) {
				motionX = 0.0D;
			}

			if (Math.abs(motionY) > 10.0D) {
				motionY = 0.0D;
			}

			if (Math.abs(motionZ) > 10.0D) {
				motionZ = 0.0D;
			}

			prevPosX = lastTickPosX = posX = var2.func_150309_d(0);
			prevPosY = lastTickPosY = posY = var2.func_150309_d(1);
			prevPosZ = lastTickPosZ = posZ = var2.func_150309_d(2);
			prevRotationYaw = rotationYaw = var7.func_150308_e(0);
			prevRotationPitch = rotationPitch = var7.func_150308_e(1);
			fallDistance = p_70020_1_.getFloat("FallDistance");
			fire = p_70020_1_.getShort("Fire");
			setAir(p_70020_1_.getShort("Air"));
			onGround = p_70020_1_.getBoolean("OnGround");
			dimension = p_70020_1_.getInteger("Dimension");
			invulnerable = p_70020_1_.getBoolean("Invulnerable");
			timeUntilPortal = p_70020_1_.getInteger("PortalCooldown");

			if (p_70020_1_.func_150297_b("UUIDMost", 4)
					&& p_70020_1_.func_150297_b("UUIDLeast", 4)) {
				entityUniqueID = new UUID(p_70020_1_.getLong("UUIDMost"),
						p_70020_1_.getLong("UUIDLeast"));
			}

			setPosition(posX, posY, posZ);
			setRotation(rotationYaw, rotationPitch);
			readEntityFromNBT(p_70020_1_);

			if (shouldSetPosAfterLoading()) {
				setPosition(posX, posY, posZ);
			}
		} catch (final Throwable var5) {
			final CrashReport var3 = CrashReport.makeCrashReport(var5,
					"Loading entity NBT");
			final CrashReportCategory var4 = var3
					.makeCategory("Entity being loaded");
			addEntityCrashInfo(var4);
			throw new ReportedException(var3);
		}
	}

	public void setAir(int p_70050_1_) {
		dataWatcher.updateObject(1, Short.valueOf((short) p_70050_1_));
	}

	/**
	 * Adds par1*0.15 to the entity's yaw, and *subtracts* par2*0.15 from the
	 * pitch. Clamps pitch from -90 to 90. Both arguments in degrees.
	 */
	public void setAngles(float p_70082_1_, float p_70082_2_) {
		final float var3 = rotationPitch;
		final float var4 = rotationYaw;
		rotationYaw = (float) (rotationYaw + p_70082_1_ * 0.15D);
		rotationPitch = (float) (rotationPitch - p_70082_2_ * 0.15D);

		if (rotationPitch < -90.0F) {
			rotationPitch = -90.0F;
		}

		if (rotationPitch > 90.0F) {
			rotationPitch = 90.0F;
		}

		prevRotationPitch += rotationPitch - var3;
		prevRotationYaw += rotationYaw - var4;
	}

	/**
	 * Sets that this entity has been attacked.
	 */
	protected void setBeenAttacked() {
		velocityChanged = true;
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
	}

	/**
	 * Will get destroyed next tick.
	 */
	public void setDead() {
		isDead = true;
	}

	public void setEating(boolean p_70019_1_) {
		setFlag(4, p_70019_1_);
	}

	public void setEntityId(int p_145769_1_) {
		field_145783_c = p_145769_1_;
	}

	/**
	 * Sets entity to burn for x amount of seconds, cannot lower amount of
	 * existing fire.
	 */
	public void setFire(int p_70015_1_) {
		int var2 = p_70015_1_ * 20;
		var2 = EnchantmentProtection.getFireTimeForEntity(this, var2);

		if (fire < var2) {
			fire = var2;
		}
	}

	/**
	 * Enable or disable a entity flag, see getEntityFlag to read the know
	 * flags.
	 */
	protected void setFlag(int p_70052_1_, boolean p_70052_2_) {
		final byte var3 = dataWatcher.getWatchableObjectByte(0);

		if (p_70052_2_) {
			dataWatcher.updateObject(0,
					Byte.valueOf((byte) (var3 | 1 << p_70052_1_)));
		} else {
			dataWatcher.updateObject(0,
					Byte.valueOf((byte) (var3 & ~(1 << p_70052_1_))));
		}
	}

	/**
	 * Called by portal blocks when an entity is within it.
	 */
	public void setInPortal() {
		if (timeUntilPortal > 0) {
			timeUntilPortal = getPortalCooldown();
		} else {
			final double var1 = prevPosX - posX;
			final double var3 = prevPosZ - posZ;

			if (!worldObj.isClient && !inPortal) {
				teleportDirection = Direction.getMovementDirection(var1, var3);
			}

			inPortal = true;
		}
	}

	public void setInvisible(boolean p_82142_1_) {
		setFlag(5, p_82142_1_);
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	public void setInWeb() {
		isInWeb = true;
		fallDistance = 0.0F;
	}

	/**
	 * Sets the location and Yaw/Pitch of an entity in the world
	 */
	public void setLocationAndAngles(double p_70012_1_, double p_70012_3_,
			double p_70012_5_, float p_70012_7_, float p_70012_8_) {
		lastTickPosX = prevPosX = posX = p_70012_1_;
		lastTickPosY = prevPosY = posY = p_70012_3_ + yOffset;
		lastTickPosZ = prevPosZ = posZ = p_70012_5_;
		rotationYaw = p_70012_7_;
		rotationPitch = p_70012_8_;
		setPosition(posX, posY, posZ);
	}

	/**
	 * Called whenever the entity is walking inside of lava.
	 */
	protected void setOnFireFromLava() {
		if (!isImmuneToFire) {
			attackEntityFrom(DamageSource.lava, 4.0F);
			setFire(15);
		}
	}

	/**
	 * Sets the x,y,z of the entity from the given parameters. Also seems to set
	 * up a bounding box.
	 */
	public void setPosition(double p_70107_1_, double p_70107_3_,
			double p_70107_5_) {
		posX = p_70107_1_;
		posY = p_70107_3_;
		posZ = p_70107_5_;
		final float var7 = width / 2.0F;
		final float var8 = height;
		boundingBox.setBounds(p_70107_1_ - var7, p_70107_3_ - yOffset + ySize,
				p_70107_5_ - var7, p_70107_1_ + var7, p_70107_3_ - yOffset
						+ ySize + var8, p_70107_5_ + var7);
	}

	/**
	 * Sets the entity's position and rotation. Args: posX, posY, posZ, yaw,
	 * pitch
	 */
	public void setPositionAndRotation(double p_70080_1_, double p_70080_3_,
			double p_70080_5_, float p_70080_7_, float p_70080_8_) {
		prevPosX = posX = p_70080_1_;
		prevPosY = posY = p_70080_3_;
		prevPosZ = posZ = p_70080_5_;
		prevRotationYaw = rotationYaw = p_70080_7_;
		prevRotationPitch = rotationPitch = p_70080_8_;
		ySize = 0.0F;
		final double var9 = prevRotationYaw - p_70080_7_;

		if (var9 < -180.0D) {
			prevRotationYaw += 360.0F;
		}

		if (var9 >= 180.0D) {
			prevRotationYaw -= 360.0F;
		}

		setPosition(posX, posY, posZ);
		setRotation(p_70080_7_, p_70080_8_);
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_,
			double p_70056_5_, float p_70056_7_, float p_70056_8_,
			int p_70056_9_) {
		setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		setRotation(p_70056_7_, p_70056_8_);
		final List var10 = worldObj.getCollidingBoundingBoxes(this,
				boundingBox.contract(0.03125D, 0.0D, 0.03125D));

		if (!var10.isEmpty()) {
			double var11 = 0.0D;

			for (int var13 = 0; var13 < var10.size(); ++var13) {
				final AxisAlignedBB var14 = (AxisAlignedBB) var10.get(var13);

				if (var14.maxY > var11) {
					var11 = var14.maxY;
				}
			}

			p_70056_3_ += var11 - boundingBox.minY;
			setPosition(p_70056_1_, p_70056_3_, p_70056_5_);
		}
	}

	/**
	 * Sets the rotation of the entity
	 */
	protected void setRotation(float p_70101_1_, float p_70101_2_) {
		rotationYaw = p_70101_1_ % 360.0F;
		rotationPitch = p_70101_2_ % 360.0F;
	}

	/**
	 * Sets the head's yaw rotation of the entity.
	 */
	public void setRotationYawHead(float p_70034_1_) {
	}

	/**
	 * Sets the width and height of the entity. Args: width, height
	 */
	protected void setSize(float p_70105_1_, float p_70105_2_) {
		float var3;

		if (p_70105_1_ != width || p_70105_2_ != height) {
			var3 = width;
			width = p_70105_1_;
			height = p_70105_2_;
			boundingBox.maxX = boundingBox.minX + width;
			boundingBox.maxZ = boundingBox.minZ + width;
			boundingBox.maxY = boundingBox.minY + height;

			if (width > var3 && !firstUpdate && !worldObj.isClient) {
				moveEntity(var3 - width, 0.0D, var3 - width);
			}
		}

		var3 = p_70105_1_ % 2.0F;

		if (var3 < 0.375D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_1;
		} else if (var3 < 0.75D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_2;
		} else if (var3 < 1.0D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_3;
		} else if (var3 < 1.375D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_4;
		} else if (var3 < 1.75D) {
			myEntitySize = Entity.EnumEntitySize.SIZE_5;
		} else {
			myEntitySize = Entity.EnumEntitySize.SIZE_6;
		}
	}

	/**
	 * Sets the sneaking flag.
	 */
	public void setSneaking(boolean p_70095_1_) {
		setFlag(1, p_70095_1_);
	}

	/**
	 * Set sprinting switch for Entity.
	 */
	public void setSprinting(boolean p_70031_1_) {
		setFlag(3, p_70031_1_);
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	public void setVelocity(double p_70016_1_, double p_70016_3_,
			double p_70016_5_) {
		motionX = p_70016_1_;
		motionY = p_70016_3_;
		motionZ = p_70016_5_;
	}

	/**
	 * Sets the reference to the World object.
	 */
	public void setWorld(World p_70029_1_) {
		worldObj = p_70029_1_;
	}

	protected boolean shouldSetPosAfterLoading() {
		return true;
	}

	@Override
	public String toString() {
		return String.format(
				"%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]",
				new Object[] {
						this.getClass().getSimpleName(),
						getCommandSenderName(),
						Integer.valueOf(field_145783_c),
						worldObj == null ? "~NULL~" : worldObj.getWorldInfo()
								.getWorldName(), Double.valueOf(posX),
						Double.valueOf(posY), Double.valueOf(posZ) });
	}

	/**
	 * Teleports the entity to another dimension. Params: Dimension number to
	 * teleport to
	 */
	public void travelToDimension(int p_71027_1_) {
		if (!worldObj.isClient && !isDead) {
			worldObj.theProfiler.startSection("changeDimension");
			final MinecraftServer var2 = MinecraftServer.getServer();
			final int var3 = dimension;
			final WorldServer var4 = var2.worldServerForDimension(var3);
			WorldServer var5 = var2.worldServerForDimension(p_71027_1_);
			dimension = p_71027_1_;

			if (var3 == 1 && p_71027_1_ == 1) {
				var5 = var2.worldServerForDimension(0);
				dimension = 0;
			}

			worldObj.removeEntity(this);
			isDead = false;
			worldObj.theProfiler.startSection("reposition");
			var2.getConfigurationManager().transferEntityToWorld(this, var3,
					var4, var5);
			worldObj.theProfiler.endStartSection("reloading");
			final Entity var6 = EntityList.createEntityByName(
					EntityList.getEntityString(this), var5);

			if (var6 != null) {
				var6.copyDataFrom(this, true);

				if (var3 == 1 && p_71027_1_ == 1) {
					final ChunkCoordinates var7 = var5.getSpawnPoint();
					var7.posY = worldObj.getTopSolidOrLiquidBlock(var7.posX,
							var7.posZ);
					var6.setLocationAndAngles(var7.posX, var7.posY, var7.posZ,
							var6.rotationYaw, var6.rotationPitch);
				}

				var5.spawnEntityInWorld(var6);
			}

			isDead = true;
			worldObj.theProfiler.endSection();
			var4.resetUpdateEntityTick();
			var5.resetUpdateEntityTick();
			worldObj.theProfiler.endSection();
		}
	}

	/**
	 * Takes in the distance the entity has fallen this tick and whether its on
	 * the ground to update the fall distance and deal fall damage if landing on
	 * the ground. Args: distanceFallenThisTick, onGround
	 */
	protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
		if (p_70064_3_) {
			if (fallDistance > 0.0F) {
				fall(fallDistance);
				fallDistance = 0.0F;
			}
		} else if (p_70064_1_ < 0.0D) {
			fallDistance = (float) (fallDistance - p_70064_1_);
		}
	}

	/**
	 * Handles updating while being ridden by an entity
	 */
	public void updateRidden() {
		if (ridingEntity.isDead) {
			ridingEntity = null;
		} else {
			motionX = 0.0D;
			motionY = 0.0D;
			motionZ = 0.0D;
			onUpdate();

			if (ridingEntity != null) {
				ridingEntity.updateRiderPosition();
				entityRiderYawDelta += ridingEntity.rotationYaw
						- ridingEntity.prevRotationYaw;

				for (entityRiderPitchDelta += ridingEntity.rotationPitch
						- ridingEntity.prevRotationPitch; entityRiderYawDelta >= 180.0D; entityRiderYawDelta -= 360.0D) {
					;
				}

				while (entityRiderYawDelta < -180.0D) {
					entityRiderYawDelta += 360.0D;
				}

				while (entityRiderPitchDelta >= 180.0D) {
					entityRiderPitchDelta -= 360.0D;
				}

				while (entityRiderPitchDelta < -180.0D) {
					entityRiderPitchDelta += 360.0D;
				}

				double var1 = entityRiderYawDelta * 0.5D;
				double var3 = entityRiderPitchDelta * 0.5D;
				final float var5 = 10.0F;

				if (var1 > var5) {
					var1 = var5;
				}

				if (var1 < -var5) {
					var1 = -var5;
				}

				if (var3 > var5) {
					var3 = var5;
				}

				if (var3 < -var5) {
					var3 = -var5;
				}

				entityRiderYawDelta -= var1;
				entityRiderPitchDelta -= var3;
			}
		}
	}

	public void updateRiderPosition() {
		if (riddenByEntity != null) {
			riddenByEntity.setPosition(posX, posY + getMountedYOffset()
					+ riddenByEntity.getYOffset(), posZ);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	protected abstract void writeEntityToNBT(NBTTagCompound p_70014_1_);

	/**
	 * Like writeToNBTOptional but does not check if the entity is ridden. Used
	 * for saving ridden entities with their riders.
	 */
	public boolean writeMountToNBT(NBTTagCompound p_98035_1_) {
		final String var2 = getEntityString();

		if (!isDead && var2 != null) {
			p_98035_1_.setString("id", var2);
			writeToNBT(p_98035_1_);
			return true;
		} else
			return false;
	}

	/**
	 * Save the entity to NBT (calls an abstract helper method to write extra
	 * data)
	 */
	public void writeToNBT(NBTTagCompound p_70109_1_) {
		try {
			p_70109_1_.setTag("Pos", newDoubleNBTList(new double[] { posX,
					posY + ySize, posZ }));
			p_70109_1_.setTag("Motion", newDoubleNBTList(new double[] {
					motionX, motionY, motionZ }));
			p_70109_1_.setTag("Rotation", newFloatNBTList(new float[] {
					rotationYaw, rotationPitch }));
			p_70109_1_.setFloat("FallDistance", fallDistance);
			p_70109_1_.setShort("Fire", (short) fire);
			p_70109_1_.setShort("Air", (short) getAir());
			p_70109_1_.setBoolean("OnGround", onGround);
			p_70109_1_.setInteger("Dimension", dimension);
			p_70109_1_.setBoolean("Invulnerable", invulnerable);
			p_70109_1_.setInteger("PortalCooldown", timeUntilPortal);
			p_70109_1_.setLong("UUIDMost", getUniqueID()
					.getMostSignificantBits());
			p_70109_1_.setLong("UUIDLeast", getUniqueID()
					.getLeastSignificantBits());
			writeEntityToNBT(p_70109_1_);

			if (ridingEntity != null) {
				final NBTTagCompound var2 = new NBTTagCompound();

				if (ridingEntity.writeMountToNBT(var2)) {
					p_70109_1_.setTag("Riding", var2);
				}
			}
		} catch (final Throwable var5) {
			final CrashReport var3 = CrashReport.makeCrashReport(var5,
					"Saving entity NBT");
			final CrashReportCategory var4 = var3
					.makeCategory("Entity being saved");
			addEntityCrashInfo(var4);
			throw new ReportedException(var3);
		}
	}

	/**
	 * Either write this entity to the NBT tag given and return true, or return
	 * false without doing anything. If this returns false the entity is not
	 * saved on disk. Ridden entities return false here as they are saved with
	 * their rider.
	 */
	public boolean writeToNBTOptional(NBTTagCompound p_70039_1_) {
		final String var2 = getEntityString();

		if (!isDead && var2 != null && riddenByEntity == null) {
			p_70039_1_.setString("id", var2);
			writeToNBT(p_70039_1_);
			return true;
		} else
			return false;
	}
}

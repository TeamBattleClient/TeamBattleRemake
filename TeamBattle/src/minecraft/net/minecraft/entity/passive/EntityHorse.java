package net.minecraft.entity.passive;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityHorse extends EntityAnimal implements IInvBasic {
	public static class GroupData implements IEntityLivingData {
		public int field_111106_b;
		public int field_111107_a;

		public GroupData(int p_i1684_1_, int p_i1684_2_) {
			field_111107_a = p_i1684_1_;
			field_111106_b = p_i1684_2_;
		}
	}

	private static final int[] armorValues = new int[] { 0, 5, 7, 11 };

	private static final String[] field_110269_bA = new String[] { "hwh",
			"hcr", "hch", "hbr", "hbl", "hgr", "hdb" };
	private static final String[] field_110273_bx = new String[] { "", "meo",
			"goo", "dio" };
	private static final String[] field_110292_bC = new String[] { "", "wo_",
			"wmo", "wdo", "bdo" };
	private static final String[] horseArmorTextures = new String[] { null,
			"textures/entity/horse/armor/horse_armor_iron.png",
			"textures/entity/horse/armor/horse_armor_gold.png",
			"textures/entity/horse/armor/horse_armor_diamond.png" };
	private static final IEntitySelector horseBreedingSelector = new IEntitySelector() {

		@Override
		public boolean isEntityApplicable(Entity p_82704_1_) {
			return p_82704_1_ instanceof EntityHorse
					&& ((EntityHorse) p_82704_1_).func_110205_ce();
		}
	};
	private static final IAttribute horseJumpStrength = new RangedAttribute(
			"horse.jumpStrength", 0.7D, 0.0D, 2.0D).setDescription(
			"Jump Strength").setShouldWatch(true);
	private static final String[] horseMarkingTextures = new String[] { null,
			"textures/entity/horse/horse_markings_white.png",
			"textures/entity/horse/horse_markings_whitefield.png",
			"textures/entity/horse/horse_markings_whitedots.png",
			"textures/entity/horse/horse_markings_blackdots.png" };
	private static final String[] horseTextures = new String[] {
			"textures/entity/horse/horse_white.png",
			"textures/entity/horse/horse_creamy.png",
			"textures/entity/horse/horse_chestnut.png",
			"textures/entity/horse/horse_brown.png",
			"textures/entity/horse/horse_black.png",
			"textures/entity/horse/horse_gray.png",
			"textures/entity/horse/horse_darkbrown.png" };

	public static boolean func_146085_a(Item p_146085_0_) {
		return p_146085_0_ == Items.iron_horse_armor
				|| p_146085_0_ == Items.golden_horse_armor
				|| p_146085_0_ == Items.diamond_horse_armor;
	}

	private int eatingHaystackCounter;
	public int field_110278_bp;
	public int field_110279_bq;
	private final String[] field_110280_bR = new String[3];
	private int field_110285_bP;
	private String field_110286_bQ;

	private boolean field_110294_bI;
	private boolean hasReproduced;
	private float headLean;
	private AnimalChest horseChest;
	protected boolean horseJumping;
	protected float jumpPower;
	private int jumpRearingCounter;
	private float mouthOpenness;
	private int openMouthCounter;
	private float prevHeadLean;
	private float prevMouthOpenness;
	private float prevRearingAmount;

	private float rearingAmount;

	/**
	 * "The higher this value, the more likely the horse is to be tamed next time a player rides it."
	 */
	protected int temper;

	public EntityHorse(World p_i1685_1_) {
		super(p_i1685_1_);
		setSize(1.4F, 1.6F);
		isImmuneToFire = false;
		setChested(false);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.2D));
		tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2D));
		tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.0D));
		tasks.addTask(6, new EntityAIWander(this, 0.7D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		func_110226_cD();
	}

	@Override
	public boolean allowLeashing() {
		return !func_110256_cu() && super.allowLeashing();
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(horseJumpStrength);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				53.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.22499999403953552D);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		final Entity var3 = p_70097_1_.getEntity();
		return riddenByEntity != null && riddenByEntity.equals(var3) ? false
				: super.attackEntityFrom(p_70097_1_, p_70097_2_);
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return riddenByEntity == null;
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	@Override
	public boolean canMateWith(EntityAnimal p_70878_1_) {
		if (p_70878_1_ == this)
			return false;
		else if (p_70878_1_.getClass() != this.getClass())
			return false;
		else {
			final EntityHorse var2 = (EntityHorse) p_70878_1_;

			if (func_110200_cJ() && var2.func_110200_cJ()) {
				final int var3 = getHorseType();
				final int var4 = var2.getHorseType();
				return var3 == var4 || var3 == 0 && var4 == 1 || var3 == 1
						&& var4 == 0;
			} else
				return false;
		}
	}

	@Override
	public EntityAgeable createChild(EntityAgeable p_90011_1_) {
		final EntityHorse var2 = (EntityHorse) p_90011_1_;
		final EntityHorse var3 = new EntityHorse(worldObj);
		final int var4 = getHorseType();
		final int var5 = var2.getHorseType();
		int var6 = 0;

		if (var4 == var5) {
			var6 = var4;
		} else if (var4 == 0 && var5 == 1 || var4 == 1 && var5 == 0) {
			var6 = 2;
		}

		if (var6 == 0) {
			final int var8 = rand.nextInt(9);
			int var7;

			if (var8 < 4) {
				var7 = getHorseVariant() & 255;
			} else if (var8 < 8) {
				var7 = var2.getHorseVariant() & 255;
			} else {
				var7 = rand.nextInt(7);
			}

			final int var9 = rand.nextInt(5);

			if (var9 < 2) {
				var7 |= getHorseVariant() & 65280;
			} else if (var9 < 4) {
				var7 |= var2.getHorseVariant() & 65280;
			} else {
				var7 |= rand.nextInt(5) << 8 & 65280;
			}

			var3.setHorseVariant(var7);
		}

		var3.setHorseType(var6);
		final double var13 = getEntityAttribute(
				SharedMonsterAttributes.maxHealth).getBaseValue()
				+ p_90011_1_.getEntityAttribute(
						SharedMonsterAttributes.maxHealth).getBaseValue()
				+ func_110267_cL();
		var3.getEntityAttribute(SharedMonsterAttributes.maxHealth)
				.setBaseValue(var13 / 3.0D);
		final double var14 = getEntityAttribute(horseJumpStrength)
				.getBaseValue()
				+ p_90011_1_.getEntityAttribute(horseJumpStrength)
						.getBaseValue() + func_110245_cM();
		var3.getEntityAttribute(horseJumpStrength).setBaseValue(var14 / 3.0D);
		final double var11 = getEntityAttribute(
				SharedMonsterAttributes.movementSpeed).getBaseValue()
				+ p_90011_1_.getEntityAttribute(
						SharedMonsterAttributes.movementSpeed).getBaseValue()
				+ func_110203_cN();
		var3.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
				.setBaseValue(var11 / 3.0D);
		return var3;
	}

	public void dropChestItems() {
		dropItemsInChest(this, horseChest);
		dropChests();
	}

	public void dropChests() {
		if (!worldObj.isClient && isChested()) {
			func_145779_a(Item.getItemFromBlock(Blocks.chest), 1);
			setChested(false);
		}
	}

	private void dropItemsInChest(Entity p_110240_1_, AnimalChest p_110240_2_) {
		if (p_110240_2_ != null && !worldObj.isClient) {
			for (int var3 = 0; var3 < p_110240_2_.getSizeInventory(); ++var3) {
				final ItemStack var4 = p_110240_2_.getStackInSlot(var3);

				if (var4 != null) {
					entityDropItem(var4, 0.0F);
				}
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
		dataWatcher.addObject(19, Byte.valueOf((byte) 0));
		dataWatcher.addObject(20, Integer.valueOf(0));
		dataWatcher.addObject(21, String.valueOf(""));
		dataWatcher.addObject(22, Integer.valueOf(0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		if (p_70069_1_ > 1.0F) {
			playSound("mob.horse.land", 0.4F, 1.0F);
		}

		final int var2 = MathHelper.ceiling_float_int(p_70069_1_ * 0.5F - 3.0F);

		if (var2 > 0) {
			attackEntityFrom(DamageSource.fall, var2);

			if (riddenByEntity != null) {
				riddenByEntity.attackEntityFrom(DamageSource.fall, var2);
			}

			final Block var3 = worldObj.getBlock(MathHelper.floor_double(posX),
					MathHelper.floor_double(posY - 0.2D - prevRotationYaw),
					MathHelper.floor_double(posZ));

			if (var3.getMaterial() != Material.air) {
				final Block.SoundType var4 = var3.stepSound;
				worldObj.playSoundAtEntity(this, var4.func_150498_e(),
						var4.func_150497_c() * 0.5F,
						var4.func_150494_d() * 0.75F);
			}
		}
	}

	private boolean func_110200_cJ() {
		return riddenByEntity == null && ridingEntity == null && isTame()
				&& isAdultHorse() && !func_110222_cv()
				&& getHealth() >= getMaxHealth();
	}

	public float func_110201_q(float p_110201_1_) {
		return prevMouthOpenness + (mouthOpenness - prevMouthOpenness)
				* p_110201_1_;
	}

	private double func_110203_cN() {
		return (0.44999998807907104D + rand.nextDouble() * 0.3D
				+ rand.nextDouble() * 0.3D + rand.nextDouble() * 0.3D) * 0.25D;
	}

	public boolean func_110205_ce() {
		return getHorseWatchableBoolean(16);
	}

	private void func_110210_cH() {
		field_110278_bp = 1;
	}

	public boolean func_110222_cv() {
		return func_110256_cu() || getHorseType() == 2;
	}

	private int func_110225_cC() {
		final int var1 = getHorseType();
		return isChested() && (var1 == 1 || var1 == 2) ? 17 : 2;
	}

	private void func_110226_cD() {
		AnimalChest var1 = horseChest;
		horseChest = new AnimalChest("HorseChest", func_110225_cC());
		horseChest.func_110133_a(getCommandSenderName());

		if (var1 != null) {
			var1.func_110132_b(this);
			final int var2 = Math.min(var1.getSizeInventory(),
					horseChest.getSizeInventory());

			for (int var3 = 0; var3 < var2; ++var3) {
				final ItemStack var4 = var1.getStackInSlot(var3);

				if (var4 != null) {
					horseChest.setInventorySlotContents(var3, var4.copy());
				}
			}

			var1 = null;
		}

		horseChest.func_110134_a(this);
		func_110232_cE();
	}

	public boolean func_110229_cs() {
		final int var1 = getHorseType();
		return var1 == 2 || var1 == 1;
	}

	private void func_110230_cF() {
		field_110286_bQ = null;
	}

	private void func_110232_cE() {
		if (!worldObj.isClient) {
			setHorseSaddled(horseChest.getStackInSlot(0) != null);

			if (func_110259_cr()) {
				func_146086_d(horseChest.getStackInSlot(1));
			}
		}
	}

	private void func_110237_h(EntityPlayer p_110237_1_) {
		p_110237_1_.rotationYaw = rotationYaw;
		p_110237_1_.rotationPitch = rotationPitch;
		setEatingHaystack(false);
		setRearing(false);

		if (!worldObj.isClient) {
			p_110237_1_.mountEntity(this);
		}
	}

	public boolean func_110239_cn() {
		return getHorseType() == 0 || func_110241_cb() > 0;
	}

	public int func_110241_cb() {
		return dataWatcher.getWatchableObjectInt(22);
	}

	public void func_110242_l(boolean p_110242_1_) {
		setHorseWatchableBoolean(16, p_110242_1_);
	}

	private double func_110245_cM() {
		return 0.4000000059604645D + rand.nextDouble() * 0.2D
				+ rand.nextDouble() * 0.2D + rand.nextDouble() * 0.2D;
	}

	public boolean func_110253_bW() {
		return isAdultHorse();
	}

	public boolean func_110256_cu() {
		final int var1 = getHorseType();
		return var1 == 3 || var1 == 4;
	}

	public boolean func_110259_cr() {
		return getHorseType() == 0;
	}

	private void func_110266_cB() {
		openHorseMouth();
		worldObj.playSoundAtEntity(this, "eating", 1.0F,
				1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F);
	}

	private float func_110267_cL() {
		return 15.0F + rand.nextInt(8) + rand.nextInt(9);
	}

	@Override
	protected void func_142017_o(float p_142017_1_) {
		if (p_142017_1_ > 6.0F && isEatingHaystack()) {
			setEatingHaystack(false);
		}
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		Block.SoundType var5 = p_145780_4_.stepSound;

		if (worldObj.getBlock(p_145780_1_, p_145780_2_ + 1, p_145780_3_) == Blocks.snow_layer) {
			var5 = Blocks.snow_layer.stepSound;
		}

		if (!p_145780_4_.getMaterial().isLiquid()) {
			final int var6 = getHorseType();

			if (riddenByEntity != null && var6 != 1 && var6 != 2) {
				++field_110285_bP;

				if (field_110285_bP > 5 && field_110285_bP % 3 == 0) {
					playSound("mob.horse.gallop", var5.func_150497_c() * 0.15F,
							var5.func_150494_d());

					if (var6 == 0 && rand.nextInt(10) == 0) {
						playSound("mob.horse.breathe",
								var5.func_150497_c() * 0.6F,
								var5.func_150494_d());
					}
				} else if (field_110285_bP <= 5) {
					playSound("mob.horse.wood", var5.func_150497_c() * 0.15F,
							var5.func_150494_d());
				}
			} else if (var5 == Block.soundTypeWood) {
				playSound("mob.horse.wood", var5.func_150497_c() * 0.15F,
						var5.func_150494_d());
			} else {
				playSound("mob.horse.soft", var5.func_150497_c() * 0.15F,
						var5.func_150494_d());
			}
		}
	}

	@Override
	protected Item func_146068_u() {
		final boolean var1 = rand.nextInt(4) == 0;
		final int var2 = getHorseType();
		return var2 == 4 ? Items.bone : var2 == 3 ? var1 ? Item.getItemById(0)
				: Items.rotten_flesh : Items.leather;
	}

	public void func_146086_d(ItemStack p_146086_1_) {
		dataWatcher.updateObject(22,
				Integer.valueOf(getHorseArmorIndex(p_146086_1_)));
		func_110230_cF();
	}

	public String func_152119_ch() {
		return dataWatcher.getWatchableObjectString(21);
	}

	public void func_152120_b(String p_152120_1_) {
		dataWatcher.updateObject(21, p_152120_1_);
	}

	protected String getAngrySoundName() {
		openHorseMouth();
		makeHorseRear();
		final int var1 = getHorseType();
		return var1 != 3 && var1 != 4 ? var1 != 1 && var1 != 2 ? "mob.horse.angry"
				: "mob.horse.donkey.angry"
				: null;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		prepareChunkForSpawn();
		return super.getCanSpawnHere();
	}

	protected EntityHorse getClosestHorse(Entity p_110250_1_, double p_110250_2_) {
		double var4 = Double.MAX_VALUE;
		Entity var6 = null;
		final List var7 = worldObj.getEntitiesWithinAABBExcludingEntity(
				p_110250_1_, p_110250_1_.boundingBox.addCoord(p_110250_2_,
						p_110250_2_, p_110250_2_), horseBreedingSelector);
		final Iterator var8 = var7.iterator();

		while (var8.hasNext()) {
			final Entity var9 = (Entity) var8.next();
			final double var10 = var9.getDistanceSq(p_110250_1_.posX,
					p_110250_1_.posY, p_110250_1_.posZ);

			if (var10 < var4) {
				var6 = var9;
				var4 = var10;
			}
		}

		return (EntityHorse) var6;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		if (hasCustomNameTag())
			return getCustomNameTag();
		else {
			final int var1 = getHorseType();

			switch (var1) {
			case 0:
			default:
				return StatCollector.translateToLocal("entity.horse.name");

			case 1:
				return StatCollector.translateToLocal("entity.donkey.name");

			case 2:
				return StatCollector.translateToLocal("entity.mule.name");

			case 3:
				return StatCollector
						.translateToLocal("entity.zombiehorse.name");

			case 4:
				return StatCollector
						.translateToLocal("entity.skeletonhorse.name");
			}
		}
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		openHorseMouth();
		final int var1 = getHorseType();
		return var1 == 3 ? "mob.horse.zombie.death"
				: var1 == 4 ? "mob.horse.skeleton.death" : var1 != 1
						&& var1 != 2 ? "mob.horse.death"
						: "mob.horse.donkey.death";
	}

	public float getGrassEatingAmount(float p_110258_1_) {
		return prevHeadLean + (headLean - prevHeadLean) * p_110258_1_;
	}

	public boolean getHasReproduced() {
		return hasReproduced;
	}

	/**
	 * 0 = iron, 1 = gold, 2 = diamond
	 */
	private int getHorseArmorIndex(ItemStack p_110260_1_) {
		if (p_110260_1_ == null)
			return 0;
		else {
			final Item var2 = p_110260_1_.getItem();
			return var2 == Items.iron_horse_armor ? 1
					: var2 == Items.golden_horse_armor ? 2
							: var2 == Items.diamond_horse_armor ? 3 : 0;
		}
	}

	public double getHorseJumpStrength() {
		return getEntityAttribute(horseJumpStrength).getAttributeValue();
	}

	public float getHorseSize() {
		final int var1 = getGrowingAge();
		return var1 >= 0 ? 1.0F : 0.5F + (-24000 - var1) / -24000.0F * 0.5F;
	}

	public String getHorseTexture() {
		if (field_110286_bQ == null) {
			setHorseTexturePaths();
		}

		return field_110286_bQ;
	}

	/**
	 * returns the horse type
	 */
	public int getHorseType() {
		return dataWatcher.getWatchableObjectByte(19);
	}

	public int getHorseVariant() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	private boolean getHorseWatchableBoolean(int p_110233_1_) {
		return (dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		openHorseMouth();

		if (rand.nextInt(3) == 0) {
			makeHorseRear();
		}

		final int var1 = getHorseType();
		return var1 == 3 ? "mob.horse.zombie.hit"
				: var1 == 4 ? "mob.horse.skeleton.hit"
						: var1 != 1 && var1 != 2 ? "mob.horse.hit"
								: "mob.horse.donkey.hit";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		openHorseMouth();

		if (rand.nextInt(10) == 0 && !isMovementBlocked()) {
			makeHorseRear();
		}

		final int var1 = getHorseType();
		return var1 == 3 ? "mob.horse.zombie.idle"
				: var1 == 4 ? "mob.horse.skeleton.idle" : var1 != 1
						&& var1 != 2 ? "mob.horse.idle"
						: "mob.horse.donkey.idle";
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	@Override
	public int getMaxSpawnedInChunk() {
		return 6;
	}

	public int getMaxTemper() {
		return 100;
	}

	public float getRearingAmount(float p_110223_1_) {
		return prevRearingAmount + (rearingAmount - prevRearingAmount)
				* p_110223_1_;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.8F;
	}

	/**
	 * Get number of ticks, at least during which the living entity will be
	 * silent.
	 */
	@Override
	public int getTalkInterval() {
		return 400;
	}

	public int getTemper() {
		return temper;
	}

	/**
	 * Returns the current armor value as determined by a call to
	 * InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue() {
		return armorValues[func_110241_cb()];
	}

	public String[] getVariantTexturePaths() {
		if (field_110286_bQ == null) {
			setHorseTexturePaths();
		}

		return field_110280_bR;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 7) {
			spawnHorseParticles(true);
		} else if (p_70103_1_ == 6) {
			spawnHorseParticles(false);
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public int increaseTemper(int p_110198_1_) {
		final int var2 = MathHelper.clamp_int(getTemper() + p_110198_1_, 0,
				getMaxTemper());
		setTemper(var2);
		return var2;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (var2 != null && var2.getItem() == Items.spawn_egg)
			return super.interact(p_70085_1_);
		else if (!isTame() && func_110256_cu())
			return false;
		else if (isTame() && isAdultHorse() && p_70085_1_.isSneaking()) {
			openGUI(p_70085_1_);
			return true;
		} else if (func_110253_bW() && riddenByEntity != null)
			return super.interact(p_70085_1_);
		else {
			if (var2 != null) {
				boolean var3 = false;

				if (func_110259_cr()) {
					byte var4 = -1;

					if (var2.getItem() == Items.iron_horse_armor) {
						var4 = 1;
					} else if (var2.getItem() == Items.golden_horse_armor) {
						var4 = 2;
					} else if (var2.getItem() == Items.diamond_horse_armor) {
						var4 = 3;
					}

					if (var4 >= 0) {
						if (!isTame()) {
							makeHorseRearWithSound();
							return true;
						}

						openGUI(p_70085_1_);
						return true;
					}
				}

				if (!var3 && !func_110256_cu()) {
					float var7 = 0.0F;
					short var5 = 0;
					byte var6 = 0;

					if (var2.getItem() == Items.wheat) {
						var7 = 2.0F;
						var5 = 60;
						var6 = 3;
					} else if (var2.getItem() == Items.sugar) {
						var7 = 1.0F;
						var5 = 30;
						var6 = 3;
					} else if (var2.getItem() == Items.bread) {
						var7 = 7.0F;
						var5 = 180;
						var6 = 3;
					} else if (Block.getBlockFromItem(var2.getItem()) == Blocks.hay_block) {
						var7 = 20.0F;
						var5 = 180;
					} else if (var2.getItem() == Items.apple) {
						var7 = 3.0F;
						var5 = 60;
						var6 = 3;
					} else if (var2.getItem() == Items.golden_carrot) {
						var7 = 4.0F;
						var5 = 60;
						var6 = 5;

						if (isTame() && getGrowingAge() == 0) {
							var3 = true;
							func_146082_f(p_70085_1_);
						}
					} else if (var2.getItem() == Items.golden_apple) {
						var7 = 10.0F;
						var5 = 240;
						var6 = 10;

						if (isTame() && getGrowingAge() == 0) {
							var3 = true;
							func_146082_f(p_70085_1_);
						}
					}

					if (getHealth() < getMaxHealth() && var7 > 0.0F) {
						heal(var7);
						var3 = true;
					}

					if (!isAdultHorse() && var5 > 0) {
						addGrowth(var5);
						var3 = true;
					}

					if (var6 > 0 && (var3 || !isTame())
							&& var6 < getMaxTemper()) {
						var3 = true;
						increaseTemper(var6);
					}

					if (var3) {
						func_110266_cB();
					}
				}

				if (!isTame() && !var3) {
					if (var2 != null
							&& var2.interactWithEntity(p_70085_1_, this))
						return true;

					makeHorseRearWithSound();
					return true;
				}

				if (!var3
						&& func_110229_cs()
						&& !isChested()
						&& var2.getItem() == Item
								.getItemFromBlock(Blocks.chest)) {
					setChested(true);
					playSound("mob.chickenplop", 1.0F,
							(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
					var3 = true;
					func_110226_cD();
				}

				if (!var3 && func_110253_bW() && !isHorseSaddled()
						&& var2.getItem() == Items.saddle) {
					openGUI(p_70085_1_);
					return true;
				}

				if (var3) {
					if (!p_70085_1_.capabilities.isCreativeMode
							&& --var2.stackSize == 0) {
						p_70085_1_.inventory.setInventorySlotContents(
								p_70085_1_.inventory.currentItem,
								(ItemStack) null);
					}

					return true;
				}
			}

			if (func_110253_bW() && riddenByEntity == null) {
				if (var2 != null && var2.interactWithEntity(p_70085_1_, this))
					return true;
				else {
					func_110237_h(p_70085_1_);
					return true;
				}
			} else
				return super.interact(p_70085_1_);
		}
	}

	public boolean isAdultHorse() {
		return !isChild();
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on the animal type)
	 */
	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return false;
	}

	public boolean isChested() {
		return getHorseWatchableBoolean(8);
	}

	public boolean isEatingHaystack() {
		return getHorseWatchableBoolean(32);
	}

	public boolean isHorseJumping() {
		return horseJumping;
	}

	public boolean isHorseSaddled() {
		return getHorseWatchableBoolean(4);
	}

	/**
	 * Dead and sleeping entities cannot move
	 */
	@Override
	protected boolean isMovementBlocked() {
		return riddenByEntity != null && isHorseSaddled() ? true
				: isEatingHaystack() || isRearing();
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder() {
		return false;
	}

	public boolean isRearing() {
		return getHorseWatchableBoolean(64);
	}

	public boolean isTame() {
		return getHorseWatchableBoolean(2);
	}

	private void makeHorseRear() {
		if (!worldObj.isClient) {
			jumpRearingCounter = 1;
			setRearing(true);
		}
	}

	public void makeHorseRearWithSound() {
		makeHorseRear();
		final String var1 = getAngrySoundName();

		if (var1 != null) {
			playSound(var1, getSoundVolume(), getSoundPitch());
		}
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		if (riddenByEntity != null
				&& riddenByEntity instanceof EntityLivingBase
				&& isHorseSaddled()) {
			prevRotationYaw = rotationYaw = riddenByEntity.rotationYaw;
			rotationPitch = riddenByEntity.rotationPitch * 0.5F;
			setRotation(rotationYaw, rotationPitch);
			rotationYawHead = renderYawOffset = rotationYaw;
			p_70612_1_ = ((EntityLivingBase) riddenByEntity).moveStrafing * 0.5F;
			p_70612_2_ = ((EntityLivingBase) riddenByEntity).moveForward;

			if (p_70612_2_ <= 0.0F) {
				p_70612_2_ *= 0.25F;
				field_110285_bP = 0;
			}

			if (onGround && jumpPower == 0.0F && isRearing()
					&& !field_110294_bI) {
				p_70612_1_ = 0.0F;
				p_70612_2_ = 0.0F;
			}

			if (jumpPower > 0.0F && !isHorseJumping() && onGround) {
				motionY = getHorseJumpStrength() * jumpPower;

				if (this.isPotionActive(Potion.jump)) {
					motionY += (getActivePotionEffect(Potion.jump)
							.getAmplifier() + 1) * 0.1F;
				}

				setHorseJumping(true);
				isAirBorne = true;

				if (p_70612_2_ > 0.0F) {
					final float var3 = MathHelper.sin(rotationYaw
							* (float) Math.PI / 180.0F);
					final float var4 = MathHelper.cos(rotationYaw
							* (float) Math.PI / 180.0F);
					motionX += -0.4F * var3 * jumpPower;
					motionZ += 0.4F * var4 * jumpPower;
					playSound("mob.horse.jump", 0.4F, 1.0F);
				}

				jumpPower = 0.0F;
			}

			stepHeight = 1.0F;
			jumpMovementFactor = getAIMoveSpeed() * 0.1F;

			if (!worldObj.isClient) {
				setAIMoveSpeed((float) getEntityAttribute(
						SharedMonsterAttributes.movementSpeed)
						.getAttributeValue());
				super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			}

			if (onGround) {
				jumpPower = 0.0F;
				setHorseJumping(false);
			}

			prevLimbSwingAmount = limbSwingAmount;
			final double var8 = posX - prevPosX;
			final double var5 = posZ - prevPosZ;
			float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0F;

			if (var7 > 1.0F) {
				var7 = 1.0F;
			}

			limbSwingAmount += (var7 - limbSwingAmount) * 0.4F;
			limbSwing += limbSwingAmount;
		} else {
			stepHeight = 0.5F;
			jumpMovementFactor = 0.02F;
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (!worldObj.isClient) {
			dropChestItems();
		}
	}

	/**
	 * Called by InventoryBasic.onInventoryChanged() on a array that is never
	 * filled.
	 */
	@Override
	public void onInventoryChanged(InventoryBasic p_76316_1_) {
		final int var2 = func_110241_cb();
		final boolean var3 = isHorseSaddled();
		func_110232_cE();

		if (ticksExisted > 20) {
			if (var2 == 0 && var2 != func_110241_cb()) {
				playSound("mob.horse.armor", 0.5F, 1.0F);
			} else if (var2 != func_110241_cb()) {
				playSound("mob.horse.armor", 0.5F, 1.0F);
			}

			if (!var3 && isHorseSaddled()) {
				playSound("mob.horse.leather", 0.5F, 1.0F);
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
		if (rand.nextInt(200) == 0) {
			func_110210_cH();
		}

		super.onLivingUpdate();

		if (!worldObj.isClient) {
			if (rand.nextInt(900) == 0 && deathTime == 0) {
				heal(1.0F);
			}

			if (!isEatingHaystack()
					&& riddenByEntity == null
					&& rand.nextInt(300) == 0
					&& worldObj.getBlock(MathHelper.floor_double(posX),
							MathHelper.floor_double(posY) - 1,
							MathHelper.floor_double(posZ)) == Blocks.grass) {
				setEatingHaystack(true);
			}

			if (isEatingHaystack() && ++eatingHaystackCounter > 50) {
				eatingHaystackCounter = 0;
				setEatingHaystack(false);
			}

			if (func_110205_ce() && !isAdultHorse() && !isEatingHaystack()) {
				final EntityHorse var1 = getClosestHorse(this, 16.0D);

				if (var1 != null && getDistanceSqToEntity(var1) > 4.0D) {
					final PathEntity var2 = worldObj.getPathEntityToEntity(
							this, var1, 16.0F, true, false, false, true);
					setPathToEntity(var2);
				}
			}
		}
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);
		int var3 = 0;
		int var7;

		if (p_110161_1_1 instanceof EntityHorse.GroupData) {
			var7 = ((EntityHorse.GroupData) p_110161_1_1).field_111107_a;
			var3 = ((EntityHorse.GroupData) p_110161_1_1).field_111106_b & 255
					| rand.nextInt(5) << 8;
		} else {
			if (rand.nextInt(10) == 0) {
				var7 = 1;
			} else {
				final int var4 = rand.nextInt(7);
				final int var5 = rand.nextInt(5);
				var7 = 0;
				var3 = var4 | var5 << 8;
			}

			p_110161_1_1 = new EntityHorse.GroupData(var7, var3);
		}

		setHorseType(var7);
		setHorseVariant(var3);

		if (rand.nextInt(5) == 0) {
			setGrowingAge(-24000);
		}

		if (var7 != 4 && var7 != 3) {
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
					func_110267_cL());

			if (var7 == 0) {
				getEntityAttribute(SharedMonsterAttributes.movementSpeed)
						.setBaseValue(func_110203_cN());
			} else {
				getEntityAttribute(SharedMonsterAttributes.movementSpeed)
						.setBaseValue(0.17499999701976776D);
			}
		} else {
			getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
					15.0D);
			getEntityAttribute(SharedMonsterAttributes.movementSpeed)
					.setBaseValue(0.20000000298023224D);
		}

		if (var7 != 2 && var7 != 1) {
			getEntityAttribute(horseJumpStrength)
					.setBaseValue(func_110245_cM());
		} else {
			getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);
		}

		setHealth(getMaxHealth());
		return (IEntityLivingData) p_110161_1_1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (worldObj.isClient && dataWatcher.hasChanges()) {
			dataWatcher.func_111144_e();
			func_110230_cF();
		}

		if (openMouthCounter > 0 && ++openMouthCounter > 30) {
			openMouthCounter = 0;
			setHorseWatchableBoolean(128, false);
		}

		if (!worldObj.isClient && jumpRearingCounter > 0
				&& ++jumpRearingCounter > 20) {
			jumpRearingCounter = 0;
			setRearing(false);
		}

		if (field_110278_bp > 0 && ++field_110278_bp > 8) {
			field_110278_bp = 0;
		}

		if (field_110279_bq > 0) {
			++field_110279_bq;

			if (field_110279_bq > 300) {
				field_110279_bq = 0;
			}
		}

		prevHeadLean = headLean;

		if (isEatingHaystack()) {
			headLean += (1.0F - headLean) * 0.4F + 0.05F;

			if (headLean > 1.0F) {
				headLean = 1.0F;
			}
		} else {
			headLean += (0.0F - headLean) * 0.4F - 0.05F;

			if (headLean < 0.0F) {
				headLean = 0.0F;
			}
		}

		prevRearingAmount = rearingAmount;

		if (isRearing()) {
			prevHeadLean = headLean = 0.0F;
			rearingAmount += (1.0F - rearingAmount) * 0.4F + 0.05F;

			if (rearingAmount > 1.0F) {
				rearingAmount = 1.0F;
			}
		} else {
			field_110294_bI = false;
			rearingAmount += (0.8F * rearingAmount * rearingAmount
					* rearingAmount - rearingAmount) * 0.6F - 0.05F;

			if (rearingAmount < 0.0F) {
				rearingAmount = 0.0F;
			}
		}

		prevMouthOpenness = mouthOpenness;

		if (getHorseWatchableBoolean(128)) {
			mouthOpenness += (1.0F - mouthOpenness) * 0.7F + 0.05F;

			if (mouthOpenness > 1.0F) {
				mouthOpenness = 1.0F;
			}
		} else {
			mouthOpenness += (0.0F - mouthOpenness) * 0.7F - 0.05F;

			if (mouthOpenness < 0.0F) {
				mouthOpenness = 0.0F;
			}
		}
	}

	public void openGUI(EntityPlayer p_110199_1_) {
		if (!worldObj.isClient
				&& (riddenByEntity == null || riddenByEntity == p_110199_1_)
				&& isTame()) {
			horseChest.func_110133_a(getCommandSenderName());
			p_110199_1_.displayGUIHorse(this, horseChest);
		}
	}

	private void openHorseMouth() {
		if (!worldObj.isClient) {
			openMouthCounter = 1;
			setHorseWatchableBoolean(128, true);
		}
	}

	public boolean prepareChunkForSpawn() {
		final int var1 = MathHelper.floor_double(posX);
		final int var2 = MathHelper.floor_double(posZ);
		worldObj.getBiomeGenForCoords(var1, var2);
		return true;
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setEatingHaystack(p_70037_1_.getBoolean("EatingHaystack"));
		func_110242_l(p_70037_1_.getBoolean("Bred"));
		setChested(p_70037_1_.getBoolean("ChestedHorse"));
		setHasReproduced(p_70037_1_.getBoolean("HasReproduced"));
		setHorseType(p_70037_1_.getInteger("Type"));
		setHorseVariant(p_70037_1_.getInteger("Variant"));
		setTemper(p_70037_1_.getInteger("Temper"));
		setHorseTamed(p_70037_1_.getBoolean("Tame"));

		if (p_70037_1_.func_150297_b("OwnerUUID", 8)) {
			func_152120_b(p_70037_1_.getString("OwnerUUID"));
		}

		final IAttributeInstance var2 = getAttributeMap()
				.getAttributeInstanceByName("Speed");

		if (var2 != null) {
			getEntityAttribute(SharedMonsterAttributes.movementSpeed)
					.setBaseValue(var2.getBaseValue() * 0.25D);
		}

		if (isChested()) {
			final NBTTagList var3 = p_70037_1_.getTagList("Items", 10);
			func_110226_cD();

			for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
				final NBTTagCompound var5 = var3.getCompoundTagAt(var4);
				final int var6 = var5.getByte("Slot") & 255;

				if (var6 >= 2 && var6 < horseChest.getSizeInventory()) {
					horseChest.setInventorySlotContents(var6,
							ItemStack.loadItemStackFromNBT(var5));
				}
			}
		}

		ItemStack var7;

		if (p_70037_1_.func_150297_b("ArmorItem", 10)) {
			var7 = ItemStack.loadItemStackFromNBT(p_70037_1_
					.getCompoundTag("ArmorItem"));

			if (var7 != null && func_146085_a(var7.getItem())) {
				horseChest.setInventorySlotContents(1, var7);
			}
		}

		if (p_70037_1_.func_150297_b("SaddleItem", 10)) {
			var7 = ItemStack.loadItemStackFromNBT(p_70037_1_
					.getCompoundTag("SaddleItem"));

			if (var7 != null && var7.getItem() == Items.saddle) {
				horseChest.setInventorySlotContents(0, var7);
			}
		} else if (p_70037_1_.getBoolean("Saddle")) {
			horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
		}

		func_110232_cE();
	}

	public void setChested(boolean p_110207_1_) {
		setHorseWatchableBoolean(8, p_110207_1_);
	}

	@Override
	public void setEating(boolean p_70019_1_) {
		setHorseWatchableBoolean(32, p_70019_1_);
	}

	public void setEatingHaystack(boolean p_110227_1_) {
		setEating(p_110227_1_);
	}

	public void setHasReproduced(boolean p_110221_1_) {
		hasReproduced = p_110221_1_;
	}

	public void setHorseJumping(boolean p_110255_1_) {
		horseJumping = p_110255_1_;
	}

	public void setHorseSaddled(boolean p_110251_1_) {
		setHorseWatchableBoolean(4, p_110251_1_);
	}

	public void setHorseTamed(boolean p_110234_1_) {
		setHorseWatchableBoolean(2, p_110234_1_);
	}

	private void setHorseTexturePaths() {
		field_110286_bQ = "horse/";
		field_110280_bR[0] = null;
		field_110280_bR[1] = null;
		field_110280_bR[2] = null;
		final int var1 = getHorseType();
		final int var2 = getHorseVariant();
		int var3;

		if (var1 == 0) {
			var3 = var2 & 255;
			final int var4 = (var2 & 65280) >> 8;
			field_110280_bR[0] = horseTextures[var3];
			field_110286_bQ = field_110286_bQ + field_110269_bA[var3];
			field_110280_bR[1] = horseMarkingTextures[var4];
			field_110286_bQ = field_110286_bQ + field_110292_bC[var4];
		} else {
			field_110280_bR[0] = "";
			field_110286_bQ = field_110286_bQ + "_" + var1 + "_";
		}

		var3 = func_110241_cb();
		field_110280_bR[2] = horseArmorTextures[var3];
		field_110286_bQ = field_110286_bQ + field_110273_bx[var3];
	}

	public void setHorseType(int p_110214_1_) {
		dataWatcher.updateObject(19, Byte.valueOf((byte) p_110214_1_));
		func_110230_cF();
	}

	public void setHorseVariant(int p_110235_1_) {
		dataWatcher.updateObject(20, Integer.valueOf(p_110235_1_));
		func_110230_cF();
	}

	private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
		final int var3 = dataWatcher.getWatchableObjectInt(16);

		if (p_110208_2_) {
			dataWatcher.updateObject(16, Integer.valueOf(var3 | p_110208_1_));
		} else {
			dataWatcher.updateObject(16, Integer.valueOf(var3 & ~p_110208_1_));
		}
	}

	public void setJumpPower(int p_110206_1_) {
		if (isHorseSaddled()) {
			if (p_110206_1_ < 0) {
				p_110206_1_ = 0;
			} else {
				field_110294_bI = true;
				makeHorseRear();
			}

			if (p_110206_1_ >= 90) {
				jumpPower = 1.0F;
			} else {
				jumpPower = 0.4F + 0.4F * p_110206_1_ / 90.0F;
			}
		}
	}

	public void setRearing(boolean p_110219_1_) {
		if (p_110219_1_) {
			setEatingHaystack(false);
		}

		setHorseWatchableBoolean(64, p_110219_1_);
	}

	/**
	 * "Sets the scale for an ageable entity according to the boolean parameter, which says if it's a child."
	 */
	@Override
	public void setScaleForAge(boolean p_98054_1_) {
		if (p_98054_1_) {
			setScale(getHorseSize());
		} else {
			setScale(1.0F);
		}
	}

	public boolean setTamedBy(EntityPlayer p_110263_1_) {
		func_152120_b(p_110263_1_.getUniqueID().toString());
		setHorseTamed(true);
		return true;
	}

	public void setTemper(int p_110238_1_) {
		temper = p_110238_1_;
	}

	/**
	 * "Spawns particles for the horse entity. par1 tells whether to spawn hearts. If it is false, it spawns smoke."
	 */
	protected void spawnHorseParticles(boolean p_110216_1_) {
		final String var2 = p_110216_1_ ? "heart" : "smoke";

		for (int var3 = 0; var3 < 7; ++var3) {
			final double var4 = rand.nextGaussian() * 0.02D;
			final double var6 = rand.nextGaussian() * 0.02D;
			final double var8 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(var2, posX + rand.nextFloat() * width * 2.0F
					- width, posY + 0.5D + rand.nextFloat() * height, posZ
					+ rand.nextFloat() * width * 2.0F - width, var4, var6, var8);
		}
	}

	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();

		if (prevRearingAmount > 0.0F) {
			final float var1 = MathHelper.sin(renderYawOffset * (float) Math.PI
					/ 180.0F);
			final float var2 = MathHelper.cos(renderYawOffset * (float) Math.PI
					/ 180.0F);
			final float var3 = 0.7F * prevRearingAmount;
			final float var4 = 0.15F * prevRearingAmount;
			riddenByEntity.setPosition(posX + var3 * var1, posY
					+ getMountedYOffset() + riddenByEntity.getYOffset() + var4,
					posZ - var3 * var2);

			if (riddenByEntity instanceof EntityLivingBase) {
				((EntityLivingBase) riddenByEntity).renderYawOffset = renderYawOffset;
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("EatingHaystack", isEatingHaystack());
		p_70014_1_.setBoolean("ChestedHorse", isChested());
		p_70014_1_.setBoolean("HasReproduced", getHasReproduced());
		p_70014_1_.setBoolean("Bred", func_110205_ce());
		p_70014_1_.setInteger("Type", getHorseType());
		p_70014_1_.setInteger("Variant", getHorseVariant());
		p_70014_1_.setInteger("Temper", getTemper());
		p_70014_1_.setBoolean("Tame", isTame());
		p_70014_1_.setString("OwnerUUID", func_152119_ch());

		if (isChested()) {
			final NBTTagList var2 = new NBTTagList();

			for (int var3 = 2; var3 < horseChest.getSizeInventory(); ++var3) {
				final ItemStack var4 = horseChest.getStackInSlot(var3);

				if (var4 != null) {
					final NBTTagCompound var5 = new NBTTagCompound();
					var5.setByte("Slot", (byte) var3);
					var4.writeToNBT(var5);
					var2.appendTag(var5);
				}
			}

			p_70014_1_.setTag("Items", var2);
		}

		if (horseChest.getStackInSlot(1) != null) {
			p_70014_1_.setTag("ArmorItem", horseChest.getStackInSlot(1)
					.writeToNBT(new NBTTagCompound()));
		}

		if (horseChest.getStackInSlot(0) != null) {
			p_70014_1_.setTag("SaddleItem", horseChest.getStackInSlot(0)
					.writeToNBT(new NBTTagCompound()));
		}
	}
}

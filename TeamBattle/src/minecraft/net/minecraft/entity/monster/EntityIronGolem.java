package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIDefendVillage;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookAtVillager;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityIronGolem extends EntityGolem {
	private int attackTimer;
	private int holdRoseTick;
	/** deincrements, and a distance-to-home check is done at 0 */
	private int homeCheckTimer;
	Village villageObj;

	public EntityIronGolem(World p_i1694_1_) {
		super(p_i1694_1_);
		setSize(1.4F, 2.9F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(5, new EntityAILookAtVillager(this));
		tasks.addTask(6, new EntityAIWander(this, 0.6D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIDefendVillage(this));
		targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(3, new EntityAINearestAttackableTarget(this,
				EntityLiving.class, 0, false, true, IMob.mobSelector));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				100.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.25D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		attackTimer = 10;
		worldObj.setEntityState(this, (byte) 4);
		final boolean var2 = p_70652_1_.attackEntityFrom(
				DamageSource.causeMobDamage(this), 7 + rand.nextInt(15));

		if (var2) {
			p_70652_1_.motionY += 0.4000000059604645D;
		}

		playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return var2;
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	@Override
	public boolean canAttackClass(Class p_70686_1_) {
		return isPlayerCreated()
				&& EntityPlayer.class.isAssignableFrom(p_70686_1_) ? false
				: super.canAttackClass(p_70686_1_);
	}

	@Override
	protected void collideWithEntity(Entity p_82167_1_) {
		if (p_82167_1_ instanceof IMob && getRNG().nextInt(20) == 0) {
			setAttackTarget((EntityLivingBase) p_82167_1_);
		}

		super.collideWithEntity(p_82167_1_);
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	@Override
	protected int decreaseAirSupply(int p_70682_1_) {
		return p_70682_1_;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final int var3 = rand.nextInt(3);
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			func_145778_a(Item.getItemFromBlock(Blocks.red_flower), 1, 0.0F);
		}

		var4 = 3 + rand.nextInt(3);

		for (int var5 = 0; var5 < var4; ++var5) {
			func_145779_a(Items.iron_ingot, 1);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.irongolem.walk", 1.0F, 1.0F);
	}

	public int getAttackTimer() {
		return attackTimer;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.irongolem.death";
	}

	public int getHoldRoseTick() {
		return holdRoseTick;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.irongolem.hit";
	}

	public Village getVillage() {
		return villageObj;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 4) {
			attackTimer = 10;
			playSound("mob.irongolem.throw", 1.0F, 1.0F);
		} else if (p_70103_1_ == 11) {
			holdRoseTick = 400;
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	public boolean isPlayerCreated() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		if (!isPlayerCreated() && attackingPlayer != null && villageObj != null) {
			villageObj.setReputationForPlayer(
					attackingPlayer.getCommandSenderName(), -5);
		}

		super.onDeath(p_70645_1_);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (attackTimer > 0) {
			--attackTimer;
		}

		if (holdRoseTick > 0) {
			--holdRoseTick;
		}

		if (motionX * motionX + motionZ * motionZ > 2.500000277905201E-7D
				&& rand.nextInt(5) == 0) {
			final int var1 = MathHelper.floor_double(posX);
			final int var2 = MathHelper.floor_double(posY
					- 0.20000000298023224D - yOffset);
			final int var3 = MathHelper.floor_double(posZ);
			final Block var4 = worldObj.getBlock(var1, var2, var3);

			if (var4.getMaterial() != Material.air) {
				worldObj.spawnParticle(
						"blockcrack_" + Block.getIdFromBlock(var4) + "_"
								+ worldObj.getBlockMetadata(var1, var2, var3),
						posX + (rand.nextFloat() - 0.5D) * width,
						boundingBox.minY + 0.1D, posZ
								+ (rand.nextFloat() - 0.5D) * width,
						4.0D * (rand.nextFloat() - 0.5D), 0.5D,
						(rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setPlayerCreated(p_70037_1_.getBoolean("PlayerCreated"));
	}

	public void setHoldingRose(boolean p_70851_1_) {
		holdRoseTick = p_70851_1_ ? 400 : 0;
		worldObj.setEntityState(this, (byte) 11);
	}

	public void setPlayerCreated(boolean p_70849_1_) {
		final byte var2 = dataWatcher.getWatchableObjectByte(16);

		if (p_70849_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
		}
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick() {
		if (--homeCheckTimer <= 0) {
			homeCheckTimer = 70 + rand.nextInt(50);
			villageObj = worldObj.villageCollectionObj.findNearestVillage(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ), 32);

			if (villageObj == null) {
				detachHome();
			} else {
				final ChunkCoordinates var1 = villageObj.getCenter();
				setHomeArea(var1.posX, var1.posY, var1.posZ,
						(int) (villageObj.getVillageRadius() * 0.6F));
			}
		}

		super.updateAITick();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("PlayerCreated", isPlayerCreated());
	}
}

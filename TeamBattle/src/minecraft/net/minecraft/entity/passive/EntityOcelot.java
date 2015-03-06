package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityOcelot extends EntityTameable {
	/**
	 * The tempt AI task for this mob, used to prevent taming while it is
	 * fleeing.
	 */
	private EntityAITempt aiTempt;

	public EntityOcelot(World p_i1688_1_) {
		super(p_i1688_1_);
		setSize(0.6F, 0.8F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, aiSit);
		tasks.addTask(3, aiTempt = new EntityAITempt(this, 0.6D, Items.fish,
				true));
		tasks.addTask(4, new EntityAIAvoidEntity(this, EntityPlayer.class,
				16.0F, 0.8D, 1.33D));
		tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
		tasks.addTask(6, new EntityAIOcelotSit(this, 1.33D));
		tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
		tasks.addTask(8, new EntityAIOcelotAttack(this));
		tasks.addTask(9, new EntityAIMate(this, 0.8D));
		tasks.addTask(10, new EntityAIWander(this, 0.8D));
		tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class,
				10.0F));
		targetTasks.addTask(1, new EntityAITargetNonTamed(this,
				EntityChicken.class, 750, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.30000001192092896D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this),
				3.0F);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			aiSit.setSitting(false);
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return !isTamed() && ticksExisted > 2400;
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	@Override
	public boolean canMateWith(EntityAnimal p_70878_1_) {
		if (p_70878_1_ == this)
			return false;
		else if (!isTamed())
			return false;
		else if (!(p_70878_1_ instanceof EntityOcelot))
			return false;
		else {
			final EntityOcelot var2 = (EntityOcelot) p_70878_1_;
			return !var2.isTamed() ? false : isInLove() && var2.isInLove();
		}
	}

	@Override
	public EntityOcelot createChild(EntityAgeable p_90011_1_) {
		final EntityOcelot var2 = new EntityOcelot(worldObj);

		if (isTamed()) {
			var2.func_152115_b(func_152113_b());
			var2.setTamed(true);
			var2.setTameSkin(getTameSkin());
		}

		return var2;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
	}

	@Override
	protected Item func_146068_u() {
		return Items.leather;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		if (worldObj.rand.nextInt(3) == 0)
			return false;
		else {
			if (worldObj.checkNoEntityCollision(boundingBox)
					&& worldObj.getCollidingBoundingBoxes(this, boundingBox)
							.isEmpty() && !worldObj.isAnyLiquid(boundingBox)) {
				final int var1 = MathHelper.floor_double(posX);
				final int var2 = MathHelper.floor_double(boundingBox.minY);
				final int var3 = MathHelper.floor_double(posZ);

				if (var2 < 63)
					return false;

				final Block var4 = worldObj.getBlock(var1, var2 - 1, var3);

				if (var4 == Blocks.grass
						|| var4.getMaterial() == Material.leaves)
					return true;
			}

			return false;
		}
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return hasCustomNameTag() ? getCustomNameTag()
				: isTamed() ? StatCollector.translateToLocal("entity.Cat.name")
						: super.getCommandSenderName();
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.cat.hitt";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.cat.hitt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return isTamed() ? isInLove() ? "mob.cat.purr"
				: rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow"
				: "";
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	public int getTameSkin() {
		return dataWatcher.getWatchableObjectByte(18);
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (isTamed()) {
			if (func_152114_e(p_70085_1_) && !worldObj.isClient
					&& !isBreedingItem(var2)) {
				aiSit.setSitting(!isSitting());
			}
		} else if (aiTempt.isRunning() && var2 != null
				&& var2.getItem() == Items.fish
				&& p_70085_1_.getDistanceSqToEntity(this) < 9.0D) {
			if (!p_70085_1_.capabilities.isCreativeMode) {
				--var2.stackSize;
			}

			if (var2.stackSize <= 0) {
				p_70085_1_.inventory.setInventorySlotContents(
						p_70085_1_.inventory.currentItem, (ItemStack) null);
			}

			if (!worldObj.isClient) {
				if (rand.nextInt(3) == 0) {
					setTamed(true);
					setTameSkin(1 + worldObj.rand.nextInt(3));
					func_152115_b(p_70085_1_.getUniqueID().toString());
					playTameEffect(true);
					aiSit.setSitting(true);
					worldObj.setEntityState(this, (byte) 7);
				} else {
					playTameEffect(false);
					worldObj.setEntityState(this, (byte) 6);
				}
			}

			return true;
		}

		return super.interact(p_70085_1_);
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on the animal type)
	 */
	@Override
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_ != null && p_70877_1_.getItem() == Items.fish;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

		if (worldObj.rand.nextInt(7) == 0) {
			for (int var2 = 0; var2 < 2; ++var2) {
				final EntityOcelot var3 = new EntityOcelot(worldObj);
				var3.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
				var3.setGrowingAge(-24000);
				worldObj.spawnEntityInWorld(var3);
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
		setTameSkin(p_70037_1_.getInteger("CatType"));
	}

	public void setTameSkin(int p_70912_1_) {
		dataWatcher.updateObject(18, Byte.valueOf((byte) p_70912_1_));
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	public void updateAITick() {
		if (getMoveHelper().isUpdating()) {
			final double var1 = getMoveHelper().getSpeed();

			if (var1 == 0.6D) {
				setSneaking(true);
				setSprinting(false);
			} else if (var1 == 1.33D) {
				setSneaking(false);
				setSprinting(true);
			} else {
				setSneaking(false);
				setSprinting(false);
			}
		} else {
			setSneaking(false);
			setSprinting(false);
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("CatType", getTameSkin());
	}
}

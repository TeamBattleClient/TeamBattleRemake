package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
	/** AI task for player control. */
	private final EntityAIControlledByPlayer aiControlledByPlayer;

	public EntityPig(World p_i1689_1_) {
		super(p_i1689_1_);
		setSize(0.9F, 0.9F);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.25D));
		tasks.addTask(2, aiControlledByPlayer = new EntityAIControlledByPlayer(
				this, 0.3F));
		tasks.addTask(3, new EntityAIMate(this, 1.0D));
		tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot_on_a_stick,
				false));
		tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot, false));
		tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class,
				6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				10.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.25D);
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For
	 * pigs, this is true if it is being ridden by a player and the player is
	 * holding a carrot-on-a-stick
	 */
	@Override
	public boolean canBeSteered() {
		final ItemStack var1 = ((EntityPlayer) riddenByEntity).getHeldItem();
		return var1 != null && var1.getItem() == Items.carrot_on_a_stick;
	}

	@Override
	public EntityPig createChild(EntityAgeable p_90011_1_) {
		return new EntityPig(worldObj);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final int var3 = rand.nextInt(3) + 1 + rand.nextInt(1 + p_70628_2_);

		for (int var4 = 0; var4 < var3; ++var4) {
			if (isBurning()) {
				func_145779_a(Items.cooked_porkchop, 1);
			} else {
				func_145779_a(Items.porkchop, 1);
			}
		}

		if (getSaddled()) {
			func_145779_a(Items.saddle, 1);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		super.fall(p_70069_1_);

		if (p_70069_1_ > 5.0F && riddenByEntity instanceof EntityPlayer) {
			((EntityPlayer) riddenByEntity)
					.triggerAchievement(AchievementList.flyPig);
		}
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.pig.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return isBurning() ? Items.cooked_porkchop : Items.porkchop;
	}

	/**
	 * Return the AI task for player control.
	 */
	public EntityAIControlledByPlayer getAIControlledByPlayer() {
		return aiControlledByPlayer;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.pig.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.pig.say";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	/**
	 * Returns true if the pig is saddled.
	 */
	public boolean getSaddled() {
		return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		if (super.interact(p_70085_1_))
			return true;
		else if (getSaddled() && !worldObj.isClient
				&& (riddenByEntity == null || riddenByEntity == p_70085_1_)) {
			p_70085_1_.mountEntity(this);
			return true;
		} else
			return false;
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
		return p_70877_1_ != null && p_70877_1_.getItem() == Items.carrot;
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		if (!worldObj.isClient) {
			final EntityPigZombie var2 = new EntityPigZombie(worldObj);
			var2.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
			var2.setLocationAndAngles(posX, posY, posZ, rotationYaw,
					rotationPitch);
			worldObj.spawnEntityInWorld(var2);
			setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		setSaddled(p_70037_1_.getBoolean("Saddle"));
	}

	/**
	 * Set or remove the saddle of the pig.
	 */
	public void setSaddled(boolean p_70900_1_) {
		if (p_70900_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setBoolean("Saddle", getSaddled());
	}
}

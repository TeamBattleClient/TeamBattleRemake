package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAICreeperSwell;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityCreeper extends EntityMob {
	/** Explosion radius for this creeper. */
	private int explosionRadius = 3;

	private int fuseTime = 30;
	/**
	 * Time when this creeper was last in an active state (Messed up code here,
	 * probably causes creeper animation to go weird)
	 */
	private int lastActiveTime;

	/**
	 * The amount of time since the creeper was close enough to the player to
	 * ignite
	 */
	private int timeSinceIgnited;

	public EntityCreeper(World p_i1733_1_) {
		super(p_i1733_1_);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAICreeperSwell(this));
		tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class,
				6.0F, 1.0D, 1.2D));
		tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		tasks.addTask(5, new EntityAIWander(this, 0.8D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class,
				8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget(this,
				EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.25D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		return true;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Byte.valueOf((byte) -1));
		dataWatcher.addObject(17, Byte.valueOf((byte) 0));
		dataWatcher.addObject(18, Byte.valueOf((byte) 0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float p_70069_1_) {
		super.fall(p_70069_1_);
		timeSinceIgnited = (int) (timeSinceIgnited + p_70069_1_ * 1.5F);

		if (timeSinceIgnited > fuseTime - 5) {
			timeSinceIgnited = fuseTime - 5;
		}
	}

	@Override
	protected Item func_146068_u() {
		return Items.gunpowder;
	}

	private void func_146077_cc() {
		if (!worldObj.isClient) {
			final boolean var1 = worldObj.getGameRules()
					.getGameRuleBooleanValue("mobGriefing");

			if (getPowered()) {
				worldObj.createExplosion(this, posX, posY, posZ,
						explosionRadius * 2, var1);
			} else {
				worldObj.createExplosion(this, posX, posY, posZ,
						explosionRadius, var1);
			}

			setDead();
		}
	}

	public boolean func_146078_ca() {
		return dataWatcher.getWatchableObjectByte(18) != 0;
	}

	public void func_146079_cb() {
		dataWatcher.updateObject(18, Byte.valueOf((byte) 1));
	}

	/**
	 * Params: (Float)Render tick. Returns the intensity of the creeper's flash
	 * when it is ignited.
	 */
	public float getCreeperFlashIntensity(float p_70831_1_) {
		return (lastActiveTime + (timeSinceIgnited - lastActiveTime)
				* p_70831_1_)
				/ (fuseTime - 2);
	}

	/**
	 * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
	 */
	public int getCreeperState() {
		return dataWatcher.getWatchableObjectByte(16);
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.creeper.death";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.creeper.say";
	}

	/**
	 * The number of iterations PathFinder.getSafePoint will execute before
	 * giving up.
	 */
	@Override
	public int getMaxSafePointTries() {
		return getAttackTarget() == null ? 3 : 3 + (int) (getHealth() - 1.0F);
	}

	/**
	 * Returns true if the creeper is powered by a lightning bolt.
	 */
	public boolean getPowered() {
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	protected boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (var2 != null && var2.getItem() == Items.flint_and_steel) {
			worldObj.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D,
					"fire.ignite", 1.0F, rand.nextFloat() * 0.4F + 0.8F);
			p_70085_1_.swingItem();

			if (!worldObj.isClient) {
				func_146079_cb();
				var2.damageItem(1, p_70085_1_);
				return true;
			}
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
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_) {
		super.onDeath(p_70645_1_);

		if (p_70645_1_.getEntity() instanceof EntitySkeleton) {
			final int var2 = Item.getIdFromItem(Items.record_13);
			final int var3 = Item.getIdFromItem(Items.record_wait);
			final int var4 = var2 + rand.nextInt(var3 - var2 + 1);
			func_145779_a(Item.getItemById(var4), 1);
		}
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt p_70077_1_) {
		super.onStruckByLightning(p_70077_1_);
		dataWatcher.updateObject(17, Byte.valueOf((byte) 1));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (isEntityAlive()) {
			lastActiveTime = timeSinceIgnited;

			if (func_146078_ca()) {
				setCreeperState(1);
			}

			final int var1 = getCreeperState();

			if (var1 > 0 && timeSinceIgnited == 0) {
				playSound("creeper.primed", 1.0F, 0.5F);
			}

			timeSinceIgnited += var1;

			if (timeSinceIgnited < 0) {
				timeSinceIgnited = 0;
			}

			if (timeSinceIgnited >= fuseTime) {
				timeSinceIgnited = fuseTime;
				func_146077_cc();
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
		dataWatcher
				.updateObject(17, Byte.valueOf((byte) (p_70037_1_
						.getBoolean("powered") ? 1 : 0)));

		if (p_70037_1_.func_150297_b("Fuse", 99)) {
			fuseTime = p_70037_1_.getShort("Fuse");
		}

		if (p_70037_1_.func_150297_b("ExplosionRadius", 99)) {
			explosionRadius = p_70037_1_.getByte("ExplosionRadius");
		}

		if (p_70037_1_.getBoolean("ignited")) {
			func_146079_cb();
		}
	}

	/**
	 * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
	 */
	public void setCreeperState(int p_70829_1_) {
		dataWatcher.updateObject(16, Byte.valueOf((byte) p_70829_1_));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);

		if (dataWatcher.getWatchableObjectByte(17) == 1) {
			p_70014_1_.setBoolean("powered", true);
		}

		p_70014_1_.setShort("Fuse", (short) fuseTime);
		p_70014_1_.setByte("ExplosionRadius", (byte) explosionRadius);
		p_70014_1_.setBoolean("ignited", func_146078_ca());
	}
}

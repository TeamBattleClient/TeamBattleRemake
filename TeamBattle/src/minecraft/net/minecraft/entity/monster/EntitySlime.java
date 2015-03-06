package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntitySlime extends EntityLiving implements IMob {
	public float prevSquishFactor;
	/** ticks until this slime jumps again */
	private int slimeJumpDelay;
	public float squishAmount;

	public float squishFactor;

	public EntitySlime(World p_i1742_1_) {
		super(p_i1742_1_);
		final int var2 = 1 << rand.nextInt(3);
		yOffset = 0.0F;
		slimeJumpDelay = rand.nextInt(20) + 10;
		setSlimeSize(var2);
	}

	protected void alterSquishAmount() {
		squishAmount *= 0.6F;
	}

	/**
	 * Indicates weather the slime is able to damage the player (based upon the
	 * slime's size)
	 */
	protected boolean canDamagePlayer() {
		return getSlimeSize() > 1;
	}

	protected EntitySlime createInstance() {
		return new EntitySlime(worldObj);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, new Byte((byte) 1));
	}

	@Override
	protected Item func_146068_u() {
		return getSlimeSize() == 1 ? Items.slime_ball : Item.getItemById(0);
	}

	/**
	 * Gets the amount of damage dealt to the player when "attacked" by the
	 * slime.
	 */
	protected int getAttackStrength() {
		return getSlimeSize();
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		final Chunk var1 = worldObj.getChunkFromBlockCoords(
				MathHelper.floor_double(posX), MathHelper.floor_double(posZ));

		if (worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT
				&& rand.nextInt(4) != 1)
			return false;
		else {
			if (getSlimeSize() == 1
					|| worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
				final BiomeGenBase var2 = worldObj.getBiomeGenForCoords(
						MathHelper.floor_double(posX),
						MathHelper.floor_double(posZ));

				if (var2 == BiomeGenBase.swampland
						&& posY > 50.0D
						&& posY < 70.0D
						&& rand.nextFloat() < 0.5F
						&& rand.nextFloat() < worldObj
								.getCurrentMoonPhaseFactor()
						&& worldObj.getBlockLightValue(
								MathHelper.floor_double(posX),
								MathHelper.floor_double(posY),
								MathHelper.floor_double(posZ)) <= rand
								.nextInt(8))
					return super.getCanSpawnHere();

				if (rand.nextInt(10) == 0
						&& var1.getRandomWithSeed(987234911L).nextInt(10) == 0
						&& posY < 40.0D)
					return super.getCanSpawnHere();
			}

			return false;
		}
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
	}

	/**
	 * Gets the amount of time the slime needs to wait between jumps.
	 */
	protected int getJumpDelay() {
		return rand.nextInt(20) + 10;
	}

	/**
	 * Returns the name of the sound played when the slime jumps.
	 */
	protected String getJumpSound() {
		return "mob.slime." + (getSlimeSize() > 1 ? "big" : "small");
	}

	/**
	 * Returns the name of a particle effect that may be randomly created by
	 * EntitySlime.onUpdate()
	 */
	protected String getSlimeParticle() {
		return "slime";
	}

	/**
	 * Returns the size of the slime.
	 */
	public int getSlimeSize() {
		return dataWatcher.getWatchableObjectByte(16);
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F * getSlimeSize();
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the
	 * faceEntity method. This is only currently use in wolves.
	 */
	@Override
	public int getVerticalFaceSpeed() {
		return 0;
	}

	/**
	 * Returns true if the slime makes a sound when it jumps (based upon the
	 * slime's size)
	 */
	protected boolean makesSoundOnJump() {
		return getSlimeSize() > 0;
	}

	/**
	 * Returns true if the slime makes a sound when it lands after a jump (based
	 * upon the slime's size)
	 */
	protected boolean makesSoundOnLand() {
		return getSlimeSize() > 2;
	}

	/**
	 * Called by a player entity when they collide with an entity
	 */
	@Override
	public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
		if (canDamagePlayer()) {
			final int var2 = getSlimeSize();

			if (canEntityBeSeen(p_70100_1_)
					&& getDistanceSqToEntity(p_70100_1_) < 0.6D * var2 * 0.6D
							* var2
					&& p_70100_1_.attackEntityFrom(
							DamageSource.causeMobDamage(this),
							getAttackStrength())) {
				playSound("mob.attack", 1.0F,
						(rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (!worldObj.isClient
				&& worldObj.difficultySetting == EnumDifficulty.PEACEFUL
				&& getSlimeSize() > 0) {
			isDead = true;
		}

		squishFactor += (squishAmount - squishFactor) * 0.5F;
		prevSquishFactor = squishFactor;
		final boolean var1 = onGround;
		super.onUpdate();
		int var2;

		if (onGround && !var1) {
			var2 = getSlimeSize();

			for (int var3 = 0; var3 < var2 * 8; ++var3) {
				final float var4 = rand.nextFloat() * (float) Math.PI * 2.0F;
				final float var5 = rand.nextFloat() * 0.5F + 0.5F;
				final float var6 = MathHelper.sin(var4) * var2 * 0.5F * var5;
				final float var7 = MathHelper.cos(var4) * var2 * 0.5F * var5;
				worldObj.spawnParticle(getSlimeParticle(), posX + var6,
						boundingBox.minY, posZ + var7, 0.0D, 0.0D, 0.0D);
			}

			if (makesSoundOnLand()) {
				playSound(
						getJumpSound(),
						getSoundVolume(),
						((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
			}

			squishAmount = -0.5F;
		} else if (!onGround && var1) {
			squishAmount = 1.0F;
		}

		alterSquishAmount();

		if (worldObj.isClient) {
			var2 = getSlimeSize();
			setSize(0.6F * var2, 0.6F * var2);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		int var2 = p_70037_1_.getInteger("Size");

		if (var2 < 0) {
			var2 = 0;
		}

		setSlimeSize(var2 + 1);
	}

	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void setDead() {
		final int var1 = getSlimeSize();

		if (!worldObj.isClient && var1 > 1 && getHealth() <= 0.0F) {
			final int var2 = 2 + rand.nextInt(3);

			for (int var3 = 0; var3 < var2; ++var3) {
				final float var4 = (var3 % 2 - 0.5F) * var1 / 4.0F;
				final float var5 = (var3 / 2 - 0.5F) * var1 / 4.0F;
				final EntitySlime var6 = createInstance();
				var6.setSlimeSize(var1 / 2);
				var6.setLocationAndAngles(posX + var4, posY + 0.5D,
						posZ + var5, rand.nextFloat() * 360.0F, 0.0F);
				worldObj.spawnEntityInWorld(var6);
			}
		}

		super.setDead();
	}

	protected void setSlimeSize(int p_70799_1_) {
		dataWatcher.updateObject(16, new Byte((byte) p_70799_1_));
		setSize(0.6F * p_70799_1_, 0.6F * p_70799_1_);
		setPosition(posX, posY, posZ);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				p_70799_1_ * p_70799_1_);
		setHealth(getMaxHealth());
		experienceValue = p_70799_1_;
	}

	@Override
	protected void updateEntityActionState() {
		despawnEntity();
		final EntityPlayer var1 = worldObj.getClosestVulnerablePlayerToEntity(
				this, 16.0D);

		if (var1 != null) {
			faceEntity(var1, 10.0F, 20.0F);
		}

		if (onGround && slimeJumpDelay-- <= 0) {
			slimeJumpDelay = getJumpDelay();

			if (var1 != null) {
				slimeJumpDelay /= 3;
			}

			isJumping = true;

			if (makesSoundOnJump()) {
				playSound(
						getJumpSound(),
						getSoundVolume(),
						((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
			}

			moveStrafing = 1.0F - rand.nextFloat() * 2.0F;
			moveForward = 1 * getSlimeSize();
		} else {
			isJumping = false;

			if (onGround) {
				moveStrafing = moveForward = 0.0F;
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("Size", getSlimeSize() - 1);
	}
}

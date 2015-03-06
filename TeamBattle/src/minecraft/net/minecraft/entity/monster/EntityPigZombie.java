package net.minecraft.entity.monster;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityPigZombie extends EntityZombie {
	private static final UUID field_110189_bq = UUID
			.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
	private static final AttributeModifier field_110190_br = new AttributeModifier(
			field_110189_bq, "Attacking speed boost", 0.45D, 0).setSaved(false);

	/** Above zero if this PigZombie is Angry. */
	private int angerLevel;

	private Entity field_110191_bu;
	/** A random delay until this PigZombie next makes a sound. */
	private int randomSoundDelay;

	public EntityPigZombie(World p_i1739_1_) {
		super(p_i1739_1_);
		isImmuneToFire = true;
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor() {
		setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(field_110186_bp).setBaseValue(0.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.5D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(
				5.0D);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			final Entity var3 = p_70097_1_.getEntity();

			if (var3 instanceof EntityPlayer) {
				final List var4 = worldObj
						.getEntitiesWithinAABBExcludingEntity(this,
								boundingBox.expand(32.0D, 32.0D, 32.0D));

				for (int var5 = 0; var5 < var4.size(); ++var5) {
					final Entity var6 = (Entity) var4.get(var5);

					if (var6 instanceof EntityPigZombie) {
						final EntityPigZombie var7 = (EntityPigZombie) var6;
						var7.becomeAngryAt(var3);
					}
				}

				becomeAngryAt(var3);
			}

			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	/**
	 * Causes this PigZombie to become angry at the supplied Entity (which will
	 * be a player).
	 */
	private void becomeAngryAt(Entity p_70835_1_) {
		entityToAttack = p_70835_1_;
		angerLevel = 400 + rand.nextInt(400);
		randomSoundDelay = rand.nextInt(40);
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int var3 = rand.nextInt(2 + p_70628_2_);
		int var4;

		for (var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.rotten_flesh, 1);
		}

		var3 = rand.nextInt(2 + p_70628_2_);

		for (var4 = 0; var4 < var3; ++var4) {
			func_145779_a(Items.gold_nugget, 1);
		}
	}

	@Override
	protected void dropRareDrop(int p_70600_1_) {
		func_145779_a(Items.gold_ingot, 1);
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack() {
		return angerLevel == 0 ? null : super.findPlayerToAttack();
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return worldObj.difficultySetting != EnumDifficulty.PEACEFUL
				&& worldObj.checkNoEntityCollision(boundingBox)
				&& worldObj.getCollidingBoundingBoxes(this, boundingBox)
						.isEmpty() && !worldObj.isAnyLiquid(boundingBox);
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.zombiepig.zpigdeath";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.zombiepig.zpighurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.zombiepig.zpig";
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		return false;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	protected boolean isAIEnabled() {
		return false;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
		super.onSpawnWithEgg(p_110161_1_);
		setVillager(false);
		return p_110161_1_;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (field_110191_bu != entityToAttack && !worldObj.isClient) {
			final IAttributeInstance var1 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			var1.removeModifier(field_110190_br);

			if (entityToAttack != null) {
				var1.applyModifier(field_110190_br);
			}
		}

		field_110191_bu = entityToAttack;

		if (randomSoundDelay > 0 && --randomSoundDelay == 0) {
			playSound(
					"mob.zombiepig.zpigangry",
					getSoundVolume() * 2.0F,
					((rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
		}

		super.onUpdate();
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		angerLevel = p_70037_1_.getShort("Anger");
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setShort("Anger", (short) angerLevel);
	}
}

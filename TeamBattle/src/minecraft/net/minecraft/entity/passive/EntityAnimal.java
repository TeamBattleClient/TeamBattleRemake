package net.minecraft.entity.passive;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
	/**
	 * This is representation of a counter for reproduction progress. (Note that
	 * this is different from the inLove which represent being in Love-Mode)
	 */
	private int breeding;

	private EntityPlayer field_146084_br;
	private int inLove;

	public EntityAnimal(World p_i1681_1_) {
		super(p_i1681_1_);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (p_70785_1_ instanceof EntityPlayer) {
			if (p_70785_2_ < 3.0F) {
				final double var3 = p_70785_1_.posX - posX;
				final double var5 = p_70785_1_.posZ - posZ;
				rotationYaw = (float) (Math.atan2(var5, var3) * 180.0D / Math.PI) - 90.0F;
				hasAttacked = true;
			}

			final EntityPlayer var7 = (EntityPlayer) p_70785_1_;

			if (var7.getCurrentEquippedItem() == null
					|| !isBreedingItem(var7.getCurrentEquippedItem())) {
				entityToAttack = null;
			}
		} else if (p_70785_1_ instanceof EntityAnimal) {
			final EntityAnimal var8 = (EntityAnimal) p_70785_1_;

			if (getGrowingAge() > 0 && var8.getGrowingAge() < 0) {
				if (p_70785_2_ < 2.5D) {
					hasAttacked = true;
				}
			} else if (inLove > 0 && var8.inLove > 0) {
				if (var8.entityToAttack == null) {
					var8.entityToAttack = this;
				}

				if (var8.entityToAttack == this && p_70785_2_ < 3.5D) {
					++var8.inLove;
					++inLove;
					++breeding;

					if (breeding % 4 == 0) {
						worldObj.spawnParticle("heart", posX + rand.nextFloat()
								* width * 2.0F - width,
								posY + 0.5D + rand.nextFloat() * height, posZ
										+ rand.nextFloat() * width * 2.0F
										- width, 0.0D, 0.0D, 0.0D);
					}

					if (breeding == 60) {
						procreate((EntityAnimal) p_70785_1_);
					}
				} else {
					breeding = 0;
				}
			} else {
				breeding = 0;
				entityToAttack = null;
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			fleeingTick = 60;

			if (!isAIEnabled()) {
				final IAttributeInstance var3 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

				if (var3.getModifier(field_110179_h) == null) {
					var3.applyModifier(field_110181_i);
				}
			}

			entityToAttack = null;
			inLove = 0;
			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * Returns true if the mob is currently able to mate with the specified mob.
	 */
	public boolean canMateWith(EntityAnimal p_70878_1_) {
		return p_70878_1_ == this ? false : p_70878_1_.getClass() != this
				.getClass() ? false : isInLove() && p_70878_1_.isInLove();
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack() {
		if (fleeingTick > 0)
			return null;
		else {
			final float var1 = 8.0F;
			List var2;
			int var3;
			EntityAnimal var4;

			if (inLove > 0) {
				var2 = worldObj.getEntitiesWithinAABB(this.getClass(),
						boundingBox.expand(var1, var1, var1));

				for (var3 = 0; var3 < var2.size(); ++var3) {
					var4 = (EntityAnimal) var2.get(var3);

					if (var4 != this && var4.inLove > 0)
						return var4;
				}
			} else if (getGrowingAge() == 0) {
				var2 = worldObj.getEntitiesWithinAABB(EntityPlayer.class,
						boundingBox.expand(var1, var1, var1));

				for (var3 = 0; var3 < var2.size(); ++var3) {
					final EntityPlayer var5 = (EntityPlayer) var2.get(var3);

					if (var5.getCurrentEquippedItem() != null
							&& isBreedingItem(var5.getCurrentEquippedItem()))
						return var5;
				}
			} else if (getGrowingAge() > 0) {
				var2 = worldObj.getEntitiesWithinAABB(this.getClass(),
						boundingBox.expand(var1, var1, var1));

				for (var3 = 0; var3 < var2.size(); ++var3) {
					var4 = (EntityAnimal) var2.get(var3);

					if (var4 != this && var4.getGrowingAge() < 0)
						return var4;
				}
			}

			return null;
		}
	}

	public void func_146082_f(EntityPlayer p_146082_1_) {
		inLove = 600;
		field_146084_br = p_146082_1_;
		entityToAttack = null;
		worldObj.setEntityState(this, (byte) 18);
	}

	public EntityPlayer func_146083_cb() {
		return field_146084_br;
	}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this
	 * creature will try to path to the block. Args: x, y, z
	 */
	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_,
			int p_70783_3_) {
		return worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) == Blocks.grass ? 10.0F
				: worldObj.getLightBrightness(p_70783_1_, p_70783_2_,
						p_70783_3_) - 0.5F;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		final int var1 = MathHelper.floor_double(posX);
		final int var2 = MathHelper.floor_double(boundingBox.minY);
		final int var3 = MathHelper.floor_double(posZ);
		return worldObj.getBlock(var1, var2 - 1, var3) == Blocks.grass
				&& worldObj.getFullBlockLightValue(var1, var2, var3) > 8
				&& super.getCanSpawnHere();
	}

	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(EntityPlayer p_70693_1_) {
		return 1 + worldObj.rand.nextInt(3);
	}

	/**
	 * Get number of ticks, at least during which the living entity will be
	 * silent.
	 */
	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	public void handleHealthUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 18) {
			for (int var2 = 0; var2 < 7; ++var2) {
				final double var3 = rand.nextGaussian() * 0.02D;
				final double var5 = rand.nextGaussian() * 0.02D;
				final double var7 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", posX + rand.nextFloat() * width
						* 2.0F - width,
						posY + 0.5D + rand.nextFloat() * height,
						posZ + rand.nextFloat() * width * 2.0F - width, var3,
						var5, var7);
			}
		} else {
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer p_70085_1_) {
		final ItemStack var2 = p_70085_1_.inventory.getCurrentItem();

		if (var2 != null && isBreedingItem(var2) && getGrowingAge() == 0
				&& inLove <= 0) {
			if (!p_70085_1_.capabilities.isCreativeMode) {
				--var2.stackSize;

				if (var2.stackSize <= 0) {
					p_70085_1_.inventory.setInventorySlotContents(
							p_70085_1_.inventory.currentItem, (ItemStack) null);
				}
			}

			func_146082_f(p_70085_1_);
			return true;
		} else
			return super.interact(p_70085_1_);
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack p_70877_1_) {
		return p_70877_1_.getItem() == Items.wheat;
	}

	/**
	 * Returns if the entity is currently in 'love mode'.
	 */
	public boolean isInLove() {
		return inLove > 0;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (getGrowingAge() != 0) {
			inLove = 0;
		}

		if (inLove > 0) {
			--inLove;
			final String var1 = "heart";

			if (inLove % 10 == 0) {
				final double var2 = rand.nextGaussian() * 0.02D;
				final double var4 = rand.nextGaussian() * 0.02D;
				final double var6 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle(var1, posX + rand.nextFloat() * width
						* 2.0F - width,
						posY + 0.5D + rand.nextFloat() * height,
						posZ + rand.nextFloat() * width * 2.0F - width, var2,
						var4, var6);
			}
		} else {
			breeding = 0;
		}
	}

	/**
	 * Creates a baby animal according to the animal type of the target at the
	 * actual position and spawns 'love' particles.
	 */
	private void procreate(EntityAnimal p_70876_1_) {
		final EntityAgeable var2 = createChild(p_70876_1_);

		if (var2 != null) {
			if (field_146084_br == null && p_70876_1_.func_146083_cb() != null) {
				field_146084_br = p_70876_1_.func_146083_cb();
			}

			if (field_146084_br != null) {
				field_146084_br.triggerAchievement(StatList.field_151186_x);

				if (this instanceof EntityCow) {
					field_146084_br
							.triggerAchievement(AchievementList.field_150962_H);
				}
			}

			setGrowingAge(6000);
			p_70876_1_.setGrowingAge(6000);
			inLove = 0;
			breeding = 0;
			entityToAttack = null;
			p_70876_1_.entityToAttack = null;
			p_70876_1_.breeding = 0;
			p_70876_1_.inLove = 0;
			var2.setGrowingAge(-24000);
			var2.setLocationAndAngles(posX, posY, posZ, rotationYaw,
					rotationPitch);

			for (int var3 = 0; var3 < 7; ++var3) {
				final double var4 = rand.nextGaussian() * 0.02D;
				final double var6 = rand.nextGaussian() * 0.02D;
				final double var8 = rand.nextGaussian() * 0.02D;
				worldObj.spawnParticle("heart", posX + rand.nextFloat() * width
						* 2.0F - width,
						posY + 0.5D + rand.nextFloat() * height,
						posZ + rand.nextFloat() * width * 2.0F - width, var4,
						var6, var8);
			}

			worldObj.spawnEntityInWorld(var2);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		super.readEntityFromNBT(p_70037_1_);
		inLove = p_70037_1_.getInteger("InLove");
	}

	public void resetInLove() {
		inLove = 0;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick() {
		if (getGrowingAge() != 0) {
			inLove = 0;
		}

		super.updateAITick();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		super.writeEntityToNBT(p_70014_1_);
		p_70014_1_.setInteger("InLove", inLove);
	}
}

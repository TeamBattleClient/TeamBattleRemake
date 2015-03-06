package net.minecraft.entity.monster;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSilverfish;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.ImmutablePair;

public class EntitySilverfish extends EntityMob {
	/**
	 * A cooldown before this entity will search for another Silverfish to join
	 * them in battle.
	 */
	private int allySummonCooldown;

	public EntitySilverfish(World p_i1740_1_) {
		super(p_i1740_1_);
		setSize(0.3F, 0.7F);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth)
				.setBaseValue(8.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(
				0.6000000238418579D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(
				1.0D);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden
	 * by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
		if (attackTime <= 0 && p_70785_2_ < 1.2F
				&& p_70785_1_.boundingBox.maxY > boundingBox.minY
				&& p_70785_1_.boundingBox.minY < boundingBox.maxY) {
			attackTime = 20;
			attackEntityAsMob(p_70785_1_);
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
			if (allySummonCooldown <= 0
					&& (p_70097_1_ instanceof EntityDamageSource || p_70097_1_ == DamageSource.magic)) {
				allySummonCooldown = 20;
			}

			return super.attackEntityFrom(p_70097_1_, p_70097_2_);
		}
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this
	 * Entity isn't interested in attacking (Animals, Spiders at day, peaceful
	 * PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack() {
		final double var1 = 8.0D;
		return worldObj.getClosestVulnerablePlayerToEntity(this, var1);
	}

	@Override
	protected void func_145780_a(int p_145780_1_, int p_145780_2_,
			int p_145780_3_, Block p_145780_4_) {
		playSound("mob.silverfish.step", 0.15F, 1.0F);
	}

	@Override
	protected Item func_146068_u() {
		return Item.getItemById(0);
	}

	/**
	 * Takes a coordinate in and returns a weight to determine how likely this
	 * creature will try to path to the block. Args: x, y, z
	 */
	@Override
	public float getBlockPathWeight(int p_70783_1_, int p_70783_2_,
			int p_70783_3_) {
		return worldObj.getBlock(p_70783_1_, p_70783_2_ - 1, p_70783_3_) == Blocks.stone ? 10.0F
				: super.getBlockPathWeight(p_70783_1_, p_70783_2_, p_70783_3_);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			final EntityPlayer var1 = worldObj.getClosestPlayerToEntity(this,
					5.0D);
			return var1 == null;
		} else
			return false;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel() {
		return true;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		renderYawOffset = rotationYaw;
		super.onUpdate();
	}

	@Override
	protected void updateEntityActionState() {
		super.updateEntityActionState();

		if (!worldObj.isClient) {
			int var1;
			int var2;
			int var3;
			int var6;

			if (allySummonCooldown > 0) {
				--allySummonCooldown;

				if (allySummonCooldown == 0) {
					var1 = MathHelper.floor_double(posX);
					var2 = MathHelper.floor_double(posY);
					var3 = MathHelper.floor_double(posZ);
					boolean var4 = false;

					for (int var5 = 0; !var4 && var5 <= 5 && var5 >= -5; var5 = var5 <= 0 ? 1 - var5
							: 0 - var5) {
						for (var6 = 0; !var4 && var6 <= 10 && var6 >= -10; var6 = var6 <= 0 ? 1 - var6
								: 0 - var6) {
							for (int var7 = 0; !var4 && var7 <= 10
									&& var7 >= -10; var7 = var7 <= 0 ? 1 - var7
									: 0 - var7) {
								if (worldObj.getBlock(var1 + var6, var2 + var5,
										var3 + var7) == Blocks.monster_egg) {
									if (!worldObj.getGameRules()
											.getGameRuleBooleanValue(
													"mobGriefing")) {
										final int var8 = worldObj
												.getBlockMetadata(var1 + var6,
														var2 + var5, var3
																+ var7);
										final ImmutablePair var9 = BlockSilverfish
												.func_150197_b(var8);
										worldObj.setBlock(var1 + var6, var2
												+ var5, var3 + var7,
												(Block) var9.getLeft(),
												((Integer) var9.getRight())
														.intValue(), 3);
									} else {
										worldObj.func_147480_a(var1 + var6,
												var2 + var5, var3 + var7, false);
									}

									Blocks.monster_egg
											.onBlockDestroyedByPlayer(worldObj,
													var1 + var6, var2 + var5,
													var3 + var7, 0);

									if (rand.nextBoolean()) {
										var4 = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			if (entityToAttack == null && !hasPath()) {
				var1 = MathHelper.floor_double(posX);
				var2 = MathHelper.floor_double(posY + 0.5D);
				var3 = MathHelper.floor_double(posZ);
				final int var10 = rand.nextInt(6);
				final Block var11 = worldObj.getBlock(var1
						+ Facing.offsetsXForSide[var10], var2
						+ Facing.offsetsYForSide[var10], var3
						+ Facing.offsetsZForSide[var10]);
				var6 = worldObj.getBlockMetadata(var1
						+ Facing.offsetsXForSide[var10], var2
						+ Facing.offsetsYForSide[var10], var3
						+ Facing.offsetsZForSide[var10]);

				if (BlockSilverfish.func_150196_a(var11)) {
					worldObj.setBlock(var1 + Facing.offsetsXForSide[var10],
							var2 + Facing.offsetsYForSide[var10], var3
									+ Facing.offsetsZForSide[var10],
							Blocks.monster_egg,
							BlockSilverfish.func_150195_a(var11, var6), 3);
					spawnExplosionParticle();
					setDead();
				} else {
					updateWanderPath();
				}
			} else if (entityToAttack != null && !hasPath()) {
				entityToAttack = null;
			}
		}
	}
}

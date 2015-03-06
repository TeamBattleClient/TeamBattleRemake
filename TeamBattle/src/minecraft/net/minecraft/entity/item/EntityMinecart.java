package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class EntityMinecart extends Entity {
	/** Minecart rotational logic matrix */
	private static final int[][][] matrix = new int[][][] {
			{ { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } },
			{ { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } },
			{ { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } },
			{ { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } },
			{ { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };

	/**
	 * Creates a new minecart of the specified type in the specified location in
	 * the given world. par0World - world to create the minecart in, double
	 * par1,par3,par5 represent x,y,z respectively. int par7 specifies the type:
	 * 1 for MinecartChest, 2 for MinecartFurnace, 3 for MinecartTNT, 4 for
	 * MinecartMobSpawner, 5 for MinecartHopper and 0 for a standard empty
	 * minecart
	 */
	public static EntityMinecart createMinecart(World p_94090_0_,
			double p_94090_1_, double p_94090_3_, double p_94090_5_,
			int p_94090_7_) {
		switch (p_94090_7_) {
		case 1:
			return new EntityMinecartChest(p_94090_0_, p_94090_1_, p_94090_3_,
					p_94090_5_);

		case 2:
			return new EntityMinecartFurnace(p_94090_0_, p_94090_1_,
					p_94090_3_, p_94090_5_);

		case 3:
			return new EntityMinecartTNT(p_94090_0_, p_94090_1_, p_94090_3_,
					p_94090_5_);

		case 4:
			return new EntityMinecartMobSpawner(p_94090_0_, p_94090_1_,
					p_94090_3_, p_94090_5_);

		case 5:
			return new EntityMinecartHopper(p_94090_0_, p_94090_1_, p_94090_3_,
					p_94090_5_);

		case 6:
			return new EntityMinecartCommandBlock(p_94090_0_, p_94090_1_,
					p_94090_3_, p_94090_5_);

		default:
			return new EntityMinecartEmpty(p_94090_0_, p_94090_1_, p_94090_3_,
					p_94090_5_);
		}
	}

	private String entityName;

	private boolean isInReverse;
	private double minecartPitch;
	private double minecartX;
	private double minecartY;
	private double minecartYaw;
	private double minecartZ;
	/** appears to be the progress of the turn */
	private int turnProgress;
	private double velocityX;
	private double velocityY;

	private double velocityZ;

	public EntityMinecart(World p_i1712_1_) {
		super(p_i1712_1_);
		preventEntitySpawning = true;
		setSize(0.98F, 0.7F);
		yOffset = height / 2.0F;
	}

	public EntityMinecart(World p_i1713_1_, double p_i1713_2_,
			double p_i1713_4_, double p_i1713_6_) {
		this(p_i1713_1_);
		setPosition(p_i1713_2_, p_i1713_4_, p_i1713_6_);
		motionX = 0.0D;
		motionY = 0.0D;
		motionZ = 0.0D;
		prevPosX = p_i1713_2_;
		prevPosY = p_i1713_4_;
		prevPosZ = p_i1713_6_;
	}

	protected void applyDrag() {
		if (riddenByEntity != null) {
			motionX *= 0.996999979019165D;
			motionY *= 0.0D;
			motionZ *= 0.996999979019165D;
		} else {
			motionX *= 0.9599999785423279D;
			motionY *= 0.0D;
			motionZ *= 0.9599999785423279D;
		}
	}

	/**
	 * Applies a velocity to each of the entities pushing them away from each
	 * other. Args: entity
	 */
	@Override
	public void applyEntityCollision(Entity p_70108_1_) {
		if (!worldObj.isClient) {
			if (p_70108_1_ != riddenByEntity) {
				if (p_70108_1_ instanceof EntityLivingBase
						&& !(p_70108_1_ instanceof EntityPlayer)
						&& !(p_70108_1_ instanceof EntityIronGolem)
						&& getMinecartType() == 0
						&& motionX * motionX + motionZ * motionZ > 0.01D
						&& riddenByEntity == null
						&& p_70108_1_.ridingEntity == null) {
					p_70108_1_.mountEntity(this);
				}

				double var2 = p_70108_1_.posX - posX;
				double var4 = p_70108_1_.posZ - posZ;
				double var6 = var2 * var2 + var4 * var4;

				if (var6 >= 9.999999747378752E-5D) {
					var6 = MathHelper.sqrt_double(var6);
					var2 /= var6;
					var4 /= var6;
					double var8 = 1.0D / var6;

					if (var8 > 1.0D) {
						var8 = 1.0D;
					}

					var2 *= var8;
					var4 *= var8;
					var2 *= 0.10000000149011612D;
					var4 *= 0.10000000149011612D;
					var2 *= 1.0F - entityCollisionReduction;
					var4 *= 1.0F - entityCollisionReduction;
					var2 *= 0.5D;
					var4 *= 0.5D;

					if (p_70108_1_ instanceof EntityMinecart) {
						final double var10 = p_70108_1_.posX - posX;
						final double var12 = p_70108_1_.posZ - posZ;
						final Vec3 var14 = Vec3.createVectorHelper(var10, 0.0D,
								var12).normalize();
						final Vec3 var15 = Vec3.createVectorHelper(
								MathHelper.cos(rotationYaw * (float) Math.PI
										/ 180.0F),
								0.0D,
								MathHelper.sin(rotationYaw * (float) Math.PI
										/ 180.0F)).normalize();
						final double var16 = Math.abs(var14.dotProduct(var15));

						if (var16 < 0.800000011920929D)
							return;

						double var18 = p_70108_1_.motionX + motionX;
						double var20 = p_70108_1_.motionZ + motionZ;

						if (((EntityMinecart) p_70108_1_).getMinecartType() == 2
								&& getMinecartType() != 2) {
							motionX *= 0.20000000298023224D;
							motionZ *= 0.20000000298023224D;
							addVelocity(p_70108_1_.motionX - var2, 0.0D,
									p_70108_1_.motionZ - var4);
							p_70108_1_.motionX *= 0.949999988079071D;
							p_70108_1_.motionZ *= 0.949999988079071D;
						} else if (((EntityMinecart) p_70108_1_)
								.getMinecartType() != 2
								&& getMinecartType() == 2) {
							p_70108_1_.motionX *= 0.20000000298023224D;
							p_70108_1_.motionZ *= 0.20000000298023224D;
							p_70108_1_.addVelocity(motionX + var2, 0.0D,
									motionZ + var4);
							motionX *= 0.949999988079071D;
							motionZ *= 0.949999988079071D;
						} else {
							var18 /= 2.0D;
							var20 /= 2.0D;
							motionX *= 0.20000000298023224D;
							motionZ *= 0.20000000298023224D;
							addVelocity(var18 - var2, 0.0D, var20 - var4);
							p_70108_1_.motionX *= 0.20000000298023224D;
							p_70108_1_.motionZ *= 0.20000000298023224D;
							p_70108_1_.addVelocity(var18 + var2, 0.0D, var20
									+ var4);
						}
					} else {
						addVelocity(-var2, 0.0D, -var4);
						p_70108_1_.addVelocity(var2 / 4.0D, 0.0D, var4 / 4.0D);
					}
				}
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (!worldObj.isClient && !isDead) {
			if (isEntityInvulnerable())
				return false;
			else {
				setRollingDirection(-getRollingDirection());
				setRollingAmplitude(10);
				setBeenAttacked();
				setDamage(getDamage() + p_70097_2_ * 10.0F);
				final boolean var3 = p_70097_1_.getEntity() instanceof EntityPlayer
						&& ((EntityPlayer) p_70097_1_.getEntity()).capabilities.isCreativeMode;

				if (var3 || getDamage() > 40.0F) {
					if (riddenByEntity != null) {
						riddenByEntity.mountEntity(this);
					}

					if (var3 && !isInventoryNameLocalized()) {
						setDead();
					} else {
						killMinecart(p_70097_1_);
					}
				}

				return true;
			}
		} else
			return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return true;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(17, new Integer(0));
		dataWatcher.addObject(18, new Integer(1));
		dataWatcher.addObject(19, new Float(0.0F));
		dataWatcher.addObject(20, new Integer(0));
		dataWatcher.addObject(21, new Integer(6));
		dataWatcher.addObject(22, Byte.valueOf((byte) 0));
	}

	public Block func_145817_o() {
		return Blocks.air;
	}

	public void func_145819_k(int p_145819_1_) {
		getDataWatcher().updateObject(
				20,
				Integer.valueOf(p_145819_1_ & 65535
						| getDisplayTileData() << 16));
		setHasDisplayTile(true);
	}

	public Block func_145820_n() {
		if (!hasDisplayTile())
			return func_145817_o();
		else {
			final int var1 = getDataWatcher().getWatchableObjectInt(20) & 65535;
			return Block.getBlockById(var1);
		}
	}

	protected void func_145821_a(int p_145821_1_, int p_145821_2_,
			int p_145821_3_, double p_145821_4_, double p_145821_6_,
			Block p_145821_8_, int p_145821_9_) {
		fallDistance = 0.0F;
		final Vec3 var10 = func_70489_a(posX, posY, posZ);
		posY = p_145821_2_;
		boolean var11 = false;
		boolean var12 = false;

		if (p_145821_8_ == Blocks.golden_rail) {
			var11 = (p_145821_9_ & 8) != 0;
			var12 = !var11;
		}

		if (((BlockRailBase) p_145821_8_).func_150050_e()) {
			p_145821_9_ &= 7;
		}

		if (p_145821_9_ >= 2 && p_145821_9_ <= 5) {
			posY = p_145821_2_ + 1;
		}

		if (p_145821_9_ == 2) {
			motionX -= p_145821_6_;
		}

		if (p_145821_9_ == 3) {
			motionX += p_145821_6_;
		}

		if (p_145821_9_ == 4) {
			motionZ += p_145821_6_;
		}

		if (p_145821_9_ == 5) {
			motionZ -= p_145821_6_;
		}

		final int[][] var13 = matrix[p_145821_9_];
		double var14 = var13[1][0] - var13[0][0];
		double var16 = var13[1][2] - var13[0][2];
		final double var18 = Math.sqrt(var14 * var14 + var16 * var16);
		final double var20 = motionX * var14 + motionZ * var16;

		if (var20 < 0.0D) {
			var14 = -var14;
			var16 = -var16;
		}

		double var22 = Math.sqrt(motionX * motionX + motionZ * motionZ);

		if (var22 > 2.0D) {
			var22 = 2.0D;
		}

		motionX = var22 * var14 / var18;
		motionZ = var22 * var16 / var18;
		double var24;
		double var26;
		double var28;
		double var30;

		if (riddenByEntity != null
				&& riddenByEntity instanceof EntityLivingBase) {
			var24 = ((EntityLivingBase) riddenByEntity).moveForward;

			if (var24 > 0.0D) {
				var26 = -Math.sin(riddenByEntity.rotationYaw * (float) Math.PI
						/ 180.0F);
				var28 = Math.cos(riddenByEntity.rotationYaw * (float) Math.PI
						/ 180.0F);
				var30 = motionX * motionX + motionZ * motionZ;

				if (var30 < 0.01D) {
					motionX += var26 * 0.1D;
					motionZ += var28 * 0.1D;
					var12 = false;
				}
			}
		}

		if (var12) {
			var24 = Math.sqrt(motionX * motionX + motionZ * motionZ);

			if (var24 < 0.03D) {
				motionX *= 0.0D;
				motionY *= 0.0D;
				motionZ *= 0.0D;
			} else {
				motionX *= 0.5D;
				motionY *= 0.0D;
				motionZ *= 0.5D;
			}
		}

		var24 = 0.0D;
		var26 = p_145821_1_ + 0.5D + var13[0][0] * 0.5D;
		var28 = p_145821_3_ + 0.5D + var13[0][2] * 0.5D;
		var30 = p_145821_1_ + 0.5D + var13[1][0] * 0.5D;
		final double var32 = p_145821_3_ + 0.5D + var13[1][2] * 0.5D;
		var14 = var30 - var26;
		var16 = var32 - var28;
		double var34;
		double var36;

		if (var14 == 0.0D) {
			posX = p_145821_1_ + 0.5D;
			var24 = posZ - p_145821_3_;
		} else if (var16 == 0.0D) {
			posZ = p_145821_3_ + 0.5D;
			var24 = posX - p_145821_1_;
		} else {
			var34 = posX - var26;
			var36 = posZ - var28;
			var24 = (var34 * var14 + var36 * var16) * 2.0D;
		}

		posX = var26 + var14 * var24;
		posZ = var28 + var16 * var24;
		setPosition(posX, posY + yOffset, posZ);
		var34 = motionX;
		var36 = motionZ;

		if (riddenByEntity != null) {
			var34 *= 0.75D;
			var36 *= 0.75D;
		}

		if (var34 < -p_145821_4_) {
			var34 = -p_145821_4_;
		}

		if (var34 > p_145821_4_) {
			var34 = p_145821_4_;
		}

		if (var36 < -p_145821_4_) {
			var36 = -p_145821_4_;
		}

		if (var36 > p_145821_4_) {
			var36 = p_145821_4_;
		}

		moveEntity(var34, 0.0D, var36);

		if (var13[0][1] != 0
				&& MathHelper.floor_double(posX) - p_145821_1_ == var13[0][0]
				&& MathHelper.floor_double(posZ) - p_145821_3_ == var13[0][2]) {
			setPosition(posX, posY + var13[0][1], posZ);
		} else if (var13[1][1] != 0
				&& MathHelper.floor_double(posX) - p_145821_1_ == var13[1][0]
				&& MathHelper.floor_double(posZ) - p_145821_3_ == var13[1][2]) {
			setPosition(posX, posY + var13[1][1], posZ);
		}

		applyDrag();
		final Vec3 var38 = func_70489_a(posX, posY, posZ);

		if (var38 != null && var10 != null) {
			final double var39 = (var10.yCoord - var38.yCoord) * 0.05D;
			var22 = Math.sqrt(motionX * motionX + motionZ * motionZ);

			if (var22 > 0.0D) {
				motionX = motionX / var22 * (var22 + var39);
				motionZ = motionZ / var22 * (var22 + var39);
			}

			setPosition(posX, var38.yCoord, posZ);
		}

		final int var45 = MathHelper.floor_double(posX);
		final int var40 = MathHelper.floor_double(posZ);

		if (var45 != p_145821_1_ || var40 != p_145821_3_) {
			var22 = Math.sqrt(motionX * motionX + motionZ * motionZ);
			motionX = var22 * (var45 - p_145821_1_);
			motionZ = var22 * (var40 - p_145821_3_);
		}

		if (var11) {
			final double var41 = Math.sqrt(motionX * motionX + motionZ
					* motionZ);

			if (var41 > 0.01D) {
				final double var43 = 0.06D;
				motionX += motionX / var41 * var43;
				motionZ += motionZ / var41 * var43;
			} else if (p_145821_9_ == 1) {
				if (worldObj
						.getBlock(p_145821_1_ - 1, p_145821_2_, p_145821_3_)
						.isNormalCube()) {
					motionX = 0.02D;
				} else if (worldObj.getBlock(p_145821_1_ + 1, p_145821_2_,
						p_145821_3_).isNormalCube()) {
					motionX = -0.02D;
				}
			} else if (p_145821_9_ == 0) {
				if (worldObj
						.getBlock(p_145821_1_, p_145821_2_, p_145821_3_ - 1)
						.isNormalCube()) {
					motionZ = 0.02D;
				} else if (worldObj.getBlock(p_145821_1_, p_145821_2_,
						p_145821_3_ + 1).isNormalCube()) {
					motionZ = -0.02D;
				}
			}
		}
	}

	public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_,
			double p_70489_5_) {
		final int var7 = MathHelper.floor_double(p_70489_1_);
		int var8 = MathHelper.floor_double(p_70489_3_);
		final int var9 = MathHelper.floor_double(p_70489_5_);

		if (BlockRailBase.func_150049_b_(worldObj, var7, var8 - 1, var9)) {
			--var8;
		}

		final Block var10 = worldObj.getBlock(var7, var8, var9);

		if (BlockRailBase.func_150051_a(var10)) {
			int var11 = worldObj.getBlockMetadata(var7, var8, var9);
			p_70489_3_ = var8;

			if (((BlockRailBase) var10).func_150050_e()) {
				var11 &= 7;
			}

			if (var11 >= 2 && var11 <= 5) {
				p_70489_3_ = var8 + 1;
			}

			final int[][] var12 = matrix[var11];
			double var13 = 0.0D;
			final double var15 = var7 + 0.5D + var12[0][0] * 0.5D;
			final double var17 = var8 + 0.5D + var12[0][1] * 0.5D;
			final double var19 = var9 + 0.5D + var12[0][2] * 0.5D;
			final double var21 = var7 + 0.5D + var12[1][0] * 0.5D;
			final double var23 = var8 + 0.5D + var12[1][1] * 0.5D;
			final double var25 = var9 + 0.5D + var12[1][2] * 0.5D;
			final double var27 = var21 - var15;
			final double var29 = (var23 - var17) * 2.0D;
			final double var31 = var25 - var19;

			if (var27 == 0.0D) {
				p_70489_1_ = var7 + 0.5D;
				var13 = p_70489_5_ - var9;
			} else if (var31 == 0.0D) {
				p_70489_5_ = var9 + 0.5D;
				var13 = p_70489_1_ - var7;
			} else {
				final double var33 = p_70489_1_ - var15;
				final double var35 = p_70489_5_ - var19;
				var13 = (var33 * var27 + var35 * var31) * 2.0D;
			}

			p_70489_1_ = var15 + var27 * var13;
			p_70489_3_ = var17 + var29 * var13;
			p_70489_5_ = var19 + var31 * var13;

			if (var29 < 0.0D) {
				++p_70489_3_;
			}

			if (var29 > 0.0D) {
				p_70489_3_ += 0.5D;
			}

			return Vec3.createVectorHelper(p_70489_1_, p_70489_3_, p_70489_5_);
		} else
			return null;
	}

	public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_,
			double p_70495_5_, double p_70495_7_) {
		final int var9 = MathHelper.floor_double(p_70495_1_);
		int var10 = MathHelper.floor_double(p_70495_3_);
		final int var11 = MathHelper.floor_double(p_70495_5_);

		if (BlockRailBase.func_150049_b_(worldObj, var9, var10 - 1, var11)) {
			--var10;
		}

		final Block var12 = worldObj.getBlock(var9, var10, var11);

		if (!BlockRailBase.func_150051_a(var12))
			return null;
		else {
			int var13 = worldObj.getBlockMetadata(var9, var10, var11);

			if (((BlockRailBase) var12).func_150050_e()) {
				var13 &= 7;
			}

			p_70495_3_ = var10;

			if (var13 >= 2 && var13 <= 5) {
				p_70495_3_ = var10 + 1;
			}

			final int[][] var14 = matrix[var13];
			double var15 = var14[1][0] - var14[0][0];
			double var17 = var14[1][2] - var14[0][2];
			final double var19 = Math.sqrt(var15 * var15 + var17 * var17);
			var15 /= var19;
			var17 /= var19;
			p_70495_1_ += var15 * p_70495_7_;
			p_70495_5_ += var17 * p_70495_7_;

			if (var14[0][1] != 0
					&& MathHelper.floor_double(p_70495_1_) - var9 == var14[0][0]
					&& MathHelper.floor_double(p_70495_5_) - var11 == var14[0][2]) {
				p_70495_3_ += var14[0][1];
			} else if (var14[1][1] != 0
					&& MathHelper.floor_double(p_70495_1_) - var9 == var14[1][0]
					&& MathHelper.floor_double(p_70495_5_) - var11 == var14[1][2]) {
				p_70495_3_ += var14[1][1];
			}

			return func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
		}
	}

	protected void func_94088_b(double p_94088_1_) {
		if (motionX < -p_94088_1_) {
			motionX = -p_94088_1_;
		}

		if (motionX > p_94088_1_) {
			motionX = p_94088_1_;
		}

		if (motionZ < -p_94088_1_) {
			motionZ = -p_94088_1_;
		}

		if (motionZ > p_94088_1_) {
			motionZ = p_94088_1_;
		}

		if (onGround) {
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		}

		moveEntity(motionX, motionY, motionZ);

		if (!onGround) {
			motionX *= 0.949999988079071D;
			motionY *= 0.949999988079071D;
			motionZ *= 0.949999988079071D;
		}
	}

	public String func_95999_t() {
		return entityName;
	}

	/**
	 * returns the bounding box for this entity
	 */
	@Override
	public AxisAlignedBB getBoundingBox() {
		return null;
	}

	/**
	 * Returns a boundingBox used to collide the entity with other entities and
	 * blocks. This enables the entity to be pushable on contact, like boats or
	 * minecarts.
	 */
	@Override
	public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
		return p_70114_1_.canBePushed() ? p_70114_1_.boundingBox : null;
	}

	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return entityName != null ? entityName : super.getCommandSenderName();
	}

	/**
	 * Gets the current amount of damage the minecart has taken. Decreases over
	 * time. The cart breaks when this is over 40.
	 */
	public float getDamage() {
		return dataWatcher.getWatchableObjectFloat(19);
	}

	public int getDefaultDisplayTileData() {
		return 0;
	}

	public int getDefaultDisplayTileOffset() {
		return 6;
	}

	public int getDisplayTileData() {
		return !hasDisplayTile() ? getDefaultDisplayTileData()
				: getDataWatcher().getWatchableObjectInt(20) >> 16;
	}

	public int getDisplayTileOffset() {
		return !hasDisplayTile() ? getDefaultDisplayTileOffset()
				: getDataWatcher().getWatchableObjectInt(21);
	}

	public abstract int getMinecartType();

	/**
	 * Returns the Y offset from the entity's position for any entity riding
	 * this one.
	 */
	@Override
	public double getMountedYOffset() {
		return height * 0.0D - 0.30000001192092896D;
	}

	/**
	 * Gets the rolling amplitude the cart rolls while being attacked.
	 */
	public int getRollingAmplitude() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	/**
	 * Gets the rolling direction the cart rolls while being attacked. Can be 1
	 * or -1.
	 */
	public int getRollingDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	public boolean hasDisplayTile() {
		return getDataWatcher().getWatchableObjectByte(22) == 1;
	}

	/**
	 * Returns if the inventory name is localized
	 */
	public boolean isInventoryNameLocalized() {
		return entityName != null;
	}

	public void killMinecart(DamageSource p_94095_1_) {
		setDead();
		final ItemStack var2 = new ItemStack(Items.minecart, 1);

		if (entityName != null) {
			var2.setStackDisplayName(entityName);
		}

		entityDropItem(var2, 0.0F);
	}

	/**
	 * Called every tick the minecart is on an activator rail. Args: x, y, z, is
	 * the rail receiving power
	 */
	public void onActivatorRailPass(int p_96095_1_, int p_96095_2_,
			int p_96095_3_, boolean p_96095_4_) {
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (getRollingAmplitude() > 0) {
			setRollingAmplitude(getRollingAmplitude() - 1);
		}

		if (getDamage() > 0.0F) {
			setDamage(getDamage() - 1.0F);
		}

		if (posY < -64.0D) {
			kill();
		}

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

		if (worldObj.isClient) {
			if (turnProgress > 0) {
				final double var19 = posX + (minecartX - posX) / turnProgress;
				final double var21 = posY + (minecartY - posY) / turnProgress;
				final double var5 = posZ + (minecartZ - posZ) / turnProgress;
				final double var7 = MathHelper
						.wrapAngleTo180_double(minecartYaw - rotationYaw);
				rotationYaw = (float) (rotationYaw + var7 / turnProgress);
				rotationPitch = (float) (rotationPitch + (minecartPitch - rotationPitch)
						/ turnProgress);
				--turnProgress;
				setPosition(var19, var21, var5);
				setRotation(rotationYaw, rotationPitch);
			} else {
				setPosition(posX, posY, posZ);
				setRotation(rotationYaw, rotationPitch);
			}
		} else {
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			motionY -= 0.03999999910593033D;
			final int var18 = MathHelper.floor_double(posX);
			var2 = MathHelper.floor_double(posY);
			final int var20 = MathHelper.floor_double(posZ);

			if (BlockRailBase.func_150049_b_(worldObj, var18, var2 - 1, var20)) {
				--var2;
			}

			final double var4 = 0.4D;
			final double var6 = 0.0078125D;
			final Block var8 = worldObj.getBlock(var18, var2, var20);

			if (BlockRailBase.func_150051_a(var8)) {
				final int var9 = worldObj.getBlockMetadata(var18, var2, var20);
				func_145821_a(var18, var2, var20, var4, var6, var8, var9);

				if (var8 == Blocks.activator_rail) {
					onActivatorRailPass(var18, var2, var20, (var9 & 8) != 0);
				}
			} else {
				func_94088_b(var4);
			}

			func_145775_I();
			rotationPitch = 0.0F;
			final double var22 = prevPosX - posX;
			final double var11 = prevPosZ - posZ;

			if (var22 * var22 + var11 * var11 > 0.001D) {
				rotationYaw = (float) (Math.atan2(var11, var22) * 180.0D / Math.PI);

				if (isInReverse) {
					rotationYaw += 180.0F;
				}
			}

			final double var13 = MathHelper.wrapAngleTo180_float(rotationYaw
					- prevRotationYaw);

			if (var13 < -170.0D || var13 >= 170.0D) {
				rotationYaw += 180.0F;
				isInReverse = !isInReverse;
			}

			setRotation(rotationYaw, rotationPitch);
			final List var15 = worldObj.getEntitiesWithinAABBExcludingEntity(
					this, boundingBox.expand(0.20000000298023224D, 0.0D,
							0.20000000298023224D));

			if (var15 != null && !var15.isEmpty()) {
				for (int var16 = 0; var16 < var15.size(); ++var16) {
					final Entity var17 = (Entity) var15.get(var16);

					if (var17 != riddenByEntity && var17.canBePushed()
							&& var17 instanceof EntityMinecart) {
						var17.applyEntityCollision(this);
					}
				}
			}

			if (riddenByEntity != null && riddenByEntity.isDead) {
				if (riddenByEntity.ridingEntity == this) {
					riddenByEntity.ridingEntity = null;
				}

				riddenByEntity = null;
			}
		}
	}

	/**
	 * Setups the entity to do the hurt animation. Only used by packets in
	 * multiplayer.
	 */
	@Override
	public void performHurtAnimation() {
		setRollingDirection(-getRollingDirection());
		setRollingAmplitude(10);
		setDamage(getDamage() + getDamage() * 10.0F);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		if (p_70037_1_.getBoolean("CustomDisplayTile")) {
			func_145819_k(p_70037_1_.getInteger("DisplayTile"));
			setDisplayTileData(p_70037_1_.getInteger("DisplayData"));
			setDisplayTileOffset(p_70037_1_.getInteger("DisplayOffset"));
		}

		if (p_70037_1_.func_150297_b("CustomName", 8)
				&& p_70037_1_.getString("CustomName").length() > 0) {
			entityName = p_70037_1_.getString("CustomName");
		}
	}

	/**
	 * Sets the current amount of damage the minecart has taken. Decreases over
	 * time. The cart breaks when this is over 40.
	 */
	public void setDamage(float p_70492_1_) {
		dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
	}

	/**
	 * Will get destroyed next tick.
	 */
	@Override
	public void setDead() {
		super.setDead();
	}

	public void setDisplayTileData(int p_94092_1_) {
		getDataWatcher().updateObject(
				20,
				Integer.valueOf(Block.getIdFromBlock(func_145820_n()) & 65535
						| p_94092_1_ << 16));
		setHasDisplayTile(true);
	}

	public void setDisplayTileOffset(int p_94086_1_) {
		getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
		setHasDisplayTile(true);
	}

	public void setHasDisplayTile(boolean p_94096_1_) {
		getDataWatcher().updateObject(22,
				Byte.valueOf((byte) (p_94096_1_ ? 1 : 0)));
	}

	/**
	 * Sets the minecart's name.
	 */
	public void setMinecartName(String p_96094_1_) {
		entityName = p_96094_1_;
	}

	/**
	 * Sets the position and rotation. Only difference from the other one is no
	 * bounding on the rotation. Args: posX, posY, posZ, yaw, pitch
	 */
	@Override
	public void setPositionAndRotation2(double p_70056_1_, double p_70056_3_,
			double p_70056_5_, float p_70056_7_, float p_70056_8_,
			int p_70056_9_) {
		minecartX = p_70056_1_;
		minecartY = p_70056_3_;
		minecartZ = p_70056_5_;
		minecartYaw = p_70056_7_;
		minecartPitch = p_70056_8_;
		turnProgress = p_70056_9_ + 2;
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	/**
	 * Sets the rolling amplitude the cart rolls while being attacked.
	 */
	public void setRollingAmplitude(int p_70497_1_) {
		dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
	}

	/**
	 * Sets the rolling direction the cart rolls while being attacked. Can be 1
	 * or -1.
	 */
	public void setRollingDirection(int p_70494_1_) {
		dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
	}

	/**
	 * Sets the velocity to the args. Args: x, y, z
	 */
	@Override
	public void setVelocity(double p_70016_1_, double p_70016_3_,
			double p_70016_5_) {
		velocityX = motionX = p_70016_1_;
		velocityY = motionY = p_70016_3_;
		velocityZ = motionZ = p_70016_5_;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		if (hasDisplayTile()) {
			p_70014_1_.setBoolean("CustomDisplayTile", true);
			p_70014_1_.setInteger(
					"DisplayTile",
					func_145820_n().getMaterial() == Material.air ? 0 : Block
							.getIdFromBlock(func_145820_n()));
			p_70014_1_.setInteger("DisplayData", getDisplayTileData());
			p_70014_1_.setInteger("DisplayOffset", getDisplayTileOffset());
		}

		if (entityName != null && entityName.length() > 0) {
			p_70014_1_.setString("CustomName", entityName);
		}
	}
}

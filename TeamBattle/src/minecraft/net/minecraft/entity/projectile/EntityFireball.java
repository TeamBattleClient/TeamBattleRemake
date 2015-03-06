package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityFireball extends Entity {
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;
	private int field_145793_f = -1;
	private int field_145794_g = -1;
	private int field_145795_e = -1;
	private Block field_145796_h;
	private boolean inGround;
	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;

	public EntityFireball(World p_i1759_1_) {
		super(p_i1759_1_);
		setSize(1.0F, 1.0F);
	}

	public EntityFireball(World p_i1760_1_, double p_i1760_2_,
			double p_i1760_4_, double p_i1760_6_, double p_i1760_8_,
			double p_i1760_10_, double p_i1760_12_) {
		super(p_i1760_1_);
		setSize(1.0F, 1.0F);
		setLocationAndAngles(p_i1760_2_, p_i1760_4_, p_i1760_6_, rotationYaw,
				rotationPitch);
		setPosition(p_i1760_2_, p_i1760_4_, p_i1760_6_);
		final double var14 = MathHelper.sqrt_double(p_i1760_8_ * p_i1760_8_
				+ p_i1760_10_ * p_i1760_10_ + p_i1760_12_ * p_i1760_12_);
		accelerationX = p_i1760_8_ / var14 * 0.1D;
		accelerationY = p_i1760_10_ / var14 * 0.1D;
		accelerationZ = p_i1760_12_ / var14 * 0.1D;
	}

	public EntityFireball(World p_i1761_1_, EntityLivingBase p_i1761_2_,
			double p_i1761_3_, double p_i1761_5_, double p_i1761_7_) {
		super(p_i1761_1_);
		shootingEntity = p_i1761_2_;
		setSize(1.0F, 1.0F);
		setLocationAndAngles(p_i1761_2_.posX, p_i1761_2_.posY, p_i1761_2_.posZ,
				p_i1761_2_.rotationYaw, p_i1761_2_.rotationPitch);
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		motionX = motionY = motionZ = 0.0D;
		p_i1761_3_ += rand.nextGaussian() * 0.4D;
		p_i1761_5_ += rand.nextGaussian() * 0.4D;
		p_i1761_7_ += rand.nextGaussian() * 0.4D;
		final double var9 = MathHelper.sqrt_double(p_i1761_3_ * p_i1761_3_
				+ p_i1761_5_ * p_i1761_5_ + p_i1761_7_ * p_i1761_7_);
		accelerationX = p_i1761_3_ / var9 * 0.1D;
		accelerationY = p_i1761_5_ / var9 * 0.1D;
		accelerationZ = p_i1761_7_ / var9 * 0.1D;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
		if (isEntityInvulnerable())
			return false;
		else {
			setBeenAttacked();

			if (p_70097_1_.getEntity() != null) {
				final Vec3 var3 = p_70097_1_.getEntity().getLookVec();

				if (var3 != null) {
					motionX = var3.xCoord;
					motionY = var3.yCoord;
					motionZ = var3.zCoord;
					accelerationX = motionX * 0.1D;
					accelerationY = motionY * 0.1D;
					accelerationZ = motionZ * 0.1D;
				}

				if (p_70097_1_.getEntity() instanceof EntityLivingBase) {
					shootingEntity = (EntityLivingBase) p_70097_1_.getEntity();
				}

				return true;
			} else
				return false;
		}
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return 1.0F;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return 15728880;
	}

	@Override
	public float getCollisionBorderSize() {
		return 1.0F;
	}

	/**
	 * Return the motion factor for this projectile. The factor is multiplied by
	 * the original motion.
	 */
	protected float getMotionFactor() {
		return 0.95F;
	}

	@Override
	public float getShadowSize() {
		return 0.0F;
	}

	/**
	 * Checks if the entity is in range to render by using the past in distance
	 * and comparing it to its average edge length * 64 * renderDistanceWeight
	 * Args: distance
	 */
	@Override
	public boolean isInRangeToRenderDist(double p_70112_1_) {
		double var3 = boundingBox.getAverageEdgeLength() * 4.0D;
		var3 *= 64.0D;
		return p_70112_1_ < var3 * var3;
	}

	/**
	 * Called when this EntityFireball hits a block or entity.
	 */
	protected abstract void onImpact(MovingObjectPosition p_70227_1_);

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		if (!worldObj.isClient
				&& (shootingEntity != null && shootingEntity.isDead || !worldObj
						.blockExists((int) posX, (int) posY, (int) posZ))) {
			setDead();
		} else {
			super.onUpdate();
			setFire(1);

			if (inGround) {
				if (worldObj.getBlock(field_145795_e, field_145793_f,
						field_145794_g) == field_145796_h) {
					++ticksAlive;

					if (ticksAlive == 600) {
						setDead();
					}

					return;
				}

				inGround = false;
				motionX *= rand.nextFloat() * 0.2F;
				motionY *= rand.nextFloat() * 0.2F;
				motionZ *= rand.nextFloat() * 0.2F;
				ticksAlive = 0;
				ticksInAir = 0;
			} else {
				++ticksInAir;
			}

			Vec3 var1 = Vec3.createVectorHelper(posX, posY, posZ);
			Vec3 var2 = Vec3.createVectorHelper(posX + motionX, posY + motionY,
					posZ + motionZ);
			MovingObjectPosition var3 = worldObj.rayTraceBlocks(var1, var2);
			var1 = Vec3.createVectorHelper(posX, posY, posZ);
			var2 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ
					+ motionZ);

			if (var3 != null) {
				var2 = Vec3.createVectorHelper(var3.hitVec.xCoord,
						var3.hitVec.yCoord, var3.hitVec.zCoord);
			}

			Entity var4 = null;
			final List var5 = worldObj.getEntitiesWithinAABBExcludingEntity(
					this, boundingBox.addCoord(motionX, motionY, motionZ)
							.expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;

			for (int var8 = 0; var8 < var5.size(); ++var8) {
				final Entity var9 = (Entity) var5.get(var8);

				if (var9.canBeCollidedWith()
						&& (!var9.isEntityEqual(shootingEntity) || ticksInAir >= 25)) {
					final float var10 = 0.3F;
					final AxisAlignedBB var11 = var9.boundingBox.expand(var10,
							var10, var10);
					final MovingObjectPosition var12 = var11
							.calculateIntercept(var1, var2);

					if (var12 != null) {
						final double var13 = var1.distanceTo(var12.hitVec);

						if (var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}

			if (var4 != null) {
				var3 = new MovingObjectPosition(var4);
			}

			if (var3 != null) {
				onImpact(var3);
			}

			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			final float var15 = MathHelper.sqrt_double(motionX * motionX
					+ motionZ * motionZ);
			rotationYaw = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) + 90.0F;

			for (rotationPitch = (float) (Math.atan2(var15, motionY) * 180.0D / Math.PI) - 90.0F; rotationPitch
					- prevRotationPitch < -180.0F; prevRotationPitch -= 360.0F) {
				;
			}

			while (rotationPitch - prevRotationPitch >= 180.0F) {
				prevRotationPitch += 360.0F;
			}

			while (rotationYaw - prevRotationYaw < -180.0F) {
				prevRotationYaw -= 360.0F;
			}

			while (rotationYaw - prevRotationYaw >= 180.0F) {
				prevRotationYaw += 360.0F;
			}

			rotationPitch = prevRotationPitch
					+ (rotationPitch - prevRotationPitch) * 0.2F;
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw)
					* 0.2F;
			float var16 = getMotionFactor();

			if (isInWater()) {
				for (int var17 = 0; var17 < 4; ++var17) {
					final float var18 = 0.25F;
					worldObj.spawnParticle("bubble", posX - motionX * var18,
							posY - motionY * var18, posZ - motionZ * var18,
							motionX, motionY, motionZ);
				}

				var16 = 0.8F;
			}

			motionX += accelerationX;
			motionY += accelerationY;
			motionZ += accelerationZ;
			motionX *= var16;
			motionY *= var16;
			motionZ *= var16;
			worldObj.spawnParticle("smoke", posX, posY + 0.5D, posZ, 0.0D,
					0.0D, 0.0D);
			setPosition(posX, posY, posZ);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
		field_145795_e = p_70037_1_.getShort("xTile");
		field_145793_f = p_70037_1_.getShort("yTile");
		field_145794_g = p_70037_1_.getShort("zTile");
		field_145796_h = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
		inGround = p_70037_1_.getByte("inGround") == 1;

		if (p_70037_1_.func_150297_b("direction", 9)) {
			final NBTTagList var2 = p_70037_1_.getTagList("direction", 6);
			motionX = var2.func_150309_d(0);
			motionY = var2.func_150309_d(1);
			motionZ = var2.func_150309_d(2);
		} else {
			setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
		p_70014_1_.setShort("xTile", (short) field_145795_e);
		p_70014_1_.setShort("yTile", (short) field_145793_f);
		p_70014_1_.setShort("zTile", (short) field_145794_g);
		p_70014_1_.setByte("inTile",
				(byte) Block.getIdFromBlock(field_145796_h));
		p_70014_1_.setByte("inGround", (byte) (inGround ? 1 : 0));
		p_70014_1_.setTag("direction", newDoubleNBTList(new double[] { motionX,
				motionY, motionZ }));
	}
}

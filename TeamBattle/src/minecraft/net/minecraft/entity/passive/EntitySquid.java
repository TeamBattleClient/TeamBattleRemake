package net.minecraft.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySquid extends EntityWaterMob {
	private float field_70871_bB;
	/** the last calculated angle of the tentacles in radians */
	public float lastTentacleAngle;
	public float prevSquidPitch;
	/** previous squidRotation in radians */
	public float prevSquidRotation;

	public float prevSquidYaw;

	private float randomMotionSpeed;

	private float randomMotionVecX;

	private float randomMotionVecY;
	private float randomMotionVecZ;

	/** change in squidRotation in radians. */
	private float rotationVelocity;
	public float squidPitch;
	/**
	 * appears to be rotation in radians; we already have pitch & yaw, so this
	 * completes the triumvirate.
	 */
	public float squidRotation;
	public float squidYaw;
	/** angle of the tentacles in radians */
	public float tentacleAngle;

	public EntitySquid(World p_i1693_1_) {
		super(p_i1693_1_);
		setSize(0.95F, 0.95F);
		rotationVelocity = 1.0F / (rand.nextFloat() + 1.0F) * 0.2F;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				10.0D);
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
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		final int var3 = rand.nextInt(3 + p_70628_2_) + 1;

		for (int var4 = 0; var4 < var3; ++var4) {
			entityDropItem(new ItemStack(Items.dye, 1, 0), 0.0F);
		}
	}

	@Override
	protected Item func_146068_u() {
		return Item.getItemById(0);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return posY > 45.0D && posY < 63.0D && super.getCanSpawnHere();
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return null;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return null;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return null;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a
	 * result of handleWaterMovement() returning true)
	 */
	@Override
	public boolean isInWater() {
		return worldObj.handleMaterialAcceleration(
				boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D),
				Material.water, this);
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		moveEntity(motionX, motionY, motionZ);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		prevSquidPitch = squidPitch;
		prevSquidYaw = squidYaw;
		prevSquidRotation = squidRotation;
		lastTentacleAngle = tentacleAngle;
		squidRotation += rotationVelocity;

		if (squidRotation > (float) Math.PI * 2F) {
			squidRotation -= (float) Math.PI * 2F;

			if (rand.nextInt(10) == 0) {
				rotationVelocity = 1.0F / (rand.nextFloat() + 1.0F) * 0.2F;
			}
		}

		if (isInWater()) {
			float var1;

			if (squidRotation < (float) Math.PI) {
				var1 = squidRotation / (float) Math.PI;
				tentacleAngle = MathHelper.sin(var1 * var1 * (float) Math.PI)
						* (float) Math.PI * 0.25F;

				if (var1 > 0.75D) {
					randomMotionSpeed = 1.0F;
					field_70871_bB = 1.0F;
				} else {
					field_70871_bB *= 0.8F;
				}
			} else {
				tentacleAngle = 0.0F;
				randomMotionSpeed *= 0.9F;
				field_70871_bB *= 0.99F;
			}

			if (!worldObj.isClient) {
				motionX = randomMotionVecX * randomMotionSpeed;
				motionY = randomMotionVecY * randomMotionSpeed;
				motionZ = randomMotionVecZ * randomMotionSpeed;
			}

			var1 = MathHelper
					.sqrt_double(motionX * motionX + motionZ * motionZ);
			renderYawOffset += (-((float) Math.atan2(motionX, motionZ))
					* 180.0F / (float) Math.PI - renderYawOffset) * 0.1F;
			rotationYaw = renderYawOffset;
			squidYaw += (float) Math.PI * field_70871_bB * 1.5F;
			squidPitch += (-((float) Math.atan2(var1, motionY)) * 180.0F
					/ (float) Math.PI - squidPitch) * 0.1F;
		} else {
			tentacleAngle = MathHelper.abs(MathHelper.sin(squidRotation))
					* (float) Math.PI * 0.25F;

			if (!worldObj.isClient) {
				motionX = 0.0D;
				motionY -= 0.08D;
				motionY *= 0.9800000190734863D;
				motionZ = 0.0D;
			}

			squidPitch = (float) (squidPitch + (-90.0F - squidPitch) * 0.02D);
		}
	}

	@Override
	protected void updateEntityActionState() {
		++entityAge;

		if (entityAge > 100) {
			randomMotionVecX = randomMotionVecY = randomMotionVecZ = 0.0F;
		} else if (rand.nextInt(50) == 0 || !inWater
				|| randomMotionVecX == 0.0F && randomMotionVecY == 0.0F
				&& randomMotionVecZ == 0.0F) {
			final float var1 = rand.nextFloat() * (float) Math.PI * 2.0F;
			randomMotionVecX = MathHelper.cos(var1) * 0.2F;
			randomMotionVecY = -0.1F + rand.nextFloat() * 0.2F;
			randomMotionVecZ = MathHelper.sin(var1) * 0.2F;
		}

		despawnEntity();
	}
}

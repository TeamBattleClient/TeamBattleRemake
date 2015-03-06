package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDropParticleFX extends EntityFX {
	/** The height of the current bob */
	private int bobTimer;

	/** the material type for dropped items/blocks */
	private final Material materialType;

	public EntityDropParticleFX(World p_i1203_1_, double p_i1203_2_,
			double p_i1203_4_, double p_i1203_6_, Material p_i1203_8_) {
		super(p_i1203_1_, p_i1203_2_, p_i1203_4_, p_i1203_6_, 0.0D, 0.0D, 0.0D);
		motionX = motionY = motionZ = 0.0D;

		if (p_i1203_8_ == Material.water) {
			particleRed = 0.0F;
			particleGreen = 0.0F;
			particleBlue = 1.0F;
		} else {
			particleRed = 1.0F;
			particleGreen = 0.0F;
			particleBlue = 0.0F;
		}

		setParticleTextureIndex(113);
		setSize(0.01F, 0.01F);
		particleGravity = 0.06F;
		materialType = p_i1203_8_;
		bobTimer = 40;
		particleMaxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
		motionX = motionY = motionZ = 0.0D;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_) {
		return materialType == Material.water ? super.getBrightness(p_70013_1_)
				: 1.0F;
	}

	@Override
	public int getBrightnessForRender(float p_70070_1_) {
		return materialType == Material.water ? super
				.getBrightnessForRender(p_70070_1_) : 257;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (materialType == Material.water) {
			particleRed = 0.2F;
			particleGreen = 0.3F;
			particleBlue = 1.0F;
		} else {
			particleRed = 1.0F;
			particleGreen = 16.0F / (40 - bobTimer + 16);
			particleBlue = 4.0F / (40 - bobTimer + 8);
		}

		motionY -= particleGravity;

		if (bobTimer-- > 0) {
			motionX *= 0.02D;
			motionY *= 0.02D;
			motionZ *= 0.02D;
			setParticleTextureIndex(113);
		} else {
			setParticleTextureIndex(112);
		}

		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (particleMaxAge-- <= 0) {
			setDead();
		}

		if (onGround) {
			if (materialType == Material.water) {
				setDead();
				worldObj.spawnParticle("splash", posX, posY, posZ, 0.0D, 0.0D,
						0.0D);
			} else {
				setParticleTextureIndex(114);
			}

			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}

		final Material var1 = worldObj.getBlock(MathHelper.floor_double(posX),
				MathHelper.floor_double(posY), MathHelper.floor_double(posZ))
				.getMaterial();

		if (var1.isLiquid() || var1.isSolid()) {
			final double var2 = MathHelper.floor_double(posY)
					+ 1
					- BlockLiquid.func_149801_b(worldObj.getBlockMetadata(
							MathHelper.floor_double(posX),
							MathHelper.floor_double(posY),
							MathHelper.floor_double(posZ)));

			if (posY < var2) {
				setDead();
			}
		}
	}
}

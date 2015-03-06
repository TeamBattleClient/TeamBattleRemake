package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityRainFX extends EntityFX {

	public EntityRainFX(World p_i1235_1_, double p_i1235_2_, double p_i1235_4_,
			double p_i1235_6_) {
		super(p_i1235_1_, p_i1235_2_, p_i1235_4_, p_i1235_6_, 0.0D, 0.0D, 0.0D);
		motionX *= 0.30000001192092896D;
		motionY = (float) Math.random() * 0.2F + 0.1F;
		motionZ *= 0.30000001192092896D;
		particleRed = 1.0F;
		particleGreen = 1.0F;
		particleBlue = 1.0F;
		setParticleTextureIndex(19 + rand.nextInt(4));
		setSize(0.01F, 0.01F);
		particleGravity = 0.06F;
		particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		motionY -= particleGravity;
		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9800000190734863D;
		motionY *= 0.9800000190734863D;
		motionZ *= 0.9800000190734863D;

		if (particleMaxAge-- <= 0) {
			setDead();
		}

		if (onGround) {
			if (Math.random() < 0.5D) {
				setDead();
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

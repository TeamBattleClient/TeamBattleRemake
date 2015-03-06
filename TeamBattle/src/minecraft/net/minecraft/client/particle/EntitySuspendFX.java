package net.minecraft.client.particle;

import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySuspendFX extends EntityFX {

	public EntitySuspendFX(World p_i1231_1_, double p_i1231_2_,
			double p_i1231_4_, double p_i1231_6_, double p_i1231_8_,
			double p_i1231_10_, double p_i1231_12_) {
		super(p_i1231_1_, p_i1231_2_, p_i1231_4_ - 0.125D, p_i1231_6_,
				p_i1231_8_, p_i1231_10_, p_i1231_12_);
		particleRed = 0.4F;
		particleGreen = 0.4F;
		particleBlue = 0.7F;
		setParticleTextureIndex(0);
		setSize(0.01F, 0.01F);
		particleScale *= rand.nextFloat() * 0.6F + 0.2F;
		motionX = p_i1231_8_ * 0.0D;
		motionY = p_i1231_10_ * 0.0D;
		motionZ = p_i1231_12_ * 0.0D;
		particleMaxAge = (int) (16.0D / (Math.random() * 0.8D + 0.2D));
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		moveEntity(motionX, motionY, motionZ);

		if (worldObj.getBlock(MathHelper.floor_double(posX),
				MathHelper.floor_double(posY), MathHelper.floor_double(posZ))
				.getMaterial() != Material.water) {
			setDead();
		}

		if (particleMaxAge-- <= 0) {
			setDead();
		}
	}
}

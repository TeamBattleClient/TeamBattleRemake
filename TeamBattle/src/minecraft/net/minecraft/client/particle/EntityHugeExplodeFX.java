package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class EntityHugeExplodeFX extends EntityFX {
	/** the maximum time for the explosion */
	private final int maximumTime = 8;

	private int timeSinceStart;

	public EntityHugeExplodeFX(World p_i1214_1_, double p_i1214_2_,
			double p_i1214_4_, double p_i1214_6_, double p_i1214_8_,
			double p_i1214_10_, double p_i1214_12_) {
		super(p_i1214_1_, p_i1214_2_, p_i1214_4_, p_i1214_6_, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		for (int var1 = 0; var1 < 6; ++var1) {
			final double var2 = posX + (rand.nextDouble() - rand.nextDouble())
					* 4.0D;
			final double var4 = posY + (rand.nextDouble() - rand.nextDouble())
					* 4.0D;
			final double var6 = posZ + (rand.nextDouble() - rand.nextDouble())
					* 4.0D;
			worldObj.spawnParticle("largeexplode", var2, var4, var6,
					(float) timeSinceStart / (float) maximumTime, 0.0D, 0.0D);
		}

		++timeSinceStart;

		if (timeSinceStart == maximumTime) {
			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
	}
}

package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityCrit2FX extends EntityFX {
	private int currentLife;
	private final int maximumLife;
	private final String particleName;
	/** Entity that had been hit and done the Critical hit on. */
	private final Entity theEntity;

	public EntityCrit2FX(World p_i1199_1_, Entity p_i1199_2_) {
		this(p_i1199_1_, p_i1199_2_, "crit");
	}

	public EntityCrit2FX(World p_i1200_1_, Entity p_i1200_2_, String p_i1200_3_) {
		super(p_i1200_1_, p_i1200_2_.posX, p_i1200_2_.boundingBox.minY
				+ p_i1200_2_.height / 2.0F, p_i1200_2_.posZ,
				p_i1200_2_.motionX, p_i1200_2_.motionY, p_i1200_2_.motionZ);
		theEntity = p_i1200_2_;
		maximumLife = 3;
		particleName = p_i1200_3_;
		onUpdate();
	}

	@Override
	public int getFXLayer() {
		return 3;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		for (int var1 = 0; var1 < 16; ++var1) {
			final double var2 = rand.nextFloat() * 2.0F - 1.0F;
			final double var4 = rand.nextFloat() * 2.0F - 1.0F;
			final double var6 = rand.nextFloat() * 2.0F - 1.0F;

			if (var2 * var2 + var4 * var4 + var6 * var6 <= 1.0D) {
				final double var8 = theEntity.posX + var2 * theEntity.width
						/ 4.0D;
				final double var10 = theEntity.boundingBox.minY
						+ theEntity.height / 2.0F + var4 * theEntity.height
						/ 4.0D;
				final double var12 = theEntity.posZ + var6 * theEntity.width
						/ 4.0D;
				worldObj.spawnParticle(particleName, var8, var10, var12, var2,
						var4 + 0.2D, var6);
			}
		}

		++currentLife;

		if (currentLife >= maximumLife) {
			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
	}
}

package net.minecraft.client.particle;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class EntityPickupFX extends EntityFX {
	private int age;
	private final Entity entityPickingUp;
	private final Entity entityToPickUp;
	private final int maxAge;

	/** renamed from yOffset to fix shadowing Entity.yOffset */
	private final float yOffs;

	public EntityPickupFX(World p_i1233_1_, Entity p_i1233_2_,
			Entity p_i1233_3_, float p_i1233_4_) {
		super(p_i1233_1_, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ,
				p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
		entityToPickUp = p_i1233_2_;
		entityPickingUp = p_i1233_3_;
		maxAge = 3;
		yOffs = p_i1233_4_;
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
		++age;

		if (age == maxAge) {
			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		float var8 = (age + p_70539_2_) / maxAge;
		var8 *= var8;
		final double var9 = entityToPickUp.posX;
		final double var11 = entityToPickUp.posY;
		final double var13 = entityToPickUp.posZ;
		final double var15 = entityPickingUp.lastTickPosX
				+ (entityPickingUp.posX - entityPickingUp.lastTickPosX)
				* p_70539_2_;
		final double var17 = entityPickingUp.lastTickPosY
				+ (entityPickingUp.posY - entityPickingUp.lastTickPosY)
				* p_70539_2_ + yOffs;
		final double var19 = entityPickingUp.lastTickPosZ
				+ (entityPickingUp.posZ - entityPickingUp.lastTickPosZ)
				* p_70539_2_;
		double var21 = var9 + (var15 - var9) * var8;
		double var23 = var11 + (var17 - var11) * var8;
		double var25 = var13 + (var19 - var13) * var8;
		final int var27 = getBrightnessForRender(p_70539_2_);
		final int var28 = var27 % 65536;
		final int var29 = var27 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				var28 / 1.0F, var29 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var21 -= interpPosX;
		var23 -= interpPosY;
		var25 -= interpPosZ;
		RenderManager.instance.func_147940_a(entityToPickUp, (float) var21,
				(float) var23, (float) var25, entityToPickUp.rotationYaw,
				p_70539_2_);
	}
}

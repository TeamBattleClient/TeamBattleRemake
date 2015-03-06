package net.minecraft.client.particle;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class EntityFootStepFX extends EntityFX {
	private static final ResourceLocation field_110126_a = new ResourceLocation(
			"textures/particle/footprint.png");
	private final TextureManager currentFootSteps;
	private int footstepAge;
	private final int footstepMaxAge;

	public EntityFootStepFX(TextureManager p_i1210_1_, World p_i1210_2_,
			double p_i1210_3_, double p_i1210_5_, double p_i1210_7_) {
		super(p_i1210_2_, p_i1210_3_, p_i1210_5_, p_i1210_7_, 0.0D, 0.0D, 0.0D);
		currentFootSteps = p_i1210_1_;
		motionX = motionY = motionZ = 0.0D;
		footstepMaxAge = 200;
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
		++footstepAge;

		if (footstepAge == footstepMaxAge) {
			setDead();
		}
	}

	@Override
	public void renderParticle(Tessellator p_70539_1_, float p_70539_2_,
			float p_70539_3_, float p_70539_4_, float p_70539_5_,
			float p_70539_6_, float p_70539_7_) {
		float var8 = (footstepAge + p_70539_2_) / footstepMaxAge;
		var8 *= var8;
		float var9 = 2.0F - var8 * 2.0F;

		if (var9 > 1.0F) {
			var9 = 1.0F;
		}

		var9 *= 0.2F;
		GL11.glDisable(GL11.GL_LIGHTING);
		final float var10 = 0.125F;
		final float var11 = (float) (posX - interpPosX);
		final float var12 = (float) (posY - interpPosY);
		final float var13 = (float) (posZ - interpPosZ);
		final float var14 = worldObj.getLightBrightness(
				MathHelper.floor_double(posX), MathHelper.floor_double(posY),
				MathHelper.floor_double(posZ));
		currentFootSteps.bindTexture(field_110126_a);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		p_70539_1_.startDrawingQuads();
		p_70539_1_.setColorRGBA_F(var14, var14, var14, var9);
		p_70539_1_.addVertexWithUV(var11 - var10, var12, var13 + var10, 0.0D,
				1.0D);
		p_70539_1_.addVertexWithUV(var11 + var10, var12, var13 + var10, 1.0D,
				1.0D);
		p_70539_1_.addVertexWithUV(var11 + var10, var12, var13 - var10, 1.0D,
				0.0D);
		p_70539_1_.addVertexWithUV(var11 - var10, var12, var13 - var10, 0.0D,
				0.0D);
		p_70539_1_.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}

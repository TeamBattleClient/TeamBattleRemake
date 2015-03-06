package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderSlime extends RenderLiving {
	private static final ResourceLocation slimeTextures = new ResourceLocation(
			"textures/entity/slime/slime.png");
	private final ModelBase scaleAmount;

	public RenderSlime(ModelBase p_i1267_1_, ModelBase p_i1267_2_,
			float p_i1267_3_) {
		super(p_i1267_1_, p_i1267_3_);
		scaleAmount = p_i1267_2_;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntitySlime) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntitySlime p_110775_1_) {
		return slimeTextures;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_,
			float p_77041_2_) {
		this.preRenderCallback((EntitySlime) p_77041_1_, p_77041_2_);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntitySlime p_77041_1_, float p_77041_2_) {
		final float var3 = p_77041_1_.getSlimeSize();
		final float var4 = (p_77041_1_.prevSquishFactor + (p_77041_1_.squishFactor - p_77041_1_.prevSquishFactor)
				* p_77041_2_)
				/ (var3 * 0.5F + 1.0F);
		final float var5 = 1.0F / (var4 + 1.0F);
		GL11.glScalef(var5 * var3, 1.0F / var5 * var3, var5 * var3);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_,
			float p_77032_3_) {
		return this.shouldRenderPass((EntitySlime) p_77032_1_, p_77032_2_,
				p_77032_3_);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntitySlime p_77032_1_, int p_77032_2_,
			float p_77032_3_) {
		if (p_77032_1_.isInvisible())
			return 0;
		else if (p_77032_2_ == 0) {
			setRenderPassModel(scaleAmount);
			GL11.glEnable(GL11.GL_NORMALIZE);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			return 1;
		} else {
			if (p_77032_2_ == 1) {
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			return -1;
		}
	}
}

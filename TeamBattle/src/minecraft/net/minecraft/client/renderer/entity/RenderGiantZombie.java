package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderGiantZombie extends RenderLiving {
	private static final ResourceLocation zombieTextures = new ResourceLocation(
			"textures/entity/zombie/zombie.png");

	/** Scale of the model to use */
	private final float scale;

	public RenderGiantZombie(ModelBase p_i1255_1_, float p_i1255_2_,
			float p_i1255_3_) {
		super(p_i1255_1_, p_i1255_2_ * p_i1255_3_);
		scale = p_i1255_3_;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntityGiantZombie) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityGiantZombie p_110775_1_) {
		return zombieTextures;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityGiantZombie p_77041_1_,
			float p_77041_2_) {
		GL11.glScalef(scale, scale, scale);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_,
			float p_77041_2_) {
		this.preRenderCallback((EntityGiantZombie) p_77041_1_, p_77041_2_);
	}
}

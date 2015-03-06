package net.minecraft.client.model;

import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelEnderCrystal extends ModelBase {
	/** The base model for the Ender Crystal. */
	private ModelRenderer base;

	/** The cube model for the Ender Crystal. */
	private final ModelRenderer cube;

	/** The glass model for the Ender Crystal. */
	private final ModelRenderer glass = new ModelRenderer(this, "glass");

	public ModelEnderCrystal(float p_i1170_1_, boolean p_i1170_2_) {
		glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		cube = new ModelRenderer(this, "cube");
		cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);

		if (p_i1170_2_) {
			base = new ModelRenderer(this, "base");
			base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
		}
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_,
			float p_78088_4_, float p_78088_5_, float p_78088_6_,
			float p_78088_7_) {
		GL11.glPushMatrix();
		GL11.glScalef(2.0F, 2.0F, 2.0F);
		GL11.glTranslatef(0.0F, -0.5F, 0.0F);

		if (base != null) {
			base.render(p_78088_7_);
		}

		GL11.glRotatef(p_78088_3_, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.8F + p_78088_4_, 0.0F);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		glass.render(p_78088_7_);
		final float var8 = 0.875F;
		GL11.glScalef(var8, var8, var8);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		GL11.glRotatef(p_78088_3_, 0.0F, 1.0F, 0.0F);
		glass.render(p_78088_7_);
		GL11.glScalef(var8, var8, var8);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
		GL11.glRotatef(p_78088_3_, 0.0F, 1.0F, 0.0F);
		cube.render(p_78088_7_);
		GL11.glPopMatrix();
	}
}

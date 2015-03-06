package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class ModelQuadruped extends ModelBase {
	public ModelRenderer body;
	protected float field_78145_g = 8.0F;
	protected float field_78151_h = 4.0F;
	public ModelRenderer head = new ModelRenderer(this, 0, 0);
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;

	public ModelQuadruped(int p_i1154_1_, float p_i1154_2_) {
		head.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, p_i1154_2_);
		head.setRotationPoint(0.0F, 18 - p_i1154_1_, -6.0F);
		body = new ModelRenderer(this, 28, 8);
		body.addBox(-5.0F, -10.0F, -7.0F, 10, 16, 8, p_i1154_2_);
		body.setRotationPoint(0.0F, 17 - p_i1154_1_, 2.0F);
		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
		leg1.setRotationPoint(-3.0F, 24 - p_i1154_1_, 7.0F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
		leg2.setRotationPoint(3.0F, 24 - p_i1154_1_, 7.0F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
		leg3.setRotationPoint(-3.0F, 24 - p_i1154_1_, -5.0F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2.0F, 0.0F, -2.0F, 4, p_i1154_1_, 4, p_i1154_2_);
		leg4.setRotationPoint(3.0F, 24 - p_i1154_1_, -5.0F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_,
			float p_78088_4_, float p_78088_5_, float p_78088_6_,
			float p_78088_7_) {
		setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_,
				p_78088_6_, p_78088_7_, p_78088_1_);

		if (isChild) {
			final float var8 = 2.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, field_78145_g * p_78088_7_, field_78151_h
					* p_78088_7_);
			head.render(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			body.render(p_78088_7_);
			leg1.render(p_78088_7_);
			leg2.render(p_78088_7_);
			leg3.render(p_78088_7_);
			leg4.render(p_78088_7_);
			GL11.glPopMatrix();
		} else {
			head.render(p_78088_7_);
			body.render(p_78088_7_);
			leg1.render(p_78088_7_);
			leg2.render(p_78088_7_);
			leg3.render(p_78088_7_);
			leg4.render(p_78088_7_);
		}
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are
	 * used for animating the movement of arms and legs, where par1 represents
	 * the time(so that arms and legs swing back and forth) and par2 represents
	 * how "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_,
			float p_78087_3_, float p_78087_4_, float p_78087_5_,
			float p_78087_6_, Entity p_78087_7_) {
		head.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		head.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		body.rotateAngleX = (float) Math.PI / 2F;
		leg1.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F
				* p_78087_2_;
		leg2.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F
				+ (float) Math.PI)
				* 1.4F * p_78087_2_;
		leg3.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F
				+ (float) Math.PI)
				* 1.4F * p_78087_2_;
		leg4.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F
				* p_78087_2_;
	}
}

package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelCreeper extends ModelBase {
	public ModelRenderer body;
	public ModelRenderer field_78133_b;
	public ModelRenderer head;
	public ModelRenderer leg1;
	public ModelRenderer leg2;
	public ModelRenderer leg3;
	public ModelRenderer leg4;

	public ModelCreeper() {
		this(0.0F);
	}

	public ModelCreeper(float p_i1147_1_) {
		final byte var2 = 4;
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1147_1_);
		head.setRotationPoint(0.0F, var2, 0.0F);
		field_78133_b = new ModelRenderer(this, 32, 0);
		field_78133_b.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1147_1_ + 0.5F);
		field_78133_b.setRotationPoint(0.0F, var2, 0.0F);
		body = new ModelRenderer(this, 16, 16);
		body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i1147_1_);
		body.setRotationPoint(0.0F, var2, 0.0F);
		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
		leg1.setRotationPoint(-2.0F, 12 + var2, 4.0F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
		leg2.setRotationPoint(2.0F, 12 + var2, 4.0F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
		leg3.setRotationPoint(-2.0F, 12 + var2, -4.0F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, p_i1147_1_);
		leg4.setRotationPoint(2.0F, 12 + var2, -4.0F);
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
		head.render(p_78088_7_);
		body.render(p_78088_7_);
		leg1.render(p_78088_7_);
		leg2.render(p_78088_7_);
		leg3.render(p_78088_7_);
		leg4.render(p_78088_7_);
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
		head.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		head.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
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

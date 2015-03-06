package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSnowMan extends ModelBase {
	public ModelRenderer body;
	public ModelRenderer bottomBody;
	public ModelRenderer head;
	public ModelRenderer leftHand;
	public ModelRenderer rightHand;

	public ModelSnowMan() {
		final float var1 = 4.0F;
		final float var2 = 0.0F;
		head = new ModelRenderer(this, 0, 0).setTextureSize(64, 64);
		head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, var2 - 0.5F);
		head.setRotationPoint(0.0F, 0.0F + var1, 0.0F);
		rightHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
		rightHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, var2 - 0.5F);
		rightHand.setRotationPoint(0.0F, 0.0F + var1 + 9.0F - 7.0F, 0.0F);
		leftHand = new ModelRenderer(this, 32, 0).setTextureSize(64, 64);
		leftHand.addBox(-1.0F, 0.0F, -1.0F, 12, 2, 2, var2 - 0.5F);
		leftHand.setRotationPoint(0.0F, 0.0F + var1 + 9.0F - 7.0F, 0.0F);
		body = new ModelRenderer(this, 0, 16).setTextureSize(64, 64);
		body.addBox(-5.0F, -10.0F, -5.0F, 10, 10, 10, var2 - 0.5F);
		body.setRotationPoint(0.0F, 0.0F + var1 + 9.0F, 0.0F);
		bottomBody = new ModelRenderer(this, 0, 36).setTextureSize(64, 64);
		bottomBody.addBox(-6.0F, -12.0F, -6.0F, 12, 12, 12, var2 - 0.5F);
		bottomBody.setRotationPoint(0.0F, 0.0F + var1 + 20.0F, 0.0F);
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
		body.render(p_78088_7_);
		bottomBody.render(p_78088_7_);
		head.render(p_78088_7_);
		rightHand.render(p_78088_7_);
		leftHand.render(p_78088_7_);
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
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_,
				p_78087_5_, p_78087_6_, p_78087_7_);
		head.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		head.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		body.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI) * 0.25F;
		final float var8 = MathHelper.sin(body.rotateAngleY);
		final float var9 = MathHelper.cos(body.rotateAngleY);
		rightHand.rotateAngleZ = 1.0F;
		leftHand.rotateAngleZ = -1.0F;
		rightHand.rotateAngleY = 0.0F + body.rotateAngleY;
		leftHand.rotateAngleY = (float) Math.PI + body.rotateAngleY;
		rightHand.rotationPointX = var9 * 5.0F;
		rightHand.rotationPointZ = -var8 * 5.0F;
		leftHand.rotationPointX = -var9 * 5.0F;
		leftHand.rotationPointZ = var8 * 5.0F;
	}
}

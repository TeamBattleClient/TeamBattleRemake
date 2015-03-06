package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSpider extends ModelBase {
	/** The spider's body box */
	public ModelRenderer spiderBody;

	/** The spider's head box */
	public ModelRenderer spiderHead;

	/** Spider's first leg */
	public ModelRenderer spiderLeg1;

	/** Spider's second leg */
	public ModelRenderer spiderLeg2;

	/** Spider's third leg */
	public ModelRenderer spiderLeg3;

	/** Spider's fourth leg */
	public ModelRenderer spiderLeg4;

	/** Spider's fifth leg */
	public ModelRenderer spiderLeg5;

	/** Spider's sixth leg */
	public ModelRenderer spiderLeg6;

	/** Spider's seventh leg */
	public ModelRenderer spiderLeg7;

	/** Spider's eight leg */
	public ModelRenderer spiderLeg8;

	/** The spider's neck box */
	public ModelRenderer spiderNeck;

	public ModelSpider() {
		final float var1 = 0.0F;
		final byte var2 = 15;
		spiderHead = new ModelRenderer(this, 32, 4);
		spiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, var1);
		spiderHead.setRotationPoint(0.0F, var2, -3.0F);
		spiderNeck = new ModelRenderer(this, 0, 0);
		spiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, var1);
		spiderNeck.setRotationPoint(0.0F, var2, 0.0F);
		spiderBody = new ModelRenderer(this, 0, 12);
		spiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, var1);
		spiderBody.setRotationPoint(0.0F, var2, 9.0F);
		spiderLeg1 = new ModelRenderer(this, 18, 0);
		spiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg1.setRotationPoint(-4.0F, var2, 2.0F);
		spiderLeg2 = new ModelRenderer(this, 18, 0);
		spiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg2.setRotationPoint(4.0F, var2, 2.0F);
		spiderLeg3 = new ModelRenderer(this, 18, 0);
		spiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg3.setRotationPoint(-4.0F, var2, 1.0F);
		spiderLeg4 = new ModelRenderer(this, 18, 0);
		spiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg4.setRotationPoint(4.0F, var2, 1.0F);
		spiderLeg5 = new ModelRenderer(this, 18, 0);
		spiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg5.setRotationPoint(-4.0F, var2, 0.0F);
		spiderLeg6 = new ModelRenderer(this, 18, 0);
		spiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg6.setRotationPoint(4.0F, var2, 0.0F);
		spiderLeg7 = new ModelRenderer(this, 18, 0);
		spiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg7.setRotationPoint(-4.0F, var2, -1.0F);
		spiderLeg8 = new ModelRenderer(this, 18, 0);
		spiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, var1);
		spiderLeg8.setRotationPoint(4.0F, var2, -1.0F);
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
		spiderHead.render(p_78088_7_);
		spiderNeck.render(p_78088_7_);
		spiderBody.render(p_78088_7_);
		spiderLeg1.render(p_78088_7_);
		spiderLeg2.render(p_78088_7_);
		spiderLeg3.render(p_78088_7_);
		spiderLeg4.render(p_78088_7_);
		spiderLeg5.render(p_78088_7_);
		spiderLeg6.render(p_78088_7_);
		spiderLeg7.render(p_78088_7_);
		spiderLeg8.render(p_78088_7_);
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
		spiderHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		spiderHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		final float var8 = (float) Math.PI / 4F;
		spiderLeg1.rotateAngleZ = -var8;
		spiderLeg2.rotateAngleZ = var8;
		spiderLeg3.rotateAngleZ = -var8 * 0.74F;
		spiderLeg4.rotateAngleZ = var8 * 0.74F;
		spiderLeg5.rotateAngleZ = -var8 * 0.74F;
		spiderLeg6.rotateAngleZ = var8 * 0.74F;
		spiderLeg7.rotateAngleZ = -var8;
		spiderLeg8.rotateAngleZ = var8;
		final float var9 = -0.0F;
		final float var10 = 0.3926991F;
		spiderLeg1.rotateAngleY = var10 * 2.0F + var9;
		spiderLeg2.rotateAngleY = -var10 * 2.0F - var9;
		spiderLeg3.rotateAngleY = var10 * 1.0F + var9;
		spiderLeg4.rotateAngleY = -var10 * 1.0F - var9;
		spiderLeg5.rotateAngleY = -var10 * 1.0F + var9;
		spiderLeg6.rotateAngleY = var10 * 1.0F - var9;
		spiderLeg7.rotateAngleY = -var10 * 2.0F + var9;
		spiderLeg8.rotateAngleY = var10 * 2.0F - var9;
		final float var11 = -(MathHelper
				.cos(p_78087_1_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_78087_2_;
		final float var12 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F
				+ (float) Math.PI) * 0.4F)
				* p_78087_2_;
		final float var13 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F
				+ (float) Math.PI / 2F) * 0.4F)
				* p_78087_2_;
		final float var14 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F
				+ (float) Math.PI * 3F / 2F) * 0.4F)
				* p_78087_2_;
		final float var15 = Math.abs(MathHelper
				.sin(p_78087_1_ * 0.6662F + 0.0F) * 0.4F) * p_78087_2_;
		final float var16 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F
				+ (float) Math.PI) * 0.4F)
				* p_78087_2_;
		final float var17 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F
				+ (float) Math.PI / 2F) * 0.4F)
				* p_78087_2_;
		final float var18 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F
				+ (float) Math.PI * 3F / 2F) * 0.4F)
				* p_78087_2_;
		spiderLeg1.rotateAngleY += var11;
		spiderLeg2.rotateAngleY += -var11;
		spiderLeg3.rotateAngleY += var12;
		spiderLeg4.rotateAngleY += -var12;
		spiderLeg5.rotateAngleY += var13;
		spiderLeg6.rotateAngleY += -var13;
		spiderLeg7.rotateAngleY += var14;
		spiderLeg8.rotateAngleY += -var14;
		spiderLeg1.rotateAngleZ += var15;
		spiderLeg2.rotateAngleZ += -var15;
		spiderLeg3.rotateAngleZ += var16;
		spiderLeg4.rotateAngleZ += -var16;
		spiderLeg5.rotateAngleZ += var17;
		spiderLeg6.rotateAngleZ += -var17;
		spiderLeg7.rotateAngleZ += var18;
		spiderLeg8.rotateAngleZ += -var18;
	}
}

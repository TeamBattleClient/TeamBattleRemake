package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class ModelChicken extends ModelBase {
	public ModelRenderer bill;
	public ModelRenderer body;
	public ModelRenderer chin;
	public ModelRenderer head;
	public ModelRenderer leftLeg;
	public ModelRenderer leftWing;
	public ModelRenderer rightLeg;
	public ModelRenderer rightWing;

	public ModelChicken() {
		final byte var1 = 16;
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2.0F, -6.0F, -2.0F, 4, 6, 3, 0.0F);
		head.setRotationPoint(0.0F, -1 + var1, -4.0F);
		bill = new ModelRenderer(this, 14, 0);
		bill.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
		bill.setRotationPoint(0.0F, -1 + var1, -4.0F);
		chin = new ModelRenderer(this, 14, 4);
		chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
		chin.setRotationPoint(0.0F, -1 + var1, -4.0F);
		body = new ModelRenderer(this, 0, 9);
		body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
		body.setRotationPoint(0.0F, var1, 0.0F);
		rightLeg = new ModelRenderer(this, 26, 0);
		rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
		rightLeg.setRotationPoint(-2.0F, 3 + var1, 1.0F);
		leftLeg = new ModelRenderer(this, 26, 0);
		leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3);
		leftLeg.setRotationPoint(1.0F, 3 + var1, 1.0F);
		rightWing = new ModelRenderer(this, 24, 13);
		rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6);
		rightWing.setRotationPoint(-4.0F, -3 + var1, 0.0F);
		leftWing = new ModelRenderer(this, 24, 13);
		leftWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6);
		leftWing.setRotationPoint(4.0F, -3 + var1, 0.0F);
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
			GL11.glTranslatef(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
			head.render(p_78088_7_);
			bill.render(p_78088_7_);
			chin.render(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			body.render(p_78088_7_);
			rightLeg.render(p_78088_7_);
			leftLeg.render(p_78088_7_);
			rightWing.render(p_78088_7_);
			leftWing.render(p_78088_7_);
			GL11.glPopMatrix();
		} else {
			head.render(p_78088_7_);
			bill.render(p_78088_7_);
			chin.render(p_78088_7_);
			body.render(p_78088_7_);
			rightLeg.render(p_78088_7_);
			leftLeg.render(p_78088_7_);
			rightWing.render(p_78088_7_);
			leftWing.render(p_78088_7_);
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
		bill.rotateAngleX = head.rotateAngleX;
		bill.rotateAngleY = head.rotateAngleY;
		chin.rotateAngleX = head.rotateAngleX;
		chin.rotateAngleY = head.rotateAngleY;
		body.rotateAngleX = (float) Math.PI / 2F;
		rightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F
				* p_78087_2_;
		leftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F
				+ (float) Math.PI)
				* 1.4F * p_78087_2_;
		rightWing.rotateAngleZ = p_78087_3_;
		leftWing.rotateAngleZ = -p_78087_3_;
	}
}

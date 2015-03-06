package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

public class ModelBiped extends ModelBase {
	/** Records whether the model should be rendered aiming a bow. */
	public boolean aimedBow;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedCloak;
	public ModelRenderer bipedEars;
	public ModelRenderer bipedHead;
	public ModelRenderer bipedHeadwear;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedLeftLeg;
	public ModelRenderer bipedRightArm;

	public ModelRenderer bipedRightLeg;

	/**
	 * Records whether the model should be rendered holding an item in the left
	 * hand, and if that item is a block.
	 */
	public int heldItemLeft;
	/**
	 * Records whether the model should be rendered holding an item in the right
	 * hand, and if that item is a block.
	 */
	public int heldItemRight;

	public boolean isSneak;

	public ModelBiped() {
		this(0.0F);
	}

	public ModelBiped(float p_i1148_1_) {
		this(p_i1148_1_, 0.0F, 64, 32);
	}

	public ModelBiped(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_,
			int p_i1149_4_) {
		textureWidth = p_i1149_3_;
		textureHeight = p_i1149_4_;
		bipedCloak = new ModelRenderer(this, 0, 0);
		bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i1149_1_);
		bipedEars = new ModelRenderer(this, 24, 0);
		bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i1149_1_);
		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_);
		bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_ + 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		bipedBody = new ModelRenderer(this, 16, 16);
		bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i1149_1_);
		bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
		bipedRightArm = new ModelRenderer(this, 40, 16);
		bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
		bipedLeftArm = new ModelRenderer(this, 40, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i1149_1_);
		bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
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
			GL11.glScalef(1.5F / var8, 1.5F / var8, 1.5F / var8);
			GL11.glTranslatef(0.0F, 16.0F * p_78088_7_, 0.0F);
			bipedHead.render(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / var8, 1.0F / var8, 1.0F / var8);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			bipedBody.render(p_78088_7_);
			bipedRightArm.render(p_78088_7_);
			bipedLeftArm.render(p_78088_7_);
			bipedRightLeg.render(p_78088_7_);
			bipedLeftLeg.render(p_78088_7_);
			bipedHeadwear.render(p_78088_7_);
			GL11.glPopMatrix();
		} else {
			bipedHead.render(p_78088_7_);
			bipedBody.render(p_78088_7_);
			bipedRightArm.render(p_78088_7_);
			bipedLeftArm.render(p_78088_7_);
			bipedRightLeg.render(p_78088_7_);
			bipedLeftLeg.render(p_78088_7_);
			bipedHeadwear.render(p_78088_7_);
		}
	}

	/**
	 * Renders the cloak of the current biped (in most cases, it's a player)
	 */
	public void renderCloak(float p_78111_1_) {
		bipedCloak.render(p_78111_1_);
	}

	/**
	 * renders the ears (specifically, deadmau5's)
	 */
	public void renderEars(float p_78110_1_) {
		bipedEars.rotateAngleY = bipedHead.rotateAngleY;
		bipedEars.rotateAngleX = bipedHead.rotateAngleX;
		bipedEars.rotationPointX = 0.0F;
		bipedEars.rotationPointY = 0.0F;
		bipedEars.render(p_78110_1_);
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
		bipedHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		bipedHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
		bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
		bipedRightArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F
				+ (float) Math.PI)
				* 2.0F * p_78087_2_ * 0.5F;
		bipedLeftArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F
				* p_78087_2_ * 0.5F;
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F)
				* 1.4F * p_78087_2_;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F
				+ (float) Math.PI)
				* 1.4F * p_78087_2_;
		bipedRightLeg.rotateAngleY = 0.0F;
		bipedLeftLeg.rotateAngleY = 0.0F;

		if (isRiding) {
			bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
			bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
			bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
			bipedRightLeg.rotateAngleY = (float) Math.PI / 10F;
			bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
		}

		if (heldItemLeft != 0) {
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F
					- (float) Math.PI / 10F * heldItemLeft;
		}

		if (heldItemRight != 0) {
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F
					- (float) Math.PI / 10F * heldItemRight;
		}

		bipedRightArm.rotateAngleY = 0.0F;
		bipedLeftArm.rotateAngleY = 0.0F;
		float var8;
		float var9;

		if (onGround > -9990.0F) {
			var8 = onGround;
			bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8)
					* (float) Math.PI * 2.0F) * 0.2F;
			bipedRightArm.rotationPointZ = MathHelper
					.sin(bipedBody.rotateAngleY) * 5.0F;
			bipedRightArm.rotationPointX = -MathHelper
					.cos(bipedBody.rotateAngleY) * 5.0F;
			bipedLeftArm.rotationPointZ = -MathHelper
					.sin(bipedBody.rotateAngleY) * 5.0F;
			bipedLeftArm.rotationPointX = MathHelper
					.cos(bipedBody.rotateAngleY) * 5.0F;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
			var8 = 1.0F - onGround;
			var8 *= var8;
			var8 *= var8;
			var8 = 1.0F - var8;
			var9 = MathHelper.sin(var8 * (float) Math.PI);
			final float var10 = MathHelper.sin(onGround * (float) Math.PI)
					* -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
			bipedRightArm.rotateAngleX = (float) (bipedRightArm.rotateAngleX - (var9 * 1.2D + var10));
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
			bipedRightArm.rotateAngleZ = MathHelper.sin(onGround
					* (float) Math.PI)
					* -0.4F;
		}

		if (isSneak) {
			bipedBody.rotateAngleX = 0.5F;
			bipedRightArm.rotateAngleX += 0.4F;
			bipedLeftArm.rotateAngleX += 0.4F;
			bipedRightLeg.rotationPointZ = 4.0F;
			bipedLeftLeg.rotationPointZ = 4.0F;
			bipedRightLeg.rotationPointY = 9.0F;
			bipedLeftLeg.rotationPointY = 9.0F;
			bipedHead.rotationPointY = 1.0F;
			bipedHeadwear.rotationPointY = 1.0F;
		} else {
			bipedBody.rotateAngleX = 0.0F;
			bipedRightLeg.rotationPointZ = 0.1F;
			bipedLeftLeg.rotationPointZ = 0.1F;
			bipedRightLeg.rotationPointY = 12.0F;
			bipedLeftLeg.rotationPointY = 12.0F;
			bipedHead.rotationPointY = 0.0F;
			bipedHeadwear.rotationPointY = 0.0F;
		}

		bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;

		if (aimedBow) {
			var8 = 0.0F;
			var9 = 0.0F;
			bipedRightArm.rotateAngleZ = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.0F;
			bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F)
					+ bipedHead.rotateAngleY;
			bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F
					+ bipedHead.rotateAngleY + 0.4F;
			bipedRightArm.rotateAngleX = -((float) Math.PI / 2F)
					+ bipedHead.rotateAngleX;
			bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F)
					+ bipedHead.rotateAngleX;
			bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
			bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
			bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
			bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
			bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
			bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
		}
	}
}

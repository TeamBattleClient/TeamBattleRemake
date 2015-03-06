package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;

public class ModelIronGolem extends ModelBase {
	/** The body model for the iron golem. */
	public ModelRenderer ironGolemBody;

	/** The head model for the iron golem. */
	public ModelRenderer ironGolemHead;

	/** The left arm model for the iron golem. */
	public ModelRenderer ironGolemLeftArm;

	/** The left leg model for the Iron Golem. */
	public ModelRenderer ironGolemLeftLeg;

	/** The right arm model for the iron golem. */
	public ModelRenderer ironGolemRightArm;

	/** The right leg model for the Iron Golem. */
	public ModelRenderer ironGolemRightLeg;

	public ModelIronGolem() {
		this(0.0F);
	}

	public ModelIronGolem(float p_i1161_1_) {
		this(p_i1161_1_, -7.0F);
	}

	public ModelIronGolem(float p_i1162_1_, float p_i1162_2_) {
		final short var3 = 128;
		final short var4 = 128;
		ironGolemHead = new ModelRenderer(this).setTextureSize(var3, var4);
		ironGolemHead.setRotationPoint(0.0F, 0.0F + p_i1162_2_, -2.0F);
		ironGolemHead.setTextureOffset(0, 0).addBox(-4.0F, -12.0F, -5.5F, 8,
				10, 8, p_i1162_1_);
		ironGolemHead.setTextureOffset(24, 0).addBox(-1.0F, -5.0F, -7.5F, 2, 4,
				2, p_i1162_1_);
		ironGolemBody = new ModelRenderer(this).setTextureSize(var3, var4);
		ironGolemBody.setRotationPoint(0.0F, 0.0F + p_i1162_2_, 0.0F);
		ironGolemBody.setTextureOffset(0, 40).addBox(-9.0F, -2.0F, -6.0F, 18,
				12, 11, p_i1162_1_);
		ironGolemBody.setTextureOffset(0, 70).addBox(-4.5F, 10.0F, -3.0F, 9, 5,
				6, p_i1162_1_ + 0.5F);
		ironGolemRightArm = new ModelRenderer(this).setTextureSize(var3, var4);
		ironGolemRightArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		ironGolemRightArm.setTextureOffset(60, 21).addBox(-13.0F, -2.5F, -3.0F,
				4, 30, 6, p_i1162_1_);
		ironGolemLeftArm = new ModelRenderer(this).setTextureSize(var3, var4);
		ironGolemLeftArm.setRotationPoint(0.0F, -7.0F, 0.0F);
		ironGolemLeftArm.setTextureOffset(60, 58).addBox(9.0F, -2.5F, -3.0F, 4,
				30, 6, p_i1162_1_);
		ironGolemLeftLeg = new ModelRenderer(this, 0, 22).setTextureSize(var3,
				var4);
		ironGolemLeftLeg.setRotationPoint(-4.0F, 18.0F + p_i1162_2_, 0.0F);
		ironGolemLeftLeg.setTextureOffset(37, 0).addBox(-3.5F, -3.0F, -3.0F, 6,
				16, 5, p_i1162_1_);
		ironGolemRightLeg = new ModelRenderer(this, 0, 22).setTextureSize(var3,
				var4);
		ironGolemRightLeg.mirror = true;
		ironGolemRightLeg.setTextureOffset(60, 0).setRotationPoint(5.0F,
				18.0F + p_i1162_2_, 0.0F);
		ironGolemRightLeg.addBox(-3.5F, -3.0F, -3.0F, 6, 16, 5, p_i1162_1_);
	}

	private float func_78172_a(float p_78172_1_, float p_78172_2_) {
		return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F)
				/ (p_78172_2_ * 0.25F);
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
		ironGolemHead.render(p_78088_7_);
		ironGolemBody.render(p_78088_7_);
		ironGolemLeftLeg.render(p_78088_7_);
		ironGolemRightLeg.render(p_78088_7_);
		ironGolemRightArm.render(p_78088_7_);
		ironGolemLeftArm.render(p_78088_7_);
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the
	 * setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_,
			float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		final EntityIronGolem var5 = (EntityIronGolem) p_78086_1_;
		final int var6 = var5.getAttackTimer();

		if (var6 > 0) {
			ironGolemRightArm.rotateAngleX = -2.0F + 1.5F
					* func_78172_a(var6 - p_78086_4_, 10.0F);
			ironGolemLeftArm.rotateAngleX = -2.0F + 1.5F
					* func_78172_a(var6 - p_78086_4_, 10.0F);
		} else {
			final int var7 = var5.getHoldRoseTick();

			if (var7 > 0) {
				ironGolemRightArm.rotateAngleX = -0.8F + 0.025F
						* func_78172_a(var7, 70.0F);
				ironGolemLeftArm.rotateAngleX = 0.0F;
			} else {
				ironGolemRightArm.rotateAngleX = (-0.2F + 1.5F * func_78172_a(
						p_78086_2_, 13.0F)) * p_78086_3_;
				ironGolemLeftArm.rotateAngleX = (-0.2F - 1.5F * func_78172_a(
						p_78086_2_, 13.0F)) * p_78086_3_;
			}
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
		ironGolemHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		ironGolemHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		ironGolemLeftLeg.rotateAngleX = -1.5F * func_78172_a(p_78087_1_, 13.0F)
				* p_78087_2_;
		ironGolemRightLeg.rotateAngleX = 1.5F * func_78172_a(p_78087_1_, 13.0F)
				* p_78087_2_;
		ironGolemLeftLeg.rotateAngleY = 0.0F;
		ironGolemRightLeg.rotateAngleY = 0.0F;
	}
}

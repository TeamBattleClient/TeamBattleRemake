package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;

public class ModelSheep1 extends ModelQuadruped {
	private float field_78152_i;

	public ModelSheep1() {
		super(12, 0.0F);
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F);
		head.setRotationPoint(0.0F, 6.0F, -8.0F);
		body = new ModelRenderer(this, 28, 8);
		body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 1.75F);
		body.setRotationPoint(0.0F, 5.0F, 2.0F);
		final float var1 = 0.5F;
		leg1 = new ModelRenderer(this, 0, 16);
		leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
		leg1.setRotationPoint(-3.0F, 12.0F, 7.0F);
		leg2 = new ModelRenderer(this, 0, 16);
		leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
		leg2.setRotationPoint(3.0F, 12.0F, 7.0F);
		leg3 = new ModelRenderer(this, 0, 16);
		leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
		leg3.setRotationPoint(-3.0F, 12.0F, -5.0F);
		leg4 = new ModelRenderer(this, 0, 16);
		leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, var1);
		leg4.setRotationPoint(3.0F, 12.0F, -5.0F);
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the
	 * setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_,
			float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_,
				p_78086_4_);
		head.rotationPointY = 6.0F + ((EntitySheep) p_78086_1_)
				.func_70894_j(p_78086_4_) * 9.0F;
		field_78152_i = ((EntitySheep) p_78086_1_).func_70890_k(p_78086_4_);
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
		head.rotateAngleX = field_78152_i;
	}
}

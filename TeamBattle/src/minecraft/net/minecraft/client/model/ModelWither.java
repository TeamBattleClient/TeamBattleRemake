package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;

public class ModelWither extends ModelBase {
	private final ModelRenderer[] field_82904_b;
	private final ModelRenderer[] field_82905_a;

	public ModelWither() {
		textureWidth = 64;
		textureHeight = 64;
		field_82905_a = new ModelRenderer[3];
		field_82905_a[0] = new ModelRenderer(this, 0, 16);
		field_82905_a[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3);
		field_82905_a[1] = new ModelRenderer(this).setTextureSize(textureWidth,
				textureHeight);
		field_82905_a[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
		field_82905_a[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3,
				10, 3);
		field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11,
				2, 2);
		field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11,
				2, 2);
		field_82905_a[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11,
				2, 2);
		field_82905_a[2] = new ModelRenderer(this, 12, 22);
		field_82905_a[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3);
		field_82904_b = new ModelRenderer[3];
		field_82904_b[0] = new ModelRenderer(this, 0, 0);
		field_82904_b[0].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
		field_82904_b[1] = new ModelRenderer(this, 32, 0);
		field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6);
		field_82904_b[1].rotationPointX = -8.0F;
		field_82904_b[1].rotationPointY = 4.0F;
		field_82904_b[2] = new ModelRenderer(this, 32, 0);
		field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6);
		field_82904_b[2].rotationPointX = 10.0F;
		field_82904_b[2].rotationPointY = 4.0F;
	}

	public int func_82903_a() {
		return 32;
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
		ModelRenderer[] var8 = field_82904_b;
		int var9 = var8.length;
		int var10;
		ModelRenderer var11;

		for (var10 = 0; var10 < var9; ++var10) {
			var11 = var8[var10];
			var11.render(p_78088_7_);
		}

		var8 = field_82905_a;
		var9 = var8.length;

		for (var10 = 0; var10 < var9; ++var10) {
			var11 = var8[var10];
			var11.render(p_78088_7_);
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the
	 * setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_,
			float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		final EntityWither var5 = (EntityWither) p_78086_1_;

		for (int var6 = 1; var6 < 3; ++var6) {
			field_82904_b[var6].rotateAngleY = (var5.func_82207_a(var6 - 1) - p_78086_1_.renderYawOffset)
					/ (180F / (float) Math.PI);
			field_82904_b[var6].rotateAngleX = var5.func_82210_r(var6 - 1)
					/ (180F / (float) Math.PI);
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
		final float var8 = MathHelper.cos(p_78087_3_ * 0.1F);
		field_82905_a[1].rotateAngleX = (0.065F + 0.05F * var8)
				* (float) Math.PI;
		field_82905_a[2].setRotationPoint(-2.0F,
				6.9F + MathHelper.cos(field_82905_a[1].rotateAngleX) * 10.0F,
				-0.5F + MathHelper.sin(field_82905_a[1].rotateAngleX) * 10.0F);
		field_82905_a[2].rotateAngleX = (0.265F + 0.1F * var8)
				* (float) Math.PI;
		field_82904_b[0].rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		field_82904_b[0].rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
	}
}

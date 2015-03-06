package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.MathHelper;

public class ModelBat extends ModelBase {
	/** The body box of the bat model. */
	private final ModelRenderer batBody;

	private final ModelRenderer batHead;

	/** The inner left wing box of the bat model. */
	private final ModelRenderer batLeftWing;

	/** The outer left wing box of the bat model. */
	private final ModelRenderer batOuterLeftWing;

	/** The outer right wing box of the bat model. */
	private final ModelRenderer batOuterRightWing;

	/** The inner right wing box of the bat model. */
	private final ModelRenderer batRightWing;

	public ModelBat() {
		textureWidth = 64;
		textureHeight = 64;
		batHead = new ModelRenderer(this, 0, 0);
		batHead.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6);
		final ModelRenderer var1 = new ModelRenderer(this, 24, 0);
		var1.addBox(-4.0F, -6.0F, -2.0F, 3, 4, 1);
		batHead.addChild(var1);
		final ModelRenderer var2 = new ModelRenderer(this, 24, 0);
		var2.mirror = true;
		var2.addBox(1.0F, -6.0F, -2.0F, 3, 4, 1);
		batHead.addChild(var2);
		batBody = new ModelRenderer(this, 0, 16);
		batBody.addBox(-3.0F, 4.0F, -3.0F, 6, 12, 6);
		batBody.setTextureOffset(0, 34).addBox(-5.0F, 16.0F, 0.0F, 10, 6, 1);
		batRightWing = new ModelRenderer(this, 42, 0);
		batRightWing.addBox(-12.0F, 1.0F, 1.5F, 10, 16, 1);
		batOuterRightWing = new ModelRenderer(this, 24, 16);
		batOuterRightWing.setRotationPoint(-12.0F, 1.0F, 1.5F);
		batOuterRightWing.addBox(-8.0F, 1.0F, 0.0F, 8, 12, 1);
		batLeftWing = new ModelRenderer(this, 42, 0);
		batLeftWing.mirror = true;
		batLeftWing.addBox(2.0F, 1.0F, 1.5F, 10, 16, 1);
		batOuterLeftWing = new ModelRenderer(this, 24, 16);
		batOuterLeftWing.mirror = true;
		batOuterLeftWing.setRotationPoint(12.0F, 1.0F, 1.5F);
		batOuterLeftWing.addBox(0.0F, 1.0F, 0.0F, 8, 12, 1);
		batBody.addChild(batRightWing);
		batBody.addChild(batLeftWing);
		batRightWing.addChild(batOuterRightWing);
		batLeftWing.addChild(batOuterLeftWing);
	}

	/**
	 * not actually sure this is size, is not used as of now, but the model
	 * would be recreated if the value changed and it seems a good match for a
	 * bats size
	 */
	public int getBatSize() {
		return 36;
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_,
			float p_78088_4_, float p_78088_5_, float p_78088_6_,
			float p_78088_7_) {
		final EntityBat var8 = (EntityBat) p_78088_1_;
		if (var8.getIsBatHanging()) {
			batHead.rotateAngleX = p_78088_6_ / (180F / (float) Math.PI);
			batHead.rotateAngleY = (float) Math.PI - p_78088_5_
					/ (180F / (float) Math.PI);
			batHead.rotateAngleZ = (float) Math.PI;
			batHead.setRotationPoint(0.0F, -2.0F, 0.0F);
			batRightWing.setRotationPoint(-3.0F, 0.0F, 3.0F);
			batLeftWing.setRotationPoint(3.0F, 0.0F, 3.0F);
			batBody.rotateAngleX = (float) Math.PI;
			batRightWing.rotateAngleX = -0.15707964F;
			batRightWing.rotateAngleY = -((float) Math.PI * 2F / 5F);
			batOuterRightWing.rotateAngleY = -1.7278761F;
			batLeftWing.rotateAngleX = batRightWing.rotateAngleX;
			batLeftWing.rotateAngleY = -batRightWing.rotateAngleY;
			batOuterLeftWing.rotateAngleY = -batOuterRightWing.rotateAngleY;
		} else {
			batHead.rotateAngleX = p_78088_6_ / (180F / (float) Math.PI);
			batHead.rotateAngleY = p_78088_5_ / (180F / (float) Math.PI);
			batHead.rotateAngleZ = 0.0F;
			batHead.setRotationPoint(0.0F, 0.0F, 0.0F);
			batRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
			batLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
			batBody.rotateAngleX = (float) Math.PI / 4F
					+ MathHelper.cos(p_78088_4_ * 0.1F) * 0.15F;
			batBody.rotateAngleY = 0.0F;
			batRightWing.rotateAngleY = MathHelper.cos(p_78088_4_ * 1.3F)
					* (float) Math.PI * 0.25F;
			batLeftWing.rotateAngleY = -batRightWing.rotateAngleY;
			batOuterRightWing.rotateAngleY = batRightWing.rotateAngleY * 0.5F;
			batOuterLeftWing.rotateAngleY = -batRightWing.rotateAngleY * 0.5F;
		}

		batHead.render(p_78088_7_);
		batBody.render(p_78088_7_);
	}
}

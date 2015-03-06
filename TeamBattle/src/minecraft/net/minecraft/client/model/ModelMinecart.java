package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelMinecart extends ModelBase {
	public ModelRenderer[] sideModels = new ModelRenderer[7];

	public ModelMinecart() {
		sideModels[0] = new ModelRenderer(this, 0, 10);
		sideModels[1] = new ModelRenderer(this, 0, 0);
		sideModels[2] = new ModelRenderer(this, 0, 0);
		sideModels[3] = new ModelRenderer(this, 0, 0);
		sideModels[4] = new ModelRenderer(this, 0, 0);
		sideModels[5] = new ModelRenderer(this, 44, 10);
		final byte var1 = 20;
		final byte var2 = 8;
		final byte var3 = 16;
		final byte var4 = 4;
		sideModels[0].addBox(-var1 / 2, -var3 / 2, -1.0F, var1, var3, 2, 0.0F);
		sideModels[0].setRotationPoint(0.0F, var4, 0.0F);
		sideModels[5].addBox(-var1 / 2 + 1, -var3 / 2 + 1, -1.0F, var1 - 2,
				var3 - 2, 1, 0.0F);
		sideModels[5].setRotationPoint(0.0F, var4, 0.0F);
		sideModels[1].addBox(-var1 / 2 + 2, -var2 - 1, -1.0F, var1 - 4, var2,
				2, 0.0F);
		sideModels[1].setRotationPoint(-var1 / 2 + 1, var4, 0.0F);
		sideModels[2].addBox(-var1 / 2 + 2, -var2 - 1, -1.0F, var1 - 4, var2,
				2, 0.0F);
		sideModels[2].setRotationPoint(var1 / 2 - 1, var4, 0.0F);
		sideModels[3].addBox(-var1 / 2 + 2, -var2 - 1, -1.0F, var1 - 4, var2,
				2, 0.0F);
		sideModels[3].setRotationPoint(0.0F, var4, -var3 / 2 + 1);
		sideModels[4].addBox(-var1 / 2 + 2, -var2 - 1, -1.0F, var1 - 4, var2,
				2, 0.0F);
		sideModels[4].setRotationPoint(0.0F, var4, var3 / 2 - 1);
		sideModels[0].rotateAngleX = (float) Math.PI / 2F;
		sideModels[1].rotateAngleY = (float) Math.PI * 3F / 2F;
		sideModels[2].rotateAngleY = (float) Math.PI / 2F;
		sideModels[3].rotateAngleY = (float) Math.PI;
		sideModels[5].rotateAngleX = -((float) Math.PI / 2F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_,
			float p_78088_4_, float p_78088_5_, float p_78088_6_,
			float p_78088_7_) {
		sideModels[5].rotationPointY = 4.0F - p_78088_4_;

		for (int var8 = 0; var8 < 6; ++var8) {
			sideModels[var8].render(p_78088_7_);
		}
	}
}

package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderSnowMan extends RenderLiving {
	private static final ResourceLocation snowManTextures = new ResourceLocation(
			"textures/entity/snowman.png");

	/** A reference to the Snowman model in RenderSnowMan. */
	private final ModelSnowMan snowmanModel;

	public RenderSnowMan() {
		super(new ModelSnowMan(), 0.5F);
		snowmanModel = (ModelSnowMan) super.mainModel;
		setRenderPassModel(snowmanModel);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return this.getEntityTexture((EntitySnowman) p_110775_1_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntitySnowman p_110775_1_) {
		return snowManTextures;
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_,
			float p_77029_2_) {
		this.renderEquippedItems((EntitySnowman) p_77029_1_, p_77029_2_);
	}

	protected void renderEquippedItems(EntitySnowman p_77029_1_,
			float p_77029_2_) {
		super.renderEquippedItems(p_77029_1_, p_77029_2_);
		final ItemStack var3 = new ItemStack(Blocks.pumpkin, 1);

		if (var3.getItem() instanceof ItemBlock) {
			GL11.glPushMatrix();
			snowmanModel.head.postRender(0.0625F);

			if (RenderBlocks.renderItemIn3d(Block.getBlockFromItem(
					var3.getItem()).getRenderType())) {
				final float var4 = 0.625F;
				GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
				GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				GL11.glScalef(var4, -var4, var4);
			}

			renderManager.itemRenderer.renderItem(p_77029_1_, var3, 0);
			GL11.glPopMatrix();
		}
	}
}

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderSnowball extends Render {
	private final int field_94150_f;
	private final Item field_94151_a;

	public RenderSnowball(Item p_i1260_1_) {
		this(p_i1260_1_, 0);
	}

	public RenderSnowball(Item p_i1259_1_, int p_i1259_2_) {
		field_94151_a = p_i1259_1_;
		field_94150_f = p_i1259_2_;
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_,
			double p_76986_4_, double p_76986_6_, float p_76986_8_,
			float p_76986_9_) {
		final IIcon var10 = field_94151_a.getIconFromDamage(field_94150_f);

		if (var10 != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef((float) p_76986_2_, (float) p_76986_4_,
					(float) p_76986_6_);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glScalef(0.5F, 0.5F, 0.5F);
			bindEntityTexture(p_76986_1_);
			final Tessellator var11 = Tessellator.instance;

			if (var10 == ItemPotion.func_94589_d("bottle_splash")) {
				final int var12 = PotionHelper.func_77915_a(
						((EntityPotion) p_76986_1_).getPotionDamage(), false);
				final float var13 = (var12 >> 16 & 255) / 255.0F;
				final float var14 = (var12 >> 8 & 255) / 255.0F;
				final float var15 = (var12 & 255) / 255.0F;
				GL11.glColor3f(var13, var14, var15);
				GL11.glPushMatrix();
				func_77026_a(var11, ItemPotion.func_94589_d("overlay"));
				GL11.glPopMatrix();
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
			}

			func_77026_a(var11, var10);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
		}
	}

	private void func_77026_a(Tessellator p_77026_1_, IIcon p_77026_2_) {
		final float var3 = p_77026_2_.getMinU();
		final float var4 = p_77026_2_.getMaxU();
		final float var5 = p_77026_2_.getMinV();
		final float var6 = p_77026_2_.getMaxV();
		final float var7 = 1.0F;
		final float var8 = 0.5F;
		final float var9 = 0.25F;
		GL11.glRotatef(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		p_77026_1_.startDrawingQuads();
		p_77026_1_.setNormal(0.0F, 1.0F, 0.0F);
		p_77026_1_.addVertexWithUV(0.0F - var8, 0.0F - var9, 0.0D, var3, var6);
		p_77026_1_.addVertexWithUV(var7 - var8, 0.0F - var9, 0.0D, var4, var6);
		p_77026_1_.addVertexWithUV(var7 - var8, var7 - var9, 0.0D, var4, var5);
		p_77026_1_.addVertexWithUV(0.0F - var8, var7 - var9, 0.0D, var3, var5);
		p_77026_1_.draw();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return TextureMap.locationItemsTexture;
	}
}

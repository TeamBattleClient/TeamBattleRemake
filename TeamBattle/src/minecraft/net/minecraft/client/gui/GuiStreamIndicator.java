package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiStreamIndicator {
	private static final ResourceLocation field_152441_a = new ResourceLocation(
			"textures/gui/stream_indicator.png");
	private final Minecraft field_152442_b;
	private float field_152443_c = 1.0F;
	private int field_152444_d = 1;

	public GuiStreamIndicator(Minecraft p_i1092_1_) {
		field_152442_b = p_i1092_1_;
	}

	private void func_152436_a(int p_152436_1_, int p_152436_2_,
			int p_152436_3_, int p_152436_4_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * field_152443_c);
		field_152442_b.getTextureManager().bindTexture(field_152441_a);
		final float var5 = 150.0F;
		final float var6 = 0.0F;
		final float var7 = p_152436_3_ * 0.015625F;
		final float var8 = 1.0F;
		final float var9 = (p_152436_3_ + 16) * 0.015625F;
		final Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.addVertexWithUV(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 16,
				var5, var6, var9);
		var10.addVertexWithUV(p_152436_1_ - p_152436_4_, p_152436_2_ + 16,
				var5, var8, var9);
		var10.addVertexWithUV(p_152436_1_ - p_152436_4_, p_152436_2_ + 0, var5,
				var8, var7);
		var10.addVertexWithUV(p_152436_1_ - 16 - p_152436_4_, p_152436_2_ + 0,
				var5, var6, var7);
		var10.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void func_152437_a(int p_152437_1_, int p_152437_2_) {
		if (field_152442_b.func_152346_Z().func_152934_n()) {
			GL11.glEnable(GL11.GL_BLEND);
			final int var3 = field_152442_b.func_152346_Z().func_152920_A();

			if (var3 > 0) {
				final String var4 = "" + var3;
				final int var5 = field_152442_b.fontRenderer
						.getStringWidth(var4);
				final int var7 = p_152437_1_ - var5 - 1;
				final int var8 = p_152437_2_ + 20 - 1;
				final int var10 = p_152437_2_ + 20
						+ field_152442_b.fontRenderer.FONT_HEIGHT - 1;
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				final Tessellator var11 = Tessellator.instance;
				GL11.glColor4f(0.0F, 0.0F, 0.0F,
						(0.65F + 0.35000002F * field_152443_c) / 2.0F);
				var11.startDrawingQuads();
				var11.addVertex(var7, var10, 0.0D);
				var11.addVertex(p_152437_1_, var10, 0.0D);
				var11.addVertex(p_152437_1_, var8, 0.0D);
				var11.addVertex(var7, var8, 0.0D);
				var11.draw();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				field_152442_b.fontRenderer.drawString(var4,
						p_152437_1_ - var5, p_152437_2_ + 20, 16777215);
			}

			func_152436_a(p_152437_1_, p_152437_2_, func_152440_b(), 0);
			func_152436_a(p_152437_1_, p_152437_2_, func_152438_c(), 17);
		}
	}

	private int func_152438_c() {
		return field_152442_b.func_152346_Z().func_152929_G() ? 48 : 32;
	}

	public void func_152439_a() {
		if (field_152442_b.func_152346_Z().func_152934_n()) {
			field_152443_c += 0.025F * field_152444_d;

			if (field_152443_c < 0.0F) {
				field_152444_d *= -1;
				field_152443_c = 0.0F;
			} else if (field_152443_c > 1.0F) {
				field_152444_d *= -1;
				field_152443_c = 1.0F;
			}
		} else {
			field_152443_c = 1.0F;
			field_152444_d = 1;
		}
	}

	private int func_152440_b() {
		return field_152442_b.func_152346_Z().func_152919_o() ? 16 : 0;
	}
}

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlot {
	protected final int field_148149_f;
	protected int field_148150_g;
	protected int field_148151_d;
	protected int field_148152_e;
	protected int field_148153_b;
	protected int field_148154_c;
	protected int field_148155_a;
	private int field_148156_n;
	private float field_148157_o = -2.0F;
	private int field_148158_l;
	private int field_148159_m;
	protected int field_148160_j;
	private final Minecraft field_148161_k;
	protected int field_148162_h;
	protected boolean field_148163_i = true;
	private boolean field_148164_v = true;
	private boolean field_148165_u;
	private boolean field_148166_t = true;
	private long field_148167_s;
	private int field_148168_r = -1;
	private float field_148169_q;
	private float field_148170_p;

	public GuiSlot(Minecraft p_i1052_1_, int p_i1052_2_, int p_i1052_3_,
			int p_i1052_4_, int p_i1052_5_, int p_i1052_6_) {
		field_148161_k = p_i1052_1_;
		field_148155_a = p_i1052_2_;
		field_148158_l = p_i1052_3_;
		field_148153_b = p_i1052_4_;
		field_148154_c = p_i1052_5_;
		field_148149_f = p_i1052_6_;
		field_148152_e = 0;
		field_148151_d = p_i1052_2_;
	}

	protected abstract void drawBackground();

	protected abstract void drawSlot(int p_148126_1_, int p_148126_2_,
			int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
			int p_148126_6_, int p_148126_7_);

	protected abstract void elementClicked(int p_148144_1_,
			boolean p_148144_2_, int p_148144_3_, int p_148144_4_);

	protected void func_148120_b(int p_148120_1_, int p_148120_2_,
			int p_148120_3_, int p_148120_4_) {
		final int var5 = getSize();
		final Tessellator var6 = Tessellator.instance;

		for (int var7 = 0; var7 < var5; ++var7) {
			final int var8 = p_148120_2_ + var7 * field_148149_f
					+ field_148160_j;
			final int var9 = field_148149_f - 4;

			if (var8 <= field_148154_c && var8 + var9 >= field_148153_b) {
				if (field_148166_t && isSelected(var7)) {
					final int var10 = field_148152_e + field_148155_a / 2
							- func_148139_c() / 2;
					final int var11 = field_148152_e + field_148155_a / 2
							+ func_148139_c() / 2;
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var6.startDrawingQuads();
					var6.setColorOpaque_I(8421504);
					var6.addVertexWithUV(var10, var8 + var9 + 2, 0.0D, 0.0D,
							1.0D);
					var6.addVertexWithUV(var11, var8 + var9 + 2, 0.0D, 1.0D,
							1.0D);
					var6.addVertexWithUV(var11, var8 - 2, 0.0D, 1.0D, 0.0D);
					var6.addVertexWithUV(var10, var8 - 2, 0.0D, 0.0D, 0.0D);
					var6.setColorOpaque_I(0);
					var6.addVertexWithUV(var10 + 1, var8 + var9 + 1, 0.0D,
							0.0D, 1.0D);
					var6.addVertexWithUV(var11 - 1, var8 + var9 + 1, 0.0D,
							1.0D, 1.0D);
					var6.addVertexWithUV(var11 - 1, var8 - 1, 0.0D, 1.0D, 0.0D);
					var6.addVertexWithUV(var10 + 1, var8 - 1, 0.0D, 0.0D, 0.0D);
					var6.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				drawSlot(var7, p_148120_1_, var8, var9, var6, p_148120_3_,
						p_148120_4_);
			}
		}
	}

	private void func_148121_k() {
		int var1 = func_148135_f();

		if (var1 < 0) {
			var1 /= 2;
		}

		if (!field_148163_i && var1 < 0) {
			var1 = 0;
		}

		if (field_148169_q < 0.0F) {
			field_148169_q = 0.0F;
		}

		if (field_148169_q > var1) {
			field_148169_q = var1;
		}
	}

	public void func_148122_a(int p_148122_1_, int p_148122_2_,
			int p_148122_3_, int p_148122_4_) {
		field_148155_a = p_148122_1_;
		field_148158_l = p_148122_2_;
		field_148153_b = p_148122_3_;
		field_148154_c = p_148122_4_;
		field_148152_e = 0;
		field_148151_d = p_148122_1_;
	}

	public int func_148124_c(int p_148124_1_, int p_148124_2_) {
		final int var3 = field_148152_e + field_148155_a / 2 - func_148139_c()
				/ 2;
		final int var4 = field_148152_e + field_148155_a / 2 + func_148139_c()
				/ 2;
		final int var5 = p_148124_2_ - field_148153_b - field_148160_j
				+ (int) field_148169_q - 4;
		final int var6 = var5 / field_148149_f;
		return p_148124_1_ < func_148137_d() && p_148124_1_ >= var3
				&& p_148124_1_ <= var4 && var6 >= 0 && var5 >= 0
				&& var6 < getSize() ? var6 : -1;
	}

	public boolean func_148125_i() {
		return field_148164_v;
	}

	public void func_148128_a(int p_148128_1_, int p_148128_2_,
			float p_148128_3_) {
		field_148150_g = p_148128_1_;
		field_148162_h = p_148128_2_;
		drawBackground();
		final int var4 = getSize();
		final int var5 = func_148137_d();
		final int var6 = var5 + 6;
		int var9;
		int var10;
		int var13;
		int var19;

		if (p_148128_1_ > field_148152_e && p_148128_1_ < field_148151_d
				&& p_148128_2_ > field_148153_b && p_148128_2_ < field_148154_c) {
			if (Mouse.isButtonDown(0) && func_148125_i()) {
				if (field_148157_o == -1.0F) {
					boolean var15 = true;

					if (p_148128_2_ >= field_148153_b
							&& p_148128_2_ <= field_148154_c) {
						final int var8 = field_148155_a / 2 - func_148139_c()
								/ 2;
						var9 = field_148155_a / 2 + func_148139_c() / 2;
						var10 = p_148128_2_ - field_148153_b - field_148160_j
								+ (int) field_148169_q - 4;
						final int var11 = var10 / field_148149_f;

						if (p_148128_1_ >= var8 && p_148128_1_ <= var9
								&& var11 >= 0 && var10 >= 0 && var11 < var4) {
							final boolean var12 = var11 == field_148168_r
									&& Minecraft.getSystemTime()
											- field_148167_s < 250L;
							elementClicked(var11, var12, p_148128_1_,
									p_148128_2_);
							field_148168_r = var11;
							field_148167_s = Minecraft.getSystemTime();
						} else if (p_148128_1_ >= var8 && p_148128_1_ <= var9
								&& var10 < 0) {
							func_148132_a(p_148128_1_ - var8, p_148128_2_
									- field_148153_b + (int) field_148169_q - 4);
							var15 = false;
						}

						if (p_148128_1_ >= var5 && p_148128_1_ <= var6) {
							field_148170_p = -1.0F;
							var19 = func_148135_f();

							if (var19 < 1) {
								var19 = 1;
							}

							var13 = (int) ((float) ((field_148154_c - field_148153_b) * (field_148154_c - field_148153_b)) / (float) func_148138_e());

							if (var13 < 32) {
								var13 = 32;
							}

							if (var13 > field_148154_c - field_148153_b - 8) {
								var13 = field_148154_c - field_148153_b - 8;
							}

							field_148170_p /= (float) (field_148154_c
									- field_148153_b - var13)
									/ (float) var19;
						} else {
							field_148170_p = 1.0F;
						}

						if (var15) {
							field_148157_o = p_148128_2_;
						} else {
							field_148157_o = -2.0F;
						}
					} else {
						field_148157_o = -2.0F;
					}
				} else if (field_148157_o >= 0.0F) {
					field_148169_q -= (p_148128_2_ - field_148157_o)
							* field_148170_p;
					field_148157_o = p_148128_2_;
				}
			} else {
				for (; !field_148161_k.gameSettings.touchscreen && Mouse.next(); field_148161_k.currentScreen
						.handleMouseInput()) {
					int var7 = Mouse.getEventDWheel();

					if (var7 != 0) {
						if (var7 > 0) {
							var7 = -1;
						} else if (var7 < 0) {
							var7 = 1;
						}

						field_148169_q += var7 * field_148149_f / 2;
					}
				}

				field_148157_o = -1.0F;
			}
		}

		func_148121_k();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		final Tessellator var16 = Tessellator.instance;
		field_148161_k.getTextureManager().bindTexture(Gui.optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		final float var17 = 32.0F;
		var16.startDrawingQuads();
		var16.setColorOpaque_I(2105376);
		var16.addVertexWithUV(field_148152_e, field_148154_c, 0.0D,
				field_148152_e / var17, (field_148154_c + (int) field_148169_q)
						/ var17);
		var16.addVertexWithUV(field_148151_d, field_148154_c, 0.0D,
				field_148151_d / var17, (field_148154_c + (int) field_148169_q)
						/ var17);
		var16.addVertexWithUV(field_148151_d, field_148153_b, 0.0D,
				field_148151_d / var17, (field_148153_b + (int) field_148169_q)
						/ var17);
		var16.addVertexWithUV(field_148152_e, field_148153_b, 0.0D,
				field_148152_e / var17, (field_148153_b + (int) field_148169_q)
						/ var17);
		var16.draw();
		var9 = field_148152_e + field_148155_a / 2 - func_148139_c() / 2 + 2;
		var10 = field_148153_b + 4 - (int) field_148169_q;

		if (field_148165_u) {
			func_148129_a(var9, var10, var16);
		}

		func_148120_b(var9, var10, p_148128_1_, p_148128_2_);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		final byte var18 = 4;
		func_148136_c(0, field_148153_b, 255, 255);
		func_148136_c(field_148154_c, field_148158_l, 255, 255);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 0, 1);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV(field_148152_e, field_148153_b + var18, 0.0D,
				0.0D, 1.0D);
		var16.addVertexWithUV(field_148151_d, field_148153_b + var18, 0.0D,
				1.0D, 1.0D);
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV(field_148151_d, field_148153_b, 0.0D, 1.0D, 0.0D);
		var16.addVertexWithUV(field_148152_e, field_148153_b, 0.0D, 0.0D, 0.0D);
		var16.draw();
		var16.startDrawingQuads();
		var16.setColorRGBA_I(0, 255);
		var16.addVertexWithUV(field_148152_e, field_148154_c, 0.0D, 0.0D, 1.0D);
		var16.addVertexWithUV(field_148151_d, field_148154_c, 0.0D, 1.0D, 1.0D);
		var16.setColorRGBA_I(0, 0);
		var16.addVertexWithUV(field_148151_d, field_148154_c - var18, 0.0D,
				1.0D, 0.0D);
		var16.addVertexWithUV(field_148152_e, field_148154_c - var18, 0.0D,
				0.0D, 0.0D);
		var16.draw();
		var19 = func_148135_f();

		if (var19 > 0) {
			var13 = (field_148154_c - field_148153_b)
					* (field_148154_c - field_148153_b) / func_148138_e();

			if (var13 < 32) {
				var13 = 32;
			}

			if (var13 > field_148154_c - field_148153_b - 8) {
				var13 = field_148154_c - field_148153_b - 8;
			}

			int var14 = (int) field_148169_q
					* (field_148154_c - field_148153_b - var13) / var19
					+ field_148153_b;

			if (var14 < field_148153_b) {
				var14 = field_148153_b;
			}

			var16.startDrawingQuads();
			var16.setColorRGBA_I(0, 255);
			var16.addVertexWithUV(var5, field_148154_c, 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV(var6, field_148154_c, 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV(var6, field_148153_b, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV(var5, field_148153_b, 0.0D, 0.0D, 0.0D);
			var16.draw();
			var16.startDrawingQuads();
			var16.setColorRGBA_I(8421504, 255);
			var16.addVertexWithUV(var5, var14 + var13, 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV(var6, var14 + var13, 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV(var6, var14, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV(var5, var14, 0.0D, 0.0D, 0.0D);
			var16.draw();
			var16.startDrawingQuads();
			var16.setColorRGBA_I(12632256, 255);
			var16.addVertexWithUV(var5, var14 + var13 - 1, 0.0D, 0.0D, 1.0D);
			var16.addVertexWithUV(var6 - 1, var14 + var13 - 1, 0.0D, 1.0D, 1.0D);
			var16.addVertexWithUV(var6 - 1, var14, 0.0D, 1.0D, 0.0D);
			var16.addVertexWithUV(var5, var14, 0.0D, 0.0D, 0.0D);
			var16.draw();
		}

		func_148142_b(p_148128_1_, p_148128_2_);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}

	protected void func_148129_a(int p_148129_1_, int p_148129_2_,
			Tessellator p_148129_3_) {
	}

	public void func_148130_a(boolean p_148130_1_) {
		field_148166_t = p_148130_1_;
	}

	protected void func_148132_a(int p_148132_1_, int p_148132_2_) {
	}

	protected void func_148133_a(boolean p_148133_1_, int p_148133_2_) {
		field_148165_u = p_148133_1_;
		field_148160_j = p_148133_2_;

		if (!p_148133_1_) {
			field_148160_j = 0;
		}
	}

	public void func_148134_d(int p_148134_1_, int p_148134_2_) {
		field_148159_m = p_148134_1_;
		field_148156_n = p_148134_2_;
	}

	public int func_148135_f() {
		return func_148138_e() - (field_148154_c - field_148153_b - 4);
	}

	private void func_148136_c(int p_148136_1_, int p_148136_2_,
			int p_148136_3_, int p_148136_4_) {
		final Tessellator var5 = Tessellator.instance;
		field_148161_k.getTextureManager().bindTexture(Gui.optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		final float var6 = 32.0F;
		var5.startDrawingQuads();
		var5.setColorRGBA_I(4210752, p_148136_4_);
		var5.addVertexWithUV(field_148152_e, p_148136_2_, 0.0D, 0.0D,
				p_148136_2_ / var6);
		var5.addVertexWithUV(field_148152_e + field_148155_a, p_148136_2_,
				0.0D, field_148155_a / var6, p_148136_2_ / var6);
		var5.setColorRGBA_I(4210752, p_148136_3_);
		var5.addVertexWithUV(field_148152_e + field_148155_a, p_148136_1_,
				0.0D, field_148155_a / var6, p_148136_1_ / var6);
		var5.addVertexWithUV(field_148152_e, p_148136_1_, 0.0D, 0.0D,
				p_148136_1_ / var6);
		var5.draw();
	}

	protected int func_148137_d() {
		return field_148155_a / 2 + 124;
	}

	protected int func_148138_e() {
		return getSize() * field_148149_f + field_148160_j;
	}

	public int func_148139_c() {
		return 220;
	}

	public void func_148140_g(int p_148140_1_) {
		field_148152_e = p_148140_1_;
		field_148151_d = p_148140_1_ + field_148155_a;
	}

	public boolean func_148141_e(int p_148141_1_) {
		return p_148141_1_ >= field_148153_b && p_148141_1_ <= field_148154_c;
	}

	protected void func_148142_b(int p_148142_1_, int p_148142_2_) {
	}

	public void func_148143_b(boolean p_148143_1_) {
		field_148164_v = p_148143_1_;
	}

	public void func_148145_f(int p_148145_1_) {
		field_148169_q += p_148145_1_;
		func_148121_k();
		field_148157_o = -2.0F;
	}

	public int func_148146_j() {
		return field_148149_f;
	}

	public void func_148147_a(GuiButton p_148147_1_) {
		if (p_148147_1_.enabled) {
			if (p_148147_1_.id == field_148159_m) {
				field_148169_q -= field_148149_f * 2 / 3;
				field_148157_o = -2.0F;
				func_148121_k();
			} else if (p_148147_1_.id == field_148156_n) {
				field_148169_q += field_148149_f * 2 / 3;
				field_148157_o = -2.0F;
				func_148121_k();
			}
		}
	}

	public int func_148148_g() {
		return (int) field_148169_q;
	}

	protected abstract int getSize();

	protected abstract boolean isSelected(int p_148131_1_);
}

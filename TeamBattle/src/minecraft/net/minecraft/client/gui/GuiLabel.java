package net.minecraft.client.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;

public class GuiLabel extends Gui {
	protected int field_146161_f;
	public int field_146162_g;
	private int field_146163_s;
	private FontRenderer field_146164_r;
	private int field_146165_q;
	private int field_146166_p;
	protected int field_146167_a;
	private int field_146168_n;
	private int field_146169_o;
	private boolean field_146170_l;
	private boolean field_146171_m;
	public boolean field_146172_j;
	private ArrayList field_146173_k;
	public int field_146174_h;

	public void func_146159_a(Minecraft p_146159_1_, int p_146159_2_,
			int p_146159_3_) {
		if (field_146172_j) {
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			func_146160_b(p_146159_1_, p_146159_2_, p_146159_3_);
			final int var4 = field_146174_h + field_146161_f / 2
					+ field_146163_s / 2;
			final int var5 = var4 - field_146173_k.size() * 10 / 2;

			for (int var6 = 0; var6 < field_146173_k.size(); ++var6) {
				if (field_146170_l) {
					drawCenteredString(field_146164_r,
							(String) field_146173_k.get(var6), field_146162_g
									+ field_146167_a / 2, var5 + var6 * 10,
							field_146168_n);
				} else {
					drawString(field_146164_r,
							(String) field_146173_k.get(var6), field_146162_g,
							var5 + var6 * 10, field_146168_n);
				}
			}
		}
	}

	protected void func_146160_b(Minecraft p_146160_1_, int p_146160_2_,
			int p_146160_3_) {
		if (field_146171_m) {
			final int var4 = field_146167_a + field_146163_s * 2;
			final int var5 = field_146161_f + field_146163_s * 2;
			final int var6 = field_146162_g - field_146163_s;
			final int var7 = field_146174_h - field_146163_s;
			drawRect(var6, var7, var6 + var4, var7 + var5, field_146169_o);
			drawHorizontalLine(var6, var6 + var4, var7, field_146166_p);
			drawHorizontalLine(var6, var6 + var4, var7 + var5, field_146165_q);
			drawVerticalLine(var6, var7, var7 + var5, field_146166_p);
			drawVerticalLine(var6 + var4, var7, var7 + var5, field_146165_q);
		}
	}
}

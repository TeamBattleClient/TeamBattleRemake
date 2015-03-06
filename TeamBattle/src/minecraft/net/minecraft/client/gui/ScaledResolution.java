package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;

public class ScaledResolution {
	private int scaledHeight;
	private final double scaledHeightD;
	private int scaledWidth;
	private final double scaledWidthD;
	private int scaleFactor;

	public ScaledResolution(Minecraft p_i1094_1_, int p_i1094_2_, int p_i1094_3_) {
		scaledWidth = p_i1094_2_;
		scaledHeight = p_i1094_3_;
		scaleFactor = 1;
		final boolean var4 = p_i1094_1_.func_152349_b();
		int var5 = p_i1094_1_.gameSettings.guiScale;

		if (var5 == 0) {
			var5 = 1000;
		}

		while (scaleFactor < var5 && scaledWidth / (scaleFactor + 1) >= 320
				&& scaledHeight / (scaleFactor + 1) >= 240) {
			++scaleFactor;
		}

		if (var4 && scaleFactor % 2 != 0 && scaleFactor != 1) {
			--scaleFactor;
		}

		scaledWidthD = (double) scaledWidth / (double) scaleFactor;
		scaledHeightD = (double) scaledHeight / (double) scaleFactor;
		scaledWidth = MathHelper.ceiling_double_int(scaledWidthD);
		scaledHeight = MathHelper.ceiling_double_int(scaledHeightD);
	}

	public int getScaledHeight() {
		return scaledHeight;
	}

	public double getScaledHeight_double() {
		return scaledHeightD;
	}

	public int getScaledWidth() {
		return scaledWidth;
	}

	public double getScaledWidth_double() {
		return scaledWidthD;
	}

	public int getScaleFactor() {
		return scaleFactor;
	}
}

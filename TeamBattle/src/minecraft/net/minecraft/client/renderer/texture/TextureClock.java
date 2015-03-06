package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;

public class TextureClock extends TextureAtlasSprite {
	private double field_94239_h;
	private double field_94240_i;

	public TextureClock(String p_i1285_1_) {
		super(p_i1285_1_);
	}

	@Override
	public void updateAnimation() {
		if (!framesTextureData.isEmpty()) {
			final Minecraft var1 = Minecraft.getMinecraft();
			double var2 = 0.0D;

			if (var1.theWorld != null && var1.thePlayer != null) {
				final float var4 = var1.theWorld.getCelestialAngle(1.0F);
				var2 = var4;

				if (!var1.theWorld.provider.isSurfaceWorld()) {
					var2 = Math.random();
				}
			}

			double var7;

			for (var7 = var2 - field_94239_h; var7 < -0.5D; ++var7) {
				;
			}

			while (var7 >= 0.5D) {
				--var7;
			}

			if (var7 < -1.0D) {
				var7 = -1.0D;
			}

			if (var7 > 1.0D) {
				var7 = 1.0D;
			}

			field_94240_i += var7 * 0.1D;
			field_94240_i *= 0.8D;
			field_94239_h += field_94240_i;
			int var6;

			for (var6 = (int) ((field_94239_h + 1.0D) * framesTextureData
					.size()) % framesTextureData.size(); var6 < 0; var6 = (var6 + framesTextureData
					.size()) % framesTextureData.size()) {
				;
			}

			if (var6 != frameCounter) {
				frameCounter = var6;
				TextureUtil.func_147955_a(
						(int[][]) framesTextureData.get(frameCounter), width,
						height, originX, originY, false, false);
			}
		}
	}
}

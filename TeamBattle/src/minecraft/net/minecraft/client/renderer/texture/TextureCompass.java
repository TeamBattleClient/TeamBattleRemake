package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TextureCompass extends TextureAtlasSprite {
	/** Speed and direction of compass rotation */
	public double angleDelta;

	/** Current compass heading in radians */
	public double currentAngle;

	public TextureCompass(String p_i1286_1_) {
		super(p_i1286_1_);
	}

	@Override
	public void updateAnimation() {
		final Minecraft var1 = Minecraft.getMinecraft();

		if (var1.theWorld != null && var1.thePlayer != null) {
			updateCompass(var1.theWorld, var1.thePlayer.posX,
					var1.thePlayer.posZ, var1.thePlayer.rotationYaw, false,
					false);
		} else {
			updateCompass((World) null, 0.0D, 0.0D, 0.0D, true, false);
		}
	}

	/**
	 * Updates the compass based on the given x,z coords and camera direction
	 */
	public void updateCompass(World p_94241_1_, double p_94241_2_,
			double p_94241_4_, double p_94241_6_, boolean p_94241_8_,
			boolean p_94241_9_) {
		if (!framesTextureData.isEmpty()) {
			double var10 = 0.0D;

			if (p_94241_1_ != null && !p_94241_8_) {
				final ChunkCoordinates var12 = p_94241_1_.getSpawnPoint();
				final double var13 = var12.posX - p_94241_2_;
				final double var15 = var12.posZ - p_94241_4_;
				p_94241_6_ %= 360.0D;
				var10 = -((p_94241_6_ - 90.0D) * Math.PI / 180.0D - Math.atan2(
						var15, var13));

				if (!p_94241_1_.provider.isSurfaceWorld()) {
					var10 = Math.random() * Math.PI * 2.0D;
				}
			}

			if (p_94241_9_) {
				currentAngle = var10;
			} else {
				double var17;

				for (var17 = var10 - currentAngle; var17 < -Math.PI; var17 += Math.PI * 2D) {
					;
				}

				while (var17 >= Math.PI) {
					var17 -= Math.PI * 2D;
				}

				if (var17 < -1.0D) {
					var17 = -1.0D;
				}

				if (var17 > 1.0D) {
					var17 = 1.0D;
				}

				angleDelta += var17 * 0.1D;
				angleDelta *= 0.8D;
				currentAngle += angleDelta;
			}

			int var18;

			for (var18 = (int) ((currentAngle / (Math.PI * 2D) + 1.0D) * framesTextureData
					.size()) % framesTextureData.size(); var18 < 0; var18 = (var18 + framesTextureData
					.size()) % framesTextureData.size()) {
				;
			}

			if (var18 != frameCounter) {
				frameCounter = var18;
				TextureUtil.func_147955_a(
						(int[][]) framesTextureData.get(frameCounter), width,
						height, originX, originY, false, false);
			}
		}
	}
}

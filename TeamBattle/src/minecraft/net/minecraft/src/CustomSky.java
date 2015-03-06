package net.minecraft.src;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class CustomSky {
	private static CustomSkyLayer[][] worldSkyLayers = null;

	private static void clearBlend(float rainBrightness) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, rainBrightness);
	}

	public static boolean hasSkyLayers(World world) {
		if (worldSkyLayers == null)
			return false;
		else if (Config.getGameSettings().renderDistanceChunks < 8)
			return false;
		else {
			final int dimId = world.provider.dimensionId;

			if (dimId >= 0 && dimId < worldSkyLayers.length) {
				final CustomSkyLayer[] sls = worldSkyLayers[dimId];
				return sls == null ? false : sls.length > 0;
			} else
				return false;
		}
	}

	private static CustomSkyLayer[][] readCustomSkies() {
		final CustomSkyLayer[][] wsls = new CustomSkyLayer[10][0];
		final String prefix = "mcpatcher/sky/world";
		int lastWorldId = -1;
		int worldCount = 0;

		while (worldCount < wsls.length) {
			final String wslsTrim = prefix + worldCount + "/sky";
			final ArrayList i = new ArrayList();
			int sls = 1;

			while (true) {
				if (sls < 1000) {
					label69: {
						final String path = wslsTrim + sls + ".properties";

						try {
							final ResourceLocation e = new ResourceLocation(
									path);
							final InputStream in = Config.getResourceStream(e);

							if (in == null) {
								break label69;
							}

							final Properties props = new Properties();
							props.load(in);
							Config.dbg("CustomSky properties: " + path);
							final String defSource = wslsTrim + sls + ".png";
							final CustomSkyLayer sl = new CustomSkyLayer(props,
									defSource);

							if (sl.isValid(path)) {
								final ResourceLocation locSource = new ResourceLocation(
										sl.source);
								final ITextureObject tex = TextureUtils
										.getTexture(locSource);

								if (tex == null) {
									Config.log("CustomSky: Texture not found: "
											+ locSource);
								} else {
									sl.textureId = tex.getGlTextureId();
									i.add(sl);
									in.close();
								}
							}
						} catch (final FileNotFoundException var15) {
							break label69;
						} catch (final IOException var16) {
							var16.printStackTrace();
						}

						++sls;
						continue;
					}
				}

				if (i.size() > 0) {
					final CustomSkyLayer[] var19 = (CustomSkyLayer[]) i
							.toArray(new CustomSkyLayer[i.size()]);
					wsls[worldCount] = var19;
					lastWorldId = worldCount;
				}

				++worldCount;
				break;
			}
		}

		if (lastWorldId < 0)
			return null;
		else {
			worldCount = lastWorldId + 1;
			final CustomSkyLayer[][] var17 = new CustomSkyLayer[worldCount][0];

			for (int var18 = 0; var18 < var17.length; ++var18) {
				var17[var18] = wsls[var18];
			}

			return var17;
		}
	}

	public static void renderSky(World world, TextureManager re,
			float celestialAngle, float rainBrightness) {
		if (worldSkyLayers != null) {
			if (Config.getGameSettings().renderDistanceChunks >= 8) {
				final int dimId = world.provider.dimensionId;

				if (dimId >= 0 && dimId < worldSkyLayers.length) {
					final CustomSkyLayer[] sls = worldSkyLayers[dimId];

					if (sls != null) {
						final long time = world.getWorldTime();
						final int timeOfDay = (int) (time % 24000L);

						for (final CustomSkyLayer sl : sls) {
							if (sl.isActive(timeOfDay)) {
								sl.render(timeOfDay, celestialAngle,
										rainBrightness);
							}
						}

						clearBlend(rainBrightness);
					}
				}
			}
		}
	}

	public static void reset() {
		worldSkyLayers = null;
	}

	public static void update() {
		reset();

		if (Config.isCustomSky()) {
			worldSkyLayers = readCustomSkies();
		}
	}
}

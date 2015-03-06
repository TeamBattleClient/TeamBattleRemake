package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class CustomColorizer {
	private static int[][] blockPalettes = null;

	private static Vec3 fogColorEnd = null;

	private static Vec3 fogColorNether = null;

	private static int[] fogColors = null;

	private static int[] foliageBirchColors = null;

	private static int[] foliageColors = null;

	private static int[] foliagePineColors = null;

	private static int[] grassColors = null;

	private static float[][][] lightMapsColorsRgb = null;

	private static int[] lightMapsHeight = null;

	private static int lilyPadColor = -1;

	private static int[] myceliumParticleColors = null;

	private static int[][] paletteColors = null;

	private static int particlePortalColor = -1;

	private static int particleWaterColor = -1;

	private static Random random = new Random();

	private static int[] redstoneColors = null;

	private static Vec3 skyColorEnd = null;

	private static int[] skyColors = null;

	private static int[] stemColors = null;

	private static float[][] sunRgbs = new float[16][3];

	private static int[] swampFoliageColors = null;

	private static int[] swampGrassColors = null;

	private static float[][] torchRgbs = new float[16][3];

	private static int[] underwaterColors = null;

	private static boolean useDefaultColorMultiplier = true;

	private static int[] waterColors = null;

	public static int getColorMultiplier(Block block, IBlockAccess blockAccess,
			int x, int y, int z) {
		if (useDefaultColorMultiplier)
			return block.colorMultiplier(blockAccess, x, y, z);
		else {
			int[] colors = null;
			int[] swampColors = null;
			int metadata;

			if (blockPalettes != null) {
				final int useSwampColors = Block.getIdFromBlock(block);

				if (useSwampColors >= 0 && useSwampColors < 256) {
					final int[] smoothColors = blockPalettes[useSwampColors];
					int type1;

					if (smoothColors.length > 1) {
						metadata = blockAccess.getBlockMetadata(x, y, z);
						type1 = smoothColors[metadata];
					} else {
						type1 = smoothColors[0];
					}

					if (type1 >= 0) {
						colors = paletteColors[type1];
					}
				}

				if (colors != null) {
					if (Config.isSmoothBiomes())
						return getSmoothColorMultiplier(block, blockAccess, x,
								y, z, colors, colors, 0, 0);

					return getCustomColor(colors, blockAccess, x, y, z);
				}
			}

			final boolean useSwampColors1 = Config.isSwampColors();
			boolean smoothColors1 = false;
			byte type2 = 0;
			metadata = 0;

			if (block != Blocks.grass && block != Blocks.tallgrass) {
				if (block == Blocks.leaves) {
					type2 = 2;
					smoothColors1 = Config.isSmoothBiomes();
					metadata = blockAccess.getBlockMetadata(x, y, z);

					if ((metadata & 3) == 1) {
						colors = foliagePineColors;
					} else if ((metadata & 3) == 2) {
						colors = foliageBirchColors;
					} else {
						colors = foliageColors;

						if (useSwampColors1) {
							swampColors = swampFoliageColors;
						} else {
							swampColors = colors;
						}
					}
				} else if (block == Blocks.vine) {
					type2 = 2;
					smoothColors1 = Config.isSmoothBiomes();
					colors = foliageColors;

					if (useSwampColors1) {
						swampColors = swampFoliageColors;
					} else {
						swampColors = colors;
					}
				}
			} else {
				type2 = 1;
				smoothColors1 = Config.isSmoothBiomes();
				colors = grassColors;

				if (useSwampColors1) {
					swampColors = swampGrassColors;
				} else {
					swampColors = colors;
				}
			}

			if (smoothColors1)
				return getSmoothColorMultiplier(block, blockAccess, x, y, z,
						colors, swampColors, type2, metadata);
			else {
				if (swampColors != colors
						&& blockAccess.getBiomeGenForCoords(x, z) == BiomeGenBase.swampland) {
					colors = swampColors;
				}

				return colors != null ? getCustomColor(colors, blockAccess, x,
						y, z) : block.colorMultiplier(blockAccess, x, y, z);
			}
		}
	}

	private static int getCustomColor(int[] colors, IBlockAccess blockAccess,
			int x, int y, int z) {
		final BiomeGenBase bgb = blockAccess.getBiomeGenForCoords(x, z);
		final double temperature = MathHelper.clamp_float(
				bgb.getFloatTemperature(x, y, z), 0.0F, 1.0F);
		double rainfall = MathHelper.clamp_float(bgb.getFloatRainfall(), 0.0F,
				1.0F);
		rainfall *= temperature;
		final int cx = (int) ((1.0D - temperature) * 255.0D);
		final int cy = (int) ((1.0D - rainfall) * 255.0D);
		return colors[cy << 8 | cx] & 16777215;
	}

	private static int[] getCustomColors(String path, int length) {
		try {
			final ResourceLocation e = new ResourceLocation(path);
			final InputStream in = Config.getResourceStream(e);

			if (in == null)
				return null;
			else {
				final int[] colors = TextureUtil.readImageData(
						Config.getResourceManager(), e);

				if (colors == null)
					return null;
				else if (length > 0 && colors.length != length) {
					Config.log("Invalid custom colors length: " + colors.length
							+ ", path: " + path);
					return null;
				} else {
					Config.log("Loading custom colors: " + path);
					return colors;
				}
			}
		} catch (final FileNotFoundException var5) {
			return null;
		} catch (final IOException var6) {
			var6.printStackTrace();
			return null;
		}
	}

	private static int[] getCustomColors(String basePath, String[] paths,
			int length) {
		for (final String path2 : paths) {
			String path = path2;
			path = basePath + path;
			final int[] cols = getCustomColors(path, length);

			if (cols != null)
				return cols;
		}

		return null;
	}

	public static int getFluidColor(Block block, IBlockAccess blockAccess,
			int x, int y, int z) {
		return block.getMaterial() != Material.water ? block.colorMultiplier(
				blockAccess, x, y, z) : waterColors != null ? Config
				.isSmoothBiomes() ? getSmoothColor(waterColors, blockAccess, x,
				y, z, 3, 1) : getCustomColor(waterColors, blockAccess, x, y, z)
				: !Config.isSwampColors() ? 16777215 : block.colorMultiplier(
						blockAccess, x, y, z);
	}

	public static Vec3 getFogColor(Vec3 fogColor3d, IBlockAccess blockAccess,
			double x, double y, double z) {
		if (fogColors == null)
			return fogColor3d;
		else {
			final int col = getSmoothColor(fogColors, blockAccess, x, y, z, 10,
					1);
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			float redF = red / 255.0F;
			float greenF = green / 255.0F;
			float blueF = blue / 255.0F;
			final float cRed = (float) fogColor3d.xCoord / 0.753F;
			final float cGreen = (float) fogColor3d.yCoord / 0.8471F;
			final float cBlue = (float) fogColor3d.zCoord;
			redF *= cRed;
			greenF *= cGreen;
			blueF *= cBlue;
			return Vec3.createVectorHelper(redF, greenF, blueF);
		}
	}

	public static Vec3 getFogColorEnd(Vec3 col) {
		return fogColorEnd == null ? col : fogColorEnd;
	}

	public static Vec3 getFogColorNether(Vec3 col) {
		return fogColorNether == null ? col : fogColorNether;
	}

	private static void getLightMapColumn(float[][] origMap, float x,
			int offset, int width, float[][] colRgb) {
		final int xLow = (int) Math.floor(x);
		final int xHigh = (int) Math.ceil(x);

		if (xLow == xHigh) {
			for (int var14 = 0; var14 < 16; ++var14) {
				final float[] var15 = origMap[offset + var14 * width + xLow];
				final float[] var16 = colRgb[var14];

				for (int var17 = 0; var17 < 3; ++var17) {
					var16[var17] = var15[var17];
				}
			}
		} else {
			final float dLow = 1.0F - (x - xLow);
			final float dHigh = 1.0F - (xHigh - x);

			for (int y = 0; y < 16; ++y) {
				final float[] rgbLow = origMap[offset + y * width + xLow];
				final float[] rgbHigh = origMap[offset + y * width + xHigh];
				final float[] rgb = colRgb[y];

				for (int i = 0; i < 3; ++i) {
					rgb[i] = rgbLow[i] * dLow + rgbHigh[i] * dHigh;
				}
			}
		}
	}

	public static int getLilypadColor() {
		return lilyPadColor < 0 ? Blocks.waterlily.getBlockColor()
				: lilyPadColor;
	}

	public static int getRedstoneColor(int level) {
		return redstoneColors == null ? -1
				: level >= 0 && level <= 15 ? redstoneColors[level] & 16777215
						: -1;
	}

	public static Vec3 getSkyColor(Vec3 skyColor3d, IBlockAccess blockAccess,
			double x, double y, double z) {
		if (skyColors == null)
			return skyColor3d;
		else {
			final int col = getSmoothColor(skyColors, blockAccess, x, y, z, 10,
					1);
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			float redF = red / 255.0F;
			float greenF = green / 255.0F;
			float blueF = blue / 255.0F;
			final float cRed = (float) skyColor3d.xCoord / 0.5F;
			final float cGreen = (float) skyColor3d.yCoord / 0.66275F;
			final float cBlue = (float) skyColor3d.zCoord;
			redF *= cRed;
			greenF *= cGreen;
			blueF *= cBlue;
			return Vec3.createVectorHelper(redF, greenF, blueF);
		}
	}

	public static Vec3 getSkyColorEnd(Vec3 col) {
		return skyColorEnd == null ? col : skyColorEnd;
	}

	public static int getSmoothColor(int[] colors, IBlockAccess blockAccess,
			double x, double y, double z, int samples, int step) {
		if (colors == null)
			return -1;
		else {
			final int x0 = (int) Math.floor(x);
			final int y0 = (int) Math.floor(y);
			final int z0 = (int) Math.floor(z);
			final int n = samples * step / 2;
			int sumRed = 0;
			int sumGreen = 0;
			int sumBlue = 0;
			int count = 0;
			int r;
			int g;
			int b;

			for (r = x0 - n; r <= x0 + n; r += step) {
				for (g = z0 - n; g <= z0 + n; g += step) {
					b = getCustomColor(colors, blockAccess, r, y0, g);
					sumRed += b >> 16 & 255;
					sumGreen += b >> 8 & 255;
					sumBlue += b & 255;
					++count;
				}
			}

			r = sumRed / count;
			g = sumGreen / count;
			b = sumBlue / count;
			return r << 16 | g << 8 | b;
		}
	}

	private static int getSmoothColorMultiplier(Block block,
			IBlockAccess blockAccess, int x, int y, int z, int[] colors,
			int[] swampColors, int type, int metadata) {
		int sumRed = 0;
		int sumGreen = 0;
		int sumBlue = 0;
		int r;
		int g;

		for (r = x - 1; r <= x + 1; ++r) {
			for (g = z - 1; g <= z + 1; ++g) {
				int[] b = colors;

				if (swampColors != colors
						&& blockAccess.getBiomeGenForCoords(r, g) == BiomeGenBase.swampland) {
					b = swampColors;
				}

				int var17;

				if (b == null) {
					switch (type) {
					case 1:
						var17 = blockAccess.getBiomeGenForCoords(r, g)
								.getBiomeGrassColor(x, y, z);
						break;

					case 2:
						if ((metadata & 3) == 1) {
							var17 = ColorizerFoliage.getFoliageColorPine();
						} else if ((metadata & 3) == 2) {
							var17 = ColorizerFoliage.getFoliageColorBirch();
						} else {
							var17 = blockAccess.getBiomeGenForCoords(r, g)
									.getBiomeFoliageColor(x, y, z);
						}

						break;

					default:
						var17 = block.colorMultiplier(blockAccess, r, y, g);
					}
				} else {
					var17 = getCustomColor(b, blockAccess, r, y, g);
				}

				sumRed += var17 >> 16 & 255;
				sumGreen += var17 >> 8 & 255;
				sumBlue += var17 & 255;
			}
		}

		r = sumRed / 9;
		g = sumGreen / 9;
		final int var16 = sumBlue / 9;
		return r << 16 | g << 8 | var16;
	}

	public static int getStemColorMultiplier(BlockStem blockStem,
			IBlockAccess blockAccess, int x, int y, int z) {
		if (stemColors == null)
			return blockStem.colorMultiplier(blockAccess, x, y, z);
		else {
			int level = blockAccess.getBlockMetadata(x, y, z);

			if (level < 0) {
				level = 0;
			}

			if (level >= stemColors.length) {
				level = stemColors.length - 1;
			}

			return stemColors[level];
		}
	}

	private static int getTextureHeight(String path, int defHeight) {
		try {
			final InputStream e = Config
					.getResourceStream(new ResourceLocation(path));

			if (e == null)
				return defHeight;
			else {
				final BufferedImage bi = ImageIO.read(e);
				return bi == null ? defHeight : bi.getHeight();
			}
		} catch (final IOException var4) {
			return defHeight;
		}
	}

	public static Vec3 getUnderwaterColor(IBlockAccess blockAccess, double x,
			double y, double z) {
		if (underwaterColors == null)
			return null;
		else {
			final int col = getSmoothColor(underwaterColors, blockAccess, x, y,
					z, 10, 1);
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			final float redF = red / 255.0F;
			final float greenF = green / 255.0F;
			final float blueF = blue / 255.0F;
			return Vec3.createVectorHelper(redF, greenF, blueF);
		}
	}

	public static Vec3 getWorldFogColor(Vec3 fogVec, WorldClient world,
			float partialTicks) {
		final int worldType = world.provider.dimensionId;

		switch (worldType) {
		case -1:
			fogVec = getFogColorNether(fogVec);
			break;

		case 0:
			final Minecraft mc = Minecraft.getMinecraft();
			fogVec = getFogColor(fogVec, mc.theWorld, mc.renderViewEntity.posX,
					mc.renderViewEntity.posY + 1.0D, mc.renderViewEntity.posZ);
			break;

		case 1:
			fogVec = getFogColorEnd(fogVec);
		}

		return fogVec;
	}

	public static Vec3 getWorldSkyColor(Vec3 skyVec, WorldClient world,
			EntityLivingBase renderViewEntity, float partialTicks) {
		final int worldType = world.provider.dimensionId;

		switch (worldType) {
		case 0:
			final Minecraft mc = Minecraft.getMinecraft();
			skyVec = getSkyColor(skyVec, mc.theWorld, mc.renderViewEntity.posX,
					mc.renderViewEntity.posY + 1.0D, mc.renderViewEntity.posZ);
			break;

		case 1:
			skyVec = getSkyColorEnd(skyVec);
		}

		return skyVec;
	}

	public static int mixColors(int c1, int c2, float w1) {
		if (w1 <= 0.0F)
			return c2;
		else if (w1 >= 1.0F)
			return c1;
		else {
			final float w2 = 1.0F - w1;
			final int r1 = c1 >> 16 & 255;
			final int g1 = c1 >> 8 & 255;
			final int b1 = c1 & 255;
			final int r2 = c2 >> 16 & 255;
			final int g2 = c2 >> 8 & 255;
			final int b2 = c2 & 255;
			final int r = (int) (r1 * w1 + r2 * w2);
			final int g = (int) (g1 * w1 + g2 * w2);
			final int b = (int) (b1 * w1 + b2 * w2);
			return r << 16 | g << 8 | b;
		}
	}

	private static int readColor(Properties props, String name) {
		final String str = props.getProperty(name);

		if (str == null)
			return -1;
		else {
			try {
				final int e = Integer.parseInt(str, 16) & 16777215;
				Config.log("Custom color: " + name + " = " + str);
				return e;
			} catch (final NumberFormatException var4) {
				Config.log("Invalid custom color: " + name + " = " + str);
				return -1;
			}
		}
	}

	private static int readColor(Properties props, String[] names) {
		for (final String name : names) {
			final int col = readColor(props, name);

			if (col >= 0)
				return col;
		}

		return -1;
	}

	private static void readColorProperties(String fileName) {
		try {
			final ResourceLocation e = new ResourceLocation(fileName);
			final InputStream in = Config.getResourceStream(e);

			if (in == null)
				return;

			Config.log("Loading " + fileName);
			final Properties props = new Properties();
			props.load(in);
			lilyPadColor = readColor(props, "lilypad");
			particleWaterColor = readColor(props, new String[] {
					"particle.water", "drop.water" });
			particlePortalColor = readColor(props, "particle.portal");
			fogColorNether = readColorVec3(props, "fog.nether");
			fogColorEnd = readColorVec3(props, "fog.end");
			skyColorEnd = readColorVec3(props, "sky.end");
			readCustomPalettes(props, fileName);
		} catch (final FileNotFoundException var4) {
			return;
		} catch (final IOException var5) {
			var5.printStackTrace();
		}
	}

	private static Vec3 readColorVec3(Properties props, String name) {
		final int col = readColor(props, name);

		if (col < 0)
			return null;
		else {
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			final float redF = red / 255.0F;
			final float greenF = green / 255.0F;
			final float blueF = blue / 255.0F;
			return Vec3.createVectorHelper(redF, greenF, blueF);
		}
	}

	private static void readCustomPalettes(Properties props, String fileName) {
		blockPalettes = new int[256][1];

		for (int palettePrefix = 0; palettePrefix < 256; ++palettePrefix) {
			blockPalettes[palettePrefix][0] = -1;
		}

		final String var18 = "palette.block.";
		final HashMap map = new HashMap();
		final Set keys = props.keySet();
		final Iterator propNames = keys.iterator();
		String name;

		while (propNames.hasNext()) {
			final String i = (String) propNames.next();
			name = props.getProperty(i);

			if (i.startsWith(var18)) {
				map.put(i, name);
			}
		}

		final String[] var19 = (String[]) map.keySet().toArray(
				new String[map.size()]);
		paletteColors = new int[var19.length][];

		for (int var20 = 0; var20 < var19.length; ++var20) {
			name = var19[var20];
			final String value = props.getProperty(name);
			Config.log("Block palette: " + name + " = " + value);
			String path = name.substring(var18.length());
			final String basePath = TextureUtils.getBasePath(fileName);
			path = TextureUtils.fixResourcePath(path, basePath);
			final int[] colors = getCustomColors(path, 65536);
			paletteColors[var20] = colors;
			final String[] indexStrs = Config.tokenize(value, " ,;");

			for (final String indexStr : indexStrs) {
				String blockStr = indexStr;
				int metadata = -1;

				if (blockStr.contains(":")) {
					final String[] blockIndex = Config.tokenize(blockStr, ":");
					blockStr = blockIndex[0];
					final String metadataStr = blockIndex[1];
					metadata = Config.parseInt(metadataStr, -1);

					if (metadata < 0 || metadata > 15) {
						Config.log("Invalid block metadata: " + blockStr
								+ " in palette: " + name);
						continue;
					}
				}

				final int var21 = Config.parseInt(blockStr, -1);

				if (var21 >= 0 && var21 <= 255) {
					if (var21 != Block.getIdFromBlock(Blocks.grass)
							&& var21 != Block.getIdFromBlock(Blocks.tallgrass)
							&& var21 != Block.getIdFromBlock(Blocks.leaves)
							&& var21 != Block.getIdFromBlock(Blocks.vine)) {
						if (metadata == -1) {
							blockPalettes[var21][0] = var20;
						} else {
							if (blockPalettes[var21].length < 16) {
								blockPalettes[var21] = new int[16];
								Arrays.fill(blockPalettes[var21], -1);
							}

							blockPalettes[var21][metadata] = var20;
						}
					}
				} else {
					Config.log("Invalid block index: " + var21
							+ " in palette: " + name);
				}
			}
		}
	}

	private static float[][] toRgb(int[] cols) {
		final float[][] colsRgb = new float[cols.length][3];

		for (int i = 0; i < cols.length; ++i) {
			final int col = cols[i];
			final float rf = (col >> 16 & 255) / 255.0F;
			final float gf = (col >> 8 & 255) / 255.0F;
			final float bf = (col & 255) / 255.0F;
			final float[] colRgb = colsRgb[i];
			colRgb[0] = rf;
			colRgb[1] = gf;
			colRgb[2] = bf;
		}

		return colsRgb;
	}

	public static void update() {
		grassColors = null;
		waterColors = null;
		foliageColors = null;
		foliageBirchColors = null;
		foliagePineColors = null;
		swampGrassColors = null;
		swampFoliageColors = null;
		skyColors = null;
		fogColors = null;
		underwaterColors = null;
		redstoneColors = null;
		stemColors = null;
		myceliumParticleColors = null;
		lightMapsColorsRgb = null;
		lightMapsHeight = null;
		lilyPadColor = -1;
		particleWaterColor = -1;
		particlePortalColor = -1;
		fogColorNether = null;
		fogColorEnd = null;
		skyColorEnd = null;
		blockPalettes = null;
		paletteColors = null;
		useDefaultColorMultiplier = true;
		final String mcpColormap = "mcpatcher/colormap/";
		grassColors = getCustomColors("textures/colormap/grass.png", 65536);
		foliageColors = getCustomColors("textures/colormap/foliage.png", 65536);
		final String[] waterPaths = new String[] { "water.png",
				"watercolorX.png" };
		waterColors = getCustomColors(mcpColormap, waterPaths, 65536);

		if (Config.isCustomColors()) {
			final String[] pinePaths = new String[] { "pine.png",
					"pinecolor.png" };
			foliagePineColors = getCustomColors(mcpColormap, pinePaths, 65536);
			final String[] birchPaths = new String[] { "birch.png",
					"birchcolor.png" };
			foliageBirchColors = getCustomColors(mcpColormap, birchPaths, 65536);
			final String[] swampGrassPaths = new String[] { "swampgrass.png",
					"swampgrasscolor.png" };
			swampGrassColors = getCustomColors(mcpColormap, swampGrassPaths,
					65536);
			final String[] swampFoliagePaths = new String[] {
					"swampfoliage.png", "swampfoliagecolor.png" };
			swampFoliageColors = getCustomColors(mcpColormap,
					swampFoliagePaths, 65536);
			final String[] sky0Paths = new String[] { "sky0.png",
					"skycolor0.png" };
			skyColors = getCustomColors(mcpColormap, sky0Paths, 65536);
			final String[] fog0Paths = new String[] { "fog0.png",
					"fogcolor0.png" };
			fogColors = getCustomColors(mcpColormap, fog0Paths, 65536);
			final String[] underwaterPaths = new String[] { "underwater.png",
					"underwatercolor.png" };
			underwaterColors = getCustomColors(mcpColormap, underwaterPaths,
					65536);
			final String[] redstonePaths = new String[] { "redstone.png",
					"redstonecolor.png" };
			redstoneColors = getCustomColors(mcpColormap, redstonePaths, 16);
			final String[] stemPaths = new String[] { "stem.png",
					"stemcolor.png" };
			stemColors = getCustomColors(mcpColormap, stemPaths, 8);
			final String[] myceliumPaths = new String[] {
					"myceliumparticle.png", "myceliumparticlecolor.png" };
			myceliumParticleColors = getCustomColors(mcpColormap,
					myceliumPaths, -1);
			final int[][] lightMapsColors = new int[3][];
			lightMapsColorsRgb = new float[3][][];
			lightMapsHeight = new int[3];

			for (int i = 0; i < lightMapsColors.length; ++i) {
				final String path = "mcpatcher/lightmap/world" + (i - 1)
						+ ".png";
				lightMapsColors[i] = getCustomColors(path, -1);

				if (lightMapsColors[i] != null) {
					lightMapsColorsRgb[i] = toRgb(lightMapsColors[i]);
				}

				lightMapsHeight[i] = getTextureHeight(path, 32);
			}

			readColorProperties("mcpatcher/color.properties");
			updateUseDefaultColorMultiplier();
		}
	}

	public static boolean updateLightmap(World world, float torchFlickerX,
			int[] lmColors, boolean nightvision) {
		if (world == null)
			return false;
		else if (lightMapsColorsRgb == null)
			return false;
		else if (!Config.isCustomColors())
			return false;
		else {
			final int worldType = world.provider.dimensionId;

			if (worldType >= -1 && worldType <= 1) {
				final int lightMapIndex = worldType + 1;
				final float[][] lightMapRgb = lightMapsColorsRgb[lightMapIndex];

				if (lightMapRgb == null)
					return false;
				else {
					final int height = lightMapsHeight[lightMapIndex];

					if (nightvision && height < 64)
						return false;
					else {
						final int width = lightMapRgb.length / height;

						if (width < 16) {
							Config.warn("Invalid lightmap width: " + width
									+ " for: /environment/lightmap" + worldType
									+ ".png");
							lightMapsColorsRgb[lightMapIndex] = null;
							return false;
						} else {
							int startIndex = 0;

							if (nightvision) {
								startIndex = width * 16 * 2;
							}

							float sun = 1.1666666F * (world
									.getSunBrightness(1.0F) - 0.2F);

							if (world.lastLightningBolt > 0) {
								sun = 1.0F;
							}

							sun = Config.limitTo1(sun);
							final float sunX = sun * (width - 1);
							final float torchX = Config
									.limitTo1(torchFlickerX + 0.5F)
									* (width - 1);
							final float gamma = Config.limitTo1(Config
									.getGameSettings().gammaSetting);
							final boolean hasGamma = gamma > 1.0E-4F;
							getLightMapColumn(lightMapRgb, sunX, startIndex,
									width, sunRgbs);
							getLightMapColumn(lightMapRgb, torchX, startIndex
									+ 16 * width, width, torchRgbs);
							final float[] rgb = new float[3];

							for (int is = 0; is < 16; ++is) {
								for (int it = 0; it < 16; ++it) {
									int r;

									for (r = 0; r < 3; ++r) {
										float g = Config
												.limitTo1(sunRgbs[is][r]
														+ torchRgbs[it][r]);

										if (hasGamma) {
											float b = 1.0F - g;
											b = 1.0F - b * b * b * b;
											g = gamma * b + (1.0F - gamma) * g;
										}

										rgb[r] = g;
									}

									r = (int) (rgb[0] * 255.0F);
									final int var21 = (int) (rgb[1] * 255.0F);
									final int var22 = (int) (rgb[2] * 255.0F);
									lmColors[is * 16 + it] = -16777216
											| r << 16 | var21 << 8 | var22;
								}
							}

							return true;
						}
					}
				}
			} else
				return false;
		}
	}

	public static void updateMyceliumFX(EntityFX fx) {
		if (myceliumParticleColors != null) {
			final int col = myceliumParticleColors[random
					.nextInt(myceliumParticleColors.length)];
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			final float redF = red / 255.0F;
			final float greenF = green / 255.0F;
			final float blueF = blue / 255.0F;
			fx.setRBGColorF(redF, greenF, blueF);
		}
	}

	public static void updatePortalFX(EntityFX fx) {
		if (particlePortalColor >= 0) {
			final int col = particlePortalColor;
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			final float redF = red / 255.0F;
			final float greenF = green / 255.0F;
			final float blueF = blue / 255.0F;
			fx.setRBGColorF(redF, greenF, blueF);
		}
	}

	public static void updateReddustFX(EntityFX fx, IBlockAccess blockAccess,
			double x, double y, double z) {
		if (redstoneColors != null) {
			final int level = blockAccess.getBlockMetadata((int) x, (int) y,
					(int) z);
			final int col = getRedstoneColor(level);

			if (col != -1) {
				final int red = col >> 16 & 255;
				final int green = col >> 8 & 255;
				final int blue = col & 255;
				final float redF = red / 255.0F;
				final float greenF = green / 255.0F;
				final float blueF = blue / 255.0F;
				fx.setRBGColorF(redF, greenF, blueF);
			}
		}
	}

	public static void updateUseDefaultColorMultiplier() {
		useDefaultColorMultiplier = foliageBirchColors == null
				&& foliagePineColors == null && swampGrassColors == null
				&& swampFoliageColors == null && blockPalettes == null
				&& Config.isSwampColors() && Config.isSmoothBiomes();
	}

	public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess) {
		if (waterColors != null) {
			final int x = (int) fx.posX;
			final int y = (int) fx.posY;
			final int z = (int) fx.posZ;
			final int col = getFluidColor(Blocks.water, blockAccess, x, y, z);
			final int red = col >> 16 & 255;
			final int green = col >> 8 & 255;
			final int blue = col & 255;
			float redF = red / 255.0F;
			float greenF = green / 255.0F;
			float blueF = blue / 255.0F;

			if (particleWaterColor >= 0) {
				final int redDrop = particleWaterColor >> 16 & 255;
				final int greenDrop = particleWaterColor >> 8 & 255;
				final int blueDrop = particleWaterColor & 255;
				redF *= redDrop / 255.0F;
				greenF *= greenDrop / 255.0F;
				blueF *= blueDrop / 255.0F;
			}

			fx.setRBGColorF(redF, greenF, blueF);
		}
	}
}

package net.minecraft.src;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

public class TextureAnimations {
	private static TextureAnimation[] textureAnimations = null;

	public static String[] getAnimationPropertiesDir(File tpDir) {
		final File dirAnim = new File(tpDir, "anim");

		if (!dirAnim.exists())
			return null;
		else if (!dirAnim.isDirectory())
			return null;
		else {
			final File[] propFiles = dirAnim.listFiles();

			if (propFiles == null)
				return null;
			else {
				final ArrayList list = new ArrayList();

				for (final File file : propFiles) {
					final String name = file.getName();

					if (!name.startsWith("custom_")
							&& name.endsWith(".properties") && file.isFile()
							&& file.canRead()) {
						Config.dbg("TextureAnimation: anim/" + file.getName());
						list.add("/anim/" + name);
					}
				}

				final String[] var7 = (String[]) list.toArray(new String[list
						.size()]);
				return var7;
			}
		}
	}

	public static String[] getAnimationPropertiesZip(File tpFile) {
		try {
			final ZipFile e = new ZipFile(tpFile);
			final Enumeration en = e.entries();
			final ArrayList list = new ArrayList();

			while (en.hasMoreElements()) {
				final ZipEntry props = (ZipEntry) en.nextElement();
				String name = props.getName();

				if (name.startsWith("assets/minecraft/mcpatcher/anim/")
						&& !name.startsWith("assets/minecraft/mcpatcher/anim/custom_")
						&& name.endsWith(".properties")) {
					final String assetsMcStr = "assets/minecraft/";
					name = name.substring(assetsMcStr.length());
					list.add(name);
				}
			}

			e.close();
			final String[] props1 = (String[]) list.toArray(new String[list
					.size()]);
			return props1;
		} catch (final IOException var7) {
			var7.printStackTrace();
			return null;
		}
	}

	public static byte[] getCustomTextureData(String imagePath, int tileWidth) {
		byte[] imageBytes = loadImage(imagePath, tileWidth);

		if (imageBytes == null) {
			imageBytes = loadImage("/anim" + imagePath, tileWidth);
		}

		return imageBytes;
	}

	public static TextureAnimation[] getTextureAnimations(IResourcePack rp) {
		if (!(rp instanceof AbstractResourcePack))
			return null;
		else {
			final AbstractResourcePack arp = (AbstractResourcePack) rp;
			final File tpFile = ResourceUtils.getResourcePackFile(arp);

			if (tpFile == null)
				return null;
			else if (!tpFile.exists())
				return null;
			else {
				String[] animPropNames = null;

				if (tpFile.isFile()) {
					animPropNames = getAnimationPropertiesZip(tpFile);
				} else {
					animPropNames = getAnimationPropertiesDir(tpFile);
				}

				if (animPropNames == null)
					return null;
				else {
					final ArrayList list = new ArrayList();

					for (final String propName : animPropNames) {
						Config.dbg("Texture animation: " + propName);

						try {
							final ResourceLocation e = new ResourceLocation(
									propName);
							final InputStream in = rp.getInputStream(e);
							final Properties props = new Properties();
							props.load(in);
							final TextureAnimation anim = makeTextureAnimation(
									props, e);

							if (anim != null) {
								final ResourceLocation locDstTex = new ResourceLocation(
										anim.getDstTex());

								if (Config.getDefiningResourcePack(locDstTex) != rp) {
									Config.dbg("Skipped: "
											+ propName
											+ ", target texture not loaded from same resource pack");
								} else {
									list.add(anim);
								}
							}
						} catch (final FileNotFoundException var12) {
							Config.warn("File not found: " + var12.getMessage());
						} catch (final IOException var13) {
							var13.printStackTrace();
						}
					}

					final TextureAnimation[] var14 = (TextureAnimation[]) list
							.toArray(new TextureAnimation[list.size()]);
					return var14;
				}
			}
		}
	}

	public static TextureAnimation[] getTextureAnimations(IResourcePack[] rps) {
		final ArrayList list = new ArrayList();

		for (final IResourcePack rp : rps) {
			final TextureAnimation[] tas = getTextureAnimations(rp);

			if (tas != null) {
				list.addAll(Arrays.asList(tas));
			}
		}

		final TextureAnimation[] var5 = (TextureAnimation[]) list
				.toArray(new TextureAnimation[list.size()]);
		return var5;
	}

	private static byte[] loadImage(String name, int targetWidth) {
		final GameSettings options = Config.getGameSettings();

		try {
			final ResourceLocation e = new ResourceLocation(name);
			final InputStream in = Config.getResourceStream(e);

			if (in == null)
				return null;
			else {
				BufferedImage image = readTextureImage(in);

				if (image == null)
					return null;
				else {
					if (targetWidth > 0 && image.getWidth() != targetWidth) {
						final double width = image.getHeight()
								/ image.getWidth();
						final int ai = (int) (targetWidth * width);
						image = scaleBufferedImage(image, targetWidth, ai);
					}

					final int var20 = image.getWidth();
					final int height = image.getHeight();
					final int[] var21 = new int[var20 * height];
					final byte[] byteBuf = new byte[var20 * height * 4];
					image.getRGB(0, 0, var20, height, var21, 0, var20);

					for (int l = 0; l < var21.length; ++l) {
						final int alpha = var21[l] >> 24 & 255;
						int red = var21[l] >> 16 & 255;
						int green = var21[l] >> 8 & 255;
						int blue = var21[l] & 255;

						if (options != null && options.anaglyph) {
							final int j3 = (red * 30 + green * 59 + blue * 11) / 100;
							final int l3 = (red * 30 + green * 70) / 100;
							final int j4 = (red * 30 + blue * 70) / 100;
							red = j3;
							green = l3;
							blue = j4;
						}

						byteBuf[l * 4 + 0] = (byte) red;
						byteBuf[l * 4 + 1] = (byte) green;
						byteBuf[l * 4 + 2] = (byte) blue;
						byteBuf[l * 4 + 3] = (byte) alpha;
					}

					return byteBuf;
				}
			}
		} catch (final FileNotFoundException var18) {
			return null;
		} catch (final Exception var19) {
			var19.printStackTrace();
			return null;
		}
	}

	public static TextureAnimation makeTextureAnimation(Properties props,
			ResourceLocation propLoc) {
		String texFrom = props.getProperty("from");
		String texTo = props.getProperty("to");
		final int x = Config.parseInt(props.getProperty("x"), -1);
		final int y = Config.parseInt(props.getProperty("y"), -1);
		final int width = Config.parseInt(props.getProperty("w"), -1);
		final int height = Config.parseInt(props.getProperty("h"), -1);

		if (texFrom != null && texTo != null) {
			if (x >= 0 && y >= 0 && width >= 0 && height >= 0) {
				final String basePath = TextureUtils.getBasePath(propLoc
						.getResourcePath());
				texFrom = TextureUtils.fixResourcePath(texFrom, basePath);
				texTo = TextureUtils.fixResourcePath(texTo, basePath);
				final byte[] imageBytes = getCustomTextureData(texFrom, width);

				if (imageBytes == null) {
					Config.warn("TextureAnimation: Source texture not found: "
							+ texTo);
					return null;
				} else {
					final ResourceLocation locTexTo = new ResourceLocation(
							texTo);

					if (!Config.hasResource(locTexTo)) {
						Config.warn("TextureAnimation: Target texture not found: "
								+ texTo);
						return null;
					} else {
						final ITextureObject destTex = TextureUtils
								.getTexture(locTexTo);

						if (destTex == null) {
							Config.warn("TextureAnimation: Target texture not found: "
									+ locTexTo);
							return null;
						} else {
							final int destTexId = destTex.getGlTextureId();
							final TextureAnimation anim = new TextureAnimation(
									texFrom, imageBytes, texTo, destTexId, x,
									y, width, height, props, 1);
							return anim;
						}
					}
				}
			} else {
				Config.warn("TextureAnimation: Invalid coordinates");
				return null;
			}
		} else {
			Config.warn("TextureAnimation: Source or target texture not specified");
			return null;
		}
	}

	private static BufferedImage readTextureImage(InputStream par1InputStream)
			throws IOException {
		final BufferedImage var2 = ImageIO.read(par1InputStream);
		par1InputStream.close();
		return var2;
	}

	public static void reset() {
		textureAnimations = null;
	}

	public static BufferedImage scaleBufferedImage(BufferedImage image,
			int width, int height) {
		final BufferedImage scaledImage = new BufferedImage(width, height, 2);
		final Graphics2D gr = scaledImage.createGraphics();
		gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gr.drawImage(image, 0, 0, width, height, (ImageObserver) null);
		return scaledImage;
	}

	public static void update() {
		final IResourcePack[] rps = Config.getResourcePacks();
		textureAnimations = getTextureAnimations(rps);
		updateAnimations();
	}

	public static void updateAnimations() {
		if (textureAnimations != null) {
			for (final TextureAnimation anim : textureAnimations) {
				anim.updateTexture();
			}
		}
	}

	public static void updateCustomAnimations() {
		if (textureAnimations != null) {
			if (Config.isAnimatedTextures()) {
				updateAnimations();
			}
		}
	}
}

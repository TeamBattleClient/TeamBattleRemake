package net.minecraft.client.renderer.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.Item;
import net.minecraft.src.Config;
import net.minecraft.src.ConnectedTextures;
import net.minecraft.src.Reflector;
import net.minecraft.src.TextureUtils;
import net.minecraft.src.WrUpdates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TextureMap extends AbstractTexture implements
		ITickableTextureObject, IIconRegister {
	public static final ResourceLocation locationBlocksTexture = new ResourceLocation(
			"textures/atlas/blocks.png");
	public static final ResourceLocation locationItemsTexture = new ResourceLocation(
			"textures/atlas/items.png");
	private static final Logger logger = LogManager.getLogger();
	public static TextureMap textureMapBlocks = null;
	public static TextureMap textureMapItems = null;
	public final String basePath;

	private int field_147636_j;
	private int field_147637_k = 1;
	private final TextureAtlasSprite[] iconGrid = null;
	private final int iconGridCountX = -1;
	private final double iconGridSizeU = -1.0D;

	private final double iconGridSizeV = -1.0D;
	private final List listAnimatedSprites = Lists.newArrayList();
	private final Map mapRegisteredSprites = Maps.newHashMap();
	private final Map mapUploadedSprites = Maps.newHashMap();
	private final TextureAtlasSprite missingImage = new TextureAtlasSprite(
			"missingno");
	/** 0 = terrain.png, 1 = items.png */
	public final int textureType;

	public TextureMap(int par1, String par2Str) {
		textureType = par1;
		basePath = par2Str;

		if (textureType == 0) {
			textureMapBlocks = this;
		}

		if (textureType == 1) {
			textureMapItems = this;
		}

		registerIcons();
	}

	private void addDebugSprite(TextureAtlasSprite ts, BufferedImage image) {
		if (ts.getFrameCount() < 1) {
			Config.warn("Debug sprite has no data: " + ts.getIconName());
		} else {
			final int[] data = ts.func_147965_a(0)[0];
			image.setRGB(ts.getOriginX(), ts.getOriginY(), ts.getIconWidth(),
					ts.getIconHeight(), data, 0, ts.getIconWidth());
		}
	}

	public void func_147632_b(int p_147632_1_) {
		field_147637_k = p_147632_1_;
	}

	public void func_147633_a(int p_147633_1_) {
		field_147636_j = p_147633_1_;
	}

	private ResourceLocation func_147634_a(ResourceLocation p_147634_1_,
			int p_147634_2_) {
		return isAbsoluteLocation(p_147634_1_) ? p_147634_2_ == 0 ? new ResourceLocation(
				p_147634_1_.getResourceDomain(), p_147634_1_.getResourcePath()
						+ ".png") : new ResourceLocation(
				p_147634_1_.getResourceDomain(), p_147634_1_.getResourcePath()
						+ "mipmap" + p_147634_2_ + ".png")
				: p_147634_2_ == 0 ? new ResourceLocation(
						p_147634_1_.getResourceDomain(),
						String.format("%s/%s%s", new Object[] { basePath,
								p_147634_1_.getResourcePath(), ".png" }))
						: new ResourceLocation(p_147634_1_.getResourceDomain(),
								String.format(
										"%s/mipmaps/%s.%d%s",
										new Object[] { basePath,
												p_147634_1_.getResourcePath(),
												Integer.valueOf(p_147634_2_),
												".png" }));
	}

	public TextureAtlasSprite getAtlasSprite(String par1Str) {
		TextureAtlasSprite var2 = (TextureAtlasSprite) mapUploadedSprites
				.get(par1Str);

		if (var2 == null) {
			var2 = missingImage;
		}

		return var2;
	}

	public TextureAtlasSprite getIconByUV(double u, double v) {
		if (iconGrid == null)
			return null;
		else {
			final int iu = (int) (u / iconGridSizeU);
			final int iv = (int) (v / iconGridSizeV);
			final int index = iv * iconGridCountX + iu;
			return index >= 0 && index <= iconGrid.length ? iconGrid[index]
					: null;
		}
	}

	public TextureAtlasSprite getIconSafe(String name) {
		return (TextureAtlasSprite) mapRegisteredSprites.get(name);
	}

	public int getMaxTextureIndex() {
		return mapRegisteredSprites.size();
	}

	public TextureAtlasSprite getMissingSprite() {
		return missingImage;
	}

	public TextureAtlasSprite getTextureExtry(String name) {
		return (TextureAtlasSprite) mapRegisteredSprites.get(name);
	}

	public int getTextureType() {
		return textureType;
	}

	private void initMissingImage() {
		int[] var1;

		if (field_147637_k > 1.0F) {
			missingImage.setIconWidth(32);
			missingImage.setIconHeight(32);
			var1 = new int[1024];
			System.arraycopy(TextureUtil.missingTextureData, 0, var1, 0,
					TextureUtil.missingTextureData.length);
			TextureUtil.func_147948_a(var1, 16, 16, 8);
		} else {
			var1 = TextureUtil.missingTextureData;
			missingImage.setIconWidth(16);
			missingImage.setIconHeight(16);
		}

		final int[][] var51 = new int[field_147636_j + 1][];
		var51[0] = var1;
		missingImage.setFramesTextureData(Lists
				.newArrayList(new int[][][] { var51 }));
		missingImage.setIndexInMap(0);
	}

	private boolean isAbsoluteLocation(ResourceLocation loc) {
		final String path = loc.getResourcePath();
		return isAbsoluteLocationPath(path);
	}

	private boolean isAbsoluteLocationPath(String resPath) {
		final String path = resPath.toLowerCase();
		return path.startsWith("mcpatcher/") || path.startsWith("optifine/");
	}

	private boolean isTerrainAnimationActive(TextureAtlasSprite ts) {
		return ts != TextureUtils.iconWaterStill
				&& ts != TextureUtils.iconWaterFlow ? ts != TextureUtils.iconLavaStill
				&& ts != TextureUtils.iconLavaFlow ? ts != TextureUtils.iconFireLayer0
				&& ts != TextureUtils.iconFireLayer1 ? ts == TextureUtils.iconPortal ? Config
				.isAnimatedPortal() : Config.isAnimatedTerrain()
				: Config.isAnimatedFire()
				: Config.isAnimatedLava()
				: Config.isAnimatedWater();
	}

	@Override
	public void loadTexture(IResourceManager par1ResourceManager)
			throws IOException {
		initMissingImage();
		func_147631_c();
		loadTextureAtlas(par1ResourceManager);
	}

	public void loadTextureAtlas(IResourceManager par1ResourceManager) {
		Config.dbg("Loading texture map: " + basePath);
		WrUpdates.finishCurrentUpdate();
		registerIcons();
		final int var2 = Minecraft.getGLMaximumTextureSize();
		final Stitcher var3 = new Stitcher(var2, var2, true, 0, field_147636_j);
		mapUploadedSprites.clear();
		listAnimatedSprites.clear();
		int var4 = Integer.MAX_VALUE;
		Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre,
				new Object[] { this });
		final Iterator var5 = mapRegisteredSprites.entrySet().iterator();

		while (true) {
			TextureAtlasSprite var8;

			while (var5.hasNext()) {
				final Entry var24 = (Entry) var5.next();
				final ResourceLocation var25 = new ResourceLocation(
						(String) var24.getKey());
				var8 = (TextureAtlasSprite) var24.getValue();
				final ResourceLocation sheetWidth = func_147634_a(var25, 0);

				if (!var8.hasCustomLoader(par1ResourceManager, var25)) {
					try {
						final IResource sheetHeight = par1ResourceManager
								.getResource(sheetWidth);
						final BufferedImage[] debugImage = new BufferedImage[1 + field_147636_j];
						debugImage[0] = ImageIO.read(sheetHeight
								.getInputStream());
						final TextureMetadataSection var26 = (TextureMetadataSection) sheetHeight
								.getMetadata("texture");

						if (var26 != null) {
							final List var28 = var26.func_148535_c();
							int var30;

							if (!var28.isEmpty()) {
								final int var18 = debugImage[0].getWidth();
								var30 = debugImage[0].getHeight();

								if (MathHelper.roundUpToPowerOfTwo(var18) != var18
										|| MathHelper
												.roundUpToPowerOfTwo(var30) != var30)
									throw new RuntimeException(
											"Unable to load extra miplevels, source-texture is not power of two");
							}

							final Iterator var182 = var28.iterator();

							while (var182.hasNext()) {
								var30 = ((Integer) var182.next()).intValue();

								if (var30 > 0 && var30 < debugImage.length - 1
										&& debugImage[var30] == null) {
									final ResourceLocation var32 = func_147634_a(
											var25, var30);

									try {
										debugImage[var30] = ImageIO
												.read(par1ResourceManager
														.getResource(var32)
														.getInputStream());
									} catch (final IOException var20) {
										logger.error(
												"Unable to load miplevel {} from: {}",
												new Object[] {
														Integer.valueOf(var30),
														var32, var20 });
									}
								}
							}
						}

						final AnimationMetadataSection var281 = (AnimationMetadataSection) sheetHeight
								.getMetadata("animation");
						var8.func_147964_a(debugImage, var281,
								field_147637_k > 1.0F);
					} catch (final RuntimeException var22) {
						logger.error("Unable to parse metadata from "
								+ sheetWidth, var22);
						continue;
					} catch (final IOException var23) {
						logger.error("Using missing texture, unable to load "
								+ sheetWidth + ", "
								+ var23.getClass().getName());
						continue;
					}

					var4 = Math
							.min(var4,
									Math.min(var8.getIconWidth(),
											var8.getIconHeight()));
					var3.addSprite(var8);
				} else if (!var8.load(par1ResourceManager, var25)) {
					var4 = Math
							.min(var4,
									Math.min(var8.getIconWidth(),
											var8.getIconHeight()));
					var3.addSprite(var8);
				}
			}

			int var241 = MathHelper.calculateLogBaseTwo(var4);

			if (var241 < 0) {
				var241 = 0;
			}

			if (var241 < field_147636_j) {
				logger.info(
						"{}: dropping miplevel from {} to {}, because of minTexel: {}",
						new Object[] { basePath,
								Integer.valueOf(field_147636_j),
								Integer.valueOf(var241), Integer.valueOf(var4) });
				field_147636_j = var241;
			}

			final Iterator var251 = mapRegisteredSprites.values().iterator();

			while (var251.hasNext()) {
				final TextureAtlasSprite sheetWidth1 = (TextureAtlasSprite) var251
						.next();

				try {
					sheetWidth1.func_147963_d(field_147636_j);
				} catch (final Throwable var19) {
					final CrashReport debugImage1 = CrashReport
							.makeCrashReport(var19, "Applying mipmap");
					final CrashReportCategory var261 = debugImage1
							.makeCategory("Sprite being mipmapped");
					var261.addCrashSectionCallable("Sprite name",
							new Callable() {

								@Override
								public String call() {
									return sheetWidth1.getIconName();
								}
							});
					var261.addCrashSectionCallable("Sprite size",
							new Callable() {

								@Override
								public String call() {
									return sheetWidth1.getIconWidth() + " x "
											+ sheetWidth1.getIconHeight();
								}
							});
					var261.addCrashSectionCallable("Sprite frames",
							new Callable() {

								@Override
								public String call() {
									return sheetWidth1.getFrameCount()
											+ " frames";
								}
							});
					var261.addCrashSection("Mipmap levels",
							Integer.valueOf(field_147636_j));
					throw new ReportedException(debugImage1);
				}
			}

			missingImage.func_147963_d(field_147636_j);
			var3.addSprite(missingImage);

			try {
				var3.doStitch();
			} catch (final StitcherException var181) {
				throw var181;
			}

			Config.dbg("Texture size: " + basePath + ", "
					+ var3.getCurrentWidth() + "x" + var3.getCurrentHeight());
			final int sheetWidth2 = var3.getCurrentWidth();
			final int sheetHeight1 = var3.getCurrentHeight();
			BufferedImage debugImage2 = null;

			if (System.getProperty("saveTextureMap", "false").equalsIgnoreCase(
					"true")) {
				debugImage2 = makeDebugImage(sheetWidth2, sheetHeight1);
			}

			logger.info(
					"Created: {}x{} {}-atlas",
					new Object[] { Integer.valueOf(var3.getCurrentWidth()),
							Integer.valueOf(var3.getCurrentHeight()), basePath });
			TextureUtil.func_147946_a(getGlTextureId(), field_147636_j,
					var3.getCurrentWidth(), var3.getCurrentHeight(),
					field_147637_k);
			final HashMap var262 = Maps.newHashMap(mapRegisteredSprites);
			Iterator var282 = var3.getStichSlots().iterator();

			while (var282.hasNext()) {
				var8 = (TextureAtlasSprite) var282.next();
				final String var301 = var8.getIconName();
				var262.remove(var301);
				mapUploadedSprites.put(var301, var8);

				try {
					TextureUtil.func_147955_a(var8.func_147965_a(0),
							var8.getIconWidth(), var8.getIconHeight(),
							var8.getOriginX(), var8.getOriginY(), false, false);

					if (debugImage2 != null) {
						addDebugSprite(var8, debugImage2);
					}
				} catch (final Throwable var21) {
					final CrashReport var321 = CrashReport.makeCrashReport(
							var21, "Stitching texture atlas");
					final CrashReportCategory var33 = var321
							.makeCategory("Texture being stitched together");
					var33.addCrashSection("Atlas path", basePath);
					var33.addCrashSection("Sprite", var8);
					throw new ReportedException(var321);
				}

				if (var8.hasAnimationMetadata()) {
					listAnimatedSprites.add(var8);
				} else {
					var8.clearFramesTextureData();
				}
			}

			var282 = var262.values().iterator();

			while (var282.hasNext()) {
				var8 = (TextureAtlasSprite) var282.next();
				var8.copyFrom(missingImage);
			}

			if (debugImage2 != null) {
				writeDebugImage(debugImage2,
						"debug_" + basePath.replace('/', '_') + ".png");
			}

			Reflector.callVoid(
					Reflector.ForgeHooksClient_onTextureStitchedPost,
					new Object[] { this });
			return;
		}
	}

	public void loadTextureSafe(IResourceManager rm) {
		try {
			loadTexture(rm);
		} catch (final IOException var3) {
			Config.warn("Error loading texture map: " + basePath);
			var3.printStackTrace();
		}
	}

	private BufferedImage makeDebugImage(int sheetWidth, int sheetHeight) {
		final BufferedImage image = new BufferedImage(sheetWidth, sheetHeight,
				2);
		final Graphics2D g = image.createGraphics();
		g.setPaint(new Color(255, 255, 0));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		return image;
	}

	@Override
	public IIcon registerIcon(String par1Str) {
		if (par1Str == null)
			throw new IllegalArgumentException("Name cannot be null!");
		else if (par1Str.indexOf(92) != -1 && !isAbsoluteLocationPath(par1Str))
			throw new IllegalArgumentException("Name cannot contain slashes!");
		else {
			Object var2 = mapRegisteredSprites.get(par1Str);

			if (var2 == null && textureType == 1
					&& Reflector.ModLoader_getCustomAnimationLogic.exists()) {
				var2 = Reflector.call(
						Reflector.ModLoader_getCustomAnimationLogic,
						new Object[] { par1Str });
			}

			if (var2 == null) {
				if (textureType == 1) {
					if ("clock".equals(par1Str)) {
						var2 = new TextureClock(par1Str);
					} else if ("compass".equals(par1Str)) {
						var2 = new TextureCompass(par1Str);
					} else {
						var2 = new TextureAtlasSprite(par1Str);
					}
				} else {
					var2 = new TextureAtlasSprite(par1Str);
				}

				mapRegisteredSprites.put(par1Str, var2);

				if (var2 instanceof TextureAtlasSprite) {
					final TextureAtlasSprite tas = (TextureAtlasSprite) var2;
					tas.setIndexInMap(mapRegisteredSprites.size());
				}
			}

			return (IIcon) var2;
		}
	}

	private void registerIcons() {
		mapRegisteredSprites.clear();
		Iterator var1;

		if (textureType == 0) {
			var1 = Block.blockRegistry.iterator();

			while (var1.hasNext()) {
				final Block var3 = (Block) var1.next();

				if (var3.getMaterial() != Material.air) {
					var3.registerBlockIcons(this);
				}
			}

			Minecraft.getMinecraft().renderGlobal
					.registerDestroyBlockIcons(this);
			RenderManager.instance.updateIcons(this);
			ConnectedTextures.updateIcons(this);
		}

		var1 = Item.itemRegistry.iterator();

		while (var1.hasNext()) {
			final Item var31 = (Item) var1.next();

			if (var31 != null && var31.getSpriteNumber() == textureType) {
				var31.registerIcons(this);
			}
		}
	}

	public boolean setTextureEntry(String name, TextureAtlasSprite entry) {
		if (!mapRegisteredSprites.containsKey(name)) {
			mapRegisteredSprites.put(name, entry);
			entry.setIndexInMap(mapRegisteredSprites.size());
			return true;
		} else
			return false;
	}

	@Override
	public void tick() {
		updateAnimations();
	}

	public void updateAnimations() {
		TextureUtil.bindTexture(getGlTextureId());
		final Iterator var1 = listAnimatedSprites.iterator();

		while (var1.hasNext()) {
			final TextureAtlasSprite var2 = (TextureAtlasSprite) var1.next();

			if (textureType == 0) {
				if (!isTerrainAnimationActive(var2)) {
					continue;
				}
			} else if (textureType == 1 && !Config.isAnimatedItems()) {
				continue;
			}

			var2.updateAnimation();
		}
	}

	private void writeDebugImage(BufferedImage image, String pngPath) {
		try {
			ImageIO.write(image, "png", new File(
					Config.getMinecraft().mcDataDir, pngPath));
		} catch (final Exception var4) {
			var4.printStackTrace();
		}
	}
}

package net.minecraft.src;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.LoadingScreenRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.Blocks;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

public class Config {
	private static int antialiasingLevel = 0;

	private static int availableProcessors = 0;

	public static final Float DEF_ALPHA_FUNC_LEVEL = Float.valueOf(0.1F);

	public static final Integer DEF_CHUNKS_LIMIT = Integer.valueOf(25);

	public static final Boolean DEF_DYNAMIC_UPDATES = Boolean.valueOf(false);

	public static final Boolean DEF_FOG_FANCY = Boolean.valueOf(true);

	public static final Float DEF_FOG_START = Float.valueOf(0.2F);

	public static final Boolean DEF_LOAD_CHUNKS_FAR = Boolean.valueOf(false);

	public static final Integer DEF_MIPMAP_LEVEL = Integer.valueOf(0);

	public static final Integer DEF_MIPMAP_TYPE = Integer.valueOf(9984);

	public static final Boolean DEF_OCCLUSION_ENABLED = Boolean.valueOf(false);

	public static final Boolean DEF_OPTIMIZE_RENDER_DISTANCE = Boolean
			.valueOf(false);

	public static final Integer DEF_PRELOADED_CHUNKS = Integer.valueOf(0);

	public static final Integer DEF_UPDATES_PER_FRAME = Integer.valueOf(3);

	private static DisplayMode desktopDisplayMode = null;

	private static boolean desktopModeChecked = false;

	private static boolean fullscreenModeChecked = false;

	private static GameSettings gameSettings = null;

	private static boolean initialized = false;

	public static final String MC_VERSION = "1.7.10";

	private static Minecraft minecraft = null;

	private static Thread minecraftThread = null;

	private static String newRelease = null;

	public static final String OF_EDITION = "HD_U";

	public static final String OF_NAME = "OptiFine";

	public static final String OF_RELEASE = "A4";

	private static PrintStream systemOut = new PrintStream(
			new FileOutputStream(FileDescriptor.out));

	private static int texturePackClouds = 0;

	public static final String VERSION = "OptiFine_1.7.10_HD_U_A4";

	public static boolean waterOpacityChanged = false;

	public static boolean zoomMode = false;

	public static Object[] addObjectToArray(Object[] arr, Object obj) {
		if (arr == null)
			throw new NullPointerException("The given array is NULL");
		else {
			final int arrLen = arr.length;
			final int newLen = arrLen + 1;
			final Object[] newArr = (Object[]) Array.newInstance(arr.getClass()
					.getComponentType(), newLen);
			System.arraycopy(arr, 0, newArr, 0, arrLen);
			newArr[arrLen] = obj;
			return newArr;
		}
	}

	public static String arrayToString(int[] arr) {
		if (arr == null)
			return "";
		else {
			final StringBuffer buf = new StringBuffer(arr.length * 5);

			for (int i = 0; i < arr.length; ++i) {
				final int x = arr[i];

				if (i > 0) {
					buf.append(", ");
				}

				buf.append(String.valueOf(x));
			}

			return buf.toString();
		}
	}

	public static String arrayToString(Object[] arr) {
		if (arr == null)
			return "";
		else {
			final StringBuffer buf = new StringBuffer(arr.length * 5);

			for (int i = 0; i < arr.length; ++i) {
				final Object obj = arr[i];

				if (i > 0) {
					buf.append(", ");
				}

				buf.append(String.valueOf(obj));
			}

			return buf.toString();
		}
	}

	public static boolean between(int val, int min, int max) {
		return val >= min && val <= max;
	}

	public static void checkDisplayMode() {
		try {
			if (minecraft.isFullScreen()) {
				if (fullscreenModeChecked)
					return;

				fullscreenModeChecked = true;
				desktopModeChecked = false;
				final DisplayMode e = Display.getDisplayMode();
				final Dimension dim = getFullscreenDimension();

				if (dim == null)
					return;

				if (e.getWidth() == dim.width && e.getHeight() == dim.height)
					return;

				final DisplayMode newMode = getDisplayMode(dim);

				if (newMode == null)
					return;

				Display.setDisplayMode(newMode);
				minecraft.displayWidth = Display.getDisplayMode().getWidth();
				minecraft.displayHeight = Display.getDisplayMode().getHeight();

				if (minecraft.displayWidth <= 0) {
					minecraft.displayWidth = 1;
				}

				if (minecraft.displayHeight <= 0) {
					minecraft.displayHeight = 1;
				}

				if (minecraft.currentScreen != null) {
					final ScaledResolution sr = new ScaledResolution(minecraft,
							minecraft.displayWidth, minecraft.displayHeight);
					final int sw = sr.getScaledWidth();
					final int sh = sr.getScaledHeight();
					minecraft.currentScreen.setWorldAndResolution(minecraft,
							sw, sh);
				}

				minecraft.loadingScreen = new LoadingScreenRenderer(minecraft);
				updateFramebufferSize();
				Display.setFullscreen(true);
				minecraft.gameSettings.updateVSync();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			} else {
				if (desktopModeChecked)
					return;

				desktopModeChecked = true;
				fullscreenModeChecked = false;
				minecraft.gameSettings.updateVSync();
				Display.update();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		} catch (final Exception var6) {
			var6.printStackTrace();
		}
	}

	public static void checkDisplaySettings() {
		if (getAntialiasingLevel() > 0) {
			final int samples = getAntialiasingLevel();
			final DisplayMode displayMode = Display.getDisplayMode();
			dbg("FSAA Samples: " + samples);

			try {
				Display.destroy();
				Display.setDisplayMode(displayMode);
				Display.create(new PixelFormat().withDepthBits(24).withSamples(
						samples));
			} catch (final LWJGLException var9) {
				warn("Error setting FSAA: " + samples + "x");
				var9.printStackTrace();

				try {
					Display.setDisplayMode(displayMode);
					Display.create(new PixelFormat().withDepthBits(24));
				} catch (final LWJGLException var8) {
					var8.printStackTrace();

					try {
						Display.setDisplayMode(displayMode);
						Display.create();
					} catch (final LWJGLException var7) {
						var7.printStackTrace();
					}
				}
			}

			if (Util.getOSType() != Util.EnumOS.OSX) {
				try {
					final File e = new File(minecraft.mcDataDir, "assets");
					final ByteBuffer bufIcon16 = readIconImage(new File(e,
							"/icons/icon_16x16.png"));
					final ByteBuffer bufIcon32 = readIconImage(new File(e,
							"/icons/icon_32x32.png"));
					final ByteBuffer[] buf = new ByteBuffer[] { bufIcon16,
							bufIcon32 };
					Display.setIcon(buf);
				} catch (final IOException var6) {
					dbg(var6.getClass().getName() + ": " + var6.getMessage());
				}
			}
		}
	}

	public static void checkGlError(String loc) {
		final int i = GL11.glGetError();

		if (i != 0) {
			final String text = GLU.gluErrorString(i);
			dbg("OpenGlError: " + i + " (" + text + "), at: " + loc);
		}
	}

	public static void checkInitialized() {
		if (!initialized) {
			if (Display.isCreated()) {
				initialized = true;
				checkOpenGlCaps();
				startVersionCheckThread();
			}
		}
	}

	private static void checkOpenGlCaps() {
		log("");
		log(getVersion());
		log("" + new Date());
		log("OS: " + System.getProperty("os.name") + " ("
				+ System.getProperty("os.arch") + ") version "
				+ System.getProperty("os.version"));
		log("Java: " + System.getProperty("java.version") + ", "
				+ System.getProperty("java.vendor"));
		log("VM: " + System.getProperty("java.vm.name") + " ("
				+ System.getProperty("java.vm.info") + "), "
				+ System.getProperty("java.vm.vendor"));
		log("LWJGL: " + Sys.getVersion());
		log("OpenGL: " + GL11.glGetString(GL11.GL_RENDERER) + " version "
				+ GL11.glGetString(GL11.GL_VERSION) + ", "
				+ GL11.glGetString(GL11.GL_VENDOR));
		final int ver = getOpenGlVersion();
		final String verStr = "" + ver / 10 + "." + ver % 10;
		log("OpenGL Version: " + verStr);

		if (!GLContext.getCapabilities().OpenGL12) {
			log("OpenGL Mipmap levels: Not available (GL12.GL_TEXTURE_MAX_LEVEL)");
		}

		if (!GLContext.getCapabilities().GL_NV_fog_distance) {
			log("OpenGL Fancy fog: Not available (GL_NV_fog_distance)");
		}

		if (!GLContext.getCapabilities().GL_ARB_occlusion_query) {
			log("OpenGL Occlussion culling: Not available (GL_ARB_occlusion_query)");
		}

		final int maxTexSize = Minecraft.getGLMaximumTextureSize();
		dbg("Maximum texture size: " + maxTexSize + "x" + maxTexSize);
	}

	public static int compareRelease(String rel1, String rel2) {
		final String[] rels1 = splitRelease(rel1);
		final String[] rels2 = splitRelease(rel2);
		final String branch1 = rels1[0];
		final String branch2 = rels2[0];

		if (!branch1.equals(branch2))
			return branch1.compareTo(branch2);
		else {
			final int rev1 = parseInt(rels1[1], -1);
			final int rev2 = parseInt(rels2[1], -1);

			if (rev1 != rev2)
				return rev1 - rev2;
			else {
				final String suf1 = rels1[2];
				final String suf2 = rels2[2];
				return suf1.compareTo(suf2);
			}
		}
	}

	public static void dbg(String s) {
		systemOut.print("[OptiFine] ");
		systemOut.println(s);
	}

	public static boolean equals(Object o1, Object o2) {
		return o1 == o2 ? true : o1 == null ? false : o1.equals(o2);
	}

	public static void error(String s) {
		systemOut.print("[OptiFine] [ERROR] ");
		systemOut.println(s);
	}

	public static float getAlphaFuncLevel() {
		return DEF_ALPHA_FUNC_LEVEL.floatValue();
	}

	public static float getAmbientOcclusionLevel() {
		return gameSettings.ofAoLevel;
	}

	public static int getAntialiasingLevel() {
		return antialiasingLevel;
	}

	public static int getAvailableProcessors() {
		return availableProcessors;
	}

	public static int getChunkViewDistance() {
		if (gameSettings == null)
			return 10;
		else {
			final int chunkDistance = gameSettings.renderDistanceChunks;
			return chunkDistance;
		}
	}

	public static IResourcePack getDefaultResourcePack() {
		return minecraft.getResourcePackRepository().rprDefaultResourcePack;
	}

	public static IResourcePack getDefiningResourcePack(ResourceLocation loc) {
		final IResourcePack[] rps = getResourcePacks();

		for (int i = rps.length - 1; i >= 0; --i) {
			final IResourcePack rp = rps[i];

			if (rp.resourceExists(loc))
				return rp;
		}

		if (getDefaultResourcePack().resourceExists(loc))
			return getDefaultResourcePack();
		else
			return null;
	}

	public static DisplayMode getDesktopDisplayMode() {
		return desktopDisplayMode;
	}

	public static DisplayMode getDisplayMode(Dimension dim)
			throws LWJGLException {
		final DisplayMode[] modes = Display.getAvailableDisplayModes();

		for (final DisplayMode dm : modes) {
			if (dm.getWidth() == dim.width
					&& dm.getHeight() == dim.height
					&& (desktopDisplayMode == null || dm.getBitsPerPixel() == desktopDisplayMode
							.getBitsPerPixel()
							&& dm.getFrequency() == desktopDisplayMode
									.getFrequency()))
				return dm;
		}

		return desktopDisplayMode;
	}

	public static float getFogStart() {
		return gameSettings.ofFogStart;
	}

	public static Dimension getFullscreenDimension() {
		if (desktopDisplayMode == null)
			return null;
		else if (gameSettings == null)
			return new Dimension(desktopDisplayMode.getWidth(),
					desktopDisplayMode.getHeight());
		else {
			final String dimStr = gameSettings.ofFullscreenMode;

			if (dimStr.equals("Default"))
				return new Dimension(desktopDisplayMode.getWidth(),
						desktopDisplayMode.getHeight());
			else {
				final String[] dimStrs = tokenize(dimStr, " x");
				return dimStrs.length < 2 ? new Dimension(
						desktopDisplayMode.getWidth(),
						desktopDisplayMode.getHeight()) : new Dimension(
						parseInt(dimStrs[0], -1), parseInt(dimStrs[1], -1));
			}
		}
	}

	public static DisplayMode[] getFullscreenDisplayModes() {
		try {
			final DisplayMode[] e = Display.getAvailableDisplayModes();
			final ArrayList list = new ArrayList();

			for (final DisplayMode comp : e) {
				if (desktopDisplayMode == null
						|| comp.getBitsPerPixel() == desktopDisplayMode
								.getBitsPerPixel()
						&& comp.getFrequency() == desktopDisplayMode
								.getFrequency()) {
					list.add(comp);
				}
			}

			final DisplayMode[] var5 = (DisplayMode[]) list
					.toArray(new DisplayMode[list.size()]);
			final Comparator var6 = new Comparator() {
				@Override
				public int compare(Object o1, Object o2) {
					final DisplayMode dm1 = (DisplayMode) o1;
					final DisplayMode dm2 = (DisplayMode) o2;
					return dm1.getWidth() != dm2.getWidth() ? dm2.getWidth()
							- dm1.getWidth() : dm1.getHeight() != dm2
							.getHeight() ? dm2.getHeight() - dm1.getHeight()
							: 0;
				}
			};
			Arrays.sort(var5, var6);
			return var5;
		} catch (final Exception var4) {
			var4.printStackTrace();
			return new DisplayMode[] { desktopDisplayMode };
		}
	}

	public static String[] getFullscreenModes() {
		final DisplayMode[] modes = getFullscreenDisplayModes();
		final String[] names = new String[modes.length];

		for (int i = 0; i < modes.length; ++i) {
			final DisplayMode mode = modes[i];
			final String name = "" + mode.getWidth() + "x" + mode.getHeight();
			names[i] = name;
		}

		return names;
	}

	public static GameSettings getGameSettings() {
		return gameSettings;
	}

	public static int getMaxDynamicTileWidth() {
		return 64;
	}

	public static Minecraft getMinecraft() {
		return minecraft;
	}

	public static int getMipmapType() {
		if (gameSettings == null)
			return DEF_MIPMAP_TYPE.intValue();
		else {
			switch (gameSettings.ofMipmapType) {
			case 0:
				return 9984;

			case 1:
				return 9986;

			case 2:
				if (isMultiTexture())
					return 9985;

				return 9986;

			case 3:
				if (isMultiTexture())
					return 9987;

				return 9986;

			default:
				return 9984;
			}
		}
	}

	public static String getNewRelease() {
		return newRelease;
	}

	private static int getOpenGlVersion() {
		return !GLContext.getCapabilities().OpenGL11 ? 10 : !GLContext
				.getCapabilities().OpenGL12 ? 11
				: !GLContext.getCapabilities().OpenGL13 ? 12 : !GLContext
						.getCapabilities().OpenGL14 ? 13 : !GLContext
						.getCapabilities().OpenGL15 ? 14 : !GLContext
						.getCapabilities().OpenGL20 ? 15 : !GLContext
						.getCapabilities().OpenGL21 ? 20 : !GLContext
						.getCapabilities().OpenGL30 ? 21 : !GLContext
						.getCapabilities().OpenGL31 ? 30 : !GLContext
						.getCapabilities().OpenGL32 ? 31 : !GLContext
						.getCapabilities().OpenGL33 ? 32 : !GLContext
						.getCapabilities().OpenGL40 ? 33 : 40;
	}

	public static int getPreloadedChunks() {
		return gameSettings.ofPreloadedChunks;
	}

	public static int getRandom(int x, int y, int z, int face) {
		int rand = intHash(face + 37);
		rand = intHash(rand + x);
		rand = intHash(rand + z);
		rand = intHash(rand + y);
		return rand;
	}

	public static RenderGlobal getRenderGlobal() {
		return minecraft == null ? null : minecraft.renderGlobal;
	}

	public static IResource getResource(ResourceLocation location)
			throws IOException {
		return minecraft.getResourceManager().getResource(location);
	}

	public static IResourceManager getResourceManager() {
		return minecraft.getResourceManager();
	}

	public static String getResourcePackNames() {
		final IResourcePack[] rps = getResourcePacks();

		if (rps.length <= 0)
			return getDefaultResourcePack().getPackName();
		else {
			final String[] names = new String[rps.length];

			for (int nameStr = 0; nameStr < rps.length; ++nameStr) {
				names[nameStr] = rps[nameStr].getPackName();
			}

			final String var3 = arrayToString(names);
			return var3;
		}
	}

	public static IResourcePack[] getResourcePacks() {
		final ResourcePackRepository rep = minecraft
				.getResourcePackRepository();
		final List entries = rep.getRepositoryEntries();
		final ArrayList list = new ArrayList();
		final Iterator rps = entries.iterator();

		while (rps.hasNext()) {
			final ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry) rps
					.next();
			list.add(entry.getResourcePack());
		}

		final IResourcePack[] rps1 = (IResourcePack[]) list
				.toArray(new IResourcePack[list.size()]);
		return rps1;
	}

	public static InputStream getResourceStream(
			IResourceManager resourceManager, ResourceLocation location)
			throws IOException {
		final IResource res = resourceManager.getResource(location);
		return res == null ? null : res.getInputStream();
	}

	public static InputStream getResourceStream(ResourceLocation location)
			throws IOException {
		return getResourceStream(minecraft.getResourceManager(), location);
	}

	public static IIcon getSideGrassTexture(IBlockAccess blockAccess, int x,
			int y, int z, int side, IIcon icon) {
		if (!isBetterGrass())
			return icon;
		else {
			IIcon fullIcon = TextureUtils.iconGrassTop;
			Object destBlock = Blocks.grass;

			if (icon == TextureUtils.iconMyceliumSide) {
				fullIcon = TextureUtils.iconMyceliumTop;
				destBlock = Blocks.mycelium;
			}

			if (isBetterGrassFancy()) {
				--y;

				switch (side) {
				case 2:
					--z;
					break;

				case 3:
					++z;
					break;

				case 4:
					--x;
					break;

				case 5:
					++x;
				}

				final Block block = blockAccess.getBlock(x, y, z);

				if (block != destBlock)
					return icon;
			}

			return fullIcon;
		}
	}

	public static IIcon getSideSnowGrassTexture(IBlockAccess blockAccess,
			int x, int y, int z, int side) {
		if (!isBetterGrass())
			return TextureUtils.iconGrassSideSnowed;
		else {
			if (isBetterGrassFancy()) {
				switch (side) {
				case 2:
					--z;
					break;

				case 3:
					++z;
					break;

				case 4:
					--x;
					break;

				case 5:
					++x;
				}

				final Block block = blockAccess.getBlock(x, y, z);

				if (block != Blocks.snow_layer && block != Blocks.snow)
					return TextureUtils.iconGrassSideSnowed;
			}

			return TextureUtils.iconSnow;
		}
	}

	public static TextureManager getTextureManager() {
		return minecraft.getTextureManager();
	}

	public static int getUpdatesPerFrame() {
		return gameSettings.ofChunkUpdates;
	}

	public static String getVersion() {
		return "OptiFine_1.7.10_HD_U_A4";
	}

	public static WorldServer getWorldServer() {
		if (minecraft == null)
			return null;
		else {
			final WorldClient world = minecraft.theWorld;

			if (world == null)
				return null;
			else {
				final IntegratedServer is = minecraft.getIntegratedServer();

				if (is == null)
					return null;
				else {
					final WorldProvider wp = world.provider;

					if (wp == null)
						return null;
					else {
						final int wd = wp.dimensionId;
						final WorldServer ws = is.worldServerForDimension(wd);
						return ws;
					}
				}
			}
		}
	}

	public static boolean hasResource(IResourceManager resourceManager,
			ResourceLocation location) {
		try {
			final IResource e = resourceManager.getResource(location);
			return e != null;
		} catch (final IOException var3) {
			return false;
		}
	}

	public static boolean hasResource(ResourceLocation location) {
		try {
			final IResource e = getResource(location);
			return e != null;
		} catch (final IOException var2) {
			return false;
		}
	}

	public static void initDisplay() {
		checkInitialized();
		antialiasingLevel = gameSettings.ofAaLevel;
		checkDisplaySettings();
		checkDisplayMode();
		minecraftThread = Thread.currentThread();
		updateThreadPriorities();
	}

	public static void initGameSettings(GameSettings settings) {
		gameSettings = settings;
		minecraft = Minecraft.getMinecraft();
		desktopDisplayMode = Display.getDesktopDisplayMode();
		updateAvailableProcessors();
	}

	public static int intHash(int x) {
		x = x ^ 61 ^ x >> 16;
		x += x << 3;
		x ^= x >> 4;
		x *= 668265261;
		x ^= x >> 15;
		return x;
	}

	public static boolean isAnimatedExplosion() {
		return gameSettings.ofAnimatedExplosion;
	}

	public static boolean isAnimatedFire() {
		return gameSettings.ofAnimatedFire;
	}

	public static boolean isAnimatedFlame() {
		return gameSettings.ofAnimatedFlame;
	}

	public static boolean isAnimatedItems() {
		return gameSettings.ofAnimatedItems;
	}

	public static boolean isAnimatedLava() {
		return gameSettings.ofAnimatedLava != 2;
	}

	public static boolean isAnimatedPortal() {
		return gameSettings.ofAnimatedPortal;
	}

	public static boolean isAnimatedRedstone() {
		return gameSettings.ofAnimatedRedstone;
	}

	public static boolean isAnimatedSmoke() {
		return gameSettings.ofAnimatedSmoke;
	}

	public static boolean isAnimatedTerrain() {
		return gameSettings.ofAnimatedTerrain;
	}

	public static boolean isAnimatedTextures() {
		return gameSettings.ofAnimatedTextures;
	}

	public static boolean isAnimatedWater() {
		return gameSettings.ofAnimatedWater != 2;
	}

	public static boolean isBetterGrass() {
		return gameSettings.ofBetterGrass != 3;
	}

	public static boolean isBetterGrassFancy() {
		return gameSettings.ofBetterGrass == 2;
	}

	public static boolean isBetterSnow() {
		return gameSettings.ofBetterSnow;
	}

	public static boolean isClearWater() {
		return gameSettings.ofClearWater;
	}

	public static boolean isCloudsFancy() {
		return gameSettings.ofClouds != 0 ? gameSettings.ofClouds == 2
				: texturePackClouds != 0 ? texturePackClouds == 2
						: gameSettings.fancyGraphics;
	}

	public static boolean isCloudsOff() {
		return gameSettings.ofClouds == 3;
	}

	public static boolean isConnectedTextures() {
		return gameSettings.ofConnectedTextures != 3;
	}

	public static boolean isConnectedTexturesFancy() {
		return gameSettings.ofConnectedTextures == 2;
	}

	public static boolean isCustomColors() {
		return gameSettings.ofCustomColors;
	}

	public static boolean isCustomFonts() {
		return gameSettings.ofCustomFonts;
	}

	public static boolean isCustomSky() {
		return gameSettings.ofCustomSky;
	}

	public static boolean isDepthFog() {
		return gameSettings.ofDepthFog;
	}

	public static boolean isDrippingWaterLava() {
		return gameSettings.ofDrippingWaterLava;
	}

	public static boolean isDroppedItemsFancy() {
		return gameSettings.ofDroppedItems == 0 ? gameSettings.fancyGraphics
				: gameSettings.ofDroppedItems == 2;
	}

	public static boolean isDynamicUpdates() {
		return gameSettings.ofChunkUpdatesDynamic;
	}

	public static boolean isFancyFogAvailable() {
		return GLContext.getCapabilities().GL_NV_fog_distance;
	}

	public static boolean isFastRender() {
		return gameSettings.ofFastRender;
	}

	public static boolean isFogFancy() {
		return !isFancyFogAvailable() ? false : gameSettings.ofFogType == 2;
	}

	public static boolean isFogFast() {
		return gameSettings.ofFogType == 1;
	}

	public static boolean isFogOff() {
		return gameSettings.ofFogType == 3;
	}

	public static boolean isFromDefaultResourcePack(ResourceLocation loc) {
		final IResourcePack rp = getDefiningResourcePack(loc);
		return rp == getDefaultResourcePack();
	}

	public static boolean isGeneratedLava() {
		return gameSettings.ofAnimatedLava == 1;
	}

	public static boolean isGeneratedWater() {
		return gameSettings.ofAnimatedWater == 1;
	}

	public static boolean isGrassFancy() {
		return gameSettings.ofGrass == 0 ? gameSettings.fancyGraphics
				: gameSettings.ofGrass == 2;
	}

	public static boolean isLazyChunkLoading() {
		return !isSingleProcessor() ? false : gameSettings.ofLazyChunkLoading;
	}

	public static boolean isLoadChunksFar() {
		return gameSettings.ofLoadFar;
	}

	public static boolean isMinecraftThread() {
		return Thread.currentThread() == minecraftThread;
	}

	public static boolean isMultiTexture() {
		return false;
	}

	public static boolean isNaturalTextures() {
		return gameSettings.ofNaturalTextures;
	}

	public static boolean isOcclusionAvailable() {
		return GLContext.getCapabilities().GL_ARB_occlusion_query;
	}

	public static boolean isOcclusionEnabled() {
		return gameSettings.advancedOpengl;
	}

	public static boolean isOcclusionFancy() {
		return !isOcclusionEnabled() ? false : gameSettings.ofOcclusionFancy;
	}

	public static boolean isPortalParticles() {
		return gameSettings.ofPortalParticles;
	}

	public static boolean isPotionParticles() {
		return gameSettings.ofPotionParticles;
	}

	public static boolean isRainFancy() {
		return gameSettings.ofRain == 0 ? gameSettings.fancyGraphics
				: gameSettings.ofRain == 2;
	}

	public static boolean isRainOff() {
		return gameSettings.ofRain == 3;
	}

	public static boolean isRainSplash() {
		return gameSettings.ofRainSplash;
	}

	public static boolean isRandomMobs() {
		return gameSettings.ofRandomMobs;
	}

	public static boolean isShowCapes() {
		return gameSettings.ofShowCapes;
	}

	public static boolean isSingleProcessor() {
		return getAvailableProcessors() <= 1;
	}

	public static boolean isSkyEnabled() {
		return gameSettings.ofSky;
	}

	public static boolean isSmoothBiomes() {
		return gameSettings.ofSmoothBiomes;
	}

	public static boolean isSmoothWorld() {
		return !isSingleProcessor() ? false : gameSettings.ofSmoothWorld;
	}

	public static boolean isStarsEnabled() {
		return gameSettings.ofStars;
	}

	public static boolean isSunMoonEnabled() {
		return gameSettings.ofSunMoon;
	}

	public static boolean isSwampColors() {
		return gameSettings.ofSwampColors;
	}

	public static boolean isTimeDayOnly() {
		return gameSettings.ofTime == 1;
	}

	public static boolean isTimeDefault() {
		return gameSettings.ofTime == 0 || gameSettings.ofTime == 2;
	}

	public static boolean isTimeNightOnly() {
		return gameSettings.ofTime == 3;
	}

	public static boolean isTranslucentBlocksFancy() {
		return gameSettings.ofTranslucentBlocks == 2;
	}

	public static boolean isTreesFancy() {
		return gameSettings.ofTrees == 0 ? gameSettings.fancyGraphics
				: gameSettings.ofTrees == 2;
	}

	public static boolean isUseAlphaFunc() {
		final float alphaFuncLevel = getAlphaFuncLevel();
		return alphaFuncLevel > DEF_ALPHA_FUNC_LEVEL.floatValue() + 1.0E-5F;
	}

	public static boolean isVoidParticles() {
		return gameSettings.ofVoidParticles;
	}

	public static boolean isWaterFancy() {
		return gameSettings.ofWater == 0 ? gameSettings.fancyGraphics
				: gameSettings.ofWater == 2;
	}

	public static boolean isWaterParticles() {
		return gameSettings.ofWaterParticles;
	}

	public static boolean isWeatherEnabled() {
		return gameSettings.ofWeather;
	}

	public static float limit(float val, float min, float max) {
		return val < min ? min : val > max ? max : val;
	}

	public static int limit(int val, int min, int max) {
		return val < min ? min : val > max ? max : val;
	}

	public static float limitTo1(float val) {
		return val < 0.0F ? 0.0F : val > 1.0F ? 1.0F : val;
	}

	public static void log(String s) {
		dbg(s);
	}

	public static float parseFloat(String str, float defVal) {
		try {
			return str == null ? defVal : Float.parseFloat(str);
		} catch (final NumberFormatException var3) {
			return defVal;
		}
	}

	public static int parseInt(String str, int defVal) {
		try {
			return str == null ? defVal : Integer.parseInt(str);
		} catch (final NumberFormatException var3) {
			return defVal;
		}
	}

	public static String readFile(File file) throws IOException {
		final FileInputStream fin = new FileInputStream(file);
		return readInputStream(fin, "ASCII");
	}

	private static ByteBuffer readIconImage(File par1File) throws IOException {
		final BufferedImage var2 = ImageIO.read(par1File);
		final int[] var3 = var2.getRGB(0, 0, var2.getWidth(), var2.getHeight(),
				(int[]) null, 0, var2.getWidth());
		final ByteBuffer var4 = ByteBuffer.allocate(4 * var3.length);
		final int[] var5 = var3;
		final int var6 = var3.length;

		for (int var7 = 0; var7 < var6; ++var7) {
			final int var8 = var5[var7];
			var4.putInt(var8 << 8 | var8 >> 24 & 255);
		}

		var4.flip();
		return var4;
	}

	public static String readInputStream(InputStream in) throws IOException {
		return readInputStream(in, "ASCII");
	}

	public static String readInputStream(InputStream in, String encoding)
			throws IOException {
		final InputStreamReader inr = new InputStreamReader(in, encoding);
		final BufferedReader br = new BufferedReader(inr);
		final StringBuffer sb = new StringBuffer();

		while (true) {
			final String line = br.readLine();

			if (line == null)
				return sb.toString();

			sb.append(line);
			sb.append("\n");
		}
	}

	public static String[] readLines(File file) throws IOException {
		final ArrayList list = new ArrayList();
		final FileInputStream fis = new FileInputStream(file);
		final InputStreamReader isr = new InputStreamReader(fis, "ASCII");
		final BufferedReader br = new BufferedReader(isr);

		while (true) {
			final String lines = br.readLine();

			if (lines == null) {
				final String[] lines1 = (String[]) list.toArray(new String[list
						.size()]);
				return lines1;
			}

			list.add(lines);
			br.close();
		}
	}

	public static void setNewRelease(String newRelease) {
		Config.newRelease = newRelease;
	}

	public static void sleep(long ms) {
		try {
			Thread.currentThread();
			Thread.sleep(ms);
		} catch (final InterruptedException var3) {
			var3.printStackTrace();
		}
	}

	private static String[] splitRelease(String relStr) {
		if (relStr != null && relStr.length() > 0) {
			final String branch = relStr.substring(0, 1);

			if (relStr.length() <= 1)
				return new String[] { branch, "", "" };
			else {
				int pos;

				for (pos = 1; pos < relStr.length()
						&& Character.isDigit(relStr.charAt(pos)); ++pos) {
					;
				}

				final String revision = relStr.substring(1, pos);

				if (pos >= relStr.length())
					return new String[] { branch, revision, "" };
				else {
					final String suffix = relStr.substring(pos);
					return new String[] { branch, revision, suffix };
				}
			}
		} else
			return new String[] { "", "", "" };
	}

	private static void startVersionCheckThread() {
		final VersionCheckThread vct = new VersionCheckThread();
		vct.start();
	}

	public static String[] tokenize(String str, String delim) {
		final StringTokenizer tok = new StringTokenizer(str, delim);
		final ArrayList list = new ArrayList();

		while (tok.hasMoreTokens()) {
			final String strs = tok.nextToken();
			list.add(strs);
		}

		final String[] strs1 = (String[]) list.toArray(new String[list.size()]);
		return strs1;
	}

	public static void updateAvailableProcessors() {
		availableProcessors = Runtime.getRuntime().availableProcessors();
	}

	private static void updateFramebufferSize() {
		minecraft.getFramebuffer().createBindFramebuffer(
				minecraft.displayWidth, minecraft.displayHeight);

		if (minecraft.entityRenderer != null) {
			minecraft.entityRenderer.updateShaderGroupSize(
					minecraft.displayWidth, minecraft.displayHeight);
		}
	}

	public static void updateTexturePackClouds() {
		texturePackClouds = 0;
		final IResourceManager rm = getResourceManager();

		if (rm != null) {
			try {
				final InputStream e = rm.getResource(
						new ResourceLocation("mcpatcher/color.properties"))
						.getInputStream();

				if (e == null)
					return;

				final Properties props = new Properties();
				props.load(e);
				e.close();
				String cloudStr = props.getProperty("clouds");

				if (cloudStr == null)
					return;

				dbg("Texture pack clouds: " + cloudStr);
				cloudStr = cloudStr.toLowerCase();

				if (cloudStr.equals("fast")) {
					texturePackClouds = 1;
				}

				if (cloudStr.equals("fancy")) {
					texturePackClouds = 2;
				}
			} catch (final Exception var4) {
				;
			}
		}
	}

	public static void updateThreadPriorities() {
		try {
			final ThreadGroup e = Thread.currentThread().getThreadGroup();

			if (e == null)
				return;

			final int num = (e.activeCount() + 10) * 2;
			final Thread[] ts = new Thread[num];
			e.enumerate(ts, false);
			final byte prioMc = 5;
			byte prioSrv = 5;

			if (isSmoothWorld()) {
				prioSrv = 3;
			}

			minecraftThread.setPriority(prioMc);

			for (final Thread t : ts) {
				if (t != null && equals(t.getName(), "Server thread")
						&& t.getPriority() != prioSrv) {
					t.setPriority(prioSrv);
					dbg("Set server thread priority: " + prioSrv + ", " + t);
				}
			}
		} catch (final Throwable var7) {
			dbg(var7.getClass().getName() + ": " + var7.getMessage());
		}
	}

	public static void warn(String s) {
		systemOut.print("[OptiFine] [WARN] ");
		systemOut.println(s);
	}
}

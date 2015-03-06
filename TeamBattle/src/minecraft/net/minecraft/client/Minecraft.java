package net.minecraft.client;

import io.netty.util.concurrent.GenericFutureListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import me.client.Client;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.achievement.GuiAchievement;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourceIndex;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.IStatStringFormat;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MinecraftError;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Session;
import net.minecraft.util.Timer;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import event.events.EventRightClick;
import event.events.EventTick;
import event.events.EventWorldLoad;

public class Minecraft implements IPlayerUsage {
	static final class SwitchMovingObjectType {
		static final int[] field_152390_a = new int[MovingObjectPosition.MovingObjectType
				.values().length];

		static {
			try {
				field_152390_a[MovingObjectPosition.MovingObjectType.ENTITY
						.ordinal()] = 1;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				field_152390_a[MovingObjectPosition.MovingObjectType.BLOCK
						.ordinal()] = 2;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	/**
	 * This is set to fpsCounter every debug screen update, and is shown on the
	 * debug screen. It's also sent as part of the usage snooping.
	 */
	private static int debugFPS;

	public static final boolean isRunningOnMac = Util.getOSType() == Util.EnumOS.OSX;

	private static final ResourceLocation locationMojangPng = new ResourceLocation(
			"textures/gui/title/mojang.png");

	private static final Logger logger = LogManager.getLogger();

	private static final List macDisplayModes = Lists
			.newArrayList(new DisplayMode[] { new DisplayMode(2560, 1600),
					new DisplayMode(2880, 1800) });

	/** A 10MiB preallocation to ensure the heap is reasonably sized. */
	public static byte[] memoryReserve = new byte[10485760];

	/**
	 * Set to 'this' in Minecraft constructor; used by some settings get methods
	 */
	private static Minecraft theMinecraft;

	/**
	 * Used in the usage snooper.
	 */
	public static int getGLMaximumTextureSize() {
		for (int var0 = 16384; var0 > 0; var0 >>= 1) {
			GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, var0,
					var0, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
					(ByteBuffer) null);
			final int var1 = GL11.glGetTexLevelParameteri(
					GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

			if (var1 != 0)
				return var0;
		}

		return -1;
	}

	/**
	 * Return the singleton Minecraft instance for the game
	 */
	public static Minecraft getMinecraft() {
		return theMinecraft;
	}

	/**
	 * Gets the system time in milliseconds.
	 */
	public static long getSystemTime() {
		return Sys.getTime() * 1000L / Sys.getTimerResolution();
	}

	/**
	 * Returns if ambient occlusion is enabled
	 */
	public static boolean isAmbientOcclusionEnabled() {
		return theMinecraft != null
				&& theMinecraft.gameSettings.ambientOcclusion != 0;
	}

	public static boolean isFancyGraphicsEnabled() {
		return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
	}

	public static boolean isGuiEnabled() {
		return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
	}

	private static boolean isJvm64bit() {
		final String[] var0 = new String[] { "sun.arch.data.model",
				"com.ibm.vm.bitmode", "os.arch" };
		final String[] var1 = var0;
		final int var2 = var0.length;

		for (int var3 = 0; var3 < var2; ++var3) {
			final String var4 = var1[var3];
			final String var5 = System.getProperty(var4);

			if (var5 != null && var5.contains("64"))
				return true;
		}

		return false;
	}

	public static void stopIntegratedServer() {
		if (theMinecraft != null) {
			final IntegratedServer var0 = theMinecraft.getIntegratedServer();

			if (var0 != null) {
				var0.stopServer();
			}
		}
	}

	/** Instance of CrashReport. */
	private CrashReport crashReporter;

	/** The GuiScreen that's being displayed at the moment. */
	public GuiScreen currentScreen;
	private ServerData currentServerData;
	/** String that shows the debug information */
	public String debug = "";
	/** Profiler currently displayed in the debug screen pie chart */
	private String debugProfilerName = "root";

	/** Approximate time (in ms) of last update to debug string */
	long debugUpdateTime = getSystemTime();
	private final List defaultResourcePacks = Lists.newArrayList();
	public int displayHeight;
	public int displayWidth;
	public EffectRenderer effectRenderer;

	public EntityRenderer entityRenderer;
	private SkinManager field_152350_aA;

	private final Queue field_152351_aB = Queues.newArrayDeque();
	private final Thread field_152352_aC = Thread.currentThread();
	private IStream field_152353_at;

	private ResourceLocation field_152354_ay;

	private final MinecraftSessionService field_152355_az;

	private final Multimap field_152356_J;

	private long field_83002_am = -1L;

	private final File fileAssets;
	private final File fileResourcepacks;

	/** The font renderer used for displaying and measuring text. */
	public FontRenderer fontRenderer;

	/** holds the current fps */
	int fpsCounter;

	private boolean fullscreen;

	/** The game settings that currently hold effect. */
	public GameSettings gameSettings;
	/** Gui achievement */
	public GuiAchievement guiAchievement;
	private boolean hasCrashed;
	public GuiIngame ingameGUI;
	/**
	 * Does the actual gameplay have focus. If so then mouse and keys will
	 * effect the player instead of menus.
	 */
	public boolean inGameHasFocus;
	private boolean integratedServerIsRunning;

	private final boolean isDemo;

	private boolean isGamePaused;

	/** Join player counter */
	private int joinPlayerCounter;
	private final boolean jvm64bit;
	private final String launchedVersion;

	/** Mouse left click counter */
	private int leftClickCounter;
	public LoadingScreenRenderer loadingScreen;

	public final File mcDataDir;
	private final DefaultResourcePack mcDefaultResourcePack;
	private Framebuffer mcFramebuffer;
	private LanguageManager mcLanguageManager;
	private MusicTicker mcMusicTicker;

	/** The profiler instance */
	public final Profiler mcProfiler = new Profiler();
	private IReloadableResourceManager mcResourceManager;
	private ResourcePackRepository mcResourcePackRepository;
	private SoundHandler mcSoundHandler;
	private final IMetadataSerializer metadataSerializer_ = new IMetadataSerializer();
	/** Mouse helper instance. */
	public MouseHelper mouseHelper;
	private NetworkManager myNetworkManager;
	/** The ray trace hit that the mouse is over. */
	public MovingObjectPosition objectMouseOver;
	public PlayerControllerMP playerController;
	public Entity pointedEntity;
	long prevFrameTime = -1L;
	private final Proxy proxy;
	/**
	 * Checked in Minecraft's while(running) loop, if true it's set to false and
	 * the textures refreshed.
	 */
	private boolean refreshTexturePacksScheduled;
	/** The RenderEngine instance used by Minecraft */
	public TextureManager renderEngine;
	public RenderGlobal renderGlobal;
	/**
	 * The Entity from which the renderer determines the render viewpoint.
	 * Currently is always the parent Minecraft class's 'thePlayer' instance.
	 * Modification of its location, rotation, or other settings at render time
	 * will modify the camera likewise, with the caveat of triggering chunk
	 * rebuilds as it moves, making it unsuitable for changing the viewpoint
	 * mid-render.
	 */
	public EntityLivingBase renderViewEntity;
	/**
	 * When you place a block, it's set to 6, decremented once per tick, when
	 * it's 0, you can place another block.
	 */
	private int rightClickDelayTimer;
	/**
	 * Set to true to keep the game loop running. Set to false by shutdown() to
	 * allow the game loop to exit cleanly.
	 */
	volatile boolean running = true;

	private ISaveFormat saveLoader;

	private String serverName;

	private int serverPort;

	public Session session;
	/** Skip render world */
	public boolean skipRenderWorld;

	public FontRenderer standardGalacticFontRenderer;

	long systemTime = getSystemTime();

	/** Display height */
	private final int tempDisplayHeight;

	/** Display width */
	private final int tempDisplayWidth;

	private TextureMap textureMapBlocks;

	/** Instance of IntegratedServer. */
	private IntegratedServer theIntegratedServer;

	public EntityClientPlayerMP thePlayer;

	public WorldClient theWorld;

	public Timer timer = new Timer(20.0F);

	/** Instance of PlayerUsageSnooper. */
	private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper(
			"client", this, MinecraftServer.getSystemTimeMillis());

	public Minecraft(Session p_i1103_1_, int p_i1103_2_, int p_i1103_3_,
			boolean p_i1103_4_, boolean p_i1103_5_, File p_i1103_6_,
			File p_i1103_7_, File p_i1103_8_, Proxy p_i1103_9_,
			String p_i1103_10_, Multimap p_i1103_11_, String p_i1103_12_) {
		theMinecraft = this;
		mcDataDir = p_i1103_6_;
		fileAssets = p_i1103_7_;
		fileResourcepacks = p_i1103_8_;
		launchedVersion = p_i1103_10_;
		field_152356_J = p_i1103_11_;
		mcDefaultResourcePack = new DefaultResourcePack(new ResourceIndex(
				p_i1103_7_, p_i1103_12_).func_152782_a());
		addDefaultResourcePack();
		proxy = p_i1103_9_ == null ? Proxy.NO_PROXY : p_i1103_9_;
		field_152355_az = new YggdrasilAuthenticationService(p_i1103_9_, UUID
				.randomUUID().toString()).createMinecraftSessionService();
		startTimerHackThread();
		session = p_i1103_1_;
		logger.info("Setting user: " + p_i1103_1_.getUsername());
		logger.info("(Session ID is " + p_i1103_1_.getSessionID() + ")");
		isDemo = p_i1103_5_;
		displayWidth = p_i1103_2_;
		displayHeight = p_i1103_3_;
		tempDisplayWidth = p_i1103_2_;
		tempDisplayHeight = p_i1103_3_;
		fullscreen = p_i1103_4_;
		jvm64bit = isJvm64bit();
		ImageIO.setUseCache(false);
		Bootstrap.func_151354_b();
	}

	private void addDefaultResourcePack() {
		defaultResourcePacks.add(mcDefaultResourcePack);
	}

	/**
	 * adds core server Info (GL version , Texture pack, isModded, type), and
	 * the worldInfo to the crash report
	 */
	public CrashReport addGraphicsAndWorldToCrashReport(CrashReport p_71396_1_) {
		p_71396_1_.getCategory().addCrashSectionCallable("Launched Version",
				new Callable() {

					@Override
					public String call() {
						return launchedVersion;
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("LWJGL",
				new Callable() {

					@Override
					public String call() {
						return Sys.getVersion();
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("OpenGL",
				new Callable() {

					@Override
					public String call() {
						return GL11.glGetString(GL11.GL_RENDERER)
								+ " GL version "
								+ GL11.glGetString(GL11.GL_VERSION) + ", "
								+ GL11.glGetString(GL11.GL_VENDOR);
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("GL Caps",
				new Callable() {

					@Override
					public String call() {
						return OpenGlHelper.func_153172_c();
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("Is Modded",
				new Callable() {

					@Override
					public String call() {
						final String var1 = ClientBrandRetriever
								.getClientModName();
						return !var1.equals("vanilla") ? "Definitely; Client brand changed to \'"
								+ var1 + "\'"
								: Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated"
										: "Probably not. Jar signature remains and client brand is untouched.";
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("Type",
				new Callable() {

					@Override
					public String call() {
						return "Client (map_client.txt)";
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("Resource Packs",
				new Callable() {

					@Override
					public String call() {
						return gameSettings.resourcePacks.toString();
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("Current Language",
				new Callable() {

					@Override
					public String call() {
						return mcLanguageManager.getCurrentLanguage()
								.toString();
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("Profiler Position",
				new Callable() {

					@Override
					public String call() {
						return mcProfiler.profilingEnabled ? mcProfiler
								.getNameOfLastSection() : "N/A (disabled)";
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable("Vec3 Pool Size",
				new Callable() {

					@Override
					public String call() {
						final byte var1 = 0;
						final int var2 = 56 * var1;
						final int var3 = var2 / 1024 / 1024;
						final byte var4 = 0;
						final int var5 = 56 * var4;
						final int var6 = var5 / 1024 / 1024;
						return var1 + " (" + var2 + " bytes; " + var3
								+ " MB) allocated, " + var4 + " (" + var5
								+ " bytes; " + var6 + " MB) used";
					}
				});
		p_71396_1_.getCategory().addCrashSectionCallable(
				"Anisotropic Filtering", new Callable() {

					@Override
					public Object call() {
						return func_152388_a();
					}

					public String func_152388_a() {
						return gameSettings.anisotropicFiltering == 1 ? "Off (1)"
								: "On (" + gameSettings.anisotropicFiltering
										+ ")";
					}
				});

		if (theWorld != null) {
			theWorld.addWorldInfoToCrashReport(p_71396_1_);
		}

		return p_71396_1_;
	}

	@Override
	public void addServerStatsToSnooper(PlayerUsageSnooper p_70000_1_) {
		p_70000_1_.func_152768_a("fps", Integer.valueOf(debugFPS));
		p_70000_1_.func_152768_a("vsync_enabled",
				Boolean.valueOf(gameSettings.enableVsync));
		p_70000_1_.func_152768_a("display_frequency",
				Integer.valueOf(Display.getDisplayMode().getFrequency()));
		p_70000_1_.func_152768_a("display_type", fullscreen ? "fullscreen"
				: "windowed");
		p_70000_1_.func_152768_a("run_time", Long.valueOf((MinecraftServer
				.getSystemTimeMillis() - p_70000_1_
				.getMinecraftStartTimeMillis()) / 60L * 1000L));
		p_70000_1_.func_152768_a("resource_packs",
				Integer.valueOf(mcResourcePackRepository.getRepositoryEntries()
						.size()));
		int var2 = 0;
		final Iterator var3 = mcResourcePackRepository.getRepositoryEntries()
				.iterator();

		while (var3.hasNext()) {
			final ResourcePackRepository.Entry var4 = (ResourcePackRepository.Entry) var3
					.next();
			p_70000_1_.func_152768_a("resource_pack[" + var2++ + "]",
					var4.getResourcePackName());
		}

		if (theIntegratedServer != null
				&& theIntegratedServer.getPlayerUsageSnooper() != null) {
			p_70000_1_.func_152768_a("snooper_partner", theIntegratedServer
					.getPlayerUsageSnooper().getUniqueID());
		}
	}

	@Override
	public void addServerTypeToSnooper(PlayerUsageSnooper p_70001_1_) {
		p_70001_1_.func_152767_b("opengl_version",
				GL11.glGetString(GL11.GL_VERSION));
		p_70001_1_.func_152767_b("opengl_vendor",
				GL11.glGetString(GL11.GL_VENDOR));
		p_70001_1_.func_152767_b("client_brand",
				ClientBrandRetriever.getClientModName());
		p_70001_1_.func_152767_b("launched_version", launchedVersion);
		final ContextCapabilities var2 = GLContext.getCapabilities();
		p_70001_1_.func_152767_b("gl_caps[ARB_arrays_of_arrays]",
				Boolean.valueOf(var2.GL_ARB_arrays_of_arrays));
		p_70001_1_.func_152767_b("gl_caps[ARB_base_instance]",
				Boolean.valueOf(var2.GL_ARB_base_instance));
		p_70001_1_.func_152767_b("gl_caps[ARB_blend_func_extended]",
				Boolean.valueOf(var2.GL_ARB_blend_func_extended));
		p_70001_1_.func_152767_b("gl_caps[ARB_clear_buffer_object]",
				Boolean.valueOf(var2.GL_ARB_clear_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_color_buffer_float]",
				Boolean.valueOf(var2.GL_ARB_color_buffer_float));
		p_70001_1_.func_152767_b("gl_caps[ARB_compatibility]",
				Boolean.valueOf(var2.GL_ARB_compatibility));
		p_70001_1_.func_152767_b(
				"gl_caps[ARB_compressed_texture_pixel_storage]",
				Boolean.valueOf(var2.GL_ARB_compressed_texture_pixel_storage));
		p_70001_1_.func_152767_b("gl_caps[ARB_compute_shader]",
				Boolean.valueOf(var2.GL_ARB_compute_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_buffer]",
				Boolean.valueOf(var2.GL_ARB_copy_buffer));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_image]",
				Boolean.valueOf(var2.GL_ARB_copy_image));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_buffer_float]",
				Boolean.valueOf(var2.GL_ARB_depth_buffer_float));
		p_70001_1_.func_152767_b("gl_caps[ARB_compute_shader]",
				Boolean.valueOf(var2.GL_ARB_compute_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_buffer]",
				Boolean.valueOf(var2.GL_ARB_copy_buffer));
		p_70001_1_.func_152767_b("gl_caps[ARB_copy_image]",
				Boolean.valueOf(var2.GL_ARB_copy_image));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_buffer_float]",
				Boolean.valueOf(var2.GL_ARB_depth_buffer_float));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_clamp]",
				Boolean.valueOf(var2.GL_ARB_depth_clamp));
		p_70001_1_.func_152767_b("gl_caps[ARB_depth_texture]",
				Boolean.valueOf(var2.GL_ARB_depth_texture));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_buffers]",
				Boolean.valueOf(var2.GL_ARB_draw_buffers));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_buffers_blend]",
				Boolean.valueOf(var2.GL_ARB_draw_buffers_blend));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_elements_base_vertex]",
				Boolean.valueOf(var2.GL_ARB_draw_elements_base_vertex));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_indirect]",
				Boolean.valueOf(var2.GL_ARB_draw_indirect));
		p_70001_1_.func_152767_b("gl_caps[ARB_draw_instanced]",
				Boolean.valueOf(var2.GL_ARB_draw_instanced));
		p_70001_1_.func_152767_b("gl_caps[ARB_explicit_attrib_location]",
				Boolean.valueOf(var2.GL_ARB_explicit_attrib_location));
		p_70001_1_.func_152767_b("gl_caps[ARB_explicit_uniform_location]",
				Boolean.valueOf(var2.GL_ARB_explicit_uniform_location));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_layer_viewport]",
				Boolean.valueOf(var2.GL_ARB_fragment_layer_viewport));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_program]",
				Boolean.valueOf(var2.GL_ARB_fragment_program));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_shader]",
				Boolean.valueOf(var2.GL_ARB_fragment_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_fragment_program_shadow]",
				Boolean.valueOf(var2.GL_ARB_fragment_program_shadow));
		p_70001_1_.func_152767_b("gl_caps[ARB_framebuffer_object]",
				Boolean.valueOf(var2.GL_ARB_framebuffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_framebuffer_sRGB]",
				Boolean.valueOf(var2.GL_ARB_framebuffer_sRGB));
		p_70001_1_.func_152767_b("gl_caps[ARB_geometry_shader4]",
				Boolean.valueOf(var2.GL_ARB_geometry_shader4));
		p_70001_1_.func_152767_b("gl_caps[ARB_gpu_shader5]",
				Boolean.valueOf(var2.GL_ARB_gpu_shader5));
		p_70001_1_.func_152767_b("gl_caps[ARB_half_float_pixel]",
				Boolean.valueOf(var2.GL_ARB_half_float_pixel));
		p_70001_1_.func_152767_b("gl_caps[ARB_half_float_vertex]",
				Boolean.valueOf(var2.GL_ARB_half_float_vertex));
		p_70001_1_.func_152767_b("gl_caps[ARB_instanced_arrays]",
				Boolean.valueOf(var2.GL_ARB_instanced_arrays));
		p_70001_1_.func_152767_b("gl_caps[ARB_map_buffer_alignment]",
				Boolean.valueOf(var2.GL_ARB_map_buffer_alignment));
		p_70001_1_.func_152767_b("gl_caps[ARB_map_buffer_range]",
				Boolean.valueOf(var2.GL_ARB_map_buffer_range));
		p_70001_1_.func_152767_b("gl_caps[ARB_multisample]",
				Boolean.valueOf(var2.GL_ARB_multisample));
		p_70001_1_.func_152767_b("gl_caps[ARB_multitexture]",
				Boolean.valueOf(var2.GL_ARB_multitexture));
		p_70001_1_.func_152767_b("gl_caps[ARB_occlusion_query2]",
				Boolean.valueOf(var2.GL_ARB_occlusion_query2));
		p_70001_1_.func_152767_b("gl_caps[ARB_pixel_buffer_object]",
				Boolean.valueOf(var2.GL_ARB_pixel_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_seamless_cube_map]",
				Boolean.valueOf(var2.GL_ARB_seamless_cube_map));
		p_70001_1_.func_152767_b("gl_caps[ARB_shader_objects]",
				Boolean.valueOf(var2.GL_ARB_shader_objects));
		p_70001_1_.func_152767_b("gl_caps[ARB_shader_stencil_export]",
				Boolean.valueOf(var2.GL_ARB_shader_stencil_export));
		p_70001_1_.func_152767_b("gl_caps[ARB_shader_texture_lod]",
				Boolean.valueOf(var2.GL_ARB_shader_texture_lod));
		p_70001_1_.func_152767_b("gl_caps[ARB_shadow]",
				Boolean.valueOf(var2.GL_ARB_shadow));
		p_70001_1_.func_152767_b("gl_caps[ARB_shadow_ambient]",
				Boolean.valueOf(var2.GL_ARB_shadow_ambient));
		p_70001_1_.func_152767_b("gl_caps[ARB_stencil_texturing]",
				Boolean.valueOf(var2.GL_ARB_stencil_texturing));
		p_70001_1_.func_152767_b("gl_caps[ARB_sync]",
				Boolean.valueOf(var2.GL_ARB_sync));
		p_70001_1_.func_152767_b("gl_caps[ARB_tessellation_shader]",
				Boolean.valueOf(var2.GL_ARB_tessellation_shader));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_border_clamp]",
				Boolean.valueOf(var2.GL_ARB_texture_border_clamp));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_buffer_object]",
				Boolean.valueOf(var2.GL_ARB_texture_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_cube_map]",
				Boolean.valueOf(var2.GL_ARB_texture_cube_map));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_cube_map_array]",
				Boolean.valueOf(var2.GL_ARB_texture_cube_map_array));
		p_70001_1_.func_152767_b("gl_caps[ARB_texture_non_power_of_two]",
				Boolean.valueOf(var2.GL_ARB_texture_non_power_of_two));
		p_70001_1_.func_152767_b("gl_caps[ARB_uniform_buffer_object]",
				Boolean.valueOf(var2.GL_ARB_uniform_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_blend]",
				Boolean.valueOf(var2.GL_ARB_vertex_blend));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_buffer_object]",
				Boolean.valueOf(var2.GL_ARB_vertex_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_program]",
				Boolean.valueOf(var2.GL_ARB_vertex_program));
		p_70001_1_.func_152767_b("gl_caps[ARB_vertex_shader]",
				Boolean.valueOf(var2.GL_ARB_vertex_shader));
		p_70001_1_.func_152767_b("gl_caps[EXT_bindable_uniform]",
				Boolean.valueOf(var2.GL_EXT_bindable_uniform));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_equation_separate]",
				Boolean.valueOf(var2.GL_EXT_blend_equation_separate));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_func_separate]",
				Boolean.valueOf(var2.GL_EXT_blend_func_separate));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_minmax]",
				Boolean.valueOf(var2.GL_EXT_blend_minmax));
		p_70001_1_.func_152767_b("gl_caps[EXT_blend_subtract]",
				Boolean.valueOf(var2.GL_EXT_blend_subtract));
		p_70001_1_.func_152767_b("gl_caps[EXT_draw_instanced]",
				Boolean.valueOf(var2.GL_EXT_draw_instanced));
		p_70001_1_.func_152767_b("gl_caps[EXT_framebuffer_multisample]",
				Boolean.valueOf(var2.GL_EXT_framebuffer_multisample));
		p_70001_1_.func_152767_b("gl_caps[EXT_framebuffer_object]",
				Boolean.valueOf(var2.GL_EXT_framebuffer_object));
		p_70001_1_.func_152767_b("gl_caps[EXT_framebuffer_sRGB]",
				Boolean.valueOf(var2.GL_EXT_framebuffer_sRGB));
		p_70001_1_.func_152767_b("gl_caps[EXT_geometry_shader4]",
				Boolean.valueOf(var2.GL_EXT_geometry_shader4));
		p_70001_1_.func_152767_b("gl_caps[EXT_gpu_program_parameters]",
				Boolean.valueOf(var2.GL_EXT_gpu_program_parameters));
		p_70001_1_.func_152767_b("gl_caps[EXT_gpu_shader4]",
				Boolean.valueOf(var2.GL_EXT_gpu_shader4));
		p_70001_1_.func_152767_b("gl_caps[EXT_multi_draw_arrays]",
				Boolean.valueOf(var2.GL_EXT_multi_draw_arrays));
		p_70001_1_.func_152767_b("gl_caps[EXT_packed_depth_stencil]",
				Boolean.valueOf(var2.GL_EXT_packed_depth_stencil));
		p_70001_1_.func_152767_b("gl_caps[EXT_paletted_texture]",
				Boolean.valueOf(var2.GL_EXT_paletted_texture));
		p_70001_1_.func_152767_b("gl_caps[EXT_rescale_normal]",
				Boolean.valueOf(var2.GL_EXT_rescale_normal));
		p_70001_1_.func_152767_b("gl_caps[EXT_separate_shader_objects]",
				Boolean.valueOf(var2.GL_EXT_separate_shader_objects));
		p_70001_1_.func_152767_b("gl_caps[EXT_shader_image_load_store]",
				Boolean.valueOf(var2.GL_EXT_shader_image_load_store));
		p_70001_1_.func_152767_b("gl_caps[EXT_shadow_funcs]",
				Boolean.valueOf(var2.GL_EXT_shadow_funcs));
		p_70001_1_.func_152767_b("gl_caps[EXT_shared_texture_palette]",
				Boolean.valueOf(var2.GL_EXT_shared_texture_palette));
		p_70001_1_.func_152767_b("gl_caps[EXT_stencil_clear_tag]",
				Boolean.valueOf(var2.GL_EXT_stencil_clear_tag));
		p_70001_1_.func_152767_b("gl_caps[EXT_stencil_two_side]",
				Boolean.valueOf(var2.GL_EXT_stencil_two_side));
		p_70001_1_.func_152767_b("gl_caps[EXT_stencil_wrap]",
				Boolean.valueOf(var2.GL_EXT_stencil_wrap));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_3d]",
				Boolean.valueOf(var2.GL_EXT_texture_3d));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_array]",
				Boolean.valueOf(var2.GL_EXT_texture_array));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_buffer_object]",
				Boolean.valueOf(var2.GL_EXT_texture_buffer_object));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_filter_anisotropic]",
				Boolean.valueOf(var2.GL_EXT_texture_filter_anisotropic));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_integer]",
				Boolean.valueOf(var2.GL_EXT_texture_integer));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_lod_bias]",
				Boolean.valueOf(var2.GL_EXT_texture_lod_bias));
		p_70001_1_.func_152767_b("gl_caps[EXT_texture_sRGB]",
				Boolean.valueOf(var2.GL_EXT_texture_sRGB));
		p_70001_1_.func_152767_b("gl_caps[EXT_vertex_shader]",
				Boolean.valueOf(var2.GL_EXT_vertex_shader));
		p_70001_1_.func_152767_b("gl_caps[EXT_vertex_weighting]",
				Boolean.valueOf(var2.GL_EXT_vertex_weighting));
		p_70001_1_.func_152767_b("gl_caps[gl_max_vertex_uniforms]", Integer
				.valueOf(GL11
						.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS)));
		GL11.glGetError();
		p_70001_1_
				.func_152767_b(
						"gl_caps[gl_max_fragment_uniforms]",
						Integer.valueOf(GL11
								.glGetInteger(GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_vertex_attribs]",
				Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_vertex_texture_image_units]",
				Integer.valueOf(GL11
						.glGetInteger(GL20.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_texture_image_units]", Integer
				.valueOf(GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_caps[gl_max_texture_image_units]",
				Integer.valueOf(GL11.glGetInteger(35071)));
		GL11.glGetError();
		p_70001_1_.func_152767_b("gl_max_texture_size",
				Integer.valueOf(getGLMaximumTextureSize()));
	}

	/**
	 * Checks for an OpenGL error. If there is one, prints the error ID and
	 * error string.
	 */
	private void checkGLError(String p_71361_1_) {
		final int var2 = GL11.glGetError();

		if (var2 != 0) {
			final String var3 = GLU.gluErrorString(var2);
			logger.error("########## GL ERROR ##########");
			logger.error("@ " + p_71361_1_);
			logger.error(var2 + ": " + var3);
		}
	}

	public void crashed(CrashReport p_71404_1_) {
		hasCrashed = true;
		crashReporter = p_71404_1_;
	}

	/**
	 * A String of how many entities are in the world
	 */
	public String debugInfoEntities() {
		return "P: " + effectRenderer.getStatistics() + ". T: "
				+ theWorld.getDebugLoadedEntities();
	}

	/**
	 * A String of renderGlobal.getDebugInfoRenders
	 */
	public String debugInfoRenders() {
		return renderGlobal.getDebugInfoRenders();
	}

	/**
	 * Wrapper around displayCrashReportInternal
	 */
	public void displayCrashReport(CrashReport p_71377_1_) {
		final File var2 = new File(getMinecraft().mcDataDir, "crash-reports");
		final File var3 = new File(var2,
				"crash-"
						+ new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")
								.format(new Date()) + "-client.txt");
		System.out.println(p_71377_1_.getCompleteReport());

		if (p_71377_1_.getFile() != null) {
			System.out
					.println("#@!@# Game crashed! Crash report saved to: #@!@# "
							+ p_71377_1_.getFile());
			System.exit(-1);
		} else if (p_71377_1_.saveToFile(var3)) {
			System.out
					.println("#@!@# Game crashed! Crash report saved to: #@!@# "
							+ var3.getAbsolutePath());
			System.exit(-1);
		} else {
			System.out
					.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
			System.exit(-2);
		}
	}

	private void displayDebugInfo(long p_71366_1_) {
		if (mcProfiler.profilingEnabled) {
			final List var3 = mcProfiler.getProfilingData(debugProfilerName);
			final Profiler.Result var4 = (Profiler.Result) var3.remove(0);
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000.0D,
					3000.0D);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
			GL11.glLineWidth(1.0F);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			final Tessellator var5 = Tessellator.instance;
			final short var6 = 160;
			final int var7 = displayWidth - var6 - 10;
			final int var8 = displayHeight - var6 * 2;
			GL11.glEnable(GL11.GL_BLEND);
			var5.startDrawingQuads();
			var5.setColorRGBA_I(0, 200);
			var5.addVertex(var7 - var6 * 1.1F, var8 - var6 * 0.6F - 16.0F, 0.0D);
			var5.addVertex(var7 - var6 * 1.1F, var8 + var6 * 2, 0.0D);
			var5.addVertex(var7 + var6 * 1.1F, var8 + var6 * 2, 0.0D);
			var5.addVertex(var7 + var6 * 1.1F, var8 - var6 * 0.6F - 16.0F, 0.0D);
			var5.draw();
			GL11.glDisable(GL11.GL_BLEND);
			double var9 = 0.0D;
			int var13;

			for (int var11 = 0; var11 < var3.size(); ++var11) {
				final Profiler.Result var12 = (Profiler.Result) var3.get(var11);
				var13 = MathHelper.floor_double(var12.field_76332_a / 4.0D) + 1;
				var5.startDrawing(6);
				var5.setColorOpaque_I(var12.func_76329_a());
				var5.addVertex(var7, var8, 0.0D);
				int var14;
				float var15;
				float var16;
				float var17;

				for (var14 = var13; var14 >= 0; --var14) {
					var15 = (float) ((var9 + var12.field_76332_a * var14
							/ var13)
							* Math.PI * 2.0D / 100.0D);
					var16 = MathHelper.sin(var15) * var6;
					var17 = MathHelper.cos(var15) * var6 * 0.5F;
					var5.addVertex(var7 + var16, var8 - var17, 0.0D);
				}

				var5.draw();
				var5.startDrawing(5);
				var5.setColorOpaque_I((var12.func_76329_a() & 16711422) >> 1);

				for (var14 = var13; var14 >= 0; --var14) {
					var15 = (float) ((var9 + var12.field_76332_a * var14
							/ var13)
							* Math.PI * 2.0D / 100.0D);
					var16 = MathHelper.sin(var15) * var6;
					var17 = MathHelper.cos(var15) * var6 * 0.5F;
					var5.addVertex(var7 + var16, var8 - var17, 0.0D);
					var5.addVertex(var7 + var16, var8 - var17 + 10.0F, 0.0D);
				}

				var5.draw();
				var9 += var12.field_76332_a;
			}

			final DecimalFormat var18 = new DecimalFormat("##0.00");
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			String var19 = "";

			if (!var4.field_76331_c.equals("unspecified")) {
				var19 = var19 + "[0] ";
			}

			if (var4.field_76331_c.length() == 0) {
				var19 = var19 + "ROOT ";
			} else {
				var19 = var19 + var4.field_76331_c + " ";
			}

			var13 = 16777215;
			fontRenderer.drawStringWithShadow(var19, var7 - var6, var8 - var6
					/ 2 - 16, var13);
			fontRenderer.drawStringWithShadow(
					var19 = var18.format(var4.field_76330_b) + "%", var7 + var6
							- fontRenderer.getStringWidth(var19), var8 - var6
							/ 2 - 16, var13);

			for (int var20 = 0; var20 < var3.size(); ++var20) {
				final Profiler.Result var21 = (Profiler.Result) var3.get(var20);
				String var22 = "";

				if (var21.field_76331_c.equals("unspecified")) {
					var22 = var22 + "[?] ";
				} else {
					var22 = var22 + "[" + (var20 + 1) + "] ";
				}

				var22 = var22 + var21.field_76331_c;
				fontRenderer.drawStringWithShadow(var22, var7 - var6, var8
						+ var6 / 2 + var20 * 8 + 20, var21.func_76329_a());
				fontRenderer.drawStringWithShadow(
						var22 = var18.format(var21.field_76332_a) + "%",
						var7 + var6 - 50 - fontRenderer.getStringWidth(var22),
						var8 + var6 / 2 + var20 * 8 + 20, var21.func_76329_a());
				fontRenderer.drawStringWithShadow(
						var22 = var18.format(var21.field_76330_b) + "%", var7
								+ var6 - fontRenderer.getStringWidth(var22),
						var8 + var6 / 2 + var20 * 8 + 20, var21.func_76329_a());
			}
		}
	}

	/**
	 * Sets the argument GuiScreen as the main (topmost visible) screen.
	 */
	public void displayGuiScreen(GuiScreen p_147108_1_) {
		if (currentScreen != null) {
			currentScreen.onGuiClosed();
		}

		if (p_147108_1_ == null && theWorld == null) {
			p_147108_1_ = new GuiMainMenu();
		} else if (p_147108_1_ == null && thePlayer.getHealth() <= 0.0F) {
			p_147108_1_ = new GuiGameOver();
		}

		if (p_147108_1_ instanceof GuiMainMenu) {
			gameSettings.showDebugInfo = false;
			ingameGUI.getChatGUI().func_146231_a();
		}

		currentScreen = p_147108_1_;

		if (p_147108_1_ != null) {
			setIngameNotInFocus();
			final ScaledResolution var2 = new ScaledResolution(this,
					displayWidth, displayHeight);
			final int var3 = var2.getScaledWidth();
			final int var4 = var2.getScaledHeight();
			p_147108_1_.setWorldAndResolution(this, var3, var4);
			skipRenderWorld = false;
		} else {
			mcSoundHandler.func_147687_e();
			setIngameFocus();
		}
	}

	/**
	 * Displays the ingame menu
	 */
	public void displayInGameMenu() {
		if (currentScreen == null) {
			displayGuiScreen(new GuiIngameMenu());

			if (isSingleplayer() && !theIntegratedServer.getPublic()) {
				mcSoundHandler.func_147689_b();
			}
		}
	}

	public void freeMemory() {
		try {
			memoryReserve = new byte[0];
			renderGlobal.deleteAllDisplayLists();
		} catch (final Throwable var4) {
			;
		}

		try {
			System.gc();
		} catch (final Throwable var3) {
			;
		}

		try {
			System.gc();
			this.loadWorld((WorldClient) null);
		} catch (final Throwable var2) {
			;
		}

		System.gc();
	}

	public ServerData func_147104_D() {
		return currentServerData;
	}

	public MusicTicker.MusicType func_147109_W() {
		return currentScreen instanceof GuiWinGame ? MusicTicker.MusicType.CREDITS
				: thePlayer != null ? thePlayer.worldObj.provider instanceof WorldProviderHell ? MusicTicker.MusicType.NETHER
						: thePlayer.worldObj.provider instanceof WorldProviderEnd ? BossStatus.bossName != null
								&& BossStatus.statusBarTime > 0 ? MusicTicker.MusicType.END_BOSS
								: MusicTicker.MusicType.END
								: thePlayer.capabilities.isCreativeMode
										&& thePlayer.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE
										: MusicTicker.MusicType.GAME
						: MusicTicker.MusicType.MENU;
	}

	private void func_147112_ai() {
		if (objectMouseOver != null) {
			final boolean var1 = thePlayer.capabilities.isCreativeMode;
			int var3 = 0;
			boolean var4 = false;
			Item var2;
			int var5;

			if (objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				var5 = objectMouseOver.blockX;
				final int var6 = objectMouseOver.blockY;
				final int var7 = objectMouseOver.blockZ;
				final Block var8 = theWorld.getBlock(var5, var6, var7);

				if (var8.getMaterial() == Material.air)
					return;

				var2 = var8.getItem(theWorld, var5, var6, var7);

				if (var2 == null)
					return;

				var4 = var2.getHasSubtypes();
				final Block var9 = var2 instanceof ItemBlock
						&& !var8.isFlowerPot() ? Block.getBlockFromItem(var2)
						: var8;
				var3 = var9.getDamageValue(theWorld, var5, var6, var7);
			} else {
				if (objectMouseOver.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY
						|| objectMouseOver.entityHit == null || !var1)
					return;

				if (objectMouseOver.entityHit instanceof EntityPainting) {
					var2 = Items.painting;
				} else if (objectMouseOver.entityHit instanceof EntityLeashKnot) {
					var2 = Items.lead;
				} else if (objectMouseOver.entityHit instanceof EntityItemFrame) {
					final EntityItemFrame var10 = (EntityItemFrame) objectMouseOver.entityHit;
					final ItemStack var12 = var10.getDisplayedItem();

					if (var12 == null) {
						var2 = Items.item_frame;
					} else {
						var2 = var12.getItem();
						var3 = var12.getItemDamage();
						var4 = true;
					}
				} else if (objectMouseOver.entityHit instanceof EntityMinecart) {
					final EntityMinecart var11 = (EntityMinecart) objectMouseOver.entityHit;

					if (var11.getMinecartType() == 2) {
						var2 = Items.furnace_minecart;
					} else if (var11.getMinecartType() == 1) {
						var2 = Items.chest_minecart;
					} else if (var11.getMinecartType() == 3) {
						var2 = Items.tnt_minecart;
					} else if (var11.getMinecartType() == 5) {
						var2 = Items.hopper_minecart;
					} else if (var11.getMinecartType() == 6) {
						var2 = Items.command_block_minecart;
					} else {
						var2 = Items.minecart;
					}
				} else if (objectMouseOver.entityHit instanceof EntityBoat) {
					var2 = Items.boat;
				} else {
					var2 = Items.spawn_egg;
					var3 = EntityList.getEntityID(objectMouseOver.entityHit);
					var4 = true;

					if (var3 <= 0
							|| !EntityList.entityEggs.containsKey(Integer
									.valueOf(var3)))
						return;
				}
			}

			thePlayer.inventory.func_146030_a(var2, var3, var4, var1);

			if (var1) {
				var5 = thePlayer.inventoryContainer.inventorySlots.size() - 9
						+ thePlayer.inventory.currentItem;
				playerController.sendSlotPacket(thePlayer.inventory
						.getStackInSlot(thePlayer.inventory.currentItem), var5);
			}
		}
	}

	public boolean func_147113_T() {
		return isGamePaused;
	}

	private void func_147115_a(boolean p_147115_1_) {
		if (!p_147115_1_) {
			leftClickCounter = 0;
		}

		if (leftClickCounter <= 0) {
			if (p_147115_1_
					&& objectMouseOver != null
					&& objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				final int var2 = objectMouseOver.blockX;
				final int var3 = objectMouseOver.blockY;
				final int var4 = objectMouseOver.blockZ;

				if (theWorld.getBlock(var2, var3, var4).getMaterial() != Material.air) {
					playerController.onPlayerDamageBlock(var2, var3, var4,
							objectMouseOver.sideHit);

					if (thePlayer.isCurrentToolAdventureModeExempt(var2, var3,
							var4)) {
						effectRenderer.addBlockHitEffects(var2, var3, var4,
								objectMouseOver.sideHit);
						thePlayer.swingItem();
					}
				}
			} else {
				playerController.resetBlockRemoving();
			}
		}
	}

	private void func_147116_af() {
		if (leftClickCounter <= 0) {
			thePlayer.swingItem();

			if (objectMouseOver == null) {
				logger.error("Null returned as \'hitResult\', this shouldn\'t happen!");

				if (playerController.isNotCreative()) {
					leftClickCounter = 10;
				}
			} else {
				switch (Minecraft.SwitchMovingObjectType.field_152390_a[objectMouseOver.typeOfHit
						.ordinal()]) {
				case 1:
					playerController.attackEntity(thePlayer,
							objectMouseOver.entityHit);
					break;

				case 2:
					final int var1 = objectMouseOver.blockX;
					final int var2 = objectMouseOver.blockY;
					final int var3 = objectMouseOver.blockZ;

					if (theWorld.getBlock(var1, var2, var3).getMaterial() == Material.air) {
						if (playerController.isNotCreative()) {
							leftClickCounter = 10;
						}
					} else {
						playerController.clickBlock(var1, var2, var3,
								objectMouseOver.sideHit);
					}
				}
			}
		}
	}

	public void func_147120_f() {
		Display.update();

		if (!fullscreen && Display.wasResized()) {
			final int var1 = displayWidth;
			final int var2 = displayHeight;
			displayWidth = Display.getWidth();
			displayHeight = Display.getHeight();

			if (displayWidth != var1 || displayHeight != var2) {
				if (displayWidth <= 0) {
					displayWidth = 1;
				}

				if (displayHeight <= 0) {
					displayHeight = 1;
				}

				resize(displayWidth, displayHeight);
			}
		}
	}

	private void func_147121_ag() {
		rightClickDelayTimer = 4;
		final EventRightClick event = new EventRightClick(rightClickDelayTimer);
		Client.getEventManager().call(event);
		rightClickDelayTimer = event.getDelay();
		boolean var1 = true;
		final ItemStack var2 = thePlayer.inventory.getCurrentItem();

		if (objectMouseOver == null) {
			logger.warn("Null returned as \'hitResult\', this shouldn\'t happen!");
		} else {
			switch (Minecraft.SwitchMovingObjectType.field_152390_a[objectMouseOver.typeOfHit
					.ordinal()]) {
			case 1:
				if (playerController.interactWithEntitySendPacket(thePlayer,
						objectMouseOver.entityHit)) {
					var1 = false;
				}

				break;

			case 2:
				final int var3 = objectMouseOver.blockX;
				final int var4 = objectMouseOver.blockY;
				final int var5 = objectMouseOver.blockZ;

				if (theWorld.getBlock(var3, var4, var5).getMaterial() != Material.air) {
					final int var6 = var2 != null ? var2.stackSize : 0;

					if (playerController.onPlayerRightClick(thePlayer,
							theWorld, var2, var3, var4, var5,
							objectMouseOver.sideHit, objectMouseOver.hitVec)) {
						var1 = false;
						thePlayer.swingItem();
					}

					if (var2 == null)
						return;

					if (var2.stackSize == 0) {
						thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
					} else if (var2.stackSize != var6
							|| playerController.isInCreativeMode()) {
						entityRenderer.itemRenderer.resetEquippedProgress();
					}
				}
			}
		}

		if (var1) {
			final ItemStack var7 = thePlayer.inventory.getCurrentItem();

			if (var7 != null
					&& playerController.sendUseItem(thePlayer, theWorld, var7)) {
				entityRenderer.itemRenderer.resetEquippedProgress2();
			}
		}
	}

	private ByteBuffer func_152340_a(InputStream p_152340_1_)
			throws IOException {
		final BufferedImage var2 = ImageIO.read(p_152340_1_);
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

	public Multimap func_152341_N() {
		return field_152356_J;
	}

	public SkinManager func_152342_ad() {
		return field_152350_aA;
	}

	public ListenableFuture func_152343_a(Callable p_152343_1_) {
		Validate.notNull(p_152343_1_);

		if (!func_152345_ab()) {
			final ListenableFutureTask var2 = ListenableFutureTask
					.create(p_152343_1_);
			synchronized (field_152351_aB) {
				field_152351_aB.add(var2);
				return var2;
			}
		} else {
			try {
				return Futures.immediateFuture(p_152343_1_.call());
			} catch (final Exception var6) {
				return Futures.immediateFailedCheckedFuture(var6);
			}
		}
	}

	public ListenableFuture func_152344_a(Runnable p_152344_1_) {
		Validate.notNull(p_152344_1_);
		return func_152343_a(Executors.callable(p_152344_1_));
	}

	public boolean func_152345_ab() {
		return Thread.currentThread() == field_152352_aC;
	}

	public IStream func_152346_Z() {
		return field_152353_at;
	}

	public MinecraftSessionService func_152347_ac() {
		return field_152355_az;
	}

	public void func_152348_aa() {
		final int var1 = Keyboard.getEventKey();

		if (var1 != 0 && !Keyboard.isRepeatEvent()) {
			if (!(currentScreen instanceof GuiControls)
					|| ((GuiControls) currentScreen).field_152177_g <= getSystemTime() - 20L) {
				if (Keyboard.getEventKeyState()) {
					if (var1 == gameSettings.field_152396_an.getKeyCode()) {
						if (func_152346_Z().func_152934_n()) {
							func_152346_Z().func_152914_u();
						} else if (func_152346_Z().func_152924_m()) {
							displayGuiScreen(new GuiYesNo(
									new GuiYesNoCallback() {

										@Override
										public void confirmClicked(
												boolean p_73878_1_,
												int p_73878_2_) {
											if (p_73878_1_) {
												Minecraft.this.func_152346_Z()
														.func_152930_t();
											}

											Minecraft.this.displayGuiScreen((GuiScreen) null);
										}
									}, I18n.format("stream.confirm_start",
											new Object[0]), "", 0));
						} else if (func_152346_Z().func_152928_D()
								&& func_152346_Z().func_152936_l()) {
							if (theWorld != null) {
								ingameGUI
										.getChatGUI()
										.func_146227_a(
												new ChatComponentText(
														"Not ready to start streaming yet!"));
							}
						} else {
							GuiStreamUnavailable.func_152321_a(currentScreen);
						}
					} else if (var1 == gameSettings.field_152397_ao
							.getKeyCode()) {
						if (func_152346_Z().func_152934_n()) {
							if (func_152346_Z().func_152919_o()) {
								func_152346_Z().func_152933_r();
							} else {
								func_152346_Z().func_152916_q();
							}
						}
					} else if (var1 == gameSettings.field_152398_ap
							.getKeyCode()) {
						if (func_152346_Z().func_152934_n()) {
							func_152346_Z().func_152931_p();
						}
					} else if (var1 == gameSettings.field_152399_aq
							.getKeyCode()) {
						field_152353_at.func_152910_a(true);
					} else if (var1 == gameSettings.field_152395_am
							.getKeyCode()) {
						toggleFullscreen();
					} else if (var1 == gameSettings.keyBindScreenshot
							.getKeyCode()) {
						ingameGUI.getChatGUI().func_146227_a(
								ScreenShotHelper.saveScreenshot(mcDataDir,
										displayWidth, displayHeight,
										mcFramebuffer));
					}
				} else if (var1 == gameSettings.field_152399_aq.getKeyCode()) {
					field_152353_at.func_152910_a(false);
				}
			}
		}
	}

	public boolean func_152349_b() {
		return mcLanguageManager.isCurrentLocaleUnicode()
				|| gameSettings.forceUnicodeFont;
	}

	/**
	 * Gets the information in the F3 menu about how many entities are
	 * infront/around you
	 */
	public String getEntityDebug() {
		return renderGlobal.getDebugInfoEntities();
	}

	public Framebuffer getFramebuffer() {
		return mcFramebuffer;
	}

	/**
	 * Returns the currently running integrated server
	 */
	public IntegratedServer getIntegratedServer() {
		return theIntegratedServer;
	}

	public LanguageManager getLanguageManager() {
		return mcLanguageManager;
	}

	public int getLimitFramerate() {
		return theWorld == null && currentScreen != null ? 30
				: gameSettings.limitFramerate;
	}

	public NetHandlerPlayClient getNetHandler() {
		return thePlayer != null ? thePlayer.sendQueue : null;
	}

	/**
	 * Returns the PlayerUsageSnooper instance.
	 */
	public PlayerUsageSnooper getPlayerUsageSnooper() {
		return usageSnooper;
	}

	public Proxy getProxy() {
		return proxy;
	}

	public IResourceManager getResourceManager() {
		return mcResourceManager;
	}

	public ResourcePackRepository getResourcePackRepository() {
		return mcResourcePackRepository;
	}

	/**
	 * Returns the save loader that is currently being used
	 */
	public ISaveFormat getSaveLoader() {
		return saveLoader;
	}

	public ScaledResolution getScaledResolution() {
		return new ScaledResolution(this, displayWidth, displayHeight);
	}

	public Session getSession() {
		return session;
	}

	public SoundHandler getSoundHandler() {
		return mcSoundHandler;
	}

	public TextureManager getTextureManager() {
		return renderEngine;
	}

	public TextureMap getTextureMapBlocks() {
		return textureMapBlocks;
	}

	/**
	 * Gets the name of the world's current chunk provider
	 */
	public String getWorldProviderName() {
		return theWorld.getProviderName();
	}

	/**
	 * Gets whether this is a demo or not.
	 */
	public final boolean isDemo() {
		return isDemo;
	}

	public boolean isFramerateLimitBelowMax() {
		return getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT
				.getValueMax();
	}

	/**
	 * Returns whether we're in full screen or not.
	 */
	public boolean isFullScreen() {
		return fullscreen;
	}

	public boolean isIntegratedServerRunning() {
		return integratedServerIsRunning;
	}

	public boolean isJava64bit() {
		return jvm64bit;
	}

	/**
	 * Returns true if there is only one player playing, and the current server
	 * is the integrated one.
	 */
	public boolean isSingleplayer() {
		return integratedServerIsRunning && theIntegratedServer != null;
	}

	/**
	 * Returns whether snooping is enabled or not.
	 */
	@Override
	public boolean isSnooperEnabled() {
		return gameSettings.snooperEnabled;
	}

	/**
	 * Arguments: World foldername, World ingame name, WorldSettings
	 */
	public void launchIntegratedServer(String p_71371_1_, String p_71371_2_,
			WorldSettings p_71371_3_) {
		this.loadWorld((WorldClient) null);
		System.gc();
		final ISaveHandler var4 = saveLoader.getSaveLoader(p_71371_1_, false);
		WorldInfo var5 = var4.loadWorldInfo();

		if (var5 == null && p_71371_3_ != null) {
			var5 = new WorldInfo(p_71371_3_, p_71371_1_);
			var4.saveWorldInfo(var5);
		}

		if (p_71371_3_ == null) {
			p_71371_3_ = new WorldSettings(var5);
		}

		try {
			theIntegratedServer = new IntegratedServer(this, p_71371_1_,
					p_71371_2_, p_71371_3_);
			theIntegratedServer.startServerThread();
			integratedServerIsRunning = true;
		} catch (final Throwable var10) {
			final CrashReport var7 = CrashReport.makeCrashReport(var10,
					"Starting integrated server");
			final CrashReportCategory var8 = var7
					.makeCategory("Starting integrated server");
			var8.addCrashSection("Level ID", p_71371_1_);
			var8.addCrashSection("Level Name", p_71371_2_);
			throw new ReportedException(var7);
		}

		loadingScreen.displayProgressMessage(I18n.format("menu.loadingLevel",
				new Object[0]));

		while (!theIntegratedServer.serverIsInRunLoop()) {
			final String var6 = theIntegratedServer.getUserMessage();

			if (var6 != null) {
				loadingScreen.resetProgresAndWorkingMessage(I18n.format(var6,
						new Object[0]));
			} else {
				loadingScreen.resetProgresAndWorkingMessage("");
			}

			try {
				Thread.sleep(200L);
			} catch (final InterruptedException var9) {
				;
			}
		}

		displayGuiScreen((GuiScreen) null);
		final SocketAddress var11 = theIntegratedServer.func_147137_ag()
				.addLocalEndpoint();
		final NetworkManager var12 = NetworkManager.provideLocalClient(var11);
		var12.setNetHandler(new NetHandlerLoginClient(var12, this,
				(GuiScreen) null));
		var12.scheduleOutboundPacket(new C00Handshake(5, var11.toString(), 0,
				EnumConnectionState.LOGIN), new GenericFutureListener[0]);
		var12.scheduleOutboundPacket(new C00PacketLoginStart(getSession()
				.func_148256_e()), new GenericFutureListener[0]);
		myNetworkManager = var12;
	}

	/**
	 * Displays a new screen.
	 */
	private void loadScreen() throws LWJGLException {
		final ScaledResolution var1 = new ScaledResolution(this, displayWidth,
				displayHeight);
		final int var2 = var1.getScaleFactor();
		final Framebuffer var3 = new Framebuffer(var1.getScaledWidth() * var2,
				var1.getScaledHeight() * var2, true);
		var3.bindFramebuffer(false);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0D, var1.getScaledWidth(), var1.getScaledHeight(), 0.0D,
				1000.0D, 3000.0D);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		try {
			field_152354_ay = renderEngine.getDynamicTextureLocation(
					"logo",
					new DynamicTexture(ImageIO.read(mcDefaultResourcePack
							.getInputStream(locationMojangPng))));
			renderEngine.bindTexture(field_152354_ay);
		} catch (final IOException var7) {
			logger.error("Unable to load logo: " + locationMojangPng, var7);
		}

		final Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		var4.setColorOpaque_I(16777215);
		var4.addVertexWithUV(0.0D, displayHeight, 0.0D, 0.0D, 0.0D);
		var4.addVertexWithUV(displayWidth, displayHeight, 0.0D, 0.0D, 0.0D);
		var4.addVertexWithUV(displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
		var4.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
		var4.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var4.setColorOpaque_I(16777215);
		final short var5 = 256;
		final short var6 = 256;
		scaledTessellator((var1.getScaledWidth() - var5) / 2,
				(var1.getScaledHeight() - var6) / 2, 0, 0, var5, var6);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		var3.unbindFramebuffer();
		var3.framebufferRender(var1.getScaledWidth() * var2,
				var1.getScaledHeight() * var2);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glFlush();
		func_147120_f();
	}

	/**
	 * unloads the current world first
	 */
	public void loadWorld(WorldClient p_71403_1_) {
		this.loadWorld(p_71403_1_, "");
	}

	/**
	 * par2Str is displayed on the loading screen to the user unloads the
	 * current world first
	 */
	public void loadWorld(WorldClient p_71353_1_, String p_71353_2_) {
		final EventWorldLoad event = new EventWorldLoad(p_71353_1_);
		Client.getEventManager().call(event);
		if (p_71353_1_ == null) {
			final NetHandlerPlayClient var3 = getNetHandler();

			if (var3 != null) {
				var3.cleanup();
			}

			if (theIntegratedServer != null) {
				theIntegratedServer.initiateShutdown();
			}

			theIntegratedServer = null;
			guiAchievement.func_146257_b();
			entityRenderer.getMapItemRenderer().func_148249_a();
		}

		renderViewEntity = null;
		myNetworkManager = null;

		if (loadingScreen != null) {
			loadingScreen.resetProgressAndMessage(p_71353_2_);
			loadingScreen.resetProgresAndWorkingMessage("");
		}

		if (p_71353_1_ == null && theWorld != null) {
			if (mcResourcePackRepository.func_148530_e() != null) {
				scheduleResourcesRefresh();
			}

			mcResourcePackRepository.func_148529_f();
			setServerData((ServerData) null);
			integratedServerIsRunning = false;
		}

		mcSoundHandler.func_147690_c();
		theWorld = p_71353_1_;

		if (p_71353_1_ != null) {
			if (renderGlobal != null) {
				renderGlobal.setWorldAndLoadRenderers(p_71353_1_);
			}

			if (effectRenderer != null) {
				effectRenderer.clearEffects(p_71353_1_);
			}

			if (thePlayer == null) {
				thePlayer = playerController.func_147493_a(p_71353_1_,
						new StatFileWriter());
				playerController.flipPlayer(thePlayer);
			}

			thePlayer.preparePlayerToSpawn();
			p_71353_1_.spawnEntityInWorld(thePlayer);
			thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
			playerController.setPlayerCapabilities(thePlayer);
			renderViewEntity = thePlayer;
		} else {
			saveLoader.flushCache();
			thePlayer = null;
		}

		System.gc();
		systemTime = 0L;
	}

	public void refreshResources() {
		final ArrayList var1 = Lists.newArrayList(defaultResourcePacks);
		final Iterator var2 = mcResourcePackRepository.getRepositoryEntries()
				.iterator();

		while (var2.hasNext()) {
			final ResourcePackRepository.Entry var3 = (ResourcePackRepository.Entry) var2
					.next();
			var1.add(var3.getResourcePack());
		}

		if (mcResourcePackRepository.func_148530_e() != null) {
			var1.add(mcResourcePackRepository.func_148530_e());
		}

		try {
			mcResourceManager.reloadResources(var1);
		} catch (final RuntimeException var4) {
			logger.info(
					"Caught error stitching, removing all assigned resourcepacks",
					var4);
			var1.clear();
			var1.addAll(defaultResourcePacks);
			mcResourcePackRepository.func_148527_a(Collections.emptyList());
			mcResourceManager.reloadResources(var1);
			gameSettings.resourcePacks.clear();
			gameSettings.saveOptions();
		}

		mcLanguageManager.parseLanguageMetadata(var1);

		if (renderGlobal != null) {
			renderGlobal.loadRenderers();
		}
	}

	/**
	 * Called to resize the current screen.
	 */
	private void resize(int p_71370_1_, int p_71370_2_) {
		displayWidth = p_71370_1_ <= 0 ? 1 : p_71370_1_;
		displayHeight = p_71370_2_ <= 0 ? 1 : p_71370_2_;

		if (currentScreen != null) {
			final ScaledResolution var3 = new ScaledResolution(this,
					p_71370_1_, p_71370_2_);
			final int var4 = var3.getScaledWidth();
			final int var5 = var3.getScaledHeight();
			currentScreen.setWorldAndResolution(this, var4, var5);
		}

		loadingScreen = new LoadingScreenRenderer(this);
		updateFramebufferSize();
	}

	public void run() {
		running = true;
		CrashReport var2;

		try {
			startGame();
		} catch (final Throwable var11) {
			var2 = CrashReport.makeCrashReport(var11, "Initializing game");
			var2.makeCategory("Initialization");
			displayCrashReport(addGraphicsAndWorldToCrashReport(var2));
			return;
		}

		while (true) {
			try {
				while (running) {
					if (!hasCrashed || crashReporter == null) {
						try {
							runGameLoop();
						} catch (final OutOfMemoryError var10) {
							freeMemory();
							displayGuiScreen(new GuiMemoryErrorScreen());
							System.gc();
						}

						continue;
					}

					displayCrashReport(crashReporter);
					return;
				}
			} catch (final MinecraftError var12) {
				;
			} catch (final ReportedException var13) {
				addGraphicsAndWorldToCrashReport(var13.getCrashReport());
				freeMemory();
				logger.fatal("Reported exception thrown!", var13);
				displayCrashReport(var13.getCrashReport());
			} catch (final Throwable var14) {
				var2 = addGraphicsAndWorldToCrashReport(new CrashReport(
						"Unexpected error", var14));
				freeMemory();
				logger.fatal("Unreported exception thrown!", var14);
				displayCrashReport(var2);
			} finally {
				shutdownMinecraftApplet();
			}

			return;
		}
	}

	/**
	 * Called repeatedly from run()
	 */
	private void runGameLoop() {
		mcProfiler.startSection("root");

		if (Display.isCreated() && Display.isCloseRequested()) {
			shutdown();
		}

		if (isGamePaused && theWorld != null) {
			final float var1 = timer.renderPartialTicks;
			timer.updateTimer();
			timer.renderPartialTicks = var1;
		} else {
			timer.updateTimer();
		}

		if ((theWorld == null || currentScreen == null)
				&& refreshTexturePacksScheduled) {
			refreshTexturePacksScheduled = false;
			refreshResources();
		}

		final long var5 = System.nanoTime();
		mcProfiler.startSection("tick");

		for (int var3 = 0; var3 < timer.elapsedTicks; ++var3) {
			runTick();
		}

		mcProfiler.endStartSection("preRenderErrors");
		final long var6 = System.nanoTime() - var5;
		checkGLError("Pre render");
		RenderBlocks.fancyGrass = gameSettings.fancyGraphics;
		mcProfiler.endStartSection("sound");
		mcSoundHandler.func_147691_a(thePlayer, timer.renderPartialTicks);
		mcProfiler.endSection();
		mcProfiler.startSection("render");
		GL11.glPushMatrix();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		mcFramebuffer.bindFramebuffer(true);
		mcProfiler.startSection("display");
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		if (thePlayer != null && thePlayer.isEntityInsideOpaqueBlock()) {
			gameSettings.thirdPersonView = 0;
		}

		mcProfiler.endSection();

		if (!skipRenderWorld) {
			mcProfiler.endStartSection("gameRenderer");
			entityRenderer.updateCameraAndRender(timer.renderPartialTicks);
			mcProfiler.endSection();
		}

		GL11.glFlush();
		mcProfiler.endSection();

		if (!Display.isActive() && fullscreen) {
			toggleFullscreen();
		}

		if (gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart) {
			if (!mcProfiler.profilingEnabled) {
				mcProfiler.clearProfiling();
			}

			mcProfiler.profilingEnabled = true;
			displayDebugInfo(var6);
		} else {
			mcProfiler.profilingEnabled = false;
			prevFrameTime = System.nanoTime();
		}

		guiAchievement.func_146254_a();
		mcFramebuffer.unbindFramebuffer();
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		mcFramebuffer.framebufferRender(displayWidth, displayHeight);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		entityRenderer.func_152430_c(timer.renderPartialTicks);
		GL11.glPopMatrix();
		mcProfiler.startSection("root");
		func_147120_f();
		Thread.yield();
		mcProfiler.startSection("stream");
		mcProfiler.startSection("update");
		field_152353_at.func_152935_j();
		mcProfiler.endStartSection("submit");
		field_152353_at.func_152922_k();
		mcProfiler.endSection();
		mcProfiler.endSection();
		checkGLError("Post render");
		++fpsCounter;
		isGamePaused = isSingleplayer() && currentScreen != null
				&& currentScreen.doesGuiPauseGame()
				&& !theIntegratedServer.getPublic();

		while (getSystemTime() >= debugUpdateTime + 1000L) {
			debugFPS = fpsCounter;
			debug = debugFPS + " fps, " + WorldRenderer.chunksUpdated
					+ " chunk updates";
			WorldRenderer.chunksUpdated = 0;
			debugUpdateTime += 1000L;
			fpsCounter = 0;
			usageSnooper.addMemoryStatsToSnooper();

			if (!usageSnooper.isSnooperRunning()) {
				usageSnooper.startSnooper();
			}
		}

		mcProfiler.endSection();

		if (isFramerateLimitBelowMax()) {
			Display.sync(getLimitFramerate());
		}
	}

	/**
	 * Runs the current tick.
	 */
	public void runTick() {
		Client.getEventManager().call(new EventTick());
		mcProfiler.startSection("scheduledExecutables");
		synchronized (field_152351_aB) {
			while (!field_152351_aB.isEmpty()) {
				((FutureTask) field_152351_aB.poll()).run();
			}
		}

		mcProfiler.endSection();

		if (rightClickDelayTimer > 0) {
			--rightClickDelayTimer;
		}

		mcProfiler.startSection("gui");

		if (!isGamePaused) {
			ingameGUI.updateTick();
		}

		mcProfiler.endStartSection("pick");
		entityRenderer.getMouseOver(1.0F);
		mcProfiler.endStartSection("gameMode");

		if (!isGamePaused && theWorld != null) {
			playerController.updateController();
		}

		mcProfiler.endStartSection("textures");

		if (!isGamePaused) {
			renderEngine.tick();
		}

		if (currentScreen == null && thePlayer != null) {
			if (thePlayer.getHealth() <= 0.0F) {
				displayGuiScreen((GuiScreen) null);
			} else if (thePlayer.isPlayerSleeping() && theWorld != null) {
				displayGuiScreen(new GuiSleepMP());
			}
		} else if (currentScreen != null && currentScreen instanceof GuiSleepMP
				&& !thePlayer.isPlayerSleeping()) {
			displayGuiScreen((GuiScreen) null);
		}

		if (currentScreen != null) {
			leftClickCounter = 10000;
		}

		CrashReport var2;
		CrashReportCategory var3;

		if (currentScreen != null) {
			try {
				currentScreen.handleInput();
			} catch (final Throwable var6) {
				var2 = CrashReport.makeCrashReport(var6,
						"Updating screen events");
				var3 = var2.makeCategory("Affected screen");
				var3.addCrashSectionCallable("Screen name", new Callable() {

					@Override
					public String call() {
						return currentScreen.getClass().getCanonicalName();
					}
				});
				throw new ReportedException(var2);
			}

			if (currentScreen != null) {
				try {
					currentScreen.updateScreen();
				} catch (final Throwable var5) {
					var2 = CrashReport.makeCrashReport(var5, "Ticking screen");
					var3 = var2.makeCategory("Affected screen");
					var3.addCrashSectionCallable("Screen name", new Callable() {

						@Override
						public String call() {
							return currentScreen.getClass().getCanonicalName();
						}
					});
					throw new ReportedException(var2);
				}
			}
		}

		if (currentScreen == null || currentScreen.field_146291_p) {
			mcProfiler.endStartSection("mouse");
			int var9;

			while (Mouse.next()) {
				var9 = Mouse.getEventButton();
				KeyBinding.setKeyBindState(var9 - 100,
						Mouse.getEventButtonState());

				if (Mouse.getEventButtonState()) {
					KeyBinding.onTick(var9 - 100);
				}

				final long var11 = getSystemTime() - systemTime;

				if (var11 <= 200L) {
					int var4 = Mouse.getEventDWheel();

					if (var4 != 0) {
						thePlayer.inventory.changeCurrentItem(var4);

						if (gameSettings.noclip) {
							if (var4 > 0) {
								var4 = 1;
							}

							if (var4 < 0) {
								var4 = -1;
							}

							gameSettings.noclipRate += var4 * 0.25F;
						}
					}

					if (currentScreen == null) {
						if (!inGameHasFocus && Mouse.getEventButtonState()) {
							setIngameFocus();
						}
					} else if (currentScreen != null) {
						currentScreen.handleMouseInput();
					}
				}
			}

			if (leftClickCounter > 0) {
				--leftClickCounter;
			}

			mcProfiler.endStartSection("keyboard");
			boolean var10;

			while (Keyboard.next()) {
				KeyBinding.setKeyBindState(Keyboard.getEventKey(),
						Keyboard.getEventKeyState());

				if (Keyboard.getEventKeyState()) {
					KeyBinding.onTick(Keyboard.getEventKey());
				}

				if (field_83002_am > 0L) {
					if (getSystemTime() - field_83002_am >= 6000L)
						throw new ReportedException(new CrashReport(
								"Manually triggered debug crash",
								new Throwable()));

					if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
						field_83002_am = -1L;
					}
				} else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
					field_83002_am = getSystemTime();
				}

				func_152348_aa();

				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == 62 && entityRenderer != null) {
						entityRenderer.deactivateShader();
					}

					if (currentScreen != null) {
						currentScreen.handleKeyboardInput();
					} else {
						//final EventKeyPressed event = new EventKeyPressed(
							//	Keyboard.getEventKey());
						//Client.getEventManager().call(event);
						//event.checkKey();
						if (Keyboard.getEventKey() == 1) {
							displayInGameMenu();
						}

						if (Keyboard.getEventKey() == 31
								&& Keyboard.isKeyDown(61)) {
							refreshResources();
						}

						if (Keyboard.getEventKey() == 20
								&& Keyboard.isKeyDown(61)) {
							refreshResources();
						}

						if (Keyboard.getEventKey() == 33
								&& Keyboard.isKeyDown(61)) {
							var10 = Keyboard.isKeyDown(42)
									| Keyboard.isKeyDown(54);
							gameSettings.setOptionValue(
									GameSettings.Options.RENDER_DISTANCE,
									var10 ? -1 : 1);
						}

						if (Keyboard.getEventKey() == 30
								&& Keyboard.isKeyDown(61)) {
							renderGlobal.loadRenderers();
						}

						if (Keyboard.getEventKey() == 35
								&& Keyboard.isKeyDown(61)) {
							gameSettings.advancedItemTooltips = !gameSettings.advancedItemTooltips;
							gameSettings.saveOptions();
						}

						if (Keyboard.getEventKey() == 48
								&& Keyboard.isKeyDown(61)) {
							RenderManager.field_85095_o = !RenderManager.field_85095_o;
						}

						if (Keyboard.getEventKey() == 25
								&& Keyboard.isKeyDown(61)) {
							gameSettings.pauseOnLostFocus = !gameSettings.pauseOnLostFocus;
							gameSettings.saveOptions();
						}

						if (Keyboard.getEventKey() == 59) {
							gameSettings.hideGUI = !gameSettings.hideGUI;
						}

						if (Keyboard.getEventKey() == 61) {
							gameSettings.showDebugInfo = !gameSettings.showDebugInfo;
							gameSettings.showDebugProfilerChart = GuiScreen
									.isShiftKeyDown();
						}

						if (gameSettings.keyBindTogglePerspective.isPressed()) {
							++gameSettings.thirdPersonView;

							if (gameSettings.thirdPersonView > 2) {
								gameSettings.thirdPersonView = 0;
							}
						}

						if (gameSettings.keyBindSmoothCamera.isPressed()) {
							gameSettings.smoothCamera = !gameSettings.smoothCamera;
						}
					}

					if (gameSettings.showDebugInfo
							&& gameSettings.showDebugProfilerChart) {
						if (Keyboard.getEventKey() == 11) {
							updateDebugProfilerName(0);
						}

						for (var9 = 0; var9 < 9; ++var9) {
							if (Keyboard.getEventKey() == 2 + var9) {
								updateDebugProfilerName(var9 + 1);
							}
						}
					}
				}
			}

			for (var9 = 0; var9 < 9; ++var9) {
				if (gameSettings.keyBindsHotbar[var9].isPressed()) {
					thePlayer.inventory.currentItem = var9;
				}
			}

			var10 = gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

			while (gameSettings.keyBindInventory.isPressed()) {
				if (playerController.func_110738_j()) {
					thePlayer.func_110322_i();
				} else {
					getNetHandler()
							.addToSendQueue(
									new C16PacketClientStatus(
											C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
					displayGuiScreen(new GuiInventory(thePlayer));
				}
			}

			while (gameSettings.keyBindDrop.isPressed()) {
				thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown());
			}

			while (gameSettings.keyBindChat.isPressed() && var10) {
				displayGuiScreen(new GuiChat());
			}

			if (currentScreen == null
					&& gameSettings.keyBindCommand.isPressed() && var10) {
				displayGuiScreen(new GuiChat("/"));
			}

			if (thePlayer.isUsingItem()) {
				if (!gameSettings.keyBindUseItem.getIsKeyPressed()) {
					playerController.onStoppedUsingItem(thePlayer);
				}

				label391:

				while (true) {
					if (!gameSettings.keyBindAttack.isPressed()) {
						while (gameSettings.keyBindUseItem.isPressed()) {
							;
						}

						while (true) {
							if (gameSettings.keyBindPickBlock.isPressed()) {
								continue;
							}

							break label391;
						}
					}
				}
			} else {
				while (gameSettings.keyBindAttack.isPressed()) {
					func_147116_af();
				}

				while (gameSettings.keyBindUseItem.isPressed()) {
					func_147121_ag();
				}

				while (gameSettings.keyBindPickBlock.isPressed()) {
					func_147112_ai();
				}
			}

			if (gameSettings.keyBindUseItem.getIsKeyPressed()
					&& rightClickDelayTimer == 0 && !thePlayer.isUsingItem()) {
				func_147121_ag();
			}

			func_147115_a(currentScreen == null
					&& gameSettings.keyBindAttack.getIsKeyPressed()
					&& inGameHasFocus);
		}

		if (theWorld != null) {
			if (thePlayer != null) {
				++joinPlayerCounter;

				if (joinPlayerCounter == 30) {
					joinPlayerCounter = 0;
					theWorld.joinEntityInSurroundings(thePlayer);
				}
			}

			mcProfiler.endStartSection("gameRenderer");

			if (!isGamePaused) {
				entityRenderer.updateRenderer();
			}

			mcProfiler.endStartSection("levelRenderer");

			if (!isGamePaused) {
				renderGlobal.updateClouds();
			}

			mcProfiler.endStartSection("level");

			if (!isGamePaused) {
				if (theWorld.lastLightningBolt > 0) {
					--theWorld.lastLightningBolt;
				}

				theWorld.updateEntities();
			}
		}

		if (!isGamePaused) {
			mcMusicTicker.update();
			mcSoundHandler.update();
		}

		if (theWorld != null) {
			if (!isGamePaused) {
				theWorld.setAllowedSpawnTypes(
						theWorld.difficultySetting != EnumDifficulty.PEACEFUL,
						true);

				try {
					theWorld.tick();
				} catch (final Throwable var7) {
					var2 = CrashReport.makeCrashReport(var7,
							"Exception in world tick");

					if (theWorld == null) {
						var3 = var2.makeCategory("Affected level");
						var3.addCrashSection("Problem", "Level is null!");
					} else {
						theWorld.addWorldInfoToCrashReport(var2);
					}

					throw new ReportedException(var2);
				}
			}

			mcProfiler.endStartSection("animateTick");

			if (!isGamePaused && theWorld != null) {
				theWorld.doVoidFogParticles(
						MathHelper.floor_double(thePlayer.posX),
						MathHelper.floor_double(thePlayer.posY),
						MathHelper.floor_double(thePlayer.posZ));
			}

			mcProfiler.endStartSection("particles");

			if (!isGamePaused) {
				effectRenderer.updateEffects();
			}
		} else if (myNetworkManager != null) {
			mcProfiler.endStartSection("pendingConnection");
			myNetworkManager.processReceivedPackets();
		}

		mcProfiler.endSection();
		systemTime = getSystemTime();
	}

	/**
	 * Loads Tessellator with a scaled resolution
	 */
	public void scaledTessellator(int p_71392_1_, int p_71392_2_,
			int p_71392_3_, int p_71392_4_, int p_71392_5_, int p_71392_6_) {
		final float var7 = 0.00390625F;
		final float var8 = 0.00390625F;
		final Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(p_71392_1_ + 0, p_71392_2_ + p_71392_6_, 0.0D,
				(p_71392_3_ + 0) * var7, (p_71392_4_ + p_71392_6_) * var8);
		var9.addVertexWithUV(p_71392_1_ + p_71392_5_, p_71392_2_ + p_71392_6_,
				0.0D, (p_71392_3_ + p_71392_5_) * var7,
				(p_71392_4_ + p_71392_6_) * var8);
		var9.addVertexWithUV(p_71392_1_ + p_71392_5_, p_71392_2_ + 0, 0.0D,
				(p_71392_3_ + p_71392_5_) * var7, (p_71392_4_ + 0) * var8);
		var9.addVertexWithUV(p_71392_1_ + 0, p_71392_2_ + 0, 0.0D,
				(p_71392_3_ + 0) * var7, (p_71392_4_ + 0) * var8);
		var9.draw();
	}

	public void scheduleResourcesRefresh() {
		refreshTexturePacksScheduled = true;
	}

	public void setDimensionAndSpawnPlayer(int p_71354_1_) {
		theWorld.setSpawnLocation();
		theWorld.removeAllEntities();
		int var2 = 0;
		String var3 = null;

		if (thePlayer != null) {
			var2 = thePlayer.getEntityId();
			theWorld.removeEntity(thePlayer);
			var3 = thePlayer.func_142021_k();
		}

		renderViewEntity = null;
		thePlayer = playerController.func_147493_a(
				theWorld,
				thePlayer == null ? new StatFileWriter() : thePlayer
						.func_146107_m());
		thePlayer.dimension = p_71354_1_;
		renderViewEntity = thePlayer;
		thePlayer.preparePlayerToSpawn();
		thePlayer.func_142020_c(var3);
		theWorld.spawnEntityInWorld(thePlayer);
		playerController.flipPlayer(thePlayer);
		thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
		thePlayer.setEntityId(var2);
		playerController.setPlayerCapabilities(thePlayer);

		if (currentScreen instanceof GuiGameOver) {
			displayGuiScreen((GuiScreen) null);
		}
	}

	/**
	 * Will set the focus to ingame if the Minecraft window is the active with
	 * focus. Also clears any GUI screen currently displayed
	 */
	public void setIngameFocus() {
		if (Display.isActive()) {
			if (!inGameHasFocus) {
				inGameHasFocus = true;
				mouseHelper.grabMouseCursor();
				displayGuiScreen((GuiScreen) null);
				leftClickCounter = 10000;
			}
		}
	}

	/**
	 * Resets the player keystate, disables the ingame focus, and ungrabs the
	 * mouse cursor.
	 */
	public void setIngameNotInFocus() {
		if (inGameHasFocus) {
			KeyBinding.unPressAllKeys();
			inGameHasFocus = false;
			mouseHelper.ungrabMouseCursor();
		}
	}

	public void setServer(String p_71367_1_, int p_71367_2_) {
		serverName = p_71367_1_;
		serverPort = p_71367_2_;
	}

	/**
	 * Set the current ServerData instance.
	 */
	public void setServerData(ServerData p_71351_1_) {
		currentServerData = p_71351_1_;
	}

	/**
	 * Called when the window is closing. Sets 'running' to false which allows
	 * the game loop to exit cleanly.
	 */
	public void shutdown() {
		running = false;
	}

	/**
	 * Shuts down the minecraft applet by stopping the resource downloads, and
	 * clearing up GL stuff; called when the application (or web page) is
	 * exited.
	 */
	public void shutdownMinecraftApplet() {
		try {
			field_152353_at.func_152923_i();
			logger.info("Stopping!");

			try {
				this.loadWorld((WorldClient) null);
			} catch (final Throwable var7) {
				;
			}

			try {
				GLAllocation.deleteTexturesAndDisplayLists();
			} catch (final Throwable var6) {
				;
			}

			mcSoundHandler.func_147685_d();
		} finally {
			Display.destroy();

			if (!hasCrashed) {
				System.exit(0);
			}
		}

		System.gc();
	}

	/**
	 * Starts the game: initializes the canvas, the title, the settings,
	 * etcetera.
	 */
	private void startGame() throws LWJGLException {
		gameSettings = new GameSettings(this, mcDataDir);

		if (gameSettings.overrideHeight > 0 && gameSettings.overrideWidth > 0) {
			displayWidth = gameSettings.overrideWidth;
			displayHeight = gameSettings.overrideHeight;
		}

		if (fullscreen) {
			Display.setFullscreen(true);
			displayWidth = Display.getDisplayMode().getWidth();
			displayHeight = Display.getDisplayMode().getHeight();

			if (displayWidth <= 0) {
				displayWidth = 1;
			}

			if (displayHeight <= 0) {
				displayHeight = 1;
			}
		} else {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
		}

		Display.setResizable(true);
		Display.setTitle("Minecraft 1.7.10");
		logger.info("LWJGL Version: " + Sys.getVersion());
		final Util.EnumOS var1 = Util.getOSType();

		if (var1 != Util.EnumOS.OSX) {
			try {
				final InputStream var2 = mcDefaultResourcePack
						.func_152780_c(new ResourceLocation(
								"icons/icon_16x16.png"));
				final InputStream var3 = mcDefaultResourcePack
						.func_152780_c(new ResourceLocation(
								"icons/icon_32x32.png"));

				if (var2 != null && var3 != null) {
					Display.setIcon(new ByteBuffer[] { func_152340_a(var2),
							func_152340_a(var3) });
				}
			} catch (final IOException var8) {
				logger.error("Couldn\'t set icon", var8);
			}
		}

		try {
			Display.create(new PixelFormat().withDepthBits(24));
		} catch (final LWJGLException var7) {
			logger.error("Couldn\'t set pixel format", var7);

			try {
				Thread.sleep(1000L);
			} catch (final InterruptedException var6) {
				;
			}

			if (fullscreen) {
				updateDisplayMode();
			}

			Display.create();
		}

		OpenGlHelper.initializeTextures();

		try {
			field_152353_at = new TwitchStream(this,
					(String) Iterables.getFirst(
							field_152356_J.get("twitch_access_token"),
							(Object) null));
		} catch (final Throwable var5) {
			field_152353_at = new NullStream(var5);
			logger.error("Couldn\'t initialize twitch stream");
		}

		mcFramebuffer = new Framebuffer(displayWidth, displayHeight, true);
		mcFramebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		guiAchievement = new GuiAchievement(this);
		metadataSerializer_.registerMetadataSectionType(
				new TextureMetadataSectionSerializer(),
				TextureMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(
				new FontMetadataSectionSerializer(), FontMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(
				new AnimationMetadataSectionSerializer(),
				AnimationMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(
				new PackMetadataSectionSerializer(), PackMetadataSection.class);
		metadataSerializer_.registerMetadataSectionType(
				new LanguageMetadataSectionSerializer(),
				LanguageMetadataSection.class);
		saveLoader = new AnvilSaveConverter(new File(mcDataDir, "saves"));
		mcResourcePackRepository = new ResourcePackRepository(
				fileResourcepacks,
				new File(mcDataDir, "server-resource-packs"),
				mcDefaultResourcePack, metadataSerializer_, gameSettings);
		mcResourceManager = new SimpleReloadableResourceManager(
				metadataSerializer_);
		mcLanguageManager = new LanguageManager(metadataSerializer_,
				gameSettings.language);
		mcResourceManager.registerReloadListener(mcLanguageManager);
		refreshResources();
		renderEngine = new TextureManager(mcResourceManager);
		mcResourceManager.registerReloadListener(renderEngine);
		field_152350_aA = new SkinManager(renderEngine, new File(fileAssets,
				"skins"), field_152355_az);
		loadScreen();
		mcSoundHandler = new SoundHandler(mcResourceManager, gameSettings);
		mcResourceManager.registerReloadListener(mcSoundHandler);
		mcMusicTicker = new MusicTicker(this);
		fontRenderer = new FontRenderer(gameSettings, new ResourceLocation(
				"textures/font/ascii.png"), renderEngine, false);

		if (gameSettings.language != null) {
			fontRenderer.setUnicodeFlag(func_152349_b());
			fontRenderer.setBidiFlag(mcLanguageManager
					.isCurrentLanguageBidirectional());
		}

		standardGalacticFontRenderer = new FontRenderer(gameSettings,
				new ResourceLocation("textures/font/ascii_sga.png"),
				renderEngine, false);
		mcResourceManager.registerReloadListener(fontRenderer);
		mcResourceManager.registerReloadListener(standardGalacticFontRenderer);
		mcResourceManager
				.registerReloadListener(new GrassColorReloadListener());
		mcResourceManager
				.registerReloadListener(new FoliageColorReloadListener());
		RenderManager.instance.itemRenderer = new ItemRenderer(this);
		entityRenderer = new EntityRenderer(this, mcResourceManager);
		mcResourceManager.registerReloadListener(entityRenderer);
		AchievementList.openInventory
				.setStatStringFormatter(new IStatStringFormat() {

					@Override
					public String formatString(String p_74535_1_) {
						try {
							return String
									.format(p_74535_1_,
											new Object[] { GameSettings
													.getKeyDisplayString(gameSettings.keyBindInventory
															.getKeyCode()) });
						} catch (final Exception var3) {
							return "Error: " + var3.getLocalizedMessage();
						}
					}
				});
		mouseHelper = new MouseHelper();
		checkGLError("Pre startup");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearDepth(1.0D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		checkGLError("Startup");
		renderGlobal = new RenderGlobal(this);
		textureMapBlocks = new TextureMap(0, "textures/blocks");
		textureMapBlocks.func_147632_b(gameSettings.anisotropicFiltering);
		textureMapBlocks.func_147633_a(gameSettings.mipmapLevels);
		renderEngine.loadTextureMap(TextureMap.locationBlocksTexture,
				textureMapBlocks);
		renderEngine.loadTextureMap(TextureMap.locationItemsTexture,
				new TextureMap(1, "textures/items"));
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		effectRenderer = new EffectRenderer(theWorld, renderEngine);
		checkGLError("Post startup");
		ingameGUI = new GuiIngame(this);

		renderEngine.func_147645_c(field_152354_ay);
		field_152354_ay = null;
		loadingScreen = new LoadingScreenRenderer(this);

		if (gameSettings.fullScreen && !fullscreen) {
			toggleFullscreen();
		}

		try {
			Display.setVSyncEnabled(gameSettings.enableVsync);
		} catch (final OpenGLException var4) {
			gameSettings.enableVsync = false;
			gameSettings.saveOptions();
		}

		Client.onStartup();
	}

	private void startTimerHackThread() {
		final Thread var1 = new Thread("Timer hack thread") {

			@Override
			public void run() {
				while (running) {
					try {
						Thread.sleep(2147483647L);
					} catch (final InterruptedException var2) {
						;
					}
				}
			}
		};
		var1.setDaemon(true);
		var1.start();
	}

	/**
	 * Toggles fullscreen mode.
	 */
	public void toggleFullscreen() {
		try {
			fullscreen = !fullscreen;

			if (fullscreen) {
				updateDisplayMode();
				displayWidth = Display.getDisplayMode().getWidth();
				displayHeight = Display.getDisplayMode().getHeight();

				if (displayWidth <= 0) {
					displayWidth = 1;
				}

				if (displayHeight <= 0) {
					displayHeight = 1;
				}
			} else {
				Display.setDisplayMode(new DisplayMode(tempDisplayWidth,
						tempDisplayHeight));
				displayWidth = tempDisplayWidth;
				displayHeight = tempDisplayHeight;

				if (displayWidth <= 0) {
					displayWidth = 1;
				}

				if (displayHeight <= 0) {
					displayHeight = 1;
				}
			}

			if (currentScreen != null) {
				resize(displayWidth, displayHeight);
			} else {
				updateFramebufferSize();
			}

			Display.setFullscreen(fullscreen);
			Display.setVSyncEnabled(gameSettings.enableVsync);
			func_147120_f();
		} catch (final Exception var2) {
			logger.error("Couldn\'t toggle fullscreen", var2);
		}
	}

	/**
	 * Update debugProfilerName in response to number keys in debug screen
	 */
	private void updateDebugProfilerName(int p_71383_1_) {
		final List var2 = mcProfiler.getProfilingData(debugProfilerName);

		if (var2 != null && !var2.isEmpty()) {
			final Profiler.Result var3 = (Profiler.Result) var2.remove(0);

			if (p_71383_1_ == 0) {
				if (var3.field_76331_c.length() > 0) {
					final int var4 = debugProfilerName.lastIndexOf(".");

					if (var4 >= 0) {
						debugProfilerName = debugProfilerName
								.substring(0, var4);
					}
				}
			} else {
				--p_71383_1_;

				if (p_71383_1_ < var2.size()
						&& !((Profiler.Result) var2.get(p_71383_1_)).field_76331_c
								.equals("unspecified")) {
					if (debugProfilerName.length() > 0) {
						debugProfilerName = debugProfilerName + ".";
					}

					debugProfilerName = debugProfilerName
							+ ((Profiler.Result) var2.get(p_71383_1_)).field_76331_c;
				}
			}
		}
	}

	private void updateDisplayMode() throws LWJGLException {
		final HashSet var1 = new HashSet();
		Collections.addAll(var1, Display.getAvailableDisplayModes());
		DisplayMode var2 = Display.getDesktopDisplayMode();

		if (!var1.contains(var2) && Util.getOSType() == Util.EnumOS.OSX) {
			final Iterator var3 = macDisplayModes.iterator();

			while (var3.hasNext()) {
				final DisplayMode var4 = (DisplayMode) var3.next();
				boolean var5 = true;
				Iterator var6 = var1.iterator();
				DisplayMode var7;

				while (var6.hasNext()) {
					var7 = (DisplayMode) var6.next();

					if (var7.getBitsPerPixel() == 32
							&& var7.getWidth() == var4.getWidth()
							&& var7.getHeight() == var4.getHeight()) {
						var5 = false;
						break;
					}
				}

				if (!var5) {
					var6 = var1.iterator();

					while (var6.hasNext()) {
						var7 = (DisplayMode) var6.next();

						if (var7.getBitsPerPixel() == 32
								&& var7.getWidth() == var4.getWidth() / 2
								&& var7.getHeight() == var4.getHeight() / 2) {
							var2 = var7;
							break;
						}
					}
				}
			}
		}

		Display.setDisplayMode(var2);
		displayWidth = var2.getWidth();
		displayHeight = var2.getHeight();
	}

	private void updateFramebufferSize() {
		mcFramebuffer.createBindFramebuffer(displayWidth, displayHeight);

		if (entityRenderer != null) {
			entityRenderer.updateShaderGroupSize(displayWidth, displayHeight);
		}
	}
}

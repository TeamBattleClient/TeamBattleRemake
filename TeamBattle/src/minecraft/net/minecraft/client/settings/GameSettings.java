package net.minecraft.client.settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.src.BlockUtils;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColorizer;
import net.minecraft.src.CustomSky;
import net.minecraft.src.IWrUpdater;
import net.minecraft.src.NaturalTextures;
import net.minecraft.src.RandomMobs;
import net.minecraft.src.Reflector;
import net.minecraft.src.TextureUtils;
import net.minecraft.src.WrUpdaterSmooth;
import net.minecraft.src.WrUpdaterThreaded;
import net.minecraft.src.WrUpdates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

public class GameSettings {
	public static enum Options {
		AA_LEVEL("AA_LEVEL", 96, "", 999, "Antialiasing", false, false), ADVANCED_OPENGL(
				"ADVANCED_OPENGL", 8, "ADVANCED_OPENGL", 8,
				"options.advancedOpengl", false, true), AMBIENT_OCCLUSION(
				"AMBIENT_OCCLUSION", 13, "AMBIENT_OCCLUSION", 13, "options.ao",
				false, false), ANAGLYPH("ANAGLYPH", 7, "ANAGLYPH", 7,
				"options.anaglyph", false, true), ANIMATED_EXPLOSION(
				"ANIMATED_EXPLOSION", 65, "", 999, "Explosion Animated", false,
				false), ANIMATED_FIRE("ANIMATED_FIRE", 58, "", 999,
				"Fire Animated", false, false), ANIMATED_FLAME(
				"ANIMATED_FLAME", 66, "", 999, "Flame Animated", false, false), ANIMATED_ITEMS(
				"ANIMATED_ITEMS", 88, "", 999, "Items Animated", false, false), ANIMATED_LAVA(
				"ANIMATED_LAVA", 57, "", 999, "Lava Animated", false, false), ANIMATED_PORTAL(
				"ANIMATED_PORTAL", 59, "", 999, "Portal Animated", false, false), ANIMATED_REDSTONE(
				"ANIMATED_REDSTONE", 64, "", 999, "Redstone Animated", false,
				false), ANIMATED_SMOKE("ANIMATED_SMOKE", 67, "", 999,
				"Smoke Animated", false, false), ANIMATED_TERRAIN(
				"ANIMATED_TERRAIN", 87, "", 999, "Terrain Animated", false,
				false), ANIMATED_TEXTURES("ANIMATED_TEXTURES", 97, "", 999,
				"Textures Animated", false, false), ANIMATED_WATER(
				"ANIMATED_WATER", 56, "", 999, "Water Animated", false, false), ANISOTROPIC_FILTERING(
				"ANISOTROPIC_FILTERING", 32, "ANISOTROPIC_FILTERING", 32,
				"options.anisotropicFiltering", true, false, 1.0F, 16.0F, 0.0F,
				(Object) null, null) {

			@Override
			protected float snapToStep(float p_148264_1_) {
				return MathHelper.roundUpToPowerOfTwo((int) p_148264_1_);
			}
		},
		AO_LEVEL("AO_LEVEL", 60, "", 999, "Smooth Lighting Level", true, false), AUTOSAVE_TICKS(
				"AUTOSAVE_TICKS", 62, "", 999, "Autosave", false, false), BETTER_GRASS(
				"BETTER_GRASS", 63, "", 999, "Better Grass", false, false), BETTER_SNOW(
				"BETTER_SNOW", 85, "", 999, "Better Snow", false, false), CHAT_COLOR(
				"CHAT_COLOR", 18, "CHAT_COLOR", 18, "options.chat.color",
				false, true), CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 29,
				"CHAT_HEIGHT_FOCUSED", 29, "options.chat.height.focused", true,
				false), CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 30,
				"CHAT_HEIGHT_UNFOCUSED", 30, "options.chat.height.unfocused",
				true, false), CHAT_LINKS("CHAT_LINKS", 19, "CHAT_LINKS", 19,
				"options.chat.links", false, true), CHAT_LINKS_PROMPT(
				"CHAT_LINKS_PROMPT", 21, "CHAT_LINKS_PROMPT", 21,
				"options.chat.links.prompt", false, true), CHAT_OPACITY(
				"CHAT_OPACITY", 20, "CHAT_OPACITY", 20, "options.chat.opacity",
				true, false), CHAT_SCALE("CHAT_SCALE", 27, "CHAT_SCALE", 27,
				"options.chat.scale", true, false), CHAT_VISIBILITY(
				"CHAT_VISIBILITY", 17, "CHAT_VISIBILITY", 17,
				"options.chat.visibility", false, false), CHAT_WIDTH(
				"CHAT_WIDTH", 28, "CHAT_WIDTH", 28, "options.chat.width", true,
				false), CHUNK_LOADING("CHUNK_LOADING", 99, "", 999,
				"Chunk Loading", false, false), CHUNK_UPDATES("CHUNK_UPDATES",
				72, "", 999, "Chunk Updates", false, false), CHUNK_UPDATES_DYNAMIC(
				"CHUNK_UPDATES_DYNAMIC", 73, "", 999, "Dynamic Updates", false,
				false), CLEAR_WATER("CLEAR_WATER", 75, "", 999, "Clear Water",
				false, false), CLOUD_HEIGHT("CLOUD_HEIGHT", 51, "", 999,
				"Cloud Height", true, false), CLOUDS("CLOUDS", 50, "", 999,
				"Clouds", false, false), CONNECTED_TEXTURES(
				"CONNECTED_TEXTURES", 95, "", 999, "Connected Textures", false,
				false), CUSTOM_COLORS("CUSTOM_COLORS", 93, "", 999,
				"Custom Colors", false, false), CUSTOM_FONTS("CUSTOM_FONTS",
				92, "", 999, "Custom Fonts", false, false), CUSTOM_SKY(
				"CUSTOM_SKY", 103, "", 999, "Custom Sky", false, false), DEPTH_FOG(
				"DEPTH_FOG", 77, "", 999, "Depth Fog", false, false), DIFFICULTY(
				"DIFFICULTY", 11, "DIFFICULTY", 11, "options.difficulty",
				false, false), DRIPPING_WATER_LAVA("DRIPPING_WATER_LAVA", 84,
				"", 999, "Dripping Water/Lava", false, false), DROPPED_ITEMS(
				"DROPPED_ITEMS", 101, "", 999, "Dropped Items", false, false), ENABLE_VSYNC(
				"ENABLE_VSYNC", 24, "ENABLE_VSYNC", 24, "options.vsync", false,
				true), FAST_MATH("FAST_MATH", 104, "", 999, "Fast Math", false,
				false), FAST_RENDER("FAST_RENDER", 105, "", 999, "Fast Render",
				false, false), FBO_ENABLE("FBO_ENABLE", 10, "FBO_ENABLE", 10,
				"options.fboEnable", false, true), FOG_FANCY("FOG_FANCY", 44,
				"FOG", 999, "Fog", false, false), FOG_START("FOG_START", 45,
				"", 999, "Fog Start", false, false), FORCE_UNICODE_FONT(
				"FORCE_UNICODE_FONT", 33, "FORCE_UNICODE_FONT", 33,
				"options.forceUnicodeFont", false, true), FOV("FOV", 2, "FOV",
				2, "options.fov", true, false, 30.0F, 110.0F, 1.0F), FRAMERATE_LIMIT(
				"FRAMERATE_LIMIT", 9, "FRAMERATE_LIMIT", 9,
				"options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F), FULLSCREEN_MODE(
				"FULLSCREEN_MODE", 86, "", 999, "Fullscreen Mode", false, false), GAMMA(
				"GAMMA", 3, "GAMMA", 3, "options.gamma", true, false), GRAPHICS(
				"GRAPHICS", 12, "GRAPHICS", 12, "options.graphics", false,
				false), GRASS("GRASS", 53, "", 999, "Grass", false, false), GUI_SCALE(
				"GUI_SCALE", 14, "GUI_SCALE", 14, "options.guiScale", false,
				false), HELD_ITEM_TOOLTIPS("HELD_ITEM_TOOLTIPS", 100, "", 999,
				"Held Item Tooltips", false, false), INVERT_MOUSE(
				"INVERT_MOUSE", 0, "INVERT_MOUSE", 0, "options.invertMouse",
				false, true), LAGOMETER("LAGOMETER", 61, "", 999, "Lagometer",
				false, false), LAZY_CHUNK_LOADING("LAZY_CHUNK_LOADING", 102,
				"", 999, "Lazy Chunk Loading", false, false), LOAD_FAR(
				"LOAD_FAR", 47, "", 999, "Load Far", false, false), MIPMAP_LEVELS(
				"MIPMAP_LEVELS", 31, "MIPMAP_LEVELS", 31,
				"options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F), MIPMAP_TYPE(
				"MIPMAP_TYPE", 46, "", 999, "Mipmap Type", false, false), NATURAL_TEXTURES(
				"NATURAL_TEXTURES", 98, "", 999, "Natural Textures", false,
				false), PARTICLES("PARTICLES", 16, "PARTICLES", 16,
				"options.particles", false, false), PORTAL_PARTICLES(
				"PORTAL_PARTICLES", 81, "", 999, "Portal Particles", false,
				false), POTION_PARTICLES("POTION_PARTICLES", 82, "", 999,
				"Potion Particles", false, false), PRELOADED_CHUNKS(
				"PRELOADED_CHUNKS", 48, "", 999, "Preloaded Chunks", false,
				false), PROFILER("PROFILER", 83, "", 999, "Debug Profiler",
				false, false), RAIN("RAIN", 54, "", 999, "Rain & Snow", false,
				false), RAIN_SPLASH("RAIN_SPLASH", 80, "", 999, "Rain Splash",
				false, false), RANDOM_MOBS("RANDOM_MOBS", 90, "", 999,
				"Random Mobs", false, false), RENDER_CLOUDS("RENDER_CLOUDS",
				15, "RENDER_CLOUDS", 15, "options.renderClouds", false, true), RENDER_DISTANCE(
				"RENDER_DISTANCE", 5, "RENDER_DISTANCE", 5,
				"options.renderDistance", true, false, 2.0F, 16.0F, 1.0F), SATURATION(
				"SATURATION", 4, "SATURATION", 4, "options.saturation", true,
				false), SENSITIVITY("SENSITIVITY", 1, "SENSITIVITY", 1,
				"options.sensitivity", true, false), SHOW_CAPE("SHOW_CAPE", 25,
				"SHOW_CAPE", 25, "options.showCape", false, true), SHOW_CAPES(
				"SHOW_CAPES", 94, "", 999, "Show Capes", false, false), SKY(
				"SKY", 69, "", 999, "Sky", false, false), SMOOTH_BIOMES(
				"SMOOTH_BIOMES", 91, "", 999, "Smooth Biomes", false, false), SMOOTH_FPS(
				"SMOOTH_FPS", 49, "", 999, "Smooth FPS", false, false), SMOOTH_WORLD(
				"SMOOTH_WORLD", 76, "", 999, "Smooth World", false, false), SNOOPER_ENABLED(
				"SNOOPER_ENABLED", 22, "SNOOPER_ENABLED", 22,
				"options.snooper", false, true), STARS("STARS", 70, "", 999,
				"Stars", false, false), STREAM_BYTES_PER_PIXEL(
				"STREAM_BYTES_PER_PIXEL", 34, "STREAM_BYTES_PER_PIXEL", 34,
				"options.stream.bytesPerPixel", true, false), STREAM_CHAT_ENABLED(
				"STREAM_CHAT_ENABLED", 41, "STREAM_CHAT_ENABLED", 41,
				"options.stream.chat.enabled", false, false), STREAM_CHAT_USER_FILTER(
				"STREAM_CHAT_USER_FILTER", 42, "STREAM_CHAT_USER_FILTER", 42,
				"options.stream.chat.userFilter", false, false), STREAM_COMPRESSION(
				"STREAM_COMPRESSION", 39, "STREAM_COMPRESSION", 39,
				"options.stream.compression", false, false), STREAM_FPS(
				"STREAM_FPS", 38, "STREAM_FPS", 38, "options.stream.fps", true,
				false), STREAM_KBPS("STREAM_KBPS", 37, "STREAM_KBPS", 37,
				"options.stream.kbps", true, false), STREAM_MIC_TOGGLE_BEHAVIOR(
				"STREAM_MIC_TOGGLE_BEHAVIOR", 43, "STREAM_MIC_TOGGLE_BEHAVIOR",
				43, "options.stream.micToggleBehavior", false, false), STREAM_SEND_METADATA(
				"STREAM_SEND_METADATA", 40, "STREAM_SEND_METADATA", 40,
				"options.stream.sendMetadata", false, true), STREAM_VOLUME_MIC(
				"STREAM_VOLUME_MIC", 35, "STREAM_VOLUME_MIC", 35,
				"options.stream.micVolumne", true, false), STREAM_VOLUME_SYSTEM(
				"STREAM_VOLUME_SYSTEM", 36, "STREAM_VOLUME_SYSTEM", 36,
				"options.stream.systemVolume", true, false), SUN_MOON(
				"SUN_MOON", 71, "", 999, "Sun & Moon", false, false), SWAMP_COLORS(
				"SWAMP_COLORS", 89, "", 999, "Swamp Colors", false, false), TIME(
				"TIME", 74, "", 999, "Time", false, false), TOUCHSCREEN(
				"TOUCHSCREEN", 26, "TOUCHSCREEN", 26, "options.touchscreen",
				false, true), TRANSLUCENT_BLOCKS("TRANSLUCENT_BLOCKS", 106, "",
				999, "Translucent Blocks", false, false), TREES("TREES", 52,
				"", 999, "Trees", false, false), USE_FULLSCREEN(
				"USE_FULLSCREEN", 23, "USE_FULLSCREEN", 23,
				"options.fullscreen", false, true), VIEW_BOBBING(
				"VIEW_BOBBING", 6, "VIEW_BOBBING", 6, "options.viewBobbing",
				false, true), VOID_PARTICLES("VOID_PARTICLES", 78, "", 999,
				"Void Particles", false, false), WATER("WATER", 55, "", 999,
				"Water", false, false), WATER_PARTICLES("WATER_PARTICLES", 79,
				"", 999, "Water Particles", false, false), WEATHER("WEATHER",
				68, "", 999, "Weather", false, false);
		public static GameSettings.Options getEnumOptions(int par0) {
			final GameSettings.Options[] var1 = values();
			final int var2 = var1.length;

			for (int var3 = 0; var3 < var2; ++var3) {
				final GameSettings.Options var4 = var1[var3];

				if (var4.returnEnumOrdinal() == par0)
					return var4;
			}

			return null;
		}

		private final boolean enumBoolean;
		private final boolean enumFloat;
		private final String enumString;
		private float valueMax;
		private float valueMin;

		private final float valueStep;

		private Options(String p_i1230_1_, int p_i1230_2_, String par1Str,
				int par2, String par3Str, boolean par4, boolean par5) {
			this(p_i1230_1_, p_i1230_2_, par1Str, par2, par3Str, par4, par5,
					0.0F, 1.0F, 0.0F);
		}

		private Options(String p_i1231_1_, int p_i1231_2_, String p_i45004_1_,
				int p_i45004_2_, String p_i45004_3_, boolean p_i45004_4_,
				boolean p_i45004_5_, float p_i45004_6_, float p_i45004_7_,
				float p_i45004_8_) {
			enumString = p_i45004_3_;
			enumFloat = p_i45004_4_;
			enumBoolean = p_i45004_5_;
			valueMin = p_i45004_6_;
			valueMax = p_i45004_7_;
			valueStep = p_i45004_8_;
		}

		private Options(String p_i1232_1_, int p_i1232_2_, String p_i45005_1_,
				int p_i45005_2_, String p_i45005_3_, boolean p_i45005_4_,
				boolean p_i45005_5_, float p_i45005_6_, float p_i45005_7_,
				float p_i45005_8_, Object p_i45005_9_) {
			this(p_i1232_1_, p_i1232_2_, p_i45005_1_, p_i45005_2_, p_i45005_3_,
					p_i45005_4_, p_i45005_5_, p_i45005_6_, p_i45005_7_,
					p_i45005_8_);
		}

		Options(String x0, int x1, String x2, int x3, String x4, boolean x5,
				boolean x6, float x7, float x8, float x9, Object x10, Object x11) {
			this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10);
		}

		public float denormalizeValue(float p_148262_1_) {
			return snapToStepClamp(valueMin + (valueMax - valueMin)
					* MathHelper.clamp_float(p_148262_1_, 0.0F, 1.0F));
		}

		public boolean getEnumBoolean() {
			return enumBoolean;
		}

		public boolean getEnumFloat() {
			return enumFloat;
		}

		public String getEnumString() {
			return enumString;
		}

		public float getValueMax() {
			return valueMax;
		}

		public float normalizeValue(float p_148266_1_) {
			return MathHelper.clamp_float(
					(snapToStepClamp(p_148266_1_) - valueMin)
							/ (valueMax - valueMin), 0.0F, 1.0F);
		}

		public int returnEnumOrdinal() {
			return ordinal();
		}

		public void setValueMax(float p_148263_1_) {
			valueMax = p_148263_1_;
		}

		protected float snapToStep(float p_148264_1_) {
			if (valueStep > 0.0F) {
				p_148264_1_ = valueStep * Math.round(p_148264_1_ / valueStep);
			}

			return p_148264_1_;
		}

		public float snapToStepClamp(float p_148268_1_) {
			p_148268_1_ = snapToStep(p_148268_1_);
			return MathHelper.clamp_float(p_148268_1_, valueMin, valueMax);
		}
	}

	static final class SwitchOptions {
		static final int[] optionIds = new int[GameSettings.Options.values().length];

		static {
			try {
				optionIds[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
			} catch (final NoSuchFieldError var16) {
				;
			}

			try {
				optionIds[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
			} catch (final NoSuchFieldError var15) {
				;
			}

			try {
				optionIds[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
			} catch (final NoSuchFieldError var14) {
				;
			}

			try {
				optionIds[GameSettings.Options.ADVANCED_OPENGL.ordinal()] = 4;
			} catch (final NoSuchFieldError var13) {
				;
			}

			try {
				optionIds[GameSettings.Options.FBO_ENABLE.ordinal()] = 5;
			} catch (final NoSuchFieldError var12) {
				;
			}

			try {
				optionIds[GameSettings.Options.RENDER_CLOUDS.ordinal()] = 6;
			} catch (final NoSuchFieldError var11) {
				;
			}

			try {
				optionIds[GameSettings.Options.CHAT_COLOR.ordinal()] = 7;
			} catch (final NoSuchFieldError var10) {
				;
			}

			try {
				optionIds[GameSettings.Options.CHAT_LINKS.ordinal()] = 8;
			} catch (final NoSuchFieldError var9) {
				;
			}

			try {
				optionIds[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 9;
			} catch (final NoSuchFieldError var8) {
				;
			}

			try {
				optionIds[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 10;
			} catch (final NoSuchFieldError var7) {
				;
			}

			try {
				optionIds[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 11;
			} catch (final NoSuchFieldError var6) {
				;
			}

			try {
				optionIds[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 12;
			} catch (final NoSuchFieldError var5) {
				;
			}

			try {
				optionIds[GameSettings.Options.SHOW_CAPE.ordinal()] = 13;
			} catch (final NoSuchFieldError var4) {
				;
			}

			try {
				optionIds[GameSettings.Options.TOUCHSCREEN.ordinal()] = 14;
			} catch (final NoSuchFieldError var3) {
				;
			}

			try {
				optionIds[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 15;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				optionIds[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 16;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	private static final String[] AMBIENT_OCCLUSIONS = new String[] {
			"options.ao.off", "options.ao.min", "options.ao.max" };

	public static final int ANIM_GENERATED = 1;

	public static final int ANIM_OFF = 2;

	public static final int ANIM_ON = 0;
	public static final int CL_DEFAULT = 0;
	public static final int CL_SMOOTH = 1;
	public static final int CL_THREADED = 2;
	public static final int DEFAULT = 0;
	public static final String DEFAULT_STR = "Default";
	public static final int FANCY = 2;
	public static final int FAST = 1;
	private static final String[] field_152391_aS = new String[] {
			"options.stream.compression.low",
			"options.stream.compression.medium",
			"options.stream.compression.high" };
	private static final String[] field_152392_aT = new String[] {
			"options.stream.chat.enabled.streaming",
			"options.stream.chat.enabled.always",
			"options.stream.chat.enabled.never" };

	private static final String[] field_152393_aU = new String[] {
			"options.stream.chat.userFilter.all",
			"options.stream.chat.userFilter.subs",
			"options.stream.chat.userFilter.mods" };
	private static final String[] field_152394_aV = new String[] {
			"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk" };
	private static final Gson gson = new Gson();
	/** GUI scale values */
	private static final String[] GUISCALES = new String[] {
			"options.guiScale.auto", "options.guiScale.small",
			"options.guiScale.normal", "options.guiScale.large" };

	private static final Logger logger = LogManager.getLogger();
	public static final int OFF = 3;
	private static final String[] PARTICLES = new String[] {
			"options.particles.all", "options.particles.decreased",
			"options.particles.minimal" };
	private static final ParameterizedType typeListString = new ParameterizedType() {

		@Override
		public Type[] getActualTypeArguments() {
			return new Type[] { String.class };
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

		@Override
		public Type getRawType() {
			return List.class;
		}
	};

	/**
	 * Represents a key or mouse button as a string. Args: key
	 */
	public static String getKeyDisplayString(int par0) {
		return par0 < 0 ? I18n.format("key.mouseButton",
				new Object[] { Integer.valueOf(par0 + 101) }) : Keyboard
				.getKeyName(par0);
	}

	/**
	 * Returns the translation of the given index in the given String array. If
	 * the index is smaller than 0 or greater than/equal to the length of the
	 * String array, it is changed to 0.
	 */
	private static String getTranslation(String[] par0ArrayOfStr, int par1) {
		if (par1 < 0 || par1 >= par0ArrayOfStr.length) {
			par1 = 0;
		}

		return I18n.format(par0ArrayOfStr[par1], new Object[0]);
	}

	/**
	 * Returns whether the specified key binding is currently being pressed.
	 */
	public static boolean isKeyDown(KeyBinding par0KeyBinding) {
		return par0KeyBinding.getKeyCode() == 0 ? false : par0KeyBinding
				.getKeyCode() < 0 ? Mouse.isButtonDown(par0KeyBinding
				.getKeyCode() + 100) : Keyboard.isKeyDown(par0KeyBinding
				.getKeyCode());
	}

	/**
	 * Whether to show advanced information on item tooltips, toggled by F3+H
	 */
	public boolean advancedItemTooltips;
	/** Advanced OpenGL */
	public boolean advancedOpengl;
	/** Smooth Lighting */
	public int ambientOcclusion = 2;
	public boolean anaglyph;
	public int anisotropicFiltering;
	public boolean chatColours;
	public float chatHeightFocused;
	public float chatHeightUnfocused;
	public boolean chatLinks;
	public boolean chatLinksPrompt;
	public float chatOpacity;
	public float chatScale;
	public EntityPlayer.EnumChatVisibility chatVisibility;
	public float chatWidth;
	/** Clouds flag */
	public boolean clouds = true;
	public boolean debugCamEnable;
	/** Change rate for debug camera */
	public float debugCamRate;
	public EnumDifficulty difficulty;
	public boolean enableVsync;
	public boolean fancyGraphics = true;
	public boolean fboEnable = true;
	public KeyBinding field_152395_am;
	public KeyBinding field_152396_an;
	public KeyBinding field_152397_ao;
	public KeyBinding field_152398_ap;
	public KeyBinding field_152399_aq;
	public float field_152400_J;
	public float field_152401_K;
	public float field_152402_L;
	public float field_152403_M;
	public float field_152404_N;
	public int field_152405_O;
	public boolean field_152406_P;
	public String field_152407_Q;
	public int field_152408_R;
	public int field_152409_S;
	public int field_152410_T;
	public boolean forceUnicodeFont;
	public float fovSetting;
	public boolean fullScreen;
	public float gammaSetting;
	/** GUI scale */
	public int guiScale;
	public boolean heldItemTooltips;
	public boolean hideGUI;
	public boolean hideServerAddress;
	public boolean invertMouse;
	public KeyBinding keyBindAttack;
	public KeyBinding keyBindBack;
	public KeyBinding keyBindChat;
	public KeyBinding keyBindCommand;
	public KeyBinding keyBindDrop;
	public KeyBinding keyBindForward;
	public KeyBinding[] keyBindings;
	public KeyBinding keyBindInventory;
	public KeyBinding keyBindJump;
	public KeyBinding keyBindLeft;
	public KeyBinding keyBindPickBlock;
	public KeyBinding keyBindPlayerList;
	public KeyBinding keyBindRight;
	public KeyBinding keyBindScreenshot;
	public KeyBinding[] keyBindsHotbar;
	public KeyBinding keyBindSmoothCamera;
	public KeyBinding keyBindSneak;
	public KeyBinding keyBindSprint;
	public KeyBinding keyBindTogglePerspective;
	public KeyBinding keyBindUseItem;
	/** Game settings language */
	public String language;
	/** The lastServer string. */
	public String lastServer;
	public int limitFramerate = 120;
	private final Map mapSoundLevels;

	protected Minecraft mc;
	public int mipmapLevels;
	public float mouseSensitivity = 0.5F;
	/** No clipping for singleplayer */
	public boolean noclip;
	/** No clipping movement rate */
	public float noclipRate;
	public int ofAaLevel = 0;
	public boolean ofAnimatedExplosion = true;
	public boolean ofAnimatedFire = true;
	public boolean ofAnimatedFlame = true;
	public boolean ofAnimatedItems = true;
	public int ofAnimatedLava = 0;

	public boolean ofAnimatedPortal = true;

	public boolean ofAnimatedRedstone = true;

	public boolean ofAnimatedSmoke = true;
	public boolean ofAnimatedTerrain = true;
	public boolean ofAnimatedTextures = true;
	public int ofAnimatedWater = 0;
	public float ofAoLevel = 1.0F;
	public int ofAutoSaveTicks = 4000;
	public int ofBetterGrass = 3;
	public boolean ofBetterSnow = false;
	public int ofChunkLoading = 0;
	public int ofChunkUpdates = 1;
	public boolean ofChunkUpdatesDynamic = false;
	public boolean ofClearWater = false;
	public int ofClouds = 0;
	public float ofCloudsHeight = 0.0F;
	public int ofConnectedTextures = 2;
	public boolean ofCustomColors = true;
	public boolean ofCustomFonts = true;
	public boolean ofCustomSky = true;
	public boolean ofDepthFog = true;
	public boolean ofDrippingWaterLava = true;
	public int ofDroppedItems = 0;
	public boolean ofFastMath = false;
	public boolean ofFastRender = true;
	public float ofFogStart = 0.8F;
	public int ofFogType = 1;
	public String ofFullscreenMode = "Default";
	public int ofGrass = 0;
	public KeyBinding ofKeyBindZoom;
	public boolean ofLagometer = false;
	public boolean ofLazyChunkLoading = Config.isSingleProcessor();
	public boolean ofLoadFar = false;
	public int ofMipmapType = 0;
	public boolean ofNaturalTextures = false;
	public boolean ofOcclusionFancy = false;
	public boolean ofPortalParticles = true;
	public boolean ofPotionParticles = true;
	public int ofPreloadedChunks = 0;
	public boolean ofProfiler = false;
	public int ofRain = 0;
	public boolean ofRainSplash = true;
	public boolean ofRandomMobs = true;
	public boolean ofShowCapes = true;
	public boolean ofSky = true;
	public boolean ofSmoothBiomes = true;
	public boolean ofSmoothFps = false;
	public boolean ofSmoothWorld = Config.isSingleProcessor();
	public boolean ofStars = true;
	public boolean ofSunMoon = true;
	public boolean ofSwampColors = true;
	public int ofTime = 0;
	public int ofTranslucentBlocks = 2;
	public int ofTrees = 0;
	public boolean ofVoidParticles = true;
	public int ofWater = 0;

	public boolean ofWaterParticles = true;
	public boolean ofWeather = true;

	private File optionsFile;

	private File optionsFileOF;

	public int overrideHeight;
	public int overrideWidth;

	/** Determines amount of particles. 0 = All, 1 = Decreased, 2 = Minimal */
	public int particleSetting;

	/** Whether to pause when the game loses focus, toggled by F3+P */
	public boolean pauseOnLostFocus;
	public int renderDistanceChunks = -1;
	public List resourcePacks = new ArrayList();
	public float saturation;

	/** Whether to show your cape */
	public boolean showCape;

	/** true if debug info should be displayed instead of version */
	public boolean showDebugInfo;

	public boolean showDebugProfilerChart;
	public boolean showInventoryAchievementHint;

	/** Smooth Camera Toggle */
	public boolean smoothCamera;

	public boolean snooperEnabled;

	public int thirdPersonView;

	public boolean touchscreen;

	public boolean viewBobbing = true;

	public GameSettings() {
		chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
		chatColours = true;
		chatLinks = true;
		chatLinksPrompt = true;
		chatOpacity = 1.0F;
		snooperEnabled = true;
		enableVsync = true;
		pauseOnLostFocus = true;
		showCape = true;
		heldItemTooltips = true;
		chatScale = 1.0F;
		chatWidth = 1.0F;
		chatHeightUnfocused = 0.44366196F;
		chatHeightFocused = 1.0F;
		showInventoryAchievementHint = true;
		mipmapLevels = 4;
		anisotropicFiltering = 1;
		mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
		field_152400_J = 0.5F;
		field_152401_K = 1.0F;
		field_152402_L = 1.0F;
		field_152403_M = 0.5412844F;
		field_152404_N = 0.31690142F;
		field_152405_O = 1;
		field_152406_P = true;
		field_152407_Q = "";
		field_152408_R = 0;
		field_152409_S = 0;
		field_152410_T = 0;
		keyBindForward = new KeyBinding("key.forward", 17,
				"key.categories.movement");
		keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
		keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
		keyBindRight = new KeyBinding("key.right", 32,
				"key.categories.movement");
		keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
		keyBindSneak = new KeyBinding("key.sneak", 42,
				"key.categories.movement");
		keyBindInventory = new KeyBinding("key.inventory", 18,
				"key.categories.inventory");
		keyBindUseItem = new KeyBinding("key.use", -99,
				"key.categories.gameplay");
		keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
		keyBindAttack = new KeyBinding("key.attack", -100,
				"key.categories.gameplay");
		keyBindPickBlock = new KeyBinding("key.pickItem", -98,
				"key.categories.gameplay");
		keyBindSprint = new KeyBinding("key.sprint", 29,
				"key.categories.gameplay");
		keyBindChat = new KeyBinding("key.chat", 20,
				"key.categories.multiplayer");
		keyBindPlayerList = new KeyBinding("key.playerlist", 15,
				"key.categories.multiplayer");
		keyBindCommand = new KeyBinding("key.command", 53,
				"key.categories.multiplayer");
		keyBindScreenshot = new KeyBinding("key.screenshot", 60,
				"key.categories.misc");
		keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63,
				"key.categories.misc");
		keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0,
				"key.categories.misc");
		field_152395_am = new KeyBinding("key.fullscreen", 87,
				"key.categories.misc");
		field_152396_an = new KeyBinding("key.streamStartStop", 64,
				"key.categories.stream");
		field_152397_ao = new KeyBinding("key.streamPauseUnpause", 65,
				"key.categories.stream");
		field_152398_ap = new KeyBinding("key.streamCommercial", 0,
				"key.categories.stream");
		field_152399_aq = new KeyBinding("key.streamToggleMic", 0,
				"key.categories.stream");
		keyBindsHotbar = new KeyBinding[] {
				new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"),
				new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"),
				new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"),
				new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"),
				new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"),
				new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"),
				new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
				new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
				new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
		keyBindings = ArrayUtils.addAll(new KeyBinding[] { keyBindAttack,
				keyBindUseItem, keyBindForward, keyBindLeft, keyBindBack,
				keyBindRight, keyBindJump, keyBindSneak, keyBindDrop,
				keyBindInventory, keyBindChat, keyBindPlayerList,
				keyBindPickBlock, keyBindCommand, keyBindScreenshot,
				keyBindTogglePerspective, keyBindSmoothCamera, keyBindSprint,
				field_152396_an, field_152397_ao, field_152398_ap,
				field_152399_aq, field_152395_am }, keyBindsHotbar);
		difficulty = EnumDifficulty.NORMAL;
		lastServer = "";
		noclipRate = 1.0F;
		debugCamRate = 1.0F;
		fovSetting = 70.0F;
		language = "en_US";
		forceUnicodeFont = false;
		limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT
				.getValueMax();
		ofKeyBindZoom = new KeyBinding("Zoom", 29, "key.categories.misc");
		keyBindings = ArrayUtils.add(keyBindings, ofKeyBindZoom);
	}

	public GameSettings(Minecraft par1Minecraft, File par2File) {
		chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
		chatColours = true;
		chatLinks = true;
		chatLinksPrompt = true;
		chatOpacity = 1.0F;
		snooperEnabled = true;
		enableVsync = true;
		pauseOnLostFocus = true;
		showCape = true;
		heldItemTooltips = true;
		chatScale = 1.0F;
		chatWidth = 1.0F;
		chatHeightUnfocused = 0.44366196F;
		chatHeightFocused = 1.0F;
		showInventoryAchievementHint = true;
		mipmapLevels = 4;
		anisotropicFiltering = 1;
		mapSoundLevels = Maps.newEnumMap(SoundCategory.class);
		field_152400_J = 0.5F;
		field_152401_K = 1.0F;
		field_152402_L = 1.0F;
		field_152403_M = 0.5412844F;
		field_152404_N = 0.31690142F;
		field_152405_O = 1;
		field_152406_P = true;
		field_152407_Q = "";
		field_152408_R = 0;
		field_152409_S = 0;
		field_152410_T = 0;
		keyBindForward = new KeyBinding("key.forward", 17,
				"key.categories.movement");
		keyBindLeft = new KeyBinding("key.left", 30, "key.categories.movement");
		keyBindBack = new KeyBinding("key.back", 31, "key.categories.movement");
		keyBindRight = new KeyBinding("key.right", 32,
				"key.categories.movement");
		keyBindJump = new KeyBinding("key.jump", 57, "key.categories.movement");
		keyBindSneak = new KeyBinding("key.sneak", 42,
				"key.categories.movement");
		keyBindInventory = new KeyBinding("key.inventory", 18,
				"key.categories.inventory");
		keyBindUseItem = new KeyBinding("key.use", -99,
				"key.categories.gameplay");
		keyBindDrop = new KeyBinding("key.drop", 16, "key.categories.gameplay");
		keyBindAttack = new KeyBinding("key.attack", -100,
				"key.categories.gameplay");
		keyBindPickBlock = new KeyBinding("key.pickItem", -98,
				"key.categories.gameplay");
		keyBindSprint = new KeyBinding("key.sprint", 29,
				"key.categories.gameplay");
		keyBindChat = new KeyBinding("key.chat", 20,
				"key.categories.multiplayer");
		keyBindPlayerList = new KeyBinding("key.playerlist", 15,
				"key.categories.multiplayer");
		keyBindCommand = new KeyBinding("key.command", 53,
				"key.categories.multiplayer");
		keyBindScreenshot = new KeyBinding("key.screenshot", 60,
				"key.categories.misc");
		keyBindTogglePerspective = new KeyBinding("key.togglePerspective", 63,
				"key.categories.misc");
		keyBindSmoothCamera = new KeyBinding("key.smoothCamera", 0,
				"key.categories.misc");
		field_152395_am = new KeyBinding("key.fullscreen", 87,
				"key.categories.misc");
		field_152396_an = new KeyBinding("key.streamStartStop", 64,
				"key.categories.stream");
		field_152397_ao = new KeyBinding("key.streamPauseUnpause", 65,
				"key.categories.stream");
		field_152398_ap = new KeyBinding("key.streamCommercial", 0,
				"key.categories.stream");
		field_152399_aq = new KeyBinding("key.streamToggleMic", 0,
				"key.categories.stream");
		keyBindsHotbar = new KeyBinding[] {
				new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"),
				new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"),
				new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"),
				new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"),
				new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"),
				new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"),
				new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"),
				new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"),
				new KeyBinding("key.hotbar.9", 10, "key.categories.inventory") };
		keyBindings = ArrayUtils.addAll(new KeyBinding[] { keyBindAttack,
				keyBindUseItem, keyBindForward, keyBindLeft, keyBindBack,
				keyBindRight, keyBindJump, keyBindSneak, keyBindDrop,
				keyBindInventory, keyBindChat, keyBindPlayerList,
				keyBindPickBlock, keyBindCommand, keyBindScreenshot,
				keyBindTogglePerspective, keyBindSmoothCamera, keyBindSprint,
				field_152396_an, field_152397_ao, field_152398_ap,
				field_152399_aq, field_152395_am }, keyBindsHotbar);
		difficulty = EnumDifficulty.NORMAL;
		lastServer = "";
		noclipRate = 1.0F;
		debugCamRate = 1.0F;
		fovSetting = 70.0F;
		language = "en_US";
		forceUnicodeFont = false;
		mc = par1Minecraft;
		optionsFile = new File(par2File, "options.txt");
		optionsFileOF = new File(par2File, "optionsof.txt");
		limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT
				.getValueMax();
		ofKeyBindZoom = new KeyBinding("Zoom", 29, "key.categories.misc");
		keyBindings = ArrayUtils.add(keyBindings, ofKeyBindZoom);
		GameSettings.Options.RENDER_DISTANCE.setValueMax(32.0F);
		renderDistanceChunks = par1Minecraft.isJava64bit() ? 12 : 8;
		loadOptions();
		Config.initGameSettings(this);
	}

	/**
	 * Gets a key binding.
	 */
	public String getKeyBinding(GameSettings.Options par1EnumOptions) {
		final String var2 = I18n.format(par1EnumOptions.getEnumString(),
				new Object[0]) + ": ";

		if (par1EnumOptions == GameSettings.Options.RENDER_DISTANCE) {
			final int var33 = (int) getOptionFloatValue(par1EnumOptions);
			String var41 = "Tiny";
			byte baseDist = 2;

			if (var33 >= 4) {
				var41 = "Short";
				baseDist = 4;
			}

			if (var33 >= 8) {
				var41 = "Normal";
				baseDist = 8;
			}

			if (var33 >= 16) {
				var41 = "Far";
				baseDist = 16;
			}

			if (var33 >= 32) {
				var41 = "Extreme";
				baseDist = 32;
			}

			final int diff = renderDistanceChunks - baseDist;
			String descr = var41;

			if (diff > 0) {
				descr = var41 + "+";
			}

			return var2 + var33 + " " + descr + "";
		} else if (par1EnumOptions == GameSettings.Options.ADVANCED_OPENGL)
			return !advancedOpengl ? var2 + "OFF" : ofOcclusionFancy ? var2
					+ "Fancy" : var2 + "Fast";
		else if (par1EnumOptions == GameSettings.Options.FOG_FANCY) {
			switch (ofFogType) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			case 3:
				return var2 + "OFF";

			default:
				return var2 + "OFF";
			}
		} else if (par1EnumOptions == GameSettings.Options.FOG_START)
			return var2 + ofFogStart;
		else if (par1EnumOptions == GameSettings.Options.MIPMAP_TYPE) {
			switch (ofMipmapType) {
			case 0:
				return var2 + "Nearest";

			case 1:
				return var2 + "Linear";

			case 2:
				return var2 + "Bilinear";

			case 3:
				return var2 + "Trilinear";

			default:
				return var2 + "Nearest";
			}
		} else if (par1EnumOptions == GameSettings.Options.LOAD_FAR)
			return ofLoadFar ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.PRELOADED_CHUNKS)
			return ofPreloadedChunks == 0 ? var2 + "OFF" : var2
					+ ofPreloadedChunks;
		else if (par1EnumOptions == GameSettings.Options.SMOOTH_FPS)
			return ofSmoothFps ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.SMOOTH_WORLD)
			return ofSmoothWorld ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.CLOUDS) {
			switch (ofClouds) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			case 3:
				return var2 + "OFF";

			default:
				return var2 + "Default";
			}
		} else if (par1EnumOptions == GameSettings.Options.TREES) {
			switch (ofTrees) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			default:
				return var2 + "Default";
			}
		} else if (par1EnumOptions == GameSettings.Options.GRASS) {
			switch (ofGrass) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			default:
				return var2 + "Default";
			}
		} else if (par1EnumOptions == GameSettings.Options.DROPPED_ITEMS) {
			switch (ofDroppedItems) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			default:
				return var2 + "Default";
			}
		} else if (par1EnumOptions == GameSettings.Options.RAIN) {
			switch (ofRain) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			case 3:
				return var2 + "OFF";

			default:
				return var2 + "Default";
			}
		} else if (par1EnumOptions == GameSettings.Options.WATER) {
			switch (ofWater) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			case 3:
				return var2 + "OFF";

			default:
				return var2 + "Default";
			}
		} else if (par1EnumOptions == GameSettings.Options.ANIMATED_WATER) {
			switch (ofAnimatedWater) {
			case 1:
				return var2 + "Dynamic";

			case 2:
				return var2 + "OFF";

			default:
				return var2 + "ON";
			}
		} else if (par1EnumOptions == GameSettings.Options.ANIMATED_LAVA) {
			switch (ofAnimatedLava) {
			case 1:
				return var2 + "Dynamic";

			case 2:
				return var2 + "OFF";

			default:
				return var2 + "ON";
			}
		} else if (par1EnumOptions == GameSettings.Options.ANIMATED_FIRE)
			return ofAnimatedFire ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_PORTAL)
			return ofAnimatedPortal ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_REDSTONE)
			return ofAnimatedRedstone ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_EXPLOSION)
			return ofAnimatedExplosion ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_FLAME)
			return ofAnimatedFlame ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_SMOKE)
			return ofAnimatedSmoke ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.VOID_PARTICLES)
			return ofVoidParticles ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.WATER_PARTICLES)
			return ofWaterParticles ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.PORTAL_PARTICLES)
			return ofPortalParticles ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.POTION_PARTICLES)
			return ofPotionParticles ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.DRIPPING_WATER_LAVA)
			return ofDrippingWaterLava ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_TERRAIN)
			return ofAnimatedTerrain ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_TEXTURES)
			return ofAnimatedTextures ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.ANIMATED_ITEMS)
			return ofAnimatedItems ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.RAIN_SPLASH)
			return ofRainSplash ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.LAGOMETER)
			return ofLagometer ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.AUTOSAVE_TICKS)
			return ofAutoSaveTicks <= 40 ? var2 + "Default (2s)"
					: ofAutoSaveTicks <= 400 ? var2 + "20s"
							: ofAutoSaveTicks <= 4000 ? var2 + "3min" : var2
									+ "30min";
		else if (par1EnumOptions == GameSettings.Options.BETTER_GRASS) {
			switch (ofBetterGrass) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			default:
				return var2 + "OFF";
			}
		} else if (par1EnumOptions == GameSettings.Options.CONNECTED_TEXTURES) {
			switch (ofConnectedTextures) {
			case 1:
				return var2 + "Fast";

			case 2:
				return var2 + "Fancy";

			default:
				return var2 + "OFF";
			}
		} else if (par1EnumOptions == GameSettings.Options.WEATHER)
			return ofWeather ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.SKY)
			return ofSky ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.STARS)
			return ofStars ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.SUN_MOON)
			return ofSunMoon ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES)
			return var2 + ofChunkUpdates;
		else if (par1EnumOptions == GameSettings.Options.CHUNK_LOADING)
			return ofChunkLoading == 1 ? var2 + "Smooth"
					: ofChunkLoading == 2 ? var2 + "Multi-Core" : var2
							+ "Default";
		else if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES_DYNAMIC)
			return ofChunkUpdatesDynamic ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.TIME)
			return ofTime == 1 ? var2 + "Day Only" : ofTime == 3 ? var2
					+ "Night Only" : var2 + "Default";
		else if (par1EnumOptions == GameSettings.Options.CLEAR_WATER)
			return ofClearWater ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.DEPTH_FOG)
			return ofDepthFog ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.AA_LEVEL)
			return ofAaLevel == 0 ? var2 + "OFF" : var2 + ofAaLevel;
		else if (par1EnumOptions == GameSettings.Options.PROFILER)
			return ofProfiler ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.BETTER_SNOW)
			return ofBetterSnow ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.SWAMP_COLORS)
			return ofSwampColors ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.RANDOM_MOBS)
			return ofRandomMobs ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.SMOOTH_BIOMES)
			return ofSmoothBiomes ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.CUSTOM_FONTS)
			return ofCustomFonts ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.CUSTOM_COLORS)
			return ofCustomColors ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.CUSTOM_SKY)
			return ofCustomSky ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.SHOW_CAPES)
			return ofShowCapes ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.NATURAL_TEXTURES)
			return ofNaturalTextures ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.FAST_MATH)
			return ofFastMath ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.FAST_RENDER)
			return ofFastRender ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.TRANSLUCENT_BLOCKS)
			return ofTranslucentBlocks == 1 ? var2 + "Fast" : var2 + "Fancy";
		else if (par1EnumOptions == GameSettings.Options.LAZY_CHUNK_LOADING)
			return ofLazyChunkLoading ? var2 + "ON" : var2 + "OFF";
		else if (par1EnumOptions == GameSettings.Options.FULLSCREEN_MODE)
			return var2 + ofFullscreenMode;
		else if (par1EnumOptions == GameSettings.Options.HELD_ITEM_TOOLTIPS)
			return heldItemTooltips ? var2 + "ON" : var2 + "OFF";
		else {
			float var32;

			if (par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT) {
				var32 = getOptionFloatValue(par1EnumOptions);
				return var32 == 0.0F ? var2 + "VSync"
						: var32 == par1EnumOptions.valueMax ? var2
								+ I18n.format("options.framerateLimit.max",
										new Object[0]) : var2 + (int) var32
								+ " fps";
			} else if (par1EnumOptions.getEnumFloat()) {
				var32 = getOptionFloatValue(par1EnumOptions);
				final float var4 = par1EnumOptions.normalizeValue(var32);
				return par1EnumOptions == GameSettings.Options.SENSITIVITY ? var4 == 0.0F ? var2
						+ I18n.format("options.sensitivity.min", new Object[0])
						: var4 == 1.0F ? var2
								+ I18n.format("options.sensitivity.max",
										new Object[0]) : var2
								+ (int) (var4 * 200.0F) + "%"
						: par1EnumOptions == GameSettings.Options.FOV ? var32 == 70.0F ? var2
								+ I18n.format("options.fov.min", new Object[0])
								: var32 == 110.0F ? var2
										+ I18n.format("options.fov.max",
												new Object[0]) : var2
										+ (int) var32
								: par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT ? var32 == par1EnumOptions.valueMax ? var2
										+ I18n.format(
												"options.framerateLimit.max",
												new Object[0]) : var2
										+ (int) var32 + " fps"
										: par1EnumOptions == GameSettings.Options.GAMMA ? var4 == 0.0F ? var2
												+ I18n.format(
														"options.gamma.min",
														new Object[0])
												: var4 == 1.0F ? var2
														+ I18n.format(
																"options.gamma.max",
																new Object[0])
														: var2
																+ "+"
																+ (int) (var4 * 100.0F)
																+ "%"
												: par1EnumOptions == GameSettings.Options.SATURATION ? var2
														+ (int) (var4 * 400.0F)
														+ "%"
														: par1EnumOptions == GameSettings.Options.CHAT_OPACITY ? var2
																+ (int) (var4 * 90.0F + 10.0F)
																+ "%"
																: par1EnumOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED ? var2
																		+ GuiNewChat
																				.func_146243_b(var4)
																		+ "px"
																		: par1EnumOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED ? var2
																				+ GuiNewChat
																						.func_146243_b(var4)
																				+ "px"
																				: par1EnumOptions == GameSettings.Options.CHAT_WIDTH ? var2
																						+ GuiNewChat
																								.func_146233_a(var4)
																						+ "px"
																						: par1EnumOptions == GameSettings.Options.RENDER_DISTANCE ? var2
																								+ (int) var32
																								+ " chunks"
																								: par1EnumOptions == GameSettings.Options.ANISOTROPIC_FILTERING ? var32 == 1.0F ? var2
																										+ I18n.format(
																												"options.off",
																												new Object[0])
																										: var2
																												+ (int) var32
																										: par1EnumOptions == GameSettings.Options.MIPMAP_LEVELS ? var32 == 0.0F ? var2
																												+ I18n.format(
																														"options.off",
																														new Object[0])
																												: var2
																														+ (int) var32
																												: par1EnumOptions == GameSettings.Options.STREAM_FPS ? var2
																														+ TwitchStream
																																.func_152948_a(var4)
																														+ " fps"
																														: par1EnumOptions == GameSettings.Options.STREAM_KBPS ? var2
																																+ TwitchStream
																																		.func_152946_b(var4)
																																+ " Kbps"
																																: par1EnumOptions == GameSettings.Options.STREAM_BYTES_PER_PIXEL ? var2
																																		+ String.format(
																																				"%.3f bpp",
																																				new Object[] { Float
																																						.valueOf(TwitchStream
																																								.func_152947_c(var4)) })
																																		: var4 == 0.0F ? var2
																																				+ I18n.format(
																																						"options.off",
																																						new Object[0])
																																				: var2
																																						+ (int) (var4 * 100.0F)
																																						+ "%";
			} else if (par1EnumOptions.getEnumBoolean()) {
				final boolean var31 = getOptionOrdinalValue(par1EnumOptions);
				return var31 ? var2 + I18n.format("options.on", new Object[0])
						: var2 + I18n.format("options.off", new Object[0]);
			} else if (par1EnumOptions == GameSettings.Options.DIFFICULTY)
				return var2
						+ I18n.format(difficulty.getDifficultyResourceKey(),
								new Object[0]);
			else if (par1EnumOptions == GameSettings.Options.GUI_SCALE)
				return var2 + getTranslation(GUISCALES, guiScale);
			else if (par1EnumOptions == GameSettings.Options.CHAT_VISIBILITY)
				return var2
						+ I18n.format(chatVisibility.getResourceKey(),
								new Object[0]);
			else if (par1EnumOptions == GameSettings.Options.PARTICLES)
				return var2 + getTranslation(PARTICLES, particleSetting);
			else if (par1EnumOptions == GameSettings.Options.AMBIENT_OCCLUSION)
				return var2
						+ getTranslation(AMBIENT_OCCLUSIONS, ambientOcclusion);
			else if (par1EnumOptions == GameSettings.Options.STREAM_COMPRESSION)
				return var2 + getTranslation(field_152391_aS, field_152405_O);
			else if (par1EnumOptions == GameSettings.Options.STREAM_CHAT_ENABLED)
				return var2 + getTranslation(field_152392_aT, field_152408_R);
			else if (par1EnumOptions == GameSettings.Options.STREAM_CHAT_USER_FILTER)
				return var2 + getTranslation(field_152393_aU, field_152409_S);
			else if (par1EnumOptions == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR)
				return var2 + getTranslation(field_152394_aV, field_152410_T);
			else if (par1EnumOptions == GameSettings.Options.GRAPHICS) {
				if (fancyGraphics)
					return var2
							+ I18n.format("options.graphics.fancy",
									new Object[0]);
				else
					return var2
							+ I18n.format("options.graphics.fast",
									new Object[0]);
			} else
				return var2;
		}
	}

	public float getOptionFloatValue(GameSettings.Options par1EnumOptions) {
		return par1EnumOptions == GameSettings.Options.CLOUD_HEIGHT ? ofCloudsHeight
				: par1EnumOptions == GameSettings.Options.AO_LEVEL ? ofAoLevel
						: par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT ? limitFramerate == GameSettings.Options.FRAMERATE_LIMIT
								.getValueMax() && enableVsync ? 0.0F
								: (float) limitFramerate
								: par1EnumOptions == GameSettings.Options.FOV ? fovSetting
										: par1EnumOptions == GameSettings.Options.GAMMA ? gammaSetting
												: par1EnumOptions == GameSettings.Options.SATURATION ? saturation
														: par1EnumOptions == GameSettings.Options.SENSITIVITY ? mouseSensitivity
																: par1EnumOptions == GameSettings.Options.CHAT_OPACITY ? chatOpacity
																		: par1EnumOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED ? chatHeightFocused
																				: par1EnumOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED ? chatHeightUnfocused
																						: par1EnumOptions == GameSettings.Options.CHAT_SCALE ? chatScale
																								: par1EnumOptions == GameSettings.Options.CHAT_WIDTH ? chatWidth
																										: par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT ? (float) limitFramerate
																												: par1EnumOptions == GameSettings.Options.ANISOTROPIC_FILTERING ? (float) anisotropicFiltering
																														: par1EnumOptions == GameSettings.Options.MIPMAP_LEVELS ? (float) mipmapLevels
																																: par1EnumOptions == GameSettings.Options.RENDER_DISTANCE ? (float) renderDistanceChunks
																																		: par1EnumOptions == GameSettings.Options.STREAM_BYTES_PER_PIXEL ? field_152400_J
																																				: par1EnumOptions == GameSettings.Options.STREAM_VOLUME_MIC ? field_152401_K
																																						: par1EnumOptions == GameSettings.Options.STREAM_VOLUME_SYSTEM ? field_152402_L
																																								: par1EnumOptions == GameSettings.Options.STREAM_KBPS ? field_152403_M
																																										: par1EnumOptions == GameSettings.Options.STREAM_FPS ? field_152404_N
																																												: 0.0F;
	}

	public boolean getOptionOrdinalValue(GameSettings.Options par1EnumOptions) {
		switch (GameSettings.SwitchOptions.optionIds[par1EnumOptions.ordinal()]) {
		case 1:
			return invertMouse;

		case 2:
			return viewBobbing;

		case 3:
			return anaglyph;

		case 4:
			return advancedOpengl;

		case 5:
			return fboEnable;

		case 6:
			return clouds;

		case 7:
			return chatColours;

		case 8:
			return chatLinks;

		case 9:
			return chatLinksPrompt;

		case 10:
			return snooperEnabled;

		case 11:
			return fullScreen;

		case 12:
			return enableVsync;

		case 13:
			return showCape;

		case 14:
			return touchscreen;

		case 15:
			return field_152406_P;

		case 16:
			return forceUnicodeFont;

		default:
			return false;
		}
	}

	public float getSoundLevel(SoundCategory p_151438_1_) {
		return mapSoundLevels.containsKey(p_151438_1_) ? ((Float) mapSoundLevels
				.get(p_151438_1_)).floatValue() : 1.0F;
	}

	public void loadOfOptions() {
		try {
			File exception = optionsFileOF;

			if (!exception.exists()) {
				exception = optionsFile;
			}

			if (!exception.exists())
				return;

			final BufferedReader bufferedreader = new BufferedReader(
					new FileReader(exception));
			String s = "";

			while ((s = bufferedreader.readLine()) != null) {
				try {
					final String[] exception1 = s.split(":");

					if (exception1[0].equals("ofRenderDistanceChunks")
							&& exception1.length >= 2) {
						renderDistanceChunks = Integer.valueOf(exception1[1])
								.intValue();
						renderDistanceChunks = Config.limit(
								renderDistanceChunks, 2, 32);
					}

					if (exception1[0].equals("ofFogType")
							&& exception1.length >= 2) {
						ofFogType = Integer.valueOf(exception1[1]).intValue();
						ofFogType = Config.limit(ofFogType, 1, 3);
					}

					if (exception1[0].equals("ofFogStart")
							&& exception1.length >= 2) {
						ofFogStart = Float.valueOf(exception1[1]).floatValue();

						if (ofFogStart < 0.2F) {
							ofFogStart = 0.2F;
						}

						if (ofFogStart > 0.81F) {
							ofFogStart = 0.8F;
						}
					}

					if (exception1[0].equals("ofMipmapType")
							&& exception1.length >= 2) {
						ofMipmapType = Integer.valueOf(exception1[1])
								.intValue();
						ofMipmapType = Config.limit(ofMipmapType, 0, 3);
					}

					if (exception1[0].equals("ofLoadFar")
							&& exception1.length >= 2) {
						ofLoadFar = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofPreloadedChunks")
							&& exception1.length >= 2) {
						ofPreloadedChunks = Integer.valueOf(exception1[1])
								.intValue();

						if (ofPreloadedChunks < 0) {
							ofPreloadedChunks = 0;
						}

						if (ofPreloadedChunks > 8) {
							ofPreloadedChunks = 8;
						}
					}

					if (exception1[0].equals("ofOcclusionFancy")
							&& exception1.length >= 2) {
						ofOcclusionFancy = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofSmoothFps")
							&& exception1.length >= 2) {
						ofSmoothFps = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofSmoothWorld")
							&& exception1.length >= 2) {
						ofSmoothWorld = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAoLevel")
							&& exception1.length >= 2) {
						ofAoLevel = Float.valueOf(exception1[1]).floatValue();
						ofAoLevel = Config.limit(ofAoLevel, 0.0F, 1.0F);
					}

					if (exception1[0].equals("ofClouds")
							&& exception1.length >= 2) {
						ofClouds = Integer.valueOf(exception1[1]).intValue();
						ofClouds = Config.limit(ofClouds, 0, 3);
					}

					if (exception1[0].equals("ofCloudsHeight")
							&& exception1.length >= 2) {
						ofCloudsHeight = Float.valueOf(exception1[1])
								.floatValue();
						ofCloudsHeight = Config.limit(ofCloudsHeight, 0.0F,
								1.0F);
					}

					if (exception1[0].equals("ofTrees")
							&& exception1.length >= 2) {
						ofTrees = Integer.valueOf(exception1[1]).intValue();
						ofTrees = Config.limit(ofTrees, 0, 2);
					}

					if (exception1[0].equals("ofGrass")
							&& exception1.length >= 2) {
						ofGrass = Integer.valueOf(exception1[1]).intValue();
						ofGrass = Config.limit(ofGrass, 0, 2);
					}

					if (exception1[0].equals("ofDroppedItems")
							&& exception1.length >= 2) {
						ofDroppedItems = Integer.valueOf(exception1[1])
								.intValue();
						ofDroppedItems = Config.limit(ofDroppedItems, 0, 2);
					}

					if (exception1[0].equals("ofRain")
							&& exception1.length >= 2) {
						ofRain = Integer.valueOf(exception1[1]).intValue();
						ofRain = Config.limit(ofRain, 0, 3);
					}

					if (exception1[0].equals("ofWater")
							&& exception1.length >= 2) {
						ofWater = Integer.valueOf(exception1[1]).intValue();
						ofWater = Config.limit(ofWater, 0, 3);
					}

					if (exception1[0].equals("ofAnimatedWater")
							&& exception1.length >= 2) {
						ofAnimatedWater = Integer.valueOf(exception1[1])
								.intValue();
						ofAnimatedWater = Config.limit(ofAnimatedWater, 0, 2);
					}

					if (exception1[0].equals("ofAnimatedLava")
							&& exception1.length >= 2) {
						ofAnimatedLava = Integer.valueOf(exception1[1])
								.intValue();
						ofAnimatedLava = Config.limit(ofAnimatedLava, 0, 2);
					}

					if (exception1[0].equals("ofAnimatedFire")
							&& exception1.length >= 2) {
						ofAnimatedFire = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedPortal")
							&& exception1.length >= 2) {
						ofAnimatedPortal = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedRedstone")
							&& exception1.length >= 2) {
						ofAnimatedRedstone = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedExplosion")
							&& exception1.length >= 2) {
						ofAnimatedExplosion = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedFlame")
							&& exception1.length >= 2) {
						ofAnimatedFlame = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedSmoke")
							&& exception1.length >= 2) {
						ofAnimatedSmoke = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofVoidParticles")
							&& exception1.length >= 2) {
						ofVoidParticles = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofWaterParticles")
							&& exception1.length >= 2) {
						ofWaterParticles = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofPortalParticles")
							&& exception1.length >= 2) {
						ofPortalParticles = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofPotionParticles")
							&& exception1.length >= 2) {
						ofPotionParticles = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofDrippingWaterLava")
							&& exception1.length >= 2) {
						ofDrippingWaterLava = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedTerrain")
							&& exception1.length >= 2) {
						ofAnimatedTerrain = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedTextures")
							&& exception1.length >= 2) {
						ofAnimatedTextures = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAnimatedItems")
							&& exception1.length >= 2) {
						ofAnimatedItems = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofRainSplash")
							&& exception1.length >= 2) {
						ofRainSplash = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofLagometer")
							&& exception1.length >= 2) {
						ofLagometer = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAutoSaveTicks")
							&& exception1.length >= 2) {
						ofAutoSaveTicks = Integer.valueOf(exception1[1])
								.intValue();
						ofAutoSaveTicks = Config.limit(ofAutoSaveTicks, 40,
								40000);
					}

					if (exception1[0].equals("ofBetterGrass")
							&& exception1.length >= 2) {
						ofBetterGrass = Integer.valueOf(exception1[1])
								.intValue();
						ofBetterGrass = Config.limit(ofBetterGrass, 1, 3);
					}

					if (exception1[0].equals("ofConnectedTextures")
							&& exception1.length >= 2) {
						ofConnectedTextures = Integer.valueOf(exception1[1])
								.intValue();
						ofConnectedTextures = Config.limit(ofConnectedTextures,
								1, 3);
					}

					if (exception1[0].equals("ofWeather")
							&& exception1.length >= 2) {
						ofWeather = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofSky") && exception1.length >= 2) {
						ofSky = Boolean.valueOf(exception1[1]).booleanValue();
					}

					if (exception1[0].equals("ofStars")
							&& exception1.length >= 2) {
						ofStars = Boolean.valueOf(exception1[1]).booleanValue();
					}

					if (exception1[0].equals("ofSunMoon")
							&& exception1.length >= 2) {
						ofSunMoon = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofChunkUpdates")
							&& exception1.length >= 2) {
						ofChunkUpdates = Integer.valueOf(exception1[1])
								.intValue();
						ofChunkUpdates = Config.limit(ofChunkUpdates, 1, 5);
					}

					if (exception1[0].equals("ofChunkLoading")
							&& exception1.length >= 2) {
						ofChunkLoading = Integer.valueOf(exception1[1])
								.intValue();
						ofChunkLoading = Config.limit(ofChunkLoading, 0, 2);
						updateChunkLoading();
					}

					if (exception1[0].equals("ofChunkUpdatesDynamic")
							&& exception1.length >= 2) {
						ofChunkUpdatesDynamic = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofTime")
							&& exception1.length >= 2) {
						ofTime = Integer.valueOf(exception1[1]).intValue();
						ofTime = Config.limit(ofTime, 0, 3);
					}

					if (exception1[0].equals("ofClearWater")
							&& exception1.length >= 2) {
						ofClearWater = Boolean.valueOf(exception1[1])
								.booleanValue();
						updateWaterOpacity();
					}

					if (exception1[0].equals("ofDepthFog")
							&& exception1.length >= 2) {
						ofDepthFog = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofAaLevel")
							&& exception1.length >= 2) {
						ofAaLevel = Integer.valueOf(exception1[1]).intValue();
						ofAaLevel = Config.limit(ofAaLevel, 0, 16);
					}

					if (exception1[0].equals("ofProfiler")
							&& exception1.length >= 2) {
						ofProfiler = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofBetterSnow")
							&& exception1.length >= 2) {
						ofBetterSnow = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofSwampColors")
							&& exception1.length >= 2) {
						ofSwampColors = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofRandomMobs")
							&& exception1.length >= 2) {
						ofRandomMobs = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofSmoothBiomes")
							&& exception1.length >= 2) {
						ofSmoothBiomes = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofCustomFonts")
							&& exception1.length >= 2) {
						ofCustomFonts = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofCustomColors")
							&& exception1.length >= 2) {
						ofCustomColors = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofCustomSky")
							&& exception1.length >= 2) {
						ofCustomSky = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofShowCapes")
							&& exception1.length >= 2) {
						ofShowCapes = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofNaturalTextures")
							&& exception1.length >= 2) {
						ofNaturalTextures = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofLazyChunkLoading")
							&& exception1.length >= 2) {
						ofLazyChunkLoading = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofFullscreenMode")
							&& exception1.length >= 2) {
						ofFullscreenMode = exception1[1];
					}

					if (exception1[0].equals("ofFastMath")
							&& exception1.length >= 2) {
						ofFastMath = Boolean.valueOf(exception1[1])
								.booleanValue();
						MathHelper.fastMath = ofFastMath;
					}

					if (exception1[0].equals("ofFastRender")
							&& exception1.length >= 2) {
						ofFastRender = Boolean.valueOf(exception1[1])
								.booleanValue();
					}

					if (exception1[0].equals("ofTranslucentBlocks")
							&& exception1.length >= 2) {
						ofTranslucentBlocks = Integer.valueOf(exception1[1])
								.intValue();
						ofTranslucentBlocks = Config.limit(ofTranslucentBlocks,
								1, 2);
					}
				} catch (final Exception var5) {
					Config.dbg("Skipping bad option: " + s);
					var5.printStackTrace();
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			bufferedreader.close();
		} catch (final Exception var6) {
			Config.warn("Failed to load options");
			var6.printStackTrace();
		}
	}

	/**
	 * Loads the options from the options file. It appears that this has
	 * replaced the previous 'loadOptions'
	 */
	public void loadOptions() {
		try {
			if (!optionsFile.exists())
				return;

			final BufferedReader var9 = new BufferedReader(new FileReader(
					optionsFile));
			String var2 = "";
			mapSoundLevels.clear();

			while ((var2 = var9.readLine()) != null) {
				try {
					final String[] var8 = var2.split(":");

					if (var8[0].equals("mouseSensitivity")) {
						mouseSensitivity = parseFloat(var8[1]);
					}

					if (var8[0].equals("invertYMouse")) {
						invertMouse = var8[1].equals("true");
					}

					if (var8[0].equals("fov")) {
						fovSetting = parseFloat(var8[1]);
					}

					if (var8[0].equals("gamma")) {
						gammaSetting = parseFloat(var8[1]);
					}

					if (var8[0].equals("saturation")) {
						saturation = parseFloat(var8[1]);
					}

					if (var8[0].equals("fov")) {
						fovSetting = parseFloat(var8[1]) * 40.0F + 70.0F;
					}

					if (var8[0].equals("renderDistance")) {
						renderDistanceChunks = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("guiScale")) {
						guiScale = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("particles")) {
						particleSetting = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("bobView")) {
						viewBobbing = var8[1].equals("true");
					}

					if (var8[0].equals("anaglyph3d")) {
						anaglyph = var8[1].equals("true");
					}

					if (var8[0].equals("advancedOpengl")) {
						advancedOpengl = var8[1].equals("true");
					}

					if (var8[0].equals("maxFps")) {
						limitFramerate = Integer.parseInt(var8[1]);
						enableVsync = false;

						if (limitFramerate <= 0) {
							limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT
									.getValueMax();
							enableVsync = true;
						}

						updateVSync();
					}

					if (var8[0].equals("fboEnable")) {
						fboEnable = var8[1].equals("true");
					}

					if (var8[0].equals("difficulty")) {
						difficulty = EnumDifficulty.getDifficultyEnum(Integer
								.parseInt(var8[1]));
					}

					if (var8[0].equals("fancyGraphics")) {
						fancyGraphics = var8[1].equals("true");
					}

					if (var8[0].equals("ao")) {
						if (var8[1].equals("true")) {
							ambientOcclusion = 2;
						} else if (var8[1].equals("false")) {
							ambientOcclusion = 0;
						} else {
							ambientOcclusion = Integer.parseInt(var8[1]);
						}
					}

					if (var8[0].equals("clouds")) {
						clouds = var8[1].equals("true");
					}

					if (var8[0].equals("resourcePacks")) {
						resourcePacks = (List) gson.fromJson(
								var2.substring(var2.indexOf(58) + 1),
								typeListString);

						if (resourcePacks == null) {
							resourcePacks = new ArrayList();
						}
					}

					if (var8[0].equals("lastServer") && var8.length >= 2) {
						lastServer = var2.substring(var2.indexOf(58) + 1);
					}

					if (var8[0].equals("lang") && var8.length >= 2) {
						language = var8[1];
					}

					if (var8[0].equals("chatVisibility")) {
						chatVisibility = EntityPlayer.EnumChatVisibility
								.getEnumChatVisibility(Integer
										.parseInt(var8[1]));
					}

					if (var8[0].equals("chatColors")) {
						chatColours = var8[1].equals("true");
					}

					if (var8[0].equals("chatLinks")) {
						chatLinks = var8[1].equals("true");
					}

					if (var8[0].equals("chatLinksPrompt")) {
						chatLinksPrompt = var8[1].equals("true");
					}

					if (var8[0].equals("chatOpacity")) {
						chatOpacity = parseFloat(var8[1]);
					}

					if (var8[0].equals("snooperEnabled")) {
						snooperEnabled = var8[1].equals("true");
					}

					if (var8[0].equals("fullscreen")) {
						fullScreen = var8[1].equals("true");
					}

					if (var8[0].equals("enableVsync")) {
						enableVsync = var8[1].equals("true");
						updateVSync();
					}

					if (var8[0].equals("hideServerAddress")) {
						hideServerAddress = var8[1].equals("true");
					}

					if (var8[0].equals("advancedItemTooltips")) {
						advancedItemTooltips = var8[1].equals("true");
					}

					if (var8[0].equals("pauseOnLostFocus")) {
						pauseOnLostFocus = var8[1].equals("true");
					}

					if (var8[0].equals("showCape")) {
						showCape = var8[1].equals("true");
					}

					if (var8[0].equals("touchscreen")) {
						touchscreen = var8[1].equals("true");
					}

					if (var8[0].equals("overrideHeight")) {
						overrideHeight = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("overrideWidth")) {
						overrideWidth = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("heldItemTooltips")) {
						heldItemTooltips = var8[1].equals("true");
					}

					if (var8[0].equals("chatHeightFocused")) {
						chatHeightFocused = parseFloat(var8[1]);
					}

					if (var8[0].equals("chatHeightUnfocused")) {
						chatHeightUnfocused = parseFloat(var8[1]);
					}

					if (var8[0].equals("chatScale")) {
						chatScale = parseFloat(var8[1]);
					}

					if (var8[0].equals("chatWidth")) {
						chatWidth = parseFloat(var8[1]);
					}

					if (var8[0].equals("showInventoryAchievementHint")) {
						showInventoryAchievementHint = var8[1].equals("true");
					}

					if (var8[0].equals("mipmapLevels")) {
						mipmapLevels = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("anisotropicFiltering")) {
						anisotropicFiltering = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("streamBytesPerPixel")) {
						field_152400_J = parseFloat(var8[1]);
					}

					if (var8[0].equals("streamMicVolume")) {
						field_152401_K = parseFloat(var8[1]);
					}

					if (var8[0].equals("streamSystemVolume")) {
						field_152402_L = parseFloat(var8[1]);
					}

					if (var8[0].equals("streamKbps")) {
						field_152403_M = parseFloat(var8[1]);
					}

					if (var8[0].equals("streamFps")) {
						field_152404_N = parseFloat(var8[1]);
					}

					if (var8[0].equals("streamCompression")) {
						field_152405_O = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("streamSendMetadata")) {
						field_152406_P = var8[1].equals("true");
					}

					if (var8[0].equals("streamPreferredServer")
							&& var8.length >= 2) {
						field_152407_Q = var2.substring(var2.indexOf(58) + 1);
					}

					if (var8[0].equals("streamChatEnabled")) {
						field_152408_R = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("streamChatUserFilter")) {
						field_152409_S = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("streamMicToggleBehavior")) {
						field_152410_T = Integer.parseInt(var8[1]);
					}

					if (var8[0].equals("forceUnicodeFont")) {
						forceUnicodeFont = var8[1].equals("true");
					}

					final KeyBinding[] var4 = keyBindings;
					int var5 = var4.length;
					int var6;

					for (var6 = 0; var6 < var5; ++var6) {
						final KeyBinding var10 = var4[var6];

						if (var8[0].equals("key_" + var10.getKeyDescription())) {
							var10.setKeyCode(Integer.parseInt(var8[1]));
						}
					}

					final SoundCategory[] var111 = SoundCategory.values();
					var5 = var111.length;

					for (var6 = 0; var6 < var5; ++var6) {
						final SoundCategory var11 = var111[var6];

						if (var8[0].equals("soundCategory_"
								+ var11.getCategoryName())) {
							mapSoundLevels.put(var11,
									Float.valueOf(parseFloat(var8[1])));
						}
					}
				} catch (final Exception var91) {
					logger.warn("Skipping bad option: " + var2);
					var91.printStackTrace();
				}
			}

			KeyBinding.resetKeyBindingArrayAndHash();
			var9.close();
		} catch (final Exception var101) {
			logger.error("Failed to load options", var101);
		}

		loadOfOptions();
	}

	/**
	 * Parses a string into a float.
	 */
	private float parseFloat(String par1Str) {
		return par1Str.equals("true") ? 1.0F : par1Str.equals("false") ? 0.0F
				: Float.parseFloat(par1Str);
	}

	public void resetSettings() {
		renderDistanceChunks = 8;
		viewBobbing = true;
		anaglyph = false;
		advancedOpengl = false;
		limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT
				.getValueMax();
		enableVsync = false;
		updateVSync();
		mipmapLevels = 4;
		anisotropicFiltering = 1;
		fancyGraphics = true;
		ambientOcclusion = 2;
		clouds = true;
		fovSetting = 0.0F;
		gammaSetting = 0.0F;
		guiScale = 0;
		particleSetting = 0;
		heldItemTooltips = true;
		ofFogType = 1;
		ofFogStart = 0.8F;
		ofMipmapType = 0;
		ofLoadFar = false;
		ofPreloadedChunks = 0;
		ofOcclusionFancy = false;
		ofSmoothFps = false;
		Config.updateAvailableProcessors();
		ofSmoothWorld = Config.isSingleProcessor();
		ofLazyChunkLoading = Config.isSingleProcessor();
		ofFastMath = false;
		ofFastRender = true;
		ofTranslucentBlocks = 2;
		ofAoLevel = 1.0F;
		ofAaLevel = 0;
		ofClouds = 0;
		ofCloudsHeight = 0.0F;
		ofTrees = 0;
		ofGrass = 0;
		ofRain = 0;
		ofWater = 0;
		ofBetterGrass = 3;
		ofAutoSaveTicks = 4000;
		ofLagometer = false;
		ofProfiler = false;
		ofWeather = true;
		ofSky = true;
		ofStars = true;
		ofSunMoon = true;
		ofChunkUpdates = 1;
		ofChunkLoading = 0;
		ofChunkUpdatesDynamic = false;
		ofTime = 0;
		ofClearWater = false;
		ofDepthFog = true;
		ofBetterSnow = false;
		ofFullscreenMode = "Default";
		ofSwampColors = true;
		ofRandomMobs = true;
		ofSmoothBiomes = true;
		ofCustomFonts = true;
		ofCustomColors = true;
		ofCustomSky = true;
		ofShowCapes = true;
		ofConnectedTextures = 2;
		ofNaturalTextures = false;
		ofAnimatedWater = 0;
		ofAnimatedLava = 0;
		ofAnimatedFire = true;
		ofAnimatedPortal = true;
		ofAnimatedRedstone = true;
		ofAnimatedExplosion = true;
		ofAnimatedFlame = true;
		ofAnimatedSmoke = true;
		ofVoidParticles = true;
		ofWaterParticles = true;
		ofRainSplash = true;
		ofPortalParticles = true;
		ofPotionParticles = true;
		ofDrippingWaterLava = true;
		ofAnimatedTerrain = true;
		ofAnimatedItems = true;
		ofAnimatedTextures = true;
		mc.renderGlobal.updateCapes();
		updateWaterOpacity();
		mc.renderGlobal.setAllRenderersVisible();
		mc.refreshResources();
		saveOptions();
	}

	public void saveOfOptions() {
		try {
			final PrintWriter exception = new PrintWriter(new FileWriter(
					optionsFileOF));
			exception.println("ofRenderDistanceChunks:" + renderDistanceChunks);
			exception.println("ofFogType:" + ofFogType);
			exception.println("ofFogStart:" + ofFogStart);
			exception.println("ofMipmapType:" + ofMipmapType);
			exception.println("ofLoadFar:" + ofLoadFar);
			exception.println("ofPreloadedChunks:" + ofPreloadedChunks);
			exception.println("ofOcclusionFancy:" + ofOcclusionFancy);
			exception.println("ofSmoothFps:" + ofSmoothFps);
			exception.println("ofSmoothWorld:" + ofSmoothWorld);
			exception.println("ofAoLevel:" + ofAoLevel);
			exception.println("ofClouds:" + ofClouds);
			exception.println("ofCloudsHeight:" + ofCloudsHeight);
			exception.println("ofTrees:" + ofTrees);
			exception.println("ofGrass:" + ofGrass);
			exception.println("ofDroppedItems:" + ofDroppedItems);
			exception.println("ofRain:" + ofRain);
			exception.println("ofWater:" + ofWater);
			exception.println("ofAnimatedWater:" + ofAnimatedWater);
			exception.println("ofAnimatedLava:" + ofAnimatedLava);
			exception.println("ofAnimatedFire:" + ofAnimatedFire);
			exception.println("ofAnimatedPortal:" + ofAnimatedPortal);
			exception.println("ofAnimatedRedstone:" + ofAnimatedRedstone);
			exception.println("ofAnimatedExplosion:" + ofAnimatedExplosion);
			exception.println("ofAnimatedFlame:" + ofAnimatedFlame);
			exception.println("ofAnimatedSmoke:" + ofAnimatedSmoke);
			exception.println("ofVoidParticles:" + ofVoidParticles);
			exception.println("ofWaterParticles:" + ofWaterParticles);
			exception.println("ofPortalParticles:" + ofPortalParticles);
			exception.println("ofPotionParticles:" + ofPotionParticles);
			exception.println("ofDrippingWaterLava:" + ofDrippingWaterLava);
			exception.println("ofAnimatedTerrain:" + ofAnimatedTerrain);
			exception.println("ofAnimatedTextures:" + ofAnimatedTextures);
			exception.println("ofAnimatedItems:" + ofAnimatedItems);
			exception.println("ofRainSplash:" + ofRainSplash);
			exception.println("ofLagometer:" + ofLagometer);
			exception.println("ofAutoSaveTicks:" + ofAutoSaveTicks);
			exception.println("ofBetterGrass:" + ofBetterGrass);
			exception.println("ofConnectedTextures:" + ofConnectedTextures);
			exception.println("ofWeather:" + ofWeather);
			exception.println("ofSky:" + ofSky);
			exception.println("ofStars:" + ofStars);
			exception.println("ofSunMoon:" + ofSunMoon);
			exception.println("ofChunkUpdates:" + ofChunkUpdates);
			exception.println("ofChunkLoading:" + ofChunkLoading);
			exception.println("ofChunkUpdatesDynamic:" + ofChunkUpdatesDynamic);
			exception.println("ofTime:" + ofTime);
			exception.println("ofClearWater:" + ofClearWater);
			exception.println("ofDepthFog:" + ofDepthFog);
			exception.println("ofAaLevel:" + ofAaLevel);
			exception.println("ofProfiler:" + ofProfiler);
			exception.println("ofBetterSnow:" + ofBetterSnow);
			exception.println("ofSwampColors:" + ofSwampColors);
			exception.println("ofRandomMobs:" + ofRandomMobs);
			exception.println("ofSmoothBiomes:" + ofSmoothBiomes);
			exception.println("ofCustomFonts:" + ofCustomFonts);
			exception.println("ofCustomColors:" + ofCustomColors);
			exception.println("ofCustomSky:" + ofCustomSky);
			exception.println("ofShowCapes:" + ofShowCapes);
			exception.println("ofNaturalTextures:" + ofNaturalTextures);
			exception.println("ofLazyChunkLoading:" + ofLazyChunkLoading);
			exception.println("ofFullscreenMode:" + ofFullscreenMode);
			exception.println("ofFastMath:" + ofFastMath);
			exception.println("ofFastRender:" + ofFastRender);
			exception.println("ofTranslucentBlocks:" + ofTranslucentBlocks);
			exception.close();
		} catch (final Exception var2) {
			Config.warn("Failed to save options");
			var2.printStackTrace();
		}
	}

	/**
	 * Saves the options to the options file.
	 */
	public void saveOptions() {
		if (Reflector.FMLClientHandler.exists()) {
			final Object var6 = Reflector.call(
					Reflector.FMLClientHandler_instance, new Object[0]);

			if (var6 != null
					&& Reflector
							.callBoolean(var6,
									Reflector.FMLClientHandler_isLoading,
									new Object[0]))
				return;
		}

		try {
			final PrintWriter var81 = new PrintWriter(new FileWriter(
					optionsFile));
			var81.println("invertYMouse:" + invertMouse);
			var81.println("mouseSensitivity:" + mouseSensitivity);
			var81.println("fov:" + (fovSetting - 70.0F) / 40.0F);
			var81.println("gamma:" + gammaSetting);
			var81.println("saturation:" + saturation);
			var81.println("renderDistance:"
					+ Config.limit(renderDistanceChunks, 2, 16));
			var81.println("guiScale:" + guiScale);
			var81.println("particles:" + particleSetting);
			var81.println("bobView:" + viewBobbing);
			var81.println("anaglyph3d:" + anaglyph);
			var81.println("advancedOpengl:" + advancedOpengl);
			var81.println("maxFps:" + limitFramerate);
			var81.println("fboEnable:" + fboEnable);
			var81.println("difficulty:" + difficulty.getDifficultyId());
			var81.println("fancyGraphics:" + fancyGraphics);
			var81.println("ao:" + ambientOcclusion);
			var81.println("clouds:" + clouds);
			var81.println("resourcePacks:" + gson.toJson(resourcePacks));
			var81.println("lastServer:" + lastServer);
			var81.println("lang:" + language);
			var81.println("chatVisibility:"
					+ chatVisibility.getChatVisibility());
			var81.println("chatColors:" + chatColours);
			var81.println("chatLinks:" + chatLinks);
			var81.println("chatLinksPrompt:" + chatLinksPrompt);
			var81.println("chatOpacity:" + chatOpacity);
			var81.println("snooperEnabled:" + snooperEnabled);
			var81.println("fullscreen:" + fullScreen);
			var81.println("enableVsync:" + enableVsync);
			var81.println("hideServerAddress:" + hideServerAddress);
			var81.println("advancedItemTooltips:" + advancedItemTooltips);
			var81.println("pauseOnLostFocus:" + pauseOnLostFocus);
			var81.println("showCape:" + showCape);
			var81.println("touchscreen:" + touchscreen);
			var81.println("overrideWidth:" + overrideWidth);
			var81.println("overrideHeight:" + overrideHeight);
			var81.println("heldItemTooltips:" + heldItemTooltips);
			var81.println("chatHeightFocused:" + chatHeightFocused);
			var81.println("chatHeightUnfocused:" + chatHeightUnfocused);
			var81.println("chatScale:" + chatScale);
			var81.println("chatWidth:" + chatWidth);
			var81.println("showInventoryAchievementHint:"
					+ showInventoryAchievementHint);
			var81.println("mipmapLevels:" + mipmapLevels);
			var81.println("anisotropicFiltering:" + anisotropicFiltering);
			var81.println("streamBytesPerPixel:" + field_152400_J);
			var81.println("streamMicVolume:" + field_152401_K);
			var81.println("streamSystemVolume:" + field_152402_L);
			var81.println("streamKbps:" + field_152403_M);
			var81.println("streamFps:" + field_152404_N);
			var81.println("streamCompression:" + field_152405_O);
			var81.println("streamSendMetadata:" + field_152406_P);
			var81.println("streamPreferredServer:" + field_152407_Q);
			var81.println("streamChatEnabled:" + field_152408_R);
			var81.println("streamChatUserFilter:" + field_152409_S);
			var81.println("streamMicToggleBehavior:" + field_152410_T);
			var81.println("forceUnicodeFont:" + forceUnicodeFont);
			final KeyBinding[] var2 = keyBindings;
			int var3 = var2.length;
			int var4;

			for (var4 = 0; var4 < var3; ++var4) {
				final KeyBinding var7 = var2[var4];
				var81.println("key_" + var7.getKeyDescription() + ":"
						+ var7.getKeyCode());
			}

			final SoundCategory[] var9 = SoundCategory.values();
			var3 = var9.length;

			for (var4 = 0; var4 < var3; ++var4) {
				final SoundCategory var8 = var9[var4];
				var81.println("soundCategory_" + var8.getCategoryName() + ":"
						+ getSoundLevel(var8));
			}

			var81.close();
		} catch (final Exception var71) {
			logger.error("Failed to save options", var71);
		}

		saveOfOptions();
		sendSettingsToServer();
	}

	/**
	 * Send a client info packet with settings information to the server
	 */
	public void sendSettingsToServer() {
		if (mc.thePlayer != null) {
			mc.thePlayer.sendQueue.addToSendQueue(new C15PacketClientSettings(
					language, renderDistanceChunks, chatVisibility,
					chatColours, difficulty, showCape));
		}
	}

	public void setAllAnimations(boolean flag) {
		final int animVal = flag ? 0 : 2;
		ofAnimatedWater = animVal;
		ofAnimatedLava = animVal;
		ofAnimatedFire = flag;
		ofAnimatedPortal = flag;
		ofAnimatedRedstone = flag;
		ofAnimatedExplosion = flag;
		ofAnimatedFlame = flag;
		ofAnimatedSmoke = flag;
		ofVoidParticles = flag;
		ofWaterParticles = flag;
		ofRainSplash = flag;
		ofPortalParticles = flag;
		ofPotionParticles = flag;
		particleSetting = flag ? 0 : 2;
		ofDrippingWaterLava = flag;
		ofAnimatedTerrain = flag;
		ofAnimatedItems = flag;
		ofAnimatedTextures = flag;
	}

	public void setKeyCodeSave(KeyBinding p_151440_1_, int p_151440_2_) {
		p_151440_1_.setKeyCode(p_151440_2_);
		saveOptions();
	}

	/**
	 * If the specified option is controlled by a slider (float value), this
	 * will set the float value.
	 */
	public void setOptionFloatValue(GameSettings.Options par1EnumOptions,
			float par2) {
		if (par1EnumOptions == GameSettings.Options.CLOUD_HEIGHT) {
			ofCloudsHeight = par2;
		}

		if (par1EnumOptions == GameSettings.Options.AO_LEVEL) {
			ofAoLevel = par2;
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.SENSITIVITY) {
			mouseSensitivity = par2;
		}

		if (par1EnumOptions == GameSettings.Options.FOV) {
			fovSetting = par2;
		}

		if (par1EnumOptions == GameSettings.Options.GAMMA) {
			gammaSetting = par2;
		}

		if (par1EnumOptions == GameSettings.Options.FRAMERATE_LIMIT) {
			limitFramerate = (int) par2;
			enableVsync = false;

			if (limitFramerate <= 0) {
				limitFramerate = (int) GameSettings.Options.FRAMERATE_LIMIT
						.getValueMax();
				enableVsync = true;
			}

			updateVSync();
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_OPACITY) {
			chatOpacity = par2;
			mc.ingameGUI.getChatGUI().func_146245_b();
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
			chatHeightFocused = par2;
			mc.ingameGUI.getChatGUI().func_146245_b();
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
			chatHeightUnfocused = par2;
			mc.ingameGUI.getChatGUI().func_146245_b();
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_WIDTH) {
			chatWidth = par2;
			mc.ingameGUI.getChatGUI().func_146245_b();
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_SCALE) {
			chatScale = par2;
			mc.ingameGUI.getChatGUI().func_146245_b();
		}

		int var3;

		if (par1EnumOptions == GameSettings.Options.ANISOTROPIC_FILTERING) {
			var3 = anisotropicFiltering;
			anisotropicFiltering = (int) par2;

			if (var3 != par2) {
				mc.getTextureMapBlocks().func_147632_b(anisotropicFiltering);
				mc.scheduleResourcesRefresh();
			}
		}

		if (par1EnumOptions == GameSettings.Options.MIPMAP_LEVELS) {
			var3 = mipmapLevels;
			mipmapLevels = (int) par2;

			if (var3 != par2) {
				mc.getTextureMapBlocks().func_147633_a(mipmapLevels);
				mc.scheduleResourcesRefresh();
			}
		}

		if (par1EnumOptions == GameSettings.Options.RENDER_DISTANCE) {
			renderDistanceChunks = (int) par2;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_BYTES_PER_PIXEL) {
			field_152400_J = par2;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_VOLUME_MIC) {
			field_152401_K = par2;
			mc.func_152346_Z().func_152915_s();
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_VOLUME_SYSTEM) {
			field_152402_L = par2;
			mc.func_152346_Z().func_152915_s();
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_KBPS) {
			field_152403_M = par2;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_FPS) {
			field_152404_N = par2;
		}
	}

	/**
	 * For non-float options. Toggles the option on/off, or cycles through the
	 * list i.e. render distances.
	 */
	public void setOptionValue(GameSettings.Options par1EnumOptions, int par2) {
		if (par1EnumOptions == GameSettings.Options.FOG_FANCY) {
			switch (ofFogType) {
			case 1:
				ofFogType = 2;

				if (!Config.isFancyFogAvailable()) {
					ofFogType = 3;
				}

				break;

			case 2:
				ofFogType = 3;
				break;

			case 3:
				ofFogType = 1;
				break;

			default:
				ofFogType = 1;
			}
		}

		if (par1EnumOptions == GameSettings.Options.FOG_START) {
			ofFogStart += 0.2F;

			if (ofFogStart > 0.81F) {
				ofFogStart = 0.2F;
			}
		}

		if (par1EnumOptions == GameSettings.Options.MIPMAP_TYPE) {
			++ofMipmapType;

			if (ofMipmapType > 3) {
				ofMipmapType = 0;
			}

			TextureUtils.refreshBlockTextures();
		}

		if (par1EnumOptions == GameSettings.Options.LOAD_FAR) {
			ofLoadFar = !ofLoadFar;
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.PRELOADED_CHUNKS) {
			ofPreloadedChunks += 2;

			if (ofPreloadedChunks > 8) {
				ofPreloadedChunks = 0;
			}

			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.SMOOTH_FPS) {
			ofSmoothFps = !ofSmoothFps;
		}

		if (par1EnumOptions == GameSettings.Options.SMOOTH_WORLD) {
			ofSmoothWorld = !ofSmoothWorld;
			Config.updateAvailableProcessors();

			if (!Config.isSingleProcessor()) {
				ofSmoothWorld = false;
			}

			Config.updateThreadPriorities();
		}

		if (par1EnumOptions == GameSettings.Options.CLOUDS) {
			++ofClouds;

			if (ofClouds > 3) {
				ofClouds = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.TREES) {
			++ofTrees;

			if (ofTrees > 2) {
				ofTrees = 0;
			}

			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.GRASS) {
			++ofGrass;

			if (ofGrass > 2) {
				ofGrass = 0;
			}

			RenderBlocks.fancyGrass = Config.isGrassFancy();
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.DROPPED_ITEMS) {
			++ofDroppedItems;

			if (ofDroppedItems > 2) {
				ofDroppedItems = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.RAIN) {
			++ofRain;

			if (ofRain > 3) {
				ofRain = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.WATER) {
			++ofWater;

			if (ofWater > 2) {
				ofWater = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_WATER) {
			++ofAnimatedWater;

			if (ofAnimatedWater > 2) {
				ofAnimatedWater = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_LAVA) {
			++ofAnimatedLava;

			if (ofAnimatedLava > 2) {
				ofAnimatedLava = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_FIRE) {
			ofAnimatedFire = !ofAnimatedFire;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_PORTAL) {
			ofAnimatedPortal = !ofAnimatedPortal;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_REDSTONE) {
			ofAnimatedRedstone = !ofAnimatedRedstone;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_EXPLOSION) {
			ofAnimatedExplosion = !ofAnimatedExplosion;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_FLAME) {
			ofAnimatedFlame = !ofAnimatedFlame;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_SMOKE) {
			ofAnimatedSmoke = !ofAnimatedSmoke;
		}

		if (par1EnumOptions == GameSettings.Options.VOID_PARTICLES) {
			ofVoidParticles = !ofVoidParticles;
		}

		if (par1EnumOptions == GameSettings.Options.WATER_PARTICLES) {
			ofWaterParticles = !ofWaterParticles;
		}

		if (par1EnumOptions == GameSettings.Options.PORTAL_PARTICLES) {
			ofPortalParticles = !ofPortalParticles;
		}

		if (par1EnumOptions == GameSettings.Options.POTION_PARTICLES) {
			ofPotionParticles = !ofPotionParticles;
		}

		if (par1EnumOptions == GameSettings.Options.DRIPPING_WATER_LAVA) {
			ofDrippingWaterLava = !ofDrippingWaterLava;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_TERRAIN) {
			ofAnimatedTerrain = !ofAnimatedTerrain;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_TEXTURES) {
			ofAnimatedTextures = !ofAnimatedTextures;
		}

		if (par1EnumOptions == GameSettings.Options.ANIMATED_ITEMS) {
			ofAnimatedItems = !ofAnimatedItems;
		}

		if (par1EnumOptions == GameSettings.Options.RAIN_SPLASH) {
			ofRainSplash = !ofRainSplash;
		}

		if (par1EnumOptions == GameSettings.Options.LAGOMETER) {
			ofLagometer = !ofLagometer;
		}

		if (par1EnumOptions == GameSettings.Options.AUTOSAVE_TICKS) {
			ofAutoSaveTicks *= 10;

			if (ofAutoSaveTicks > 40000) {
				ofAutoSaveTicks = 40;
			}
		}

		if (par1EnumOptions == GameSettings.Options.BETTER_GRASS) {
			++ofBetterGrass;

			if (ofBetterGrass > 3) {
				ofBetterGrass = 1;
			}

			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.CONNECTED_TEXTURES) {
			++ofConnectedTextures;

			if (ofConnectedTextures > 3) {
				ofConnectedTextures = 1;
			}

			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.WEATHER) {
			ofWeather = !ofWeather;
		}

		if (par1EnumOptions == GameSettings.Options.SKY) {
			ofSky = !ofSky;
		}

		if (par1EnumOptions == GameSettings.Options.STARS) {
			ofStars = !ofStars;
		}

		if (par1EnumOptions == GameSettings.Options.SUN_MOON) {
			ofSunMoon = !ofSunMoon;
		}

		if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES) {
			++ofChunkUpdates;

			if (ofChunkUpdates > 5) {
				ofChunkUpdates = 1;
			}
		}

		if (par1EnumOptions == GameSettings.Options.CHUNK_LOADING) {
			++ofChunkLoading;

			if (ofChunkLoading > 2) {
				ofChunkLoading = 0;
			}

			updateChunkLoading();
		}

		if (par1EnumOptions == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
			ofChunkUpdatesDynamic = !ofChunkUpdatesDynamic;
		}

		if (par1EnumOptions == GameSettings.Options.TIME) {
			++ofTime;

			if (ofTime > 3) {
				ofTime = 0;
			}
		}

		if (par1EnumOptions == GameSettings.Options.CLEAR_WATER) {
			ofClearWater = !ofClearWater;
			updateWaterOpacity();
		}

		if (par1EnumOptions == GameSettings.Options.DEPTH_FOG) {
			ofDepthFog = !ofDepthFog;
		}

		if (par1EnumOptions == GameSettings.Options.AA_LEVEL) {
			ofAaLevel = 0;
		}

		if (par1EnumOptions == GameSettings.Options.PROFILER) {
			ofProfiler = !ofProfiler;
		}

		if (par1EnumOptions == GameSettings.Options.BETTER_SNOW) {
			ofBetterSnow = !ofBetterSnow;
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.SWAMP_COLORS) {
			ofSwampColors = !ofSwampColors;
			CustomColorizer.updateUseDefaultColorMultiplier();
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.RANDOM_MOBS) {
			ofRandomMobs = !ofRandomMobs;
			RandomMobs.resetTextures();
		}

		if (par1EnumOptions == GameSettings.Options.SMOOTH_BIOMES) {
			ofSmoothBiomes = !ofSmoothBiomes;
			CustomColorizer.updateUseDefaultColorMultiplier();
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.CUSTOM_FONTS) {
			ofCustomFonts = !ofCustomFonts;
			mc.fontRenderer
					.onResourceManagerReload(Config.getResourceManager());
			mc.standardGalacticFontRenderer.onResourceManagerReload(Config
					.getResourceManager());
		}

		if (par1EnumOptions == GameSettings.Options.CUSTOM_COLORS) {
			ofCustomColors = !ofCustomColors;
			CustomColorizer.update();
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.CUSTOM_SKY) {
			ofCustomSky = !ofCustomSky;
			CustomSky.update();
		}

		if (par1EnumOptions == GameSettings.Options.SHOW_CAPES) {
			ofShowCapes = !ofShowCapes;
			mc.renderGlobal.updateCapes();
		}

		if (par1EnumOptions == GameSettings.Options.NATURAL_TEXTURES) {
			ofNaturalTextures = !ofNaturalTextures;
			NaturalTextures.update();
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.FAST_MATH) {
			ofFastMath = !ofFastMath;
			MathHelper.fastMath = ofFastMath;
		}

		if (par1EnumOptions == GameSettings.Options.FAST_RENDER) {
			ofFastRender = !ofFastRender;
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.TRANSLUCENT_BLOCKS) {
			if (ofTranslucentBlocks == 1) {
				ofTranslucentBlocks = 2;
			} else {
				ofTranslucentBlocks = 1;
			}

			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.LAZY_CHUNK_LOADING) {
			ofLazyChunkLoading = !ofLazyChunkLoading;
			Config.updateAvailableProcessors();

			if (!Config.isSingleProcessor()) {
				ofLazyChunkLoading = false;
			}

			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.FULLSCREEN_MODE) {
			final List modeList = Arrays.asList(Config.getFullscreenModes());

			if (ofFullscreenMode.equals("Default")) {
				ofFullscreenMode = (String) modeList.get(0);
			} else {
				int index = modeList.indexOf(ofFullscreenMode);

				if (index < 0) {
					ofFullscreenMode = "Default";
				} else {
					++index;

					if (index >= modeList.size()) {
						ofFullscreenMode = "Default";
					} else {
						ofFullscreenMode = (String) modeList.get(index);
					}
				}
			}
		}

		if (par1EnumOptions == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
			heldItemTooltips = !heldItemTooltips;
		}

		if (par1EnumOptions == GameSettings.Options.INVERT_MOUSE) {
			invertMouse = !invertMouse;
		}

		if (par1EnumOptions == GameSettings.Options.GUI_SCALE) {
			guiScale = guiScale + par2 & 3;
		}

		if (par1EnumOptions == GameSettings.Options.PARTICLES) {
			particleSetting = (particleSetting + par2) % 3;
		}

		if (par1EnumOptions == GameSettings.Options.VIEW_BOBBING) {
			viewBobbing = !viewBobbing;
		}

		if (par1EnumOptions == GameSettings.Options.RENDER_CLOUDS) {
			clouds = !clouds;
		}

		if (par1EnumOptions == GameSettings.Options.FORCE_UNICODE_FONT) {
			forceUnicodeFont = !forceUnicodeFont;
			mc.fontRenderer.setUnicodeFlag(mc.getLanguageManager()
					.isCurrentLocaleUnicode() || forceUnicodeFont);
		}

		if (par1EnumOptions == GameSettings.Options.ADVANCED_OPENGL) {
			if (!Config.isOcclusionAvailable()) {
				ofOcclusionFancy = false;
				advancedOpengl = false;
			} else if (!advancedOpengl) {
				advancedOpengl = true;
				ofOcclusionFancy = false;
			} else if (!ofOcclusionFancy) {
				ofOcclusionFancy = true;
			} else {
				ofOcclusionFancy = false;
				advancedOpengl = false;
			}

			mc.renderGlobal.setAllRenderersVisible();
		}

		if (par1EnumOptions == GameSettings.Options.FBO_ENABLE) {
			fboEnable = !fboEnable;
		}

		if (par1EnumOptions == GameSettings.Options.ANAGLYPH) {
			anaglyph = !anaglyph;
			mc.refreshResources();
		}

		if (par1EnumOptions == GameSettings.Options.DIFFICULTY) {
			difficulty = EnumDifficulty.getDifficultyEnum(difficulty
					.getDifficultyId() + par2 & 3);
		}

		if (par1EnumOptions == GameSettings.Options.GRAPHICS) {
			fancyGraphics = !fancyGraphics;
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.AMBIENT_OCCLUSION) {
			ambientOcclusion = (ambientOcclusion + par2) % 3;
			mc.renderGlobal.loadRenderers();
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_VISIBILITY) {
			chatVisibility = EntityPlayer.EnumChatVisibility
					.getEnumChatVisibility((chatVisibility.getChatVisibility() + par2) % 3);
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_COMPRESSION) {
			field_152405_O = (field_152405_O + par2) % 3;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_SEND_METADATA) {
			field_152406_P = !field_152406_P;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_CHAT_ENABLED) {
			field_152408_R = (field_152408_R + par2) % 3;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_CHAT_USER_FILTER) {
			field_152409_S = (field_152409_S + par2) % 3;
		}

		if (par1EnumOptions == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
			field_152410_T = (field_152410_T + par2) % 2;
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_COLOR) {
			chatColours = !chatColours;
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_LINKS) {
			chatLinks = !chatLinks;
		}

		if (par1EnumOptions == GameSettings.Options.CHAT_LINKS_PROMPT) {
			chatLinksPrompt = !chatLinksPrompt;
		}

		if (par1EnumOptions == GameSettings.Options.SNOOPER_ENABLED) {
			snooperEnabled = !snooperEnabled;
		}

		if (par1EnumOptions == GameSettings.Options.SHOW_CAPE) {
			showCape = !showCape;
		}

		if (par1EnumOptions == GameSettings.Options.TOUCHSCREEN) {
			touchscreen = !touchscreen;
		}

		if (par1EnumOptions == GameSettings.Options.USE_FULLSCREEN) {
			fullScreen = !fullScreen;

			if (mc.isFullScreen() != fullScreen) {
				mc.toggleFullscreen();
			}
		}

		if (par1EnumOptions == GameSettings.Options.ENABLE_VSYNC) {
			enableVsync = !enableVsync;
			Display.setVSyncEnabled(enableVsync);
		}

		saveOptions();
	}

	public void setSoundLevel(SoundCategory p_151439_1_, float p_151439_2_) {
		mc.getSoundHandler().setSoundLevel(p_151439_1_, p_151439_2_);
		mapSoundLevels.put(p_151439_1_, Float.valueOf(p_151439_2_));
	}

	/**
	 * Should render clouds
	 */
	public boolean shouldRenderClouds() {
		return renderDistanceChunks >= 4 && clouds;
	}

	public void updateChunkLoading() {
		switch (ofChunkLoading) {
		case 1:
			WrUpdates.setWrUpdater(new WrUpdaterSmooth());
			break;

		case 2:
			WrUpdates.setWrUpdater(new WrUpdaterThreaded());
			break;

		default:
			WrUpdates.setWrUpdater((IWrUpdater) null);
		}

		if (mc.renderGlobal != null) {
			mc.renderGlobal.loadRenderers();
		}
	}

	public void updateVSync() {
		Display.setVSyncEnabled(enableVsync);
	}

	private void updateWaterOpacity() {
		if (mc.getIntegratedServer() != null) {
			Config.waterOpacityChanged = true;
		}

		byte opacity = 3;

		if (ofClearWater) {
			opacity = 1;
		}

		BlockUtils.setLightOpacity(Blocks.water, opacity);
		BlockUtils.setLightOpacity(Blocks.flowing_water, opacity);

		if (mc.theWorld != null) {
			final IChunkProvider cp = mc.theWorld.getChunkProvider();

			if (cp != null) {
				for (int x = -512; x < 512; ++x) {
					for (int z = -512; z < 512; ++z) {
						if (cp.chunkExists(x, z)) {
							final Chunk c = cp.provideChunk(x, z);

							if (c != null && !(c instanceof EmptyChunk)) {
								final ExtendedBlockStorage[] ebss = c
										.getBlockStorageArray();

								for (final ExtendedBlockStorage ebs : ebss) {
									if (ebs != null) {
										final NibbleArray na = ebs
												.getSkylightArray();

										if (na != null) {
											final byte[] data = na.data;

											for (int d = 0; d < data.length; ++d) {
												data[d] = 0;
											}
										}
									}
								}

								c.generateSkylightMap();
							}
						}
					}
				}

				mc.renderGlobal.loadRenderers();
			}
		}
	}
}

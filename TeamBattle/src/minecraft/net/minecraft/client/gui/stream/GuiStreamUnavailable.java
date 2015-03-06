package net.minecraft.client.gui.stream;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.client.stream.NullStream;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Session;
import net.minecraft.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import tv.twitch.ErrorCode;

import com.google.common.collect.Lists;

public class GuiStreamUnavailable extends GuiScreen {
	public static enum Reason {
		ACCOUNT_NOT_BOUND("ACCOUNT_NOT_BOUND", 7, new ChatComponentTranslation(
				"stream.unavailable.account_not_bound", new Object[0]),
				new ChatComponentTranslation(
						"stream.unavailable.account_not_bound.okay",
						new Object[0])), ACCOUNT_NOT_MIGRATED(
				"ACCOUNT_NOT_MIGRATED", 6, new ChatComponentTranslation(
						"stream.unavailable.account_not_migrated",
						new Object[0]), new ChatComponentTranslation(
						"stream.unavailable.account_not_migrated.okay",
						new Object[0])), FAILED_TWITCH_AUTH(
				"FAILED_TWITCH_AUTH", 8, new ChatComponentTranslation(
						"stream.unavailable.failed_auth", new Object[0]),
				new ChatComponentTranslation(
						"stream.unavailable.failed_auth.okay", new Object[0])), FAILED_TWITCH_AUTH_ERROR(
				"FAILED_TWITCH_AUTH_ERROR", 9, new ChatComponentTranslation(
						"stream.unavailable.failed_auth_error", new Object[0])), INITIALIZATION_FAILURE(
				"INITIALIZATION_FAILURE", 10, new ChatComponentTranslation(
						"stream.unavailable.initialization_failure",
						new Object[0]), new ChatComponentTranslation(
						"stream.unavailable.report_to_mojang", new Object[0])), LIBRARY_ARCH_MISMATCH(
				"LIBRARY_ARCH_MISMATCH", 1, new ChatComponentTranslation(
						"stream.unavailable.library_arch_mismatch",
						new Object[0])), LIBRARY_FAILURE("LIBRARY_FAILURE", 2,
				new ChatComponentTranslation(
						"stream.unavailable.library_failure", new Object[0]),
				new ChatComponentTranslation(
						"stream.unavailable.report_to_mojang", new Object[0])), NO_FBO(
				"NO_FBO", 0, new ChatComponentTranslation(
						"stream.unavailable.no_fbo", new Object[0])), UNKNOWN(
				"UNKNOWN", 11, new ChatComponentTranslation(
						"stream.unavailable.unknown", new Object[0]),
				new ChatComponentTranslation(
						"stream.unavailable.report_to_mojang", new Object[0])), UNSUPPORTED_OS_MAC(
				"UNSUPPORTED_OS_MAC", 4, new ChatComponentTranslation(
						"stream.unavailable.not_supported.mac", new Object[0]),
				new ChatComponentTranslation(
						"stream.unavailable.not_supported.mac.okay",
						new Object[0])), UNSUPPORTED_OS_OTHER(
				"UNSUPPORTED_OS_OTHER",
				5,
				new ChatComponentTranslation(
						"stream.unavailable.not_supported.other", new Object[0])), UNSUPPORTED_OS_WINDOWS(
				"UNSUPPORTED_OS_WINDOWS", 3, new ChatComponentTranslation(
						"stream.unavailable.not_supported.windows",
						new Object[0]));
		private final IChatComponent field_152574_m;
		private final IChatComponent field_152575_n;

		private Reason(String p_i1066_1_, int p_i1066_2_,
				IChatComponent p_i1066_3_) {
			this(p_i1066_1_, p_i1066_2_, p_i1066_3_, (IChatComponent) null);
		}

		private Reason(String p_i1067_1_, int p_i1067_2_,
				IChatComponent p_i1067_3_, IChatComponent p_i1067_4_) {
			field_152574_m = p_i1067_3_;
			field_152575_n = p_i1067_4_;
		}

		public IChatComponent func_152559_b() {
			return field_152575_n;
		}

		public IChatComponent func_152561_a() {
			return field_152574_m;
		}
	}

	static final class SwitchReason {
		static final int[] field_152577_a;

		static final int[] field_152578_b;

		static final int[] field_152579_c = new int[IStream.AuthFailureReason
				.values().length];

		static {
			try {
				field_152579_c[IStream.AuthFailureReason.INVALID_TOKEN
						.ordinal()] = 1;
			} catch (final NoSuchFieldError var11) {
				;
			}

			try {
				field_152579_c[IStream.AuthFailureReason.ERROR.ordinal()] = 2;
			} catch (final NoSuchFieldError var10) {
				;
			}

			field_152578_b = new int[Util.EnumOS.values().length];

			try {
				field_152578_b[Util.EnumOS.WINDOWS.ordinal()] = 1;
			} catch (final NoSuchFieldError var9) {
				;
			}

			try {
				field_152578_b[Util.EnumOS.OSX.ordinal()] = 2;
			} catch (final NoSuchFieldError var8) {
				;
			}

			field_152577_a = new int[GuiStreamUnavailable.Reason.values().length];

			try {
				field_152577_a[GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND
						.ordinal()] = 1;
			} catch (final NoSuchFieldError var7) {
				;
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH
						.ordinal()] = 2;
			} catch (final NoSuchFieldError var6) {
				;
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED
						.ordinal()] = 3;
			} catch (final NoSuchFieldError var5) {
				;
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC
						.ordinal()] = 4;
			} catch (final NoSuchFieldError var4) {
				;
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.UNKNOWN.ordinal()] = 5;
			} catch (final NoSuchFieldError var3) {
				;
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.LIBRARY_FAILURE
						.ordinal()] = 6;
			} catch (final NoSuchFieldError var2) {
				;
			}

			try {
				field_152577_a[GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE
						.ordinal()] = 7;
			} catch (final NoSuchFieldError var1) {
				;
			}
		}
	}

	private static final Logger field_152322_a = LogManager.getLogger();

	public static void func_152321_a(GuiScreen p_152321_0_) {
		final Minecraft var1 = Minecraft.getMinecraft();
		final IStream var2 = var1.func_152346_Z();

		if (!OpenGlHelper.framebufferSupported) {
			final ArrayList var3 = Lists.newArrayList();
			var3.add(new ChatComponentTranslation(
					"stream.unavailable.no_fbo.version", new Object[] { GL11
							.glGetString(GL11.GL_VERSION) }));
			var3.add(new ChatComponentTranslation(
					"stream.unavailable.no_fbo.blend",
					new Object[] { Boolean.valueOf(GLContext.getCapabilities().GL_EXT_blend_func_separate) }));
			var3.add(new ChatComponentTranslation(
					"stream.unavailable.no_fbo.arb",
					new Object[] { Boolean.valueOf(GLContext.getCapabilities().GL_ARB_framebuffer_object) }));
			var3.add(new ChatComponentTranslation(
					"stream.unavailable.no_fbo.ext",
					new Object[] { Boolean.valueOf(GLContext.getCapabilities().GL_EXT_framebuffer_object) }));
			var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
					GuiStreamUnavailable.Reason.NO_FBO, var3));
		} else if (var2 instanceof NullStream) {
			if (((NullStream) var2)
					.func_152937_a()
					.getMessage()
					.contains(
							"Can\'t load AMD 64-bit .dll on a IA 32-bit platform")) {
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.LIBRARY_ARCH_MISMATCH));
			} else {
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.LIBRARY_FAILURE));
			}
		} else if (!var2.func_152928_D()
				&& var2.func_152912_E() == ErrorCode.TTV_EC_OS_TOO_OLD) {
			switch (GuiStreamUnavailable.SwitchReason.field_152578_b[Util
					.getOSType().ordinal()]) {
			case 1:
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.UNSUPPORTED_OS_WINDOWS));
				break;

			case 2:
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.UNSUPPORTED_OS_MAC));
				break;

			default:
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.UNSUPPORTED_OS_OTHER));
			}
		} else if (!var1.func_152341_N().containsKey("twitch_access_token")) {
			if (var1.getSession().func_152428_f() == Session.Type.LEGACY) {
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.ACCOUNT_NOT_MIGRATED));
			} else {
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.ACCOUNT_NOT_BOUND));
			}
		} else if (!var2.func_152913_F()) {
			switch (GuiStreamUnavailable.SwitchReason.field_152579_c[var2
					.func_152918_H().ordinal()]) {
			case 1:
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH));
				break;

			case 2:
			default:
				var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
						GuiStreamUnavailable.Reason.FAILED_TWITCH_AUTH_ERROR));
			}
		} else if (var2.func_152912_E() != null) {
			final List var4 = Arrays
					.asList(new ChatComponentTranslation[] { new ChatComponentTranslation(
							"stream.unavailable.initialization_failure.extra",
							new Object[] { ErrorCode.getString(var2
									.func_152912_E()) }) });
			var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
					GuiStreamUnavailable.Reason.INITIALIZATION_FAILURE, var4));
		} else {
			var1.displayGuiScreen(new GuiStreamUnavailable(p_152321_0_,
					GuiStreamUnavailable.Reason.UNKNOWN));
		}
	}

	private final List field_152323_r;
	private final IChatComponent field_152324_f;

	private final GuiScreen field_152325_g;

	private final GuiStreamUnavailable.Reason field_152326_h;

	private final List field_152327_i;

	public GuiStreamUnavailable(GuiScreen p_i1070_1_,
			GuiStreamUnavailable.Reason p_i1070_2_) {
		this(p_i1070_1_, p_i1070_2_, (List) null);
	}

	public GuiStreamUnavailable(GuiScreen p_i1071_1_,
			GuiStreamUnavailable.Reason p_i1071_2_, List p_i1071_3_) {
		field_152324_f = new ChatComponentTranslation(
				"stream.unavailable.title", new Object[0]);
		field_152323_r = Lists.newArrayList();
		field_152325_g = p_i1071_1_;
		field_152326_h = p_i1071_2_;
		field_152327_i = p_i1071_3_;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 1) {
				switch (GuiStreamUnavailable.SwitchReason.field_152577_a[field_152326_h
						.ordinal()]) {
				case 1:
				case 2:
					func_152320_a("https://account.mojang.com/me/settings");
					break;

				case 3:
					func_152320_a("https://account.mojang.com/migrate");
					break;

				case 4:
					func_152320_a("http://www.apple.com/osx/");
					break;

				case 5:
				case 6:
				case 7:
					func_152320_a("http://bugs.mojang.com/browse/MC");
				}
			}

			mc.displayGuiScreen(field_152325_g);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		int var4 = Math.max(
				(int) (height * 0.85D / 2.0D - field_152323_r.size()
						* fontRendererObj.FONT_HEIGHT / 2.0F), 50);
		drawCenteredString(fontRendererObj, field_152324_f.getFormattedText(),
				width / 2, var4 - fontRendererObj.FONT_HEIGHT * 2, 16777215);

		for (final Iterator var5 = field_152323_r.iterator(); var5.hasNext(); var4 += fontRendererObj.FONT_HEIGHT) {
			final String var6 = (String) var5.next();
			drawCenteredString(fontRendererObj, var6, width / 2, var4, 10526880);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	private void func_152320_a(String p_152320_1_) {
		try {
			final Class var2 = Class.forName("java.awt.Desktop");
			final Object var3 = var2.getMethod("getDesktop", new Class[0])
					.invoke((Object) null, new Object[0]);
			var2.getMethod("browse", new Class[] { URI.class }).invoke(var3,
					new Object[] { new URI(p_152320_1_) });
		} catch (final Throwable var4) {
			field_152322_a.error("Couldn\'t open link", var4);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		if (field_152323_r.isEmpty()) {
			field_152323_r.addAll(fontRendererObj.listFormattedStringToWidth(
					field_152326_h.func_152561_a().getFormattedText(),
					(int) (width * 0.75F)));

			if (field_152327_i != null) {
				field_152323_r.add("");
				final Iterator var1 = field_152327_i.iterator();

				while (var1.hasNext()) {
					final ChatComponentTranslation var2 = (ChatComponentTranslation) var1
							.next();
					field_152323_r.add(var2.getUnformattedTextForChat());
				}
			}
		}

		if (field_152326_h.func_152559_b() != null) {
			buttons.add(new GuiButton(0, width / 2 - 155, height - 50, 150, 20,
					I18n.format("gui.cancel", new Object[0])));
			buttons.add(new GuiButton(1, width / 2 - 155 + 160, height - 50,
					150, 20, I18n.format(field_152326_h.func_152559_b()
							.getFormattedText(), new Object[0])));
		} else {
			buttons.add(new GuiButton(0, width / 2 - 75, height - 50, 150, 20,
					I18n.format("gui.cancel", new Object[0])));
		}
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
	}
}

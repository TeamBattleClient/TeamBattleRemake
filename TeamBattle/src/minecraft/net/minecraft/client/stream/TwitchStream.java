package net.minecraft.client.stream;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.helpers.Strings;
import org.lwjgl.opengl.GL11;

import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.FrameBuffer;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.VideoParams;
import tv.twitch.chat.ChatMessage;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TwitchStream implements BroadcastController.BroadcastListener,
		ChatController.ChatListener, IngestServerTester.IngestTestListener,
		IStream {
	public static final Marker field_152949_a = MarkerManager
			.getMarker("STREAM");

	private static final Logger field_152950_b = LogManager.getLogger();

	private static boolean field_152965_q;

	static {
		try {
			if (Util.getOSType() == Util.EnumOS.WINDOWS) {
				System.loadLibrary("avutil-ttv-51");
				System.loadLibrary("swresample-ttv-0");
				System.loadLibrary("libmp3lame-ttv");

				if (System.getProperty("os.arch").contains("64")) {
					System.loadLibrary("libmfxsw64");
				} else {
					System.loadLibrary("libmfxsw32");
				}
			}

			field_152965_q = true;
		} catch (final Throwable var1) {
			field_152965_q = false;
		}
	}

	public static int func_152946_b(float p_152946_0_) {
		return MathHelper.floor_float(230.0F + p_152946_0_ * 3270.0F);
	}

	public static float func_152947_c(float p_152947_0_) {
		return 0.1F + p_152947_0_ * 0.1F;
	}

	public static int func_152948_a(float p_152948_0_) {
		return MathHelper.floor_float(10.0F + p_152948_0_ * 50.0F);
	}

	private final BroadcastController field_152951_c;
	private final ChatController field_152952_d;
	private final Minecraft field_152953_e;
	private final IChatComponent field_152954_f = new ChatComponentText(
			"Twitch");
	private final Map field_152955_g = Maps.newHashMap();
	private Framebuffer field_152956_h;
	private boolean field_152957_i;
	private int field_152958_j = 30;
	private long field_152959_k = 0L;

	private boolean field_152961_m;

	private boolean field_152962_n;

	private boolean field_152963_o;

	private IStream.AuthFailureReason field_152964_p;

	public TwitchStream(Minecraft p_i1012_1_, final String p_i1012_2_) {
		field_152964_p = IStream.AuthFailureReason.ERROR;
		field_152953_e = p_i1012_1_;
		field_152951_c = new BroadcastController();
		field_152952_d = new ChatController();
		field_152951_c.func_152841_a(this);
		field_152952_d.func_152990_a(this);
		field_152951_c.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
		field_152952_d.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
		field_152954_f.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);

		if (Strings.isNotEmpty(p_i1012_2_) && OpenGlHelper.framebufferSupported) {
			final Thread var3 = new Thread("Twitch authenticator") {

				@Override
				public void run() {
					try {
						final URL var1 = new URL(
								"https://api.twitch.tv/kraken?oauth_token="
										+ URLEncoder
												.encode(p_i1012_2_, "UTF-8"));
						final String var2 = HttpUtil.func_152755_a(var1);
						final JsonObject var3 = JsonUtils
								.getJsonElementAsJsonObject(
										new JsonParser().parse(var2),
										"Response");
						final JsonObject var4 = JsonUtils.func_152754_s(var3,
								"token");

						if (JsonUtils.getJsonObjectBooleanFieldValue(var4,
								"valid")) {
							final String var5 = JsonUtils
									.getJsonObjectStringFieldValue(var4,
											"user_name");
							TwitchStream.field_152950_b
									.debug(TwitchStream.field_152949_a,
											"Authenticated with twitch; username is {}",
											new Object[] { var5 });
							final AuthToken var6 = new AuthToken();
							var6.data = p_i1012_2_;
							field_152951_c.func_152818_a(var5, var6);
							field_152952_d.func_152998_c(var5);
							field_152952_d.func_152994_a(var6);
							Runtime.getRuntime().addShutdownHook(
									new Thread("Twitch shutdown hook") {

										@Override
										public void run() {
											TwitchStream.this.func_152923_i();
										}
									});
							field_152951_c.func_152817_A();
						} else {
							field_152964_p = IStream.AuthFailureReason.INVALID_TOKEN;
							TwitchStream.field_152950_b.error(
									TwitchStream.field_152949_a,
									"Given twitch access token is invalid");
						}
					} catch (final IOException var7) {
						field_152964_p = IStream.AuthFailureReason.ERROR;
						TwitchStream.field_152950_b.error(
								TwitchStream.field_152949_a,
								"Could not authenticate with twitch", var7);
					}
				}
			};
			var3.setDaemon(true);
			var3.start();
		}
	}

	@Override
	public void func_152891_a(BroadcastController.BroadcastState p_152891_1_) {
		field_152950_b.debug(field_152949_a, "Broadcast state changed to {}",
				new Object[] { p_152891_1_ });

		if (p_152891_1_ == BroadcastController.BroadcastState.Initialized) {
			field_152951_c
					.func_152827_a(BroadcastController.BroadcastState.Authenticated);
		}
	}

	@Override
	public void func_152892_c(ErrorCode p_152892_1_) {
		ChatComponentTranslation var2;

		if (p_152892_1_ == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED) {
			var2 = new ChatComponentTranslation(
					"stream.unavailable.soundflower.chat.link", new Object[0]);
			var2.getChatStyle()
					.setChatClickEvent(
							new ClickEvent(
									ClickEvent.Action.OPEN_URL,
									"https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
			var2.getChatStyle().setUnderlined(Boolean.valueOf(true));
			final ChatComponentTranslation var3 = new ChatComponentTranslation(
					"stream.unavailable.soundflower.chat",
					new Object[] { var2 });
			var3.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
			field_152953_e.ingameGUI.getChatGUI().func_146227_a(var3);
		} else {
			var2 = new ChatComponentTranslation(
					"stream.unavailable.unknown.chat",
					new Object[] { ErrorCode.getString(p_152892_1_) });
			var2.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
			field_152953_e.ingameGUI.getChatGUI().func_146227_a(var2);
		}
	}

	@Override
	public void func_152893_b(ErrorCode p_152893_1_) {
		field_152950_b.warn(
				field_152949_a,
				"Issue submitting frame: {} (Error code {})",
				new Object[] { ErrorCode.getString(p_152893_1_),
						Integer.valueOf(p_152893_1_.getValue()) });
		field_152953_e.ingameGUI.getChatGUI().func_146234_a(
				new ChatComponentText("Issue streaming frame: " + p_152893_1_
						+ " (" + ErrorCode.getString(p_152893_1_) + ")"), 2);
	}

	@Override
	public void func_152894_a(StreamInfo p_152894_1_) {
		field_152950_b.debug(
				field_152949_a,
				"Stream info updated; {} viewers on stream ID {}",
				new Object[] { Integer.valueOf(p_152894_1_.viewers),
						Long.valueOf(p_152894_1_.streamId) });
	}

	@Override
	public void func_152895_a() {
		field_152950_b.info(field_152949_a, "Logged out of twitch");
	}

	@Override
	public void func_152896_a(IngestList p_152896_1_) {
	}

	@Override
	public void func_152897_a(ErrorCode p_152897_1_) {
		if (ErrorCode.succeeded(p_152897_1_)) {
			field_152950_b.debug(field_152949_a, "Login attempt successful");
			field_152961_m = true;
		} else {
			field_152950_b.warn(
					field_152949_a,
					"Login attempt unsuccessful: {} (error code {})",
					new Object[] { ErrorCode.getString(p_152897_1_),
							Integer.valueOf(p_152897_1_.getValue()) });
			field_152961_m = false;
		}
	}

	@Override
	public void func_152898_a(ErrorCode p_152898_1_, GameInfo[] p_152898_2_) {
	}

	@Override
	public void func_152899_b() {
		func_152915_s();
		field_152950_b.info(field_152949_a, "Broadcast to Twitch has started");
	}

	@Override
	public void func_152900_a(ErrorCode p_152900_1_, AuthToken p_152900_2_) {
	}

	@Override
	public void func_152901_c() {
		field_152950_b.info(field_152949_a, "Broadcast to Twitch has stopped");
	}

	@Override
	public void func_152902_f() {
	}

	@Override
	public void func_152903_a(ChatMessage[] p_152903_1_) {
		final ChatMessage[] var2 = p_152903_1_;
		final int var3 = p_152903_1_.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			final ChatMessage var5 = var2[var4];
			func_152939_a(var5.userName, var5);

			if (func_152940_a(var5.modes, var5.subscriptions,
					field_152953_e.gameSettings.field_152409_S)) {
				final ChatComponentText var6 = new ChatComponentText(
						var5.userName);
				final ChatComponentTranslation var7 = new ChatComponentTranslation(
						"chat.stream." + (var5.action ? "emote" : "text"),
						new Object[] {
								field_152954_f,
								var6,
								EnumChatFormatting
										.getTextWithoutFormattingCodes(var5.message) });

				if (var5.action) {
					var7.getChatStyle().setItalic(Boolean.valueOf(true));
				}

				final ChatComponentText var8 = new ChatComponentText("");
				var8.appendSibling(new ChatComponentTranslation(
						"stream.userinfo.chatTooltip", new Object[0]));
				final Iterator var9 = GuiTwitchUserMode.func_152328_a(
						var5.modes, var5.subscriptions, (IStream) null)
						.iterator();

				while (var9.hasNext()) {
					final IChatComponent var10 = (IChatComponent) var9.next();
					var8.appendText("\n");
					var8.appendSibling(var10);
				}

				var6.getChatStyle().setChatHoverEvent(
						new HoverEvent(HoverEvent.Action.SHOW_TEXT, var8));
				var6.getChatStyle().setChatClickEvent(
						new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO,
								var5.userName));
				field_152953_e.ingameGUI.getChatGUI().func_146227_a(var7);
			}
		}
	}

	@Override
	public void func_152904_a(ChatUserInfo[] p_152904_1_,
			ChatUserInfo[] p_152904_2_, ChatUserInfo[] p_152904_3_) {
		ChatUserInfo[] var4 = p_152904_2_;
		int var5 = p_152904_2_.length;
		int var6;
		ChatUserInfo var7;

		for (var6 = 0; var6 < var5; ++var6) {
			var7 = var4[var6];
			field_152955_g.remove(var7.displayName);
		}

		var4 = p_152904_3_;
		var5 = p_152904_3_.length;

		for (var6 = 0; var6 < var5; ++var6) {
			var7 = var4[var6];
			field_152955_g.put(var7.displayName, var7);
		}

		var4 = p_152904_1_;
		var5 = p_152904_1_.length;

		for (var6 = 0; var6 < var5; ++var6) {
			var7 = var4[var6];
			field_152955_g.put(var7.displayName, var7);
		}
	}

	@Override
	public void func_152905_e() {
		field_152950_b.debug(field_152949_a, "Chat disconnected");
		field_152955_g.clear();
	}

	@Override
	public void func_152906_d() {
		field_152950_b.debug(field_152949_a, "Chat connected");
	}

	@Override
	public void func_152907_a(IngestServerTester p_152907_1_,
			IngestServerTester.IngestTestState p_152907_2_) {
		field_152950_b.debug(field_152949_a, "Ingest test state changed to {}",
				new Object[] { p_152907_2_ });

		if (p_152907_2_ == IngestServerTester.IngestTestState.Finished) {
		}
	}

	@Override
	public boolean func_152908_z() {
		return field_152951_c.func_152825_o();
	}

	@Override
	public void func_152909_x() {
		final IngestServerTester var1 = field_152951_c.func_152838_J();

		if (var1 != null) {
			var1.func_153042_a(this);
		}
	}

	@Override
	public void func_152910_a(boolean p_152910_1_) {
		field_152963_o = p_152910_1_;
		func_152915_s();
	}

	@Override
	public void func_152911_a(Metadata p_152911_1_, long p_152911_2_) {
		if (func_152934_n() && field_152957_i) {
			final long var4 = field_152951_c.func_152844_x();

			if (!field_152951_c.func_152840_a(p_152911_1_.func_152810_c(), var4
					+ p_152911_2_, p_152911_1_.func_152809_a(),
					p_152911_1_.func_152806_b())) {
				field_152950_b.warn(field_152949_a,
						"Couldn\'t send stream metadata action at {}: {}",
						new Object[] { Long.valueOf(var4 + p_152911_2_),
								p_152911_1_ });
			} else {
				field_152950_b
						.debug(field_152949_a,
								"Sent stream metadata action at {}: {}",
								new Object[] {
										Long.valueOf(var4 + p_152911_2_),
										p_152911_1_ });
			}
		}
	}

	@Override
	public ErrorCode func_152912_E() {
		return !field_152965_q ? ErrorCode.TTV_EC_OS_TOO_OLD : field_152951_c
				.func_152852_P();
	}

	@Override
	public boolean func_152913_F() {
		return field_152961_m;
	}

	@Override
	public void func_152914_u() {
		if (field_152951_c.func_152819_E()) {
			field_152950_b.info(field_152949_a, "Stopped streaming to Twitch");
		} else {
			field_152950_b.warn(field_152949_a,
					"Could not stop streaming to Twitch");
		}
	}

	@Override
	public void func_152915_s() {
		if (func_152934_n()) {
			final float var1 = field_152953_e.gameSettings.field_152402_L;
			final boolean var2 = field_152962_n || var1 <= 0.0F;
			field_152951_c.func_152837_b(var2 ? 0.0F : var1);
			field_152951_c.func_152829_a(func_152929_G() ? 0.0F
					: field_152953_e.gameSettings.field_152401_K);
		}
	}

	@Override
	public void func_152916_q() {
		field_152951_c.func_152847_F();
		field_152962_n = true;
		func_152915_s();
	}

	@Override
	public void func_152917_b(String p_152917_1_) {
		field_152952_d.func_152992_g(p_152917_1_);
	}

	@Override
	public IStream.AuthFailureReason func_152918_H() {
		return field_152964_p;
	}

	@Override
	public boolean func_152919_o() {
		return field_152951_c.func_152839_p();
	}

	@Override
	public int func_152920_A() {
		return func_152934_n() ? field_152951_c.func_152816_j().viewers : 0;
	}

	@Override
	public String func_152921_C() {
		return field_152952_d.field_153005_c;
	}

	@Override
	public void func_152922_k() {
		if (field_152951_c.func_152850_m() && !field_152951_c.func_152839_p()) {
			final long var1 = System.nanoTime();
			final long var3 = 1000000000 / field_152958_j;
			final long var5 = var1 - field_152959_k;
			final boolean var7 = var5 >= var3;

			if (var7) {
				final FrameBuffer var8 = field_152951_c.func_152822_N();
				final Framebuffer var9 = field_152953_e.getFramebuffer();
				field_152956_h.bindFramebuffer(true);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, field_152956_h.framebufferWidth,
						field_152956_h.framebufferHeight, 0.0D, 1000.0D,
						3000.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPushMatrix();
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glViewport(0, 0, field_152956_h.framebufferWidth,
						field_152956_h.framebufferHeight);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_BLEND);
				final float var10 = field_152956_h.framebufferWidth;
				final float var11 = field_152956_h.framebufferHeight;
				final float var12 = (float) var9.framebufferWidth
						/ (float) var9.framebufferTextureWidth;
				final float var13 = (float) var9.framebufferHeight
						/ (float) var9.framebufferTextureHeight;
				var9.bindFramebufferTexture();
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MIN_FILTER, 9729.0F);
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D,
						GL11.GL_TEXTURE_MAG_FILTER, 9729.0F);
				final Tessellator var14 = Tessellator.instance;
				var14.startDrawingQuads();
				var14.addVertexWithUV(0.0D, var11, 0.0D, 0.0D, var13);
				var14.addVertexWithUV(var10, var11, 0.0D, var12, var13);
				var14.addVertexWithUV(var10, 0.0D, 0.0D, var12, 0.0D);
				var14.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				var14.draw();
				var9.unbindFramebufferTexture();
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				field_152951_c.func_152846_a(var8);
				field_152956_h.unbindFramebuffer();
				field_152951_c.func_152859_b(var8);
				field_152959_k = var1;
			}
		}
	}

	@Override
	public void func_152923_i() {
		field_152950_b.debug(field_152949_a, "Shutdown streaming");
		field_152951_c.func_152851_B();
		field_152952_d.func_152993_m();
	}

	@Override
	public boolean func_152924_m() {
		return field_152951_c.func_152857_n();
	}

	@Override
	public IngestServer[] func_152925_v() {
		return field_152951_c.func_152855_t().getServers();
	}

	@Override
	public ChatUserInfo func_152926_a(String p_152926_1_) {
		return (ChatUserInfo) field_152955_g.get(p_152926_1_);
	}

	@Override
	public boolean func_152927_B() {
		return field_152952_d.func_152991_c()
				&& field_152952_d.field_153005_c.equals(field_152951_c
						.func_152843_l().name);
	}

	@Override
	public boolean func_152928_D() {
		return field_152965_q && field_152951_c.func_152858_b();
	}

	@Override
	public boolean func_152929_G() {
		final boolean var1 = field_152953_e.gameSettings.field_152410_T == 1;
		return field_152962_n
				|| field_152953_e.gameSettings.field_152401_K <= 0.0F
				|| var1 != field_152963_o;
	}

	@Override
	public void func_152930_t() {
		final GameSettings var1 = field_152953_e.gameSettings;
		final VideoParams var2 = field_152951_c.func_152834_a(
				func_152946_b(var1.field_152403_M),
				func_152948_a(var1.field_152404_N),
				func_152947_c(var1.field_152400_J),
				(float) field_152953_e.displayWidth
						/ (float) field_152953_e.displayHeight);

		switch (var1.field_152405_O) {
		case 0:
			var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
			break;

		case 1:
			var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
			break;

		case 2:
			var2.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
		}

		if (field_152956_h == null) {
			field_152956_h = new Framebuffer(var2.outputWidth,
					var2.outputHeight, false);
		} else {
			field_152956_h.createBindFramebuffer(var2.outputWidth,
					var2.outputHeight);
		}

		if (var1.field_152407_Q != null && var1.field_152407_Q.length() > 0) {
			final IngestServer[] var3 = func_152925_v();
			final int var4 = var3.length;

			for (int var5 = 0; var5 < var4; ++var5) {
				final IngestServer var6 = var3[var5];

				if (var6.serverUrl.equals(var1.field_152407_Q)) {
					field_152951_c.func_152824_a(var6);
					break;
				}
			}
		}

		field_152958_j = var2.targetFps;
		field_152957_i = var1.field_152406_P;
		field_152951_c.func_152836_a(var2);
		field_152950_b.info(
				field_152949_a,
				"Streaming at {}/{} at {} kbps to {}",
				new Object[] { Integer.valueOf(var2.outputWidth),
						Integer.valueOf(var2.outputHeight),
						Integer.valueOf(var2.maxKbps),
						field_152951_c.func_152833_s().serverUrl });
		field_152951_c.func_152828_a((String) null, "Minecraft", (String) null);
	}

	@Override
	public void func_152931_p() {
		if (field_152951_c.func_152830_D()) {
			field_152950_b.debug(field_152949_a,
					"Requested commercial from Twitch");
		} else {
			field_152950_b.warn(field_152949_a,
					"Could not request commercial from Twitch");
		}
	}

	@Override
	public IngestServerTester func_152932_y() {
		return field_152951_c.func_152856_w();
	}

	@Override
	public void func_152933_r() {
		field_152951_c.func_152854_G();
		field_152962_n = false;
		func_152915_s();
	}

	@Override
	public boolean func_152934_n() {
		return field_152951_c.func_152850_m();
	}

	@Override
	public void func_152935_j() {
		final int var1 = field_152953_e.gameSettings.field_152408_R;
		final ChatController.ChatState var2 = field_152952_d.func_153000_j();

		if (var1 == 2) {
			if (var2 == ChatController.ChatState.Connected) {
				field_152950_b.debug(field_152949_a,
						"Disconnecting from twitch chat per user options");
				field_152952_d.func_153002_l();
			}
		} else if (var1 == 1) {
			if ((var2 == ChatController.ChatState.Disconnected || var2 == ChatController.ChatState.Uninitialized)
					&& field_152951_c.func_152849_q()) {
				field_152950_b.debug(field_152949_a,
						"Connecting to twitch chat per user options");
				func_152942_I();
			}
		} else if (var1 == 0) {
			if ((var2 == ChatController.ChatState.Disconnected || var2 == ChatController.ChatState.Uninitialized)
					&& func_152934_n()) {
				field_152950_b.debug(field_152949_a,
						"Connecting to twitch chat as user is streaming");
				func_152942_I();
			} else if (var2 == ChatController.ChatState.Connected
					&& !func_152934_n()) {
				field_152950_b
						.debug(field_152949_a,
								"Disconnecting from twitch chat as user is no longer streaming");
				field_152952_d.func_153002_l();
			}
		}

		field_152951_c.func_152821_H();
		field_152952_d.func_152997_n();
	}

	@Override
	public boolean func_152936_l() {
		return field_152951_c.func_152849_q();
	}

	private void func_152939_a(String p_152939_1_, ChatMessage p_152939_2_) {
		ChatUserInfo var3 = (ChatUserInfo) field_152955_g.get(p_152939_1_);

		if (var3 == null) {
			var3 = new ChatUserInfo();
			var3.displayName = p_152939_1_;
			field_152955_g.put(p_152939_1_, var3);
		}

		var3.subscriptions = p_152939_2_.subscriptions;
		var3.modes = p_152939_2_.modes;
		var3.emoticonSet = p_152939_2_.emoticonSet;
		var3.nameColorARGB = p_152939_2_.nameColorARGB;
	}

	private boolean func_152940_a(HashSet p_152940_1_, HashSet p_152940_2_,
			int p_152940_3_) {
		return p_152940_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED) ? false
				: p_152940_1_
						.contains(ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) ? true
						: p_152940_1_
								.contains(ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) ? true
								: p_152940_1_
										.contains(ChatUserMode.TTV_CHAT_USERMODE_STAFF) ? true
										: p_152940_3_ == 0 ? true
												: p_152940_3_ == 1 ? p_152940_2_
														.contains(ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER)
														: false;
	}

	protected void func_152942_I() {
		final ChatController.ChatState var1 = field_152952_d.func_153000_j();
		final String var2 = field_152951_c.func_152843_l().name;

		if (var1 == ChatController.ChatState.Uninitialized) {
			field_152952_d.func_152985_f(var2);
			field_152952_d.field_153005_c = var2;
		} else if (var1 == ChatController.ChatState.Disconnected) {
			field_152952_d.func_152986_d(var2);
		} else {
			field_152950_b.warn("Invalid twitch chat state {}",
					new Object[] { var1 });
		}
	}
}

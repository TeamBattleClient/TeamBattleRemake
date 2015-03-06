package net.minecraft.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import me.client.Client;
import me.client.ui.screens.GuiAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	public static final String field_96138_a = "Please click "
			+ EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET
			+ " for more information.";

	private static final Logger logger = LogManager.getLogger();

	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");

	/** The RNG used by the Main Menu Screen. */
	private static final Random rand = new Random();
	private static final ResourceLocation splashTexts = new ResourceLocation(
			"texts/splashes.txt");

	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };

	private GuiButton buttonResetDemo;
	private String field_104024_v;
	private final Object field_104025_t = new Object();
	private ResourceLocation field_110351_G;
	private String field_146972_A;
	private int field_92019_w;
	private int field_92020_v;

	private int field_92021_u;
	private int field_92022_t;
	private int field_92023_s;
	private int field_92024_r;
	private String field_92025_p;
	/** Timer used to rotate the panorama, increases every tick. */
	private int panoramaTimer;
	/** The splash message. */
	private String splashText;
	/** Counts the number of screen updates. */
	private final float updateCounter;
	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;

	public GuiMainMenu() {
		field_146972_A = field_96138_a;
		splashText = "missingno";
		BufferedReader var1 = null;

		try {
			final ArrayList var2 = new ArrayList();
			var1 = new BufferedReader(new InputStreamReader(Minecraft
					.getMinecraft().getResourceManager()
					.getResource(splashTexts).getInputStream(), Charsets.UTF_8));
			String var3;

			while ((var3 = var1.readLine()) != null) {
				var3 = var3.trim();

				if (!var3.isEmpty()) {
					var2.add(var3);
				}
			}

			if (!var2.isEmpty()) {
				do {
					splashText = (String) var2.get(rand.nextInt(var2.size()));
				} while (splashText.hashCode() == 125780783);
			}
		} catch (final IOException var12) {
			;
		} finally {
			if (var1 != null) {
				try {
					var1.close();
				} catch (final IOException var11) {
					;
				}
			}
		}

		updateCounter = rand.nextFloat();
		field_92025_p = "";
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
		}

		if (p_146284_1_.id == 5) {
			mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc
					.getLanguageManager()));
		}

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (p_146284_1_.id == 2) {
			mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (p_146284_1_.id == 14) {
			func_140005_i();
		}

		if (p_146284_1_.id == 15) {
			mc.displayGuiScreen(new GuiAltManager());
		}

		

		if (p_146284_1_.id == 4) {
			mc.shutdown();
		}

		if (p_146284_1_.id == 11) {
			mc.launchIntegratedServer("Demo_World", "Demo_World",
					DemoWorldServer.demoWorldSettings);
		}

		if (p_146284_1_.id == 12) {
			final ISaveFormat var2 = mc.getSaveLoader();
			final WorldInfo var3 = var2.getWorldInfo("Demo_World");

			if (var3 != null) {
				final GuiYesNo var4 = GuiSelectWorld.func_152129_a(this,
						var3.getWorldName(), 12);
				mc.displayGuiScreen(var4);
			}
		}
	}

	/**
	 * Adds Demo buttons on Main Menu for players who are playing Demo.
	 */
	private void addDemoButtons(int p_73972_1_, int p_73972_2_) {
		buttons.add(new GuiButton(11, width / 2 - 100, p_73972_1_, I18n.format(
				"menu.playdemo", new Object[0])));
		buttons.add(buttonResetDemo = new GuiButton(12, width / 2 - 100,
				p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo",
						new Object[0])));
		final ISaveFormat var3 = mc.getSaveLoader();
		final WorldInfo var4 = var3.getWorldInfo("Demo_World");

		if (var4 == null) {
			buttonResetDemo.enabled = false;
		}
	}

	/**
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for players who
	 * have bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int p_73969_1_,
			int p_73969_2_) {
		buttons.add(new GuiButton(1, width / 2 - 100, p_73969_1_, I18n.format(
				"menu.singleplayer", new Object[0])));
		buttons.add(new GuiButton(2, width / 2 - 100, p_73969_1_ + p_73969_2_
				* 1, I18n.format("menu.multiplayer", new Object[0])));
		// buttonList.add(new GuiButton(14, width / 2 - 100, p_73969_1_
		// + p_73969_2_ * 2, 99, 20, I18n.format("menu.online",
		// new Object[0])));
		buttons.add(new GuiButton(15, width / 2 + 2, p_73969_1_ + p_73969_2_
				* 2, 99, 20, "Alt Manager"));
		buttons.add(new GuiButton(16, width / 2 - 100, p_73969_1_ + p_73969_2_
				* 2, 99, 20, "Changelog"));
	}

	@Override
	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
		if (p_73878_1_ && p_73878_2_ == 12) {
			final ISaveFormat var6 = mc.getSaveLoader();
			var6.flushCache();
			var6.deleteWorldDirectory("Demo_World");
			mc.displayGuiScreen(this);
		} else if (p_73878_2_ == 13) {
			if (p_73878_1_) {
				try {
					final Class var3 = Class.forName("java.awt.Desktop");
					final Object var4 = var3.getMethod("getDesktop",
							new Class[0]).invoke((Object) null, new Object[0]);
					var3.getMethod("browse", new Class[] { URI.class }).invoke(
							var4, new Object[] { new URI(field_104024_v) });
				} catch (final Throwable var5) {
					logger.error("Couldn\'t open link", var5);
				}
			}

			mc.displayGuiScreen(this);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
		final Tessellator var4 = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		final byte var5 = 8;

		for (int var6 = 0; var6 < var5 * var5; ++var6) {
			GL11.glPushMatrix();
			final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
			final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
			final float var9 = 0.0F;
			GL11.glTranslatef(var7, var8, var9);
			GL11.glRotatef(
					MathHelper.sin((panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F,
					1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-(panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F,
					0.0F);

			for (int var10 = 0; var10 < 6; ++var10) {
				GL11.glPushMatrix();

				if (var10 == 1) {
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 3) {
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (var10 == 4) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (var10 == 5) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				mc.getTextureManager().bindTexture(titlePanoramaPaths[var10]);
				var4.startDrawingQuads();
				var4.setColorRGBA_I(16777215, 255 / (var6 + 1));
				final float var11 = 0.0F;
				var4.addVertexWithUV(-1.0D, -1.0D, 1.0D, 0.0F + var11,
						0.0F + var11);
				var4.addVertexWithUV(1.0D, -1.0D, 1.0D, 1.0F - var11,
						0.0F + var11);
				var4.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - var11,
						1.0F - var11);
				var4.addVertexWithUV(-1.0D, 1.0D, 1.0D, 0.0F + var11,
						1.0F - var11);
				var4.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		var4.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		renderSkybox(p_73863_1_, p_73863_2_, p_73863_3_);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		final Tessellator var4 = Tessellator.instance;
		final short var5 = 274;
		final int var6 = width / 2 - var5 / 2;
		final byte var7 = 30;
		drawGradientRect(0, 0, width, height, -2130706433, 16777215);
		drawGradientRect(0, 0, width, height, 0, Integer.MIN_VALUE);
		mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if (updateCounter < 1.0E-4D) {
			drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 99, 44);
			drawTexturedModalRect(var6 + 99, var7 + 0, 129, 0, 27, 44);
			drawTexturedModalRect(var6 + 99 + 26, var7 + 0, 126, 0, 3, 44);
			drawTexturedModalRect(var6 + 99 + 26 + 3, var7 + 0, 99, 0, 26, 44);
			drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
		} else {
			drawTexturedModalRect(var6 + 0, var7 + 0, 0, 0, 155, 44);
			drawTexturedModalRect(var6 + 155, var7 + 0, 0, 45, 155, 44);
		}

		var4.setColorOpaque_I(-1);
		GL11.glPushMatrix();
		GL11.glTranslatef(width / 2 + 90, 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float var8 = 1.8F - MathHelper
				.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F
						* (float) Math.PI * 2.0F) * 0.1F);
		var8 = var8 * 100.0F
				/ (fontRendererObj.getStringWidth(splashText) + 32);
		GL11.glScalef(var8, var8, var8);
		drawCenteredString(fontRendererObj, splashText, 0, -8, -256);
		GL11.glPopMatrix();
		String var9 = "Minecraft 1.7.10";

		if (mc.isDemo()) {
			var9 = var9 + " Demo";
		}

		drawString(fontRendererObj, "TeamBattle for " + var9, 2, height - 20, -1);
		drawString(fontRendererObj,
				"Build "
						+ (Client.isNewerVersionAvailable() ? "\247c"
								: "\247a") + "#" + Client.getBuild(), 2,
				height - 10, -1);
		final String var10 = "Copyright of Mojang AB. Do not distribute!";
		drawString(fontRendererObj, var10,
				width - fontRendererObj.getStringWidth(var10) - 2, height - 10,
				-1);

		if (field_92025_p != null && field_92025_p.length() > 0) {
			drawRect(field_92022_t - 2, field_92021_u - 2, field_92020_v + 2,
					field_92019_w - 1, 1428160512);
			drawString(fontRendererObj, field_92025_p, field_92022_t,
					field_92021_u, -1);
			drawString(fontRendererObj, field_146972_A,
					(width - field_92024_r) / 2,
					((GuiButton) buttons.get(0)).field_146129_i - 12, -1);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	private void func_140005_i() {
		final RealmsBridge var1 = new RealmsBridge();
		var1.switchToRealms(this);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		viewportTexture = new DynamicTexture(256, 256);
		field_110351_G = mc.getTextureManager().getDynamicTextureLocation(
				"background", viewportTexture);
		final Calendar var1 = Calendar.getInstance();
		var1.setTime(new Date());

		if (var1.get(2) + 1 == 11 && var1.get(5) == 9) {
			splashText = "Happy birthday, ez!";
		} else if (var1.get(2) + 1 == 6 && var1.get(5) == 1) {
			splashText = "Happy birthday, Notch!";
		} else if (var1.get(2) + 1 == 12 && var1.get(5) == 24) {
			splashText = "Merry X-mas!";
		} else if (var1.get(2) + 1 == 1 && var1.get(5) == 1) {
			splashText = "Happy new year!";
		} else if (var1.get(2) + 1 == 10 && var1.get(5) == 31) {
			splashText = "OOoooOOOoooo! Spooky!";
		}

		final int var3 = height / 4 + 48;

		if (mc.isDemo()) {
			addDemoButtons(var3, 24);
		} else {
			addSingleplayerMultiplayerButtons(var3, 24);
		}

		buttons.add(new GuiButton(0, width / 2 - 100, var3 + 72 + 12, 98, 20,
				I18n.format("menu.options", new Object[0])));
		buttons.add(new GuiButton(4, width / 2 + 2, var3 + 72 + 12, 98, 20,
				I18n.format("menu.quit", new Object[0])));
		buttons.add(new GuiButtonLanguage(5, width / 2 - 124, var3 + 72 + 12));
		synchronized (field_104025_t) {
			field_92023_s = fontRendererObj.getStringWidth(field_92025_p);
			field_92024_r = fontRendererObj.getStringWidth(field_146972_A);
			final int var5 = Math.max(field_92023_s, field_92024_r);
			field_92022_t = (width - var5) / 2;
			field_92021_u = ((GuiButton) buttons.get(0)).field_146129_i - 24;
			field_92020_v = field_92022_t + var5;
			field_92019_w = field_92021_u + 24;
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		synchronized (field_104025_t) {
			if (field_92025_p.length() > 0 && p_73864_1_ >= field_92022_t
					&& p_73864_1_ <= field_92020_v
					&& p_73864_2_ >= field_92021_u
					&& p_73864_2_ <= field_92019_w) {
				final GuiConfirmOpenLink var5 = new GuiConfirmOpenLink(this,
						field_104024_v, 13, true);
				var5.func_146358_g();
				mc.displayGuiScreen(var5);
			}
		}
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
		mc.getFramebuffer().unbindFramebuffer();
		GL11.glViewport(0, 0, 256, 256);
		drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		rotateAndBlurSkybox(p_73971_3_);
		mc.getFramebuffer().bindFramebuffer(true);
		GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
		final Tessellator var4 = Tessellator.instance;
		var4.startDrawingQuads();
		final float var5 = width > height ? 120.0F / width : 120.0F / height;
		final float var6 = height * var5 / 256.0F;
		final float var7 = width * var5 / 256.0F;
		var4.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		final int var8 = width;
		final int var9 = height;
		var4.addVertexWithUV(0.0D, var9, zLevel, 0.5F - var6, 0.5F + var7);
		var4.addVertexWithUV(var8, var9, zLevel, 0.5F - var6, 0.5F - var7);
		var4.addVertexWithUV(var8, 0.0D, zLevel, 0.5F + var6, 0.5F - var7);
		var4.addVertexWithUV(0.0D, 0.0D, zLevel, 0.5F + var6, 0.5F + var7);
		var4.draw();
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float p_73968_1_) {
		mc.getTextureManager().bindTexture(field_110351_G);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColorMask(true, true, true, false);
		final Tessellator var2 = Tessellator.instance;
		var2.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		final byte var3 = 3;

		for (int var4 = 0; var4 < var3; ++var4) {
			var2.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (var4 + 1));
			final int var5 = width;
			final int var6 = height;
			final float var7 = (var4 - var3 / 2) / 256.0F;
			var2.addVertexWithUV(var5, var6, zLevel, 0.0F + var7, 1.0D);
			var2.addVertexWithUV(var5, 0.0D, zLevel, 1.0F + var7, 1.0D);
			var2.addVertexWithUV(0.0D, 0.0D, zLevel, 1.0F + var7, 0.0D);
			var2.addVertexWithUV(0.0D, var6, zLevel, 0.0F + var7, 0.0D);
		}

		var2.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColorMask(true, true, true, true);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		++panoramaTimer;
	}
}

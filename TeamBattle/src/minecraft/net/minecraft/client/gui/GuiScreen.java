package net.minecraft.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiScreen extends Gui {
	/**
	 * Holds a instance of RenderItem, used to draw the achievement icons on
	 * screen (is based on ItemStack)
	 */
	protected static RenderItem itemRender = new RenderItem();

	/**
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString() {
		try {
			final Transferable var0 = Toolkit.getDefaultToolkit()
					.getSystemClipboard().getContents((Object) null);

			if (var0 != null
					&& var0.isDataFlavorSupported(DataFlavor.stringFlavor))
				return (String) var0.getTransferData(DataFlavor.stringFlavor);
		} catch (final Exception var1) {
			;
		}

		return "";
	}

	/**
	 * Returns true if either windows ctrl key is down or if either mac meta key
	 * is down
	 */
	public static boolean isCtrlKeyDown() {
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219)
				|| Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29)
				|| Keyboard.isKeyDown(157);
	}

	/**
	 * Returns true if either shift key is down
	 */
	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}

	/**
	 * Stores the given string in the system clipboard
	 */
	public static void setClipboardString(String p_146275_0_) {
		try {
			final StringSelection var1 = new StringSelection(p_146275_0_);
			Toolkit.getDefaultToolkit().getSystemClipboard()
					.setContents(var1, (ClipboardOwner) null);
		} catch (final Exception var2) {
			;
		}
	}

	/** A list of all the buttons in this container. */
	protected List buttons = new ArrayList();
	private int eventButton;

	public boolean field_146291_p;

	private int field_146298_h;
	/** The FontRenderer used by GuiScreen */
	protected FontRenderer fontRendererObj;
	/** The height of the screen object. */
	public int height;
	/** A list of all the labels in this container. */
	protected List labelList = new ArrayList();

	private long lastMouseEvent;

	/** Reference to the Minecraft object. */
	protected Minecraft mc;

	/** The button that was just pressed. */
	private GuiButton selectedButton;

	/** The width of the screen object. */
	public int width;

	protected void actionPerformed(GuiButton p_146284_1_) {
	}

	public void confirmClicked(boolean p_73878_1_, int p_73878_2_) {
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return true;
	}

	public void drawDefaultBackground() {
		func_146270_b(0);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		int var4;

		for (var4 = 0; var4 < buttons.size(); ++var4) {
			((GuiButton) buttons.get(var4)).drawButton(mc, p_73863_1_,
					p_73863_2_);
		}

		for (var4 = 0; var4 < labelList.size(); ++var4) {
			((GuiLabel) labelList.get(var4)).func_146159_a(mc, p_73863_1_,
					p_73863_2_);
		}
	}

	public void func_146270_b(int p_146270_1_) {
		if (mc.theWorld != null) {
			drawGradientRect(0, 0, width, height, -1072689136, -804253680);
		} else {
			func_146278_c(p_146270_1_);
		}
	}

	public void func_146278_c(int p_146278_1_) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		final Tessellator var2 = Tessellator.instance;
		mc.getTextureManager().bindTexture(optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		final float var3 = 32.0F;
		var2.startDrawingQuads();
		var2.setColorOpaque_I(4210752);
		var2.addVertexWithUV(0.0D, height, 0.0D, 0.0D, height / var3
				+ p_146278_1_);
		var2.addVertexWithUV(width, height, 0.0D, width / var3, height / var3
				+ p_146278_1_);
		var2.addVertexWithUV(width, 0.0D, 0.0D, width / var3, p_146278_1_);
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, p_146278_1_);
		var2.draw();
	}

	protected void func_146279_a(String p_146279_1_, int p_146279_2_,
			int p_146279_3_) {
		func_146283_a(Arrays.asList(new String[] { p_146279_1_ }), p_146279_2_,
				p_146279_3_);
	}

	protected void func_146283_a(List p_146283_1_, int p_146283_2_,
			int p_146283_3_) {
		if (!p_146283_1_.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int var4 = 0;
			final Iterator var5 = p_146283_1_.iterator();

			while (var5.hasNext()) {
				final String var6 = (String) var5.next();
				final int var7 = fontRendererObj.getStringWidth(var6);

				if (var7 > var4) {
					var4 = var7;
				}
			}

			int var14 = p_146283_2_ + 12;
			int var15 = p_146283_3_ - 12;
			int var8 = 8;

			if (p_146283_1_.size() > 1) {
				var8 += 2 + (p_146283_1_.size() - 1) * 10;
			}

			if (var14 + var4 > width) {
				var14 -= 28 + var4;
			}

			if (var15 + var8 + 6 > height) {
				var15 = height - var8 - 6;
			}

			zLevel = 300.0F;
			itemRender.zLevel = 300.0F;
			final int var9 = -267386864;
			drawGradientRect(var14 - 3, var15 - 4, var14 + var4 + 3, var15 - 3,
					var9, var9);
			drawGradientRect(var14 - 3, var15 + var8 + 3, var14 + var4 + 3,
					var15 + var8 + 4, var9, var9);
			drawGradientRect(var14 - 3, var15 - 3, var14 + var4 + 3, var15
					+ var8 + 3, var9, var9);
			drawGradientRect(var14 - 4, var15 - 3, var14 - 3, var15 + var8 + 3,
					var9, var9);
			drawGradientRect(var14 + var4 + 3, var15 - 3, var14 + var4 + 4,
					var15 + var8 + 3, var9, var9);
			final int var10 = 1347420415;
			final int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
			drawGradientRect(var14 - 3, var15 - 3 + 1, var14 - 3 + 1, var15
					+ var8 + 3 - 1, var10, var11);
			drawGradientRect(var14 + var4 + 2, var15 - 3 + 1, var14 + var4 + 3,
					var15 + var8 + 3 - 1, var10, var11);
			drawGradientRect(var14 - 3, var15 - 3, var14 + var4 + 3,
					var15 - 3 + 1, var10, var10);
			drawGradientRect(var14 - 3, var15 + var8 + 2, var14 + var4 + 3,
					var15 + var8 + 3, var11, var11);

			for (int var12 = 0; var12 < p_146283_1_.size(); ++var12) {
				final String var13 = (String) p_146283_1_.get(var12);
				fontRendererObj.drawStringWithShadow(var13, var14, var15, -1);

				if (var12 == 0) {
					var15 += 2;
				}

				var15 += 10;
			}

			zLevel = 0.0F;
			itemRender.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	protected void func_146285_a(ItemStack p_146285_1_, int p_146285_2_,
			int p_146285_3_) {
		final List var4 = p_146285_1_.getTooltip(mc.thePlayer,
				mc.gameSettings.advancedItemTooltips);

		for (int var5 = 0; var5 < var4.size(); ++var5) {
			if (var5 == 0) {
				var4.set(var5, p_146285_1_.getRarity().rarityColor
						+ (String) var4.get(var5));
			} else {
				var4.set(var5,
						EnumChatFormatting.GRAY + (String) var4.get(var5));
			}
		}

		func_146283_a(var4, p_146285_2_, p_146285_3_);
	}

	/**
	 * Delegates mouse and keyboard input.
	 */
	public void handleInput() {
		if (Mouse.isCreated()) {
			while (Mouse.next()) {
				handleMouseInput();
			}
		}

		if (Keyboard.isCreated()) {
			while (Keyboard.next()) {
				handleKeyboardInput();
			}
		}
	}

	/**
	 * Handles keyboard input.
	 */
	public void handleKeyboardInput() {
		if (Keyboard.getEventKeyState()) {
			keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
		}

		mc.func_152348_aa();
	}

	/**
	 * Handles mouse input.
	 */
	public void handleMouseInput() {
		final int var1 = Mouse.getEventX() * width / mc.displayWidth;
		final int var2 = height - Mouse.getEventY() * height / mc.displayHeight
				- 1;
		final int var3 = Mouse.getEventButton();

		if (Mouse.getEventButtonState()) {
			if (mc.gameSettings.touchscreen && field_146298_h++ > 0)
				return;

			eventButton = var3;
			lastMouseEvent = Minecraft.getSystemTime();
			mouseClicked(var1, var2, eventButton);
		} else if (var3 != -1) {
			if (mc.gameSettings.touchscreen && --field_146298_h > 0)
				return;

			eventButton = -1;
			mouseMovedOrUp(var1, var2, var3);
		} else if (eventButton != -1 && lastMouseEvent > 0L) {
			final long var4 = Minecraft.getSystemTime() - lastMouseEvent;
			mouseClickMove(var1, var2, eventButton, var4);
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1) {
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		if (p_73864_3_ == 0) {
			for (int var4 = 0; var4 < buttons.size(); ++var4) {
				final GuiButton var5 = (GuiButton) buttons.get(var4);

				if (var5.mousePressed(mc, p_73864_1_, p_73864_2_)) {
					selectedButton = var5;
					var5.func_146113_a(mc.getSoundHandler());
					actionPerformed(var5);
				}
			}
		}
	}

	protected void mouseClickMove(int p_146273_1_, int p_146273_2_,
			int p_146273_3_, long p_146273_4_) {
	}

	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		if (selectedButton != null && p_146286_3_ == 0) {
			selectedButton.mouseReleased(p_146286_1_, p_146286_2_);
			selectedButton = null;
		}
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	public void onGuiClosed() {
	}

	/**
	 * Causes the screen to lay out its subcomponents again. This is the
	 * equivalent of the Java call Container.validate()
	 */
	public void setWorldAndResolution(Minecraft p_146280_1_, int p_146280_2_,
			int p_146280_3_) {
		mc = p_146280_1_;
		fontRendererObj = p_146280_1_.fontRenderer;
		width = p_146280_2_;
		height = p_146280_3_;
		buttons.clear();
		initGui();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
	}
}

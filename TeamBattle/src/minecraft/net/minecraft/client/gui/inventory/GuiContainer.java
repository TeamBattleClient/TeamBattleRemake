package net.minecraft.client.gui.inventory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class GuiContainer extends GuiScreen {
	protected static final ResourceLocation field_147001_a = new ResourceLocation(
			"textures/gui/container/inventory.png");
	private Slot field_146985_D;
	private long field_146986_E;
	private int field_146987_F;
	private int field_146988_G;
	private Slot field_146989_A;
	private long field_146990_B;
	private ItemStack field_146991_C;
	private int field_146992_L;
	private boolean field_146993_M;
	private ItemStack field_146994_N;
	private boolean field_146995_H;
	private int field_146996_I;
	private long field_146997_J;
	private Slot field_146998_K;
	protected int field_146999_f = 176;
	protected int field_147000_g = 166;
	public Container field_147002_h;
	protected int field_147003_i;
	private boolean field_147004_w;
	private Slot field_147005_v;
	private Slot field_147006_u;
	protected boolean field_147007_t;
	protected final Set field_147008_s = new HashSet();
	protected int field_147009_r;
	private int field_147010_z;
	private int field_147011_y;
	private ItemStack field_147012_x;

	public GuiContainer(Container p_i1072_1_) {
		field_147002_h = p_i1072_1_;
		field_146995_H = true;
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
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		final int var4 = field_147003_i;
		final int var5 = field_147009_r;
		func_146976_a(p_73863_3_, p_73863_1_, p_73863_2_);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(var4, var5, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		field_147006_u = null;
		final short var6 = 240;
		final short var7 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				var6 / 1.0F, var7 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var11;

		for (int var8 = 0; var8 < field_147002_h.inventorySlots.size(); ++var8) {
			final Slot var9 = (Slot) field_147002_h.inventorySlots.get(var8);
			func_146977_a(var9);

			if (func_146981_a(var9, p_73863_1_, p_73863_2_)
					&& var9.func_111238_b()) {
				field_147006_u = var9;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				final int var10 = var9.xDisplayPosition;
				var11 = var9.yDisplayPosition;
				GL11.glColorMask(true, true, true, false);
				drawGradientRect(var10, var11, var10 + 16, var11 + 16,
						-2130706433, -2130706433);
				GL11.glColorMask(true, true, true, true);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		func_146979_b(p_73863_1_, p_73863_2_);
		final InventoryPlayer var15 = mc.thePlayer.inventory;
		ItemStack var16 = field_147012_x == null ? var15.getItemStack()
				: field_147012_x;

		if (var16 != null) {
			final byte var17 = 8;
			var11 = field_147012_x == null ? 8 : 16;
			String var12 = null;

			if (field_147012_x != null && field_147004_w) {
				var16 = var16.copy();
				var16.stackSize = MathHelper
						.ceiling_float_int(var16.stackSize / 2.0F);
			} else if (field_147007_t && field_147008_s.size() > 1) {
				var16 = var16.copy();
				var16.stackSize = field_146996_I;

				if (var16.stackSize == 0) {
					var12 = "" + EnumChatFormatting.YELLOW + "0";
				}
			}

			func_146982_a(var16, p_73863_1_ - var4 - var17, p_73863_2_ - var5
					- var11, var12);
		}

		if (field_146991_C != null) {
			float var18 = (Minecraft.getSystemTime() - field_146990_B) / 100.0F;

			if (var18 >= 1.0F) {
				var18 = 1.0F;
				field_146991_C = null;
			}

			var11 = field_146989_A.xDisplayPosition - field_147011_y;
			final int var20 = field_146989_A.yDisplayPosition - field_147010_z;
			final int var13 = field_147011_y + (int) (var11 * var18);
			final int var14 = field_147010_z + (int) (var20 * var18);
			func_146982_a(field_146991_C, var13, var14, (String) null);
		}

		GL11.glPopMatrix();

		if (var15.getItemStack() == null && field_147006_u != null
				&& field_147006_u.getHasStack()) {
			final ItemStack var19 = field_147006_u.getStack();
			func_146285_a(var19, p_73863_1_, p_73863_2_);
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	private Slot func_146975_c(int p_146975_1_, int p_146975_2_) {
		for (int var3 = 0; var3 < field_147002_h.inventorySlots.size(); ++var3) {
			final Slot var4 = (Slot) field_147002_h.inventorySlots.get(var3);

			if (func_146981_a(var4, p_146975_1_, p_146975_2_))
				return var4;
		}

		return null;
	}

	protected abstract void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_);

	private void func_146977_a(Slot p_146977_1_) {
		final int var2 = p_146977_1_.xDisplayPosition;
		final int var3 = p_146977_1_.yDisplayPosition;
		ItemStack var4 = p_146977_1_.getStack();
		boolean var5 = false;
		boolean var6 = p_146977_1_ == field_147005_v && field_147012_x != null
				&& !field_147004_w;
		final ItemStack var7 = mc.thePlayer.inventory.getItemStack();
		String var8 = null;

		if (p_146977_1_ == field_147005_v && field_147012_x != null
				&& field_147004_w && var4 != null) {
			var4 = var4.copy();
			var4.stackSize /= 2;
		} else if (field_147007_t && field_147008_s.contains(p_146977_1_)
				&& var7 != null) {
			if (field_147008_s.size() == 1)
				return;

			if (Container.func_94527_a(p_146977_1_, var7, true)
					&& field_147002_h.canDragIntoSlot(p_146977_1_)) {
				var4 = var7.copy();
				var5 = true;
				Container.func_94525_a(
						field_147008_s,
						field_146987_F,
						var4,
						p_146977_1_.getStack() == null ? 0 : p_146977_1_
								.getStack().stackSize);

				if (var4.stackSize > var4.getMaxStackSize()) {
					var8 = EnumChatFormatting.YELLOW + ""
							+ var4.getMaxStackSize();
					var4.stackSize = var4.getMaxStackSize();
				}

				if (var4.stackSize > p_146977_1_.getSlotStackLimit()) {
					var8 = EnumChatFormatting.YELLOW + ""
							+ p_146977_1_.getSlotStackLimit();
					var4.stackSize = p_146977_1_.getSlotStackLimit();
				}
			} else {
				field_147008_s.remove(p_146977_1_);
				func_146980_g();
			}
		}

		zLevel = 100.0F;
		itemRender.zLevel = 100.0F;

		if (var4 == null) {
			final IIcon var9 = p_146977_1_.getBackgroundIconIndex();

			if (var9 != null) {
				GL11.glDisable(GL11.GL_LIGHTING);
				mc.getTextureManager().bindTexture(
						TextureMap.locationItemsTexture);
				drawTexturedModelRectFromIcon(var2, var3, var9, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				var6 = true;
			}
		}

		if (!var6) {
			if (var5) {
				drawRect(var2, var3, var2 + 16, var3 + 16, -2130706433);
			}

			GL11.glEnable(GL11.GL_DEPTH_TEST);
			itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
					mc.getTextureManager(), var4, var2, var3);
			itemRender.renderItemOverlayIntoGUI(fontRendererObj,
					mc.getTextureManager(), var4, var2, var3, var8);
		}

		itemRender.zLevel = 0.0F;
		zLevel = 0.0F;
	}

	protected boolean func_146978_c(int p_146978_1_, int p_146978_2_,
			int p_146978_3_, int p_146978_4_, int p_146978_5_, int p_146978_6_) {
		final int var7 = field_147003_i;
		final int var8 = field_147009_r;
		p_146978_5_ -= var7;
		p_146978_6_ -= var8;
		return p_146978_5_ >= p_146978_1_ - 1
				&& p_146978_5_ < p_146978_1_ + p_146978_3_ + 1
				&& p_146978_6_ >= p_146978_2_ - 1
				&& p_146978_6_ < p_146978_2_ + p_146978_4_ + 1;
	}

	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
	}

	private void func_146980_g() {
		final ItemStack var1 = mc.thePlayer.inventory.getItemStack();

		if (var1 != null && field_147007_t) {
			field_146996_I = var1.stackSize;
			ItemStack var4;
			int var5;

			for (final Iterator var2 = field_147008_s.iterator(); var2
					.hasNext(); field_146996_I -= var4.stackSize - var5) {
				final Slot var3 = (Slot) var2.next();
				var4 = var1.copy();
				var5 = var3.getStack() == null ? 0 : var3.getStack().stackSize;
				Container.func_94525_a(field_147008_s, field_146987_F, var4,
						var5);

				if (var4.stackSize > var4.getMaxStackSize()) {
					var4.stackSize = var4.getMaxStackSize();
				}

				if (var4.stackSize > var3.getSlotStackLimit()) {
					var4.stackSize = var3.getSlotStackLimit();
				}
			}
		}
	}

	private boolean func_146981_a(Slot p_146981_1_, int p_146981_2_,
			int p_146981_3_) {
		return func_146978_c(p_146981_1_.xDisplayPosition,
				p_146981_1_.yDisplayPosition, 16, 16, p_146981_2_, p_146981_3_);
	}

	private void func_146982_a(ItemStack p_146982_1_, int p_146982_2_,
			int p_146982_3_, String p_146982_4_) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		zLevel = 200.0F;
		itemRender.zLevel = 200.0F;
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
				mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_);
		itemRender.renderItemOverlayIntoGUI(fontRendererObj,
				mc.getTextureManager(), p_146982_1_, p_146982_2_, p_146982_3_
						- (field_147012_x == null ? 0 : 8), p_146982_4_);
		zLevel = 0.0F;
		itemRender.zLevel = 0.0F;
	}

	protected boolean func_146983_a(int p_146983_1_) {
		if (mc.thePlayer.inventory.getItemStack() == null
				&& field_147006_u != null) {
			for (int var2 = 0; var2 < 9; ++var2) {
				if (p_146983_1_ == mc.gameSettings.keyBindsHotbar[var2]
						.getKeyCode()) {
					func_146984_a(field_147006_u, field_147006_u.slotNumber,
							var2, 2);
					return true;
				}
			}
		}

		return false;
	}

	protected void func_146984_a(Slot p_146984_1_, int p_146984_2_,
			int p_146984_3_, int p_146984_4_) {
		if (p_146984_1_ != null) {
			p_146984_2_ = p_146984_1_.slotNumber;
		}

		mc.playerController.windowClick(field_147002_h.windowId, p_146984_2_,
				p_146984_3_, p_146984_4_, mc.thePlayer);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		mc.thePlayer.openContainer = field_147002_h;
		field_147003_i = (width - field_146999_f) / 2;
		field_147009_r = (height - field_147000_g) / 2;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1
				|| p_73869_2_ == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.thePlayer.closeScreen();
		}

		func_146983_a(p_73869_2_);

		if (field_147006_u != null && field_147006_u.getHasStack()) {
			if (p_73869_2_ == mc.gameSettings.keyBindPickBlock.getKeyCode()) {
				func_146984_a(field_147006_u, field_147006_u.slotNumber, 0, 3);
			} else if (p_73869_2_ == mc.gameSettings.keyBindDrop.getKeyCode()) {
				func_146984_a(field_147006_u, field_147006_u.slotNumber,
						isCtrlKeyDown() ? 1 : 0, 4);
			}
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	@Override
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) {
		super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
		final boolean var4 = p_73864_3_ == mc.gameSettings.keyBindPickBlock
				.getKeyCode() + 100;
		final Slot var5 = func_146975_c(p_73864_1_, p_73864_2_);
		final long var6 = Minecraft.getSystemTime();
		field_146993_M = field_146998_K == var5 && var6 - field_146997_J < 250L
				&& field_146992_L == p_73864_3_;
		field_146995_H = false;

		if (p_73864_3_ == 0 || p_73864_3_ == 1 || var4) {
			final int var8 = field_147003_i;
			final int var9 = field_147009_r;
			final boolean var10 = p_73864_1_ < var8 || p_73864_2_ < var9
					|| p_73864_1_ >= var8 + field_146999_f
					|| p_73864_2_ >= var9 + field_147000_g;
			int var11 = -1;

			if (var5 != null) {
				var11 = var5.slotNumber;
			}

			if (var10) {
				var11 = -999;
			}

			if (mc.gameSettings.touchscreen && var10
					&& mc.thePlayer.inventory.getItemStack() == null) {
				mc.displayGuiScreen((GuiScreen) null);
				return;
			}

			if (var11 != -1) {
				if (mc.gameSettings.touchscreen) {
					if (var5 != null && var5.getHasStack()) {
						field_147005_v = var5;
						field_147012_x = null;
						field_147004_w = p_73864_3_ == 1;
					} else {
						field_147005_v = null;
					}
				} else if (!field_147007_t) {
					if (mc.thePlayer.inventory.getItemStack() == null) {
						if (p_73864_3_ == mc.gameSettings.keyBindPickBlock
								.getKeyCode() + 100) {
							func_146984_a(var5, var11, p_73864_3_, 3);
						} else {
							final boolean var12 = var11 != -999
									&& (Keyboard.isKeyDown(42) || Keyboard
											.isKeyDown(54));
							byte var13 = 0;

							if (var12) {
								field_146994_N = var5 != null
										&& var5.getHasStack() ? var5.getStack()
										: null;
								var13 = 1;
							} else if (var11 == -999) {
								var13 = 4;
							}

							func_146984_a(var5, var11, p_73864_3_, var13);
						}

						field_146995_H = true;
					} else {
						field_147007_t = true;
						field_146988_G = p_73864_3_;
						field_147008_s.clear();

						if (p_73864_3_ == 0) {
							field_146987_F = 0;
						} else if (p_73864_3_ == 1) {
							field_146987_F = 1;
						}
					}
				}
			}
		}

		field_146998_K = var5;
		field_146997_J = var6;
		field_146992_L = p_73864_3_;
	}

	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_,
			int p_146273_3_, long p_146273_4_) {
		final Slot var6 = func_146975_c(p_146273_1_, p_146273_2_);
		final ItemStack var7 = mc.thePlayer.inventory.getItemStack();

		if (field_147005_v != null && mc.gameSettings.touchscreen) {
			if (p_146273_3_ == 0 || p_146273_3_ == 1) {
				if (field_147012_x == null) {
					if (var6 != field_147005_v) {
						field_147012_x = field_147005_v.getStack().copy();
					}
				} else if (field_147012_x.stackSize > 1 && var6 != null
						&& Container.func_94527_a(var6, field_147012_x, false)) {
					final long var8 = Minecraft.getSystemTime();

					if (field_146985_D == var6) {
						if (var8 - field_146986_E > 500L) {
							func_146984_a(field_147005_v,
									field_147005_v.slotNumber, 0, 0);
							func_146984_a(var6, var6.slotNumber, 1, 0);
							func_146984_a(field_147005_v,
									field_147005_v.slotNumber, 0, 0);
							field_146986_E = var8 + 750L;
							--field_147012_x.stackSize;
						}
					} else {
						field_146985_D = var6;
						field_146986_E = var8;
					}
				}
			}
		} else if (field_147007_t && var6 != null && var7 != null
				&& var7.stackSize > field_147008_s.size()
				&& Container.func_94527_a(var6, var7, true)
				&& var6.isItemValid(var7)
				&& field_147002_h.canDragIntoSlot(var6)) {
			field_147008_s.add(var6);
			func_146980_g();
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_,
			int p_146286_3_) {
		final Slot var4 = func_146975_c(p_146286_1_, p_146286_2_);
		final int var5 = field_147003_i;
		final int var6 = field_147009_r;
		final boolean var7 = p_146286_1_ < var5 || p_146286_2_ < var6
				|| p_146286_1_ >= var5 + field_146999_f
				|| p_146286_2_ >= var6 + field_147000_g;
		int var8 = -1;

		if (var4 != null) {
			var8 = var4.slotNumber;
		}

		if (var7) {
			var8 = -999;
		}

		Slot var10;
		Iterator var11;

		if (field_146993_M && var4 != null && p_146286_3_ == 0
				&& field_147002_h.func_94530_a((ItemStack) null, var4)) {
			if (isShiftKeyDown()) {
				if (var4 != null && var4.inventory != null
						&& field_146994_N != null) {
					var11 = field_147002_h.inventorySlots.iterator();

					while (var11.hasNext()) {
						var10 = (Slot) var11.next();

						if (var10 != null
								&& var10.canTakeStack(mc.thePlayer)
								&& var10.getHasStack()
								&& var10.inventory == var4.inventory
								&& Container.func_94527_a(var10,
										field_146994_N, true)) {
							func_146984_a(var10, var10.slotNumber, p_146286_3_,
									1);
						}
					}
				}
			} else {
				func_146984_a(var4, var8, p_146286_3_, 6);
			}

			field_146993_M = false;
			field_146997_J = 0L;
		} else {
			if (field_147007_t && field_146988_G != p_146286_3_) {
				field_147007_t = false;
				field_147008_s.clear();
				field_146995_H = true;
				return;
			}

			if (field_146995_H) {
				field_146995_H = false;
				return;
			}

			boolean var9;

			if (field_147005_v != null && mc.gameSettings.touchscreen) {
				if (p_146286_3_ == 0 || p_146286_3_ == 1) {
					if (field_147012_x == null && var4 != field_147005_v) {
						field_147012_x = field_147005_v.getStack();
					}

					var9 = Container.func_94527_a(var4, field_147012_x, false);

					if (var8 != -1 && field_147012_x != null && var9) {
						func_146984_a(field_147005_v,
								field_147005_v.slotNumber, p_146286_3_, 0);
						func_146984_a(var4, var8, 0, 0);

						if (mc.thePlayer.inventory.getItemStack() != null) {
							func_146984_a(field_147005_v,
									field_147005_v.slotNumber, p_146286_3_, 0);
							field_147011_y = p_146286_1_ - var5;
							field_147010_z = p_146286_2_ - var6;
							field_146989_A = field_147005_v;
							field_146991_C = field_147012_x;
							field_146990_B = Minecraft.getSystemTime();
						} else {
							field_146991_C = null;
						}
					} else if (field_147012_x != null) {
						field_147011_y = p_146286_1_ - var5;
						field_147010_z = p_146286_2_ - var6;
						field_146989_A = field_147005_v;
						field_146991_C = field_147012_x;
						field_146990_B = Minecraft.getSystemTime();
					}

					field_147012_x = null;
					field_147005_v = null;
				}
			} else if (field_147007_t && !field_147008_s.isEmpty()) {
				func_146984_a((Slot) null, -999,
						Container.func_94534_d(0, field_146987_F), 5);
				var11 = field_147008_s.iterator();

				while (var11.hasNext()) {
					var10 = (Slot) var11.next();
					func_146984_a(var10, var10.slotNumber,
							Container.func_94534_d(1, field_146987_F), 5);
				}

				func_146984_a((Slot) null, -999,
						Container.func_94534_d(2, field_146987_F), 5);
			} else if (mc.thePlayer.inventory.getItemStack() != null) {
				if (p_146286_3_ == mc.gameSettings.keyBindPickBlock
						.getKeyCode() + 100) {
					func_146984_a(var4, var8, p_146286_3_, 3);
				} else {
					var9 = var8 != -999
							&& (Keyboard.isKeyDown(42) || Keyboard
									.isKeyDown(54));

					if (var9) {
						field_146994_N = var4 != null && var4.getHasStack() ? var4
								.getStack() : null;
					}

					func_146984_a(var4, var8, p_146286_3_, var9 ? 1 : 0);
				}
			}
		}

		if (mc.thePlayer.inventory.getItemStack() == null) {
			field_146997_J = 0L;
		}

		field_147007_t = false;
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		if (mc.thePlayer != null) {
			field_147002_h.onContainerClosed(mc.thePlayer);
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead) {
			mc.thePlayer.closeScreen();
		}
	}
}

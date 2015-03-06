package net.minecraft.client.gui.achievement;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiAchievements extends GuiScreen implements IProgressMeter {
	private static final int field_146559_A = AchievementList.maxDisplayColumn * 24 - 77;
	private static final int field_146560_B = AchievementList.maxDisplayRow * 24 - 77;
	private static final ResourceLocation field_146561_C = new ResourceLocation(
			"textures/gui/achievement/achievement_background.png");
	private static final int field_146571_z = AchievementList.minDisplayRow * 24 - 112;
	private static final int field_146572_y = AchievementList.minDisplayColumn * 24 - 112;
	private int field_146554_D;
	protected int field_146555_f = 256;
	private final StatFileWriter field_146556_E;
	protected int field_146557_g = 202;
	private boolean field_146558_F = true;
	protected GuiScreen field_146562_a;
	protected int field_146563_h;
	protected int field_146564_i;
	protected double field_146565_w;
	protected double field_146566_v;
	protected double field_146567_u;
	protected double field_146568_t;
	protected double field_146569_s;
	protected float field_146570_r = 1.0F;
	protected double field_146573_x;

	public GuiAchievements(GuiScreen p_i45026_1_, StatFileWriter p_i45026_2_) {
		field_146562_a = p_i45026_1_;
		field_146556_E = p_i45026_2_;
		final short var3 = 141;
		final short var4 = 141;
		field_146569_s = field_146567_u = field_146565_w = AchievementList.openInventory.displayColumn
				* 24 - var3 / 2 - 12;
		field_146568_t = field_146566_v = field_146573_x = AchievementList.openInventory.displayRow
				* 24 - var4 / 2;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (!field_146558_F) {
			if (p_146284_1_.id == 1) {
				mc.displayGuiScreen(field_146562_a);
			}
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	@Override
	public boolean doesGuiPauseGame() {
		return !field_146558_F;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		if (field_146558_F) {
			drawDefaultBackground();
			drawCenteredString(fontRendererObj,
					I18n.format("multiplayer.downloadingStats", new Object[0]),
					width / 2, height / 2, 16777215);
			drawCenteredString(
					fontRendererObj,
					field_146510_b_[(int) (Minecraft.getSystemTime() / 150L % field_146510_b_.length)],
					width / 2, height / 2 + fontRendererObj.FONT_HEIGHT * 2,
					16777215);
		} else {
			int var4;

			if (Mouse.isButtonDown(0)) {
				var4 = (width - field_146555_f) / 2;
				final int var5 = (height - field_146557_g) / 2;
				final int var6 = var4 + 8;
				final int var7 = var5 + 17;

				if ((field_146554_D == 0 || field_146554_D == 1)
						&& p_73863_1_ >= var6 && p_73863_1_ < var6 + 224
						&& p_73863_2_ >= var7 && p_73863_2_ < var7 + 155) {
					if (field_146554_D == 0) {
						field_146554_D = 1;
					} else {
						field_146567_u -= (p_73863_1_ - field_146563_h)
								* field_146570_r;
						field_146566_v -= (p_73863_2_ - field_146564_i)
								* field_146570_r;
						field_146565_w = field_146569_s = field_146567_u;
						field_146573_x = field_146568_t = field_146566_v;
					}

					field_146563_h = p_73863_1_;
					field_146564_i = p_73863_2_;
				}
			} else {
				field_146554_D = 0;
			}

			var4 = Mouse.getDWheel();
			final float var11 = field_146570_r;

			if (var4 < 0) {
				field_146570_r += 0.25F;
			} else if (var4 > 0) {
				field_146570_r -= 0.25F;
			}

			field_146570_r = MathHelper.clamp_float(field_146570_r, 1.0F, 2.0F);

			if (field_146570_r != var11) {
				final float var12 = var11 * field_146555_f;
				final float var8 = var11 * field_146557_g;
				final float var9 = field_146570_r * field_146555_f;
				final float var10 = field_146570_r * field_146557_g;
				field_146567_u -= (var9 - var12) * 0.5F;
				field_146566_v -= (var10 - var8) * 0.5F;
				field_146565_w = field_146569_s = field_146567_u;
				field_146573_x = field_146568_t = field_146566_v;
			}

			if (field_146565_w < field_146572_y) {
				field_146565_w = field_146572_y;
			}

			if (field_146573_x < field_146571_z) {
				field_146573_x = field_146571_z;
			}

			if (field_146565_w >= field_146559_A) {
				field_146565_w = field_146559_A - 1;
			}

			if (field_146573_x >= field_146560_B) {
				field_146573_x = field_146560_B - 1;
			}

			drawDefaultBackground();
			func_146552_b(p_73863_1_, p_73863_2_, p_73863_3_);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			func_146553_h();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
	}

	@Override
	public void func_146509_g() {
		if (field_146558_F) {
			field_146558_F = false;
		}
	}

	protected void func_146552_b(int p_146552_1_, int p_146552_2_,
			float p_146552_3_) {
		int var4 = MathHelper.floor_double(field_146569_s
				+ (field_146567_u - field_146569_s) * p_146552_3_);
		int var5 = MathHelper.floor_double(field_146568_t
				+ (field_146566_v - field_146568_t) * p_146552_3_);

		if (var4 < field_146572_y) {
			var4 = field_146572_y;
		}

		if (var5 < field_146571_z) {
			var5 = field_146571_z;
		}

		if (var4 >= field_146559_A) {
			var4 = field_146559_A - 1;
		}

		if (var5 >= field_146560_B) {
			var5 = field_146560_B - 1;
		}

		final int var6 = (width - field_146555_f) / 2;
		final int var7 = (height - field_146557_g) / 2;
		final int var8 = var6 + 16;
		final int var9 = var7 + 17;
		zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_GEQUAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(var8, var9, -200.0F);
		GL11.glScalef(1.0F / field_146570_r, 1.0F / field_146570_r, 0.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		final int var10 = var4 + 288 >> 4;
		final int var11 = var5 + 288 >> 4;
		final int var12 = (var4 + 288) % 16;
		final int var13 = (var5 + 288) % 16;
		final Random var19 = new Random();
		final float var20 = 16.0F / field_146570_r;
		final float var21 = 16.0F / field_146570_r;
		int var22;
		int var24;
		int var25;

		for (var22 = 0; var22 * var20 - var13 < 155.0F; ++var22) {
			final float var23 = 0.6F - (var11 + var22) / 25.0F * 0.3F;
			GL11.glColor4f(var23, var23, var23, 1.0F);

			for (var24 = 0; var24 * var21 - var12 < 224.0F; ++var24) {
				var19.setSeed(mc.getSession().getPlayerID().hashCode() + var10
						+ var24 + (var11 + var22) * 16);
				var25 = var19.nextInt(1 + var11 + var22) + (var11 + var22) / 2;
				IIcon var26 = Blocks.sand.getIcon(0, 0);

				if (var25 <= 37 && var11 + var22 != 35) {
					if (var25 == 22) {
						if (var19.nextInt(2) == 0) {
							var26 = Blocks.diamond_ore.getIcon(0, 0);
						} else {
							var26 = Blocks.redstone_ore.getIcon(0, 0);
						}
					} else if (var25 == 10) {
						var26 = Blocks.iron_ore.getIcon(0, 0);
					} else if (var25 == 8) {
						var26 = Blocks.coal_ore.getIcon(0, 0);
					} else if (var25 > 4) {
						var26 = Blocks.stone.getIcon(0, 0);
					} else if (var25 > 0) {
						var26 = Blocks.dirt.getIcon(0, 0);
					}
				} else {
					var26 = Blocks.bedrock.getIcon(0, 0);
				}

				mc.getTextureManager().bindTexture(
						TextureMap.locationBlocksTexture);
				drawTexturedModelRectFromIcon(var24 * 16 - var12, var22 * 16
						- var13, var26, 16, 16);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		mc.getTextureManager().bindTexture(field_146561_C);
		int var30;
		int var31;
		int var39;

		for (var22 = 0; var22 < AchievementList.achievementList.size(); ++var22) {
			final Achievement var35 = (Achievement) AchievementList.achievementList
					.get(var22);

			if (var35.parentAchievement != null) {
				var24 = var35.displayColumn * 24 - var4 + 11;
				var25 = var35.displayRow * 24 - var5 + 11;
				var39 = var35.parentAchievement.displayColumn * 24 - var4 + 11;
				final int var27 = var35.parentAchievement.displayRow * 24
						- var5 + 11;
				final boolean var28 = field_146556_E
						.hasAchievementUnlocked(var35);
				final boolean var29 = field_146556_E
						.canUnlockAchievement(var35);
				var30 = field_146556_E.func_150874_c(var35);

				if (var30 <= 4) {
					var31 = -16777216;

					if (var28) {
						var31 = -6250336;
					} else if (var29) {
						var31 = -16711936;
					}

					drawHorizontalLine(var24, var39, var25, var31);
					drawVerticalLine(var39, var25, var27, var31);

					if (var24 > var39) {
						drawTexturedModalRect(var24 - 11 - 7, var25 - 5, 114,
								234, 7, 11);
					} else if (var24 < var39) {
						drawTexturedModalRect(var24 + 11, var25 - 5, 107, 234,
								7, 11);
					} else if (var25 > var27) {
						drawTexturedModalRect(var24 - 5, var25 - 11 - 7, 96,
								234, 11, 7);
					} else if (var25 < var27) {
						drawTexturedModalRect(var24 - 5, var25 + 11, 96, 241,
								11, 7);
					}
				}
			}
		}

		Achievement var34 = null;
		final RenderItem var36 = new RenderItem();
		final float var37 = (p_146552_1_ - var8) * field_146570_r;
		final float var38 = (p_146552_2_ - var9) * field_146570_r;
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		int var43;
		int var44;

		for (var39 = 0; var39 < AchievementList.achievementList.size(); ++var39) {
			final Achievement var41 = (Achievement) AchievementList.achievementList
					.get(var39);
			var43 = var41.displayColumn * 24 - var4;
			var44 = var41.displayRow * 24 - var5;

			if (var43 >= -24 && var44 >= -24
					&& var43 <= 224.0F * field_146570_r
					&& var44 <= 155.0F * field_146570_r) {
				var30 = field_146556_E.func_150874_c(var41);
				float var45;

				if (field_146556_E.hasAchievementUnlocked(var41)) {
					var45 = 0.75F;
					GL11.glColor4f(var45, var45, var45, 1.0F);
				} else if (field_146556_E.canUnlockAchievement(var41)) {
					var45 = 1.0F;
					GL11.glColor4f(var45, var45, var45, 1.0F);
				} else if (var30 < 3) {
					var45 = 0.3F;
					GL11.glColor4f(var45, var45, var45, 1.0F);
				} else if (var30 == 3) {
					var45 = 0.2F;
					GL11.glColor4f(var45, var45, var45, 1.0F);
				} else {
					if (var30 != 4) {
						continue;
					}

					var45 = 0.1F;
					GL11.glColor4f(var45, var45, var45, 1.0F);
				}

				mc.getTextureManager().bindTexture(field_146561_C);

				if (var41.getSpecial()) {
					drawTexturedModalRect(var43 - 2, var44 - 2, 26, 202, 26, 26);
				} else {
					drawTexturedModalRect(var43 - 2, var44 - 2, 0, 202, 26, 26);
				}

				if (!field_146556_E.canUnlockAchievement(var41)) {
					var45 = 0.1F;
					GL11.glColor4f(var45, var45, var45, 1.0F);
					var36.renderWithColor = false;
				}

				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_CULL_FACE);
				var36.renderItemAndEffectIntoGUI(mc.fontRenderer,
						mc.getTextureManager(), var41.theItemStack, var43 + 3,
						var44 + 3);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDisable(GL11.GL_LIGHTING);

				if (!field_146556_E.canUnlockAchievement(var41)) {
					var36.renderWithColor = true;
				}

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

				if (var37 >= var43 && var37 <= var43 + 22 && var38 >= var44
						&& var38 <= var44 + 22) {
					var34 = var41;
				}
			}
		}

		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_146561_C);
		drawTexturedModalRect(var6, var7, 0, 0, field_146555_f, field_146557_g);
		zLevel = 0.0F;
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(p_146552_1_, p_146552_2_, p_146552_3_);

		if (var34 != null) {
			String var40 = var34.func_150951_e().getUnformattedText();
			final String var42 = var34.getDescription();
			var43 = p_146552_1_ + 12;
			var44 = p_146552_2_ - 4;
			var30 = field_146556_E.func_150874_c(var34);

			if (!field_146556_E.canUnlockAchievement(var34)) {
				String var32;
				int var33;

				if (var30 == 3) {
					var40 = I18n.format("achievement.unknown", new Object[0]);
					var31 = Math
							.max(fontRendererObj.getStringWidth(var40), 120);
					var32 = new ChatComponentTranslation(
							"achievement.requires",
							new Object[] { var34.parentAchievement
									.func_150951_e() }).getUnformattedText();
					var33 = fontRendererObj.splitStringWidth(var32, var31);
					drawGradientRect(var43 - 3, var44 - 3, var43 + var31 + 3,
							var44 + var33 + 12 + 3, -1073741824, -1073741824);
					fontRendererObj.drawSplitString(var32, var43, var44 + 12,
							var31, -9416624);
				} else if (var30 < 3) {
					var31 = Math
							.max(fontRendererObj.getStringWidth(var40), 120);
					var32 = new ChatComponentTranslation(
							"achievement.requires",
							new Object[] { var34.parentAchievement
									.func_150951_e() }).getUnformattedText();
					var33 = fontRendererObj.splitStringWidth(var32, var31);
					drawGradientRect(var43 - 3, var44 - 3, var43 + var31 + 3,
							var44 + var33 + 12 + 3, -1073741824, -1073741824);
					fontRendererObj.drawSplitString(var32, var43, var44 + 12,
							var31, -9416624);
				} else {
					var40 = null;
				}
			} else {
				var31 = Math.max(fontRendererObj.getStringWidth(var40), 120);
				int var46 = fontRendererObj.splitStringWidth(var42, var31);

				if (field_146556_E.hasAchievementUnlocked(var34)) {
					var46 += 12;
				}

				drawGradientRect(var43 - 3, var44 - 3, var43 + var31 + 3, var44
						+ var46 + 3 + 12, -1073741824, -1073741824);
				fontRendererObj.drawSplitString(var42, var43, var44 + 12,
						var31, -6250336);

				if (field_146556_E.hasAchievementUnlocked(var34)) {
					fontRendererObj.drawStringWithShadow(
							I18n.format("achievement.taken", new Object[0]),
							var43, var44 + var46 + 4, -7302913);
				}
			}

			if (var40 != null) {
				fontRendererObj.drawStringWithShadow(
						var40,
						var43,
						var44,
						field_146556_E.canUnlockAchievement(var34) ? var34
								.getSpecial() ? -128 : -1
								: var34.getSpecial() ? -8355776 : -8355712);
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	protected void func_146553_h() {
		final int var1 = (width - field_146555_f) / 2;
		final int var2 = (height - field_146557_g) / 2;
		fontRendererObj.drawString(
				I18n.format("gui.achievements", new Object[0]), var1 + 15,
				var2 + 5, 4210752);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		mc.getNetHandler().addToSendQueue(
				new C16PacketClientStatus(
						C16PacketClientStatus.EnumState.REQUEST_STATS));
		buttons.clear();
		buttons.add(new GuiOptionButton(1, width / 2 + 24, height / 2 + 74, 80,
				20, I18n.format("gui.done", new Object[0])));
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == mc.gameSettings.keyBindInventory.getKeyCode()) {
			mc.displayGuiScreen((GuiScreen) null);
			mc.setIngameFocus();
		} else {
			super.keyTyped(p_73869_1_, p_73869_2_);
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		if (!field_146558_F) {
			field_146569_s = field_146567_u;
			field_146568_t = field_146566_v;
			final double var1 = field_146565_w - field_146567_u;
			final double var3 = field_146573_x - field_146566_v;

			if (var1 * var1 + var3 * var3 < 4.0D) {
				field_146567_u += var1;
				field_146566_v += var3;
			} else {
				field_146567_u += var1 * 0.85D;
				field_146566_v += var3 * 0.85D;
			}
		}
	}
}

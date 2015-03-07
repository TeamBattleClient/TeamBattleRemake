package down.TeamBattle.Modules.Modules.addons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import down.TeamBattle.TeamBattleClient;
import down.TeamBattle.Utils.RenderHelper;
import down.TeamBattle.gUI.NahrFont;
import down.TeamBattle.gUI.NahrFont.FontType;

public final class CustomTTFfont extends GuiNewChat {
	private final NahrFont font = new NahrFont("Verdana", 26);
	private final Minecraft mc;
	private int y1;

	public CustomTTFfont(Minecraft mc) {
		super(mc);
		this.mc = mc;
		x = 2;
		y = 20;
	}

	@Override
	public void func_146230_a(int p_146230_1_) {
		if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
			final int var2 = func_146232_i();
			boolean var3 = false;
			int var4 = 0;
			final int var5 = field_146253_i.size();
			final float var6 = mc.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (var5 > 0) {
				if (func_146241_e()) {
					var3 = true;
				}

				final float var7 = func_146244_h();
				MathHelper.ceiling_float_int(func_146228_f() / var7);
				GL11.glPushMatrix();
				GL11.glScalef(var7, var7, 1.0F);
				int var9;
				int var11;
				int var14;

				for (var9 = 0; var9 + field_146250_j < field_146253_i.size()
						&& var9 < var2; ++var9) {
					final ChatLine var10 = (ChatLine) field_146253_i.get(var9
							+ field_146250_j);

					if (var10 != null) {
						var11 = p_146230_1_ - var10.getUpdatedCounter();

						if (var11 < 200 || var3) {
							double var12 = var11 / 200.0D;
							var12 = 1.0D - var12;
							var12 *= 10.0D;

							if (var12 < 0.0D) {
								var12 = 0.0D;
							}

							if (var12 > 1.0D) {
								var12 = 1.0D;
							}

							var12 *= var12;
							var14 = (int) (255.0D * var12);

							if (var3) {
								var14 = 255;
							}

							var14 = (int) (var14 * var6);
							++var4;
						}
					}
				}

				final float width = func_146228_f();
				GL11.glTranslated(x, y, 0);
				y1 = -var2 * 9;
				GL11.glTranslated(0, -8, 0);
				final int color = drag ? 0x80000000 : 0x60000000;
				final int bcolor = drag ? 0x60000000 : 0x40000000;
				if (func_146241_e()) {
					final int mmm = -var2 * 9;
					RenderHelper.drawBorderedRect(0.0F, mmm - 11, width + 3.0F,
							mmm + 2.5F, 2.0F, bcolor, color);
					font.drawString("Chat", 3.0F, mmm - 13,
							NahrFont.FontType.NORMAL, 0xFFFFFFFF, 0xFF000000);
					RenderHelper.drawBorderedRect(0.0F, mmm + 4, width + 3.0F,
							9.0F, 2.0F, bcolor, color);
				} else if (var4 > 0) {
					final int mmm = -var4 * 9;
					RenderHelper.drawBorderedRect(0.0F, mmm + 4, width + 3.0F,
							9.0F, 2.0F, bcolor, color);
				}
				GL11.glLineWidth(1.0F);
				GL11.glTranslated(0, 5, 0);

				for (var9 = 0; var9 + field_146250_j < field_146253_i.size()
						&& var9 < var2; ++var9) {
					final ChatLine var10 = (ChatLine) field_146253_i.get(var9
							+ field_146250_j);

					if (var10 != null) {
						var11 = p_146230_1_ - var10.getUpdatedCounter();

						if (var11 < 200 || var3) {
							double var12 = var11 / 200.0D;
							var12 = 1.0D - var12;
							var12 *= 10.0D;

							if (var12 < 0.0D) {
								var12 = 0.0D;
							}

							if (var12 > 1.0D) {
								var12 = 1.0D;
							}

							var12 *= var12;
							var14 = (int) (255.0D * var12);

							if (var3) {
								var14 = 255;
							}

							var14 = (int) (var14 * var6);
							++var4;

							if (var14 > 0) {
								final byte var15 = 2;
								final int var16 = -var9 * 9;
								final String var17 = TeamBattleClient.getFriendManager()
										.replaceNames(
												var10.func_151461_a()
														.getFormattedText(),
												true);
								font.drawString(var17, var15, var16 - 12,
										FontType.SHADOW_THIN, 0xFFFFFFFF,
										0xFF000000);
								GL11.glDisable(GL11.GL_ALPHA_TEST);
							}
						}
					}
				}
				GL11.glTranslated(-x, -y, 0);

				if (var3) {
					var9 = mc.fontRenderer.FONT_HEIGHT;
					GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
					final int var18 = var5 * var9 + var5;
					var11 = var4 * var9 + var4;
					final int var19 = field_146250_j * var11 / var5;
					final int var13 = var11 * var11 / var18;

					if (var18 != var11) {
						var14 = var19 > 0 ? 170 : 96;
						final int var20 = field_146251_k ? 13382451 : 3355562;
						drawRect(0, -var19, 2, -var19 - var13, var20
								+ (var14 << 24));
						drawRect(2, -var19, 1, -var19 - var13,
								13421772 + (var14 << 24));
					}
				}

				GL11.glPopMatrix();
			}
		}
	}

	private boolean isMouseOverTitle(int par1, int par2) {
		final int height = mc.getScaledResolution().getScaledHeight();
		return par1 >= this.x && par2 >= this.y + this.y1 - 20 + height - 48
				&& par1 <= this.x + func_146228_f() + 3
				&& par2 <= this.y + this.y1 - 6 + height - 48;
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3) {
		if (isMouseOverTitle(par1, par2) && par3 == 0) {
			this.drag = true;
			this.dragX = this.x - par1;
			this.dragY = this.y - par2;
		}
	}

	@Override
	public void mouseMovedOrUp(int par1, int par2, int par3) {
		if (par3 == 0) {
			this.drag = false;
		}
	}

	public void prepareScissorBox(float x, float y, float x2, float y2) {
		final int factor = mc.getScaledResolution().getScaleFactor();
		GL11.glScissor((int) (x * factor), (int) ((mc.getScaledResolution()
				.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
				(int) ((y2 - y) * factor));
	}
}

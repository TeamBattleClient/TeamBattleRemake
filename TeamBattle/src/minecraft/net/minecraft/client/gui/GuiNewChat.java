package net.minecraft.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.client.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class GuiNewChat extends Gui {
	private static final Logger logger = LogManager.getLogger();

	public static int func_146233_a(float p_146233_0_) {
		final short var1 = 320;
		final byte var2 = 40;
		return MathHelper.floor_float(p_146233_0_ * (var1 - var2) + var2);
	}

	public static int func_146243_b(float p_146243_0_) {
		final short var1 = 180;
		final byte var2 = 20;
		return MathHelper.floor_float(p_146243_0_ * (var1 - var2) + var2);
	}

	protected boolean drag;
	private final Minecraft field_146247_f;
	private final List field_146248_g = new ArrayList();
	protected int field_146250_j;
	protected boolean field_146251_k;
	private final List field_146252_h = new ArrayList();

	protected final List field_146253_i = new ArrayList();

	protected int x, y, dragX, dragY;

	public GuiNewChat(Minecraft p_i1022_1_) {
		field_146247_f = p_i1022_1_;
	}

	public void drag(int x, int y) {
		this.x = x + dragX;
		this.y = y + dragY;
	}

	public void func_146227_a(IChatComponent p_146227_1_) {
		func_146234_a(p_146227_1_, 0);
	}

	public int func_146228_f() {
		return func_146233_a(field_146247_f.gameSettings.chatWidth);
	}

	public void func_146229_b(int p_146229_1_) {
		field_146250_j += p_146229_1_;
		final int var2 = field_146253_i.size();

		if (field_146250_j > var2 - func_146232_i()) {
			field_146250_j = var2 - func_146232_i();
		}

		if (field_146250_j <= 0) {
			field_146250_j = 0;
			field_146251_k = false;
		}
	}

	public void func_146230_a(int p_146230_1_) {
		if (field_146247_f.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
			final int var2 = func_146232_i();
			boolean var3 = false;
			int var4 = 0;
			final int var5 = field_146253_i.size();
			final float var6 = field_146247_f.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (var5 > 0) {
				if (func_146241_e()) {
					var3 = true;
				}

				final float var7 = func_146244_h();
				final int var8 = MathHelper.ceiling_float_int(func_146228_f()
						/ var7);
				GL11.glPushMatrix();
				GL11.glTranslatef(2.0F, 20.0F, 0.0F);
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

							if (var14 > 3) {
								final byte var15 = 2;
								final int var16 = -var9 * 9;
								final String var17 = Client.getFriendManager()
										.replaceNames(
												var10.func_151461_a()
														.getFormattedText(),
												true);
								drawRect(
										var15,
										var16 - 9,
										var15
												+ var8
												+ (var17.startsWith("\247r\2473[LM]\247f") ? 0
														: 4), var16,
										var14 / 2 << 24);
								if (var17.startsWith("\247r\2473[LM]\247f")) {
									drawRect(var15 + var8, var16 - 9, var15
											+ var8 + 4, var16, 0x8000AAAA);
								}
								field_146247_f.fontRenderer
										.drawStringWithShadow(var17, var15,
												var16 - 8,
												16777215 + (var14 << 24));
								GL11.glDisable(GL11.GL_ALPHA_TEST);
							}
						}
					}
				}

				if (var3) {
					var9 = field_146247_f.fontRenderer.FONT_HEIGHT;
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

	public void func_146231_a() {
		field_146253_i.clear();
		field_146252_h.clear();
		field_146248_g.clear();
	}

	public int func_146232_i() {
		return func_146246_g() / 9;
	}

	public void func_146234_a(IChatComponent p_146234_1_, int p_146234_2_) {
		func_146237_a(p_146234_1_, p_146234_2_,
				field_146247_f.ingameGUI.getUpdateCounter(), false);
		logger.info("[CHAT] "
				+ StringUtils.stripControlCodes(p_146234_1_
						.getUnformattedText()));
	}

	private String func_146235_b(String p_146235_1_) {
		return Minecraft.getMinecraft().gameSettings.chatColours ? p_146235_1_
				: EnumChatFormatting.getTextWithoutFormattingCodes(p_146235_1_);
	}

	public IChatComponent func_146236_a(int p_146236_1_, int p_146236_2_) {
		if (!func_146241_e())
			return null;
		else {
			final ScaledResolution var3 = new ScaledResolution(field_146247_f,
					field_146247_f.displayWidth, field_146247_f.displayHeight);
			final int var4 = var3.getScaleFactor();
			final float var5 = func_146244_h();
			int var6 = p_146236_1_ / var4 - 3;
			int var7 = p_146236_2_ / var4 - 27;
			var6 = MathHelper.floor_float(var6 / var5);
			var7 = MathHelper.floor_float(var7 / var5);

			if (var6 >= 0 && var7 >= 0) {
				final int var8 = Math.min(func_146232_i(),
						field_146253_i.size());

				if (var6 <= MathHelper.floor_float(func_146228_f()
						/ func_146244_h())
						&& var7 < field_146247_f.fontRenderer.FONT_HEIGHT
								* var8 + var8) {
					final int var9 = var7
							/ field_146247_f.fontRenderer.FONT_HEIGHT
							+ field_146250_j;

					if (var9 >= 0 && var9 < field_146253_i.size()) {
						final ChatLine var10 = (ChatLine) field_146253_i
								.get(var9);
						int var11 = 0;
						final Iterator var12 = var10.func_151461_a().iterator();

						while (var12.hasNext()) {
							final IChatComponent var13 = (IChatComponent) var12
									.next();

							if (var13 instanceof ChatComponentText) {
								var11 += field_146247_f.fontRenderer
										.getStringWidth(func_146235_b(((ChatComponentText) var13)
												.getChatComponentText_TextValue()));

								if (var11 > var6)
									return var13;
							}
						}
					}

					return null;
				} else
					return null;
			} else
				return null;
		}
	}

	private void func_146237_a(IChatComponent p_146237_1_, int p_146237_2_,
			int p_146237_3_, boolean p_146237_4_) {
		if (p_146237_2_ != 0) {
			func_146242_c(p_146237_2_);
		}

		final int var5 = MathHelper.floor_float(func_146228_f()
				/ func_146244_h());
		int var6 = 0;
		ChatComponentText var7 = new ChatComponentText("");
		final ArrayList var8 = Lists.newArrayList();
		final ArrayList var9 = Lists.newArrayList(p_146237_1_);

		for (int var10 = 0; var10 < var9.size(); ++var10) {
			final IChatComponent var11 = (IChatComponent) var9.get(var10);
			final String var12 = func_146235_b(var11.getChatStyle()
					.getFormattingCode() + var11.getUnformattedTextForChat());
			int var13 = field_146247_f.fontRenderer.getStringWidth(var12);
			ChatComponentText var14 = new ChatComponentText(var12);
			var14.setChatStyle(var11.getChatStyle().createShallowCopy());
			boolean var15 = false;

			if (var6 + var13 > var5) {
				String var16 = field_146247_f.fontRenderer.trimStringToWidth(
						var12, var5 - var6, false);
				String var17 = var16.length() < var12.length() ? var12
						.substring(var16.length()) : null;

				if (var17 != null && var17.length() > 0) {
					final int var18 = var16.lastIndexOf(" ");

					if (var18 >= 0
							&& field_146247_f.fontRenderer.getStringWidth(var12
									.substring(0, var18)) > 0) {
						var16 = var12.substring(0, var18);
						var17 = var12.substring(var18);
					}

					final ChatComponentText var19 = new ChatComponentText(var17);
					var19.setChatStyle(var11.getChatStyle().createShallowCopy());
					var9.add(var10 + 1, var19);
				}

				var13 = field_146247_f.fontRenderer.getStringWidth(var16);
				var14 = new ChatComponentText(var16);
				var14.setChatStyle(var11.getChatStyle().createShallowCopy());
				var15 = true;
			}

			if (var6 + var13 <= var5) {
				var6 += var13;
				var7.appendSibling(var14);
			} else {
				var15 = true;
			}

			if (var15) {
				var8.add(var7);
				var6 = 0;
				var7 = new ChatComponentText("");
			}
		}

		var8.add(var7);
		final boolean var20 = func_146241_e();
		IChatComponent var22;

		for (final Iterator var21 = var8.iterator(); var21.hasNext(); field_146253_i
				.add(0, new ChatLine(p_146237_3_, var22, p_146237_2_))) {
			var22 = (IChatComponent) var21.next();

			if (var20 && field_146250_j > 0) {
				field_146251_k = true;
				func_146229_b(1);
			}
		}

		while (field_146253_i.size() > 100) {
			field_146253_i.remove(field_146253_i.size() - 1);
		}

		if (!p_146237_4_) {
			field_146252_h.add(0, new ChatLine(p_146237_3_, p_146237_1_,
					p_146237_2_));

			while (field_146252_h.size() > 100) {
				field_146252_h.remove(field_146252_h.size() - 1);
			}
		}
	}

	public List func_146238_c() {
		return field_146248_g;
	}

	public void func_146239_a(String p_146239_1_) {
		if (field_146248_g.isEmpty()
				|| !((String) field_146248_g.get(field_146248_g.size() - 1))
						.equals(p_146239_1_)) {
			field_146248_g.add(p_146239_1_);
		}
	}

	public boolean func_146241_e() {
		return field_146247_f.currentScreen instanceof GuiChat;
	}

	public void func_146242_c(int p_146242_1_) {
		Iterator var2 = field_146253_i.iterator();
		ChatLine var3;

		while (var2.hasNext()) {
			var3 = (ChatLine) var2.next();

			if (var3.getChatLineID() == p_146242_1_) {
				var2.remove();
			}
		}

		var2 = field_146252_h.iterator();

		while (var2.hasNext()) {
			var3 = (ChatLine) var2.next();

			if (var3.getChatLineID() == p_146242_1_) {
				var2.remove();
				break;
			}
		}
	}

	public float func_146244_h() {
		return field_146247_f.gameSettings.chatScale;
	}

	public void func_146245_b() {
		field_146253_i.clear();
		resetScroll();

		for (int var1 = field_146252_h.size() - 1; var1 >= 0; --var1) {
			final ChatLine var2 = (ChatLine) field_146252_h.get(var1);
			func_146237_a(var2.func_151461_a(), var2.getChatLineID(),
					var2.getUpdatedCounter(), true);
		}
	}

	public int func_146246_g() {
		return func_146243_b(func_146241_e() ? field_146247_f.gameSettings.chatHeightFocused
				: field_146247_f.gameSettings.chatHeightUnfocused);
	}

	public void mouseClicked(int x, int y, int button) {

	}

	public void mouseMovedOrUp(int x, int y, int button) {

	}

	public void resetScroll() {
		field_146250_j = 0;
		field_146251_k = false;
	}
}

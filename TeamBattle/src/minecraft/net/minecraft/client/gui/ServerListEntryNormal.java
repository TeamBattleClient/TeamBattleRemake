package net.minecraft.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ServerListEntryNormal implements GuiListExtended.IGuiListEntry {
	private static final ThreadPoolExecutor field_148302_b = new ScheduledThreadPoolExecutor(
			5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d")
					.setDaemon(true).build());
	private static final Logger logger = LogManager.getLogger();
	private long field_148298_f;
	private String field_148299_g;
	private final Minecraft field_148300_d;
	private final ServerData field_148301_e;
	private final GuiMultiplayer field_148303_c;
	private DynamicTexture field_148305_h;
	private final ResourceLocation field_148306_i;

	protected ServerListEntryNormal(GuiMultiplayer p_i45048_1_,
			ServerData p_i45048_2_) {
		field_148303_c = p_i45048_1_;
		field_148301_e = p_i45048_2_;
		field_148300_d = Minecraft.getMinecraft();
		field_148306_i = new ResourceLocation("servers/" + p_i45048_2_.serverIP
				+ "/icon");
		field_148305_h = (DynamicTexture) field_148300_d.getTextureManager()
				.getTexture(field_148306_i);
	}

	@Override
	public void func_148277_b(int p_148277_1_, int p_148277_2_,
			int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
	}

	@Override
	public boolean func_148278_a(int p_148278_1_, int p_148278_2_,
			int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
		field_148303_c.func_146790_a(p_148278_1_);

		if (Minecraft.getSystemTime() - field_148298_f < 250L) {
			field_148303_c.func_146796_h();
		}

		field_148298_f = Minecraft.getSystemTime();
		return false;
	}

	@Override
	public void func_148279_a(int p_148279_1_, int p_148279_2_,
			int p_148279_3_, int p_148279_4_, int p_148279_5_,
			Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
			boolean p_148279_9_) {
		if (!field_148301_e.field_78841_f) {
			field_148301_e.field_78841_f = true;
			field_148301_e.pingToServer = -2L;
			field_148301_e.serverMOTD = "";
			field_148301_e.populationInfo = "";
			field_148302_b.submit(new Runnable() {

				@Override
				public void run() {
					try {
						field_148303_c.func_146789_i().func_147224_a(
								field_148301_e);
					} catch (final UnknownHostException var2) {
						field_148301_e.pingToServer = -1L;
						field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED
								+ "Can\'t resolve hostname";
					} catch (final Exception var3) {
						field_148301_e.pingToServer = -1L;
						field_148301_e.serverMOTD = EnumChatFormatting.DARK_RED
								+ "Can\'t connect to server.";
					}
				}
			});
		}

		final boolean var10 = field_148301_e.field_82821_f > 5;
		final boolean var11 = field_148301_e.field_82821_f < 5;
		final boolean var12 = var10 || var11;
		field_148300_d.fontRenderer.drawString(field_148301_e.serverName,
				p_148279_2_ + 32 + 3, p_148279_3_ + 1, 16777215);
		final List var13 = field_148300_d.fontRenderer
				.listFormattedStringToWidth(field_148301_e.serverMOTD,
						p_148279_4_ - 32 - 2);

		for (int var14 = 0; var14 < Math.min(var13.size(), 2); ++var14) {
			field_148300_d.fontRenderer.drawString((String) var13.get(var14),
					p_148279_2_ + 32 + 3, p_148279_3_ + 12
							+ field_148300_d.fontRenderer.FONT_HEIGHT * var14,
					8421504);
		}

		final String var22 = var12 ? EnumChatFormatting.DARK_RED
				+ field_148301_e.gameVersion : field_148301_e.populationInfo;
		final int var15 = field_148300_d.fontRenderer.getStringWidth(var22);
		field_148300_d.fontRenderer.drawString(var22, p_148279_2_ + p_148279_4_
				- var15 - 15 - 2, p_148279_3_ + 1, 8421504);
		byte var16 = 0;
		String var18 = null;
		int var17;
		String var19;

		if (var12) {
			var17 = 5;
			var19 = var10 ? "Client out of date!" : "Server out of date!";
			var18 = field_148301_e.field_147412_i;
		} else if (field_148301_e.field_78841_f
				&& field_148301_e.pingToServer != -2L) {
			if (field_148301_e.pingToServer < 0L) {
				var17 = 5;
			} else if (field_148301_e.pingToServer < 150L) {
				var17 = 0;
			} else if (field_148301_e.pingToServer < 300L) {
				var17 = 1;
			} else if (field_148301_e.pingToServer < 600L) {
				var17 = 2;
			} else if (field_148301_e.pingToServer < 1000L) {
				var17 = 3;
			} else {
				var17 = 4;
			}

			if (field_148301_e.pingToServer < 0L) {
				var19 = "(no connection)";
			} else {
				var19 = field_148301_e.pingToServer + "ms";
				var18 = field_148301_e.field_147412_i;
			}
		} else {
			var16 = 1;
			var17 = (int) (Minecraft.getSystemTime() / 100L + p_148279_1_ * 2 & 7L);

			if (var17 > 4) {
				var17 = 8 - var17;
			}

			var19 = "Pinging...";
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		field_148300_d.getTextureManager().bindTexture(Gui.icons);
		Gui.func_146110_a(p_148279_2_ + p_148279_4_ - 15, p_148279_3_,
				var16 * 10, 176 + var17 * 8, 10, 8, 256.0F, 256.0F);

		if (field_148301_e.func_147409_e() != null
				&& !field_148301_e.func_147409_e().equals(field_148299_g)) {
			field_148299_g = field_148301_e.func_147409_e();
			func_148297_b();
			field_148303_c.func_146795_p().saveServerList();
		}

		if (field_148305_h != null) {
			field_148300_d.getTextureManager().bindTexture(field_148306_i);
			Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32,
					32.0F, 32.0F);
		}

		final int var20 = p_148279_7_ - p_148279_2_;
		final int var21 = p_148279_8_ - p_148279_3_;

		if (var20 >= p_148279_4_ - 15 && var20 <= p_148279_4_ - 5 && var21 >= 0
				&& var21 <= 8) {
			field_148303_c.func_146793_a(var19);
		} else if (var20 >= p_148279_4_ - var15 - 15 - 2
				&& var20 <= p_148279_4_ - 15 - 2 && var21 >= 0 && var21 <= 8) {
			field_148303_c.func_146793_a(var18);
		}
	}

	public ServerData func_148296_a() {
		return field_148301_e;
	}

	private void func_148297_b() {
		if (field_148301_e.func_147409_e() == null) {
			field_148300_d.getTextureManager().func_147645_c(field_148306_i);
			field_148305_h = null;
		} else {
			final ByteBuf var2 = Unpooled.copiedBuffer(
					field_148301_e.func_147409_e(), Charsets.UTF_8);
			final ByteBuf var3 = Base64.decode(var2);
			BufferedImage var1;
			label74: {
				try {
					var1 = ImageIO.read(new ByteBufInputStream(var3));
					Validate.validState(var1.getWidth() == 64,
							"Must be 64 pixels wide", new Object[0]);
					Validate.validState(var1.getHeight() == 64,
							"Must be 64 pixels high", new Object[0]);
					break label74;
				} catch (final Exception var8) {
					logger.error("Invalid icon for server "
							+ field_148301_e.serverName + " ("
							+ field_148301_e.serverIP + ")", var8);
					field_148301_e.func_147407_a((String) null);
				} finally {
					var2.release();
					var3.release();
				}

				return;
			}

			if (field_148305_h == null) {
				field_148305_h = new DynamicTexture(var1.getWidth(),
						var1.getHeight());
				field_148300_d.getTextureManager().loadTexture(field_148306_i,
						field_148305_h);
			}

			var1.getRGB(0, 0, var1.getWidth(), var1.getHeight(),
					field_148305_h.getTextureData(), 0, var1.getWidth());
			field_148305_h.updateDynamicTexture();
		}
	}
}

package net.minecraft.client.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiScreenBook extends GuiScreen {
	static class NextPageButton extends GuiButton {
		private final boolean field_146151_o;

		public NextPageButton(int p_i1079_1_, int p_i1079_2_, int p_i1079_3_,
				boolean p_i1079_4_) {
			super(p_i1079_1_, p_i1079_2_, p_i1079_3_, 23, 13, "");
			field_146151_o = p_i1079_4_;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_,
				int p_146112_3_) {
			if (field_146125_m) {
				final boolean var4 = p_146112_2_ >= field_146128_h
						&& p_146112_3_ >= field_146129_i
						&& p_146112_2_ < field_146128_h + field_146120_f
						&& p_146112_3_ < field_146129_i + field_146121_g;
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				p_146112_1_.getTextureManager().bindTexture(
						GuiScreenBook.field_146466_f);
				int var5 = 0;
				int var6 = 192;

				if (var4) {
					var5 += 23;
				}

				if (!field_146151_o) {
					var6 += 13;
				}

				drawTexturedModalRect(field_146128_h, field_146129_i, var5,
						var6, 23, 13);
			}
		}
	}

	private static final ResourceLocation field_146466_f = new ResourceLocation(
			"textures/gui/book.png");
	private static final Logger logger = LogManager.getLogger();
	private GuiButton field_146465_D;
	private GuiButton field_146467_E;
	private final EntityPlayer field_146468_g;
	private GuiButton field_146469_F;
	private GuiScreenBook.NextPageButton field_146470_A;
	private GuiScreenBook.NextPageButton field_146471_B;
	private GuiButton field_146472_C;
	private final ItemStack field_146474_h;
	private final boolean field_146475_i;
	private int field_146476_w = 1;
	private final int field_146477_v = 192;
	private final int field_146478_u = 192;
	private int field_146479_t;
	private boolean field_146480_s;
	private boolean field_146481_r;
	private String field_146482_z = "";
	private NBTTagList field_146483_y;

	private int field_146484_x;

	public GuiScreenBook(EntityPlayer p_i1080_1_, ItemStack p_i1080_2_,
			boolean p_i1080_3_) {
		field_146468_g = p_i1080_1_;
		field_146474_h = p_i1080_2_;
		field_146475_i = p_i1080_3_;

		if (p_i1080_2_.hasTagCompound()) {
			final NBTTagCompound var4 = p_i1080_2_.getTagCompound();
			field_146483_y = var4.getTagList("pages", 8);

			if (field_146483_y != null) {
				field_146483_y = (NBTTagList) field_146483_y.copy();
				field_146476_w = field_146483_y.tagCount();

				if (field_146476_w < 1) {
					field_146476_w = 1;
				}
			}
		}

		if (field_146483_y == null && p_i1080_3_) {
			field_146483_y = new NBTTagList();
			field_146483_y.appendTag(new NBTTagString(""));
			field_146476_w = 1;
		}
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.enabled) {
			if (p_146284_1_.id == 0) {
				mc.displayGuiScreen((GuiScreen) null);
				func_146462_a(false);
			} else if (p_146284_1_.id == 3 && field_146475_i) {
				field_146480_s = true;
			} else if (p_146284_1_.id == 1) {
				if (field_146484_x < field_146476_w - 1) {
					++field_146484_x;
				} else if (field_146475_i) {
					func_146461_i();

					if (field_146484_x < field_146476_w - 1) {
						++field_146484_x;
					}
				}
			} else if (p_146284_1_.id == 2) {
				if (field_146484_x > 0) {
					--field_146484_x;
				}
			} else if (p_146284_1_.id == 5 && field_146480_s) {
				func_146462_a(true);
				mc.displayGuiScreen((GuiScreen) null);
			} else if (p_146284_1_.id == 4 && field_146480_s) {
				field_146480_s = false;
			}

			func_146464_h();
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_146466_f);
		final int var4 = (width - field_146478_u) / 2;
		final byte var5 = 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146478_u, field_146477_v);
		String var6;
		String var7;
		int var8;

		if (field_146480_s) {
			var6 = field_146482_z;

			if (field_146475_i) {
				if (field_146479_t / 6 % 2 == 0) {
					var6 = var6 + "" + EnumChatFormatting.BLACK + "_";
				} else {
					var6 = var6 + "" + EnumChatFormatting.GRAY + "_";
				}
			}

			var7 = I18n.format("book.editTitle", new Object[0]);
			var8 = fontRendererObj.getStringWidth(var7);
			fontRendererObj.drawString(var7, var4 + 36 + (116 - var8) / 2,
					var5 + 16 + 16, 0);
			final int var9 = fontRendererObj.getStringWidth(var6);
			fontRendererObj.drawString(var6, var4 + 36 + (116 - var9) / 2,
					var5 + 48, 0);
			final String var10 = I18n.format("book.byAuthor",
					new Object[] { field_146468_g.getCommandSenderName() });
			final int var11 = fontRendererObj.getStringWidth(var10);
			fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + var10,
					var4 + 36 + (116 - var11) / 2, var5 + 48 + 10, 0);
			final String var12 = I18n.format("book.finalizeWarning",
					new Object[0]);
			fontRendererObj
					.drawSplitString(var12, var4 + 36, var5 + 80, 116, 0);
		} else {
			var6 = I18n.format(
					"book.pageIndicator",
					new Object[] { Integer.valueOf(field_146484_x + 1),
							Integer.valueOf(field_146476_w) });
			var7 = "";

			if (field_146483_y != null && field_146484_x >= 0
					&& field_146484_x < field_146483_y.tagCount()) {
				var7 = field_146483_y.getStringTagAt(field_146484_x);
			}

			if (field_146475_i) {
				if (fontRendererObj.getBidiFlag()) {
					var7 = var7 + "_";
				} else if (field_146479_t / 6 % 2 == 0) {
					var7 = var7 + "" + EnumChatFormatting.BLACK + "_";
				} else {
					var7 = var7 + "" + EnumChatFormatting.GRAY + "_";
				}
			}

			var8 = fontRendererObj.getStringWidth(var6);
			fontRendererObj.drawString(var6, var4 - var8 + field_146478_u - 44,
					var5 + 16, 0);
			fontRendererObj.drawSplitString(var7, var4 + 36, var5 + 16 + 16,
					116, 0);
		}

		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	private String func_146456_p() {
		return field_146483_y != null && field_146484_x >= 0
				&& field_146484_x < field_146483_y.tagCount() ? field_146483_y
				.getStringTagAt(field_146484_x) : "";
	}

	private void func_146457_a(String p_146457_1_) {
		if (field_146483_y != null && field_146484_x >= 0
				&& field_146484_x < field_146483_y.tagCount()) {
			field_146483_y.func_150304_a(field_146484_x, new NBTTagString(
					p_146457_1_));
			field_146481_r = true;
		}
	}

	private void func_146459_b(String p_146459_1_) {
		final String var2 = func_146456_p();
		final String var3 = var2 + p_146459_1_;
		final int var4 = fontRendererObj.splitStringWidth(var3 + ""
				+ EnumChatFormatting.BLACK + "_", 118);

		if (var4 <= 118 && var3.length() < 256) {
			func_146457_a(var3);
		}
	}

	private void func_146460_c(char p_146460_1_, int p_146460_2_) {
		switch (p_146460_2_) {
		case 14:
			if (!field_146482_z.isEmpty()) {
				field_146482_z = field_146482_z.substring(0,
						field_146482_z.length() - 1);
				func_146464_h();
			}

			return;

		case 28:
		case 156:
			if (!field_146482_z.isEmpty()) {
				func_146462_a(true);
				mc.displayGuiScreen((GuiScreen) null);
			}

			return;

		default:
			if (field_146482_z.length() < 16
					&& ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
				field_146482_z = field_146482_z
						+ Character.toString(p_146460_1_);
				func_146464_h();
				field_146481_r = true;
			}
		}
	}

	private void func_146461_i() {
		if (field_146483_y != null && field_146483_y.tagCount() < 50) {
			field_146483_y.appendTag(new NBTTagString(""));
			++field_146476_w;
			field_146481_r = true;
		}
	}

	private void func_146462_a(boolean p_146462_1_) {
		if (field_146475_i && field_146481_r) {
			if (field_146483_y != null) {
				String var2;

				while (field_146483_y.tagCount() > 1) {
					var2 = field_146483_y.getStringTagAt(field_146483_y
							.tagCount() - 1);

					if (var2.length() != 0) {
						break;
					}

					field_146483_y.removeTag(field_146483_y.tagCount() - 1);
				}

				if (field_146474_h.hasTagCompound()) {
					final NBTTagCompound var10 = field_146474_h
							.getTagCompound();
					var10.setTag("pages", field_146483_y);
				} else {
					field_146474_h.setTagInfo("pages", field_146483_y);
				}

				var2 = "MC|BEdit";

				if (p_146462_1_) {
					var2 = "MC|BSign";
					field_146474_h.setTagInfo("author", new NBTTagString(
							field_146468_g.getCommandSenderName()));
					field_146474_h.setTagInfo("title", new NBTTagString(
							field_146482_z.trim()));
					field_146474_h.func_150996_a(Items.written_book);
				}

				final ByteBuf var3 = Unpooled.buffer();

				try {
					new PacketBuffer(var3)
							.writeItemStackToBuffer(field_146474_h);
					mc.getNetHandler().addToSendQueue(
							new C17PacketCustomPayload(var2, var3));
				} catch (final Exception var8) {
					logger.error("Couldn\'t send book info", var8);
				} finally {
					var3.release();
				}
			}
		}
	}

	private void func_146463_b(char p_146463_1_, int p_146463_2_) {
		switch (p_146463_1_) {
		case 22:
			func_146459_b(GuiScreen.getClipboardString());
			return;

		default:
			switch (p_146463_2_) {
			case 14:
				final String var3 = func_146456_p();

				if (var3.length() > 0) {
					func_146457_a(var3.substring(0, var3.length() - 1));
				}

				return;

			case 28:
			case 156:
				func_146459_b("\n");
				return;

			default:
				if (ChatAllowedCharacters.isAllowedCharacter(p_146463_1_)) {
					func_146459_b(Character.toString(p_146463_1_));
				}
			}
		}
	}

	private void func_146464_h() {
		field_146470_A.field_146125_m = !field_146480_s
				&& (field_146484_x < field_146476_w - 1 || field_146475_i);
		field_146471_B.field_146125_m = !field_146480_s && field_146484_x > 0;
		field_146472_C.field_146125_m = !field_146475_i || !field_146480_s;

		if (field_146475_i) {
			field_146465_D.field_146125_m = !field_146480_s;
			field_146469_F.field_146125_m = field_146480_s;
			field_146467_E.field_146125_m = field_146480_s;
			field_146467_E.enabled = field_146482_z.trim().length() > 0;
		}
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		Keyboard.enableRepeatEvents(true);

		if (field_146475_i) {
			buttons.add(field_146465_D = new GuiButton(3, width / 2 - 100,
					4 + field_146477_v, 98, 20, I18n.format("book.signButton",
							new Object[0])));
			buttons.add(field_146472_C = new GuiButton(0, width / 2 + 2,
					4 + field_146477_v, 98, 20, I18n.format("gui.done",
							new Object[0])));
			buttons.add(field_146467_E = new GuiButton(5, width / 2 - 100,
					4 + field_146477_v, 98, 20, I18n.format(
							"book.finalizeButton", new Object[0])));
			buttons.add(field_146469_F = new GuiButton(4, width / 2 + 2,
					4 + field_146477_v, 98, 20, I18n.format("gui.cancel",
							new Object[0])));
		} else {
			buttons.add(field_146472_C = new GuiButton(0, width / 2 - 100,
					4 + field_146477_v, 200, 20, I18n.format("gui.done",
							new Object[0])));
		}

		final int var1 = (width - field_146478_u) / 2;
		final byte var2 = 2;
		buttons.add(field_146470_A = new GuiScreenBook.NextPageButton(1,
				var1 + 120, var2 + 154, true));
		buttons.add(field_146471_B = new GuiScreenBook.NextPageButton(2,
				var1 + 38, var2 + 154, false));
		func_146464_h();
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		super.keyTyped(p_73869_1_, p_73869_2_);

		if (field_146475_i) {
			if (field_146480_s) {
				func_146460_c(p_73869_1_, p_73869_2_);
			} else {
				func_146463_b(p_73869_1_, p_73869_2_);
			}
		}
	}

	/**
	 * "Called when the screen is unloaded. Used to disable keyboard repeat events."
	 */
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();
		++field_146479_t;
	}
}

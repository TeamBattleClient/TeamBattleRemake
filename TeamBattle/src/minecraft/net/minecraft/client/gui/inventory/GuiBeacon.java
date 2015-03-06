package net.minecraft.client.gui.inventory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class GuiBeacon extends GuiContainer {
	static class Button extends GuiButton {
		private boolean field_146142_r;
		private final int field_146143_q;
		private final int field_146144_p;
		private final ResourceLocation field_146145_o;

		protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_,
				ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_) {
			super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
			field_146145_o = p_i1077_4_;
			field_146144_p = p_i1077_5_;
			field_146143_q = p_i1077_6_;
		}

		@Override
		public void drawButton(Minecraft p_146112_1_, int p_146112_2_,
				int p_146112_3_) {
			if (field_146125_m) {
				p_146112_1_.getTextureManager().bindTexture(
						GuiBeacon.field_147025_v);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				field_146123_n = p_146112_2_ >= field_146128_h
						&& p_146112_3_ >= field_146129_i
						&& p_146112_2_ < field_146128_h + field_146120_f
						&& p_146112_3_ < field_146129_i + field_146121_g;
				final short var4 = 219;
				int var5 = 0;

				if (!enabled) {
					var5 += field_146120_f * 2;
				} else if (field_146142_r) {
					var5 += field_146120_f * 1;
				} else if (field_146123_n) {
					var5 += field_146120_f * 3;
				}

				drawTexturedModalRect(field_146128_h, field_146129_i, var5,
						var4, field_146120_f, field_146121_g);

				if (!GuiBeacon.field_147025_v.equals(field_146145_o)) {
					p_146112_1_.getTextureManager().bindTexture(field_146145_o);
				}

				drawTexturedModalRect(field_146128_h + 2, field_146129_i + 2,
						field_146144_p, field_146143_q, 18, 18);
			}
		}

		public void func_146140_b(boolean p_146140_1_) {
			field_146142_r = p_146140_1_;
		}

		public boolean func_146141_c() {
			return field_146142_r;
		}
	}

	class CancelButton extends GuiBeacon.Button {

		public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_) {
			super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.field_147025_v,
					112, 220);
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			func_146279_a(I18n.format("gui.cancel", new Object[0]),
					p_146111_1_, p_146111_2_);
		}
	}

	class ConfirmButton extends GuiBeacon.Button {

		public ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_) {
			super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.field_147025_v,
					90, 220);
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			func_146279_a(I18n.format("gui.done", new Object[0]), p_146111_1_,
					p_146111_2_);
		}
	}

	class PowerButton extends GuiBeacon.Button {
		private final int field_146148_q;
		private final int field_146149_p;

		public PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_,
				int p_i1076_5_, int p_i1076_6_) {
			super(
					p_i1076_2_,
					p_i1076_3_,
					p_i1076_4_,
					GuiContainer.field_147001_a,
					0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18,
					198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
			field_146149_p = p_i1076_5_;
			field_146148_q = p_i1076_6_;
		}

		@Override
		public void func_146111_b(int p_146111_1_, int p_146111_2_) {
			String var3 = I18n
					.format(Potion.potionTypes[field_146149_p].getName(),
							new Object[0]);

			if (field_146148_q >= 3 && field_146149_p != Potion.regeneration.id) {
				var3 = var3 + " II";
			}

			func_146279_a(var3, p_146111_1_, p_146111_2_);
		}
	}

	private static final ResourceLocation field_147025_v = new ResourceLocation(
			"textures/gui/container/beacon.png");

	private static final Logger logger = LogManager.getLogger();

	private final TileEntityBeacon field_147024_w;

	private boolean field_147027_y;

	private GuiBeacon.ConfirmButton field_147028_x;

	public GuiBeacon(InventoryPlayer p_i1078_1_, TileEntityBeacon p_i1078_2_) {
		super(new ContainerBeacon(p_i1078_1_, p_i1078_2_));
		field_147024_w = p_i1078_2_;
		field_146999_f = 230;
		field_147000_g = 219;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == -2) {
			mc.displayGuiScreen((GuiScreen) null);
		} else if (p_146284_1_.id == -1) {
			final String var2 = "MC|Beacon";
			final ByteBuf var3 = Unpooled.buffer();

			try {
				var3.writeInt(field_147024_w.func_146007_j());
				var3.writeInt(field_147024_w.func_146006_k());
				mc.getNetHandler().addToSendQueue(
						new C17PacketCustomPayload(var2, var3));
			} catch (final Exception var8) {
				logger.error("Couldn\'t send beacon info", var8);
			} finally {
				var3.release();
			}

			mc.displayGuiScreen((GuiScreen) null);
		} else if (p_146284_1_ instanceof GuiBeacon.PowerButton) {
			if (((GuiBeacon.PowerButton) p_146284_1_).func_146141_c())
				return;

			final int var10 = p_146284_1_.id;
			final int var11 = var10 & 255;
			final int var4 = var10 >> 8;

			if (var4 < 3) {
				field_147024_w.func_146001_d(var11);
			} else {
				field_147024_w.func_146004_e(var11);
			}

			buttons.clear();
			initGui();
			updateScreen();
		}
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147025_v);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);
		itemRender.zLevel = 100.0F;
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
				mc.getTextureManager(), new ItemStack(Items.emerald),
				var4 + 42, var5 + 109);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
				mc.getTextureManager(), new ItemStack(Items.diamond),
				var4 + 42 + 22, var5 + 109);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
				mc.getTextureManager(), new ItemStack(Items.gold_ingot),
				var4 + 42 + 44, var5 + 109);
		itemRender.renderItemAndEffectIntoGUI(fontRendererObj,
				mc.getTextureManager(), new ItemStack(Items.iron_ingot),
				var4 + 42 + 66, var5 + 109);
		itemRender.zLevel = 0.0F;
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		RenderHelper.disableStandardItemLighting();
		drawCenteredString(fontRendererObj,
				I18n.format("tile.beacon.primary", new Object[0]), 62, 10,
				14737632);
		drawCenteredString(fontRendererObj,
				I18n.format("tile.beacon.secondary", new Object[0]), 169, 10,
				14737632);
		final Iterator var3 = buttons.iterator();

		while (var3.hasNext()) {
			final GuiButton var4 = (GuiButton) var3.next();

			if (var4.func_146115_a()) {
				var4.func_146111_b(p_146979_1_ - field_147003_i, p_146979_2_
						- field_147009_r);
				break;
			}
		}

		RenderHelper.enableGUIStandardItemLighting();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		buttons.add(field_147028_x = new GuiBeacon.ConfirmButton(-1,
				field_147003_i + 164, field_147009_r + 107));
		buttons.add(new GuiBeacon.CancelButton(-2, field_147003_i + 190,
				field_147009_r + 107));
		field_147027_y = true;
		field_147028_x.enabled = false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		super.updateScreen();

		if (field_147027_y && field_147024_w.func_145998_l() >= 0) {
			field_147027_y = false;
			int var2;
			int var3;
			int var4;
			int var5;
			GuiBeacon.PowerButton var6;

			for (int var1 = 0; var1 <= 2; ++var1) {
				var2 = TileEntityBeacon.field_146009_a[var1].length;
				var3 = var2 * 22 + (var2 - 1) * 2;

				for (var4 = 0; var4 < var2; ++var4) {
					var5 = TileEntityBeacon.field_146009_a[var1][var4].id;
					var6 = new GuiBeacon.PowerButton(var1 << 8 | var5,
							field_147003_i + 76 + var4 * 24 - var3 / 2,
							field_147009_r + 22 + var1 * 25, var5, var1);
					buttons.add(var6);

					if (var1 >= field_147024_w.func_145998_l()) {
						var6.enabled = false;
					} else if (var5 == field_147024_w.func_146007_j()) {
						var6.func_146140_b(true);
					}
				}
			}

			final byte var7 = 3;
			var2 = TileEntityBeacon.field_146009_a[var7].length + 1;
			var3 = var2 * 22 + (var2 - 1) * 2;

			for (var4 = 0; var4 < var2 - 1; ++var4) {
				var5 = TileEntityBeacon.field_146009_a[var7][var4].id;
				var6 = new GuiBeacon.PowerButton(var7 << 8 | var5,
						field_147003_i + 167 + var4 * 24 - var3 / 2,
						field_147009_r + 47, var5, var7);
				buttons.add(var6);

				if (var7 >= field_147024_w.func_145998_l()) {
					var6.enabled = false;
				} else if (var5 == field_147024_w.func_146006_k()) {
					var6.func_146140_b(true);
				}
			}

			if (field_147024_w.func_146007_j() > 0) {
				final GuiBeacon.PowerButton var8 = new GuiBeacon.PowerButton(
						var7 << 8 | field_147024_w.func_146007_j(),
						field_147003_i + 167 + (var2 - 1) * 24 - var3 / 2,
						field_147009_r + 47, field_147024_w.func_146007_j(),
						var7);
				buttons.add(var8);

				if (var7 >= field_147024_w.func_145998_l()) {
					var8.enabled = false;
				} else if (field_147024_w.func_146007_j() == field_147024_w
						.func_146006_k()) {
					var8.func_146140_b(true);
				}
			}
		}

		field_147028_x.enabled = field_147024_w.getStackInSlot(0) != null
				&& field_147024_w.func_146007_j() > 0;
	}
}

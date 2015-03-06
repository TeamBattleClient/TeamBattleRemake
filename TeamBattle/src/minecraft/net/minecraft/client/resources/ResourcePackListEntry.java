package net.minecraft.client.resources;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public abstract class ResourcePackListEntry implements
		GuiListExtended.IGuiListEntry {
	private static final ResourceLocation field_148316_c = new ResourceLocation(
			"textures/gui/resource_packs.png");
	protected final GuiScreenResourcePacks field_148315_b;
	protected final Minecraft field_148317_a;

	public ResourcePackListEntry(GuiScreenResourcePacks p_i45051_1_) {
		field_148315_b = p_i45051_1_;
		field_148317_a = Minecraft.getMinecraft();
	}

	@Override
	public void func_148277_b(int p_148277_1_, int p_148277_2_,
			int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
	}

	@Override
	public boolean func_148278_a(int p_148278_1_, int p_148278_2_,
			int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
		if (func_148310_d() && p_148278_5_ <= 32) {
			if (func_148309_e()) {
				field_148315_b.func_146962_b(this).remove(this);
				field_148315_b.func_146963_h().add(0, this);
				return true;
			}

			if (p_148278_5_ < 16 && func_148308_f()) {
				field_148315_b.func_146962_b(this).remove(this);
				field_148315_b.func_146964_g().add(0, this);
				return true;
			}

			List var7;
			int var8;

			if (p_148278_5_ > 16 && p_148278_6_ < 16 && func_148314_g()) {
				var7 = field_148315_b.func_146962_b(this);
				var8 = var7.indexOf(this);
				var7.remove(this);
				var7.add(var8 - 1, this);
				return true;
			}

			if (p_148278_5_ > 16 && p_148278_6_ > 16 && func_148307_h()) {
				var7 = field_148315_b.func_146962_b(this);
				var8 = var7.indexOf(this);
				var7.remove(this);
				var7.add(var8 + 1, this);
				return true;
			}
		}

		return false;
	}

	@Override
	public void func_148279_a(int p_148279_1_, int p_148279_2_,
			int p_148279_3_, int p_148279_4_, int p_148279_5_,
			Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_,
			boolean p_148279_9_) {
		func_148313_c();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32, 32, 32.0F,
				32.0F);
		int var11;

		if ((field_148317_a.gameSettings.touchscreen || p_148279_9_)
				&& func_148310_d()) {
			field_148317_a.getTextureManager().bindTexture(field_148316_c);
			Gui.drawRect(p_148279_2_, p_148279_3_, p_148279_2_ + 32,
					p_148279_3_ + 32, -1601138544);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			final int var10 = p_148279_7_ - p_148279_2_;
			var11 = p_148279_8_ - p_148279_3_;

			if (func_148309_e()) {
				if (var10 < 32) {
					Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 32.0F,
							32, 32, 256.0F, 256.0F);
				} else {
					Gui.func_146110_a(p_148279_2_, p_148279_3_, 0.0F, 0.0F, 32,
							32, 256.0F, 256.0F);
				}
			} else {
				if (func_148308_f()) {
					if (var10 < 16) {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 32.0F,
								32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 32.0F,
								0.0F, 32, 32, 256.0F, 256.0F);
					}
				}

				if (func_148314_g()) {
					if (var10 < 32 && var10 > 16 && var11 < 16) {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 96.0F,
								32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 96.0F,
								0.0F, 32, 32, 256.0F, 256.0F);
					}
				}

				if (func_148307_h()) {
					if (var10 < 32 && var10 > 16 && var11 > 16) {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 64.0F,
								32.0F, 32, 32, 256.0F, 256.0F);
					} else {
						Gui.func_146110_a(p_148279_2_, p_148279_3_, 64.0F,
								0.0F, 32, 32, 256.0F, 256.0F);
					}
				}
			}
		}

		String var14 = func_148312_b();
		var11 = field_148317_a.fontRenderer.getStringWidth(var14);

		if (var11 > 157) {
			var14 = field_148317_a.fontRenderer.trimStringToWidth(var14,
					157 - field_148317_a.fontRenderer.getStringWidth("..."))
					+ "...";
		}

		field_148317_a.fontRenderer.drawStringWithShadow(var14,
				p_148279_2_ + 32 + 2, p_148279_3_ + 1, 16777215);
		final List var12 = field_148317_a.fontRenderer
				.listFormattedStringToWidth(func_148311_a(), 157);

		for (int var13 = 0; var13 < 2 && var13 < var12.size(); ++var13) {
			field_148317_a.fontRenderer.drawStringWithShadow(
					(String) var12.get(var13), p_148279_2_ + 32 + 2,
					p_148279_3_ + 12 + 10 * var13, 8421504);
		}
	}

	protected boolean func_148307_h() {
		final List var1 = field_148315_b.func_146962_b(this);
		final int var2 = var1.indexOf(this);
		return var2 >= 0 && var2 < var1.size() - 1
				&& ((ResourcePackListEntry) var1.get(var2 + 1)).func_148310_d();
	}

	protected boolean func_148308_f() {
		return field_148315_b.func_146961_a(this);
	}

	protected boolean func_148309_e() {
		return !field_148315_b.func_146961_a(this);
	}

	protected boolean func_148310_d() {
		return true;
	}

	protected abstract String func_148311_a();

	protected abstract String func_148312_b();

	protected abstract void func_148313_c();

	protected boolean func_148314_g() {
		final List var1 = field_148315_b.func_146962_b(this);
		final int var2 = var1.indexOf(this);
		return var2 > 0
				&& ((ResourcePackListEntry) var1.get(var2 - 1)).func_148310_d();
	}
}

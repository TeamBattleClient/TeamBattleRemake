package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiBrewingStand extends GuiContainer {
	private static final ResourceLocation field_147014_u = new ResourceLocation(
			"textures/gui/container/brewing_stand.png");
	private final TileEntityBrewingStand field_147013_v;

	public GuiBrewingStand(InventoryPlayer p_i1081_1_,
			TileEntityBrewingStand p_i1081_2_) {
		super(new ContainerBrewingStand(p_i1081_1_, p_i1081_2_));
		field_147013_v = p_i1081_2_;
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147014_u);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);
		final int var6 = field_147013_v.func_145935_i();

		if (var6 > 0) {
			int var7 = (int) (28.0F * (1.0F - var6 / 400.0F));

			if (var7 > 0) {
				drawTexturedModalRect(var4 + 97, var5 + 16, 176, 0, 9, var7);
			}

			final int var8 = var6 / 2 % 7;

			switch (var8) {
			case 0:
				var7 = 29;
				break;

			case 1:
				var7 = 24;
				break;

			case 2:
				var7 = 20;
				break;

			case 3:
				var7 = 16;
				break;

			case 4:
				var7 = 11;
				break;

			case 5:
				var7 = 6;
				break;

			case 6:
				var7 = 0;
			}

			if (var7 > 0) {
				drawTexturedModalRect(var4 + 65, var5 + 14 + 29 - var7, 185,
						29 - var7, 12, var7);
			}
		}
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		final String var3 = field_147013_v.isInventoryNameLocalized() ? field_147013_v
				.getInventoryName() : I18n.format(
				field_147013_v.getInventoryName(), new Object[0]);
		fontRendererObj.drawString(var3,
				field_146999_f / 2 - fontRendererObj.getStringWidth(var3) / 2,
				6, 4210752);
		fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				field_147000_g - 96 + 2, 4210752);
	}
}

package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiDispenser extends GuiContainer {
	private static final ResourceLocation field_147088_v = new ResourceLocation(
			"textures/gui/container/dispenser.png");
	public TileEntityDispenser field_147089_u;

	public GuiDispenser(InventoryPlayer p_i1098_1_,
			TileEntityDispenser p_i1098_2_) {
		super(new ContainerDispenser(p_i1098_1_, p_i1098_2_));
		field_147089_u = p_i1098_2_;
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147088_v);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		final String var3 = field_147089_u.isInventoryNameLocalized() ? field_147089_u
				.getInventoryName() : I18n.format(
				field_147089_u.getInventoryName(), new Object[0]);
		fontRendererObj.drawString(var3,
				field_146999_f / 2 - fontRendererObj.getStringWidth(var3) / 2,
				6, 4210752);
		fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				field_147000_g - 96 + 2, 4210752);
	}
}

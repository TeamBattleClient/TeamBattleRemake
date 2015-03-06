package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiCrafting extends GuiContainer {
	private static final ResourceLocation field_147019_u = new ResourceLocation(
			"textures/gui/container/crafting_table.png");

	public GuiCrafting(InventoryPlayer p_i1084_1_, World p_i1084_2_,
			int p_i1084_3_, int p_i1084_4_, int p_i1084_5_) {
		super(new ContainerWorkbench(p_i1084_1_, p_i1084_2_, p_i1084_3_,
				p_i1084_4_, p_i1084_5_));
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147019_u);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(
				I18n.format("container.crafting", new Object[0]), 28, 6,
				4210752);
		fontRendererObj.drawString(
				I18n.format("container.inventory", new Object[0]), 8,
				field_147000_g - 96 + 2, 4210752);
	}
}

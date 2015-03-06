package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiChest extends GuiContainer {
	private static final ResourceLocation field_147017_u = new ResourceLocation(
			"textures/gui/container/generic_54.png");
	private final IInventory field_147015_w;
	private final IInventory field_147016_v;
	private final int field_147018_x;

	public GuiChest(IInventory p_i1083_1_, IInventory p_i1083_2_) {
		super(new ContainerChest(p_i1083_1_, p_i1083_2_));
		field_147016_v = p_i1083_1_;
		field_147015_w = p_i1083_2_;
		field_146291_p = false;
		final short var3 = 222;
		final int var4 = var3 - 108;
		field_147018_x = p_i1083_2_.getSizeInventory() / 9;
		field_147000_g = var4 + field_147018_x * 18;
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147017_u);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f,
				field_147018_x * 18 + 17);
		drawTexturedModalRect(var4, var5 + field_147018_x * 18 + 17, 0, 126,
				field_146999_f, 96);
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(
				field_147015_w.isInventoryNameLocalized() ? field_147015_w
						.getInventoryName() : I18n.format(
						field_147015_w.getInventoryName(), new Object[0]), 8,
				6, 4210752);
		fontRendererObj.drawString(
				field_147016_v.isInventoryNameLocalized() ? field_147016_v
						.getInventoryName() : I18n.format(
						field_147016_v.getInventoryName(), new Object[0]), 8,
				field_147000_g - 96 + 2, 4210752);
	}
}

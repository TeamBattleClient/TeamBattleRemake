package net.minecraft.client.gui.inventory;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiScreenHorseInventory extends GuiContainer {
	private static final ResourceLocation field_147031_u = new ResourceLocation(
			"textures/gui/container/horse.png");
	private final IInventory field_147029_w;
	private final IInventory field_147030_v;
	private float field_147032_z;
	private float field_147033_y;
	private final EntityHorse field_147034_x;

	public GuiScreenHorseInventory(IInventory p_i1093_1_,
			IInventory p_i1093_2_, EntityHorse p_i1093_3_) {
		super(new ContainerHorseInventory(p_i1093_1_, p_i1093_2_, p_i1093_3_));
		field_147030_v = p_i1093_1_;
		field_147029_w = p_i1093_2_;
		field_147034_x = p_i1093_3_;
		field_146291_p = false;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		field_147033_y = p_73863_1_;
		field_147032_z = p_73863_2_;
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147031_u);
		final int var4 = (width - field_146999_f) / 2;
		final int var5 = (height - field_147000_g) / 2;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);

		if (field_147034_x.isChested()) {
			drawTexturedModalRect(var4 + 79, var5 + 17, 0, field_147000_g, 90,
					54);
		}

		if (field_147034_x.func_110259_cr()) {
			drawTexturedModalRect(var4 + 7, var5 + 35, 0, field_147000_g + 54,
					18, 18);
		}

		GuiInventory.func_147046_a(var4 + 51, var5 + 60, 17, var4 + 51
				- field_147033_y, var5 + 75 - 50 - field_147032_z,
				field_147034_x);
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(
				field_147029_w.isInventoryNameLocalized() ? field_147029_w
						.getInventoryName() : I18n.format(
						field_147029_w.getInventoryName(), new Object[0]), 8,
				6, 4210752);
		fontRendererObj.drawString(
				field_147030_v.isInventoryNameLocalized() ? field_147030_v
						.getInventoryName() : I18n.format(
						field_147030_v.getInventoryName(), new Object[0]), 8,
				field_147000_g - 96 + 2, 4210752);
	}
}

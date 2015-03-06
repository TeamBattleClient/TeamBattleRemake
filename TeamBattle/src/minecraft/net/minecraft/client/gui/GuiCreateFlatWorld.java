package net.minecraft.client.gui;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiCreateFlatWorld extends GuiScreen {
	class Details extends GuiSlot {
		public int field_148228_k = -1;

		public Details() {
			super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width,
					GuiCreateFlatWorld.this.height, 43,
					GuiCreateFlatWorld.this.height - 60, 24);
		}

		@Override
		protected void drawBackground() {
		}

		@Override
		protected void drawSlot(int p_148126_1_, int p_148126_2_,
				int p_148126_3_, int p_148126_4_, Tessellator p_148126_5_,
				int p_148126_6_, int p_148126_7_) {
			final FlatLayerInfo var8 = (FlatLayerInfo) field_146387_g
					.getFlatLayers().get(
							field_146387_g.getFlatLayers().size() - p_148126_1_
									- 1);
			final Item var9 = Item.getItemFromBlock(var8.func_151536_b());
			final ItemStack var10 = var8.func_151536_b() == Blocks.air ? null
					: new ItemStack(var9, 1, var8.getFillBlockMeta());
			final String var11 = var10 != null && var9 != null ? var9
					.getItemStackDisplayName(var10) : "Air";
			func_148225_a(p_148126_2_, p_148126_3_, var10);
			GuiCreateFlatWorld.this.fontRendererObj.drawString(var11,
					p_148126_2_ + 18 + 5, p_148126_3_ + 3, 16777215);
			String var12;

			if (p_148126_1_ == 0) {
				var12 = I18n.format("createWorld.customize.flat.layer.top",
						new Object[] { Integer.valueOf(var8.getLayerCount()) });
			} else if (p_148126_1_ == field_146387_g.getFlatLayers().size() - 1) {
				var12 = I18n.format("createWorld.customize.flat.layer.bottom",
						new Object[] { Integer.valueOf(var8.getLayerCount()) });
			} else {
				var12 = I18n.format("createWorld.customize.flat.layer",
						new Object[] { Integer.valueOf(var8.getLayerCount()) });
			}

			GuiCreateFlatWorld.this.fontRendererObj.drawString(
					var12,
					p_148126_2_
							+ 2
							+ 213
							- GuiCreateFlatWorld.this.fontRendererObj
									.getStringWidth(var12), p_148126_3_ + 3,
					16777215);
		}

		@Override
		protected void elementClicked(int p_148144_1_, boolean p_148144_2_,
				int p_148144_3_, int p_148144_4_) {
			field_148228_k = p_148144_1_;
			func_146375_g();
		}

		@Override
		protected int func_148137_d() {
			return field_148155_a - 70;
		}

		private void func_148224_c(int p_148224_1_, int p_148224_2_,
				int p_148224_3_, int p_148224_4_) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GuiCreateFlatWorld.this.mc.getTextureManager().bindTexture(
					Gui.statIcons);
			final Tessellator var9 = Tessellator.instance;
			var9.startDrawingQuads();
			var9.addVertexWithUV(p_148224_1_ + 0, p_148224_2_ + 18,
					GuiCreateFlatWorld.this.zLevel,
					(p_148224_3_ + 0) * 0.0078125F,
					(p_148224_4_ + 18) * 0.0078125F);
			var9.addVertexWithUV(p_148224_1_ + 18, p_148224_2_ + 18,
					GuiCreateFlatWorld.this.zLevel,
					(p_148224_3_ + 18) * 0.0078125F,
					(p_148224_4_ + 18) * 0.0078125F);
			var9.addVertexWithUV(p_148224_1_ + 18, p_148224_2_ + 0,
					GuiCreateFlatWorld.this.zLevel,
					(p_148224_3_ + 18) * 0.0078125F,
					(p_148224_4_ + 0) * 0.0078125F);
			var9.addVertexWithUV(p_148224_1_ + 0, p_148224_2_ + 0,
					GuiCreateFlatWorld.this.zLevel,
					(p_148224_3_ + 0) * 0.0078125F,
					(p_148224_4_ + 0) * 0.0078125F);
			var9.draw();
		}

		private void func_148225_a(int p_148225_1_, int p_148225_2_,
				ItemStack p_148225_3_) {
			func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);

			if (p_148225_3_ != null) {
				RenderHelper.enableGUIStandardItemLighting();
				GuiCreateFlatWorld.field_146392_a.renderItemIntoGUI(
						GuiCreateFlatWorld.this.fontRendererObj,
						GuiCreateFlatWorld.this.mc.getTextureManager(),
						p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
				RenderHelper.disableStandardItemLighting();
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}

		private void func_148226_e(int p_148226_1_, int p_148226_2_) {
			func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
		}

		@Override
		protected int getSize() {
			return field_146387_g.getFlatLayers().size();
		}

		@Override
		protected boolean isSelected(int p_148131_1_) {
			return p_148131_1_ == field_148228_k;
		}
	}

	private static RenderItem field_146392_a = new RenderItem();
	private final GuiCreateWorld field_146385_f;
	private GuiButton field_146386_v;
	private FlatGeneratorInfo field_146387_g = FlatGeneratorInfo
			.getDefaultFlatGenerator();
	private GuiButton field_146388_u;
	private GuiButton field_146389_t;
	private GuiCreateFlatWorld.Details field_146390_s;
	private String field_146391_r;
	private String field_146393_h;

	private String field_146394_i;

	public GuiCreateFlatWorld(GuiCreateWorld p_i1029_1_, String p_i1029_2_) {
		field_146385_f = p_i1029_1_;
		func_146383_a(p_i1029_2_);
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		final int var2 = field_146387_g.getFlatLayers().size()
				- field_146390_s.field_148228_k - 1;

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(field_146385_f);
		} else if (p_146284_1_.id == 0) {
			field_146385_f.field_146334_a = func_146384_e();
			mc.displayGuiScreen(field_146385_f);
		} else if (p_146284_1_.id == 5) {
			mc.displayGuiScreen(new GuiFlatPresets(this));
		} else if (p_146284_1_.id == 4 && func_146382_i()) {
			field_146387_g.getFlatLayers().remove(var2);
			field_146390_s.field_148228_k = Math.min(
					field_146390_s.field_148228_k, field_146387_g
							.getFlatLayers().size() - 1);
		}

		field_146387_g.func_82645_d();
		func_146375_g();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		drawDefaultBackground();
		field_146390_s.func_148128_a(p_73863_1_, p_73863_2_, p_73863_3_);
		drawCenteredString(fontRendererObj, field_146393_h, width / 2, 8,
				16777215);
		final int var4 = width / 2 - 92 - 16;
		drawString(fontRendererObj, field_146394_i, var4, 32, 16777215);
		drawString(fontRendererObj, field_146391_r, var4 + 2 + 213
				- fontRendererObj.getStringWidth(field_146391_r), 32, 16777215);
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}

	public void func_146375_g() {
		final boolean var1 = func_146382_i();
		field_146386_v.enabled = var1;
		field_146388_u.enabled = var1;
		field_146388_u.enabled = false;
		field_146389_t.enabled = false;
	}

	private boolean func_146382_i() {
		return field_146390_s.field_148228_k > -1
				&& field_146390_s.field_148228_k < field_146387_g
						.getFlatLayers().size();
	}

	public void func_146383_a(String p_146383_1_) {
		field_146387_g = FlatGeneratorInfo
				.createFlatGeneratorFromString(p_146383_1_);
	}

	public String func_146384_e() {
		return field_146387_g.toString();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();
		field_146393_h = I18n.format("createWorld.customize.flat.title",
				new Object[0]);
		field_146394_i = I18n.format("createWorld.customize.flat.tile",
				new Object[0]);
		field_146391_r = I18n.format("createWorld.customize.flat.height",
				new Object[0]);
		field_146390_s = new GuiCreateFlatWorld.Details();
		buttons.add(field_146389_t = new GuiButton(2, width / 2 - 154,
				height - 52, 100, 20, I18n.format(
						"createWorld.customize.flat.addLayer", new Object[0])
						+ " (NYI)"));
		buttons.add(field_146388_u = new GuiButton(3, width / 2 - 50,
				height - 52, 100, 20, I18n.format(
						"createWorld.customize.flat.editLayer", new Object[0])
						+ " (NYI)"));
		buttons.add(field_146386_v = new GuiButton(4, width / 2 - 155,
				height - 52, 150, 20, I18n
						.format("createWorld.customize.flat.removeLayer",
								new Object[0])));
		buttons.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20,
				I18n.format("gui.done", new Object[0])));
		buttons.add(new GuiButton(5, width / 2 + 5, height - 52, 150, 20, I18n
				.format("createWorld.customize.presets", new Object[0])));
		buttons.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n
				.format("gui.cancel", new Object[0])));
		field_146389_t.field_146125_m = field_146388_u.field_146125_m = false;
		field_146387_g.func_82645_d();
		func_146375_g();
	}
}

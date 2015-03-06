package net.minecraft.client.gui.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiInventory extends InventoryEffectRenderer {
	public static void func_147046_a(int p_147046_0_, int p_147046_1_,
			int p_147046_2_, float p_147046_3_, float p_147046_4_,
			EntityLivingBase p_147046_5_) {
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
		GL11.glTranslatef(p_147046_0_, p_147046_1_, 50.0F);
		GL11.glScalef(-p_147046_2_, p_147046_2_, p_147046_2_);
		GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
		final float var6 = p_147046_5_.renderYawOffset;
		final float var7 = p_147046_5_.rotationYaw;
		final float var8 = p_147046_5_.rotationPitch;
		final float var9 = p_147046_5_.prevRotationYawHead;
		final float var10 = p_147046_5_.rotationYawHead;
		GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-((float) Math.atan(p_147046_4_ / 40.0F)) * 20.0F, 1.0F,
				0.0F, 0.0F);
		p_147046_5_.renderYawOffset = (float) Math.atan(p_147046_3_ / 40.0F) * 20.0F;
		p_147046_5_.rotationYaw = (float) Math.atan(p_147046_3_ / 40.0F) * 40.0F;
		p_147046_5_.rotationPitch = -((float) Math.atan(p_147046_4_ / 40.0F)) * 20.0F;
		p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
		p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
		GL11.glTranslatef(0.0F, p_147046_5_.yOffset, 0.0F);
		RenderManager.instance.playerViewY = 180.0F;
		RenderManager.instance.func_147940_a(p_147046_5_, 0.0D, 0.0D, 0.0D,
				0.0F, 1.0F);
		p_147046_5_.renderYawOffset = var6;
		p_147046_5_.rotationYaw = var7;
		p_147046_5_.rotationPitch = var8;
		p_147046_5_.prevRotationYawHead = var9;
		p_147046_5_.rotationYawHead = var10;
		GL11.glPopMatrix();
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}

	private float field_147047_v;

	private float field_147048_u;

	public GuiInventory(EntityPlayer p_i1094_1_) {
		super(p_i1094_1_.inventoryContainer);
		field_146291_p = true;
	}

	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			mc.displayGuiScreen(new GuiAchievements(this, mc.thePlayer
					.func_146107_m()));
		}

		if (p_146284_1_.id == 1) {
			mc.displayGuiScreen(new GuiStats(this, mc.thePlayer.func_146107_m()));
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		field_147048_u = p_73863_1_;
		field_147047_v = p_73863_2_;
	}

	@Override
	protected void func_146976_a(float p_146976_1_, int p_146976_2_,
			int p_146976_3_) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(field_147001_a);
		final int var4 = field_147003_i;
		final int var5 = field_147009_r;
		drawTexturedModalRect(var4, var5, 0, 0, field_146999_f, field_147000_g);
		func_147046_a(var4 + 51, var5 + 75, 30, var4 + 51 - field_147048_u,
				var5 + 75 - 50 - field_147047_v, mc.thePlayer);
	}

	@Override
	protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
		fontRendererObj.drawString(
				I18n.format("container.crafting", new Object[0]), 86, 16,
				4210752);
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		buttons.clear();

		if (mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
		} else {
			super.initGui();
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void updateScreen() {
		if (mc.playerController.isInCreativeMode()) {
			mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
		}
	}
}

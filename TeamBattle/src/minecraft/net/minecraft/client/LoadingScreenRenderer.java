package net.minecraft.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;

import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdate {
	/**
	 * The text currently displayed (i.e. the argument to the last call to
	 * printText or func_73722_d)
	 */
	private String currentlyDisplayedText = "";

	private final ScaledResolution field_146587_f;

	private final Framebuffer field_146588_g;
	private long field_73723_d = Minecraft.getSystemTime();
	private boolean field_73724_e;
	private String field_73727_a = "";
	/** A reference to the Minecraft object. */
	private final Minecraft mc;

	public LoadingScreenRenderer(Minecraft p_i1017_1_) {
		mc = p_i1017_1_;
		field_146587_f = new ScaledResolution(p_i1017_1_,
				p_i1017_1_.displayWidth, p_i1017_1_.displayHeight);
		field_146588_g = new Framebuffer(p_i1017_1_.displayWidth,
				p_i1017_1_.displayHeight, false);
		field_146588_g.setFramebufferFilter(9728);
	}

	/**
	 * "Saving level", or the loading,or downloading equivelent
	 */
	@Override
	public void displayProgressMessage(String p_73720_1_) {
		field_73724_e = true;
		func_73722_d(p_73720_1_);
	}

	@Override
	public void func_146586_a() {
	}

	public void func_73722_d(String p_73722_1_) {
		currentlyDisplayedText = p_73722_1_;

		if (!mc.running) {
			if (!field_73724_e)
				throw new MinecraftError();
		} else {
			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();

			if (OpenGlHelper.isFramebufferEnabled()) {
				final int var2 = field_146587_f.getScaleFactor();
				GL11.glOrtho(0.0D, field_146587_f.getScaledWidth() * var2,
						field_146587_f.getScaledHeight() * var2, 0.0D, 100.0D,
						300.0D);
			} else {
				final ScaledResolution var3 = new ScaledResolution(mc,
						mc.displayWidth, mc.displayHeight);
				GL11.glOrtho(0.0D, var3.getScaledWidth_double(),
						var3.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
			}

			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(0.0F, 0.0F, -200.0F);
		}
	}

	/**
	 * This is called with "Working..." by resetProgressAndMessage
	 */
	@Override
	public void resetProgresAndWorkingMessage(String p_73719_1_) {
		if (!mc.running) {
			if (!field_73724_e)
				throw new MinecraftError();
		} else {
			field_73723_d = 0L;
			field_73727_a = p_73719_1_;
			setLoadingProgress(-1);
			field_73723_d = 0L;
		}
	}

	/**
	 * this string, followed by "working..." and then the "% complete" are the 3
	 * lines shown. This resets progress to 0, and the WorkingString to
	 * "working...".
	 */
	@Override
	public void resetProgressAndMessage(String p_73721_1_) {
		field_73724_e = false;
		func_73722_d(p_73721_1_);
	}

	/**
	 * Updates the progress bar on the loading screen to the specified amount.
	 * Args: loadProgress
	 */
	@Override
	public void setLoadingProgress(int p_73718_1_) {
		if (!mc.running) {
			if (!field_73724_e)
				throw new MinecraftError();
		} else {
			final long var2 = Minecraft.getSystemTime();

			if (var2 - field_73723_d >= 100L) {
				field_73723_d = var2;
				final ScaledResolution var4 = new ScaledResolution(mc,
						mc.displayWidth, mc.displayHeight);
				final int var5 = var4.getScaleFactor();
				final int var6 = var4.getScaledWidth();
				final int var7 = var4.getScaledHeight();

				if (OpenGlHelper.isFramebufferEnabled()) {
					field_146588_g.framebufferClear();
				} else {
					GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
				}

				field_146588_g.bindFramebuffer(false);
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0.0D, var4.getScaledWidth_double(),
						var4.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				GL11.glTranslatef(0.0F, 0.0F, -200.0F);

				if (!OpenGlHelper.isFramebufferEnabled()) {
					GL11.glClear(GL11.GL_COLOR_BUFFER_BIT
							| GL11.GL_DEPTH_BUFFER_BIT);
				}

				final Tessellator var8 = Tessellator.instance;
				mc.getTextureManager().bindTexture(Gui.optionsBackground);
				final float var9 = 32.0F;
				var8.startDrawingQuads();
				var8.setColorOpaque_I(4210752);
				var8.addVertexWithUV(0.0D, var7, 0.0D, 0.0D, var7 / var9);
				var8.addVertexWithUV(var6, var7, 0.0D, var6 / var9, var7 / var9);
				var8.addVertexWithUV(var6, 0.0D, 0.0D, var6 / var9, 0.0D);
				var8.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
				var8.draw();

				if (p_73718_1_ >= 0) {
					final byte var10 = 100;
					final byte var11 = 2;
					final int var12 = var6 / 2 - var10 / 2;
					final int var13 = var7 / 2 + 16;
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					var8.startDrawingQuads();
					var8.setColorOpaque_I(8421504);
					var8.addVertex(var12, var13, 0.0D);
					var8.addVertex(var12, var13 + var11, 0.0D);
					var8.addVertex(var12 + var10, var13 + var11, 0.0D);
					var8.addVertex(var12 + var10, var13, 0.0D);
					var8.setColorOpaque_I(8454016);
					var8.addVertex(var12, var13, 0.0D);
					var8.addVertex(var12, var13 + var11, 0.0D);
					var8.addVertex(var12 + p_73718_1_, var13 + var11, 0.0D);
					var8.addVertex(var12 + p_73718_1_, var13, 0.0D);
					var8.draw();
					GL11.glEnable(GL11.GL_TEXTURE_2D);
				}

				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				mc.fontRenderer.drawStringWithShadow(currentlyDisplayedText,
						(var6 - mc.fontRenderer
								.getStringWidth(currentlyDisplayedText)) / 2,
						var7 / 2 - 4 - 16, 16777215);
				mc.fontRenderer
						.drawStringWithShadow(field_73727_a,
								(var6 - mc.fontRenderer
										.getStringWidth(field_73727_a)) / 2,
								var7 / 2 - 4 + 8, 16777215);
				field_146588_g.unbindFramebuffer();

				if (OpenGlHelper.isFramebufferEnabled()) {
					field_146588_g.framebufferRender(var6 * var5, var7 * var5);
				}

				mc.func_147120_f();

				try {
					Thread.yield();
				} catch (final Exception var14) {
					;
				}
			}
		}
	}
}

package net.minecraft.client.shader;

import java.util.Iterator;
import java.util.List;

import javax.vecmath.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class Shader {
	public final Framebuffer framebufferIn;
	public final Framebuffer framebufferOut;
	private final List listAuxFramebuffers = Lists.newArrayList();
	private final List listAuxHeights = Lists.newArrayList();
	private final List listAuxNames = Lists.newArrayList();
	private final List listAuxWidths = Lists.newArrayList();
	private final ShaderManager manager;
	private Matrix4f projectionMatrix;

	public Shader(IResourceManager p_i45089_1_, String p_i45089_2_,
			Framebuffer p_i45089_3_, Framebuffer p_i45089_4_)
			throws JsonException {
		manager = new ShaderManager(p_i45089_1_, p_i45089_2_);
		framebufferIn = p_i45089_3_;
		framebufferOut = p_i45089_4_;
	}

	public void addAuxFramebuffer(String p_148041_1_, Object p_148041_2_,
			int p_148041_3_, int p_148041_4_) {
		listAuxNames.add(listAuxNames.size(), p_148041_1_);
		listAuxFramebuffers.add(listAuxFramebuffers.size(), p_148041_2_);
		listAuxWidths.add(listAuxWidths.size(), Integer.valueOf(p_148041_3_));
		listAuxHeights.add(listAuxHeights.size(), Integer.valueOf(p_148041_4_));
	}

	public void deleteShader() {
		manager.func_147988_a();
	}

	public ShaderManager getShaderManager() {
		return manager;
	}

	public void loadShader(float p_148042_1_) {
		preLoadShader();
		framebufferIn.unbindFramebuffer();
		final float var2 = framebufferOut.framebufferTextureWidth;
		final float var3 = framebufferOut.framebufferTextureHeight;
		GL11.glViewport(0, 0, (int) var2, (int) var3);
		manager.func_147992_a("DiffuseSampler", framebufferIn);

		for (int var4 = 0; var4 < listAuxFramebuffers.size(); ++var4) {
			manager.func_147992_a((String) listAuxNames.get(var4),
					listAuxFramebuffers.get(var4));
			manager.func_147984_b("AuxSize" + var4).func_148087_a(
					((Integer) listAuxWidths.get(var4)).intValue(),
					((Integer) listAuxHeights.get(var4)).intValue());
		}

		manager.func_147984_b("ProjMat").func_148088_a(projectionMatrix);
		manager.func_147984_b("InSize").func_148087_a(
				framebufferIn.framebufferTextureWidth,
				framebufferIn.framebufferTextureHeight);
		manager.func_147984_b("OutSize").func_148087_a(var2, var3);
		manager.func_147984_b("Time").func_148090_a(p_148042_1_);
		final Minecraft var8 = Minecraft.getMinecraft();
		manager.func_147984_b("ScreenSize").func_148087_a(var8.displayWidth,
				var8.displayHeight);
		manager.func_147995_c();
		framebufferOut.framebufferClear();
		framebufferOut.bindFramebuffer(false);
		GL11.glDepthMask(false);
		GL11.glColorMask(true, true, true, false);
		final Tessellator var5 = Tessellator.instance;
		var5.startDrawingQuads();
		var5.setColorOpaque_I(-1);
		var5.addVertex(0.0D, var3, 500.0D);
		var5.addVertex(var2, var3, 500.0D);
		var5.addVertex(var2, 0.0D, 500.0D);
		var5.addVertex(0.0D, 0.0D, 500.0D);
		var5.draw();
		GL11.glDepthMask(true);
		GL11.glColorMask(true, true, true, true);
		manager.func_147993_b();
		framebufferOut.unbindFramebuffer();
		framebufferIn.unbindFramebufferTexture();
		final Iterator var6 = listAuxFramebuffers.iterator();

		while (var6.hasNext()) {
			final Object var7 = var6.next();

			if (var7 instanceof Framebuffer) {
				((Framebuffer) var7).unbindFramebufferTexture();
			}
		}
	}

	private void preLoadShader() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_COLOR_MATERIAL);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void setProjectionMatrix(Matrix4f p_148045_1_) {
		projectionMatrix = p_148045_1_;
	}
}

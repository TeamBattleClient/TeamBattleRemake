package net.minecraft.client.shader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ShaderGroup {
	private float field_148036_j;
	private float field_148037_k;
	private final List listFramebuffers = Lists.newArrayList();
	private final List listShaders = Lists.newArrayList();
	private final Framebuffer mainFramebuffer;
	private int mainFramebufferHeight;
	private int mainFramebufferWidth;
	private final Map mapFramebuffers = Maps.newHashMap();
	private Matrix4f projectionMatrix;
	private final IResourceManager resourceManager;
	private final String shaderGroupName;

	public ShaderGroup(TextureManager p_i1050_1_, IResourceManager p_i1050_2_,
			Framebuffer p_i1050_3_, ResourceLocation p_i1050_4_)
			throws JsonException {
		resourceManager = p_i1050_2_;
		mainFramebuffer = p_i1050_3_;
		field_148036_j = 0.0F;
		field_148037_k = 0.0F;
		mainFramebufferWidth = p_i1050_3_.framebufferWidth;
		mainFramebufferHeight = p_i1050_3_.framebufferHeight;
		shaderGroupName = p_i1050_4_.toString();
		resetProjectionMatrix();
		func_152765_a(p_i1050_1_, p_i1050_4_);
	}

	public void addFramebuffer(String p_148020_1_, int p_148020_2_,
			int p_148020_3_) {
		final Framebuffer var4 = new Framebuffer(p_148020_2_, p_148020_3_, true);
		var4.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
		mapFramebuffers.put(p_148020_1_, var4);

		if (p_148020_2_ == mainFramebufferWidth
				&& p_148020_3_ == mainFramebufferHeight) {
			listFramebuffers.add(var4);
		}
	}

	public Shader addShader(String p_148023_1_, Framebuffer p_148023_2_,
			Framebuffer p_148023_3_) throws JsonException {
		final Shader var4 = new Shader(resourceManager, p_148023_1_,
				p_148023_2_, p_148023_3_);
		listShaders.add(listShaders.size(), var4);
		return var4;
	}

	public void createBindFramebuffers(int p_148026_1_, int p_148026_2_) {
		mainFramebufferWidth = mainFramebuffer.framebufferTextureWidth;
		mainFramebufferHeight = mainFramebuffer.framebufferTextureHeight;
		resetProjectionMatrix();
		Iterator var3 = listShaders.iterator();

		while (var3.hasNext()) {
			final Shader var4 = (Shader) var3.next();
			var4.setProjectionMatrix(projectionMatrix);
		}

		var3 = listFramebuffers.iterator();

		while (var3.hasNext()) {
			final Framebuffer var5 = (Framebuffer) var3.next();
			var5.createBindFramebuffer(p_148026_1_, p_148026_2_);
		}
	}

	public void deleteShaderGroup() {
		Iterator var1 = mapFramebuffers.values().iterator();

		while (var1.hasNext()) {
			final Framebuffer var2 = (Framebuffer) var1.next();
			var2.deleteFramebuffer();
		}

		var1 = listShaders.iterator();

		while (var1.hasNext()) {
			final Shader var3 = (Shader) var1.next();
			var3.deleteShader();
		}

		listShaders.clear();
	}

	private void func_152764_a(TextureManager p_152764_1_,
			JsonElement p_152764_2_) throws JsonException {
		final JsonObject var3 = JsonUtils.getJsonElementAsJsonObject(
				p_152764_2_, "pass");
		final String var4 = JsonUtils.getJsonObjectStringFieldValue(var3,
				"name");
		final String var5 = JsonUtils.getJsonObjectStringFieldValue(var3,
				"intarget");
		final String var6 = JsonUtils.getJsonObjectStringFieldValue(var3,
				"outtarget");
		final Framebuffer var7 = getFramebuffer(var5);
		final Framebuffer var8 = getFramebuffer(var6);

		if (var7 == null)
			throw new JsonException("Input target \'" + var5
					+ "\' does not exist");
		else if (var8 == null)
			throw new JsonException("Output target \'" + var6
					+ "\' does not exist");
		else {
			final Shader var9 = addShader(var4, var7, var8);
			final JsonArray var10 = JsonUtils
					.getJsonObjectJsonArrayFieldOrDefault(var3, "auxtargets",
							(JsonArray) null);

			if (var10 != null) {
				int var11 = 0;

				for (final Iterator var12 = var10.iterator(); var12.hasNext(); ++var11) {
					final JsonElement var13 = (JsonElement) var12.next();

					try {
						final JsonObject var14 = JsonUtils
								.getJsonElementAsJsonObject(var13, "auxtarget");
						final String var30 = JsonUtils
								.getJsonObjectStringFieldValue(var14, "name");
						final String var16 = JsonUtils
								.getJsonObjectStringFieldValue(var14, "id");
						final Framebuffer var17 = getFramebuffer(var16);

						if (var17 == null) {
							final ResourceLocation var18 = new ResourceLocation(
									"textures/effect/" + var16 + ".png");

							try {
								resourceManager.getResource(var18);
							} catch (final FileNotFoundException var24) {
								throw new JsonException(
										"Render target or texture \'" + var16
												+ "\' does not exist");
							}

							p_152764_1_.bindTexture(var18);
							final ITextureObject var19 = p_152764_1_
									.getTexture(var18);
							final int var20 = JsonUtils
									.getJsonObjectIntegerFieldValue(var14,
											"width");
							final int var21 = JsonUtils
									.getJsonObjectIntegerFieldValue(var14,
											"height");
							final boolean var22 = JsonUtils
									.getJsonObjectBooleanFieldValue(var14,
											"bilinear");

							if (var22) {
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
										GL11.GL_TEXTURE_MIN_FILTER,
										GL11.GL_LINEAR);
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
										GL11.GL_TEXTURE_MAG_FILTER,
										GL11.GL_LINEAR);
							} else {
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
										GL11.GL_TEXTURE_MIN_FILTER,
										GL11.GL_NEAREST);
								GL11.glTexParameteri(GL11.GL_TEXTURE_2D,
										GL11.GL_TEXTURE_MAG_FILTER,
										GL11.GL_NEAREST);
							}

							var9.addAuxFramebuffer(var30,
									Integer.valueOf(var19.getGlTextureId()),
									var20, var21);
						} else {
							var9.addAuxFramebuffer(var30, var17,
									var17.framebufferTextureWidth,
									var17.framebufferTextureHeight);
						}
					} catch (final Exception var25) {
						final JsonException var15 = JsonException
								.func_151379_a(var25);
						var15.func_151380_a("auxtargets[" + var11 + "]");
						throw var15;
					}
				}
			}

			final JsonArray var26 = JsonUtils
					.getJsonObjectJsonArrayFieldOrDefault(var3, "uniforms",
							(JsonArray) null);

			if (var26 != null) {
				int var27 = 0;

				for (final Iterator var28 = var26.iterator(); var28.hasNext(); ++var27) {
					final JsonElement var29 = (JsonElement) var28.next();

					try {
						initUniform(var29);
					} catch (final Exception var23) {
						final JsonException var31 = JsonException
								.func_151379_a(var23);
						var31.func_151380_a("uniforms[" + var27 + "]");
						throw var31;
					}
				}
			}
		}
	}

	public void func_152765_a(TextureManager p_152765_1_,
			ResourceLocation p_152765_2_) throws JsonException {
		final JsonParser var3 = new JsonParser();
		InputStream var4 = null;

		try {
			final IResource var5 = resourceManager.getResource(p_152765_2_);
			var4 = var5.getInputStream();
			final JsonObject var22 = var3.parse(
					IOUtils.toString(var4, Charsets.UTF_8)).getAsJsonObject();
			JsonArray var7;
			int var8;
			Iterator var9;
			JsonElement var10;
			JsonException var12;

			if (JsonUtils.jsonObjectFieldTypeIsArray(var22, "targets")) {
				var7 = var22.getAsJsonArray("targets");
				var8 = 0;

				for (var9 = var7.iterator(); var9.hasNext(); ++var8) {
					var10 = (JsonElement) var9.next();

					try {
						initTarget(var10);
					} catch (final Exception var19) {
						var12 = JsonException.func_151379_a(var19);
						var12.func_151380_a("targets[" + var8 + "]");
						throw var12;
					}
				}
			}

			if (JsonUtils.jsonObjectFieldTypeIsArray(var22, "passes")) {
				var7 = var22.getAsJsonArray("passes");
				var8 = 0;

				for (var9 = var7.iterator(); var9.hasNext(); ++var8) {
					var10 = (JsonElement) var9.next();

					try {
						func_152764_a(p_152765_1_, var10);
					} catch (final Exception var18) {
						var12 = JsonException.func_151379_a(var18);
						var12.func_151380_a("passes[" + var8 + "]");
						throw var12;
					}
				}
			}
		} catch (final Exception var20) {
			final JsonException var6 = JsonException.func_151379_a(var20);
			var6.func_151381_b(p_152765_2_.getResourcePath());
			throw var6;
		} finally {
			IOUtils.closeQuietly(var4);
		}
	}

	private Framebuffer getFramebuffer(String p_148017_1_) {
		return p_148017_1_ == null ? null : p_148017_1_
				.equals("minecraft:main") ? mainFramebuffer
				: (Framebuffer) mapFramebuffers.get(p_148017_1_);
	}

	public final String getShaderGroupName() {
		return shaderGroupName;
	}

	private void initTarget(JsonElement p_148027_1_) throws JsonException {
		if (JsonUtils.jsonElementTypeIsString(p_148027_1_)) {
			addFramebuffer(p_148027_1_.getAsString(), mainFramebufferWidth,
					mainFramebufferHeight);
		} else {
			final JsonObject var2 = JsonUtils.getJsonElementAsJsonObject(
					p_148027_1_, "target");
			final String var3 = JsonUtils.getJsonObjectStringFieldValue(var2,
					"name");
			final int var4 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(
					var2, "width", mainFramebufferWidth);
			final int var5 = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(
					var2, "height", mainFramebufferHeight);

			if (mapFramebuffers.containsKey(var3))
				throw new JsonException(var3 + " is already defined");

			addFramebuffer(var3, var4, var5);
		}
	}

	private void initUniform(JsonElement p_148028_1_) throws JsonException {
		final JsonObject var2 = JsonUtils.getJsonElementAsJsonObject(
				p_148028_1_, "uniform");
		final String var3 = JsonUtils.getJsonObjectStringFieldValue(var2,
				"name");
		final ShaderUniform var4 = ((Shader) listShaders
				.get(listShaders.size() - 1)).getShaderManager().func_147991_a(
				var3);

		if (var4 == null)
			throw new JsonException("Uniform \'" + var3 + "\' does not exist");
		else {
			final float[] var5 = new float[4];
			int var6 = 0;
			final JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(var2,
					"values");

			for (final Iterator var8 = var7.iterator(); var8.hasNext(); ++var6) {
				final JsonElement var9 = (JsonElement) var8.next();

				try {
					var5[var6] = JsonUtils.getJsonElementFloatValue(var9,
							"value");
				} catch (final Exception var12) {
					final JsonException var11 = JsonException
							.func_151379_a(var12);
					var11.func_151380_a("values[" + var6 + "]");
					throw var11;
				}
			}

			switch (var6) {
			case 0:
			default:
				break;

			case 1:
				var4.func_148090_a(var5[0]);
				break;

			case 2:
				var4.func_148087_a(var5[0], var5[1]);
				break;

			case 3:
				var4.func_148095_a(var5[0], var5[1], var5[2]);
				break;

			case 4:
				var4.func_148081_a(var5[0], var5[1], var5[2], var5[3]);
			}
		}
	}

	public void loadShaderGroup(float p_148018_1_) {
		if (p_148018_1_ < field_148037_k) {
			field_148036_j += 1.0F - field_148037_k;
			field_148036_j += p_148018_1_;
		} else {
			field_148036_j += p_148018_1_ - field_148037_k;
		}

		for (field_148037_k = p_148018_1_; field_148036_j > 20.0F; field_148036_j -= 20.0F) {
			;
		}

		final Iterator var2 = listShaders.iterator();

		while (var2.hasNext()) {
			final Shader var3 = (Shader) var2.next();
			var3.loadShader(field_148036_j / 20.0F);
		}
	}

	private void resetProjectionMatrix() {
		projectionMatrix = new Matrix4f();
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2.0F / mainFramebuffer.framebufferTextureWidth;
		projectionMatrix.m11 = 2.0F / -mainFramebuffer.framebufferTextureHeight;
		projectionMatrix.m22 = -0.0020001999F;
		projectionMatrix.m33 = 1.0F;
		projectionMatrix.m03 = -1.0F;
		projectionMatrix.m13 = 1.0F;
		projectionMatrix.m23 = -1.0001999F;
	}
}

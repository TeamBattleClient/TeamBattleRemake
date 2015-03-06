package net.minecraft.client.renderer.texture;

public abstract class AbstractTexture implements ITextureObject {
	protected int glTextureId = -1;

	public void func_147631_c() {
		if (glTextureId != -1) {
			TextureUtil.deleteTexture(glTextureId);
			glTextureId = -1;
		}
	}

	@Override
	public int getGlTextureId() {
		if (glTextureId == -1) {
			glTextureId = TextureUtil.glGenTextures();
		}

		return glTextureId;
	}
}

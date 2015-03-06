package net.minecraft.client.resources.data;

import java.util.Collections;
import java.util.List;

public class TextureMetadataSection implements IMetadataSection {
	private final List field_148536_c;
	private final boolean textureBlur;
	private final boolean textureClamp;

	public TextureMetadataSection(boolean p_i45102_1_, boolean p_i45102_2_,
			List p_i45102_3_) {
		textureBlur = p_i45102_1_;
		textureClamp = p_i45102_2_;
		field_148536_c = p_i45102_3_;
	}

	public List func_148535_c() {
		return Collections.unmodifiableList(field_148536_c);
	}

	public boolean getTextureBlur() {
		return textureBlur;
	}

	public boolean getTextureClamp() {
		return textureClamp;
	}
}

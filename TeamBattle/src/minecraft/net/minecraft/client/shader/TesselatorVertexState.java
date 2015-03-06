package net.minecraft.client.shader;

public class TesselatorVertexState {
	private final boolean hasBrightness;
	private final boolean hasColor;
	private final boolean hasNormals;
	private final boolean hasTexture;
	private int[] rawBuffer;
	private int rawBufferIndex;
	private int vertexCount;

	public TesselatorVertexState(int[] p_i45079_1_, int p_i45079_2_,
			int p_i45079_3_, boolean p_i45079_4_, boolean p_i45079_5_,
			boolean p_i45079_6_, boolean p_i45079_7_) {
		rawBuffer = p_i45079_1_;
		rawBufferIndex = p_i45079_2_;
		vertexCount = p_i45079_3_;
		hasTexture = p_i45079_4_;
		hasBrightness = p_i45079_5_;
		hasNormals = p_i45079_6_;
		hasColor = p_i45079_7_;
	}

	public void addTessellatorVertexState(TesselatorVertexState tsv) {
		if (tsv != null) {
			if (tsv.hasBrightness == hasBrightness && tsv.hasColor == hasColor
					&& tsv.hasNormals == hasNormals
					&& tsv.hasTexture == hasTexture) {
				final int newRawBufferIndex = rawBufferIndex
						+ tsv.rawBufferIndex;
				final int[] newRawBuffer = new int[newRawBufferIndex];
				System.arraycopy(rawBuffer, 0, newRawBuffer, 0, rawBufferIndex);
				System.arraycopy(tsv.rawBuffer, 0, newRawBuffer,
						rawBufferIndex, tsv.rawBufferIndex);
				rawBuffer = newRawBuffer;
				rawBufferIndex = newRawBufferIndex;
				vertexCount += tsv.vertexCount;
			} else
				throw new IllegalArgumentException("Incompatible vertex states");
		}
	}

	public boolean getHasBrightness() {
		return hasBrightness;
	}

	public boolean getHasColor() {
		return hasColor;
	}

	public boolean getHasNormals() {
		return hasNormals;
	}

	public boolean getHasTexture() {
		return hasTexture;
	}

	public int[] getRawBuffer() {
		return rawBuffer;
	}

	public int getRawBufferIndex() {
		return rawBufferIndex;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}

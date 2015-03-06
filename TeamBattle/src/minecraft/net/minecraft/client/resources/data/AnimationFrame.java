package net.minecraft.client.resources.data;

public class AnimationFrame {
	private final int frameIndex;
	private final int frameTime;

	public AnimationFrame(int p_i1307_1_) {
		this(p_i1307_1_, -1);
	}

	public AnimationFrame(int p_i1308_1_, int p_i1308_2_) {
		frameIndex = p_i1308_1_;
		frameTime = p_i1308_2_;
	}

	public int getFrameIndex() {
		return frameIndex;
	}

	public int getFrameTime() {
		return frameTime;
	}

	public boolean hasNoTime() {
		return frameTime == -1;
	}
}

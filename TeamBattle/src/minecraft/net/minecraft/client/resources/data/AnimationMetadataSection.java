package net.minecraft.client.resources.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class AnimationMetadataSection implements IMetadataSection {
	private final List animationFrames;
	private final int frameHeight;
	private final int frameTime;
	private final int frameWidth;

	public AnimationMetadataSection(List p_i1309_1_, int p_i1309_2_,
			int p_i1309_3_, int p_i1309_4_) {
		animationFrames = p_i1309_1_;
		frameWidth = p_i1309_2_;
		frameHeight = p_i1309_3_;
		frameTime = p_i1309_4_;
	}

	public boolean frameHasTime(int p_110470_1_) {
		return !((AnimationFrame) animationFrames.get(p_110470_1_)).hasNoTime();
	}

	private AnimationFrame getAnimationFrame(int p_130072_1_) {
		return (AnimationFrame) animationFrames.get(p_130072_1_);
	}

	public int getFrameCount() {
		return animationFrames.size();
	}

	public int getFrameHeight() {
		return frameHeight;
	}

	public int getFrameIndex(int p_110468_1_) {
		return ((AnimationFrame) animationFrames.get(p_110468_1_))
				.getFrameIndex();
	}

	public Set getFrameIndexSet() {
		final HashSet var1 = Sets.newHashSet();
		final Iterator var2 = animationFrames.iterator();

		while (var2.hasNext()) {
			final AnimationFrame var3 = (AnimationFrame) var2.next();
			var1.add(Integer.valueOf(var3.getFrameIndex()));
		}

		return var1;
	}

	public int getFrameTime() {
		return frameTime;
	}

	public int getFrameTimeSingle(int p_110472_1_) {
		final AnimationFrame var2 = getAnimationFrame(p_110472_1_);
		return var2.hasNoTime() ? frameTime : var2.getFrameTime();
	}

	public int getFrameWidth() {
		return frameWidth;
	}
}
